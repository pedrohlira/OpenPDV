package br.com.openpdv.modelo.ecf;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que representa os pagamentos diarios no sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_pagamento_totais")
@XmlRootElement
public class EcfPagamentoTotais extends Dados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecf_pagamento_totais_id")
    private Integer ecfPagamentoTotaisId;
    @Column(name = "ecf_pagamento_totais_data")
    @Temporal(TemporalType.DATE)
    private Date ecfPagamentoTotaisData;
    @Column(name = "ecf_pagamento_totais_documento")
    private String ecfPagamentoTotaisDocumento;
    @Column(name = "ecf_pagamento_totais_valor")
    private Double ecfPagamentoTotaisValor;
    @JoinColumn(name = "ecf_pagamento_tipo_id")
    @ManyToOne
    private EcfPagamentoTipo ecfPagamentoTipo;

    /**
     * Construtor padrao
     */
    public EcfPagamentoTotais() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfPagamentoTotaisId o id do registro.
     */
    public EcfPagamentoTotais(Integer ecfPagamentoTotaisId) {
        super("EcfPagamentoTotais", "ecfPagamentoTotaisId", "ecfPagamentoTotaisData");
        this.ecfPagamentoTotaisId = ecfPagamentoTotaisId;
    }

    @Override
    public Integer getId() {
        return ecfPagamentoTotaisId;
    }

    @Override
    public void setId(Integer id) {
        ecfPagamentoTotaisId = id;
    }

    //GETs e SETs
    public EcfPagamentoTipo getEcfPagamentoTipo() {
        return ecfPagamentoTipo;
    }

    public void setEcfPagamentoTipo(EcfPagamentoTipo ecfPagamentoTipo) {
        this.ecfPagamentoTipo = ecfPagamentoTipo;
    }

    public Date getEcfPagamentoTotaisData() {
        return ecfPagamentoTotaisData;
    }

    public void setEcfPagamentoTotaisData(Date ecfPagamentoTotaisData) {
        this.ecfPagamentoTotaisData = ecfPagamentoTotaisData;
    }

    public String getEcfPagamentoTotaisDocumento() {
        return ecfPagamentoTotaisDocumento;
    }

    public void setEcfPagamentoTotaisDocumento(String ecfPagamentoTotaisDocumento) {
        this.ecfPagamentoTotaisDocumento = ecfPagamentoTotaisDocumento;
    }

    public Integer getEcfPagamentoTotaisId() {
        return ecfPagamentoTotaisId;
    }

    public void setEcfPagamentoTotaisId(Integer ecfPagamentoTotaisId) {
        this.ecfPagamentoTotaisId = ecfPagamentoTotaisId;
    }

    public Double getEcfPagamentoTotaisValor() {
        return ecfPagamentoTotaisValor;
    }

    public void setEcfPagamentoTotaisValor(Double ecfPagamentoTotaisValor) {
        this.ecfPagamentoTotaisValor = ecfPagamentoTotaisValor;
    }
}
