package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.*;
import br.com.phdss.ECF;
import br.com.phdss.EComando;
import br.com.phdss.IECF;
import br.com.phdss.controlador.PAF;
import br.com.phdss.modelo.anexo.vi.*;
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
    private EcfImpressora impressora;
    private Date inicio;
    private Date fim;
    private String path;
    private IECF ecf;

    /**
     * Construtor padrao passando os parametros necessarios.
     *
     * @param impressora o objeto da impressora que foi escolhida ou usada.
     * @param inicio a data do inicio do filtro.
     * @param fim a data do final do filtro.
     */
    public ComandoEmitirMovimentoECF(EcfImpressora impressora, Date inicio, Date fim) {
        this.log = Logger.getLogger(ComandoEmitirMovimentoECF.class);
        this.impressora = impressora;
        this.inicio = inicio;
        this.fim = fim;
        this.service = new CoreService();
        this.ecf = ECF.getInstancia();
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            R01 r01 = new R01();
            List<R02> listaR02 = new ArrayList<>();
            List<R03> listaR03 = new ArrayList<>();
            List<R04> listaR04 = new ArrayList<>();
            List<R05> listaR05 = new ArrayList<>();
            List<R06> listaR06 = new ArrayList<>();
            List<R07> listaR07 = new ArrayList<>();

            // r01
            r01.setSerie(impressora.getEcfImpressoraSerie());
            r01.setMfAdicional(impressora.getEcfImpressoraMfadicional());
            r01.setModeloECF(impressora.getEcfImpressoraModelo());
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
            GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{dt1, dt2, fo1});
            List<EcfZ> ecfZs = service.selecionar(new EcfZ(), 0, 0, gf);
            for (EcfZ ecfz : ecfZs) {
                R02 r02 = new R02();
                r02.setSerie(impressora.getEcfImpressoraSerie());
                r02.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                r02.setModeloECF(impressora.getEcfImpressoraModelo());
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
                    r03.setModeloECF(impressora.getEcfImpressoraModelo());
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
                    r04.setModeloECF(impressora.getEcfImpressoraModelo());
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
                        r05.setModeloECF(impressora.getEcfImpressoraModelo());
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
                        r07.setModeloECF(impressora.getEcfImpressoraModelo());
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
                    r06.setModeloECF(impressora.getEcfImpressoraModelo());
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

            // seta os dados
            AnexoVI anexoVI = new AnexoVI();
            anexoVI.setR01(r01);
            anexoVI.setListaR02(listaR02);
            anexoVI.setListaR03(listaR03);
            anexoVI.setListaR04(listaR04);
            anexoVI.setListaR05(listaR05);
            anexoVI.setListaR06(listaR06);
            anexoVI.setListaR07(listaR07);

            // formando o nome do arquivo
            String arquivo = impressora.getEcfImpressoraIdentificacao();
            if (impressora.getEcfImpressoraSerie().length() > 14) {
                arquivo += impressora.getEcfImpressoraSerie().substring(impressora.getEcfImpressoraSerie().length() - 14);
            } else {
                arquivo += impressora.getEcfImpressoraSerie();
            }
            arquivo += new SimpleDateFormat("ddMMyyyy").format(new Date());
            arquivo += ".txt";
            path = PAF.gerarMovimentosECF(anexoVI, arquivo);
        } catch (Exception ex) {
            log.error("Erro ao gerar o arquivo de movimento do ECF.", ex);
            throw new OpenPdvException(ex);
        }
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
