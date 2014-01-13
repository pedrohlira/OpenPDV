package br.com.openpdv.controlador.core;

import br.com.openpdv.rest.RestContexto;
import br.com.phdss.Util;
import br.com.phdss.controlador.PAF;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;

/**
 * Classe que representa a conexão com o banco de dados.
 *
 * @author Pedro H. Lira
 */
public class Conexao {

    public static final Properties DADOS = new Properties();
    private static final Logger log = Logger.getLogger(Conexao.class);

    /**
     * Lendo os dados do config externo.
     */
    static {
        // descriptografando os dados de acesso do BD
        try {
            Util.descriptografar("db" + System.getProperty("file.separator") + "banco.txt", DADOS);
            // arrumando a url do banco
            String url = DADOS.getProperty("eclipselink.jdbc.url", "").replace("+", "=");
            DADOS.setProperty("eclipselink.jdbc.url", url);
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    /**
     * Construtor padrao.
     */
    private Conexao() {
    }

    /**
     * Metodo que retorna uma instancia do manipulador de entidades.
     *
     * @return um objeto que manipula as entidades no banco de dados.
     * @throws Exception dispara caso nao consiga conectar.
     */
    public static EntityManagerFactory getInstancia() throws Exception {
        return getInstancia("openpdv_pu");
    }

    /**
     * Metodo que retorna uma instancia do manipulador de entidades.
     *
     * @param pu o nome da unidade de persistência que deseja utilizar.
     * @return um objeto que manipula as entidades no banco de dados.
     * @throws Exception dispara caso nao consiga conectar.
     */
    public static EntityManagerFactory getInstancia(String pu) throws Exception {
        return getInstancia(pu, DADOS);
    }

    /**
     * Metodo que retorna uma instancia do manipulador de entidades.
     *
     * @param pu o nome da unidade de persistência que deseja utilizar.
     * @param dados conjunto de informacoes para acesso ao banco.
     * @return um objeto que manipula as entidades no banco de dados.
     * @throws Exception dispara caso nao consiga conectar.
     */
    public static EntityManagerFactory getInstancia(String pu, Properties dados) throws Exception {
        if (!dados.isEmpty()) {
            return Persistence.createEntityManagerFactory(pu, dados);
        } else {
            throw new Exception("Dados vazios, verificar arquivo db/banco.txt");
        }
    }

    /**
     * Metodo que gera um objeto de comunicaxao com o RESTful do sistema.
     *
     * @param path o caminho especifico do comando no servidor.
     * @return um objeto de referencia web.
     */
    public static WebResource getRest(String path) {
        Client c = getClientRest();

        // set a url completa
        StringBuilder sb = new StringBuilder();
        sb.append(Util.getConfig().get("sinc.servidor")).append(":");
        sb.append(Util.getConfig().get("sinc.porta")).append(path);
        WebResource wr = c.resource(sb.toString());

        return wr;
    }

    /**
     * Metodo que gera um cliente rest usando os parametros padroes de
     * comunicacao.
     *
     * @return um objeto de acesso ao RestFull.
     */
    public static Client getClientRest() {
        // seta o cliente
        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(RestContexto.class);
        Client c = Client.create(cc);
        c.setFollowRedirects(true);
        c.setConnectTimeout(Integer.valueOf(Util.getConfig().get("sinc.timeout")) * 1000);
        c.setReadTimeout(Integer.valueOf(Util.getConfig().get("sinc.timeout")) * 1000);

        // cria a autenticacao
        String usuario = PAF.AUXILIAR.getProperty("cli.cnpj");
        String senha = PAF.AUXILIAR.getProperty("ecf.serie").split(";")[0];

        // criptografa a senha se estiver setado no config
        if (Boolean.valueOf(Util.getConfig().get("sinc.criptografar"))) {
            senha = Util.encriptar(senha);
        }

        // seta os filtros
        c.addFilter(new HTTPBasicAuthFilter(usuario, senha));
        c.addFilter(new GZIPContentEncodingFilter(true));
        return c;
    }
}
