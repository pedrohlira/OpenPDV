package br.com.openpdv.visao.venda;

import br.com.openpdv.controlador.ECF;
import br.com.openpdv.controlador.EComandoECF;
import br.com.openpdv.controlador.TEF;
import br.com.openpdv.controlador.comandos.ComandoCancelarVenda;
import br.com.openpdv.controlador.comandos.ComandoFecharVenda;
import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import br.com.openpdv.modelo.ecf.EcfPagamentoTipo;
import br.com.openpdv.visao.core.Aguarde;
import br.com.openpdv.visao.core.Caixa;
import br.com.openpdv.visao.core.Gerente;
import java.awt.event.KeyEvent;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 * Classe que representa o fechamento da venda.
 *
 * @author Pedro H. Lira
 */
public class Fechamento extends javax.swing.JDialog {

    private static Fechamento fechamento;
    private Logger log;
    private DefaultTableModel dtmPag;
    private List<EcfPagamentoTipo> tipos;
    private BigDecimal total;
    private BigDecimal acres;
    private BigDecimal desc;
    private BigDecimal apagar;
    private BigDecimal pago;
    private BigDecimal troco;
    private boolean pagDinheiro;
    private boolean pagCartao;
    private int tefs;

    /**
     * Construtor padrao.
     */
    private Fechamento() {
        super(Caixa.getInstancia());
        initComponents();
        log = Logger.getLogger(Fechamento.class);
        dtmPag = (DefaultTableModel) tabPagamentos.getModel();

        CoreService<EcfPagamentoTipo> service = new CoreService<>();
        try {
            tipos = service.selecionar(new EcfPagamentoTipo(), 0, 0, null);
        } catch (OpenPdvException ex) {
            log.error("Erro ao selecionar os tipo de pagamentos.", ex);
            JOptionPane.showMessageDialog(fechamento, "Problemas ao carregar as formas de pagamentos.", "Fechar Venda", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo que retorna a instancia unica de Fechamento.
     *
     * @param total o valor total da venda.
     * @return o objeto de Fechamento.
     */
    public static Fechamento getInstancia(double total) {
        if (fechamento == null) {
            fechamento = new Fechamento();
        }

        fechamento.limpar();
        fechamento.total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP);
        fechamento.atualizar();
        return fechamento;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnDinheiro = new javax.swing.JButton();
        btnCartao = new javax.swing.JButton();
        btnCheque = new javax.swing.JButton();
        btnAcres = new javax.swing.JButton();
        btnDesc = new javax.swing.JButton();
        spPagamentos = new javax.swing.JScrollPane();
        tabPagamentos = new javax.swing.JTable();
        lblPago = new javax.swing.JLabel();
        lblPagoValor = new javax.swing.JLabel();
        separador = new javax.swing.JSeparator();
        lblPagar = new javax.swing.JLabel();
        lblPagarValor = new javax.swing.JLabel();
        lblTroco = new javax.swing.JLabel();
        lblTrocoValor = new javax.swing.JLabel();
        separador1 = new javax.swing.JSeparator();
        lblTEF = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Fechar Venda");
        setModal(true);
        setResizable(false);

        btnDinheiro.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        btnDinheiro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/preco.png"))); // NOI18N
        btnDinheiro.setText("DINHEIRO");
        btnDinheiro.setToolTipText("Pagamento com dinheiro.");
        btnDinheiro.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDinheiro.setMaximumSize(new java.awt.Dimension(100, 75));
        btnDinheiro.setMinimumSize(new java.awt.Dimension(100, 75));
        btnDinheiro.setPreferredSize(new java.awt.Dimension(100, 75));
        btnDinheiro.setSize(new java.awt.Dimension(100, 75));
        btnDinheiro.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDinheiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDinheiroActionPerformed(evt);
            }
        });
        btnDinheiro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDinheiroKeyPressed(evt);
            }
        });

        btnCartao.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        btnCartao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cartao.png"))); // NOI18N
        btnCartao.setText("CARTÃO");
        btnCartao.setToolTipText("Pagamento com cartão de crédito ou débito.");
        btnCartao.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCartao.setMaximumSize(new java.awt.Dimension(100, 75));
        btnCartao.setMinimumSize(new java.awt.Dimension(100, 75));
        btnCartao.setPreferredSize(new java.awt.Dimension(100, 75));
        btnCartao.setSize(new java.awt.Dimension(100, 75));
        btnCartao.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCartao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCartaoActionPerformed(evt);
            }
        });
        btnCartao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCartaoKeyPressed(evt);
            }
        });

        btnCheque.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        btnCheque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cheque.png"))); // NOI18N
        btnCheque.setText("CHEQUE");
        btnCheque.setToolTipText("Pagamento com cheque");
        btnCheque.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCheque.setMaximumSize(new java.awt.Dimension(100, 75));
        btnCheque.setMinimumSize(new java.awt.Dimension(100, 75));
        btnCheque.setPreferredSize(new java.awt.Dimension(100, 75));
        btnCheque.setSize(new java.awt.Dimension(100, 75));
        btnCheque.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCheque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChequeActionPerformed(evt);
            }
        });
        btnCheque.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnChequeKeyPressed(evt);
            }
        });

        btnAcres.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        btnAcres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/valor.png"))); // NOI18N
        btnAcres.setText("ACRÉSCIMO");
        btnAcres.setToolTipText("Adicionar acréscimo.");
        btnAcres.setMaximumSize(new java.awt.Dimension(100, 40));
        btnAcres.setMinimumSize(new java.awt.Dimension(100, 40));
        btnAcres.setPreferredSize(new java.awt.Dimension(100, 40));
        btnAcres.setSize(new java.awt.Dimension(100, 40));
        btnAcres.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAcres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcresActionPerformed(evt);
            }
        });
        btnAcres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAcresKeyPressed(evt);
            }
        });

        btnDesc.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        btnDesc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/receber.png"))); // NOI18N
        btnDesc.setText("DESCONTO");
        btnDesc.setToolTipText("Adicionar desconto.");
        btnDesc.setMaximumSize(new java.awt.Dimension(100, 40));
        btnDesc.setMinimumSize(new java.awt.Dimension(100, 40));
        btnDesc.setPreferredSize(new java.awt.Dimension(100, 40));
        btnDesc.setSize(new java.awt.Dimension(100, 40));
        btnDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescActionPerformed(evt);
            }
        });
        btnDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDescKeyPressed(evt);
            }
        });

        tabPagamentos.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabPagamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Tipo", "Valor", "Confirmado", "Data", "NSU", "Arquivo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabPagamentos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabPagamentos.setFocusable(false);
        tabPagamentos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabPagamentos.getTableHeader().setReorderingAllowed(false);
        spPagamentos.setViewportView(tabPagamentos);
        tabPagamentos.getColumnModel().getColumn(0).setMinWidth(0);
        tabPagamentos.getColumnModel().getColumn(0).setPreferredWidth(0);
        tabPagamentos.getColumnModel().getColumn(0).setMaxWidth(0);
        tabPagamentos.getColumnModel().getColumn(1).setResizable(false);
        tabPagamentos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabPagamentos.getColumnModel().getColumn(2).setResizable(false);
        tabPagamentos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabPagamentos.getColumnModel().getColumn(3).setResizable(false);
        tabPagamentos.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabPagamentos.getColumnModel().getColumn(4).setMinWidth(0);
        tabPagamentos.getColumnModel().getColumn(4).setPreferredWidth(0);
        tabPagamentos.getColumnModel().getColumn(4).setMaxWidth(0);
        tabPagamentos.getColumnModel().getColumn(5).setMinWidth(0);
        tabPagamentos.getColumnModel().getColumn(5).setPreferredWidth(0);
        tabPagamentos.getColumnModel().getColumn(5).setMaxWidth(0);
        tabPagamentos.getColumnModel().getColumn(6).setMinWidth(0);
        tabPagamentos.getColumnModel().getColumn(6).setPreferredWidth(0);
        tabPagamentos.getColumnModel().getColumn(6).setMaxWidth(0);

        lblPago.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        lblPago.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPago.setText("TOTAL PAGO :");

        lblPagoValor.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        lblPagoValor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPagoValor.setText("R$ 0,00");

        separador.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblPagar.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblPagar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPagar.setText("TOTAL Á PAGAR");

        lblPagarValor.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblPagarValor.setForeground(new java.awt.Color(255, 0, 0));
        lblPagarValor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPagarValor.setText("R$ 0,00");

        lblTroco.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblTroco.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTroco.setText("TROCO");

        lblTrocoValor.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblTrocoValor.setForeground(new java.awt.Color(0, 0, 255));
        lblTrocoValor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTrocoValor.setText("R$ 0,00");

        lblTEF.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N

        btnCancelar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cancelar.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setToolTipText("Cancela o processo de fechamento.");
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
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, separador1)
                    .add(layout.createSequentialGroup()
                        .add(12, 12, 12)
                        .add(lblTEF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(112, 112, 112)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(lblPago, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(44, 44, 44)
                                .add(lblPagoValor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(spPagamentos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(btnAcres, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 156, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnDesc, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(layout.createSequentialGroup()
                                .add(btnDinheiro, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnCartao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(btnCheque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(lblTroco, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblPagarValor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(lblTrocoValor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(lblPagar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 315, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(0, 10, Short.MAX_VALUE)))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(btnDinheiro, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnCartao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnCheque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btnAcres, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnDesc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(spPagamentos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblPagoValor)
                            .add(lblPago)))
                    .add(layout.createSequentialGroup()
                        .add(lblPagar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblPagarValor)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(lblTroco)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblTrocoValor))
                    .add(separador))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, lblTEF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(48, 48, 48))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-692)/2, (screenSize.height-363)/2, 692, 363);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDinheiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDinheiroActionPerformed
        Object obj = JOptionPane.showInputDialog(this, "Digite o valor do pagamento.", "DINHEIRO",
                JOptionPane.OK_CANCEL_OPTION, btnDinheiro.getIcon(), null, Util.formataNumero(apagar.subtract(pago).doubleValue(), 1, 2, false));

        if (obj != null) {
            try {
                String texto = obj.toString().replace(".", "").replace(",", ".");
                BigDecimal valor = new BigDecimal(texto).setScale(2, RoundingMode.HALF_UP);
                dinheiro(valor);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Valor informado inválido!", "DINHEIRO", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDinheiroActionPerformed

    private void btnDinheiroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDinheiroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnDinheiroActionPerformed(null);
        }
    }//GEN-LAST:event_btnDinheiroKeyPressed

    private void btnCartaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCartaoActionPerformed
        Object obj = JOptionPane.showInputDialog(this, "Digite o valor do pagamento.", "CARTÃO",
                JOptionPane.OK_CANCEL_OPTION, btnCartao.getIcon(), null, Util.formataNumero(apagar.subtract(pago).doubleValue(), 1, 2, false));

        if (obj != null) {
            try {
                String texto = obj.toString().replace(".", "").replace(",", ".");
                BigDecimal valor = new BigDecimal(texto).setScale(2, RoundingMode.HALF_UP);
                cartao(valor);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Valor informado inválido!", "CARTÃO", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnCartaoActionPerformed

    private void btnCartaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCartaoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnCartaoActionPerformed(null);
        }
    }//GEN-LAST:event_btnCartaoKeyPressed

    private void btnAcresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcresActionPerformed
        acresdesc(100, "ACRESCIMO");
    }//GEN-LAST:event_btnAcresActionPerformed

    private void btnAcresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAcresKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnAcresActionPerformed(null);
        }
    }//GEN-LAST:event_btnAcresKeyPressed

    private void btnDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescActionPerformed
        Gerente.getInstancia(new AsyncCallback<Integer>() {

            @Override
            public void sucesso(Integer resultado) {
                acresdesc(resultado, "DESCONTO");
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(null, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        }).setVisible(true);
    }//GEN-LAST:event_btnDescActionPerformed

    private void btnDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDescKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnDescActionPerformed(null);
        }
    }//GEN-LAST:event_btnDescKeyPressed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnCancelarActionPerformed(null);
        }
    }//GEN-LAST:event_btnCancelarKeyPressed

    private void btnChequeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChequeActionPerformed
        Object obj = JOptionPane.showInputDialog(this, "Digite o valor do pagamento.", "CHEQUE",
                JOptionPane.OK_CANCEL_OPTION, btnCheque.getIcon(), null, Util.formataNumero(apagar.subtract(pago).doubleValue(), 1, 2, false));

        if (obj != null) {
            try {
                String texto = obj.toString().replace(".", "").replace(",", ".");
                BigDecimal valor = new BigDecimal(texto).setScale(2, RoundingMode.HALF_UP);
                cheque(valor);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Valor informado inválido!", "CHEQUE", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnChequeActionPerformed

    private void btnChequeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnChequeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnChequeActionPerformed(null);
        }
    }//GEN-LAST:event_btnChequeKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAcres;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCartao;
    private javax.swing.JButton btnCheque;
    private javax.swing.JButton btnDesc;
    private javax.swing.JButton btnDinheiro;
    private javax.swing.JLabel lblPagar;
    private javax.swing.JLabel lblPagarValor;
    private javax.swing.JLabel lblPago;
    private javax.swing.JLabel lblPagoValor;
    private javax.swing.JLabel lblTEF;
    private javax.swing.JLabel lblTroco;
    private javax.swing.JLabel lblTrocoValor;
    private javax.swing.JSeparator separador;
    private javax.swing.JSeparator separador1;
    private javax.swing.JScrollPane spPagamentos;
    private javax.swing.JTable tabPagamentos;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que trata do pagamento com dinheiro.
     *
     * @param valor o dinheiro informado do pagamento.
     */
    private void dinheiro(BigDecimal valor) {
        int pos = -1;
        for (int i = 0; i < dtmPag.getRowCount(); i++) {
            String tipo = dtmPag.getValueAt(i, 1).toString();
            if (tipo.equals("DINHEIRO")) {
                pos = i;
                break;
            }
        }

        if (valor.compareTo(new BigDecimal(0.00)) > 0) {
            if (pos > -1) {
                dtmPag.setValueAt(valor.doubleValue(), pos, 2);
            } else {
                dtmPag.addRow(new Object[]{Util.getConfig().get("ecf.dinheiro"), "DINHEIRO", valor.doubleValue(), true, new Date(), "", ""});
            }
        } else if (pos > -1) {
            dtmPag.removeRow(pos);
        }
        atualizar();
    }

    /**
     * Metodo que trata do pagamento com cartao.
     *
     * @param valor o valor informado do pagamento.
     */
    private void cartao(final BigDecimal valor) {
        if (valor.doubleValue() > 0.00 && valor.compareTo(apagar.subtract(pago)) <= 0) {
            if (tefs < Integer.valueOf(Util.getConfig().get("tef.cartoes"))) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // chama o metodo do tef para operacao com cartao
                        String coo = Util.formataNumero(Caixa.getInstancia().getVenda().getEcfVendaCoo(), 6, 0, false);
                        try {
                            String id = TEF.gerarId();
                            TEF.realizarTransacao(id, coo, valor.doubleValue());

                            String rede = TEF.getDados().get("010-000");
                            Date data = new SimpleDateFormat("ddMMyyyyHHmmss").parse(TEF.getDados().get("022-000") + TEF.getDados().get("023-000"));
                            String nsu = TEF.getDados().get("012-000");
                            String arquivo = TEF.getPathTmp().getAbsolutePath() + System.getProperty("file.separator") + "pendente" + id + ".txt";
                            boolean confirmado = false;

                            // caso o pagamento seja menor que o total restante, podendo ter outro cartao
                            if (valor.compareTo(apagar.subtract(pago)) < 0) {
                                // confirma e gera o backup
                                String msg = TEF.getDados().get("030-000");
                                TEF.confirmarTransacao(id, true);
                                lblTEF.setText("TEF [" + msg + "]");
                                confirmado = true;
                                File pendente = new File(arquivo);
                                arquivo = TEF.getPathTmp().getAbsolutePath() + System.getProperty("file.separator") + "backup" + id + ".txt";
                                File backup = new File(arquivo);
                                pendente.renameTo(backup);
                            }
                            dtmPag.addRow(new Object[]{"00", rede, valor.doubleValue(), confirmado, data, nsu, arquivo});
                            tefs++;

                            Aguarde.getInstancia().setVisible(false);
                            atualizar();
                        } catch (Exception ex) {
                            Aguarde.getInstancia().setVisible(false);
                            if (ex.getMessage() != null) {
                                JOptionPane.showMessageDialog(fechamento, ex.getMessage(), "TEF", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }).start();

                Aguarde.getInstancia().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Máximo de " + Util.getConfig().get("tef.cartoes") + " pagamentos com TEF atingido!", "TEF", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Valor informado deve ser maior que zero e menor ou igual ao total a pagar!", "CARTÃO", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo que trata do pagamento com cheque.
     *
     * @param valor o valor informado do pagamento.
     */
    private void cheque(final BigDecimal valor) {
        if (valor.doubleValue() > 0.00 && valor.compareTo(apagar.subtract(pago)) <= 0) {
            if (tefs < Integer.valueOf(Util.getConfig().get("tef.cartoes"))) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // chama o metodo do tef para operacao com cheque
                        try {
                            String id = TEF.gerarId();
                            TEF.consultarCheque(id, valor.doubleValue());

                            String rede = TEF.getDados().get("010-000");
                            Date data = new SimpleDateFormat("ddMMyyyyHHmmss").parse(TEF.getDados().get("022-000") + TEF.getDados().get("023-000"));
                            String nsu = TEF.getDados().get("012-000");
                            String arquivo = TEF.getPathTmp().getAbsolutePath() + System.getProperty("file.separator") + "pendente" + id + ".txt";
                            boolean confirmado = false;

                            // caso o pagamento seja menor que o total restante, podendo ter outro cartao
                            if (valor.compareTo(apagar.subtract(pago)) < 0) {
                                // confirma e gera o backup
                                String msg = TEF.getDados().get("030-000");
                                TEF.confirmarTransacao(id, true);
                                lblTEF.setText("TEF [" + msg + "]");
                                confirmado = true;
                                File pendente = new File(arquivo);
                                arquivo = TEF.getPathTmp().getAbsolutePath() + System.getProperty("file.separator") + "backup" + id + ".txt";
                                File backup = new File(arquivo);
                                pendente.renameTo(backup);
                            }
                            dtmPag.addRow(new Object[]{Util.getConfig().get("ecf.cheque"), rede, valor.doubleValue(), confirmado, data, nsu, arquivo});

                            Aguarde.getInstancia().setVisible(false);
                            atualizar();
                        } catch (Exception ex) {
                            Aguarde.getInstancia().setVisible(false);
                            if (ex.getMessage() != null) {
                                JOptionPane.showMessageDialog(fechamento, ex.getMessage(), "TEF", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }).start();
                Aguarde.getInstancia().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Máximo de " + Util.getConfig().get("tef.cartoes") + " pagamentos com TEF atingido!", "TEF", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Valor informado deve ser maior que zero e menor ou igual ao total a pagar!", "CHEQUE", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    /**
     * Metodo que trata do acrescimo e desconto da venda.
     *
     * @param max o maximo em porcentagem que pode oferecer.
     * @param texto o texto de distingue o ACRESCIMO do DESCONTO.
     */
    private void acresdesc(int max, String nome) {
        String texto = JOptionPane.showInputDialog(this, "Digite o valor.\nPara porcentagem coloque % no final.", nome.toUpperCase(), JOptionPane.OK_CANCEL_OPTION);
        boolean porcento = false;

        if (texto != null) {
            texto = texto.replace(".", "").replace(",", ".");
            if (texto.endsWith("%")) {
                porcento = true;
                texto = texto.replace("%", "");
            }

            try {
                BigDecimal valor = new BigDecimal(texto).setScale(2, RoundingMode.HALF_UP);
                int pos = -1;
                if (porcento) {
                    valor = total.multiply(valor).divide(new BigDecimal(100));
                }

                for (int i = 0; i < dtmPag.getRowCount(); i++) {
                    String tipo = dtmPag.getValueAt(i, 1).toString();
                    if (tipo.equals(nome.toUpperCase())) {
                        pos = i;
                        break;
                    }
                }

                if (valor.doubleValue() > 0.00 && valor.compareTo(total) < 0) {
                    BigDecimal maximo = total.multiply(new BigDecimal(max)).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
                    if (maximo.compareTo(valor) >= 0) {
                        if (pos > -1) {
                            dtmPag.setValueAt(valor, pos, 2);
                        } else {
                            dtmPag.addRow(new Object[]{"", nome.toUpperCase(), valor, false, null, "", ""});
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Valor informado é maior que o permitido!\nPorcentagem máximo permitida pelo gerente [" + max + "%]", nome.toUpperCase(), JOptionPane.WARNING_MESSAGE);
                    }
                } else if (pos > -1) {
                    dtmPag.removeRow(pos);
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Valor informado inválido!", nome.toUpperCase(), JOptionPane.INFORMATION_MESSAGE);
            }
            atualizar();
        }
    }

    /**
     * Metodo que limpa a tela de fechamento.
     */
    private void limpar() {
        // botoes
        btnDinheiro.setEnabled(true);
        btnAcres.setEnabled(true);
        btnDesc.setEnabled(true);
        btnCancelar.setEnabled(true);
        // pagamentos
        while (dtmPag.getRowCount() > 0) {
            dtmPag.removeRow(0);
        }
        // variaveis
        total = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        acres = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        desc = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        pago = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        apagar = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        troco = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        tefs = 0;
        // rotulos
        lblPagarValor.setText("R$ 0,00");
        lblPagoValor.setText("R$ 0,00");
        lblTrocoValor.setText("R$ 0,00");
        lblTEF.setText("");
    }

    /**
     * Metodo que atualiza os valores visuais para o caixa.
     */
    private void atualizar() {
        // calcula
        acres = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        desc = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        pago = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        pagDinheiro = false;
        pagCartao = false;

        for (int i = 0; i < dtmPag.getRowCount(); i++) {
            String tipo = dtmPag.getValueAt(i, 1).toString();
            BigDecimal valor = new BigDecimal(dtmPag.getValueAt(i, 2).toString()).setScale(2, RoundingMode.HALF_UP);
            switch (tipo) {
                case "ACRESCIMO":
                    acres = valor;
                    break;
                case "DESCONTO":
                    desc = valor;
                    break;
                default:
                    pago = pago.add(valor);
                    pagDinheiro |= tipo.equals("DINHEIRO");
                    pagCartao |= !tipo.equals("DINHEIRO");
                    break;
            }
        }
        apagar = total.add(acres).subtract(desc);
        troco = pago.compareTo(apagar) > 0 && pagDinheiro ? pago.subtract(apagar) : new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        // imprime na tela
        lblPagarValor.setText("R$ " + Util.formataNumero(apagar.doubleValue(), 1, 2, true));
        lblPagoValor.setText("R$ " + Util.formataNumero(pago.doubleValue(), 1, 2, true));
        lblTrocoValor.setText("R$ " + Util.formataNumero(troco.doubleValue(), 1, 2, true));

        if (pago.compareTo(apagar) >= 0) {
            fechar();
        }
    }

    /**
     * Metodo que efetua o pagamento na ECF, BD e Tela.
     */
    private void fechar() {
        btnDinheiro.setEnabled(false);
        btnCartao.setEnabled(false);
        btnAcres.setEnabled(false);
        btnDesc.setEnabled(false);
        btnCancelar.setEnabled(false);

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (pagDinheiro) {
                        ECF.enviar(EComandoECF.ECF_AbreGaveta);
                    }
                    new ComandoFecharVenda(getPagamentos(), total.doubleValue(), acres.subtract(desc).doubleValue(), troco.doubleValue()).executar();
                } catch (OpenPdvException ex) {
                    Aguarde.getInstancia().getLblMensagem().setText("Aguarde o processamento...");
                    Aguarde.getInstancia().setVisible(false);
                    log.error("Erro ao fechar a venda.", ex);
                    JOptionPane.showMessageDialog(fechamento, "Não foi possível fechar a venda completamente.\nA venda será cancelada!", "Fechar Venda", JOptionPane.ERROR_MESSAGE);
                    cancelar();
                } finally {
                    dispose();
                }
            }
        }).start();

        if (TEF.getDados() != null) {
            Aguarde.getInstancia().getLblMensagem().setText(TEF.getDados().get("030-000"));
        }
        Aguarde.getInstancia().setVisible(true);
    }

    /**
     * Metodo que cancela o pagamento.
     */
    private void cancelar() {
        if (pagCartao) {
            int escolha = JOptionPane.showOptionDialog(this, "Já tem pagamento com cartão autorizado.\nDeseja cancelar a venda atual?", "Fechar Venda",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                Gerente.getInstancia(new AsyncCallback<Integer>() {

                    @Override
                    public void sucesso(Integer resultado) {
                        try {
                            new ComandoCancelarVenda().executar();
                        } catch (OpenPdvException ex) {
                            JOptionPane.showMessageDialog(null, "Erro ao cancelar a venda.", "Fechar Venda", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    @Override
                    public void falha(Exception excecao) {
                        JOptionPane.showMessageDialog(null, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
                    }
                }).setVisible(true);
            }
        } else {
            Caixa.getInstancia().statusMenus(Caixa.getInstancia().getModo());
            dispose();
        }
    }

    /**
     * Metodo que identifica os pagamentos de acordo com o tipo
     *
     * @return a lista de pagamentos de vendas.
     */
    private List<EcfPagamento> getPagamentos() {
        List<EcfPagamento> pagamentos = new ArrayList<>();
        // reserva o primeiro pagamento
        pagamentos.add(null);

        for (int i = 0; i < dtmPag.getRowCount(); i++) {
            String codigo = dtmPag.getValueAt(i, 0).toString();
            String rede = dtmPag.getValueAt(i, 1).toString();
            Double valor = (Double) dtmPag.getValueAt(i, 2);
            Date data = (Date) dtmPag.getValueAt(i, 4);
            String nsu = dtmPag.getValueAt(i, 5).toString();
            String arquivo = dtmPag.getValueAt(i, 6).toString();

            for (EcfPagamentoTipo tipo : tipos) {
                if (tipo.getEcfPagamentoTipoCodigo().equals(codigo) || tipo.getEcfPagamentoTipoRede().equalsIgnoreCase(rede)) {
                    EcfPagamento vendaPagamento = new EcfPagamento();
                    vendaPagamento.setEcfVenda(Caixa.getInstancia().getVenda());
                    vendaPagamento.setEcfPagamentoTipo(tipo);
                    vendaPagamento.setEcfPagamentoGnf(0);
                    vendaPagamento.setEcfPagamentoData(data);
                    vendaPagamento.setEcfPagamentoValor(valor);
                    vendaPagamento.setEcfPagamentoNsu(nsu);
                    vendaPagamento.setEcfPagamentoEstorno('N');
                    vendaPagamento.setArquivo(arquivo);
                    if (codigo.equals(Util.getConfig().get("ecf.dinheiro"))) {
                        // se dinheiro coloca na primeira posicao
                        pagamentos.set(0, vendaPagamento);
                    } else {
                        // se cartao ou cheque adiciona e deixa na ordem
                        pagamentos.add(vendaPagamento);
                    }
                    break;
                }
            }
        }

        // verifica se teve dinheiro, caso contrario remove a posicao 0
        if (pagamentos.get(0) == null) {
            pagamentos.remove(0);
        }
        return pagamentos;
    }
}
