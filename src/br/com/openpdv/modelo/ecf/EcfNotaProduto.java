package br.com.openpdv.modelo.ecf;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdProduto;
import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

/**
 * Classe que representa os itens da nota de consumidor no sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_nota_produto")
@XmlRootElement
public class EcfNotaProduto extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecf_nota_produto_id")
    private Integer ecfNotaProdutoId;
    @Column(name = "ecf_nota_produto_quantidade")
    private Double ecfNotaProdutoQuantidade;
    @Column(name = "ecf_nota_produto_bruto")
    private Double ecfNotaProdutoBruto;
    @Column(name = "ecf_nota_produto_desconto")
    private Double ecfNotaProdutoDesconto;
    @Column(name = "ecf_nota_produto_liquido")
    private Double ecfNotaProdutoLiquido;
    @Column(name = "ecf_nota_produto_icms")
    private Double ecfNotaProdutoIcms;
    @Column(name = "ecf_nota_produto_ipi")
    private Double ecfNotaProdutoIpi;
    @Column(name = "ecf_nota_produto_ordem")
    private int ecfNotaProdutoOrdem;
    @ManyToOne
    @JoinColumn(name = "ecf_nota_id")
    @XmlInverseReference(mappedBy = "ecfNotaProdutos")
    private EcfNota ecfNota;
    @ManyToOne
    @JoinColumn(name = "prod_produto_id")
    private ProdProduto prodProduto;
    @ManyToOne
    @JoinColumn(name = "prod_embalagem_id")
    private ProdEmbalagem prodEmbalagem;

    /**
     * Construtor padrao
     */
    public EcfNotaProduto() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfNotaProdutoId o id do registro.
     */
    public EcfNotaProduto(Integer ecfNotaProdutoId) {
        super("EcfNotaProduto", "ecfNotaProdutoId", "ecfNotaProdutoOrdem");
        this.ecfNotaProdutoId = ecfNotaProdutoId;
    }

    @Override
    public Integer getId() {
        return ecfNotaProdutoId;
    }

    @Override
    public void setId(Integer id) {
        ecfNotaProdutoId = id;
    }

    public Integer getEcfNotaProdutoId() {
        return ecfNotaProdutoId;
    }

    public void setEcfNotaProdutoId(Integer ecfNotaProdutoId) {
        this.ecfNotaProdutoId = ecfNotaProdutoId;
    }

    public Double getEcfNotaProdutoQuantidade() {
        return ecfNotaProdutoQuantidade;
    }

    public void setEcfNotaProdutoQuantidade(Double ecfNotaProdutoQuantidade) {
        this.ecfNotaProdutoQuantidade = ecfNotaProdutoQuantidade;
    }

    public Double getEcfNotaProdutoBruto() {
        return ecfNotaProdutoBruto;
    }

    public void setEcfNotaProdutoBruto(Double ecfNotaProdutoBruto) {
        this.ecfNotaProdutoBruto = ecfNotaProdutoBruto;
    }

    public Double getEcfNotaProdutoDesconto() {
        return ecfNotaProdutoDesconto;
    }

    public void setEcfNotaProdutoDesconto(Double ecfNotaProdutoDesconto) {
        this.ecfNotaProdutoDesconto = ecfNotaProdutoDesconto;
    }

    public Double getEcfNotaProdutoLiquido() {
        return ecfNotaProdutoLiquido;
    }

    public void setEcfNotaProdutoLiquido(Double ecfNotaProdutoLiquido) {
        this.ecfNotaProdutoLiquido = ecfNotaProdutoLiquido;
    }

    public Double getEcfNotaProdutoIcms() {
        return ecfNotaProdutoIcms;
    }

    public void setEcfNotaProdutoIcms(Double ecfNotaProdutoIcms) {
        this.ecfNotaProdutoIcms = ecfNotaProdutoIcms;
    }

    public Double getEcfNotaProdutoIpi() {
        return ecfNotaProdutoIpi;
    }

    public void setEcfNotaProdutoIpi(Double ecfNotaProdutoIpi) {
        this.ecfNotaProdutoIpi = ecfNotaProdutoIpi;
    }

    public int getEcfNotaProdutoOrdem() {
        return ecfNotaProdutoOrdem;
    }

    public void setEcfNotaProdutoOrdem(int ecfNotaProdutoOrdem) {
        this.ecfNotaProdutoOrdem = ecfNotaProdutoOrdem;
    }

    public EcfNota getEcfNota() {
        return ecfNota;
    }

    public void setEcfNota(EcfNota ecfNota) {
        this.ecfNota = ecfNota;
    }

    public ProdEmbalagem getProdEmbalagem() {
        return prodEmbalagem;
    }

    public void setProdEmbalagem(ProdEmbalagem prodEmbalagem) {
        this.prodEmbalagem = prodEmbalagem;
    }

    public ProdProduto getProdProduto() {
        return prodProduto;
    }

    public void setProdProduto(ProdProduto prodProduto) {
        this.prodProduto = prodProduto;
    }

}
