package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.Conexao;
import br.com.openpdv.controlador.core.CoreService;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.core.EBusca;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroObjeto;
import br.com.openpdv.modelo.ecf.EcfPagamentoTipo;
import br.com.openpdv.modelo.produto.ProdComposicao;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdGrade;
import br.com.openpdv.modelo.produto.ProdGradeTipo;
import br.com.openpdv.modelo.produto.ProdPreco;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.modelo.sistema.SisUsuario;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.controlador.PAF;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.log4j.Logger;

/**
 * Classe que realiza a acao de recedor os dados do servidor.
 *
 * @author Pedro H. Lira
 */
public abstract class ComandoReceberDados implements IComando {

    private CoreService service;
    private Logger log;
    private StringBuilder erros;
    private boolean prodSinc;
    private EntityManagerFactory emf;
    private EntityManager em;

    public ComandoReceberDados() {
        this.service = new CoreService();
        this.log = Logger.getLogger(ComandoReceberDados.class);
        this.erros = new StringBuilder();
        this.prodSinc = true;
    }

    /**
     * Metodo estatico para criar uma instancia de acordo com o config.
     *
     * @return o objeto que envia dados.
     */
    public static ComandoReceberDados getInstancia() {
        ComandoReceberDados crd = Util.getConfig().getProperty("sinc.tipo").equals("rest") ? new ComandoReceberDadosRemoto() : new ComandoReceberDadosLocal();
        return crd;
    }

