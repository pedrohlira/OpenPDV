package br.com.openpdv.visao.core;

import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.controlador.permissao.Login;
import br.com.openpdv.modelo.core.EModo;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.sistema.SisUsuario;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import javax.swing.*;
import org.apache.log4j.Logger;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

/**
 * Classe que representa o login de permissao do gerente para funcoes
 * especificas.
 *
 * @author Pedro H. Lira
 */
public class Gerente extends javax.swing.JDialog {

    private static Gerente gerente;
    private Logger log;
    private AsyncCallback<Integer> async;
    private SisUsuario sisGerente;

    /**
     * Construtor padrao.
     */
    private Gerente() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Gerente.class);
        initComponents();
    }

    /**
     * Metodo que retorna a instancia unica de Gerente.
     *
     * @param async objeto assincrono para resposta da acao.
     * @return o objeto de Gerente.
     */
    public static Gerente getInstancia(AsyncCallback<Integer> async) {
        if (gerente == null) {
            gerente = new Gerente();
        }

        gerente.txtGerenteUsuario.setText("");
        gerente.pswGerenteSenha.setText("");
        gerente.async = async;
        gerente.sisGerente = null;
        return gerente;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblGerenteUsuario = new javax.swing.JLabel();
        txtGerenteUsuario = new javax.swing.JTextField();
        lblGerenteSenha = new javax.swing.JLabel();
        pswGerenteSenha = new javax.swing.JPasswordField();
        separador = new javax.swing.JSeparator();
        btnOk = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gerente");
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        lblGerenteUsuario.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblGerenteUsuario.setText("Usuário:");

        txtGerenteUsuario.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtGerenteUsuario.setToolTipText("Login do gerente.");
        txtGerenteUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGerenteUsuarioKeyPressed(evt);
            }
        });

        lblGerenteSenha.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblGerenteSenha.setText("Senha:");

        pswGerenteSenha.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        pswGerenteSenha.setToolTipText("Senha do gerente.");
        pswGerenteSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pswGerenteSenhaKeyPressed(evt);
            }
        });

        btnOk.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/ok.png"))); // NOI18N
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(86, 28));
        btnOk.setMinimumSize(new java.awt.Dimension(86, 28));
        btnOk.setPreferredSize(new java.awt.Dimension(86, 28));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        btnOk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnOkKeyPressed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cancelar.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
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
                .add(20, 20, 20)
                .add(lblGerenteUsuario)
                .add(18, 18, 18)
                .add(txtGerenteUsuario, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 195, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(lblGerenteSenha)
                .add(27, 27, 27)
                .add(pswGerenteSenha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 195, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 259, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(96, 96, 96)
                .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(btnCancelar))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(23, 23, 23)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(lblGerenteUsuario))
                    .add(txtGerenteUsuario, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(6, 6, 6)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(8, 8, 8)
                        .add(lblGerenteSenha))
                    .add(pswGerenteSenha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(6, 6, 6)
                .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnCancelar)))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-300)/2, (screenSize.height-172)/2, 300, 172);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cancelar();
        }
    }//GEN-LAST:event_btnCancelarKeyPressed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        ok();
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnOkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOkKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ok();
        }
    }//GEN-LAST:event_btnOkKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cancelar();
    }//GEN-LAST:event_formWindowClosing

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        txtGerenteUsuario.requestFocus();
    }//GEN-LAST:event_formWindowActivated

    private void txtGerenteUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGerenteUsuarioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pswGerenteSenha.requestFocus();
        }
    }//GEN-LAST:event_txtGerenteUsuarioKeyPressed

    private void pswGerenteSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pswGerenteSenhaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ok();
        }
    }//GEN-LAST:event_pswGerenteSenhaKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblGerenteSenha;
    private javax.swing.JLabel lblGerenteUsuario;
    private javax.swing.JPasswordField pswGerenteSenha;
    private javax.swing.JSeparator separador;
    private javax.swing.JTextField txtGerenteUsuario;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo com a acaoo do botao OK.
     */
    private void ok() {
        // valida se preencheu todos os campos
        if (txtGerenteUsuario.getText().equals("") || pswGerenteSenha.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Gerente", JOptionPane.OK_OPTION);
        } else {
            // faz a codificao da senha em SHA-1
            ConfigurablePasswordEncryptor sha = new ConfigurablePasswordEncryptor();
            sha.setAlgorithm("SHA-1");
            sha.setPlainDigest(true);
            sha.setStringOutputType("hexadecimal");
            String senhaGe = sha.encryptPassword(new String(pswGerenteSenha.getPassword()));

            try {
                sisGerente = Login.autorizar(txtGerenteUsuario.getText(), senhaGe);
                setVisible(false);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        async.sucesso(sisGerente.getSisUsuarioDesconto());
                    }
                }).start();
            } catch (OpenPdvException ex) {
                log.debug("Erro na permissao de gerente!", ex);
                setVisible(false);
                async.falha(ex);
            } finally {
                Caixa.getInstancia().setJanela(null);
            }
        }
    }

    @Override
    public void setVisible(boolean b) {
        Caixa caixa = Caixa.getInstancia();
        if (b) {
            caixa.statusMenus(EModo.OFF);
            caixa.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } else {
            caixa.statusMenus(caixa.getModo());
            caixa.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            Caixa.getInstancia().setJanela(null);
        }
        super.setVisible(b);
    }

    /**
     * Metodo com a acao do botao Cancelar.
     */
    private void cancelar() {
        setVisible(false);
    }

    //GETs e SETs
    public AsyncCallback<Integer> getAsync() {
        return async;
    }

    public void setAsync(AsyncCallback<Integer> async) {
        this.async = async;
    }

    public SisUsuario getSisGerente() {
        return sisGerente;
    }

    public void setSisGerente(SisUsuario sisGerente) {
        this.sisGerente = sisGerente;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

    public JButton getBtnOk() {
        return btnOk;
    }

    public void setBtnOk(JButton btnOk) {
        this.btnOk = btnOk;
    }

    public JLabel getLblGerenteSenha() {
        return lblGerenteSenha;
    }

    public void setLblGerenteSenha(JLabel lblGerenteSenha) {
        this.lblGerenteSenha = lblGerenteSenha;
    }

    public JLabel getLblGerenteUsuario() {
        return lblGerenteUsuario;
    }

    public void setLblGerenteUsuario(JLabel lblGerenteUsuario) {
        this.lblGerenteUsuario = lblGerenteUsuario;
    }

    public JPasswordField getPswGerenteSenha() {
        return pswGerenteSenha;
    }

    public void setPswGerenteSenha(JPasswordField pswGerenteSenha) {
        this.pswGerenteSenha = pswGerenteSenha;
    }

    public JSeparator getSeparador() {
        return separador;
    }

    public void setSeparador(JSeparator separador) {
        this.separador = separador;
    }

    public JTextField getTxtGerenteUsuario() {
        return txtGerenteUsuario;
    }

    public void setTxtGerenteUsuario(JTextField txtGerenteUsuario) {
        this.txtGerenteUsuario = txtGerenteUsuario;
    }
}
