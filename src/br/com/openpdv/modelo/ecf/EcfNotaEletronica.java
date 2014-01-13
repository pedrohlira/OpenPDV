package br.com.openpdv.modelo.ecf;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.core.EDirecao;
import br.com.openpdv.modelo.sistema.SisCliente;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que representa uma nota eletronica no sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_nota_eletronica")
@XmlRootElement
public class EcfNotaEletronica extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecf_nota_eletronica_id")
    private Integer ecfNotaEletronicaId;
    @Column(name = "ecf_nota_eletronica_status")
    private String ecfNotaEletronicaStatus;
    @Column(name = "ecf_nota_eletronica_numero")
    private int ecfNotaEletronicaNumero;
    @Column(name = "ecf_nota_eletronica_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ecfNotaEletronicaData;
    @Column(name = "ecf_nota_eletronica_valor")
    private Double ecfNotaEletronicaValor;
    @Column(name = "ecf_nota_eletronica_chave")
    private String ecfNotaEletronicaChave;
    @Column(name = "ecf_nota_eletronica_protocolo")
    private String ecfNotaEletronicaProtocolo;
    @Column(name = "ecf_nota_eletronica_icms")
    private Double ecfNotaEletronicaIcms;
    @Column(name = "ecf_nota_eletronica_ipi")
    private Double ecfNotaEletronicaIpi;
    @Column(name = "ecf_nota_eletronica_pis")
    private Double ecfNotaEletronicaPis;
    @Column(name = "ecf_nota_eletronica_cofins")
    private Double ecfNotaEletronicaCofins;
    @Lob
    @Column(name = "ecf_nota_eletronica_xml")
    private String ecfNotaEletronicaXml;
    @Column(name = "ecf_nota_eletronica_protocolo_cancelado")
    private String ecfNotaEletronicaProtocoloCancelado;
    @Lob
    @Column(name = "ecf_nota_eletronica_xml_cancelado")
    private String ecfNotaEletronicaXmlCancelado;
    @Column(name = "ecf_nota_eletronica_recibo")
    private String ecfNotaEletronicaRecibo;
    @JoinColumn(name = "sis_cliente_id")
    @ManyToOne
    private SisCliente sisCliente;

    /**
     * Construtor padrao
     */
    public EcfNotaEletronica() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfNotaEletronicaId o id do registro.
     */
    public EcfNotaEletronica(Integer ecfNotaEletronicaId) {
        super("EcfNotaEletronica", "ecfNotaEletronicaId", "ecfNotaEletronicaData", EDirecao.DESC);
        this.ecfNotaEletronicaId = ecfNotaEletronicaId;
    }

    @Override
    public Integer getId() {
        return ecfNotaEletronicaId;
    }

    @Override
    public void setId(Integer id) {
        ecfNotaEletronicaId = id;
    }

    public Integer getEcfNotaEletronicaId() {
        return ecfNotaEletronicaId;
    }

    public void setEcfNotaEletronicaId(Integer ecfNotaEletronicaId) {
        this.ecfNotaEletronicaId = ecfNotaEletronicaId;
    }

    public String getEcfNotaEletronicaStatus() {
        return ecfNotaEletronicaStatus;
    }

    public void setEcfNotaEletronicaStatus(String ecfNotaEletronicaStatus) {
        this.ecfNotaEletronicaStatus = ecfNotaEletronicaStatus;
    }

    public int getEcfNotaEletronicaNumero() {
        return ecfNotaEletronicaNumero;
    }

    public void setEcfNotaEletronicaNumero(int ecfNotaEletronicaNumero) {
        this.ecfNotaEletronicaNumero = ecfNotaEletronicaNumero;
    }

    public Date getEcfNotaEletronicaData() {
        return ecfNotaEletronicaData;
    }

    public void setEcfNotaEletronicaData(Date ecfNotaEletronicaData) {
        this.ecfNotaEletronicaData = ecfNotaEletronicaData;
    }

    public Double getEcfNotaEletronicaValor() {
        return ecfNotaEletronicaValor;
    }

    public void setEcfNotaEletronicaValor(Double ecfNotaEletronicaValor) {
        this.ecfNotaEletronicaValor = ecfNotaEletronicaValor;
    }

    public String getEcfNotaEletronicaChave() {
        return ecfNotaEletronicaChave;
    }

    public void setEcfNotaEletronicaChave(String ecfNotaEletronicaChave) {
        this.ecfNotaEletronicaChave = ecfNotaEletronicaChave;
    }

    public String getEcfNotaEletronicaProtocolo() {
        return ecfNotaEletronicaProtocolo;
    }

    public void setEcfNotaEletronicaProtocolo(String ecfNotaEletronicaProtocolo) {
        this.ecfNotaEletronicaProtocolo = ecfNotaEletronicaProtocolo;
    }

    public Double getEcfNotaEletronicaIcms() {
        return ecfNotaEletronicaIcms;
    }

    public void setEcfNotaEletronicaIcms(Double ecfNotaEletronicaIcms) {
        this.ecfNotaEletronicaIcms = ecfNotaEletronicaIcms;
    }

    public Double getEcfNotaEletronicaIpi() {
        return ecfNotaEletronicaIpi;
    }

    public void setEcfNotaEletronicaIpi(Double ecfNotaEletronicaIpi) {
        this.ecfNotaEletronicaIpi = ecfNotaEletronicaIpi;
    }

    public Double getEcfNotaEletronicaPis() {
        return ecfNotaEletronicaPis;
    }

    public void setEcfNotaEletronicaPis(Double ecfNotaEletronicaPis) {
        this.ecfNotaEletronicaPis = ecfNotaEletronicaPis;
    }

    public Double getEcfNotaEletronicaCofins() {
        return ecfNotaEletronicaCofins;
    }

    public void setEcfNotaEletronicaCofins(Double ecfNotaEletronicaCofins) {
        this.ecfNotaEletronicaCofins = ecfNotaEletronicaCofins;
    }

    public String getEcfNotaEletronicaXml() {
        return ecfNotaEletronicaXml;
    }

    public void setEcfNotaEletronicaXml(String ecfNotaEletronicaXml) {
        this.ecfNotaEletronicaXml = ecfNotaEletronicaXml;
    }

    public String getEcfNotaEletronicaProtocoloCancelado() {
        return ecfNotaEletronicaProtocoloCancelado;
    }

    public void setEcfNotaEletronicaProtocoloCancelado(String ecfNotaEletronicaProtocoloCancelado) {
        this.ecfNotaEletronicaProtocoloCancelado = ecfNotaEletronicaProtocoloCancelado;
    }

    public String getEcfNotaEletronicaXmlCancelado() {
        return ecfNotaEletronicaXmlCancelado;
    }

    public void setEcfNotaEletronicaXmlCancelado(String ecfNotaEletronicaXmlCancelado) {
        this.ecfNotaEletronicaXmlCancelado = ecfNotaEletronicaXmlCancelado;
    }

    public String getEcfNotaEletronicaRecibo() {
        return ecfNotaEletronicaRecibo;
    }

    public void setEcfNotaEletronicaRecibo(String ecfNotaEletronicaRecibo) {
        this.ecfNotaEletronicaRecibo = ecfNotaEletronicaRecibo;
    }

    public SisCliente getSisCliente() {
        return sisCliente;
    }

    public void setSisCliente(SisCliente sisCliente) {
        this.sisCliente = sisCliente;
    }
}