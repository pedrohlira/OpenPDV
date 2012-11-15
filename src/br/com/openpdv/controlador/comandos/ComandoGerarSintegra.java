package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.NFe;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.EDirecao;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.*;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.openpdv.nfe.TNFe;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.controlador.PAF;
import br.com.phdss.modelo.sintegra.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * Classe que realiza a acao de gerar o Sintegra.
 *
 * @author Pedro H. Lira
 */
public class ComandoGerarSintegra implements IComando {

    private String path;
    private Date inicio;
    private Date fim;
    private String[] opcoes;
    private CoreService service;
    private SisEmpresa emp;
    private List<EcfNotaEletronica> nfes;
    private List<EcfNota> notas;
    private List<EcfZ> zs;
    private List<ProdProduto> estoque;

    public ComandoGerarSintegra(Date inicio, Date fim, String[] opcoes) {
        this.inicio = inicio;
        this.fim = fim;
        this.opcoes = opcoes;
        this.service = new CoreService();
        this.emp = Caixa.getInstancia().getEmpresa();
    }

    @Override
    public void executar() throws OpenPdvException {
        // ajustando a data fim para documento, pois o mesmo usa datetime
        Calendar cal = Calendar.getInstance();
        cal.setTime(fim);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        fim = cal.getTime();

        // recupera as nfes emitidas no periodo
        FiltroData fd1 = new FiltroData("ecfNotaEletronicaData", ECompara.MAIOR_IGUAL, inicio);
        FiltroData fd2 = new FiltroData("ecfNotaEletronicaData", ECompara.MENOR, fim);
        GrupoFiltro gp1 = new GrupoFiltro(EJuncao.E, new IFiltro[]{fd1, fd2});
        EcfNotaEletronica ene = new EcfNotaEletronica();
        ene.setOrdemDirecao(EDirecao.ASC);
        nfes = service.selecionar(ene, 0, 0, gp1);

        // recupera as notas emitidas no periodo
        FiltroData fd3 = new FiltroData("ecfNotaData", ECompara.MAIOR_IGUAL, inicio);
        FiltroData fd4 = new FiltroData("ecfNotaData", ECompara.MENOR, fim);
        GrupoFiltro gp2 = new GrupoFiltro(EJuncao.E, new IFiltro[]{fd3, fd4});
        EcfNota en = new EcfNota();
        en.setOrdemDirecao(EDirecao.ASC);
        notas = service.selecionar(en, 0, 0, gp2);

        // recupera as leituras Z no periodo
        FiltroData fd7 = new FiltroData("ecfZMovimento", ECompara.MAIOR_IGUAL, inicio);
        FiltroData fd8 = new FiltroData("ecfZMovimento", ECompara.MENOR, fim);
        GrupoFiltro gf4 = new GrupoFiltro(EJuncao.E, new IFiltro[]{fd7, fd8});
        EcfZ ez = new EcfZ();
        ez.setOrdemDirecao(EDirecao.ASC);
        zs = service.selecionar(ez, 0, 0, gf4);

        // recupera os produtos com estoque maior que zero
        FiltroNumero fn = new FiltroNumero("prodProdutoEstoque", ECompara.DIFERENTE, 0);
        ProdProduto pp = new ProdProduto();
        pp.setCampoOrdem("prodProdutoId");
        estoque = service.selecionar(pp, 0, 0, fn);

        Sintegra sintegra = new Sintegra();
        // Dados 10
        sintegra.setDados10(getDados10());
        // Dados 11
        sintegra.setDados11(getDados11());
        // Dados 50
        sintegra.setDados50(getDados50());
        // Dados 54
        sintegra.setDados54(getDados54());
        // Dados 60M
        sintegra.setDados60M(getDados60M());
        // Dados 60R
        sintegra.setDados60R(getDados60R());
        // Dados 61
        sintegra.setDados61(getDados61());
        // Dados 61R
        sintegra.setDados61R(getDados61R());
        // Dados 74
        sintegra.setDados74(getDados74());
        // Dados 75
        sintegra.setDados75(getDados75());
        // Dados 90 e gerado internamento pelo PAF

        try {
            // gerar o arquivo
            path = PAF.gerarVendasPeriodo(sintegra);
        } catch (Exception ex) {
            throw new OpenPdvException(ex);
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private Dados10 getDados10() {
        Dados10 d10 = new Dados10();
        d10.setCnpj(emp.getSisEmpresaCnpj());
        d10.setIe(emp.getSisEmpresaIe());
        d10.setRazao(emp.getSisEmpresaRazao());
        d10.setMunicipio(emp.getSisMunicipio().getSisMunicipioDescricao());
        d10.setUf(emp.getSisMunicipio().getSisEstado().getSisEstadoSigla());
        d10.setFax(emp.getSisEmpresaFone());
        d10.setInicio(inicio);
        d10.setFim(fim);
        d10.setConvenio(opcoes[0]);
        d10.setNatureza(opcoes[1]);
        d10.setFinalidade(opcoes[2]);

        Util.normaliza(d10);
        return d10;
    }

    private Dados11 getDados11() {
        Dados11 d11 = new Dados11();
        d11.setLogradouro(emp.getSisEmpresaLogradouro());
        d11.setNumero(emp.getSisEmpresaNumero());
        d11.setComplemento(emp.getSisEmpresaComplemento());
        d11.setBairro(emp.getSisEmpresaBairro());
        d11.setCep(emp.getSisEmpresaCep());
        d11.setResponsavel(emp.getSisEmpresaResponsavel());
        d11.setTelefone(emp.getSisEmpresaFone());

        Util.normaliza(d11);
        return d11;
    }

    private List<Dados50> getDados50() {
        List<Dados50> ld50 = new ArrayList<>();
        for (EcfNotaEletronica nfe : nfes) {
            try {
                Dados50 d50 = new Dados50();
                if (nfe.getEcfNotaEletronicaStatus().equals(ENotaStatus.AUTORIZADO.toString()) || nfe.getEcfNotaEletronicaStatus().equals(ENotaStatus.CANCELADO.toString())) {
                    // pega a NFe
                    String xml = nfe.getEcfNotaEletronicaXml();
                    int I = xml.indexOf("<infNFe");
                    int F = xml.indexOf("</NFe>") + 6;
                    String texto = "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">" + xml.substring(I, F);

                    TNFe tnfe = NFe.xmlToObj(texto, TNFe.class);
                    TNFe.InfNFe.Total.ICMSTot icms = tnfe.getInfNFe().getTotal().getICMSTot();

                    d50.setCnpj(nfe.getSisCliente().getSisClienteDoc());
                    d50.setIe("ISENTO");
                    d50.setData(nfe.getEcfNotaEletronicaData());
                    d50.setUf(emp.getSisMunicipio().getSisEstado().getSisEstadoSigla());
                    d50.setModelo(55);
                    d50.setSerie(Util.getConfig().get("nfe.serie"));
                    d50.setNumero(nfe.getEcfNotaEletronicaNumero());
                    d50.setCfop(5201);
                    d50.setEmitente("P");
                    d50.setValor(nfe.getEcfNotaEletronicaValor());
                    d50.setBase_icms(Double.valueOf(icms.getVBC()));
                    d50.setValor_icms(Double.valueOf(icms.getVICMS()));
                    double aliq = d50.getValor_icms() > 0 ? 17 : 0;
                    double isento = (d50.getBase_icms() * aliq / 100) - d50.getValor_icms();
                    d50.setValor_isento(isento);
                    d50.setOutras(Double.valueOf(icms.getVOutro()));
                    d50.setAliq_icms(aliq);
                    d50.setSituacao(nfe.getEcfNotaEletronicaStatus().equals(ENotaStatus.AUTORIZADO.toString()) ? "N" : "S");
                } else {
                    d50.setData(nfe.getEcfNotaEletronicaData());
                    d50.setUf(emp.getSisMunicipio().getSisEstado().getSisEstadoSigla());
                    d50.setModelo(55);
                    d50.setSerie(Util.getConfig().get("nfe.serie"));
                    d50.setNumero(nfe.getEcfNotaEletronicaNumero());
                    d50.setEmitente("P");
                    d50.setSituacao("4");
                }

                Util.normaliza(d50);
                ld50.add(d50);
            } catch (Exception ex) {
                continue;
            }
        }

        return ld50;
    }

    private List<Dados54> getDados54() {
        List<Dados54> ld54 = new ArrayList<>();
        for (EcfNotaEletronica nfe : nfes) {
            try {
                if (nfe.getEcfNotaEletronicaStatus().equals(ENotaStatus.AUTORIZADO.toString()) || nfe.getEcfNotaEletronicaStatus().equals(ENotaStatus.CANCELADO.toString())) {
                    // pega a NFe
                    String xml = nfe.getEcfNotaEletronicaXml();
                    int I = xml.indexOf("<infNFe");
                    int F = xml.indexOf("</NFe>") + 6;
                    String texto = "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">" + xml.substring(I, F);
                    TNFe tnfe = NFe.xmlToObj(texto, TNFe.class);

                    for (TNFe.InfNFe.Det det : tnfe.getInfNFe().getDet()) {
                        // encontro o produto
                        IFiltro filtro;
                        if (det.getProd().getCEAN().equals("")) {
                            filtro = new FiltroNumero("prodProdutoId", ECompara.IGUAL, det.getProd().getCProd());
                        } else {
                            filtro = new FiltroTexto("prodProdutoBarra", ECompara.IGUAL, det.getProd().getCEAN());
                        }
                        ProdProduto prod = (ProdProduto) service.selecionar(new ProdProduto(), filtro);

                        Dados54 d54 = new Dados54();
                        d54.setCnpj(nfe.getSisCliente().getSisClienteDoc());
                        d54.setModelo(55);
                        d54.setSerie(Util.getConfig().get("nfe.serie"));
                        d54.setNumero(nfe.getEcfNotaEletronicaNumero());
                        d54.setCfop(Integer.valueOf(det.getProd().getCFOP()));
                        if (prod.getProdProdutoCstCson().length() == 3) {
                            d54.setCst(prod.getProdProdutoCstCson());
                        } else {
                            d54.setCst(prod.getProdProdutoOrigem() + prod.getProdProdutoCstCson());
                        }
                        d54.setNumero(Integer.valueOf(det.getNItem()));
                        d54.setCodigo(prod.getId() + "");
                        d54.setQtd(Double.valueOf(det.getProd().getQCom()));
                        d54.setValor(Double.valueOf(det.getProd().getVProd()));
                        double desc = det.getProd().getVDesc() == null ? 0.00 : Double.valueOf(det.getProd().getVDesc());
                        d54.setDesconto(desc);
                        d54.setBase_icms(prod.getProdProdutoIcms() > 0 ? d54.getValor() : 0.00);
                        d54.setBase_icmsST(0.00);
                        d54.setValor_ipi(0.00);
                        d54.setAliq_icms(prod.getProdProdutoIcms());

                        Util.normaliza(d54);
                        ld54.add(d54);
                    }
                }
            } catch (Exception ex) {
                continue;
            }
        }

        return ld54;
    }

    private List<Dados60M> getDados60M() {
        List<Dados60M> ld60m = new ArrayList<>();

        for (EcfZ z : zs) {
            Dados60M d60m = new Dados60M();
            d60m.setData(z.getEcfZMovimento());
            d60m.setSerie(z.getEcfImpressora().getEcfImpressoraSerie());
            d60m.setCaixa(z.getEcfImpressora().getEcfImpressoraCaixa());
            d60m.setModelo(z.getEcfImpressora().getEcfImpressoraCodigo());
            d60m.setCooInicial(z.getEcfZCooIni());
            d60m.setCooFinal(z.getEcfZCooFin());
            d60m.setCrz(z.getEcfZCrz());
            d60m.setCro(z.getEcfZCro());
            d60m.setValorBruto(z.getEcfZBruto());
            d60m.setValorGeral(z.getEcfZGt());

            // Dados 60A
            d60m.setDados60A(getDados60A(z));
            // Dados 60D
            d60m.setDados60D(getDados60D(z));
            // Dados 60I
            d60m.setDados60I(getDados60I(z));

            Util.normaliza(d60m);
            ld60m.add(d60m);
        }

        return ld60m;
    }

    private List<Dados60A> getDados60A(EcfZ z) {
        List<Dados60A> ld60a = new ArrayList<>();

        for (EcfZTotais total : z.getEcfZTotais()) {
            Dados60A d60a = new Dados60A();
            d60a.setData(z.getEcfZMovimento());
            d60a.setSerie(z.getEcfImpressora().getEcfImpressoraSerie());
            if (total.getEcfZTotaisCodigo().length() == 7 && 
                    (total.getEcfZTotaisCodigo().substring(2, 3).equals("T") || total.getEcfZTotaisCodigo().substring(2, 3).equals("S"))) {
                d60a.setTotalizador(total.getEcfZTotaisCodigo().substring(3));
            } else if (total.getEcfZTotaisCodigo().startsWith("C")) {
                d60a.setTotalizador("CANC");
            } else if (total.getEcfZTotaisCodigo().startsWith("D")) {
                d60a.setTotalizador("DESC");
            } else if (total.getEcfZTotaisCodigo().startsWith("S")) {
                d60a.setTotalizador("ISS");
            } else {
                d60a.setTotalizador(total.getEcfZTotaisCodigo());
            }
            d60a.setValor(total.getEcfZTotaisValor());

            Util.normaliza(d60a);
            ld60a.add(d60a);
        }

        return ld60a;
    }

    private List<Dados60D> getDados60D(EcfZ z) {
        List<Dados60D> ld60d = new ArrayList<>();

        // agrupando os itens vendidos pelo id produto no dia.
        Map<Integer, List<EcfVendaProduto>> diario = new HashMap<>();
        for (EcfVenda venda : z.getEcfVendas()) {
            for (EcfVendaProduto vp : venda.getEcfVendaProdutos()) {
                List<EcfVendaProduto> lista = diario.get(vp.getProdProduto().getId());
                if (lista == null) {
                    lista = new ArrayList<>();
                    lista.add(vp);
                    diario.put(vp.getProdProduto().getId(), lista);
                } else {
                    lista.add(vp);
                }
            }
        }

        // gerando os valores do dia por produto
        for (Entry<Integer, List<EcfVendaProduto>> entry : diario.entrySet()) {
            double qtd = 0.00;
            double liquido = 0.00;
            double icms = 0.00;
            char trib = 0;
            double aliq = 0.00;

            // soma os valores
            for (EcfVendaProduto vp : entry.getValue()) {
                qtd += vp.getEcfVendaProdutoQuantidade();
                liquido += vp.getEcfVendaProdutoLiquido();
                icms += vp.getEcfVendaProdutoIcms();
                trib = vp.getProdProduto().getProdProdutoTributacao();
                aliq = vp.getProdProduto().getProdProdutoIcms();
            }

            Dados60D d60d = new Dados60D();
            d60d.setData(z.getEcfZMovimento());
            d60d.setSerie(z.getEcfImpressora().getEcfImpressoraSerie());
            d60d.setCodigo(entry.getKey() + "");
            d60d.setQtd(qtd);
            d60d.setLiquido(liquido);
            if (trib == 'T') {
                d60d.setBase_icms(liquido);
                d60d.setTributacao(Util.formataNumero(aliq * 100, 4, 0, false));
                d60d.setValor_icms(icms);
            } else {
                d60d.setTributacao(trib + "");
            }

            Util.normaliza(d60d);
            ld60d.add(d60d);
        }

        return ld60d;
    }

    private List<Dados60I> getDados60I(EcfZ z) {
        List<Dados60I> ld60i = new ArrayList<>();

        for (EcfVenda venda : z.getEcfVendas()) {
            for (EcfVendaProduto vp : venda.getEcfVendaProdutos()) {
                Dados60I d60i = new Dados60I();
                d60i.setData(z.getEcfZMovimento());
                d60i.setSerie(z.getEcfImpressora().getEcfImpressoraSerie());
                d60i.setModelo(z.getEcfImpressora().getEcfImpressoraCodigo());
                d60i.setCoo(venda.getEcfVendaCoo());
                d60i.setItem(vp.getEcfVendaProdutoOrdem());
                d60i.setCodigo(vp.getProdProduto().getId() + "");
                d60i.setQtd(vp.getEcfVendaProdutoQuantidade());
                d60i.setLiquido(vp.getEcfVendaProdutoLiquido());
                if (vp.getProdProduto().getProdProdutoTributacao() == 'T') {
                    d60i.setBase_icms(vp.getEcfVendaProdutoLiquido());
                    d60i.setTributacao(Util.formataNumero(vp.getProdProduto().getProdProdutoIcms() * 100, 4, 0, false));
                    d60i.setValor_icms(vp.getEcfVendaProdutoIcms());
                } else {
                    d60i.setTributacao(vp.getProdProduto().getProdProdutoTributacao() + "");
                }

                Util.normaliza(d60i);
                ld60i.add(d60i);
            }
        }

        return ld60i;
    }

    private List<Dados60R> getDados60R() {
        List<Dados60R> ld60r = new ArrayList<>();

        // agrupando os itens vendidos pelo mes+ano+id produto.
        Map<String, List<EcfVendaProduto>> mensal = new HashMap<>();
        for (EcfZ z : zs) {
            String mesAno = Util.formataData(z.getEcfZMovimento(), "MMyyyy");
            for (EcfVenda venda : z.getEcfVendas()) {
                for (EcfVendaProduto vp : venda.getEcfVendaProdutos()) {
                    String chave = mesAno + "-" + vp.getProdProduto().getId();
                    List<EcfVendaProduto> lista = mensal.get(chave);
                    if (lista == null) {
                        lista = new ArrayList<>();
                        lista.add(vp);
                        mensal.put(chave, lista);
                    } else {
                        lista.add(vp);
                    }
                }
            }
        }

        // gerando os valores do mes/ano por produto
        for (Entry<String, List<EcfVendaProduto>> entry : mensal.entrySet()) {
            double qtd = 0.00;
            double liquido = 0.00;
            char trib = 0;
            double aliq = 0.00;

            // soma os valores
            for (EcfVendaProduto vp : entry.getValue()) {
                qtd += vp.getEcfVendaProdutoQuantidade();
                liquido += vp.getEcfVendaProdutoLiquido();
                trib = vp.getProdProduto().getProdProdutoTributacao();
                aliq = vp.getProdProduto().getProdProdutoIcms();
            }

            Dados60R d60r = new Dados60R();
            d60r.setMesAno(Integer.valueOf(entry.getKey().substring(0, 6)));
            d60r.setCodigo(entry.getKey() + "");
            d60r.setQtd(qtd);
            d60r.setLiquido(liquido);
            if (trib == 'T') {
                d60r.setBase_icms(liquido);
                d60r.setTributacao(Util.formataNumero(aliq * 100, 4, 0, false));
            } else {
                d60r.setTributacao(trib + "");
            }

            Util.normaliza(d60r);
            ld60r.add(d60r);
        }

        return ld60r;
    }

    private List<Dados61> getDados61() {
        List<Dados61> ld61 = new ArrayList<>();

        // agrupando as notas por dia+serie+subserie
        Map<String, List<EcfNota>> grupo = new HashMap<>();
        for (EcfNota nota : notas) {
            String chave = Util.formataData(nota.getEcfNotaData(), "ddMMyyyy") + nota.getEcfNotaSerie() + nota.getEcfNotaSubserie();
            List<EcfNota> lista = grupo.get(chave);
            if (lista == null) {
                lista = new ArrayList<>();
                lista.add(nota);
                grupo.put(chave, lista);
            } else {
                lista.add(nota);
            }
        }

        // soma os valores agrupados
        for (Entry<String, List<EcfNota>> entry : grupo.entrySet()) {
            Dados61 d61 = new Dados61();

            for (EcfNota nota : entry.getValue()) {
                d61.setData(nota.getEcfNotaData());
                d61.setModelo(2);
                d61.setSerie(nota.getEcfNotaSerie());
                d61.setSubserie(nota.getEcfNotaSubserie());
                if (nota.getEcfNotaNumero() < d61.getNumInicial()) {
                    d61.setNumInicial(nota.getEcfNotaNumero());
                }
                if (nota.getEcfNotaNumero() > d61.getNumFinal()) {
                    d61.setNumFinal(nota.getEcfNotaNumero());
                }
                if (!nota.isEcfNotaCancelada()) {
                    d61.setValorTotal(d61.getValorTotal() + nota.getEcfNotaLiquido());
                    double base_icms = 0.00;
                    double valor_icms = 0.00;
                    double isento = 0.00;
                    double aliq = 0.00;
                    for (EcfNotaProduto np : nota.getEcfNotaProdutos()) {
                        if (np.getProdProduto().getProdProdutoTributacao() == 'T') {
                            base_icms += np.getEcfNotaProdutoLiquido();
                            valor_icms += (np.getEcfNotaProdutoLiquido() * np.getProdProduto().getProdProdutoIcms() / 100);
                            aliq = np.getProdProduto().getProdProdutoIcms();
                        } else if (np.getProdProduto().getProdProdutoTributacao() == 'I' || np.getProdProduto().getProdProdutoTributacao() == 'N') {
                            isento += np.getEcfNotaProdutoLiquido();
                        }
                    }
                    d61.setBase_icms(base_icms);
                    d61.setValor_icms(valor_icms);
                    d61.setValor_isento(isento);
                    d61.setOutras(0.00);
                    d61.setAliq_icms(aliq);
                }
            }

            Util.normaliza(d61);
            ld61.add(d61);
        }

        return ld61;
    }

    private List<Dados61R> getDados61R() {
        List<Dados61R> ld61r = new ArrayList<>();

        // agrupando as notas por mes+ano+id do produto
        Map<String, List<EcfNotaProduto>> mensal = new HashMap<>();
        for (EcfNota nota : notas) {
            String mesAno = Util.formataData(nota.getEcfNotaData(), "MMyyyy");
            for (EcfNotaProduto np : nota.getEcfNotaProdutos()) {
                String chave = mesAno + "-" + np.getProdProduto().getId();
                List<EcfNotaProduto> lista = mensal.get(chave);
                if (lista == null) {
                    lista = new ArrayList<>();
                    lista.add(np);
                    mensal.put(chave, lista);
                } else {
                    lista.add(np);
                }
            }
        }

        // gerando os valores do mes/ano por produto
        for (Entry<String, List<EcfNotaProduto>> entry : mensal.entrySet()) {
            double qtd = 0.00;
            double bruto = 0.00;
            char trib = 0;
            double aliq = 0.00;

            // soma os valores
            for (EcfNotaProduto np : entry.getValue()) {
                qtd += np.getEcfNotaProdutoQuantidade();
                bruto += np.getEcfNotaProdutoBruto();
                trib = np.getProdProduto().getProdProdutoTributacao();
                aliq = np.getProdProduto().getProdProdutoIcms();
            }

            Dados61R d61r = new Dados61R();
            d61r.setMesAno(Integer.valueOf(entry.getKey().substring(0, 6)));
            d61r.setCodigo(entry.getKey() + "");
            d61r.setQtd(qtd);
            d61r.setBruto(bruto);
            if (trib == 'T') {
                d61r.setBase_icms(bruto);
                d61r.setAliq_icms(aliq);
            }

            Util.normaliza(d61r);
            ld61r.add(d61r);
        }

        return ld61r;
    }

    private List<Dados74> getDados74() {
        List<Dados74> ld74 = new ArrayList<>();

        for (ProdProduto prod : estoque) {
            Dados74 d74 = new Dados74();
            d74.setData(fim);
            d74.setCodigo(prod.getId() + "");
            d74.setQtd(prod.getProdProdutoEstoque());
            d74.setValor(prod.getProdProdutoPreco());
            d74.setPosse("1");
            d74.setCnpj(emp.getSisEmpresaCnpj());
            d74.setIe(emp.getSisEmpresaIe());
            d74.setUf(emp.getSisMunicipio().getSisEstado().getSisEstadoSigla());

            Util.normaliza(d74);
            ld74.add(d74);
        }

        return ld74;
    }

    private List<Dados75> getDados75() {
        List<Dados75> ld75 = new ArrayList<>();

        for (ProdProduto prod : estoque) {
            Dados75 d75 = new Dados75();
            d75.setInicio(inicio);
            d75.setFim(fim);
            d75.setCodigo(prod.getId() + "");
            d75.setNcm(prod.getProdProdutoNcm());
            d75.setDescricao(prod.getProdProdutoDescricao());
            d75.setUnd(prod.getProdEmbalagem().getProdEmbalagemNome());
            d75.setAliq_ipi(0.00);
            d75.setAliq_icms(prod.getProdProdutoIcms());
            d75.setReducao(0.00);
            d75.setBase_icmsST(0.00);

            Util.normaliza(d75);
            ld75.add(d75);
        }

        return ld75;
    }
}
