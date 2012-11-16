package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.Conexao;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.EBusca;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroObjeto;
import br.com.openpdv.modelo.ecf.EcfImpressora;
import br.com.openpdv.modelo.ecf.EcfPagamentoTipo;
import br.com.openpdv.modelo.produto.ProdComposicao;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdPreco;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.openpdv.modelo.sistema.SisUsuario;
import br.com.phdss.controlador.PAF;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.log4j.Logger;

/**
 * Classe que realiza a acao de recedor os dados do servidor.
 *
 * @author Pedro H. Lira
 */
public class ComandoReceberDados implements IComando {

    private CoreService service;
    private Logger log;

    public ComandoReceberDados() {
        this.service = new CoreService();
        this.log = Logger.getLogger(ComandoReceberDados.class);
    }

    @Override
    public void executar() throws OpenPdvException {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();
            em.getTransaction().begin();
            WebResource wr;

            // atualiza os usuarios
            wr = Util.getRest(Util.getConfig().get("sinc.host") + "/usuario");
            List<SisUsuario> usuarios = wr.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<SisUsuario>>() {
            });
            for (SisUsuario usu : usuarios) {
                usu.setSisUsuarioLogin(Util.normaliza(usu.getSisUsuarioLogin()));
                service.salvar(em, usu);
            }
            log.debug("Dados usuarios recebidos");

            // atualiza os tipos de pagamento
            wr = Util.getRest(Util.getConfig().get("sinc.host") + "/tipo_pagamento/");
            List<EcfPagamentoTipo> tiposPagamento = wr.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<EcfPagamentoTipo>>() {
            });
            for (EcfPagamentoTipo tipo : tiposPagamento) {
                // Nao adicionar formas que tem o codigo 00
                if (!tipo.getEcfPagamentoTipoCodigo().equals("00")) {
                    tipo.setEcfPagamentoTipoDescricao(Util.normaliza(tipo.getEcfPagamentoTipoDescricao()));
                    service.salvar(em, tipo);
                }
            }
            log.debug("Dados tipos pagamento recebidos");

            // atualiza as embalagens
            wr = Util.getRest(Util.getConfig().get("sinc.host") + "/embalagem");
            List<ProdEmbalagem> embalagens = wr.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<ProdEmbalagem>>() {
            });
            for (ProdEmbalagem emb : embalagens) {
                service.salvar(em, emb);
            }
            log.debug("Dados embalagens recebidos");
            em.getTransaction().commit();

            // recupera os novos produtos
            int limite = Integer.valueOf(Util.getConfig().get("sinc.limite"));
            int pagina = 0;
            List<ProdProduto> novos;
            List<ProdPreco> precos = new ArrayList<>();
            List<ProdComposicao> comps = new ArrayList<>();

            // parametros
            Date dc = (Date) service.buscar(new ProdProduto(), "prodProdutoCadastrado", EBusca.MAXIMO, null);
            Date da = (Date) service.buscar(new ProdProduto(), "prodProdutoAlterado", EBusca.MAXIMO, null);
            MultivaluedMap<String, String> mm = new MultivaluedMapImpl();
            mm.putSingle("data", Util.getDataHora(dc));
            mm.putSingle("limite", String.valueOf(limite));

            do {
                mm.putSingle("pagina", String.valueOf(pagina));
                wr = Util.getRest(Util.getConfig().get("sinc.host") + "/produtoNovo");
                novos = wr.queryParams(mm).accept(MediaType.APPLICATION_JSON).get(new GenericType<List<ProdProduto>>() {
                });

                em.getTransaction().begin();
                for (ProdProduto prod : novos) {
                    // guarda as sub listas
                    for (ProdPreco pp : prod.getProdPrecos()) {
                        pp.setProdProduto(prod);
                        precos.add(pp);
                    }
                    for (ProdComposicao pc : prod.getProdComposicoes()) {
                        pc.setProdProdutoPrincipal(prod);
                        comps.add(pc);
                    }

                    // salva o produto
                    prod.setProdPrecos(null);
                    prod.setProdComposicoes(null);
                    prod.setProdProdutoDescricao(Util.normaliza(prod.getProdProdutoDescricao()));
                    service.salvar(em, prod);
                }
                em.getTransaction().commit();

                log.debug("Dados dos produtos novos recebidos da pagina " + pagina);
                pagina++;
            } while (novos.size() == limite);

            em.getTransaction().begin();
            // salva os precos
            for (ProdPreco preco : precos) {
                service.salvar(em, preco);
            }
            // salva os itens apos salvar todos os produtos.
            for (ProdComposicao comp : comps) {
                service.salvar(em, comp);
            }
            em.getTransaction().commit();

            // recupera os produtos atualizados
            pagina = 0;
            List<ProdProduto> atualizados;
            mm.putSingle("data", Util.getDataHora(da));

            do {
                mm.putSingle("pagina", String.valueOf(pagina));
                wr = Util.getRest(Util.getConfig().get("sinc.host") + "/produtoAtualizado");
                atualizados = wr.queryParams(mm).accept(MediaType.APPLICATION_JSON).get(new GenericType<List<ProdProduto>>() {
                });

                em.getTransaction().begin();
                for (ProdProduto prod : atualizados) {
                    // guarda as sub listas
                    precos = prod.getProdPrecos();
                    prod.setProdPrecos(null);
                    comps = prod.getProdComposicoes();
                    prod.setProdComposicoes(null);

                    // salva o produto
                    prod.setProdProdutoDescricao(Util.normaliza(prod.getProdProdutoDescricao()));
                    service.salvar(em, prod);

                    // salva os precos
                    if (!precos.isEmpty()) {
                        FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, prod);
                        Sql sql = new Sql(new ProdPreco(), EComandoSQL.EXCLUIR, fo);
                        service.executar(em, sql);
                        for (ProdPreco preco : precos) {
                            preco.setProdProduto(prod);
                            service.salvar(em, preco);
                        }
                    }

                    // salva os itens
                    if (!comps.isEmpty()) {
                        FiltroObjeto fo1 = new FiltroObjeto("prodProdutoPrincipal", ECompara.IGUAL, prod);
                        Sql sql1 = new Sql(new ProdComposicao(), EComandoSQL.EXCLUIR, fo1);
                        service.executar(em, sql1);
                        for (ProdComposicao comp : comps) {
                            comp.setProdProdutoPrincipal(prod);
                            service.salvar(em, comp);
                        }
                    }
                }
                em.getTransaction().commit();

                log.debug("Dados dos produtos atualizados recebidos da pagina " + pagina);
                pagina++;
            } while (novos.size() == limite);

            // se sucesso atualiza no arquivo a data do ultimo recebimento
            PAF.AUXILIAR.setProperty("out.recebimento", Util.getDataHora(new Date()));
            PAF.criptografar();
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao receber os dados.", ex);
            throw new OpenPdvException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
                emf.close();
            }
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }
}
