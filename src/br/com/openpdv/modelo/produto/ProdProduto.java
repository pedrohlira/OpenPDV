package br.com.openpdv.modelo.produto;

import br.com.openpdv.modelo.core.Colecao;
import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.core.ELetra;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que representa os produtos do sistema.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_produto")
@XmlRootElement
public class ProdProduto extends Dados implements Serializable {

    @Id
    @Column(name = "prod_produto_id")
    private Integer prodProdutoId;
    @Column(name = "prod_produto_ncm")
    private String prodProdutoNcm;
    @Column(name = "prod_produto_barra")
    private String prodProdutoBarra;
    @Column(name = "prod_produto_descricao")
    private String prodProdutoDescricao;
    @Column(name = "prod_produto_referencia")
    private String prodProdutoReferencia;
    @Column(name = "prod_produto_preco")
    private Double prodProdutoPreco;
    @Column(name = "prod_produto_estoque")
    private Double prodProdutoEstoque;
    @Column(name = "prod_produto_tipo")
    private String prodProdutoTipo;
    @Column(name = "prod_produto_origem")
    private char prodProdutoOrigem;
    @Column(name = "prod_produto_cst_cson")
    private String prodProdutoCstCson;
    @Column(name = "prod_produto_tributacao")
    private char prodProdutoTributacao;
    @Column(name = "prod_produto_icms")
    private Double prodProdutoIcms;
    @Column(name = "prod_produto_issqn")
    private Double prodProdutoIssqn;
    @Column(name = "prod_produto_iat")
    private char prodProdutoIat;
    @Column(name = "prod_produto_ippt")
    private char prodProdutoIppt;
    @Column(name = "prod_produto_cadastrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date prodProdutoCadastrado;
    @Column(name = "prod_produto_alterado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date prodProdutoAlterado;
    @Column(name = "prod_produto_ativo")
    private boolean prodProdutoAtivo;
    @JoinColumn(name = "prod_embalagem_id")
    @ManyToOne
    private ProdEmbalagem prodEmbalagem;
    @OneToMany(mappedBy = "prodProduto", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<ProdPreco> prodPrecos;
    @OneToMany(mappedBy = "prodProdutoPrincipal", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<ProdComposicao> prodComposicoes;
    @OneToMany(mappedBy = "prodProduto", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<ProdGrade> prodGrades;

    /**
     * Construtor padrao
     */
    public ProdProduto() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param prodProdutoId o id do registro.
     */
    public ProdProduto(Integer prodProdutoId) {
        super("ProdProduto", "prodProdutoId", "prodProdutoDescricao");
        this.prodProdutoId = prodProdutoId;
        this.setTipoLetra(ELetra.GRANDE);
        Colecao col = new Colecao("ProdPreco", "t.prodPrecos", "LEFT JOIN", "t1");
        Colecao col1 = new Colecao("ProdGrade", "t.prodGrades", "LEFT JOIN", "t2");
        this.setColecao(new Colecao[]{col, col1});
    }

    @Override
    public Integer getId() {
        return prodProdutoId;
    }

    @Override
    public void setId(Integer id) {
        prodProdutoId = id;
    }

    // GETs e SETs
    public Integer getProdProdutoId() {
        return prodProdutoId;
    }

    public void setProdProdutoId(Integer prodProdutoId) {
        this.prodProdutoId = prodProdutoId;
    }

    public String getProdProdutoNcm() {
        return prodProdutoNcm;
    }

    public void setProdProdutoNcm(String prodProdutoNcm) {
        this.prodProdutoNcm = prodProdutoNcm;
    }

    public String getProdProdutoBarra() {
        return prodProdutoBarra;
    }

    public void setProdProdutoBarra(String prodProdutoBarra) {
        this.prodProdutoBarra = prodProdutoBarra;
    }

    public String getProdProdutoDescricao() {
        return prodProdutoDescricao;
    }

    public void setProdProdutoDescricao(String prodProdutoDescricao) {
        this.prodProdutoDescricao = prodProdutoDescricao;
    }

    public String getProdProdutoReferencia() {
        return prodProdutoReferencia;
    }

    public void setProdProdutoReferencia(String prodProdutoReferencia) {
        this.prodProdutoReferencia = prodProdutoReferencia;
    }

    public Double getProdProdutoPreco() {
        return prodProdutoPreco;
    }

    public void setProdProdutoPreco(Double prodProdutoPreco) {
        this.prodProdutoPreco = prodProdutoPreco;
    }

    public Double getProdProdutoEstoque() {
        return prodProdutoEstoque;
    }

    public void setProdProdutoEstoque(Double prodProdutoEstoque) {
        this.prodProdutoEstoque = prodProdutoEstoque;
    }

    public String getProdProdutoTipo() {
        return prodProdutoTipo;
    }

    public void setProdProdutoTipo(String prodProdutoTipo) {
        this.prodProdutoTipo = prodProdutoTipo;
    }

    public char getProdProdutoOrigem() {
        return prodProdutoOrigem;
    }

    public void setProdProdutoOrigem(char prodProdutoOrigem) {
        this.prodProdutoOrigem = prodProdutoOrigem;
    }

    public String getProdProdutoCstCson() {
        return prodProdutoCstCson;
    }

    public void setProdProdutoCstCson(String prodProdutoCstCson) {
        this.prodProdutoCstCson = prodProdutoCstCson;
    }

    public char getProdProdutoTributacao() {
        return prodProdutoTributacao;
    }

    public void setProdProdutoTributacao(char prodProdutoTributacao) {
        this.prodProdutoTributacao = prodProdutoTributacao;
    }

    public Double getProdProdutoIcms() {
        return prodProdutoIcms;
    }

    public void setProdProdutoIcms(Double prodProdutoIcms) {
        this.prodProdutoIcms = prodProdutoIcms;
    }

    public Double getProdProdutoIssqn() {
        return prodProdutoIssqn;
    }

    public void setProdProdutoIssqn(Double prodProdutoIssqn) {
        this.prodProdutoIssqn = prodProdutoIssqn;
    }

    public char getProdProdutoIat() {
        return prodProdutoIat;
    }

    public void setProdProdutoIat(char prodProdutoIat) {
        this.prodProdutoIat = prodProdutoIat;
    }

    public char getProdProdutoIppt() {
        return prodProdutoIppt;
    }

    public void setProdProdutoIppt(char prodProdutoIppt) {
        this.prodProdutoIppt = prodProdutoIppt;
    }

    public Date getProdProdutoCadastrado() {
        return prodProdutoCadastrado;
    }

    public void setProdProdutoCadastrado(Date prodProdutoCadastrado) {
        this.prodProdutoCadastrado = prodProdutoCadastrado;
    }

    public Date getProdProdutoAlterado() {
        return prodProdutoAlterado;
    }

    public void setProdProdutoAlterado(Date prodProdutoAlterado) {
        this.prodProdutoAlterado = prodProdutoAlterado;
    }

    public boolean getProdProdutoAtivo() {
        return prodProdutoAtivo;
    }

    public void setProdProdutoAtivo(boolean prodProdutoAtivo) {
        this.prodProdutoAtivo = prodProdutoAtivo;
    }

    public List<ProdComposicao> getProdComposicoes() {
        return prodComposicoes;
    }

    public void setProdComposicoes(List<ProdComposicao> prodComposicoes) {
        this.prodComposicoes = prodComposicoes;
    }

    public ProdEmbalagem getProdEmbalagem() {
        return prodEmbalagem;
    }

    public void setProdEmbalagem(ProdEmbalagem prodEmbalagem) {
        this.prodEmbalagem = prodEmbalagem;
    }

    public List<ProdPreco> getProdPrecos() {
        return prodPrecos;
    }

    public void setProdPrecos(List<ProdPreco> prodPrecos) {
        this.prodPrecos = prodPrecos;
    }

    public List<ProdGrade> getProdGrades() {
        return prodGrades;
    }

    public void setProdGrades(List<ProdGrade> prodGrades) {
        this.prodGrades = prodGrades;
    }
}
