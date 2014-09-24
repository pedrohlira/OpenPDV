package br.com.openpdv.visao.principal;

import br.com.openpdv.controlador.core.TextFieldLimit;
import br.com.phdss.Util;
import br.com.openpdv.controlador.permissao.PermissaoService;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.sistema.SisUsuario;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

/**
 * Classe que representa a listagem dos usuarios do sistema.
 *
 * @author Pedro H. Lira
 */
public class Usuarios extends javax.swing.JDialog {

    private static Usuarios usuarios;
    private Logger log;
    private int row;
    private int cod;
    private DefaultTableModel dtm;
    private PermissaoService service;

    /**
     * Construtor padrao.
     */
    private Usuarios() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Usuarios.class);
        initComponents();

        service = new PermissaoService();
        dtm = (DefaultTableModel) tabUsuarios.getModel();
        tabUsuarios.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                row = tabUsuarios.getSelectedRow();
                setDados();
            }
        });

        // colocando limites nos campos
        txtLogin.setDocument(new TextFieldLimit(40));
        txtSenha.setDocument(new TextFieldLimit(40));
        txtDesconto.setDocument(new TextFieldLimit(10));
    }

    /**
     * Metodo que retorna a instancia do componente.
     *
     * @return o objeto do componente.
     */
    public static Usuarios getInstancia() {
        if (usuarios == null) {
            usuarios = new Usuarios();
        }

        usuarios.setLista();
        return usuarios;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spUsuarios = new javax.swing.JScrollPane();
        tabUsuarios = new javax.swing.JTable();
        panUsuarios = new javax.swing.JPanel();
        lblUsuario = new javax.swing.JLabel();
        txtLogin = new javax.swing.JTextField();
        lblSenha = new javax.swing.JLabel();
        txtSenha = new javax.swing.JPasswordField();
        lblDesconto = new javax.swing.JLabel();
        txtDesconto = new javax.swing.JFormattedTextField();
        chkAtivo = new javax.swing.JCheckBox();
        chkCaixa = new javax.swing.JCheckBox();
        chkGerente = new javax.swing.JCheckBox();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Usuários");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tabUsuarios.setAutoCreateRowSorter(true);
        tabUsuarios.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Login", "Senha", "Desconto", "Ativo", "Caixa", "Gerente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
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
        tabUsuarios.setRowHeight(20);
        tabUsuarios.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabUsuarios.setShowVerticalLines(false);
        tabUsuarios.getTableHeader().setReorderingAllowed(false);
        spUsuarios.setViewportView(tabUsuarios);
        tabUsuarios.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tabUsuarios.getColumnModel().getColumnCount() > 0) {
            tabUsuarios.getColumnModel().getColumn(0).setResizable(false);
            tabUsuarios.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabUsuarios.getColumnModel().getColumn(1).setResizable(false);
            tabUsuarios.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabUsuarios.getColumnModel().getColumn(2).setMinWidth(1);
            tabUsuarios.getColumnModel().getColumn(2).setPreferredWidth(1);
            tabUsuarios.getColumnModel().getColumn(2).setMaxWidth(1);
            tabUsuarios.getColumnModel().getColumn(3).setResizable(false);
            tabUsuarios.getColumnModel().getColumn(3).setPreferredWidth(50);
            tabUsuarios.getColumnModel().getColumn(4).setResizable(false);
            tabUsuarios.getColumnModel().getColumn(4).setPreferredWidth(50);
            tabUsuarios.getColumnModel().getColumn(5).setResizable(false);
            tabUsuarios.getColumnModel().getColumn(5).setPreferredWidth(50);
            tabUsuarios.getColumnModel().getColumn(6).setResizable(false);
            tabUsuarios.getColumnModel().getColumn(6).setPreferredWidth(50);
        }

        panUsuarios.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblUsuario.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblUsuario.setText("Login:");

        txtLogin.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblSenha.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblSenha.setText("Senha:");

        txtSenha.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblDesconto.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDesconto.setText("Desconto:");

        txtDesconto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtDesconto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDesconto.setText("0");
        txtDesconto.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        chkAtivo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        chkAtivo.setText("Ativo");

        chkCaixa.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        chkCaixa.setText("Caixa");

        chkGerente.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        chkGerente.setText("Gerente");

        org.jdesktop.layout.GroupLayout panUsuariosLayout = new org.jdesktop.layout.GroupLayout(panUsuarios);
        panUsuarios.setLayout(panUsuariosLayout);
        panUsuariosLayout.setHorizontalGroup(
            panUsuariosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panUsuariosLayout.createSequentialGroup()
                .add(panUsuariosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panUsuariosLayout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(lblUsuario))
                    .add(panUsuariosLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(txtLogin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 183, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panUsuariosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panUsuariosLayout.createSequentialGroup()
                        .add(lblSenha)
                        .add(113, 113, 113)
                        .add(lblDesconto)
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(panUsuariosLayout.createSequentialGroup()
                        .add(txtSenha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 139, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(txtDesconto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chkAtivo)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(chkCaixa)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chkGerente, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panUsuariosLayout.setVerticalGroup(
            panUsuariosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panUsuariosLayout.createSequentialGroup()
                .add(panUsuariosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panUsuariosLayout.createSequentialGroup()
                        .add(8, 8, 8)
                        .add(lblUsuario))
                    .add(panUsuariosLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(panUsuariosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblSenha)
                            .add(lblDesconto))
                        .add(3, 3, 3)
                        .add(panUsuariosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(panUsuariosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(txtSenha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(txtLogin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(panUsuariosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(txtDesconto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(chkAtivo)
                                .add(chkGerente)
                                .add(chkCaixa)))))
                .add(3, 3, 3))
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
                        .add(spUsuarios)
                        .add(panUsuarios, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(spUsuarios, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(panUsuarios, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(647, 322));
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
    private javax.swing.JCheckBox chkAtivo;
    private javax.swing.JCheckBox chkCaixa;
    private javax.swing.JCheckBox chkGerente;
    private javax.swing.JLabel lblDesconto;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel panUsuarios;
    private javax.swing.JScrollPane spUsuarios;
    private javax.swing.JTable tabUsuarios;
    private javax.swing.JFormattedTextField txtDesconto;
    private javax.swing.JTextField txtLogin;
    private javax.swing.JPasswordField txtSenha;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo para adicionar um novo registro.
     */
    private void novo() {
        tabUsuarios.getSelectionModel().clearSelection();
        txtLogin.requestFocus();
    }

    /**
     * Metodo que salva um novo registro ou atualiza um existente.
     */
    private void salvar() {
        // criptografia da senha
        ConfigurablePasswordEncryptor sha = new ConfigurablePasswordEncryptor();
        sha.setAlgorithm("SHA-1");
        sha.setPlainDigest(true);
        sha.setStringOutputType("hexadecimal");
        SisUsuario usuario = null;
        boolean erro = false;

        // verifica se e um novo ou edicao
        if (row >= 0) {
            if (txtLogin.getText().equals("") || txtDesconto.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Os campos de Login e Desconto são obrigatórios!", "Usuários", JOptionPane.INFORMATION_MESSAGE);
                erro = true;
            } else {
                usuario = new SisUsuario(cod);
                if (txtSenha.getPassword().length > 0) {
                    String senha = sha.encryptPassword(new String(txtSenha.getPassword()));
                    usuario.setSisUsuarioSenha(senha);
                } else {
                    usuario.setSisUsuarioSenha(tabUsuarios.getModel().getValueAt(row, 2).toString());
                }
            }
        } else {
            if (txtLogin.getText().equals("") || txtSenha.getPassword().length == 0 || txtDesconto.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Usuários", JOptionPane.INFORMATION_MESSAGE);
                erro = true;
            } else {
                usuario = new SisUsuario();
                String senha = sha.encryptPassword(new String(txtSenha.getPassword()));
                usuario.setSisUsuarioSenha(senha);
            }
        }

        if (!erro) {
            // pega os dados dos outros campos
            usuario.setSisUsuarioLogin(txtLogin.getText());
            usuario.setSisUsuarioDesconto(Integer.valueOf(txtDesconto.getText()));
            usuario.setSisUsuarioAtivo(chkAtivo.isSelected());
            usuario.setSisUsuarioCaixa(chkCaixa.isSelected());
            usuario.setSisUsuarioGerente(chkGerente.isSelected());

            try {
                service.salvar(usuario);
                JOptionPane.showMessageDialog(this, "Registro salvo com sucesso.", "Usuários", JOptionPane.INFORMATION_MESSAGE);
                setLista();
            } catch (OpenPdvException ex) {
                log.error("Erro ao salvar o usuario.", ex);
                JOptionPane.showMessageDialog(this, "Não foi possível salvar o registro!", "Usuários", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Metodo que exclui um registro do sistema.
     */
    private void excluir() {
        if (cod > 0) {
            int escolha = JOptionPane.showOptionDialog(this, "Deseja remover o registro selecionado?", "Usuários",
                    JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
            if (escolha == 0) {
                try {
                    service.deletar(new SisUsuario(cod));
                    setLista();
                } catch (OpenPdvException ex) {
                    log.debug("Erro ao excluir o usuario -> " + cod, ex);
                    JOptionPane.showMessageDialog(this, "Este registro não pode ser excluído!", "Usuários", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um registro na listagem.", "Usuários", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo que seta os valores da tabela vindas do banco de dados.
     */
    private void setLista() {
        try {
            List<SisUsuario> lista = service.selecionar(new SisUsuario(), 0, 0, null);

            while (dtm.getRowCount() > 0) {
                dtm.removeRow(0);
            }

            for (SisUsuario usuario : lista) {
                Object[] obj = new Object[]{usuario.getId(), usuario.getSisUsuarioLogin(), usuario.getSisUsuarioSenha(),
                    usuario.getSisUsuarioDesconto(), usuario.isSisUsuarioAtivo(), usuario.isSisUsuarioCaixa(), usuario.isSisUsuarioGerente()};
                dtm.addRow(obj);
            }

            row = -1;
            setDados();
        } catch (OpenPdvException ex) {
            log.error("Erro ao selecionar os usuarios do sistema", ex);
        }
    }

    /**
     * Metodo que seta os valores nos campos do formulario.
     */
    private void setDados() {
        if (row == -1) {
            cod = 0;
            txtLogin.setText("");
            txtSenha.setText("");
            txtDesconto.setText("0");
            chkAtivo.setSelected(false);
            chkCaixa.setSelected(false);
            chkGerente.setSelected(false);
        } else {
            int rowModel = tabUsuarios.convertRowIndexToModel(row);
            cod = Integer.valueOf(tabUsuarios.getModel().getValueAt(rowModel, 0).toString());
            txtLogin.setText(tabUsuarios.getModel().getValueAt(rowModel, 1).toString());
            txtSenha.setText("");
            txtDesconto.setText(tabUsuarios.getModel().getValueAt(rowModel, 3).toString());
            chkAtivo.setSelected(Boolean.valueOf(tabUsuarios.getModel().getValueAt(rowModel, 4).toString()));
            chkCaixa.setSelected(Boolean.valueOf(tabUsuarios.getModel().getValueAt(rowModel, 5).toString()));
            chkGerente.setSelected(Boolean.valueOf(tabUsuarios.getModel().getValueAt(rowModel, 6).toString()));
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

    public JCheckBox getChkAtivo() {
        return chkAtivo;
    }

    public void setChkAtivo(JCheckBox chkAtivo) {
        this.chkAtivo = chkAtivo;
    }

    public JCheckBox getChkCaixa() {
        return chkCaixa;
    }

    public void setChkCaixa(JCheckBox chkCaixa) {
        this.chkCaixa = chkCaixa;
    }

    public JCheckBox getChkGerente() {
        return chkGerente;
    }

    public void setChkGerente(JCheckBox chkGerente) {
        this.chkGerente = chkGerente;
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

    public JLabel getLblDesconto() {
        return lblDesconto;
    }

    public void setLblDesconto(JLabel lblDesconto) {
        this.lblDesconto = lblDesconto;
    }

    public JLabel getLblSenha() {
        return lblSenha;
    }

    public void setLblSenha(JLabel lblSenha) {
        this.lblSenha = lblSenha;
    }

    public JLabel getLblUsuario() {
        return lblUsuario;
    }

    public void setLblUsuario(JLabel lblUsuario) {
        this.lblUsuario = lblUsuario;
    }

    public JPanel getPanUsuarios() {
        return panUsuarios;
    }

    public void setPanUsuarios(JPanel panUsuarios) {
        this.panUsuarios = panUsuarios;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public PermissaoService getService() {
        return service;
    }

    public void setService(PermissaoService service) {
        this.service = service;
    }

    public JScrollPane getSpUsuarios() {
        return spUsuarios;
    }

    public void setSpUsuarios(JScrollPane spUsuarios) {
        this.spUsuarios = spUsuarios;
    }

    public JTable getTabUsuarios() {
        return tabUsuarios;
    }

    public void setTabUsuarios(JTable tabUsuarios) {
        this.tabUsuarios = tabUsuarios;
    }

    public JFormattedTextField getTxtDesconto() {
        return txtDesconto;
    }

    public void setTxtDesconto(JFormattedTextField txtDesconto) {
        this.txtDesconto = txtDesconto;
    }

    public JTextField getTxtLogin() {
        return txtLogin;
    }

    public void setTxtLogin(JTextField txtLogin) {
        this.txtLogin = txtLogin;
    }

    public JPasswordField getTxtSenha() {
        return txtSenha;
    }

    public void setTxtSenha(JPasswordField txtSenha) {
        this.txtSenha = txtSenha;
    }
}
