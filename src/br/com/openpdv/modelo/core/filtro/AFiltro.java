package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.parametro.AParametro;

/**
 * Classe que abstrai as implementacoes do filtro definindos todos os metodos
 * com funcionalidades padronizadas.
 *
 * @param <E> usando generico para tipar o modelo de filtro usado.
 * @author Pedro H. Lira
 */
public abstract class AFiltro<E> extends AParametro<E> implements IFiltro<E> {

    /**
     * Construtor padrao.
     */
    public AFiltro() {
    }

    /**
     * Construtor que define o campo, a comparacao e o valor.
     *
     * @param campo o nome do campo.
     * @param compara o tipo de comparacao usada.
     * @param valor o valor do filtro.
     */
    public AFiltro(String campo, ECompara compara, String valor) {
        this.campo = campo;
        this.compara = compara;
        setValorString(valor);
    }

    /**
     * Construtor que define o campo, a comparacao e o valor.
     *
     * @param campo o nome do campo.
     * @param compara o tipo de comparacao usada.
     * @param valor o valor do filtro.
     */
    public AFiltro(String campo, ECompara compara, E valor) {
        this.campo = campo;
        this.compara = compara;
        this.valor = valor;
    }

    @Override
    public ECompara getCompara() {
        return compara;
    }

    @Override
    public void setCompara(ECompara compara) {
        this.compara = compara;
    }
}
