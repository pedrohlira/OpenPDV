package br.com.openpdv.modelo.ecf;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

/**
 * Classe que representa o pagamento da venda no sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_pagamento")
@XmlRootElement
public class EcfPagamento extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecf_pagamento_id")
    private Integer ecfPagamentoId;
    @Column(name = "ecf_pagamento_gnf")
    private int ecfPagamentoGnf;
    @Column(name = "ecf_pagamento_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ecfPagamentoData;
    @Column(name = "ecf_pagamento_valor")
    private Double ecfPagamentoValor;
    @Column(name = "ecf_pagamento_nsu")
    private String ecfPagamentoNsu;
    @Column(name = "ecf_pagamento_estorno")
    private char ecfPagamentoEstorno;
    @Column(name = "ecf_pagamento_estorno_gnf")
    private int ecfPagamentoEstornoGnf;
    @Column(name = "ecf_pagamento_estorno_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ecfPagamentoEstornoData;
    @Column(name = "ecf_pagamento_estorno_valor")
    private Double ecfPagamentoEstornoValor;
    @Column(name = "ecf_pagamento_estorno_nsu")
    private String ecfPagamentoEstornoNsu;
    @JoinColumn(name = "ecf_venda_id")
    @ManyToOne
    @XmlInverseReference(mappedBy = "ecfPagamentos")
    private EcfVenda ecfVenda;
    @JoinColumn(name = "ecf_pagamento_tipo_id")
    @ManyToOne
    private EcfPagamentoTipo ecfPagamentoTipo;
    @OneToMany(mappedBy = "EcfPagamento", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<EcfPagamentoParcela> ecfPagamentoParcelas;
    private transient String arquivo;

    /**
     * Construtor padrao
     */
    public EcfPagamento() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfPagamentoId o id do registro.
     */
    public EcfPagamento(Integer ecfPagamentoId) {
        super("ecfPagamento", "ecfPagamentoId", "ecfPagamentoId");
        this.ecfPagamentoId = ecfPagamentoId;
    }

    @Override
    public Integer getId() {
        return ecfPagamentoId;
    }

    @Override
    public void setId(Integer id) {
        ecfPagamentoId = id;
    }

    public Integer getecfPagamentoId() {
        return ecfPagamentoId;
    }

    //GETs e SETs
    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public Date getEcfPagamentoData() {
        return ecfPagamentoData;
    }

    public void setEcfPagamentoData(Date ecfPagamentoData) {
        this.ecfPagamentoData = ecfPagamentoData;
    }

    public char getEcfPagamentoEstorno() {
        return ecfPagamentoEstorno;
    }

    public void setEcfPagamentoEstorno(char ecfPagamentoEstorno) {
        this.ecfPagamentoEstorno = ecfPagamentoEstorno;
    }

    public Date getEcfPagamentoEstornoData() {
        return ecfPagamentoEstornoData;
    }

    public void setEcfPagamentoEstornoData(Date ecfPagamentoEstornoData) {
        this.ecfPagamentoEstornoData = ecfPagamentoEstornoData;
    }

    public int getEcfPagamentoEstornoGnf() {
        return ecfPagamentoEstornoGnf;
    }

    public void setEcfPagamentoEstornoGnf(int ecfPagamentoEstornoGnf) {
        this.ecfPagamentoEstornoGnf = ecfPagamentoEstornoGnf;
    }

    public String getEcfPagamentoEstornoNsu() {
        return ecfPagamentoEstornoNsu;
    }

    public void setEcfPagamentoEstornoNsu(String ecfPagamentoEstornoNsu) {
        this.ecfPagamentoEstornoNsu = ecfPagamentoEstornoNsu;
    }

    public Double getEcfPagamentoEstornoValor() {
        return ecfPagamentoEstornoValor;
    }

    public void setEcfPagamentoEstornoValor(Double ecfPagamentoEstornoValor) {
        this.ecfPagamentoEstornoValor = ecfPagamentoEstornoValor;
    }

    public int getEcfPagamentoGnf() {
        return ecfPagamentoGnf;
    }

    public void setEcfPagamentoGnf(int ecfPagamentoGnf) {
        this.ecfPagamentoGnf = ecfPagamentoGnf;
    }

    public Integer getEcfPagamentoId() {
        return ecfPagamentoId;
    }

    public void setEcfPagamentoId(Integer ecfPagamentoId) {
        this.ecfPagamentoId = ecfPagamentoId;
    }

    public String getEcfPagamentoNsu() {
        return ecfPagamentoNsu;
    }

    public void setEcfPagamentoNsu(String ecfPagamentoNsu) {
        this.ecfPagamentoNsu = ecfPagamentoNsu;
    }

    public EcfPagamentoTipo getEcfPagamentoTipo() {
        return ecfPagamentoTipo;
    }

    public void setEcfPagamentoTipo(EcfPagamentoTipo ecfPagamentoTipo) {
        this.ecfPagamentoTipo = ecfPagamentoTipo;
    }

    public Double getEcfPagamentoValor() {
        return ecfPagamentoValor;
    }

    public void setEcfPagamentoValor(Double ecfPagamentoValor) {
        this.ecfPagamentoValor = ecfPagamentoValor;
    }

    public EcfVenda getEcfVenda() {
        return ecfVenda;
    }

    public void setEcfVenda(EcfVenda ecfVenda) {
        this.ecfVenda = ecfVenda;
    }

    public List<EcfPagamentoParcela> getEcfPagamentoParcelas() {
        return ecfPagamentoParcelas;
    }

    public void setEcfPagamentoParcelas(List<EcfPagamentoParcela> ecfPagamentoParcelas) {
        this.ecfPagamentoParcelas = ecfPagamentoParcelas;
    }
}
