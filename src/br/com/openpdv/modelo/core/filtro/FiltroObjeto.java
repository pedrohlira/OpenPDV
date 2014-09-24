package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.phdss.Util;

/**
 * Classe que define um filtro do tipo objeto.
 *
 * @author Pedro H. Lira
 */
public class FiltroObjeto extends AbstractFiltro<Dados> {

    protected Dados valor;

    /**
     * @see AbstractFiltro#AbstractFiltro()
     */
    public FiltroObjeto() {
        super();
    }

    /**
     * @see AbstractFiltro#AbstractFiltro(java.lang.String,
     * br.com.openpdv.modelo.core.filtro.ECompara, java.lang.Object)
     * @param campo o nome do campo.
     * @param compara o tipo de comparacao usada.
     * @param valor o valor do filtro.
     */
    public FiltroObjeto(String campo, ECompara compara, Dados valor) {
        super(campo, compara, valor);
    }

    @Override
    public String getSql() throws OpenPdvException {
        if (compara == ECompara.IGUAL || compara == ECompara.DIFERENTE) {
            return super.getSql();
        } else if (compara == ECompara.NULO || compara == ECompara.VAZIO) {
            return Util.tratarPrefixo(campo) + " " + compara.toString();
        } else {
            throw new OpenPdvException("Tipo de comparacao usada para este filtro nao suportada.");
        }
    }

    @Override
    public void setValorString(String valor) {
        throw new NullPointerException("Metodo nao permitido");
    }

    @Override
    public Dados getValor() {
        return valor;
    }

    @Override
    public void setValor(Dados valor) {
        this.valor = valor;
    }

}
