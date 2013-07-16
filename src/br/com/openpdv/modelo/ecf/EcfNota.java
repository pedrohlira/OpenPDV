package br.com.openpdv.modelo.ecf;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.core.EDirecao;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que representa uma nota de consumidor no sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_nota")
@XmlRootElement
public class EcfNota extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecf_nota_id")
    private Integer ecfNotaId;
    @Column(name = "ecf_nota_serie")
    private String ecfNotaSerie;
    @Column(name = "ecf_nota_subserie")
    private String ecfNotaSubserie;
    @Column(name = "ecf_nota_numero")
    private int ecfNotaNumero;
    @Column(name = "ecf_nota_data")
    @Temporal(TemporalType.DATE)
    private Date ecfNotaData;
    @Column(name = "ecf_nota_bruto")
    private Double ecfNotaBruto;
    @Column(name = "ecf_nota_desconto")
    private Double ecfNotaDesconto;
    @Column(name = "ecf_nota_liquido")
    private Double ecfNotaLiquido;
    @Column(name = "ecf_nota_pis")
    private Double ecfNotaPis;
    @Column(name = "ecf_nota_cofins")
    private Double ecfNotaCofins;
    @Column(name = "ecf_nota_cancelada")
    private boolean ecfNotaCancelada;
    @ManyToOne
    @JoinColumn(name = "sis_cliente_id")
    private SisCliente sisCliente;
    @OneToMany(mappedBy = "ecfNota", fetch = FetchType.EAGER)
    private List<EcfNotaProduto> ecfNotaProdutos;

    /**
     * Construtor padrao
     */
    public EcfNota() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfNotaId o id do registro.
     */
    public EcfNota(Integer ecfNotaId) {
        super("EcfNota", "ecfNotaId", "ecfNotaData", EDirecao.DESC);
        this.ecfNotaId = ecfNotaId;
    }

    @Override
    public Integer getId() {
        return ecfNotaId;
    }

    @Override
    public void setId(Integer id) {
        ecfNotaId = id;
    }

    public Integer getEcfNotaId() {
        return ecfNotaId;
    }

    public void setEcfNotaId(Integer ecfNotaId) {
        this.ecfNotaId = ecfNotaId;
    }

    public String getEcfNotaSerie() {
        return ecfNotaSerie;
    }

    public void setEcfNotaSerie(String ecfNotaSerie) {
        this.ecfNotaSerie = ecfNotaSerie;
    }

    public String getEcfNotaSubserie() {
        return ecfNotaSubserie;
    }

    public void setEcfNotaSubserie(String ecfNotaSubserie) {
        this.ecfNotaSubserie = ecfNotaSubserie;
    }

    public int getEcfNotaNumero() {
        return ecfNotaNumero;
    }

    public void setEcfNotaNumero(int ecfNotaNumero) {
        this.ecfNotaNumero = ecfNotaNumero;
    }

    public Date getEcfNotaData() {
        return ecfNotaData;
    }

    public void setEcfNotaData(Date ecfNotaData) {
        this.ecfNotaData = ecfNotaData;
    }

    public Double getEcfNotaBruto() {
        return ecfNotaBruto;
    }

    public void setEcfNotaBruto(Double ecfNotaBruto) {
        this.ecfNotaBruto = ecfNotaBruto;
    }

    public Double getEcfNotaDesconto() {
        return ecfNotaDesconto;
    }

    public void setEcfNotaDesconto(Double ecfNotaDesconto) {
        this.ecfNotaDesconto = ecfNotaDesconto;
    }

    public Double getEcfNotaLiquido() {
        return ecfNotaLiquido;
    }

    public void setEcfNotaLiquido(Double ecfNotaLiquido) {
        this.ecfNotaLiquido = ecfNotaLiquido;
    }

    public Double getEcfNotaPis() {
        return ecfNotaPis;
    }

    public void setEcfNotaPis(Double ecfNotaPis) {
        this.ecfNotaPis = ecfNotaPis;
    }

    public Double getEcfNotaCofins() {
        return ecfNotaCofins;
    }

    public void setEcfNotaCofins(Double ecfNotaCofins) {
        this.ecfNotaCofins = ecfNotaCofins;
    }

    public boolean isEcfNotaCancelada() {
        return ecfNotaCancelada;
    }

    public void setEcfNotaCancelada(boolean ecfNotaCancelada) {
        this.ecfNotaCancelada = ecfNotaCancelada;
    }

    public List<EcfNotaProduto> getEcfNotaProdutos() {
        return ecfNotaProdutos;
    }

    public void setEcfNotaProdutos(List<EcfNotaProduto> ecfNotaProdutos) {
        this.ecfNotaProdutos = ecfNotaProdutos;
    }

    public SisCliente getSisCliente() {
        return sisCliente;
    }

    public void setSisCliente(SisCliente sisCliente) {
        this.sisCliente = sisCliente;
    }
}
