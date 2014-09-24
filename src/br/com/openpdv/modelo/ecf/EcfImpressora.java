package br.com.openpdv.modelo.ecf;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que representa a impressora do sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_impressora")
@XmlRootElement
public class EcfImpressora extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecf_impressora_id")
    private Integer ecfImpressoraId;
    @Column(name = "ecf_impressora_codigo")
    private String ecfImpressoraCodigo;
    @Column(name = "ecf_impressora_mfadicional")
    private String ecfImpressoraMfadicional;
    @Column(name = "ecf_impressora_identificacao")
    private String ecfImpressoraIdentificacao;
    @Column(name = "ecf_impressora_serie")
    private String ecfImpressoraSerie;
    @Column(name = "ecf_impressora_tipo")
    private String ecfImpressoraTipo;
    @Column(name = "ecf_impressora_marca")
    private String ecfImpressoraMarca;
    @Column(name = "ecf_impressora_modelo")
    private String ecfImpressoraModelo;
    @Column(name = "ecf_impressora_caixa")
    private int ecfImpressoraCaixa;
    @Column(name = "ecf_impressora_ativo")
    private boolean ecfImpressoraAtivo;

    /**
     * Construtor padrao
     */
    public EcfImpressora() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfImpressoraId o id do registro.
     */
    public EcfImpressora(Integer ecfImpressoraId) {
        super("EcfImpressora", "ecfImpressoraId", "ecfImpressoraId");
        this.ecfImpressoraId = ecfImpressoraId;
    }

    @Override
    public Integer getId() {
        return ecfImpressoraId;
    }

    @Override
    public void setId(Integer id) {
        ecfImpressoraId = id;
    }

    // GETs e SETs
    public Integer getEcfImpressoraId() {
        return ecfImpressoraId;
    }

    public void setEcfImpressoraId(Integer ecfImpressoraId) {
        this.ecfImpressoraId = ecfImpressoraId;
    }

    public String getEcfImpressoraCodigo() {
        return ecfImpressoraCodigo;
    }

    public void setEcfImpressoraCodigo(String ecfImpressoraCodigo) {
        this.ecfImpressoraCodigo = ecfImpressoraCodigo;
    }

    public String getEcfImpressoraMfadicional() {
        return ecfImpressoraMfadicional;
    }

    public void setEcfImpressoraMfadicional(String ecfImpressoraMfadicional) {
        this.ecfImpressoraMfadicional = ecfImpressoraMfadicional;
    }

    public String getEcfImpressoraIdentificacao() {
        return ecfImpressoraIdentificacao;
    }

    public void setEcfImpressoraIdentificacao(String ecfImpressoraIdentificacao) {
        this.ecfImpressoraIdentificacao = ecfImpressoraIdentificacao;
    }

    public String getEcfImpressoraSerie() {
        return ecfImpressoraSerie;
    }

    public void setEcfImpressoraSerie(String ecfImpressoraSerie) {
        this.ecfImpressoraSerie = ecfImpressoraSerie;
    }

    public String getEcfImpressoraTipo() {
        return ecfImpressoraTipo;
    }

    public void setEcfImpressoraTipo(String ecfImpressoraTipo) {
        this.ecfImpressoraTipo = ecfImpressoraTipo;
    }

    public String getEcfImpressoraMarca() {
        return ecfImpressoraMarca;
    }

    public void setEcfImpressoraMarca(String ecfImpressoraMarca) {
        this.ecfImpressoraMarca = ecfImpressoraMarca;
    }

    public String getEcfImpressoraModelo() {
        return ecfImpressoraModelo;
    }

    public void setEcfImpressoraModelo(String ecfImpressoraModelo) {
        this.ecfImpressoraModelo = ecfImpressoraModelo;
    }

    public int getEcfImpressoraCaixa() {
        return ecfImpressoraCaixa;
    }

    public void setEcfImpressoraCaixa(int ecfImpressoraCaixa) {
        this.ecfImpressoraCaixa = ecfImpressoraCaixa;
    }

    public boolean isEcfImpressoraAtivo() {
        return ecfImpressoraAtivo;
    }

    public void setEcfImpressoraAtivo(boolean ecfImpressoraAtivo) {
        this.ecfImpressoraAtivo = ecfImpressoraAtivo;
    }
}
