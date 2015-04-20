package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.OpenPdvException;

/**
 * Classe que define um filtro do tipo boleano.
 *
 * @author Pedro H. Lira
 */
public class FiltroBinario extends AbstractFiltro<Boolean> {

    protected Boolean valor;

    /**
     * @see AbstractFiltro#AbstractFiltro()
     */
    public FiltroBinario() {
        super();
    }

    /**
     * @param campo o nome do campo.
     * @param compara o tipo de comparacao usada.
     * @param valor o valor do filtro em Boolean.
     * @see AbstractFiltro#AbstractFiltro(java.lang.String, plugin.modelo.filtro.ECompara, java.io.Serializable)
     */
    public FiltroBinario(String campo, ECompara compara, Boolean valor) {
        super(campo, compara, valor);
    }

    /**
     * @param campo o nome do campo.
     * @param compara o tipo de comparacao usada.
     * @param valor o valor do filtro em String.
     * @see AbstractFiltro#AbstractFiltro(java.lang.String, plugin.modelo.filtro.ECompara, java.lang.String)
     */
    public FiltroBinario(String campo, ECompara compara, String valor) {
        super(campo, compara, valor);
    }

    @Override
    public String getSql() throws OpenPdvException {
        if (compara == ECompara.IGUAL || compara == ECompara.DIFERENTE) {
            return super.getSql();
        } else {
            throw new OpenPdvException("O tipo de comparacao usado nao e aceito!");
        }
    }

    @Override
    public void setValorString(String valor) throws OpenPdvException {
        setValor(Boolean.valueOf(valor));
    }

    @Override
    public Boolean getValor() {
        return this.valor;
    }

    @Override
    public void setValor(Boolean valor) {
        this.valor = valor;
    }
}
