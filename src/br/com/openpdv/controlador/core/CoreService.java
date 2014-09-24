package br.com.openpdv.controlador.core;

import br.com.openpdv.modelo.core.*;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.Filtro;
import br.com.openpdv.modelo.core.parametro.Parametro;
import br.com.openpdv.modelo.ecf.EcfDocumento;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.phdss.Util;
import br.com.phdss.controlador.PAF;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 * Classe que implementa na parte do servidor a resposta a chamada de
 * procedimento do cliente, executando os comandos de persistencia no banco de
 * dados.
 *
 * @param <E> o tipo de dados.
 * @author Pedro H. Lira
 */
public class CoreService<E extends Dados> {

    protected Logger log;

    /**
     * Construtor padrao.
     */
    public CoreService() {
        log = Logger.getLogger(CoreService.class);
    }

    /**
     * Construtor padrao passando a classe de logger.
     *
     * @param classe a classe que sera usada
     */
    protected CoreService(Class classe) {
        log = Logger.getLogger(classe);
    }

    /**
     * Metodo que seleciona os dados no BD.
     *
     * @param dados param inicio param limite param filtro
     * @param inicio a pagina de inicio de captura dos dados.
     * @param limite a quantidade de registros maximos retornados.
     * @param filtro o filtro utilizado para restringir o resultado.
     * @return uma lista de elementos informado, encontrado no BD.
     * @throws OpenPdvException dispara caso nao seja possivel selecionar os
     * dados.
     */
    public List<E> selecionar(E dados, int inicio, int limite, Filtro filtro) throws OpenPdvException {
        // mosta a instrucao padrao
        String sql = String.format("SELECT DISTINCT t FROM %s t ", dados.getTabela());
        sql += getColecao(dados.getColecao());
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            // recupera uma instância do gerenciador de entidades
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();

            // caso tenha filtros, recupera no padrao sql e adiciona a instrucao
            if (filtro != null) {
                sql += String.format(" WHERE %s", filtro.getSql());
            }

            // caso seja passado um campo para ordenar, adiciona o comando a
            // instrucao
            if (dados.getCampoOrdem() != null && !dados.getCampoOrdem().isEmpty()) {
                String ordem = dados.getCampoOrdem();
                Pattern pat = Pattern.compile("^t\\d*\\.");
                Matcher mat = pat.matcher(ordem);
                if (!mat.find()) {
                    ordem = "t." + ordem;
                }
                EDirecao direcao = dados.getOrdemDirecao() == null ? EDirecao.ASC : dados.getOrdemDirecao();
                sql += String.format(" ORDER BY %s %s", ordem, direcao.toString());
            }

            // pega a transacao padrao e inicia
            em.getTransaction().begin();
            // gera um query
            log.debug("Sql gerado: " + sql);
            Query rs = em.createQuery(sql);

            // se foi definido um limete de resgistros, caso contrario recupera
            // todos.
            if (limite > 0) {
                inicio = inicio < 0 ? 0 : inicio;
                // seta a posicao inicial de recuperacao dos registros
                // (paginacao)
                rs.setFirstResult(inicio);
                // seta a posicao a quantidade total de registros (paginacao)
                rs.setMaxResults(limite);
            }

            // se foi passados filtros coloca agora os valores nos devidos
            // campos
            if (filtro != null) {
                Collection<Filtro> params = filtro.getParametro();
                for (Filtro fil : params) {
                    if (fil.getCompara() != ECompara.NULO && fil.getCompara() != ECompara.VAZIO) {
                        rs.setParameter(fil.getCampoId(), fil.getValor());
                    }
                }
            }

            // realiza toda a operacao caso tudo tenha sucesso
            em.getTransaction().commit();
            // recupera a lista de dados
            List<E> lista = (List<E>) rs.getResultList();
            return lista;
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao selecionar", ex);
            throw new OpenPdvException(ex.getMessage());
        } finally {
            if (em != null && emf != null) {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que seleciona somente um registro no BD.
     *
     * @param dados o tipo de dados solicitado.
     * @param filtro o filtro de regsitracao de selecao.
     * @return o objeto tipado solicitado retornado do BD, ou null se nao
     * encontrar.
     * @throws OpenPdvException dispara caso nao seja possivel selecionar os
     * dados.
     */
    public E selecionar(E dados, Filtro filtro) throws OpenPdvException {
        // formata o sql
        String sql = String.format("SELECT DISTINCT t FROM %s t ", dados.getTabela());
        sql += getColecao(dados.getColecao());

        // pega o resultado
        E obj = (E) getResultado(sql, filtro);
        return obj;
    }

    /**
     * Metodo que realiza um busca, usando condiciona matematico.
     *
     * @param dados o tipo de dados que deseja buscar.
     * @param campo o nome do campo a ser usada na comparacao.
     * @param busca o tipo de operacao matematica solicitada.
     * @return um objeto tipado como solicitado encontrado no BD, ou null se nao
     * encontrar.
     * @throws OpenPdvException dispara caso nao seja possivel selecionar os
     * dados.
     */
    public E buscar(E dados, String campo, EBusca busca) throws OpenPdvException {
        // formata o sql
        String sql = String.format("SELECT t FROM %s t WHERE t.%s = (SELECT %s(t1.%s) FROM %s t1)", dados.getTabela(), campo, busca.toString(), campo, dados.getTabela());

        // pega o resultado
        E obj = (E) getResultado(sql, null);
        return obj;
    }

    /**
     * Metodo que realiza um busca, usando condiciona matematico.
     *
     * @param dados o tipo de dados que deseja buscar.
     * @param campo o nome do campo a ser usada na comparacao.
     * @param busca o tipo de operacao matematica solicitada.
     * @param filtro o filtro utilizado para restringir a massa de dados usada
     * na comparacao.
     * @return um objeto tipado como solicitado encontrado no BD, ou null se nao
     * encontrar.
     * @throws OpenPdvException dispara caso nao seja possivel selecionar os
     * dados.
     */
    public Object buscar(E dados, String campo, EBusca busca, Filtro filtro) throws OpenPdvException {
        Pattern pat = Pattern.compile("^t\\d*\\.");
        Matcher mat = pat.matcher(campo);
        if (!mat.find()) {
            campo = "t." + campo;
        }

        // verifica se é contagem
        String conta = EBusca.CONTAGEM == busca ? "DISTINCT " : "";

        // formata o sql
        String sql = String.format("SELECT %s(%s%s) FROM %s t ", busca.toString(), conta, campo, dados.getTabela());
        sql += getColecao(dados.getColecao());

        // pega o resultado
        return getResultado(sql, filtro);
    }

    /**
     * Metodo padrao para recuperar um registro.
     *
     * @param sql a instrucao em EQL formatada.
     * @param filtro o filtro a ser usado.
     * @return um unidade de classe de acordo com a generic.
     * @throws OpenPdvException dispara caso nao seja possivel selecionar os
     * dados.
     */
    public Object getResultado(String sql, Filtro filtro) throws OpenPdvException {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();

            // caso tenha filtros, recupera no padrao sql e adiciona a instrucao
            if (filtro != null) {
                sql += " WHERE " + filtro.getSql();
            }

            // pega a transacao padrao e inicia
            em.getTransaction().begin();
            // gera um query
            log.debug("Sql gerado: " + sql);
            Query rs = em.createQuery(sql);

            // se foi passados filtros coloca agora os valores nos devidos
            // campos
            if (filtro != null) {
                Collection<Filtro> params = filtro.getParametro();

                for (Filtro fil : params) {
                    if (fil.getCompara() != ECompara.NULO && fil.getCompara() != ECompara.VAZIO) {
                        rs.setParameter(fil.getCampoId(), fil.getValor());
                    }
                }
            }

            // realiza toda a operacao caso tudo tenha sucesso
            em.getTransaction().commit();
            try {
                return rs.getSingleResult();
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao pegar resultado", ex);
            throw new OpenPdvException(ex.getMessage());
        } finally {
            if (em != null && emf != null) {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que salva a entidade usando a uma nova transacao.
     *
     * @param unidades cuma colecao de entidades.
     * @return a entidade com valores salvos.
     * @throws OpenPdvException dispara uma excecao em caso de erro.
     */
    public Collection<E> salvar(Collection<E> unidades) throws OpenPdvException {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();
            em.getTransaction().begin();
            salvar(em, unidades);
            em.getTransaction().commit();
            return unidades;
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao salvar", ex);
            throw new OpenPdvException(ex.getMessage());
        } finally {
            if (em != null && emf != null) {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que salva a entidade usando a mesma transacao passada.
     *
     * @param em o gerenciado de entidade.
     * @param unidades cuma colecao de entidades.
     * @return a entidade com valores salvos.
     * @throws OpenPdvException dispara uma excecao em caso de erro.
     */
    public Collection<E> salvar(EntityManager em, Collection<E> unidades) throws OpenPdvException {
        if (unidades != null) {
            for (E unidade : unidades) {
                salvar(em, unidade);
            }
        }
        return unidades;
    }

    /**
     * Metodo que salva a entidade usando com uma nova transacao.
     *
     * @param unidade a entidade
     * @return a entidade com valores salvos.
     * @throws OpenPdvException dispara uma excecao em caso de erro.
     */
    public E salvar(E unidade) throws OpenPdvException {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();
            em.getTransaction().begin();
            salvar(em, unidade);
            em.getTransaction().commit();
            return unidade;
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao salvar", ex);
            throw new OpenPdvException(ex.getMessage());
        } finally {
            if (em != null && emf != null) {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que salva a entidade usando a mesma transacao passada.
     *
     * @param em o gerenciado de entidade.
     * @param unidade a entidade
     * @return a entidade com valores salvos.
     * @throws OpenPdvException dispara uma excecao em caso de erro.
     */
    public E salvar(EntityManager em, E unidade) throws OpenPdvException {
        try {
            padronizaLetras(unidade, unidade.getTipoLetra());
            String ead = Util.encriptar(unidade);
            unidade.setEad(ead);
            if (unidade.getId() == null || unidade.getId() == 0) {
                unidade.setId(null);
                em.persist(unidade);
            } else {
                em.merge(unidade);
            }
            PAF.validarPAF(unidade, 1);
            return unidade;
        } catch (Exception ex) {
            log.error("Erro ao salvar", ex);
            throw new OpenPdvException(ex.getMessage());
        }
    }

    /**
     * Metodo que deleta uma colecao de entidades com uma nova transacao.
     *
     * @param unidades a colecao de entidades.
     * @throws OpenPdvException dispara uma excecao em caso de erro.
     */
    public void deletar(Collection<E> unidades) throws OpenPdvException {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();

            if (unidades != null && !unidades.isEmpty()) {
                em.getTransaction().begin();
                deletar(em, unidades);
                em.getTransaction().commit();
            }
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao deletar", ex);
            throw new OpenPdvException(ex.getMessage());
        } finally {
            if (em != null && emf != null) {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que deleta uma colecao de entidades com a mesma transacao.
     *
     * @param em o gerenciador de entidades.
     * @param unidades a colecao de entidades.
     * @throws OpenPdvException dispara uma excecao em caso de erro.
     */
    public void deletar(EntityManager em, Collection<E> unidades) throws OpenPdvException {
        for (E unidade : unidades) {
            deletar(em, unidade);
        }
    }

    /**
     * Metodo que deleta a entidade com uma nova transacao.
     *
     * @param unidade a entidade
     * @throws OpenPdvException dispara uma excecao em caso de erro.
     */
    public void deletar(E unidade) throws OpenPdvException {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();
            em.getTransaction().begin();
            deletar(em, unidade);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao deletar", ex);
            throw new OpenPdvException(ex.getMessage());
        } finally {
            if (em != null && emf != null) {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que deleta a entidade com a mesma transacao passada.
     *
     * @param em o gerenciador de entidades.
     * @param unidade a entidade
     * @throws OpenPdvException dispara uma excecao em caso de erro.
     */
    public void deletar(EntityManager em, E unidade) throws OpenPdvException {
        try {
            unidade = (E) em.find(unidade.getClass(), unidade.getId());
            em.remove(unidade);
            PAF.validarPAF(unidade, -1);
        } catch (Exception ex) {
            log.error("Erro ao deletar", ex);
            throw new OpenPdvException(ex.getMessage());
        }
    }

    /**
     * Metodo para executar instrucões diretas no BD com uma nova transacao.
     *
     * @param sqls as instruções Sqls em formato de objeto.
     * @return um inteiro informando a quantidade de registros afetados.
     * @throws OpenPdvException dispara uma excecao em caso de erro.
     */
    public List<Integer> executar(Sql<E>... sqls) throws OpenPdvException {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        List<Integer> resultado = new ArrayList<>();

        try {
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();

            if (sqls != null) {
                em.getTransaction().begin();
                for (Sql<E> sql : sqls) {
                    resultado.add(executar(em, sql));
                }
                em.getTransaction().commit();
            }
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao executar", ex);
            throw new OpenPdvException(ex.getMessage());
        } finally {
            if (em != null && emf != null) {
                em.close();
                emf.close();
            }
        }

        return resultado;
    }

    /**
     * Metodo para executar instrucões diretas no BD com a mesma transacao.
     *
     * @param em o gerenciador de entidades.
     * @param sql a instrucao Sql em formato de objeto.
     * @return um inteiro informando a quantidade de registros afetados.
     * @throws OpenPdvException dispara uma excecao em caso de erro.
     */
    public Integer executar(EntityManager em, Sql<E> sql) throws OpenPdvException {
        Integer resultado = null;

        if (sql != null) {
            // recupera uma instância do gerenciador de entidades
            E dados = sql.getClasse();
            Pattern pat = Pattern.compile("t\\d+\\.");

            // gerando a acao
            String acao;
            if (sql.getComando() == EComandoSQL.ATUALIZAR) {
                // caso a acao seja atualizar um campo de colecao
                Matcher mat = pat.matcher(sql.getParametro().getSql());
                if (mat.find()) {
                    return atualizar(em, sql);
                } else {
                    acao = "UPDATE " + dados.getTabela() + " t SET " + sql.getParametro().getSql();
                }
            } else {
                acao = "DELETE FROM " + dados.getTabela() + " t ";
            }

            // caso tenha filtros, recupera no padrao sql e adiciona a
            // instrucao
            if (sql.getFiltro() != null) {
                Matcher mat = pat.matcher(sql.getFiltro().getSql());
                if (mat.find()) {
                    if (sql.getComando() == EComandoSQL.ATUALIZAR) {
                        return atualizar(em, sql);
                    } else {
                        return excluir(em, sql);
                    }
                } else {
                    acao += String.format(" WHERE %s", sql.getFiltro().getSql());
                }
            }

            // gera um query
            log.debug("Sql gerado: " + acao);
            Query rs = em.createQuery(acao);

            // se foi passados filtros coloca agora os valores nos
            // devidos campos
            if (sql.getFiltro() != null) {
                Collection<Filtro> params = sql.getFiltro().getParametro();
                for (Filtro fil : params) {
                    if (fil.getCompara() != ECompara.NULO && fil.getCompara() != ECompara.VAZIO) {
                        rs.setParameter(fil.getCampoId(), fil.getValor());
                    }
                }
            }

            // se foi passados parametros coloca agora os valores nos
            // devidos campos
            if (sql.getParametro() != null) {
                Collection<Parametro> params = sql.getParametro().getParametro();
                for (Parametro par : params) {
                    rs.setParameter(par.getCampoId(), par.getValor());
                }
            }

            // executa o comando
            resultado = rs.executeUpdate();
        }

        return resultado;
    }

    /**
     * Metodo para executar instrucões diretas no BD com uma nova transacao.
     *
     * @param sql a instrucao Sql em formato de objeto.
     * @return um inteiro informando a quantidade de registros afetados.
     * @throws OpenPdvException dispara uma excecao em caso de erro.
     */
    public Integer executar(String sql) throws OpenPdvException {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        Integer resultado = null;

        try {
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();

            if (sql != null && !sql.equals("")) {
                em.getTransaction().begin();
                Query rs = em.createNativeQuery(sql);
                resultado = rs.executeUpdate();
                em.getTransaction().commit();
            }
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao executar", ex);
            throw new OpenPdvException(ex.getMessage());
        } finally {
            if (em != null && emf != null) {
                em.close();
                emf.close();
            }
        }

        return resultado;
    }

    /**
     * Metodo para executar instrucões de atualizacao diretas no BD com a mesma
     * transacao.
     *
     * @param em o gerenciador de entidades.
     * @param sql a instrucao Sql em formato de objeto.
     * @return um inteiro informando a quantidade de registros afetados.
     * @throws OpenPdvException dispara uma excecao em caso de erro.
     */
    private Integer atualizar(EntityManager em, Sql<E> sql) throws OpenPdvException {
        int resultado = 0;
        String nMet = "set" + sql.getParametro().getCampo().replaceAll("t\\d*\\.", "");

        // faz a selecao dos objetos
        E dado = sql.getClasse();
        Collection<E> lista = selecionar(dado, 0, 0, sql.getFiltro());

        try {
            // percorre cada um para atualizar o campo
            for (E obj : lista) {
                // os metodos do objeto
                for (Method met : obj.getClass().getMethods()) {
                    // verifica se é o get e retorna List
                    if (Util.isGetter(met) && met.getReturnType() == List.class) {
                        List<E> vMet = (List<E>) met.invoke(obj, new Object[]{});
                        // percorre as colecoes
                        for (Colecao col : dado.getColecao()) {
                            // verifica se tem valor e compativel com o objeto
                            if (vMet != null && !vMet.isEmpty() && vMet.get(0).getTabela().equals(col.getTabela())) {
                                // percorre os objetos
                                for (E subObj : vMet) {
                                    // percorre os metodos do objeto final
                                    for (Method subMet : subObj.getClass().getMethods()) {
                                        // verifica se é set e tem o mesmo nome
                                        if (Util.isSetter(subMet) && subMet.getName().equalsIgnoreCase(nMet)) {
                                            // seta o valor
                                            setValor(subMet, subObj, sql);
                                            break;
                                        }
                                    }
                                }

                                // salva os objetos altualizados
                                salvar(em, vMet);
                                resultado += vMet.size();
                            }
                        }
                        // verifica se é set e tem o mesmo nome
                    } else if (Util.isSetter(met) && met.getName().equalsIgnoreCase(nMet)) {
                        // seta o valor
                        setValor(met, obj, sql);
                        resultado++;
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Erro ao atualizar", ex);
            resultado = 0;
        }

        return resultado;
    }

    /**
     * Metodo que faz o set do valor no objeto identificando o verdadeiro tipo.
     *
     * @param sMet o Metodo a ser chamado.
     * @param obj o objeto a ser atingindo.
     * @param sql a instrucao que contem o valor.
     * @throws Exception caso ocorra alguma excecao.
     */
    private void setValor(Method sMet, E obj, Sql<E> sql) throws Exception {
        String nomeGet = sMet.getName().replace("set", "get");
        Method gMet = obj.getClass().getMethod(nomeGet);
        String valor = sql.getParametro().getValor().toString();

        if (gMet.getReturnType() == Boolean.class || gMet.getReturnType() == boolean.class) {
            sMet.invoke(obj, new Object[]{Boolean.valueOf(valor)});
        } else if (gMet.getReturnType() == Byte.class || gMet.getReturnType() == byte.class) {
            sMet.invoke(obj, new Object[]{Byte.valueOf(valor)});
        } else if (gMet.getReturnType() == Short.class || gMet.getReturnType() == short.class) {
            sMet.invoke(obj, new Object[]{Short.valueOf(valor)});
        } else if (gMet.getReturnType() == Integer.class || gMet.getReturnType() == int.class) {
            sMet.invoke(obj, new Object[]{Integer.valueOf(valor)});
        } else if (gMet.getReturnType() == Long.class || gMet.getReturnType() == long.class) {
            sMet.invoke(obj, new Object[]{Long.valueOf(valor)});
        } else if (gMet.getReturnType() == Float.class || gMet.getReturnType() == float.class) {
            sMet.invoke(obj, new Object[]{Float.valueOf(valor)});
        } else if (gMet.getReturnType() == Double.class || gMet.getReturnType() == double.class) {
            sMet.invoke(obj, new Object[]{Double.valueOf(valor)});
        } else if (gMet.getReturnType() == Date.class) {
            sMet.invoke(obj, new Object[]{(Date) sql.getParametro().getValor()});
        } else if (gMet.getReturnType() == Character.class || gMet.getReturnType() == char.class) {
            sMet.invoke(obj, new Object[]{valor.charAt(0)});
        } else {
            sMet.invoke(obj, new Object[]{valor});
        }
    }

    /**
     * Metodo para executar instrucões de exclusoes diretas no BD com a mesma
     * transacao.
     *
     * @param em o gerenciador de entidades.
     * @param sql a instrucao Sql em formato de objeto.
     * @return um inteiro informando a quantidade de registros afetados.
     * @throws OpenPdvException dispara uma excecao em caso de erro.
     */
    private Integer excluir(EntityManager em, Sql<E> sql) throws OpenPdvException {
        // faz a selecao dos objetos
        E dado = sql.getClasse();
        Collection<E> lista = selecionar(dado, 0, 0, sql.getFiltro());
        deletar(em, lista);

        return lista.size();
    }

    /**
     * Metodo que gera a instrucao de colecoes em JQL.
     *
     * @param colecao um array de colecoes de tabelas.
     * @return uma string no formato de busca.
     */
    private String getColecao(Colecao[] colecao) {
        String sql = "";
        if (colecao != null) {
            for (Colecao col : colecao) {
                sql += String.format("%s %s %s ", col.getJuncao(), col.getCampo(), col.getPrefixo());
            }
        }
        return sql;
    }

    /**
     * Metodo que padrozina os tamanhos da letras ao salvar os dados.
     *
     * @param unidade o objeto a ser salvo.
     * @param tipo o tipo de letra padrao usado.
     */
    private void padronizaLetras(Object unidade, ELetra tipo) {
        if (tipo != ELetra.NORMAL) {
            for (Method metodo : unidade.getClass().getMethods()) {
                try {
                    if (Util.isGetter(metodo)) {
                        Object valorMetodo = metodo.invoke(unidade, new Object[]{});
                        if (valorMetodo != null) {
                            if (metodo.getReturnType() == String.class) {
                                String nomeMetodo = metodo.getName().replaceFirst("get", "set");
                                Method set = unidade.getClass().getMethod(nomeMetodo, new Class[]{String.class});
                                String valor = tipo == ELetra.GRANDE ? valorMetodo.toString().trim().toUpperCase() : valorMetodo.toString().trim().toLowerCase();
                                set.invoke(unidade, new Object[]{valor});
                            } else if (metodo.getReturnType().getSuperclass() == Dados.class) {
                                padronizaLetras(valorMetodo, tipo);
                            }
                        }
                    }
                } catch (Exception ex) {
                    // nada
                }
            }
        }
    }
}
