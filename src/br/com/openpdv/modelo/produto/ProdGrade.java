package br.com.openpdv.modelo.produto;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.core.ELetra;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

/**
 * Classe que representa uma grade no sistema.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_grade")
@XmlRootElement
public class ProdGrade extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_grade_id")
    private Integer prodGradeId;
    @Column(name = "prod_grade_barra")
    private String prodGradeBarra;
    @Column(name = "prod_grade_tamanho")
    private String prodGradeTamanho;
    @Column(name = "prod_grade_cor")
    private String prodGradeCor;
    @Column(name = "prod_grade_opcao")
    private String prodGradeOpcao;
    @Column(name = "prod_grade_estoque")
    private Double prodGradeEstoque;
    @ManyToOne
    @JoinColumn(name = "prod_produto_id")
    @XmlInverseReference(mappedBy = "prodGrades")
    private ProdProduto prodProduto;

    public ProdGrade() {
        this(0);
    }

    public ProdGrade(int prodGradeId) {
        super("ProdGrade", "prodGradeId", "prodGradeId");
        this.prodGradeId = prodGradeId;
        this.setTipoLetra(ELetra.GRANDE);
    }

    @Override
    public Integer getId() {
        return prodGradeId;
    }

    @Override
    public void setId(Integer id) {
        this.prodGradeId = id;
    }

    public int getProdGradeId() {
        return prodGradeId;
    }

    public void setProdGradeId(int prodGradeId) {
        this.prodGradeId = prodGradeId;
    }

    public String getProdGradeBarra() {
        return prodGradeBarra;
    }

    public void setProdGradeBarra(String prodGradeBarra) {
        this.prodGradeBarra = prodGradeBarra;
    }

    public String getProdGradeTamanho() {
        return prodGradeTamanho;
    }

    public void setProdGradeTamanho(String prodGradeTamanho) {
        this.prodGradeTamanho = prodGradeTamanho;
    }

    public String getProdGradeCor() {
        return prodGradeCor;
    }

    public void setProdGradeCor(String prodGradeCor) {
        this.prodGradeCor = prodGradeCor;
    }

    public String getProdGradeOpcao() {
        return prodGradeOpcao;
    }

    public void setProdGradeOpcao(String prodGradeOpcao) {
        this.prodGradeOpcao = prodGradeOpcao;
    }

    public ProdProduto getProdProduto() {
        return prodProduto;
    }

    public void setProdProduto(ProdProduto prodProduto) {
        this.prodProduto = prodProduto;
    }

    public Double getProdGradeEstoque() {
        return prodGradeEstoque;
    }

    public void setProdGradeEstoque(Double prodGradeEstoque) {
        this.prodGradeEstoque = prodGradeEstoque;
    }
}