package br.com.openpdv.modelo.produto;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

/**
 * Classe que representa uma composicao de produtos no sistema.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_composicao")
@XmlRootElement
public class ProdComposicao extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_composicao_id")
    private Integer prodComposicaoId;
    @Column(name = "prod_composicao_quantidade")
    private Double prodComposicaoQuantidade;
    @Column(name = "prod_composicao_valor")
    private Double prodComposicaoValor;
    @ManyToOne
    @JoinColumn(name = "prod_produto_principal")
    @XmlInverseReference(mappedBy = "prodComposicoes")
    private ProdProduto prodProdutoPrincipal;
    @ManyToOne
    @JoinColumn(name = "prod_produto_id")
    private ProdProduto prodProduto;
    @ManyToOne
    @JoinColumn(name = "prod_embalagem_id")
    private ProdEmbalagem prodEmbalagem;

    /**
     * Construtor padrao
     */
    public ProdComposicao() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param prodComposicaoId o id do registro.
     */
    public ProdComposicao(Integer prodComposicaoId) {
        super("ProdComposicao", "prodComposicaoId", "prodComposicaoValor");
        this.prodComposicaoId = prodComposicaoId;
    }

    @Override
    public Integer getId() {
        return prodComposicaoId;
    }

    @Override
    public void setId(Integer id) {
        prodComposicaoId = id;
    }

    // GETs e SETs
    public Integer getProdComposicaoId() {
        return prodComposicaoId;
    }

    public void setProdComposicaoId(Integer prodComposicaoId) {
        this.prodComposicaoId = prodComposicaoId;
    }

    public Double getProdComposicaoQuantidade() {
        return prodComposicaoQuantidade;
    }

    public void setProdComposicaoQuantidade(Double prodComposicaoQuantidade) {
        this.prodComposicaoQuantidade = prodComposicaoQuantidade;
    }

    public Double getProdComposicaoValor() {
        return prodComposicaoValor;
    }

    public void setProdComposicaoValor(Double prodComposicaoValor) {
        this.prodComposicaoValor = prodComposicaoValor;
    }

    public ProdProduto getProdProdutoPrincipal() {
        return prodProdutoPrincipal;
    }

    public void setProdProdutoPrincipal(ProdProduto prodProdutoPrincipal) {
        this.prodProdutoPrincipal = prodProdutoPrincipal;
    }

    public ProdProduto getProdProduto() {
        return this.prodProduto;
    }

    public void setProdProduto(ProdProduto prodProduto) {
        this.prodProduto = prodProduto;
    }

    public ProdEmbalagem getProdEmbalagem() {
        return this.prodEmbalagem;
    }

    public void setProdEmbalagem(ProdEmbalagem prodEmbalagem) {
        this.prodEmbalagem = prodEmbalagem;
    }
}