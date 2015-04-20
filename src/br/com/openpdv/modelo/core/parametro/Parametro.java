package br.com.openpdv.modelo.core.parametro;

import br.com.openpdv.modelo.core.OpenPdvException;
import java.io.Serializable;
import java.util.Collection;

/**
 * Interface que define os parametros utilizados nos comandos.
 *
 * @param <E> recebe como generico uma classe serializavel.
 * @author Pedro H. Lira
 */
public interface Parametro<E extends Serializable> extends Serializable {

    /**
     * Metodo que retorna o nome do campo unico para uso na interacao de
     * valores.
     *
     * @return o nome do campo unico.
     */
    public String getCampoId();

    /**
     * Metodo que retorna o nome do campo na tabela usado pelo filtro.
     *
     * @return o nome do campo.
     */
    public String getCampo();

    /**
     * Metodo que define o nome do campo na tabela usado pelo filtro.
     *
     * @param campo o nome do campo.
     */
    public void setCampo(String campo);

    /**
     * Metodo que retorna o valor do tipo generico informado usado pelo filtro.
     *
     * @return o valor do campo.
     */
    public E getValor();

    /**
     * Metodo que define o valor do tipo generico informado usado pelo filtro.
     *
     * @param valor o valor do campo.
     */
    public void setValor(E valor);

    /**
     * Metodo que define o valor do tipo generico informado usado pelo filtro.
     *
     * @param valor o valor do campo no tipo String.
     * @throws OpenPdvException caso nao aceite passar valor por String.
     */
    public void setValorString(String valor) throws OpenPdvException;

    /**
     * Metodo que retorna os parametros inbutidos de forma recursiva.
     *
     * @return uma colecao de parametros tipados e definidos.
     */
    public Collection<Parametro<E>> getParametro();

    /**
     * Metodo que retorna os parametros inbutidos de forma recursiva.
     *
     * @param parametro o objeto a ser usado para pesquisa dos parametros.
     * @return uma colecao de parametros tipados e definidos.
     */
    public Collection<Parametro<E>> getParametro(Parametro<E> parametro);

    /**
     * Metodo que retorna a instrucao SQL em formato JPA.
     *
     * @return a instrucao no formato JQL.
     * @throws OpenPdvException dispara uma excecao caso a formatacao esteja
     * errada.
     */
    public String getSql() throws OpenPdvException;
}