package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.parametro.ParametroException;

/**
 * Classe que define um filtro do tipo texto.
 *
 * @author Pedro H. Lira
 */
public class FiltroTexto extends AFiltro<String> {

    /**
     * @see AFiltro#AFiltro()
     */
    public FiltroTexto() {
        super();
    }

    /**
     * @see AFiltro#AFiltro(String, ECompara, String)
     */
    public FiltroTexto(String campo, ECompara compara, String valor) {
        super.campo = campo;
        super.compara = compara;
        super.valor = valor;
    }

    @Override
    public String getSql() throws ParametroException {
        if (compara != ECompara.MAIOR && compara != ECompara.MAIOR_IGUAL && compara != ECompara.MENOR && compara != ECompara.MENOR_IGUAL) {
            tratarPrefixo();
            return "UPPER(" + prefixo + campo + ") " + compara.toString() + " :" + getCampoId();
        } else {
            throw new ParametroException("Tipo de comparacao usada para este filtro nao suportada.");
        }
    }

    @Override
    public String getValor() {
        String retorno;

        if (valor != null) {
            switch (compara) {
                case CONTEM:
                    retorno = ("%" + valor.toUpperCase() + "%");
                    break;
                case CONTEM_FIM:
                    retorno = ("%" + valor.toUpperCase());
                    break;
                case CONTEM_INICIO:
                    retorno = (valor.toUpperCase() + "%");
                    break;
                default:
                    retorno = (valor.toUpperCase());
            }
        } else {
            retorno = null;
        }

        return retorno;
    }

    @Override
    public void setValorString(String valor) {
        super.valor = valor;
    }
}
