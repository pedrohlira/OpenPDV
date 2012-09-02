package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.ECF;
import br.com.openpdv.controlador.EComandoECF;
import br.com.openpdv.controlador.PAF;
import br.com.openpdv.controlador.TEF;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.core.parametro.*;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.ecf.EcfVendaProduto;
import br.com.openpdv.visao.core.Aguarde;
import br.com.openpdv.visao.core.Caixa;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;

/**
 * Classe que realiza a acao de fechar uma venda.
 *
 * @author Pedro H. Lira
 */
public class ComandoFecharVenda implements IComando {

    private Logger log;
    private List<EcfPagamento> pagamentos;
    private double bruto;
    private double acres_desc;
    private double troco;

    /**
     * Construtor padrao.
     *
     * @param pagamentos a lista de pagamentos realizados.
     * @param bruto o valor total da venda.
     * @param acres_desc valor de acrescimo (positivo) ou desconto (negativo).
     * @param troco o valor do troco da venda.
     */
    public ComandoFecharVenda(List<EcfPagamento> pagamentos, double bruto, double acres_desc, double troco) {
        this.log = Logger.getLogger(ComandoFecharVenda.class);
        this.pagamentos = pagamentos;
        this.bruto = bruto;
        this.acres_desc = acres_desc;
        this.troco = troco;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            TEF.blockInput(true);
            // fecha a venda no cupom
            fecharVendaECF();
            // salva no bd
            fecharVendaBanco();
            // salva o documento para relatorio
            new ComandoSalvarDocumento("RV").executar();
            // imprime os cartoes se tiver
            new ComandoImprimirCartao(pagamentos, troco).executar();
            // coloca na tela
            fecharVendaTela();
            TEF.blockInput(false);
        } catch (OpenPdvException ex) {
            TEF.blockInput(false);
            throw ex;
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    /**
     * Metodo para fechar uma venda no ECF.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void fecharVendaECF() throws OpenPdvException {
        // sub totaliza
        String AD = Util.formataNumero(acres_desc, 1, 2, false).replace(",", ".");
        StringBuilder sb = new StringBuilder();
        sb.append(Util.formataTexto("MD5: " + PAF.AUXILIAR.getProperty("out.autenticado"), " ", ECF.COL, true));
        // caso nao tenha sido informado o cliente
        if (Caixa.getInstancia().getVenda().getSisCliente() == null) {
            sb.append("CONSUMIDOR NAO INFORMOU O CPF");
        }

        ECF.enviar(EComandoECF.ECF_SubtotalizaCupom, AD, sb.toString());
        // soma os pagamento que possuem o mesmo codigo
        Map<String, Double> pags = new HashMap<>();
        for (EcfPagamento pag : pagamentos) {
            if (pags.containsKey(pag.getEcfPagamentoTipo().getEcfPagamentoTipoCodigo())) {
                double valor = pag.getEcfPagamentoValor() + pags.get(pag.getEcfPagamentoTipo().getEcfPagamentoTipoCodigo());
                pags.put(pag.getEcfPagamentoTipo().getEcfPagamentoTipoCodigo(), valor);
            } else {
                pags.put(pag.getEcfPagamentoTipo().getEcfPagamentoTipoCodigo(), pag.getEcfPagamentoValor());
            }
        }
        for (Entry<String, Double> pag : pags.entrySet()) {
            String valor = Util.formataNumero(pag.getValue(), 1, 2, false).replace(",", ".");
            ECF.enviar(EComandoECF.ECF_EfetuaPagamento, pag.getKey(), valor);
        }
        // fecha a venda
        String[] resp = ECF.enviar(EComandoECF.ECF_FechaCupom);
        if (ECF.ERRO.equals(resp[0])) {
            log.error("Erro ao fechar a venda. -> " + resp[1]);
            throw new OpenPdvException(resp[1]);
        }
    }

    /**
     * Metodo para fechar uma venda no BD.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void fecharVendaBanco() throws OpenPdvException {
        // fecha a venda
        EcfVenda venda = Caixa.getInstancia().getVenda();
        List<Sql> sqls = new ArrayList<>();
        FiltroNumero fn = new FiltroNumero("ecfVendaId", ECompara.IGUAL, venda.getId());
        ParametroNumero pn1 = new ParametroNumero("ecfVendaBruto", bruto);
        ParametroNumero pn2 = new ParametroNumero(acres_desc > 0 ? "ecfVendaAcrescimo" : "ecfVendaDesconto", Math.abs(acres_desc));
        ParametroNumero pn3 = new ParametroNumero("ecfVendaLiquido", bruto + acres_desc);
        ParametroBinario pb = new ParametroBinario("ecfVendaFechada", true);
        GrupoParametro gp = new GrupoParametro(new IParametro[]{pn1, pn2, pn3, pb});
        Sql sql = new Sql(new EcfVenda(), EComandoSQL.ATUALIZAR, fn, gp);
        sqls.add(sql);

        // atualiza estoque
        for (EcfVendaProduto vendaProduto : venda.getEcfVendaProdutos()) {
            if (!vendaProduto.getEcfVendaProdutoCancelado()) {
                // fatorando a quantida no estoque
                double qtd = vendaProduto.getEcfVendaProdutoQuantidade();
                if (vendaProduto.getProdEmbalagem().getProdEmbalagemId() != vendaProduto.getProdProduto().getProdEmbalagem().getProdEmbalagemId()) {
                    qtd *= vendaProduto.getProdEmbalagem().getProdEmbalagemUnidade();
                    qtd /= vendaProduto.getProdProduto().getProdEmbalagem().getProdEmbalagemUnidade();
                }
                // atualiza o estoque
                ParametroFormula pf = new ParametroFormula("prodProdutoEstoque", -1 * qtd);
                FiltroNumero fn1 = new FiltroNumero("prodProdutoId", ECompara.IGUAL, vendaProduto.getProdProduto().getId());
                Sql sql1 = new Sql(vendaProduto.getProdProduto(), EComandoSQL.ATUALIZAR, fn1, pf);
                sqls.add(sql1);
            }
        }
        CoreService service = new CoreService();
        service.executar(sqls);
    }

    /**
     * Metodo para fechar uma venda na Tela.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void fecharVendaTela() throws OpenPdvException {
        Aguarde.getInstancia().getLblMensagem().setText("Aguarde o processamento...");
        Aguarde.getInstancia().setVisible(false);
        Caixa.getInstancia().getBobina().removeAllElements();
        Caixa.getInstancia().modoDisponivel();
    }
}
