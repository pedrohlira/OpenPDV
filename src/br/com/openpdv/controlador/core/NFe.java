package br.com.openpdv.controlador.core;

import br.com.openpdv.controlador.PAF;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.ENotaStatus;
import br.inf.portalfiscal.www.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.nfeinutilizacao2.NfeInutilizacao2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.nferecepcao2.NfeRecepcao2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.nferetrecepcao2.NfeRetRecepcao2Stub;
import java.io.*;
import java.rmi.RemoteException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.*;
import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * Classe que executa operacoes comuns sobre o xml da NFe.
 *
 * @author Pedro H. Lira
 */
public class NFe {

    // log do sistmea
    private static Logger log;
    // conf de homologacao contendo as urls do WS
    private static Properties HOMOLOGACAO = new Properties();
    // conf de producao contendo as urls do WS
    private static Properties PRODUCAO = new Properties();
    // colecao de estados do ambiente virual nacional
    private static List<String> SVAN = new ArrayList<>();
    // colecao de estados do ambiente virual do RS
    private static List<String> SVRS = new ArrayList<>();

    // setando os configs da sefaz
    static {
        log = Logger.getLogger(NFe.class);
        try {
            // carrega a homologacao
            try (FileInputStream homo = new FileInputStream("nfe/sefazH.properties")) {
                HOMOLOGACAO.load(homo);
            }
            // carrega a producao
            try (FileInputStream prod = new FileInputStream("nfe/sefazP.properties")) {
                PRODUCAO.load(prod);
            }
            // seta os estados do ambiente virtual nacional
            SVAN.addAll(Arrays.asList(PRODUCAO.getProperty("SVAN_Estados").split(",")));
            // seta os estados do ambiente virtual RS
            SVRS.addAll(Arrays.asList(PRODUCAO.getProperty("SVRS_Estados").split(",")));
        } catch (Exception ex) {
            log.error("Erro ao ler config da sefaz", ex);
        }
    }

    /**
     * Construtor padrao
     *
     * @throws OpenPdvException caso nao consiga se conectar na sefaz.
     */
    public NFe() throws OpenPdvException {
        try {
            // conecta na sefaz
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
            Object[] chaves = lerCertificado(fac);
            PrivateKey pk = (PrivateKey) chaves[0];
            X509Certificate x509 = (X509Certificate) chaves[2];
            SocketFactoryDinamico sfd = new SocketFactoryDinamico(x509, pk, "nfe/NFeCacerts");

            // ativando o protocolo
            Protocol protocol = new Protocol("https", sfd, 443);
            Protocol.registerProtocol("https", protocol);
        } catch (Exception ex) {
            throw new OpenPdvException("Problemas com o certificado.", ex);
        }
    }

    /**
     * Metodo que assina digitalmente um xml.
     *
     * @param doc o documento xml a ser assinado.
     * @param status o tipo de status do xml.
     * @return o proprio xml ja assinadao.
     * @throws OpenPdvException caso ocorra algum erro.
     */
    public String assinarXML(Document doc, ENotaStatus status) throws OpenPdvException {
        try {
            // chaves
            String tag;
            switch (status) {
                case AUTORIZANDO:
                    tag = "NFe";
                    break;
                case CANCELANDO:
                    tag = "infCanc";
                    break;
                case INUTILIZANDO:
                    tag = "infInut";
                    break;
                default:
                    return null;
            }

            // trabalhando o certificado
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
            List<Transform> transformList = signatureFactory(fac);
            Object[] chaves = lerCertificado(fac);
            PrivateKey pk = (PrivateKey) chaves[0];
            KeyInfo ki = (KeyInfo) chaves[1];

            // parse no xml e pega o id
            Element ele = (Element) doc.getElementsByTagName(status == ENotaStatus.AUTORIZANDO ? "infNFe" : tag).item(0);
            String id = ele.getAttribute("Id");
            // adiciona a referencia
            Reference ref = fac.newReference("#" + id, fac.newDigestMethod(DigestMethod.SHA1, null), transformList, null, null);
            // adiciona a informacao da assinatura
            SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                    fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));
            // adiciona a assinatura
            XMLSignature signature = fac.newXMLSignature(si, ki);
            DOMSignContext dsc = new DOMSignContext(pk, status == ENotaStatus.AUTORIZANDO ? doc.getElementsByTagName(tag).item(0) : doc.getFirstChild());
            signature.sign(dsc);

