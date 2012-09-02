package br.com.openpdv.modelo.core.parametro;

/**
 * Classe que define um parametro do tipo boleano.
 *
 * @author Pedro H. Lira
 */
public class ParametroBinario extends AParametro<Boolean> {

    /**
     * @see AParametro#AParametro()
     */
    public ParametroBinario() {
        super();
    }

    /**
     * @see AParametro#AParametro(String, String)
     */
    public ParametroBinario(String campo, String valor) {
        super(campo, valor);
    }

    /**
     * @see AParametro#AParametro(String, Object)
     */
    public ParametroBinario(String campo, boolean valor) {
        super(campo, valor);
    }

    @Override
    public void setValorString(String valor) {
        if (valor == null || valor.equalsIgnoreCase("false")) {
            super.setValor(false);
        } else {
            super.setValor(true);
        }
    }
}
