package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.core.parametro.GrupoParametro;
import br.com.openpdv.modelo.core.parametro.ParametroBinario;
import br.com.openpdv.modelo.core.parametro.ParametroFormula;
import br.com.openpdv.modelo.core.parametro.ParametroNumero;
import br.com.openpdv.modelo.core.parametro.ParametroObjeto;
import br.com.openpdv.modelo.ecf.EcfTroca;
import br.com.openpdv.modelo.ecf.EcfTrocaProduto;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.ecf.EcfVendaProduto;
import br.com.openpdv.modelo.produto.ProdGrade;
import br.com.openpdv.modelo.sistema.SisUsuario;
import br.com.openpdv.visao.core.Aguarde;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.ECF;
import br.com.phdss.EComando;
import br.com.phdss.IECF;
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
    private IECF ecf;

    /**
     * Construtor padrao.
     *
     * @param auto define se o cancelamento foi automatico.
     */
    public ComandoCancelarVenda(boolean auto) {
        this(auto, null);
    }

    /**
     * Construtor padrao.
     *
     * @param gerente informa o gerente que cancelou.
     */
    public ComandoCancelarVenda(SisUsuario gerente) {
        this(false, gerente);
    }

    /**
     * Construtor padrao.
     *
     * @param auto define se o cancelamento foi automatico.
     * @param gerente informa o gerente que cancelou.
     */
    public ComandoCancelarVenda(boolean auto, SisUsuario gerente) {
        this.log = Logger.getLogger(ComandoCancelarVenda.class);
        this.service = new CoreService();
        this.auto = auto;
        this.ecf = ECF.getInstancia();

        try {
            List<EcfVenda> vendas = service.selecionar(new EcfVenda(), 0, 1, null);
            if (!vendas.isEmpty()) {
                venda = vendas.get(0);
                venda.setSisGerente(gerente);
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
                // cancela na tela
                cancelarVendaTela();
                // atualizando o servidor
                if (!Util.getConfig().get("sinc.servidor").endsWith("localhost")) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                List<EcfVenda> lista = new ArrayList<>();
                                lista.add(venda);
                                lista = ComandoEnviarDados.getInstancia().enviar("venda", lista);
                                // marca a venda como sincronizada
                                if (!lista.isEmpty()) {
                                    CoreService service = new CoreService();
                                    venda.setEcfVendaSinc(true);
                                    service.salvar(venda);
                                }
                            } catch (Exception ex) {
                                log.error("Nao enviou no momento a venda com id -> " + venda.getId(), ex);
                            }
                        }
                    }).start();
                }
            } catch (OpenPdvException ex) {
                if (!auto) {
                    throw new OpenPdvException("A última venda já está cancelada.");
                }
            }

            // cancela os cartoes
            try {
                if (Boolean.valueOf(Util.getConfig().get("pag.cartao"))) {
                    new ComandoCancelarPagamento(venda.getEcfPagamentos(), auto).executar();
                }
                Caixa.getInstancia().modoDisponivel();
            } catch (OpenPdvException ex) {
                log.error("Erro ao cancelar os cartoes.", ex);
                Caixa.getInstancia().modoIndisponivel();
                JOptionPane.showMessageDialog(null, "Ocorreram problemas ao cancelar os cartões!\nSistema ficará indisponível até resolver o problema!", "TEF", JOptionPane.ERROR_MESSAGE);
            }
            // cancela a venda na tela
            cancelarVendaTela();
        } else {
            throw new OpenPdvException("A última venda já está cancelada.");
        }
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
        String[] resp = ecf.enviar(EComando.ECF_CancelaCupom, venda.getEcfVendaCcf() + "", venda.getEcfVendaCoo() + "", venda.getEcfVendaLiquido().toString());
        if (IECF.ERRO.equals(resp[0])) {
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
        double valor = 0.00;
        for (EcfVendaProduto vp : venda.getEcfVendaProdutos()) {
            // atualiza estoque
            if (venda.getEcfVendaFechada() && !vp.getEcfVendaProdutoCancelado()) {
                // fatorando a quantida no estoque
                double qtd = vp.getEcfVendaProdutoQuantidade();
                if (vp.getProdEmbalagem().getProdEmbalagemId() != vp.getProdProduto().getProdEmbalagem().getProdEmbalagemId()) {
                    qtd *= vp.getProdEmbalagem().getProdEmbalagemUnidade();
                    qtd /= vp.getProdProduto().getProdEmbalagem().getProdEmbalagemUnidade();
                }
                // atualiza o estoque
                ParametroFormula pf = new ParametroFormula("prodProdutoEstoque", qtd);
                FiltroNumero fn1 = new FiltroNumero("prodProdutoId", ECompara.IGUAL, vp.getProdProduto().getId());
                Sql sql1 = new Sql(vp.getProdProduto(), EComandoSQL.ATUALIZAR, fn1, pf);
                sqls.add(sql1);
                // adiciona estoque da grade caso o produto tenha
                for (ProdGrade grade : vp.getProdProduto().getProdGrades()) {
                    if (grade.getProdGradeBarra().equals(vp.getEcfVendaProdutoBarra())) {
                        ParametroFormula pf2 = new ParametroFormula("prodGradeEstoque", qtd);
                        FiltroNumero fn2 = new FiltroNumero("prodGradeId", ECompara.IGUAL, grade.getId());
                        Sql sql2 = new Sql(grade, EComandoSQL.ATUALIZAR, fn2, pf2);
                        sqls.add(sql2);
                        break;
                    }
                }
            }
            valor += vp.getEcfVendaProdutoBruto();
        }

        // remove a troca caso exista uma vinculada
        if (venda.getEcfTrocas() != null) {
            for (EcfTroca troca : venda.getEcfTrocas()) {
                FiltroNumero fn = new FiltroNumero("ecfTrocaId", ECompara.IGUAL, troca.getId());
                Sql sql = new Sql(troca, EComandoSQL.EXCLUIR, fn);
                sqls.add(sql);

                // atualiza o estoque
                for (EcfTrocaProduto tp : troca.getEcfTrocaProdutos()) {
                    // fatorando a quantida no estoque
                    double qtd = tp.getEcfTrocaProdutoQuantidade();
                    if (tp.getProdEmbalagem().getProdEmbalagemId() != tp.getProdProduto().getProdEmbalagem().getProdEmbalagemId()) {
                        qtd *= tp.getProdEmbalagem().getProdEmbalagemUnidade();
                        qtd /= tp.getProdProduto().getProdEmbalagem().getProdEmbalagemUnidade();
                    }

                    // atualiza o estoque
                    ParametroFormula pf = new ParametroFormula("prodProdutoEstoque", -1 * qtd);
                    FiltroNumero fn1 = new FiltroNumero("prodProdutoId", ECompara.IGUAL, tp.getProdProduto().getId());
                    Sql sql1 = new Sql(tp.getProdProduto(), EComandoSQL.ATUALIZAR, fn1, pf);
                    sqls.add(sql1);
                    // remove estoque da grade caso o produto tenha
                    if (tp.getProdProduto().getProdGrades() != null) {
                        for (ProdGrade grade : tp.getProdProduto().getProdGrades()) {
                            if (grade.getProdGradeBarra().equals(tp.getEcfTrocaProdutoBarra())) {
                                ParametroFormula pf2 = new ParametroFormula("prodGradeEstoque", -1 * qtd);
                                FiltroNumero fn2 = new FiltroNumero("prodGradeId", ECompara.IGUAL, grade.getId());
                                Sql sql2 = new Sql(grade, EComandoSQL.ATUALIZAR, fn2, pf2);
                                sqls.add(sql2);
                                break;
                            }
                        }
                    }
                }
            }
        }

        // atualiza o status da venda
        GrupoParametro gp = new GrupoParametro();
        FiltroNumero fn = new FiltroNumero("ecfVendaId", ECompara.IGUAL, venda.getId());
        if (!venda.getEcfVendaFechada()) {
            ParametroNumero pn1 = new ParametroNumero("ecfVendaBruto", valor);
            gp.add(pn1);
            ParametroNumero pn2 = new ParametroNumero("ecfVendaLiquido", valor);
            gp.add(pn2);
        }
        ParametroBinario pb = new ParametroBinario("ecfVendaCancelada", true);
        gp.add(pb);
        ParametroBinario pb1 = new ParametroBinario("ecfVendaSinc", false);
        gp.add(pb1);
        ParametroObjeto po = new ParametroObjeto("sisGerente", venda.getSisGerente());
        gp.add(po);
        Sql sql = new Sql(new EcfVenda(), EComandoSQL.ATUALIZAR, fn, gp);
        sqls.add(sql);

        // efetiva as instrucoes SQLs.
        service.executar(sqls.toArray(new Sql[]{}));
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
