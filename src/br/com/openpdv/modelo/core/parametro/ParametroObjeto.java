package br.com.openpdv.modelo.core.parametro;

import br.com.openpdv.modelo.core.Dados;

/**
 * Classe que define um parametro do tipo objeto.
 *
 * @author Pedro H. Lira
 */
public class ParametroObjeto extends AbstractParametro<Dados> {

    protected Dados valor;

    /**
     * @see AbstractParametro#AbstractParametro()
     */
    public ParametroObjeto() {
        super();
    }

    /**
     * @see AbstractParametro#AbstractParametro(java.lang.String,
     * java.lang.Object)
     * @param campo o nome do campo.
     * @param valor o valor do filtro.
     */
    public ParametroObjeto(String campo, Dados valor) {
        super(campo, valor);
    }

    @Override
    public void setValorString(String valor) {
        throw new NullPointerException("Metodo nao permitido");
    }

    @Override
    public Dados getValor() {
        return valor;
    }

    @Override
    public void setValor(Dados valor) {
        this.valor = valor;
    }
}
