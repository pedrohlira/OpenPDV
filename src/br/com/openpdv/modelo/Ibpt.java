package br.com.openpdv.modelo;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe que representa a aliquotas de impostos aproximados do sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ibpt")
public class Ibpt extends Dados implements Serializable {

    @Id
    @Column(name = "ibpt_codigo")
    private String ibptCodigo;
    @Column(name = "ibpt_ex")
    private String ibptEx;
    @Column(name = "ibpt_tabela")
    private int ibptTabela;
    @Column(name = "ibpt_descricao")
    private String ibptDescricao;
    @Column(name = "ibpt_aliqNac")
    private Double ibptAliqNac;
    @Column(name = "ibpt_aliqImp")
    private Double ibptAliqImp;
    @Column(name = "ibpt_versao")
    private String ibptVersao;

    /**
     * Construtor padrao
     */
    public Ibpt() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ibptId o id do registro.
     */
    public Ibpt(Integer ibptId) {
        super("Ibpt", "ibptCodigo", "ibptCodigo");
    }

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public void setId(Integer id) {
    }

    // GETs e SETs
    public String getIbptCodigo() {
        return ibptCodigo;
    }

    public void setIbptCodigo(String ibptCodigo) {
        this.ibptCodigo = ibptCodigo;
    }

    public String getIbptEx() {
        return ibptEx;
    }

    public void setIbptEx(String ibptEx) {
        this.ibptEx = ibptEx;
    }

    public int getIbptTabela() {
        return ibptTabela;
    }

    public void setIbptTabela(int ibptTabela) {
        this.ibptTabela = ibptTabela;
    }

    public String getIbptDescricao() {
        return ibptDescricao;
    }

    public void setIbptDescricao(String ibptDescricao) {
        this.ibptDescricao = ibptDescricao;
    }

    public Double getIbptAliqNac() {
        return ibptAliqNac;
    }

    public void setIbptAliqNac(Double ibptAliqNac) {
        this.ibptAliqNac = ibptAliqNac;
    }

    public Double getIbptAliqImp() {
        return ibptAliqImp;
    }

    public void setIbptAliqImp(Double ibptAliqImp) {
        this.ibptAliqImp = ibptAliqImp;
    }

    public String getIbptVersao() {
        return ibptVersao;
    }

    public void setIbptVersao(String ibptVersao) {
        this.ibptVersao = ibptVersao;
    }
}
