package br.com.openpdv.modelo.ecf;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que representa um documento impresso pelo sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_documento")
@XmlRootElement
public class EcfDocumento extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecf_documento_id")
    private Integer ecfDocumentoId;
    @Column(name = "ecf_documento_usuario")
    private int ecfDocumentoUsuario;
    @Column(name = "ecf_documento_coo")
    private int ecfDocumentoCoo;
    @Column(name = "ecf_documento_gnf")
    private int ecfDocumentoGnf;
    @Column(name = "ecf_documento_grg")
    private int ecfDocumentoGrg;
    @Column(name = "ecf_documento_cdc")
    private int ecfDocumentoCdc;
    @Column(name = "ecf_documento_tipo")
    private String ecfDocumentoTipo;
    @Column(name = "ecf_documento_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ecfDocumentoData;
    @JoinColumn(name = "ecf_impressora_id")
    @ManyToOne
    private EcfImpressora ecfImpressora;

    /**
     * Construtor padrao
     */
    public EcfDocumento() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfDocumentoId o id do registro.
     */
    public EcfDocumento(Integer ecfDocumentoId) {
        super("EcfDocumento", "ecfDocumentoId", "ecfDocumentoData");
        this.ecfDocumentoId = ecfDocumentoId;
    }

    @Override
    public Integer getId() {
        return ecfDocumentoId;
    }

    @Override
    public void setId(Integer id) {
        ecfDocumentoId = id;
    }

    public Integer getEcfDocumentoId() {
        return ecfDocumentoId;
    }

    public void setEcfDocumentoId(Integer ecfDocumentoId) {
        this.ecfDocumentoId = ecfDocumentoId;
    }

    public int getEcfDocumentoUsuario() {
        return ecfDocumentoUsuario;
    }

    public void setEcfDocumentoUsuario(int ecfDocumentoUsuario) {
        this.ecfDocumentoUsuario = ecfDocumentoUsuario;
    }

    public int getEcfDocumentoCoo() {
        return ecfDocumentoCoo;
    }

    public void setEcfDocumentoCoo(int ecfDocumentoCoo) {
        this.ecfDocumentoCoo = ecfDocumentoCoo;
    }

    public int getEcfDocumentoGnf() {
        return ecfDocumentoGnf;
    }

    public void setEcfDocumentoGnf(int ecfDocumentoGnf) {
        this.ecfDocumentoGnf = ecfDocumentoGnf;
    }

    public int getEcfDocumentoGrg() {
        return ecfDocumentoGrg;
    }

    public void setEcfDocumentoGrg(int ecfDocumentoGrg) {
        this.ecfDocumentoGrg = ecfDocumentoGrg;
    }

    public int getEcfDocumentoCdc() {
        return ecfDocumentoCdc;
    }

    public void setEcfDocumentoCdc(int ecfDocumentoCdc) {
        this.ecfDocumentoCdc = ecfDocumentoCdc;
    }

    public String getEcfDocumentoTipo() {
        return ecfDocumentoTipo;
    }

    public void setEcfDocumentoTipo(String ecfDocumentoTipo) {
        this.ecfDocumentoTipo = ecfDocumentoTipo;
    }

    public Date getEcfDocumentoData() {
        return ecfDocumentoData;
    }

    public void setEcfDocumentoData(Date ecfDocumentoData) {
        this.ecfDocumentoData = ecfDocumentoData;
    }

    public EcfImpressora getEcfImpressora() {
        return ecfImpressora;
    }

    public void setEcfImpressora(EcfImpressora ecfImpressora) {
        this.ecfImpressora = ecfImpressora;
    }
}
