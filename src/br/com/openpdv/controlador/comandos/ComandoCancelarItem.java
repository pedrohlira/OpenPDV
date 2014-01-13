package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.core.parametro.ParametroBinario;
import br.com.openpdv.modelo.ecf.EcfVendaProduto;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.ECF;
import br.com.phdss.EComando;
import br.com.phdss.IECF;
import org.apache.log4j.Logger;

/**
 * Classe que realiza a acao de cancelar um item na venda.
 *
 * @author Pedro H. Lira
 */
public class ComandoCancelarItem implements IComando {

    private Logger log;
    private EcfVendaProduto vendaProduto;
    private IECF ecf;

    /**
     * Construtor padrao.
     */
    public ComandoCancelarItem() {
        this(null);
    }

    /**
     * Construtor padrao.
     *
     * @param vendaProduto o produto da venda.
     */
    public ComandoCancelarItem(EcfVendaProduto vendaProduto) {
        this.log = Logger.getLogger(ComandoCancelarItem.class);
        this.vendaProduto = vendaProduto;
        this.ecf = ECF.getInstancia();
    }

    @Override
    public void executar() throws OpenPdvException {
        // cancela o item no ECF
        cancelarItemEcf();
        // cancela o item no BD.
        cancelarItemBanco();
        // cancela o item na Tela.
        cancelarItemTela();
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    /**
     * Metodo que cancela o item no ECF.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void cancelarItemEcf() throws OpenPdvException {
        String codigo = vendaProduto.getEcfVendaProdutoCodigo();
        String aliquota = getAliquota().replace(",", ".");
        String qtd = Util.formataNumero(vendaProduto.getEcfVendaProdutoQuantidade(), 1, 2, false).replace(",", ".");
        String valor = Util.formataNumero(vendaProduto.getEcfVendaProdutoBruto(), 1, 2, false).replace(",", ".");
        String und = vendaProduto.getProdEmbalagem().getProdEmbalagemNome();
        if (und.length() > 3) {
            und = und.substring(0, 3);
        }

        String[] resp = ecf.enviar(EComando.ECF_CancelaItemVendido, new String[]{vendaProduto.getEcfVendaProdutoOrdem() + "", codigo, aliquota, qtd, valor, und});
        if (IECF.ERRO.equals(resp[0])) {
            log.error("Erro ao cancelar o item no ECF. -> " + resp[1]);
            throw new OpenPdvException(resp[1]);
        }
    }

    /**
     * Metodo que cancela o item no BD.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void cancelarItemBanco() throws OpenPdvException {
        FiltroNumero fn = new FiltroNumero("ecfVendaProdutoId", ECompara.IGUAL, vendaProduto.getId());
        ParametroBinario pb = new ParametroBinario("ecfVendaProdutoCancelado", true);
        Sql sql = new Sql(new EcfVendaProduto(), EComandoSQL.ATUALIZAR, fn, pb);

        CoreService service = new CoreService<>();
        service.executar(sql);
    }

    /**
     * Metodo que cancela o item na Tela.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void cancelarItemTela() throws OpenPdvException {
        String aliquota = getAliquota().replace(",", ".");
        String qtd = Util.formataNumero(vendaProduto.getEcfVendaProdutoQuantidade(), 1, 2, false).replace(",", ".");
        String valor = Util.formataNumero(vendaProduto.getEcfVendaProdutoBruto(), 1, 2, false).replace(",", ".");
        double total = Double.valueOf(qtd) * Double.valueOf(valor);
        String und = vendaProduto.getProdEmbalagem().getProdEmbalagemNome();
        if (und.length() > 3) {
            und = und.substring(0, 3);
        }

        // linha 1
        StringBuilder linha1 = new StringBuilder("cancelamento item:");
        linha1.append(Util.formataNumero(vendaProduto.getEcfVendaProdutoOrdem(), 3, 0, false)).append("  ");
        linha1.append(Util.formataTexto(vendaProduto.getEcfVendaProdutoCodigo(), " ", 14, Util.EDirecao.DIREITA));
        Caixa.getInstancia().getBobina().addElement(linha1.toString());
        // linha 2
        StringBuilder linha2 = new StringBuilder();
        linha2.append(Util.formataTexto(Util.formataNumero(qtd, 1, 0, false), " ", 8, Util.EDirecao.ESQUERDA));
        linha2.append(und).append(" X ");
        linha2.append(Util.formataTexto(Util.formataNumero(valor, 1, 2, false), " ", 11, Util.EDirecao.DIREITA));
        linha2.append(Util.formataTexto(aliquota, " ", 9, Util.EDirecao.DIREITA));
        linha2.append(Util.formataTexto("-" + Util.formataNumero(total, 1, 2, false) + " ", " ", 14, Util.EDirecao.ESQUERDA));
        Caixa.getInstancia().getBobina().addElement(linha2.toString());
        // colocando no foco selecionado
        Caixa.getInstancia().getLstBobina().setSelectedIndex(Caixa.getInstancia().getBobina().getSize() - 1);
        Caixa.getInstancia().getLstBobina().ensureIndexIsVisible(Caixa.getInstancia().getBobina().getSize() - 1);
    }

    /**
     * Metodo que identifica o tipo e porcentagem da aliquota.
     *
     * @return no formato requerido.
     */
    private String getAliquota() {
        String aliquota;
        switch (vendaProduto.getEcfVendaProdutoTributacao()) {
            case 'T':
                aliquota = Util.formataNumero(vendaProduto.getEcfVendaProdutoIcms(), 2, 2, false) + "T";
                break;
            case 'S':
                aliquota = Util.formataNumero(vendaProduto.getEcfVendaProdutoIssqn(), 2, 2, false) + "S";
                break;
            default:
                aliquota = String.valueOf(vendaProduto.getEcfVendaProdutoTributacao()) + String.valueOf(vendaProduto.getEcfVendaProdutoTributacao());
                break;
        }
        return aliquota;
    }

    public EcfVendaProduto getVendaProduto() {
        return vendaProduto;
    }

    public void setVendaProduto(EcfVendaProduto vendaProduto) {
        this.vendaProduto = vendaProduto;
    }
}
