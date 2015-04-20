package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.phdss.Util;

/**
 * Classe que define um filtro do tipo texto.
 *
 * @author Pedro H. Lira
 */
public class FiltroTexto extends AbstractFiltro<String> {

    protected String valor;
    
    /**
     * @see AbstractFiltro#AbstractFiltro()
     */
    public FiltroTexto() {
        super();
    }

    /**
     * @param campo o nome do campo.
     * @param compara o tipo de comparacao usada.
     * @param valor o valor do filtro em String.
     * @see AbstractFiltro#AbstractFiltro(java.lang.String, plugin.modelo.filtro.ECompara, java.lang.String) 
     */
    public FiltroTexto(String campo, ECompara compara, String valor) {
        this.campo = campo;
        this.compara = compara;
        this.valor = valor;
    }

    @Override
    public String getSql() throws OpenPdvException {
        if (compara != ECompara.MAIOR && compara != ECompara.MAIOR_IGUAL && compara != ECompara.MENOR && compara != ECompara.MENOR_IGUAL) {
            return "UPPER(" + Util.tratarPrefixo(campo) + ") " + compara.toString() + " :" + getCampoId();
        } else {
            throw new OpenPdvException("O tipo de comparacao usado nao e aceito!");
        }
    }

    @Override
    public void setValorString(String valor) throws OpenPdvException {
        this.valor = valor;
    }

    @Override
    public String getValor() {
        String retorno;

        if (valor != null) {
            if (compara == ECompara.CONTEM) {
                retorno = ("%" + valor.toUpperCase() + "%");
            } else if (compara == ECompara.CONTEM_FIM) {
                retorno = ("%" + valor.toUpperCase());
            } else if (compara == ECompara.CONTEM_INICIO) {
                retorno = (valor.toUpperCase() + "%");
            } else {
                retorno = (valor.toUpperCase());
            }
        } else {
            retorno = null;
        }

        return retorno;
    }

    @Override
    public void setValor(String valor) {
        this.valor = valor;
    }
}