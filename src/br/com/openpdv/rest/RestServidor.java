package br.com.openpdv.rest;

import br.com.openpdv.controlador.core.Conexao;
import br.com.openpdv.controlador.core.NFe;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.EJuncao;
import br.com.openpdv.modelo.core.filtro.FiltroData;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.core.filtro.FiltroObjeto;
import br.com.openpdv.modelo.core.filtro.FiltroTexto;
import br.com.openpdv.modelo.core.filtro.GrupoFiltro;
import br.com.openpdv.modelo.core.filtro.IFiltro;
import br.com.openpdv.modelo.core.parametro.ParametroFormula;
import br.com.openpdv.modelo.core.parametro.ParametroObjeto;
import br.com.openpdv.modelo.ecf.*;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdGrade;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.opensig.nfe.TNFe;
import br.com.opensig.nfe.TNfeProc;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.Logger;

/**
 * Classe que representa a comunicao do Cliente para o Servidor via Rest
 *
 * @author Pedro H. Lira
 */
@Provider
@Path("/openpdv/server")
public class RestServidor extends ARest {

    /**
     * Construtor padrao.
     */
    public RestServidor() {
        super();
        log = Logger.getLogger(RestServidor.class);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Override
    public String ajuda() throws RestException {
        return super.ajuda();
    }

    /**
     * Metodo que cadastra na base do server as notas de consumidor emitidas
     * pelos sistemas em modo client.
     *
     * @param ecfNota um objeto do tipo Nota.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/nota")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setNota(EcfNota ecfNota) throws RestException {
        autorizar();
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            // valida se ja existe
            FiltroTexto ft = new FiltroTexto("ecfNotaSerie", ECompara.IGUAL, ecfNota.getEcfNotaSerie());
            FiltroTexto ft1 = new FiltroTexto("ecfNotaSubserie", ECompara.IGUAL, ecfNota.getEcfNotaSubserie());
            FiltroNumero fn = new FiltroNumero("ecfNotaNumero", ECompara.IGUAL, ecfNota.getEcfNotaNumero());
            GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{ft, ft1, fn});
            EcfNota aux = (EcfNota) service.selecionar(ecfNota, gf);

            if (aux == null) {
                emf = Conexao.getInstancia();
                em = emf.createEntityManager();
                em.getTransaction().begin();

                // identifica o cliente
                if (ecfNota.getSisCliente() != null) {
                    SisCliente cliente = getCliente(em, ecfNota.getSisCliente());
                    ecfNota.setSisCliente(cliente);
                }

                // salva a nota
                List<EcfNotaProduto> nps = ecfNota.getEcfNotaProdutos();
                ecfNota.setEcfNotaProdutos(null);
                ecfNota.setId(0);
                ecfNota = (EcfNota) service.salvar(em, ecfNota);
                em.getTransaction().commit();

                // salva os produtos vendidos
                List<Sql> sqls = new ArrayList<>();
                for (EcfNotaProduto np : nps) {
                    np.setId(0);
                    np.setEcfNota(ecfNota);
                    getEstoque(sqls, np.getEcfNotaProdutoQuantidade(), np.getProdEmbalagem(), np.getProdProduto(), np.getEcfNotaProdutoBarra());
                }
                service.salvar(nps);

                // atualiza o estoque se nao cancelada
                if (ecfNota.isEcfNotaCancelada() == false) {
                    service.executar(sqls.toArray(new Sql[]{}));
                }
            }
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao salvar a nota.", ex);
            throw new RestException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que cadastra na base do server as nfe emitidas pelos sistemas em
     * modo client.
     *
     * @param ecfNfe um objeto do tipo NFe.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/nfe")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setNfe(EcfNotaEletronica ecfNfe) throws RestException {
        autorizar();
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            FiltroTexto ft1 = new FiltroTexto("ecfNotaEletronicaChave", ECompara.IGUAL, ecfNfe.getEcfNotaEletronicaChave());
            EcfNotaEletronica aux = (EcfNotaEletronica) service.selecionar(ecfNfe, ft1);

            if (aux == null) {
                emf = Conexao.getInstancia();
                em = emf.createEntityManager();
                em.getTransaction().begin();

                // identifica o cliente
                if (ecfNfe.getSisCliente() != null) {
                    SisCliente cliente = getCliente(em, ecfNfe.getSisCliente());
                    ecfNfe.setSisCliente(cliente);
                }

                // salva a nfe
                ecfNfe.setId(0);
                ecfNfe = (EcfNotaEletronica) service.salvar(em, ecfNfe);

                // atualiza o estoque
                if (ecfNfe.getEcfNotaEletronicaStatus().equals(ENotaStatus.AUTORIZADO.toString())) {
                    TNfeProc produtos = NFe.xmlToObj(ecfNfe.getEcfNotaEletronicaXml(), TNfeProc.class);
                    List<Sql> sqls = new ArrayList<>();
                    for (TNFe.InfNFe.Det det : produtos.getNFe().getInfNFe().getDet()) {
                        // achando o produto
                        IFiltro filtro;
                        if (det.getProd().getCEAN() == null || det.getProd().getCEAN().equals("")) {
                            filtro = new FiltroNumero("prodProdutoId", ECompara.IGUAL, det.getProd().getCProd());
                        } else {
                            filtro = new FiltroTexto("prodProdutoBarra", ECompara.IGUAL, det.getProd().getCEAN());
                        }
                        ProdProduto prod = (ProdProduto) service.selecionar(new ProdProduto(), filtro);
                        // achando a embalagem usada na venda
                        FiltroTexto ft = new FiltroTexto("prodEmbalagemNome", ECompara.IGUAL, det.getProd().getUCom());
                        ProdEmbalagem emb = (ProdEmbalagem) service.selecionar(new ProdEmbalagem(), ft);
                        double qtd = Double.valueOf(det.getProd().getQCom());
                        // monta a atualizacao do estoque
                        getEstoque(sqls, qtd, emb, prod, "");
                    }
                    em.getTransaction().commit();
                    // remove do estoque
                    service.executar(sqls.toArray(new Sql[]{}));

                    // salva o xml no arquivo
                    File xml = new File("nfe/" + ecfNfe.getEcfNotaEletronicaChave() + "-procNFe.xml");
                    try (FileWriter fw = new FileWriter(xml)) {
                        fw.write(ecfNfe.getEcfNotaEletronicaXml());
                    } catch (IOException io) {
                        log.error("Erro ao salvar o arquivo xml.", io);
                    }

                    // salva o danfe no arquivo
                    File danfe = new File("nfe/" + ecfNfe.getEcfNotaEletronicaChave() + "-procNFe.pdf");
                    try (FileOutputStream fos = new FileOutputStream(danfe)) {
                        byte[] pdf = NFe.getDanfe(ecfNfe.getEcfNotaEletronicaXml());
                        fos.write(pdf);
                        fos.flush();
                    } catch (IOException io) {
                        log.error("Erro ao salvar o arquivo danfe.", io);
                    }
                } else if (ecfNfe.getEcfNotaEletronicaStatus().equals(ENotaStatus.CANCELADO.toString())) {
                    TNfeProc produtos = NFe.xmlToObj(ecfNfe.getEcfNotaEletronicaXml(), TNfeProc.class);
                    List<Sql> sqls = new ArrayList<>();
                    for (TNFe.InfNFe.Det det : produtos.getNFe().getInfNFe().getDet()) {
                        // achando o produto
                        IFiltro filtro;
                        if (det.getProd().getCEAN() == null || det.getProd().getCEAN().equals("")) {
                            filtro = new FiltroNumero("prodProdutoId", ECompara.IGUAL, det.getProd().getCProd());
                        } else {
                            filtro = new FiltroTexto("prodProdutoBarra", ECompara.IGUAL, det.getProd().getCEAN());
                        }
                        ProdProduto prod = (ProdProduto) service.selecionar(new ProdProduto(), filtro);
                        // achando a embalagem usada na venda
                        FiltroTexto ft = new FiltroTexto("prodEmbalagemNome", ECompara.IGUAL, det.getProd().getUCom());
                        ProdEmbalagem emb = (ProdEmbalagem) service.selecionar(new ProdEmbalagem(), ft);
                        double qtd = Double.valueOf(det.getProd().getQCom());
                        // devolve ao estoque
                        getEstoque(sqls, -1 * qtd, emb, prod, "");
                    }
                    em.getTransaction().commit();
                    // remove do estoque
                    service.executar(sqls.toArray(new Sql[]{}));

                    // salva o xml no arquivo
                    File xml = new File("nfe/" + ecfNfe.getEcfNotaEletronicaChave() + "-procCanNFe.xml");
                    try (FileWriter fw = new FileWriter(xml)) {
                        fw.write(ecfNfe.getEcfNotaEletronicaXmlCancelado());
                    } catch (IOException io) {
                        log.error("Erro ao salvar o arquivo xml de cancelamento.", io);
                    }
                } else if (ecfNfe.getEcfNotaEletronicaStatus().equals(ENotaStatus.INUTILIZADO.toString())) {
                    em.getTransaction().commit();
                    // salva o xml no arquivo
                    File xml = new File("nfe/" + ecfNfe.getEcfNotaEletronicaChave() + "-procInutNFe.xml");
                    try (FileWriter fw = new FileWriter(xml)) {
                        fw.write(ecfNfe.getEcfNotaEletronicaXml());
                    } catch (IOException io) {
                        log.error("Erro ao salvar o arquivo xml de inutilizacao.", io);
                    }
                }
            }
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao salvar a nfe.", ex);
            throw new RestException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que cadastra na base do server os clientesclieados pelos sistemas
     * em modo client.
     *
     * @param cliente um objeto do tipo SisCliente.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/cliente")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setCliente(SisCliente cliente) throws RestException {
        autorizar();
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();
            em.getTransaction().begin();
            getCliente(em, cliente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.error("Erro ao salvar cliente.", ex);
            throw new RestException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que cadastra na base do server as vendas, produtos vendidos,
     * pagamentos emitidos pelos sistemas em modo client.
     *
     * @param venda um objeto do tipo Venda.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/venda")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setVenda(EcfVenda venda) throws RestException {
        autorizar();
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();
            em.getTransaction().begin();

            // pega a impressora correta no sistema
            EcfImpressora imp = getImp(venda.getEcfImpressora().getEcfImpressoraSerie());
            venda.setEcfImpressora(imp);

            // identifica o cliente
            if (venda.getSisCliente() != null) {
                SisCliente cliente = getCliente(em, venda.getSisCliente());
                venda.setSisCliente(cliente);
            }

            // guarda os produtos vendidos , pagamentos e trocas
            List<EcfVendaProduto> vps = venda.getEcfVendaProdutos();
            List<EcfPagamento> pags = venda.getEcfPagamentos();
            List<EcfTroca> trocas = venda.getEcfTrocas();
            List<Sql> sqls = new ArrayList<>();

            // caso a venda esteja cancelada e ja estava salva no servidor
            if (venda.getEcfVendaCancelada()) {
                // filtro pra deletar a venda ja salva antes
                FiltroObjeto fo = new FiltroObjeto("ecfImpressora", ECompara.IGUAL, imp);
                FiltroNumero fn = new FiltroNumero("ecfVendaCcf", ECompara.IGUAL, venda.getEcfVendaCcf());
                FiltroNumero fn1 = new FiltroNumero("ecfVendaCoo", ECompara.IGUAL, venda.getEcfVendaCoo());
                GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fo, fn, fn1});
                // executa o comando
                Sql sql = new Sql(venda, EComandoSQL.EXCLUIR, gf);
                List<Integer> resp = service.executar(sql);
                // caso ja exista e tenha deletado, remove do estoque
                if (resp.get(0) == 1) {
                    for (EcfVendaProduto vp : vps) {
                        if (!vp.getEcfVendaProdutoCancelado()) {
                            getEstoque(sqls, vp.getEcfVendaProdutoQuantidade() * -1, vp.getProdEmbalagem(), vp.getProdProduto(), vp.getEcfVendaProdutoBarra());
                        }
                    }
                }
            }

            // salva a venda
            venda.setId(0);
            venda.setEcfVendaProdutos(null);
            venda.setEcfPagamentos(null);
            venda.setEcfTrocas(null);
            venda = (EcfVenda) service.salvar(em, venda);

            // salva os produtos e atualiza o estoque
            for (EcfVendaProduto vp : vps) {
                vp.setId(0);
                vp.setEcfVenda(venda);
                vp = (EcfVendaProduto) service.salvar(em, vp);

                if (!venda.getEcfVendaCancelada() && !vp.getEcfVendaProdutoCancelado()) {
                    getEstoque(sqls, vp.getEcfVendaProdutoQuantidade(), vp.getProdEmbalagem(), vp.getProdProduto(), vp.getEcfVendaProdutoBarra());
                }
            }

            // atualiza com as instrucoes SQL.
            for (Sql sql : sqls) {
                service.executar(em, sql);
            }

            if (!venda.getEcfVendaCancelada()) {
                // salva os pagamentos
                for (EcfPagamento pag : pags) {
                    pag.setEcfVenda(venda);
                    salvarPagamento(em, pag);
                }

                // salva as trocas
                if (trocas != null) {
                    for (EcfTroca troca : trocas) {
                        troca.setEcfVenda(venda);
                        salvarTroca(em, troca);
                    }
                }
            }

            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao salvar venda.", ex);
            throw new RestException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que cadastra na base do server as reducoes Z, totais, documentos
     * emitidos pelos sistemas em modo client.
     *
     * @param ecfZ um objeto do tipo ReducaoZ com a lista de documentos anexada.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/reducaoZ")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setReducaoZ(EcfZ ecfZ) throws RestException {
        autorizar();
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            // pega a impressora correta no sistema
            EcfImpressora imp = getImp(ecfZ.getEcfImpressora().getEcfImpressoraSerie());

            // valida se ja existe
            FiltroObjeto fo = new FiltroObjeto("ecfImpressora", ECompara.IGUAL, imp);
            FiltroNumero fn = new FiltroNumero("ecfZCrz", ECompara.IGUAL, ecfZ.getEcfZCrz());
            GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fo, fn});
            EcfZ aux = (EcfZ) service.selecionar(ecfZ, gf);

            if (aux == null) {
                emf = Conexao.getInstancia();
                em = emf.createEntityManager();
                em.getTransaction().begin();

                // guarda os totais e vendas e docs
                List<EcfZTotais> totais = ecfZ.getEcfZTotais();
                List<EcfDocumento> docs = ecfZ.getEcfDocumentos();

                // salva a reduzaoZ
                ecfZ.setId(0);
                ecfZ.setEcfImpressora(imp);
                ecfZ.setEcfZTotais(null);
                ecfZ.setEcfVendas(null);
                ecfZ.setEcfDocumentos(null);
                ecfZ = (EcfZ) service.salvar(em, ecfZ);

                // salva os totais
                for (EcfZTotais tot : totais) {
                    tot.setEcfZ(ecfZ);
                    tot.setId(0);
                    service.salvar(em, tot);
                }

                // salva os documentos
                for (EcfDocumento doc : docs) {
                    doc.setEcfImpressora(imp);
                    doc.setEcfZ(ecfZ);
                    doc.setId(0);
                    service.salvar(em, doc);
                }

                // atualiza as vendas
                Calendar cal = Calendar.getInstance();
                cal.setTime(ecfZ.getEcfZMovimento());
                cal.add(Calendar.DAY_OF_MONTH, 1);
                Date fim = cal.getTime();

                FiltroData fd = new FiltroData("ecfVendaData", ECompara.MAIOR_IGUAL, ecfZ.getEcfZMovimento());
                FiltroData fd1 = new FiltroData("ecfVendaData", ECompara.MENOR, fim);
                GrupoFiltro gf1 = new GrupoFiltro(EJuncao.E, new IFiltro[]{fo, fd, fd1});
                ParametroObjeto po = new ParametroObjeto("ecfZ", ecfZ);

                Sql sql = new Sql(new EcfVenda(), EComandoSQL.ATUALIZAR, gf1, po);
                service.executar(em, sql);
                em.getTransaction().commit();
            }
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            log.error("Erro ao salvar reduzao Z.", ex);
            throw new RestException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que salva a troca no sistema fazendo as validacoes.
     *
     * @param em um objeto de transacao.
     * @param troca o objeto de troca.
     * @throws OpenPdvException dispara em caso de erro ao salvar.
     */
    private void salvarTroca(EntityManager em, EcfTroca troca) throws OpenPdvException {
        // guarda os produtos da troca
        List<EcfTrocaProduto> tps = troca.getEcfTrocaProdutos();

        // salva a troca
        troca.setId(0);
        troca.setEcfTrocaProdutos(null);
        troca = (EcfTroca) service.salvar(em, troca);

        // salva os produtos da troca e atualiza o estoque
        for (EcfTrocaProduto tp : tps) {
            tp.setId(0);
            tp.setEcfTroca(troca);
            service.salvar(em, tp);
        }
    }

    /**
     * Metodo que salva os pagamentos no sistema fazendo as validacoes.
     *
     * @param em um objeto de transacao.
     * @param pag o objeto de pagamento.
     * @throws OpenPdvException dispara em caso de erro ao salvar.
     */
    private void salvarPagamento(EntityManager em, EcfPagamento pag) throws OpenPdvException {
        // guarda as parcelas
        List<EcfPagamentoParcela> parcelas = pag.getEcfPagamentoParcelas();

        // salva o pagamento
        pag.setId(0);
        pag.setEcfPagamentoParcelas(null);
        pag = (EcfPagamento) service.salvar(em, pag);

        // salva as parcelas
        for (EcfPagamentoParcela parcela : parcelas) {
            parcela.setEcfPagamento(pag);
            parcela.setId(0);
            service.salvar(em, parcela);
        }
    }

    /**
     * Metodo que encontra o cliente dentro do sistema usando os dados do
     * cliente enviado, como o CNPJ.
     *
     * @param em um objeto de transacao.
     * @param sisCliente o obejto de cliente.
     * @return o cliente encontrado nesta base de dados.
     * @throws OpenPdvException dispara em caso de erro ao selecionar.
     */
    private SisCliente getCliente(EntityManager em, SisCliente sisCliente) {
        try {
            FiltroTexto ft = new FiltroTexto("sisClienteDoc", ECompara.IGUAL, sisCliente.getSisClienteDoc().replaceAll("\\D", ""));
            SisCliente aux = (SisCliente) service.selecionar(sisCliente, ft);

            // se existir atualiza, senao cria um novo
            sisCliente.setId(aux != null ? aux.getSisClienteId() : 0);
            sisCliente.setSisClienteSinc(true);
            return (SisCliente) service.salvar(em, sisCliente);
        } catch (OpenPdvException ex) {
            return null;
        }
    }

    /**
     * Metodo que gera o SQL de atualizacao do estoque para as vendas recebidas.
     *
     * @param qtd a quantidade de produtos vendidos.
     * @param emb o tipo de embalagem usada na venda.
     * @param prod o produto que foi vendido.
     * @param barra o codigo escolhido na hora da venda.
     * @return uma instrucao de SQL no formato de objeto para ser executada.
     * @throws CoreException dispara caso nao consiga gerar o sql de
     * atualizacao.
     */
    private void getEstoque(List<Sql> sqls, double qtd, ProdEmbalagem emb, ProdProduto prod, String barra) throws OpenPdvException {
        // fatorando a quantida no estoque
        if (emb.getProdEmbalagemId() != prod.getProdEmbalagem().getProdEmbalagemId()) {
            qtd *= emb.getProdEmbalagemUnidade();
            qtd /= prod.getProdEmbalagem().getProdEmbalagemUnidade();
        }

        // atualiza o estoque
        ParametroFormula pf = new ParametroFormula("prodProdutoEstoque", -1 * qtd);
        FiltroNumero fn1 = new FiltroNumero("prodProdutoId", ECompara.IGUAL, prod.getId());
        Sql sql = new Sql(new ProdProduto(), EComandoSQL.ATUALIZAR, fn1, pf);
        sqls.add(sql);

        // remove estoque da grade caso o produto tenha
        if (prod.getProdGrades() != null) {
            // formando os parametros e filtros
            ParametroFormula pf2 = new ParametroFormula("prodGradeEstoque", -1 * qtd);
            FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, prod);
            FiltroTexto ft = new FiltroTexto("prodGradeBarra", ECompara.IGUAL, barra);
            GrupoFiltro gf1 = new GrupoFiltro(EJuncao.E, new IFiltro[]{fo, ft});
            // busca o item
            Sql sql1 = new Sql(new ProdGrade(), EComandoSQL.ATUALIZAR, gf1, pf2);
            sqls.add(sql1);
        }
    }
}
