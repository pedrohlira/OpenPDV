package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.NFe;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.ENotaStatus;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import br.com.opensig.eventocancnfe.TEvento;
import br.com.opensig.retenveventocancnfe.TRetEnvEvento;
import java.util.Date;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.w3c.dom.Document;

/**
 * Classe responsavel pela acao de enviar o XML da NFe de cancelamento para sefaz.
 *
 * @author Pedro H. Lira
 */
public class ComandoEnviarNFeCancelada implements IComando {

    private JAXBElement<TEvento> element;
    private NFe nfe;
    private EcfNotaEletronica nota;

    public ComandoEnviarNFeCancelada(JAXBElement<TEvento> element, EcfNotaEletronica nota) {
        this.element = element;
        this.nota = nota;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            // transforma o elemento em string
            long id = new Date().getTime();
            nfe = new NFe();
            String xml = NFe.objToXml(element, TEvento.class);

            // adicionando dados ao xml
            if (xml.indexOf("<envEvento") < 0) {
                int eveINI = xml.indexOf("<evento");
                int eveFIM = xml.indexOf("</evento>");
                xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envEvento xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"" + Util.getConfig().get("nfe.evento") + "\"><idLote>" + id
                        + "</idLote>" + xml.substring(eveINI, eveFIM) + "</evento></envEvento>";
            }

            // assina o documento
            Document doc = NFe.getXml(xml);
            xml = nfe.assinarXML(doc, ENotaStatus.CANCELANDO);

            // envia para sefaz
            String proc = nfe.evento(xml);

            // analisa o retorno e seta os status
            TRetEnvEvento ret = NFe.xmlToObj(proc, TRetEnvEvento.class);
            // verifica se sucesso
            if (ret.getCStat().equals("128")) {
                nota.setEcfNotaEletronicaStatus(ENotaStatus.CANCELADO.toString());
                nota.setEcfNotaEletronicaProtocoloCancelado(ret.getRetEvento().get(0).getInfEvento().getNProt());
                nota.setEcfNotaEletronicaXmlCancelado(montaProcCancNfe(nota.getEcfNotaEletronicaXmlCancelado(), proc, Util.getConfig().get("nfe.evento")));
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
     * Metodo que monta o xml final inserindo o protocolo.
     *
     * @param canc os dados originais enviados.
     * @param proc os dados do protocolo recebidos.
     * @return uma string com o xml completo.
     */
    private String montaProcCancNfe(String canc, String proc, String versao) {
        // transforma em doc
        Document doc1 = NFe.getXml(canc);
        Document doc2 = NFe.getXml(proc);

        // pega as tags corretas
        canc = NFe.getXml(doc1.getElementsByTagName("evento").item(0));
        proc = NFe.getXml(doc2.getElementsByTagName("retEvento").item(0));

        // unifica
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<procEventoNFe versao=\"").append(versao).append("\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
        sb.append(canc.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
        sb.append(proc.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
        sb.append("</procEventoNFe>");

        return sb.toString();
    }
}
