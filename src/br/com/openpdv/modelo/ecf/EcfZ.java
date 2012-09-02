package br.com.openpdv.modelo.ecf;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que representa os daods da leitura Z no sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_z")
@XmlRootElement
public class EcfZ extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecf_z_id")
    private Integer ecfZId;
    @Column(name = "ecf_z_usuario")
    private int ecfZUsuario;
    @Column(name = "ecf_z_crz")
    private int ecfZCrz;
    @Column(name = "ecf_z_coo_ini")
    private int ecfZCooIni;
    @Column(name = "ecf_z_coo_fin")
    private int ecfZCooFin;
    @Column(name = "ecf_z_cro")
    private int ecfZCro;
    @Column(name = "ecf_z_movimento")
    @Temporal(TemporalType.DATE)
    private Date ecfZMovimento;
    @Column(name = "ecf_z_emissao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ecfZEmissao;
    @Column(name = "ecf_z_bruto")
    private Double ecfZBruto;
    @Column(name = "ecf_z_gt")
    private Double ecfZGt;
    @Column(name = "ecf_z_issqn")
    private boolean ecfZIssqn;
    @JoinColumn(name = "ecf_impressora_id")
    @ManyToOne
    private EcfImpressora ecfImpressora;
    @OneToMany(mappedBy = "ecfZ", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<EcfZTotais> ecfZTotais;
    @OneToMany(mappedBy = "ecfZ", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<EcfVenda> ecfVendas;
    // somente para salvar no servidor via REST
    @Transient
    private List<EcfDocumento> ecfDocumentos;

    /**
     * Construtor padrao
     */
    public EcfZ() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfZId o id do registro.
     */
    public EcfZ(Integer ecfZId) {
        super("EcfZ", "ecfZId", "ecfZMovimento");
        this.ecfZId = ecfZId;
    }

    @Override
    public Integer getId() {
        return ecfZId;
    }

    @Override
    public void setId(Integer id) {
        ecfZId = id;
    }

    public Integer getEcfZId() {
        return ecfZId;
    }

    public void setEcfZId(Integer ecfZId) {
        this.ecfZId = ecfZId;
    }

    public int getEcfZUsuario() {
        return ecfZUsuario;
    }

    public void setEcfZUsuario(int ecfZUsuario) {
        this.ecfZUsuario = ecfZUsuario;
    }

    public int getEcfZCrz() {
        return ecfZCrz;
    }

    public void setEcfZCrz(int ecfZCrz) {
        this.ecfZCrz = ecfZCrz;
    }

    public int getEcfZCooIni() {
        return ecfZCooIni;
    }

    public void setEcfZCooIni(int ecfZCooIni) {
        this.ecfZCooIni = ecfZCooIni;
    }

    public int getEcfZCooFin() {
        return ecfZCooFin;
    }

    public void setEcfZCooFin(int ecfZCooFin) {
        this.ecfZCooFin = ecfZCooFin;
    }

    public int getEcfZCro() {
        return ecfZCro;
    }

    public void setEcfZCro(int ecfZCro) {
        this.ecfZCro = ecfZCro;
    }

    public Date getEcfZMovimento() {
        return ecfZMovimento;
    }

    public void setEcfZMovimento(Date ecfZMovimento) {
        this.ecfZMovimento = ecfZMovimento;
    }

    public Date getEcfZEmissao() {
        return ecfZEmissao;
    }

    public void setEcfZEmissao(Date ecfZEmissao) {
        this.ecfZEmissao = ecfZEmissao;
    }

    public Double getEcfZBruto() {
        return ecfZBruto;
    }

    public void setEcfZBruto(Double ecfZBruto) {
        this.ecfZBruto = ecfZBruto;
    }

    public Double getEcfZGt() {
        return ecfZGt;
    }

    public void setEcfZGt(Double ecfZGt) {
        this.ecfZGt = ecfZGt;
    }

    public boolean getEcfZIssqn() {
        return ecfZIssqn;
    }

    public void setEcfZIssqn(boolean ecfZIssqn) {
        this.ecfZIssqn = ecfZIssqn;
    }

    public EcfImpressora getEcfImpressora() {
        return ecfImpressora;
    }

    public void setEcfImpressora(EcfImpressora ecfImpressora) {
        this.ecfImpressora = ecfImpressora;
    }

    public List<EcfZTotais> getEcfZTotais() {
        return ecfZTotais;
    }

    public void setEcfZTotais(List<EcfZTotais> ecfZTotais) {
        this.ecfZTotais = ecfZTotais;
    }

    public List<EcfVenda> getEcfVendas() {
        return ecfVendas;
    }

    public void setEcfVendas(List<EcfVenda> ecfVendas) {
        this.ecfVendas = ecfVendas;
    }

    public List<EcfDocumento> getEcfDocumentos() {
        return ecfDocumentos;
    }

    public void setEcfDocumentos(List<EcfDocumento> ecfDocumentos) {
        this.ecfDocumentos = ecfDocumentos;
    }
}
