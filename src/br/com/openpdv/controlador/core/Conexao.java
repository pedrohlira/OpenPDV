package br.com.openpdv.controlador.core;

import br.com.phdss.controlador.PAF;
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
            PAF.descriptografar("db" + System.getProperty("file.separator") + "banco.txt", DADOS);
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
}
