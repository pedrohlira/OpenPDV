package br.com.openpdv.modelo.core.parametro;

import br.com.openpdv.modelo.core.OpenPdvException;

/**
 * Classe que define um parametro do tipo boleano.
 *
 * @author Pedro H. Lira
 */
public class ParametroBinario extends AbstractParametro<Boolean> {

    protected Boolean valor;

    /**
     * @see AbstractParametro#AbstractParametro()
     */
    public ParametroBinario() {
        super();
    }

    /**
     * @param campo o nome do campo.
     * @param valor o valor do campo em Boolean.
     * @see AbstractParametro#AbstractParametro(java.lang.String, java.io.Serializable)
     */
    public ParametroBinario(String campo, Boolean valor) {
        super(campo, valor);
    }

    /**
     * @param campo o nome do campo.
     * @param valor o valor do campo em String.
     * @see AbstractParametro#AbstractParametro(java.lang.String, java.lang.String)
     */
    public ParametroBinario(String campo, String valor) {
        super(campo, valor);
    }

    @Override
    public void setValorString(String valor) throws OpenPdvException {
        setValor(valor != null && valor.equals("1"));
    }

    @Override
    public Boolean getValor() {
        return this.valor;
    }

    @Override
    public void setValor(Boolean valor) {
        this.valor = valor;
    }
}
