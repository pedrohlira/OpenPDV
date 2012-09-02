package br.com.openpdv.modelo.produto;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

/**
 * Classe que representa os precos adicionais de produtos do sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_preco")
@XmlRootElement
public class ProdPreco extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_preco_id")
    private Integer prodPrecoId;
    @Column(name = "prod_preco_valor")
    private Double prodPrecoValor;
    @Column(name = "prod_preco_barra")
    private String prodPrecoBarra;
    @ManyToOne
    @JoinColumn(name = "prod_produto_id")
    @XmlInverseReference(mappedBy = "prodPrecos")
    private ProdProduto prodProduto;
    @ManyToOne
    @JoinColumn(name = "prod_embalagem_id")
    private ProdEmbalagem prodEmbalagem;

    /**
     * Construtor padrao
     */
    public ProdPreco() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param prodPrecoId o id do registro.
     */
    public ProdPreco(Integer prodPrecoId) {
        super("ProdPreco", "prodPrecoId", "prodPrecoId");
        this.prodPrecoId = prodPrecoId;
    }

    @Override
    public Integer getId() {
        return prodPrecoId;
    }

    @Override
    public void setId(Integer id) {
        prodPrecoId = id;
    }

    // GETs e SETs
    public Integer getProdPrecoId() {
        return this.prodPrecoId;
    }

    public void setProdPrecoId(Integer prodPrecoId) {
        this.prodPrecoId = prodPrecoId;
    }

    public Double getProdPrecoValor() {
        return this.prodPrecoValor;
    }

    public void setProdPrecoValor(Double prodPrecoValor) {
        this.prodPrecoValor = prodPrecoValor;
    }

    public String getProdPrecoBarra() {
        return prodPrecoBarra;
    }

    public void setProdPrecoBarra(String prodPrecoBarra) {
        this.prodPrecoBarra = prodPrecoBarra;
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
