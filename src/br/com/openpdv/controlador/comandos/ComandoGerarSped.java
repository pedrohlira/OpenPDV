package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.NFe;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.EDirecao;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.*;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.openpdv.visao.core.Caixa;
import br.com.opensig.nfe.TNFe;
import br.com.opensig.nfe.TNFe.InfNFe.Det;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.COFINS;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Imposto.PIS;
import br.com.opensig.nfe.TNFe.InfNFe.Det.Prod;
import br.com.opensig.nfe.TNFe.InfNFe.Ide;
import br.com.opensig.nfe.TNFe.InfNFe.Total.ICMSTot;
import br.com.opensig.nfe.TNFe.InfNFe.Transp;
import br.com.phdss.controlador.PAF;
import br.com.phdss.modelo.sped.Sped;
import br.com.phdss.modelo.sped.bloco0.*;
import br.com.phdss.modelo.sped.bloco1.Bloco1;
import br.com.phdss.modelo.sped.bloco1.Dados1001;
import br.com.phdss.modelo.sped.bloco1.Dados1010;
import br.com.phdss.modelo.sped.bloco1.Dados1990;
import br.com.phdss.modelo.sped.blocoC.*;
import br.com.phdss.modelo.sped.blocoD.BlocoD;
import br.com.phdss.modelo.sped.blocoD.DadosD001;
import br.com.phdss.modelo.sped.blocoD.DadosD990;
import br.com.phdss.modelo.sped.blocoE.BlocoE;
import br.com.phdss.modelo.sped.blocoE.DadosE001;
import br.com.phdss.modelo.sped.blocoE.DadosE990;
import br.com.phdss.modelo.sped.blocoG.BlocoG;
import br.com.phdss.modelo.sped.blocoG.DadosG001;
import br.com.phdss.modelo.sped.blocoG.DadosG990;
import br.com.phdss.modelo.sped.blocoH.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * Classe que realiza a acao de gerar o SPED Fiscal.
 *
 * @author Pedro H. Lira
 */
public class ComandoGerarSped implements IComando {

    private String path;
    private Date inicio;
    private Date fim;
    private int finalidade;
    private CoreService service;
    private SisEmpresa emp;
    private EcfImpressora imp;
    private List<EcfNotaEletronica> nfes;
    private List<EcfNota> notas;
    private List<EcfZ> zs;
    private List<ProdProduto> estoque;

    public ComandoGerarSped(Date inicio, Date fim, int finalidade) {
        this.inicio = inicio;
        this.fim = fim;
        this.finalidade = finalidade;
        this.service = new CoreService();
        this.emp = Caixa.getInstancia().getEmpresa();
        this.imp = Caixa.getInstancia().getImpressora();
    }

