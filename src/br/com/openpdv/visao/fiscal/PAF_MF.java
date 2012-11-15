package br.com.openpdv.visao.fiscal;

import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.visao.core.Aguarde;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import br.com.phdss.controlador.PAF;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 * Classe que representa os menus de memoria fiscal exigidos pelo PAF.
 *
 * @author Pedro H. Lira
 */
public class PAF_MF extends JDialog {

    private static PAF_MF paf_mf;
    private Logger log;
    private EComandoECF comando;
    private String param1;
    private String param2;
    private String param3;

    /**
     * Construtor padrao.
     */
    private PAF_MF() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(PAF_MF.class);
        initComponents();
    }

    /**
     * Metodo que retorna a instancia unica de PAF_MF.
     *
     * @param comando informa o tipo de comando.
     * @return o objeto de PAF_MF.
     */
    public static PAF_MF getInstancia(EComandoECF comando) {
        if (paf_mf == null) {
            paf_mf = new PAF_MF();
        }

        paf_mf.comando = comando;
        paf_mf.limpar();
        return paf_mf;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        panOpcao = new javax.swing.JPanel();
        radECF = new javax.swing.JRadioButton();
        radEspelho = new javax.swing.JRadioButton();
        radCotepe = new javax.swing.JRadioButton();
        panFiltro = new javax.swing.JPanel();
        radData = new javax.swing.JRadioButton();
        radIntervalo = new javax.swing.JRadioButton();
        panPeriodo = new javax.swing.JPanel();
        lblDtInicio = new javax.swing.JLabel();
        txtDtInicio = new javax.swing.JFormattedTextField();
        lblDtFim = new javax.swing.JLabel();
        txtDtFim = new javax.swing.JFormattedTextField();
        lblPrimeiro = new javax.swing.JLabel();
        txtPrimeiro = new javax.swing.JFormattedTextField();
        lblUltimo = new javax.swing.JLabel();
        txtUltimo = new javax.swing.JFormattedTextField();
        separador = new javax.swing.JSeparator();
        btnOk = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setFont(new java.awt.Font("Serif", 0, 10)); // NOI18N
        setIconImage(null);
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panOpcao.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tipo:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Serif", 1, 12))); // NOI18N
        panOpcao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        radECF.setBackground(getBackground());
        buttonGroup2.add(radECF);
        radECF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        radECF.setSelected(true);
        radECF.setText("a) Impressão do documento no ECF");

        radEspelho.setBackground(getBackground());
        buttonGroup2.add(radEspelho);
        radEspelho.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        radEspelho.setText("b) Gravação de arquivo eletrônico no formato de espelho");

        buttonGroup2.add(radCotepe);
        radCotepe.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        radCotepe.setText("c) Gravação de arquivo eletrônico Ato Cotepe/ICMS 17/04");

        javax.swing.GroupLayout panOpcaoLayout = new javax.swing.GroupLayout(panOpcao);
        panOpcao.setLayout(panOpcaoLayout);
        panOpcaoLayout.setHorizontalGroup(
            panOpcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panOpcaoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panOpcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panOpcaoLayout.createSequentialGroup()
                        .addComponent(radECF, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(20, Short.MAX_VALUE))
                    .addGroup(panOpcaoLayout.createSequentialGroup()
                        .addGroup(panOpcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radEspelho, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(radCotepe))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panOpcaoLayout.setVerticalGroup(
            panOpcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panOpcaoLayout.createSequentialGroup()
                .addComponent(radECF)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radEspelho)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radCotepe))
        );

        panFiltro.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtro:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Serif", 1, 12))); // NOI18N
        panFiltro.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        buttonGroup1.add(radData);
        radData.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        radData.setSelected(true);
        radData.setText("Período de Data");
        radData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radDataActionPerformed(evt);
            }
        });

        buttonGroup1.add(radIntervalo);
        radIntervalo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        radIntervalo.setText("Intervalo de Contador");
        radIntervalo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radIntervaloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panFiltroLayout = new javax.swing.GroupLayout(panFiltro);
        panFiltro.setLayout(panFiltroLayout);
        panFiltroLayout.setHorizontalGroup(
            panFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFiltroLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(radData)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                .addComponent(radIntervalo))
        );
        panFiltroLayout.setVerticalGroup(
            panFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(radData)
                .addComponent(radIntervalo))
        );

        panPeriodo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblDtInicio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDtInicio.setText("Data inicial:");

        txtDtInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtDtInicio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblDtFim.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDtFim.setText("Data final:");

        txtDtFim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtDtFim.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblPrimeiro.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblPrimeiro.setText("Primeiro:");

        txtPrimeiro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtPrimeiro.setEnabled(false);
        txtPrimeiro.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblUltimo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblUltimo.setText("Último:");

        txtUltimo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtUltimo.setEnabled(false);
        txtUltimo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        javax.swing.GroupLayout panPeriodoLayout = new javax.swing.GroupLayout(panPeriodo);
        panPeriodo.setLayout(panPeriodoLayout);
        panPeriodoLayout.setHorizontalGroup(
            panPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPeriodoLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(panPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panPeriodoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblDtInicio)
                        .addGap(29, 29, 29)
                        .addComponent(lblDtFim)
                        .addGap(37, 37, 37)
                        .addComponent(lblPrimeiro)
                        .addGap(46, 46, 46)
                        .addComponent(lblUltimo))
                    .addGroup(panPeriodoLayout.createSequentialGroup()
                        .addComponent(txtDtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(txtDtFim, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(txtPrimeiro, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(txtUltimo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panPeriodoLayout.setVerticalGroup(
            panPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panPeriodoLayout.createSequentialGroup()
                .addGroup(panPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDtInicio)
                    .addComponent(lblDtFim)
                    .addComponent(lblPrimeiro)
                    .addComponent(lblUltimo))
                .addGap(3, 3, 3)
                .addGroup(panPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDtFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrimeiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUltimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(panOpcao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(separador, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(187, 187, 187)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(panOpcao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(panFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(panPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(separador, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-410)/2, (screenSize.height-302)/2, 410, 302);
    }// </editor-fold>//GEN-END:initComponents

    private void radDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radDataActionPerformed
        txtPrimeiro.setEnabled(false);
        txtUltimo.setEnabled(false);
        txtDtInicio.setEnabled(true);
        txtDtFim.setEnabled(true);
    }//GEN-LAST:event_radDataActionPerformed

    private void radIntervaloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radIntervaloActionPerformed
        txtPrimeiro.setEnabled(true);
        txtUltimo.setEnabled(true);
        txtDtInicio.setEnabled(false);
        txtDtFim.setEnabled(false);
    }//GEN-LAST:event_radIntervaloActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        botaoOK();
}//GEN-LAST:event_btnOkActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
        Caixa.getInstancia().setJanela(null);
}//GEN-LAST:event_btnCancelarActionPerformed

    private void btnOkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOkKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoOK();
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
    private javax.swing.JLabel lblPrimeiro;
    private javax.swing.JLabel lblUltimo;
    private javax.swing.JPanel panFiltro;
    private javax.swing.JPanel panOpcao;
    private javax.swing.JPanel panPeriodo;
    private javax.swing.JRadioButton radCotepe;
    private javax.swing.JRadioButton radData;
    private javax.swing.JRadioButton radECF;
    private javax.swing.JRadioButton radEspelho;
    private javax.swing.JRadioButton radIntervalo;
    private javax.swing.JSeparator separador;
    private javax.swing.JFormattedTextField txtDtFim;
    private javax.swing.JFormattedTextField txtDtInicio;
    private javax.swing.JFormattedTextField txtPrimeiro;
    private javax.swing.JFormattedTextField txtUltimo;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que limpa os campos deixando no estado inicial.
     */
    private void limpar() {
        radData.setSelected(true);
        radData.doClick();
        txtDtInicio.setText(null);
        txtDtFim.setText(null);
        txtPrimeiro.setText(null);
        txtUltimo.setText(null);
        btnOk.setEnabled(true);
        btnCancelar.setEnabled(true);
        Rectangle rec = getBounds();

        switch (comando) {
            case ECF_PafMf_Lmfc_Impressao:
                setTitle("LMFC");
                panOpcao.setVisible(true);
                radECF.setSelected(true);
                radCotepe.setVisible(true);
                rec.height = 320;
                break;
            case ECF_PafMf_Lmfs_Impressao:
                setTitle("LMFS");
                panOpcao.setVisible(true);
                radECF.setSelected(true);
                radCotepe.setVisible(false);
                rec.height = 300;
                break;
            case ECF_PafMf_Mfd_Espelho:
                setTitle("Espelho MFD");
                panOpcao.setVisible(false);
                radEspelho.setSelected(true);
                rec.height = 230;
                break;
            case ECF_PafMf_Mfd_Cotepe1704:
                setTitle("Arquivo MFD");
                panOpcao.setVisible(false);
                radEspelho.setSelected(true);
                rec.height = 230;
                break;
        }
        setBounds(rec);
    }

    /**
     * Metodo que tem a acao do botao OK.
     */
    private void botaoOK() {
        if (validar()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    String[] resp;
                    if (radECF.isSelected()) {
                        param3 = null;
                        resp = PAF.leituraMF(comando, new String[]{param1, param2});
                    } else {
                        String nome = getTitle() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".txt";
                        param3 = PAF.getPathArquivos() + nome;
                        switch (comando) {
                            case ECF_PafMf_Lmfc_Impressao:
                                if (radEspelho.isSelected()) {
                                    resp = PAF.leituraMF(EComandoECF.ECF_PafMf_Lmfc_Espelho, new String[]{param1, param2, param3});
                                } else {
                                    resp = PAF.leituraMF(EComandoECF.ECF_PafMf_Lmfc_Cotepe1704, new String[]{param1, param2, param3});
                                }
                                break;
                            case ECF_PafMf_Lmfs_Impressao:
                                resp = PAF.leituraMF(EComandoECF.ECF_PafMf_Lmfs_Espelho, new String[]{param1, param2, param3});
                                break;
                            default:
                                resp = PAF.leituraMF(comando, new String[]{param1, param2, param3});
                        }
                    }

                    Aguarde.getInstancia().setVisible(false);
                    if (ECF.OK.equals(resp[0]) && param3 != null) {
                        JOptionPane.showMessageDialog(paf_mf, "Arquivo gerado com sucesso em:\n" + param3, getTitle(), JOptionPane.INFORMATION_MESSAGE);
                    } else if (ECF.ERRO.equals(resp[0])) {
                        if (param3 == null) {
                            log.error("Nao foi possivel emitir a leitura fiscal " + getTitle() + "! -> " + resp[1]);
                            JOptionPane.showMessageDialog(paf_mf, "Não foi possível emitir a leitura de " + getTitle() + "!", "Menu Fiscal", JOptionPane.WARNING_MESSAGE);
                        } else {
                            log.error("Nao foi possivel gerar o arquivo de " + getTitle() + "! -> " + resp[1]);
                            JOptionPane.showMessageDialog(paf_mf, "Não foi possível gerar o arquivo de " + getTitle() + "!", "Menu Fiscal", JOptionPane.WARNING_MESSAGE);
                        }
                    }

                }
            }).start();

            Caixa.getInstancia().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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

        if (radData.isSelected()) {
            param1 = txtDtInicio.getText();
            param2 = txtDtFim.getText();
        } else {
            param1 = txtPrimeiro.getText();
            param2 = txtUltimo.getText();
        }

        if (param1.equals("") || param2.equals("")) {
            JOptionPane.showMessageDialog(this, "As duas informações são necessárias!", getTitle(), JOptionPane.WARNING_MESSAGE);
            retorno = false;
        } else if (radData.isSelected()) {
            Date dt1 = Util.getData(param1);
            Date dt2 = Util.getData(param2);
            if (dt1 == null || dt2 == null) {
                JOptionPane.showMessageDialog(this, "As duas datas precisam ser válidas!", getTitle(), JOptionPane.WARNING_MESSAGE);
                retorno = false;
            } else if (dt1.compareTo(dt2) > 0) {
                JOptionPane.showMessageDialog(this, "A data inicial não pode ser maior que a data final!", getTitle(), JOptionPane.WARNING_MESSAGE);
                retorno = false;
            } else if (dt2.compareTo(new Date()) > 0) {
                JOptionPane.showMessageDialog(this, "A data final não pode ser maior que a data atual!", getTitle(), JOptionPane.WARNING_MESSAGE);
                retorno = false;
            }
        } else {
            long crz1 = Long.valueOf(param1);
            long crz2 = Long.valueOf(param2);
            if (crz1 < 1) {
                JOptionPane.showMessageDialog(this, "Primeiro CRZ inválido!", getTitle(), JOptionPane.WARNING_MESSAGE);
                retorno = false;
            } else if (crz2 < 1) {
                JOptionPane.showMessageDialog(this, "Último CRZ inválido!", getTitle(), JOptionPane.WARNING_MESSAGE);
                retorno = false;
            } else if (crz1 > crz2) {
                JOptionPane.showMessageDialog(this, "Último CRZ menor que o primeiro CRZ!", getTitle(), JOptionPane.WARNING_MESSAGE);
                retorno = false;
            }
        }

        return retorno;
    }

    //GETs e SETs
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

    public EComandoECF getComando() {
        return comando;
    }

    public void setComando(EComandoECF comando) {
        this.comando = comando;
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

    public JLabel getLblPrimeiro() {
        return lblPrimeiro;
    }

    public void setLblPrimeiro(JLabel lblPrimeiro) {
        this.lblPrimeiro = lblPrimeiro;
    }

    public JLabel getLblUltimo() {
        return lblUltimo;
    }

    public void setLblUltimo(JLabel lblUltimo) {
        this.lblUltimo = lblUltimo;
    }

    public JPanel getPanFiltro() {
        return panFiltro;
    }

    public void setPanFiltro(JPanel panFiltro) {
        this.panFiltro = panFiltro;
    }

    public JPanel getPanOpcao() {
        return panOpcao;
    }

    public void setPanOpcao(JPanel panOpcao) {
        this.panOpcao = panOpcao;
    }

    public JPanel getPanPeriodo() {
        return panPeriodo;
    }

    public void setPanPeriodo(JPanel panPeriodo) {
        this.panPeriodo = panPeriodo;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public JRadioButton getRadCRZ() {
        return radIntervalo;
    }

    public void setRadCRZ(JRadioButton radCRZ) {
        this.radIntervalo = radCRZ;
    }

    public JRadioButton getRadCotepe() {
        return radCotepe;
    }

    public void setRadCotepe(JRadioButton radCotepe) {
        this.radCotepe = radCotepe;
    }

    public JRadioButton getRadData() {
        return radData;
    }

    public void setRadData(JRadioButton radData) {
        this.radData = radData;
    }

    public JRadioButton getRadECF() {
        return radECF;
    }

    public void setRadECF(JRadioButton radECF) {
        this.radECF = radECF;
    }

    public JRadioButton getRadEspelho() {
        return radEspelho;
    }

    public void setRadEspelho(JRadioButton radEspelho) {
        this.radEspelho = radEspelho;
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

    public JFormattedTextField getTxtPrimeiro() {
        return txtPrimeiro;
    }

    public void setTxtPrimeiro(JFormattedTextField txtPrimeiro) {
        this.txtPrimeiro = txtPrimeiro;
    }

    public JFormattedTextField getTxtUltimo() {
        return txtUltimo;
    }

    public void setTxtUltimo(JFormattedTextField txtUltimo) {
        this.txtUltimo = txtUltimo;
    }
}
