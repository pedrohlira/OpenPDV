package br.com.openpdv.controlador.comandos;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.rest.RestContexto;
import br.com.phdss.Util;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.eclipse.persistence.jaxb.MarshallerProperties;

/**
 * Classe que realiza a acao de enviar os dados ao servidor.
 *
 * @author Pedro H. Lira
 */
public class ComandoEnviarDadosLocal extends ComandoEnviarDados {

    private FileWriter fw;

    public ComandoEnviarDadosLocal() {
        super();
    }

    public ComandoEnviarDadosLocal(Date inicio, Date fim) {
        super(inicio, fim);
    }

    public ComandoEnviarDadosLocal(int primeiro, int ultimo) {
        super(primeiro, ultimo);
    }

    @Override
    protected <E extends Dados> List<E> enviar(String tipo, List<E> lista) throws Exception {
        StringBuilder sb = new StringBuilder(Util.getPathArquivos());
        sb.append("exportar").append(System.getProperty("file.separator"));
        sb.append(tipo).append("_").append(new Date().getTime()).append(".json");
        fw = new FileWriter(sb.toString());

        JAXBContext contexto = new RestContexto().getContext(null);
        Marshaller m = contexto.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
        m.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
        m.marshal(lista, fw);
        fw.close();
        return lista;
    }

}
