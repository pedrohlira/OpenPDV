package br.com.openpdv.modelo.core.parametro;

import java.util.Collection;

/**
 * Interface que define os parametros utilizados nos comandos.
 *
 * @param <E> recebe como generico uma classe.
 * @author Pedro H. Lira
 */
public interface IParametro<E> {

    /**
     * Metodo que retorna o nome do campo unico para uso na interacao de
     * valores.
     *
     * @return o nome do campo único.
     */
    public String getCampoId();

    /**
     * Metodo que retorna o prefixo do campo na tabela usado pelo filtro.
     *
     * @return o prefixo do campo.
     */
    public String getCampoPrefixo();

    /**
     * Metodo que define o prefixo do campo na tabela usado pelo filtro.
     *
     * @param prefixo o prefixo dele.
     */
    public void setCampoPrefixo(String prefixo);

    /**
     * Metodo que retorna o nome do campo na tabela usado pelo filtro.
     *
     * @return o nome do campo.
     */
    public String getCampo();

    /**
     * Metodo que define o nome do campo na tabela usado pelo filtro.
     *
     * @param campo o nome dele.
     */
    public void setCampo(String campo);

    /**
     * Metodo que retorna o valor do tipo generico informado usado pelo filtro.
     *
     * @return o valor do filtro.
     */
    public E getValor();

    /**
     * Metodo que define o valor do tipo generico informado usado pelo filtro.
     *
     * @param valor o conteudo dele
     */
    public void setValor(E valor);

    /**
     * Metodo que define o valor do tipo generico informado usado pelo filtro.
     *
     * @param valor o conteudo dele no tipo String.
     */
    public void setValorString(String valor);

    /**
     * Metodo que retorna os parametros inbutidos de forma recursiva.
     *
     * @return uma colecao de parametros tipados e definidos.
     */
    public Collection<IParametro<E>> getParametro();

    /**
     * Metodo que retorna os parametros imbutidos de forma recursiva.
     *
     * @param parametro o objeto a ser usado para pesquisa dos parametros.
     * @return uma colecao de parametros tipados e definidos.
     */
    public Collection<IParametro<E>> getParametro(IParametro<E> parametro);

    /**
     * Metodo que retorna a instrucao SQL em formato JPA.
     *
     * @return a instrucao no formato JQL.
     * @throws ParametroException dispara uma excecao caso a formatacao esteja
     * errada.
     */
    public String getSql() throws ParametroException;
}
