package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.parametro.IParametro;

/**
 * Interface que define os filtros utilizados nas listagens.
 *
 * @param <E> recebe como generico uma classe.
 * @author Pedro H. Lira
 */
public interface IFiltro<E> extends IParametro<E> {

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
