package br.com.openpdv.modelo.core.filtro;

/**
 * Enumerador que define os tipos de comparacoes usados pelos filtros nas
 * listagens.
 *
 * @author Pedro H. Lira
 */
public enum ECompara {

    /**
     * Informa que o valor do campo deve ser
     * <code>=</code> ao valor informado.
     */
    IGUAL,
    /**
     * Informa que o valor do campo deve ser
     * <code>!=</code> ao valor informado.
     */
    DIFERENTE,
    /**
     * Informa que o valor do campo deve ser
     * <code>></code> ao valor informado.
     */
    MAIOR,
    /**
     * Informa que o valor do campo deve ser
     * <code><</code> ao valor informado.
     */
    MENOR,
    /**
     * Informa que o valor do campo deve ser
     * <code>>=</code> ao valor informado.
     */
    MAIOR_IGUAL,
    /**
     * Informa que o valor do campo deve ser
     * <code><=</code> ao valor informado.
     */
    MENOR_IGUAL,
    /**
     * Informa que o valor do campo deve ser
     * <code>LIKE</code> ao valor informado.
     */
    CONTEM,
    /**
     * Informa que o valor do campo deve ser
     * <code>LIKE %</code> ao valor informado.
     */
    CONTEM_INICIO,
    /**
     * Informa que o valor do campo deve ser
     * <code>% LIKE</code> ao valor informado.
     */
    CONTEM_FIM,
    /**
     * Informa que o valor do campo deve ser
     * <code>IS NULL</code>.
     */
    NULO,
    /**
     * Informa que o valor do campo deve ser
     * <code>IS EMPTY</code>.
     */
    VAZIO;

    /**
     * Metodo que retorna no formato JQL a comparacao do enumerador.
     *
     * @return uma string no padrao de JQL.
     */
    @Override
    public String toString() {
        switch (this) {
            case IGUAL:
                return "=";
            case DIFERENTE:
                return "<>";
            case MAIOR:
                return ">";
            case MENOR:
                return "<";
            case MAIOR_IGUAL:
                return ">=";
            case MENOR_IGUAL:
                return "<=";
            case NULO:
                return "IS NULL";
            case VAZIO:
                return "IS EMPTY";
            default:
                return "LIKE";
        }
    }

    /**
     * Metodo que retorna o tipo de comparacao baseado no modelo html.
     *
     * @param valor formato de html para o tipo de comparacao.
     * @return um tipo de ECompara.
     */
    public static ECompara toCompara(String valor) {
        switch (valor.toLowerCase()) {
            case "lt":
                return MENOR;
            case "gt":
                return MAIOR;
            default:
                return IGUAL;
        }
    }
}