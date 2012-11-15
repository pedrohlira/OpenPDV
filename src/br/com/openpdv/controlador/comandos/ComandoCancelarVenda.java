package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.core.parametro.ParametroBinario;
import br.com.openpdv.modelo.core.parametro.ParametroFormula;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.ecf.EcfVendaProduto;
import br.com.openpdv.visao.core.Aguarde;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 * Classe que realiza a acao de cancelar uma venda.
 *
 * @author Pedro H. Lira
 */
public class ComandoCancelarVenda implements IComando {

    private Logger log;
    private CoreService service;
    private EcfVenda venda;
    private boolean auto;

    /**
     * Construtor padrao.
     */
    public ComandoCancelarVenda(boolean auto) {
        this.log = Logger.getLogger(ComandoCancelarVenda.class);
        this.service = new CoreService();
        this.auto = auto;

        try {
            List<EcfVenda> vendas = service.selecionar(new EcfVenda(), 0, 1, null);
            if (!vendas.isEmpty()) {
                venda = vendas.get(0);
            }
        } catch (OpenPdvException ex) {
            venda = null;
        }
    }

    @Override
    public void executar() throws OpenPdvException {
        if (venda != null && !venda.getEcfVendaCancelada()) {
            try {
                // cancela a venda no cupom.
                cancelarVendaECF();
                // cancela a venda no BD.
                cancelarVendaBanco();
            } catch (OpenPdvException ex) {
                // caso o cancelamente nao seja automatico, dispara a excecao
                if (!auto) {
                    cancelarVendaTela();
                    throw new OpenPdvException("A última venda já está cancelada.");
                }
            }

            // cancela os cartoes
            try {
                new ComandoCancelarCartao(venda.getEcfPagamentos(), auto).executar();
                Caixa.getInstancia().modoDisponivel();
            } catch (OpenPdvException ex) {
                log.error("Erro ao cancelar os cartoes.", ex);
                Caixa.getInstancia().modoIndisponivel();
                JOptionPane.showMessageDialog(null, "Ocorreram problemas ao cancelar os cartões!\nSistema ficará indisponível até resolver o problema!", "TEF", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            throw new OpenPdvException("A última venda já está cancelada.");
        }

        // cancela a venda na tela
        cancelarVendaTela();
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    /**
     * Metodo para cancelar uma venda no ECF.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void cancelarVendaECF() throws OpenPdvException {
        String[] resp = ECF.enviar(EComandoECF.ECF_CancelaCupom);
        if (ECF.ERRO.equals(resp[0])) {
            log.error("Erro ao cancelar a venda. -> " + resp[1]);
            throw new OpenPdvException(resp[1]);
        }
    }

    /**
     * Metodo para cancelar uma venda no BD.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void cancelarVendaBanco() throws OpenPdvException {
        List<Sql> sqls = new ArrayList<>();
        FiltroNumero fn = new FiltroNumero("ecfVendaId", ECompara.IGUAL, venda.getId());
        ParametroBinario pb = new ParametroBinario("ecfVendaCancelada", true);
        Sql sql = new Sql(new EcfVenda(), EComandoSQL.ATUALIZAR, fn, pb);
        sqls.add(sql);

        // atualiza estoque
        if (venda.getEcfVendaFechada()) {
            for (EcfVendaProduto vendaProduto : venda.getEcfVendaProdutos()) {
                if (!vendaProduto.getEcfVendaProdutoCancelado()) {
                    // fatorando a quantida no estoque
                    double qtd = vendaProduto.getEcfVendaProdutoQuantidade();
                    if (vendaProduto.getProdEmbalagem().getProdEmbalagemId() != vendaProduto.getProdProduto().getProdEmbalagem().getProdEmbalagemId()) {
                        qtd *= vendaProduto.getProdEmbalagem().getProdEmbalagemUnidade();
                        qtd /= vendaProduto.getProdProduto().getProdEmbalagem().getProdEmbalagemUnidade();
                    }
                    // atualiza o estoque
                    ParametroFormula pf = new ParametroFormula("prodProdutoEstoque", qtd);
                    FiltroNumero fn1 = new FiltroNumero("prodProdutoId", ECompara.IGUAL, vendaProduto.getProdProduto().getId());
                    Sql sql1 = new Sql(vendaProduto.getProdProduto(), EComandoSQL.ATUALIZAR, fn1, pf);
                    sqls.add(sql1);
                }
            }
        }
        service.executar(sqls);
    }

    /**
     * Metodo para cancelar uma venda na Tela.
     *
     * @throws OpenPdvException dispara caso nao consiga executar.
     */
    public void cancelarVendaTela() throws OpenPdvException {
        Caixa.getInstancia().getBobina().removeAllElements();
        Aguarde.getInstancia().setVisible(false);
    }
}
