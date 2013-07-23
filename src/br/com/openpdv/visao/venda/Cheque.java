package br.com.openpdv.visao.venda;

import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.TextFieldLimit;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import br.com.openpdv.visao.core.*;
import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 * Classe que representa outra forma de pagamento.
 *
 * @author Pedro H. Lira
 */
public class Cheque extends javax.swing.JDialog {

    private static Cheque cheque;
    private Logger log;
    private CoreService service;
    private AsyncCallback<EcfPagamento> async;
    private double total;

    /**
     * Construtor padrao.
     */
    private Cheque() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Cheque.class);
        initComponents();
        service = new CoreService();
    }

    /**
     * Metodo que retorna a instancia unica de Gerente.
     *
     * @param async objeto assincrono para resposta da acao.
     * @return o objeto de Gerente.
     */
    public static Cheque getInstancia(AsyncCallback<EcfPagamento> async, double total) {
        if (cheque == null) {
            cheque = new Cheque();
        }

        cheque.txtBarra.setText("");
        cheque.txtBarra.setDocument(new TextFieldLimit(32));
        cheque.txtValor.setValue(total);
        cheque.txtVencimento.setText(Util.formataData(new Date(), "dd/MM/yyyy"));
        cheque.async = async;
        cheque.total = total;
        return cheque;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblBarra = new javax.swing.JLabel();
        txtBarra = new javax.swing.JTextField();
        lblValor = new javax.swing.JLabel();
        txtValor = new javax.swing.JFormattedTextField();
        lblVencimento = new javax.swing.JLabel();
        txtVencimento = new javax.swing.JFormattedTextField();
        separador = new javax.swing.JSeparator();
        btnOk = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pagamento");
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

        lblBarra.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblBarra.setText("Barra:");

        txtBarra.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtBarra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarraKeyPressed(evt);
            }
        });

        lblValor.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblValor.setText("Valor:");

        txtValor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txtValor.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtValor.setToolTipText("");
        txtValor.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtValor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorKeyPressed(evt);
            }
        });

        lblVencimento.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblVencimento.setText("Vencimento:");

        txtVencimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtVencimento.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

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
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 339, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(lblValor)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtValor)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblVencimento)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtVencimento, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 125, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancelar))
                    .add(layout.createSequentialGroup()
                        .add(lblBarra)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtBarra)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblBarra)
                    .add(txtBarra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblValor)
                    .add(txtValor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblVencimento)
                    .add(txtVencimento, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancelar)
                    .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-370)/2, (screenSize.height-157)/2, 370, 157);
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
        txtBarra.requestFocus();
    }//GEN-LAST:event_formWindowActivated

    private void txtValorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtVencimento.requestFocus();
        }
    }//GEN-LAST:event_txtValorKeyPressed

    private void txtBarraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarraKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnOk.requestFocus();
        }
    }//GEN-LAST:event_txtBarraKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblBarra;
    private javax.swing.JLabel lblValor;
    private javax.swing.JLabel lblVencimento;
    private javax.swing.JSeparator separador;
    private javax.swing.JTextField txtBarra;
    private javax.swing.JFormattedTextField txtValor;
    private javax.swing.JFormattedTextField txtVencimento;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo com a acaoo do botao OK.
     */
    private void ok() {
        boolean valido = true;
        EcfPagamento pag = new EcfPagamento();

        try {
            // recupera a barra
            String barra;
            if (txtBarra.getText().equals("") || txtBarra.getText().length() < 30) {
                throw new Exception();
            } else {
                barra = txtBarra.getText();
            }
            pag.setEcfPagamentoNsu(barra);
            // recupera o valor
            Double valor = Double.valueOf(txtValor.getValue().toString());
            if (valor == 0.00 || valor.compareTo(total) > 0) {
                throw new Exception();
            }
            pag.setEcfPagamentoValor(valor);
            // recupera o vencimento
            Date vencimento = Util.formataData(txtVencimento.getText(), "dd/MM/yyyy");
            if (vencimento == null) {
                throw new Exception();
            }
            pag.setEcfPagamentoData(vencimento);
            // seta os demais valores do pagamento
            pag.setEcfPagamentoGnf(0);
            pag.setEcfPagamentoValor(valor);
            pag.setEcfPagamentoNsu(barra);
            pag.setEcfPagamentoEstorno('N');
            pag.setEcfVenda(Caixa.getInstancia().getVenda());
        } catch (Exception ex) {
            valido = false;
        }

        // se valido procegue o processo
        if (valido) {
            setVisible(false);
            async.sucesso(pag);
        } else {
            JOptionPane.showMessageDialog(null, "Deve-se informar todos os dados, ou dados errados!", "Cheque", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo com a acao do botao Cancelar.
     */
    private void cancelar() {
        setVisible(false);
        async.falha(new OpenPdvException("Operação de cheque cancelada!"));
    }

    //GETs e SETs
    public AsyncCallback<EcfPagamento> getAsync() {
        return async;
    }

    public void setAsync(AsyncCallback<EcfPagamento> async) {
        this.async = async;
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

    public JLabel getLblValor() {
        return lblValor;
    }

    public void setLblValor(JLabel lblValor) {
        this.lblValor = lblValor;
    }

    public JSeparator getSeparador() {
        return separador;
    }

    public void setSeparador(JSeparator separador) {
        this.separador = separador;
    }

    public JFormattedTextField getTxtValor() {
        return txtValor;
    }

    public void setTxtValor(JFormattedTextField txtValor) {
        this.txtValor = txtValor;
    }

    public static Cheque getCheque() {
        return cheque;
    }

    public static void setCheque(Cheque cheque) {
        Cheque.cheque = cheque;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public JLabel getLblBarra() {
        return lblBarra;
    }

    public void setLblBarra(JLabel lblBarra) {
        this.lblBarra = lblBarra;
    }

    public JLabel getLblVencimento() {
        return lblVencimento;
    }

    public void setLblVencimento(JLabel lblVencimento) {
        this.lblVencimento = lblVencimento;
    }

    public JTextField getTxtBarra() {
        return txtBarra;
    }

    public void setTxtBarra(JTextField txtBarra) {
        this.txtBarra = txtBarra;
    }

    public JFormattedTextField getTxtVencimento() {
        return txtVencimento;
    }

    public void setTxtVencimento(JFormattedTextField txtVencimento) {
        this.txtVencimento = txtVencimento;
    }
}
