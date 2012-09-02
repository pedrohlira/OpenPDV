package br.com.openpdv.rest;

import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.EcfImpressora;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import br.com.openpdv.modelo.ecf.EcfPagamentoTipo;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.openpdv.modelo.sistema.SisUsuario;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

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

    @Path("/nfe")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getNfe() throws RestException {
        autorizar();
        try {
            List<EcfNotaEletronica> nfes = service.selecionar(new EcfNotaEletronica(), 0, 1, null);
            Integer resp;
            if (nfes != null && nfes.size() == 1) {
                resp = nfes.get(0).getEcfNotaEletronicaNumero() + 1;
            } else {
                resp = Integer.valueOf(Util.getConfig().get("nfe.numero")) + 1;
            }
            return resp.toString();
        } catch (OpenPdvException ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }

    @Path("/empresa/{cnpj}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SisEmpresa getEmpresa(@PathParam("cnpj") String cnpj) throws RestException {
        try {
            FiltroTexto ft = new FiltroTexto("sisEmpresaCnpj", ECompara.IGUAL, cnpj.replaceAll("[^0-9]", ""));
            return (SisEmpresa) service.selecionar(new SisEmpresa(), ft);
        } catch (Exception ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }

    @Path("/impressora")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EcfImpressora> getImpressora() throws RestException {
        try{
            return service.selecionar(new EcfImpressora(), 0, 0, null);
        } catch (Exception ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }

    @Path("/impressora/{serie}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public EcfImpressora getImpressora(@PathParam("serie") String serie) throws RestException {
        return getImp(serie);
    }

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

    @Path("/produtoNovo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProdProduto> getProdutoNovo(@QueryParam("data") String data, @QueryParam("pagina") int pagina, @QueryParam("limite") int limite) throws RestException {
        autorizar();
        try {
            IFiltro filtro = null;
            if (data != null && !data.equals("")) {
                filtro = new FiltroData("prodProdutoCadastrado", ECompara.MAIOR, Util.getDataHora(data));
            }

            ProdProduto prod = new ProdProduto();
            prod.setCampoOrdem("prodProdutoCadastrado");
            return service.selecionar(prod, pagina * limite, limite, filtro);
        } catch (Exception ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }

    @Path("/produtoAtualizado")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProdProduto> getProdutoAtualizado(@QueryParam("data") String data, @QueryParam("pagina") int pagina, @QueryParam("limite") int limite) throws RestException {
        autorizar();
        try {
            IFiltro filtro = null;
            if (data != null && !data.equals("")) {
                FiltroData fd1 = new FiltroData("prodProdutoAlterado", ECompara.MAIOR, Util.getDataHora(data));
                FiltroData fd2 = new FiltroData("prodProdutoCadastrado", ECompara.MENOR, Util.getDataHora(data));
                filtro = new GrupoFiltro(EJuncao.E, new IFiltro[]{fd1, fd2});
            }

            ProdProduto prod = new ProdProduto();
            prod.setCampoOrdem("prodProdutoAlterado");
            return service.selecionar(prod, pagina * limite, limite, filtro);
        } catch (Exception ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }
}
