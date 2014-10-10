package br.com.openpdv.modelo.core;

import br.com.openpdv.modelo.core.filtro.Filtro;
import br.com.openpdv.modelo.core.parametro.Parametro;

/**
 * Classe de abstrae os dados das classes POJOs que representam os dados das
 * tabelas.
 *
 * @author Pedro H. Lira
 */
public class Sql<E extends Dados>{

    private E classe;
    private EComandoSQL comando;
    private Parametro parametro;
    private Filtro filtro;

    /**
     * Construtor padrão.
     */
    public Sql() {
        this(null, null);
    }

    /**
     * Construtor que define as valores padrões de cada classe POJO.
     *
     * @param classe a classe de dados.
     * @param comando o comando a ser executado.
     */
    public Sql(E classe, EComandoSQL comando) {
        this(classe, comando, null, null);
    }

    /**
     * Construtor que define as valores padrões de cada classe POJO.
     *
     * @param classe a classe de dados.
     * @param comando o comando a ser executado.
     * @param filtro o filtro utilizado pelo comando para agir somente nos
     * registros especificos.
     */
    public Sql(E classe, EComandoSQL comando, Filtro filtro) {
        this(classe, comando, filtro, null);
    }

    /**
     * Construtor que define as valores padrões de cada classe POJO.
     *
     * @param classe a classe de dados.
     * @param comando o comando a ser executado.
     * @param filtro o filtro utilizado pelo comando para agir somente nos
     * registros especificos.
     * @param parametro o parametro utilizado para atualização dos dados.
     */
    public Sql(E classe, EComandoSQL comando, Filtro filtro, Parametro parametro) {
        this.classe = classe;
        this.comando = comando;
        this.filtro = filtro;
        this.parametro = parametro;
    }

    // GETs e SETs
    public void setClasse(E classe) {
        this.classe = classe;
    }

    public E getClasse() {
        return classe;
    }

    public EComandoSQL getComando() {
        return comando;
    }

    public void setComando(EComandoSQL comando) {
        this.comando = comando;
    }

    public Filtro getFiltro() {
        return filtro;
    }

    public void setFiltro(Filtro filtro) {
        this.filtro = filtro;
    }

    public Parametro getParametro() {
        return parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }
}
