package br.com.openpdv.visao.principal;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.TextFieldLimit;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 * Classe que representa a listagem das embalagens do sistema.
 *
 * @author Pedro H. Lira
 */
public class Embalagens extends javax.swing.JDialog {
    
    private static Embalagens embalagens;
    private Logger log;
    private int row;
    private int cod;
    private DefaultTableModel dtm;
    private CoreService<ProdEmbalagem> service;

    /**
     * Construtor padrao.
     */
    private Embalagens() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Embalagens.class);
        initComponents();
        
        service = new CoreService<>();
        dtm = (DefaultTableModel) tabEmbalagens.getModel();
        tabEmbalagens.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                row = tabEmbalagens.getSelectedRow();
                setDados();
            }
        });

        // colocando limites nos campos
        txtNome.setDocument(new TextFieldLimit(6));
        txtUnidades.setDocument(new TextFieldLimit(10, true));
        txtDescricao.setDocument(new TextFieldLimit(100));
    }

    /**
     * Metodo que retorna a instancia do componente.
     *
     * @return o objeto do componente.
     */
    public static Embalagens getInstancia() {
        if (embalagens == null) {
            embalagens = new Embalagens();
        }
        
        embalagens.setLista();
        return embalagens;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spEmbalagens = new javax.swing.JScrollPane();
        tabEmbalagens = new javax.swing.JTable();
        panEmbalagens = new javax.swing.JPanel();
        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        lblUnidades = new javax.swing.JLabel();
        txtUnidades = new javax.swing.JFormattedTextField();
        lblDescricao = new javax.swing.JLabel();
        txtDescricao = new javax.swing.JTextField();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Embalegens");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tabEmbalagens.setAutoCreateRowSorter(true);
        tabEmbalagens.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabEmbalagens.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Nome", "Unidades", "Descrição"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
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
        tabEmbalagens.setCellSelectionEnabled(false);
        tabEmbalagens.setRowHeight(20);
        tabEmbalagens.setRowSelectionAllowed(true);
        tabEmbalagens.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabEmbalagens.setShowGrid(true);
        tabEmbalagens.setShowVerticalLines(false);
        tabEmbalagens.getTableHeader().setReorderingAllowed(false);
        spEmbalagens.setViewportView(tabEmbalagens);
        tabEmbalagens.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabEmbalagens.getColumnModel().getColumn(0).setResizable(false);
        tabEmbalagens.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabEmbalagens.getColumnModel().getColumn(1).setResizable(false);
        tabEmbalagens.getColumnModel().getColumn(1).setPreferredWidth(50);
        tabEmbalagens.getColumnModel().getColumn(2).setResizable(false);
        tabEmbalagens.getColumnModel().getColumn(2).setPreferredWidth(50);
        tabEmbalagens.getColumnModel().getColumn(3).setResizable(false);
        tabEmbalagens.getColumnModel().getColumn(3).setPreferredWidth(400);

        panEmbalagens.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblNome.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblNome.setText("Nome:");

        txtNome.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblUnidades.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblUnidades.setText("Unidades:");

        txtUnidades.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtUnidades.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtUnidades.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblDescricao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDescricao.setText("Descrição:");

        txtDescricao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        org.jdesktop.layout.GroupLayout panEmbalagensLayout = new org.jdesktop.layout.GroupLayout(panEmbalagens);
        panEmbalagens.setLayout(panEmbalagensLayout);
        panEmbalagensLayout.setHorizontalGroup(
            panEmbalagensLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panEmbalagensLayout.createSequentialGroup()
                .add(6, 6, 6)
                .add(panEmbalagensLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblNome))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panEmbalagensLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtUnidades, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblUnidades))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panEmbalagensLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panEmbalagensLayout.createSequentialGroup()
                        .add(lblDescricao)
                        .add(0, 387, Short.MAX_VALUE))
                    .add(txtDescricao))
                .addContainerGap())
        );
        panEmbalagensLayout.setVerticalGroup(
            panEmbalagensLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panEmbalagensLayout.createSequentialGroup()
                .add(12, 12, 12)
                .add(panEmbalagensLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblNome)
                    .add(panEmbalagensLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(lblUnidades)
                        .add(lblDescricao)))
                .add(3, 3, 3)
                .add(panEmbalagensLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtUnidades, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtDescricao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        btnNovo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/novo.png"))); // NOI18N
        btnNovo.setText("Novo");
        btnNovo.setMaximumSize(new java.awt.Dimension(100, 30));
        btnNovo.setMinimumSize(new java.awt.Dimension(100, 30));
        btnNovo.setPreferredSize(new java.awt.Dimension(100, 30));
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });
        btnNovo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnNovoKeyPressed(evt);
            }
        });

        btnSalvar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/salvar.png"))); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.setMaximumSize(new java.awt.Dimension(100, 30));
        btnSalvar.setMinimumSize(new java.awt.Dimension(100, 30));
        btnSalvar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });
        btnSalvar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSalvarKeyPressed(evt);
            }
        });

        btnExcluir.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/excluir.png"))); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.setMaximumSize(new java.awt.Dimension(100, 30));
        btnExcluir.setMinimumSize(new java.awt.Dimension(100, 30));
        btnExcluir.setPreferredSize(new java.awt.Dimension(100, 30));
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });
        btnExcluir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnExcluirKeyPressed(evt);
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
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(spEmbalagens, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 648, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(27, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(panEmbalagens, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(spEmbalagens, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(panEmbalagens, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-681)/2, (screenSize.height-308)/2, 681, 308);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        salvar();
    }//GEN-LAST:event_btnSalvarActionPerformed
    
    private void btnSalvarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalvarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            salvar();
        }
    }//GEN-LAST:event_btnSalvarKeyPressed
    
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
        Caixa.getInstancia().setJanela(null);
    }//GEN-LAST:event_btnCancelarActionPerformed
    
    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            dispose();
            Caixa.getInstancia().setJanela(null);
        }
    }//GEN-LAST:event_btnCancelarKeyPressed
    
    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        novo();
    }//GEN-LAST:event_btnNovoActionPerformed
    
    private void btnNovoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNovoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            novo();
        }
    }//GEN-LAST:event_btnNovoKeyPressed
    
    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btnExcluirActionPerformed
    
    private void btnExcluirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnExcluirKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            excluir();
        }
    }//GEN-LAST:event_btnExcluirKeyPressed
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Caixa.getInstancia().setJanela(null);
    }//GEN-LAST:event_formWindowClosing
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JLabel lblDescricao;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblUnidades;
    private javax.swing.JPanel panEmbalagens;
    private javax.swing.JScrollPane spEmbalagens;
    private javax.swing.JTable tabEmbalagens;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtNome;
    private javax.swing.JFormattedTextField txtUnidades;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo para adicionar um novo registro.
     */
    private void novo() {
        tabEmbalagens.getSelectionModel().clearSelection();
        txtNome.requestFocus();
    }

    /**
     * Metodo que salva um novo registro ou atualiza um existente.
     */
    private void salvar() {
        if (txtNome.getText().equals("") || txtUnidades.getText().equals("") || Integer.valueOf(txtUnidades.getText()) < 1 || txtDescricao.getText().equals("") || txtNome.getText().equalsIgnoreCase(txtDescricao.getText())) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!\nUnidades precisa ser maior que zero.\nNome não pode ser igual a descrição.", "Embalagens", JOptionPane.INFORMATION_MESSAGE);
        } else {
            ProdEmbalagem embalagem = new ProdEmbalagem(cod);
            embalagem.setProdEmbalagemNome(txtNome.getText());
            embalagem.setProdEmbalagemUnidade(Integer.valueOf(txtUnidades.getText()));
            embalagem.setProdEmbalagemDescricao(txtDescricao.getText());
            
            try {
                service.salvar(embalagem);
                JOptionPane.showMessageDialog(this, "Registro salvo com sucesso.", "Embalagens", JOptionPane.INFORMATION_MESSAGE);
                setLista();
            } catch (OpenPdvException ex) {
                log.error("Erro ao salvar a embalagem.", ex);
                JOptionPane.showMessageDialog(this, "Não foi possível salvar o registro!", "Embalagens", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Metodo que exclui um registro do sistema.
     */
    private void excluir() {
        if (cod > 0) {
            int escolha = JOptionPane.showOptionDialog(this, "Deseja remover o registro selecionado?", "Embalagens",
                    JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
            if (escolha == 0) {
                try {
                    service.deletar(new ProdEmbalagem(cod));
                    setLista();
                } catch (OpenPdvException ex) {
                    log.debug("Erro ao excluir a embalagem -> " + cod, ex);
                    JOptionPane.showMessageDialog(this, "Esta registro não pode ser excluído!", "Embalagens", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um registro na listagem.", "Embalagens", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo que seta os valores da tabela vindas do banco de dados.
     */
    private void setLista() {
        try {
            List<ProdEmbalagem> lista = service.selecionar(new ProdEmbalagem(), 0, 0, null);
            
            while (dtm.getRowCount() > 0) {
                dtm.removeRow(0);
            }
            
            for (ProdEmbalagem embalagem : lista) {
                Object[] obj = new Object[]{embalagem.getId(), embalagem.getProdEmbalagemNome(), embalagem.getProdEmbalagemUnidade(), embalagem.getProdEmbalagemDescricao()};
                dtm.addRow(obj);
            }
            
            row = -1;
            setDados();
        } catch (OpenPdvException ex) {
            log.error("Erro ao selecionar as embalagens do sistema", ex);
        }
    }

    /**
     * Metodo que seta os valores nos campos do formulario.
     */
    private void setDados() {
        if (row == -1) {
            cod = 0;
            txtNome.setText("");
            txtUnidades.setText("1");
            txtDescricao.setText("");
        } else {
            int rowModel = tabEmbalagens.convertRowIndexToModel(row);
            cod = Integer.valueOf(tabEmbalagens.getModel().getValueAt(rowModel, 0).toString());
            txtNome.setText(tabEmbalagens.getModel().getValueAt(rowModel, 1).toString());
            txtUnidades.setText(tabEmbalagens.getModel().getValueAt(rowModel, 2).toString());
            txtDescricao.setText(tabEmbalagens.getModel().getValueAt(rowModel, 3).toString());
        }
    }

    // GETs e SETs
    public JButton getBtnCancelar() {
        return btnCancelar;
    }
    
    public void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }
    
    public JButton getBtnExcluir() {
        return btnExcluir;
    }
    
    public void setBtnExcluir(JButton btnExcluir) {
        this.btnExcluir = btnExcluir;
    }
    
    public JButton getBtnNovo() {
        return btnNovo;
    }
    
    public void setBtnNovo(JButton btnNovo) {
        this.btnNovo = btnNovo;
    }
    
    public JButton getBtnSalvar() {
        return btnSalvar;
    }
    
    public void setBtnSalvar(JButton btnSalvar) {
        this.btnSalvar = btnSalvar;
    }
    
    public int getCod() {
        return cod;
    }
    
    public void setCod(int cod) {
        this.cod = cod;
    }
    
    public DefaultTableModel getDtm() {
        return dtm;
    }
    
    public void setDtm(DefaultTableModel dtm) {
        this.dtm = dtm;
    }
    
    public JLabel getLblDescricao() {
        return lblDescricao;
    }
    
    public void setLblDescricao(JLabel lblDescricao) {
        this.lblDescricao = lblDescricao;
    }
    
    public JLabel getLblNome() {
        return lblNome;
    }
    
    public void setLblNome(JLabel lblNome) {
        this.lblNome = lblNome;
    }
    
    public JLabel getLblUnidades() {
        return lblUnidades;
    }
    
    public void setLblUnidades(JLabel lblUnidades) {
        this.lblUnidades = lblUnidades;
    }
    
    public JPanel getPanEmbalagens() {
        return panEmbalagens;
    }
    
    public void setPanEmbalagens(JPanel panEmbalagens) {
        this.panEmbalagens = panEmbalagens;
    }
    
    public int getRow() {
        return row;
    }
    
    public void setRow(int row) {
        this.row = row;
    }
    
    public CoreService<ProdEmbalagem> getService() {
        return service;
    }
    
    public void setService(CoreService<ProdEmbalagem> service) {
        this.service = service;
    }
    
    public JScrollPane getSpEmbalagens() {
        return spEmbalagens;
    }
    
    public void setSpEmbalagens(JScrollPane spEmbalagens) {
        this.spEmbalagens = spEmbalagens;
    }
    
    public JTable getTabEmbalagens() {
        return tabEmbalagens;
    }
    
    public void setTabEmbalagens(JTable tabEmbalagens) {
        this.tabEmbalagens = tabEmbalagens;
    }
    
    public JTextField getTxtDescricao() {
        return txtDescricao;
    }
    
    public void setTxtDescricao(JTextField txtDescricao) {
        this.txtDescricao = txtDescricao;
    }
    
    public JTextField getTxtNome() {
        return txtNome;
    }
    
    public void setTxtNome(JTextField txtNome) {
        this.txtNome = txtNome;
    }
    
    public JFormattedTextField getTxtUnidades() {
        return txtUnidades;
    }
    
    public void setTxtUnidades(JFormattedTextField txtUnidades) {
        this.txtUnidades = txtUnidades;
    }
}
