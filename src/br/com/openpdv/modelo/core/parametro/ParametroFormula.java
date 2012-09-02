package br.com.openpdv.modelo.core.parametro;

/**
 * Classe que define um parametro do tipo numero que faz uso do proprio valor na
 * atualizacao.
 *
 * @author Pedro H. Lira
 */
public class ParametroFormula extends ParametroNumero {

    private String campo2;
    private String oper;

    /**
     * @see AParametro#AParametro()
     */
    public ParametroFormula() {
        super();
    }

    /**
     * @see AParametro#AParametro(String, Object)
     */
    public ParametroFormula(String campo, Number valor) {
        this(campo, valor.toString());
    }

    public ParametroFormula(String campo, String campo2, Number valor) {
        this(campo, campo2, valor.toString());
    }

    /**
     * @see AParametro#AParametro(String, String)
     */
    public ParametroFormula(String campo, String valor) {
        this(campo, campo, valor);
    }

    public ParametroFormula(String campo, String campo2, String valor) {
        this.campo = campo;
        this.campo2 = campo2;
        setValorString(valor);
    }

    @Override
    public String getSql() throws ParametroException {
        String c1 = prefixo + campo;
        String c2 = prefixo + campo2;
        return c1 + " = " + c2 + oper + getCampoId();
    }

    public void setValorString(String valor) {
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
