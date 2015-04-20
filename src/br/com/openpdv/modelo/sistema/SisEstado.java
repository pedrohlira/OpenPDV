package br.com.openpdv.modelo.sistema;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modelo que representa a UF no sistema.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "sis_estado")
@XmlRootElement
public class SisEstado extends Dados implements Serializable {

    @Id
    @Column(name = "sis_estado_id")
    private Integer sisEstadoId;
    @Column(name = "sis_estado_ibge")
    private int sisEstadoIbge;
    @Column(name = "sis_estado_descricao")
    private String sisEstadoDescricao;
    @Column(name = "sis_estado_sigla")
    private String sisEstadoSigla;

    /**
     * Construtor padrao
     */
    public SisEstado() {
        this(0);
    }

    /**
     * Construtor padrao passando o id
     *
     * @param sisEstadoId o id do estado.
     */
    public SisEstado(Integer sisEstadoId) {
        super("SisEstado", "sisEstadoId", "sisEstadoId");
        this.sisEstadoId = sisEstadoId;
    }

    @Override
    public Integer getId() {
        return this.sisEstadoId;
    }

    @Override
    public void setId(Integer id) {
        this.sisEstadoId = id;
    }

    // GETs e SETs

    public Integer getSisEstadoId() {
        return sisEstadoId;
    }

    public void setSisEstadoId(Integer sisEstadoId) {
        this.sisEstadoId = sisEstadoId;
    }

    public int getSisEstadoIbge() {
        return sisEstadoIbge;
    }

    public void setSisEstadoIbge(int sisEstadoIbge) {
        this.sisEstadoIbge = sisEstadoIbge;
    }

    public String getSisEstadoDescricao() {
        return sisEstadoDescricao;
    }

    public void setSisEstadoDescricao(String sisEstadoDescricao) {
        this.sisEstadoDescricao = sisEstadoDescricao;
    }

    public String getSisEstadoSigla() {
        return sisEstadoSigla;
    }

    public void setSisEstadoSigla(String sisEstadoSigla) {
        this.sisEstadoSigla = sisEstadoSigla;
    }

    @Override
    public String toString() {
        return this.sisEstadoSigla;
    }
    
}
