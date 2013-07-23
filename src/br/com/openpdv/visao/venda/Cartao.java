package br.com.openpdv.visao.venda;

import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.TextFieldLimit;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.EJuncao;
import br.com.openpdv.modelo.core.filtro.FiltroBinario;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.core.filtro.FiltroTexto;
import br.com.openpdv.modelo.core.filtro.GrupoFiltro;
import br.com.openpdv.modelo.core.filtro.IFiltro;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import br.com.openpdv.modelo.ecf.EcfPagamentoParcela;
import br.com.openpdv.modelo.ecf.EcfPagamentoTipo;
import br.com.openpdv.visao.core.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 * Classe que representa outra forma de pagamento.
 *
 * @author Pedro H. Lira
 */
public class Cartao extends javax.swing.JDialog {

    private static Cartao cartao;
    private Logger log;
    private CoreService service;
    private AsyncCallback<EcfPagamento> async;
    private double total;

    /**
     * Construtor padrao.
     */
    private Cartao() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Cartao.class);
        initComponents();
        service = new CoreService();
        carregaTipos();
    }

    /**
     * Metodo que retorna a instancia unica de Gerente.
     *
     * @param async objeto assincrono para resposta da acao.
     * @return o objeto de Gerente.
     */
    public static Cartao getInstancia(AsyncCallback<EcfPagamento> async, double total) {
        if (cartao == null) {
            cartao = new Cartao();
        }

        cartao.cmbTipo.setSelectedIndex(-1);
        cartao.txtValor.setValue(total);
        cartao.spParcela.setValue(1);
        cartao.txtDoc.setText("");
        cartao.txtDoc.setDocument(new TextFieldLimit(10));
        cartao.async = async;
        cartao.total = total;
        return cartao;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTipo = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox();
        lblValor = new javax.swing.JLabel();
        txtValor = new javax.swing.JFormattedTextField();
        lblParcela = new javax.swing.JLabel();
        spParcela = new javax.swing.JSpinner();
        lblDoc = new javax.swing.JLabel();
        txtDoc = new javax.swing.JTextField();
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

        lblTipo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblTipo.setText("Tipo:");

        cmbTipo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmbTipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbTipoKeyPressed(evt);
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

        lblParcela.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblParcela.setText("Parcelas:");

        spParcela.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        spParcela.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        spParcela.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                spParcelaKeyPressed(evt);
            }
        });

        lblDoc.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDoc.setText("Doc:");

        txtDoc.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtDoc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDocKeyPressed(evt);
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
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 339, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(lblValor)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtValor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblParcela)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(spParcela, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblDoc)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtDoc))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(lblTipo)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(cmbTipo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 302, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancelar)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblTipo)
                    .add(cmbTipo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblValor)
                    .add(txtValor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblParcela)
                    .add(spParcela, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblDoc)
                    .add(txtDoc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancelar)
                    .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
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
        cmbTipo.requestFocus();
    }//GEN-LAST:event_formWindowActivated

    private void cmbTipoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTipoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtValor.requestFocus();
        }
    }//GEN-LAST:event_cmbTipoKeyPressed

    private void txtValorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            spParcela.requestFocus();
        }
    }//GEN-LAST:event_txtValorKeyPressed

    private void spParcelaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spParcelaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtDoc.requestFocus();
        }
    }//GEN-LAST:event_spParcelaKeyPressed

    private void txtDocKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDocKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnOk.requestFocus();
        }
    }//GEN-LAST:event_txtDocKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox cmbTipo;
    private javax.swing.JLabel lblDoc;
    private javax.swing.JLabel lblParcela;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblValor;
    private javax.swing.JSeparator separador;
    private javax.swing.JSpinner spParcela;
    private javax.swing.JTextField txtDoc;
    private javax.swing.JFormattedTextField txtValor;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que carrega os tipos na combo box
     */
    private void carregaTipos() {
        try {
            FiltroBinario fb = new FiltroBinario("ecfPagamentoTipoTef", ECompara.IGUAL, true);
            FiltroTexto ft = new FiltroTexto("ecfPagamentoTipoCodigo", ECompara.DIFERENTE, Util.getConfig().get("ecf.cheque"));
            GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fb, ft});
            List<EcfPagamentoTipo> tipos = service.selecionar(new EcfPagamentoTipo(), 0, 0, gf);
            for (EcfPagamentoTipo tipo : tipos) {
                cmbTipo.addItem(tipo.getEcfPagamentoTipoId() + " - [" + tipo.getEcfPagamentoTipoDescricao() + "]");
            }
        } catch (OpenPdvException ex) {
            log.error("Nao conseguiu carregar os tipos -> ", ex);
            JOptionPane.showMessageDialog(this, "Não foi possível carregar os tipos de pagamentos!", "Cartão", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Metodo com a acaoo do botao OK.
     */
    private void ok() {
        boolean valido = true;
        EcfPagamento pag = new EcfPagamento();

        try {
            // recupera o tipo
            EcfPagamentoTipo tipo = null;
            if (cmbTipo.getSelectedIndex() >= 0) {
                String[] selecionado = cmbTipo.getSelectedItem().toString().split(" - ");
                FiltroNumero fn = new FiltroNumero("ecfPagamentoTipoId", ECompara.IGUAL, selecionado[0]);
                tipo = (EcfPagamentoTipo) service.selecionar(new EcfPagamentoTipo(), fn);
                tipo.setEcfPagamentoTipoTef(false);
                tipo.setEcfPagamentoTipoRede("POS");
                pag.setEcfPagamentoTipo(tipo);
            } else {
                throw new Exception();
            }
            // recupera o valor
            Double valor = Double.valueOf(txtValor.getValue().toString());
            pag.setEcfPagamentoValor(valor);
            if (valor == 0.00 || valor.compareTo(total) > 0) {
                throw new Exception();
            }
            // recupera o Doc
            String doc;
            if (txtDoc.getText().equals("")) {
                throw new Exception();
            }else{
                doc = "DV" + txtDoc.getText();
            }
            pag.setEcfPagamentoNsu(doc);
            // recupera as parcelas
            Date hoje = new Date();
            List<EcfPagamentoParcela> parcelas = new ArrayList<>();
            if (tipo.isEcfPagamentoTipoDebito()) {
                EcfPagamentoParcela parcela = new EcfPagamentoParcela();
                parcela.setEcfPagamentoParcelaData(hoje);
                parcela.setEcfPagamentoParcelaValor(valor);
                parcela.setEcfPagamentoParcelaNsu(doc);
                parcelas.add(parcela);
            } else {
                int numPar = Integer.valueOf(spParcela.getValue().toString());
                Calendar cal = Calendar.getInstance();
                cal.setTime(hoje);
                for (int i = 1; i <= numPar; i++) {
                    cal.add(Calendar.MONTH, 1);
                    EcfPagamentoParcela parcela = new EcfPagamentoParcela();
                    parcela.setEcfPagamentoParcelaData(cal.getTime());
                    parcela.setEcfPagamentoParcelaValor(valor / numPar);
                    parcela.setEcfPagamentoParcelaNsu(doc);
                    parcelas.add(parcela);
                }
            }
            pag.setEcfPagamentoParcelas(parcelas);
            // seta os demais valores do pagamento
            pag.setEcfPagamentoData(hoje);
            pag.setEcfPagamentoGnf(0);
            pag.setEcfPagamentoValor(valor);
            pag.setEcfPagamentoNsu(doc);
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
            JOptionPane.showMessageDialog(null, "Deve-se informar todos os dados, ou dados errados!", "Cartão", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo com a acao do botao Cancelar.
     */
    private void cancelar() {
        setVisible(false);
        async.falha(new OpenPdvException("Operação de cartão cancelada!"));
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

    public JComboBox getCmbTipo() {
        return cmbTipo;
    }

    public void setCmbTipo(JComboBox cmbTipo) {
        this.cmbTipo = cmbTipo;
    }

    public JLabel getLblDoc() {
        return lblDoc;
    }

    public void setLblDoc(JLabel lblDoc) {
        this.lblDoc = lblDoc;
    }

    public JLabel getLblParcela() {
        return lblParcela;
    }

    public void setLblParcela(JLabel lblParcela) {
        this.lblParcela = lblParcela;
    }

    public JLabel getLblTipo() {
        return lblTipo;
    }

    public void setLblTipo(JLabel lblTipo) {
        this.lblTipo = lblTipo;
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

    public JSpinner getSpParcela() {
        return spParcela;
    }

    public void setSpParcela(JSpinner spParcela) {
        this.spParcela = spParcela;
    }

    public JTextField getTxtDoc() {
        return txtDoc;
    }

    public void setTxtDoc(JTextField txtDoc) {
        this.txtDoc = txtDoc;
    }

    public JFormattedTextField getTxtValor() {
        return txtValor;
    }

    public void setTxtValor(JFormattedTextField txtValor) {
        this.txtValor = txtValor;
    }
}
