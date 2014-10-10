package br.com.openpdv.controlador.core;

import java.awt.Component;
import java.text.NumberFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Classe que transforma visualmente a formatacao da celula da tabela para
 * valor.
 *
 * @author Pedro H. Lira
 */
public class TableCellRendererNumber extends DefaultTableCellRenderer {

    private NumberFormat nf;

    /**
     * Construtor padrao.
     *
     * @param nf o formato do numero.
     */
    public TableCellRendererNumber(NumberFormat nf) {
        this.nf = nf;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value instanceof Double){
            value = nf.format((Double) value);
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
