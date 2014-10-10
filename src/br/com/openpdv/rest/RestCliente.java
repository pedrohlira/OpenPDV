package br.com.openpdv.rest;

import br.com.phdss.Util;
import br.com.openpdv.modelo.core.EBusca;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import br.com.openpdv.modelo.ecf.EcfPagamentoTipo;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdGradeTipo;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.modelo.sistema.SisUsuario;
import java.util.Date;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.Logger;

/**
 * Classe que representa a comunicao do Servidor para o Cliente via Rest
 *
 * @author Pedro H. Lira
 */
@Provider
@Path("/openpdv/host")
public class RestCliente extends ARest {

    /**
     * Construtor padrao.
     */
    public RestCliente() {
        super();
        log = Logger.getLogger(RestCliente.class);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Override
    public String ajuda() throws RestException {
        return super.ajuda();
    }

    /**
     * Metodo que retorna o proximo numero de NFe a ser usado.
     *
     * @return uma string com o nuemro da NFe.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/nfe")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getNfe() throws RestException {
        autorizar();
        try {
            Number nfe = (Number) service.buscar(new EcfNotaEletronica(), "ecfNotaEletronicaNumero", EBusca.MAXIMO, null);
            Integer resp;
            if (nfe != null && nfe.intValue() > 0) {
                resp = nfe.intValue() + 1;
            } else {
                resp = Integer.valueOf(Util.getConfig().getProperty("nfe.numero")) + 1;
            }
            return resp.toString();
        } catch (OpenPdvException ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }

    /**
     * Metodo que retorna a lista de usuario permitidos ao acesso ao sistema.
     *
     * @return uma lista de objetos usuario em formato JSON.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/usuario")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SisUsuario> getUsuario() throws RestException {
        autorizar();
        try {
            return service.selecionar(new SisUsuario(), 0, 0, null);
        } catch (Exception ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }

    /**
     * Metodo que retorna a lista de tipos de pagamento cadastrados no sistema.
     *
     * @return uma lista de objetos tipos de pagamento em formato JSON.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/tipo_pagamento")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EcfPagamentoTipo> getPagamentoTipo() throws RestException {
        autorizar();
        try {
            return service.selecionar(new EcfPagamentoTipo(), 0, 0, null);
        } catch (Exception ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }

    /**
     * Metodo que retorna a lista de embalagens cadastradas no sistema.
     *
     * @return uma lista de objetos embalagem em formato JSON.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/embalagem")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProdEmbalagem> getEmbalagem() throws RestException {
        autorizar();
        try {
            return service.selecionar(new ProdEmbalagem(), 0, 0, null);
        } catch (Exception ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }

    /**
     * Metodo que retorna a lista de tipos de grades cadastradas no sistema.
     *
     * @return uma lista de objetos tipo grade em formato JSON.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/tipo_grade")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProdGradeTipo> getGradeTipo() throws RestException {
        autorizar();
        try {
            return service.selecionar(new ProdGradeTipo(), 0, 0, null);
        } catch (Exception ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }

    /**
     * Metodo que retorna a lista de novos produtos cadastrados no sistema.
     *
     * @param id o ultimo id cadastro no banco do pdv.
     * @param pagina numero da pagina de retorno dos dados comecando pelo ZERO.
     * @param limite limite de registros a serem retornados.
     * @return uma lista de produtos novos cadastrados no sistema.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/produtoNovo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProdProduto> getProdutoNovo(@QueryParam("id") int id, @QueryParam("pagina") int pagina, @QueryParam("limite") int limite) throws RestException {
        autorizar();
        try {
            FiltroNumero fn = new FiltroNumero("prodProdutoId", ECompara.MAIOR, id);
            ProdProduto prod = new ProdProduto();
            prod.setCampoOrdem("prodProdutoId");
            return service.selecionar(prod, pagina * limite, limite, fn);
        } catch (Exception ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }

    /**
     * Metodo que retorna a lista de novos produtos atualizados no sistema.
     *
     * @param data data usada como corte para considerar produto atualizado.
     * @param pagina numero da pagina de retorno dos dados comecando pelo ZERO.
     * @param limite limite de registros a serem retornados.
     * @return uma lista de produtos novos cadastrados no sistema.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/produtoAtualizado")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProdProduto> getProdutoAtualizado(@QueryParam("data") String data, @QueryParam("pagina") int pagina, @QueryParam("limite") int limite) throws RestException {
        autorizar();
        try {
            Date alterado = Util.formataData(data, "dd/MM/yyyy");
            Filtro filtro = null;
            if (alterado != null) {
                FiltroData fd1 = new FiltroData("prodProdutoAlterado", ECompara.MAIOR, alterado);
                FiltroData fd2 = new FiltroData("prodProdutoCadastrado", ECompara.MENOR, alterado);
                filtro = new FiltroGrupo(Filtro.E, fd1, fd2);
            }

            ProdProduto prod = new ProdProduto();
            prod.setCampoOrdem("prodProdutoAlterado");
            return service.selecionar(prod, pagina * limite, limite, filtro);
        } catch (Exception ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }
    
    /**
     * Metodo que retorna a lista de cliente do sistema.
     *
     * @param data data usada como corte para considerar novo cliente.
     * @param pagina numero da pagina de retorno dos dados comecando pelo ZERO.
     * @param limite limite de registros a serem retornados.
     * @return uma lista de objetos cliente em formato JSON.
     * @throws RestException em caso de nao conseguir acessar a informacao.
     */
    @Path("/cliente")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SisCliente> getClientes(@QueryParam("data") String data, @QueryParam("pagina") int pagina, @QueryParam("limite") int limite) throws RestException {
        autorizar();
        try {
            Date dt = Util.formataData(data, "dd/MM/yyyy");
            FiltroData fd = null;
            if (dt != null) {
                fd = new FiltroData("sisClienteData", ECompara.MAIOR_IGUAL, dt);
            }
            return service.selecionar(new SisCliente(), pagina * limite, limite, fd);
        } catch (Exception ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }
}
