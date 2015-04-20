package br.com.openpdv.visao.venda;

import br.com.openpdv.controlador.comandos.ComandoCartaoPresente;
import br.com.openpdv.controlador.comandos.ComandoCancelarVenda;
import br.com.openpdv.controlador.comandos.ComandoFecharVenda;
import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.TableCellRendererNumber;
import br.com.openpdv.controlador.permissao.Login;
import br.com.phdss.Util;
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
import br.com.phdss.EComando;
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
    private BigDecimal falta;
    private BigDecimal troco;
    private boolean pagDinheiro;
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
        limite = Util.getConfig().getProperty("tef.cartoes") != null ? Integer.valueOf(Util.getConfig().getProperty("tef.cartoes")) : 1;
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

        CoreService<EcfPagamentoTipo> service = new CoreService<>();
        try {
            fechamento.tipos = service.selecionar(new EcfPagamentoTipo(), 0, 0, null);
        } catch (OpenPdvException ex) {
            fechamento.log.error("Erro ao selecionar os tipo de pagamentos.", ex);
            JOptionPane.showMessageDialog(fechamento, "Problemas ao carregar as formas de pagamentos.", "Fechar Venda", JOptionPane.ERROR_MESSAGE);
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
        btnTef = new javax.swing.JButton();
        btnPos = new javax.swing.JButton();
        btnPresente = new javax.swing.JButton();
        btnCheque = new javax.swing.JButton();
        btnTroca = new javax.swing.JButton();
        btnDesc = new javax.swing.JButton();
        btnAcres = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        spPagamentos = new javax.swing.JScrollPane();
        tabPagamentos = new javax.swing.JTable();
        lblPagar = new javax.swing.JLabel();
        lblPagarValor = new javax.swing.JLabel();
        lblPago = new javax.swing.JLabel();
        lblPagoValor = new javax.swing.JLabel();
        separador = new javax.swing.JSeparator();
        lblFalta = new javax.swing.JLabel();
        lblFaltaValor = new javax.swing.JLabel();
        lblTroco = new javax.swing.JLabel();
        lblTrocoValor = new javax.swing.JLabel();
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

        btnTef.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnTef.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cartao.png"))); // NOI18N
        btnTef.setText("<html><center><b>C</b>ARTÃO</center></html>");
        btnTef.setToolTipText("Pagamento com cartão, escolha o tipo no menu ao lado. Atalho = C");
        btnTef.setMaximumSize(new java.awt.Dimension(100, 75));
        btnTef.setMinimumSize(new java.awt.Dimension(100, 75));
        btnTef.setPreferredSize(new java.awt.Dimension(100, 75));
        btnTef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTefActionPerformed(evt);
            }
        });
        btnTef.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnTefKeyPressed(evt);
            }
        });

        btnPos.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnPos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/informacao.png"))); // NOI18N
        btnPos.setText("<html><center><b>P</b>OS</center></html>");
        btnPos.setToolTipText("Pagamento com cartão através da maquineta. Atalho = P");
        btnPos.setMaximumSize(new java.awt.Dimension(100, 75));
        btnPos.setMinimumSize(new java.awt.Dimension(100, 75));
        btnPos.setPreferredSize(new java.awt.Dimension(100, 75));
        btnPos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPosActionPerformed(evt);
            }
        });
        btnPos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPosKeyPressed(evt);
            }
        });

        btnPresente.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnPresente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/estoque.png"))); // NOI18N
        btnPresente.setText("<html><center>CA<b>R</b>TÃO PRESENTE</center></html>");
        btnPresente.setToolTipText("Pagamento com cartão presente da empresa. Atalho = R");
        btnPresente.setMaximumSize(new java.awt.Dimension(100, 75));
        btnPresente.setMinimumSize(new java.awt.Dimension(100, 75));
        btnPresente.setPreferredSize(new java.awt.Dimension(100, 75));
        btnPresente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPresenteActionPerformed(evt);
            }
        });
        btnPresente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPresenteKeyPressed(evt);
            }
        });

        btnCheque.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnCheque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cheque.png"))); // NOI18N
        btnCheque.setText("<html><center>C<b>H</b>EQUE</center></html>");
        btnCheque.setToolTipText("Pagamento com cheque. Atalho = H");
        btnCheque.setMaximumSize(new java.awt.Dimension(100, 75));
        btnCheque.setMinimumSize(new java.awt.Dimension(100, 75));
        btnCheque.setPreferredSize(new java.awt.Dimension(100, 75));
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

        btnTroca.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnTroca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/sincroniza.png"))); // NOI18N
        btnTroca.setText("<html><center><b>T</b>ROCA</center></html>");
        btnTroca.setToolTipText("Adicionar valor de troca. Atalho = T");
        btnTroca.setMaximumSize(new java.awt.Dimension(100, 40));
        btnTroca.setMinimumSize(new java.awt.Dimension(100, 40));
        btnTroca.setPreferredSize(new java.awt.Dimension(100, 40));
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

        btnDesc.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnDesc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/receber.png"))); // NOI18N
        btnDesc.setText("<html><center>D<b>E</b>SCONTO</center></html>");
        btnDesc.setToolTipText("Adicionar desconto. Atalho = E");
        btnDesc.setMaximumSize(new java.awt.Dimension(100, 40));
        btnDesc.setMinimumSize(new java.awt.Dimension(100, 40));
        btnDesc.setPreferredSize(new java.awt.Dimension(100, 40));
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

        btnAcres.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnAcres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/valor.png"))); // NOI18N
        btnAcres.setText("<html><center><b>A</b>CRÉSCIMO</center></html>");
        btnAcres.setToolTipText("Adicionar acréscimo. Atalho = A");
        btnAcres.setMaximumSize(new java.awt.Dimension(100, 40));
        btnAcres.setMinimumSize(new java.awt.Dimension(100, 40));
        btnAcres.setPreferredSize(new java.awt.Dimension(100, 40));
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

        btnRemover.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/excluir.png"))); // NOI18N
        btnRemover.setToolTipText("Remover o item de pagamento. Atalho = R");
        btnRemover.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemover.setMaximumSize(new java.awt.Dimension(100, 40));
        btnRemover.setMinimumSize(new java.awt.Dimension(100, 40));
        btnRemover.setPreferredSize(new java.awt.Dimension(100, 40));
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });
        btnRemover.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnRemoverKeyPressed(evt);
            }
        });

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
        tabPagamentos.setFocusable(false);
        tabPagamentos.setRequestFocusEnabled(false);
        tabPagamentos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabPagamentos.getTableHeader().setReorderingAllowed(false);
        tabPagamentos.setVerifyInputWhenFocusTarget(false);
        spPagamentos.setViewportView(tabPagamentos);
        if (tabPagamentos.getColumnModel().getColumnCount() > 0) {
            tabPagamentos.getColumnModel().getColumn(0).setMinWidth(0);
            tabPagamentos.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabPagamentos.getColumnModel().getColumn(0).setMaxWidth(0);
            tabPagamentos.getColumnModel().getColumn(1).setResizable(false);
            tabPagamentos.getColumnModel().getColumn(1).setPreferredWidth(95);
            tabPagamentos.getColumnModel().getColumn(2).setMinWidth(0);
            tabPagamentos.getColumnModel().getColumn(2).setPreferredWidth(0);
            tabPagamentos.getColumnModel().getColumn(2).setMaxWidth(0);
            tabPagamentos.getColumnModel().getColumn(3).setResizable(false);
            tabPagamentos.getColumnModel().getColumn(3).setPreferredWidth(95);
            tabPagamentos.getColumnModel().getColumn(3).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
            tabPagamentos.getColumnModel().getColumn(4).setResizable(false);
            tabPagamentos.getColumnModel().getColumn(4).setPreferredWidth(95);
            tabPagamentos.getColumnModel().getColumn(5).setMinWidth(0);
            tabPagamentos.getColumnModel().getColumn(5).setPreferredWidth(0);
            tabPagamentos.getColumnModel().getColumn(5).setMaxWidth(0);
            tabPagamentos.getColumnModel().getColumn(6).setMinWidth(0);
            tabPagamentos.getColumnModel().getColumn(6).setPreferredWidth(0);
            tabPagamentos.getColumnModel().getColumn(6).setMaxWidth(0);
            tabPagamentos.getColumnModel().getColumn(7).setMinWidth(0);
            tabPagamentos.getColumnModel().getColumn(7).setPreferredWidth(0);
            tabPagamentos.getColumnModel().getColumn(7).setMaxWidth(0);
        }

        lblPagar.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblPagar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPagar.setText("PAGAR");
        lblPagar.setFocusable(false);
        lblPagar.setRequestFocusEnabled(false);
        lblPagar.setVerifyInputWhenFocusTarget(false);

        lblPagarValor.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblPagarValor.setForeground(new java.awt.Color(255, 0, 0));
        lblPagarValor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPagarValor.setText("R$ 0,00");
        lblPagarValor.setFocusable(false);
        lblPagarValor.setRequestFocusEnabled(false);
        lblPagarValor.setVerifyInputWhenFocusTarget(false);

        lblPago.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblPago.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPago.setText("PAGO");
        lblPago.setFocusable(false);
        lblPago.setRequestFocusEnabled(false);
        lblPago.setVerifyInputWhenFocusTarget(false);

        lblPagoValor.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblPagoValor.setForeground(new java.awt.Color(0, 255, 0));
        lblPagoValor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPagoValor.setText("R$ 0,00");
        lblPagoValor.setFocusable(false);
        lblPagoValor.setRequestFocusEnabled(false);
        lblPagoValor.setVerifyInputWhenFocusTarget(false);

        separador.setRequestFocusEnabled(false);
        separador.setVerifyInputWhenFocusTarget(false);

        lblFalta.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblFalta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblFalta.setText("FALTA");
        lblFalta.setFocusable(false);
        lblFalta.setRequestFocusEnabled(false);
        lblFalta.setVerifyInputWhenFocusTarget(false);

        lblFaltaValor.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblFaltaValor.setForeground(new java.awt.Color(255, 153, 0));
        lblFaltaValor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFaltaValor.setText("R$ 0,00");
        lblFaltaValor.setFocusable(false);
        lblFaltaValor.setVerifyInputWhenFocusTarget(false);

        lblTroco.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblTroco.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTroco.setText("TROCO");
        lblTroco.setFocusable(false);
        lblTroco.setRequestFocusEnabled(false);
        lblTroco.setVerifyInputWhenFocusTarget(false);

        lblTrocoValor.setFont(new java.awt.Font("Serif", 1, 36)); // NOI18N
        lblTrocoValor.setForeground(new java.awt.Color(0, 0, 255));
        lblTrocoValor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTrocoValor.setText("R$ 0,00");
        lblTrocoValor.setFocusable(false);
        lblTrocoValor.setVerifyInputWhenFocusTarget(false);

        separador1.setRequestFocusEnabled(false);
        separador1.setVerifyInputWhenFocusTarget(false);

        lblTEF.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblTEF.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTEF.setFocusable(false);
        lblTEF.setRequestFocusEnabled(false);
        lblTEF.setVerifyInputWhenFocusTarget(false);

        btnCancelar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cancelar.png"))); // NOI18N
        btnCancelar.setText("CANCELAR [ESC]");
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
                .add(6, 6, 6)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(spPagamentos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 312, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btnDinheiro, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnPos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnCheque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnDesc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btnTef, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnPresente, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnTroca, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnAcres, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(0, 0, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblTEF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblFalta, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(lblTroco, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblPagar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(lblPago, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .add(2, 2, 2)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblPagarValor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblPagoValor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblFaltaValor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblTrocoValor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .add(separador)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(separador1))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {btnAcres, btnCheque, btnDesc, btnDinheiro, btnPos, btnPresente, btnTef, btnTroca}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(lblPagar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblPagarValor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(lblPago, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblPagoValor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btnTef, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnDinheiro, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btnPos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnPresente, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(btnCheque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(btnTroca, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(btnDesc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(btnAcres, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(spPagamentos, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                                    .add(btnRemover, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .add(layout.createSequentialGroup()
                                .add(28, 28, 28)
                                .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(lblFalta, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(lblFaltaValor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(18, 18, 18)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(lblTroco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(lblTrocoValor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(separador1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblTEF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {btnAcres, btnCheque, btnDesc, btnDinheiro, btnPos, btnPresente, btnTef, btnTroca}, org.jdesktop.layout.GroupLayout.VERTICAL);

        setSize(new java.awt.Dimension(731, 351));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDinheiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDinheiroActionPerformed
        Object obj = JOptionPane.showInputDialog(this, "Digite o valor do pagamento.", "DINHEIRO",
                JOptionPane.OK_CANCEL_OPTION, btnDinheiro.getIcon(), null, Util.formataNumero(falta.doubleValue(), 1, 2, false));

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
            btnTef.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnDinheiroKeyPressed

    private void btnTefKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTefKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnTefActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnDinheiro.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnPos.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnTefKeyPressed

    private void btnAcresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcresActionPerformed
        final Gerente gerente = Gerente.getInstancia(null);
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                Caixa.getInstancia().getVenda().setSisGerente(gerente.getSisGerente());
                String texto = JOptionPane.showInputDialog(fechamento, "Digite o valor.\nPara porcentagem coloque % no final.", "ACRESCIMO", JOptionPane.OK_CANCEL_OPTION);
                if (acresdesc(resultado, "ACRESCIMO", texto) == false) {
                    gerente.setAsync(this);
                    gerente.setVisible(true);
                }
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(fechamento, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            gerente.setSisGerente(Login.getOperador());
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            gerente.setAsync(async);
            gerente.setVisible(true);
        }
    }//GEN-LAST:event_btnAcresActionPerformed

    private void btnAcresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAcresKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnAcresActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnDesc.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnRemover.requestFocus();
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
                if (acresdesc(resultado, "DESCONTO", texto) == false) {
                    gerente.setAsync(this);
                    gerente.setVisible(true);
                }
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(fechamento, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            gerente.setSisGerente(Login.getOperador());
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            gerente.setAsync(async);
            gerente.setVisible(true);
        }
    }//GEN-LAST:event_btnDescActionPerformed

    private void btnDescKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDescKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnDescActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnTroca.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnAcres.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnDescKeyPressed

    private void btnChequeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChequeActionPerformed
        AsyncCallback<EcfPagamento> async = new AsyncCallback<EcfPagamento>() {
            @Override
            public void sucesso(EcfPagamento resultado) {
                if (resultado != null) {
                    dtmPag.addRow(new Object[]{Util.getConfig().getProperty("ecf.cheque"), "CHEQUE", false, resultado.getEcfPagamentoValor(), true, resultado.getEcfPagamentoData(), resultado.getEcfPagamentoNsu(), ""});
                    atualizar();
                }
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(fechamento, excecao.getMessage(), "CHEQUE", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        Cheque cheque = Cheque.getInstancia(async, falta.doubleValue());
        cheque.setVisible(true);
    }//GEN-LAST:event_btnChequeActionPerformed

    private void btnChequeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnChequeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnChequeActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnPresente.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnTroca.requestFocus();
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

        if (Login.getOperador().isSisUsuarioGerente()) {
            gerente.setSisGerente(Login.getOperador());
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            gerente.setAsync(async);
            gerente.setVisible(true);
        }
    }//GEN-LAST:event_btnTrocaActionPerformed

    private void btnTrocaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTrocaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnTrocaActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnCheque.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnDesc.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnTrocaKeyPressed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        int pos = tabPagamentos.getSelectedRow();
        if (pos > -1) {
            String codigo = dtmPag.getValueAt(pos, 0).toString();
            if (!codigo.equals("00")) {
                int resp = JOptionPane.showConfirmDialog(fechamento, "Deseja remover este item de pagamento?", "PAGAMENTO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    if (codigo.equals(Util.getConfig().getProperty("ecf.presente"))) {
                        String nsu = dtmPag.getValueAt(pos, 6).toString();
                        try {
                            new ComandoCartaoPresente("ativarCartao", nsu, Caixa.getInstancia().getVenda().getSisVendedor()).executar();
                            dtmPag.removeRow(pos);
                            atualizar();
                        } catch (OpenPdvException ex) {
                            JOptionPane.showMessageDialog(fechamento, ex.getMessage(), "PAGAMENTO", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        dtmPag.removeRow(pos);
                        atualizar();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(fechamento, "Cartão não pode ser removido, pois precisa ser estornado.", "PAGAMENTO", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(fechamento, "Selecione um item de pagamento para remover.", "PAGAMENTO", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnRemoverKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnRemoverKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnRemoverActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnAcres.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnCancelar.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnRemoverKeyPressed

    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cancelar();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnRemover.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnDinheiro.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnCancelarKeyPressed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnTefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTefActionPerformed
        Object obj = JOptionPane.showInputDialog(this, "Digite o valor do pagamento.", "TEF",
                JOptionPane.OK_CANCEL_OPTION, btnTef.getIcon(), null, Util.formataNumero(falta.doubleValue(), 1, 2, false));

        if (obj != null) {
            try {
                String texto = obj.toString().replace(".", "").replace(",", ".");
                BigDecimal valor = new BigDecimal(texto).setScale(2, RoundingMode.HALF_UP);
                tef(valor);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Valor informado inválido!", "TEF", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnTefActionPerformed

    private void btnPosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPosActionPerformed
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
                JOptionPane.showMessageDialog(fechamento, excecao.getMessage(), "POS", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        Pos pos = Pos.getInstancia(async, falta.doubleValue());
        pos.setVisible(true);
    }//GEN-LAST:event_btnPosActionPerformed

    private void btnPosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnPosActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnTef.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnPresente.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnPosKeyPressed

    private void btnPresenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPresenteActionPerformed
        Object obj = JOptionPane.showInputDialog(this, "Digite o número do cartão presente.", "CARTÃO PRESENTE",
                JOptionPane.OK_CANCEL_OPTION, btnPresente.getIcon(), null, null);

        if (obj != null) {
            try {
                ComandoCartaoPresente comando = new ComandoCartaoPresente("desativarCartao", obj.toString(), Caixa.getInstancia().getVenda().getSisVendedor());
                comando.executar();
                presente(comando.getValor(), obj.toString());
            } catch (OpenPdvException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "CARTÃO PRESENTE", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnPresenteActionPerformed

    private void btnPresenteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPresenteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnPresenteActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            btnPos.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            btnCheque.requestFocus();
        } else {
            atalhos(evt);
        }
    }//GEN-LAST:event_btnPresenteKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAcres;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCheque;
    private javax.swing.JButton btnDesc;
    private javax.swing.JButton btnDinheiro;
    private javax.swing.JButton btnPos;
    private javax.swing.JButton btnPresente;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnTef;
    private javax.swing.JButton btnTroca;
    private javax.swing.JLabel lblFalta;
    private javax.swing.JLabel lblFaltaValor;
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
        } else if (e.getKeyCode() == KeyEvent.VK_C & btnTef.isEnabled()) { // TEF
            btnTefActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_P & btnPos.isEnabled()) { // POS
            btnPosActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_R & btnPresente.isEnabled()) { // Presente
            btnPresenteActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_H && btnCheque.isEnabled()) { // Cheque
            btnChequeActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_T && btnTroca.isEnabled()) { // Troca
            btnTrocaActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_E && btnDesc.isEnabled()) { // Desconto
            btnDescActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_A && btnAcres.isEnabled()) { // Acrescimo
            btnAcresActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_R && btnRemover.isEnabled()) { // Remover
            btnRemoverActionPerformed(null);
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
        for (int i = 0; i < dtmPag.getRowCount(); i++) {
            String tipo = dtmPag.getValueAt(i, 1).toString();
            if (tipo.equals("DINHEIRO")) {
                dtmPag.removeRow(i);
                atualizar();
                break;
            }
        }

        if (tefs > 0 && valor.compareTo(apagar.subtract(pago)) > 0) {
            JOptionPane.showMessageDialog(this, "Valor informado deve ser menor ou igual ao total a pagar!", "DINHEIRO", JOptionPane.INFORMATION_MESSAGE);
        } else if (tefs == 0 && valor.subtract(apagar.subtract(pago)).doubleValue() >= 100.00) {
            JOptionPane.showMessageDialog(this, "Valor informado não pode ter troco maior ou igual a R$ 100,00!", "DINHEIRO", JOptionPane.INFORMATION_MESSAGE);
        } else if (valor.compareTo(apagar) >= 0 && pago.doubleValue() > 0.00) {
            JOptionPane.showMessageDialog(this, "Valor informado deve ser menor ao total a pagar!", "DINHEIRO", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (valor.compareTo(new BigDecimal(0.00)) > 0) {
                dtmPag.addRow(new Object[]{Util.getConfig().getProperty("ecf.dinheiro"), "DINHEIRO", false, valor.doubleValue(), true, new Date(), "", ""});
                atualizar();
            }
        }
    }

    /**
     * Metodo que trata do pagamento com cartao.
     *
     * @param valor o valor informado do pagamento.
     */
    private void tef(final BigDecimal valor) {
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

                            // caso o valor pago no cartao seja diferente do informado
                            if (total.subtract(valor).doubleValue() > 0.00) {
                                acresdesc(99, "ACRESCIMO", total.subtract(valor).toString().replace(".", ","));
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
            String restante = Util.formataNumero(falta.doubleValue(), 1, 2, false);
            JOptionPane.showMessageDialog(this, "Valor informado deve ser maior que 0,00 e menor ou igual à " + restante, "TEF", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo que trata do pagamento com cartao presente.
     *
     * @param valor o valor informado do pagamento.
     * @param numero o numero do cartao presente usado.
     */
    private void presente(BigDecimal valor, String numero) {
        double sobra = valor.subtract(apagar.subtract(pago)).doubleValue();
        if (sobra > 0 && sobra < apagar.doubleValue()) {
            acresdesc(99, "ACRESCIMO", Util.formataNumero(sobra, 1, 2, false));
        }
        dtmPag.addRow(new Object[]{Util.getConfig().getProperty("ecf.presente"), "PRESENTE", false, valor.doubleValue(), true, new Date(), numero, ""});
        atualizar();
    }

    /**
     * Metodo que trata do acrescimo e desconto da venda.
     *
     * @param max o maximo em porcentagem que pode oferecer.
     * @param nome o nome de distingue o ACRESCIMO do DESCONTO.
     * @param texto o valor digitado pelo operador.
     */
    private boolean acresdesc(int max, String nome, String texto) {
        boolean porcento = false;

        if (texto != null) {
            texto = texto.replace(".", "").replace(",", ".");
            if (texto.endsWith("%")) {
                porcento = true;
                texto = texto.replace("%", "");
            }

            try {
                BigDecimal valor = new BigDecimal(texto).setScale(2, RoundingMode.HALF_UP);
                if (porcento) {
                    valor = total.multiply(valor).divide(new BigDecimal(100));
                }

                for (int i = 0; i < dtmPag.getRowCount(); i++) {
                    String tipo = dtmPag.getValueAt(i, 1).toString();
                    if (tipo.equals(nome.toUpperCase())) {
                        dtmPag.removeRow(i);
                        atualizar();
                        break;
                    }
                }

                if (valor.doubleValue() > 0.00 && valor.compareTo(total) < 0) {
                    BigDecimal maximo = total.multiply(new BigDecimal(max)).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
                    if (maximo.compareTo(valor) >= 0) {
                        dtmPag.addRow(new Object[]{"", nome.toUpperCase(), false, valor.doubleValue(), true, null, "", ""});
                        atualizar();
                    } else {
                        JOptionPane.showMessageDialog(this, "Valor informado é maior que o permitido!\nPorcentagem máximo permitida pelo gerente [" + max + "%]", nome.toUpperCase(), JOptionPane.WARNING_MESSAGE);
                        return false;
                    }
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Valor informado inválido!", nome.toUpperCase(), JOptionPane.INFORMATION_MESSAGE);
            }
        }
        return true;
    }

    /**
     * Metodo que seleciona um troca para a venda.
     */
    private void trocar() {
        AsyncCallback<EcfTroca> async = new AsyncCallback<EcfTroca>() {
            @Override
            public void sucesso(EcfTroca resultado) {
                BigDecimal valor = new BigDecimal(resultado.getEcfTrocaValor()).setScale(2, RoundingMode.HALF_UP);
                if (valor.compareTo(falta) > 0) {
                    JOptionPane.showMessageDialog(fechamento, "Valor informado deve ser menor ou igual ao total a pagar!", "TROCA", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    dtmPag.addRow(new Object[]{Util.getConfig().getProperty("ecf.troca"), "TROCA", false, valor.doubleValue(), true, new Date(), "", resultado});
                }
                atualizar();
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
     * Metodo que limpa a tela de fechamento.
     */
    private void limpar() {
        // botoes
        btnDinheiro.setEnabled(Boolean.valueOf(Util.getConfig().getProperty("pag.dinheiro")));
        btnTef.setEnabled(Boolean.valueOf(Util.getConfig().getProperty("pag.tef")));
        btnPos.setEnabled(Boolean.valueOf(Util.getConfig().getProperty("pag.pos")));
        btnPresente.setEnabled(Boolean.valueOf(Util.getConfig().getProperty("pag.presente")));
        btnCheque.setEnabled(Boolean.valueOf(Util.getConfig().getProperty("pag.cheque")));
        btnTroca.setEnabled(Boolean.valueOf(Util.getConfig().getProperty("pag.troca")));
        btnDesc.setEnabled(true);
        btnAcres.setEnabled(true);
        btnRemover.setEnabled(true);
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
        falta = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
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
                default:
                    pago = pago.add(valor);
                    break;
            }
        }
        apagar = total.add(acres).subtract(desc);
        falta = apagar.compareTo(pago) > 0 ? apagar.subtract(pago) : new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        troco = pago.compareTo(apagar) > 0 && pagDinheiro ? pago.subtract(apagar) : new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
        // imprime na tela
        lblPagarValor.setText("R$ " + Util.formataNumero(apagar.doubleValue(), 1, 2, true));
        lblPagoValor.setText("R$ " + Util.formataNumero(pago.doubleValue(), 1, 2, true));
        lblFaltaValor.setText("R$ " + Util.formataNumero(falta.doubleValue(), 1, 2, true));
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
        btnTef.setEnabled(false);
        btnPos.setEnabled(false);
        btnPresente.setEnabled(false);
        btnCheque.setEnabled(false);
        btnTroca.setEnabled(false);
        btnAcres.setEnabled(false);
        btnDesc.setEnabled(false);
        btnRemover.setEnabled(false);
        btnCancelar.setEnabled(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (pagDinheiro) {
                        ECF.getInstancia().enviar(EComando.ECF_AbreGaveta);
                    }
                    new ComandoFecharVenda(getPagamentos(), total.doubleValue(), acres.subtract(desc).doubleValue(), troco.doubleValue(), obs).executar();
                } catch (OpenPdvException ex) {
                    log.error("Erro ao fechar a venda.", ex);
                    cancelar();
                } finally {
                    Aguarde.getInstancia().setVisible(false);
                    dispose();
                    Caixa.getInstancia().requestFocus();
                }
            }
        }).start();
        Aguarde.getInstancia().setVisible(true);
    }

    /**
     * Metodo que cancela o pagamento.
     */
    private void cancelar() {
        if (tefs > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ComandoCancelarVenda(true).executar();
                        Caixa caixa = Caixa.getInstancia();
                        caixa.modoDisponivel();
                        caixa.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        dispose();
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
            for (EcfPagamento pag : getPagamentos()) {
                if (pag.getEcfPagamentoTipo().getEcfPagamentoTipoCodigo().equals(Util.getConfig().getProperty("ecf.presente"))) {
                    try {
                        new ComandoCartaoPresente("ativarCartao", pag.getEcfPagamentoNsu(), Caixa.getInstancia().getVenda().getSisVendedor()).executar();
                    } catch (OpenPdvException ex) {
                        JOptionPane.showMessageDialog(fechamento, ex.getMessage(), "PAGAMENTO", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            Caixa caixa = Caixa.getInstancia();
            caixa.modoAberto();
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
        List<EcfPagamento> tefCartao = new ArrayList<>();
        // reserva o primeiro pagamento para dinheiro
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
                            && tipo.getEcfPagamentoTipoCodigo().equals(Util.getConfig().getProperty("ecf.cartao")))) {
                        EcfPagamento pag = new EcfPagamento();
                        pag.setEcfVenda(Caixa.getInstancia().getVenda());
                        pag.setEcfPagamentoTipo(tipo);
                        pag.setEcfPagamentoGnf(0);
                        pag.setEcfPagamentoData(data);
                        pag.setEcfPagamentoValor(valor);
                        pag.setEcfPagamentoNsu(nsu);
                        pag.setEcfPagamentoEstorno('N');
                        pag.setArquivo(arquivo);
                        if (codigo.equals(Util.getConfig().getProperty("ecf.dinheiro"))) {
                            // se dinheiro coloca na primeira posicao
                            pagamentos.set(0, pag);
                        } else if (codigo.equals(Util.getConfig().getProperty("ecf.troca"))) {
                            if (Caixa.getInstancia().getVenda().getEcfTrocas() == null) {
                                Caixa.getInstancia().getVenda().setEcfTrocas(new ArrayList<EcfTroca>());
                            }
                            Caixa.getInstancia().getVenda().getEcfTrocas().add((EcfTroca) dtmPag.getValueAt(i, 7));
                            pagamentos.add(pag);
                        } else if (!tipo.isEcfPagamentoTipoTef()) {
                            // se for cheque sem tef ou presente
                            pagamentos.add(pag);
                        } else {
                            // se cartao ou cheque adiciona e deixa na ordem
                            tefCartao.add(pag);
                        }
                        break;
                    }
                }
            } else {
                EcfPagamento pag = (EcfPagamento) dtmPag.getValueAt(i, 7);
                pagamentos.add(pag);
            }
        }

        // verifica se teve dinheiro, caso contrario remove a posicao 0
        if (pagamentos.get(0) == null) {
            pagamentos.remove(0);
        }

        // adiciona os TEFs por ultimo
        for (EcfPagamento pag : tefCartao) {
            pagamentos.add(pag);
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

    public JButton getBtnRemover() {
        return btnRemover;
    }

    public void setBtnRemover(JButton btnRemover) {
        this.btnRemover = btnRemover;
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

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public BigDecimal getFalta() {
        return falta;
    }

    public void setFalta(BigDecimal falta) {
        this.falta = falta;
    }

    public JButton getBtnPos() {
        return btnPos;
    }

    public void setBtnPos(JButton btnPos) {
        this.btnPos = btnPos;
    }

    public JButton getBtnPresente() {
        return btnPresente;
    }

    public void setBtnPresente(JButton btnPresente) {
        this.btnPresente = btnPresente;
    }

    public JButton getBtnTef() {
        return btnTef;
    }

    public void setBtnTef(JButton btnTef) {
        this.btnTef = btnTef;
    }

    public JLabel getLblFalta() {
        return lblFalta;
    }

    public void setLblFalta(JLabel lblFalta) {
        this.lblFalta = lblFalta;
    }

    public JLabel getLblFaltaValor() {
        return lblFaltaValor;
    }

    public void setLblFaltaValor(JLabel lblFaltaValor) {
        this.lblFaltaValor = lblFaltaValor;
    }

    public JSeparator getSeparador() {
        return separador;
    }

    public void setSeparador(JSeparator separador) {
        this.separador = separador;
    }

}
