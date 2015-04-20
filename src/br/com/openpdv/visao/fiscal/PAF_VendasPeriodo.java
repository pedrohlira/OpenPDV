package br.com.openpdv.visao.fiscal;

import br.com.openpdv.controlador.comandos.ComandoGerarSintegra;
import br.com.openpdv.controlador.comandos.ComandoGerarSped;
import br.com.openpdv.visao.core.Aguarde;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 * Classe que representa os menus de memoria fiscal exigidos pelo PAF.
 *
 * @author Pedro H. Lira
 */
public class PAF_VendasPeriodo extends javax.swing.JDialog {

    private static PAF_VendasPeriodo paf_vendas;
    private static Logger log;
    private Date param1;
    private Date param2;

    /**
     * Construtor padrao.
     */
    private PAF_VendasPeriodo() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(PAF_VendasPeriodo.class);
        initComponents();
    }

    /**
     * Metodo que retorna a instancia unica de PAF_VendasPeriodo.
     *
     * @return o objeto de PAF_Estoque.
     */
    public static PAF_VendasPeriodo getInstancia() {
        if (paf_vendas == null) {
            paf_vendas = new PAF_VendasPeriodo();
        }

        paf_vendas.limpar();
        return paf_vendas;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grpOpcao = new javax.swing.ButtonGroup();
        radSintegra = new javax.swing.JRadioButton();
        separador1 = new javax.swing.JSeparator();
        radSped = new javax.swing.JRadioButton();
        chkSubstituto = new javax.swing.JCheckBox();
        panSintegra = new javax.swing.JPanel();
        lblConvenio = new javax.swing.JLabel();
        cmbConvenio = new javax.swing.JComboBox();
        lblNatureza = new javax.swing.JLabel();
        cmbNatureza = new javax.swing.JComboBox();
        lblFinalidade = new javax.swing.JLabel();
        cmbFinalidade = new javax.swing.JComboBox();
        panPeriodo = new javax.swing.JPanel();
        lblDtInicio = new javax.swing.JLabel();
        lblDtFim = new javax.swing.JLabel();
        txtDtInicio = new javax.swing.JFormattedTextField();
        txtDtFim = new javax.swing.JFormattedTextField();
        separador2 = new javax.swing.JSeparator();
        btnCancelar = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Vendas do Período");
        setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        setModal(true);
        setResizable(false);

        grpOpcao.add(radSintegra);
        radSintegra.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        radSintegra.setSelected(true);
        radSintegra.setText("Sintegra");
        radSintegra.setToolTipText("Gerar arquivo no layout do Convênio 57/95");
        radSintegra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radSintegraActionPerformed(evt);
            }
        });

        separador1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        grpOpcao.add(radSped);
        radSped.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        radSped.setText("Sped Fiscal");
        radSped.setToolTipText("Gerar arquivo no layout do Ato COTEPE/ICMS 09/08");
        radSped.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radSpedActionPerformed(evt);
            }
        });

        chkSubstituto.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        chkSubstituto.setText("Arquivo Substituto");
        chkSubstituto.setToolTipText("Marque esta opção seja arquivo de substituição");

        panSintegra.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Serif", 0, 12))); // NOI18N

        lblConvenio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblConvenio.setText("Convênio:");

        cmbConvenio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmbConvenio.setMaximumRowCount(3);
        cmbConvenio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3 - Convênio 57/95 Alt. 76/03", "2 - Convênio 57/95 Versão 69/02 Alt. 142/02", "1 - Convênio 57/95 Versão 31/99 Alt. 30/02" }));

        lblNatureza.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblNatureza.setText("Natureza:");

        cmbNatureza.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmbNatureza.setMaximumRowCount(3);
        cmbNatureza.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3 - Totalidade das operações do informante", "2 - Interestaduais : Operações com ou sem Substituição Tributária", "1 - Interestaduais : Somente operações com Substituição Tributária" }));

        lblFinalidade.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblFinalidade.setText("Finalidade:");

        cmbFinalidade.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmbFinalidade.setMaximumRowCount(4);
        cmbFinalidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1 - Normal", "2 - Retificação total de arquivo", "3 - Retificação aditiva de arquivo", "5 - Desfazimento" }));

        org.jdesktop.layout.GroupLayout panSintegraLayout = new org.jdesktop.layout.GroupLayout(panSintegra);
        panSintegra.setLayout(panSintegraLayout);
        panSintegraLayout.setHorizontalGroup(
            panSintegraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panSintegraLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(panSintegraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblNatureza)
                    .add(lblConvenio)
                    .add(lblFinalidade))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panSintegraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, cmbConvenio, 0, 1, Short.MAX_VALUE)
                    .add(cmbNatureza, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 264, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cmbFinalidade, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        panSintegraLayout.setVerticalGroup(
            panSintegraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panSintegraLayout.createSequentialGroup()
                .addContainerGap()
                .add(panSintegraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblConvenio)
                    .add(cmbConvenio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panSintegraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblNatureza)
                    .add(cmbNatureza, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panSintegraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblFinalidade)
                    .add(cmbFinalidade, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panPeriodo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Período dos Dados", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Serif", 0, 12))); // NOI18N

        lblDtInicio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDtInicio.setText("Data inicial:");

        lblDtFim.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDtFim.setText("Data final:");

        txtDtInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtDtInicio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        txtDtFim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtDtFim.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        org.jdesktop.layout.GroupLayout panPeriodoLayout = new org.jdesktop.layout.GroupLayout(panPeriodo);
        panPeriodo.setLayout(panPeriodoLayout);
        panPeriodoLayout.setHorizontalGroup(
            panPeriodoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panPeriodoLayout.createSequentialGroup()
                .add(10, 10, 10)
                .add(lblDtInicio)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtDtInicio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 19, Short.MAX_VALUE)
                .add(lblDtFim)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtDtFim, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panPeriodoLayout.setVerticalGroup(
            panPeriodoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panPeriodoLayout.createSequentialGroup()
                .add(panPeriodoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblDtInicio)
                    .add(lblDtFim)
                    .add(txtDtInicio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtDtFim, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(separador2)
                    .add(layout.createSequentialGroup()
                        .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(radSintegra)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(separador1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(radSped)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chkSubstituto))
                    .add(panSintegra, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(panPeriodo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(radSintegra)
                        .add(radSped)
                        .add(chkSubstituto))
                    .add(separador1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panSintegra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(panPeriodo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(370, 324));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        gerar();
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnOkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOkKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            gerar();
        }
    }//GEN-LAST:event_btnOkKeyPressed

    private void radSintegraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radSintegraActionPerformed
        limpar();
    }//GEN-LAST:event_radSintegraActionPerformed

    private void radSpedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radSpedActionPerformed
        chkSubstituto.setEnabled(true);
        cmbConvenio.setEnabled(false);
        cmbNatureza.setEnabled(false);
        cmbFinalidade.setEnabled(false);
        txtDtInicio.requestFocus();
    }//GEN-LAST:event_radSpedActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnOk;
    private javax.swing.JCheckBox chkSubstituto;
    private javax.swing.JComboBox cmbConvenio;
    private javax.swing.JComboBox cmbFinalidade;
    private javax.swing.JComboBox cmbNatureza;
    private javax.swing.ButtonGroup grpOpcao;
    private javax.swing.JLabel lblConvenio;
    private javax.swing.JLabel lblDtFim;
    private javax.swing.JLabel lblDtInicio;
    private javax.swing.JLabel lblFinalidade;
    private javax.swing.JLabel lblNatureza;
    private javax.swing.JPanel panPeriodo;
    private javax.swing.JPanel panSintegra;
    private javax.swing.JRadioButton radSintegra;
    private javax.swing.JRadioButton radSped;
    private javax.swing.JSeparator separador1;
    private javax.swing.JSeparator separador2;
    private javax.swing.JFormattedTextField txtDtFim;
    private javax.swing.JFormattedTextField txtDtInicio;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que limpa os campos deixando no estado inicial.
     */
    private void limpar() {
        radSintegra.setSelected(true);
        chkSubstituto.setSelected(false);
        chkSubstituto.setEnabled(false);
        cmbConvenio.setEnabled(true);
        cmbConvenio.setSelectedIndex(0);
        cmbNatureza.setEnabled(true);
        cmbNatureza.setSelectedIndex(0);
        cmbFinalidade.setEnabled(true);
        cmbFinalidade.setSelectedIndex(0);
        txtDtInicio.setValue(null);
        txtDtFim.setValue(null);
        txtDtInicio.requestFocus();
    }

    /**
     * Metodo que faz a validacao dos campos antes de gerar o arquivo.
     *
     * @return retorna true se pasosu na validacao ou false caso contrario.
     */
    private boolean validar() {
        boolean retorno = true;
        param1 = (Date) txtDtInicio.getValue();
        param2 = (Date) txtDtFim.getValue();

        if (param1 == null || param2 == null) {
            JOptionPane.showMessageDialog(this, "As duas datas precisam ser válidas!", "Vendas do Período", JOptionPane.WARNING_MESSAGE);
            retorno = false;
        } else if (param1.compareTo(param2) > 0) {
            JOptionPane.showMessageDialog(this, "A data inicial não pode ser maior que a data final!", "Vendas do Período", JOptionPane.WARNING_MESSAGE);
            retorno = false;
        } else if (param2.compareTo(new Date()) > 0) {
            JOptionPane.showMessageDialog(this, "A data final não pode ser maior que a data atual!", "Vendas do Período", JOptionPane.WARNING_MESSAGE);
            retorno = false;
        }

        return retorno;
    }

    /**
     * Metodo que gera o arquivo de acordo com as opcoes selecionadas.
     */
    private void gerar() {
        if (validar()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        String path;
                        if (radSintegra.isSelected()) {
                            String[] opcoes = new String[3];
                            opcoes[0] = cmbConvenio.getSelectedItem().toString().split(" - ")[0];
                            opcoes[1] = cmbNatureza.getSelectedItem().toString().split(" - ")[0];
                            opcoes[2] = cmbFinalidade.getSelectedItem().toString().split(" - ")[0];
                            
                            ComandoGerarSintegra gerarSintegra = new ComandoGerarSintegra(param1, param2, opcoes);
                            gerarSintegra.executar();
                            path = gerarSintegra.getPath();
                        } else {
                            ComandoGerarSped gerarSped = new ComandoGerarSped(param1, param2, chkSubstituto.isSelected() ? 1 : 0);
                            gerarSped.executar();
                            path = gerarSped.getPath();
                        }

                        Aguarde.getInstancia().setVisible(false);
                        JOptionPane.showMessageDialog(paf_vendas, "Arquivo gerado com sucesso em:\n" + path, "Vendas do Período", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        Aguarde.getInstancia().setVisible(false);
                        log.error("Nao foi possivel gerar o arquivo -> ", ex);
                        JOptionPane.showMessageDialog(paf_vendas, "Não foi possível gerar o arquivo!", "Vendas do Período", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }).start();

            Aguarde.getInstancia().setVisible(true);
        }
    }
}
