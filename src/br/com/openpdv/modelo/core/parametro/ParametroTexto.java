package br.com.openpdv.modelo.core.parametro;

/**
 * Classe que define um parametro do tipo texto.
 *
 * @author Pedro H. Lira
 */
public class ParametroTexto extends AParametro<String> {

    /**
     * @see AParametro#AParametro()
     */
    public ParametroTexto() {
        super();
    }

    /**
     * @see AParametro#AParametro(String, String)
     */
    public ParametroTexto(String campo, String valor) {
        super.campo = campo;
        super.valor = valor;
    }

    @Override
    public void setValorString(String valor) {
        super.valor = valor;
    }
}