    @Override
    public void executar() throws OpenPdvException {
        // recupera as nfes emitidas no periodo
        FiltroData fd1 = new FiltroData("ecfNotaEletronicaData", ECompara.MAIOR_IGUAL, inicio);
        FiltroData fd2 = new FiltroData("ecfNotaEletronicaData", ECompara.MENOR_IGUAL, fim);
        GrupoFiltro gp1 = new GrupoFiltro(EJuncao.E, new IFiltro[]{fd1, fd2});
        EcfNotaEletronica ene = new EcfNotaEletronica();
        ene.setOrdemDirecao(EDirecao.ASC);
        nfes = service.selecionar(ene, 0, 0, gp1);

        // recupera as notas emitidas no periodo
        FiltroData fd3 = new FiltroData("ecfNotaData", ECompara.MAIOR_IGUAL, inicio);
        FiltroData fd4 = new FiltroData("ecfNotaData", ECompara.MENOR_IGUAL, fim);
        GrupoFiltro gp2 = new GrupoFiltro(EJuncao.E, new IFiltro[]{fd3, fd4});
        EcfNota en = new EcfNota();
        en.setOrdemDirecao(EDirecao.ASC);
        notas = service.selecionar(en, 0, 0, gp2);

        // recupera as leituras Z no periodo
        FiltroData fd7 = new FiltroData("ecfZMovimento", ECompara.MAIOR_IGUAL, inicio);
        FiltroData fd8 = new FiltroData("ecfZMovimento", ECompara.MENOR_IGUAL, fim);
        GrupoFiltro gf4 = new GrupoFiltro(EJuncao.E, new IFiltro[]{fd7, fd8});
        EcfZ ez = new EcfZ();
        ez.setOrdemDirecao(EDirecao.ASC);
        zs = service.selecionar(ez, 0, 0, gf4);

        // recupera os produtos com estoque maior que zero
        FiltroNumero fn = new FiltroNumero("prodProdutoEstoque", ECompara.MAIOR, 0);
        ProdProduto pp = new ProdProduto();
        pp.setCampoOrdem("prodProdutoId");
        estoque = service.selecionar(pp, 0, 0, fn);

        Sped sped = new Sped();
        // bloco0
        sped.setBloco0(getBloco0());
        // blocoC
        sped.setBlocoC(getBlocoC());
        // blocoD
        sped.setBlocoD(getBlocoD());
        // blocoE
        sped.setBlocoE(getBlocoE());
        // blocoG
        sped.setBlocoG(getBlocoG());
        // blocoH
        sped.setBlocoH(getBlocoH());
        // bloco1
        sped.setBloco1(getBloco1());
        // bloco9 e gerado internamento pelo PAF

        try {
            // gerar o arquivo
            path = PAF.gerarVendasPeriodo(sped);
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

    private Bloco0 getBloco0() throws OpenPdvException {
        int linhas = 0;
        Bloco0 bl0 = new Bloco0();

        // Registro 0000
        bl0.setD0000(getDados0());
        linhas++;
        // Registro 0001
        Dados0001 d1 = new Dados0001();
        d1.setInd_mov(0);
        bl0.setD0001(d1);
        linhas++;
        // Registro 0005
        bl0.setD0005(getDados5());
        linhas++;
        // Registro 0100
        bl0.setD0100(getDados100());
        linhas++;
        // Registro 0150, 0190, 0200, 0220
        Map<Integer, Dados0150> l150 = new HashMap<>();
        Map<Integer, Dados0190> l190 = new HashMap<>();
        Map<Integer, Dados0200> l200 = new HashMap<>();
        Map<Integer, Dados0220> l220 = new HashMap<>();
        // notas eletronicas
        for (EcfNotaEletronica nfe : nfes) {
            // clientes
            if (!l150.containsKey(nfe.getSisCliente().getId())) {
                l150.put(nfe.getSisCliente().getId(), getDados150(nfe.getSisCliente()));
            }
        }
        // notas de consumidor
        for (EcfNota nota : notas) {
            // clientes
            if (!l150.containsKey(nota.getSisCliente().getId())) {
                l150.put(nota.getSisCliente().getId(), getDados150(nota.getSisCliente()));
            }
            if (!nota.isEcfNotaCancelada()) {
                for (EcfNotaProduto np : nota.getEcfNotaProdutos()) {
                    // embalagens
                    if (!l190.containsKey(np.getProdEmbalagem().getId())) {
                        l190.put(np.getProdEmbalagem().getId(), getDados190(np.getProdEmbalagem()));
                    }
                    // produtos
                    if (!l200.containsKey(np.getProdProduto().getId())) {
                        l200.put(np.getProdProduto().getId(), getDados200(np.getProdProduto()));
                    }
                    // fator de conversao
                    if (!l220.containsKey(np.getProdEmbalagem().getId())) {
                        l220.put(np.getProdEmbalagem().getId(), getDados220(np.getProdEmbalagem()));
                    }
                }
            }
        }
        // cupom fiscais
        for (EcfZ z : zs) {
            for (EcfVenda venda : z.getEcfVendas()) {
                if (!venda.getEcfVendaCancelada()) {
                    for (EcfVendaProduto vp : venda.getEcfVendaProdutos()) {
                        if (!vp.getEcfVendaProdutoCancelado()) {
                            // embalagens
                            if (!l190.containsKey(vp.getProdEmbalagem().getId())) {
                                l190.put(vp.getProdEmbalagem().getId(), getDados190(vp.getProdEmbalagem()));
                            }
                            // produtos
                            if (!l200.containsKey(vp.getProdProduto().getId())) {
                                l200.put(vp.getProdProduto().getId(), getDados200(vp.getProdProduto()));
                            }
                            // fator de conversao
                            if (!l220.containsKey(vp.getProdEmbalagem().getId())) {
                                l220.put(vp.getProdEmbalagem().getId(), getDados220(vp.getProdEmbalagem()));
                            }
                        }
                    }
                }
            }
        }
        // estoque de produtos
        for (ProdProduto prod : estoque) {
            // embalagens
            if (!l190.containsKey(prod.getProdEmbalagem().getId())) {
                l190.put(prod.getProdEmbalagem().getId(), getDados190(prod.getProdEmbalagem()));
            }
            // produtos
            if (!l200.containsKey(prod.getId())) {
                l200.put(prod.getId(), getDados200(prod));
            }
            // fator de conversao
            if (!l220.containsKey(prod.getProdEmbalagem().getId())) {
                l220.put(prod.getProdEmbalagem().getId(), getDados220(prod.getProdEmbalagem()));
            }
        }
        bl0.setD0150(new ArrayList<>(l150.values()));
        linhas += bl0.getD0150().size();
        bl0.setD0190(new ArrayList<>(l190.values()));
        linhas += bl0.getD0190().size();
        bl0.setD0200(new ArrayList<>(l200.values()));
        linhas += bl0.getD0200().size();
        bl0.setD0220(new ArrayList<>(l220.values()));
        linhas += bl0.getD0220().size();
        // Registro 0450
        bl0.setD0450(getDados450());
        linhas++;
        // Registro 0500
        bl0.setD0500(getDados500());
        linhas++;
        // Registro 0990
        Dados0990 d990 = new Dados0990();
        d990.setQtd_lin(linhas++);
        bl0.setD0990(d990);
        return bl0;
    }

    private Dados0000 getDados0() {
        Dados0000 d0 = new Dados0000();
        d0.setCod_ver(Integer.valueOf(Util.getConfig().get("sped.layout")));
        d0.setCod_fin(finalidade);
        d0.setDt_ini(inicio);
        d0.setDt_fin(fim);
        d0.setNome(emp.getSisEmpresaRazao());
        d0.setCnpj(emp.getSisEmpresaCnpj());
        d0.setUf(emp.getSisMunicipio().getSisEstado().getSisEstadoSigla());
        d0.setIe(emp.getSisEmpresaIe());
        d0.setCod_mun(emp.getSisMunicipio().getSisMunicipioIbge());
        d0.setIm(emp.getSisEmpresaIm());
        d0.setInd_perfil(Util.getConfig().get("sped.perfil"));
        d0.setInd_ativ(1);
        Util.normaliza(d0);
        return d0;
    }

    private Dados0005 getDados5() {
        Dados0005 d5 = new Dados0005();
        d5.setFantasia(emp.getSisEmpresaFantasia());
        d5.setCep(Integer.valueOf(emp.getSisEmpresaCep()));
        d5.setEnd(emp.getSisEmpresaLogradouro());
        d5.setNum(emp.getSisEmpresaNumero() + "");
        d5.setCompl(emp.getSisEmpresaComplemento());
        d5.setBairro(emp.getSisEmpresaBairro());
        d5.setFone(emp.getSisEmpresaFone());
        d5.setEmail(emp.getSisEmpresaEmail());
        Util.normaliza(d5);
        return d5;
    }

    private Dados0100 getDados100() throws OpenPdvException {
        // contador do cliente
        FiltroBinario fb = new FiltroBinario("sisEmpresaContador", ECompara.IGUAL, true);
        SisEmpresa cont = (SisEmpresa) service.selecionar(new SisEmpresa(), fb);

        Dados0100 d100 = new Dados0100();
        d100.setNome(cont.getSisEmpresaRazao());
        d100.setCpf(cont.getSisEmpresaCnpj());
        d100.setCrc(cont.getSisEmpresaIe());
        d100.setCep(Integer.valueOf(cont.getSisEmpresaCep()));
        d100.setEnd(cont.getSisEmpresaLogradouro());
        d100.setNum(cont.getSisEmpresaNumero() + "");
        d100.setCompl(cont.getSisEmpresaComplemento());
        d100.setBairro(cont.getSisEmpresaBairro());
        d100.setFone(cont.getSisEmpresaFone());
        d100.setEmail(cont.getSisEmpresaEmail());
        d100.setCod_mun(cont.getSisMunicipio().getSisMunicipioIbge());
        Util.normaliza(d100);
        return d100;
    }

    private Dados0150 getDados150(SisCliente cli) {
        Dados0150 d150 = new Dados0150();
        d150.setCod_part(cli.getId() + "");
        d150.setNome(cli.getSisClienteNome());
        d150.setCod_pais(1058);
        Util.normaliza(d150);
        return d150;
    }

    private Dados0190 getDados190(ProdEmbalagem emb) {
        Dados0190 d190 = new Dados0190();
        d190.setUnid(emb.getProdEmbalagemNome());
        d190.setDescr(emb.getProdEmbalagemDescricao());
        Util.normaliza(d190);
        return d190;
    }

    private Dados0200 getDados200(ProdProduto prod) {
        Dados0200 d200 = new Dados0200();
        d200.setCod_item(prod.getId() + "");
        d200.setDescr_item(prod.getProdProdutoDescricao());
        d200.setCod_barra(prod.getProdProdutoBarra());
        d200.setCod_ant_item("");
        d200.setUnid_inv(prod.getProdEmbalagem().getProdEmbalagemNome());
        d200.setTipo_item(Integer.valueOf(prod.getProdProdutoTipo()));
        if (prod.getProdProdutoNcm().length() == 8) {
            d200.setCod_ncm(prod.getProdProdutoNcm());
            d200.setCod_gen(prod.getProdProdutoNcm().substring(0, 2));
        } else {
            d200.setCod_gen(prod.getProdProdutoNcm());
        }
        d200.setEx_ipi("");
        d200.setAliq_icms(prod.getProdProdutoIcms());
        Util.normaliza(d200);
        return d200;
    }

    private Dados0220 getDados220(ProdEmbalagem emb) {
        Dados0220 d220 = new Dados0220();
        d220.setUnid_conv(emb.getProdEmbalagemNome());
        d220.setFat_conv(emb.getProdEmbalagemUnidade());
        Util.normaliza(d220);
        return d220;
    }

    private List<Dados0450> getDados450() {
        List<Dados0450> l450 = new ArrayList<>();
        Dados0450 d450 = new Dados0450();
        d450.setCod_inf("1");
        d450.setTxt("MD5 DO PAF-ECF GERADOR DA NFE");
        l450.add(d450);
        Util.normaliza(d450);
        return l450;
    }

    private Dados0500 getDados500() {
        Dados0500 d500 = new Dados0500();
        d500.setDt_alt(fim);
        d500.setCod_nat_cc("01");
        d500.setInd_cta("S");
        d500.setNivel(1);
        d500.setCod_cta("ESTOQUE");
        d500.setNome_cta("CONTA DE CONTROLE DE ESTOQUE");
        Util.normaliza(d500);
        return d500;
    }

    private BlocoC getBlocoC() {
        int linhas = 0;
        BlocoC blC = new BlocoC();
        // Registro C001
        DadosC001 c1 = new DadosC001();
        c1.setInd_mov(0);
        blC.setdC001(c1);
        linhas++;
        // Registro C100
        Entry<Integer, List<DadosC100>> ec100 = getDadosC100();
        blC.setdC100(ec100.getValue());
        linhas += ec100.getKey();
        // Registro C300
        Entry<Integer, List<DadosC300>> ec300 = getDadosC300();
        blC.setdC300(ec300.getValue());
        linhas += ec300.getKey();
        // Registro C350
        Entry<Integer, List<DadosC350>> ec350 = getDadosC350();
        blC.setdC350(ec350.getValue());
        linhas += ec350.getKey();
        // Registro C400
        Entry<Integer, List<DadosC400>> ec400 = getDadosC400();
        blC.setdC400(ec400.getValue());
        linhas += ec400.getKey();
        // Registro C990
        DadosC990 c990 = new DadosC990();
        c990.setQtd_lin(linhas++);
        blC.setdC990(c990);
        return blC;
    }

    private Entry<Integer, List<DadosC100>> getDadosC100() {
        List<DadosC100> lc100 = new ArrayList<>();
        Integer linhas = 0;

        // nfes
        for (EcfNotaEletronica nfe : nfes) {
            DadosC100 c100 = new DadosC100();
            if (nfe.getEcfNotaEletronicaStatus().equals(ENotaStatus.INUTILIZADO.toString())) {
                // Registro C100 para inutilizada
                c100.setCod_sit("05");
                c100.setSer(Util.getConfig().get("nfe.serie"));
                c100.setCod_mod("55");
                c100.setNum_doc(nfe.getEcfNotaEletronicaNumero());
            } else {
                // pega a NFe
                String xml = nfe.getEcfNotaEletronicaXml();
                int I = xml.indexOf("<infNFe");
                int F = xml.indexOf("</NFe>") + 6;
                String texto = "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">" + xml.substring(I, F);

                try {
                    TNFe tnfe = NFe.xmlToObj(texto, TNFe.class);
                    Ide ide = tnfe.getInfNFe().getIde();
                    ICMSTot icms = tnfe.getInfNFe().getTotal().getICMSTot();
                    Transp transp = tnfe.getInfNFe().getTransp();
                    String cod_sit = nfe.getEcfNotaEletronicaStatus().equals(ENotaStatus.AUTORIZADO.toString()) ? "00" : "02";

                    // Registro C100
                    c100.setCod_sit(cod_sit);
                    c100.setSer(Util.getConfig().get("nfe.serie"));
                    c100.setCod_mod("55");
                    c100.setNum_doc(nfe.getEcfNotaEletronicaNumero());
                    c100.setChv_nfe(nfe.getEcfNotaEletronicaChave());
                    if (cod_sit.equals("00")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        c100.setDt_doc(sdf.parse(ide.getDEmi()));
                        c100.setDt_e_s(sdf.parse(ide.getDEmi()));
                        c100.setVl_doc(Double.valueOf(icms.getVNF()));
                        c100.setInd_pgto(ide.getIndPag());
                        c100.setVl_desc(Double.valueOf(icms.getVDesc()));
                        c100.setVl_merc(Double.valueOf(icms.getVProd()));
                        c100.setInd_frt(transp.getModFrete());
                        c100.setVl_frt(Double.valueOf(icms.getVFrete()));
                        c100.setVl_seg(Double.valueOf(icms.getVSeg()));
                        c100.setVl_out_da(Double.valueOf(icms.getVOutro()));
                        c100.setVl_bc_icms(Double.valueOf(icms.getVBC()));
                        c100.setVl_icms(Double.valueOf(icms.getVICMS()));
                        c100.setVl_bc_icms_st(Double.valueOf(icms.getVBCST()));
                        c100.setVl_icms_st(Double.valueOf(icms.getVST()));
                        c100.setVl_ipi(Double.valueOf(icms.getVIPI()));
                        c100.setVl_pis(Double.valueOf(icms.getVPIS()));
                        c100.setVl_cofins(Double.valueOf(icms.getVCOFINS()));

                        // Registro C110
                        c100.setdC110(getDadosC110());
                        linhas++;

                        // Registro C140
                        c100.setdC140(getDadosC140(c100.getDt_doc(), c100.getVl_doc()));
                        linhas += 2;

                        // Registro C170 nao pode ser informador em NFe
                        c100.setdC170(null);

                        // Registro C190
                        List<DadosC190> lc190 = getDadosC190(tnfe);
                        c100.setdC190(lc190);
                        linhas += lc190.size();
                    }
                } catch (Exception ex) {
                    continue;
                }
            }
            lc100.add(c100);
            linhas++;
        }
        return new AbstractMap.SimpleEntry(linhas, lc100);
    }

    private DadosC110 getDadosC110() {
        DadosC110 c110 = new DadosC110();
        c110.setCod_inf("1");
        c110.setTxt_compl(PAF.AUXILIAR.getProperty("out.autenticado"));
        return c110;
    }

    private DadosC140 getDadosC140(Date data, double valor) {
        DadosC140 c140 = new DadosC140();
        c140.setInd_emit("0");
        c140.setInd_tit("99");
        c140.setDesc_tit("DINHEIRO");
        c140.setNum_tit("");
        c140.setQtd_parc(1);
        c140.setVl_tit(valor);
        // Registro C141
        c140.setdC141(getDadosC141(data, valor));
        return c140;
    }

    private List<DadosC141> getDadosC141(Date data, double valor) {
        List<DadosC141> lc141 = new ArrayList<>();
        DadosC141 c141 = new DadosC141();
        c141.setNum_parc(1);
        c141.setDt_vcto(data);
        c141.setVl_parc(valor);
        lc141.add(c141);
        return lc141;
    }

    private DadosC170 getDadosC170(Det dados) {
        Prod prod = dados.getProd();
        DadosC170 c170 = new DadosC170();
        c170.setVl_item(Double.valueOf(prod.getVProd()));
        c170.setCfop(Integer.valueOf(prod.getCFOP()));

        try {
            ICMS icms = dados.getImposto().getICMS();
            if (Util.getConfig().get("nfe.crt").equals("1")) {
                // verifica qual icms CSON foi usado
                if (icms.getICMSSN101() != null) {
                    c170.setCst_icms(icms.getICMSSN101().getCSOSN());
                    c170.setVl_bc_icms(0.00);
                    c170.setAliq_icms(0.00);
                    c170.setVl_icms(0.00);
                } else if (icms.getICMSSN102() != null) {
                    c170.setCst_icms(icms.getICMSSN102().getCSOSN());
                    c170.setVl_bc_icms(0.00);
                    c170.setAliq_icms(0.00);
                    c170.setVl_icms(0.00);
                } else if (icms.getICMSSN201() != null) {
                    c170.setCst_icms(icms.getICMSSN201().getCSOSN());
                    c170.setVl_bc_icms_st(Double.valueOf(icms.getICMSSN201().getVBCST()));
                    c170.setAliq_st(Double.valueOf(icms.getICMSSN201().getPICMSST()));
                    c170.setVl_icms_st(Double.valueOf(icms.getICMSSN201().getVICMSST()));
                } else if (icms.getICMSSN202() != null) {
                    c170.setCst_icms(icms.getICMSSN202().getCSOSN());
                    c170.setVl_bc_icms_st(Double.valueOf(icms.getICMSSN202().getVBCST()));
                    c170.setAliq_st(Double.valueOf(icms.getICMSSN202().getPICMSST()));
                    c170.setVl_icms_st(Double.valueOf(icms.getICMSSN202().getVICMSST()));
                } else if (icms.getICMSSN500() != null) {
                    c170.setCst_icms(icms.getICMSSN500().getCSOSN());
                    c170.setVl_bc_icms_st(Double.valueOf(icms.getICMSSN500().getVBCSTRet()));
                    c170.setVl_icms_st(Double.valueOf(icms.getICMSSN500().getVICMSSTRet()));
                    c170.setAliq_st(c170.getVl_icms_st() * 100 / c170.getVl_bc_icms_st());
                } else if (icms.getICMSSN900() != null) {
                    c170.setCst_icms(icms.getICMSSN900().getCSOSN());
                    if (icms.getICMSSN900().getModBC() != null) {
                        c170.setVl_bc_icms(Double.valueOf(icms.getICMSSN900().getVBC()));
                        c170.setAliq_icms(Double.valueOf(icms.getICMSSN900().getPICMS()));
                        c170.setVl_icms(Double.valueOf(icms.getICMSSN900().getVICMS()));
                    } else {
                        c170.setVl_bc_icms(Double.valueOf(icms.getICMSSN900().getVBCST()));
                        c170.setAliq_icms(Double.valueOf(icms.getICMSSN900().getPICMSST()));
                        c170.setVl_icms(Double.valueOf(icms.getICMSSN900().getVICMSST()));
                    }
                }
            } else {
                // verifica qual icms CST foi usado
                if (icms.getICMS00() != null) {
                    c170.setCst_icms("0" + icms.getICMS00().getCST());
                    c170.setVl_bc_icms(Double.valueOf(icms.getICMS00().getVBC()));
                    c170.setAliq_icms(Double.valueOf(icms.getICMS00().getPICMS()));
                    c170.setVl_icms(Double.valueOf(icms.getICMS00().getVICMS()));
                } else if (icms.getICMS10() != null) {
                    c170.setCst_icms("0" + icms.getICMS10().getCST());
                    c170.setVl_bc_icms_st(Double.valueOf(icms.getICMS10().getVBCST()));
                    c170.setAliq_st(Double.valueOf(icms.getICMS10().getPICMSST()));
                    c170.setVl_icms_st(Double.valueOf(icms.getICMS10().getVICMSST()));
                } else if (icms.getICMS20() != null) {
                    c170.setCst_icms("0" + icms.getICMS20().getCST());
                    c170.setVl_bc_icms(Double.valueOf(icms.getICMS20().getVBC()));
                    c170.setAliq_icms(Double.valueOf(icms.getICMS20().getPICMS()));
                    c170.setVl_icms(Double.valueOf(icms.getICMS20().getVICMS()));
                } else if (icms.getICMS30() != null) {
                    c170.setCst_icms("0" + icms.getICMS30().getCST());
                    c170.setVl_bc_icms_st(Double.valueOf(icms.getICMS30().getVBCST()));
                    c170.setAliq_st(Double.valueOf(icms.getICMS30().getPICMSST()));
                    c170.setVl_icms_st(Double.valueOf(icms.getICMS30().getVICMSST()));
                } else if (icms.getICMS40() != null) {
                    c170.setCst_icms("0" + icms.getICMS40().getCST());
                    c170.setVl_bc_icms(0.00);
                    c170.setAliq_icms(0.00);
                    c170.setVl_icms(0.00);
                } else if (icms.getICMS51() != null) {
                    c170.setCst_icms("0" + icms.getICMS51().getCST());
                    c170.setVl_bc_icms(Double.valueOf(icms.getICMS51().getVBC()));
                    c170.setAliq_icms(Double.valueOf(icms.getICMS51().getPICMS()));
                    c170.setVl_icms(Double.valueOf(icms.getICMS51().getVICMS()));
                } else if (icms.getICMS60() != null) {
                    c170.setCst_icms("0" + icms.getICMS60().getCST());
                    c170.setVl_bc_icms_st(Double.valueOf(icms.getICMS60().getVBCSTRet()));
                    c170.setAliq_st(c170.getVl_icms_st() * 100 / c170.getVl_bc_icms_st());
                    c170.setVl_icms_st(Double.valueOf(icms.getICMS60().getVICMSSTRet()));
                } else if (icms.getICMS70() != null) {
                    c170.setCst_icms("0" + icms.getICMS70().getCST());
                    if (icms.getICMS70().getModBC() != null) {
                        c170.setVl_bc_icms(Double.valueOf(icms.getICMS70().getVBC()));
                        c170.setAliq_icms(Double.valueOf(icms.getICMS70().getPICMS()));
                        c170.setVl_icms(Double.valueOf(icms.getICMS70().getVICMS()));
                    } else {
                        c170.setVl_bc_icms_st(Double.valueOf(icms.getICMS70().getVBCST()));
                        c170.setVl_icms_st(Double.valueOf(icms.getICMS70().getVICMSST()));
                        c170.setAliq_st(Double.valueOf(icms.getICMS70().getPICMSST()));
                    }
                } else if (icms.getICMS90() != null) {
                    c170.setCst_icms("0" + icms.getICMS90().getCST());
                    if (icms.getICMS90().getModBC() != null) {
                        c170.setVl_bc_icms(Double.valueOf(icms.getICMS90().getVBC()));
                        c170.setAliq_icms(Double.valueOf(icms.getICMS90().getPICMS()));
                        c170.setVl_icms(Double.valueOf(icms.getICMS90().getVICMS()));
                    } else {
                        c170.setVl_bc_icms_st(Double.valueOf(icms.getICMS90().getVBCST()));
                        c170.setVl_icms_st(Double.valueOf(icms.getICMS90().getVICMSST()));
                        c170.setAliq_st(Double.valueOf(icms.getICMS90().getPICMSST()));
                    }
                }
            }
        } catch (Exception e) {
            c170.setCst_icms("000");
            c170.setVl_bc_icms(0.00);
            c170.setAliq_icms(0.00);
            c170.setVl_icms(0.00);
        }

        // pis
        PIS pis = dados.getImposto().getPIS();
        if (pis != null) {
            try {
                if (pis.getPISAliq() != null) {
                    c170.setCst_pis(pis.getPISAliq().getCST());
                    c170.setVl_bc_pis(Double.valueOf(pis.getPISAliq().getVBC()));
                    c170.setAliq_pis(Double.valueOf(pis.getPISAliq().getPPIS()));
                    c170.setAliq2_pis(null);
                    c170.setVl_pis(Double.valueOf(pis.getPISAliq().getVPIS()));
                } else if (pis.getPISNT() != null) {
                    c170.setCst_pis(pis.getPISNT().getCST());
                } else if (pis.getPISOutr() != null) {
                    c170.setCst_pis(pis.getPISOutr().getCST());
                    c170.setVl_bc_pis(Double.valueOf(pis.getPISOutr().getVBC()));
                    c170.setAliq_pis(Double.valueOf(pis.getPISOutr().getPPIS()));
                    c170.setAliq2_pis(null);
                    c170.setQuant_bc_pis(pis.getPISOutr().getQBCProd() == null ? 0.00 : Double.valueOf(pis.getPISOutr().getQBCProd()));
                    c170.setVl_pis(Double.valueOf(pis.getPISOutr().getVPIS()));
                } else if (pis.getPISQtde() != null) {
                    c170.setCst_pis(pis.getPISQtde().getCST());
                    c170.setAliq_pis(null);
                    c170.setAliq2_pis(pis.getPISQtde().getVAliqProd() == null ? 0.00 : Double.valueOf(pis.getPISQtde().getVAliqProd()));
                    c170.setQuant_bc_pis(pis.getPISQtde().getQBCProd() == null ? 0.00 : Double.valueOf(pis.getPISQtde().getQBCProd()));
                    c170.setVl_pis(Double.valueOf(pis.getPISQtde().getVPIS()));
                }
            } catch (Exception e) {
                c170.setCst_pis("");
            }
        } else {
            c170.setCst_pis("");
        }

        // cofins
        COFINS cofins = dados.getImposto().getCOFINS();
        if (cofins != null) {
            try {
                if (cofins.getCOFINSAliq() != null) {
                    c170.setCst_cofins(cofins.getCOFINSAliq().getCST());
                    c170.setVl_bc_cofins(Double.valueOf(cofins.getCOFINSAliq().getVBC()));
                    c170.setAliq_cofins(Double.valueOf(cofins.getCOFINSAliq().getPCOFINS()));
                    c170.setAliq2_cofins(null);
                    c170.setVl_cofins(Double.valueOf(cofins.getCOFINSAliq().getVCOFINS()));
                } else if (cofins.getCOFINSNT() != null) {
                    c170.setCst_cofins(cofins.getCOFINSNT().getCST());
                } else if (cofins.getCOFINSOutr() != null) {
                    c170.setCst_cofins(cofins.getCOFINSOutr().getCST());
                    c170.setVl_bc_cofins(Double.valueOf(cofins.getCOFINSOutr().getVBC()));
                    c170.setAliq_cofins(Double.valueOf(cofins.getCOFINSOutr().getPCOFINS()));
                    c170.setAliq2_cofins(null);
                    c170.setQuant_bc_cofins(cofins.getCOFINSOutr().getQBCProd() == null ? 0.00 : Double.valueOf(cofins.getCOFINSOutr().getQBCProd()));
                    c170.setVl_cofins(Double.valueOf(cofins.getCOFINSOutr().getVCOFINS()));
                } else if (cofins.getCOFINSQtde() != null) {
                    c170.setCst_cofins(cofins.getCOFINSQtde().getCST());
                    c170.setAliq_cofins(null);
                    c170.setAliq2_cofins(cofins.getCOFINSQtde().getVAliqProd() == null ? 0.00 : Double.valueOf(cofins.getCOFINSQtde().getVAliqProd()));
                    c170.setQuant_bc_cofins(cofins.getCOFINSQtde().getQBCProd() == null ? 0.00 : Double.valueOf(cofins.getCOFINSQtde().getQBCProd()));
                    c170.setVl_cofins(Double.valueOf(cofins.getCOFINSQtde().getVCOFINS()));
                }
            } catch (Exception e) {
                c170.setCst_cofins("");
            }
        } else {
            c170.setCst_cofins("");
        }

        return c170;
    }

    private List<DadosC190> getDadosC190(TNFe tnfe) {
        // agrupa os valores pela combinacao icms+cfop+aliq
        Map<String, List<DadosC170>> analitico = new HashMap<>();
        for (Det det : tnfe.getInfNFe().getDet()) {
            DadosC170 c170 = getDadosC170(det);
            String chave = c170.getCst_icms() + c170.getCfop() + c170.getAliq_icms();

            List<DadosC170> lista = analitico.get(chave);
            if (lista == null) {
                lista = new ArrayList<>();
                lista.add(c170);
                analitico.put(chave, lista);
            } else {
                lista.add(c170);
            }
        }

        // soma os valores agrupados
        List<DadosC190> lc190 = new ArrayList<>();
        for (Entry<String, List<DadosC170>> entry : analitico.entrySet()) {
            DadosC190 c190 = new DadosC190();
            c190.setVl_ipi(0.00);
            c190.setCod_obs("");
            for (DadosC170 c170 : entry.getValue()) {
                c190.setCst_icms(c170.getCst_icms());
                c190.setCfop(c170.getCfop());
                c190.setAliq_icms(c170.getAliq_icms());
                c190.setVl_opr(c190.getVl_opr() + c170.getVl_item());
                c190.setVl_bc_icms(c190.getVl_bc_icms() + c170.getVl_bc_icms());
                c190.setVl_icms(c190.getVl_icms() + c170.getVl_icms());
                c190.setVl_bc_icms_st(c190.getVl_bc_icms_st() + c170.getVl_bc_icms_st());
                c190.setVl_icms_st(c190.getVl_icms_st() + c170.getVl_icms_st());
            }
            lc190.add(c190);
        }

        return lc190;
    }

    private Entry<Integer, List<DadosC300>> getDadosC300() {
        List<DadosC300> lc300 = new ArrayList<>();
        Integer linhas = 0;

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
            DadosC300 c300 = new DadosC300();
            List<DadosC310> lc310 = new ArrayList<>();
            Map<String, List<EcfNotaProduto>> analitico = new HashMap<>();

            for (EcfNota nota : entry.getValue()) {
                c300.setCod_mod("02");
                c300.setSer(nota.getEcfNotaSerie());
                c300.setSub(nota.getEcfNotaSubserie());
                c300.setDt_doc(nota.getEcfNotaData());
                if (nota.getEcfNotaNumero() < c300.getNum_doc_ini()) {
                    c300.setNum_doc_ini(nota.getEcfNotaNumero());
                }
                if (nota.getEcfNotaNumero() > c300.getNum_doc_fin()) {
                    c300.setNum_doc_fin(nota.getEcfNotaNumero());
                }
                if (!nota.isEcfNotaCancelada()) {
                    c300.setVl_doc(c300.getVl_doc() + nota.getEcfNotaLiquido());
                    c300.setVl_pis(c300.getVl_pis() + nota.getEcfNotaPis());
                    c300.setVl_cofins(c300.getVl_cofins() + nota.getEcfNotaCofins());
                } else {
                    // Registro C310
                    DadosC310 c310 = new DadosC310();
                    c310.setNum_doc_canc(nota.getEcfNotaNumero());
                    lc310.add(c310);
                    linhas++;
                }

                // agrupando os itens por cst+cfop+aliq
                for (EcfNotaProduto np : nota.getEcfNotaProdutos()) {
                    ProdProduto prod = np.getProdProduto();
                    String chave = prod.getProdProdutoCstCson() + (prod.getProdProdutoTributacao() == 'F' ? "5403" : "5102") + prod.getProdProdutoIcms().toString();
                    List<EcfNotaProduto> lista = analitico.get(chave);
                    if (lista == null) {
                        lista = new ArrayList<>();
                        lista.add(np);
                        analitico.put(chave, lista);
                    } else {
                        lista.add(np);
                    }
                }
            }

            // Registro C320
            Entry<Integer, List<DadosC320>> ec320 = getDadosC320(analitico);
            c300.setdC320(ec320.getValue());
            linhas += ec320.getKey();

            c300.setdC310(lc310);
            lc300.add(c300);
            linhas++;
        }

        return new AbstractMap.SimpleEntry(linhas, lc300);
    }

    private Entry<Integer, List<DadosC320>> getDadosC320(Map<String, List<EcfNotaProduto>> analitico) {
        List<DadosC320> lc320 = new ArrayList<>();
        int linhas = 0;

        for (Entry<String, List<EcfNotaProduto>> entry : analitico.entrySet()) {
            DadosC320 c320 = new DadosC320();
            c320.setCod_obs("");
            Map<Integer, List<EcfNotaProduto>> produtos = new HashMap<>();

            for (EcfNotaProduto np : entry.getValue()) {
                ProdProduto prod = np.getProdProduto();
                c320.setCst_icms(prod.getProdProdutoCstCson());
                c320.setCfop(prod.getProdProdutoTributacao() == 'F' ? 5403 : 5102);
                c320.setAliq_icms(np.getEcfNotaProdutoIcms());
                c320.setVl_opr(c320.getVl_opr() + np.getEcfNotaProdutoLiquido());

                // agrupando os produtos pelo id
                List<EcfNotaProduto> lista = produtos.get(prod.getId());
                if (lista == null) {
                    lista = new ArrayList<>();
                    lista.add(np);
                    produtos.put(prod.getId(), lista);
                } else {
                    lista.add(np);
                }
            }
            if (c320.getAliq_icms() > 0) {
                c320.setVl_bc_icms(c320.getVl_opr());
                c320.setVl_icms(c320.getVl_bc_icms() * c320.getAliq_icms() / 100);
            }

            // Registro C321
            List<DadosC321> lc321 = getDadosC321(produtos);
            c320.setdC321(lc321);
            linhas += lc321.size();

            lc320.add(c320);
            linhas++;
        }

        return new AbstractMap.SimpleEntry(linhas, lc320);
    }

    private List<DadosC321> getDadosC321(Map<Integer, List<EcfNotaProduto>> produtos) {
        List<DadosC321> lc321 = new ArrayList<>();

        for (Entry<Integer, List<EcfNotaProduto>> entry : produtos.entrySet()) {
            DadosC321 c321 = new DadosC321();
            for (EcfNotaProduto np : entry.getValue()) {
                c321.setCod_item(entry.getKey() + "");
                c321.setQtd(np.getEcfNotaProdutoQuantidade());
                c321.setUnid(np.getProdEmbalagem().getProdEmbalagemNome());
                c321.setVl_item(c321.getVl_item() + np.getEcfNotaProdutoLiquido());
                c321.setDesc(c321.getDesc() + np.getEcfNotaProdutoDesconto());
                if (np.getProdProduto().getProdProdutoIcms() > 0) {
                    c321.setVl_bc_icms(c321.getVl_item());
                    c321.setVl_icms(c321.getVl_item() * np.getProdProduto().getProdProdutoIcms() / 100);
                }
            }
            c321.setVl_pis(c321.getVl_item() * Double.valueOf(Util.getConfig().get("nfe.pis")) / 100);
            c321.setVl_cofins(c321.getVl_item() * Double.valueOf(Util.getConfig().get("nfe.cofins")) / 100);

            lc321.add(c321);
        }

        return lc321;
    }

    private Entry<Integer, List<DadosC350>> getDadosC350() {
        List<DadosC350> lc350 = new ArrayList<>();
        Integer linhas = 0;

        for (EcfNota nota : notas) {
            if (!nota.isEcfNotaCancelada()) {
                DadosC350 c350 = new DadosC350();
                c350.setSer(nota.getEcfNotaSerie());
                c350.setSub(nota.getEcfNotaSubserie());
                c350.setNum_doc(nota.getEcfNotaNumero());
                c350.setDt_doc(nota.getEcfNotaData());
                c350.setCnpj_cpf(nota.getSisCliente().getSisClienteDoc());
                c350.setVl_merc(nota.getEcfNotaBruto());
                c350.setVl_doc(nota.getEcfNotaLiquido());
                c350.setVl_desc(nota.getEcfNotaDesconto());
                c350.setVl_pis(nota.getEcfNotaPis());
                c350.setVl_cofins(nota.getEcfNotaCofins());

                // Registro C370
                List<DadosC370> lc370 = getDadosC370(nota);
                c350.setdC370(lc370);
                linhas += lc370.size();

                // agrupa os itens pelo cst+cfop+aliq
                Map<String, List<EcfNotaProduto>> analitico = new HashMap<>();
                for (EcfNotaProduto np : nota.getEcfNotaProdutos()) {
                    ProdProduto prod = np.getProdProduto();
                    String chave = prod.getProdProdutoCstCson() + (prod.getProdProdutoTributacao() == 'F' ? "5403" : "5102") + prod.getProdProdutoIcms();
                    List<EcfNotaProduto> lista = analitico.get(chave);
                    if (lista == null) {
                        lista = new ArrayList<>();
                        lista.add(np);
                        analitico.put(chave, lista);
                    } else {
                        lista.add(np);
                    }
                }

                // Registro C390
                List<DadosC390> lc390 = getDadosC390(analitico);
                c350.setdC390(lc390);
                linhas += lc390.size();

                lc350.add(c350);
                linhas++;
            }
        }

        return new AbstractMap.SimpleEntry(linhas, lc350);
    }

    private List<DadosC370> getDadosC370(EcfNota nota) {
        List<DadosC370> lc370 = new ArrayList<>();
        for (EcfNotaProduto np : nota.getEcfNotaProdutos()) {
            DadosC370 c370 = new DadosC370();
            c370.setNum_item(np.getEcfNotaProdutoOrdem());
            c370.setCod_item(np.getProdProduto().getId() + "");
            c370.setQtd(np.getEcfNotaProdutoQuantidade());
            c370.setUnid(np.getProdEmbalagem().getProdEmbalagemNome());
            c370.setVl_item(np.getEcfNotaProdutoBruto());
            c370.setVl_desc(np.getEcfNotaProdutoDesconto());
            lc370.add(c370);
        }
        return lc370;
    }

    private List<DadosC390> getDadosC390(Map<String, List<EcfNotaProduto>> analitico) {
        List<DadosC390> lc390 = new ArrayList<>();

        for (Entry<String, List<EcfNotaProduto>> entry : analitico.entrySet()) {
            DadosC390 c390 = new DadosC390();
            c390.setCod_obs("");
            for (EcfNotaProduto np : entry.getValue()) {
                ProdProduto prod = np.getProdProduto();
                c390.setCst_icms(prod.getProdProdutoCstCson());
                c390.setCfop(prod.getProdProdutoTributacao() == 'F' ? 5403 : 5102);
                c390.setAliq_icms(prod.getProdProdutoIcms());
                c390.setVl_opr(c390.getVl_opr() + np.getEcfNotaProdutoLiquido());
            }
            c390.setVl_bc_icms(c390.getAliq_icms() > 0 ? c390.getVl_opr() : 0.00);
            c390.setVl_icms(c390.getVl_bc_icms() * c390.getAliq_icms() / 100);
            c390.setVl_red_bc(0.00);
            lc390.add(c390);
        }

        return lc390;
    }

    private Entry<Integer, List<DadosC400>> getDadosC400() {
        List<DadosC400> lc400 = new ArrayList<>();
        Integer linhas = 0;

        // Registro C400
        DadosC400 c400 = new DadosC400();
        c400.setCod_mod(imp.getEcfImpressoraCodigo());
        c400.setEcf_mod(imp.getEcfImpressoraModelo());
        c400.setEcf_fab(imp.getEcfImpressoraSerie());
        c400.setEcf_cx(imp.getEcfImpressoraCaixa());

        // Registro C405
        List<DadosC405> lc405 = new ArrayList<>();
        for (EcfZ z : zs) {
            DadosC405 c405 = getDadosC405(z);
            // Registro 410
            c405.setdC410(getDadosC410(z));
            linhas++;
            // Registro C420
            Entry<Integer, List<DadosC420>> ec420 = getDadosC420(z);
            c405.setdC420(ec420.getValue());
            linhas += ec420.getKey();
            // Registro C460
            Entry<Integer, List<DadosC460>> ec460 = getDadosC460(z);
            c405.setdC460(ec460.getValue());
            linhas += ec460.getKey();
            // agrupa os valores pela combinacao icms+cfop+aliq
            Map<String, List<DadosC470>> analitico = new HashMap<>();
            for (DadosC460 c460 : ec460.getValue()) {
                for (DadosC470 c470 : c460.getdC470()) {
                    String chave = c470.getCst_icms() + c470.getCfop() + c470.getAliq_icms();
                    List<DadosC470> lista = analitico.get(chave);
                    if (lista == null) {
                        lista = new ArrayList<>();
                        lista.add(c470);
                        analitico.put(chave, lista);
                    } else {
                        lista.add(c470);
                    }
                }
            }
            // Registro C490
            List<DadosC490> lc490 = getDadosC490(analitico);
            c405.setdC490(lc490);
            linhas += lc490.size();

            lc405.add(c405);
            linhas++;
        }
        c400.setdC405(lc405);

        lc400.add(c400);
        linhas++;
        return new AbstractMap.SimpleEntry(linhas, lc400);
    }

    private DadosC405 getDadosC405(EcfZ z) {
        DadosC405 c405 = new DadosC405();
        c405.setDt_doc(z.getEcfZMovimento());
        c405.setCro(z.getEcfZCro());
        c405.setCrz(z.getEcfZCrz());
        c405.setNum_coo_fin(z.getEcfZCooFin());
        c405.setVl_brt(z.getEcfZBruto());
        c405.setGt_fin(z.getEcfZGt());
        return c405;
    }

    private DadosC410 getDadosC410(EcfZ z) {
        // recupera o valor liquido total vendido no dia
        double liquido = 0.00;
        for (EcfZTotais t : z.getEcfZTotais()) {
            if (!t.getEcfZTotaisCodigo().equals("OPNF") && !t.getEcfZTotaisCodigo().startsWith("D") && !t.getEcfZTotaisCodigo().startsWith("C")) {
                liquido += t.getEcfZTotaisValor();
            }
        }

        DadosC410 c410 = new DadosC410();
        c410.setVl_pis(liquido * Double.valueOf(Util.getConfig().get("nfe.pis")) / 100);
        c410.setVl_cofins(liquido * Double.valueOf(Util.getConfig().get("nfe.cofins")) / 100);
        return c410;
    }

    private Entry<Integer, List<DadosC420>> getDadosC420(EcfZ z) {
        List<DadosC420> lc420 = new ArrayList<>();
        int linhas = 0;

        for (EcfZTotais t : z.getEcfZTotais()) {
            if (t.getEcfZTotaisValor() > 0) {
                DadosC420 c420 = new DadosC420();
                c420.setCod_tot_par(t.getEcfZTotaisCodigo());
                c420.setVlr_acum_tot(t.getEcfZTotaisValor());
                if (t.getEcfZTotaisCodigo().length() == 7) {
                    c420.setNr_tot(t.getEcfZTotaisCodigo().substring(0, 2));
                    c420.setDescr_nr_tot("Tributado");
                }
                // Registro C425, somente para perfil B
                if (Util.getConfig().get("sped.perfil").equals("B")) {
                    List<DadosC425> lc425 = getDadosC425(z);
                    c420.setdC425(lc425);
                    linhas += lc425.size();
                }

                lc420.add(c420);
            }
        }

        linhas += lc420.size();
        return new AbstractMap.SimpleEntry(linhas, lc420);
    }

    private List<DadosC425> getDadosC425(EcfZ z) {
        Map<Integer, EcfVendaProduto> m425 = new HashMap<>();

        // agrupando os valores
        for (EcfVenda venda : z.getEcfVendas()) {
            if (!venda.getEcfVendaCancelada()) {
                for (EcfVendaProduto prodVenda : venda.getEcfVendaProdutos()) {
                    EcfVendaProduto vp = m425.get(prodVenda.getProdProduto().getProdProdutoId());
                    if (vp == null) {
                        m425.put(prodVenda.getProdProduto().getProdProdutoId(), prodVenda);
                    } else {
                        vp.setEcfVendaProdutoQuantidade(vp.getEcfVendaProdutoQuantidade() + prodVenda.getEcfVendaProdutoQuantidade());
                        vp.setEcfVendaProdutoTotal(vp.getEcfVendaProdutoTotal() + prodVenda.getEcfVendaProdutoTotal());
                    }
                }
            }
        }

        // montando a lista de registros
        List<DadosC425> lc425 = new ArrayList<>();
        for (EcfZTotais tot : z.getEcfZTotais()) {
            // itens que compoem este total
            char trib;
            switch (tot.getEcfZTotaisCodigo()) {
                case "F1":
                    trib = 'F';
                    break;
                case "I1":
                    trib = 'I';
                    break;
                case "N1":
                    trib = 'N';
                    break;
                default:
                    trib = 'T';
                    break;
            }

            for (EcfVendaProduto vp : m425.values()) {
                if (vp.getProdProduto().getProdProdutoTributacao() == trib) {
                    DadosC425 c425 = new DadosC425();
                    c425.setCod_item(vp.getProdProduto().getProdProdutoId() + "");
                    c425.setQtd(vp.getEcfVendaProdutoQuantidade());
                    c425.setUnid(vp.getProdEmbalagem().getProdEmbalagemNome());
                    c425.setVl_item(vp.getEcfVendaProdutoTotal());
                    c425.setVl_pis(vp.getEcfVendaProdutoTotal() * Double.valueOf(Util.getConfig().get("nfe.pis")) / 100);
                    c425.setVl_cofins(vp.getEcfVendaProdutoTotal() * Double.valueOf(Util.getConfig().get("nfe.cofins")) / 100);
                    lc425.add(c425);
                }
            }
        }

        return lc425;
    }

    private Entry<Integer, List<DadosC460>> getDadosC460(EcfZ z) {
        List<DadosC460> lc460 = new ArrayList<>();
        int linhas = 0;

        for (EcfVenda venda : z.getEcfVendas()) {
            DadosC460 c460 = new DadosC460();
            c460.setCod_mod(z.getEcfImpressora().getEcfImpressoraCodigo());
            c460.setCod_sit(venda.getEcfVendaCancelada() ? "02" : "00");
            c460.setNum_doc(venda.getEcfVendaCoo());
            if (!venda.getEcfVendaCancelada()) {
                c460.setDt_doc(venda.getEcfVendaData());
                c460.setVl_doc(venda.getEcfVendaLiquido());
                c460.setVl_pis(venda.getEcfVendaLiquido() * Double.valueOf(Util.getConfig().get("nfe.pis")) / 100);
                c460.setVl_cofins(venda.getEcfVendaLiquido() * Double.valueOf(Util.getConfig().get("nfe.cofins")) / 100);
                if (venda.getSisCliente() != null) {
                    c460.setCpf_cnpj(venda.getSisCliente().getSisClienteDoc());
                    c460.setNom_adq(venda.getSisCliente().getSisClienteNome());
                }
                // Registro C470
                List<DadosC470> lc470 = getDadosC470(venda);
                c460.setdC470(lc470);
                linhas += lc470.size();
            } else {
                c460.setdC470(new ArrayList<DadosC470>());
            }

            lc460.add(c460);
            linhas++;
        }

        return new AbstractMap.SimpleEntry(linhas, lc460);
    }

    private List<DadosC470> getDadosC470(EcfVenda venda) {
        List<DadosC470> lc470 = new ArrayList<>();
        for (EcfVendaProduto vp : venda.getEcfVendaProdutos()) {
            ProdProduto prod = vp.getProdProduto();
            DadosC470 c470 = new DadosC470();
            c470.setCod_item(prod.getProdProdutoId() + "");
            c470.setQtd(vp.getEcfVendaProdutoQuantidade());
            c470.setUnid(vp.getProdEmbalagem().getProdEmbalagemNome());
            c470.setCfop(vp.getEcfVendaProdutoTributacao() == 'F' ? 5403 : 5102);
            c470.setVl_item(vp.getEcfVendaProdutoTotal());
            c470.setCst_icms(vp.getEcfVendaProdutoCstCson().length() == 2 ? prod.getProdProdutoOrigem() + vp.getEcfVendaProdutoCstCson() : vp.getEcfVendaProdutoCstCson());
            c470.setAliq_icms(vp.getEcfVendaProdutoIcms());
            lc470.add(c470);
        }
        return lc470;
    }

    private List<DadosC490> getDadosC490(Map<String, List<DadosC470>> analitico) {
        List<DadosC490> lc490 = new ArrayList<>();
        for (Entry<String, List<DadosC470>> entry : analitico.entrySet()) {
            DadosC490 c490 = new DadosC490();
            for (DadosC470 c470 : entry.getValue()) {
                c490.setCst_icms(c470.getCst_icms());
                c490.setCfop(c470.getCfop());
                c490.setAliq_icms(c470.getAliq_icms());
                c490.setVl_opr(c490.getVl_opr() + c470.getVl_item());
            }
            c490.setVl_bc_icms(c490.getAliq_icms() > 0 ? c490.getVl_opr() : 0.00);
            c490.setVl_icms(c490.getVl_bc_icms() * c490.getAliq_icms() / 100);
            c490.setCod_obs("");
            lc490.add(c490);
        }
        return lc490;
    }

    private BlocoD getBlocoD() {
        BlocoD blD = new BlocoD();
        // Registro D001
        DadosD001 d1 = new DadosD001();
        d1.setInd_mov(1);
        blD.setdD001(d1);
        // Registro D990
        DadosD990 d990 = new DadosD990();
        d990.setQtd_lin(2);
        blD.setdD990(d990);
        return blD;
    }

    private BlocoE getBlocoE() {
        BlocoE blE = new BlocoE();
        // Registro E001
        DadosE001 e1 = new DadosE001();
        e1.setInd_mov(1);
        blE.setdE001(e1);
        // Registro E990
        DadosE990 e990 = new DadosE990();
        e990.setQtd_lin(2);
        blE.setdE990(e990);
        return blE;
    }

    private BlocoG getBlocoG() {
        BlocoG blG = new BlocoG();
        // Registro G001
        DadosG001 g1 = new DadosG001();
        g1.setInd_mov(1);
        blG.setdG001(g1);
        // Registro G990
        DadosG990 g990 = new DadosG990();
        g990.setQtd_lin(2);
        blG.setdG990(g990);
        return blG;
    }

    private BlocoH getBlocoH() {
        int linhas = 0;
        double total = 0.00;
        BlocoH blH = new BlocoH();

        // Registro H001
        DadosH001 dh1 = new DadosH001();
        dh1.setInd_mov(estoque.isEmpty() ? 1 : 0);
        blH.setdH001(dh1);
        linhas++;

        if (estoque.size() > 0) {
            // Registro H010
            List<DadosH010> lh10 = new ArrayList<>();
            for (ProdProduto prod : estoque) {
                DadosH010 dh10 = getDadosH10(prod);
                lh10.add(dh10);
                total += dh10.getVl_item();
            }
            blH.setdH010(lh10);
            linhas += lh10.size();
            // Registro H005
            DadosH005 dh5 = new DadosH005();
            dh5.setDt_inv(fim);
            dh5.setVl_inv(total);
            dh5.setMot_inv("01");
            blH.setdH005(dh5);
            linhas++;
        }

        // Registro H990
        DadosH990 dh990 = new DadosH990();
        dh990.setQtd_lin(linhas++);
        blH.setdH990(dh990);
        return blH;
    }

    private DadosH010 getDadosH10(ProdProduto prod) {
        DadosH010 dh10 = new DadosH010();
        dh10.setCod_item(prod.getProdProdutoId() + "");
        dh10.setUnid(prod.getProdEmbalagem().getProdEmbalagemNome());
        dh10.setQtd(prod.getProdProdutoEstoque());
        dh10.setVl_unit(prod.getProdProdutoPreco());
        dh10.setVl_item(prod.getProdProdutoEstoque() * prod.getProdProdutoPreco());
        dh10.setInd_prop("0");
        dh10.setCod_part("");
        dh10.setTxt_compl("");
        dh10.setCod_cta("ESTOQUE");
        Util.normaliza(dh10);
        return dh10;
    }

    private Bloco1 getBloco1() {
        Bloco1 bl1 = new Bloco1();
        // Registro 1001
        Dados1001 _1 = new Dados1001();
        _1.setInd_mov(0);
        bl1.setD1001(_1);
        // Registro 1010
        Dados1010 _10 = new Dados1010();
        _10.setInd_exp("N");
        _10.setInd_ccrf("N");
        _10.setInd_comb("N");
        _10.setInd_usina("N");
        _10.setInd_va("N");
        _10.setInd_ee("N");
        _10.setInd_cart("N");
        _10.setInd_form("N");
        _10.setInd_aer("N");
        bl1.setD1010(_10);
        // Registro 1990
        Dados1990 _1990 = new Dados1990();
        _1990.setQtd_lin(3);
        bl1.setD1990(_1990);
        return bl1;
    }
}
