package br.com.openpdv.controlador.comandos;

import br.com.openpdv.cancnfe.TCancNFe;
import br.com.openpdv.cancnfe.TCancNFe.InfCanc;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import javax.xml.bind.JAXBElement;

/**
 * Classe que realiza a acao de gerar o XML da NFe para cancelamento.
 *
 * @author Pedro H. Lira
 */
public class ComandoGerarNFeCancelada implements IComando {

    private JAXBElement<TCancNFe> element;
    private EcfNotaEletronica nota;
    private String obs;

    public ComandoGerarNFeCancelada(EcfNotaEletronica nota, String obs) {
        this.nota = nota;
        this.obs = obs;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            InfCanc infCanc = new InfCanc();
            infCanc.setId("ID" + nota.getEcfNotaEletronicaChave());
            infCanc.setTpAmb(Util.getConfig().get("nfe.tipoamb"));
            infCanc.setChNFe(nota.getEcfNotaEletronicaChave());
            infCanc.setNProt(nota.getEcfNotaEletronicaProtocolo());
            infCanc.setXJust(Util.normaliza(obs));
            infCanc.setXServ("CANCELAR");
            TCancNFe cancNfe = new TCancNFe();
            cancNfe.setInfCanc(infCanc);
            cancNfe.setVersao(Util.getConfig().get("nfe.versao"));

            element = new br.com.openpdv.cancnfe.ObjectFactory().createCancNFe(cancNfe);
        } catch (Exception e) {
            throw new OpenPdvException(e.getMessage());
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
    public JAXBElement<TCancNFe> getElement() {
        return element;
    }
}
