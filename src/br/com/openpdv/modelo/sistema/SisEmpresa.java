package br.com.openpdv.modelo.sistema;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modelo que representa a empresa no sistema.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "sis_empresa")
@XmlRootElement
public class SisEmpresa extends Dados implements Serializable {

    @Id
    @Column(name = "sis_empresa_id")
    private Integer sisEmpresaId;
    @Column(name = "sis_empresa_razao")
    private String sisEmpresaRazao;
    @Column(name = "sis_empresa_fantasia")
    private String sisEmpresaFantasia;
    @Column(name = "sis_empresa_cnpj")
    private String sisEmpresaCnpj;
    @Column(name = "sis_empresa_ie")
    private String sisEmpresaIe;
    @Column(name = "sis_empresa_im")
    private String sisEmpresaIm;
    @Column(name = "sis_empresa_logradouro")
    private String sisEmpresaLogradouro;
    @Column(name = "sis_empresa_numero")
    private int sisEmpresaNumero;
    @Column(name = "sis_empresa_complemento")
    private String sisEmpresaComplemento;
    @Column(name = "sis_empresa_bairro")
    private String sisEmpresaBairro;
    @Column(name = "sis_empresa_cep")
    private String sisEmpresaCep;
    @Column(name = "sis_empresa_responsavel")
    private String sisEmpresaResponsavel;
    @Column(name = "sis_empresa_fone")
    private String sisEmpresaFone;
    @Column(name = "sis_empresa_email")
    private String sisEmpresaEmail;
    @JoinColumn(name = "sis_municipio_id", referencedColumnName = "sis_municipio_id")
    @ManyToOne
    private SisMunicipio sisMunicipio;

    /**
     * Construtor padrao
     */
    public SisEmpresa() {
        this(0);
    }

    /**
     * Construtor padrao passando o id
     *
     * @param sisEmpresaId o id da empresa
     */
    public SisEmpresa(Integer sisEmpresaId) {
        super("SisEmpresa", "sisEmpresaId", "sisEmpresaId");
        this.sisEmpresaId = sisEmpresaId;
    }

    @Override
    public Integer getId() {
        return this.sisEmpresaId;
    }

    @Override
    public void setId(Integer id) {
        this.sisEmpresaId = id;
    }

    // GETs e SETs
    public Integer getSisEmpresaId() {
        return sisEmpresaId;
    }

    public void setSisEmpresaId(Integer sisEmpresaId) {
        this.sisEmpresaId = sisEmpresaId;
    }

    public String getSisEmpresaRazao() {
        return sisEmpresaRazao;
    }

    public void setSisEmpresaRazao(String sisEmpresaRazao) {
        this.sisEmpresaRazao = sisEmpresaRazao;
    }

    public String getSisEmpresaFantasia() {
        return sisEmpresaFantasia;
    }

    public void setSisEmpresaFantasia(String sisEmpresaFantasia) {
        this.sisEmpresaFantasia = sisEmpresaFantasia;
    }

    public String getSisEmpresaCnpj() {
        return sisEmpresaCnpj;
    }

    public void setSisEmpresaCnpj(String sisEmpresaCnpj) {
        this.sisEmpresaCnpj = sisEmpresaCnpj;
    }

    public String getSisEmpresaIe() {
        return sisEmpresaIe;
    }

    public void setSisEmpresaIe(String sisEmpresaIe) {
        this.sisEmpresaIe = sisEmpresaIe;
    }

    public String getSisEmpresaIm() {
        return sisEmpresaIm;
    }

    public void setSisEmpresaIm(String sisEmpresaIm) {
        this.sisEmpresaIm = sisEmpresaIm;
    }

    public String getSisEmpresaLogradouro() {
        return sisEmpresaLogradouro;
    }

    public void setSisEmpresaLogradouro(String sisEmpresaLogradouro) {
        this.sisEmpresaLogradouro = sisEmpresaLogradouro;
    }

    public int getSisEmpresaNumero() {
        return sisEmpresaNumero;
    }

    public void setSisEmpresaNumero(int sisEmpresaNumero) {
        this.sisEmpresaNumero = sisEmpresaNumero;
    }

    public String getSisEmpresaComplemento() {
        return sisEmpresaComplemento;
    }

    public void setSisEmpresaComplemento(String sisEmpresaComplemento) {
        this.sisEmpresaComplemento = sisEmpresaComplemento;
    }

    public String getSisEmpresaBairro() {
        return sisEmpresaBairro;
    }

    public void setSisEmpresaBairro(String sisEmpresaBairro) {
        this.sisEmpresaBairro = sisEmpresaBairro;
    }

    public String getSisEmpresaCep() {
        return sisEmpresaCep;
    }

    public void setSisEmpresaCep(String sisEmpresaCep) {
        this.sisEmpresaCep = sisEmpresaCep;
    }

    public String getSisEmpresaResponsavel() {
        return sisEmpresaResponsavel;
    }

    public void setSisEmpresaResponsavel(String sisEmpresaResponsavel) {
        this.sisEmpresaResponsavel = sisEmpresaResponsavel;
    }

    public String getSisEmpresaFone() {
        return sisEmpresaFone;
    }

    public void setSisEmpresaFone(String sisEmpresaFone) {
        this.sisEmpresaFone = sisEmpresaFone;
    }

    public String getSisEmpresaEmail() {
        return sisEmpresaEmail;
    }

    public void setSisEmpresaEmail(String sisEmpresaEmail) {
        this.sisEmpresaEmail = sisEmpresaEmail;
    }

    public SisMunicipio getSisMunicipio() {
        return sisMunicipio;
    }

    public void setSisMunicipio(SisMunicipio sisMunicipio) {
        this.sisMunicipio = sisMunicipio;
    }
}
