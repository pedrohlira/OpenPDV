package br.com.openpdv.modelo.core.parametro;

import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.phdss.Util;

/**
 * Classe que define um parametro do tipo numero que faz uso do proprio valor na atualizacao.
 *
 * @author Pedro H. Lira
 */
public final class ParametroFormula extends ParametroNumero {

    private String campo2;
    private String oper;

    /**
     * @see ParametroNumero#ParametroNumero()
     */
    public ParametroFormula() {
        super();
    }

    /**
     * @param campo o nome do campo.
     * @param valor o valor do campo em Number.
     * @see ParametroNumero#ParametroNumero(java.lang.String, java.lang.Number)
     */
    public ParametroFormula(String campo, Number valor) {
        this(campo, valor.toString());
    }

    /**
     * @param campo o nome do campo.
     * @param valor o valor do campo em String.
     * @see ParametroNumero#ParametroNumero(java.lang.String, java.lang.String)
     */
    public ParametroFormula(String campo, String valor) {
        this(campo, campo, valor);
    }

    /**
     * @param campo o nome do campo.
     * @param campo2 o nome do segundo campo.
     * @param valor o valor do campo em Number.
     * @see #ParametroFormula(java.lang.String, java.lang.String, java.lang.String) 
     */
    public ParametroFormula(String campo, String campo2, Number valor) {
        this(campo, campo2, valor.toString());
    }

    /**
     * Construtor padrao da Formula.
     *
     * @param campo o nome do campo.
     * @param campo2 o nome do segundo campo.
     * @param valor o valor do campo em String.
     */
    public ParametroFormula(String campo, String campo2, String valor) {
        this.campo = campo;
        this.campo2 = campo2;
        try {
            setValorString(valor);
        } catch (OpenPdvException e) {
            this.valor = null;
        }
    }

    @Override
    public String getSql() throws OpenPdvException {
        return Util.tratarPrefixo(campo) + " = " + Util.tratarPrefixo(campo2) + oper + getCampoId();
    }

    @Override
    public void setValorString(String valor) throws OpenPdvException {
        String op = valor.substring(0, 1);
        if (!(op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/"))) {
            op = "+";
        }
        valor = valor.replace(op, "");
        oper = op + ":";
        super.setValorString(valor);
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }

}
