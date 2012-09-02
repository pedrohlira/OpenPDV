package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.parametro.ParametroException;

/**
 * Classe que define um filtro do tipo campo.
 *
 * @author Pedro H. Lira
 */
public class FiltroCampo extends AFiltro<String> {

    /**
     * @see AFiltro#AFiltro()
     */
    public FiltroCampo() {
        super();
    }

    /**
     * @see AFiltro#AFiltro(String, ECompara, String)
     */
    public FiltroCampo(String campo, ECompara compara, String campo2) {
        super.campo = campo;
        super.compara = compara;
        super.valor = campo2;
    }

    @Override
    public String getSql() throws ParametroException {
        if (compara != ECompara.NULO && compara != ECompara.VAZIO) {
            return campo + " " + compara.toString() + " " + valor;
        } else {
            throw new ParametroException("Tipo de comparacao usada para este filtro nao suportada.");
        }
    }

    @Override
    public void setValorString(String valor) {
        super.valor = valor;
    }
}
