package br.com.openpdv.visao.principal;

import br.com.openpdv.controlador.core.*;
import br.com.openpdv.modelo.core.EBusca;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.produto.ProdComposicao;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdGrade;
import br.com.openpdv.modelo.produto.ProdGradeTipo;
import br.com.openpdv.modelo.produto.ProdPreco;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * Classe que representa a listagem dos produtos do sistema.
 *
 * @author Pedro H. Lira
 */
public class Produtos extends javax.swing.JDialog {

    private static Produtos produtos;
    private Logger log;
    private int row;
    private int cod;
    private DefaultTableModel dtmProduto;
    private DefaultTableModel dtmPreco;
    private DefaultTableModel dtmItem;
    private DefaultTableModel dtmGrade;
    private CoreService service;

    /**
     * Construtor padrao.
     */
    private Produtos() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Produtos.class);
        initComponents();

        service = new CoreService<>();
        dtmProduto = (DefaultTableModel) tabProdutos.getModel();
        dtmPreco = (DefaultTableModel) tabPreco.getModel();
        dtmItem = (DefaultTableModel) tabItem.getModel();
        dtmGrade = (DefaultTableModel) tabGrade.getModel();
        tabProdutos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                row = tabProdutos.getSelectedRow();
                setDados();
            }
        });

        // colocando limites nos campos
        txtNCM.setDocument(new TextFieldLimit(8, true));
        txtBarra.setDocument(new TextFieldLimit(14, true));
        txtDescricao.setDocument(new TextFieldLimit(100));
        txtREF.setDocument(new TextFieldLimit(20));
        txtPreco.setDocument(new TextFieldLimit(10));
        txtEstoque.setDocument(new TextFieldLimit(10));
        txtCST_CSON.setDocument(new TextFieldLimit(3, true));
        txtICMS.setDocument(new TextFieldLimit(5));
        txtISSQN.setDocument(new TextFieldLimit(5));
    }

    /**
     * Metodo que retorna a instancia do componente.
     *
     * @return o objeto do componente.
     */
    public static Produtos getInstancia() {
        if (produtos == null) {
            produtos = new Produtos();
        }

        produtos.setEmbalagens();
        produtos.setTipos();
        produtos.setLista(null);
        return produtos;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblFiltro = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        spProdutos = new javax.swing.JScrollPane();
        tabProdutos = new javax.swing.JTable();
        tabProd = new javax.swing.JTabbedPane();
        panProdutos = new javax.swing.JPanel();
        lblNCM = new javax.swing.JLabel();
        txtNCM = new javax.swing.JTextField();
        lblBarra = new javax.swing.JLabel();
        txtBarra = new javax.swing.JTextField();
        lblDescricao = new javax.swing.JLabel();
        txtDescricao = new javax.swing.JTextField();
        lblREF = new javax.swing.JLabel();
        txtREF = new javax.swing.JTextField();
        lblPreco = new javax.swing.JLabel();
        txtPreco = new javax.swing.JFormattedTextField();
        lblEmbalagem = new javax.swing.JLabel();
        cmbEmbalagem = new javax.swing.JComboBox();
        lblEstoque = new javax.swing.JLabel();
        txtEstoque = new javax.swing.JFormattedTextField();
        lblOrigem = new javax.swing.JLabel();
        cmbOrigem = new javax.swing.JComboBox();
        lblCST_CSON = new javax.swing.JLabel();
        txtCST_CSON = new javax.swing.JTextField();
        lblTributacao = new javax.swing.JLabel();
        cmbTributacao = new javax.swing.JComboBox();
        lblICMS = new javax.swing.JLabel();
        txtICMS = new javax.swing.JFormattedTextField();
        lblISSQN = new javax.swing.JLabel();
        txtISSQN = new javax.swing.JFormattedTextField();
        lblIAT = new javax.swing.JLabel();
        cmbIAT = new javax.swing.JComboBox();
        lblIPPT = new javax.swing.JLabel();
        cmbIPPT = new javax.swing.JComboBox();
        lblTipo = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox();
        chkAtivo = new javax.swing.JCheckBox();
        panPrecos = new javax.swing.JPanel();
        btnPrecoAdicionar = new javax.swing.JButton();
        btnPrecoRemover = new javax.swing.JButton();
        spPreco = new javax.swing.JScrollPane();
        tabPreco = new javax.swing.JTable();
        panItens = new javax.swing.JPanel();
        btnItemAdicionar = new javax.swing.JButton();
        btnItemRemover = new javax.swing.JButton();
        spItem = new javax.swing.JScrollPane();
        tabItem = new javax.swing.JTable();
        panGrades = new javax.swing.JPanel();
        btnGradeAdicionar = new javax.swing.JButton();
        btnGradeRemover = new javax.swing.JButton();
        spGrade = new javax.swing.JScrollPane();
        tabGrade = new javax.swing.JTable();
        txtLimite = new javax.swing.JFormattedTextField();
        lblLimite = new javax.swing.JLabel();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Produtos");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
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

        spProdutos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabProdutos.setAutoCreateRowSorter(true);
        tabProdutos.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "NCM", "Barra", "Descricao", "REF", "Preço", "EmbalagemID", "Embalagem", "Estoque", "Tipo", "Origem", "CST/CSON", "Tributação", "ICMS%", "ISSQN%", "IAT", "IPPT", "Cadastrado", "Alterado", "Ativo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabProdutos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabProdutos.setRowHeight(20);
        tabProdutos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabProdutos.setShowGrid(true);
        tabProdutos.setShowVerticalLines(false);
        tabProdutos.getTableHeader().setReorderingAllowed(false);
        spProdutos.setViewportView(tabProdutos);
        tabProdutos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabProdutos.getColumnModel().getColumn(0).setPreferredWidth(75);
        tabProdutos.getColumnModel().getColumn(1).setPreferredWidth(75);
        tabProdutos.getColumnModel().getColumn(2).setPreferredWidth(125);
        tabProdutos.getColumnModel().getColumn(3).setPreferredWidth(300);
        tabProdutos.getColumnModel().getColumn(4).setPreferredWidth(75);
        tabProdutos.getColumnModel().getColumn(5).setPreferredWidth(80);
        tabProdutos.getColumnModel().getColumn(5).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
        tabProdutos.getColumnModel().getColumn(6).setMinWidth(1);
        tabProdutos.getColumnModel().getColumn(6).setPreferredWidth(1);
        tabProdutos.getColumnModel().getColumn(6).setMaxWidth(1);
        tabProdutos.getColumnModel().getColumn(7).setPreferredWidth(75);
        tabProdutos.getColumnModel().getColumn(8).setPreferredWidth(80);
        tabProdutos.getColumnModel().getColumn(8).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getNumberInstance()));
        tabProdutos.getColumnModel().getColumn(9).setPreferredWidth(50);
        tabProdutos.getColumnModel().getColumn(10).setPreferredWidth(50);
        tabProdutos.getColumnModel().getColumn(11).setPreferredWidth(75);
        tabProdutos.getColumnModel().getColumn(12).setPreferredWidth(75);
        tabProdutos.getColumnModel().getColumn(13).setPreferredWidth(50);
        tabProdutos.getColumnModel().getColumn(13).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getNumberInstance()));
        tabProdutos.getColumnModel().getColumn(14).setPreferredWidth(50);
        tabProdutos.getColumnModel().getColumn(14).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getNumberInstance()));
        tabProdutos.getColumnModel().getColumn(15).setPreferredWidth(50);
        tabProdutos.getColumnModel().getColumn(16).setPreferredWidth(50);
        tabProdutos.getColumnModel().getColumn(17).setPreferredWidth(150);
        tabProdutos.getColumnModel().getColumn(18).setPreferredWidth(150);
        tabProdutos.getColumnModel().getColumn(19).setPreferredWidth(50);

        tabProd.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        panProdutos.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        lblNCM.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblNCM.setText("NCM:");

        txtNCM.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtNCM.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblBarra.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblBarra.setText("Barra:");

        txtBarra.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblDescricao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDescricao.setText("Descricção:");

        txtDescricao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblREF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblREF.setText("REF:");

        txtREF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtREF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblPreco.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblPreco.setText("Preço:");

        txtPreco.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        txtPreco.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPreco.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblEmbalagem.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblEmbalagem.setText("Embalagem:");

        cmbEmbalagem.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblEstoque.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblEstoque.setText("Estoque:");

        txtEstoque.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.####"))));
        txtEstoque.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtEstoque.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblOrigem.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblOrigem.setText("Origem:");

        cmbOrigem.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmbOrigem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0 - Nacional, exceto as indicadas nos códigos 3 a 5]", "1 - [Estrangeira - Importação direta, exceto a indicada no código 6']", "2 - [Estrangeira - Adquirida no mercado interno, exceto a indicada no código 7]", "3 - [Nacional, mercadoria ou bem com Conteúdo de Importação superior a 40% (quarenta por cento)]", "4 - [Nacional, cuja produção tenha sido feita em conformidade com os processos produtivos básicos de que tratam o Decreto-Lei]", "5 - [Nacional, mercadoria ou bem com Conteúdo de Importação inferior ou igual a 40% (quarenta por cento)]", "6 - [Estrangeira - Importação direta, sem similar nacional, constante em lista de Resolução CAMEX]", "7 - [Estrangeira - Adquirida no mercado interno, sem similar nacional, constante em lista de Resolução CAMEX]" }));

        lblCST_CSON.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCST_CSON.setText("CST/CSON:");

        txtCST_CSON.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCST_CSON.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblTributacao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblTributacao.setText("Tributação:");

        cmbTributacao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmbTributacao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "T - [Tributado pelo ICMS]", "S - [Tributado pelo ISSQN]", "F - [Substituição Tributária]", "I -  [Isento]", "N - [Não Tributado]" }));

        lblICMS.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblICMS.setText("ICMS:");

        txtICMS.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        txtICMS.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtICMS.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblISSQN.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblISSQN.setText("ISSQN:");

        txtISSQN.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        txtISSQN.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtISSQN.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblIAT.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblIAT.setText("IAT:");

        cmbIAT.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmbIAT.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A - [Arrendondamento]", "T - [Truncamento]" }));

        lblIPPT.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblIPPT.setText("IPPT:");

        cmbIPPT.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmbIPPT.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "P - [Própria]", "T - [Terceiro]" }));

        lblTipo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblTipo.setText("Tipo:");

        cmbTipo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmbTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00 - [MERCADORIA PARA REVENDA]", "01 - [MATÉRIA-PRIMA]", "02 - [EMBALAGEM]", "03 - [PRODUTO EM PROCESSO]", "04 - [PRODUTO ACABADO]", "05 - [SUBPRODUTO]", "06 - [PRODUTO INTERMEDIÁRIO]", "07 - [MATERIAL DE USO E CONSUMO]", "08 - [ATIVO IMOBILIZADO]", "09 - [SERVIÇOS]", "10 - [OUTROS INSUMOS]", "99 - [OUTROS]" }));

        chkAtivo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        chkAtivo.setText("Ativo");

        org.jdesktop.layout.GroupLayout panProdutosLayout = new org.jdesktop.layout.GroupLayout(panProdutos);
        panProdutos.setLayout(panProdutosLayout);
        panProdutosLayout.setHorizontalGroup(
            panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panProdutosLayout.createSequentialGroup()
                .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(panProdutosLayout.createSequentialGroup()
                        .add(11, 11, 11)
                        .add(lblIPPT)
                        .add(99, 99, 99)
                        .add(lblTipo))
                    .add(panProdutosLayout.createSequentialGroup()
                        .add(cmbIPPT, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 121, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(cmbTipo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(chkAtivo))
                    .add(panProdutosLayout.createSequentialGroup()
                        .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(panProdutosLayout.createSequentialGroup()
                                .add(txtEstoque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(6, 6, 6)
                                .add(cmbOrigem, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 152, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(6, 6, 6)
                                .add(txtCST_CSON, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(panProdutosLayout.createSequentialGroup()
                                .add(3, 3, 3)
                                .add(lblEstoque)
                                .add(192, 192, 192)
                                .add(lblCST_CSON, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(6, 6, 6)
                        .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblTributacao)
                            .add(cmbTributacao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(6, 6, 6)
                        .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblICMS)
                            .add(txtICMS, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(6, 6, 6)
                        .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblISSQN)
                            .add(txtISSQN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblIAT)
                            .add(cmbIAT, 0, 0, Short.MAX_VALUE)))
                    .add(panProdutosLayout.createSequentialGroup()
                        .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(panProdutosLayout.createSequentialGroup()
                                .add(6, 6, 6)
                                .add(lblNCM)
                                .add(53, 53, 53)
                                .add(lblBarra)
                                .add(92, 92, 92)
                                .add(lblDescricao))
                            .add(panProdutosLayout.createSequentialGroup()
                                .add(txtNCM, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(6, 6, 6)
                                .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(panProdutosLayout.createSequentialGroup()
                                        .add(6, 6, 6)
                                        .add(lblOrigem))
                                    .add(panProdutosLayout.createSequentialGroup()
                                        .add(txtBarra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(6, 6, 6)
                                        .add(txtDescricao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 368, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                        .add(6, 6, 6)
                        .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtREF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblREF))
                        .add(6, 6, 6)
                        .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtPreco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblPreco))
                        .add(6, 6, 6)
                        .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblEmbalagem)
                            .add(cmbEmbalagem, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(42, 42, 42))
        );
        panProdutosLayout.setVerticalGroup(
            panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panProdutosLayout.createSequentialGroup()
                .add(6, 6, 6)
                .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblNCM)
                    .add(lblBarra)
                    .add(lblDescricao)
                    .add(lblREF)
                    .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(lblEmbalagem)
                        .add(lblPreco)))
                .add(6, 6, 6)
                .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtNCM, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtBarra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtDescricao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtREF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtPreco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(panProdutosLayout.createSequentialGroup()
                        .add(1, 1, 1)
                        .add(cmbEmbalagem, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(panProdutosLayout.createSequentialGroup()
                            .add(6, 6, 6)
                            .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(lblEstoque)
                                .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(lblCST_CSON)
                                    .add(lblTributacao))))
                        .add(panProdutosLayout.createSequentialGroup()
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(lblISSQN)
                                .add(lblICMS)))
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, panProdutosLayout.createSequentialGroup()
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(lblIAT)))
                    .add(panProdutosLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblOrigem)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtEstoque, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtCST_CSON, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtICMS, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtISSQN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(panProdutosLayout.createSequentialGroup()
                        .add(1, 1, 1)
                        .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cmbOrigem, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cmbTributacao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cmbIAT, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(6, 6, 6)
                .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblIPPT)
                    .add(lblTipo))
                .add(7, 7, 7)
                .add(panProdutosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cmbIPPT, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cmbTipo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkAtivo)))
        );

        tabProd.addTab("Formulário", new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/formulario.png")), panProdutos); // NOI18N

        panPrecos.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        btnPrecoAdicionar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnPrecoAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/novo.png"))); // NOI18N
        btnPrecoAdicionar.setText("Adicionar");
        btnPrecoAdicionar.setMaximumSize(new java.awt.Dimension(100, 30));
        btnPrecoAdicionar.setMinimumSize(new java.awt.Dimension(100, 30));
        btnPrecoAdicionar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnPrecoAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrecoAdicionarActionPerformed(evt);
            }
        });
        btnPrecoAdicionar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPrecoAdicionarKeyPressed(evt);
            }
        });

        btnPrecoRemover.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnPrecoRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/excluir.png"))); // NOI18N
        btnPrecoRemover.setText("Remover");
        btnPrecoRemover.setMaximumSize(new java.awt.Dimension(100, 30));
        btnPrecoRemover.setMinimumSize(new java.awt.Dimension(100, 30));
        btnPrecoRemover.setPreferredSize(new java.awt.Dimension(100, 30));
        btnPrecoRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrecoRemoverActionPerformed(evt);
            }
        });
        btnPrecoRemover.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPrecoRemoverKeyPressed(evt);
            }
        });

        spPreco.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabPreco.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabPreco.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Embalagem", "Preço", "Barra"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabPreco.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabPreco.setRowHeight(20);
        tabPreco.setShowGrid(true);
        tabPreco.setShowVerticalLines(false);
        tabPreco.getTableHeader().setReorderingAllowed(false);
        spPreco.setViewportView(tabPreco);
        tabPreco.getColumnModel().getColumn(0).setMinWidth(1);
        tabPreco.getColumnModel().getColumn(0).setPreferredWidth(1);
        tabPreco.getColumnModel().getColumn(0).setMaxWidth(1);
        tabPreco.getColumnModel().getColumn(1).setMinWidth(100);
        tabPreco.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabPreco.getColumnModel().getColumn(1).setMaxWidth(100);
        tabPreco.getColumnModel().getColumn(2).setMinWidth(100);
        tabPreco.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabPreco.getColumnModel().getColumn(2).setMaxWidth(100);
        tabPreco.getColumnModel().getColumn(3).setPreferredWidth(150);

        org.jdesktop.layout.GroupLayout panPrecosLayout = new org.jdesktop.layout.GroupLayout(panPrecos);
        panPrecos.setLayout(panPrecosLayout);
        panPrecosLayout.setHorizontalGroup(
            panPrecosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panPrecosLayout.createSequentialGroup()
                .add(6, 6, 6)
                .add(panPrecosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panPrecosLayout.createSequentialGroup()
                        .add(btnPrecoAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(btnPrecoRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(spPreco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 855, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        panPrecosLayout.setVerticalGroup(
            panPrecosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panPrecosLayout.createSequentialGroup()
                .add(panPrecosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnPrecoAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnPrecoRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(6, 6, 6)
                .add(spPreco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        tabProd.addTab("Preços", new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/preco.png")), panPrecos); // NOI18N

        btnItemAdicionar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnItemAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/novo.png"))); // NOI18N
        btnItemAdicionar.setText("Adicionar");
        btnItemAdicionar.setMaximumSize(new java.awt.Dimension(100, 30));
        btnItemAdicionar.setMinimumSize(new java.awt.Dimension(100, 30));
        btnItemAdicionar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnItemAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemAdicionarActionPerformed(evt);
            }
        });
        btnItemAdicionar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnItemAdicionarKeyPressed(evt);
            }
        });

        btnItemRemover.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnItemRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/excluir.png"))); // NOI18N
        btnItemRemover.setText("Remover");
        btnItemRemover.setMaximumSize(new java.awt.Dimension(100, 30));
        btnItemRemover.setMinimumSize(new java.awt.Dimension(100, 30));
        btnItemRemover.setPreferredSize(new java.awt.Dimension(100, 30));
        btnItemRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemRemoverActionPerformed(evt);
            }
        });
        btnItemRemover.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnItemRemoverKeyPressed(evt);
            }
        });

        spItem.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabItem.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Cod Produto", "Produto", "Embalagem", "QTD", "Preço"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabItem.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabItem.setRowHeight(20);
        tabItem.setShowGrid(true);
        tabItem.setShowVerticalLines(false);
        tabItem.getTableHeader().setReorderingAllowed(false);
        spItem.setViewportView(tabItem);
        tabItem.getColumnModel().getColumn(0).setMinWidth(1);
        tabItem.getColumnModel().getColumn(0).setPreferredWidth(1);
        tabItem.getColumnModel().getColumn(0).setMaxWidth(1);
        tabItem.getColumnModel().getColumn(1).setMinWidth(100);
        tabItem.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabItem.getColumnModel().getColumn(1).setMaxWidth(100);
        tabItem.getColumnModel().getColumn(2).setMinWidth(400);
        tabItem.getColumnModel().getColumn(2).setPreferredWidth(400);
        tabItem.getColumnModel().getColumn(2).setMaxWidth(400);
        tabItem.getColumnModel().getColumn(3).setMinWidth(100);
        tabItem.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabItem.getColumnModel().getColumn(3).setMaxWidth(100);
        tabItem.getColumnModel().getColumn(4).setMinWidth(100);
        tabItem.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabItem.getColumnModel().getColumn(4).setMaxWidth(100);
        tabItem.getColumnModel().getColumn(5).setMinWidth(100);
        tabItem.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabItem.getColumnModel().getColumn(5).setMaxWidth(100);

        org.jdesktop.layout.GroupLayout panItensLayout = new org.jdesktop.layout.GroupLayout(panItens);
        panItens.setLayout(panItensLayout);
        panItensLayout.setHorizontalGroup(
            panItensLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panItensLayout.createSequentialGroup()
                .add(6, 6, 6)
                .add(panItensLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panItensLayout.createSequentialGroup()
                        .add(btnItemAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(btnItemRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(spItem, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 855, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        panItensLayout.setVerticalGroup(
            panItensLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panItensLayout.createSequentialGroup()
                .add(panItensLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnItemAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnItemRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(6, 6, 6)
                .add(spItem, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        tabProd.addTab("Itens", new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/padrao.png")), panItens); // NOI18N

        panGrades.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        btnGradeAdicionar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnGradeAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/novo.png"))); // NOI18N
        btnGradeAdicionar.setText("Adicionar");
        btnGradeAdicionar.setMaximumSize(new java.awt.Dimension(100, 30));
        btnGradeAdicionar.setMinimumSize(new java.awt.Dimension(100, 30));
        btnGradeAdicionar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnGradeAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGradeAdicionarActionPerformed(evt);
            }
        });
        btnGradeAdicionar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnGradeAdicionarKeyPressed(evt);
            }
        });

        btnGradeRemover.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnGradeRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/excluir.png"))); // NOI18N
        btnGradeRemover.setText("Remover");
        btnGradeRemover.setMaximumSize(new java.awt.Dimension(100, 30));
        btnGradeRemover.setMinimumSize(new java.awt.Dimension(100, 30));
        btnGradeRemover.setPreferredSize(new java.awt.Dimension(100, 30));
        btnGradeRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGradeRemoverActionPerformed(evt);
            }
        });
        btnGradeRemover.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnGradeRemoverKeyPressed(evt);
            }
        });

        spGrade.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabGrade.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabGrade.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Barra", "Tamanho", "Cor", "Opção", "Estoque"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabGrade.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabGrade.setRowHeight(20);
        tabGrade.setShowGrid(true);
        tabGrade.setShowVerticalLines(false);
        tabGrade.getTableHeader().setReorderingAllowed(false);
        spGrade.setViewportView(tabGrade);
        tabGrade.getColumnModel().getColumn(0).setMinWidth(1);
        tabGrade.getColumnModel().getColumn(0).setPreferredWidth(1);
        tabGrade.getColumnModel().getColumn(0).setMaxWidth(1);
        tabGrade.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabGrade.getColumnModel().getColumn(2).setMinWidth(100);
        tabGrade.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabGrade.getColumnModel().getColumn(2).setMaxWidth(100);
        tabGrade.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabGrade.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabGrade.getColumnModel().getColumn(5).setMinWidth(100);
        tabGrade.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabGrade.getColumnModel().getColumn(5).setMaxWidth(100);

        org.jdesktop.layout.GroupLayout panGradesLayout = new org.jdesktop.layout.GroupLayout(panGrades);
        panGrades.setLayout(panGradesLayout);
        panGradesLayout.setHorizontalGroup(
            panGradesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panGradesLayout.createSequentialGroup()
                .add(6, 6, 6)
                .add(panGradesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panGradesLayout.createSequentialGroup()
                        .add(btnGradeAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(btnGradeRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(spGrade, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 855, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        panGradesLayout.setVerticalGroup(
            panGradesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panGradesLayout.createSequentialGroup()
                .add(panGradesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnGradeAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnGradeRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(6, 6, 6)
                .add(spGrade, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        tabProd.addTab("Grades", new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/grade.png")), panGrades); // NOI18N

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
                .add(6, 6, 6)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(spProdutos)
                    .add(layout.createSequentialGroup()
                        .add(txtLimite, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(lblLimite)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 284, Short.MAX_VALUE)
                        .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(tabProd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(lblFiltro)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtFiltro)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnPesquisar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(13, 13, 13)
                        .add(lblFiltro))
                    .add(layout.createSequentialGroup()
                        .add(5, 5, 5)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(txtFiltro, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnPesquisar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(7, 7, 7)
                .add(spProdutos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 317, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabProd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 208, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtLimite, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(lblLimite))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-900)/2, (screenSize.height-662)/2, 900, 662);
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

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnExcluirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnExcluirKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            excluir();
        }
    }//GEN-LAST:event_btnExcluirKeyPressed

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        IFiltro filtro = Pesquisa.pesquisar(txtFiltro.getText().toUpperCase());
        setLista(filtro);
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void btnPesquisarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPesquisarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnPesquisarActionPerformed(null);
        }
    }//GEN-LAST:event_btnPesquisarKeyPressed

    private void txtFiltroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnPesquisarActionPerformed(null);
        }
    }//GEN-LAST:event_txtFiltroKeyPressed

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

    private void btnPrecoAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrecoAdicionarActionPerformed
        adicionarPreco();
    }//GEN-LAST:event_btnPrecoAdicionarActionPerformed

    private void btnPrecoAdicionarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPrecoAdicionarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            adicionarPreco();
        }
    }//GEN-LAST:event_btnPrecoAdicionarKeyPressed

    private void btnPrecoRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrecoRemoverActionPerformed
        removerPreco();
    }//GEN-LAST:event_btnPrecoRemoverActionPerformed

    private void btnPrecoRemoverKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPrecoRemoverKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            removerPreco();
        }
    }//GEN-LAST:event_btnPrecoRemoverKeyPressed

    private void btnItemAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemAdicionarActionPerformed
        adicionarItem();
    }//GEN-LAST:event_btnItemAdicionarActionPerformed

    private void btnItemAdicionarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnItemAdicionarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            adicionarItem();
        }
    }//GEN-LAST:event_btnItemAdicionarKeyPressed

    private void btnItemRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemRemoverActionPerformed
        removerItem();
    }//GEN-LAST:event_btnItemRemoverActionPerformed

    private void btnItemRemoverKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnItemRemoverKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            removerItem();
        }
    }//GEN-LAST:event_btnItemRemoverKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Caixa.getInstancia().setJanela(null);
    }//GEN-LAST:event_formWindowClosing

    private void btnGradeAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradeAdicionarActionPerformed
        adicionarGrade();
    }//GEN-LAST:event_btnGradeAdicionarActionPerformed

    private void btnGradeAdicionarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnGradeAdicionarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            adicionarGrade();
        }
    }//GEN-LAST:event_btnGradeAdicionarKeyPressed

    private void btnGradeRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradeRemoverActionPerformed
        removerGrade();
    }//GEN-LAST:event_btnGradeRemoverActionPerformed

    private void btnGradeRemoverKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnGradeRemoverKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            removerGrade();
        }
    }//GEN-LAST:event_btnGradeRemoverKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnGradeAdicionar;
    private javax.swing.JButton btnGradeRemover;
    private javax.swing.JButton btnItemAdicionar;
    private javax.swing.JButton btnItemRemover;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnPrecoAdicionar;
    private javax.swing.JButton btnPrecoRemover;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JCheckBox chkAtivo;
    private javax.swing.JComboBox cmbEmbalagem;
    private javax.swing.JComboBox cmbIAT;
    private javax.swing.JComboBox cmbIPPT;
    private javax.swing.JComboBox cmbOrigem;
    private javax.swing.JComboBox cmbTipo;
    private javax.swing.JComboBox cmbTributacao;
    private javax.swing.JLabel lblBarra;
    private javax.swing.JLabel lblCST_CSON;
    private javax.swing.JLabel lblDescricao;
    private javax.swing.JLabel lblEmbalagem;
    private javax.swing.JLabel lblEstoque;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JLabel lblIAT;
    private javax.swing.JLabel lblICMS;
    private javax.swing.JLabel lblIPPT;
    private javax.swing.JLabel lblISSQN;
    private javax.swing.JLabel lblLimite;
    private javax.swing.JLabel lblNCM;
    private javax.swing.JLabel lblOrigem;
    private javax.swing.JLabel lblPreco;
    private javax.swing.JLabel lblREF;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblTributacao;
    private javax.swing.JPanel panGrades;
    private javax.swing.JPanel panItens;
    private javax.swing.JPanel panPrecos;
    private javax.swing.JPanel panProdutos;
    private javax.swing.JScrollPane spGrade;
    private javax.swing.JScrollPane spItem;
    private javax.swing.JScrollPane spPreco;
    private javax.swing.JScrollPane spProdutos;
    private javax.swing.JTable tabGrade;
    private javax.swing.JTable tabItem;
    private javax.swing.JTable tabPreco;
    private javax.swing.JTabbedPane tabProd;
    private javax.swing.JTable tabProdutos;
    private javax.swing.JTextField txtBarra;
    private javax.swing.JTextField txtCST_CSON;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JFormattedTextField txtEstoque;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JFormattedTextField txtICMS;
    private javax.swing.JFormattedTextField txtISSQN;
    private javax.swing.JFormattedTextField txtLimite;
    private javax.swing.JTextField txtNCM;
    private javax.swing.JFormattedTextField txtPreco;
    private javax.swing.JTextField txtREF;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo para adicionar um novo registro.
     */
    private void novo() {
        tabProdutos.clearSelection();
        tabProd.setSelectedIndex(0);
        txtNCM.requestFocus();
    }

    /**
     * Metodo que salva um novo registro ou atualiza um existente.
     */
    private void salvar() {
        if (txtNCM.getText().equals("") || txtNCM.getText().length() < 8 || txtDescricao.getText().equals("") || txtPreco.getText().equals("") || txtEstoque.getText().equals("")
                || txtCST_CSON.getText().equals("") || txtICMS.getText().equals("") || txtISSQN.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Produtos", JOptionPane.INFORMATION_MESSAGE);
        } else {
            EntityManagerFactory emf = null;
            EntityManager em = null;

            try {
                ProdProduto prod = new ProdProduto(cod);
                prod.setProdProdutoNcm(txtNCM.getText());
                prod.setProdProdutoBarra(txtBarra.getText().equals("") ? null : txtBarra.getText());
                prod.setProdProdutoDescricao(txtDescricao.getText());
                prod.setProdProdutoReferencia(txtREF.getText());
                prod.setProdProdutoPreco(Double.valueOf(txtPreco.getText().replace(",", ".")));
                // embalagem
                String[] emb = cmbEmbalagem.getSelectedItem().toString().split(" - ");
                ProdEmbalagem embalagem = new ProdEmbalagem(Integer.valueOf(emb[0]));
                prod.setProdEmbalagem(embalagem);
                prod.setProdProdutoEstoque(Double.valueOf(txtEstoque.getText().replace(",", ".")));
                // origem
                String[] ori = cmbOrigem.getSelectedItem().toString().split(" - ");
                prod.setProdProdutoOrigem(ori[0].charAt(0));
                prod.setProdProdutoCstCson(txtCST_CSON.getText());
                // tributacao
                String[] trib = cmbTributacao.getSelectedItem().toString().split(" - ");
                prod.setProdProdutoTributacao(trib[0].charAt(0));
                prod.setProdProdutoIcms(Double.valueOf(txtICMS.getText().replace(",", ".")));
                prod.setProdProdutoIssqn(Double.valueOf(txtISSQN.getText().replace(",", ".")));
                // iat
                String[] iat = cmbIAT.getSelectedItem().toString().split(" - ");
                prod.setProdProdutoIat(iat[0].charAt(0));
                // ippt
                String[] ippt = cmbIPPT.getSelectedItem().toString().split(" - ");
                prod.setProdProdutoIppt(ippt[0].charAt(0));
                // tipo
                String[] tp = cmbTipo.getSelectedItem().toString().split(" - ");
                prod.setProdProdutoTipo(tp[0]);
                if (cod == 0) {
                    prod.setProdProdutoCadastrado(new Date());
                    prod.setProdProdutoAlterado(null);
                } else {
                    Date cadastro = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(tabProdutos.getModel().getValueAt(row, 17).toString());
                    prod.setProdProdutoCadastrado(cadastro);
                    prod.setProdProdutoAlterado(new Date());
                }
                prod.setProdProdutoAtivo(chkAtivo.isSelected());

                // salva
                service.salvar(prod);
                if (prod.getProdProdutoId() == null || prod.getProdProdutoId() == 0) {
                    prod = (ProdProduto) service.buscar(new ProdProduto(), "prodProdutoId", EBusca.MAXIMO);
                }

                // recupera uma instancia do gerenciador de entidades
                emf = Conexao.getInstancia();
                em = emf.createEntityManager();
                em.getTransaction().begin();

                // valida sub-listas
                int subListas = 0;
                List<ProdPreco> precos = new ArrayList<>();
                List<ProdComposicao> itens = new ArrayList<>();
                List<ProdGrade> grades = new ArrayList<>();
                if (validaPrecos(precos, prod) && validaItens(itens, prod) && validarGrades(grades, prod)) {
                    if (!precos.isEmpty()) {
                        subListas++;
                    }
                    if (!itens.isEmpty()) {
                        subListas++;
                    }
                    if (!grades.isEmpty()) {
                        subListas++;
                    }
                    if (subListas > 1) {
                        em.getTransaction().rollback();
                        JOptionPane.showMessageDialog(this, "O produto somente pode ter um das 3 sub listas.", "Produtos", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // precos
                        salvarPrecos(em, prod, precos);
                        // itens
                        salvarItens(em, prod, itens);
                        // grades
                        salvarGrades(em, prod, grades);
                        em.getTransaction().commit();
                        JOptionPane.showMessageDialog(this, "Registro salvo com sucesso.", "Produtos", JOptionPane.INFORMATION_MESSAGE);
                        btnPesquisarActionPerformed(null);
                    }
                } else {
                    em.getTransaction().rollback();
                }
            } catch (Exception ex) {
                if (em != null && em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                log.error("Erro ao salvar o produto.", ex);
                JOptionPane.showMessageDialog(this, "Não foi possível salvar o registro!", "Produtos", JOptionPane.WARNING_MESSAGE);
            } finally {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que valida os precos do produto
     *
     * @param precos a lista de precos
     * @param prod o produto atual
     * @return verdadeiro se valido, falso caso contrario
     */
    private boolean validaPrecos(List<ProdPreco> precos, ProdProduto prod) {
        try {
            for (int i = 0; i < dtmPreco.getRowCount(); i++) {
                ProdPreco pp = new ProdPreco();
                pp.setProdProduto(new ProdProduto(prod.getId()));
                String[] emb = dtmPreco.getValueAt(i, 1).toString().split(" - ");
                pp.setProdEmbalagem(new ProdEmbalagem(Integer.valueOf(emb[0])));
                pp.setProdPrecoValor(Double.valueOf(dtmPreco.getValueAt(i, 2).toString()));
                pp.setProdPrecoBarra(dtmPreco.getValueAt(i, 3) == null || dtmPreco.getValueAt(i, 3).toString().equals("") ? null : dtmPreco.getValueAt(i, 3).toString());
                precos.add(pp);
            }
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Problemas com os dados de preços!", "Preços", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    /**
     * Metodo que salva os precos
     *
     * @param em a transacao ativa
     * @param prod o produto ativo
     * @param precos a lista de dados
     * @throws OpenPdvException caso ocorra alguma exececao
     */
    private void salvarPrecos(EntityManager em, ProdProduto prod, List<ProdPreco> precos) throws OpenPdvException {
        // deleta
        if (cod > 0) {
            FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, prod);
            Sql sql = new Sql(new ProdPreco(), EComandoSQL.EXCLUIR, fo);
            service.executar(em, sql);
        }

        // insere
        service.salvar(em, precos);
    }

    /**
     * Metodo que valida os itens do produto
     *
     * @param itens a lista de dados
     * @param prod o produto atual.
     * @return verdadeiro se valido, falso caso contrario
     */
    private boolean validaItens(List<ProdComposicao> itens, ProdProduto prod) {
        boolean resp = true;

        try {
            double total = 0.00;
            for (int i = 0; i < dtmItem.getRowCount(); i++) {
                ProdComposicao comp = new ProdComposicao();
                comp.setProdProdutoPrincipal(new ProdProduto(prod.getId()));
                comp.setProdProduto(new ProdProduto(Integer.valueOf(dtmItem.getValueAt(i, 1).toString())));
                String[] emb = dtmItem.getValueAt(i, 3).toString().split(" - ");
                comp.setProdEmbalagem(new ProdEmbalagem(Integer.valueOf(emb[0])));
                comp.setProdComposicaoQuantidade(Double.valueOf(dtmItem.getValueAt(i, 4).toString()));
                comp.setProdComposicaoValor(Double.valueOf(dtmItem.getValueAt(i, 5).toString()));
                itens.add(comp);
                total += comp.getProdComposicaoValor();
                // valida se o produto e o mesmo do principal
                if (prod.getId() == comp.getProdProduto().getId()) {
                    JOptionPane.showMessageDialog(this, "Os itens deste produto não pode ser o mesmo produto.", "Itens", JOptionPane.WARNING_MESSAGE);
                    resp = false;
                    break;
                }
            }
            // valida se valor igual ao do produto
            if (itens.size() > 0 && total != prod.getProdProdutoPreco()) {
                JOptionPane.showMessageDialog(this, "O total dos valores dos itens, não é igual ao valor do produto.", "Itens", JOptionPane.WARNING_MESSAGE);
                resp = false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Problemas com os dados de itens!", "Itens", JOptionPane.WARNING_MESSAGE);
            resp = false;
        }

        return resp;
    }

    /**
     * Metodo que salva os itens
     *
     * @param em a transacao ativa
     * @param prod o produto ativo
     * @param itens a lista de dados
     * @throws OpenPdvException caso ocorra alguma exececao
     */
    private void salvarItens(EntityManager em, ProdProduto prod, List<ProdComposicao> itens) throws OpenPdvException {
        // deleta
        if (cod > 0) {
            FiltroObjeto fo = new FiltroObjeto("prodProdutoPrincipal", ECompara.IGUAL, prod);
            Sql sql = new Sql(new ProdComposicao(), EComandoSQL.EXCLUIR, fo);
            service.executar(em, sql);
        }

        // insere
        service.salvar(em, itens);
    }

    /**
     * Metodo que valida as grades do produto
     *
     * @param grades a lista de dados
     * @param prod o produto atual.
     * @return verdadeiro se valido, falso caso contrario
     */
    private boolean validarGrades(List<ProdGrade> grades, ProdProduto prod) {
        boolean resp = true;
        try {
            double estoque = 0.00;
            for (int i = 0; i < dtmGrade.getRowCount(); i++) {
                ProdGrade grade = new ProdGrade();
                grade.setProdProduto(new ProdProduto(prod.getId()));
                grade.setProdGradeBarra(dtmGrade.getValueAt(i, 1) == null || dtmGrade.getValueAt(i, 1).toString().equals("") ? null : dtmGrade.getValueAt(i, 1).toString());
                grade.setProdGradeTamanho(dtmGrade.getValueAt(i, 2).toString());
                grade.setProdGradeCor(dtmGrade.getValueAt(i, 3).toString());
                grade.setProdGradeOpcao(dtmGrade.getValueAt(i, 4).toString());
                grade.setProdGradeEstoque(Double.valueOf(dtmGrade.getValueAt(i, 5).toString()));
                grades.add(grade);
                estoque += grade.getProdGradeEstoque();
            }
            // valida se estoque igual ao do produto
            if (grades.size() > 0 && estoque != prod.getProdProdutoEstoque()) {
                JOptionPane.showMessageDialog(this, "O total do estoque da grade, não é igual ao estoque do produto.", "Grades", JOptionPane.WARNING_MESSAGE);
                resp = false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Problemas com os dados de grades!", "Grades", JOptionPane.WARNING_MESSAGE);
            resp = false;
        }

        return resp;
    }

    /**
     * Metodo que salva as grades
     *
     * @param em a transacao ativa
     * @param prod o produto ativo
     * @param grades a lista de dados
     * @throws OpenPdvException caso ocorra alguma exececao
     */
    private void salvarGrades(EntityManager em, ProdProduto prod, List<ProdGrade> grades) throws OpenPdvException {
        // deleta
        if (cod > 0) {
            FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, prod);
            Sql sql = new Sql(new ProdGrade(), EComandoSQL.EXCLUIR, fo);
            service.executar(em, sql);
        }

        // insere
        service.salvar(em, grades);
    }

    /**
     * Metodo que exclui um registro do sistema.
     */
    private void excluir() {
        if (cod > 0) {
            int escolha = JOptionPane.showOptionDialog(this, "Deseja remover o registro selecionado?", "Produtos",
                    JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
            if (escolha == 0) {
                try {
                    service.deletar(new ProdProduto(cod));
                    tabProdutos.clearSelection();
                    btnPesquisarActionPerformed(null);
                } catch (OpenPdvException ex) {
                    log.debug("Erro ao excluir o produto -> " + cod, ex);
                    JOptionPane.showMessageDialog(this, "Este registro não pode ser excluído!", "Produtos", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um registro na listagem.", "Produtos", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo que seta os valores da tabela vindas do banco de dados.
     */
    private void setLista(IFiltro filtro) {
        try {
            int limite = Integer.valueOf(txtLimite.getText());
            List<ProdProduto> lista = service.selecionar(new ProdProduto(), 0, limite, filtro);
            while (dtmProduto.getRowCount() > 0) {
                dtmProduto.removeRow(0);
            }

            for (ProdProduto prod : lista) {
                String cadastrado = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(prod.getProdProdutoCadastrado());
                String alterado = prod.getProdProdutoAlterado() == null ? "" : new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(prod.getProdProdutoAlterado());
                String barra = prod.getProdProdutoBarra() == null ? "" : prod.getProdProdutoBarra();

                Object[] obj = new Object[]{prod.getId(), prod.getProdProdutoNcm(), barra, prod.getProdProdutoDescricao(), prod.getProdProdutoReferencia(),
                    prod.getProdProdutoPreco(), prod.getProdEmbalagem().getId(), prod.getProdEmbalagem().getProdEmbalagemNome(), prod.getProdProdutoEstoque(), prod.getProdProdutoTipo(), "" + prod.getProdProdutoOrigem(),
                    prod.getProdProdutoCstCson(), "" + prod.getProdProdutoTributacao(), prod.getProdProdutoIcms(), prod.getProdProdutoIssqn(), "" + prod.getProdProdutoIat(), "" + prod.getProdProdutoIppt(),
                    cadastrado, alterado, prod.getProdProdutoAtivo()};
                dtmProduto.addRow(obj);
            }

            row = -1;
            setDados();
        } catch (OpenPdvException ex) {
            log.error("Erro ao selecionar os produtos do sistema", ex);
        }
    }

    /**
     * Metodo que seta os valores nos campos do formulario.
     */
    private void setDados() {
        // removendo os precos
        while (dtmPreco.getRowCount() > 0) {
            dtmPreco.removeRow(0);
        }
        // removendo os itens
        while (dtmItem.getRowCount() > 0) {
            dtmItem.removeRow(0);
        }
        // removendo as grades
        while (dtmGrade.getRowCount() > 0) {
            dtmGrade.removeRow(0);
        }

        if (row == -1) {
            cod = 0;
            txtNCM.setText("");
            txtBarra.setText("");
            txtDescricao.setText("");
            txtREF.setText("");
            txtPreco.setText("0,00");
            cmbEmbalagem.setSelectedIndex(0);
            txtEstoque.setText("0");
            cmbOrigem.setSelectedIndex(0);
            txtCST_CSON.setText("");
            cmbTributacao.setSelectedIndex(0);
            txtICMS.setText("0,00");
            txtISSQN.setText("0,00");
            cmbIAT.setSelectedIndex(0);
            cmbIPPT.setSelectedIndex(0);
            cmbTipo.setSelectedIndex(0);
            chkAtivo.setSelected(true);
        } else {
            int rowModel = tabProdutos.convertRowIndexToModel(row);
            cod = Integer.valueOf(tabProdutos.getModel().getValueAt(rowModel, 0).toString());
            txtNCM.setText(tabProdutos.getModel().getValueAt(rowModel, 1).toString());
            txtBarra.setText(tabProdutos.getModel().getValueAt(rowModel, 2).toString());
            txtDescricao.setText(tabProdutos.getModel().getValueAt(rowModel, 3).toString());
            txtREF.setText(tabProdutos.getModel().getValueAt(rowModel, 4).toString());
            txtPreco.setText(tabProdutos.getModel().getValueAt(rowModel, 5).toString().replace(".", ","));
            Util.selecionarCombo(cmbEmbalagem, tabProdutos.getModel().getValueAt(rowModel, 6).toString());
            txtEstoque.setText(tabProdutos.getModel().getValueAt(rowModel, 8).toString().replace(".", ","));
            Util.selecionarCombo(cmbTipo, tabProdutos.getModel().getValueAt(rowModel, 9).toString());
            Util.selecionarCombo(cmbOrigem, tabProdutos.getModel().getValueAt(rowModel, 10).toString());
            txtCST_CSON.setText(tabProdutos.getModel().getValueAt(rowModel, 11).toString());
            Util.selecionarCombo(cmbTributacao, tabProdutos.getModel().getValueAt(rowModel, 12).toString());
            txtICMS.setText(tabProdutos.getModel().getValueAt(rowModel, 13).toString().replace(".", ","));
            txtISSQN.setText(tabProdutos.getModel().getValueAt(rowModel, 14).toString().replace(".", ","));
            Util.selecionarCombo(cmbIAT, tabProdutos.getModel().getValueAt(rowModel, 15).toString());
            Util.selecionarCombo(cmbIPPT, tabProdutos.getModel().getValueAt(rowModel, 16).toString());
            chkAtivo.setSelected(Boolean.valueOf(tabProdutos.getModel().getValueAt(rowModel, 19).toString()));

            try {
                // recupera o produto para colocar os precos e itens
                ProdProduto prod = new ProdProduto();
                FiltroNumero fn = new FiltroNumero("prodProdutoId", ECompara.IGUAL, cod);
                prod = (ProdProduto) service.selecionar(prod, fn);
                for (ProdPreco preco : prod.getProdPrecos()) {
                    Object[] obj = new Object[]{preco.getId(), preco.getProdEmbalagem().getId() + " - " + preco.getProdEmbalagem().getProdEmbalagemNome(),
                        preco.getProdPrecoValor(), preco.getProdPrecoBarra()};
                    dtmPreco.addRow(obj);
                }
                for (ProdComposicao item : prod.getProdComposicoes()) {
                    Object[] obj = new Object[]{item.getId(), item.getProdProduto().getId(), item.getProdProduto().getProdProdutoDescricao(),
                        item.getProdEmbalagem().getId() + " - " + item.getProdEmbalagem().getProdEmbalagemNome(), item.getProdComposicaoQuantidade(), item.getProdComposicaoValor()};
                    dtmItem.addRow(obj);
                }
                for (ProdGrade grade : prod.getProdGrades()) {
                    Object[] obj = new Object[]{grade.getId(), grade.getProdGradeBarra(), grade.getProdGradeTamanho(), grade.getProdGradeCor(), grade.getProdGradeOpcao(), grade.getProdGradeEstoque()};
                    dtmGrade.addRow(obj);
                }
            } catch (OpenPdvException ex) {
                log.error("Erro ao selecionar o produto do sistema", ex);
            }
        }
    }

    /**
     * Metodo que carrega os valores das embalagens.
     */
    private void setEmbalagens() {
        cmbEmbalagem.removeAllItems();
        JComboBox embalagem = new JComboBox();

        try {
            List<ProdEmbalagem> lista = service.selecionar(new ProdEmbalagem(), 0, 0, null);
            for (ProdEmbalagem emb : lista) {
                cmbEmbalagem.addItem(emb.getId() + " - [" + emb.getProdEmbalagemNome() + "]");
                embalagem.addItem(emb.getId() + " - [" + emb.getProdEmbalagemNome() + "]");
            }
        } catch (OpenPdvException ex) {
            log.error("Nao carregou as embalagens.", ex);
        } finally {
            tabPreco.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(embalagem));
            tabItem.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(embalagem));
        }
    }

    /**
     * Metodo que carrega os valores dos tipo de grades.
     */
    private void setTipos() {
        JComboBox tam = new JComboBox();
        JComboBox cor = new JComboBox();
        JComboBox opc = new JComboBox();

        try {
            List<ProdGradeTipo> lista = service.selecionar(new ProdGradeTipo(), 0, 0, null);
            for (ProdGradeTipo tipo : lista) {
                if (tipo.getProdGradeTipoOpcao() == 'T') {
                    tam.addItem(tipo.getProdGradeTipoNome());
                } else if (tipo.getProdGradeTipoOpcao() == 'C') {
                    cor.addItem(tipo.getProdGradeTipoNome());
                } else {
                    opc.addItem(tipo.getProdGradeTipoNome());
                }
            }
        } catch (OpenPdvException ex) {
            log.error("Nao carregou os tipos.", ex);
        } finally {
            tabGrade.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(tam));
            tabGrade.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(cor));
            tabGrade.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(opc));
        }
    }

    /**
     * Adicionar uma linha ao preco.
     */
    private void adicionarPreco() {
        dtmPreco.addRow(new Object[]{0, cmbEmbalagem.getItemAt(0), 0.00, ""});
    }

    /**
     * Remove a linha selecionada do preco.
     */
    private void removerPreco() {
        int rowPreco = tabPreco.getSelectedRow();
        if (rowPreco >= 0) {
            dtmPreco.removeRow(rowPreco);
        }
    }

    /**
     * Adicionar uma linha ao item.
     */
    private void adicionarItem() {
        dtmItem.addRow(new Object[]{0, 0, "", cmbEmbalagem.getItemAt(0), 0, 0.00});
    }

    /**
     * Remove a linha selecionada do item.
     */
    private void removerItem() {
        int rowItem = tabItem.getSelectedRow();
        if (rowItem >= 0) {
            dtmItem.removeRow(rowItem);
        }
    }

    /**
     * Adicionar uma linha a grade.
     */
    private void adicionarGrade() {
        dtmGrade.addRow(new Object[]{0, "", "", "", "", 0.00});
    }

    /**
     * Remove a linha selecionada da grade.
     */
    private void removerGrade() {
        int rowGrade = tabGrade.getSelectedRow();
        if (rowGrade >= 0) {
            dtmGrade.removeRow(rowGrade);
        }
    }

    // GETs e SETs
    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

    public JButton getBtnExcluir() {
        return btnExcluir;
    }

    public void setBtnExcluir(JButton btnExcluir) {
        this.btnExcluir = btnExcluir;
    }

    public JButton getBtnItemAdicionar() {
        return btnItemAdicionar;
    }

    public void setBtnItemAdicionar(JButton btnItemAdicionar) {
        this.btnItemAdicionar = btnItemAdicionar;
    }

    public JButton getBtnItemRemover() {
        return btnItemRemover;
    }

    public void setBtnItemRemover(JButton btnItemRemover) {
        this.btnItemRemover = btnItemRemover;
    }

    public JButton getBtnNovo() {
        return btnNovo;
    }

    public void setBtnNovo(JButton btnNovo) {
        this.btnNovo = btnNovo;
    }

    public JButton getBtnPesquisar() {
        return btnPesquisar;
    }

    public void setBtnPesquisar(JButton btnPesquisar) {
        this.btnPesquisar = btnPesquisar;
    }

    public JButton getBtnPrecoAdicionar() {
        return btnPrecoAdicionar;
    }

    public void setBtnPrecoAdicionar(JButton btnPrecoAdicionar) {
        this.btnPrecoAdicionar = btnPrecoAdicionar;
    }

    public JButton getBtnPrecoRemover() {
        return btnPrecoRemover;
    }

    public void setBtnPrecoRemover(JButton btnPrecoRemover) {
        this.btnPrecoRemover = btnPrecoRemover;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public void setBtnSalvar(JButton btnSalvar) {
        this.btnSalvar = btnSalvar;
    }

    public JCheckBox getChkAtivo() {
        return chkAtivo;
    }

    public void setChkAtivo(JCheckBox chkAtivo) {
        this.chkAtivo = chkAtivo;
    }

    public JComboBox getCmbEmbalagem() {
        return cmbEmbalagem;
    }

    public void setCmbEmbalagem(JComboBox cmbEmbalagem) {
        this.cmbEmbalagem = cmbEmbalagem;
    }

    public JComboBox getCmbIAT() {
        return cmbIAT;
    }

    public void setCmbIAT(JComboBox cmbIAT) {
        this.cmbIAT = cmbIAT;
    }

    public JComboBox getCmbIPPT() {
        return cmbIPPT;
    }

    public void setCmbIPPT(JComboBox cmbIPPT) {
        this.cmbIPPT = cmbIPPT;
    }

    public JComboBox getCmbOrigem() {
        return cmbOrigem;
    }

    public void setCmbOrigem(JComboBox cmbOrigem) {
        this.cmbOrigem = cmbOrigem;
    }

    public JComboBox getCmbTipo() {
        return cmbTipo;
    }

    public void setCmbTipo(JComboBox cmbTipo) {
        this.cmbTipo = cmbTipo;
    }

    public JComboBox getCmbTributacao() {
        return cmbTributacao;
    }

    public void setCmbTributacao(JComboBox cmbTributacao) {
        this.cmbTributacao = cmbTributacao;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public DefaultTableModel getDtmItem() {
        return dtmItem;
    }

    public void setDtmItem(DefaultTableModel dtmItem) {
        this.dtmItem = dtmItem;
    }

    public DefaultTableModel getDtmPreco() {
        return dtmPreco;
    }

    public void setDtmPreco(DefaultTableModel dtmPreco) {
        this.dtmPreco = dtmPreco;
    }

    public DefaultTableModel getDtmProduto() {
        return dtmProduto;
    }

    public void setDtmProduto(DefaultTableModel dtmProduto) {
        this.dtmProduto = dtmProduto;
    }

    public JLabel getLblBarra() {
        return lblBarra;
    }

    public void setLblBarra(JLabel lblBarra) {
        this.lblBarra = lblBarra;
    }

    public JLabel getLblCST_CSON() {
        return lblCST_CSON;
    }

    public void setLblCST_CSON(JLabel lblCST_CSON) {
        this.lblCST_CSON = lblCST_CSON;
    }

    public JLabel getLblDescricao() {
        return lblDescricao;
    }

    public void setLblDescricao(JLabel lblDescricao) {
        this.lblDescricao = lblDescricao;
    }

    public JLabel getLblEmbalagem() {
        return lblEmbalagem;
    }

    public void setLblEmbalagem(JLabel lblEmbalagem) {
        this.lblEmbalagem = lblEmbalagem;
    }

    public JLabel getLblEstoque() {
        return lblEstoque;
    }

    public void setLblEstoque(JLabel lblEstoque) {
        this.lblEstoque = lblEstoque;
    }

    public JLabel getLblFiltro() {
        return lblFiltro;
    }

    public void setLblFiltro(JLabel lblFiltro) {
        this.lblFiltro = lblFiltro;
    }

    public JLabel getLblIAT() {
        return lblIAT;
    }

    public void setLblIAT(JLabel lblIAT) {
        this.lblIAT = lblIAT;
    }

    public JLabel getLblICMS() {
        return lblICMS;
    }

    public void setLblICMS(JLabel lblICMS) {
        this.lblICMS = lblICMS;
    }

    public JLabel getLblIPPT() {
        return lblIPPT;
    }

    public void setLblIPPT(JLabel lblIPPT) {
        this.lblIPPT = lblIPPT;
    }

    public JLabel getLblISSQN() {
        return lblISSQN;
    }

    public void setLblISSQN(JLabel lblISSQN) {
        this.lblISSQN = lblISSQN;
    }

    public JLabel getLblLimite() {
        return lblLimite;
    }

    public void setLblLimite(JLabel lblLimite) {
        this.lblLimite = lblLimite;
    }

    public JLabel getLblNCM() {
        return lblNCM;
    }

    public void setLblNCM(JLabel lblNCM) {
        this.lblNCM = lblNCM;
    }

    public JLabel getLblOrigem() {
        return lblOrigem;
    }

    public void setLblOrigem(JLabel lblOrigem) {
        this.lblOrigem = lblOrigem;
    }

    public JLabel getLblPreco() {
        return lblPreco;
    }

    public void setLblPreco(JLabel lblPreco) {
        this.lblPreco = lblPreco;
    }

    public JLabel getLblREF() {
        return lblREF;
    }

    public void setLblREF(JLabel lblREF) {
        this.lblREF = lblREF;
    }

    public JLabel getLblTipo() {
        return lblTipo;
    }

    public void setLblTipo(JLabel lblTipo) {
        this.lblTipo = lblTipo;
    }

    public JLabel getLblTributacao() {
        return lblTributacao;
    }

    public void setLblTributacao(JLabel lblTributacao) {
        this.lblTributacao = lblTributacao;
    }

    public JPanel getPanItens() {
        return panItens;
    }

    public void setPanItens(JPanel panItens) {
        this.panItens = panItens;
    }

    public JPanel getPanPrecos() {
        return panPrecos;
    }

    public void setPanPrecos(JPanel panPrecos) {
        this.panPrecos = panPrecos;
    }

    public JPanel getPanProdutos() {
        return panProdutos;
    }

    public void setPanProdutos(JPanel panProdutos) {
        this.panProdutos = panProdutos;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public CoreService getService() {
        return service;
    }

    public void setService(CoreService service) {
        this.service = service;
    }

    public JScrollPane getSpItem() {
        return spItem;
    }

    public void setSpItem(JScrollPane spItem) {
        this.spItem = spItem;
    }

    public JScrollPane getSpPreco() {
        return spPreco;
    }

    public void setSpPreco(JScrollPane spPreco) {
        this.spPreco = spPreco;
    }

    public JScrollPane getSpProdutos() {
        return spProdutos;
    }

    public void setSpProdutos(JScrollPane spProdutos) {
        this.spProdutos = spProdutos;
    }

    public JTable getTabItem() {
        return tabItem;
    }

    public void setTabItem(JTable tabItem) {
        this.tabItem = tabItem;
    }

    public JTable getTabPreco() {
        return tabPreco;
    }

    public void setTabPreco(JTable tabPreco) {
        this.tabPreco = tabPreco;
    }

    public JTabbedPane getTabProd() {
        return tabProd;
    }

    public void setTabProd(JTabbedPane tabProd) {
        this.tabProd = tabProd;
    }

    public JTable getTabProdutos() {
        return tabProdutos;
    }

    public void setTabProdutos(JTable tabProdutos) {
        this.tabProdutos = tabProdutos;
    }

    public JTextField getTxtBarra() {
        return txtBarra;
    }

    public void setTxtBarra(JTextField txtBarra) {
        this.txtBarra = txtBarra;
    }

    public JTextField getTxtCST_CSON() {
        return txtCST_CSON;
    }

    public void setTxtCST_CSON(JTextField txtCST_CSON) {
        this.txtCST_CSON = txtCST_CSON;
    }

    public JTextField getTxtDescricao() {
        return txtDescricao;
    }

    public void setTxtDescricao(JTextField txtDescricao) {
        this.txtDescricao = txtDescricao;
    }

    public JFormattedTextField getTxtEstoque() {
        return txtEstoque;
    }

    public void setTxtEstoque(JFormattedTextField txtEstoque) {
        this.txtEstoque = txtEstoque;
    }

    public JTextField getTxtFiltro() {
        return txtFiltro;
    }

    public void setTxtFiltro(JTextField txtFiltro) {
        this.txtFiltro = txtFiltro;
    }

    public JFormattedTextField getTxtICMS() {
        return txtICMS;
    }

    public void setTxtICMS(JFormattedTextField txtICMS) {
        this.txtICMS = txtICMS;
    }

    public JFormattedTextField getTxtISSQN() {
        return txtISSQN;
    }

    public void setTxtISSQN(JFormattedTextField txtISSQN) {
        this.txtISSQN = txtISSQN;
    }

    public JFormattedTextField getTxtLimite() {
        return txtLimite;
    }

    public void setTxtLimite(JFormattedTextField txtLimite) {
        this.txtLimite = txtLimite;
    }

    public JTextField getTxtNCM() {
        return txtNCM;
    }

    public void setTxtNCM(JTextField txtNCM) {
        this.txtNCM = txtNCM;
    }

    public JFormattedTextField getTxtPreco() {
        return txtPreco;
    }

    public void setTxtPreco(JFormattedTextField txtPreco) {
        this.txtPreco = txtPreco;
    }

    public JTextField getTxtREF() {
        return txtREF;
    }

    public void setTxtREF(JTextField txtREF) {
        this.txtREF = txtREF;
    }

    public DefaultTableModel getDtmGrade() {
        return dtmGrade;
    }

    public void setDtmGrade(DefaultTableModel dtmGrade) {
        this.dtmGrade = dtmGrade;
    }

    public JButton getBtnGradeAdicionar() {
        return btnGradeAdicionar;
    }

    public void setBtnGradeAdicionar(JButton btnGradeAdicionar) {
        this.btnGradeAdicionar = btnGradeAdicionar;
    }

    public JButton getBtnGradeRemover() {
        return btnGradeRemover;
    }

    public void setBtnGradeRemover(JButton btnGradeRemover) {
        this.btnGradeRemover = btnGradeRemover;
    }

    public JPanel getPanGrades() {
        return panGrades;
    }

    public void setPanGrades(JPanel panGrades) {
        this.panGrades = panGrades;
    }

    public JScrollPane getSpGrade() {
        return spGrade;
    }

    public void setSpGrade(JScrollPane spGrade) {
        this.spGrade = spGrade;
    }

    public JTable getTabGrade() {
        return tabGrade;
    }

    public void setTabGrade(JTable tabGrade) {
        this.tabGrade = tabGrade;
    }
}
