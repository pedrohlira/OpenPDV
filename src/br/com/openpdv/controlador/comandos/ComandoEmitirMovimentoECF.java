package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.core.EBusca;
import br.com.openpdv.modelo.core.EDirecao;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.*;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.ECF;
import br.com.phdss.EComando;
import br.com.phdss.IECF;
import br.com.phdss.controlador.PAF;
import static br.com.phdss.controlador.PAF.AUXILIAR;
import br.com.phdss.modelo.anexo.iv.A2;
import br.com.phdss.modelo.anexo.iv.AnexoIV;
import br.com.phdss.modelo.anexo.iv.E2;
import br.com.phdss.modelo.anexo.iv.E3;
import br.com.phdss.modelo.anexo.iv.P2;
import br.com.phdss.modelo.anexo.iv.R01;
import br.com.phdss.modelo.anexo.iv.R02;
import br.com.phdss.modelo.anexo.iv.R03;
import br.com.phdss.modelo.anexo.iv.R04;
import br.com.phdss.modelo.anexo.iv.R05;
import br.com.phdss.modelo.anexo.iv.R06;
import br.com.phdss.modelo.anexo.iv.R07;
import br.com.phdss.modelo.anexo.iv.U1;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * Classe responsavel por executar o movimento do ecf.
 *
 * @author Pedro H. Lira
 */
public class ComandoEmitirMovimentoECF implements IComando {

    private Logger log;
    private CoreService service;
    private IECF ecf;
    private SisEmpresa empresa;
    private EcfImpressora impressora;
    private Date inicio;
    private Date fim;
    private List<ProdProduto> listaProd;
    private boolean auto;
    private String path;
    private String modeloECF;
    // objetos
    private U1 u1 = new U1();
    private List<A2> listaA2 = new ArrayList<>();
    private List<P2> listaP2 = new ArrayList<>();
    private List<E2> listaE2 = new ArrayList<>();
    private E3 e3 = new E3();
    private R01 r01 = new R01();
    private List<R02> listaR02 = new ArrayList<>();
    private List<R03> listaR03 = new ArrayList<>();
    private List<R04> listaR04 = new ArrayList<>();
    private List<R05> listaR05 = new ArrayList<>();
    private List<R06> listaR06 = new ArrayList<>();
    private List<R07> listaR07 = new ArrayList<>();

