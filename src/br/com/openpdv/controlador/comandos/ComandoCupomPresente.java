package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.ecf.EcfVendaProduto;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.ECF;
import br.com.phdss.EComando;
import br.com.phdss.IECF;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 * Classe que realiza a impressao do cupom de presente.
 *
 * @author Pedro H. Lira
 */
public class ComandoCupomPresente implements IComando {

    private Logger log = Logger.getLogger(ComandoCupomPresente.class);
    private CoreService service = new CoreService();
    private EcfVenda venda;
    private IECF ecf;

    /**
     * Construtor padrao.
     *
     * @param ccf o numero do codigo do cupom fiscal.
     */
    public ComandoCupomPresente(int ccf) {
        try {
            FiltroNumero fn = new FiltroNumero("ecfVendaCcf", ECompara.IGUAL, ccf);
            this.venda = (EcfVenda) service.selecionar(new EcfVenda(), fn);
        } catch (OpenPdvException ex) {
            this.venda = null;
        } finally {
            this.ecf = ECF.getInstancia();
        }
    }

    /**
     * Construtor padrao.
     *
     * @param venda a venda seleciona, seja a ultima ou pelo ccf.
     */
    public ComandoCupomPresente(EcfVenda venda) {
        this.venda = venda;
        this.ecf = ECF.getInstancia();
    }

