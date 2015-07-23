package br.com.openpdv.visao.principal;

import br.com.openpdv.controlador.comandos.ComandoEnviarDados;
import br.com.openpdv.controlador.core.TextFieldLimit;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.phdss.Util;
import br.com.openpdv.visao.core.Aguarde;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.EComando;
import br.com.phdss.IECF;
import br.com.phdss.controlador.PAF;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 * Classe que representa os menus de memoria fiscal exigidos pelo PAF.
 *
 * @author Pedro H. Lira
 */
public class Sincronismo extends JDialog {

    private static Sincronismo sinc;
    private Logger log;
    private String param1;
    private String param2;

    /**
     * Construtor padrao.
     */
    private Sincronismo() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Sincronismo.class);
        initComponents();
        
        // colocando limites nos campos
        txtPrimeiro.setDocument(new TextFieldLimit(6, true));
        txtUltimo.setDocument(new TextFieldLimit(6, true));
    }

    /**
     * Metodo que retorna a instancia unica de PAF_MF.
     *
     * @return o objeto de PAF_MF.
     */
    public static Sincronismo getInstancia() {
        if (sinc == null) {
            sinc = new Sincronismo();
        }

        sinc.limpar();
        return sinc;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        panOpcao = new javax.swing.JPanel();
        radReducao = new javax.swing.JRadioButton();
        radVenda = new javax.swing.JRadioButton();
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
        btnEnviar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sincronismo - Enviar");
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

        radReducao.setBackground(getBackground());
        buttonGroup2.add(radReducao);
        radReducao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        radReducao.setSelected(true);
        radReducao.setText("Redução Z");

        radVenda.setBackground(getBackground());
        buttonGroup2.add(radVenda);
        radVenda.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        radVenda.setText("Vendas");

        javax.swing.GroupLayout panOpcaoLayout = new javax.swing.GroupLayout(panOpcao);
        panOpcao.setLayout(panOpcaoLayout);
        panOpcaoLayout.setHorizontalGroup(
            panOpcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panOpcaoLayout.createSequentialGroup()
                .addGroup(panOpcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radReducao)
                    .addComponent(radVenda))
                .addGap(0, 105, Short.MAX_VALUE))
        );
        panOpcaoLayout.setVerticalGroup(
            panOpcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panOpcaoLayout.createSequentialGroup()
                .addComponent(radReducao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radVenda))
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
        radIntervalo.setText("Intervalo de CRZ ou CCF");
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
                .addGroup(panFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radData)
                    .addComponent(radIntervalo))
                .addGap(0, 69, Short.MAX_VALUE))
        );
        panFiltroLayout.setVerticalGroup(
            panFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFiltroLayout.createSequentialGroup()
                .addComponent(radData)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addContainerGap()
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
                .addContainerGap(15, Short.MAX_VALUE))
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

        btnEnviar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnEnviar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/ok.png"))); // NOI18N
        btnEnviar.setText("Enviar");
        btnEnviar.setMaximumSize(new java.awt.Dimension(100, 30));
        btnEnviar.setMinimumSize(new java.awt.Dimension(100, 30));
        btnEnviar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });
        btnEnviar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEnviarKeyPressed(evt);
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
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(separador, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(panPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panOpcao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panOpcao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(panPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separador, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(417, 232));
        setLocationRelativeTo(null);
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

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        botaoEnviar();
}//GEN-LAST:event_btnEnviarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
        Caixa.getInstancia().setJanela(null);
}//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEnviarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEnviarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoEnviar();
        }
    }//GEN-LAST:event_btnEnviarKeyPressed

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
    private javax.swing.JButton btnEnviar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel lblDtFim;
    private javax.swing.JLabel lblDtInicio;
    private javax.swing.JLabel lblPrimeiro;
    private javax.swing.JLabel lblUltimo;
    private javax.swing.JPanel panFiltro;
    private javax.swing.JPanel panOpcao;
    private javax.swing.JPanel panPeriodo;
    private javax.swing.JRadioButton radData;
    private javax.swing.JRadioButton radIntervalo;
    private javax.swing.JRadioButton radReducao;
    private javax.swing.JRadioButton radVenda;
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
        btnEnviar.setEnabled(true);
        btnCancelar.setEnabled(true);
    }

    /**
     * Metodo que tem a acao do botao OK.
     */
    private void botaoEnviar() {
        if (validar()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    ComandoEnviarDados comando;
                    if (radData.isSelected()) {
                        comando = ComandoEnviarDados.getInstancia(Util.getData(param1), Util.getData(param2));
                    } else {
                        comando = ComandoEnviarDados.getInstancia(Integer.valueOf(param1), Integer.valueOf(param2));
                    }
                    if (radReducao.isSelected()) {
                        comando.zs(false);
                    } else {
                        comando.vendas(false);
                    }

                    Aguarde.getInstancia().setVisible(false);
                    Caixa.getInstancia().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    try {
                        comando.analisar();
                        JOptionPane.showMessageDialog(Caixa.getInstancia(), "Realizado com sucesso.", "Sincronismo - Enviar", JOptionPane.INFORMATION_MESSAGE);
                    } catch (OpenPdvException ex) {
                        log.error("Não conseguiu enviar dados ao servidor.", ex);
                        JOptionPane.showMessageDialog(Caixa.getInstancia(), ex.getMessage(), "Sincronismo - Enviar", JOptionPane.WARNING_MESSAGE);
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
            int valor1 = Integer.valueOf(param1);
            int valor2 = Integer.valueOf(param2);
            if (valor1 < 1) {
                JOptionPane.showMessageDialog(this, "Primeiro CRZ ou CCF não pode ser menor que 1!", getTitle(), JOptionPane.WARNING_MESSAGE);
                retorno = false;
            } else if (valor2 < 1) {
                JOptionPane.showMessageDialog(this, "Último CRZ ou CCF não pode ser menor que 1!", getTitle(), JOptionPane.WARNING_MESSAGE);
                retorno = false;
            } else if (valor1 > valor2) {
                JOptionPane.showMessageDialog(this, "Último CRZ ou CCF menor que o primeiro CRZ ou CCF!", getTitle(), JOptionPane.WARNING_MESSAGE);
                retorno = false;
            }
        }

        return retorno;
    }

    //GETs e SETs
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

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

    public JButton getBtnEnviar() {
        return btnEnviar;
    }

    public void setBtnEnviar(JButton btnEnviar) {
        this.btnEnviar = btnEnviar;
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

    public JRadioButton getRadData() {
        return radData;
    }

    public void setRadData(JRadioButton radData) {
        this.radData = radData;
    }

    public JRadioButton getRadIntervalo() {
        return radIntervalo;
    }

    public void setRadIntervalo(JRadioButton radIntervalo) {
        this.radIntervalo = radIntervalo;
    }

    public JRadioButton getRadReducao() {
        return radReducao;
    }

    public void setRadReducao(JRadioButton radReducao) {
        this.radReducao = radReducao;
    }

    public JRadioButton getRadVenda() {
        return radVenda;
    }

    public void setRadVenda(JRadioButton radVenda) {
        this.radVenda = radVenda;
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
