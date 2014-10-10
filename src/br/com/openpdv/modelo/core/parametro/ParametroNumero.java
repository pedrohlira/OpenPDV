package br.com.openpdv.modelo.core.parametro;

import br.com.openpdv.modelo.core.OpenPdvException;

/**
 * Classe que define um parametro do tipo numero.
 *
 * @author Pedro H. Lira
 */
public class ParametroNumero extends AbstractParametro<Number> {

    protected Number valor;

    /**
     * @see AbstractParametro#AbstractParametro()
     */
    public ParametroNumero() {
        super();
    }

    /**
     * @param campo o nome do campo.
     * @param valor o valor do campo em Number.
     * @see AbstractParametro#AbstractParametro(java.lang.String, java.io.Serializable)
     */
    public ParametroNumero(String campo, Number valor) {
        super(campo, valor);
    }

    /**
     * @param campo o nome do campo.
     * @param valor o valor do campo em String.
     * @see AbstractParametro#AbstractParametro(java.lang.String, java.lang.String)
     */
    public ParametroNumero(String campo, String valor) {
        super(campo, valor);
    }

    @Override
    public void setValorString(String valor) throws OpenPdvException {
        if (valor == null) {
            setValor(0);
        } else if (valor.indexOf(".") > 0) {
            setValor(Double.valueOf(valor));
        } else {
            setValor(Long.valueOf(valor));
        }
    }

    @Override
    public Number getValor() {
        return this.valor;
    }

    @Override
    public void setValor(Number valor) {
        this.valor = valor;
    }
}
