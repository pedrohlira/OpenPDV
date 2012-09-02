package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.parametro.ParametroException;

/**
 * Classe que define um filtro do tipo numero.
 *
 * @author Pedro H. Lira
 */
public class FiltroNumero extends AFiltro<Number> {

    /**
     * @see AFiltro#AFiltro()
     */
    public FiltroNumero() {
        super();
    }

    /**
     * @see AFiltro#AFiltro(String, ECompara, String)
     */
    public FiltroNumero(String campo, ECompara compara, String valor) {
        super(campo, compara, valor);
    }

    /**
     * @see AFiltro#AFiltro(String, ECompara, Object)
     */
    public FiltroNumero(String campo, ECompara compara, Number valor) {
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
        if (valor == null) {
            super.setValor(0);
        } else if (valor.indexOf(".") > 0) {
            super.setValor(Double.valueOf(valor));
        } else {
            super.setValor(Long.valueOf(valor));
        }
    }
}
