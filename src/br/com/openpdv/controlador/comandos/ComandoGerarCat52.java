package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.EJuncao;
import br.com.openpdv.modelo.core.filtro.FiltroData;
import br.com.openpdv.modelo.core.filtro.FiltroObjeto;
import br.com.openpdv.modelo.core.filtro.GrupoFiltro;
import br.com.openpdv.modelo.core.filtro.IFiltro;
import br.com.openpdv.modelo.ecf.EcfDocumento;
import br.com.openpdv.modelo.ecf.EcfImpressora;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.ecf.EcfVendaProduto;
import br.com.openpdv.modelo.ecf.EcfZ;
import br.com.openpdv.modelo.ecf.EcfZTotais;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import br.com.phdss.controlador.PAF;
import br.com.phdss.modelo.cat52.Cat52;
import br.com.phdss.modelo.cat52.E00;
import br.com.phdss.modelo.cat52.E01;
import br.com.phdss.modelo.cat52.E02;
import br.com.phdss.modelo.cat52.E12;
import br.com.phdss.modelo.cat52.E13;
import br.com.phdss.modelo.cat52.E14;
import br.com.phdss.modelo.cat52.E15;
import br.com.phdss.modelo.cat52.E16;
import br.com.phdss.modelo.cat52.E21;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Classe responsavel por executar o arquivo cat52 do ecf.
 *
 * @author Pedro H. Lira
 */
public class ComandoGerarCat52 implements IComando {

    private Logger log;
    private SisEmpresa empresa;
    private EcfImpressora impressora;
    private EcfZ ecfZ;
    private String path;

