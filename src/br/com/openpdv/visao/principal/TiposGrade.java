package br.com.openpdv.visao.principal;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.TextFieldLimit;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.produto.ProdGradeTipo;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 * Classe que representa a listagem dos tipo de grade do sistema.
 *
 * @author Pedro H. Lira
 */
public class TiposGrade extends javax.swing.JDialog {

    private static TiposGrade tipos;
    private Logger log;
    private int row;
    private int cod;
    private DefaultTableModel dtm;
    private CoreService<ProdGradeTipo> service;

    /**
     * Construtor padrao.
     */
    private TiposGrade() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(ProdGradeTipo.class);
        initComponents();

        service = new CoreService<>();
        dtm = (DefaultTableModel) tabTipos.getModel();
        tabTipos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                row = tabTipos.getSelectedRow();
                setDados();
            }
        });

        // colocando limites nos campos
        txtNome.setDocument(new TextFieldLimit(50));
    }

    /**
     * Metodo que retorna a instancia do componente.
     *
     * @return o objeto do componente.
     */
    public static TiposGrade getInstancia() {
        if (tipos == null) {
            tipos = new TiposGrade();
        }

        tipos.setLista();
        return tipos;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spTipos = new javax.swing.JScrollPane();
        tabTipos = new javax.swing.JTable();
        panTipos = new javax.swing.JPanel();
        lblOpcao = new javax.swing.JLabel();
        cmbOpcao = new javax.swing.JComboBox();
        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tipos de Grade");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tabTipos.setAutoCreateRowSorter(true);
        tabTipos.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabTipos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Opção", "Nome"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabTipos.setRowHeight(20);
        tabTipos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabTipos.setShowGrid(true);
        tabTipos.setShowVerticalLines(false);
        tabTipos.getTableHeader().setReorderingAllowed(false);
        spTipos.setViewportView(tabTipos);
        tabTipos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabTipos.getColumnModel().getColumn(0).setResizable(false);
        tabTipos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabTipos.getColumnModel().getColumn(1).setResizable(false);
        tabTipos.getColumnModel().getColumn(1).setPreferredWidth(50);
        tabTipos.getColumnModel().getColumn(2).setResizable(false);
        tabTipos.getColumnModel().getColumn(2).setPreferredWidth(400);

        panTipos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblOpcao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblOpcao.setText("Opção:");

        cmbOpcao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmbOpcao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "T - [TAMANHO]", "C - [COR]", "O - [OPÇÃO]" }));

        lblNome.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblNome.setText("Nome:");

        txtNome.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        org.jdesktop.layout.GroupLayout panTiposLayout = new org.jdesktop.layout.GroupLayout(panTipos);
        panTipos.setLayout(panTiposLayout);
        panTiposLayout.setHorizontalGroup(
            panTiposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panTiposLayout.createSequentialGroup()
                .addContainerGap()
                .add(panTiposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cmbOpcao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblOpcao))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panTiposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panTiposLayout.createSequentialGroup()
                        .add(lblNome)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(txtNome))
                .addContainerGap())
        );
        panTiposLayout.setVerticalGroup(
            panTiposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panTiposLayout.createSequentialGroup()
                .addContainerGap()
                .add(panTiposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblOpcao)
                    .add(lblNome))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panTiposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cmbOpcao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(spTipos, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                    .add(panTipos, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(spTipos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(panTipos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-612)/2, (screenSize.height-321)/2, 612, 321);
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
    private javax.swing.JComboBox cmbOpcao;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblOpcao;
    private javax.swing.JPanel panTipos;
    private javax.swing.JScrollPane spTipos;
    private javax.swing.JTable tabTipos;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo para adicionar um novo registro.
     */
    private void novo() {
        tabTipos.getSelectionModel().clearSelection();
        txtNome.requestFocus();
    }

    /**
     * Metodo que salva um novo registro ou atualiza um existente.
     */
    private void salvar() {
        if (txtNome.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Tipos de Grade", JOptionPane.INFORMATION_MESSAGE);
        } else {
            ProdGradeTipo tipo = new ProdGradeTipo(cod);
            tipo.setProdGradeTipoNome(txtNome.getText());
            String[] opc = cmbOpcao.getSelectedItem().toString().split(" - ");
            tipo.setProdGradeTipoOpcao(opc[0].charAt(0));
            try {
                service.salvar(tipo);
                JOptionPane.showMessageDialog(this, "Registro salvo com sucesso.", "Tipos de Grade", JOptionPane.INFORMATION_MESSAGE);
                setLista();
            } catch (OpenPdvException ex) {
                log.error("Erro ao salvar o tipo de grade.", ex);
                JOptionPane.showMessageDialog(this, "Não foi possível salvar o registro!", "Tipos de Grade", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Metodo que exclui um registro do sistema.
     */
    private void excluir() {
        if (cod > 0) {
            int escolha = JOptionPane.showOptionDialog(this, "Deseja remover o registro selecionado?", "Tipos de Grade",
                    JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
            if (escolha == 0) {
                try {
                    service.deletar(new ProdGradeTipo(cod));
                    setLista();
                } catch (OpenPdvException ex) {
                    log.debug("Erro ao excluir o tipo de grade -> " + cod, ex);
                    JOptionPane.showMessageDialog(this, "Esta registro não pode ser excluído!", "Tipos de Grade", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um registro na listagem.", "Tipos de Grade", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo que seta os valores da tabela vindas do banco de dados.
     */
    private void setLista() {
        try {
            List<ProdGradeTipo> lista = service.selecionar(new ProdGradeTipo(), 0, 0, null);

            while (dtm.getRowCount() > 0) {
                dtm.removeRow(0);
            }

            for (ProdGradeTipo tipo : lista) {
                Object[] obj = new Object[]{tipo.getId(), tipo.getProdGradeTipoOpcao(), tipo.getProdGradeTipoNome()};
                dtm.addRow(obj);
            }

            row = -1;
            setDados();
        } catch (OpenPdvException ex) {
            log.error("Erro ao selecionar os tipo de grade do sistema", ex);
        }
    }

    /**
     * Metodo que seta os valores nos campos do formulario.
     */
    private void setDados() {
        if (row == -1) {
            cod = 0;
            cmbOpcao.setSelectedIndex(0);
            txtNome.setText("");
        } else {
            int rowModel = tabTipos.convertRowIndexToModel(row);
            cod = Integer.valueOf(tabTipos.getModel().getValueAt(rowModel, 0).toString());
            Util.selecionarCombo(cmbOpcao, tabTipos.getModel().getValueAt(rowModel, 1).toString());
            txtNome.setText(tabTipos.getModel().getValueAt(rowModel, 2).toString());
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

    public JLabel getLblNome() {
        return lblNome;
    }

    public void setLblNome(JLabel lblNome) {
        this.lblNome = lblNome;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public CoreService<ProdGradeTipo> getService() {
        return service;
    }

    public void setService(CoreService<ProdGradeTipo> service) {
        this.service = service;
    }

    public JTextField getTxtNome() {
        return txtNome;
    }

    public void setTxtNome(JTextField txtNome) {
        this.txtNome = txtNome;
    }

    public JComboBox getCmbOpcao() {
        return cmbOpcao;
    }

    public void setCmbOpcao(JComboBox cmbOpcao) {
        this.cmbOpcao = cmbOpcao;
    }

    public JLabel getLblOpcao() {
        return lblOpcao;
    }

    public void setLblOpcao(JLabel lblOpcao) {
        this.lblOpcao = lblOpcao;
    }

    public JPanel getPanTipos() {
        return panTipos;
    }

    public void setPanTipos(JPanel panTipos) {
        this.panTipos = panTipos;
    }

    public JScrollPane getSpTipos() {
        return spTipos;
    }

    public void setSpTipos(JScrollPane spTipos) {
        this.spTipos = spTipos;
    }

    public JTable getTabTipos() {
        return tabTipos;
    }

    public void setTabTipos(JTable tabTipos) {
        this.tabTipos = tabTipos;
    }
}
