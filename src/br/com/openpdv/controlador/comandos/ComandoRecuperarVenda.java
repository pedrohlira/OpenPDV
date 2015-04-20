package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.ecf.EcfVendaProduto;
import br.com.openpdv.visao.core.Caixa;
import java.util.List;

/**
 * Classe que realiza a acao de recuperacao de uma venda.
 *
 * @author Pedro H. Lira
 */
public class ComandoRecuperarVenda implements IComando {

    @Override
    public void executar() throws OpenPdvException {
        // recupera a ultima venda do sistema.
        CoreService service = new CoreService();
        List<EcfVenda> vendas = service.selecionar(new EcfVenda(), 0, 1, null);
        EcfVenda venda = vendas.get(0);
        Caixa.getInstancia().setVenda(venda);
        // abre a venda na tela
        new ComandoAbrirVenda().abrirVendaTela();
        // adiciona os itens na tela
        ComandoAdicionarItem addItem = new ComandoAdicionarItem();
        for (EcfVendaProduto vp : venda.getEcfVendaProdutos()) {
            addItem.setVendaProduto(vp);
            addItem.adicionarItemTela(vp.getEcfVendaProdutoOrdem() + "");
            if (vp.getEcfVendaProdutoCancelado()) {
                new ComandoCancelarItem(vp).cancelarItemTela();
            }
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // acao nao disponivel
    }
}
