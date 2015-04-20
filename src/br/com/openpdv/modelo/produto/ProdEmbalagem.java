package br.com.openpdv.modelo.produto;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.core.ELetra;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que representa as embalagens do sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_embalagem")
@XmlRootElement
public class ProdEmbalagem extends Dados implements Serializable {

    @Id
    @Column(name = "prod_embalagem_id")
    private Integer prodEmbalagemId;
    @Column(name = "prod_embalagem_nome")
    private String prodEmbalagemNome;
    @Column(name = "prod_embalagem_unidade")
    private int prodEmbalagemUnidade;
    @Column(name = "prod_embalagem_descricao")
    private String prodEmbalagemDescricao;

    /**
     * Construtor padrao
     */
    public ProdEmbalagem() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param prodEmbalagemId o id do registro.
     */
    public ProdEmbalagem(Integer prodEmbalagemId) {
        super("ProdEmbalagem", "prodEmbalagemId", "prodEmbalagemId");
        this.prodEmbalagemId = prodEmbalagemId;
        this.setTipoLetra(ELetra.GRANDE);
    }

    @Override
    public Integer getId() {
        return prodEmbalagemId;
    }

    @Override
    public void setId(Integer id) {
        prodEmbalagemId = id;
    }

    // GETs e SETs
    public Integer getProdEmbalagemId() {
        return this.prodEmbalagemId;
    }

    public void setProdEmbalagemId(Integer prodEmbalagemId) {
        this.prodEmbalagemId = prodEmbalagemId;
    }

    public String getProdEmbalagemNome() {
        return prodEmbalagemNome;
    }

    public void setProdEmbalagemNome(String prodEmbalagemNome) {
        this.prodEmbalagemNome = prodEmbalagemNome;
    }

    public int getProdEmbalagemUnidade() {
        return prodEmbalagemUnidade;
    }

    public void setProdEmbalagemUnidade(int prodEmbalagemUnidade) {
        this.prodEmbalagemUnidade = prodEmbalagemUnidade;
    }

    public String getProdEmbalagemDescricao() {
        return this.prodEmbalagemDescricao;
    }

    public void setProdEmbalagemDescricao(String prodEmbalagemDescricao) {
        this.prodEmbalagemDescricao = prodEmbalagemDescricao;
    }
}
