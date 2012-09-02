package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.core.parametro.ParametroException;

/**
 * Classe que define um filtro do tipo objeto.
 *
 * @author Pedro H. Lira
 */
public class FiltroObjeto extends AFiltro<Dados> {

    /**
     * @see AFiltro#AFiltro()
     */
    public FiltroObjeto() {
        super();
    }

    /**
     * @see AFiltro#AFiltro(String, ECompara, Object)
     */
    public FiltroObjeto(String campo, ECompara compara, Dados valor) {
        super(campo, compara, valor);
    }

    @Override
    public String getSql() throws ParametroException {
        if (compara == ECompara.IGUAL || compara == ECompara.DIFERENTE) {
            return super.getSql();
        } else if (compara == ECompara.NULO || compara == ECompara.VAZIO) {
            tratarPrefixo();
            return prefixo + campo + " " + compara.toString();
        } else {
            throw new ParametroException("Tipo de comparacao usada para este filtro nao suportada.");
        }
    }

    @Override
    public void setValorString(String valor) {
        throw new NullPointerException("Metodo nao permitido");
    }
}
