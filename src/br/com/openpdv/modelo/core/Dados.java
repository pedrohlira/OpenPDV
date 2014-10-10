package br.com.openpdv.modelo.core;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe de abstrai os dados das classes POJOs que representam os dados das
 * tabelas.
 *
 * @author Pedro H. Lira
 */
@XmlRootElement
@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Dados implements Serializable {

    /**
     * Chave EAD que cria o hash da linha.
     */
    @Column(name = "ead")
    private String ead;
    /**
     * Nome do objeto que representa a tabela
     */
    private transient String tabela;
    /**
     * Um array do objeto Colecao, que sao as tabelas/objetos que se relacionam
     */
    private transient Colecao[] colecao;
    /**
     * Nome no campo do objeto que representa o id
     */
    private transient String campoId;
    /**
     * Nome do campo do objeto que representa a ordenacao
     */
    private transient String campoOrdem;
    /**
     * Um enum EDirecao para definir a direcao da ordenacao
     */
    private transient EDirecao ordemDirecao;
    /**
     * Um enum ELetra para definir os tamanhos padroes das letras
     */
    private transient ELetra tipoLetra;

    /**
     * Construtor padrao.
     */
    public Dados() {
        this(null, null);
    }

    /**
     * Construtor que define as valores padroes de cada classe POJO.
     *
     * @param tabela o nome da classe que representa a tabela.
     * @param campoId o nome do campo que é o Id da tabela.
     */
    public Dados(String tabela, String campoId) {
        this(tabela, campoId, campoId);
    }

    /**
     * Construtor que define as valores padroes de cada classe POJO.
     *
     * @param tabela o nome da classe que representa a tabela.
     * @param campoId o nome do campo que é o Id da tabela.
     * @param campoOrdem o nome do campo que será usado como ordenaçao padrao.
     */
    public Dados(String tabela, String campoId, String campoOrdem) {
        this(tabela, campoId, campoOrdem, EDirecao.ASC);
    }

    /**
     * Construtor que define as valores padroes de cada classe POJO.
     *
     * @param tabela o nome da classe que representa a tabela.
     * @param campoId o nome do campo que é o Id da tabela.
     * @param campoOrdem o nome do campo que será usado como ordenaçao padrao.
     * @param ordemDirecao a direçao de ordenaçao padrao.
     */
    public Dados(String tabela, String campoId, String campoOrdem, EDirecao ordemDirecao) {
        this.tabela = tabela;
        this.campoId = campoId;
        this.campoOrdem = campoOrdem;
        this.ordemDirecao = ordemDirecao;
        this.tipoLetra = ELetra.NORMAL;
    }

    /**
     * Metodo padrao para retornar o valor do Id de qualquer classe POJO.
     *
     * @return o getId do registro.
     */
    public abstract Integer getId();

    /**
     * Metodo padrao para define o valor do id de qualquer classe POJO.
     *
     * @param id o valor a ser definido.
     */
    public abstract void setId(Integer id);

    // GETs e SETs
    public String getEad() {
        return ead;
    }

    public void setEad(String ead) {
        this.ead = ead;
    }
    
    public EDirecao getOrdemDirecao() {
        return ordemDirecao;
    }

    public String getCampoOrdem() {
        return campoOrdem;
    }

    public String getCampoId() {
        return campoId;
    }

    public String getTabela() {
        return tabela;
    }

    public Colecao[] getColecao() {
        return colecao;
    }

    public void setOrdemDirecao(EDirecao ordemDirecao) {
        this.ordemDirecao = ordemDirecao;
    }

    public void setCampoId(String campoId) {
        this.campoId = campoId;
    }

    public void setCampoOrdem(String campoOrdem) {
        this.campoOrdem = campoOrdem;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public void setColecao(Colecao[] colecao) {
        this.colecao = colecao;
    }

    public ELetra getTipoLetra() {
        return tipoLetra;
    }

    public void setTipoLetra(ELetra tipoLetra) {
        this.tipoLetra = tipoLetra;
    }
}
