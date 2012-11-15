package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.EcfDocumento;
import br.com.openpdv.modelo.ecf.EcfNota;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import br.com.openpdv.modelo.ecf.EcfZ;
import br.com.phdss.controlador.PAF;
import com.sun.jersey.api.client.WebResource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 * Classe que realiza a acao de enviar os dados ao servidor.
 *
 * @author Pedro H. Lira
 */
public class ComandoEnviarDados implements IComando {

    private CoreService service;
    private Logger log;

    public ComandoEnviarDados() {
        this.service = new CoreService();
        this.log = Logger.getLogger(ComandoEnviarDados.class);
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            Date data = Util.getDataHora(PAF.AUXILIAR.getProperty("out.envio", null));
            WebResource wr;

            // enviando as notas
            IFiltro filtro = null;
            if (data != null) {
                filtro = new FiltroData("ecfNotaData", ECompara.MAIOR, data);
            }
            List<EcfNota> notas = service.selecionar(new EcfNota(), 0, 0, filtro);
            if (!notas.isEmpty()) {
                wr = Util.getRest(Util.getConfig().get("sinc.server") + "/nota");
                for (EcfNota nota : notas) {
                    wr.type(MediaType.APPLICATION_JSON).put(nota);
                }
                log.debug("Notas enviadas");
            }

            // enviando as nfes
            if (data != null) {
                filtro = new FiltroData("ecfNotaEletronicaData", ECompara.MAIOR, data);
            }
            List<EcfNotaEletronica> nfes = service.selecionar(new EcfNotaEletronica(), 0, 0, filtro);
            if (!nfes.isEmpty()) {
                wr = Util.getRest(Util.getConfig().get("sinc.server") + "/nfe");
                for (EcfNotaEletronica nfe : nfes) {
                    wr.type(MediaType.APPLICATION_JSON).put(nfe);
                }
                log.debug("NFes enviadas");
            }

            // enviando as reducoes Z
            if (data != null) {
                filtro = new FiltroData("ecfZEmissao", ECompara.MAIOR_IGUAL, data);
            }
            List<EcfZ> zs = service.selecionar(new EcfZ(), 0, 0, filtro);
            if (!zs.isEmpty()) {
                wr = Util.getRest(Util.getConfig().get("sinc.server") + "/reducaoZ");
                for (EcfZ z : zs) {
                    // seleciona os documentos desta Z
                    Date inicio = z.getEcfZMovimento();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(inicio);
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    Date fim = cal.getTime();

                    FiltroData fd1 = new FiltroData("ecfDocumentoData", ECompara.MAIOR, inicio);
                    FiltroData fd2 = new FiltroData("ecfDocumentoData", ECompara.MENOR, fim);
                    GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fd1, fd2});
                    List<EcfDocumento> docs = service.selecionar(new EcfDocumento(), 0, 0, gf);

                    z.setEcfDocumentos(docs);
                    wr.type(MediaType.APPLICATION_JSON).put(z);
                }
                log.debug("Zs enviadas");
            }

            // se sucesso atualiza no arquivo a data do ultimo envio
            PAF.AUXILIAR.setProperty("out.envio", Util.getDataHora(new Date()));
            PAF.criptografar();
        } catch (Exception ex) {
            log.error("Erro no envio dos dados para sincronismo.", ex);
            throw new OpenPdvException("Erro no envio dos dados para sincronismo.", ex);
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }
}
