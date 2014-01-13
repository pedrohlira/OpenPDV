/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.EBusca;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Classe que totaliza os pagamentos realizados.
 *
 * @author Pedro H. Lira
 */
public class ComandoTotalizarPagamentos implements IComando {

    private CoreService service;
    private Date data;

    /**
     * Construtor padrao.
     *
     * @param data a data de totalizacao.
     */
    public ComandoTotalizarPagamentos(Date data) {
        this.service = new CoreService();
        this.data = data;
    }

    @Override
    public void executar() throws OpenPdvException {
        // dia seguinte
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.add(Calendar.DAY_OF_MONTH, 1);

        // filtros padroes
        FiltroTexto ft = new FiltroTexto("ecfPagamentoEstorno", ECompara.IGUAL, "N");
        FiltroData fd1 = new FiltroData("ecfPagamentoData", ECompara.MAIOR_IGUAL, data);
        FiltroData fd2 = new FiltroData("ecfPagamentoData", ECompara.MENOR, cal.getTime());

        // deleta os totais dos pagamento da data passada.
        FiltroData fdDel1 = new FiltroData("ecfPagamentoTotaisData", ECompara.MAIOR_IGUAL, data);
        FiltroData fdDel2 = new FiltroData("ecfPagamentoTotaisData", ECompara.MENOR, cal.getTime());
        GrupoFiltro gfDel = new GrupoFiltro(EJuncao.E, new IFiltro[]{fdDel1, fdDel2});
        Sql sql = new Sql(new EcfPagamentoTotais(), EComandoSQL.EXCLUIR, gfDel);
        service.executar(sql);

        // seleciona todas as formas de pagamentos
        List<EcfPagamentoTipo> tipos = service.selecionar(new EcfPagamentoTipo(), 0, 0, null);
        for (EcfPagamentoTipo tipo : tipos) {
            // soma todos os valores pagos no dia pelo tipo nao estornado
            FiltroObjeto fo = new FiltroObjeto("ecfPagamentoTipo", ECompara.IGUAL, tipo);
            GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fo, ft, fd1, fd2});
            Object obj = service.buscar(new EcfPagamento(), "ecfPagamentoValor", EBusca.SOMA, gf);
            double valor = Double.valueOf(obj != null ? obj.toString() : "0");

            // se valor maior que zero adicionar a tabela
            if (valor > 0.00) {
                EcfPagamentoTotais ept = new EcfPagamentoTotais();
                ept.setEcfPagamentoTipo(tipo);
                ept.setEcfPagamentoTotaisData(data);
                ept.setEcfPagamentoTotaisValor(valor);
                ept.setEcfPagamentoTotaisDocumento("CUPOM FISCAL");
                service.salvar(ept);
            }

            // caso o tipo seja dinheiro, adiciona os pagamentos das NF e NFe
            if (tipo.getEcfPagamentoTipoCodigo().equals(Util.getConfig().get("ecf.dinheiro"))) {
                // soma as notas fiscais do dia que nao foram canceladas
                FiltroData fd = new FiltroData("ecfNotaData", ECompara.IGUAL, data);
                FiltroBinario fb = new FiltroBinario("ecfNotaCancelada", ECompara.IGUAL, false);
                GrupoFiltro gf1 = new GrupoFiltro(EJuncao.E, new IFiltro[]{fd, fb});
                Object obj1 = service.buscar(new EcfNota(), "ecfNotaLiquido", EBusca.SOMA, gf1);
                double valor1 = Double.valueOf(obj1 != null ? obj1.toString() : "0");

                // se valor maior que zero adicionar a tabela
                if (valor1 > 0.00) {
                    EcfPagamentoTotais ept = new EcfPagamentoTotais();
                    ept.setEcfPagamentoTipo(tipo);
                    ept.setEcfPagamentoTotaisData(data);
                    ept.setEcfPagamentoTotaisValor(valor1);
                    ept.setEcfPagamentoTotaisDocumento("NOTA FISCAL");
                    service.salvar(ept);
                }

                // somas as nfe do dia que nao foram canceladas ou inutilizadas
                FiltroTexto ft1 = new FiltroTexto("ecfNotaEletronicaStatus", ECompara.IGUAL, ENotaStatus.AUTORIZADO.toString());
                FiltroData fd3 = new FiltroData("ecfNotaEletronicaData", ECompara.MAIOR_IGUAL, data);
                FiltroData fd4 = new FiltroData("ecfNotaEletronicaData", ECompara.MENOR, cal.getTime());
                GrupoFiltro gf2 = new GrupoFiltro(EJuncao.E, new IFiltro[]{ft1, fd3, fd4});
                Object obj2 = service.buscar(new EcfNotaEletronica(), "ecfNotaEletronicaValor", EBusca.SOMA, gf2);
                double valor2 = Double.valueOf(obj2 != null ? obj2.toString() : "0");

                // se valor maior que zero adicionar a tabela
                if (valor2 > 0.00) {
                    EcfPagamentoTotais ept = new EcfPagamentoTotais();
                    ept.setEcfPagamentoTipo(tipo);
                    ept.setEcfPagamentoTotaisData(data);
                    ept.setEcfPagamentoTotaisValor(valor2);
                    ept.setEcfPagamentoTotaisDocumento("NFE");
                    service.salvar(ept);
                }
            }
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }
}
