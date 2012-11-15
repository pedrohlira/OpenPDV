package br.com.openpdv.rest;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.EcfImpressora;
import br.com.phdss.controlador.PAF;
import com.sun.jersey.core.util.Base64;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;
import org.apache.log4j.Logger;

/**
 * Classe abstrata que fornece metodos padroes do REST e sua validacao no
 * servidor.
 *
 * @author Pedro H. Lira
 */
public abstract class ARest {

    /**
     * O cabecalho de envio de dados no contexto atual.
     */
    @Context
    protected HttpHeaders headers;
    /**
     * O objeto de persistencia dos dados.
     */
    protected CoreService service;
    /**
     * O cnpj informado na validacao.
     */
    protected String cnpj;
    /**
     * O serial do ECF informado na validacao.
     */
    protected String serie;
    /**
     * O objeto de log do sistema.
     */
    protected Logger log;

    /**
     * Construtor padrao.
     */
    public ARest() {
        service = new CoreService();
    }

    /**
     * Metodo que retorna o WADL para acessar o REST.
     *
     * @return um texto em formato XML.
     *
     * @throws RestException dispara um erro caso nao consiga.
     */
    protected String ajuda() throws RestException {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<center>Acesse a URL abaixo:</center><br>");
        sb.append("<center><a href='/application.wadl'>WADL</a></center>");
        sb.append("</html>");
        return sb.toString();
    }

    /**
     * Metodo que recupera no sistema a impressora pela serie informada.
     *
     * @param serie o campos usado como filtro.
     *
     * @return um objeto de impressora.
     *
     * @throws RestException dispara um erro caso nao consiga.
     */
    protected EcfImpressora getImp(String serie) throws RestException {
        try {
            FiltroTexto ft = new FiltroTexto("ecfImpressoraSerie", ECompara.IGUAL, serie);
            FiltroBinario fb = new FiltroBinario("ecfImpressoraAtivo", ECompara.IGUAL, true);
            GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{ft, fb});
            return (EcfImpressora) service.selecionar(new EcfImpressora(), gf);
        } catch (Exception ex) {
            log.error(ex);
            throw new RestException(ex);
        }
    }

    /**
     * Metodo que autoriza o acesso do cliente ao servidor REST, usando as
     * informacoes enviadas pela Autorization Basic (CNPJ do cliente, SERIE do
     * ECF criptograda com a chave privada)
     *
     * @throws RestException dispara um erro caso nao consiga.
     */
    protected void autorizar() throws RestException {
        try {
            // recupera os dados de autenticacao
            String header = headers.getRequestHeader("authorization").get(0);
            header = header.substring("Basic ".length());
            String[] creds = Base64.base64Decode(header).split(":");
            cnpj = creds[0];
            serie = PAF.descriptar(creds[1]);

            // realiza a validacao
            if (cnpj.equals(PAF.AUXILIAR.getProperty("cli.cnpj"))) {
                if (getImp(serie) == null) {
                    throw new RestException(Status.UNAUTHORIZED);
                }
            } else {
                throw new RestException(Status.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            log.error(ex);
            throw new RestException("Informar os dados de autenticacao!");
        }
    }
}
