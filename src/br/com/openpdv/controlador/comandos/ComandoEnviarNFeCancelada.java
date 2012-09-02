package br.com.openpdv.controlador.comandos;

import br.com.openpdv.cancnfe.TCancNFe;
import br.com.openpdv.controlador.core.NFe;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.ENotaStatus;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import br.com.openpdv.retcancnfe.TRetCancNFe;
import br.com.openpdv.visao.core.Caixa;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.w3c.dom.Document;

/**
 * Classe responsavel pela acao de enviar o XML da NFe de cancelamento para
 * sefaz.
 *
 * @author Pedro H. Lira
 */
public class ComandoEnviarNFeCancelada implements IComando {

    private JAXBElement<TCancNFe> element;
    private NFe nfe;
    private EcfNotaEletronica nota;

    public ComandoEnviarNFeCancelada(JAXBElement<TCancNFe> element, EcfNotaEletronica nota) {
        this.element = element;
        this.nota = nota;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            // transforma o elemento em string
            nfe = new NFe();
            String xml = NFe.objToXml(element, TCancNFe.class);

            // assina
            Document doc = NFe.getXml(xml);
            xml = nfe.assinarXML(doc, ENotaStatus.CANCELANDO);

            // envia para sefaz
            String uf = Caixa.getInstancia().getEmpresa().getSisMunicipio().getSisEstado().getSisEstadoIbge() + "";
            String ambiente = Util.getConfig().get("nfe.tipoamb");
            String canc = nfe.cancelar(xml, uf, ambiente);

            // analisa o retorno e seta os status
            TRetCancNFe ret = NFe.xmlToObj(canc, TRetCancNFe.class);
            if (ret.getInfCanc().getCStat().equals("101")) {
                nota.setEcfNotaEletronicaStatus(ENotaStatus.CANCELADO.toString());
                nota.setEcfNotaEletronicaXmlCancelado(montaProcCancNfe(xml, canc));
                nota.setEcfNotaEletronicaProtocoloCancelado(ret.getInfCanc().getNProt());
            } else {
                throw new OpenPdvException(ret.getInfCanc().getXMotivo());
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
     * Metodo que monta o xml final inserindo o protocolo.
     *
     * @param canc os dados originais enviados.
     * @param proc os dados do protocolo recebidos.
     * @return uma string com o xml completo.
     */
    private String montaProcCancNfe(String canc, String proc) {
        // transforma em doc
        Document doc1 = NFe.getXml(canc);
        Document doc2 = NFe.getXml(proc);

        // pega as tags corretas
        canc = NFe.getXml(doc1.getElementsByTagName("cancNFe").item(0));
        proc = NFe.getXml(doc2.getElementsByTagName("retCancNFe").item(0));

        // unifica
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<procCancNFe versao=\"").append(Util.getConfig().get("nfe.versao")).append("\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
        sb.append(canc.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
        sb.append(proc.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
        sb.append("</procCancNFe>");

        return sb.toString();
    }

}
