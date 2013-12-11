package br.com.openpdv.controlador.comandos;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.rest.RestContexto;
import br.com.phdss.controlador.PAF;
import com.sun.jersey.api.client.GenericType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.eclipse.persistence.jaxb.MarshallerProperties;

/**
 * Classe que realiza a acao de recedor os dados do servidor.
 *
 * @author Pedro H. Lira
 */
public class ComandoReceberDadosLocal extends ComandoReceberDados {

    private File arq;

    @Override
    public <E extends Dados> List<E> receber(String tipo, GenericType<List<E>> classe) throws Exception {
        StringBuilder sb = new StringBuilder(PAF.getPathArquivos());
        sb.append("importar").append(System.getProperty("file.separator"));
        sb.append(tipo).append(".json");
        arq = new File(sb.toString());

        if (arq.exists()) {
            JAXBContext contexto = new RestContexto().getContext(null);
            Unmarshaller um = contexto.createUnmarshaller();
            um.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
            um.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
            List<E> lista = (List<E>) um.unmarshal(arq);
            arq.delete();
            return lista;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public <E extends Dados> List<E> receber(String tipo, GenericType<List<E>> classe, MultivaluedMap<String, String> mm) throws Exception {
        return receber(tipo, classe);
    }

}
