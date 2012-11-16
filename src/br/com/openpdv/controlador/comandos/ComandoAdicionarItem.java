package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfVendaProduto;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import br.com.phdss.controlador.PAF;
import org.apache.log4j.Logger;

/**
 * Classe que realiza a acao de adicionar um item na venda.
 *
 * @author Pedro H. Lira
 */
public class ComandoAdicionarItem implements IComando {

    private Logger log;
    private EcfVendaProduto vendaProduto;

    /**
     * Construtor padrao.
     */
    public ComandoAdicionarItem() {
        this(null);
    }

    /**
     * Construtor padrao.
     *
     * @param vendaProduto o produto da venda.
     */
    public ComandoAdicionarItem(EcfVendaProduto vendaProduto) {
        this.log = Logger.getLogger(ComandoCancelarItem.class);
        this.vendaProduto = vendaProduto;
    }

    @Override
    public void executar() throws OpenPdvException {
        // adicionar o item no ECF
        adicionarItemEcf();
        // pega o ultimo numero
        String[] resp = ECF.enviar(EComandoECF.ECF_NumUltItem);
        // salva no bd
        adicionarItemBanco(resp[1]);
        // mostra na tela
        adicionarItemTela(resp[1]);
    }

    @Override
    public void desfazer() throws OpenPdvException {
        new ComandoCancelarItem(vendaProduto).executar();
    }

    /**
     * Metodo para adicionar o item no ECF.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void adicionarItemEcf() throws OpenPdvException {
        String codigo = vendaProduto.getEcfVendaProdutoCodigo();
        String descricao = Util.normaliza(vendaProduto.getProdProduto().getProdProdutoDescricao()).replace(",", ".");
        String aliquota = getAliquota().replace(",", ".");
        String qtd = Util.formataNumero(vendaProduto.getEcfVendaProdutoQuantidade(), 1, 2, false).replace(",", ".");
        String valor = Util.formataNumero(vendaProduto.getEcfVendaProdutoBruto(), 1, 2, false).replace(",", ".");
        String und = vendaProduto.getProdEmbalagem().getProdEmbalagemNome();
        if (und.length() > 3) {
            und = und.substring(0, 3);
        }

        String[] resp = ECF.enviar(EComandoECF.ECF_VendeItem, new String[]{codigo, descricao, aliquota, qtd, valor, "0", und});
        if (ECF.OK.equals(resp[0])) {
            // atualiza o gt
            try {
                resp = ECF.enviar(EComandoECF.ECF_GrandeTotal);
                if (ECF.OK.equals(resp[0])) {
                    PAF.AUXILIAR.setProperty("ecf.gt", resp[1]);
                    PAF.criptografar();
                } else {
                    throw new Exception(resp[1]);
                }
            } catch (Exception ex) {
                log.error("Erro ao atualizar o GT. -> ", ex);
                throw new OpenPdvException("Erro ao atualizar o GT.");
            }
        } else {
            log.error("Erro ao adicionar o item no ECF. -> " + resp[1]);
            throw new OpenPdvException(resp[1]);
        }
    }

    /**
     * Metodo para adicionar o item no BD.
     *
     * @param ordem o numero do item vendido.
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void adicionarItemBanco(final String ordem) throws OpenPdvException {
        vendaProduto.setEcfVenda(Caixa.getInstancia().getVenda());
        vendaProduto.setEcfVendaProdutoOrdem(Integer.valueOf(ordem));
        vendaProduto.setEcfVendaProdutoCancelado(false);
        // salva no bd
        CoreService<EcfVendaProduto> service = new CoreService<>();
        vendaProduto = service.salvar(vendaProduto);
    }

    /**
     * Metodo para adicionar o item na Tela.
     *
     * @param ordem o numero do item vendido.
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void adicionarItemTela(String ordem) throws OpenPdvException {
        // linha 1
        StringBuilder linha1 = new StringBuilder();
        linha1.append(Util.formataNumero(ordem, 3, 0, false));
        linha1.append("  ");
        linha1.append(Util.formataTexto(vendaProduto.getEcfVendaProdutoCodigo(), " ", 14, false));
        linha1.append(" ");
        linha1.append(vendaProduto.getProdProduto().getProdProdutoDescricao().length() > 28
                ? vendaProduto.getProdProduto().getProdProdutoDescricao().substring(0, 28) : vendaProduto.getProdProduto().getProdProdutoDescricao());
        Caixa.getInstancia().getBobina().addElement(linha1.toString());
        // linha 2
        StringBuilder linha2 = new StringBuilder();
        linha2.append(Util.formataTexto(Util.formataNumero(vendaProduto.getEcfVendaProdutoQuantidade(), 1, 2, false), " ", 8, true));
        linha2.append(" ");
        linha2.append(vendaProduto.getProdEmbalagem().getProdEmbalagemNome().length() > 3
                ? vendaProduto.getProdEmbalagem().getProdEmbalagemNome().subSequence(0, 3) : vendaProduto.getProdEmbalagem().getProdEmbalagemNome());
        linha2.append(" x ");
        linha2.append(Util.formataTexto(Util.formataNumero(vendaProduto.getEcfVendaProdutoLiquido(), 1, 2, false), " ", 13, true));
        linha2.append(Util.formataTexto(getAliquota(), " ", 5, false));
        linha2.append(Util.formataTexto(Util.formataNumero(vendaProduto.getEcfVendaProdutoTotal(), 1, 2, false), " ", 13, false));
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
