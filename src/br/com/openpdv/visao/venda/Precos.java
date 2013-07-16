package br.com.openpdv.visao.venda;

import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.controlador.core.TableCellRendererNumber;
import br.com.openpdv.modelo.produto.ProdPreco;
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
public class Precos extends javax.swing.JDialog {

    private static Precos precos;
    private DefaultTableModel dtm;
    private AsyncCallback<ProdPreco> async;

    /**
     * Construtor padrao.
     */
    private Precos() {
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
    public static Precos getInstancia(AsyncCallback<ProdPreco> async, ProdProduto produto) {
        if (precos == null) {
            precos = new Precos();
        }

        precos.dtm = (DefaultTableModel) precos.tabPreco.getModel();
        precos.async = async;
        precos.setLista(produto);
        return precos;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spPreco = new javax.swing.JScrollPane();
        tabPreco = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Preços");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
        setResizable(false);

        tabPreco.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabPreco.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Embalagem", "Preço", "Barra"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabPreco.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabPreco.setColumnSelectionAllowed(true);
        tabPreco.setRowHeight(20);
        tabPreco.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabPreco.setShowGrid(true);
        tabPreco.setShowVerticalLines(false);
        tabPreco.getTableHeader().setReorderingAllowed(false);
        tabPreco.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabPrecoMouseClicked(evt);
            }
        });
        tabPreco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabPrecoKeyPressed(evt);
            }
        });
        spPreco.setViewportView(tabPreco);
        tabPreco.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabPreco.getColumnModel().getColumn(0).setMinWidth(1);
        tabPreco.getColumnModel().getColumn(0).setPreferredWidth(1);
        tabPreco.getColumnModel().getColumn(0).setMaxWidth(1);
        tabPreco.getColumnModel().getColumn(1).setResizable(false);
        tabPreco.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabPreco.getColumnModel().getColumn(2).setResizable(false);
        tabPreco.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabPreco.getColumnModel().getColumn(2).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
        tabPreco.getColumnModel().getColumn(3).setResizable(false);
        tabPreco.getColumnModel().getColumn(3).setPreferredWidth(200);

        getContentPane().add(spPreco, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-438)/2, (screenSize.height-151)/2, 438, 151);
    }// </editor-fold>//GEN-END:initComponents

    private void tabPrecoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabPrecoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ok();
        }
    }//GEN-LAST:event_tabPrecoKeyPressed

    private void tabPrecoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabPrecoMouseClicked
        if (evt.getClickCount() == 2) {
            ok();
        }
    }//GEN-LAST:event_tabPrecoMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane spPreco;
    private javax.swing.JTable tabPreco;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que seta os valores dos precos disponiveis.
     */
    private void setLista(ProdProduto produto) {
        // limpa
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }

        // adiciona o principal
        String barra = produto.getProdProdutoBarra() == null ? "" : produto.getProdProdutoBarra();
        ProdPreco precoP = new ProdPreco();
        precoP.setProdEmbalagem(produto.getProdEmbalagem());
        precoP.setProdPrecoBarra(barra);
        precoP.setProdPrecoValor(produto.getProdProdutoPreco());
        Object[] prod = new Object[]{precoP, produto.getProdEmbalagem().getId() + " - " + produto.getProdEmbalagem().getProdEmbalagemNome(),
            produto.getProdProdutoPreco(), barra};
        dtm.addRow(prod);

        // adiciona outras opcoes
        for (ProdPreco preco : produto.getProdPrecos()) {
            Object[] obj = new Object[]{preco, preco.getProdEmbalagem().getId() + " - " + preco.getProdEmbalagem().getProdEmbalagemNome(),
                preco.getProdPrecoValor(), preco.getProdPrecoBarra()};
            dtm.addRow(obj);
        }

        // seta o focus
        tabPreco.addRowSelectionInterval(0, 0);
        tabPreco.requestFocus();
    }

    /**
     * Metodo que cancela o registro.
     */
    private void cancelar() {
        setVisible(false);
        async.sucesso(null);
        Caixa.getInstancia().setJanela(null);
        dispose();
    }

    /**
     * Metodo que seleciona o preco.
     */
    private void ok() {
        int row = tabPreco.convertRowIndexToModel(tabPreco.getSelectedRow());
        if (row >= 0) {
            setVisible(false);
            async.sucesso((ProdPreco) tabPreco.getModel().getValueAt(row, 0));
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

    public AsyncCallback<ProdPreco> getAsync() {
        return async;
    }

    public void setAsync(AsyncCallback<ProdPreco> async) {
        this.async = async;
    }

    public JScrollPane getSpPreco() {
        return spPreco;
    }

    public void setSpPreco(JScrollPane spPreco) {
        this.spPreco = spPreco;
    }

    public JTable getTabPreco() {
        return tabPreco;
    }

    public void setTabPreco(JTable tabPreco) {
        this.tabPreco = tabPreco;
    }
}
