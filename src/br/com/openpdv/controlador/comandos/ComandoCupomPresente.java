package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.ecf.EcfVendaProduto;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

    /**
     * Construtor padrao.
     */
    public ComandoCupomPresente() {
        try {
            List<EcfVenda> vendas = service.selecionar(new EcfVenda(), 0, 1, null);
            this.venda = vendas.get(0);
        } catch (OpenPdvException ex) {
            this.venda = null;
        }
    }

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
        }
    }

    /**
     * Construtor padrao.
     *
     * @param venda a venda seleciona, seja a ultima ou pelo ccf.
     */
    public ComandoCupomPresente(EcfVenda venda) {
        this.venda = venda;
    }

    @Override
    public void executar() throws OpenPdvException {
        if (venda != null) {
            StringBuilder sb = new StringBuilder();

            // abrindo o relatorio
            String[] resp = ECF.enviar(EComandoECF.ECF_AbreRelatorioGerencial, Util.getConfig().get("ecf.relpresente"));
            if (resp[0].equals("ERRO")) {
                ECF.enviar(EComandoECF.ECF_CorrigeEstadoErro);
                throw new OpenPdvException(resp[1]);
            }

            // cabecalho
            sb.append(ECF.LD).append(ECF.SL);
            sb.append("<CE><N>CUPOM PRESENTE</N></CE>").append(ECF.SL);
            sb.append(ECF.LD).append(ECF.SL);

            // dados da venda
            sb.append("<N>Venda: </N>").append(new SimpleDateFormat("dd/MM/yyyy").format(venda.getEcfVendaData()));
            sb.append(" CCF: ").append(Util.formataNumero(venda.getEcfVendaCcf(), 6, 0, false));
            sb.append(" COO: ").append(Util.formataNumero(venda.getEcfVendaCoo(), 6, 0, false));
            sb.append(ECF.SL);
            sb.append("ITEM CODIGO         DESCRICAO               QTD.").append(ECF.SL);
            sb.append(ECF.LS);

            // dados dos produtos
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
            for (EcfVendaProduto vp : venda.getEcfVendaProdutos()) {
                sb.append(Util.formataNumero(vp.getEcfVendaProdutoOrdem(), 3, 0, false));
                sb.append("  ");
                sb.append(Util.formataTexto(vp.getEcfVendaProdutoCodigo(), " ", 14, true));
                sb.append(" ");
                String desc = vp.getProdProduto().getProdProdutoDescricao().length() > 23 ? vp.getProdProduto().getProdProdutoDescricao().substring(0, 23) : vp.getProdProduto().getProdProdutoDescricao();
                sb.append(Util.formataTexto(desc, " ", 23, true));
                sb.append("  ");
                sb.append(Util.formataTexto(Util.formataNumero(vp.getEcfVendaProdutoQuantidade(), 1, 0, false), " ", 3, false));
                sb.append(ECF.SL);
            }

            // rodape
            sb.append(ECF.LD).append(ECF.SL);
            sb.append(Util.getConfig().get("ecf.msgpresente")).append(ECF.SL);
            sb.append(ECF.LD).append(ECF.SL);

            // envia o comando com todo o texto
            ECF.enviar(EComandoECF.ECF_LinhaRelatorioGerencial, sb.toString());
            if (resp[0].equals("ERRO")) {
                ECF.enviar(EComandoECF.ECF_CorrigeEstadoErro);
                throw new OpenPdvException(resp[1]);
            } else {
                ECF.enviar(EComandoECF.ECF_FechaRelatorio);
            }
        } else {
            log.error("Nao achou a venda para gerar o cupom de presente.");
            throw new OpenPdvException();
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
    }
}
