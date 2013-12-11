package br.com.openpdv.visao.principal;

import br.com.openpdv.visao.principal.*;
import br.com.openpdv.controlador.core.*;
import br.com.openpdv.controlador.permissao.Login;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.EDirecao;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.core.parametro.ParametroObjeto;
import br.com.openpdv.modelo.ecf.EcfDocumento;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.ecf.EcfZ;
import br.com.openpdv.modelo.ecf.EcfZTotais;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 * Classe que representa a listagem dos zs do sistema.
 *
 * @author Pedro H. Lira
 */
public class LeiturasZ extends javax.swing.JDialog {

    private static LeiturasZ leituraZ;
    private Logger log;
    private int row;
    private int cod;
    private DefaultTableModel dtmZ;
    private DefaultTableModel dtmTotal;
    private CoreService service;

    /**
     * Construtor padrao.
     */
    private LeiturasZ() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Produtos.class);
        initComponents();

        service = new CoreService<>();
        dtmZ = (DefaultTableModel) tabLeituras.getModel();
        dtmTotal = (DefaultTableModel) tabTotal.getModel();
        tabLeituras.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                row = tabLeituras.getSelectedRow();
                setDados();
            }
        });

        // colocando limites nos campos
        txtCOOinicio.setDocument(new TextFieldLimit(8, true));
        txtCOOfim.setDocument(new TextFieldLimit(8, true));
        txtCRO.setDocument(new TextFieldLimit(3, true));
        txtCRZ.setDocument(new TextFieldLimit(8, true));
        txtBruto.setDocument(new TextFieldLimit(16));
        txtTotal.setDocument(new TextFieldLimit(16));
    }

    /**
     * Metodo que retorna a instancia do componente.
     *
     * @return o objeto do componente.
     */
    public static LeiturasZ getInstancia() {
        if (leituraZ == null) {
            leituraZ = new LeiturasZ();
        }

        leituraZ.setLista();
        return leituraZ;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spLeituras = new javax.swing.JScrollPane();
        tabLeituras = new javax.swing.JTable();
        panLeitura = new javax.swing.JPanel();
        lblCOOinicio = new javax.swing.JLabel();
        txtCOOinicio = new javax.swing.JTextField();
        lblCOOfim = new javax.swing.JLabel();
        txtCOOfim = new javax.swing.JTextField();
        lblCRO = new javax.swing.JLabel();
        txtCRO = new javax.swing.JTextField();
        lblCRZ = new javax.swing.JLabel();
        txtCRZ = new javax.swing.JTextField();
        lblData = new javax.swing.JLabel();
        txtData = new javax.swing.JFormattedTextField();
        lblBruto = new javax.swing.JLabel();
        txtBruto = new javax.swing.JFormattedTextField();
        chkIssqn = new javax.swing.JCheckBox();
        lblTotal = new javax.swing.JLabel();
        txtTotal = new javax.swing.JFormattedTextField();
        tabTotais = new javax.swing.JTabbedPane();
        panTotais = new javax.swing.JPanel();
        btnAdicionar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        spTotais = new javax.swing.JScrollPane();
        tabTotal = new javax.swing.JTable();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Leituras Z");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        spLeituras.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabLeituras.setAutoCreateRowSorter(true);
        tabLeituras.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabLeituras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "COO Inicio", "COO Fim", "CRO", "CRZ", "Data", "Bruto", "Total", "Issqn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabLeituras.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabLeituras.setRowHeight(20);
        tabLeituras.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabLeituras.setShowGrid(true);
        tabLeituras.setShowVerticalLines(false);
        tabLeituras.getTableHeader().setReorderingAllowed(false);
        spLeituras.setViewportView(tabLeituras);
        tabLeituras.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tabLeituras.getColumnModel().getColumnCount() > 0) {
            tabLeituras.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabLeituras.getColumnModel().getColumn(1).setPreferredWidth(75);
            tabLeituras.getColumnModel().getColumn(2).setPreferredWidth(75);
            tabLeituras.getColumnModel().getColumn(3).setPreferredWidth(50);
            tabLeituras.getColumnModel().getColumn(4).setPreferredWidth(50);
            tabLeituras.getColumnModel().getColumn(5).setPreferredWidth(75);
            tabLeituras.getColumnModel().getColumn(6).setPreferredWidth(75);
            tabLeituras.getColumnModel().getColumn(6).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
            tabLeituras.getColumnModel().getColumn(7).setPreferredWidth(100);
            tabLeituras.getColumnModel().getColumn(7).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
            tabLeituras.getColumnModel().getColumn(8).setPreferredWidth(50);
        }

        panLeitura.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLeitura.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        lblCOOinicio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCOOinicio.setText("COO Início:");

        txtCOOinicio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCOOinicio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblCOOfim.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCOOfim.setText("COO Fim:");

        txtCOOfim.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCOOfim.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblCRO.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCRO.setText("CRO:");

        txtCRO.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCRO.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblCRZ.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCRZ.setText("CRZ:");

        txtCRZ.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCRZ.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblData.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblData.setText("Data:");

        txtData.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtData.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblBruto.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblBruto.setText("Bruto:");

        txtBruto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        txtBruto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBruto.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        chkIssqn.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        chkIssqn.setText("Issqn");

        lblTotal.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblTotal.setText("Total:");

        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotal.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        org.jdesktop.layout.GroupLayout panLeituraLayout = new org.jdesktop.layout.GroupLayout(panLeitura);
        panLeitura.setLayout(panLeituraLayout);
        panLeituraLayout.setHorizontalGroup(
            panLeituraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panLeituraLayout.createSequentialGroup()
                .add(panLeituraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panLeituraLayout.createSequentialGroup()
                        .add(txtCOOinicio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtCOOfim, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtCRO, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtCRZ, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                    .add(panLeituraLayout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(lblCOOinicio)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblCOOfim)
                        .add(18, 18, 18)
                        .add(lblCRO)
                        .add(18, 18, 18)
                        .add(lblCRZ)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panLeituraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtData, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblData))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panLeituraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtBruto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblBruto))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panLeituraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panLeituraLayout.createSequentialGroup()
                        .add(txtTotal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 115, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chkIssqn))
                    .add(lblTotal))
                .addContainerGap())
        );
        panLeituraLayout.setVerticalGroup(
            panLeituraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panLeituraLayout.createSequentialGroup()
                .add(6, 6, 6)
                .add(panLeituraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblCOOinicio)
                    .add(lblBruto)
                    .add(lblCOOfim)
                    .add(lblCRO)
                    .add(lblCRZ)
                    .add(lblData)
                    .add(lblTotal))
                .add(6, 6, 6)
                .add(panLeituraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtCOOinicio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtBruto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkIssqn)
                    .add(txtCOOfim, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtCRO, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtCRZ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtData, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtTotal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabTotais.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        panTotais.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        btnAdicionar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/novo.png"))); // NOI18N
        btnAdicionar.setText("Adicionar");
        btnAdicionar.setMaximumSize(new java.awt.Dimension(100, 30));
        btnAdicionar.setMinimumSize(new java.awt.Dimension(100, 30));
        btnAdicionar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });
        btnAdicionar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAdicionarKeyPressed(evt);
            }
        });

        btnRemover.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/excluir.png"))); // NOI18N
        btnRemover.setText("Remover");
        btnRemover.setMaximumSize(new java.awt.Dimension(100, 30));
        btnRemover.setMinimumSize(new java.awt.Dimension(100, 30));
        btnRemover.setPreferredSize(new java.awt.Dimension(100, 30));
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

        spTotais.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabTotal.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Código", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabTotal.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabTotal.setRowHeight(20);
        tabTotal.setShowGrid(true);
        tabTotal.setShowVerticalLines(false);
        tabTotal.getTableHeader().setReorderingAllowed(false);
        spTotais.setViewportView(tabTotal);
        if (tabTotal.getColumnModel().getColumnCount() > 0) {
            tabTotal.getColumnModel().getColumn(0).setMinWidth(1);
            tabTotal.getColumnModel().getColumn(0).setPreferredWidth(1);
            tabTotal.getColumnModel().getColumn(0).setMaxWidth(1);
            tabTotal.getColumnModel().getColumn(1).setMinWidth(100);
            tabTotal.getColumnModel().getColumn(1).setPreferredWidth(100);
            tabTotal.getColumnModel().getColumn(1).setMaxWidth(100);
            tabTotal.getColumnModel().getColumn(2).setMinWidth(100);
            tabTotal.getColumnModel().getColumn(2).setPreferredWidth(100);
            tabTotal.getColumnModel().getColumn(2).setMaxWidth(100);
            tabTotal.getColumnModel().getColumn(2).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
        }

        org.jdesktop.layout.GroupLayout panTotaisLayout = new org.jdesktop.layout.GroupLayout(panTotais);
        panTotais.setLayout(panTotaisLayout);
        panTotaisLayout.setHorizontalGroup(
            panTotaisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panTotaisLayout.createSequentialGroup()
                .add(6, 6, 6)
                .add(panTotaisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(spTotais, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 597, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(panTotaisLayout.createSequentialGroup()
                        .add(btnAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(btnRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(258, 258, 258))
        );
        panTotaisLayout.setVerticalGroup(
            panTotaisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panTotaisLayout.createSequentialGroup()
                .add(panTotaisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(6, 6, 6)
                .add(spTotais, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        tabTotais.addTab("Totais", new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/preco.png")), panTotais); // NOI18N

        btnNovo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/novo.png"))); // NOI18N
        btnNovo.setText("Novo");
        btnNovo.setMaximumSize(new java.awt.Dimension(100, 30));
        btnNovo.setMinimumSize(new java.awt.Dimension(100, 30));
        btnNovo.setPreferredSize(new java.awt.Dimension(100, 30));
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });
        btnNovo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnNovoKeyPressed(evt);
            }
        });

        btnSalvar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/salvar.png"))); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.setMaximumSize(new java.awt.Dimension(100, 30));
        btnSalvar.setMinimumSize(new java.awt.Dimension(100, 30));
        btnSalvar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });
        btnSalvar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSalvarKeyPressed(evt);
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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(spLeituras)
                            .add(tabTotais, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(panLeitura, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(spLeituras, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panLeitura, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabTotais, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 208, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(640, 531));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        salvar();
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnSalvarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalvarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            salvar();
        }
    }//GEN-LAST:event_btnSalvarKeyPressed

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

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        novo();
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnNovoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNovoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            novo();
        }
    }//GEN-LAST:event_btnNovoKeyPressed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        adicionarTotal();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnAdicionarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAdicionarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            adicionarTotal();
        }
    }//GEN-LAST:event_btnAdicionarKeyPressed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        removerTotal();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnRemoverKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnRemoverKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            removerTotal();
        }
    }//GEN-LAST:event_btnRemoverKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Caixa.getInstancia().setJanela(null);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JCheckBox chkIssqn;
    private javax.swing.JLabel lblBruto;
    private javax.swing.JLabel lblCOOfim;
    private javax.swing.JLabel lblCOOinicio;
    private javax.swing.JLabel lblCRO;
    private javax.swing.JLabel lblCRZ;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel panLeitura;
    private javax.swing.JPanel panTotais;
    private javax.swing.JScrollPane spLeituras;
    private javax.swing.JScrollPane spTotais;
    private javax.swing.JTable tabLeituras;
    private javax.swing.JTabbedPane tabTotais;
    private javax.swing.JTable tabTotal;
    private javax.swing.JFormattedTextField txtBruto;
    private javax.swing.JTextField txtCOOfim;
    private javax.swing.JTextField txtCOOinicio;
    private javax.swing.JTextField txtCRO;
    private javax.swing.JTextField txtCRZ;
    private javax.swing.JFormattedTextField txtData;
    private javax.swing.JFormattedTextField txtTotal;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo para adicionar um novo registro.
     */
    private void novo() {
        tabLeituras.clearSelection();
        tabTotais.setSelectedIndex(0);
        txtCOOinicio.requestFocus();
    }

    /**
     * Metodo que salva um novo registro ou atualiza um existente.
     */
    private void salvar() {
        if (txtCOOinicio.getText().equals("") || txtCOOfim.getText().equals("") || txtCRO.getText().equals("") || txtCRZ.getText().equals("") || txtData.getText().equals("") || txtBruto.getText().equals("") || txtTotal.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "LeituraZ", JOptionPane.INFORMATION_MESSAGE);
        } else {
            EntityManagerFactory emf = null;
            EntityManager em = null;

            try {
                // salva a Z
                EcfZ z = new EcfZ(cod);
                z.setEcfImpressora(Caixa.getInstancia().getImpressora());
                z.setEcfZUsuario(Login.getOperador() == null ? 0 : Login.getOperador().getId());
                z.setEcfZCooIni(Integer.valueOf(txtCOOinicio.getText()));
                z.setEcfZCooFin(Integer.valueOf(txtCOOfim.getText()));
                z.setEcfZCro(Integer.valueOf(txtCRO.getText()));
                z.setEcfZCrz(Integer.valueOf(txtCRZ.getText()));
                z.setEcfZMovimento(new SimpleDateFormat("dd/MM/yyyy").parse(txtData.getText()));
                z.setEcfZEmissao(new Date());
                z.setEcfZBruto(Double.valueOf(txtBruto.getText().replace(",", ".")));
                z.setEcfZGt(Double.valueOf(txtTotal.getText().replace(",", ".")));
                z.setEcfZIssqn(chkIssqn.isSelected());
                z = (EcfZ) service.salvar(z);

                // recupera uma instancia do gerenciador de entidades
                emf = Conexao.getInstancia();
                em = emf.createEntityManager();
                em.getTransaction().begin();

                // valida e salva os totais
                List<EcfZTotais> totais = new ArrayList<>();
                if (validaTotais(totais, z)) {
                    salvarTotais(em, z, totais);
                } else {
                    throw new Exception();
                }

                Calendar cal = Calendar.getInstance();
                cal.setTime(z.getEcfZMovimento());
                cal.add(Calendar.DAY_OF_MONTH, 1);

                // atualiza as vendas, marcando que pertence a esta Z
                ParametroObjeto po = new ParametroObjeto("ecfZ", z);
                FiltroObjeto fo = new FiltroObjeto("ecfImpressora", ECompara.IGUAL, Caixa.getInstancia().getImpressora());
                FiltroData fd1 = new FiltroData("ecfVendaData", ECompara.MAIOR_IGUAL, z.getEcfZMovimento());
                FiltroData fd2 = new FiltroData("ecfVendaData", ECompara.MENOR, cal.getTime());
                GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fo, fd1, fd2});
                Sql sql = new Sql(new EcfVenda(), EComandoSQL.ATUALIZAR, gf, po);
                service.executar(em, sql);

                // atualiza os documentos, marcando que pertence a esta Z
                fd1 = new FiltroData("ecfDocumentoData", ECompara.MAIOR_IGUAL, z.getEcfZMovimento());
                fd2 = new FiltroData("ecfDocumentoData", ECompara.MENOR, cal.getTime());
                gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fo, fd1, fd2});
                sql = new Sql(new EcfDocumento(), EComandoSQL.ATUALIZAR, gf, po);
                service.executar(em, sql);

                em.getTransaction().commit();
                JOptionPane.showMessageDialog(this, "Registro salvo com sucesso.", "LeituraZ", JOptionPane.INFORMATION_MESSAGE);
                setLista();
            } catch (Exception ex) {
                if (em != null && em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                log.error("Erro ao salvar o produto.", ex);
                JOptionPane.showMessageDialog(this, "Não foi possível salvar o registro!", "LeituraZ", JOptionPane.WARNING_MESSAGE);
            } finally {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que valida os totais da Z.
     *
     * @param totais a lista de totais.
     * @param z a leitura Z atual.
     * @return verdadeiro se valido, falso caso contrario
     */
    private boolean validaTotais(List<EcfZTotais> totais, EcfZ z) {
        try {
            for (int i = 0; i < dtmTotal.getRowCount(); i++) {
                EcfZTotais total = new EcfZTotais();
                total.setEcfZ(new EcfZ(z.getId()));
                total.setEcfZTotaisCodigo(dtmTotal.getValueAt(i, 1).toString());
                total.setEcfZTotaisValor(Double.valueOf(dtmTotal.getValueAt(i, 2).toString()));
                totais.add(total);
            }
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Problemas com os dados de totais!", "Totais", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    /**
     * Metodo que salva os totais.
     *
     * @param em a transacao ativa
     * @param z a leitura Z atual.
     * @param totais a lista de totais.
     * @throws OpenPdvException caso ocorra alguma exececao
     */
    private void salvarTotais(EntityManager em, EcfZ z, List<EcfZTotais> totais) throws OpenPdvException {
        // deleta
        if (cod > 0) {
            FiltroObjeto fo = new FiltroObjeto("ecfZ", ECompara.IGUAL, z);
            Sql sql = new Sql(new EcfZTotais(), EComandoSQL.EXCLUIR, fo);
            service.executar(em, sql);
        }

        // insere
        service.salvar(em, totais);
    }

    /**
     * Metodo que seta os valores da tabela vindas do banco de dados.
     */
    private void setLista() {
        try {
            EcfZ ecfZ = new EcfZ();
            ecfZ.setOrdemDirecao(EDirecao.DESC);
            List<EcfZ> lista = service.selecionar(ecfZ, 0, 0, null);
            while (dtmZ.getRowCount() > 0) {
                dtmZ.removeRow(0);
            }

            for (EcfZ z : lista) {
                String data = new SimpleDateFormat("dd/MM/yyyy").format(z.getEcfZMovimento());
                Object[] obj = new Object[]{z.getId(), z.getEcfZCooIni(), z.getEcfZCooFin(), z.getEcfZCro(), z.getEcfZCrz(), data, z.getEcfZBruto(), z.getEcfZGt(), z.getEcfZIssqn()};
                dtmZ.addRow(obj);
            }

            row = -1;
            setDados();
        } catch (OpenPdvException ex) {
            log.error("Erro ao selecionar as Zs do sistema", ex);
        }
    }

    /**
     * Metodo que seta os valores nos campos do formulario.
     */
    private void setDados() {
        // removendo os totais
        while (dtmTotal.getRowCount() > 0) {
            dtmTotal.removeRow(0);
        }

        if (row == -1) {
            cod = 0;
            txtCOOinicio.setText("");
            txtCOOfim.setText("");
            txtCRO.setText("");
            txtCRZ.setText("");
            txtData.setText(null);
            txtBruto.setText("");
            txtTotal.setText("");
            chkIssqn.setSelected(false);
            btnSalvar.setEnabled(true);
        } else {
            int rowModel = tabLeituras.convertRowIndexToModel(row);
            cod = Integer.valueOf(tabLeituras.getModel().getValueAt(rowModel, 0).toString());
            txtCOOinicio.setText(tabLeituras.getModel().getValueAt(rowModel, 1).toString());
            txtCOOfim.setText(tabLeituras.getModel().getValueAt(rowModel, 2).toString());
            txtCRO.setText(tabLeituras.getModel().getValueAt(rowModel, 3).toString());
            txtCRZ.setText(tabLeituras.getModel().getValueAt(rowModel, 4).toString());
            txtData.setText(tabLeituras.getModel().getValueAt(rowModel, 5).toString());
            txtBruto.setText(tabLeituras.getModel().getValueAt(rowModel, 6).toString().replace(".", ","));
            txtTotal.setText(tabLeituras.getModel().getValueAt(rowModel, 7).toString().replace(".", ","));
            chkIssqn.setSelected(Boolean.valueOf(tabLeituras.getModel().getValueAt(rowModel, 8).toString()));
            btnSalvar.setEnabled(false);

            try {
                // recupera o z para colocar os totais
                EcfZ z = new EcfZ();
                FiltroNumero fn = new FiltroNumero("ecfZId", ECompara.IGUAL, cod);
                z = (EcfZ) service.selecionar(z, fn);
                for (EcfZTotais total : z.getEcfZTotais()) {
                    Object[] obj = new Object[]{total.getId(), total.getEcfZTotaisCodigo(), total.getEcfZTotaisValor()};
                    dtmTotal.addRow(obj);
                }
            } catch (OpenPdvException ex) {
                log.error("Erro ao selecionar a Z do sistema", ex);
            }
        }
    }

    /**
     * Adicionar uma linha ao total.
     */
    private void adicionarTotal() {
        dtmTotal.addRow(new Object[]{0, "", 0.00});
    }

    /**
     * Remove a linha selecionada do total.
     */
    private void removerTotal() {
        int rowTotal = tabTotal.getSelectedRow();
        if (rowTotal >= 0) {
            dtmTotal.removeRow(rowTotal);
        }
    }

    // GETs e SETs
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public DefaultTableModel getDtmZ() {
        return dtmZ;
    }

    public void setDtmZ(DefaultTableModel dtmZ) {
        this.dtmZ = dtmZ;
    }

    public DefaultTableModel getDtmTotal() {
        return dtmTotal;
    }

    public void setDtmTotal(DefaultTableModel dtmTotal) {
        this.dtmTotal = dtmTotal;
    }

    public JButton getBtnAdicionar() {
        return btnAdicionar;
    }

    public void setBtnAdicionar(JButton btnAdicionar) {
        this.btnAdicionar = btnAdicionar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

    public JButton getBtnNovo() {
        return btnNovo;
    }

    public void setBtnNovo(JButton btnNovo) {
        this.btnNovo = btnNovo;
    }

    public JButton getBtnRemover() {
        return btnRemover;
    }

    public void setBtnRemover(JButton btnRemover) {
        this.btnRemover = btnRemover;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public void setBtnSalvar(JButton btnSalvar) {
        this.btnSalvar = btnSalvar;
    }

    public JCheckBox getChkIssqn() {
        return chkIssqn;
    }

    public void setChkIssqn(JCheckBox chkIssqn) {
        this.chkIssqn = chkIssqn;
    }

    public JLabel getLblBruto() {
        return lblBruto;
    }

    public void setLblBruto(JLabel lblBruto) {
        this.lblBruto = lblBruto;
    }

    public JLabel getLblCOOfim() {
        return lblCOOfim;
    }

    public void setLblCOOfim(JLabel lblCOOfim) {
        this.lblCOOfim = lblCOOfim;
    }

    public JLabel getLblCOOinicio() {
        return lblCOOinicio;
    }

    public void setLblCOOinicio(JLabel lblCOOinicio) {
        this.lblCOOinicio = lblCOOinicio;
    }

    public JLabel getLblCRO() {
        return lblCRO;
    }

    public void setLblCRO(JLabel lblCRO) {
        this.lblCRO = lblCRO;
    }

    public JLabel getLblCRZ() {
        return lblCRZ;
    }

    public void setLblCRZ(JLabel lblCRZ) {
        this.lblCRZ = lblCRZ;
    }

    public JLabel getLblData() {
        return lblData;
    }

    public void setLblData(JLabel lblData) {
        this.lblData = lblData;
    }

    public JLabel getLblTotal() {
        return lblTotal;
    }

    public void setLblTotal(JLabel lblTotal) {
        this.lblTotal = lblTotal;
    }

    public JPanel getPanLeitura() {
        return panLeitura;
    }

    public void setPanLeitura(JPanel panLeitura) {
        this.panLeitura = panLeitura;
    }

    public JPanel getPanTotais() {
        return panTotais;
    }

    public void setPanTotais(JPanel panTotais) {
        this.panTotais = panTotais;
    }

    public JScrollPane getSpLeituras() {
        return spLeituras;
    }

    public void setSpLeituras(JScrollPane spLeituras) {
        this.spLeituras = spLeituras;
    }

    public JScrollPane getSpTotais() {
        return spTotais;
    }

    public void setSpTotais(JScrollPane spTotais) {
        this.spTotais = spTotais;
    }

    public JTable getTabLeituras() {
        return tabLeituras;
    }

    public void setTabLeituras(JTable tabLeituras) {
        this.tabLeituras = tabLeituras;
    }

    public JTabbedPane getTabTotais() {
        return tabTotais;
    }

    public void setTabTotais(JTabbedPane tabTotais) {
        this.tabTotais = tabTotais;
    }

    public JTable getTabTotal() {
        return tabTotal;
    }

    public void setTabTotal(JTable tabTotal) {
        this.tabTotal = tabTotal;
    }

    public JFormattedTextField getTxtBruto() {
        return txtBruto;
    }

    public void setTxtBruto(JFormattedTextField txtBruto) {
        this.txtBruto = txtBruto;
    }

    public JTextField getTxtCOOfim() {
        return txtCOOfim;
    }

    public void setTxtCOOfim(JTextField txtCOOfim) {
        this.txtCOOfim = txtCOOfim;
    }

    public JTextField getTxtCOOinicio() {
        return txtCOOinicio;
    }

    public void setTxtCOOinicio(JTextField txtCOOinicio) {
        this.txtCOOinicio = txtCOOinicio;
    }

    public JTextField getTxtCRO() {
        return txtCRO;
    }

    public void setTxtCRO(JTextField txtCRO) {
        this.txtCRO = txtCRO;
    }

    public JTextField getTxtCRZ() {
        return txtCRZ;
    }

    public void setTxtCRZ(JTextField txtCRZ) {
        this.txtCRZ = txtCRZ;
    }

    public JFormattedTextField getTxtData() {
        return txtData;
    }

    public void setTxtData(JFormattedTextField txtData) {
        this.txtData = txtData;
    }

    public JFormattedTextField getTxtTotal() {
        return txtTotal;
    }

    public void setTxtTotal(JFormattedTextField txtTotal) {
        this.txtTotal = txtTotal;
    }

}
