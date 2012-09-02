package br.com.openpdv.modelo.core.parametro;

import br.com.openpdv.modelo.core.filtro.ECompara;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe que abstrai as implementacoes do parametro definindos todos os metodos
 * com funcionalidades padronizadas.
 *
 * @param <E> usando generico para tipar o modelo de parametro usado.
 * <p/>
 * @author Pedro H. Lira
 */
public abstract class AParametro<E> implements IParametro<E> {

    /**
     * Campo contendo o tipo de comparacao a ser usada no filtro.
     */
    protected ECompara compara = ECompara.IGUAL;
    /**
     * Campo contendo o prefixo do campos de acordo com a tabela referenciada.
     */
    protected String prefixo = "t.";
    /**
     * Campo contendo o nome do campo do filtro.
     */
    protected String campo;
    /**
     * Campo contendo o valor a ser usado pelo filtro.F
     */
    protected E valor;
    /*
     * geracao de um identificador unico para o campo do filtro.
     */
    private int campoId = (int) (Math.random() * Integer.MAX_VALUE);

    /**
     * Construtor padrao.
     */
    public AParametro() {
    }

    /**
     * Construtor que define o campo, a comparacao e o valor.
     *
     * @param campo o nome do campo.
     * @param valor o valor do filtro.
     * <p/>
     * @throws ParametroException
     */
    public AParametro(String campo, String valor) {
        this.campo = campo;
        setValorString(valor);
    }

    /**
     * Construtor que define o campo, a comparacao e o valor.
     *
     * @param campo o nome do campo.
     * @param valor o valor do filtro.
     */
    public AParametro(String campo, E valor) {
        this.campo = campo;
        this.valor = valor;
    }

    // Gets e Seteres
    @Override
    public String getCampoId() {
        return campo.replace('.', '_') + "_" + campoId;
    }

    @Override
    public String getCampoPrefixo() {
        return prefixo;
    }

    @Override
    public String getCampo() {
        return campo;
    }

    @Override
    public void setCampoPrefixo(String prefixo) {
        this.prefixo = prefixo;
    }

    @Override
    public void setCampo(String campo) {
        this.campo = campo;
    }

    @Override
    public E getValor() {
        return valor;
    }

    @Override
    public void setValor(E valor) {
        this.valor = valor;
    }

    @Override
    public String getSql() throws ParametroException {
        tratarPrefixo();
        return prefixo + campo + " " + compara.toString() + " :" + getCampoId();
    }

    @Override
    public Collection<IParametro<E>> getParametro() {
        return getParametro(this);
    }

    @Override
    public Collection<IParametro<E>> getParametro(IParametro<E> filtro) {
        Collection<IParametro<E>> parametros = new ArrayList<>();
        parametros.add(filtro);
        return parametros;
    }

    @Override
    public abstract void setValorString(String valor);

    protected void tratarPrefixo() {
        // valida se o filtro e ja tem prefixo
        Matcher mat = Pattern.compile("^t\\d+\\.").matcher(campo);
        if (mat.find()) {
            prefixo = "";
        }
    }
}