    @Override
    public void executar() throws OpenPdvException {
        // pega a conexao
        try {
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();
        } catch (Exception ex) {
            throw new OpenPdvException("Nao consegui acesso ao banco.", ex);
        }

        if (Util.getConfig().getProperty("sinc.usuario").equals("true")) {
            // atualiza os usuarios
            usuarios();
        }
        if (Util.getConfig().getProperty("sinc.pagamento").equals("true")) {
            // atualiza os tipos de pagamento
            pagamentos();
        }
        if (Util.getConfig().getProperty("sinc.embalagem").equals("true")) {
            // atualiza as embalagens
            embalagens();
        }
        if (Util.getConfig().getProperty("sinc.grade").equals("true")) {
            // atualiza os tipos de grades
            grades();
        }
        if (Util.getConfig().getProperty("sinc.produto").equals("true")) {
            // recupera os novos produtos
            produtosNovos();
            // recupera os produtos atualizados
            produtosAtualizados();
        }
        if (Util.getConfig().getProperty("sinc.cliente").equals("true")) {
            // atualiza os clientes
            clientes();
        }

        // verifica se teve algum erro em produtos
        if (prodSinc == false) {
            erros.append("Erro no recebimento dos Produtos.\n");
        }

        // se sucesso atualiza no arquivo a data do ultimo recebimento
        if (erros.length() == 0) {
            salvar();
        } else {
            int escolha = JOptionPane.showOptionDialog(Caixa.getInstancia(), "Ocorreu algum problema no sincronismo.\nDeseja ignorar este erro?", "Sincronismo",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                salvar();
            } else {
                erros.append("Verifique o log do sistema.");
                throw new OpenPdvException(erros.toString());
            }
        }

        // fecha a conexao
        if (em != null && emf != null) {
            em.close();
            emf.close();
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    /**
     * Metodo que recebe o objeto do servidor ou local.
     *
     * @param <E> O tipo de dados.
     * @param tipo o nome da url/arquivo do objeto.
     * @param classe uma instacia do tipo da classe.
     * @return uma lista de tipos.
     * @throws Exception dispara em caso de erro.
     */
    public abstract <E extends Dados> List<E> receber(String tipo, GenericType<List<E>> classe) throws Exception;

    /**
     * Metodo que recebe o objeto do servidor ou local.
     *
     * @param <E> O tipo de dados.
     * @param classe uma instacia do tipo da classe.
     * @param tipo o nome da url/arquivo do objeto.
     * @param mm lista de parametros.
     * @return uma lista de tipos.
     * @throws Exception dispara em caso de erro.
     */
    public abstract <E extends Dados> List<E> receber(String tipo, GenericType<List<E>> classe, MultivaluedMap<String, String> mm) throws Exception;

    /**
     * Metodo que salva os usuarios.
     */
    private void usuarios() {
        try {
            List<SisUsuario> usuarios = receber("usuario", new GenericType<List<SisUsuario>>() {
            });
            em.getTransaction().begin();
            for (SisUsuario usu : usuarios) {
                usu.setSisUsuarioLogin(Util.normaliza(usu.getSisUsuarioLogin()));
                service.salvar(em, usu);
            }
            em.getTransaction().commit();
            log.info("Dados usuarios recebidos -> " + usuarios.size());
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            erros.append("Erro no recebimento dos Usuarios.\n");
            log.error("Erro no recebimento dos Usuarios.", ex);
        }
    }

    /**
     * Metodo que salva os pagamentos.
     */
    private void pagamentos() {
        try {
            List<EcfPagamentoTipo> tiposPagamento = receber("tipo_pagamento", new GenericType<List<EcfPagamentoTipo>>() {
            });
            em.getTransaction().begin();
            for (EcfPagamentoTipo tipo : tiposPagamento) {
                // Identifica se a forma de pagamento e uma das permitidas [Dinheiro, Cheque, Cartao, Cartao Presente, Troca]
                if (tipo.getEcfPagamentoTipoDescricao().equalsIgnoreCase("dinheiro")) {
                    tipo.setEcfPagamentoTipoCodigo(Util.getConfig().getProperty("ecf.dinheiro"));
                    service.salvar(em, tipo);
                } else if (tipo.getEcfPagamentoTipoDescricao().equalsIgnoreCase("cheque")) {
                    tipo.setEcfPagamentoTipoCodigo(Util.getConfig().getProperty("ecf.cheque"));
                    service.salvar(em, tipo);
                } else if (tipo.isEcfPagamentoTipoTef()) {
                    tipo.setEcfPagamentoTipoCodigo(Util.getConfig().getProperty("ecf.cartao"));
                    service.salvar(em, tipo);
                } else if (tipo.getEcfPagamentoTipoDescricao().equalsIgnoreCase("cartao presente")) {
                    tipo.setEcfPagamentoTipoCodigo(Util.getConfig().getProperty("ecf.presente"));
                    service.salvar(em, tipo);
                } else if (tipo.getEcfPagamentoTipoDescricao().equalsIgnoreCase("troca")) {
                    tipo.setEcfPagamentoTipoCodigo(Util.getConfig().getProperty("ecf.troca"));
                    service.salvar(em, tipo);
                }
            }
            em.getTransaction().commit();
            log.info("Dados tipos pagamento recebidos -> " + tiposPagamento.size());
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            erros.append("Erro no recebimento dos Tipos de Pagamentos.\n");
            log.error("Erro no recebimento dos Tipos de Pagamentos.", ex);
        }
    }

    /**
     * Metodo que salva as embalagens.
     */
    private void embalagens() {
        try {
            Integer maxId = (Integer) service.buscar(new ProdEmbalagem(), "prodEmbalagemId", EBusca.MAXIMO, null);
            MultivaluedMap<String, String> mm = new MultivaluedMapImpl();
            mm.putSingle("id", maxId != null ? maxId.toString() : "0");
            List<ProdEmbalagem> embalagens = receber("embalagem", new GenericType<List<ProdEmbalagem>>() {
            }, mm);

            em.getTransaction().begin();
            for (ProdEmbalagem emb : embalagens) {
                service.salvar(em, emb);
            }
            em.getTransaction().commit();
            log.info("Dados embalagens recebidos -> " + embalagens.size());
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            erros.append("Erro no recebimento das Embalagens.\n");
            log.error("Erro no recebimento das Embalagens.", ex);
        }
    }

    /**
     * Metodo que salva as grades.
     */
    private void grades() {
        try {
            Integer maxId = (Integer) service.buscar(new ProdGradeTipo(), "prodGradeTipoId", EBusca.MAXIMO, null);
            MultivaluedMap<String, String> mm = new MultivaluedMapImpl();
            mm.putSingle("id", maxId != null ? maxId.toString() : "0");
            List<ProdGradeTipo> tipos = receber("tipo_grade", new GenericType<List<ProdGradeTipo>>() {
            }, mm);

            em.getTransaction().begin();
            for (ProdGradeTipo tipo : tipos) {
                service.salvar(em, tipo);
            }
            em.getTransaction().commit();
            log.info("Dados tipos de grade recebidos -> " + tipos.size());
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            erros.append("Erro no recebimento dos Tipos de Grades.\n");
            log.error("Erro no recebimento dos Tipos de Grades.", ex);
        }
    }

    /**
     * Metodo que salva os produtos novos.
     */
    private void produtosNovos() {
        int limite = Integer.valueOf(Util.getConfig().getProperty("sinc.limite"));
        int pagina = 0;
        List<ProdProduto> novos;
        MultivaluedMap<String, String> mm = new MultivaluedMapImpl();

        // produtos novos
        try {
            Integer maxId = (Integer) service.buscar(new ProdProduto(), "prodProdutoId", EBusca.MAXIMO, null);
            mm.putSingle("id", maxId != null ? maxId.toString() : "0");
            mm.putSingle("limite", String.valueOf(limite));
            List<ProdPreco> precos = new ArrayList<>();
            List<ProdComposicao> comps = new ArrayList<>();
            List<ProdGrade> grades = new ArrayList<>();

            do {
                mm.putSingle("pagina", String.valueOf(pagina));
                novos = receber("produtoNovo", new GenericType<List<ProdProduto>>() {
                }, mm);

                for (ProdProduto prod : novos) {
                    precos.clear();
                    comps.clear();
                    grades.clear();

                    // guarda as sub listas
                    for (ProdPreco pp : prod.getProdPrecos()) {
                        pp.setProdProduto(prod);
                        precos.add(pp);
                    }
                    for (ProdComposicao pc : prod.getProdComposicoes()) {
                        pc.setProdProdutoPrincipal(prod);
                        comps.add(pc);
                    }
                    for (ProdGrade pg : prod.getProdGrades()) {
                        pg.setProdProduto(prod);
                        grades.add(pg);
                    }

                    try {
                        em.getTransaction().begin();
                        // salva o produto
                        prod.setProdPrecos(null);
                        prod.setProdComposicoes(null);
                        prod.setProdGrades(null);
                        prod.setProdProdutoDescricao(Util.normaliza(prod.getProdProdutoDescricao()));
                        service.salvar(em, prod);
                        // salva os precos
                        if (!precos.isEmpty()) {
                            service.salvar(em, precos);
                        }
                        // salva os itens
                        if (!comps.isEmpty()) {
                            service.salvar(em, comps);
                        }
                        // salva as grades 
                        if (!grades.isEmpty()) {
                            service.salvar(em, grades);
                        }
                        em.getTransaction().commit();
                    } catch (Exception ex) {
                        if (em != null && em.getTransaction().isActive()) {
                            em.getTransaction().rollback();
                        }
                        prodSinc = false;
                        log.error("Nao salvou o produto com ID = " + prod.getProdProdutoId(), ex);
                    }
                }
                log.info("Dados novos produtos recebidos -> " + novos.size());
                pagina++;
            } while (novos.size() == limite);
        } catch (Exception ex) {
            prodSinc = false;
            log.error("Nao conseguiu acessar os dados dos novos produtos.", ex);
        }
    }

    /**
     * Metodo que salva os produtos atualizados.
     */
    private void produtosAtualizados() {
        int limite = Integer.valueOf(Util.getConfig().getProperty("sinc.limite"));
        int pagina = 0;
        List<ProdProduto> atualizados;
        MultivaluedMap<String, String> mm = new MultivaluedMapImpl();

        try {
            Date da = (Date) service.buscar(new ProdProduto(), "prodProdutoAlterado", EBusca.MAXIMO, null);
            mm.putSingle("data", da != null ? Util.getData(da) : "");
            mm.putSingle("limite", String.valueOf(limite));
            List<ProdPreco> precos = new ArrayList<>();
            List<ProdComposicao> comps = new ArrayList<>();
            List<ProdGrade> grades = new ArrayList<>();

            do {
                mm.putSingle("pagina", String.valueOf(pagina));
                atualizados = receber("produtoAtualizado", new GenericType<List<ProdProduto>>() {
                }, mm);

                for (ProdProduto prod : atualizados) {
                    precos.clear();
                    comps.clear();
                    grades.clear();

                    // guarda as sub listas
                    for (ProdPreco pp : prod.getProdPrecos()) {
                        pp.setProdProduto(prod);
                        precos.add(pp);
                    }
                    for (ProdComposicao pc : prod.getProdComposicoes()) {
                        pc.setProdProdutoPrincipal(prod);
                        comps.add(pc);
                    }
                    for (ProdGrade pg : prod.getProdGrades()) {
                        pg.setProdProduto(prod);
                        grades.add(pg);
                    }

                    try {
                        // salva o produto
                        prod.setProdPrecos(null);
                        prod.setProdComposicoes(null);
                        prod.setProdGrades(null);
                        prod.setProdProdutoDescricao(Util.normaliza(prod.getProdProdutoDescricao()));
                        service.salvar(prod);
                    } catch (Exception ex) {
                        prodSinc = false;
                        log.error("Nao atualizou o produto com ID = " + prod.getProdProdutoId(), ex);
                    }
                    
                    try {
                        // salva os precos
                        if (!precos.isEmpty()) {
                            FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, prod);
                            Sql sql = new Sql(new ProdPreco(), EComandoSQL.EXCLUIR, fo);
                            service.executar(sql);
                            service.salvar(precos);
                        }
                        // salva os itens
                        if (!comps.isEmpty()) {
                            FiltroObjeto fo1 = new FiltroObjeto("prodProdutoPrincipal", ECompara.IGUAL, prod);
                            Sql sql = new Sql(new ProdComposicao(), EComandoSQL.EXCLUIR, fo1);
                            service.executar(sql);
                            service.salvar(comps);
                        }
                        // salva as grades
                        if (!grades.isEmpty()) {
                            FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, prod);
                            Sql sql = new Sql(new ProdGrade(), EComandoSQL.EXCLUIR, fo);
                            service.executar(sql);
                            service.salvar(grades);
                        }
                    } catch (Exception ex) {
                        prodSinc = false;
                        log.error("Nao atualizou as sub-tabelas do produto com ID = " + prod.getProdProdutoId(), ex);
                    }
                }
                log.info("Dados produtos atualizados recebidos -> " + atualizados.size());
                pagina++;
            } while (atualizados.size() == limite);
        } catch (Exception ex) {
            prodSinc = false;
            log.error("Nao conseguiu acessar os dados dos produtos atualizados.", ex);
        }
    }

