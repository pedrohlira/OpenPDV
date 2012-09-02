package br.com.openpdv.modelo.core.filtro;

/**
 * Enumerador que define as juncoes dos filtros nas condicoes de filtragem das
 * listagens.
 *
 * @author Pedro H. Lira
 */
public enum EJuncao {

    /**
     * Campo do tipo interseccao, onde as condicoes anterior e posteior devem
     * ser satisfeitas.
     */
    E,
    /**
     * Campo do tipo uniao, onde uma das duas condicoes anterior ou posterior
     * deve ser satisfeita.
     */
    OU;

    /**
     * Metodo que retorna no formato JQL a juncao do enumerador.
     *
     * @return uma string no padrao de JQL.
     */
    @Override
    public String toString() {
        return this == E ? "AND" : "OR";
    }
}
