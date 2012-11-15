package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import br.com.phdss.TEF;
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
    private boolean auto;

    /**
     * Construtor padrao.
     *
     * @param pagamentos a lista de pagamentos realizados.
     */
    public ComandoCancelarCartao(List<EcfPagamento> pagamentos, boolean auto) {
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

                    if (!auto && pag.getEcfPagamentoTipo().isEcfPagamentoTipoTef() && !pag.getEcfPagamentoTipo().getEcfPagamentoTipoCodigo().equals(Util.getConfig().get("ecf.cheque"))) {
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
                    }

                    service.salvar(pag);
                }
            }
        }
    }
}
