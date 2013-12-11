package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import br.com.openpdv.modelo.ecf.EcfPagamentoParcela;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import br.com.phdss.TEF;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Classe que realiza a acao de imprimir e confirmar as operacoes de Cartao.
 *
 * @author Pedro H. Lira
 */
public class ComandoImprimirCartao implements IComando {

    private EcfPagamento pag;
    private EComandoECF comando = null;

    /**
     * Construtor padrao.
     */
    public ComandoImprimirCartao() {
    }

    /**
     * Metodo que abre as operacoes com cartao
     *
     * @param pag o pagamento em cartao.
     * @param total o total geral de todos os cartoes.
     */
    public void abrir(EcfPagamento pag, double total) {
        this.pag = pag;

        if (comando == null) {
            try {
                // abre o relatorio vinculado
                String coo = pag.getEcfVenda().getEcfVendaCoo() + "";
                String codigo = pag.getEcfPagamentoTipo().getEcfPagamentoTipoCodigo();
                String valor = Util.formataNumero(total, 1, 2, false).replace(",", ".");
                ECF.enviar(EComandoECF.ECF_AbreCupomVinculado, coo, codigo, "", valor);
                comando = EComandoECF.ECF_LinhaCupomVinculado;
            } catch (Exception ex) {
                // abre o relatorio gerencial
                ECF.enviar(EComandoECF.ECF_AbreRelatorioGerencial);
                comando = EComandoECF.ECF_LinhaRelatorioGerencial;
            }
        }
    }

    @Override
    public void executar() throws OpenPdvException {
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

                // salva o documento para relatorio
                new ComandoSalvarDocumento(comando == EComandoECF.ECF_LinhaCupomVinculado ? "CC" : "RG").executar();
            }

            // gerar as parcelas
            List<EcfPagamentoParcela> parcelas = gerarParcela(dados, pag);
            pag.setEcfPagamentoParcelas(parcelas);
        } catch (Exception ex) {
            fechar();
            throw new OpenPdvException(ex);
        }
    }

    /**
     * Metodo que fecha as operacoes com cartao
     */
    public void fechar() {
        if (comando != null) {
            comando = null;
            ECF.enviar(EComandoECF.ECF_FechaRelatorio);
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // metodo nao aplicavel.
    }

    /**
     * Metodo que adiciona a lista de parcelas as parcelas correspondente do pagamento informado.
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
}
