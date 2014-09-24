package br.com.openpdv.modelo.core.parametro;

import br.com.openpdv.modelo.core.OpenPdvException;
import java.io.Serializable;
import java.util.*;

/**
 * Classe que representa um agrupamento de parametros, podendo ter parametros ou
 * mesmo grupos de parametros.
 *
 * @author Pedro H. Lira
 */
public final class ParametroGrupo extends AbstractParametro {

    private final List<Parametro> parametros = new LinkedList<>();

    /**
     * Construtor padrao.
     */
    public ParametroGrupo() {
    }

    /**
     * Construtor passando os parametros.
     *
     * @param parametros um array de parametros.
     */
    public ParametroGrupo(Parametro... parametros) {
        if (parametros != null) {
            for (Parametro par : parametros) {
                add(par);
            }
        }
    }

    /**
     * Metodo que remove um parametro ou grupo.
     *
     * @param parametro do tipo IParametro.
     */
    public void remove(Parametro parametro) {
        parametros.remove(parametro);
    }

    /**
     * Metodo que limpa os parametros do grupo.
     */
    public void clear() {
        parametros.clear();
    }

    /**
     * Metodo que retorna a quantidade de parametros dentro do grupo.
     *
     * @return quantidade de filtros.
     */
    public int size() {
        return parametros.size();
    }

    /**
     * Metodo que adiciona um parametro ou grupo filtro final.
     *
     * @param parametro do tipo IParametro.
     */
    public void add(Parametro parametro) {
        parametros.add(parametro);
    }

    /**
     * Metodo que fornece uma interacao nos objetos de parametros.
     *
     * @return um interador de IParametro.
     */
    public Iterator<Parametro> iterator() {
        return parametros.iterator();
    }

    /**
     * Metodo que fornece uma array de IParametro.
     *
     * @return um array de IParametro.
     */
    public Parametro[] toArray() {
        return parametros.toArray(new Parametro[]{});
    }

    @Override
    public String getSql() throws OpenPdvException {
        StringBuilder sql = new StringBuilder();
        for (Parametro par : parametros) {
            sql.append(par.getSql()).append(", ");
        }
        return sql.substring(0, sql.length() - 2);
    }

    @Override
    public Collection<Parametro> getParametro() {
        return getParametro(this);
    }

    @Override
    public Collection<Parametro> getParametro(Parametro filtro) {
        Collection<Parametro> params = new ArrayList<>();
        ParametroGrupo gf = (ParametroGrupo) filtro;

        for (Iterator<Parametro> it = gf.iterator(); it.hasNext();) {
            Parametro par = it.next();
            params.addAll(par.getParametro(par));
        }

        return params;
    }

    @Override
    public Serializable getValor() {
        return null;
    }

    @Override
    public void setValor(Serializable valor) {
    }

    @Override
    public void setValorString(String valor) throws OpenPdvException {
    }

}
