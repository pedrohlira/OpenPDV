package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.inutnfe.TInutNFe;
import br.com.openpdv.inutnfe.TInutNFe.InfInut;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.openpdv.visao.core.Caixa;
import java.util.Date;
import javax.xml.bind.JAXBElement;

/**
 * Classe que realiza a acao de gerar o XML da NFe para inutilizacao.
 *
 * @author Pedro H. Lira
 */
public class ComandoGerarNFeInutilizada implements IComando {

    private JAXBElement<TInutNFe> element;
    private EcfNotaEletronica nota;
    private String numero;
    private String obs;

    public ComandoGerarNFeInutilizada(String numero, String obs) {
        this.numero = numero;
        this.obs = obs;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            SisEmpresa empresa = Caixa.getInstancia().getEmpresa();
            // desmembra a chave
            String uf = empresa.getSisMunicipio().getSisEstado().getSisEstadoIbge() + "";
            String ano = Util.formataData(new Date(), "yyyy").substring(2, 4);
            String cnpj = empresa.getSisEmpresaCnpj().replaceAll("[^0-9]", "");
            String modo = "55";
            String serie = Util.formataNumero(Util.getConfig().get("nfe.serie"), 3, 0, false);
            String nfIni = Util.formataNumero(numero, 9, 0, false);
            String nfFim = Util.formataNumero(numero, 9, 0, false);
            String id = "ID" + uf + ano + cnpj + modo + serie + nfIni + nfFim;

            // gerar o objeto
            InfInut infInut = new InfInut();
            infInut.setTpAmb(Util.getConfig().get("nfe.tipoamb"));
            infInut.setId(id);
            infInut.setCUF(uf);
            infInut.setAno(ano);
            infInut.setCNPJ(cnpj);
            infInut.setMod(modo);
            infInut.setSerie(Integer.valueOf(serie) + "");
            infInut.setNNFIni(numero + "");
            infInut.setNNFFin(numero + "");
            infInut.setXJust(Util.normaliza(obs));
            infInut.setXServ("INUTILIZAR");
            TInutNFe inutNfe = new TInutNFe();
            inutNfe.setInfInut(infInut);
            inutNfe.setVersao(Util.getConfig().get("nfe.versao"));

            element = new br.com.openpdv.inutnfe.ObjectFactory().createInutNFe(inutNfe);
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
    public JAXBElement<TInutNFe> getElement() {
        return element;
    }
}
