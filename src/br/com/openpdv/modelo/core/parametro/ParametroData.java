package br.com.openpdv.modelo.core.parametro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe que define um parametro do tipo data.
 *
 * @author Pedro H. Lira
 */
public class ParametroData extends AParametro<Date> {

    /**
     * @see AParametro#AParametro()
     */
    public ParametroData() {
        super();
    }

    /**
     * @see AParametro#AParametro(String, String)
     */
    public ParametroData(String campo, String valor) {
        super(campo, valor);
    }

    /**
     * @see AParametro#AParametro(String, Object)
     */
    public ParametroData(String campo, Date valor) {
        super(campo, valor);
    }

    @Override
    public void setValorString(String valor) {
        if (valor != null) {
            try {
                super.setValor(SimpleDateFormat.getDateInstance().parse(valor));
            } catch (ParseException ex) {
                throw new NullPointerException("Data invalida passada");
            }
        }
    }
}
