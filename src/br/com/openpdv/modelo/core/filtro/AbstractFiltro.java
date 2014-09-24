package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.parametro.AbstractParametro;
import br.com.phdss.Util;
import java.io.Serializable;

/**
 * Classe que abstrai as implementacoes do filtro definindos todos os metodos
 * com funcionalidades padronizadas.
 *
 * @param <E> usando generico para tipar o modelo de filtro usado.
 * @author Pedro H. Lira
 */
public abstract class AbstractFiltro<E extends Serializable> extends AbstractParametro<E> implements Filtro<E> {

    /**
     * Campo contendo a juncao do filtro quando usada em grupo.
     */
    protected String juncao;
    /**
     * Campo contendo o tipo de comparacao usada no filtro.
     */
    protected ECompara compara = ECompara.IGUAL;

    /**
     * Construtor padrao.
     */
    public AbstractFiltro() {
    }

    /**
     * Construtor que define o campo, a comparacao e o valor.
     *
     * @param campo o nome do campo.
     * @param compara o tipo de comparacao usada.
     * @param valor o valor do filtro.
     */
    public AbstractFiltro(String campo, ECompara compara, String valor) {
        this.campo = campo;
        this.compara = compara;
        try {
            setValorString(valor);
        } catch (OpenPdvException e) {
            this.setValor(null);
        }
    }

    /**
     * Construtor que define o campo, a comparacao e o valor.
     *
     * @param campo o nome do campo.
     * @param compara o tipo de comparacao usada.
     * @param valor o valor do filtro.
     */
    public AbstractFiltro(String campo, ECompara compara, E valor) {
        this.campo = campo;
        this.compara = compara;
        this.setValor(valor);
    }

    @Override
    public String getSql() throws OpenPdvException {
        return Util.tratarPrefixo(campo) + " " + compara.toString() + " :" + getCampoId();
    }

    @Override
    public String getJuncao() {
        return juncao;
    }

    @Override
    public void setJuncao(String juncao) {
        this.juncao = juncao;
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

