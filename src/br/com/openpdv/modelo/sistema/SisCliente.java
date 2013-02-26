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
    @Column(name = "sis_cliente_doc1")
    private String sisClienteDoc1;
    @Column(name = "sis_cliente_nome")
    private String sisClienteNome;
    @Column(name = "sis_cliente_endereco")
    private String sisClienteEndereco;
    @Column(name = "sis_cliente_numero")
    private int sisClienteNumero;
    @Column(name = "sis_cliente_complemento")
    private String sisClienteComplemento;
    @Column(name = "sis_cliente_bairro")
    private String sisClienteBairro;
    @Column(name = "sis_cliente_cep")
    private String sisClienteCep;
    @Column(name = "sis_cliente_telefone")
    private String sisClienteTelefone;
    @Column(name = "sis_cliente_email")
    private String sisClienteEmail;
    @Column(name = "sis_cliente_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sisClienteData;
    @JoinColumn(name = "sis_municipio_id", referencedColumnName = "sis_municipio_id")
    @ManyToOne
    private SisMunicipio sisMunicipio;
    // usado somente na identificacao
    private transient SisUsuario vendedor;

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

    public String getSisClienteDoc1() {
        return sisClienteDoc1;
    }

    public void setSisClienteDoc1(String sisClienteDoc1) {
        this.sisClienteDoc1 = sisClienteDoc1;
    }

    public int getSisClienteNumero() {
        return sisClienteNumero;
    }

    public void setSisClienteNumero(int sisClienteNumero) {
        this.sisClienteNumero = sisClienteNumero;
    }

    public String getSisClienteComplemento() {
        return sisClienteComplemento;
    }

    public void setSisClienteComplemento(String sisClienteComplemento) {
        this.sisClienteComplemento = sisClienteComplemento;
    }

    public String getSisClienteBairro() {
        return sisClienteBairro;
    }

    public void setSisClienteBairro(String sisClienteBairro) {
        this.sisClienteBairro = sisClienteBairro;
    }

    public String getSisClienteCep() {
        return sisClienteCep;
    }

    public void setSisClienteCep(String sisClienteCep) {
        this.sisClienteCep = sisClienteCep;
    }

    public String getSisClienteTelefone() {
        return sisClienteTelefone;
    }

    public void setSisClienteTelefone(String sisClienteTelefone) {
        this.sisClienteTelefone = sisClienteTelefone;
    }

    public String getSisClienteEmail() {
        return sisClienteEmail;
    }

    public void setSisClienteEmail(String sisClienteEmail) {
        this.sisClienteEmail = sisClienteEmail;
    }

    public SisMunicipio getSisMunicipio() {
        return sisMunicipio;
    }

    public void setSisMunicipio(SisMunicipio sisMunicipio) {
        this.sisMunicipio = sisMunicipio;
    }

    public Date getSisClienteData() {
        return sisClienteData;
    }

    public void setSisClienteData(Date sisClienteData) {
        this.sisClienteData = sisClienteData;
    }

    public SisUsuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(SisUsuario vendedor) {
        this.vendedor = vendedor;
    }
}
