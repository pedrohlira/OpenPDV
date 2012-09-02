package br.com.openpdv.modelo.ecf;

import br.com.openpdv.modelo.core.Dados;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que representa o tipo de pagamento do sistama.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "ecf_pagamento_tipo")
@XmlRootElement
public class EcfPagamentoTipo extends Dados implements Serializable {

    @Id
    @Column(name = "ecf_pagamento_tipo_id")
    private Integer ecfPagamentoTipoId;
    @Column(name = "ecf_pagamento_tipo_codigo")
    private String ecfPagamentoTipoCodigo;
    @Column(name = "ecf_pagamento_tipo_descricao")
    private String ecfPagamentoTipoDescricao;
    @Column(name = "ecf_pagamento_tipo_tef")
    private boolean ecfPagamentoTipoTef;
    @Column(name = "ecf_pagamento_tipo_vinculado")
    private boolean ecfPagamentoTipoVinculado;
    @Column(name = "ecf_pagamento_tipo_rede")
    private String ecfPagamentoTipoRede;

    /**
     * Construtor padrao
     */
    public EcfPagamentoTipo() {
        this(0);
    }

    /**
     * Contrutor padrao passando o id
     *
     * @param ecfPagamentoTipoId o id do registro.
     */
    public EcfPagamentoTipo(Integer ecfPagamentoTipoId) {
        super("EcfPagamentoTipo", "ecfPagamentoTipoId", "ecfPagamentoTipoCodigo");
        this.ecfPagamentoTipoId = ecfPagamentoTipoId;
    }

    @Override
    public Integer getId() {
        return ecfPagamentoTipoId;
    }

    @Override
    public void setId(Integer id) {
        ecfPagamentoTipoId = id;
    }

    // GETs e SETs

    public String getEcfPagamentoTipoCodigo() {
        return ecfPagamentoTipoCodigo;
    }

    public void setEcfPagamentoTipoCodigo(String ecfPagamentoTipoCodigo) {
        this.ecfPagamentoTipoCodigo = ecfPagamentoTipoCodigo;
    }

    public String getEcfPagamentoTipoDescricao() {
        return ecfPagamentoTipoDescricao;
    }

    public void setEcfPagamentoTipoDescricao(String ecfPagamentoTipoDescricao) {
        this.ecfPagamentoTipoDescricao = ecfPagamentoTipoDescricao;
    }

    public Integer getEcfPagamentoTipoId() {
        return ecfPagamentoTipoId;
    }

    public void setEcfPagamentoTipoId(Integer ecfPagamentoTipoId) {
        this.ecfPagamentoTipoId = ecfPagamentoTipoId;
    }

    public String getEcfPagamentoTipoRede() {
        return ecfPagamentoTipoRede;
    }

    public void setEcfPagamentoTipoRede(String ecfPagamentoTipoRede) {
        this.ecfPagamentoTipoRede = ecfPagamentoTipoRede;
    }

    public boolean isEcfPagamentoTipoTef() {
        return ecfPagamentoTipoTef;
    }

    public void setEcfPagamentoTipoTef(boolean ecfPagamentoTipoTef) {
        this.ecfPagamentoTipoTef = ecfPagamentoTipoTef;
    }

    public boolean isEcfPagamentoTipoVinculado() {
        return ecfPagamentoTipoVinculado;
    }

    public void setEcfPagamentoTipoVinculado(boolean ecfPagamentoTipoVinculado) {
        this.ecfPagamentoTipoVinculado = ecfPagamentoTipoVinculado;
    }
}