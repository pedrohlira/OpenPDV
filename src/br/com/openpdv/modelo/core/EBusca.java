package br.com.openpdv.modelo.core;

/**
 * Enumerador que define os meios de busca aritmética no banco de dados.
 * <p/>
 * @author Pedro H. Lira
 */
public enum EBusca {

    /**
     * Campo que define como SUM busca.
     */
    SOMA,
    /**
     * Campo que define como AVG busca.
     */
    MEDIA,
    /**
     * Campo que define como MAX busca.
     */
    MAXIMO,
    /**
     * Campo que define como MIN busca.
     */
    MINIMO,
    /**
     * Campo que define como COUNT busca.
     */
    CONTAGEM;

    /**
     * Metodo que retorna no formato JQL a busca do enumerador.
     * <p/>
     * @return uma string no padrao de JQL.
     */
    @Override
    public String toString() {
        switch (this) {
            case SOMA:
                return "SUM";
            case MEDIA:
                return "AVG";
            case MAXIMO:
                return "MAX";
            case MINIMO:
                return "MIN";
            default:
                return "COUNT";
        }
    }

    /**
     * Metodo que retorna o tipo de Busca pela string.
     * <p/>
     * @param tipo a string que representa a busca.
     * <p/>
     * @return o EBusca correspondente a string.
     */
    public static EBusca getBusca(String tipo) {
        if (tipo.equalsIgnoreCase("soma") || tipo.equalsIgnoreCase("sum")) {
            return EBusca.SOMA;
        } else if (tipo.equalsIgnoreCase("media") || tipo.equalsIgnoreCase("avg")) {
            return EBusca.MEDIA;
        } else if (tipo.equalsIgnoreCase("maximo") || tipo.equalsIgnoreCase("max")) {
            return EBusca.MAXIMO;
        } else if (tipo.equalsIgnoreCase("minimo") || tipo.equalsIgnoreCase("min")) {
            return EBusca.MINIMO;
        } else if (tipo.equalsIgnoreCase("contagem") || tipo.equalsIgnoreCase("count")) {
            return EBusca.CONTAGEM;
        } else {
            return null;
        }
    }
}