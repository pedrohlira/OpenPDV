package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.core.parametro.ParametroBinario;
import br.com.openpdv.modelo.ecf.EcfVendaProduto;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Classe que realiza a acao de cancelar um item na venda.
 *
 * @author Pedro H. Lira
 */
public class ComandoCancelarItem implements IComando {

    private Logger log;
    private EcfVendaProduto vendaProduto;

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
        String[] resp = ECF.enviar(EComandoECF.ECF_CancelaItemVendido, vendaProduto.getEcfVendaProdutoOrdem() + "");
        if (ECF.ERRO.equals(resp[0])) {
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
        List<Sql> sqls = new ArrayList<>();
        FiltroNumero fn = new FiltroNumero("ecfVendaProdutoId", ECompara.IGUAL, vendaProduto.getId());
        ParametroBinario pb = new ParametroBinario("ecfVendaProdutoCancelado", true);
        Sql sql = new Sql(new EcfVendaProduto(), EComandoSQL.ATUALIZAR, fn, pb);
        sqls.add(sql);

        CoreService service = new CoreService<>();
        service.executar(sqls);
    }

    /**
     * Metodo que cancela o item na Tela.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void cancelarItemTela() throws OpenPdvException {
        Caixa.getInstancia().getBobina().addElement(Util.formataTexto("", "*", ECF.COL, true));
        // linha 1
        StringBuilder linha1 = new StringBuilder();
        linha1.append(Util.formataNumero(vendaProduto.getEcfVendaProdutoOrdem(), 3, 0, false));
        linha1.append("  ");
        linha1.append(Util.formataTexto(vendaProduto.getEcfVendaProdutoCodigo(), " ", 14, true));
        linha1.append(" ");
        linha1.append(vendaProduto.getProdProduto().getProdProdutoDescricao().length() > 28
                ? vendaProduto.getProdProduto().getProdProdutoDescricao().substring(0, 28) : vendaProduto.getProdProduto().getProdProdutoDescricao());
        Caixa.getInstancia().getBobina().addElement(linha1.toString());
        // linha 2
        Caixa.getInstancia().getBobina().addElement("<html><b>ITEM CANCELADO</b></html>");
        Caixa.getInstancia().getBobina().addElement(Util.formataTexto("", "*", ECF.COL, true));
        // colocando no foco selecionado
        Caixa.getInstancia().getLstBobina().setSelectedIndex(Caixa.getInstancia().getBobina().getSize() - 1);
        Caixa.getInstancia().getLstBobina().ensureIndexIsVisible(Caixa.getInstancia().getBobina().getSize() - 1);
    }

    public EcfVendaProduto getVendaProduto() {
        return vendaProduto;
    }

    public void setVendaProduto(EcfVendaProduto vendaProduto) {
        this.vendaProduto = vendaProduto;
    }
}