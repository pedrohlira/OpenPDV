package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.Conexao;
import br.com.openpdv.controlador.permissao.Login;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.sistema.SisUsuario;
import br.com.phdss.ECF;
import br.com.phdss.EComando;
import br.com.phdss.IECF;
import br.com.phdss.Util;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe que realiza as operacoes de Cartao Presente.
 *
 * @author Pedro H. Lira
 */
public class ComandoCartaoPresente implements IComando {

    private final IECF ecf;
    private final String acao;
    private final String numero;
    private final SisUsuario usuario;
    private BigDecimal valor;

    /**
     * Construtor padrao.
     *
     * @param acao o comando de ativarCartao ou desativarCartao.
     * @param numero o numero do cartao presente.
     * @param usuario o vendedor que esta usando o cartao.
     */
    public ComandoCartaoPresente(String acao, String numero, SisUsuario usuario) {
        this.ecf = ECF.getInstancia();
        this.acao = acao;
        this.numero = numero;
        this.usuario = usuario == null ? Login.getOperador() : usuario;
    }

    @Override
    public void executar() throws OpenPdvException {
        try {
            WebResource wr = Conexao.getRest(Util.getConfig().getProperty("sinc.server") + "/" + acao + "/" + usuario.getId());
            String resp = wr.type(MediaType.TEXT_PLAIN).accept(MediaType.TEXT_PLAIN).put(String.class, numero);
            JSONObject json = new JSONObject(resp);
            if (json.getString("status").equalsIgnoreCase("OK")) {
                this.valor = new BigDecimal(json.getDouble("valor")).setScale(2, RoundingMode.HALF_UP);
            } else {
                throw new OpenPdvException(json.getString("msg"));
            }
        } catch (UniformInterfaceException | JSONException ex) {
            throw new OpenPdvException("Erro de comunicação com o servidor!", ex);
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    /**
     * Metodo que imprime o recibo do cartao presente.
     *
     * @throws OpenPdvException dispara caso nao consiga.
     */
    public void recibo() throws OpenPdvException {
        // 1 via
        abrirCupom();
        fecharCupom(texto());
        // 2 via
        abrirCupom();
        fecharCupom(texto());
    }

    private void abrirCupom() throws OpenPdvException {
        // abrindo o relatorio
        String[] resp = ecf.enviar(EComando.ECF_AbreRelatorioGerencial, Util.getConfig().getProperty("ecf.relcartao"));
        if (resp[0].equals("ERRO")) {
            ecf.enviar(EComando.ECF_CorrigeEstadoErro);
            throw new OpenPdvException(resp[1]);
        }
    }

    private String texto() {
        String preco = "R$ " + Util.formataNumero(this.valor.doubleValue(), 1, 2, true);
        StringBuilder sb = new StringBuilder();
        // cabecalho
        sb.append(IECF.LD).append(IECF.SL);
        sb.append("<N>").append(Util.formataTexto("CARTÃO PRESENTE", " ", IECF.COL, Util.EDirecao.AMBOS)).append("</N>").append(IECF.SL);
        sb.append(IECF.LD).append(IECF.SL);
        sb.append("VENDEDOR: ").append(usuario.getSisUsuarioLogin()).append("  <N>NÚMERO: ").append(this.numero).append("</N>").append(IECF.SL);
        // dados do cartao
        sb.append(IECF.SL).append(IECF.SL);
        sb.append("<N>").append(Util.formataTexto(preco.replace(",", "."), " ", IECF.COL, Util.EDirecao.AMBOS)).append("</N>");
        sb.append(IECF.SL).append(IECF.SL);
        return sb.toString();
    }

    private void fecharCupom(String texto) throws OpenPdvException {
        // envia o comando com todo o texto
        String[] resp = ecf.enviar(EComando.ECF_LinhaRelatorioGerencial, texto);
        if (resp[0].equals(IECF.ERRO)) {
            ecf.enviar(EComando.ECF_CorrigeEstadoErro);
            throw new OpenPdvException(resp[1]);
        } else {
            ecf.enviar(EComando.ECF_FechaRelatorio);
            ecf.enviar(EComando.ECF_CortaPapel);
        }
    }

    //GETs
    public String getAcao() {
        return acao;
    }

    public String getNumero() {
        return numero;
    }

    public SisUsuario getUsuario() {
        return usuario;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
