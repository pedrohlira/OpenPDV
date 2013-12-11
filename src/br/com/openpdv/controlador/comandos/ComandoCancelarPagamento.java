package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import br.com.phdss.TEF;
import java.util.Date;
import java.util.List;

/**
 * Classe que realiza a acao de cencelar as operacoes de Cartao.
 *
 * @author Pedro H. Lira
 */
public class ComandoCancelarPagamento implements IComando {

    private CoreService service;
    private List<EcfPagamento> pagamentos;
    private boolean auto;

    /**
     * Construtor padrao.
     *
     * @param pagamentos a lista de pagamentos realizados.
     * @param auto informa se o cancelamento e automatico.
     */
    public ComandoCancelarPagamento(List<EcfPagamento> pagamentos, boolean auto) {
        this.service = new CoreService();
        this.pagamentos = pagamentos;
        this.auto = auto;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            // cancela primeiro os pendentes ou backup
            TEF.cancelarPendentes(auto);
            // depois cancela os registros que ja foram salvos em BD.
            cancelarSalvos();
        } catch (Exception ex) {
            throw new OpenPdvException(ex);
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    /**
     * Metodo que cancela as Cartao que já foram concluídas e salvas no BD.
     *
     * @throws Exception caso nao consiga cancelar alguma Cartao.
     */
    private void cancelarSalvos() throws Exception {
        if (pagamentos != null) {
            for (EcfPagamento pag : pagamentos) {
                if (pag.getEcfPagamentoEstorno() == 'N') {
                    pag.setEcfPagamentoEstorno('S');
                    pag.setEcfPagamentoEstornoData(new Date());
                    pag.setEcfPagamentoEstornoValor(pag.getEcfPagamentoValor());
                    if (!auto && pag.getEcfPagamentoTipo().isEcfPagamentoTipoTef() && !pag.getEcfPagamentoTipo().getEcfPagamentoTipoCodigo().equals(Util.getConfig().get("ecf.cheque")) && !pag.getEcfPagamentoNsu().startsWith("DV")) {
                        new ComandoCancelarCartao(pag).executar();
                    }
                    service.salvar(pag);
                }
            }
        }
    }
}
