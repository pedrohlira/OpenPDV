package br.com.openpdv.modelo.sistema;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.core.ELetra;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modelo que representa um cliente no sistema.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "sis_cliente")
@XmlRootElement
public class SisCliente extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sis_cliente_id")
    private Integer sisClienteId;
    @Column(name = "sis_cliente_doc")
    private String sisClienteDoc;
    @Column(name = "sis_cliente_nome")
    private String sisClienteNome;
    @Column(name = "sis_cliente_endereco")
    private String sisClienteEndereco;
    @Column(name = "sis_cliente_cadastrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sisClienteCadastrado;

    // usados somente na NFe
    private transient String ie;
    private transient int numero;
    private transient String bairro;
    private transient String cep;
    private transient SisMunicipio sisMunicipio;
    private transient String fone;
    private transient String email;

    /**
     * Construtor padrao
     */
    public SisCliente() {
        this(0);
    }

    /**
     * Construtor padrao passando o id
     *
     * @param sisClienteId o id do cliente
     */
    public SisCliente(Integer sisClienteId) {
        super("SisCliente", "sisClienteId", "sisClienteNome");
        this.sisClienteId = sisClienteId;
        this.setTipoLetra(ELetra.GRANDE);
    }

    @Override
    public Integer getId() {
        return this.sisClienteId;
    }

    @Override
    public void setId(Integer id) {
        this.sisClienteId = id;
    }

    // GETs e SETs
    public String getSisClienteDoc() {
        return sisClienteDoc;
    }

    public void setSisClienteDoc(String sisClienteDoc) {
        this.sisClienteDoc = sisClienteDoc;
    }

    public String getSisClienteEndereco() {
        return sisClienteEndereco;
    }

    public void setSisClienteEndereco(String sisClienteEndereco) {
        this.sisClienteEndereco = sisClienteEndereco;
    }

    public Integer getSisClienteId() {
        return sisClienteId;
    }

    public void setSisClienteId(Integer sisClienteId) {
        this.sisClienteId = sisClienteId;
    }

    public String getSisClienteNome() {
        return sisClienteNome;
    }

    public void setSisClienteNome(String sisClienteNome) {
        this.sisClienteNome = sisClienteNome;
    }

    public Date getSisClienteCadastrado() {
        return sisClienteCadastrado;
    }

    public void setSisClienteCadastrado(Date sisClienteCadastrado) {
        this.sisClienteCadastrado = sisClienteCadastrado;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public SisMunicipio getSisMunicipio() {
        return sisMunicipio;
    }

    public void setSisMunicipio(SisMunicipio sisMunicipio) {
        this.sisMunicipio = sisMunicipio;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
