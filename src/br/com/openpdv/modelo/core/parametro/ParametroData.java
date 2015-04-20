package br.com.openpdv.modelo.core.parametro;

import br.com.openpdv.modelo.core.OpenPdvException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.persistence.TemporalType;

/**
 * Classe que define um parametro do tipo data.
 *
 * @author Pedro H. Lira
 */
public class ParametroData extends AbstractParametro<Date> {

    protected Date valor;

    /**
     * @see AbstractParametro#AbstractParametro() 
     */
    public ParametroData() {
        super();
    }

    /**
     * @param campo o nome do campo.
     * @param valor o valor do campo em Date.
     * @see AbstractParametro#AbstractParametro(java.lang.String, java.io.Serializable) 
     */
    public ParametroData(String campo, Date valor) {
        super(campo, valor);
    }

    /**
     * @param campo o nome do campo.
     * @param valor o valor do campo em String.
     * @see AbstractParametro#AbstractParametro(java.lang.String, java.lang.String) 
     */
    public ParametroData(String campo, String valor) {
        super(campo, valor);
    }

    @Override
    public void setValorString(String valor) throws OpenPdvException {
        Date data = null;
        try {
            if (valor != null && valor.contains("/") && valor.contains(":")) {
                data = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss", Locale.US).parse(valor);
            } else if (valor != null && valor.contains("/")) {
                data = new SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(valor);
            } else if (valor != null && valor.contains(":")) {
                data = new SimpleDateFormat("hh:mm:ss", Locale.US).parse(valor);
            }
        } catch (ParseException ex) {
            data = null;
        }
        setValor(data);
    }

    @Override
    public Date getValor() {
        return this.valor;
    }

    @Override
    public void setValor(Date valor) {
        this.valor = valor;
    }
}
