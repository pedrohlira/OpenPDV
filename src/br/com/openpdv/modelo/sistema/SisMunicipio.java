package br.com.openpdv.modelo.sistema;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modelo que representa o municipio no sistema.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "sis_municipio")
@XmlRootElement
public class SisMunicipio extends Dados implements Serializable {

    @Id
    @Column(name = "sis_municipio_id")
    private Integer sisMunicipioId;
    @Column(name = "sis_municipio_ibge")
    private int sisMunicipioIbge;
    @Column(name = "sis_municipio_descricao")
    private String sisMunicipioDescricao;
    @JoinColumn(name = "sis_estado_id", referencedColumnName = "sis_estado_id")
    @ManyToOne
    private SisEstado sisEstado;

    /**
     * Construtor padrao
     */
    public SisMunicipio() {
        this(0);
    }

    /**
     * Construtor padrao passando o id
     *
     * @param sisMunicipioId o id do municipio.
     */
    public SisMunicipio(Integer sisMunicipioId) {
        super("SisMunicipio", "sisMunicipioId", "sisMunicipioDescricao");
        this.sisMunicipioId = sisMunicipioId;
    }

    @Override
    public Integer getId() {
        return this.sisMunicipioId;
    }

    @Override
    public void setId(Integer id) {
        this.sisMunicipioId = id;
    }

    // GETs e SETs
    public Integer getSisMunicipioId() {
        return sisMunicipioId;
    }

    public void setSisMunicipioId(Integer sisMunicipioId) {
        this.sisMunicipioId = sisMunicipioId;
    }

    public int getSisMunicipioIbge() {
        return sisMunicipioIbge;
    }

    public void setSisMunicipioIbge(int sisMunicipioIbge) {
        this.sisMunicipioIbge = sisMunicipioIbge;
    }

    public String getSisMunicipioDescricao() {
        return sisMunicipioDescricao;
    }

    public void setSisMunicipioDescricao(String sisMunicipioDescricao) {
        this.sisMunicipioDescricao = sisMunicipioDescricao;
    }

    public SisEstado getSisEstado() {
        return sisEstado;
    }

    public void setSisEstado(SisEstado sisEstado) {
        this.sisEstado = sisEstado;
    }
}
