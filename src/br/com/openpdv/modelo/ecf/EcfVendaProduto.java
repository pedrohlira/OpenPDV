package br.com.openpdv.modelo.ecf;

import br.com.phdss.Util;
import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdProduto;
import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

/**
 * Classe que representa a produto vendido do sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_venda_produto")
@XmlRootElement
public class EcfVendaProduto extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecf_venda_produto_id")
    private Integer ecfVendaProdutoId;
    @Column(name = "ecf_venda_produto_barra")
    private String ecfVendaProdutoBarra;
    @Column(name = "ecf_venda_produto_cst_cson")
    private String ecfVendaProdutoCstCson;
    @Column(name = "ecf_venda_produto_tributacao")
    private char ecfVendaProdutoTributacao;
    @Column(name = "ecf_venda_produto_icms")
    private Double ecfVendaProdutoIcms;
    @Column(name = "ecf_venda_produto_issqn")
    private Double ecfVendaProdutoIssqn;
    @Column(name = "ecf_venda_produto_quantidade")
    private Double ecfVendaProdutoQuantidade;
    @Column(name = "ecf_venda_produto_bruto")
    private Double ecfVendaProdutoBruto;
    @Column(name = "ecf_venda_produto_desconto")
    private Double ecfVendaProdutoDesconto;
    @Column(name = "ecf_venda_produto_acrescimo")
    private Double ecfVendaProdutoAcrescimo;
    @Column(name = "ecf_venda_produto_liquido")
    private Double ecfVendaProdutoLiquido;
    @Column(name = "ecf_venda_produto_total")
    private Double ecfVendaProdutoTotal;
    @Column(name = "ecf_venda_produto_cancelado")
    private boolean ecfVendaProdutoCancelado;
    @Column(name = "ecf_venda_produto_ordem")
    private int ecfVendaProdutoOrdem;
    @ManyToOne
    @JoinColumn(name = "ecf_venda_id")
    @XmlInverseReference(mappedBy = "ecfVendaProdutos")
    private EcfVenda ecfVenda;
    @JoinColumn(name = "prod_produto_id")
    @ManyToOne
    private ProdProduto prodProduto;
    @JoinColumn(name = "prod_embalagem_id")
    @ManyToOne
    private ProdEmbalagem prodEmbalagem;

    /**
     * Construtor padrao
     */
    public EcfVendaProduto() {
        this(0);
    }

    /**
     * Costrutor informando os dados do produto e quantidade.
     *
     * @param produto referencia para o produto selecionado.
     * @param quantidade a quantidade total vendida.
     * @param barra o codigo de barras do produto.
     */
    public EcfVendaProduto(ProdProduto produto, Double quantidade, String barra) {
        this(0);
        this.prodProduto = produto;
        this.prodEmbalagem = produto.getProdEmbalagem();
        this.ecfVendaProdutoBarra = barra;
        this.ecfVendaProdutoCstCson = produto.getProdProdutoCstCson();
        this.ecfVendaProdutoTributacao = produto.getProdProdutoTributacao();
        this.ecfVendaProdutoIcms = produto.getProdProdutoIcms();
        this.ecfVendaProdutoIssqn = produto.getProdProdutoIssqn();
        this.ecfVendaProdutoQuantidade = quantidade;
        this.ecfVendaProdutoBruto = produto.getProdProdutoPreco();
        this.ecfVendaProdutoDesconto = 0.00;
        this.ecfVendaProdutoAcrescimo = 0.00;
        this.ecfVendaProdutoLiquido = produto.getProdProdutoPreco();
        this.ecfVendaProdutoTotal = quantidade * produto.getProdProdutoPreco();
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfVendaProdutoId o id do registro.
     */
    public EcfVendaProduto(Integer ecfVendaProdutoId) {
        super("EcfVendaProduto", "ecfVendaProdutoId", "ecfVendaProdutoOrdem");
        this.ecfVendaProdutoId = ecfVendaProdutoId;
    }

    @Override
    public Integer getId() {
        return ecfVendaProdutoId;
    }

    @Override
    public void setId(Integer id) {
        ecfVendaProdutoId = id;
    }

    public Integer getEcfVendaProdutoId() {
        return ecfVendaProdutoId;
    }

    public void setEcfVendaProdutoId(Integer ecfVendaProdutoId) {
        this.ecfVendaProdutoId = ecfVendaProdutoId;
    }

    public String getEcfVendaProdutoBarra() {
        return ecfVendaProdutoBarra;
    }

    public void setEcfVendaProdutoBarra(String ecfVendaProdutoBarra) {
        this.ecfVendaProdutoBarra = ecfVendaProdutoBarra;
    }

    public String getEcfVendaProdutoCstCson() {
        return ecfVendaProdutoCstCson;
    }

    public void setEcfVendaProdutoCstCson(String ecfVendaProdutoCstCson) {
        this.ecfVendaProdutoCstCson = ecfVendaProdutoCstCson;
    }

    public char getEcfVendaProdutoTributacao() {
        return ecfVendaProdutoTributacao;
    }

    public void setEcfVendaProdutoTributacao(char ecfVendaProdutoTributacao) {
        this.ecfVendaProdutoTributacao = ecfVendaProdutoTributacao;
    }

    public Double getEcfVendaProdutoIcms() {
        return ecfVendaProdutoIcms;
    }

    public void setEcfVendaProdutoIcms(Double ecfVendaProdutoIcms) {
        this.ecfVendaProdutoIcms = ecfVendaProdutoIcms;
    }

    public Double getEcfVendaProdutoIssqn() {
        return ecfVendaProdutoIssqn;
    }

    public void setEcfVendaProdutoIssqn(Double ecfVendaProdutoIssqn) {
        this.ecfVendaProdutoIssqn = ecfVendaProdutoIssqn;
    }

    public Double getEcfVendaProdutoQuantidade() {
        return ecfVendaProdutoQuantidade;
    }

    public void setEcfVendaProdutoQuantidade(Double ecfVendaProdutoQuantidade) {
        this.ecfVendaProdutoQuantidade = ecfVendaProdutoQuantidade;
    }

    public Double getEcfVendaProdutoBruto() {
        return ecfVendaProdutoBruto;
    }

    public void setEcfVendaProdutoBruto(Double ecfVendaProdutoBruto) {
        this.ecfVendaProdutoBruto = ecfVendaProdutoBruto;
    }

    public Double getEcfVendaProdutoDesconto() {
        return ecfVendaProdutoDesconto;
    }

    public void setEcfVendaProdutoDesconto(Double ecfVendaProdutoDesconto) {
        this.ecfVendaProdutoDesconto = ecfVendaProdutoDesconto;
    }

    public Double getEcfVendaProdutoAcrescimo() {
        return ecfVendaProdutoAcrescimo;
    }

    public void setEcfVendaProdutoAcrescimo(Double ecfVendaProdutoAcrescimo) {
        this.ecfVendaProdutoAcrescimo = ecfVendaProdutoAcrescimo;
    }

    public Double getEcfVendaProdutoLiquido() {
        return ecfVendaProdutoLiquido;
    }

    public void setEcfVendaProdutoLiquido(Double ecfVendaProdutoLiquido) {
        this.ecfVendaProdutoLiquido = ecfVendaProdutoLiquido;
    }

    public Double getEcfVendaProdutoTotal() {
        return ecfVendaProdutoTotal;
    }

    public void setEcfVendaProdutoTotal(Double ecfVendaProdutoTotal) {
        this.ecfVendaProdutoTotal = ecfVendaProdutoTotal;
    }

    public boolean getEcfVendaProdutoCancelado() {
        return ecfVendaProdutoCancelado;
    }

    public void setEcfVendaProdutoCancelado(boolean ecfVendaProdutoCancelado) {
        this.ecfVendaProdutoCancelado = ecfVendaProdutoCancelado;
    }

    public int getEcfVendaProdutoOrdem() {
        return ecfVendaProdutoOrdem;
    }

    public void setEcfVendaProdutoOrdem(int ecfVendaProdutoOrdem) {
        this.ecfVendaProdutoOrdem = ecfVendaProdutoOrdem;
    }

    public EcfVenda getEcfVenda() {
        return ecfVenda;
    }

    public void setEcfVenda(EcfVenda ecfVenda) {
        this.ecfVenda = ecfVenda;
    }

    public ProdProduto getProdProduto() {
        return prodProduto;
    }

    public void setProdProduto(ProdProduto prodProduto) {
        this.prodProduto = prodProduto;
    }

    public ProdEmbalagem getProdEmbalagem() {
        return prodEmbalagem;
    }

    public void setProdEmbalagem(ProdEmbalagem prodEmbalagem) {
        this.prodEmbalagem = prodEmbalagem;
    }

    public String getEcfVendaProdutoCodigo() {
        return ecfVendaProdutoBarra == null || ecfVendaProdutoBarra.equals("") ? Util.formataNumero(prodProduto.getProdProdutoId(), 6, 0, false) : ecfVendaProdutoBarra;
    }
}
