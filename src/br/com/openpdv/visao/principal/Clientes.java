package br.com.openpdv.visao.principal;

import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.TextFieldLimit;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.core.filtro.FiltroObjeto;
import br.com.openpdv.modelo.core.filtro.FiltroTexto;
import br.com.openpdv.modelo.core.filtro.FiltroGrupo;
import br.com.openpdv.modelo.core.filtro.Filtro;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.modelo.sistema.SisEstado;
import br.com.openpdv.modelo.sistema.SisMunicipio;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 * Classe que representa a listagem dos clientes do sistema.
 *
 * @author Pedro H. Lira
 */
public class Clientes extends javax.swing.JDialog {

    private static Clientes clientes;
    private Logger log;
    private int row;
    private int cod;
    private DefaultTableModel dtm;
    private CoreService service;
    private AsyncCallback<SisCliente> async;

    /**
     * Construtor padrao.
     */
    private Clientes() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Clientes.class);
        service = new CoreService<>();
        initComponents();
        setUF();

        dtm = (DefaultTableModel) tabClientes.getModel();
        tabClientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                row = tabClientes.getSelectedRow();
                setDados();
            }
        });

        // colocando limites nos campos
        txtCPF_CNPJ.setDocument(new TextFieldLimit(14, true));
        txtNome.setDocument(new TextFieldLimit(100));
        txtRG_IE.setDocument(new TextFieldLimit(20));
        txtEndereco.setDocument(new TextFieldLimit(255));
        txtNumero.setDocument(new TextFieldLimit(11, true));
        txtComplemento.setDocument(new TextFieldLimit(100));
        txtBairro.setDocument(new TextFieldLimit(100));
        txtCEP.setDocument(new TextFieldLimit(9));
        txtTelefone.setDocument(new TextFieldLimit(100));
        txtEmail.setDocument(new TextFieldLimit(100));
        taObs.setDocument(new TextFieldLimit(255));
    }

    /**
     * Metodo que retorna a instancia do componente.
     *
     * @param async o objeto de acesso assincrono.
     * @return o objeto do componente.
     */
    public static Clientes getInstancia(AsyncCallback<SisCliente> async) {
        if (clientes == null) {
            clientes = new Clientes();
        }

        clientes.btnNovo.setEnabled(async == null);
        clientes.btnExcluir.setEnabled(async == null);
        clientes.async = async;
        FiltroNumero fn = new FiltroNumero("sisClienteId", ECompara.IGUAL, 0);
        clientes.setLista(fn);
        return clientes;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblFiltro = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        spClientes = new javax.swing.JScrollPane();
        tabClientes = new javax.swing.JTable();
        painel = new javax.swing.JPanel();
        lblCPF_CNPJ = new javax.swing.JLabel();
        txtCPF_CNPJ = new javax.swing.JTextField();
        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        lblRG_IE = new javax.swing.JLabel();
        txtRG_IE = new javax.swing.JTextField();
        lblEndereco = new javax.swing.JLabel();
        txtEndereco = new javax.swing.JTextField();
        lblNumero = new javax.swing.JLabel();
        txtNumero = new javax.swing.JFormattedTextField();
        lblComplemento = new javax.swing.JLabel();
        txtComplemento = new javax.swing.JTextField();
        lblBairro = new javax.swing.JLabel();
        txtBairro = new javax.swing.JTextField();
        lblCEP = new javax.swing.JLabel();
        txtCEP = new javax.swing.JFormattedTextField();
        lblUF = new javax.swing.JLabel();
        cmbUF = new javax.swing.JComboBox();
        lblMunicipio = new javax.swing.JLabel();
        cmbMunicipio = new javax.swing.JComboBox();
        lblTelefone = new javax.swing.JLabel();
        txtTelefone = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblObs = new javax.swing.JLabel();
        spObs = new javax.swing.JScrollPane();
        taObs = new javax.swing.JTextArea();
        txtLimite = new javax.swing.JFormattedTextField();
        lblLimite = new javax.swing.JLabel();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Clientes");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
        setPreferredSize(new java.awt.Dimension(790, 282));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lblFiltro.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblFiltro.setText("Filtro:");

        txtFiltro.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtFiltro.setToolTipText("Digite o texto de busca e precione ENTER. Use % após o texto para filtrar pelo inicio e % antes do texto para filtrar pelo fim.");
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFiltroKeyPressed(evt);
            }
        });

        btnPesquisar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/pesquisa.png"))); // NOI18N
        btnPesquisar.setText("Pesquisar");
        btnPesquisar.setMaximumSize(new java.awt.Dimension(100, 30));
        btnPesquisar.setMinimumSize(new java.awt.Dimension(100, 30));
        btnPesquisar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });
        btnPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPesquisarKeyPressed(evt);
            }
        });

        tabClientes.setAutoCreateRowSorter(true);
        tabClientes.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "CPF/CNPJ", "Nome", "IE", "Endereço", "Nº", "Complemento", "Bairro", "CEP", "Município", "Telefone", "E-mail", "OBS", "Sinc"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabClientes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabClientes.setRowHeight(20);
        tabClientes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabClientes.setShowVerticalLines(false);
        tabClientes.getTableHeader().setReorderingAllowed(false);
        spClientes.setViewportView(tabClientes);
        tabClientes.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tabClientes.getColumnModel().getColumnCount() > 0) {
            tabClientes.getColumnModel().getColumn(0).setResizable(false);
            tabClientes.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabClientes.getColumnModel().getColumn(1).setResizable(false);
            tabClientes.getColumnModel().getColumn(1).setPreferredWidth(100);
            tabClientes.getColumnModel().getColumn(2).setResizable(false);
            tabClientes.getColumnModel().getColumn(2).setPreferredWidth(200);
            tabClientes.getColumnModel().getColumn(3).setResizable(false);
            tabClientes.getColumnModel().getColumn(3).setPreferredWidth(50);
            tabClientes.getColumnModel().getColumn(4).setResizable(false);
            tabClientes.getColumnModel().getColumn(4).setPreferredWidth(200);
            tabClientes.getColumnModel().getColumn(5).setResizable(false);
            tabClientes.getColumnModel().getColumn(5).setPreferredWidth(50);
            tabClientes.getColumnModel().getColumn(6).setResizable(false);
            tabClientes.getColumnModel().getColumn(6).setPreferredWidth(100);
            tabClientes.getColumnModel().getColumn(7).setResizable(false);
            tabClientes.getColumnModel().getColumn(7).setPreferredWidth(100);
            tabClientes.getColumnModel().getColumn(8).setResizable(false);
            tabClientes.getColumnModel().getColumn(8).setPreferredWidth(50);
            tabClientes.getColumnModel().getColumn(9).setResizable(false);
            tabClientes.getColumnModel().getColumn(9).setPreferredWidth(100);
            tabClientes.getColumnModel().getColumn(10).setResizable(false);
            tabClientes.getColumnModel().getColumn(10).setPreferredWidth(100);
            tabClientes.getColumnModel().getColumn(11).setResizable(false);
            tabClientes.getColumnModel().getColumn(11).setPreferredWidth(200);
            tabClientes.getColumnModel().getColumn(12).setResizable(false);
            tabClientes.getColumnModel().getColumn(12).setPreferredWidth(200);
            tabClientes.getColumnModel().getColumn(13).setResizable(false);
            tabClientes.getColumnModel().getColumn(13).setPreferredWidth(50);
        }

        painel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblCPF_CNPJ.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCPF_CNPJ.setText("CPF/CNPJ:");

        txtCPF_CNPJ.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCPF_CNPJ.setToolTipText("CPF ou CNPJ.");

        lblNome.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblNome.setText("Nome:");

        txtNome.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtNome.setToolTipText("Nome ou Razão Social.");

        lblRG_IE.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblRG_IE.setText("RG/IE:");

        txtRG_IE.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtRG_IE.setToolTipText("CPF ou CNPJ.");

        lblEndereco.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblEndereco.setText("Endereço:");

        txtEndereco.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblNumero.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblNumero.setText("Nº:");

        txtNumero.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtNumero.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblComplemento.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblComplemento.setText("Compl:");

        txtComplemento.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblBairro.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblBairro.setText("Bairro:");

        txtBairro.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtBairro.setToolTipText("Nome ou Razão Social.");

        lblCEP.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCEP.setText("CEP:");

        try {
            txtCEP.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCEP.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblUF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblUF.setText("UF:");

        cmbUF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmbUF.setMaximumRowCount(20);
        cmbUF.setToolTipText("Selecione um Estado");
        cmbUF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbUFActionPerformed(evt);
            }
        });

        lblMunicipio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblMunicipio.setText("Município:");

        cmbMunicipio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmbMunicipio.setMaximumRowCount(20);
        cmbMunicipio.setToolTipText("Selecione uma Cidade");

        lblTelefone.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblTelefone.setText("Fone:");

        txtTelefone.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtTelefone.setToolTipText("CPF ou CNPJ.");

        lblEmail.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblEmail.setText("Email:");

        txtEmail.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtEmail.setToolTipText("Nome ou Razão Social.");

        lblObs.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblObs.setText("OBS:");

        taObs.setColumns(50);
        taObs.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        taObs.setLineWrap(true);
        taObs.setRows(4);
        taObs.setTabSize(5);
        spObs.setViewportView(taObs);

        org.jdesktop.layout.GroupLayout painelLayout = new org.jdesktop.layout.GroupLayout(painel);
        painel.setLayout(painelLayout);
        painelLayout.setHorizontalGroup(
            painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(painelLayout.createSequentialGroup()
                .addContainerGap()
                .add(painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, painelLayout.createSequentialGroup()
                        .add(lblUF)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmbUF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblMunicipio)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmbMunicipio, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblTelefone)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtTelefone, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblEmail)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtEmail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(1, 1, 1))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, painelLayout.createSequentialGroup()
                        .add(painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblEndereco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblCPF_CNPJ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(painelLayout.createSequentialGroup()
                                .add(txtCPF_CNPJ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 121, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblNome)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtNome)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblRG_IE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtRG_IE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblCEP)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                            .add(painelLayout.createSequentialGroup()
                                .add(txtEndereco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 232, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblNumero)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtNumero, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblComplemento)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtComplemento)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblBairro)
                                .add(9, 9, 9)))
                        .add(painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(txtBairro)
                            .add(txtCEP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)))
                    .add(painelLayout.createSequentialGroup()
                        .add(lblObs)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(spObs)))
                .addContainerGap())
        );
        painelLayout.setVerticalGroup(
            painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(painelLayout.createSequentialGroup()
                .addContainerGap()
                .add(painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblNome)
                    .add(txtNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblCPF_CNPJ)
                    .add(txtCPF_CNPJ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblRG_IE)
                    .add(txtRG_IE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblCEP)
                    .add(txtCEP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblEndereco)
                    .add(txtEndereco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblNumero)
                    .add(txtNumero, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblBairro)
                    .add(txtBairro, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblComplemento)
                    .add(txtComplemento, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblUF)
                    .add(cmbUF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblMunicipio)
                    .add(cmbMunicipio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblTelefone)
                    .add(lblEmail)
                    .add(txtEmail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtTelefone, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblObs)
                    .add(spObs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtLimite.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtLimite.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtLimite.setText("100");
        txtLimite.setToolTipText("Número máximo de registros na listagem. [0] para todos");
        txtLimite.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtLimite.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLimiteFocusLost(evt);
            }
        });

        lblLimite.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblLimite.setText("Limite de registros.");

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

        btnExcluir.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/excluir.png"))); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.setMaximumSize(new java.awt.Dimension(100, 30));
        btnExcluir.setMinimumSize(new java.awt.Dimension(100, 30));
        btnExcluir.setPreferredSize(new java.awt.Dimension(100, 30));
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });
        btnExcluir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnExcluirKeyPressed(evt);
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
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(lblFiltro)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtFiltro, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 621, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnPesquisar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, painel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, spClientes)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                            .add(txtLimite, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(6, 6, 6)
                            .add(lblLimite)
                            .add(170, 170, 170)
                            .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblFiltro)
                    .add(txtFiltro, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnPesquisar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spClientes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 216, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(painel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(txtLimite, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(layout.createSequentialGroup()
                            .add(6, 6, 6)
                            .add(lblLimite))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(811, 541));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cancelar();
        }
    }//GEN-LAST:event_btnCancelarKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Caixa.getInstancia().setJanela(null);
    }//GEN-LAST:event_formWindowClosing

    private void cmbUFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUFActionPerformed
        setMunicipio((SisEstado) cmbUF.getSelectedItem());
    }//GEN-LAST:event_cmbUFActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        novo();
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnNovoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNovoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            novo();
        }
    }//GEN-LAST:event_btnNovoKeyPressed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        salvar();
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnSalvarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalvarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            salvar();
        }
    }//GEN-LAST:event_btnSalvarKeyPressed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnExcluirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnExcluirKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            excluir();
        }
    }//GEN-LAST:event_btnExcluirKeyPressed

    private void txtLimiteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLimiteFocusLost
        try {
            int limite = Integer.valueOf(txtLimite.getText());
            if (limite < 0) {
                txtLimite.setText("100");
            }
        } catch (NumberFormatException ex) {
            txtLimite.setText("100");
        }
    }//GEN-LAST:event_txtLimiteFocusLost

    private void txtFiltroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnPesquisarActionPerformed(null);
        }
    }//GEN-LAST:event_txtFiltroKeyPressed

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        String texto = txtFiltro.getText().toUpperCase();
        if (texto.equals("")) {
            setLista(null);
        } else {
            FiltroTexto ft = new FiltroTexto("sisClienteNome", ECompara.CONTEM, texto);
            FiltroTexto ft1 = new FiltroTexto("sisClienteDoc", ECompara.IGUAL, texto.replaceAll("\\D", ""));
            FiltroGrupo filtro = new FiltroGrupo(Filtro.OU, ft, ft1);
            setLista(filtro);
        }
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void btnPesquisarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPesquisarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnPesquisarActionPerformed(null);
        }
    }//GEN-LAST:event_btnPesquisarKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox cmbMunicipio;
    private javax.swing.JComboBox cmbUF;
    private javax.swing.JLabel lblBairro;
    private javax.swing.JLabel lblCEP;
    private javax.swing.JLabel lblCPF_CNPJ;
    private javax.swing.JLabel lblComplemento;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEndereco;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JLabel lblLimite;
    private javax.swing.JLabel lblMunicipio;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblNumero;
    private javax.swing.JLabel lblObs;
    private javax.swing.JLabel lblRG_IE;
    private javax.swing.JLabel lblTelefone;
    private javax.swing.JLabel lblUF;
    private javax.swing.JPanel painel;
    private javax.swing.JScrollPane spClientes;
    private javax.swing.JScrollPane spObs;
    private javax.swing.JTextArea taObs;
    private javax.swing.JTable tabClientes;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JFormattedTextField txtCEP;
    private javax.swing.JTextField txtCPF_CNPJ;
    private javax.swing.JTextField txtComplemento;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEndereco;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JFormattedTextField txtLimite;
    private javax.swing.JTextField txtNome;
    private javax.swing.JFormattedTextField txtNumero;
    private javax.swing.JTextField txtRG_IE;
    private javax.swing.JTextField txtTelefone;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo para adicionar um novo registro.
     */
    private void novo() {
        tabClientes.getSelectionModel().clearSelection();
        txtCPF_CNPJ.requestFocus();
    }

    /**
     * Metodo que salva um novo registro ou atualiza um existente.
     */
    private void salvar() {
        if (!validar() || txtNome.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "CPF/CNPJ e ou Nome nao sao validos!", "Clientes", JOptionPane.INFORMATION_MESSAGE);
        } else {
            SisCliente cliente = new SisCliente(cod);
            cliente.setSisClienteDoc(txtCPF_CNPJ.getText().replaceAll("\\D", ""));
            cliente.setSisClienteNome(txtNome.getText());
            cliente.setSisClienteDoc1(txtRG_IE.getText());
            cliente.setSisClienteEndereco(txtEndereco.getText());
            cliente.setSisClienteNumero(Integer.valueOf(txtNumero.getText()));
            cliente.setSisClienteComplemento(txtComplemento.getText());
            cliente.setSisClienteBairro(txtBairro.getText());
            cliente.setSisClienteCep(txtCEP.getText());
            cliente.setSisMunicipio((SisMunicipio) cmbMunicipio.getSelectedItem());
            cliente.setSisClienteTelefone(txtTelefone.getText());
            cliente.setSisClienteEmail(txtEmail.getText());
            cliente.setSisClienteData(new Date());
            cliente.setSisClienteObservacao(taObs.getText());
            cliente.setSisClienteSinc(false);

            try {
                cliente = (SisCliente) service.salvar(cliente);
                JOptionPane.showMessageDialog(this, "Registro salvo com sucesso.", "Clientes", JOptionPane.INFORMATION_MESSAGE);

                if (async != null) {
                    async.sucesso(cliente);
                    dispose();
                } else {
                    btnPesquisarActionPerformed(null);
                }
            } catch (OpenPdvException ex) {
                log.error("Erro ao salvar a embalagem.", ex);
                JOptionPane.showMessageDialog(this, "Não foi possível salvar o registro!", "Clientes", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Metodo que exclui um registro do sistema.
     */
    private void excluir() {
        if (cod > 0) {
            int escolha = JOptionPane.showOptionDialog(this, "Deseja remover o registro selecionado?", "Clientes",
                    JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
            if (escolha == 0) {
                try {
                    service.deletar(new SisCliente(cod));
                    btnPesquisarActionPerformed(null);
                } catch (OpenPdvException ex) {
                    log.debug("Erro ao excluir o cliente -> " + cod, ex);
                    JOptionPane.showMessageDialog(this, "Esta registro não pode ser excluído!", "Clientes", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um registro na listagem.", "Clientes", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo que cancela o registro do sistema.
     */
    private void cancelar() {
        if (async != null) {
            async.falha(null);
        }
        dispose();
        Caixa.getInstancia().setJanela(null);
    }

    /**
     * Metodo que valida o CPF e/ou CNPJ
     *
     * @return retorna verdadeiro se valido, falso caso contrario.
     */
    private boolean validar() {
        String texto = txtCPF_CNPJ.getText().replaceAll("\\D", "");
        if (!texto.equals("")) {
            if (texto.length() == 11) {
                return Util.isCPF(texto);
            } else if (texto.length() == 14) {
                return Util.isCNPJ(texto);
            }
        }
        return false;
    }

    /**
     * Metodo que seta os valores da tabela vindas do banco de dados.
     */
    private void setLista(Filtro filtro) {
        try {
            int limite = Integer.valueOf(txtLimite.getText());
            List<SisCliente> clis = service.selecionar(new SisCliente(), 0, limite, filtro);
            while (dtm.getRowCount() > 0) {
                dtm.removeRow(0);
            }

            for (SisCliente cli : clis) {
                Object[] obj = new Object[]{cli.getSisClienteId(), cli.getSisClienteDoc(), cli.getSisClienteNome(), cli.getSisClienteDoc1(), cli.getSisClienteEndereco(),
                    cli.getSisClienteNumero(), cli.getSisClienteComplemento(), cli.getSisClienteBairro(), cli.getSisClienteCep(),
                    cli.getSisMunicipio(), cli.getSisClienteTelefone(), cli.getSisClienteEmail(), cli.getSisClienteObservacao(), cli.isSisClienteSinc()
                };
                dtm.addRow(obj);
            }

            row = -1;
            setDados();
        } catch (OpenPdvException ex) {
            log.error("Erro ao selecionar os clientes do sistema", ex);
        }
    }

    /**
     * Metodo que seta os valores nos campos do formulario.
     */
    private void setDados() {
        if (row == -1) {
            cod = 0;
            txtCPF_CNPJ.setText("");
            txtNome.setText("NAO INFORMADO");
            txtRG_IE.setText("");
            txtEndereco.setText("");
            txtNumero.setText("0");
            txtComplemento.setText("");
            txtBairro.setText("");
            txtCEP.setText("");
            Util.selecionarCombo(cmbUF, Caixa.getInstancia().getEmpresa().getSisMunicipio().getSisEstado().getSisEstadoSigla());
            Util.selecionarCombo(cmbMunicipio, Caixa.getInstancia().getEmpresa().getSisMunicipio().getSisMunicipioDescricao());
            txtTelefone.setText("");
            txtEmail.setText("");
            taObs.setText("");
        } else {
            int rowModel = tabClientes.convertRowIndexToModel(row);
            cod = Integer.valueOf(tabClientes.getModel().getValueAt(rowModel, 0).toString());
            txtCPF_CNPJ.setText(tabClientes.getModel().getValueAt(rowModel, 1).toString());
            txtNome.setText(tabClientes.getModel().getValueAt(rowModel, 2).toString());
            txtRG_IE.setText(tabClientes.getModel().getValueAt(rowModel, 3).toString());
            txtEndereco.setText(tabClientes.getModel().getValueAt(rowModel, 4).toString());
            txtNumero.setText(tabClientes.getModel().getValueAt(rowModel, 5).toString());
            txtComplemento.setText(tabClientes.getModel().getValueAt(rowModel, 6).toString());
            txtBairro.setText(tabClientes.getModel().getValueAt(rowModel, 7).toString());
            txtCEP.setText(tabClientes.getModel().getValueAt(rowModel, 8).toString());
            SisMunicipio mun = (SisMunicipio) tabClientes.getModel().getValueAt(rowModel, 9);
            Util.selecionarCombo(cmbUF, mun.getSisEstado().getSisEstadoSigla());
            Util.selecionarCombo(cmbMunicipio, mun.getSisMunicipioDescricao());
            txtTelefone.setText(tabClientes.getModel().getValueAt(rowModel, 10).toString());
            txtEmail.setText(tabClientes.getModel().getValueAt(rowModel, 11).toString());
            taObs.setText(tabClientes.getModel().getValueAt(rowModel, 12).toString());
        }
    }

    /**
     * Metodo que carrega os valores das UFs.
     */
    private void setUF() {
        try {
            List<SisEstado> lista = service.selecionar(new SisEstado(), 0, 0, null);
            cmbUF.removeAllItems();
            for (SisEstado est : lista) {
                cmbUF.addItem(est);
            }
        } catch (OpenPdvException ex) {
            log.error("Nao carregou as UFs.", ex);
        } finally {
            cmbUF.setSelectedIndex(-1);
        }
    }

    /**
     * Metodo que carrega os valores dos municipios.
     */
    private void setMunicipio(SisEstado uf) {
        try {
            FiltroObjeto fo = new FiltroObjeto("sisEstado", ECompara.IGUAL, uf);
            List<SisMunicipio> lista = service.selecionar(new SisMunicipio(), 0, 0, fo);
            cmbMunicipio.removeAllItems();
            for (SisMunicipio mun : lista) {
                cmbMunicipio.addItem(mun);
            }
        } catch (OpenPdvException ex) {
            log.error("Nao carregou as cidades.", ex);
        } finally {
            cmbMunicipio.setSelectedIndex(-1);
        }
    }

    // GETs e SETs
    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }

    public void setDtm(DefaultTableModel dtm) {
        this.dtm = dtm;
    }

    public CoreService<SisCliente> getService() {
        return service;
    }

    public void setService(CoreService<SisCliente> service) {
        this.service = service;
    }

    public JScrollPane getSpClientes() {
        return spClientes;
    }

    public void setSpClientes(JScrollPane spClientes) {
        this.spClientes = spClientes;
    }

    public JTable getTabClientes() {
        return tabClientes;
    }

    public void setTabClientes(JTable tabClientes) {
        this.tabClientes = tabClientes;
    }

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

    public JButton getBtnExcluir() {
        return btnExcluir;
    }

    public void setBtnExcluir(JButton btnExcluir) {
        this.btnExcluir = btnExcluir;
    }

    public JButton getBtnNovo() {
        return btnNovo;
    }

    public void setBtnNovo(JButton btnNovo) {
        this.btnNovo = btnNovo;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public void setBtnSalvar(JButton btnSalvar) {
        this.btnSalvar = btnSalvar;
    }

    public JComboBox getCmbMunicipio() {
        return cmbMunicipio;
    }

    public void setCmbMunicipio(JComboBox cmbMunicipio) {
        this.cmbMunicipio = cmbMunicipio;
    }

    public JComboBox getCmbUF() {
        return cmbUF;
    }

    public void setCmbUF(JComboBox cmbUF) {
        this.cmbUF = cmbUF;
    }

    public JLabel getLblBairro() {
        return lblBairro;
    }

    public void setLblBairro(JLabel lblBairro) {
        this.lblBairro = lblBairro;
    }

    public JLabel getLblCEP() {
        return lblCEP;
    }

    public void setLblCEP(JLabel lblCEP) {
        this.lblCEP = lblCEP;
    }

    public JLabel getLblCPF_CNPJ() {
        return lblCPF_CNPJ;
    }

    public void setLblCPF_CNPJ(JLabel lblCPF_CNPJ) {
        this.lblCPF_CNPJ = lblCPF_CNPJ;
    }

    public JLabel getLblComplemento() {
        return lblComplemento;
    }

    public void setLblComplemento(JLabel lblComplemento) {
        this.lblComplemento = lblComplemento;
    }

    public JLabel getLblEmail() {
        return lblEmail;
    }

    public void setLblEmail(JLabel lblEmail) {
        this.lblEmail = lblEmail;
    }

    public JLabel getLblEndereco() {
        return lblEndereco;
    }

    public void setLblEndereco(JLabel lblEndereco) {
        this.lblEndereco = lblEndereco;
    }

    public JLabel getLblIE() {
        return lblRG_IE;
    }

    public void setLblIE(JLabel lblIE) {
        this.lblRG_IE = lblIE;
    }

    public JLabel getLblMunicipio() {
        return lblMunicipio;
    }

    public void setLblMunicipio(JLabel lblMunicipio) {
        this.lblMunicipio = lblMunicipio;
    }

    public JLabel getLblNome() {
        return lblNome;
    }

    public void setLblNome(JLabel lblNome) {
        this.lblNome = lblNome;
    }

    public JLabel getLblNumero() {
        return lblNumero;
    }

    public void setLblNumero(JLabel lblNumero) {
        this.lblNumero = lblNumero;
    }

    public JLabel getLblTelefone() {
        return lblTelefone;
    }

    public void setLblTelefone(JLabel lblTelefone) {
        this.lblTelefone = lblTelefone;
    }

    public JLabel getLblUF() {
        return lblUF;
    }

    public void setLblUF(JLabel lblUF) {
        this.lblUF = lblUF;
    }

    public JPanel getPainel() {
        return painel;
    }

    public void setPainel(JPanel painel) {
        this.painel = painel;
    }

    public JTextField getTxtBairro() {
        return txtBairro;
    }

    public void setTxtBairro(JTextField txtBairro) {
        this.txtBairro = txtBairro;
    }

    public JFormattedTextField getTxtCEP() {
        return txtCEP;
    }

    public void setTxtCEP(JFormattedTextField txtCEP) {
        this.txtCEP = txtCEP;
    }

    public JTextField getTxtCPF_CNPJ() {
        return txtCPF_CNPJ;
    }

    public void setTxtCPF_CNPJ(JTextField txtCPF_CNPJ) {
        this.txtCPF_CNPJ = txtCPF_CNPJ;
    }

    public JTextField getTxtComplemento() {
        return txtComplemento;
    }

    public void setTxtComplemento(JTextField txtComplemento) {
        this.txtComplemento = txtComplemento;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public void setTxtEmail(JTextField txtEmail) {
        this.txtEmail = txtEmail;
    }

    public JTextField getTxtEndereco() {
        return txtEndereco;
    }

    public void setTxtEndereco(JTextField txtEndereco) {
        this.txtEndereco = txtEndereco;
    }

    public JTextField getTxtIE() {
        return txtRG_IE;
    }

    public void setTxtIE(JTextField txtIE) {
        this.txtRG_IE = txtIE;
    }

    public JTextField getTxtNome() {
        return txtNome;
    }

    public void setTxtNome(JTextField txtNome) {
        this.txtNome = txtNome;
    }

    public JFormattedTextField getTxtNumero() {
        return txtNumero;
    }

    public void setTxtNumero(JFormattedTextField txtNumero) {
        this.txtNumero = txtNumero;
    }

    public JTextField getTxtTelefone() {
        return txtTelefone;
    }

    public void setTxtTelefone(JTextField txtTelefone) {
        this.txtTelefone = txtTelefone;
    }

    public JLabel getLblRG_IE() {
        return lblRG_IE;
    }

    public void setLblRG_IE(JLabel lblRG_IE) {
        this.lblRG_IE = lblRG_IE;
    }

    public JTextField getTxtRG_IE() {
        return txtRG_IE;
    }

    public void setTxtRG_IE(JTextField txtRG_IE) {
        this.txtRG_IE = txtRG_IE;
    }

    public JButton getBtnPesquisar() {
        return btnPesquisar;
    }

    public void setBtnPesquisar(JButton btnPesquisar) {
        this.btnPesquisar = btnPesquisar;
    }

    public JLabel getLblFiltro() {
        return lblFiltro;
    }

    public void setLblFiltro(JLabel lblFiltro) {
        this.lblFiltro = lblFiltro;
    }

    public JLabel getLblLimite() {
        return lblLimite;
    }

    public void setLblLimite(JLabel lblLimite) {
        this.lblLimite = lblLimite;
    }

    public JLabel getLblObs() {
        return lblObs;
    }

    public void setLblObs(JLabel lblObs) {
        this.lblObs = lblObs;
    }

    public JScrollPane getSpObs() {
        return spObs;
    }

    public void setSpObs(JScrollPane spObs) {
        this.spObs = spObs;
    }

    public JTextArea getTaObs() {
        return taObs;
    }

    public void setTaObs(JTextArea taObs) {
        this.taObs = taObs;
    }

    public JTextField getTxtFiltro() {
        return txtFiltro;
    }

    public void setTxtFiltro(JTextField txtFiltro) {
        this.txtFiltro = txtFiltro;
    }

    public JFormattedTextField getTxtLimite() {
        return txtLimite;
    }

    public void setTxtLimite(JFormattedTextField txtLimite) {
        this.txtLimite = txtLimite;
    }

    public AsyncCallback<SisCliente> getAsync() {
        return async;
    }

    public void setAsync(AsyncCallback<SisCliente> async) {
        this.async = async;
    }
}
