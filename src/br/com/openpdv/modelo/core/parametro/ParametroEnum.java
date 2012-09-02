package br.com.openpdv.modelo.core.parametro;

/**
 * Classe que define um parametro do tipo enum.
 *
 * @author Pedro H. Lira
 */
public class ParametroEnum extends AParametro<Enum> {

    /**
     * @see AParametro#AParametro()
     */
    public ParametroEnum() {
        super();
    }

    /**
     * @see AParametro#AParametro(String, Object)
     */
    public ParametroEnum(String campo, Enum valor) {
        super(campo, valor);
    }

    @Override
    public void setValorString(String valor) {
        throw new NullPointerException("Metodo nao permitido");
    }
}
