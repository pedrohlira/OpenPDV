package br.com.openpdv.modelo.core.parametro;

import java.util.*;

/**
 * Classe que representa um agrupamento de parametros, podendo ter parametros ou
 * mesmo grupos de parametros.
 *
 * @author Pedro H. Lira
 */
public class GrupoParametro implements IParametro {

    private List<IParametro> parametros = new LinkedList<>();

    /**
     * Construtor padrão.
     */
    public GrupoParametro() {
    }

    /**
     * Construtor passando os parametros.
     *
     * @param parametros um array de parametros.
     */
    public GrupoParametro(IParametro[] parametros) {
        if (parametros != null) {
            for (IParametro par : parametros) {
                add(par);
            }
        }
    }

    /**
     * Metodo que remove um parametro ou grupo.
     *
     * @param parametro do tipo IParametro.
     */
    public void remove(IParametro parametro) {
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
    public void add(IParametro parametro) {
        parametros.add(parametro);
    }

    /**
     * Metodo que fornece uma interação nos objetos de parametros.
     *
     * @return um interador de IParametro.
     */
    public Iterator<IParametro> iterator() {
        return parametros.iterator();
    }

    /**
     * Metodo que fornece uma array de IParametro.
     *
     * @return um array de IParametro.
     */
    public IParametro[] toArray() {
        return parametros.toArray(new IParametro[]{});
    }

    @Override
    public String getSql() throws ParametroException {
        StringBuilder sql = new StringBuilder();
        for (IParametro par : parametros) {
            sql.append(par.getSql()).append(", ");
        }
        return sql.substring(0, sql.length() - 2);
    }

    @Override
    public Collection<IParametro> getParametro() {
        return getParametro(this);
    }

    @Override
    public Collection<IParametro> getParametro(IParametro filtro) {
        Collection<IParametro> params = new ArrayList<>();
        GrupoParametro gf = (GrupoParametro) filtro;

        for (Iterator<IParametro> it = gf.iterator(); it.hasNext();) {
            IParametro par = it.next();
            params.addAll(par.getParametro(par));
        }

        return params;
    }

    // GETs e SETs
    @Override
    public String getCampo() {
        return null;
    }

    @Override
    public void setCampo(String campo) {
    }

    @Override
    public Object getValor() {
        return null;
    }

    @Override
    public void setValor(Object valor) {
    }

    @Override
    public String getCampoId() {
        return null;
    }

    @Override
    public void setValorString(String valor) {
    }

    @Override
    public String getCampoPrefixo() {
        return null;
    }

    @Override
    public void setCampoPrefixo(String prefixo) {
    }
}
