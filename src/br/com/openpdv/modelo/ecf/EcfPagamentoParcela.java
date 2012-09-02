package br.com.openpdv.modelo.ecf;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

/**
 * Classe que representa a parcela do pagamento no sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_pagamento_parcela")
@XmlRootElement
public class EcfPagamentoParcela extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecf_pagamento_parcela_id")
    private Integer ecfPagamentoParcelaId;
    @Column(name = "ecf_pagamento_parcela_data")
    @Temporal(TemporalType.DATE)
    private Date ecfPagamentoParcelaData;
    @Column(name = "ecf_pagamento_parcela_valor")
    private Double ecfPagamentoParcelaValor;
    @Column(name = "ecf_pagamento_parcela_nsu")
    private String ecfPagamentoParcelaNsu;
    @JoinColumn(name = "ecf_pagamento_id")
    @ManyToOne
    @XmlInverseReference(mappedBy = "ecfPagamentoParcelas")
    private EcfPagamento ecfPagamento;

    /**
     * Construtor padrao
     */
    public EcfPagamentoParcela() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfPagamentoParcelaId o id do registro.
     */
    public EcfPagamentoParcela(Integer ecfPagamentoParcelaId) {
        super("EcfPagamentoParcela", "ecfPagamentoParcelaId", "ecfPagamentoParcelaId");
        this.ecfPagamentoParcelaId = ecfPagamentoParcelaId;
    }

    @Override
    public Integer getId() {
        return ecfPagamentoParcelaId;
    }

    @Override
    public void setId(Integer id) {
        ecfPagamentoParcelaId = id;
    }

    public Integer getEcfPagamentoParcelaId() {
        return ecfPagamentoParcelaId;
    }

    public void setEcfPagamentoParcelaId(Integer ecfPagamentoParcelaId) {
        this.ecfPagamentoParcelaId = ecfPagamentoParcelaId;
    }

    public Date getEcfPagamentoParcelaData() {
        return ecfPagamentoParcelaData;
    }

    public void setEcfPagamentoParcelaData(Date ecfPagamentoParcelaData) {
        this.ecfPagamentoParcelaData = ecfPagamentoParcelaData;
    }

    public Double getEcfPagamentoParcelaValor() {
        return ecfPagamentoParcelaValor;
    }

    public void setEcfPagamentoParcelaValor(Double ecfPagamentoParcelaValor) {
        this.ecfPagamentoParcelaValor = ecfPagamentoParcelaValor;
    }

    public String getEcfPagamentoParcelaNsu() {
        return ecfPagamentoParcelaNsu;
    }

    public void setEcfPagamentoParcelaNsu(String ecfPagamentoParcelaNsu) {
        this.ecfPagamentoParcelaNsu = ecfPagamentoParcelaNsu;
    }

    public EcfPagamento getEcfPagamento() {
        return ecfPagamento;
    }

    public void setEcfPagamento(EcfPagamento ecfPagamento) {
        this.ecfPagamento = ecfPagamento;
    }
}
