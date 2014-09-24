package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.Conexao;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.Dados;
import com.sun.jersey.api.client.WebResource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.MediaType;

/**
 * Classe que realiza a acao de enviar os dados ao servidor.
 *
 * @author Pedro H. Lira
 */
public class ComandoEnviarDadosRemoto extends ComandoEnviarDados {

    public ComandoEnviarDadosRemoto() {
        super();
    }

    public ComandoEnviarDadosRemoto(Date inicio, Date fim) {
        super(inicio, fim);
    }

    @Override
    protected <E extends Dados> List<E> enviar(String tipo, List<E> lista) throws Exception {
        WebResource wr = Conexao.getRest(Util.getConfig().getProperty("sinc.server") + "/" + tipo);
        List<E> enviados = new ArrayList<>();

        for (E obj : lista) {
            try {
                wr.type(MediaType.APPLICATION_JSON).put(obj);
                enviados.add(obj);
            } catch (Exception ex) {
                log.error("Nao enviou " + tipo + " com id -> " + obj.getId(), ex);
            }
        }
        return enviados;
    }

}
