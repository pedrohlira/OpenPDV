package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.OpenPdvException;

/**
 * Classe que define um filtro do tipo numero.
 *
 * @author Pedro H. Lira
 */
public class FiltroNumero extends AbstractFiltro<Number> {

    protected Number valor;

    /**
     * @see AbstractFiltro#AbstractFiltro()
     */
    public FiltroNumero() {
        super();
    }

    /**
     * @param campo o nome do campo.
     * @param compara o tipo de comparacao usada.
     * @param valor o valor do filtro em Number.
     * @see AbstractFiltro#AbstractFiltro(java.lang.String, plugin.modelo.filtro.ECompara, java.io.Serializable) 
     */
    public FiltroNumero(String campo, ECompara compara, Number valor) {
        super(campo, compara, valor);
    }

    /**
     * @param campo o nome do campo.
     * @param compara o tipo de comparacao usada.
     * @param valor o valor do filtro em String.
     * @see AbstractFiltro#AbstractFiltro(java.lang.String, plugin.modelo.filtro.ECompara, java.lang.String) 
     */
    public FiltroNumero(String campo, ECompara compara, String valor) {
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
        if (valor == null) {
            setValor(0);
        } else if (valor.indexOf(".") > 0) {
            setValor(Double.valueOf(valor));
        } else {
            setValor(Long.valueOf(valor));
        }
    }

    @Override
    public Number getValor() {
        return this.valor;
    }

    @Override
    public void setValor(Number valor) {
        this.valor = valor;
    }
}
