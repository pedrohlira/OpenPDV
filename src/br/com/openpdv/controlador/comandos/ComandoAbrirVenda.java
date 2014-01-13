package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.phdss.Util;
import br.com.openpdv.controlador.permissao.Login;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.ecf.EcfVendaProduto;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.ECF;
import br.com.phdss.EComando;
import br.com.phdss.IECF;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * Classe que realiza a acao de abrir uma venda.
 *
 * @author Pedro H. Lira
 */
public class ComandoAbrirVenda implements IComando {

    private Logger log;
    private SisCliente cliente;
    private String[] resp;
    private IECF ecf;

    /**
     * Costrutor padrao.
     */
    public ComandoAbrirVenda() {
        this(null);
    }

    /**
     * Construtor padrao.
     *
     * @param cliente o objeto do cliente selecionado.
     */
    public ComandoAbrirVenda(SisCliente cliente) {
        this.log = Logger.getLogger(ComandoAbrirVenda.class);
        this.cliente = cliente;
        this.ecf = ECF.getInstancia();
    }

    @Override
    public void executar() throws OpenPdvException {
        // abre a venda no cupom
        abrirVendaECF();
        // salva no bd
        try {
            AbrirVendaBanco();
        } catch (OpenPdvException ex) {
            ecf.enviar(EComando.ECF_CancelaCupom);
            throw ex;
        }
        // coloca na tela
        abrirVendaTela();
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    /**
     * Metodo que abre a venda no ECF.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void abrirVendaECF() throws OpenPdvException {
        ecf.enviar(EComando.ECF_CorrigeEstadoErro);
        resp = ecf.enviar(EComando.ECF_AbreCupom);
        if (IECF.ERRO.equals(resp[0])) {
            log.error("Erro ao abrir a venda no ECF. -> " + resp[1]);
            throw new OpenPdvException(resp[1]);
        }
    }

    /**
     * Metodo que abre a venda no Banco.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void AbrirVendaBanco() throws OpenPdvException {
        EcfVenda venda = new EcfVenda();
        venda.setEcfImpressora(Caixa.getInstancia().getImpressora());
        venda.setSisUsuario(Login.getOperador());
        if (cliente != null) {
            venda.setSisVendedor(cliente.getVendedor());
            if (cliente.getSisClienteId() == 0) {
                cliente = null;
            }
        }
        venda.setSisCliente(cliente);
        venda.setInformouCliente(cliente != null);
        // ccf
        resp = ecf.enviar(EComando.ECF_NumCCF);
        if (IECF.OK.equals(resp[0])) {
            venda.setEcfVendaCcf(Integer.valueOf(resp[1]));
        } else {
            log.error("Erro ao abrir a venda no BD. -> " + resp[1]);
            throw new OpenPdvException(resp[1]);
        }
        // coo
        resp = ecf.enviar(EComando.ECF_NumCupom);
        if (IECF.OK.equals(resp[0])) {
            venda.setEcfVendaCoo(Integer.valueOf(resp[1]));
        } else {
            log.error("Erro ao abrir a venda no BD. -> " + resp[1]);
            throw new OpenPdvException(resp[1]);
        }
        // data
        resp = ecf.enviar(EComando.ECF_DataHora);
        if (IECF.OK.equals(resp[0])) {
            Date data = null;
            try {
                data = new SimpleDateFormat("dd/MM/yy HH:mm:ss").parse(resp[1]);
            } catch (ParseException ex) {
                data = new Date();
            } finally {
                venda.setEcfVendaData(data);
            }
        } else {
            log.error("Erro ao abrir a venda no BD. -> " + resp[1]);
            throw new OpenPdvException(resp[1]);
        }
        venda.setEcfVendaBruto(0.00);
        venda.setEcfVendaDesconto(0.00);
        venda.setEcfVendaAcrescimo(0.00);
        venda.setEcfVendaLiquido(0.00);
        venda.setEcfVendaFechada(false);
        venda.setEcfVendaCancelada(false);

        // salva no bd
        CoreService<EcfVenda> service = new CoreService<>();
        venda = service.salvar(venda);
        venda.setEcfVendaProdutos(new ArrayList<EcfVendaProduto>());
        Caixa.getInstancia().setVenda(venda);
    }

    /**
     * Metodo que abre a venda na Tela.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void abrirVendaTela() throws OpenPdvException {
        Caixa.getInstancia().getBobina().removeAllElements();
        Caixa.getInstancia().getBobina().addElement(IECF.LS);
        EcfVenda venda = Caixa.getInstancia().getVenda();
        String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(venda.getEcfVendaData());
        Caixa.getInstancia().getBobina().addElement(data
                + "  CCF: " + Util.formataNumero(venda.getEcfVendaCcf(), 6, 0, false)
                + "  COO: " + Util.formataNumero(venda.getEcfVendaCoo(), 6, 0, false));
        Caixa.getInstancia().getBobina().addElement("                  CUPOM FISCAL                  ");
        // linha 1
        Caixa.getInstancia().getBobina().addElement("ITEM CÓDIGO         DESCRIÇÃO                   ");
        // linha 2
        Caixa.getInstancia().getBobina().addElement("QTD.     UN      VL.UNIT.(R$) ST     VL.ITEM(R$)");
        Caixa.getInstancia().getBobina().addElement(IECF.LS);
    }

    public SisCliente getCliente() {
        return cliente;
    }

    public void setCliente(SisCliente cliente) {
        this.cliente = cliente;
    }

    public String[] getResp() {
        return resp;
    }

    public void setResp(String[] resp) {
        this.resp = resp;
    }
}
