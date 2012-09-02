package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.parametro.ParametroException;

/**
 * Classe que define um filtro do tipo boleano.
 *
 * @author Pedro H. Lira
 */
public class FiltroBinario extends AFiltro<Boolean> {

    /**
     * @see AFiltro#AFiltro()
     */
    public FiltroBinario() {
        super();
    }

    /**
     * @see AFiltro#AFiltro(String, ECompara, String)
     */
    public FiltroBinario(String campo, ECompara compara, String valor) {
        super(campo, compara, valor);
    }

    /**
     * @see AFiltro#AFiltro(String, ECompara, Object)
     */
    public FiltroBinario(String campo, ECompara compara, boolean valor) {
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
        if (valor == null || valor.equalsIgnoreCase("false")) {
            super.setValor(false);
        } else {
            super.setValor(true);
        }
    }
}
