package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.Conexao;
import br.com.openpdv.controlador.core.CoreService;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import br.com.openpdv.modelo.ecf.EcfPagamentoParcela;
import java.io.File;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Classe que realiza a acao de imprimir e confirmar as operacoes de Cartao.
 *
 * @author Pedro H. Lira
 */
public class ComandoSalvarPagamento implements IComando {

    private Logger log;
    private CoreService service;
    private List<EcfPagamento> pagamentos;

    /**
     * Construtor padrao.
     *
     * @param pagamentos a lista de pagamentos realizados.
     */
    public ComandoSalvarPagamento(List<EcfPagamento> pagamentos) {
        this.log = Logger.getLogger(ComandoSalvarPagamento.class);
        this.service = new CoreService();
        this.pagamentos = pagamentos;
    }

    @Override
    public void executar() throws OpenPdvException {
        List<File> impressos = new ArrayList<>();

        // soma os valores de todos os cartoes, para colocar no CCD e o total da venda
        double valCard = 0.00;
        double total = 0.00;
        for (EcfPagamento pag : pagamentos) {
            if (pag.getEcfPagamentoTipo().isEcfPagamentoTipoTef()) {
                valCard += pag.getEcfPagamentoValor();
            }
            total += pag.getEcfPagamentoValor();
        }

        // percorre os pagamentos para inserir no banco e imprimir cartoes
        ComandoImprimirCartao cmdCartao = new ComandoImprimirCartao();
        for (EcfPagamento pag : pagamentos) {
            if (pag.getEcfPagamentoTipo().isEcfPagamentoTipoTef() && Boolean.valueOf(Util.getConfig().get("pag.cartao"))) {
                cmdCartao.abrir(pag, valCard, total);
                cmdCartao.executar();
                impressos.add(new File(pag.getArquivo()));
            } else if (pag.getEcfPagamentoParcelas() == null) {
                List<EcfPagamentoParcela> parcelas = new ArrayList<>();
                EcfPagamentoParcela parcela = new EcfPagamentoParcela();
                parcela.setEcfPagamentoParcelaData(pag.getEcfPagamentoData());
                parcela.setEcfPagamentoParcelaValor(pag.getEcfPagamentoValor());
                parcela.setEcfPagamentoParcelaNsu(pag.getEcfPagamentoNsu());
                parcelas.add(parcela);
                pag.setEcfPagamentoParcelas(parcelas);
            }
            // salva o pagamento
            salvarPagamento(pag);
        }
        cmdCartao.fechar();

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