    /**
     * Construtor padrao passando os parametros necessarios.
     *
     * @param impressora o objeto da impressora que foi escolhida ou usada.
     * @param inicio a data do inicio do filtro.
     * @param fim a data do final do filtro.
     * @param listaProd
     * @param auto
     */
    public ComandoEmitirMovimentoECF(EcfImpressora impressora, Date inicio, Date fim, List<ProdProduto> listaProd, boolean auto) {
        this.log = Logger.getLogger(ComandoEmitirMovimentoECF.class);
        this.service = new CoreService();
        this.ecf = ECF.getInstancia();
        this.empresa = Caixa.getInstancia().getEmpresa();
        this.impressora = impressora;
        this.inicio = inicio;
        this.fim = fim;
        this.listaProd = listaProd;
        this.auto = auto;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            modeloECF = validarEAD(impressora, impressora.getEcfImpressoraModelo(), 20);
            // seta os valores nos objetos
            setU1();
            if (!auto) {
                setListaA2();
                setListaP2();
                setListaE2();
                setE3();
            }
            setRs();
            // gera o arquivo
            AnexoIV anexoIV = new AnexoIV(u1, listaA2, listaP2, listaE2, e3, r01, listaR02, listaR03, listaR04, listaR05, listaR06, listaR07);
            // formando o nome do arquivo
            String arquivo = impressora.getEcfImpressoraIdentificacao();
            if (impressora.getEcfImpressoraSerie().length() > 14) {
                arquivo += impressora.getEcfImpressoraSerie().substring(impressora.getEcfImpressoraSerie().length() - 14);
            } else {
                arquivo += impressora.getEcfImpressoraSerie();
            }
            arquivo += new SimpleDateFormat("ddMMyyyy").format(new Date());
            arquivo += ".txt";
            path = PAF.gerarRegistros(anexoIV, arquivo);
        } catch (Exception ex) {
            log.error("Erro ao gerar o arquivo de movimento do ECF.", ex);
            throw new OpenPdvException(ex);
        }
    }

    // cria o objeto modelo U1
    private void setU1() {
        // verifica as quantidades de registros no banco das tabelas monitoradas.
        long parcial, total = 0, registros = Long.valueOf(AUXILIAR.getProperty("paf.registros"));
        try {
            parcial = (Long) service.buscar(new EcfDocumento(), "ecfDocumentoId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfImpressora(), "ecfImpressoraId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfNota(), "ecfNotaId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfNotaEletronica(), "ecfNotaEletronicaId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfNotaProduto(), "ecfNotaProdutoId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfPagamento(), "ecfPagamentoId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfPagamentoParcela(), "ecfPagamentoParcelaId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfPagamentoTipo(), "ecfPagamentoTipoId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfPagamentoTotais(), "ecfPagamentoTotaisId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfTroca(), "ecfTrocaId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfTrocaProduto(), "ecfTrocaProdutoId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfVenda(), "ecfVendaId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfVendaProduto(), "ecfVendaProdutoId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfZ(), "ecfZId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new EcfZTotais(), "ecfZTotaisId", EBusca.CONTAGEM, null);
            total += parcial;
            parcial = (Long) service.buscar(new ProdProduto(), "prodProdutoId", EBusca.CONTAGEM, null);
            total += parcial;
        } catch (OpenPdvException ex) {
            // nada
        }
        u1.setCnpj(empresa.getSisEmpresaCnpj());
        u1.setIe(empresa.getSisEmpresaIe());
        u1.setIm(empresa.getSisEmpresaIm());
        if (total == registros) {
            u1.setRazao(Util.normaliza(empresa.getSisEmpresaRazao()));
        } else {
            String invalido = Util.formataTexto(Util.normaliza(empresa.getSisEmpresaRazao()), "?", 50, Util.EDirecao.DIREITA);
            u1.setRazao(invalido.replace(" ", "?"));
        }
    }

    // cria a lista de modelo A2
    private void setListaA2() throws OpenPdvException {
        // totaliza os pagamentos de hoje
        String shoje = Util.formataData(new Date(), "dd/MM/yyyy");
        Date dhoje = Util.formataData(shoje, "dd/MM/yyyy");
        new ComandoTotalizarPagamentos(dhoje).executar();
        // ajustando a data fim para documento, pois o mesmo usa datetime
        Calendar cal = Calendar.getInstance();
        cal.setTime(fim);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        fim = cal.getTime();
        // recupera todos os pagamentos do periodo
        FiltroData fd1 = new FiltroData("ecfPagamentoTotaisData", ECompara.MAIOR_IGUAL, inicio);
        FiltroData fd2 = new FiltroData("ecfPagamentoTotaisData", ECompara.MENOR, fim);
        FiltroGrupo gf = new FiltroGrupo(Filtro.E, fd1, fd2);
        List<EcfPagamentoTotais> totais = service.selecionar(new EcfPagamentoTotais(), 0, 0, gf);
        // gera a lista A2

        for (EcfPagamentoTotais total : totais) {
            // em caso de cartao adiciona o modo
            String doc = total.getEcfPagamentoTipo().getEcfPagamentoTipoDescricao();
            if (total.getEcfPagamentoTipo().getEcfPagamentoTipoCodigo().equals(Util.getConfig().getProperty("ecf.cartao"))) {
                doc = total.getEcfPagamentoTipo().isEcfPagamentoTipoDebito() ? "CARTAO DEBITO" : "CARTAO CREDITO";
            }

            A2 a2 = new A2();
            a2.setData(total.getEcfPagamentoTotaisData());
            a2.setMeioPagamento(validarEAD(total, doc, 25));
            a2.setTipoPagamento(total.getEcfPagamentoTotaisDocumento().equals("CUPOM FISCAL") ? "1" : "3");
            a2.setValor(total.getEcfPagamentoTotaisValor());
            listaA2.add(a2);
        }
    }

    // cria a lista de modelo P2
    private void setListaP2() {
        for (ProdProduto prod : listaProd) {
            P2 p2 = new P2();
            p2.setCnpj(empresa.getSisEmpresaCnpj());
            p2.setCodigo(prod.getProdProdutoBarra() == null ? prod.getProdProdutoId().toString() : prod.getProdProdutoBarra());
            p2.setDescricao(prod.getProdProdutoDescricao());
            p2.setUnidade(validarEAD(prod, prod.getProdEmbalagem().getProdEmbalagemNome(), 6));
            p2.setIat(prod.getProdProdutoIat());
            p2.setIppt(prod.getProdProdutoIppt());
            p2.setTributacao(prod.getProdProdutoTributacao());
            p2.setAliquota(prod.getProdProdutoIcms());
            p2.setValor(prod.getProdProdutoPreco());
            listaP2.add(p2);
        }
    }

    // cria a lista de modelo E2
    private void setListaE2() {
        for (ProdProduto prod : listaProd) {
            E2 e2 = new E2();
            e2.setCnpj(empresa.getSisEmpresaCnpj());
            e2.setCodigo(prod.getProdProdutoBarra() == null ? prod.getProdProdutoId().toString() : prod.getProdProdutoBarra());
            e2.setDescricao(Util.normaliza(prod.getProdProdutoDescricao()));
            e2.setUnidade(validarEAD(prod, prod.getProdEmbalagem().getProdEmbalagemNome(), 6));
            e2.setEstoque(prod.getProdProdutoEstoque());
            listaE2.add(e2);
        }
    }

    // cria o objeto modelo E3
    private void setE3() throws OpenPdvException {
        // achando a primeira venda do dia
        EcfVenda venda = new EcfVenda();
        venda.setOrdemDirecao(EDirecao.ASC);
        FiltroData dt1 = new FiltroData("ecfVendaData", ECompara.MAIOR_IGUAL, new Date());
        FiltroData dt2 = new FiltroData("ecfVendaData", ECompara.MENOR_IGUAL, new Date());
        FiltroGrupo gf = new FiltroGrupo(Filtro.E, dt1, dt2);
        List<EcfVenda> vendas = service.selecionar(venda, 0, 1, gf);
        Date data = new Date();
        if (!vendas.isEmpty()) {
            data = vendas.get(0).getEcfVendaData();
        }

        e3.setSerie(impressora.getEcfImpressoraSerie());
        e3.setMfAdicional(impressora.getEcfImpressoraMfadicional());
        e3.setTipoECF(impressora.getEcfImpressoraTipo());
        e3.setMarcaECF(impressora.getEcfImpressoraMarca());
        e3.setModeloECF(modeloECF);
        e3.setData(data);
    }

    // cria o objeto modelo R01
    private void setRs() throws Exception {
        // r01
        r01.setSerie(impressora.getEcfImpressoraSerie());
        r01.setMfAdicional(impressora.getEcfImpressoraMfadicional());
        r01.setModeloECF(modeloECF);
        r01.setUsuario(1);
        r01.setTipoECF(impressora.getEcfImpressoraTipo());
        r01.setMarcaECF(impressora.getEcfImpressoraMarca());
        String[] resp = ecf.enviar(EComando.ECF_NumVersao);
        if (IECF.OK.equals(resp[0])) {
            r01.setVersaoSB(resp[1]);
        }
        resp = ecf.enviar(EComando.ECF_DataHoraSB);
        if (IECF.OK.equals(resp[0])) {
            r01.setDataSB(new SimpleDateFormat("dd/MM/yy HH:mm:ss").parse(resp[1]));
        }
        r01.setNumeroECF(impressora.getEcfImpressoraCaixa());
        r01.setEmpresaCNPJ(PAF.AUXILIAR.getProperty("cli.cnpj"));
        r01.setEmpresaIE(PAF.AUXILIAR.getProperty("cli.ie"));
        r01.setShCNPJ(PAF.AUXILIAR.getProperty("sh.cnpj"));
        r01.setShIE(PAF.AUXILIAR.getProperty("sh.ie"));
        r01.setShIM(Integer.valueOf(PAF.AUXILIAR.getProperty("sh.im")));
        r01.setShRazao(PAF.AUXILIAR.getProperty("sh.razao"));
        r01.setPafNome(PAF.AUXILIAR.getProperty("paf.nome"));
        r01.setPafVersao(PAF.AUXILIAR.getProperty("paf.versao"));
        StringBuilder arquivoMD5 = new StringBuilder(System.getProperty("user.dir"));
        arquivoMD5.append(System.getProperty("file.separator"));
        arquivoMD5.append("arquivos");
        arquivoMD5.append(System.getProperty("file.separator"));
        arquivoMD5.append("arquivoMD5.txt");
        r01.setPafMD5(Util.gerarMD5(arquivoMD5.toString()));
        r01.setPafER(PAF.AUXILIAR.getProperty("paf.er"));
        r01.setInicio(inicio);
        r01.setFim(fim);

        // r02
        FiltroData dt1 = new FiltroData("ecfZMovimento", ECompara.MAIOR_IGUAL, inicio);
        FiltroData dt2 = new FiltroData("ecfZMovimento", ECompara.MENOR_IGUAL, fim);
        FiltroObjeto fo1 = new FiltroObjeto("ecfImpressora", ECompara.IGUAL, impressora);
        FiltroGrupo gf = new FiltroGrupo(Filtro.E, dt1, dt2, fo1);
        List<EcfZ> ecfZs = service.selecionar(new EcfZ(), 0, 0, gf);

        for (EcfZ ecfz : ecfZs) {
            R02 r02 = new R02();
            r02.setSerie(impressora.getEcfImpressoraSerie());
            r02.setMfAdicional(impressora.getEcfImpressoraMfadicional());
            r02.setModeloECF(validarEAD(ecfz, modeloECF, 20));
            r02.setUsuario(ecfz.getEcfZUsuario());
            r02.setCrz(ecfz.getEcfZCrz());
            r02.setCoo(ecfz.getEcfZCooFin());
            r02.setCro(ecfz.getEcfZCro());
            r02.setMovimento(ecfz.getEcfZMovimento());
            r02.setEmissao(ecfz.getEcfZEmissao());
            r02.setBruto(ecfz.getEcfZBruto());
            r02.setIssqn(ecfz.getEcfZIssqn() ? 'S' : 'N');
            listaR02.add(r02);
            // r03
            Set<String> totalizadores = new HashSet<>();
            for (EcfZTotais total : ecfz.getEcfZTotais()) {
                R03 r03 = new R03();
                r03.setSerie(impressora.getEcfImpressoraSerie());
                r03.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                r03.setModeloECF(validarEAD(total, modeloECF, 20));
                r03.setUsuario(1);
                r03.setCrz(ecfz.getEcfZCrz());
                r03.setTotalizador(total.getEcfZTotaisCodigo());
                r03.setValor(total.getEcfZTotaisValor());
                listaR03.add(r03);
                // para uso nos itens da venda
                totalizadores.add(total.getEcfZTotaisCodigo());
            }
            // r04
            for (EcfVenda venda : ecfz.getEcfVendas()) {
                R04 r04 = new R04();
                r04.setSerie(impressora.getEcfImpressoraSerie());
                r04.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                r04.setModeloECF(validarEAD(venda, modeloECF, 20));
                r04.setUsuario(1);
                r04.setDocumento(venda.getEcfVendaCcf());
                r04.setCoo(venda.getEcfVendaCoo());
                r04.setData(venda.getEcfVendaData());
                r04.setBruto(venda.getEcfVendaFechada() ? venda.getEcfVendaBruto() : 0.00);
                r04.setDesconto(venda.getEcfVendaDesconto());
                r04.setTipoDesconto('V');
                r04.setAcrescimo(venda.getEcfVendaAcrescimo());
                r04.setTipoAcrescimo('V');
                r04.setLiquido(venda.getEcfVendaFechada() ? venda.getEcfVendaLiquido() : 0.00);
                r04.setCancelado(venda.getEcfVendaCancelada() ? 'S' : 'N');
                r04.setCanceladoAcrescimo(venda.getEcfVendaAcrescimo());
                r04.setOrdemAcresDesc('A');
                if (venda.getSisCliente() != null) {
                    r04.setClienteNome(venda.getSisCliente().getSisClienteNome());
                    r04.setClienteCPF(venda.getSisCliente().getSisClienteDoc().replaceAll("\\D", ""));
                }
                listaR04.add(r04);
                // r05
                for (EcfVendaProduto vp : venda.getEcfVendaProdutos()) {
                    R05 r05 = new R05();
                    r05.setSerie(impressora.getEcfImpressoraSerie());
                    r05.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                    r05.setModeloECF(validarEAD(vp, modeloECF, 20));
                    r05.setUsuario(1);
                    r05.setDocumento(venda.getEcfVendaCcf());
                    r05.setCoo(venda.getEcfVendaCoo());
                    r05.setItem(vp.getEcfVendaProdutoOrdem());
                    r05.setCodigo(vp.getEcfVendaProdutoCodigo());
                    r05.setDescricao(vp.getProdProduto().getProdProdutoDescricao());
                    r05.setQuantidade(vp.getEcfVendaProdutoQuantidade());
                    r05.setUnidade(vp.getProdEmbalagem().getProdEmbalagemNome());
                    r05.setBruto(vp.getEcfVendaProdutoBruto());
                    r05.setDesconto(vp.getEcfVendaProdutoDesconto());
                    r05.setAcrescimo(vp.getEcfVendaProdutoAcrescimo());
                    r05.setTotal(vp.getEcfVendaProdutoTotal());
                    if (vp.getEcfVendaProdutoTributacao() == 'T' || vp.getEcfVendaProdutoTributacao() == 'S') {
                        String sufixo = vp.getEcfVendaProdutoTributacao() + Util.formataNumero(vp.getEcfVendaProdutoIcms(), 2, 2, false).replace(",", "");
                        for (String tot : totalizadores) {
                            if (tot.endsWith(sufixo)) {
                                r05.setTotalizador(tot);
                                break;
                            }
                        }
                    } else {
                        r05.setTotalizador(vp.getEcfVendaProdutoTributacao() + "1");
                    }
                    if (vp.getEcfVendaProdutoCancelado()) {
                        r05.setCancelado('S');
                        r05.setCanceladoQtd(vp.getEcfVendaProdutoQuantidade());
                        r05.setCanceladoValor(vp.getEcfVendaProdutoTotal());
                    } else {
                        r05.setCancelado('N');
                        r05.setCanceladoQtd(0.00);
                        r05.setCanceladoValor(0.00);
                    }
                    r05.setCanceladoAcrescimo(0.00);
                    r05.setIat(vp.getProdProduto().getProdProdutoIat());
                    r05.setIppt(vp.getProdProduto().getProdProdutoIppt());
                    r05.setDecimalQuantidade(2);
                    r05.setDecimalValor(2);
                    listaR05.add(r05);
                }
                // r07
                for (EcfPagamento vp : venda.getEcfPagamentos()) {
                    R07 r07 = new R07();
                    r07.setSerie(impressora.getEcfImpressoraSerie());
                    r07.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                    r07.setModeloECF(validarEAD(vp, modeloECF, 20));
                    r07.setUsuario(1);
                    r07.setCoo(venda.getEcfVendaCoo());
                    r07.setCcf(venda.getEcfVendaCcf());
                    r07.setGnf(vp.getEcfPagamentoGnf());
                    r07.setMeioPagamento(vp.getEcfPagamentoTipo().getEcfPagamentoTipoDescricao());
                    r07.setValor(vp.getEcfPagamentoValor());
                    r07.setData(venda.getEcfVendaData());
                    r07.setEstorno(vp.getEcfPagamentoEstorno());
                    r07.setValorEstorno(vp.getEcfPagamentoEstornoValor());
                    listaR07.add(r07);
                }
            }
            // r06
            for (EcfDocumento doc : ecfz.getEcfDocumentos()) {
                R06 r06 = new R06();
                r06.setSerie(impressora.getEcfImpressoraSerie());
                r06.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                r06.setModeloECF(validarEAD(doc, modeloECF, 20));
                r06.setUsuario(1);
                r06.setCoo(doc.getEcfDocumentoCoo());
                r06.setGnf(doc.getEcfDocumentoGnf());
                r06.setGrg(doc.getEcfDocumentoGrg());
                r06.setCdc(doc.getEcfDocumentoCdc());
                r06.setTipo(doc.getEcfDocumentoTipo());
                r06.setData(doc.getEcfDocumentoData());
                listaR06.add(r06);
            }
        }
    }

    // valida se o ead no objeto e o mesmo que o gerado com seus dados atuais.
    private String validarEAD(Dados dado, String valor, int tamanho) {
        if (!Util.encriptar(dado).equals(dado.getEad())) {
            String invalido = Util.formataTexto(valor, "?", tamanho, Util.EDirecao.DIREITA);
            valor = (invalido.replace(" ", "?"));
        }
        return valor;
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public EcfImpressora getImpressora() {
        return impressora;
    }

    public void setImpressora(EcfImpressora impressora) {
        this.impressora = impressora;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public CoreService getService() {
        return service;
    }

    public void setService(CoreService service) {
        this.service = service;
    }
}
