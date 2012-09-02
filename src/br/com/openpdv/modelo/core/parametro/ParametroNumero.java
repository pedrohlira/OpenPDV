package br.com.openpdv.modelo.core.parametro;

/**
 * Classe que define um parametro do tipo numero.
 *
 * @author Pedro H. Lira
 */
public class ParametroNumero extends AParametro<Number> {

    /**
     * @see AParametro#AParametro()
     */
    public ParametroNumero() {
        super();
    }

    /**
     * @see AParametro#AParametro(String, String)
     */
    public ParametroNumero(String campo, String valor) {
        super(campo, valor);
    }

    /**
     * @see AParametro#AParametro(String, Object)
     */
    public ParametroNumero(String campo, Number valor) {
        super(campo, valor);
    }

    @Override
    public void setValorString(String valor) {
        if (valor == null) {
            super.setValor(0);
        } else if (valor.indexOf(".") > 0) {
            super.setValor(Float.valueOf(valor));
        } else {
            super.setValor(Integer.valueOf(valor));
        }
    }
}
