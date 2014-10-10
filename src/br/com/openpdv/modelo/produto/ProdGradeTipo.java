package br.com.openpdv.modelo.produto;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.core.ELetra;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que representa um sub tipo de produto no sistema.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_grade_tipo")
@XmlRootElement
public class ProdGradeTipo extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_grade_tipo_id")
    private Integer prodGradeTipoId;
    @Column(name = "prod_grade_tipo_nome")
    private String prodGradeTipoNome;
    @Column(name = "prod_grade_tipo_opcao")
    private char prodGradeTipoOpcao;

    public ProdGradeTipo() {
        this(0);
    }

    public ProdGradeTipo(int prodGradeTipoId) {
        super("ProdGradeTipo", "prodGradeTipoId", "prodGradeTipoNome");
        this.prodGradeTipoId = prodGradeTipoId;
        this.setTipoLetra(ELetra.GRANDE);
    }

    @Override
    public Integer getId() {
        return prodGradeTipoId;
    }

    @Override
    public void setId(Integer id) {
        prodGradeTipoId = id;
    }

    public int getProdGradeTipoId() {
        return prodGradeTipoId;
    }

    public void setProdGradeTipoId(int prodGradeTipoId) {
        this.prodGradeTipoId = prodGradeTipoId;
    }

    public String getProdGradeTipoNome() {
        return prodGradeTipoNome;
    }

    public void setProdGradeTipoNome(String prodGradeTipoNome) {
        this.prodGradeTipoNome = prodGradeTipoNome;
    }

    public char getProdGradeTipoOpcao() {
        return prodGradeTipoOpcao;
    }

    public void setProdGradeTipoOpcao(char prodGradeTipoOpcao) {
        this.prodGradeTipoOpcao = prodGradeTipoOpcao;
    }
}
