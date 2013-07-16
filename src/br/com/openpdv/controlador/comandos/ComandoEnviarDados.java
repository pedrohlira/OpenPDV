package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.EJuncao;
import br.com.openpdv.modelo.core.filtro.FiltroData;
import br.com.openpdv.modelo.core.filtro.GrupoFiltro;
import br.com.openpdv.modelo.core.filtro.IFiltro;
import br.com.openpdv.modelo.ecf.EcfDocumento;
import br.com.openpdv.modelo.ecf.EcfNota;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import br.com.openpdv.modelo.ecf.EcfZ;
import br.com.phdss.controlador.PAF;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.util.ArrayList;
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
    private Date data;
    private StringBuilder erros;

    public ComandoEnviarDados() {
        this(Util.getDataHora(PAF.AUXILIAR.getProperty("out.envio", null)));
    }

    public ComandoEnviarDados(Date data) {
        this.service = new CoreService();
        this.log = Logger.getLogger(ComandoEnviarDados.class);
        this.data = data;
        this.erros = new StringBuilder();
    }

    @Override
    public void executar() throws OpenPdvException {
        WebResource wr;
        IFiltro filtro = null;

        // enviando as notas
        try {
            if (data != null) {
                filtro = new FiltroData("ecfNotaData", ECompara.MAIOR, data);
            }
            List<EcfNota> notas = service.selecionar(new EcfNota(), 0, 0, filtro);
            if (!notas.isEmpty()) {

                wr = Util.getRest(Util.getConfig().get("sinc.server") + "/nota");
                for (EcfNota nota : notas) {
                    wr.type(MediaType.APPLICATION_JSON).put(nota);
                }
                log.info("Notas enviadas = " + notas.size());
            }
        } catch (Exception ex) {
            erros.append("Erro no envio das Notas.\n");
            log.error("Erro no envio das Notas.", ex);
        }

        // enviando as nfes
        try {
            if (data != null) {
                filtro = new FiltroData("ecfNotaEletronicaData", ECompara.MAIOR, data);
            }
            List<EcfNotaEletronica> nfes = service.selecionar(new EcfNotaEletronica(), 0, 0, filtro);
            if (!nfes.isEmpty()) {
                wr = Util.getRest(Util.getConfig().get("sinc.server") + "/nfe");
                for (EcfNotaEletronica nfe : nfes) {
                    wr.type(MediaType.APPLICATION_JSON).put(nfe);
                }
                log.info("NFes enviadas = " + nfes.size());
            }
        } catch (Exception ex) {
            erros.append("Erro no envio das NFEs.\n");
            log.error("Erro no envio das NFEs.", ex);
        }

        // enviando as reducoes Z
        try {
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
                log.info("Zs enviadas = " + zs.size());
            }
        } catch (Exception ex) {
            erros.append("Erro no envio das Zs.\n");
            log.error("Erro no envio das Zs.", ex);
        }

        // se sucesso atualiza no arquivo a data do ultimo envio
        if (erros.length() == 0) {
            try {
                PAF.AUXILIAR.setProperty("out.envio", Util.getDataHora(new Date()));
                PAF.criptografar();
            } catch (Exception ex) {
                throw new OpenPdvException("Erro ao salvar no arquivo auxiliar.\nVerifique o log do sistema.", ex);
            }
        } else {
            erros.append("Verifique o log do sistema.");
            throw new OpenPdvException(erros.toString());
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }
}
