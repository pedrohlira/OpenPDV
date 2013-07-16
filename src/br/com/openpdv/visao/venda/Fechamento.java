package br.com.openpdv.visao.venda;

import br.com.openpdv.controlador.comandos.ComandoCancelarVenda;
import br.com.openpdv.controlador.comandos.ComandoCupomPresente;
import br.com.openpdv.controlador.comandos.ComandoFecharVenda;
import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.TableCellRendererNumber;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.EModo;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfPagamento;
import br.com.openpdv.modelo.ecf.EcfPagamentoTipo;
import br.com.openpdv.modelo.ecf.EcfTroca;
import br.com.openpdv.visao.core.Aguarde;
import br.com.openpdv.visao.core.Caixa;
import br.com.openpdv.visao.core.Gerente;
import br.com.openpdv.visao.principal.Trocas;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import br.com.phdss.TEF;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.h2.store.fs.FileUtils;

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
    private int limite;
    private EModo modo;
    private String obs;

    /**
     * Construtor padrao.
     */
    private Fechamento() {
        super(Caixa.getInstancia());
        initComponents();
        log = Logger.getLogger(Fechamento.class);
        dtmPag = (DefaultTableModel) tabPagamentos.getModel();
        limite = Util.getConfig().get("tef.cartoes") != null ? Integer.valueOf(Util.getConfig().get("tef.cartoes")) : 1;

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

        Caixa caixa = Caixa.getInstancia();
        fechamento.modo = caixa.getModo();
        caixa.setModo(EModo.OFF);
        caixa.statusMenus(EModo.OFF);
        caixa.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        return fechamento;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnDinheiro = new javax.swing.JButton();
        btnCartao = new javax.swing.JButton();
        btnCheque = new javax.swing.JButton();
        btnOutros = new javax.swing.JButton();
        btnAcres = new javax.swing.JButton();
        btnDesc = new javax.swing.JButton();
        btnTroca = new javax.swing.JButton();
        spPagamentos = new javax.swing.JScrollPane();
        tabPagamentos = new javax.swing.JTable();
        lblPago = new javax.swing.JLabel();
        lblPagoValor = new javax.swing.JLabel();
        separador = new javax.swing.JSeparator();
        lblPagar = new javax.swing.JLabel();
        lblPagarValor = new javax.swing.JLabel();
        lblTroco = new javax.swing.JLabel();
        lblTrocoValor = new javax.swing.JLabel();
        chkPresente = new javax.swing.JCheckBox();
        separador1 = new javax.swing.JSeparator();
        lblTEF = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Fechar Venda");
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        btnDinheiro.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnDinheiro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/preco.png"))); // NOI18N
        btnDinheiro.setText("<html><center><b>D</b>INHEIRO</center></html>");
        btnDinheiro.setToolTipText("Pagamento com dinheiro. Atalho = D");
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

        btnCartao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnCartao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cartao.png"))); // NOI18N
        btnCartao.setText("<html><center><b>C</b>ARTÃO</center></html>");
        btnCartao.setToolTipText("Pagamento com cartão. Atalho = C");
        btnCartao.setMaximumSize(new java.awt.Dimension(100, 75));
        btnCartao.setMinimumSize(new java.awt.Dimension(100, 75));
        btnCartao.setPreferredSize(new java.awt.Dimension(100, 75));
        btnCartao.setSize(new java.awt.Dimension(100, 75));
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

        btnCheque.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnCheque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cheque.png"))); // NOI18N
        btnCheque.setText("<html><center>C<b>H</b>EQUE</center></html>");
        btnCheque.setToolTipText("Pagamento com cheque. Atalho = H");
        btnCheque.setMaximumSize(new java.awt.Dimension(100, 75));
        btnCheque.setMinimumSize(new java.awt.Dimension(100, 75));
        btnCheque.setPreferredSize(new java.awt.Dimension(100, 75));
        btnCheque.setSize(new java.awt.Dimension(100, 75));
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

        btnOutros.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnOutros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/informacao.png"))); // NOI18N
        btnOutros.setText("<html><center><b>O</b>UTROS</center></html>");
        btnOutros.setToolTipText("Adicionar valor de troca. Atalho = T");
        btnOutros.setMaximumSize(new java.awt.Dimension(100, 40));
        btnOutros.setMinimumSize(new java.awt.Dimension(100, 40));
        btnOutros.setPreferredSize(new java.awt.Dimension(100, 40));
        btnOutros.setSize(new java.awt.Dimension(100, 40));
        btnOutros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutrosActionPerformed(evt);
            }
        });
        btnOutros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnOutrosKeyPressed(evt);
            }
        });

        btnAcres.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnAcres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/valor.png"))); // NOI18N
        btnAcres.setText("<html><center><b>A</b>CRÉS</center></html>");
        btnAcres.setToolTipText("Adicionar acréscimo. Atalho = A");
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

        btnDesc.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnDesc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/receber.png"))); // NOI18N
        btnDesc.setText("<html><center>D<b>E</b>SC</center></html>");
        btnDesc.setToolTipText("Adicionar desconto. Atalho = E");
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

        btnTroca.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnTroca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/sincroniza.png"))); // NOI18N
        btnTroca.setText("<html><center><b>T</b>ROCA</center></html>");
        btnTroca.setToolTipText("Adicionar valor de troca. Atalho = T");
        btnTroca.setMaximumSize(new java.awt.Dimension(100, 40));
        btnTroca.setMinimumSize(new java.awt.Dimension(100, 40));
        btnTroca.setPreferredSize(new java.awt.Dimension(100, 40));
        btnTroca.setSize(new java.awt.Dimension(100, 40));
        btnTroca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrocaActionPerformed(evt);
            }
        });
        btnTroca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnTrocaKeyPressed(evt);
            }
        });

        spPagamentos.setFocusTraversalKeysEnabled(false);
        spPagamentos.setFocusable(false);
        spPagamentos.setRequestFocusEnabled(false);
        spPagamentos.setVerifyInputWhenFocusTarget(false);

        tabPagamentos.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabPagamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Tipo", "Debito", "Valor", "Confirmado", "Data", "NSU", "Arquivo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Double.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabPagamentos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabPagamentos.setFocusTraversalKeysEnabled(false);
        tabPagamentos.setFocusable(false);
        tabPagamentos.setRequestFocusEnabled(false);
        tabPagamentos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabPagamentos.getTableHeader().setReorderingAllowed(false);
        tabPagamentos.setVerifyInputWhenFocusTarget(false);
        spPagamentos.setViewportView(tabPagamentos);
        tabPagamentos.getColumnModel().getColumn(0).setMinWidth(0);
        tabPagamentos.getColumnModel().getColumn(0).setPreferredWidth(0);
        tabPagamentos.getColumnModel().getColumn(0).setMaxWidth(0);
        tabPagamentos.getColumnModel().getColumn(1).setResizable(false);
        tabPagamentos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabPagamentos.getColumnModel().getColumn(2).setMinWidth(0);
        tabPagamentos.getColumnModel().getColumn(2).setPreferredWidth(0);
        tabPagamentos.getColumnModel().getColumn(2).setMaxWidth(0);
        tabPagamentos.getColumnModel().getColumn(3).setResizable(false);
        tabPagamentos.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabPagamentos.getColumnModel().getColumn(3).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
        tabPagamentos.getColumnModel().getColumn(4).setResizable(false);
        tabPagamentos.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabPagamentos.getColumnModel().getColumn(5).setMinWidth(0);
        tabPagamentos.getColumnModel().getColumn(5).setPreferredWidth(0);
        tabPagamentos.getColumnModel().getColumn(5).setMaxWidth(0);
        tabPagamentos.getColumnModel().getColumn(6).setMinWidth(0);
        tabPagamentos.getColumnModel().getColumn(6).setPreferredWidth(0);
        tabPagamentos.getColumnModel().getColumn(6).setMaxWidth(0);
        tabPagamentos.getColumnModel().getColumn(7).setMinWidth(0);
        tabPagamentos.getColumnModel().getColumn(7).setPreferredWidth(0);
        tabPagamentos.getColumnModel().getColumn(7).setMaxWidth(0);

        lblPago.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        lblPago.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPago.setText("TOTAL PAGO :");
        lblPago.setFocusTraversalKeysEnabled(false);
        lblPago.setFocusable(false);
        lblPago.setRequestFocusEnabled(false);
        lblPago.setVerifyInputWhenFocusTarget(false);

        lblPagoValor.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        lblPagoValor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPagoValor.setText("R$ 0,00");
        lblPagoValor.setFocusTraversalKeysEnabled(false);
        lblPagoValor.setFocusable(false);
        lblPagoValor.setRequestFocusEnabled(false);
        lblPagoValor.setVerifyInputWhenFocusTarget(false);

        separador.setOrientation(javax.swing.SwingConstants.VERTICAL);
        separador.setFocusTraversalKeysEnabled(false);
        separador.setRequestFocusEnabled(false);
        separador.setVerifyInputWhenFocusTarget(false);

        lblPagar.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblPagar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPagar.setText("TOTAL Á PAGAR");
        lblPagar.setFocusTraversalKeysEnabled(false);
        lblPagar.setFocusable(false);
        lblPagar.setRequestFocusEnabled(false);
        lblPagar.setVerifyInputWhenFocusTarget(false);

        lblPagarValor.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblPagarValor.setForeground(new java.awt.Color(255, 0, 0));
        lblPagarValor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPagarValor.setText("R$ 0,00");
        lblPagarValor.setFocusTraversalKeysEnabled(false);
        lblPagarValor.setFocusable(false);
        lblPagarValor.setRequestFocusEnabled(false);
        lblPagarValor.setVerifyInputWhenFocusTarget(false);

        lblTroco.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblTroco.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTroco.setText("TROCO");
        lblTroco.setFocusTraversalKeysEnabled(false);
        lblTroco.setFocusable(false);
        lblTroco.setRequestFocusEnabled(false);
        lblTroco.setVerifyInputWhenFocusTarget(false);

        lblTrocoValor.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblTrocoValor.setForeground(new java.awt.Color(0, 0, 255));
        lblTrocoValor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTrocoValor.setText("R$ 0,00");
        lblTrocoValor.setFocusTraversalKeysEnabled(false);
        lblTrocoValor.setFocusable(false);
        lblTrocoValor.setVerifyInputWhenFocusTarget(false);

        chkPresente.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        chkPresente.setText("Imprimir cupom para presente.");

        separador1.setFocusTraversalKeysEnabled(false);
        separador1.setRequestFocusEnabled(false);
        separador1.setVerifyInputWhenFocusTarget(false);

        lblTEF.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        lblTEF.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTEF.setFocusTraversalKeysEnabled(false);
        lblTEF.setFocusable(false);
        lblTEF.setRequestFocusEnabled(false);
        lblTEF.setVerifyInputWhenFocusTarget(false);

        btnCancelar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cancelar.png"))); // NOI18N
        btnCancelar.setText("Cancelar [ESC]");
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
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(lblTEF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 511, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnCancelar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                        .add(lblPago, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(44, 44, 44)
                                        .add(lblPagoValor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, spPagamentos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                        .add(btnAcres, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(btnDesc, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(btnTroca, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                            .add(btnCheque, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                                            .add(btnDinheiro, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                            .add(btnCartao, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                                            .add(btnOutros, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .add(0, 0, Short.MAX_VALUE)))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblTroco, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, lblPagarValor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(lblTrocoValor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(layout.createSequentialGroup()
                                        .add(lblPagar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 315, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(0, 10, Short.MAX_VALUE))
                                    .add(chkPresente, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, btnCartao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .add(btnDinheiro, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(btnCheque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnOutros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btnAcres, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(btnDesc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(btnTroca, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(spPagamentos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblPagoValor)
                            .add(lblPago)))
                    .add(separador)
                    .add(layout.createSequentialGroup()
                        .add(lblPagar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblPagarValor)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(lblTroco)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblTrocoValor)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chkPresente)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblTEF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-692)/2, (screenSize.height-360)/2, 692, 360);
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
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnCancelar.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnCartao.requestFocus();
        } else {
            atalhos(evt);
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
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnDinheiro.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnCheque.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnCartaoKeyPressed

    private void btnAcresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcresActionPerformed
        final Gerente gerente = Gerente.getInstancia(null);
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                Caixa.getInstancia().getVenda().setSisGerente(gerente.getSisGerente());
                String texto = JOptionPane.showInputDialog(fechamento, "Digite o valor.\nPara porcentagem coloque % no final.", "ACRESCIMO", JOptionPane.OK_CANCEL_OPTION);
                acresdesc(0, "ACRESCIMO", texto);
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(fechamento, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        gerente.setAsync(async);
        gerente.setVisible(true);
    }//GEN-LAST:event_btnAcresActionPerformed

    private void btnAcresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAcresKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnAcresActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnOutros.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnDesc.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnAcresKeyPressed

    private void btnDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescActionPerformed
        final Gerente gerente = Gerente.getInstancia(null);
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                Caixa.getInstancia().getVenda().setSisGerente(gerente.getSisGerente());
                String texto = JOptionPane.showInputDialog(fechamento, "Digite o valor.\nPara porcentagem coloque % no final.", "DESCONTO", JOptionPane.OK_CANCEL_OPTION);
                acresdesc(resultado, "DESCONTO", texto);
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(fechamento, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        gerente.setAsync(async);
        gerente.setVisible(true);
    }//GEN-LAST:event_btnDescActionPerformed

    private void btnDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDescKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnDescActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnAcres.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnTroca.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnDescKeyPressed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cancelar();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnTroca.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnDinheiro.requestFocus();
        } else {
            atalhos(evt);
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
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnCartao.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnOutros.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnChequeKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        atalhos(evt);
    }//GEN-LAST:event_formKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        btnDinheiro.requestFocus();
    }//GEN-LAST:event_formWindowActivated

    private void btnTrocaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrocaActionPerformed
        final Gerente gerente = Gerente.getInstancia(null);
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                Caixa.getInstancia().getVenda().setSisGerente(gerente.getSisGerente());
                trocar();
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(fechamento, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        gerente.setAsync(async);
        gerente.setVisible(true);
    }//GEN-LAST:event_btnTrocaActionPerformed

    private void btnTrocaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTrocaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnTrocaActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnDesc.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnCancelar.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnTrocaKeyPressed

    private void btnOutrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutrosActionPerformed
        AsyncCallback<EcfPagamento> async = new AsyncCallback<EcfPagamento>() {
            @Override
            public void sucesso(EcfPagamento resultado) {
                if (resultado != null) {
                    EcfPagamentoTipo tipo = resultado.getEcfPagamentoTipo();
                    dtmPag.addRow(new Object[]{tipo.getEcfPagamentoTipoCodigo(), "POS", tipo.isEcfPagamentoTipoDebito(), resultado.getEcfPagamentoValor(), true,
                                resultado.getEcfPagamentoData(), resultado.getEcfPagamentoNsu(), resultado});
                    atualizar();
                }
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(fechamento, excecao.getMessage(), "Pagamento", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        Pagamento pagamento = Pagamento.getInstancia(async, apagar.subtract(pago).doubleValue());
        pagamento.setVisible(true);
    }//GEN-LAST:event_btnOutrosActionPerformed

    private void btnOutrosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOutrosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnOutrosActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnCheque.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnAcres.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnOutrosKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAcres;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCartao;
    private javax.swing.JButton btnCheque;
    private javax.swing.JButton btnDesc;
    private javax.swing.JButton btnDinheiro;
    private javax.swing.JButton btnOutros;
    private javax.swing.JButton btnTroca;
    private javax.swing.JCheckBox chkPresente;
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
     * Metodo que trata os eventos do teclado para acionar via atalho.
     *
     * @param e a tecla precionada.
     */
    private void atalhos(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D && btnDinheiro.isEnabled()) { // Dinheiro
            btnDinheiroActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_C && btnCartao.isEnabled()) { // Cartao
            btnCartaoActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_H && btnCheque.isEnabled()) { // Cheque
            btnChequeActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_O && btnOutros.isEnabled()) { // Outros
            btnOutrosActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_A && btnAcres.isEnabled()) { // Acrescimo
            btnAcresActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_E && btnDesc.isEnabled()) { // Desconto
            btnDescActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_T && btnTroca.isEnabled()) { // Desconto
            btnTrocaActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && btnCancelar.isEnabled()) { // Cancelar
            cancelar();
        }
    }

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

        if (tefs > 0 && valor.compareTo(apagar.subtract(pago)) > 0) {
            JOptionPane.showMessageDialog(this, "Valor informado deve ser menor ou igual ao total a pagar!", "DINHEIRO", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (valor.compareTo(new BigDecimal(0.00)) > 0) {
                if (pos > -1) {
                    dtmPag.setValueAt(valor.doubleValue(), pos, 3);
                } else {
                    dtmPag.addRow(new Object[]{Util.getConfig().get("ecf.dinheiro"), "DINHEIRO", false, valor.doubleValue(), true, new Date(), "", ""});
                }
            } else if (pos > -1) {
                dtmPag.removeRow(pos);
            }
        }
        atualizar();
    }

    /**
     * Metodo que trata do pagamento com cartao.
     *
     * @param valor o valor informado do pagamento.
     */
    private void cartao(final BigDecimal valor) {
        if ((limite == 1 && valor.compareTo(apagar.subtract(pago)) == 0) || (limite > 1 && valor.doubleValue() > 0.00 && valor.compareTo(apagar.subtract(pago)) <= 0)) {
            if (tefs < limite) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // chama o metodo do tef para operacao com cartao
                        String coo = Util.formataNumero(Caixa.getInstancia().getVenda().getEcfVendaCoo(), 6, 0, false);
                        try {
                            String id = TEF.gerarId();
                            TEF.realizarTransacao(id, coo, valor.doubleValue());
                            TEF.focar(Caixa.getInstancia().getTitle());

                            BigDecimal total = new BigDecimal(Double.valueOf(TEF.getDados().get("003-000")) / 100).setScale(2, RoundingMode.HALF_UP);
                            String rede = TEF.getDados().get("010-000");
                            int trans = Integer.valueOf(TEF.getDados().get("011-000"));
                            Date data = new SimpleDateFormat("ddMMyyyyHHmmss").parse(TEF.getDados().get("022-000") + TEF.getDados().get("023-000"));
                            String nsu = TEF.getDados().get("012-000");
                            String arquivo = TEF.getPathTmp().getAbsolutePath() + System.getProperty("file.separator") + "pendente" + id + ".txt";
                            String msg = TEF.getDados().get("030-000");
                            boolean confirmado = false;

                            // caso o valor pago no cartao seja maior que o informado
                            if (total.subtract(valor).doubleValue() > 0.00) {
                                acresdesc(0, "ACRESCIMO", total.subtract(valor).toString().replace(".", ","));
                            } else if (total.subtract(valor).doubleValue() < 0.00) {
                                acresdesc(99, "DESCONTO", valor.subtract(total).toString().replace(".", ","));
                            }

                            // caso o pagamento seja menor que o total restante, podendo ter outro cartao
                            if (total.compareTo(apagar.subtract(pago)) < 0) {
                                // confirma e gera o backup
                                FileUtils.copy(arquivo, arquivo.replace("pendente", "backup"));
                                arquivo = arquivo.replace("pendente", "backup");
                                TEF.confirmarTransacao(id, true);
                                lblTEF.setText("TEF [" + msg + "]");
                                confirmado = true;
                                Aguarde.getInstancia().setVisible(false);
                            } else {
                                Aguarde.getInstancia().getLblMensagem().setText(msg);
                            }
                            dtmPag.addRow(new Object[]{"00", rede, (trans >= 20 && trans <= 25), total.doubleValue(), confirmado, data, nsu, arquivo});
                            tefs++;

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
                JOptionPane.showMessageDialog(this, "Máximo de " + limite + " pagamentos com TEF atingido!", "TEF", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            String restante = Util.formataNumero(apagar.subtract(pago).doubleValue(), 1, 2, false);
            JOptionPane.showMessageDialog(this, "Valor informado deve ser maior que 0,00 e igual à " + restante, "CARTÃO", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo que trata do pagamento com cheque.
     *
     * @param valor o valor informado do pagamento.
     */
    private void cheque(final BigDecimal valor) {
        if ((limite == 1 && valor.compareTo(apagar.subtract(pago)) == 0) || (limite > 1 && valor.doubleValue() > 0.00 && valor.compareTo(apagar.subtract(pago)) <= 0)) {
            for (EcfPagamentoTipo tipo : tipos) {
                if (tipo.getEcfPagamentoTipoCodigo().equals(Util.getConfig().get("ecf.cheque"))) {
                    if (tipo.isEcfPagamentoTipoTef()) {
                        if (tefs < limite) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // chama o metodo do tef para operacao com cheque
                                    try {
                                        String id = TEF.gerarId();
                                        TEF.consultarCheque(id, valor.doubleValue());

                                        Date data = new SimpleDateFormat("ddMMyyyyHHmmss").parse(TEF.getDados().get("022-000") + TEF.getDados().get("023-000"));
                                        String nsu = TEF.getDados().get("012-000");
                                        String arquivo = TEF.getPathTmp().getAbsolutePath() + System.getProperty("file.separator") + "pendente" + id + ".txt";
                                        boolean confirmado = false;

                                        // caso o pagamento seja menor que o total restante, podendo ter outro cartao
                                        if (valor.compareTo(apagar.subtract(pago)) < 0) {
                                            // confirma e gera o backup
                                            FileUtils.copy(arquivo, arquivo.replace("pendente", "backup"));
                                            arquivo = arquivo.replace("pendente", "backup");
                                            String msg = TEF.getDados().get("030-000");
                                            TEF.confirmarTransacao(id, true);
                                            lblTEF.setText("TEF [" + msg + "]");
                                            confirmado = true;
                                        }
                                        dtmPag.addRow(new Object[]{Util.getConfig().get("ecf.cheque"), "CHEQUE", false, valor.doubleValue(), confirmado, data, nsu, arquivo});

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
                            JOptionPane.showMessageDialog(this, "Máximo de " + limite + " pagamentos com TEF atingido!", "TEF", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        dtmPag.addRow(new Object[]{Util.getConfig().get("ecf.cheque"), "CHEQUE", false, valor.doubleValue(), true, new Date(), "", ""});
                        atualizar();
                    }
                    break;
                }
            }
        } else {
            String restante = Util.formataNumero(apagar.subtract(pago).doubleValue(), 1, 2, false);
            JOptionPane.showMessageDialog(this, "Valor informado deve ser maior que 0,00 e igual à " + restante, "CHEQUE", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo que trata do acrescimo e desconto da venda.
     *
     * @param max o maximo em porcentagem que pode oferecer.
     * @param nome o nome de distingue o ACRESCIMO do DESCONTO.
     * @param texto o valor digitado pelo operador.
     */
    private void acresdesc(int max, String nome, String texto) {
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

                if (valor.doubleValue() > 0.00 && (valor.compareTo(total) < 0 || nome.equals("ACRESCIMO"))) {
                    BigDecimal maximo = total.multiply(new BigDecimal(max)).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
                    if (maximo.compareTo(valor) >= 0 || nome.equals("ACRESCIMO")) {
                        if (pos > -1) {
                            dtmPag.setValueAt(valor.doubleValue(), pos, 3);
                        } else {
                            dtmPag.addRow(new Object[]{"", nome.toUpperCase(), false, valor.doubleValue(), false, null, "", ""});
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
     * Metodo que seleciona um troca para a venda.
     */
    private void trocar() {
        AsyncCallback<EcfTroca> async = new AsyncCallback<EcfTroca>() {
            @Override
            public void sucesso(EcfTroca resultado) {
                Caixa.getInstancia().getVenda().setEcfTroca(resultado);
                BigDecimal valor = new BigDecimal(resultado.getEcfTrocaValor()).setScale(2, RoundingMode.HALF_UP);
                troca(valor);
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(fechamento, "Selecione uma troca na listagem!", "TROCA", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        Trocas trocas = Trocas.getInstancia(async);
        trocas.setVisible(true);
    }

    /**
     * Metodo que trata do pagamento com troca.
     *
     * @param valor a troca informada do pagamento.
     */
    private void troca(BigDecimal valor) {
        int pos = -1;
        for (int i = 0; i < dtmPag.getRowCount(); i++) {
            String tipo = dtmPag.getValueAt(i, 1).toString();
            if (tipo.equals("TROCA")) {
                pos = i;
                break;
            }
        }

        if (tefs > 0 && valor.compareTo(apagar.subtract(pago)) > 0) {
            Caixa.getInstancia().getVenda().setEcfTroca(null);
            JOptionPane.showMessageDialog(this, "Valor informado deve ser menor ou igual ao total a pagar!", "TROCA", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (valor.compareTo(new BigDecimal(0.00)) > 0) {
                if (pos > -1) {
                    dtmPag.setValueAt(valor.doubleValue(), pos, 3);
                } else {
                    dtmPag.addRow(new Object[]{Util.getConfig().get("ecf.troca"), "TROCA", false, valor.doubleValue(), true, new Date(), "", ""});
                }
            } else if (pos > -1) {
                dtmPag.removeRow(pos);
            }
        }
        atualizar();
    }

    /**
     * Metodo que limpa a tela de fechamento.
     */
    private void limpar() {
        // botoes
        btnDinheiro.setEnabled(true);
        if (Util.getConfig().get("tef.titulo") != null) {
            btnCartao.setEnabled(true);
            btnCheque.setEnabled(true);
        } else {
            btnCartao.setEnabled(false);
            btnCheque.setEnabled(false);
        }
        btnOutros.setEnabled(true);
        btnAcres.setEnabled(true);
        btnDesc.setEnabled(true);
        btnTroca.setEnabled(true);
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
            BigDecimal valor = new BigDecimal(dtmPag.getValueAt(i, 3).toString()).setScale(2, RoundingMode.HALF_UP);
            switch (tipo) {
                case "ACRESCIMO":
                    acres = valor;
                    break;
                case "DESCONTO":
                    desc = valor;
                    break;
                case "DINHEIRO":
                    pago = pago.add(valor);
                    pagDinheiro = true;
                    break;
                case "TROCA":
                    pago = pago.add(valor);
                    break;
                default:
                    pago = pago.add(valor);
                    pagCartao = true;
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
        btnCheque.setEnabled(false);
        btnOutros.setEnabled(false);
        btnAcres.setEnabled(false);
        btnDesc.setEnabled(false);
        btnTroca.setEnabled(false);
        btnCancelar.setEnabled(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (pagDinheiro) {
                        ECF.enviar(EComandoECF.ECF_AbreGaveta);
                    }
                    new ComandoFecharVenda(getPagamentos(), total.doubleValue(), acres.subtract(desc).doubleValue(), troco.doubleValue(), obs).executar();
                    // se precisar imprimir o cupom presente
                    if (chkPresente.isSelected()) {
                        ComandoCupomPresente ccp = new ComandoCupomPresente(Caixa.getInstancia().getVenda());
                        ccp.executar();
                    }
                } catch (OpenPdvException ex) {
                    log.error("Erro ao fechar a venda.", ex);
                    cancelar();
                } finally {
                    Aguarde.getInstancia().setVisible(false);
                    dispose();
                }
            }
        }).start();
        Aguarde.getInstancia().setVisible(true);
    }

    /**
     * Metodo que cancela o pagamento.
     */
    private void cancelar() {
        if (pagCartao) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ComandoCancelarVenda(true).executar();
                    } catch (OpenPdvException ex) {
                        log.error("Erro ao cancelar A VENDA.", ex);
                        JOptionPane.showMessageDialog(null, "Erro ao cancelar a venda.", "Fechar Venda", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        Aguarde.getInstancia().setVisible(false);
                    }
                }
            }).start();
            Aguarde.getInstancia().setVisible(true);
        } else {
            Caixa caixa = Caixa.getInstancia();
            caixa.setModo(modo);
            caixa.statusMenus(modo);
            caixa.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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
        // reserva o primeiro pagamento para dinheiro
        pagamentos.add(null);
        // reserva o segunda pagamento para troca
        pagamentos.add(null);

        for (int i = 0; i < dtmPag.getRowCount(); i++) {
            String codigo = dtmPag.getValueAt(i, 0).toString();
            String rede = dtmPag.getValueAt(i, 1).toString();
            Boolean debito = (Boolean) dtmPag.getValueAt(i, 2);
            Double valor = (Double) dtmPag.getValueAt(i, 3);
            Date data = (Date) dtmPag.getValueAt(i, 5);
            String nsu = dtmPag.getValueAt(i, 6).toString();
            String arquivo = dtmPag.getValueAt(i, 7).toString();

            // se a rede for diferente de POS faz o processo normal
            if (!rede.equalsIgnoreCase("POS")) {
                for (EcfPagamentoTipo tipo : tipos) {
                    if (tipo.getEcfPagamentoTipoCodigo().equals(codigo)
                            || (tipo.getEcfPagamentoTipoRede().equalsIgnoreCase(rede)
                            && tipo.isEcfPagamentoTipoDebito() == debito
                            && tipo.getEcfPagamentoTipoCodigo().equals(Util.getConfig().get("ecf.cartao")))) {
                        EcfPagamento pag = new EcfPagamento();
                        pag.setEcfVenda(Caixa.getInstancia().getVenda());
                        pag.setEcfPagamentoTipo(tipo);
                        pag.setEcfPagamentoGnf(0);
                        pag.setEcfPagamentoData(data);
                        pag.setEcfPagamentoValor(valor);
                        pag.setEcfPagamentoNsu(nsu);
                        pag.setEcfPagamentoEstorno('N');
                        pag.setArquivo(arquivo);
                        if (codigo.equals(Util.getConfig().get("ecf.dinheiro"))) {
                            // se dinheiro coloca na primeira posicao
                            pagamentos.set(0, pag);
                        } else if (codigo.equals(Util.getConfig().get("ecf.troca"))) {
                            // se troca coloca na segunda posicao
                            pagamentos.set(1, pag);
                        } else {
                            // se cartao ou cheque adiciona e deixa na ordem
                            pagamentos.add(pag);
                        }
                        break;
                    }
                }
            } else {
                EcfPagamento pag = (EcfPagamento) dtmPag.getValueAt(i, 7);
                pagamentos.add(pag);
            }
        }

        // verifica se teve troca, caso contrario remove a posicao 1
        if (pagamentos.get(1) == null) {
            pagamentos.remove(1);
        }

        // verifica se teve dinheiro, caso contrario remove a posicao 0
        if (pagamentos.get(0) == null) {
            pagamentos.remove(0);
        }
        return pagamentos;
    }

    public BigDecimal getAcres() {
        return acres;
    }

    public void setAcres(BigDecimal acres) {
        this.acres = acres;
    }

    public BigDecimal getApagar() {
        return apagar;
    }

    public void setApagar(BigDecimal apagar) {
        this.apagar = apagar;
    }

    public JButton getBtnAcres() {
        return btnAcres;
    }

    public void setBtnAcres(JButton btnAcres) {
        this.btnAcres = btnAcres;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

    public JButton getBtnCartao() {
        return btnCartao;
    }

    public void setBtnCartao(JButton btnCartao) {
        this.btnCartao = btnCartao;
    }

    public JButton getBtnCheque() {
        return btnCheque;
    }

    public void setBtnCheque(JButton btnCheque) {
        this.btnCheque = btnCheque;
    }

    public JButton getBtnDesc() {
        return btnDesc;
    }

    public void setBtnDesc(JButton btnDesc) {
        this.btnDesc = btnDesc;
    }

    public JButton getBtnDinheiro() {
        return btnDinheiro;
    }

    public void setBtnDinheiro(JButton btnDinheiro) {
        this.btnDinheiro = btnDinheiro;
    }

    public BigDecimal getDesc() {
        return desc;
    }

    public void setDesc(BigDecimal desc) {
        this.desc = desc;
    }

    public DefaultTableModel getDtmPag() {
        return dtmPag;
    }

    public void setDtmPag(DefaultTableModel dtmPag) {
        this.dtmPag = dtmPag;
    }

    public JLabel getLblPagar() {
        return lblPagar;
    }

    public void setLblPagar(JLabel lblPagar) {
        this.lblPagar = lblPagar;
    }

    public JLabel getLblPagarValor() {
        return lblPagarValor;
    }

    public void setLblPagarValor(JLabel lblPagarValor) {
        this.lblPagarValor = lblPagarValor;
    }

    public JLabel getLblPago() {
        return lblPago;
    }

    public void setLblPago(JLabel lblPago) {
        this.lblPago = lblPago;
    }

    public JLabel getLblPagoValor() {
        return lblPagoValor;
    }

    public void setLblPagoValor(JLabel lblPagoValor) {
        this.lblPagoValor = lblPagoValor;
    }

    public JLabel getLblTEF() {
        return lblTEF;
    }

    public void setLblTEF(JLabel lblTEF) {
        this.lblTEF = lblTEF;
    }

    public JLabel getLblTroco() {
        return lblTroco;
    }

    public void setLblTroco(JLabel lblTroco) {
        this.lblTroco = lblTroco;
    }

    public JLabel getLblTrocoValor() {
        return lblTrocoValor;
    }

    public void setLblTrocoValor(JLabel lblTrocoValor) {
        this.lblTrocoValor = lblTrocoValor;
    }

    public boolean isPagCartao() {
        return pagCartao;
    }

    public void setPagCartao(boolean pagCartao) {
        this.pagCartao = pagCartao;
    }

    public boolean isPagDinheiro() {
        return pagDinheiro;
    }

    public void setPagDinheiro(boolean pagDinheiro) {
        this.pagDinheiro = pagDinheiro;
    }

    public BigDecimal getPago() {
        return pago;
    }

    public void setPago(BigDecimal pago) {
        this.pago = pago;
    }

    public JSeparator getSeparador() {
        return separador;
    }

    public void setSeparador(JSeparator separador) {
        this.separador = separador;
    }

    public JSeparator getSeparador1() {
        return separador1;
    }

    public void setSeparador1(JSeparator separador1) {
        this.separador1 = separador1;
    }

    public JScrollPane getSpPagamentos() {
        return spPagamentos;
    }

    public void setSpPagamentos(JScrollPane spPagamentos) {
        this.spPagamentos = spPagamentos;
    }

    public JTable getTabPagamentos() {
        return tabPagamentos;
    }

    public void setTabPagamentos(JTable tabPagamentos) {
        this.tabPagamentos = tabPagamentos;
    }

    public int getTefs() {
        return tefs;
    }

    public void setTefs(int tefs) {
        this.tefs = tefs;
    }

    public List<EcfPagamentoTipo> getTipos() {
        return tipos;
    }

    public void setTipos(List<EcfPagamentoTipo> tipos) {
        this.tipos = tipos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public void setTroco(BigDecimal troco) {
        this.troco = troco;
    }

    public int getLimite() {
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }

    public EModo getModo() {
        return modo;
    }

    public void setModo(EModo modo) {
        this.modo = modo;
    }

    public JButton getBtnTroca() {
        return btnTroca;
    }

    public void setBtnTroca(JButton btnTroca) {
        this.btnTroca = btnTroca;
    }

    public JButton getBtnOutros() {
        return btnOutros;
    }

    public void setBtnOutros(JButton btnOutros) {
        this.btnOutros = btnOutros;
    }

    public JCheckBox getChkPresente() {
        return chkPresente;
    }

    public void setChkPresente(JCheckBox chkPresente) {
        this.chkPresente = chkPresente;
    }
}