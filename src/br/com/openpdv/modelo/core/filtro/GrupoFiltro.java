package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.parametro.IParametro;
import br.com.openpdv.modelo.core.parametro.ParametroException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Classe que representa um agrupamento de filtros, podendo ter filtros ou mesmo
 * grupos de filtros.
 *
 * @author Pedro H. Lira
 */
public class GrupoFiltro implements IFiltro {

    private Map<IFiltro, EJuncao> filtros = new LinkedHashMap<>();

    /**
     * Construtor padrao.
     */
    public GrupoFiltro() {
    }

    /**
     * Contrutor com multiplos filtros
     *
     * @param juncao a Juncao padrao de todos os filtros passados.
     * @param filtros multiplos filtros.
     */
    public GrupoFiltro(EJuncao juncao, IFiltro[] filtros) {
        if (filtros != null) {
            for (IFiltro fil : filtros) {
                add(fil, juncao);
            }
        }
    }

    /**
     * Metodo que remove um filtro ou grupo.
     *
     * @param filtro do tipo IFiltro.
     */
    public void remove(IFiltro filtro) {
        filtros.remove(filtro);
    }

    /**
     * Metodo que limpa os filtros do grupo.
     */
    public void clear() {
        filtros.clear();
    }

    /**
     * Metodo que retorna a quantidade de filtros dentro do grupo.
     *
     * @return quantidade de filtros.
     */
    public int size() {
        return filtros.size();
    }

    /**
     * Metodo que adiciona um filtro ou grupo sem junçao, filtro final.
     *
     * @param filtro do tipo IFiltro.
     * @throws ParametroException ocorre caso seja adicionado mais de um filtro
     * com junçao null.
     */
    public void add(IFiltro filtro) throws IllegalArgumentException {
        add(filtro, null);
    }

    /**
     * Metodo que adiciona um filtro ou grupo.
     *
     * @param filtro do tipo IFiltro.
     * @param juncao a forma de uniao com o próximo filtro, deve ser null caso
     * nao tenha outro filtro.
     * @throws ParametroException ocorre caso seja adicionado mais de um filtro
     * com junçao null.
     */
    public void add(IFiltro filtro, EJuncao juncao) throws IllegalArgumentException {
        if (juncao == null && filtros.containsValue(null)) {
            throw new IllegalArgumentException("Falta a juncao!");
        } else {
            filtros.put(filtro, juncao);
        }
    }

    /**
     * Metodo que fornece uma interaçao nos objetos de filtros.
     *
     * @return um interador de IFiltro.
     */
    public Iterator<IFiltro> iterator() {
        return filtros.keySet().iterator();
    }

    public IFiltro[] toArray() {
        return filtros.keySet().toArray(new IFiltro[]{});
    }

    @Override
    public String getSql() throws ParametroException {
        StringBuilder sql = new StringBuilder();
        if (!filtros.isEmpty()) {
            sql.append("(");

            for (Iterator<Entry<IFiltro, EJuncao>> it = filtros.entrySet().iterator(); it.hasNext();) {
                Entry<IFiltro, EJuncao> grupoFiltro = it.next();
                if (grupoFiltro.getKey() != null) {
                    sql.append(grupoFiltro.getKey().getSql());
                }
                if (grupoFiltro.getValue() != null && it.hasNext()) {
                    sql.append(" ").append(grupoFiltro.getValue().toString()).append(" ");
                }
            }

            sql.append(")");
        }

        return sql.toString();
    }

    @Override
    public Collection<IParametro> getParametro() {
        return getParametro(this);
    }

    @Override
    public Collection<IParametro> getParametro(IParametro param) {
        Collection<IParametro> parametros = new ArrayList<>();
        GrupoFiltro gf = (GrupoFiltro) param;

        for (Iterator<IFiltro> it = gf.iterator(); it.hasNext();) {
            IParametro par = it.next();
            parametros.addAll(par.getParametro(par));
        }

        return parametros;
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
    public ECompara getCompara() {
        return null;
    }

    @Override
    public void setCompara(ECompara compara) {
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
