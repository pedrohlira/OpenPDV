package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.parametro.ParametroException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe que define um filtro do tipo data.
 *
 * @author Pedro H. Lira
 */
public class FiltroData extends AFiltro<Date> {

    /**
     * @see AFiltro#AFiltro()
     */
    public FiltroData() {
        super();
    }

    /**
     * @see AFiltro#AFiltro(String, ECompara, String)
     */
    public FiltroData(String campo, ECompara compara, String valor) {
        super(campo, compara, valor);
    }

    /**
     * @see AFiltro#AFiltro(String, ECompara, Object)
     */
    public FiltroData(String campo, ECompara compara, Date valor) {
        super(campo, compara, valor);
    }

    @Override
    public String getSql() throws ParametroException {
        if (compara != ECompara.CONTEM && compara != ECompara.CONTEM_FIM && compara != ECompara.CONTEM_INICIO) {
            return super.getSql();
        } else {
            throw new ParametroException("Tipo de comparacao usada para este filtro nao suportada.");
        }
    }

    @Override
    public void setValorString(String valor) {
        if (valor != null) {
            try {
                super.setValor(SimpleDateFormat.getInstance().parse(valor));
            } catch (ParseException ex) {
                throw new NullPointerException("Formato da data invalido.");
            }
        }
    }
}
