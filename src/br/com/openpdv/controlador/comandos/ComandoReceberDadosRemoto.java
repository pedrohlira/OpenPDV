package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.Conexao;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.Dados;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Classe que realiza a acao de recedor os dados do servidor.
 *
 * @author Pedro H. Lira
 */
public class ComandoReceberDadosRemoto extends ComandoReceberDados {

    private WebResource wr;

    @Override
    public <E extends Dados> List<E> receber(String tipo, GenericType<List<E>> classe) throws Exception {
        wr = Conexao.getRest(Util.getConfig().get("sinc.host") + "/" + tipo);
        List<E> lista = wr.accept(MediaType.APPLICATION_JSON).get(classe);
        return lista;
    }

    @Override
    public <E extends Dados> List<E> receber(String tipo, GenericType<List<E>> classe, MultivaluedMap<String, String> mm) throws Exception {
        wr = Conexao.getRest(Util.getConfig().get("sinc.host") + "/" + tipo);
        List<E> lista = wr.queryParams(mm).accept(MediaType.APPLICATION_JSON).get(classe);
        return lista;
    }

}