    /**
     * Metodo que salva os clientes.
     */
    private void clientes() {
        int limite = Integer.valueOf(Util.getConfig().getProperty("sinc.limite"));
        int pagina = 0;
        List<SisCliente> clientes;
        MultivaluedMap<String, String> mm = new MultivaluedMapImpl();

        try {
            Date da = (Date) service.buscar(new SisCliente(), "sisClienteData", EBusca.MAXIMO, null);
            mm.putSingle("data", da != null ? Util.getData(da) : "");
            mm.putSingle("limite", String.valueOf(limite));

            do {
                mm.putSingle("pagina", String.valueOf(pagina));
                clientes = receber("cliente", new GenericType<List<SisCliente>>() {
                }, mm);

                em.getTransaction().begin();
                for (SisCliente cli : clientes) {
                    cli.setSisClienteSinc(true);
                    service.salvar(em, cli);
                }
                em.getTransaction().commit();
                log.info("Dados clientes recebidos -> " + clientes.size());
                pagina++;
            } while (clientes.size() == limite);
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            erros.append("Erro no recebimento dos Clientes.\n");
            log.error("Erro no recebimento dos Clientes.", ex);
        }
    }

    /**
     * Metodo que salva a data de encio no arquivo.
     *
     * @throws OpenPdvException caso ocorra algum erro.
     */
    private void salvar() throws OpenPdvException {
        try {
            PAF.AUXILIAR.setProperty("out.recebimento", Util.getData(new Date()));
            Util.criptografar(null, PAF.AUXILIAR);
        } catch (Exception ex) {
            throw new OpenPdvException("Erro ao salvar no arquivo auxiliar.\nVerifique o log do sistema.", ex);
        }
    }
}
