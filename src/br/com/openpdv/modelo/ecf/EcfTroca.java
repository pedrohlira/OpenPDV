package br.com.openpdv.modelo.ecf;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.core.EDirecao;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que representa uma troca do consumidor no sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_troca")
@XmlRootElement
public class EcfTroca extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecf_troca_id")
    private Integer ecfTrocaId;
    @Column(name = "ecf_troca_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ecfTrocaData;
    @Column(name = "ecf_troca_valor")
    private Double ecfTrocaValor;
    @Column(name = "ecf_troca_ecf")
    private int ecfTrocaEcf;
    @Column(name = "ecf_troca_coo")
    private int ecfTrocaCoo;
    @Column(name = "ecf_troca_ativo")
    private boolean ecfTrocaAtivo;
    @Column(name = "ecf_troca_cliente")
    private String ecfTrocaCliente;
    @OneToMany(mappedBy = "ecfTroca", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<EcfTrocaProduto> ecfTrocaProdutos;

    /**
     * Construtor padrao
     */
    public EcfTroca() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfTrocaId o id do registro.
     */
    public EcfTroca(Integer ecfTrocaId) {
        super("EcfTroca", "ecfTrocaId", "ecfTrocaData", EDirecao.DESC);
        this.ecfTrocaId = ecfTrocaId;
    }

    @Override
    public Integer getId() {
        return ecfTrocaId;
    }

    @Override
    public void setId(Integer id) {
        ecfTrocaId = id;
    }

    public Integer getEcfTrocaId() {
        return ecfTrocaId;
    }

    public void setEcfTrocaId(Integer ecfTrocaId) {
        this.ecfTrocaId = ecfTrocaId;
    }

    public Date getEcfTrocaData() {
        return ecfTrocaData;
    }

    public void setEcfTrocaData(Date ecfTrocaData) {
        this.ecfTrocaData = ecfTrocaData;
    }

    public Double getEcfTrocaValor() {
        return ecfTrocaValor;
    }

    public void setEcfTrocaValor(Double ecfTrocaValor) {
        this.ecfTrocaValor = ecfTrocaValor;
    }

    public boolean isEcfTrocaAtivo() {
        return ecfTrocaAtivo;
    }

    public void setEcfTrocaAtivo(boolean ecfTrocaAtivo) {
        this.ecfTrocaAtivo = ecfTrocaAtivo;
    }

    public int getEcfTrocaEcf() {
        return ecfTrocaEcf;
    }

    public void setEcfTrocaEcf(int ecfTrocaEcf) {
        this.ecfTrocaEcf = ecfTrocaEcf;
    }

    public int getEcfTrocaCoo() {
        return ecfTrocaCoo;
    }

    public void setEcfTrocaCoo(int ecfTrocaCoo) {
        this.ecfTrocaCoo = ecfTrocaCoo;
    }

    public String getEcfTrocaCliente() {
        return ecfTrocaCliente;
    }

    public void setEcfTrocaCliente(String ecfTrocaCliente) {
        this.ecfTrocaCliente = ecfTrocaCliente;
    }

    public List<EcfTrocaProduto> getEcfTrocaProdutos() {
        return ecfTrocaProdutos;
    }

    public void setEcfTrocaProdutos(List<EcfTrocaProduto> ecfTrocaProdutos) {
        this.ecfTrocaProdutos = ecfTrocaProdutos;
    }
}
