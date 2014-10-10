package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.parametro.Parametro;
import java.io.Serializable;

/**
 * Interface que define os filtros utilizados nas listagens.
 *
 * @param <E> recebe como generico uma classe.
 * @author Pedro H. Lira
 */
/**
 * Interface que define os filtros utilizados nas listagens.
 *
 * @param <E> recebe como generico uma classe serializavel.
 * @author Pedro H. Lira
 */
public interface Filtro<E extends Serializable> extends Parametro<E> {

    /**
     * Constante que faz a juncao de filtros por concatenacao.
     */
    public String E = "AND";
    /**
     * Constante que faz a juncao de filtros por uniao.
     */
    public String OU = "OR";

    /**
     * Metodo que retorna o tipo de juncao usada no filtro.
     *
     * @return a juncao usada.
     */
    public String getJuncao();

    /**
     * Metodo que define o tipo de juncao usada no filtro.
     *
     * @param juncao o tipo de juncao.
     */
    public void setJuncao(String juncao);

    /**
     * Metodo que retorna o tipo de comparacao usada no filtro.
     *
     * @return a comparacao usada.
     */
    public ECompara getCompara();

    /**
     * Metodo que define o tipo de comparacao usada no filtro.
     *
     * @param compara o tipo de comparacao.
     */
    public void setCompara(ECompara compara);
}
