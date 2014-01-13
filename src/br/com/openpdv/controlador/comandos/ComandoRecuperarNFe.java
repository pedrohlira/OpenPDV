package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.NFe;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.ENotaStatus;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import br.com.opensig.consrecinfe.TConsReciNFe;
import br.com.opensig.retconsrecinfe.TProtNFe.InfProt;
import br.com.opensig.retconsrecinfe.TRetConsReciNFe;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.w3c.dom.Document;

/**
 * Classe responsavel pela acao de recuperar o status do XML da NFe enviado para
 * sefaz.
 *
 * @author Pedro H. Lira
 */
public class ComandoRecuperarNFe implements IComando {

    private NFe nfe;
    private EcfNotaEletronica nota;

    public ComandoRecuperarNFe(EcfNotaEletronica nota) {
        this.nota = nota;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            nfe = new NFe();
            // espera o tempo de processamento na sefaz
            Thread.sleep(5000);
            // monta o xml de recuperacao
            TConsReciNFe reci = new TConsReciNFe();
            reci.setVersao(Util.getConfig().get("nfe.versao"));
            reci.setTpAmb(Util.getConfig().get("nfe.tipoamb"));
            reci.setNRec(nota.getEcfNotaEletronicaRecibo());
            JAXBElement<TConsReciNFe> element = new br.com.opensig.consrecinfe.ObjectFactory().createConsReciNFe(reci);
            String xml = NFe.objToXml(element, TConsReciNFe.class);
            
            // envia para sefaz
            Document doc = NFe.getXml(nota.getEcfNotaEletronicaXml());
            String uf = NFe.getValorTag(doc.getDocumentElement(), "cUF");
            String serie = NFe.getValorTag(doc.getDocumentElement(), "serie");
            String ambiente = NFe.getValorTag(doc.getDocumentElement(), "tpAmb");
            String proc = nfe.retornoNFe(xml, uf, ambiente, serie);
            // analisa o retorno e seta os status
            TRetConsReciNFe ret = NFe.xmlToObj(proc, TRetConsReciNFe.class);
            // verifica se sucesso
            if (ret.getProtNFe().isEmpty()) {
                throw new OpenPdvException(ret.getXMotivo());
            } else {
                if (ret.getProtNFe().get(0).getInfProt().getCStat().equals("100")) {
                    InfProt prot = ret.getProtNFe().get(0).getInfProt();
                    nota.setEcfNotaEletronicaXml(montaProcNfe(nota.getEcfNotaEletronicaXml(), proc));
                    nota.setEcfNotaEletronicaStatus(ENotaStatus.AUTORIZADO.toString());
                    nota.setEcfNotaEletronicaProtocolo(prot.getNProt());
                } else {
                    throw new OpenPdvException(ret.getProtNFe().get(0).getInfProt().getXMotivo());
                }
            }
        } catch (InterruptedException | JAXBException ex) {
            throw new OpenPdvException(ex.getMessage());
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    /**
     * Metodo que monta o xml final inserindo o protocolo.
     *
     * @param xml os dados originais enviados.
     * @param proc os dados do protocolo recebidos.
     * @return uma string com o xml completo.
     */
    private String montaProcNfe(String xml, String proc) {
        // transforma em doc
        Document doc1 = NFe.getXml(xml);
        Document doc2 = NFe.getXml(proc);

        // pega as tags corretas
        xml = NFe.getXml(doc1.getElementsByTagName("NFe").item(0));
        proc = NFe.getXml(doc2.getElementsByTagName("protNFe").item(0));

        // unifica
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<nfeProc versao=\"").append(Util.getConfig().get("nfe.versao")).append("\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
        sb.append(xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
        sb.append(proc.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
        sb.append("</nfeProc>");
        return sb.toString();
    }
}
