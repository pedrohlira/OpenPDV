package br.com.openpdv.controlador.core;

/**
 * Interface que permite chamada de funcoes assincronas para produtos.
 *
 * @author Pedro H. Lira
 * @param <E> o tipo referenciado.
 * @param <T> o tipo de retorno usado.
 */
public interface AsyncDoubleBack<E, T> {

    /**
     * Metodo disparado quando a resposta da acao obteve sucesso.
     *
     * @param referencia um objeto do tipo generico.
     * @param resultado um objeto do tipo generico.
     */
    public void sucesso(E referencia, T resultado);

    /**
     * Metodo disparado quando a resposta da acao falhou.
     *
     * @param excecao um objeto de excecao disparada.
     */
    public void falha(Exception excecao);
}
