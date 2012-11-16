package br.com.openpdv.rest;

import br.com.openpdv.controlador.core.Conexao;
import br.com.openpdv.controlador.core.NFe;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.core.filtro.FiltroTexto;
import br.com.openpdv.modelo.core.filtro.IFiltro;
import br.com.openpdv.modelo.core.parametro.ParametroFormula;
import br.com.openpdv.modelo.ecf.*;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.openpdv.nfe.TNFe;
import br.com.openpdv.nfe.TNfeProc;
import br.com.phdss.controlador.PAF;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
     * Metodo usado para cadastrar a empresa no BD atraves do sistema, onde sera
     * validado com os dados contidos no arquivo criptografo auxiliar.
     *
     * @param sisEmpresa objeto do tipo empresa a ser cadastrado.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/empresa")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setEmpresa(SisEmpresa sisEmpresa) throws RestException {
        try {
            if (PAF.AUXILIAR.getProperty("cli.cnpj").equals(sisEmpresa.getSisEmpresaCnpj())) {
                sisEmpresa.setId(1);
                service.salvar(sisEmpresa);
            } else {
                throw new RestException("O CNPJ informado da empresa não é igual ao registrado no arquivo auxiliar.txt");
            }
        } catch (Exception ex) {
            log.error("Erro ao salvar a empresa.", ex);
            throw new RestException(ex);
        }
    }

    /**
     * Metodo usado para cadastrar o ECF no BD atraves do sistema, onde sera
     * validado com os dados contidos no arquivo criptografo auxiliar.
     *
     * @param ecfImpressora objeto do tipo impressora a ser cadastrado.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/impressora")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setImpressora(EcfImpressora ecfImpressora) throws RestException {
        try {
            service.salvar(ecfImpressora);
        } catch (Exception ex) {
            log.error("Erro ao salvar a impressora.", ex);
            throw new RestException(ex);
        }
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
            List<Sql> sqls = new ArrayList<Sql>();
            for (EcfNotaProduto np : nps) {
                np.setId(0);
                np.setEcfNota(ecfNota);
                Sql sql = getEstoque(np.getEcfNotaProdutoQuantidade(), np.getProdEmbalagem(), np.getProdProduto());
                sqls.add(sql);
            }
            service.salvar(nps);

            // atualiza o estoque se nao cancelada
            if (ecfNota.isEcfNotaCancelada() == false) {
                service.executar(sqls);
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
                    sqls.add(getEstoque(qtd, emb, prod));
                }
                em.getTransaction().commit();
                // remove do estoque
                service.executar(sqls);

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
                    sqls.add(getEstoque(-1 * qtd, emb, prod));
                }
                em.getTransaction().commit();
                // remove do estoque
                service.executar(sqls);

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
     * Metodo que cadastra na base do server as reducoes Z, totais, vendas,
     * produtos vendidos, pagamentos, documentos emitidos pelos sistemas em modo
     * client.
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
            emf = Conexao.getInstancia();
            em = emf.createEntityManager();
            em.getTransaction().begin();

            // pega a impressora correta no sistema
            EcfImpressora imp = getImp(ecfZ.getEcfImpressora().getEcfImpressoraSerie());
            ecfZ.setEcfImpressora(imp);

            // guarda os totais e vendas e docs
            List<EcfZTotais> totais = ecfZ.getEcfZTotais();
            List<EcfVenda> vendas = ecfZ.getEcfVendas();
            List<EcfDocumento> docs = ecfZ.getEcfDocumentos();

            // salva a reduzaoZ
            ecfZ.setId(0);
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

            // salva as vendas
            for (EcfVenda venda : vendas) {
                venda.setEcfZ(ecfZ);
                salvarVenda(em, venda);
            }

            // salva os documentos
            for (EcfDocumento doc : docs) {
                doc.setEcfImpressora(imp);
                doc.setId(0);
                service.salvar(em, doc);
            }

            em.getTransaction().commit();
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
     * Metodo que salva as vendas no sistema fazendo as validacoes.
     *
     * @param em um objeto de transacao.
     * @param venda o objeto de venda.
     * @throws OpenPdvException dispara em caso de erro ao salvar.
     */
    private void salvarVenda(EntityManager em, EcfVenda venda) throws OpenPdvException {
        // identifica o cliente
        if (venda.getSisCliente() != null) {
            SisCliente cliente = getCliente(em, venda.getSisCliente());
            venda.setSisCliente(cliente);
        }

        // guarda os produtos vendidos e pagamentos
        List<EcfVendaProduto> vps = venda.getEcfVendaProdutos();
        List<EcfPagamento> pags = venda.getEcfPagamentos();

        // salva a venda
        venda.setId(0);
        venda.setEcfVendaProdutos(null);
        venda.setEcfPagamentos(null);
        venda = (EcfVenda) service.salvar(em, venda);

        // salva os produtos e atualiza o estoque
        for (EcfVendaProduto vp : vps) {
            vp.setId(0);
            vp.setEcfVenda(venda);
            vp = (EcfVendaProduto) service.salvar(em, vp);

            if (!vp.getEcfVendaProdutoCancelado()) {
                service.executar(em, getEstoque(vp.getEcfVendaProdutoQuantidade(), vp.getProdEmbalagem(), vp.getProdProduto()));
            }
        }

        // salva os pagamentos
        for (EcfPagamento pag : pags) {
            pag.setEcfVenda(venda);
            salvarPagamento(em, pag);
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
    private SisCliente getCliente(EntityManager em, SisCliente sisCliente) throws OpenPdvException {
        FiltroTexto ft = new FiltroTexto("sisClienteDoc", ECompara.IGUAL, sisCliente.getSisClienteDoc().replaceAll("[^0-9]", ""));
        SisCliente aux = (SisCliente) service.selecionar(new SisCliente(), ft);

        // se existir retornar, senao cria um novo
        if (aux != null) {
            return aux;
        } else {
            sisCliente.setId(0);
            sisCliente.setSisClienteCadastrado(new Date());
            return (SisCliente) service.salvar(em, sisCliente);
        }
    }

    /**
     * Metodo que gera o SQL de atualizacao do estoque para as vendas recebidas.
     *
     * @param qtd a quantidade de produtos vendidos.
     * @param emb o tipo de embalagem usada na venda.
     * @param prod o produto que foi vendido.
     * @return uma instrucao de SQL no formato de objeto para ser executada.
     * @throws OpenPdvException dispara caso nao consiga gerar o sql de
     * atualizacao.
     */
    private Sql getEstoque(double qtd, ProdEmbalagem emb, ProdProduto prod) throws OpenPdvException {
        // fatorando a quantida no estoque
        if (emb.getProdEmbalagemId() != prod.getProdEmbalagem().getProdEmbalagemId()) {
            qtd *= emb.getProdEmbalagemUnidade();
            qtd /= prod.getProdEmbalagem().getProdEmbalagemUnidade();
        }

        // atualiza o estoque
        ParametroFormula pf = new ParametroFormula("prodProdutoEstoque", -1 * qtd);
        FiltroNumero fn1 = new FiltroNumero("prodProdutoId", ECompara.IGUAL, prod.getId());
        return new Sql(new ProdProduto(), EComandoSQL.ATUALIZAR, fn1, pf);
    }
}
