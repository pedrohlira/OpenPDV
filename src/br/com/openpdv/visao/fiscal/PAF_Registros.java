package br.com.openpdv.visao.fiscal;

import br.com.openpdv.controlador.comandos.ComandoEmitirMovimentoECF;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.EcfImpressora;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.visao.core.Aguarde;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.controlador.PAF;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 * Classe que representa os menus de memoria fiscal exigidos pelo PAF.
 *
 * @author Pedro H. Lira
 */
public class PAF_Registros extends JDialog {

    private static PAF_Registros paf_registros;
    private static Logger log;
    private Filtro filtro;
    private Date inicio;
    private Date fim;

    /**
     * Construtor padrao.
     */
    private PAF_Registros() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(PAF_Registros.class);
        initComponents();
    }

    /**
     * Metodo que retorna a instancia unica de PAF_Estoque.
     *
     * @return o objeto de PAF_Estoque.
     */
    public static PAF_Registros getInstancia() {
        if (paf_registros == null) {
            paf_registros = new PAF_Registros();
        }

        paf_registros.limpar();
        return paf_registros;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        panOpcao = new javax.swing.JPanel();
        radTotal = new javax.swing.JRadioButton();
        radParcial = new javax.swing.JRadioButton();
        panFiltro = new javax.swing.JPanel();
        radCodigo = new javax.swing.JRadioButton();
        radDescricao = new javax.swing.JRadioButton();
        panPesquisa = new javax.swing.JPanel();
        lblPrimeiro = new javax.swing.JLabel();
        txtPrimeiro = new javax.swing.JFormattedTextField();
        lblUltimo = new javax.swing.JLabel();
        txtUltimo = new javax.swing.JFormattedTextField();
        lblDescricao = new javax.swing.JLabel();
        txtDescricao = new javax.swing.JTextField();
        panPeriodo = new javax.swing.JPanel();
        lblDtInicio = new javax.swing.JLabel();
        lblDtFim = new javax.swing.JLabel();
        txtDtInicio = new javax.swing.JFormattedTextField();
        txtDtFim = new javax.swing.JFormattedTextField();
        separador = new javax.swing.JSeparator();
        btnOk = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registros do PAF-ECF");
        setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        setIconImage(null);
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panOpcao.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tipo:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Serif", 1, 12))); // NOI18N

        radTotal.setBackground(getBackground());
        buttonGroup2.add(radTotal);
        radTotal.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        radTotal.setSelected(true);
        radTotal.setText("a) Estoque Total");
        radTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radTotalActionPerformed(evt);
            }
        });

        radParcial.setBackground(getBackground());
        buttonGroup2.add(radParcial);
        radParcial.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        radParcial.setText("b) Estoque Parcial");
        radParcial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radParcialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panOpcaoLayout = new javax.swing.GroupLayout(panOpcao);
        panOpcao.setLayout(panOpcaoLayout);
        panOpcaoLayout.setHorizontalGroup(
            panOpcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panOpcaoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panOpcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radTotal)
                    .addComponent(radParcial))
                .addGap(0, 72, Short.MAX_VALUE))
        );
        panOpcaoLayout.setVerticalGroup(
            panOpcaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panOpcaoLayout.createSequentialGroup()
                .addComponent(radTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radParcial)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panFiltro.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtro:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Serif", 1, 12))); // NOI18N

        buttonGroup1.add(radCodigo);
        radCodigo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        radCodigo.setSelected(true);
        radCodigo.setText("Intervalo de Código");
        radCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radCodigoActionPerformed(evt);
            }
        });

        buttonGroup1.add(radDescricao);
        radDescricao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        radDescricao.setText("Descrição");
        radDescricao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radDescricaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panFiltroLayout = new javax.swing.GroupLayout(panFiltro);
        panFiltro.setLayout(panFiltroLayout);
        panFiltroLayout.setHorizontalGroup(
            panFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFiltroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radDescricao)
                    .addComponent(radCodigo))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        panFiltroLayout.setVerticalGroup(
            panFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFiltroLayout.createSequentialGroup()
                .addComponent(radCodigo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radDescricao)
                .addContainerGap())
        );

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

        lblDescricao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDescricao.setText("Descrição");

        txtDescricao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtDescricao.setEnabled(false);

        javax.swing.GroupLayout panPesquisaLayout = new javax.swing.GroupLayout(panPesquisa);
        panPesquisa.setLayout(panPesquisaLayout);
        panPesquisaLayout.setHorizontalGroup(
            panPesquisaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPesquisaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panPesquisaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPrimeiro, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrimeiro))
                .addGap(5, 5, 5)
                .addGroup(panPesquisaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panPesquisaLayout.createSequentialGroup()
                        .addComponent(lblUltimo)
                        .addGap(54, 54, 54)
                        .addComponent(lblDescricao))
                    .addGroup(panPesquisaLayout.createSequentialGroup()
                        .addComponent(txtUltimo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        panPesquisaLayout.setVerticalGroup(
            panPesquisaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panPesquisaLayout.createSequentialGroup()
                .addGroup(panPesquisaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUltimo)
                    .addComponent(lblDescricao)
                    .addComponent(lblPrimeiro))
                .addGap(1, 1, 1)
                .addGroup(panPesquisaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panPesquisaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPrimeiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtUltimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panPeriodo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Período dos Dados", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Serif", 1, 12))); // NOI18N

        lblDtInicio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDtInicio.setText("Data inicial:");

        lblDtFim.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDtFim.setText("Data final:");

        txtDtInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtDtInicio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        txtDtFim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtDtFim.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        javax.swing.GroupLayout panPeriodoLayout = new javax.swing.GroupLayout(panPeriodo);
        panPeriodo.setLayout(panPeriodoLayout);
        panPeriodoLayout.setHorizontalGroup(
            panPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panPeriodoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblDtInicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(lblDtFim)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDtFim, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panPeriodoLayout.setVerticalGroup(
            panPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPeriodoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblDtInicio)
                .addComponent(lblDtFim)
                .addComponent(txtDtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtDtFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(207, 207, 207)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(panOpcao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(panFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(panPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(panPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(separador, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panOpcao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separador, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(449, 291));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void radCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radCodigoActionPerformed
        txtPrimeiro.setEnabled(true);
        txtUltimo.setEnabled(true);
        txtDescricao.setText(null);
        txtDescricao.setEnabled(false);
        txtPrimeiro.requestFocus();
    }//GEN-LAST:event_radCodigoActionPerformed

    private void radDescricaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radDescricaoActionPerformed
        txtPrimeiro.setEnabled(false);
        txtPrimeiro.setText(null);
        txtUltimo.setEnabled(false);
        txtUltimo.setText(null);
        txtDescricao.setEnabled(true);
        txtDescricao.requestFocus();
    }//GEN-LAST:event_radDescricaoActionPerformed

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

    private void radTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radTotalActionPerformed
        limpar();
    }//GEN-LAST:event_radTotalActionPerformed

    private void radParcialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radParcialActionPerformed
        radCodigo.setSelected(true);
        radCodigo.setEnabled(true);
        radDescricao.setEnabled(true);
        radCodigo.doClick();
    }//GEN-LAST:event_radParcialActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Caixa.getInstancia().setJanela(null);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnOk;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel lblDescricao;
    private javax.swing.JLabel lblDtFim;
    private javax.swing.JLabel lblDtInicio;
    private javax.swing.JLabel lblPrimeiro;
    private javax.swing.JLabel lblUltimo;
    private javax.swing.JPanel panFiltro;
    private javax.swing.JPanel panOpcao;
    private javax.swing.JPanel panPeriodo;
    private javax.swing.JPanel panPesquisa;
    private javax.swing.JRadioButton radCodigo;
    private javax.swing.JRadioButton radDescricao;
    private javax.swing.JRadioButton radParcial;
    private javax.swing.JRadioButton radTotal;
    private javax.swing.JSeparator separador;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JFormattedTextField txtDtFim;
    private javax.swing.JFormattedTextField txtDtInicio;
    private javax.swing.JFormattedTextField txtPrimeiro;
    private javax.swing.JFormattedTextField txtUltimo;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que limpa os campos deixando no estado inicial.
     */
    private void limpar() {
        radTotal.setSelected(true);
        radCodigo.setSelected(false);
        radCodigo.setEnabled(false);
        radDescricao.setSelected(false);
        radDescricao.setEnabled(false);
        txtPrimeiro.setText(null);
        txtPrimeiro.setEnabled(false);
        txtUltimo.setText(null);
        txtUltimo.setEnabled(false);
        txtDescricao.setText(null);
        txtDescricao.setEnabled(false);
        txtDtInicio.setText(null);
        txtDtFim.setText(null);
    }

    /**
     * Metodo que tem a acao do botao OK.
     */
    private void botaoOK() {
        if (validar()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // conexao com dados
                        CoreService service = new CoreService();
                        // recupera a impressora
                        FiltroTexto ft = new FiltroTexto("ecfImpressoraSerie", ECompara.IGUAL, PAF.AUXILIAR.getProperty("ecf.serie").split(";")[0]);
                        EcfImpressora impressora = new EcfImpressora();
                        impressora = (EcfImpressora) service.selecionar(impressora, ft);
                        // recupera os produtos
                        ProdProduto dados = new ProdProduto();
                        dados.setCampoOrdem(dados.getCampoId());
                        List<ProdProduto> listaProd = service.selecionar(dados, 0, 0, filtro);
                        // executa o comando
                        ComandoEmitirMovimentoECF comando = new ComandoEmitirMovimentoECF(impressora, inicio, fim, listaProd, false);
                        comando.executar();
                        Aguarde.getInstancia().setVisible(false);
                        JOptionPane.showMessageDialog(paf_registros, "Arquivo gerado com sucesso em:\n" + comando.getPath(), "Menu Fiscal", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        Aguarde.getInstancia().setVisible(false);
                        log.error("Nao foi possivel gerar o arquivo -> ", ex);
                        JOptionPane.showMessageDialog(paf_registros, "Não foi possível gerar o arquivo!", "Menu Fiscal", JOptionPane.WARNING_MESSAGE);
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
        inicio = (Date) txtDtInicio.getValue();
        fim = (Date) txtDtFim.getValue();

        if (inicio == null || fim == null) {
            JOptionPane.showMessageDialog(this, "As duas datas precisam ser válidas!", "Registros do PAF-ECF", JOptionPane.WARNING_MESSAGE);
            retorno = false;
        } else if (inicio.compareTo(fim) > 0) {
            JOptionPane.showMessageDialog(this, "A data inicial não pode ser maior que a data final!", "Registros do PAF-ECF", JOptionPane.WARNING_MESSAGE);
            retorno = false;
        } else if (fim.compareTo(new Date()) > 0) {
            JOptionPane.showMessageDialog(this, "A data final não pode ser maior que a data atual!", "Registros do PAF-ECF", JOptionPane.WARNING_MESSAGE);
            retorno = false;
        }

        if (radTotal.isSelected()) {
            filtro = null;
        } else {
            if (radCodigo.isSelected()) {
                if (txtPrimeiro.getText().equals("") || txtUltimo.getText().equals("")) {
                    JOptionPane.showMessageDialog(this, "As duas informações de código são necessárias!", "Estoque", JOptionPane.WARNING_MESSAGE);
                    retorno = false;
                } else {
                    int primeiro = Integer.valueOf(txtPrimeiro.getText());
                    int ultimo = Integer.valueOf(txtUltimo.getText());
                    if (primeiro < 1) {
                        JOptionPane.showMessageDialog(this, "Primeiro código inválido!", "Estoque", JOptionPane.WARNING_MESSAGE);
                        retorno = false;
                    } else if (ultimo < 1) {
                        JOptionPane.showMessageDialog(this, "Último código inválido!", "Estoque", JOptionPane.WARNING_MESSAGE);
                        retorno = false;
                    } else if (primeiro > ultimo) {
                        JOptionPane.showMessageDialog(this, "Último código menor que o primeiro código!", "Estoque", JOptionPane.WARNING_MESSAGE);
                        retorno = false;
                    } else {
                        FiltroNumero fn1 = new FiltroNumero("prodProdutoId", ECompara.MAIOR_IGUAL, primeiro);
                        FiltroNumero fn2 = new FiltroNumero("prodProdutoId", ECompara.MENOR_IGUAL, ultimo);
                        filtro = new FiltroGrupo(Filtro.E, fn1, fn2);
                    }
                }
            } else {
                if (txtDescricao.getText().equals("")) {
                    JOptionPane.showMessageDialog(this, "O texto da descrição é necessário!", "Estoque", JOptionPane.WARNING_MESSAGE);
                    retorno = false;
                } else {
                    filtro = new FiltroTexto("prodProdutoDescricao", ECompara.CONTEM, txtDescricao.getText());
                }
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

    public JLabel getLblDescricao() {
        return lblDescricao;
    }

    public void setLblDescricao(JLabel lblDescricao) {
        this.lblDescricao = lblDescricao;
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
        return panPesquisa;
    }

    public void setPanPeriodo(JPanel panPeriodo) {
        this.panPesquisa = panPeriodo;
    }

    public JRadioButton getRadCodigo() {
        return radCodigo;
    }

    public void setRadCodigo(JRadioButton radCodigo) {
        this.radCodigo = radCodigo;
    }

    public JRadioButton getRadDescricao() {
        return radDescricao;
    }

    public void setRadDescricao(JRadioButton radDescricao) {
        this.radDescricao = radDescricao;
    }

    public JRadioButton getRadParcial() {
        return radParcial;
    }

    public void setRadParcial(JRadioButton radParcial) {
        this.radParcial = radParcial;
    }

    public JRadioButton getRadTotal() {
        return radTotal;
    }

    public void setRadTotal(JRadioButton radTotal) {
        this.radTotal = radTotal;
    }

    public JSeparator getSeparador() {
        return separador;
    }

    public void setSeparador(JSeparator separador) {
        this.separador = separador;
    }

    public JTextField getTxtDescricao() {
        return txtDescricao;
    }

    public void setTxtDescricao(JTextField txtDescricao) {
        this.txtDescricao = txtDescricao;
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
