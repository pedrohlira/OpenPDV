package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.Conexao;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.modelo.Ibpt;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.Filtro;
import br.com.openpdv.modelo.core.filtro.FiltroGrupo;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.core.filtro.FiltroTexto;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import br.com.openpdv.modelo.ecf.EcfNotaProduto;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.Util;
import br.com.phdss.controlador.PAF;
import br.inf.portalfiscal.nfe.schema.nfe.TEnderEmi;
import br.inf.portalfiscal.nfe.schema.nfe.TEndereco;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Dest;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.COFINS;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS00;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS30;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS40;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS60;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS90;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.PIS;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.PIS.PISAliq;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Imposto.PIS.PISOutr;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Det.Prod;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Emit;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Ide;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.InfAdic;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Total;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Total.ICMSTot;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe.InfNFe.Transp;
import br.inf.portalfiscal.nfe.schema.nfe.TUf;
import br.inf.portalfiscal.nfe.schema.nfe.TUfEmi;
import com.sun.jersey.api.client.WebResource;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.apache.log4j.Logger;

/**
 * Classe que realiza a acao de gerar o XML da NFe.
 *
 * @author Pedro H. Lira
 */
public class ComandoGerarNFe implements IComando {

    private Logger log;
    private CoreService servico;
    private Date data;
    private SisEmpresa empresa;
    private SisCliente cliente;
    private List<EcfNotaProduto> produtos;
    private String cNF;
    private String cDV;
    private String nNF;
    private String chave;
    private double valorProd;
    private double baseICMS;
    private double baseST;
    private double valorICMS;
    private double valorST;
    private double valorPis;
    private double valorCofins;
    private JAXBElement<TNFe> element;

