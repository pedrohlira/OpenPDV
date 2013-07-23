package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import br.com.phdss.TEF;

/**
 * Classe que realiza a acao de cancelar as operacoes de Cartao.
 *
 * @author Pedro H. Lira
 */
public class ComandoCancelarCartao implements IComando {

    private EcfPagamento pag;

    /**
     * Construtor padrao.
     *
     * @param pag o pagamento de cartao a ser cancelado.
     */
    public ComandoCancelarCartao(EcfPagamento pag) {
        this.pag = pag;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            // cancela a trasacao
            String id = TEF.gerarId();
            TEF.cancelarTransacao(id, pag.getEcfPagamentoValor(), pag.getEcfPagamentoTipo().getEcfPagamentoTipoRede(), pag.getEcfPagamentoNsu(), pag.getEcfPagamentoData());

            try {
                TEF.bloquear(true);
                ECF.enviar(EComandoECF.ECF_FechaRelatorio);
                ECF.enviar(EComandoECF.ECF_AbreRelatorioGerencial, Util.getConfig().get("ecf.reltef"));
                TEF.imprimirVias(TEF.getDados(), EComandoECF.ECF_LinhaRelatorioGerencial);
                ECF.enviar(EComandoECF.ECF_FechaRelatorio);
                pag.setEcfPagamentoEstornoNsu(TEF.getDados().get("012-000"));
                TEF.confirmarTransacao(id, true);

                // pega o numero GNF da impressao do cartao
                String[] resp = ECF.enviar(EComandoECF.ECF_NumGNF);
                pag.setEcfPagamentoGnf(Integer.valueOf(resp[1]));
                // salva o documento para relatorio
                new ComandoSalvarDocumento("RG").executar();
                TEF.bloquear(false);
            } catch (Exception ex) {
                TEF.bloquear(false);
                ECF.enviar(EComandoECF.ECF_FechaRelatorio);
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
