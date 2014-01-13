package br.com.openpdv.visao.fiscal;

import br.com.openpdv.controlador.comandos.ComandoGerarCat52;
import br.com.openpdv.controlador.core.CoreService;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.EJuncao;
import br.com.openpdv.modelo.core.filtro.FiltroData;
import br.com.openpdv.modelo.core.filtro.FiltroObjeto;
import br.com.openpdv.modelo.core.filtro.GrupoFiltro;
import br.com.openpdv.modelo.core.filtro.IFiltro;
import br.com.openpdv.modelo.ecf.EcfZ;
import br.com.openpdv.visao.core.Aguarde;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 * Classe que representa os menus de memoria fiscal exigidos pelo PAF.
 *
 * @author Pedro H. Lira
 */
public class PAF_ArquivoCat52 extends JDialog {

    private static PAF_ArquivoCat52 paf_cat52;
    private Date inicio;
    private Date fim;

    /**
     * Construtor padrao.
     */
    private PAF_ArquivoCat52() {
        super(Caixa.getInstancia());
        initComponents();
    }

    /**
     * Metodo que retorna a instancia unica de PAF_PAGAMENTO.
     *
     * @return o objeto de PAF_PAGAMENTO.
     */
    public static PAF_ArquivoCat52 getInstancia() {
        if (paf_cat52 == null) {
            paf_cat52 = new PAF_ArquivoCat52();
        }

        paf_cat52.txtDtInicio.setText(null);
        paf_cat52.txtDtFim.setText(null);
        return paf_cat52;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        lblDtInicio = new javax.swing.JLabel();
        txtDtInicio = new javax.swing.JFormattedTextField();
        lblDtFim = new javax.swing.JLabel();
        txtDtFim = new javax.swing.JFormattedTextField();
        separador = new javax.swing.JSeparator();
        btnOk = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gerar Arquivo Cat52");
        setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        setIconImage(null);
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lblDtInicio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDtInicio.setText("Data inicial:");

        txtDtInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtDtInicio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblDtFim.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDtFim.setText("Data final:");

        txtDtFim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtDtFim.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        btnOk.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/ok.png"))); // NOI18N
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(100, 30));
        btnOk.setMinimumSize(new java.awt.Dimension(100, 30));
        btnOk.setPreferredSize(new java.awt.Dimension(100, 30));
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(separador, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(lblDtInicio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDtFim)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDtFim, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDtInicio)
                    .addComponent(txtDtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDtFim)
                    .addComponent(txtDtFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(323, 129));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        ok();
}//GEN-LAST:event_btnOkActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
        Caixa.getInstancia().setJanela(null);
}//GEN-LAST:event_btnCancelarActionPerformed

    private void btnOkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOkKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ok();
        }
    }//GEN-LAST:event_btnOkKeyPressed

    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            dispose();
            Caixa.getInstancia().setJanela(null);
        }
    }//GEN-LAST:event_btnCancelarKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Caixa.getInstancia().setJanela(null);
    }//GEN-LAST:event_formWindowClosing
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnOk;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel lblDtFim;
    private javax.swing.JLabel lblDtInicio;
    private javax.swing.JSeparator separador;
    private javax.swing.JFormattedTextField txtDtFim;
    private javax.swing.JFormattedTextField txtDtInicio;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que tem a acao do botao OK.
     */
    private void ok() {
        if (validar()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    boolean sucesso = true;
                    List<EcfZ> zs = new ArrayList<>();

                    try {
                        // seleciona as Zs deste periodo
                        CoreService service = new CoreService();
                        FiltroObjeto fo = new FiltroObjeto("ecfImpressora", ECompara.IGUAL, Caixa.getInstancia().getImpressora());
                        FiltroData dt1 = new FiltroData("ecfZMovimento", ECompara.MAIOR_IGUAL, inicio);
                        FiltroData dt2 = new FiltroData("ecfZMovimento", ECompara.MENOR_IGUAL, fim);
                        GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fo, dt1, dt2});
                        zs = service.selecionar(new EcfZ(), 0, 0, gf);
                    } catch (OpenPdvException ex) {
                        sucesso = false;
                    }

                    for (EcfZ z : zs) {
                        try {
                            new ComandoGerarCat52(Caixa.getInstancia().getEmpresa(), Caixa.getInstancia().getImpressora(), z).executar();
                        } catch (OpenPdvException ex) {
                            sucesso = false;
                        }
                    }

                    Aguarde.getInstancia().setVisible(false);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(paf_cat52, "Arquivos gerados com sucesso!", "Menu Fiscal", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(paf_cat52, "Alguns arquivo não foram gerados!", "Menu Fiscal", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }).start();

            Aguarde.getInstancia().setVisible(true);
        }
    }

    /**
     * Metodo que faz a validacao dos campos antes de emitir a leitura fiscal.
     *
     * @return retorna true se pasosu na validacao ou false caso contrario.
     */
    private boolean validar() {
        boolean retorno = true;

        if (txtDtInicio.getText().equals("") || txtDtFim.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "As duas informações são necessárias!", "Gerar Arquivo Cat52", JOptionPane.WARNING_MESSAGE);
            retorno = false;
        } else {
            inicio = Util.getData(txtDtInicio.getText());
            fim = Util.getData(txtDtFim.getText());
            if (inicio == null || fim == null) {
                JOptionPane.showMessageDialog(this, "As duas datas precisam ser válidas!", "Gerar Arquivo Cat52", JOptionPane.WARNING_MESSAGE);
                retorno = false;
            } else if (inicio.compareTo(fim) > 0) {
                JOptionPane.showMessageDialog(this, "A data inicial não pode ser maior que a data final!", "Gerar Arquivo Cat52", JOptionPane.WARNING_MESSAGE);
                retorno = false;
            } else if (fim.compareTo(new Date()) > 0) {
                JOptionPane.showMessageDialog(this, "A data final não pode ser maior que a data atual!", "Gerar Arquivo Cat52", JOptionPane.WARNING_MESSAGE);
                retorno = false;
            }
        }

        return retorno;
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

    public ButtonGroup getButtonGroup1() {
        return buttonGroup1;
    }

    public void setButtonGroup1(ButtonGroup buttonGroup1) {
        this.buttonGroup1 = buttonGroup1;
    }

    public ButtonGroup getButtonGroup2() {
        return buttonGroup2;
    }

    public void setButtonGroup2(ButtonGroup buttonGroup2) {
        this.buttonGroup2 = buttonGroup2;
    }

    public JLabel getLblDtFim() {
        return lblDtFim;
    }

    public void setLblDtFim(JLabel lblDtFim) {
        this.lblDtFim = lblDtFim;
    }

    public JLabel getLblDtInicio() {
        return lblDtInicio;
    }

    public void setLblDtInicio(JLabel lblDtInicio) {
        this.lblDtInicio = lblDtInicio;
    }

    public JSeparator getSeparador() {
        return separador;
    }

    public void setSeparador(JSeparator separador) {
        this.separador = separador;
    }

    public JFormattedTextField getTxtDtFim() {
        return txtDtFim;
    }

    public void setTxtDtFim(JFormattedTextField txtDtFim) {
        this.txtDtFim = txtDtFim;
    }

    public JFormattedTextField getTxtDtInicio() {
        return txtDtInicio;
    }

    public void setTxtDtInicio(JFormattedTextField txtDtInicio) {
        this.txtDtInicio = txtDtInicio;
    }
}
