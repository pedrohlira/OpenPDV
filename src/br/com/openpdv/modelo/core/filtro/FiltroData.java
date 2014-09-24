package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.OpenPdvException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Classe que define um filtro do tipo data.
 *
 * @author Pedro H. Lira
 */
public class FiltroData extends AbstractFiltro<Date> {

    protected Date valor;

    /**
     * @see AbstractFiltro#AbstractFiltro()
     */
    public FiltroData() {
        super();
    }

    /**
     * @param campo o nome do campo.
     * @param compara o tipo de comparacao usada.
     * @param valor o valor do filtro em Date.
     * @see AbstractFiltro#AbstractFiltro(java.lang.String,
     * plugin.modelo.filtro.ECompara, java.io.Serializable)
     */
    public FiltroData(String campo, ECompara compara, Date valor) {
        super(campo, compara, valor);
    }

    /**
     * @param campo o nome do campo.
     * @param compara o tipo de comparacao usada.
     * @param valor o valor do filtro em String.
     * @see AbstractFiltro#AbstractFiltro(java.lang.String,
     * plugin.modelo.filtro.ECompara, java.lang.String)
     */
    public FiltroData(String campo, ECompara compara, String valor) {
        super(campo, compara, valor);
    }

    @Override
    public String getSql() throws OpenPdvException {
        if (compara != ECompara.CONTEM && compara != ECompara.CONTEM_FIM && compara != ECompara.CONTEM_INICIO) {
            return super.getSql();
        } else {
            throw new OpenPdvException("O tipo de comparacao usado nao e aceito!");
        }
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
