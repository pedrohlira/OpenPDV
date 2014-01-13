package br.com.openpdv.controlador.comandos;

import br.com.phdss.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import br.com.phdss.ECF;
import br.com.phdss.EComando;
import br.com.phdss.IECF;
import br.com.phdss.TEF;

/**
 * Classe que realiza a acao de cancelar as operacoes de Cartao.
 *
 * @author Pedro H. Lira
 */
public class ComandoCancelarCartao implements IComando {

    private EcfPagamento pag;
    private IECF ecf;

    /**
     * Construtor padrao.
     *
     * @param pag o pagamento de cartao a ser cancelado.
     */
    public ComandoCancelarCartao(EcfPagamento pag) {
        this.pag = pag;
        this.ecf = ECF.getInstancia();
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            // cancela a trasacao
            String id = TEF.gerarId();
            TEF.cancelarTransacao(id, pag.getEcfPagamentoValor(), pag.getEcfPagamentoTipo().getEcfPagamentoTipoRede(), pag.getEcfPagamentoNsu(), pag.getEcfPagamentoData());

            try {
                TEF.bloquear(true);
                ecf.enviar(EComando.ECF_FechaRelatorio);
                ecf.enviar(EComando.ECF_AbreRelatorioGerencial, Util.getConfig().get("ecf.reltef"));
                TEF.imprimirVias(TEF.getDados(), EComando.ECF_LinhaRelatorioGerencial);
                ecf.enviar(EComando.ECF_FechaRelatorio);
                pag.setEcfPagamentoEstornoNsu(TEF.getDados().get("012-000"));
                TEF.confirmarTransacao(id, true);

                // pega o numero GNF da impressao do cartao
                String[] resp = ecf.enviar(EComando.ECF_NumGNF);
                pag.setEcfPagamentoGnf(Integer.valueOf(resp[1]));
                // salva o documento para relatorio
                new ComandoSalvarDocumento("RG").executar();
                TEF.bloquear(false);
            } catch (Exception ex) {
                TEF.bloquear(false);
                ecf.enviar(EComando.ECF_FechaRelatorio);
                TEF.confirmarTransacao(id, false);
                throw new OpenPdvException(ex);
            }
        } catch (Exception ex) {
            throw new OpenPdvException(ex);
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }
}
