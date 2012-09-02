package br.com.openpdv.visao.venda;

import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.TextFieldLimit;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroTexto;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 * Classe que representa o formulario de identificacao do cliente no sistema.
 *
 * @author Pedro H. Lira
 */
public class Identificar extends javax.swing.JDialog {

    private static Identificar identificar;
    private Logger log;
    private AsyncCallback<SisCliente> async;
    private SisCliente cliente;
    private CoreService<SisCliente> service;

    /**
     * Construtor padrao.
     */
    private Identificar() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Identificar.class);
        service = new CoreService<>();
        initComponents();

        // limitando campos
        txtCPF_CNPJ.setDocument(new TextFieldLimit(18, true));
        txtNome.setDocument(new TextFieldLimit(100));
        txtEndereco.setDocument(new TextFieldLimit(255));
    }

    /**
     * Metodo que retorna a instancia unica de Usuarios.
     *
     * @param async o metodo assincrono de resposta.
     * @return o objeto de Usuarios.
     */
    public static Identificar getInstancia(AsyncCallback<SisCliente> async) {
        if (identificar == null) {
            identificar = new Identificar();
        }

        identificar.async = async;
        return identificar;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCPF = new javax.swing.JLabel();
        txtCPF_CNPJ = new javax.swing.JTextField();
        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        lblEndereco = new javax.swing.JLabel();
        txtEndereco = new javax.swing.JTextField();
        separador = new javax.swing.JSeparator();
        btnOK = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Identificar Cliente");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
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

        lblCPF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCPF.setText("CPF/CNPJ:");

        txtCPF_CNPJ.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCPF_CNPJ.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCPF_CNPJKeyPressed(evt);
            }
        });

        lblNome.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblNome.setText("Nome:");

        txtNome.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomeKeyPressed(evt);
            }
        });

        lblEndereco.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblEndereco.setText("Endereço:");

        txtEndereco.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtEndereco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEnderecoKeyPressed(evt);
            }
        });

        btnOK.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/salvar.png"))); // NOI18N
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
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblEndereco)
                            .add(lblCPF))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(txtCPF_CNPJ, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblNome)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 246, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(txtEndereco)))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(layout.createSequentialGroup()
                            .add(btnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 475, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(lblCPF)
                        .add(txtCPF_CNPJ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(txtNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(lblNome)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblEndereco)
                    .add(txtEndereco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-504)/2, (screenSize.height-160)/2, 504, 160);
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        salvar();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnOKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOKKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            salvar();
        }
    }//GEN-LAST:event_btnOKKeyPressed

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

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        limpar();
    }//GEN-LAST:event_formWindowActivated

    private void txtCPF_CNPJKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCPF_CNPJKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNome.requestFocus();
        }
    }//GEN-LAST:event_txtCPF_CNPJKeyPressed

    private void txtNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtEndereco.requestFocus();
        }
    }//GEN-LAST:event_txtNomeKeyPressed

    private void txtEnderecoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEnderecoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnOK.requestFocus();
        }
    }//GEN-LAST:event_txtEnderecoKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel lblCPF;
    private javax.swing.JLabel lblEndereco;
    private javax.swing.JLabel lblNome;
    private javax.swing.JSeparator separador;
    private javax.swing.JTextField txtCPF_CNPJ;
    private javax.swing.JTextField txtEndereco;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que salva um novo registro.
     */
    private void salvar() {
        if (validar()) {
            try {
                String texto = txtCPF_CNPJ.getText().replaceAll("[^0-9]", "");
                FiltroTexto ft = new FiltroTexto("sisClienteDoc", ECompara.IGUAL, texto);
                cliente = service.selecionar(new SisCliente(), ft);
                if (cliente == null) {
                    cliente = new SisCliente();
                }

                cliente.setSisClienteDoc(texto);
                cliente.setSisClienteNome(txtNome.getText().replace(",", ""));
                cliente.setSisClienteEndereco(txtEndereco.getText().replace(",", ""));
                cliente.setSisClienteCadastrado(new Date());
                cliente = service.salvar(cliente);

                setVisible(false);
                async.sucesso(cliente);
            } catch (OpenPdvException ex) {
                async.falha(ex);
                log.error("Erro ao salvar o cliente.", ex);
            } finally {
                Caixa.getInstancia().setJanela(null);
                dispose();
            }
        } else if (txtCPF_CNPJ.getText().equals("")) {
            cancelar();
        } else {
            JOptionPane.showMessageDialog(this, "O CPF/CNPJ não é válido!", "Identificar Cliente", JOptionPane.INFORMATION_MESSAGE);
            txtCPF_CNPJ.requestFocus();
        }
    }

    /**
     * Metodo que cancela o registro.
     */
    private void cancelar() {
        async.sucesso(null);
        Caixa.getInstancia().setJanela(null);
        dispose();
    }

    /**
     * Metodo que limpa os campos.
     */
    private void limpar() {
        cliente = null;
        txtCPF_CNPJ.setText("");
        txtNome.setText("");
        txtEndereco.setText("");
        txtCPF_CNPJ.requestFocus();
    }

    /**
     * Metodo que valida o CPF e/ou CNPJ
     *
     * @return retorna verdadeiro se valido, falso caso contrario.
     */
    private boolean validar() {
        String texto = txtCPF_CNPJ.getText();
        texto = texto.replaceAll("[^0-9]", "");
        if (!texto.equals("")) {
            if (texto.length() == 11) {
                return Util.isCPF(texto);
            } else if (texto.length() == 14) {
                return Util.isCNPJ(texto);
            }
        }

        return false;
    }

    // GETs e SETs
    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

    public JButton getBtnOK() {
        return btnOK;
    }

    public void setBtnOK(JButton btnOK) {
        this.btnOK = btnOK;
    }

    public SisCliente getCliente() {
        return cliente;
    }

    public void setCliente(SisCliente cliente) {
        this.cliente = cliente;
    }

    public JLabel getLblCPF() {
        return lblCPF;
    }

    public void setLblCPF(JLabel lblCPF) {
        this.lblCPF = lblCPF;
    }

    public JLabel getLblEndereco() {
        return lblEndereco;
    }

    public void setLblEndereco(JLabel lblEndereco) {
        this.lblEndereco = lblEndereco;
    }

    public JLabel getLblNome() {
        return lblNome;
    }

    public void setLblNome(JLabel lblNome) {
        this.lblNome = lblNome;
    }

    public JSeparator getSeparador() {
        return separador;
    }

    public void setSeparador(JSeparator separador) {
        this.separador = separador;
    }

    public JTextField getTxtEndereco() {
        return txtEndereco;
    }

    public void setTxtEndereco(JTextField txtEndereco) {
        this.txtEndereco = txtEndereco;
    }

    public JTextField getTxtNome() {
        return txtNome;
    }

    public void setTxtNome(JTextField txtNome) {
        this.txtNome = txtNome;
    }

    public AsyncCallback<SisCliente> getAsync() {
        return async;
    }

    public void setAsync(AsyncCallback<SisCliente> async) {
        this.async = async;
    }

    public CoreService<SisCliente> getService() {
        return service;
    }

    public void setService(CoreService<SisCliente> service) {
        this.service = service;
    }

    public JTextField getTxtCPF_CNPJ() {
        return txtCPF_CNPJ;
    }

    public void setTxtCPF_CNPJ(JTextField txtCPF_CNPJ) {
        this.txtCPF_CNPJ = txtCPF_CNPJ;
    }
}
