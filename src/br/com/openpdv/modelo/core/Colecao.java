package br.com.openpdv.modelo.core;

/**
 * Classe que representa uma colecao para o objeto, assim como Join para tabela.
 *
 * @author Pedro H. Lira
 */
public class Colecao {

    private String tabela;
    private String juncao;
    private String campo;
    private String prefixo;

    /**
     * Construtor padrao.
     */
    public Colecao() {
    }

    /**
     * Construtor que recebe a tabela, o campo usado, a juncao especifica e o
     * prefixo resultante.
     *
     * @param tabela o nome da unidade objeto da tabela.
     * @param campo o nome da propriedade do objeto.
     * @param juncao a instrucao em JQL de juncao.
     * @param prefixo o ideintificador da juncao.
     */
    public Colecao(String tabela, String campo, String juncao, String prefixo) {
        this.tabela = tabela;
        this.juncao = juncao;
        this.campo = campo;
        this.prefixo = prefixo;
    }

    //GETs e SETs
    
    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public String getJuncao() {
        return juncao;
    }

    public void setJuncao(String juncao) {
        this.juncao = juncao;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getPrefixo() {
        return prefixo;
    }

    public void setPrefixo(String prefixo) {
        this.prefixo = prefixo;
    }
}
