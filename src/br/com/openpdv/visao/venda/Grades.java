package br.com.openpdv.visao.venda;

import br.com.openpdv.controlador.core.AsyncDoubleBack;
import br.com.openpdv.controlador.core.TableCellRendererNumber;
import br.com.openpdv.modelo.produto.ProdGrade;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Classe que representa a listagem dos precos do produto.
 *
 * @author Pedro H. Lira
 */
public class Grades extends javax.swing.JDialog {

    private static Grades grades;
    private DefaultTableModel dtm;
    private ProdProduto produto;
    private AsyncDoubleBack<ProdProduto, ProdGrade> async;

    /**
     * Construtor padrao.
     */
    private Grades() {
        super(Caixa.getInstancia());
        initComponents();
    }

    /**
     * Metodo que retorna a instancia unica de Precos.
     *
     * @param async o metodo assincrono de resposta.
     * @param produto o produto pesquisado.
     * @return o objeto de Precos.
     */
    public static Grades getInstancia(AsyncDoubleBack<ProdProduto, ProdGrade> async, ProdProduto produto) {
        if (grades == null) {
            grades = new Grades();
        }

        grades.dtm = (DefaultTableModel) grades.tabGrade.getModel();
        grades.async = async;
        grades.produto = produto;
        grades.setLista();
        return grades;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spGrade = new javax.swing.JScrollPane();
        tabGrade = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Grades");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
        setResizable(false);

        tabGrade.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabGrade.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Barra", "Tamanho", "Cor", "Opção", "Estoque"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabGrade.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabGrade.setRowHeight(20);
        tabGrade.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabGrade.setShowGrid(true);
        tabGrade.setShowVerticalLines(false);
        tabGrade.getTableHeader().setReorderingAllowed(false);
        tabGrade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabGradeMouseClicked(evt);
            }
        });
        tabGrade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabGradeKeyPressed(evt);
            }
        });
        spGrade.setViewportView(tabGrade);
        tabGrade.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabGrade.getColumnModel().getColumn(0).setMinWidth(1);
        tabGrade.getColumnModel().getColumn(0).setPreferredWidth(1);
        tabGrade.getColumnModel().getColumn(0).setMaxWidth(1);
        tabGrade.getColumnModel().getColumn(1).setResizable(false);
        tabGrade.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabGrade.getColumnModel().getColumn(2).setResizable(false);
        tabGrade.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabGrade.getColumnModel().getColumn(3).setResizable(false);
        tabGrade.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabGrade.getColumnModel().getColumn(4).setResizable(false);
        tabGrade.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabGrade.getColumnModel().getColumn(5).setResizable(false);
        tabGrade.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabGrade.getColumnModel().getColumn(5).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getNumberInstance()));

        getContentPane().add(spGrade, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-576)/2, (screenSize.height-151)/2, 576, 151);
    }// </editor-fold>//GEN-END:initComponents

    private void tabGradeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabGradeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ok();
        }
    }//GEN-LAST:event_tabGradeKeyPressed

    private void tabGradeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabGradeMouseClicked
        if (evt.getClickCount() == 2) {
            ok();
        }
    }//GEN-LAST:event_tabGradeMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane spGrade;
    private javax.swing.JTable tabGrade;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que seta os valores dos precos disponiveis.
     */
    private void setLista() {
        // limpa
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }

        // adiciona as grades
        for (ProdGrade grade : produto.getProdGrades()) {
            Object[] obj = new Object[]{grade, grade.getProdGradeBarra(), grade.getProdGradeTamanho(), grade.getProdGradeCor(), grade.getProdGradeOpcao(), grade.getProdGradeEstoque()};
            dtm.addRow(obj);
        }

        // seta o focus
        tabGrade.addRowSelectionInterval(0, 0);
        tabGrade.requestFocus();
    }

    /**
     * Metodo que seleciona o preco.
     */
    private void ok() {
        int row = tabGrade.convertRowIndexToModel(tabGrade.getSelectedRow());
        if (row >= 0) {
            setVisible(false);
            async.sucesso(produto, (ProdGrade) tabGrade.getModel().getValueAt(row, 0));
            Caixa.getInstancia().setJanela(null);
            dispose();
        }
    }

    // GETs e SETs
    public DefaultTableModel getDtm() {
        return dtm;
    }

    public void setDtm(DefaultTableModel dtm) {
        this.dtm = dtm;
    }

    public AsyncDoubleBack<ProdProduto, ProdGrade> getAsync() {
        return async;
    }

    public void setAsync(AsyncDoubleBack<ProdProduto, ProdGrade> async) {
        this.async = async;
    }

    public JScrollPane getSpGrade() {
        return spGrade;
    }

    public void setSpGrade(JScrollPane spGrade) {
        this.spGrade = spGrade;
    }

    public JTable getTabGrade() {
        return tabGrade;
    }

    public void setTabGrade(JTable tabGrade) {
        this.tabGrade = tabGrade;
    }
}