    /**
     * Construtor padrao com os parametros necessarios.
     *
     * @param cliente os dados do cliente receptor.
     * @param produtos os dados dos produtos vendidos.
     */
    public ComandoGerarNFe(SisCliente cliente, List<EcfNotaProduto> produtos) {
        log = Logger.getLogger(ComandoGerarNFe.class);
        this.servico = new CoreService();
        this.data = new Date();
        this.empresa = Caixa.getInstancia().getEmpresa();
        this.cliente = cliente;
        this.produtos = produtos;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            // numero
            getNumero();
            // chave
            getChaveAcesso();
            // informacoes da NFe
            InfNFe infNFe = new InfNFe();
            infNFe.setId("NFe" + chave);
            infNFe.setVersao(Util.getConfig().getProperty("nfe.versao"));
            // no do ide
            infNFe.setIde(getIde());
            // no do emissor
            infNFe.setEmit(getEmissor());
            // no do destinatario
            infNFe.setDest(getDestinatario());
            // produtos
            getProdutos(infNFe.getDet());
            // totais
            infNFe.setTotal(getTotais());
            // transporte (sem frete)
            Transp transp = new Transp();
            transp.setModFrete("9");
            infNFe.setTransp(transp);
            // informacoes
            infNFe.setInfAdic(getInformacoes());
            // finaliza
            TNFe nfe = new TNFe();
            nfe.setInfNFe(infNFe);

            element = new br.inf.portalfiscal.nfe.schema.nfe.ObjectFactory().createNFe(nfe);
        } catch (OpenPdvException ex) {
            log.error("Erro na montagem do xml.", ex);
            throw new OpenPdvException(ex);
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    /**
     * Metodo que retorna o xml da nota gerada.
     *
     * @return uma objeto contendo um xml ou null se teve erro na geracao.
     */
    public JAXBElement<TNFe> getElement() {
        return element;
    }

    /**
     * Metodo que gera o numero da NF adicionando 1 ao ultimo numero gerado.
     *
     * @throws Exception dispara em caso de errro.
     */
    private void getNumero() throws OpenPdvException {
        String numero = "";
        if (Util.getConfig().getProperty("sinc.servidor").endsWith("localhost")) {
            List<EcfNotaEletronica> nfes = servico.selecionar(new EcfNotaEletronica(), 0, 1, null);
            if (nfes != null && nfes.size() == 1) {
                numero += nfes.get(0).getEcfNotaEletronicaNumero() + 1;
            } else {
                numero += Integer.valueOf(Util.getConfig().getProperty("nfe.numero")) + 1;
            }
        } else {
            WebResource wr = Conexao.getRest(Util.getConfig().getProperty("sinc.host") + "/nfe");
            numero += wr.accept(MediaType.TEXT_PLAIN_TYPE).get(String.class);
        }

        nNF = Util.formataNumero(numero, 9, 0, false);
    }

    /**
     * Metodo que gera a chave unica da NFe.
     */
    private void getChaveAcesso() {
        StringBuilder sb = new StringBuilder();
        // uf
        sb.append(empresa.getSisMunicipio().getSisEstado().getSisEstadoIbge());
        // data
        sb.append(Util.formataData(data, "yyMM"));
        // cnpj
        sb.append(empresa.getSisEmpresaCnpj().replaceAll("\\D", ""));
        // / modo
        sb.append("55");
        // serie
        sb.append(Util.formataNumero(Util.getConfig().getProperty("nfe.serie"), 3, 0, false));
        // numero nf
        sb.append(nNF);
        // tipo emissao
        sb.append("1");
        // codigo nfe
        cNF = (data.getTime() + "").substring(0, 8);
        sb.append(cNF);
        // dv nfe 1 digito, por 9 casas
        cDV = modulo11(sb.toString(), 1, 9);
        sb.append(cDV);
        // coloca no config
        chave = sb.toString();
    }

    /**
     * Metodo que gera os dados de identificacao da NFe.
     *
     * @return o objeto requerido.
     */
    private Ide getIde() {
        Ide ide = new Ide();
        // uf
        ide.setCUF(empresa.getSisMunicipio().getSisEstado().getSisEstadoIbge() + "");
        // numero fiscal
        ide.setCNF(cNF);
        // natureza
        ide.setNatOp("VENDA DE MERCADORIAS");
        // forma pagamento
        ide.setIndPag("0");
        // modo
        ide.setMod("55");
        // serie
        ide.setSerie(Util.getConfig().getProperty("nfe.serie"));
        // numero nf
        ide.setNNF(Integer.valueOf(nNF) + "");
        // data emissao
        ide.setDhEmi(Util.formataData(data, "yyyy-MM-dd"));
        // data saida
        ide.setDhSaiEnt(Util.formataData(data, "yyyy-MM-dd"));
        // operacao
        ide.setTpNF("1");
        // municipio
        ide.setCMunFG(empresa.getSisMunicipio().getSisMunicipioIbge() + "");
        // impressao
        ide.setTpImp("1");
        // emissao
        ide.setTpEmis("1");
        // verificador
        ide.setCDV(cDV);
        // ambiente
        ide.setTpAmb(Util.getConfig().getProperty("nfe.tipoamb"));
        // finalidade
        ide.setFinNFe("1");
        // processo emissao
        ide.setProcEmi("0");
        // versao processo
        ide.setVerProc("OPENPDV");

        return ide;
    }

    /**
     * Metodo que gera os dados do emissor da NFe.
     *
     * @return o objeto requerido.
     */
    private Emit getEmissor() {
        Emit emit = new Emit();
        // crt
        emit.setCRT(Util.getConfig().getProperty("nfe.crt"));
        // empresa
        emit.setCNPJ(empresa.getSisEmpresaCnpj().replaceAll("\\D", ""));
        String razao = empresa.getSisEmpresaRazao();
        razao = razao.length() > 60 ? razao.substring(0, 60) : razao;
        emit.setXNome(razao);
        String fantasia = empresa.getSisEmpresaFantasia();
        fantasia = fantasia.length() > 15 ? fantasia.substring(0, 15) : fantasia;
        emit.setXFant(fantasia);
        emit.setIE(empresa.getSisEmpresaIe().replaceAll("\\D", ""));
        // endereco
        TEnderEmi enderEmit = new TEnderEmi();
        enderEmit.setXLgr(empresa.getSisEmpresaLogradouro());
        enderEmit.setNro(empresa.getSisEmpresaNumero() + "");
        if (!empresa.getSisEmpresaComplemento().equals("")) {
            enderEmit.setXCpl(empresa.getSisEmpresaComplemento());
        }
        enderEmit.setXBairro(empresa.getSisEmpresaBairro());
        enderEmit.setCMun(empresa.getSisMunicipio().getSisMunicipioIbge() + "");
        enderEmit.setXMun(empresa.getSisMunicipio().getSisMunicipioDescricao());
        enderEmit.setUF(TUfEmi.valueOf(empresa.getSisMunicipio().getSisEstado().getSisEstadoSigla()));
        enderEmit.setCEP(empresa.getSisEmpresaCep().replaceAll("\\D", ""));
        enderEmit.setCPais("1058");
        enderEmit.setXPais("BRASIL");
        if (!empresa.getSisEmpresaFone().equals("")) {
            enderEmit.setFone(empresa.getSisEmpresaFone().replaceAll("\\D", ""));
        }
        emit.setEnderEmit(enderEmit);

        return emit;
    }

    /**
     * Metodo que gera os dados do destinatario da NFe.
     *
     * @return o objeto requerido.
     */
    private Dest getDestinatario() {
        Dest dest = new Dest();
        String nome = cliente.getSisClienteNome().trim();
        nome = nome.length() > 60 ? nome.substring(0, 60) : nome;

        // empresa
        if (Util.getConfig().getProperty("nfe.tipoamb").equals("2")) {
            dest.setCNPJ("99999999000191");
            dest.setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
            dest.setIE("");
        } else if (cliente.getSisClienteDoc().length() == 18) {
            dest.setCNPJ(cliente.getSisClienteDoc().replaceAll("\\D", ""));
            dest.setXNome(nome);
            dest.setIE(cliente.getSisClienteDoc1().replaceAll("\\D", ""));
        } else {
            dest.setCPF(cliente.getSisClienteDoc().replaceAll("\\D", ""));
            dest.setXNome(nome);
            dest.setIE("");
        }
        // endereco
        TEndereco enderDest = new TEndereco();
        enderDest.setXLgr(cliente.getSisClienteEndereco().trim());
        enderDest.setNro(cliente.getSisClienteNumero() + "");
        enderDest.setXBairro(cliente.getSisClienteBairro().trim());
        enderDest.setCMun(cliente.getSisMunicipio().getSisMunicipioIbge() + "");
        enderDest.setXMun(cliente.getSisMunicipio().getSisMunicipioDescricao());
        enderDest.setUF(TUf.valueOf(cliente.getSisMunicipio().getSisEstado().getSisEstadoSigla()));
        enderDest.setCEP(cliente.getSisClienteCep().replaceAll("\\D", ""));
        enderDest.setCPais("1058");
        enderDest.setXPais("BRASIL");
        if (!cliente.getSisClienteTelefone().replaceAll("\\D", "").equals("")) {
            enderDest.setFone(cliente.getSisClienteTelefone().replaceAll("\\D", ""));
        }
        dest.setEnderDest(enderDest);
        if (!cliente.getSisClienteEmail().equals("")) {
            dest.setEmail(cliente.getSisClienteEmail().toLowerCase().trim());
        }
        return dest;
    }

    /**
     * Metodo que adiciona os produtos na NFe.
     *
     * @param dets uma lista que sera preenchida.
     */
    private void getProdutos(List<Det> dets) {
        int i = 1;
        for (EcfNotaProduto np : produtos) {
            ProdProduto pp = np.getProdProduto();

            // setando o item
            Det det = new Det();
            det.setNItem((i++) + "");
            // cod produto
            Prod prod = new Prod();
            if (pp.getProdProdutoBarra() == null) {
                prod.setCProd(Util.formataNumero(pp.getProdProdutoId(), 6, 0, false));
            } else {
                prod.setCProd(pp.getProdProdutoBarra());
            }
            // barra
            prod.setCEAN(np.getEcfVendaProdutoCodigo());
            // descricao
            prod.setXProd(pp.getProdProdutoDescricao().trim());
            // ncm
            prod.setNCM(pp.getProdProdutoNcm());
            // cfop
            prod.setCFOP(pp.getProdProdutoTributacao() == 'F' ? "5403" : "5102");
            // unidade
            prod.setUCom(pp.getProdEmbalagem().getProdEmbalagemNome());
            // quantidde
            prod.setQCom(getValorNfe(np.getEcfNotaProdutoQuantidade(), 4));
            // valor unitario
            prod.setVUnCom(getValorNfe(np.getEcfNotaProdutoLiquido(), 4));
            // valor produto
            double totalLiquido = np.getEcfNotaProdutoQuantidade() * np.getEcfNotaProdutoLiquido();
            String strProd = getValorNfe(totalLiquido, 2);
            valorProd += Double.valueOf(strProd);
            prod.setVProd(strProd);
            // barra do tributo
            prod.setCEANTrib(np.getEcfVendaProdutoCodigo());
            // unidade do tributo
            prod.setUTrib(pp.getProdEmbalagem().getProdEmbalagemNome());
            // quantidde do tributo
            prod.setQTrib(getValorNfe(np.getEcfNotaProdutoQuantidade(), 4));
            // valor unitario
            prod.setVUnTrib(getValorNfe(np.getEcfNotaProdutoLiquido(), 4));
            // total da NF
            prod.setIndTot("1");
            // setando o produto
            det.setProd(prod);
            // setando os impostos
            det.setImposto(getImposto(np, totalLiquido));
            // adiciona a lista
            dets.add(det);
        }
    }

    /**
     * Metodo que faz os calculos de impostos por produto.
     *
     * @param np o objeto de venda de produto.
     * @param totalLiquido o valor total liquido deste produto.
     * @return o objeto requerido.
     */
    private Imposto getImposto(EcfNotaProduto np, double totalLiquido) {
        Imposto imposto = new Imposto();
        ICMS icms;
        // icms
        if (Util.getConfig().getProperty("nfe.crt").equals("1")) {
            icms = getSimples(np, totalLiquido);
        } else {
            icms = getNormal(np, totalLiquido);
        }
        JAXBElement<ICMS> icmsElement = new JAXBElement<>(new QName("ICMS"), ICMS.class, icms);
        imposto.getContent().add(icmsElement);
        // pis
        JAXBElement<PIS> pisElement = new JAXBElement<>(new QName("PIS"), PIS.class, getPIS(totalLiquido));
        imposto.getContent().add(pisElement);
        // confins
        JAXBElement<COFINS> cofinsElement = new JAXBElement<>(new QName("COFINS"), COFINS.class, getCOFINS(totalLiquido));
        imposto.getContent().add(cofinsElement);

        return imposto;
    }

    /**
     * Metodo que faz o calculo de icms por produto para empresa do simples.
     *
     * @param np o objeto de venda de produto.
     * @param totalLiquido o valor total liquido deste produto.
     * @return o objeto requerido.
     */
    private ICMS getSimples(EcfNotaProduto np, double totalLiquido) {
        ICMS icms = new ICMS();
        String cson = np.getProdProduto().getProdProdutoCstCson();
        String origem = np.getProdProduto().getProdProdutoOrigem() + "";

        switch (cson) {
            case "101":
                ICMSSN101 icmssn101 = new ICMSSN101();
                icmssn101.setOrig(origem);
                icmssn101.setCSOSN(cson);
                double porcento = Double.valueOf(Util.getConfig().getProperty("nfe.cson"));
                icmssn101.setPCredSN(getValorNfe(porcento, 2));
                double valor = totalLiquido * porcento / 100;
                icmssn101.setVCredICMSSN(getValorNfe(valor, 2));
                icms.setICMSSN101(icmssn101);
                break;
            case "102":
                ICMSSN102 icmssn102 = new ICMSSN102();
                icmssn102.setOrig(origem);
                icmssn102.setCSOSN(cson);
                icms.setICMSSN102(icmssn102);
                break;
            case "201":
                ICMSSN201 icmssn201 = new ICMSSN201();
                icmssn201.setOrig(origem);
                icmssn201.setCSOSN(cson);
                icmssn201.setModBCST(Util.getConfig().getProperty("nfe.modocalcst"));
                icmssn201.setVBCST("0.00");
                icmssn201.setPICMSST("0.00");
                icmssn201.setVICMSST("0.00");
                icmssn201.setPCredSN("0.00");
                icmssn201.setVCredICMSSN("0.00");
                icms.setICMSSN201(icmssn201);
                break;
            case "202":
                ICMSSN202 icmssn202 = new ICMSSN202();
                icmssn202.setOrig(origem);
                icmssn202.setCSOSN(cson);
                icmssn202.setModBCST(Util.getConfig().getProperty("nfe.modocalcst"));
                icmssn202.setVBCST("0.00");
                icmssn202.setPICMSST("0.00");
                icmssn202.setVICMSST("0.00");
                icms.setICMSSN202(icmssn202);
                break;
            case "500":
                ICMSSN500 icmssn500 = new ICMSSN500();
                icmssn500.setOrig(origem);
                icmssn500.setCSOSN(cson);
                icmssn500.setVBCSTRet("0.00");
                icmssn500.setVICMSSTRet("0.00");
                icms.setICMSSN500(icmssn500);
                break;
            default:
                ICMSSN900 icmssn900 = new ICMSSN900();
                icmssn900.setOrig(origem);
                icmssn900.setCSOSN(cson);
                icms.setICMSSN900(icmssn900);
                break;
        }

        return icms;
    }

    /**
     * Metodo que faz o calculo de icms por produto para empresa normal.
     *
     * @param np o objeto de venda de produto.
     * @param totalLiquido o valor total liquido deste produto.
     * @return o objeto requerido.
     */
    private ICMS getNormal(EcfNotaProduto np, double totalLiquido) {
        ICMS icms = new ICMS();
        String cst = np.getProdProduto().getProdProdutoCstCson();
        String origem = np.getProdProduto().getProdProdutoOrigem() + "";

        // se é 10 e muda pra 60
        switch (cst) {
            case "10":
                cst = "60";
                break;
            case "41":
                cst = "40";
                break;
        }

        // modo base calculo normal
        switch (cst) {
            case "00":
                ICMS00 icms00 = new ICMS00();
                icms00.setOrig(origem);
                icms00.setCST(cst);
                icms00.setModBC(Util.getConfig().getProperty("nfe.modocalc"));
                // valor da base de calculo
                double porcento = np.getProdProduto().getProdProdutoIcms();
                String strBase = porcento == 0.00 ? "0.00" : getValorNfe(totalLiquido, 2);
                double base = Double.valueOf(strBase);
                icms00.setVBC(strBase);
                icms00.setPICMS(getValorNfe(porcento, 2));
                // valor icms
                String strValor = getValorNfe(base * porcento / 100, 2);
                double valor = Double.valueOf(strValor);
                icms00.setVICMS(strValor);
                icms.setICMS00(icms00);
                // executa a soma dos impostos
                baseICMS += base;
                valorICMS += valor;
                break;
            case "30":
                ICMS30 icms30 = new ICMS30();
                icms30.setOrig(origem);
                icms30.setCST(cst);
                icms30.setModBCST(Util.getConfig().getProperty("nfe.modocalcst"));
                icms30.setVBCST("0.00");
                icms30.setPICMSST("0.00");
                icms30.setVICMSST("0.00");
                icms.setICMS30(icms30);
                break;
            case "40":
                ICMS40 icms40 = new ICMS40();
                icms40.setOrig(origem);
                icms40.setCST(cst);
                icms.setICMS40(icms40);
                break;
            case "60":
                ICMS60 icms60 = new ICMS60();
                icms60.setOrig(origem);
                icms60.setCST(cst);
                icms60.setVBCSTRet("0.00");
                icms60.setVICMSSTRet("0.00");
                icms.setICMS60(icms60);
                break;
            default:
                ICMS90 icms90 = new ICMS90();
                icms90.setOrig(origem);
                icms90.setCST(cst);
                icms.setICMS90(icms90);
                break;
        }

        return icms;
    }

    /**
     * Metodo que faz o calculo de pis por produto.
     *
     * @param totalLiquido o valor total liquido deste produto.
     * @return o objeto requerido.
     */
    private PIS getPIS(double totalLiquido) {
        PIS pis = new PIS();
        // faz o calculo do valor e define
        double porcento = Double.valueOf(Util.getConfig().getProperty("nfe.pis"));
        double valor = totalLiquido * porcento / 100;
        String strValor = getValorNfe(valor, 2);
        valorPis += Double.valueOf(strValor);

        // isento ou simples nacional
        if (porcento == 0.00) {
            PISOutr outr = new PISOutr();
            outr.setCST("99");
            outr.setVBC("0.00");
            outr.setPPIS("0.00");
            outr.setVPIS("0.00");
            pis.setPISOutr(outr);
        } else {
            PISAliq aliq = new PISAliq();
            aliq.setCST("01");
            aliq.setVBC(getValorNfe(totalLiquido, 2));
            aliq.setPPIS(getValorNfe(porcento, 2));
            aliq.setVPIS(strValor);
            pis.setPISAliq(aliq);
        }

        return pis;
    }

    /**
     * Metodo que faz o calculo de cofins por produto.
     *
     * @param totalLiquido o valor total liquido deste produto.
     * @return o objeto requerido.
     */
    private COFINS getCOFINS(double totalLiquido) {
        COFINS cofins = new COFINS();
        // faz o calculo do valor e define
        double porcento = Double.valueOf(Util.getConfig().getProperty("nfe.cofins"));
        double valor = totalLiquido * porcento / 100;
        String strValor = getValorNfe(valor, 2);
        valorCofins += Double.valueOf(strValor);

        // isento ou simples nacional
        if (porcento == 0.00) {
            COFINSOutr outr = new COFINSOutr();
            outr.setCST("99");
            outr.setVBC("0.00");
            outr.setPCOFINS("0.00");
            outr.setVCOFINS("0.00");
            cofins.setCOFINSOutr(outr);
        } else {
            COFINSAliq aliq = new COFINSAliq();
            aliq.setCST("01");
            aliq.setVBC(getValorNfe(totalLiquido, 2));
            aliq.setPCOFINS(getValorNfe(porcento, 2));
            aliq.setVCOFINS(strValor);
            cofins.setCOFINSAliq(aliq);
        }

        return cofins;
    }

    /**
     * Metodo que faz o calculo de valores totais de impostos.
     *
     * @return o objeto requerido.
     */
    private Total getTotais() {
        Total total = new Total();
        ICMSTot icmstot = new ICMSTot();
        icmstot.setVBC(getValorNfe(baseICMS, 2));
        icmstot.setVICMS(getValorNfe(valorICMS, 2));
        icmstot.setVBCST(getValorNfe(baseST, 2));
        icmstot.setVST(getValorNfe(valorST, 2));
        icmstot.setVProd(getValorNfe(valorProd, 2));
        icmstot.setVFrete("0.00");
        icmstot.setVSeg("0.00");
        icmstot.setVDesc("0.00");
        icmstot.setVII("0.00");
        icmstot.setVIPI("0.00");
        icmstot.setVPIS(getValorNfe(valorPis, 2));
        icmstot.setVCOFINS(getValorNfe(valorCofins, 2));
        icmstot.setVOutro("0.00");
        icmstot.setVNF(getValorNfe(valorProd, 2));
        total.setICMSTot(icmstot);
        return total;
    }

    /**
     * Metodo que gerar os dados adicionais da NFe.
     *
     * @return o objeto requerido.
     */
    private InfAdic getInformacoes() {
        StringBuilder sb = new StringBuilder();
        // adiciona o MD5 do auxiliar.txt
        sb.append("MD5: ").append(PAF.AUXILIAR.getProperty("out.autenticado"));
        // uma mensagem padrao se precisar
        if (Util.getConfig().getProperty("nfe.info") != null) {
            sb.append("#").append(Util.getConfig().getProperty("nfe.info"));
        }
        // caso a opcao de mostrar os valores de impostos esteja ativa
        boolean mostraIbpt = Boolean.valueOf(Util.getConfig().getProperty("nfe.ibpt"));
        if (mostraIbpt) {
            double impostos = 0.00;

            for (EcfNotaProduto np : produtos) {
                FiltroTexto ft = new FiltroTexto("ibptCodigo", ECompara.IGUAL, np.getProdProduto().getProdProdutoNcm());
                FiltroNumero fn = new FiltroNumero("ibptTabela", ECompara.IGUAL, np.getProdProduto().getProdProdutoTipo().equals("09") ? 1 : 0);
                FiltroGrupo gf = new FiltroGrupo(Filtro.E, ft, fn);
                Ibpt ibpt;
                try {
                    ibpt = (Ibpt) servico.selecionar(new Ibpt(), gf);
                } catch (OpenPdvException ex) {
                    ibpt = null;
                }

                if (ibpt != null) {
                    char ori = np.getProdProduto().getProdProdutoOrigem();
                    double taxa = (ori == '0' || ori == '3' || ori == '4' || ori == '5') ? ibpt.getIbptAliqNac() : ibpt.getIbptAliqImp();
                    impostos += np.getEcfNotaProdutoLiquido() * taxa / 100;
                }
            }
            double porcentagem = impostos / valorProd * 100;
            sb.append("#Valor Aproximado dos Tributos R$ ");
            sb.append(Util.formataNumero(impostos, 1, 2, false)).append(" [");
            sb.append(Util.formataNumero(porcentagem, 1, 2, false)).append("%] Fonte: IBPT");
        }

        InfAdic inf = new InfAdic();
        inf.setInfCpl(sb.toString());
        return inf;
    }

    /**
     * Metodo que formata o valor numerico de acordo com o desejado pela NFe.
     *
     * @param valor o valor a ser formatado.
     * @param decimal a quantidade de casas decimais.
     * @return um texto com o valor formatado.
     */
    private String getValorNfe(double valor, int decimal) {
        return Util.formataNumero(valor, 1, decimal, false).replace(",", ".");
    }

    /**
     * Metodo que faz o cálculo de modulo 11.
     *
     * @param fonte o numero a ser usado para calculo.
     * @param dig quantos digitos de retorno, 1 ou 2.
     * @param limite quantos digitos usados para multiplicacao.
     * @return o dv do fonte.
     */
    private String modulo11(String fonte, int dig, int limite) {
        for (int n = 1; n <= dig; n++) {
            int soma = 0;
            int mult = 2;

            for (int i = fonte.length() - 1; i >= 0; i--) {
                soma += (mult * Integer.valueOf(fonte.substring(i, i + 1)));
                if (++mult > limite) {
                    mult = 2;
                }
            }
            fonte += ((soma * 10) % 11) % 10;
        }
        return fonte.substring(fonte.length() - dig);
    }
}
