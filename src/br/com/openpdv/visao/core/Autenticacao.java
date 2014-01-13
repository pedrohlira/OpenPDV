package br.com.openpdv.visao.core;

import br.com.openpdv.controlador.comandos.ComandoRecuperarVenda;
import br.com.openpdv.controlador.permissao.Login;
import br.com.phdss.ECF;
import br.com.phdss.fiscal.ACBR;
import br.com.phdss.EComando;
import br.com.phdss.EEstado;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.*;
import org.apache.log4j.Logger;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

/**
 * Classe de inicio do sistema.
 *
 * @author Pedro H. Lira
 */
public class Autenticacao extends JDialog {

    private static Autenticacao autenticacao;
    private Logger log;

    /**
     * Construtor principal.
     */
    private Autenticacao() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Autenticacao.class);
        initComponents();
        if (System.getProperty("os.name").contains("Windows")) {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/openpdv/imagens/logo.png")));
        }
    }

    /**
     * Metodo que retorna a instancia unica da Autenticacao.
     *
     * @return o objeto de Autenticacao.
     */
    public static Autenticacao getInstancia() {
        if (autenticacao == null) {
            autenticacao = new Autenticacao();
        }
        return autenticacao;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        separador = new javax.swing.JSeparator();
        btnEntrar = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        btnConsulta = new javax.swing.JButton();
        btnFiscal = new javax.swing.JButton();
        panOperador = new javax.swing.JPanel();
        lblOperadorUsuario = new javax.swing.JLabel();
        txtOperadorUsuario = new javax.swing.JTextField();
        lblOperadorSenha = new javax.swing.JLabel();
        pswOperadorSenha = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setModal(true);
        setResizable(false);
        setUndecorated(true);

        btnEntrar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnEntrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/entrar.png"))); // NOI18N
        btnEntrar.setText("Entrar");
        btnEntrar.setToolTipText("Logar no sistema.");
        btnEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarActionPerformed(evt);
            }
        });
        btnEntrar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEntrarKeyPressed(evt);
            }
        });

        btnSair.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/sair.png"))); // NOI18N
        btnSair.setText("Sair");
        btnSair.setToolTipText("Sair do sistema.");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });
        btnSair.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSairKeyPressed(evt);
            }
        });

        btnConsulta.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnConsulta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/pesquisa.png"))); // NOI18N
        btnConsulta.setText("Consulta");
        btnConsulta.setToolTipText("Somente modo consulta.");
        btnConsulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultaActionPerformed(evt);
            }
        });
        btnConsulta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnConsultaKeyPressed(evt);
            }
        });

        btnFiscal.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnFiscal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/fiscal.png"))); // NOI18N
        btnFiscal.setText("Menu Fiscal");
        btnFiscal.setToolTipText("");
        btnFiscal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiscalActionPerformed(evt);
            }
        });
        btnFiscal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnFiscalKeyPressed(evt);
            }
        });

        panOperador.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Operador", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Serif", 1, 12))); // NOI18N
        panOperador.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        panOperador.setMinimumSize(new java.awt.Dimension(300, 100));
        panOperador.setPreferredSize(new java.awt.Dimension(300, 100));

        lblOperadorUsuario.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblOperadorUsuario.setText("Usuário:");

        txtOperadorUsuario.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtOperadorUsuario.setToolTipText("Login do usuário.");
        txtOperadorUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOperadorUsuarioKeyPressed(evt);
            }
        });

        lblOperadorSenha.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblOperadorSenha.setText("Senha:");

        pswOperadorSenha.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        pswOperadorSenha.setToolTipText("Senha do usuário.");
        pswOperadorSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pswOperadorSenhaKeyPressed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout panOperadorLayout = new org.jdesktop.layout.GroupLayout(panOperador);
        panOperador.setLayout(panOperadorLayout);
        panOperadorLayout.setHorizontalGroup(
            panOperadorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panOperadorLayout.createSequentialGroup()
                .add(6, 6, 6)
                .add(panOperadorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblOperadorSenha)
                    .add(lblOperadorUsuario))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panOperadorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pswOperadorSenha)
                    .add(txtOperadorUsuario))
                .addContainerGap())
        );
        panOperadorLayout.setVerticalGroup(
            panOperadorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panOperadorLayout.createSequentialGroup()
                .add(14, 14, 14)
                .add(panOperadorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panOperadorLayout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(lblOperadorUsuario))
                    .add(txtOperadorUsuario, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(6, 6, 6)
                .add(panOperadorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panOperadorLayout.createSequentialGroup()
                        .add(8, 8, 8)
                        .add(lblOperadorSenha))
                    .add(pswOperadorSenha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(btnEntrar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnConsulta, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnFiscal)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnSair, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(separador)
                    .add(panOperador, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(panOperador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnConsulta)
                    .add(btnSair)
                    .add(btnEntrar)
                    .add(btnFiscal))
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-455)/2, (screenSize.height-175)/2, 455, 175);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarActionPerformed
        entrar();
    }//GEN-LAST:event_btnEntrarActionPerformed

    private void btnConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultaActionPerformed
        consultar();
    }//GEN-LAST:event_btnConsultaActionPerformed

    private void btnEntrarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEntrarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            entrar();
        }
    }//GEN-LAST:event_btnEntrarKeyPressed

    private void btnConsultaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnConsultaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            consultar();
        }
    }//GEN-LAST:event_btnConsultaKeyPressed

    private void btnFiscalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiscalActionPerformed
        fiscal();
    }//GEN-LAST:event_btnFiscalActionPerformed

    private void btnFiscalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnFiscalKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            fiscal();
        }
    }//GEN-LAST:event_btnFiscalKeyPressed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnSairKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSairKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSairActionPerformed(null);
        }
    }//GEN-LAST:event_btnSairKeyPressed

    private void txtOperadorUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOperadorUsuarioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pswOperadorSenha.requestFocus();
        }
    }//GEN-LAST:event_txtOperadorUsuarioKeyPressed

    private void pswOperadorSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pswOperadorSenhaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            entrar();
        }
    }//GEN-LAST:event_pswOperadorSenhaKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConsulta;
    private javax.swing.JButton btnEntrar;
    private javax.swing.JButton btnFiscal;
    private javax.swing.JButton btnSair;
    private javax.swing.JLabel lblOperadorSenha;
    private javax.swing.JLabel lblOperadorUsuario;
    private javax.swing.JPanel panOperador;
    private javax.swing.JPasswordField pswOperadorSenha;
    private javax.swing.JSeparator separador;
    private javax.swing.JTextField txtOperadorUsuario;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo com a acao do botao entrar.
     */
    private void entrar() {
        // valida se preencheu todos os campos
        if (txtOperadorUsuario.getText().equals("") || pswOperadorSenha.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Login", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // faz a codificao da senha em SHA-1
            ConfigurablePasswordEncryptor sha = new ConfigurablePasswordEncryptor();
            sha.setAlgorithm("SHA-1");
            sha.setPlainDigest(true);
            sha.setStringOutputType("hexadecimal");
            String senhaOp = sha.encryptPassword(new String(pswOperadorSenha.getPassword()));

            try {
                Login.logar(txtOperadorUsuario.getText(), senhaOp);
                Caixa.getInstancia().getLblOperador().setText("Operador : " + txtOperadorUsuario.getText().toUpperCase());
                Caixa.getInstancia().getLblCaixa().setText("Caixa : " + ECF.getInstancia().enviar(EComando.ECF_NumECF)[1]);
                txtOperadorUsuario.setText("");
                pswOperadorSenha.setText("");
                Caixa.getInstancia().setJanela(null);
                dispose();

                EEstado estado = ECF.getInstancia().validarEstado();
                if (estado == EEstado.estVenda || estado == EEstado.estPagamento) {
                    new ComandoRecuperarVenda().executar();
                    Caixa.getInstancia().modoAberto();
                } else {
                    Caixa.getInstancia().modoDisponivel();
                }
            } catch (Exception ex) {
                log.debug("Erro no login!", ex);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Login", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Metodo com a acao do botao consultar.
     */
    private void consultar() {
        Caixa.getInstancia().modoConsulta();
        Caixa.getInstancia().setJanela(null);
        dispose();
    }

    /**
     * Metodo com a acao do botao menu fiscal.
     */
    private void fiscal() {
        consultar();
        Caixa.getInstancia().getMnuFiscal().doClick();
    }

    //GETs e SETs
    public JButton getBtnConsulta() {
        return btnConsulta;
    }

    public void setBtnConsulta(JButton btnConsulta) {
        this.btnConsulta = btnConsulta;
    }

    public JButton getBtnEntrar() {
        return btnEntrar;
    }

    public void setBtnEntrar(JButton btnEntrar) {
        this.btnEntrar = btnEntrar;
    }

    public JLabel getLblOperadorSenha() {
        return lblOperadorSenha;
    }

    public void setLblOperadorSenha(JLabel lblOperadorSenha) {
        this.lblOperadorSenha = lblOperadorSenha;
    }

    public JLabel getLblOperadorUsuario() {
        return lblOperadorUsuario;
    }

    public void setLblOperadorUsuario(JLabel lblOperadorUsuario) {
        this.lblOperadorUsuario = lblOperadorUsuario;
    }

    public JPanel getPanOperador() {
        return panOperador;
    }

    public void setPanOperador(JPanel panOperador) {
        this.panOperador = panOperador;
    }

    public JPasswordField getPswOperadorSenha() {
        return pswOperadorSenha;
    }

    public void setPswOperadorSenha(JPasswordField pswOperadorSenha) {
        this.pswOperadorSenha = pswOperadorSenha;
    }

    public JTextField getTxtOperadorUsuario() {
        return txtOperadorUsuario;
    }

    public void setTxtOperadorUsuario(JTextField txtOperadorUsuario) {
        this.txtOperadorUsuario = txtOperadorUsuario;
    }

    public JButton getBtnFiscal() {
        return btnFiscal;
    }

    public void setBtnFiscal(JButton btnFiscal) {
        this.btnFiscal = btnFiscal;
    }

    public JButton getBtnSair() {
        return btnSair;
    }

    public void setBtnSair(JButton btnSair) {
        this.btnSair = btnSair;
    }

    public JSeparator getSeparador() {
        return separador;
    }

    public void setSeparador(JSeparator separador) {
        this.separador = separador;
    }
}
