package br.com.openpdv.controlador.core;

/**
 * Interface que permite chamada de funcoes assincronas.
 *
 * @author Pedro H. Lira
 * @param <T> o tipo de retorno usado.
 */
public interface AsyncCallback<T> {

    /**
     * Metodo disparado quando a resposta da acao obteve sucesso.
     *
     * @param resultado um objeto do tipo passado.
     */
    public void sucesso(T resultado);

    /**
     * Metodo disparado quando a resposta da acao falhou.
     *
     * @param excecao um objeto de excecao disparada.
     */
    public void falha(Exception excecao);
}
