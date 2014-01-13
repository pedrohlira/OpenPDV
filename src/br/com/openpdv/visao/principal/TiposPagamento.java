package br.com.openpdv.visao.principal;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.TextFieldLimit;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfPagamentoTipo;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 * Classe que representa a listagem dos Tipos de Pagamento do sistema.
 *
 * @author Pedro H. Lira
 */
public class TiposPagamento extends javax.swing.JDialog {

    private static TiposPagamento tipoPagamentos;
    private Logger log;
    private int row;
    private int cod;
    private DefaultTableModel dtm;
    private CoreService<EcfPagamentoTipo> service;

    /**
     * Construtor padrao.
     */
    private TiposPagamento() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(TiposPagamento.class);
        initComponents();

        service = new CoreService<>();
        dtm = (DefaultTableModel) tabTipoPagamentos.getModel();
        tabTipoPagamentos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                row = tabTipoPagamentos.getSelectedRow();
                setDados();
            }
        });

        // colocando limites nos campos
        txtCodigo.setDocument(new TextFieldLimit(2,true));
        txtDescricao.setDocument(new TextFieldLimit(20));
        txtRede.setDocument(new TextFieldLimit(20));
    }

    /**
     * Metodo que retorna a instancia do componente.
     *
     * @return o objeto do componente.
     */
    public static TiposPagamento getInstancia() {
        if (tipoPagamentos == null) {
            tipoPagamentos = new TiposPagamento();
        }

        tipoPagamentos.setLista();
        return tipoPagamentos;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spTipoPagamentos = new javax.swing.JScrollPane();
        tabTipoPagamentos = new javax.swing.JTable();
        panTipoPagamentos = new javax.swing.JPanel();
        lblCodigo = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        lblDescricao = new javax.swing.JLabel();
        txtDescricao = new javax.swing.JTextField();
        chkTef = new javax.swing.JCheckBox();
        chkVinculado = new javax.swing.JCheckBox();
        chkDebito = new javax.swing.JCheckBox();
        lblRede = new javax.swing.JLabel();
        txtRede = new javax.swing.JTextField();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tipos de Pagamento");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tabTipoPagamentos.setAutoCreateRowSorter(true);
        tabTipoPagamentos.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabTipoPagamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Código", "Descrição", "TEF", "Vinculado", "Débito", "Rede"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabTipoPagamentos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabTipoPagamentos.setRowHeight(20);
        tabTipoPagamentos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabTipoPagamentos.setShowGrid(true);
        tabTipoPagamentos.setShowVerticalLines(false);
        tabTipoPagamentos.getTableHeader().setReorderingAllowed(false);
        spTipoPagamentos.setViewportView(tabTipoPagamentos);
        tabTipoPagamentos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tabTipoPagamentos.getColumnModel().getColumnCount() > 0) {
            tabTipoPagamentos.getColumnModel().getColumn(0).setResizable(false);
            tabTipoPagamentos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabTipoPagamentos.getColumnModel().getColumn(1).setResizable(false);
            tabTipoPagamentos.getColumnModel().getColumn(1).setPreferredWidth(75);
            tabTipoPagamentos.getColumnModel().getColumn(2).setResizable(false);
            tabTipoPagamentos.getColumnModel().getColumn(2).setPreferredWidth(175);
            tabTipoPagamentos.getColumnModel().getColumn(3).setResizable(false);
            tabTipoPagamentos.getColumnModel().getColumn(3).setPreferredWidth(50);
            tabTipoPagamentos.getColumnModel().getColumn(4).setResizable(false);
            tabTipoPagamentos.getColumnModel().getColumn(4).setPreferredWidth(75);
            tabTipoPagamentos.getColumnModel().getColumn(5).setResizable(false);
            tabTipoPagamentos.getColumnModel().getColumn(5).setPreferredWidth(75);
            tabTipoPagamentos.getColumnModel().getColumn(6).setResizable(false);
            tabTipoPagamentos.getColumnModel().getColumn(6).setPreferredWidth(125);
        }

        panTipoPagamentos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblCodigo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCodigo.setText("C:ódigo");

        txtCodigo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblDescricao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDescricao.setText("Descrição:");

        txtDescricao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        chkTef.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        chkTef.setText("TEF");

        chkVinculado.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        chkVinculado.setText("Vinculado");

        chkDebito.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        chkDebito.setText("Débito");

        lblRede.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblRede.setText("Rede:");

        txtRede.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        org.jdesktop.layout.GroupLayout panTipoPagamentosLayout = new org.jdesktop.layout.GroupLayout(panTipoPagamentos);
        panTipoPagamentos.setLayout(panTipoPagamentosLayout);
        panTipoPagamentosLayout.setHorizontalGroup(
            panTipoPagamentosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panTipoPagamentosLayout.createSequentialGroup()
                .add(10, 10, 10)
                .add(panTipoPagamentosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lblCodigo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(txtCodigo))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panTipoPagamentosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panTipoPagamentosLayout.createSequentialGroup()
                        .add(txtDescricao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 196, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chkTef)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chkVinculado)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(chkDebito))
                    .add(lblDescricao))
                .add(panTipoPagamentosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panTipoPagamentosLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(lblRede))
                    .add(panTipoPagamentosLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtRede, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 144, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(12, 12, 12))
        );
        panTipoPagamentosLayout.setVerticalGroup(
            panTipoPagamentosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panTipoPagamentosLayout.createSequentialGroup()
                .add(8, 8, 8)
                .add(panTipoPagamentosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblCodigo)
                    .add(lblDescricao)
                    .add(lblRede))
                .add(3, 3, 3)
                .add(panTipoPagamentosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtCodigo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkTef)
                    .add(chkVinculado)
                    .add(txtDescricao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtRede, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkDebito))
                .add(1, 1, 1))
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
                .add(6, 6, 6)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(spTipoPagamentos)
                        .add(panTipoPagamentos, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .add(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(spTipoPagamentos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(panTipoPagamentos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(23, 23, 23))
        );

        setSize(new java.awt.Dimension(660, 307));
        setLocationRelativeTo(null);
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
    private javax.swing.JCheckBox chkDebito;
    private javax.swing.JCheckBox chkTef;
    private javax.swing.JCheckBox chkVinculado;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblDescricao;
    private javax.swing.JLabel lblRede;
    private javax.swing.JPanel panTipoPagamentos;
    private javax.swing.JScrollPane spTipoPagamentos;
    private javax.swing.JTable tabTipoPagamentos;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtRede;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo para adicionar um novo registro.
     */
    private void novo() {
        tabTipoPagamentos.getSelectionModel().clearSelection();
        txtCodigo.requestFocus();
    }

    /**
     * Metodo que salva um novo registro ou atualiza um existente.
     */
    private void salvar() {
        if (txtCodigo.getText().equals("") || txtDescricao.getText().equals("") || txtRede.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Tipos de Pagamento", JOptionPane.INFORMATION_MESSAGE);
        } else {
            EcfPagamentoTipo pt = new EcfPagamentoTipo();
            pt.setEcfPagamentoTipoId(cod);
            pt.setEcfPagamentoTipoCodigo(txtCodigo.getText());
            pt.setEcfPagamentoTipoDescricao(txtDescricao.getText());
            pt.setEcfPagamentoTipoRede(txtRede.getText());
            pt.setEcfPagamentoTipoTef(chkTef.isSelected());
            pt.setEcfPagamentoTipoVinculado(chkVinculado.isSelected());
            pt.setEcfPagamentoTipoDebito(chkDebito.isSelected());

            try {
                service.salvar(pt);
                JOptionPane.showMessageDialog(this, "Registro salvo com sucesso.", "Tipos de Pagamento", JOptionPane.INFORMATION_MESSAGE);
                setLista();
            } catch (OpenPdvException ex) {
                log.error("Erro ao salvar o tipo de pagamento.", ex);
                JOptionPane.showMessageDialog(this, "Não foi possível salvar o registro!", "Tipos de Pagamento", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Metodo que exclui um registro do sistema.
     */
    private void excluir() {
        if (cod > 0) {
            int escolha = JOptionPane.showOptionDialog(this, "Deseja remover o registro selecionado?", "Tipos de Pagamento",
                    JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
            if (escolha == 0) {
                try {
                    service.deletar(new EcfPagamentoTipo(cod));
                    setLista();
                } catch (OpenPdvException ex) {
                    log.debug("Erro ao excluir o usuario -> " + cod, ex);
                    JOptionPane.showMessageDialog(this, "Este registro não pode ser excluído!", "Tipos de Pagamento", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um registro na listagem.", "Tipos de Pagamento", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo que seta os valores da tabela vindas do banco de dados.
     */
    private void setLista() {
        try {
            List<EcfPagamentoTipo> lista = service.selecionar(new EcfPagamentoTipo(), 0, 0, null);

            while (dtm.getRowCount() > 0) {
                dtm.removeRow(0);
            }

            for (EcfPagamentoTipo pt : lista) {
                Object[] obj = new Object[]{pt.getId(), pt.getEcfPagamentoTipoCodigo(), pt.getEcfPagamentoTipoDescricao(),
                    pt.isEcfPagamentoTipoTef(), pt.isEcfPagamentoTipoVinculado(), pt.isEcfPagamentoTipoDebito(), pt.getEcfPagamentoTipoRede()};
                dtm.addRow(obj);
            }

            row = -1;
            setDados();
        } catch (OpenPdvException ex) {
            log.error("Erro ao selecionar os Tipos de Pagamento do sistema", ex);
        }
    }

    /**
     * Metodo que seta os valores nos campos do formulario.
     */
    private void setDados() {
        if (row == -1) {
            cod = 0;
            txtCodigo.setText("");
            txtDescricao.setText("");
            chkTef.setSelected(false);
            chkVinculado.setSelected(false);
            chkDebito.setSelected(false);
            txtRede.setText("");
        } else {
            int rowModel = tabTipoPagamentos.convertRowIndexToModel(row);
            cod = Integer.valueOf(tabTipoPagamentos.getModel().getValueAt(rowModel, 0).toString());
            txtCodigo.setText(tabTipoPagamentos.getModel().getValueAt(rowModel, 1).toString());
            txtDescricao.setText(tabTipoPagamentos.getModel().getValueAt(rowModel, 2).toString());
            chkTef.setSelected(Boolean.valueOf(tabTipoPagamentos.getModel().getValueAt(rowModel, 3).toString()));
            chkVinculado.setSelected(Boolean.valueOf(tabTipoPagamentos.getModel().getValueAt(rowModel, 4).toString()));
            chkDebito.setSelected(Boolean.valueOf(tabTipoPagamentos.getModel().getValueAt(rowModel, 5).toString()));
            txtRede.setText(tabTipoPagamentos.getModel().getValueAt(rowModel, 6).toString());
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

    public JCheckBox getChkTef() {
        return chkTef;
    }

    public void setChkTef(JCheckBox chkTef) {
        this.chkTef = chkTef;
    }

    public JCheckBox getChkVinculado() {
        return chkVinculado;
    }

    public void setChkVinculado(JCheckBox chkVinculado) {
        this.chkVinculado = chkVinculado;
    }

    public JCheckBox getChkDebito() {
        return chkDebito;
    }

    public void setChkDebito(JCheckBox chkDebito) {
        this.chkDebito = chkDebito;
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

    public JLabel getLblCodigo() {
        return lblCodigo;
    }

    public void setLblCodigo(JLabel lblCodigo) {
        this.lblCodigo = lblCodigo;
    }

    public JLabel getLblDescricao() {
        return lblDescricao;
    }

    public void setLblDescricao(JLabel lblDescricao) {
        this.lblDescricao = lblDescricao;
    }

    public JLabel getLblRede() {
        return lblRede;
    }

    public void setLblRede(JLabel lblRede) {
        this.lblRede = lblRede;
    }

    public JPanel getPanTipoPagamentos() {
        return panTipoPagamentos;
    }

    public void setPanTipoPagamentos(JPanel panTipoPagamentos) {
        this.panTipoPagamentos = panTipoPagamentos;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public CoreService<EcfPagamentoTipo> getService() {
        return service;
    }

    public void setService(CoreService<EcfPagamentoTipo> service) {
        this.service = service;
    }

    public JScrollPane getSpTipoPagamentos() {
        return spTipoPagamentos;
    }

    public void setSpTipoPagamentos(JScrollPane spTipoPagamentos) {
        this.spTipoPagamentos = spTipoPagamentos;
    }

    public JTable getTabTipoPagamentos() {
        return tabTipoPagamentos;
    }

    public void setTabTipoPagamentos(JTable tabTipoPagamentos) {
        this.tabTipoPagamentos = tabTipoPagamentos;
    }

    public JTextField getTxtCodigo() {
        return txtCodigo;
    }

    public void setTxtCodigo(JTextField txtCodigo) {
        this.txtCodigo = txtCodigo;
    }

    public JTextField getTxtDescricao() {
        return txtDescricao;
    }

    public void setTxtDescricao(JTextField txtDescricao) {
        this.txtDescricao = txtDescricao;
    }

    public JTextField getTxtRede() {
        return txtRede;
    }

    public void setTxtRede(JTextField txtRede) {
        this.txtRede = txtRede;
    }
}
