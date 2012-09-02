package br.com.openpdv.modelo.core.parametro;

import br.com.openpdv.modelo.core.Dados;

/**
 * Classe que define um parametro do tipo objeto.
 *
 * @author Pedro H. Lira
 */
public class ParametroObjeto extends AParametro<Dados> {

    /**
     * @see AParametro#AParametro()
     */
    public ParametroObjeto() {
        super();
    }

    /**
     * @see AParametro#AParametro(String, Object)
     */
    public ParametroObjeto(String campo, Dados valor) {
        super(campo, valor);
    }

    @Override
    public void setValorString(String valor) {
        throw new NullPointerException("Metodo nao permitido");
    }
}
