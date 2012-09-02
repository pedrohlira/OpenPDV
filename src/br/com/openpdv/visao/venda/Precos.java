package br.com.openpdv.visao.venda;

import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.modelo.produto.ProdPreco;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
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
        separador = new javax.swing.JSeparator();
        btnOK = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Preços");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

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
        tabPreco.setCellSelectionEnabled(false);
        tabPreco.setRowHeight(20);
        tabPreco.setRowSelectionAllowed(true);
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
        tabPreco.getColumnModel().getColumn(1).setMinWidth(100);
        tabPreco.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabPreco.getColumnModel().getColumn(1).setMaxWidth(100);
        tabPreco.getColumnModel().getColumn(2).setMinWidth(100);
        tabPreco.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabPreco.getColumnModel().getColumn(2).setMaxWidth(100);
        tabPreco.getColumnModel().getColumn(3).setMinWidth(200);
        tabPreco.getColumnModel().getColumn(3).setPreferredWidth(200);
        tabPreco.getColumnModel().getColumn(3).setMaxWidth(200);

        btnOK.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/ok.png"))); // NOI18N
        btnOK.setText("OK");
        btnOK.setMaximumSize(new java.awt.Dimension(100, 30));
        btnOK.setMinimumSize(new java.awt.Dimension(100, 30));
        btnOK.setPreferredSize(new java.awt.Dimension(100, 30));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        btnOK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnOKKeyPressed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cancelar.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setMaximumSize(new java.awt.Dimension(100, 30));
        btnCancelar.setMinimumSize(new java.awt.Dimension(100, 30));
        btnCancelar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        btnCancelar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCancelarKeyPressed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(btnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 426, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(spPreco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 426, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(spPreco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-449)/2, (screenSize.height-219)/2, 449, 219);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cancelar();
        }
    }//GEN-LAST:event_btnCancelarKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cancelar();
    }//GEN-LAST:event_formWindowClosing

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        ok();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnOKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOKKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ok();
        }
    }//GEN-LAST:event_btnOKKeyPressed

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
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnOK;
    private javax.swing.JSeparator separador;
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
    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

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

    public JButton getBtnOK() {
        return btnOK;
    }

    public void setBtnOK(JButton btnOK) {
        this.btnOK = btnOK;
    }

    public JSeparator getSeparador() {
        return separador;
    }

    public void setSeparador(JSeparator separador) {
        this.separador = separador;
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
