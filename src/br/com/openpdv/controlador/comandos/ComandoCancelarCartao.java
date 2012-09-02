package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.ECF;
import br.com.openpdv.controlador.EComandoECF;
import br.com.openpdv.controlador.TEF;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import java.util.Date;
import java.util.List;

/**
 * Classe que realiza a acao de cencelar as operacoes de Cartao.
 *
 * @author Pedro H. Lira
 */
public class ComandoCancelarCartao implements IComando {

    private CoreService service;
    private List<EcfPagamento> pagamentos;

    /**
     * Construtor padrao.
     *
     * @param pagamentos a lista de pagamentos realizados.
     */
    public ComandoCancelarCartao(List<EcfPagamento> pagamentos) {
        this.service = new CoreService();
        this.pagamentos = pagamentos;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            // cancela primeiro os pendentes ou backup
            TEF.cancelarPendentes();
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
     * @throws OpenPdvException caso nao consiga cancelar alguma Cartao.
     */
    private void cancelarSalvos() throws OpenPdvException {
        for (EcfPagamento pag : pagamentos) {
            if (pag.getEcfPagamentoEstorno() == 'N') {
                pag.setEcfPagamentoEstorno('S');
                pag.setEcfPagamentoEstornoData(new Date());
                pag.setEcfPagamentoEstornoValor(pag.getEcfPagamentoValor());

                if (pag.getEcfPagamentoTipo().isEcfPagamentoTipoTef()) {
                    // imprime as vias
                    try {
                        // cancela a trasacao
                        String id = TEF.gerarId();
                        TEF.cancelarTransacao(id, pag.getEcfPagamentoValor(), pag.getEcfPagamentoTipo().getEcfPagamentoTipoRede(), pag.getEcfPagamentoNsu(), pag.getEcfPagamentoData());

                        ECF.enviar(EComandoECF.ECF_AbreRelatorioGerencial, Util.getConfig().get("ecf.reltef"));
                        TEF.imprimirVias(TEF.getDados(), EComandoECF.ECF_LinhaRelatorioGerencial);
                        ECF.enviar(EComandoECF.ECF_FechaRelatorio);

                        pag.setEcfPagamentoEstornoNsu(TEF.getDados().get("012-000"));
                        // confirma o cancelamento ao GP
                        TEF.confirmarTransacao(id, true);
                        TEF.deletarPendente(id);

                        // pega o numero GNF da impressao do cartao
                        String[] resp = ECF.enviar(EComandoECF.ECF_NumGNF);
                        pag.setEcfPagamentoGnf(Integer.valueOf(resp[1]));
                        // salva o documento para relatorio
                        new ComandoSalvarDocumento("RG").executar();
                    } catch (Exception ex) {
                        ECF.enviar(EComandoECF.ECF_FechaRelatorio);
                        throw new OpenPdvException(ex);
                    }
                }

                service.salvar(pag);
            }
        }
    }
}