    @Override
    public void executar() throws OpenPdvException {
        if (venda != null) {
            // ordena os produtos
            Collections.sort(venda.getEcfVendaProdutos(), new Comparator<EcfVendaProduto>() {
                @Override
                public int compare(EcfVendaProduto o1, EcfVendaProduto o2) {
                    if (o1.getEcfVendaProdutoOrdem() < o2.getEcfVendaProdutoOrdem()) {
                        return -1;
                    } else if (o1.getEcfVendaProdutoOrdem() > o2.getEcfVendaProdutoOrdem()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            String cabecalho = cabecalho();
            String rodape = rodape();

            int escolha = JOptionPane.showOptionDialog(Caixa.getInstancia(), "Quais itens deseja imprimir no cupom?", "Cupom Presente",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Todos", "1-a-1", "Somente 1"}, JOptionPane.YES_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                // abrindo
                abrirCupom();
                StringBuilder sb = new StringBuilder();
                sb.append(cabecalho);
                // dados dos itens
                for (EcfVendaProduto vp : venda.getEcfVendaProdutos()) {
                    if (vp.getEcfVendaProdutoCancelado() == false) {
                        sb.append(item(vp));
                    }
                }
                // fechando
                sb.append(rodape);
                fecharCupom(sb.toString());
            } else if (escolha == JOptionPane.NO_OPTION) {
                for (EcfVendaProduto vp : venda.getEcfVendaProdutos()) {
                    if (vp.getEcfVendaProdutoCancelado() == false) {
                        // abrindo
                        abrirCupom();
                        StringBuilder sb = new StringBuilder();
                        sb.append(cabecalho);
                        // dados do item
                        sb.append(item(vp));
                        // fechando
                        sb.append(rodape);
                        fecharCupom(sb.toString());
                        try {
                            // espera 1 segundo para chamar o proximo
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            // se der erro pula
                        }
                    }
                }
            } else {
                String texto = JOptionPane.showInputDialog(Caixa.getInstancia(), "Insira o número do item.", 1);
                if (texto != null) {
                    texto = texto.replaceAll("\\D", "");
                    try {
                        int item = Integer.valueOf(texto);
                        if (item < 1 || item > venda.getEcfVendaProdutos().size()) {
                            JOptionPane.showMessageDialog(Caixa.getInstancia(), "O número informado não corresponde a nenhum item da venda.", "Cupom Presente", JOptionPane.WARNING_MESSAGE);
                        } else {
                            EcfVendaProduto vp = venda.getEcfVendaProdutos().get(item - 1);
                            if (vp.getEcfVendaProdutoCancelado() == false) {
                                // abrindo
                                abrirCupom();
                                StringBuilder sb = new StringBuilder();
                                sb.append(cabecalho);
                                // dados do item
                                sb.append(item(vp));
                                // fechando
                                sb.append(rodape);
                                fecharCupom(sb.toString());
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(Caixa.getInstancia(), "Não foi informado um número inteiro.", "Cupom Presente", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        } else {
            log.error("Nao achou a venda para gerar o cupom de presente.");
            throw new OpenPdvException("Nao achou a venda para gerar o cupom de presente.");
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
    }

    private void abrirCupom() throws OpenPdvException {
        // abrindo o relatorio
        String[] resp = ecf.enviar(EComando.ECF_AbreRelatorioGerencial, Util.getConfig().getProperty("ecf.relpresente"));
        if (resp[0].equals("ERRO")) {
            ecf.enviar(EComando.ECF_CorrigeEstadoErro);
            throw new OpenPdvException(resp[1]);
        }
    }

    private String cabecalho() {
        StringBuilder sb = new StringBuilder();
        // cabecalho
        sb.append(IECF.LD).append(IECF.SL);
        sb.append("<N>").append(Util.formataTexto("CUPOM PRESENTE", " ", IECF.COL, Util.EDirecao.AMBOS)).append("</N>").append(IECF.SL);
        sb.append(IECF.LD).append(IECF.SL);
        // dados da venda
        sb.append("Venda: ").append(new SimpleDateFormat("dd/MM/yyyy").format(venda.getEcfVendaData()));
        sb.append(" CCF: ").append(Util.formataNumero(venda.getEcfVendaCcf(), 6, 0, false));
        sb.append(" COO: ").append(Util.formataNumero(venda.getEcfVendaCoo(), 6, 0, false));
        sb.append(IECF.SL);
        sb.append("ITEM CODIGO         DESCRICAO               QTD.").append(IECF.SL);
        sb.append(IECF.LS).append(IECF.SL);
        return sb.toString();
    }

    private String item(EcfVendaProduto vp) {
        StringBuilder sb = new StringBuilder();
        sb.append(Util.formataNumero(vp.getEcfVendaProdutoOrdem(), 3, 0, false));
        sb.append("  ");
        sb.append(Util.formataTexto(vp.getEcfVendaProdutoCodigo(), " ", 14, Util.EDirecao.DIREITA));
        sb.append(" ");
        String desc = vp.getProdProduto().getProdProdutoDescricao().length() > 23 ? vp.getProdProduto().getProdProdutoDescricao().substring(0, 23) : vp.getProdProduto().getProdProdutoDescricao();
        sb.append(Util.formataTexto(desc, " ", 23, Util.EDirecao.DIREITA));
        sb.append("  ");
        sb.append(Util.formataTexto(Util.formataNumero(vp.getEcfVendaProdutoQuantidade(), 1, 0, false), " ", 3, Util.EDirecao.ESQUERDA));
        sb.append(IECF.SL);
        return sb.toString();
    }

    private String rodape() {
        StringBuilder sb = new StringBuilder();
        // rodape
        sb.append(IECF.LD).append(IECF.SL);
        sb.append(Util.getConfig().getProperty("ecf.msgpresente")).append(IECF.SL);
        sb.append("OPERADOR: ").append(venda.getSisUsuario().getSisUsuarioLogin());
        if (venda.getSisVendedor() != null) {
            sb.append(" - VENDEDOR: ").append(venda.getSisVendedor().getSisUsuarioLogin());
        }
        sb.append(IECF.SL).append(IECF.LD).append(IECF.SL);
        return sb.toString();
    }

    private void fecharCupom(String texto) throws OpenPdvException {
        // envia o comando com todo o texto
        String[] resp = ecf.enviar(EComando.ECF_LinhaRelatorioGerencial, texto);
        if (resp[0].equals(IECF.ERRO)) {
            ecf.enviar(EComando.ECF_CorrigeEstadoErro);
            throw new OpenPdvException(resp[1]);
        } else {
            ecf.enviar(EComando.ECF_FechaRelatorio);
        }
    }
}
