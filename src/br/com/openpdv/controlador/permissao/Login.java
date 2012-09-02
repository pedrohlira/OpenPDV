package br.com.openpdv.controlador.permissao;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.sistema.SisUsuario;
import org.h2.command.dml.BackupCommand;
import org.h2.tools.Backup;

/**
 * Classe que representa o login do sistema, do tipo Singleton.
 *
 * @author Pedro H. Lira
 */
public class Login {

    private static SisUsuario operador;

    private Login() {
    }

    /**
     * Metodo para efetuar o login no sistema.
     *
     * @param usuarioOp nome do operador
     * @param senhaOp   senha do operador
     * <p/>
     * @throws OpenPdvException caso ocorra alguma exececao
     */
    public static void logar(String usuarioOp, String senhaOp) throws OpenPdvException {
        PermissaoService service = new PermissaoService();
        operador = service.validar(usuarioOp, senhaOp);

        if (operador == null) {
            throw new OpenPdvException("Usuário ou Senha do operador inválido!");
        }
    }

    /**
     * Metodo que autoriza uma acao do sistema, verificando se e um gerente.
     *
     * @param usuarioGe o nome do usuario Gerente
     * @param senhaGe   a senha do usuario Gerente
     * <p/>
     * @return um inteiro com o valor em porcentagem de desconto permitido.
     * <p/>
     * @throws OpenPdvException caso ocorra alguma excecao ou os dados sejam
     * invalidos
     */
    public static int autorizar(String usuarioGe, String senhaGe) throws OpenPdvException {
        PermissaoService service = new PermissaoService();
        SisUsuario gerente = service.validar(usuarioGe, senhaGe);
        if (gerente == null || !gerente.isSisUsuarioGerente()) {
            throw new OpenPdvException("Usuário ou Senha do gerente inválido!");
        } else {
            return gerente.getSisUsuarioDesconto();
        }
    }

    /**
     * Metodo que sai do sistema.
     */
    public static void sair() {
        System.exit(0);
    }

    // GETs e SETs
    public static SisUsuario getOperador() {
        return operador;
    }
}
