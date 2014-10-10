package br.com.openpdv.controlador.permissao;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.sistema.SisUsuario;

/**
 * Classe que implementa a chamada no servidor da função de entrar no sistema,
 * acessando os dados para autenticar o usuário junto ao servidor.
 *
 * @author Pedro H. Lira
 */
public class PermissaoService extends CoreService<SisUsuario> {

    /**
     * Construtor padrao.
     */
    public PermissaoService() {
        super(PermissaoService.class);
    }

    /**
     * Metodo que valida o usuario no sistema.
     *
     * @param usuario login do usuario.
     * @param senha   senha do usuario.
     * @return Um objeto SisUsuario caso consiga logar.
     * @throws OpenPdvException dispara uma excecao caso nao consiga.
     */
    public SisUsuario validar(String usuario, String senha) throws OpenPdvException {
        // cria os dois filtros contendo os valores de login
        FiltroTexto ft1 = new FiltroTexto("sisUsuarioLogin", ECompara.IGUAL, usuario);
        FiltroTexto ft2 = new FiltroTexto("sisUsuarioSenha", ECompara.IGUAL, senha);
        FiltroBinario fb1 = new FiltroBinario("sisUsuarioAtivo", ECompara.IGUAL, true);
        FiltroBinario fb2 = new FiltroBinario("sisUsuarioCaixa", ECompara.IGUAL, true);
        FiltroGrupo gf = new FiltroGrupo(Filtro.E, ft1, ft2, fb1, fb2);

        try {
            return selecionar(new SisUsuario(), gf);
        } catch (Exception ex) {
            log.error("Erro ao validar usuario.", ex);
            throw new OpenPdvException(ex.getMessage());
        }
    }
}
