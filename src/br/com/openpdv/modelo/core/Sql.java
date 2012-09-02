package br.com.openpdv.modelo.core;

import br.com.openpdv.modelo.core.filtro.IFiltro;
import br.com.openpdv.modelo.core.parametro.IParametro;

/**
 * Classe de abstrae os dados das classes POJOs que representam os dados das
 * tabelas.
 *
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Sql<E extends Dados>{

    private E classe;
    private EComandoSQL comando;
    private IParametro parametro;
    private IFiltro filtro;

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
    public Sql(E classe, EComandoSQL comando, IFiltro filtro) {
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
    public Sql(E classe, EComandoSQL comando, IFiltro filtro, IParametro parametro) {
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

    public IFiltro getFiltro() {
        return filtro;
    }

    public void setFiltro(IFiltro filtro) {
        this.filtro = filtro;
    }

    public IParametro getParametro() {
        return parametro;
    }

    public void setParametro(IParametro parametro) {
        this.parametro = parametro;
    }
}
