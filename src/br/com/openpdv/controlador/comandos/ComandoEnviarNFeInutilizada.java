package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.NFe;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.ENotaStatus;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import br.com.openpdv.visao.core.Caixa;
import br.com.opensig.inutnfe.TInutNFe;
import br.com.opensig.retinutnfe.TRetInutNFe;
import java.util.Date;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.w3c.dom.Document;

/**
 * Classe responsavel pela acao de enviar o XML da NFe de inutilizacao para
 * sefaz.
 *
 * @author Pedro H. Lira
 */
public class ComandoEnviarNFeInutilizada implements IComando {

    private JAXBElement<TInutNFe> element;
    private NFe nfe;
    private EcfNotaEletronica nota;

    public ComandoEnviarNFeInutilizada(JAXBElement<TInutNFe> element) {
        this.element = element;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            // transforma o elemento em string
            nfe = new NFe();
            String xml = NFe.objToXml(element, TInutNFe.class);

            // assina
            Document doc = NFe.getXml(xml);
            xml = nfe.assinarXML(doc, ENotaStatus.INUTILIZANDO);

            // envia para sefaz
            String uf = Caixa.getInstancia().getEmpresa().getSisMunicipio().getSisEstado().getSisEstadoIbge() + "";
            String ambiente = Util.getConfig().get("nfe.tipoamb");
            String inut = nfe.inutilizar(xml, uf, ambiente);

            // analisa o retorno e seta os status
            TRetInutNFe ret = NFe.xmlToObj(inut, TRetInutNFe.class);
            if (ret.getInfInut().getCStat().equals("102")) {
                nota = new EcfNotaEletronica();
                nota.setEcfNotaEletronicaStatus(ENotaStatus.INUTILIZADO.toString());
                nota.setEcfNotaEletronicaNumero(Integer.valueOf(ret.getInfInut().getNNFIni()));
                nota.setEcfNotaEletronicaData(new Date());
                nota.setEcfNotaEletronicaValor(0.00);
                nota.setEcfNotaEletronicaChave(ret.getInfInut().getId().replace("ID", ""));
                nota.setEcfNotaEletronicaProtocolo(ret.getInfInut().getNProt());
                nota.setEcfNotaEletronicaIcms(0.00);
                nota.setEcfNotaEletronicaIpi(0.00);
                nota.setEcfNotaEletronicaPis(0.00);
                nota.setEcfNotaEletronicaCofins(0.00);
                nota.setEcfNotaEletronicaXml(montaProcInutNfe(xml, inut));
                nota.setEcfNotaEletronicaXmlCancelado("");
                nota.setEcfNotaEletronicaProtocoloCancelado("");
                nota.setEcfNotaEletronicaRecibo("");
            } else {
                throw new OpenPdvException(ret.getInfInut().getXMotivo());
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
     * @param inut os dados originais enviados.
     * @param proc os dados do protocolo recebidos.
     * @return uma string com o xml completo.
     */
    private String montaProcInutNfe(String inut, String proc) {
        // transforma em doc
        Document doc1 = NFe.getXml(inut);
        Document doc2 = NFe.getXml(proc);

        // pega as tags corretas
        inut = NFe.getXml(doc1.getElementsByTagName("inutNFe").item(0));
        proc = NFe.getXml(doc2.getElementsByTagName("retInutNFe").item(0));

        // unifica
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<ProcInutNFe versao=\"").append(Util.getConfig().get("nfe.versao")).append("\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
        sb.append(inut.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
        sb.append(proc.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
        sb.append("</ProcInutNFe>");

        return sb.toString();
    }
}
