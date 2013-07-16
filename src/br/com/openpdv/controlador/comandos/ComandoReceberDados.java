package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
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
import br.com.phdss.controlador.PAF;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private StringBuilder erros;

    public ComandoReceberDados() {
        this.service = new CoreService();
        this.log = Logger.getLogger(ComandoReceberDados.class);
        this.erros = new StringBuilder();
    }

    @Override
    public void executar() throws OpenPdvException {
        WebResource wr;

        // atualiza os usuarios
        try {
            wr = Util.getRest(Util.getConfig().get("sinc.host") + "/usuario");
            List<SisUsuario> usuarios = wr.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<SisUsuario>>() {
            });

            for (SisUsuario usu : usuarios) {
                usu.setSisUsuarioLogin(Util.normaliza(usu.getSisUsuarioLogin()));
                service.salvar(usu);
            }
            log.info("Dados usuarios recebidos -> " + usuarios.size());
        } catch (Exception ex) {
            erros.append("Erro no recebimento dos Usuarios.\n");
            log.error("Erro no recebimento dos Usuarios.", ex);
        }

        // atualiza os clientes
        try {
            Date daCli = (Date) service.buscar(new SisCliente(), "sisClienteData", EBusca.MAXIMO, null);
            wr = Util.getRest(Util.getConfig().get("sinc.host") + "/cliente");
            List<SisCliente> clientes = wr.queryParam("data", daCli != null ? Util.getData(daCli) : "").accept(MediaType.APPLICATION_JSON).get(new GenericType<List<SisCliente>>() {
            });

            for (SisCliente cli : clientes) {
                service.salvar(cli);
            }
            log.info("Dados clientes recebidos -> " + clientes.size());
        } catch (Exception ex) {
            erros.append("Erro no recebimento dos Clientes.\n");
            log.error("Erro no recebimento dos Clientes.", ex);
        }

        // atualiza os tipos de pagamento
        try {
            wr = Util.getRest(Util.getConfig().get("sinc.host") + "/tipo_pagamento/");
            List<EcfPagamentoTipo> tiposPagamento = wr.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<EcfPagamentoTipo>>() {
            });

            for (EcfPagamentoTipo tipo : tiposPagamento) {
                // Identifica se a forma de pagamento e uma das 4 permitidas [Dinheiro, Cheque, Cartao ou Troca]
                if (tipo.getEcfPagamentoTipoDescricao().equalsIgnoreCase("dinheiro")) {
                    tipo.setEcfPagamentoTipoCodigo(Util.getConfig().get("ecf.dinheiro"));
                    service.salvar(tipo);
                } else if (tipo.getEcfPagamentoTipoDescricao().equalsIgnoreCase("cheque")) {
                    tipo.setEcfPagamentoTipoCodigo(Util.getConfig().get("ecf.cheque"));
                    service.salvar(tipo);
                } else if (tipo.isEcfPagamentoTipoTef()) {
                    tipo.setEcfPagamentoTipoCodigo(Util.getConfig().get("ecf.cartao"));
                    service.salvar(tipo);
                } else if (tipo.getEcfPagamentoTipoDescricao().equalsIgnoreCase("troca")) {
                    tipo.setEcfPagamentoTipoCodigo(Util.getConfig().get("ecf.troca"));
                    service.salvar(tipo);
                }
            }
            log.info("Dados tipos pagamento recebidos -> " + tiposPagamento.size());
        } catch (Exception ex) {
            erros.append("Erro no recebimento dos Tipos de Pagamentos.\n");
            log.error("Erro no recebimento dos Tipos de Pagamentos.", ex);
        }

        // atualiza as embalagens
        try {
            wr = Util.getRest(Util.getConfig().get("sinc.host") + "/embalagem");
            List<ProdEmbalagem> embalagens = wr.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<ProdEmbalagem>>() {
            });

            for (ProdEmbalagem emb : embalagens) {
                service.salvar(emb);
            }
            log.info("Dados embalagens recebidos -> " + embalagens.size());
        } catch (Exception ex) {
            erros.append("Erro no recebimento das Embalagens.\n");
            log.error("Erro no recebimento das Embalagens.", ex);
        }

        // atualiza os tipos de grades
        try {
            wr = Util.getRest(Util.getConfig().get("sinc.host") + "/tipo_grade");
            List<ProdGradeTipo> tipos = wr.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<ProdGradeTipo>>() {
            });

            for (ProdGradeTipo tipo : tipos) {
                service.salvar(tipo);
            }
            log.info("Dados tipos grade recebidos -> " + tipos.size());
        } catch (Exception ex) {
            erros.append("Erro no recebimento dos Tipos de Grades.\n");
            log.error("Erro no recebimento dos Tipos de Grades.", ex);
        }

        // recupera os novos produtos
        boolean prodSinc = true;
        int limite = Integer.valueOf(Util.getConfig().get("sinc.limite"));
        int pagina = 0;
        List<ProdProduto> novos;
        List<ProdPreco> precos = new ArrayList<>();
        List<ProdComposicao> comps = new ArrayList<>();
        List<ProdGrade> grades = new ArrayList<>();

        // parametros
        MultivaluedMap<String, String> mm = new MultivaluedMapImpl();
        Integer maxId = (Integer) service.buscar(new ProdProduto(), "prodProdutoId", EBusca.MAXIMO, null);
        mm.putSingle("id", maxId != null ? maxId.toString() : "0");
        mm.putSingle("limite", String.valueOf(limite));

        do {
            mm.putSingle("pagina", String.valueOf(pagina));
            wr = Util.getRest(Util.getConfig().get("sinc.host") + "/produtoNovo");
            novos = wr.queryParams(mm).accept(MediaType.APPLICATION_JSON).get(new GenericType<List<ProdProduto>>() {
            });

            for (ProdProduto prod : novos) {
                try {
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
                    // salva o produto
                    prod.setProdPrecos(null);
                    prod.setProdComposicoes(null);
                    prod.setProdGrades(null);
                    prod.setProdProdutoDescricao(Util.normaliza(prod.getProdProdutoDescricao()));
                    service.salvar(prod);
                } catch (Exception ex) {
                    prodSinc = false;
                    log.error("Nao salvou o produto com ID = " + prod.getProdProdutoId(), ex);
                }
            }
            log.info("Dados dos produtos novos recebidos da pagina " + pagina);
            pagina++;
        } while (novos.size() == limite);

        // salva os precos
        for (ProdPreco preco : precos) {
            try {
                service.salvar(preco);
            } catch (Exception ex) {
                prodSinc = false;
                log.error("Nao salvou o preco do produto com ID = " + preco.getProdProduto().getProdProdutoId(), ex);
            }
        }
        // salva os itens
        for (ProdComposicao comp : comps) {
            try {
                service.salvar(comp);
                prodSinc = false;
            } catch (Exception ex) {
                log.error("Nao salvou a composicao do produto com ID = " + comp.getProdProduto().getProdProdutoId(), ex);
            }
        }
        // salva as grades 
        for (ProdGrade grade : grades) {
            try {
                service.salvar(grade);
                prodSinc = false;
            } catch (Exception ex) {
                log.error("Nao salvou a grade do produto com ID = " + grade.getProdProduto().getProdProdutoId(), ex);
            }
        }

        // recupera os produtos atualizados
        pagina = 0;
        List<ProdProduto> atualizados;
        mm.clear();
        Date da = (Date) service.buscar(new ProdProduto(), "prodProdutoAlterado", EBusca.MAXIMO, null);
        mm.putSingle("data", da != null ? Util.getData(da) : "");
        mm.putSingle("limite", String.valueOf(limite));

        do {
            mm.putSingle("pagina", String.valueOf(pagina));
            wr = Util.getRest(Util.getConfig().get("sinc.host") + "/produtoAtualizado");
            atualizados = wr.queryParams(mm).accept(MediaType.APPLICATION_JSON).get(new GenericType<List<ProdProduto>>() {
            });

            for (ProdProduto prod : atualizados) {
                try {
                    // guarda as sub listas
                    precos = prod.getProdPrecos();
                    prod.setProdPrecos(null);
                    comps = prod.getProdComposicoes();
                    prod.setProdComposicoes(null);
                    grades = prod.getProdGrades();
                    prod.setProdGrades(null);
                    // salva o produto
                    prod.setProdProdutoDescricao(Util.normaliza(prod.getProdProdutoDescricao()));
                    service.salvar(prod);
                    // salva os precos
                    if (!precos.isEmpty()) {
                        FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, prod);
                        Sql sql = new Sql(new ProdPreco(), EComandoSQL.EXCLUIR, fo);
                        service.executar(sql);
                        for (ProdPreco preco : precos) {
                            preco.setProdProduto(prod);
                            service.salvar(preco);
                        }
                    }
                    // salva os itens
                    if (!comps.isEmpty()) {
                        FiltroObjeto fo1 = new FiltroObjeto("prodProdutoPrincipal", ECompara.IGUAL, prod);
                        Sql sql1 = new Sql(new ProdComposicao(), EComandoSQL.EXCLUIR, fo1);
                        service.executar(sql1);
                        for (ProdComposicao comp : comps) {
                            comp.setProdProdutoPrincipal(prod);
                            service.salvar(comp);
                        }
                    }
                    // salva as grades
                    if (!grades.isEmpty()) {
                        FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, prod);
                        Sql sql = new Sql(new ProdGrade(), EComandoSQL.EXCLUIR, fo);
                        service.executar(sql);
                        for (ProdGrade grade : grades) {
                            grade.setProdProduto(prod);
                            service.salvar(grade);
                        }
                    }
                } catch (Exception ex) {
                    prodSinc = false;
                    log.error("Nao atualizou o produto com ID = " + prod.getProdProdutoId(), ex);
                }
            }
            log.info("Dados dos produtos atualizados recebidos da pagina " + pagina);
            pagina++;
        } while (atualizados.size() == limite);

        // verifica se teve algum erro em produtos
        if (prodSinc == false) {
            erros.append("Ocorreu erro ao receber algum Produto.\n");
        }

        // se sucesso atualiza no arquivo a data do ultimo recebimento
        if (erros.length() == 0) {
            try {
                PAF.AUXILIAR.setProperty("out.recebimento", Util.getDataHora(new Date()));
                PAF.criptografar();
            } catch (Exception ex) {
                throw new OpenPdvException("Erro ao salvar no arquivo auxiliar.\nVerifique o log do sistema.", ex);
            }
        } else {
            erros.append("Verifique o log do sistema.");
            throw new OpenPdvException(erros.toString());
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }
}
