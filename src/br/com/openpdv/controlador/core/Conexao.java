package br.com.openpdv.controlador.core;

import br.com.openpdv.controlador.PAF;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
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

    private static Map<String, String> dados = new HashMap<>();
    private static Logger log;

    /**
     * Lendo os dados do config externo.
     */
    static {
        log = Logger.getLogger(Conexao.class);
        String chaveSenha = "";
        String usuarioSenha = "";
        String arquivoSenha = "";

        // descriptografando os dados de acesso do BD
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("conf" + System.getProperty("file.separator") + "banco.properties")) {
            props.load(fis);
            for (String chave : props.stringPropertyNames()) {
                if (chave.endsWith("user")) {
                    dados.put(chave, PAF.descriptar(props.getProperty(chave)));
                } else if (chave.endsWith("password")) {
                    chaveSenha = chave;
                    usuarioSenha = PAF.descriptar(props.getProperty(chave));
                } else if (chave.endsWith("filepwd")) {
                    arquivoSenha = PAF.descriptar(props.getProperty(chave));
                } else {
                    dados.put(chave, props.getProperty(chave));
                }
            }
            dados.put(chaveSenha, arquivoSenha + " " + usuarioSenha);
        } catch (Exception ex) {
            log.error("Nao carregou os dados de acesso ao banco de dados.", ex);
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
     * <p/>
     * @throws Exception dispara caso nao consiga conectar.
     */
    public static EntityManagerFactory getInstancia() throws Exception {
        return getInstancia("openpdv_pu");
    }

    /**
     * Metodo que retorna uma instancia do manipulador de entidades.
     *
     * @param pu o nome da unidade de persistência que deseja utilizar.
     * <p/>
     * @return um objeto que manipula as entidades no banco de dados.
     * <p/>
     * @throws Exception dispara caso nao consiga conectar.
     */
    public static EntityManagerFactory getInstancia(String pu) throws Exception {
        return getInstancia(pu, dados);
    }

    /**
     * Metodo que retorna uma instancia do manipulador de entidades.
     *
     * @param pu    o nome da unidade de persistência que deseja utilizar.
     * @param dados conjunto de informacoes para acesso ao banco.
     * <p/>
     * @return um objeto que manipula as entidades no banco de dados.
     * <p/>
     * @throws Exception dispara caso nao consiga conectar.
     */
    public static EntityManagerFactory getInstancia(String pu, Map<String, String> dados) throws Exception {
        return Persistence.createEntityManagerFactory(pu, dados);
    }
}
