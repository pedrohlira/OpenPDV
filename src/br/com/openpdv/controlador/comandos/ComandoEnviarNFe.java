package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.NFe;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.ENotaStatus;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import br.inf.portalfiscal.nfe.schema.nfe.TNFe;
import br.inf.portalfiscal.nfe.schema.retenvinfe.TRetEnviNFe;
import java.util.Date;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Classe responsavel pela acao de enviar o XML da NFe para sefaz.
 *
 * @author Pedro H. Lira
 */
public class ComandoEnviarNFe implements IComando {

    private JAXBElement<TNFe> element;
    private NFe nfe;
    private EcfNotaEletronica nota;

    public ComandoEnviarNFe(JAXBElement<TNFe> element) {
        this.element = element;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            // transforma o elemento em string
            nfe = new NFe();
            String xml = NFe.objToXml(element, TNFe.class);

            // adicionando dados ao xml
            int nfeINI = xml.indexOf("<NFe");
            int nfeFIM = xml.indexOf("</NFe>");
            xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><enviNFe xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\""
                    + Util.getConfig().getProperty("nfe.versao") + "\"><idLote>" + new Date().getTime() + "</idLote>" + xml.substring(nfeINI, nfeFIM) + "</NFe></enviNFe>";

            // assinando o documento
            Document doc = NFe.getXml(xml);
            xml = nfe.assinarXML(doc, ENotaStatus.AUTORIZANDO);

            // envia para sefaz
            String recibo = nfe.enviarNFe(xml);

            // analisa o retorno
            TRetEnviNFe ret = NFe.xmlToObj(recibo, TRetEnviNFe.class);
            if (ret.getCStat().equals("103")) {
                gerarModelo(xml, ret.getInfRec().getNRec());
            } else {
                throw new OpenPdvException(ret.getXMotivo());
            }
        } catch (JAXBException ex) {
            throw new OpenPdvException(ex);
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    /**
     * Metodo que retorna o modelo de objeto para ser salvo no banco
     *
     * @return null caso tenha ocorrido erros no envio.
     */
    public EcfNotaEletronica getNota() {
        return nota;
    }

    /**
     * Metodo que gera um objeto modelo da nota eletronica.
     *
     * @param xml os dados completos que foram enviados para sefaz.
     * @param recibo o numero do recibo devolvido pela sefaz.
     * @throws OpenPdvException dispara um erro caso ocorra.
     */
    private void gerarModelo(String xml, String recibo) throws OpenPdvException {
        Document doc = NFe.getXml(xml);
        // recupera a chave
        String chave = doc.getElementsByTagName("infNFe").item(0).getAttributes().item(0).getNodeValue().replace("NFe", "");
        // recupera o numero
        String numero = NFe.getValorTag(doc.getDocumentElement(), "nNF");
        // recupera a data
        String data = NFe.getValorTag(doc.getDocumentElement(), "dEmi");
        Date dtData = Util.formataData(data, "yyyy-MM-dd");
        // recupera os totais
        Element total = (Element) doc.getElementsByTagName("total").item(0);
        // recupera o valor
        String valor = NFe.getValorTag(total, "vNF");
        // recupera o icms
        String icms = NFe.getValorTag(total, "vICMS");
        if (icms == null) {
            icms = "0.00";
        }
        // recupera o ipi
        String ipi = NFe.getValorTag(total, "vIPI");
        if (ipi == null) {
            ipi = "0.00";
        }
        // recupera o pis
        String pis = NFe.getValorTag(total, "vPIS");
        if (pis == null) {
            pis = "0.00";
        }
        // recupera o cofins
        String cofins = NFe.getValorTag(total, "vCOFINS");
        if (cofins == null) {
            cofins = "0.00";
        }

        // cria a nota no banco
        nota = new EcfNotaEletronica();
        nota.setEcfNotaEletronicaStatus(ENotaStatus.AUTORIZANDO.toString());
        nota.setEcfNotaEletronicaNumero(Integer.valueOf(numero));
        nota.setEcfNotaEletronicaData(dtData);
        nota.setEcfNotaEletronicaValor(Double.valueOf(valor));
        nota.setEcfNotaEletronicaChave(chave);
        nota.setEcfNotaEletronicaIcms(Double.valueOf(icms));
        nota.setEcfNotaEletronicaIpi(Double.valueOf(ipi));
        nota.setEcfNotaEletronicaPis(Double.valueOf(pis));
        nota.setEcfNotaEletronicaCofins(Double.valueOf(cofins));
        nota.setEcfNotaEletronicaProtocolo("");
        nota.setEcfNotaEletronicaXml(xml);
        nota.setEcfNotaEletronicaProtocoloCancelado("");
        nota.setEcfNotaEletronicaXmlCancelado("");
        nota.setEcfNotaEletronicaRecibo(recibo);
    }
}
