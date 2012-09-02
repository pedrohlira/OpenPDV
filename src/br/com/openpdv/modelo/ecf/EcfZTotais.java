package br.com.openpdv.modelo.ecf;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

/**
 * Classe que representa os totais da leitura Z no sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_z_totais")
@XmlRootElement
public class EcfZTotais extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecf_z_totais_id")
    private Integer ecfZTotaisId;
    @Column(name = "ecf_z_totais_codigo")
    private String ecfZTotaisCodigo;
    @Column(name = "ecf_z_totais_valor")
    private Double ecfZTotaisValor;
    @ManyToOne
    @JoinColumn(name = "ecf_z_id")
    @XmlInverseReference(mappedBy = "ecfZTotais")
    private EcfZ ecfZ;

    /**
     * Construtor padrao
     */
    public EcfZTotais() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfZTotaisId o id do registro.
     */
    public EcfZTotais(Integer ecfZTotaisId) {
        super("EcfZTotais", "ecfZTotaisId", "ecfZTotaisId");
        this.ecfZTotaisId = ecfZTotaisId;
    }

    @Override
    public Integer getId() {
        return ecfZTotaisId;
    }

    @Override
    public void setId(Integer id) {
        ecfZTotaisId = id;
    }

    public Integer getEcfZTotaisId() {
        return ecfZTotaisId;
    }

    public void setEcfZTotaisId(Integer ecfZTotaisId) {
        this.ecfZTotaisId = ecfZTotaisId;
    }

    public String getEcfZTotaisCodigo() {
        return ecfZTotaisCodigo;
    }

    public void setEcfZTotaisCodigo(String ecfZTotaisCodigo) {
        this.ecfZTotaisCodigo = ecfZTotaisCodigo;
    }

    public Double getEcfZTotaisValor() {
        return ecfZTotaisValor;
    }

    public void setEcfZTotaisValor(Double ecfZTotaisValor) {
        this.ecfZTotaisValor = ecfZTotaisValor;
    }

    public EcfZ getEcfZ() {
        return ecfZ;
    }

    public void setEcfZ(EcfZ ecfZ) {
        this.ecfZ = ecfZ;
    }
}