    /**
     * Construtor padrao passando os parametros necessarios.
     *
     * @param empresa o obejto da empresa que esta sendo usada.
     * @param impressora o objeto da impressora que esta sendo usada.
     * @param data a data do inicio do filtro.
     */
    public ComandoGerarCat52(SisEmpresa empresa, EcfImpressora impressora, Date data) {
        this.log = Logger.getLogger(ComandoGerarCat52.class);
        this.empresa = empresa;
        this.impressora = impressora;

        // seleciona a Z
        CoreService service = new CoreService();
        FiltroObjeto fo = new FiltroObjeto("ecfImpressora", ECompara.IGUAL, impressora);
        FiltroData dt = new FiltroData("ecfZMovimento", ECompara.IGUAL, data);
        GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fo, dt});
        try {
            this.ecfZ = (EcfZ) service.selecionar(new EcfZ(), gf);
        } catch (OpenPdvException ex) {
            this.ecfZ = null;
        }
    }

    /**
     * Construtor padrao passando os parametros necessarios.
     *
     * @param empresa o obejto da empresa que esta sendo usada.
     * @param impressora o objeto da impressora que esta sendo usada.
     * @param ecfZ o objeto da leituraZ.
     */
    public ComandoGerarCat52(SisEmpresa empresa, EcfImpressora impressora, EcfZ ecfZ) {
        this.log = Logger.getLogger(ComandoGerarCat52.class);
        this.empresa = empresa;
        this.impressora = impressora;
        this.ecfZ = ecfZ;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            if (ecfZ != null) {
                // cria os objetos
                E00 e00 = new E00();
                E01 e01 = new E01();
                E02 e02 = new E02();
                E12 e12 = new E12();
                List<E13> listaE13 = new ArrayList<>();
                List<E14> listaE14 = new ArrayList<>();
                List<E15> listaE15 = new ArrayList<>();
                List<E16> listaE16 = new ArrayList<>();
                List<E21> listaE21 = new ArrayList<>();

                // e00
                e00.setSerie(impressora.getEcfImpressoraSerie());
                e00.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                e00.setModelo(impressora.getEcfImpressoraModelo());
                e00.setUsuario(1);
                e00.setTipoEcf(impressora.getEcfImpressoraTipo());
                e00.setMarcaEcf(impressora.getEcfImpressoraMarca());
                e00.setCnpj_cpf(PAF.AUXILIAR.getProperty("sh.cnpj").replaceAll("\\D", ""));
                e00.setIe(PAF.AUXILIAR.getProperty("sh.ie").replaceAll("\\D", ""));
                e00.setIm(PAF.AUXILIAR.getProperty("sh.im").replaceAll("\\D", ""));
                e00.setRazao(PAF.AUXILIAR.getProperty("sh.razao"));
                e00.setNomeAplicativo(PAF.AUXILIAR.getProperty("paf.nome"));
                e00.setVersaoAplicativo(PAF.AUXILIAR.getProperty("paf.versao"));

                // e01
                e01.setSerie(impressora.getEcfImpressoraSerie());
                e01.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                e01.setModelo(impressora.getEcfImpressoraModelo());
                e01.setUsuario(1);
                String[] resp = ECF.enviar(EComandoECF.ECF_NumVersao);
                if (ECF.OK.equals(resp[0])) {
                    e01.setVersaoSB(resp[1]);
                } else {
                    e01.setVersaoSB("01.00.00");
                }
                resp = ECF.enviar(EComandoECF.ECF_DataHoraSB);
                Date dataSB;
                if (ECF.OK.equals(resp[0])) {
                    dataSB = new SimpleDateFormat("dd/MM/yy HH:mm:ss").parse(resp[1]);
                } else {
                    dataSB = new Date();
                }
                e01.setDataSB(dataSB);
                e01.setSequencial(impressora.getEcfImpressoraCaixa());
                e01.setCnpj(empresa.getSisEmpresaCnpj().replaceAll("\\D", ""));
                e01.setComando("APL");
                e01.setCrzIni(ecfZ.getEcfZCrz());
                e01.setCrzFim(ecfZ.getEcfZCrz());
                e01.setDataIni(ecfZ.getEcfZMovimento());
                e01.setDataFim(ecfZ.getEcfZMovimento());
                String id = impressora.getEcfImpressoraIdentificacao();
                e01.setBiblioteca(id.substring(0, 2) + "." + id.substring(2, 4) + "." + id.substring(4));
                e01.setAtoCotepe("PC5207 01.00.00");

                // e02
                e02.setSerie(impressora.getEcfImpressoraSerie());
                e02.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                e02.setModelo(impressora.getEcfImpressoraModelo());
                e02.setUsuario(1);
                e02.setCnpj(empresa.getSisEmpresaCnpj().replaceAll("\\D", ""));
                e02.setIe(empresa.getSisEmpresaIe().replaceAll("\\D", ""));
                e02.setRazao(empresa.getSisEmpresaRazao());
                e02.setEndereco(empresa.getSisEmpresaLogradouro());
                e02.setCadastro(dataSB);
                e02.setCro(Integer.valueOf(PAF.AUXILIAR.getProperty("ecf.cro")));
                e02.setGt(Double.valueOf(PAF.AUXILIAR.getProperty("ecf.gt").replace(",", ".")));

                // e12
                e12.setSerie(impressora.getEcfImpressoraSerie());
                e12.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                e12.setModelo(impressora.getEcfImpressoraModelo());
                e12.setUsuario(1);
                e12.setCrz(ecfZ.getEcfZCrz());
                e12.setCoo(ecfZ.getEcfZCooFin());
                e12.setCro(ecfZ.getEcfZCro());
                e12.setMovimento(ecfZ.getEcfZMovimento());
                e12.setEmissao(ecfZ.getEcfZEmissao());
                e12.setBruto(ecfZ.getEcfZBruto());
                e12.setIssqn(ecfZ.getEcfZIssqn() ? 'S' : 'N');

                // e13
                Set<String> totalizadores = new HashSet<>();
                for (EcfZTotais total : ecfZ.getEcfZTotais()) {
                    E13 e13 = new E13();
                    e13.setSerie(impressora.getEcfImpressoraSerie());
                    e13.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                    e13.setModelo(impressora.getEcfImpressoraModelo());
                    e13.setUsuario(1);
                    e13.setCrz(ecfZ.getEcfZCrz());
                    e13.setTotalizador(total.getEcfZTotaisCodigo());
                    e13.setValor(total.getEcfZTotaisValor());
                    listaE13.add(e13);
                    // para uso nos itens da venda
                    totalizadores.add(total.getEcfZTotaisCodigo());
                }

                // e14
                for (EcfVenda venda : ecfZ.getEcfVendas()) {
                    E14 e14 = new E14();
                    e14.setSerie(impressora.getEcfImpressoraSerie());
                    e14.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                    e14.setModelo(impressora.getEcfImpressoraModelo());
                    e14.setUsuario(1);
                    e14.setCcf(venda.getEcfVendaCcf());
                    e14.setCoo(venda.getEcfVendaCoo());
                    e14.setData(venda.getEcfVendaData());
                    e14.setBruto(venda.getEcfVendaFechada() ? venda.getEcfVendaBruto() : 0.00);
                    e14.setDesconto(venda.getEcfVendaDesconto());
                    e14.setDescontoTipo('V');
                    e14.setAcrescimo(venda.getEcfVendaAcrescimo());
                    e14.setAcrescimoTipo('V');
                    e14.setLiquido(venda.getEcfVendaFechada() ? venda.getEcfVendaLiquido() : 0.00);
                    e14.setCancelado(venda.getEcfVendaCancelada() ? 'S' : 'N');
                    e14.setCanceladoAcrescimo(venda.getEcfVendaAcrescimo());
                    e14.setOrdemDA('A');
                    if (venda.getSisCliente() != null) {
                        e14.setCliente(venda.getSisCliente().getSisClienteNome());
                        e14.setCnpj_cpf(venda.getSisCliente().getSisClienteDoc().replaceAll("\\D", ""));
                    }
                    listaE14.add(e14);

                    // e15
                    for (EcfVendaProduto vp : venda.getEcfVendaProdutos()) {
                        E15 e15 = new E15();
                        e15.setSerie(impressora.getEcfImpressoraSerie());
                        e15.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                        e15.setModelo(impressora.getEcfImpressoraModelo());
                        e15.setUsuario(1);
                        e15.setCoo(venda.getEcfVendaCoo());
                        e15.setCcf(venda.getEcfVendaCcf());
                        e15.setItem(vp.getEcfVendaProdutoOrdem());
                        e15.setCodigo(vp.getEcfVendaProdutoCodigo());
                        e15.setDescricao(vp.getProdProduto().getProdProdutoDescricao());
                        e15.setQtd(vp.getEcfVendaProdutoQuantidade());
                        e15.setUnd(vp.getProdEmbalagem().getProdEmbalagemNome());
                        e15.setBruto(vp.getEcfVendaProdutoBruto());
                        e15.setDesconto(vp.getEcfVendaProdutoDesconto());
                        e15.setAcrescimo(vp.getEcfVendaProdutoAcrescimo());
                        e15.setLiquido(vp.getEcfVendaProdutoTotal());
                        if (vp.getEcfVendaProdutoTributacao() == 'T' || vp.getEcfVendaProdutoTributacao() == 'S') {
                            String sufixo = vp.getEcfVendaProdutoTributacao() + Util.formataNumero(vp.getEcfVendaProdutoIcms(), 2, 2, false).replace(",", "");
                            for (String tot : totalizadores) {
                                if (tot.endsWith(sufixo)) {
                                    e15.setTotalizador(tot);
                                    break;
                                }
                            }
                        } else {
                            e15.setTotalizador(vp.getEcfVendaProdutoTributacao() + "1");
                        }
                        if (vp.getEcfVendaProdutoCancelado()) {
                            e15.setCancelado('S');
                            e15.setQtdCancelado(vp.getEcfVendaProdutoQuantidade());
                            e15.setValorCancelado(vp.getEcfVendaProdutoTotal());
                        } else {
                            e15.setCancelado('N');
                            e15.setQtdCancelado(0.00);
                            e15.setValorCancelado(0.00);
                        }
                        e15.setAcrescimoCancelado(0.00);
                        e15.setIat(vp.getProdProduto().getProdProdutoIat());
                        e15.setQtdDecimais(2);
                        e15.setValorDecimais(2);
                        listaE15.add(e15);
                    }

                    // e17
                    for (EcfPagamento pag : venda.getEcfPagamentos()) {
                        E21 e21 = new E21();
                        e21.setSerie(impressora.getEcfImpressoraSerie());
                        e21.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                        e21.setModelo(impressora.getEcfImpressoraModelo());
                        e21.setUsuario(1);
                        e21.setCoo(venda.getEcfVendaCoo());
                        e21.setCcf(venda.getEcfVendaCcf());
                        e21.setGnf(pag.getEcfPagamentoGnf());
                        e21.setDescricao(pag.getEcfPagamentoTipo().getEcfPagamentoTipoDescricao());
                        e21.setValor(pag.getEcfPagamentoValor());
                        e21.setEstorno('N');
                        e21.setEstornoValor(0.00);
                        listaE21.add(e21);

                        // caso tenha um estorno gera outro registro
                        if (pag.getEcfPagamentoEstorno() == 'S') {
                            E21 e21a = new E21();
                            e21a.setSerie(impressora.getEcfImpressoraSerie());
                            e21a.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                            e21a.setModelo(impressora.getEcfImpressoraModelo());
                            e21a.setUsuario(1);
                            e21a.setCoo(venda.getEcfVendaCoo());
                            e21a.setCcf(venda.getEcfVendaCcf());
                            e21a.setGnf(pag.getEcfPagamentoGnf());
                            e21a.setDescricao(pag.getEcfPagamentoTipo().getEcfPagamentoTipoDescricao());
                            e21a.setValor(pag.getEcfPagamentoValor());
                            e21a.setEstorno('S');
                            e21a.setEstornoValor(pag.getEcfPagamentoEstornoValor());
                            listaE21.add(e21a);
                        }
                    }
                }

                // e16
                for (EcfDocumento doc : ecfZ.getEcfDocumentos()) {
                    E16 e16 = new E16();
                    e16.setSerie(impressora.getEcfImpressoraSerie());
                    e16.setMfAdicional(impressora.getEcfImpressoraMfadicional());
                    e16.setModelo(impressora.getEcfImpressoraModelo());
                    e16.setUsuario(1);
                    e16.setCoo(doc.getEcfDocumentoCoo());
                    e16.setGnf(doc.getEcfDocumentoGnf());
                    e16.setGrg(doc.getEcfDocumentoGrg());
                    e16.setCdc(doc.getEcfDocumentoCdc());
                    e16.setCrz(ecfZ.getEcfZCrz());
                    e16.setDenominacao(doc.getEcfDocumentoTipo());
                    e16.setData(doc.getEcfDocumentoData());
                    listaE16.add(e16);
                }

                // seta os dados
                Cat52 cat52 = new Cat52();
                cat52.setE00(e00);
                cat52.setE01(e01);
                cat52.setE02(e02);
                cat52.setE12(e12);
                cat52.setListaE13(listaE13);
                cat52.setListaE14(listaE14);
                cat52.setListaE15(listaE15);
                cat52.setListaE16(listaE16);
                cat52.setListaE21(listaE21);

                // gera o arquivo
                path = PAF.gerarArquivoCat52(cat52);
            }
        } catch (Exception ex) {
            log.error("Erro ao gerar o arquivo cat52 do ECF.", ex);
            throw new OpenPdvException(ex);
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public SisEmpresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(SisEmpresa empresa) {
        this.empresa = empresa;
    }

    public EcfImpressora getImpressora() {
        return impressora;
    }

    public void setImpressora(EcfImpressora impressora) {
        this.impressora = impressora;
    }

    public EcfZ getEcfZ() {
        return ecfZ;
    }

    public void setEcfZ(EcfZ ecfZ) {
        this.ecfZ = ecfZ;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