            return getXml(doc);
        } catch (Exception ex) {
            throw new OpenPdvException(ex);
        }
    }

    /**
     * Metodo que envia um NFe nova para Sefaz.
     *
     * @param xml o arquivo em formato de string.
     * @return uma string com o xml de resposta.
     * @throws OpenPdvException caso ocorra algum erro.
     */
    public String enviarNFe(String xml) throws OpenPdvException {
        try {
            Document doc = getXml(xml);
            String uf = getValorTag(doc.getDocumentElement(), "cUF");
            String serie = getValorTag(doc.getDocumentElement(), "serie");
            String ambiente = getValorTag(doc.getDocumentElement(), "tpAmb");
            int iserie = Integer.valueOf(serie);
            String url = identificarXml(uf, ambiente, "NfeRecepcao", iserie >= 900);

            OMElement ome = AXIOMUtil.stringToOM(xml);
            NfeRecepcao2Stub.NfeDadosMsg dadosMsg = new NfeRecepcao2Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);

            NfeRecepcao2Stub.NfeCabecMsg nfeCabecMsg = new NfeRecepcao2Stub.NfeCabecMsg();
            nfeCabecMsg.setCUF(uf);
            nfeCabecMsg.setVersaoDados(Util.getConfig().get("nfe.versao"));

            NfeRecepcao2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeRecepcao2Stub.NfeCabecMsgE();
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

            NfeRecepcao2Stub stub = new NfeRecepcao2Stub(url);
            NfeRecepcao2Stub.NfeRecepcaoLote2Result result = stub.nfeRecepcaoLote2(dadosMsg, nfeCabecMsgE);

            return result.getExtraElement().toString();
        } catch (NumberFormatException | OpenPdvException | XMLStreamException | RemoteException ex) {
            log.error("Erro ao enviar o xml para sefaz :: ", ex);
            throw new OpenPdvException(ex);
        }
    }

    /**
     * Metodo que recupera a resposta da Sefaz sobre o xml enviado.
     *
     * @param xml o arquivo em formato de string.
     * @param uf o estado da nota.
     * @param ambiente o ambiente que a nota foi gerada.
     * @param serie o numero de serie da nota.
     * @return uma string com o xml de resposta.
     * @throws OpenPdvException caso ocorra algum erro.
     */
    public String retornoNFe(String xml, String uf, String ambiente, String serie) throws OpenPdvException {
        try {
            int iserie = Integer.valueOf(serie);
            String url = identificarXml(uf, ambiente, "NfeRetRecepcao", iserie >= 900);

            OMElement ome = AXIOMUtil.stringToOM(xml);
            NfeRetRecepcao2Stub.NfeDadosMsg dadosMsg = new NfeRetRecepcao2Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);

            NfeRetRecepcao2Stub.NfeCabecMsg nfeCabecMsg = new NfeRetRecepcao2Stub.NfeCabecMsg();
            nfeCabecMsg.setCUF(uf);
            nfeCabecMsg.setVersaoDados(Util.getConfig().get("nfe.versao"));

            NfeRetRecepcao2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeRetRecepcao2Stub.NfeCabecMsgE();
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

            NfeRetRecepcao2Stub stub = new NfeRetRecepcao2Stub(url);
            NfeRetRecepcao2Stub.NfeRetRecepcao2Result result = stub.nfeRetRecepcao2(dadosMsg, nfeCabecMsgE);

            return result.getExtraElement().toString();
        } catch (NumberFormatException | OpenPdvException | XMLStreamException | RemoteException ex) {
            log.error("Erro ao consultar o recibo do xml na sefaz :: ", ex);
            throw new OpenPdvException(ex);
        }
    }

    /**
     * Metodo que cancela uma NFe ja enviada.
     *
     * @param xml o arquivo em formato de string.
     * @param uf o estado da nota.
     * @param ambiente o ambiente que a nota foi gerada.
     * @return uma string com o xml de resposta.
     * @throws OpenPdvException caso ocorra algum erro.
     */
    public String cancelar(String xml, String uf, String ambiente) throws OpenPdvException {
        try {
            String url = identificarXml(uf, ambiente, "NfeCancelamento", false);

            OMElement ome = AXIOMUtil.stringToOM(xml);
            NfeCancelamento2Stub.NfeDadosMsg dadosMsg = new NfeCancelamento2Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);

            NfeCancelamento2Stub.NfeCabecMsg nfeCabecMsg = new NfeCancelamento2Stub.NfeCabecMsg();
            nfeCabecMsg.setCUF(uf);
            nfeCabecMsg.setVersaoDados(Util.getConfig().get("nfe.versao"));

            NfeCancelamento2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeCancelamento2Stub.NfeCabecMsgE();
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

            NfeCancelamento2Stub stub = new NfeCancelamento2Stub(url);
            NfeCancelamento2Stub.NfeCancelamentoNF2Result result = stub.nfeCancelamentoNF2(dadosMsg, nfeCabecMsgE);

            return result.getExtraElement().toString();
        } catch (OpenPdvException | XMLStreamException | RemoteException ex) {
            log.error("Erro ao cancelar uma nfe na sefaz :: ", ex);
            throw new OpenPdvException(ex);
        }
    }

    /**
     * Metodo que inutiliza uma NFe que nao foi enviada.
     *
     * @param xml o arquivo em formato de string.
     * @param uf o estado da nota.
     * @param ambiente o ambiente que a nota foi gerada.
     * @return uma string com o xml de resposta.
     * @throws OpenPdvException caso ocorra algum erro.
     */
    public String inutilizar(String xml, String uf, String ambiente) throws OpenPdvException {
        try {
            String url = identificarXml(uf, ambiente, "NfeInutilizacao", false);

            OMElement ome = AXIOMUtil.stringToOM(xml);
            NfeInutilizacao2Stub.NfeDadosMsg dadosMsg = new NfeInutilizacao2Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);

            NfeInutilizacao2Stub.NfeCabecMsg nfeCabecMsg = new NfeInutilizacao2Stub.NfeCabecMsg();
            nfeCabecMsg.setCUF(uf);
            nfeCabecMsg.setVersaoDados(Util.getConfig().get("nfe.versao"));

            NfeInutilizacao2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeInutilizacao2Stub.NfeCabecMsgE();
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

            NfeInutilizacao2Stub stub = new NfeInutilizacao2Stub(url);
            NfeInutilizacao2Stub.NfeInutilizacaoNF2Result result = stub.nfeInutilizacaoNF2(dadosMsg, nfeCabecMsgE);

            return result.getExtraElement().toString();
        } catch (OpenPdvException | XMLStreamException | RemoteException ex) {
            log.error("Erro ao cancelar uma nfe na sefaz :: ", ex);
            throw new OpenPdvException(ex);
        }
    }

    /**
     * Metodo que envolopa a assinatura do xml.
     *
     * @param signatureFactory a fabrica de assinatura.
     * @return uma lista de transformacoes realizadas.
     * @throws Exception caso ocorra algum erro.
     */
    private List<Transform> signatureFactory(XMLSignatureFactory signatureFactory) throws Exception {
        // seta as transformacaoes e envelopes
        List<Transform> transformList = new ArrayList<>();
        TransformParameterSpec tps = null;
        Transform envelopedTransform = signatureFactory.newTransform(Transform.ENVELOPED, tps);
        Transform c14NTransform = signatureFactory.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", tps);

        transformList.add(envelopedTransform);
        transformList.add(c14NTransform);
        return transformList;
    }

    /**
     * Metodo que le o certificado digital A1
     *
     * @param signatureFactory a fabrica de assinatura.
     * @return um array de objetos contendo as chaves.
     * @throws Exception caso ocorra algum erro.
     */
    private Object[] lerCertificado(XMLSignatureFactory signatureFactory) throws Exception {
        // chave
        PrivateKey pk = null;
        // descriptografa a senha
        String senha = PAF.descriptar(Util.getConfig().get("nfe.senha"));

        // le o certificado
        InputStream entrada = new FileInputStream("nfe/certificado.pfx");
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try {
            ks.load(entrada, senha.toCharArray());
        } catch (IOException e) {
            throw new Exception("Senha do Certificado Digital incorreta ou Certificado invalido.");
        }

        // acha a chave
        KeyStore.PrivateKeyEntry pkEntry = null;
        Enumeration<String> aliasesEnum = ks.aliases();
        while (aliasesEnum.hasMoreElements()) {
            String alias = (String) aliasesEnum.nextElement();
            if (ks.isKeyEntry(alias)) {
                pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(senha.toCharArray()));
                pk = pkEntry.getPrivateKey();
                break;
            }
        }

        // gera a assinatura
        X509Certificate cert = (X509Certificate) pkEntry.getCertificate();
        KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
        List<X509Certificate> x509Content = new ArrayList<>();

        // gera a informacao
        x509Content.add(cert);
        X509Data x509Data = keyInfoFactory.newX509Data(x509Content);
        KeyInfo ki = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));

        return new Object[]{pk, ki, cert};
    }

    /**
     * Metodo que identifica os tipos do xml.
     *
     * @param uf o estado a ser usado.
     * @param tipo o tipo de servidor.
     * @param servico qual o serviço que será usado na sefaz.
     * @param scan caso seja usado o servidor nacional.
     * @return Uma string contendo a URL do WS da Sefaz.
     * @throws OpenPdvException dispara uma excecao caso ocorra algum erro.
     */
    private String identificarXml(String uf, String tipo, String servico, boolean scan) throws OpenPdvException {
        try {
            // identifica se o estado é virtual ou scan
            String chave;
            if (scan) {
                chave = "SCAN_" + servico;
            } else if (SVAN.contains(uf)) {
                chave = "SVAN_" + servico;
            } else if (SVRS.contains(uf)) {
                chave = "SVRS_" + servico;
            } else {
                chave = uf + "_" + servico;
            }

            // identifica a url
            String url = tipo.equals("1") ? PRODUCAO.getProperty(chave) : HOMOLOGACAO.getProperty(chave);
            if (url == null) {
                throw new OpenPdvException("Este servico [" + servico + "] nao esta disponivel para o Estado = " + uf);
            } else {
                return url;
            }
        } catch (Exception ex) {
            throw new OpenPdvException(ex);
        }
    }

    /**
     * Metodo que Converte XML em Objeto.
     *
     * @param xml o arquivo em formato string.
     * @param classe o nome da classe especifica.
     * @return T o tipo passado de Objeto.
     * @exception JAXBException dispara uma excecao caso ocorra erro.
     *
     */
    public static <T> T xmlToObj(String xml, Class classe) throws JAXBException {
        Document doc = getXml(xml);
        JAXBContext context = JAXBContext.newInstance(classe);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        JAXBElement<T> element = (JAXBElement<T>) unmarshaller.unmarshal(doc, classe);
        return element.getValue();
    }

    /**
     * Metodo que converte Objeto em XML.
     *
     * @param <T> o tipo passado de Objeto.
     * @param element o Objeto passado.
     * @param classe o nome da classe especifica.
     * @return o arquivo em formato String.
     * @throws JAXBException dispara uma excecao caso ocorra erro.
     */
    public static <T> String objToXml(JAXBElement<T> element, Class classe) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(classe);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        marshaller.marshal(element, sw);

        // retira ns indesejado
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + sw.toString();
        xml = xml.replace("ns2:", "");
        xml = xml.replace(":ns2", "");
        xml = xml.replace(" xmlns=\"http://www.w3.org/2000/09/xmldsig#\"", "");
        // retira as quebras de linhas
        xml = xml.replace("\r", "");
        xml = xml.replace("\n", "");
        // retira acentos
        xml = Util.normaliza(xml);
        // remove alguns caracteres especiais
        xml = xml.replaceAll(Util.getConfig().get("nfe.regexp"), "");
        return xml;
    }

    /**
     * @see #getXml(java.lang.String, java.lang.String,
     * org.xml.sax.ErrorHandler)
     */
    public static Document getXml(String xml) {
        return getXml(xml, null, null);
    }

    /**
     * Metodo que transforma uma string em documento xml.
     *
     * @param xml na forma de texto.
     * @param xsd caminho real do arquivo xsd.
     * @param error interceptador de erros do parse.
     * @return um DOM do xml ou null caso aconteca algum erro.
     */
    public static Document getXml(String xml, String xsd, ErrorHandler error) {
        try {
            // gera um objeto DOM do xml
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            if (xsd != null) {
                dbf.setValidating(true);
                dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", XMLConstants.W3C_XML_SCHEMA_NS_URI);
                dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", xsd);
            }
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            docBuilder.setErrorHandler(error);
            return docBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
        } catch (IllegalArgumentException | ParserConfigurationException | SAXException | IOException e) {
            return null;
        }
    }

    /**
     * Metodo que transforma em string um documento xml.
     *
     * @param node o documento xml tipo DOM.
     * @return uma String do xml ou null em caso de erro.
     */
    public static String getXml(Node node) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(node), new StreamResult(os));
            return os.toString();
        } catch (TransformerFactoryConfigurationError | TransformerException e) {
            return null;
        }
    }

    /**
     * Metodo que retorna o valor de uma tag dentro do xml.
     *
     * @param ele elemento xml em forma de objeto.
     * @param tag nome da tag que deseja recuperar o valor.
     * @return valor da tag encontrada ou NULL se nao achada.
     */
    public static String getValorTag(Element ele, String tag) {
        try {
            return ele.getElementsByTagName(tag).item(0).getFirstChild().getNodeValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Metodo que gera o danfe em formato pdf.
     *
     * @param xml os dados da NFe.
     * @return um array de bytes contendo o pdf do danfe.
     */
    public static byte[] getDanfe(String xml) {
        byte[] pdf;

        try {
            // gera um objeto DOM do xml
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
            // local do relatorio jasper
            String jasper = "nfe/danfe1.jasper";
            // fonte de dados
            String xpath = getValorTag(doc.getDocumentElement(), "nProt");
            xpath = xpath == null ? "/nfe/infNFe/det" : "/nfeProc/NFe/infNFe/det";
            JRXmlDataSource ds = new JRXmlDataSource(doc, xpath);
            // parametros
            Map<String, Object> param = new HashMap<>();
            param.put("Logo", "../conf/logo_mini.png");
            param.put("REPORT_LOCALE", Locale.getDefault());
            // gerando o relatorio
            JasperPrint print = JasperFillManager.fillReport(jasper, param, ds);
            // exportando em pdf
            pdf = JasperExportManager.exportReportToPdf(print);
        } catch (ParserConfigurationException | SAXException | IOException | JRException e) {
            log.error("Nao gerou o Danfe", e);
            pdf = null;
        }

        return pdf;
    }
}
