package br.com.openpdv.modelo.core.parametro;

import br.com.openpdv.modelo.core.OpenPdvException;

/**
 * Classe que define um parametro do tipo texto.
 *
 * @author Pedro H. Lira
 */
public class ParametroTexto extends AbstractParametro<String> {

    protected String valor;
    
    /**
     * @see AbstractParametro#AbstractParametro()
     */
    public ParametroTexto() {
        super();
    }

    /**
     * @param campo o nome do campo.
     * @param valor o valor do campo em String.
     * @see AbstractParametro#AbstractParametro(java.lang.String, java.lang.String) 
     */
    public ParametroTexto(String campo, String valor) {
        this.campo = campo;
        this.valor = valor;
    }

    @Override
    public void setValorString(String valor) throws OpenPdvException {
        this.valor = valor;
    }

    @Override
    public String getValor() {
        return this.valor;
    }

    @Override
    public void setValor(String valor) {
        this.valor = valor;
    }
}
