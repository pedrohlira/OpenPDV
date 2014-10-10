package br.com.openpdv.modelo.core.parametro;

import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.phdss.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Classe que abstrai as implementacoes do parametro definindos todos os metodos
 * com funcionalidades padronizadas.
 *
 * @param <E> usando generico para tipar o modelo de parametro usado.
 * @author Pedro H. Lira
 */
public abstract class AbstractParametro<E extends Serializable> implements Parametro<E> {

    /**
     * geracao de um identificador unico para o campo do filtro.
     */
    public final int campoId = (int) (Math.random() * Integer.MAX_VALUE);

    /**
     * Campo contendo o nome do campo do filtro.
     */
    protected String campo;

    /**
     * Construtor padrao.
     */
    public AbstractParametro() {
    }

    /**
     * Construtor que define o campo e o valor.
     *
     * @param campo o nome do campo.
     * @param valor o valor do campo em String.
     */
    public AbstractParametro(String campo, String valor) {
        this.campo = campo;
        try {
            setValorString(valor);
        } catch (OpenPdvException e) {
            this.setValor(null);
        }
    }

    /**
     * Construtor que define o campo e o valor.
     *
     * @param campo o nome do campo.
     * @param valor o valor do campo.
     */
    public AbstractParametro(String campo, E valor) {
        this.campo = campo;
        this.setValor(valor);
    }

    @Override
    public String getCampoId() {
        return campo.replace('.', '_') + "_" + campoId;
    }

    @Override
    public String getCampo() {
        return campo;
    }

    @Override
    public void setCampo(String campo) {
        this.campo = campo;
    }

    @Override
    public String getSql() throws OpenPdvException {
        return Util.tratarPrefixo(campo) + " = :" + getCampoId();
    }

    @Override
    public Collection<Parametro<E>> getParametro() {
        return getParametro(this);
    }

    @Override
    public Collection<Parametro<E>> getParametro(Parametro<E> filtro) {
        Collection<Parametro<E>> parametros = new ArrayList<>();
        parametros.add(filtro);
        return parametros;
    }
}
