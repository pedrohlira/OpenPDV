package br.com.openpdv.modelo.core.filtro;

import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.parametro.Parametro;
import java.io.Serializable;
import java.util.*;

/**
 * Classe que representa um agrupamento de filtros, podendo ter filtros ou mesmo grupos de filtros.
 *
 * @author Pedro H. Lira
 */
public final class FiltroGrupo extends AbstractFiltro {

    private final List<Filtro> filtros = new LinkedList<>();

    /**
     * Construtor padrao.
     */
    public FiltroGrupo() {
    }

    /**
     * Contrutor com multiplos filtros
     *
     * @param juncao a String padrao de todos os filtros passados.
     * @param filtros multiplos filtros.
     */
    public FiltroGrupo(String juncao, Filtro... filtros) {
        if (filtros != null) {
            for (Filtro fil : filtros) {
                add(fil, juncao);
            }
        }
    }

    /**
     * Metodo que remove um filtro ou grupo.
     *
     * @param filtro do tipo IFiltro.
     */
    public void remove(Filtro filtro) {
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
     * Metodo que adiciona um filtro ou grupo sem juncao, filtro final.
     *
     * @param filtro do tipo IFiltro.
     * @throws IllegalArgumentException ocorre caso seja adicionado mais de um filtro com juncao null.
     */
    public void add(Filtro filtro) throws IllegalArgumentException {
        add(filtro, filtro.getJuncao());
    }

    /**
     * Metodo que adiciona um filtro ou grupo.
     *
     * @param filtro do tipo IFiltro.
     * @param juncao a forma de uniao com o proximo filtro, deve ser null caso nao tenha outro filtro.
     * @throws IllegalArgumentException ocorre caso seja adicionado mais de um filtro com juncao null.
     */
    public void add(Filtro filtro, String juncao) throws IllegalArgumentException {
        filtro.setJuncao(juncao);
        filtros.add(filtro);
    }

    /**
     * Metodo que fornece uma interacao nos objetos de filtros.
     *
     * @return um interador de IFiltro.
     */
    public Iterator<Filtro> iterator() {
        return filtros.iterator();
    }

    public Filtro[] toArray() {
        return filtros.toArray(new Filtro[]{});
    }

    @Override
    public String getSql() throws OpenPdvException {
        StringBuilder sql = new StringBuilder("(");
        for (Iterator<Filtro> it = filtros.iterator(); it.hasNext();) {
            Filtro filtro = it.next();
            sql.append(filtro.getSql());
            if (filtro.getJuncao() != null && !filtro.getJuncao().equals("") && it.hasNext()) {
                sql.append(" ").append(filtro.getJuncao()).append(" ");
            }
        }
        sql.append(")");
        return sql.toString();
    }

    @Override
    public Collection<Parametro> getParametro() {
        return getParametro(this);
    }

    @Override
    public Collection<Parametro> getParametro(Parametro param) {
        Collection<Parametro> parametros = new ArrayList<>();
        FiltroGrupo gf = (FiltroGrupo) param;

        for (Iterator<Filtro> it = gf.iterator(); it.hasNext();) {
            Parametro par = it.next();
            parametros.addAll(par.getParametro(par));
        }

        return parametros;
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
