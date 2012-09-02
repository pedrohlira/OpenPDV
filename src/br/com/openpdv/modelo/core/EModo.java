package br.com.openpdv.modelo.core;

/**
 * Enumerador que define os modos de status do caixa.
 *
 * @author Pedro H. Lira
 */
public enum EModo {

    /**
     * Status que permite abrir uma nova venda.
     */
    DISPONIVEL,
    /**
     * Status que nao permite nenhuma operacao com o ECF.
     */
    INDISPONIVEL,
    /**
     * Status do caixa com uma venda em aberto.
     */
    ABERTO,
    /**
     * Status que somente permite realizar consultas e menu fiscal.
     */
    CONSULTA,
    /**
     * Status que nenhum menu fica disponivel.
     */
    OFF;
}
