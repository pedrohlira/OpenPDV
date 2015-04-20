package br.com.openpdv.controlador.comandos;

import br.com.phdss.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import br.inf.portalfiscal.nfe.schema.eventoCancNFe.TEvento;
import br.inf.portalfiscal.nfe.schema.eventoCancNFe.TEvento.InfEvento;
import br.inf.portalfiscal.nfe.schema.eventoCancNFe.TEvento.InfEvento.DetEvento;
import java.util.Date;
import javax.xml.bind.JAXBElement;

/**
 * Classe que realiza a acao de gerar o XML da NFe para cancelamento.
 *
 * @author Pedro H. Lira
 */
public class ComandoGerarNFeCancelada implements IComando {

    private JAXBElement<TEvento> element;
    private EcfNotaEletronica nota;
    private String obs;

    public ComandoGerarNFeCancelada(EcfNotaEletronica nota, String obs) {
        this.nota = nota;
        this.obs = obs;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            // desmembra a chave
            Date agora = new Date();
            String chave = nota.getEcfNotaEletronicaChave();
            String uf = chave.substring(0, 2);
            String cnpj = chave.substring(6, 20);
            String data = Util.formataData(agora, "yyyy-MM-dd'T'HH:mm:ssz").replace("GMT", "");
            String tipo = "110111";
            String versao = Util.getConfig().getProperty("nfe.evento");
            String seq = "1";

            // informacoes
            InfEvento infEvento = new InfEvento();
            infEvento.setId("ID" + tipo + chave + "01");
            infEvento.setCOrgao(uf);
            infEvento.setTpAmb(Util.getConfig().getProperty("nfe.tipoamb"));
            infEvento.setCNPJ(cnpj);
            infEvento.setChNFe(chave);
            infEvento.setDhEvento(data);
            infEvento.setTpEvento(tipo);
            infEvento.setNSeqEvento(seq);
            infEvento.setVerEvento(versao);

            // descricao
            DetEvento detEvento = new DetEvento();
            detEvento.setVersao(versao);
            detEvento.setDescEvento("Cancelamento");
            detEvento.setNProt(nota.getEcfNotaEletronicaProtocolo());
            detEvento.setXJust(Util.normaliza(obs));
            infEvento.setDetEvento(detEvento);

            // evento
            TEvento evento = new TEvento();
            evento.setInfEvento(infEvento);
            evento.setVersao(versao);

            // transforma em string o xml e salva
            element = new br.inf.portalfiscal.nfe.schema.eventoCancNFe.ObjectFactory().createEvento(evento);
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
    public JAXBElement<TEvento> getElement() {
        return element;
    }
}
