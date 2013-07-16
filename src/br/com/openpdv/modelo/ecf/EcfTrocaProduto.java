package br.com.openpdv.modelo.ecf;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdProduto;
import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

/**
 * Classe que representa os itens da troca do consumidor no sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_troca_produto")
@XmlRootElement
public class EcfTrocaProduto extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecf_troca_produto_id")
    private Integer ecfTrocaProdutoId;
    @Column(name = "ecf_troca_produto_barra")
    private String ecfTrocaProdutoBarra;
    @Column(name = "ecf_troca_produto_quantidade")
    private Double ecfTrocaProdutoQuantidade;
    @Column(name = "ecf_troca_produto_valor")
    private Double ecfTrocaProdutoValor;
    @Column(name = "ecf_troca_produto_total")
    private Double ecfTrocaProdutoTotal;
    @Column(name = "ecf_troca_produto_ordem")
    private int ecfTrocaProdutoOrdem;
    @ManyToOne
    @JoinColumn(name = "ecf_troca_id")
    @XmlInverseReference(mappedBy = "ecfTrocaProdutos")
    private EcfTroca ecfTroca;
    @ManyToOne
    @JoinColumn(name = "prod_produto_id")
    private ProdProduto prodProduto;
    @ManyToOne
    @JoinColumn(name = "prod_embalagem_id")
    private ProdEmbalagem prodEmbalagem;

    /**
     * Construtor padrao
     */
    public EcfTrocaProduto() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfTrocaProdutoId o id do registro.
     */
    public EcfTrocaProduto(Integer ecfTrocaProdutoId) {
        super("EcfTrocaProduto", "ecfTrocaProdutoId", "ecfTrocaProdutoOrdem");
        this.ecfTrocaProdutoId = ecfTrocaProdutoId;
    }

    @Override
    public Integer getId() {
        return ecfTrocaProdutoId;
    }

    @Override
    public void setId(Integer id) {
        ecfTrocaProdutoId = id;
    }

    public Integer getEcfTrocaProdutoId() {
        return ecfTrocaProdutoId;
    }

    public void setEcfTrocaProdutoId(Integer ecfTrocaProdutoId) {
        this.ecfTrocaProdutoId = ecfTrocaProdutoId;
    }

    public String getEcfTrocaProdutoBarra() {
        return ecfTrocaProdutoBarra;
    }

    public void setEcfTrocaProdutoBarra(String ecfTrocaProdutoBarra) {
        this.ecfTrocaProdutoBarra = ecfTrocaProdutoBarra;
    }

    public Double getEcfTrocaProdutoQuantidade() {
        return ecfTrocaProdutoQuantidade;
    }

    public void setEcfTrocaProdutoQuantidade(Double ecfTrocaProdutoQuantidade) {
        this.ecfTrocaProdutoQuantidade = ecfTrocaProdutoQuantidade;
    }

    public Double getEcfTrocaProdutoValor() {
        return ecfTrocaProdutoValor;
    }

    public void setEcfTrocaProdutoValor(Double ecfTrocaProdutoValor) {
        this.ecfTrocaProdutoValor = ecfTrocaProdutoValor;
    }

    public Double getEcfTrocaProdutoTotal() {
        return ecfTrocaProdutoTotal;
    }

    public void setEcfTrocaProdutoTotal(Double ecfTrocaProdutoTotal) {
        this.ecfTrocaProdutoTotal = ecfTrocaProdutoTotal;
    }

    public int getEcfTrocaProdutoOrdem() {
        return ecfTrocaProdutoOrdem;
    }

    public void setEcfTrocaProdutoOrdem(int ecfTrocaProdutoOrdem) {
        this.ecfTrocaProdutoOrdem = ecfTrocaProdutoOrdem;
    }

    public EcfTroca getEcfTroca() {
        return ecfTroca;
    }

    public void setEcfTroca(EcfTroca ecfTroca) {
        this.ecfTroca = ecfTroca;
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
}
