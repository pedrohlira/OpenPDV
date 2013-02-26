package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.Conexao;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import br.com.openpdv.modelo.ecf.EcfPagamentoParcela;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import br.com.phdss.TEF;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Classe que realiza a acao de imprimir e confirmar as operacoes de Cartao.
 *
 * @author Pedro H. Lira
 */
public class ComandoImprimirCartao implements IComando {

    private Logger log;
    private CoreService service;
    private List<EcfPagamento> pagamentos;
    private double troco;

    /**
     * Construtor padrao.
     *
     * @param pagamentos a lista de pagamentos realizados.
     * @param troco o valor do troco da venda.
     */
    public ComandoImprimirCartao(List<EcfPagamento> pagamentos, double troco) {
        this.log = Logger.getLogger(ComandoImprimirCartao.class);
        this.service = new CoreService();
        this.pagamentos = pagamentos;
        this.troco = troco;
    }

    @Override
    public void executar() throws OpenPdvException {
        EComandoECF comando = null;
        List<File> impressos = new ArrayList<>();

        // percorre os pagamentos para inserir no banco e imprimir cartoes
        for (int i = 0; i < pagamentos.size(); i++) {
            EcfPagamento pag = pagamentos.get(i);
            List<EcfPagamentoParcela> parcelas;

            if (pag.getEcfPagamentoTipo().isEcfPagamentoTipoTef()) {
                // abre o relatorio vinculado
                if (comando == null) {
                    // soma os valores de todos os cartoes, para colocar no CCD
                    double valCard = pag.getEcfPagamentoValor();
                    for (int j = i + 1; j < pagamentos.size(); j++) {
                        if (pagamentos.get(j).getEcfPagamentoTipo().isEcfPagamentoTipoTef()) {
                            valCard += pagamentos.get(j).getEcfPagamentoValor();
                        }
                    }

                    String coo = pag.getEcfVenda().getEcfVendaCoo() + "";
                    String codigo = pag.getEcfPagamentoTipo().getEcfPagamentoTipoCodigo();
                    String valor = Util.formataNumero(valCard, 1, 2, false).replace(",", ".");
                    ECF.enviar(EComandoECF.ECF_FechaRelatorio);
                    ECF.enviar(EComandoECF.ECF_AbreCupomVinculado, coo, codigo, "", valor);
                    comando = EComandoECF.ECF_LinhaCupomVinculado;
                }

                // imprime as vias
                try {
                    String arq = null;
                    if (pag.getArquivo().contains("pendente")) {
                        arq = TEF.lerArquivo(TEF.getRespIntPos001(), 0);
                    }
                    if (arq == null) {
                        arq = TEF.lerArquivo(pag.getArquivo(), 0);
                    }
                    Map<String, String> dados = TEF.iniToMap(arq);
                    TEF.imprimirVias(dados, comando);

                    // se for o pendente, precisa confirmar no GP e sera o ultimo cartao
                    if (pag.getArquivo().contains("pendente")) {
                        String id = pag.getArquivo().replaceAll("\\D", "");
                        ECF.enviar(EComandoECF.ECF_FechaRelatorio);
                        TEF.confirmarTransacao(id, true);

                        // pega o numero GNF da impressao do cartao
                        String[] resp = ECF.enviar(EComandoECF.ECF_NumGNF);
                        pag.setEcfPagamentoGnf(Integer.valueOf(resp[1]));

                        // salva o documento para relatorio e deleta o arquivo
                        new ComandoSalvarDocumento(comando == EComandoECF.ECF_LinhaCupomVinculado ? "CC" : "RG").executar();
                    }

                    // gerar as parcelas e deleta o arquivo
                    parcelas = gerarParcela(dados, pag);
                    impressos.add(new File(pag.getArquivo()));
                } catch (Exception ex) {
                    ECF.enviar(EComandoECF.ECF_FechaRelatorio);
                    throw new OpenPdvException(ex);
                }
            } else {
                parcelas = new ArrayList<>();
                EcfPagamentoParcela parcela = new EcfPagamentoParcela();
                parcela.setEcfPagamentoParcelaData(pag.getEcfPagamentoData());
                parcela.setEcfPagamentoParcelaValor(pag.getEcfPagamentoValor());
                parcela.setEcfPagamentoParcelaNsu("");
                parcelas.add(parcela);
            }

            // salva o pagamento
            pag.setEcfPagamentoParcelas(parcelas);
            salvarPagamento(pag);
        }

        // caso todos os cartoes tenham todas as vias impressas, deleta os arquivos pendentes dos mesmos
        for (File file : impressos) {
            file.delete();
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // metodo nao aplicavel.
    }

    /**
     * Metodo que adiciona a lista de parcelas as parcelas correspondente do
     * pagamento informado.
     *
     * @param dados o mapa de dados lidos do arquivo.
     * @param pagamento o objeto de pagamento a ser considerado.
     * @return uma lista de parcelas efetuadas pelo cartao.
     */
    private List<EcfPagamentoParcela> gerarParcela(Map<String, String> dados, EcfPagamento pagamento) {
        // recupera as parcelas
        List<EcfPagamentoParcela> parcelas = new ArrayList<>();

        if (dados.get("018-000") == null) {
            EcfPagamentoParcela parcela = new EcfPagamentoParcela();
            parcela.setEcfPagamentoParcelaData(pagamento.getEcfPagamentoData());
            parcela.setEcfPagamentoParcelaValor(pagamento.getEcfPagamentoValor());
            parcela.setEcfPagamentoParcelaNsu(pagamento.getEcfPagamentoNsu());
            parcelas.add(parcela);
        } else {
            int faturas = Integer.valueOf(dados.get("018-000"));
            for (int fat = 1; fat <= faturas; fat++) {
                Date dt;
                Double vl;
                String nsu;
                String chave = "019-" + Util.formataNumero(fat, 3, 0, false);

                if (dados.get(chave) != null) {
                    // data
                    try {
                        dt = new SimpleDateFormat("ddMMyyyy").parse(dados.get(chave));
                    } catch (ParseException ex) {
                        dt = pagamento.getEcfPagamentoData();
                    }
                    // valor
                    vl = Double.valueOf(dados.get(chave.replace("019", "020"))) / 100;
                    // nsu
                    nsu = dados.get(chave.replace("019", "021"));
                } else {
                    // data
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(pagamento.getEcfPagamentoData());
                    cal.add(Calendar.MONTH, fat);
                    dt = cal.getTime();
                    // valor
                    vl = pagamento.getEcfPagamentoValor() / faturas;
                    // nsu
                    nsu = pagamento.getEcfPagamentoNsu();
                }
                // adicionando
                EcfPagamentoParcela parcela = new EcfPagamentoParcela();
                parcela.setEcfPagamentoParcelaData(dt);
                parcela.setEcfPagamentoParcelaValor(vl);
                parcela.setEcfPagamentoParcelaNsu(nsu);
                parcelas.add(parcela);
            }
        }
        return parcelas;
    }

    /**
     * Metodo que a operacao de salvar os dados no banco de dados para posterior
     * uso, ate mesmo de cancelamento.
     *
     * @param pagamento objeto de pagamento a ser salvo.
     * @throws OpenPdvException dispara caso nao consiga salvar.
     */
    private void salvarPagamento(EcfPagamento pagamento) throws OpenPdvException {
        // salva pagamento no BD
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();
            em.getTransaction().begin();

            pagamento = (EcfPagamento) service.salvar(em, pagamento);
            for (EcfPagamentoParcela parcela : pagamento.getEcfPagamentoParcelas()) {
                parcela.setEcfPagamento(pagamento);
            }
            service.salvar(em, pagamento.getEcfPagamentoParcelas());

            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao salvar", ex);
            throw new OpenPdvException(ex);
        } finally {
            if (em != null) {
                em.close();
                emf.close();
            }
        }
    }
}
