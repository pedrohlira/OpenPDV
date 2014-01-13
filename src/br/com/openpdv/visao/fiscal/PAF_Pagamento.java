package br.com.openpdv.visao.fiscal;

import br.com.openpdv.controlador.comandos.ComandoSalvarDocumento;
import br.com.openpdv.controlador.comandos.ComandoTotalizarPagamentos;
import br.com.openpdv.controlador.core.CoreService;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.EcfPagamentoTotais;
import br.com.openpdv.visao.core.Aguarde;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.controlador.PAF;
import br.com.phdss.modelo.anexo.vi.R07;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 * Classe que representa os menus de memoria fiscal exigidos pelo PAF.
 *
 * @author Pedro H. Lira
 */
public class PAF_Pagamento extends JDialog {

    private static PAF_Pagamento paf_pagamento;
    private Logger log;
    private Date inicio;
    private Date fim;

    /**
     * Construtor padrao.
     */
    private PAF_Pagamento() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(PAF_Pagamento.class);
        initComponents();
    }

    /**
     * Metodo que retorna a instancia unica de PAF_PAGAMENTO.
     *
     * @return o objeto de PAF_PAGAMENTO.
     */
    public static PAF_Pagamento getInstancia() {
        if (paf_pagamento == null) {
            paf_pagamento = new PAF_Pagamento();
        }

        paf_pagamento.txtDtInicio.setText(null);
        paf_pagamento.txtDtFim.setText(null);
        return paf_pagamento;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        lblDtInicio = new javax.swing.JLabel();
        lblDtFim = new javax.swing.JLabel();
        txtDtInicio = new javax.swing.JFormattedTextField();
        txtDtFim = new javax.swing.JFormattedTextField();
        separador = new javax.swing.JSeparator();
        btnOk = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Formas de Pagamento");
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

        lblDtFim.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDtFim.setText("Data final:");

        txtDtInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtDtInicio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(lblDtInicio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDtFim)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDtFim, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(separador, javax.swing.GroupLayout.Alignment.LEADING))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-317)/2, (screenSize.height-125)/2, 317, 125);
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
                    try {
                        // totaliza os pagamentos de hoje
                        String shoje = Util.formataData(new Date(), "dd/MM/yyyy");
                        Date dhoje = Util.formataData(shoje, "dd/MM/yyyy");
                        ComandoTotalizarPagamentos totalizar = new ComandoTotalizarPagamentos(dhoje);
                        totalizar.executar();
                        
                        // ajustando a data fim para documento, pois o mesmo usa datetime
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(fim);
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        fim = cal.getTime();

                        // recupera todos os pagamentos do periodo
                        CoreService<EcfPagamentoTotais> service = new CoreService<>();
                        FiltroData fd1 = new FiltroData("ecfPagamentoTotaisData", ECompara.MAIOR_IGUAL, inicio);
                        FiltroData fd2 = new FiltroData("ecfPagamentoTotaisData", ECompara.MENOR, fim);
                        GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fd1, fd2});
                        List<EcfPagamentoTotais> totais = service.selecionar(new EcfPagamentoTotais(), 0, 0, gf);

                        // gera no formato aceito pelo PAF
                        List<R07> pagamentos = new ArrayList<>();
                        for (EcfPagamentoTotais total : totais) {
                            // em caso de cartao adiciona o modo
                            String doc = total.getEcfPagamentoTipo().getEcfPagamentoTipoDescricao();
                            if (total.getEcfPagamentoTipo().getEcfPagamentoTipoCodigo().equals(Util.getConfig().get("ecf.cartao"))) {
                                doc = total.getEcfPagamentoTipo().isEcfPagamentoTipoDebito() ? "CARTAO DEBITO" : "CARTAO CREDITO";
                            }

                            R07 r07 = new R07();
                            r07.setData(total.getEcfPagamentoTotaisData());
                            r07.setMeioPagamento(doc);
                            r07.setSerie(total.getEcfPagamentoTotaisDocumento());
                            r07.setValor(total.getEcfPagamentoTotaisValor());
                            pagamentos.add(r07);
                        }
                        
                        PAF.emitirMeiosPagamentos(txtDtInicio.getText(), txtDtFim.getText(), pagamentos, Util.getConfig().get("ecf.relpag"));
                        new ComandoSalvarDocumento("RG").executar();
                        Aguarde.getInstancia().setVisible(false);
                    } catch (Exception ex) {
                        Aguarde.getInstancia().setVisible(false);
                        log.error("Nao foi possivel emitir o relatorio -> ", ex);
                        JOptionPane.showMessageDialog(paf_pagamento, "Não foi possível emitir o relatório!", "Menu Fiscal", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "As duas informações são necessárias!", "Meios de Pagamento", JOptionPane.WARNING_MESSAGE);
            retorno = false;
        } else {
            inicio = Util.getData(txtDtInicio.getText());
            fim = Util.getData(txtDtFim.getText());
            if (inicio == null || fim == null) {
                JOptionPane.showMessageDialog(this, "As duas datas precisam ser válidas!", "Meios de Pagamento", JOptionPane.WARNING_MESSAGE);
                retorno = false;
            } else if (inicio.compareTo(fim) > 0) {
                JOptionPane.showMessageDialog(this, "A data inicial não pode ser maior que a data final!", "Meios de Pagamento", JOptionPane.WARNING_MESSAGE);
                retorno = false;
            } else if (fim.compareTo(new Date()) > 0) {
                JOptionPane.showMessageDialog(this, "A data final não pode ser maior que a data atual!", "Meios de Pagamento", JOptionPane.WARNING_MESSAGE);
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
