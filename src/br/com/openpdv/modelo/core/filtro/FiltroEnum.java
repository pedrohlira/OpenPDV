package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.parametro.ParametroException;

/**
 * Classe que define um filtro do tipo enum.
 *
 * @author Pedro H. Lira
 */
public class FiltroEnum extends AFiltro<Enum> {

    /**
     * @see AFiltro#AFiltro()
     */
    public FiltroEnum() {
        super();
    }

    /**
     * @see AFiltro#AFiltro(String, ECompara, Object)
     */
    public FiltroEnum(String campo, ECompara compara, Enum valor) {
        super(campo, compara, valor);
    }

    @Override
    public String getSql() throws ParametroException {
        if (compara == ECompara.IGUAL || compara == ECompara.DIFERENTE) {
            return super.getSql();
        } else {
            throw new ParametroException("Tipo de comparacao usada para este filtro nao suportada.");
        }
    }

    @Override
    public void setValorString(String valor) {
        throw new NullPointerException("Metodo nao permitido");
    }
}
