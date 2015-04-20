package br.com.openpdv.visao.principal;

import br.com.openpdv.controlador.core.*;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroBinario;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.ecf.EcfTroca;
import br.com.openpdv.modelo.ecf.EcfTrocaProduto;
import br.com.openpdv.modelo.produto.ProdComposicao;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdGrade;
import br.com.openpdv.modelo.produto.ProdPreco;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.visao.core.Caixa;
import br.com.openpdv.visao.venda.Grades;
import br.com.openpdv.visao.venda.Precos;
import br.com.phdss.Util;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;

/**
 * Classe que representa a listagem das notas de consumidor do sistema.
 *
 * @author Pedro H. Lira
 */
public class Trocas extends javax.swing.JDialog {

    private static Trocas trocas;
    private Logger log;
    private int row;
    private DefaultTableModel dtmTroca;
    private DefaultTableModel dtmProdutos;
    private CoreService service;
    private EcfTroca selecionado;
    private AsyncCallback<EcfTroca> async;
    /**
     * Variavel que responde de modo assincrono a pesquisa de produto.
     */
    private AsyncCallback<ProdProduto> pesquisado = new AsyncCallback<ProdProduto>() {
        @Override
        public void sucesso(final ProdProduto prod) {
            if (prod == null) {
                JOptionPane.showMessageDialog(trocas, "Produto não encontrado.", "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (!prod.getProdPrecos().isEmpty()) {
                    Precos.getInstancia(new AsyncDoubleBack<ProdProduto, ProdPreco>() {
                        @Override
                        public void sucesso(ProdProduto produto, ProdPreco preco) {
                            try {
                                prod.setProdEmbalagem(preco.getProdEmbalagem());
                                prod.setProdProdutoPreco(preco.getProdPrecoValor());
                                prod.setProdProdutoBarra(preco.getProdPrecoBarra());
                                adicionar(produto, 1.00);
                            } catch (OpenPdvException ex) {
                                erro(ex);
                            }
                        }

                        @Override
                        public void falha(Exception excecao) {
                            erro(excecao);
                        }
                    }, prod).setVisible(true);
                } else if (!prod.getProdComposicoes().isEmpty()) {
                    // percorre os itens do produto
                    for (ProdComposicao comp : prod.getProdComposicoes()) {
                        ProdProduto item = comp.getProdProduto();
                        item.setProdEmbalagem(comp.getProdEmbalagem());
                        item.setProdProdutoPreco(comp.getProdComposicaoValor() / comp.getProdComposicaoQuantidade());

                        try {
                            adicionar(item, comp.getProdComposicaoQuantidade());
                        } catch (OpenPdvException ex) {
                            erro(ex);
                            break;
                        }
                    }
                } else if (!prod.getProdGrades().isEmpty()) {
                    Grades.getInstancia(new AsyncDoubleBack<ProdProduto, ProdGrade>() {
                        @Override
                        public void sucesso(ProdProduto produto, ProdGrade grade) {
                            try {
                                prod.setProdProdutoBarra(grade.getProdGradeBarra());
                                adicionar(prod, 1.00);
                            } catch (OpenPdvException ex) {
                                erro(ex);
                            }
                        }

                        @Override
                        public void falha(Exception excecao) {
                            erro(excecao);
                        }
                    }, prod).setVisible(true);
                } else {
                    try {
                        adicionar(prod, 1.00);
                    } catch (OpenPdvException ex) {
                        erro(ex);
                    }
                }
            }
            btnAdicionar.requestFocus();
        }

        @Override
        public void falha(Exception excecao) {
            erro(excecao);
        }

        private void adicionar(ProdProduto prod, double qtd) throws OpenPdvException {
            Object[] obj = new Object[]{0, prod, prod.getProdProdutoDescricao(), prod.getProdEmbalagem().getId() + " - " + prod.getProdEmbalagem().getProdEmbalagemNome(),
                qtd, prod.getProdProdutoPreco(), prod.getProdProdutoPreco(), dtmProdutos.getRowCount() + 1};
            dtmProdutos.addRow(obj);
            totalizar();
        }

        private void erro(Exception ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(trocas, "Não foi possível adicionar o produto!", "Pesquisa", JOptionPane.WARNING_MESSAGE);
            btnAdicionar.requestFocus();
        }
    };

    /**
     * Construtor padrao.
     */
    private Trocas() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Trocas.class);
        initComponents();

        service = new CoreService<>();
        dtmTroca = (DefaultTableModel) tabTrocas.getModel();
        dtmProdutos = (DefaultTableModel) tabProdutos.getModel();
        // escuta a selecao de linhas
        tabTrocas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                row = tabTrocas.getSelectedRow();
                setDados();
            }
        });
        // escuta as alteracoes de valores de desc e qtd
        dtmProdutos.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int col = e.getColumn();
                int row = e.getFirstRow();
                TableModel model = (TableModel) e.getSource();

                if (col == 4 || col == 5) {
                    Double qtd = (Double) model.getValueAt(row, 4);
                    Double valor = (Double) model.getValueAt(row, 5);
                    model.setValueAt(valor * qtd, row, 6);
                    totalizar();
                }
            }
        });

        // colocando limites nos campos
        txtCpfCnpj.setDocument(new TextFieldLimit(14, true));
        txtECF.setDocument(new TextFieldLimit(3, true));
        txtCOO.setDocument(new TextFieldLimit(6, true));
    }

    /**
     * Metodo que retorna a instancia do componente.
     *
     * @param async a acao a ser executada apos selecionar.
     * @return o objeto do componente.
     */
    public static Trocas getInstancia(AsyncCallback<EcfTroca> async) {
        if (trocas == null) {
            trocas = new Trocas();
        }

        trocas.setLista();
        trocas.async = async;
        trocas.btnSelecionar.setVisible(async != null);
        return trocas;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spTrocas = new javax.swing.JScrollPane();
        tabTrocas = new javax.swing.JTable();
        separador1 = new javax.swing.JSeparator();
        lblCpfCnpj = new javax.swing.JLabel();
        txtCpfCnpj = new javax.swing.JTextField();
        lblValor = new javax.swing.JLabel();
        txtValor = new javax.swing.JFormattedTextField();
        lblECF = new javax.swing.JLabel();
        txtECF = new javax.swing.JTextField();
        lblCOO = new javax.swing.JLabel();
        txtCOO = new javax.swing.JTextField();
        separador2 = new javax.swing.JSeparator();
        btnAdicionar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        spProdutos = new javax.swing.JScrollPane();
        tabProdutos = new javax.swing.JTable();
        btnSelecionar = new javax.swing.JButton();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnEcxluir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Trocas");
        setModal(true);
        setResizable(false);

        spTrocas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        spTrocas.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        tabTrocas.setAutoCreateRowSorter(true);
        tabTrocas.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabTrocas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Data", "CPF/CNPJ", "Valor", "ECF", "COO", "Ativo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Boolean.class
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
        tabTrocas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabTrocas.setRowHeight(20);
        tabTrocas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabTrocas.getTableHeader().setReorderingAllowed(false);
        spTrocas.setViewportView(tabTrocas);
        if (tabTrocas.getColumnModel().getColumnCount() > 0) {
            tabTrocas.getColumnModel().getColumn(0).setMinWidth(0);
            tabTrocas.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabTrocas.getColumnModel().getColumn(0).setMaxWidth(0);
            tabTrocas.getColumnModel().getColumn(1).setResizable(false);
            tabTrocas.getColumnModel().getColumn(1).setPreferredWidth(120);
            tabTrocas.getColumnModel().getColumn(2).setResizable(false);
            tabTrocas.getColumnModel().getColumn(2).setPreferredWidth(175);
            tabTrocas.getColumnModel().getColumn(3).setResizable(false);
            tabTrocas.getColumnModel().getColumn(3).setPreferredWidth(75);
            tabTrocas.getColumnModel().getColumn(3).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
            tabTrocas.getColumnModel().getColumn(4).setResizable(false);
            tabTrocas.getColumnModel().getColumn(4).setPreferredWidth(50);
            tabTrocas.getColumnModel().getColumn(5).setResizable(false);
            tabTrocas.getColumnModel().getColumn(5).setPreferredWidth(50);
            tabTrocas.getColumnModel().getColumn(6).setResizable(false);
            tabTrocas.getColumnModel().getColumn(6).setPreferredWidth(75);
        }

        lblCpfCnpj.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCpfCnpj.setText("CPF/CNPJ:");

        txtCpfCnpj.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCpfCnpj.setToolTipText("Digite o CPF ou CNPJ do cliente.");
        txtCpfCnpj.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCpfCnpjFocusLost(evt);
            }
        });

        lblValor.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblValor.setText("Valor:");

        txtValor.setEditable(false);
        txtValor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        txtValor.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtValor.setFocusable(false);
        txtValor.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblECF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblECF.setText("ECF:");

        txtECF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtECF.setToolTipText("Digite o CPF ou CNPJ do cliente.");

        lblCOO.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCOO.setText("COO:");

        txtCOO.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCOO.setToolTipText("Digite o CPF ou CNPJ do cliente.");

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

        spProdutos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        spProdutos.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        tabProdutos.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "ProdProduto", "Produto", "Embalagem", "Qtd", "Valor", "Total", "Ordem"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, false, false
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
        tabProdutos.getTableHeader().setReorderingAllowed(false);
        spProdutos.setViewportView(tabProdutos);
        if (tabProdutos.getColumnModel().getColumnCount() > 0) {
            tabProdutos.getColumnModel().getColumn(0).setMinWidth(0);
            tabProdutos.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabProdutos.getColumnModel().getColumn(0).setMaxWidth(0);
            tabProdutos.getColumnModel().getColumn(1).setMinWidth(0);
            tabProdutos.getColumnModel().getColumn(1).setPreferredWidth(0);
            tabProdutos.getColumnModel().getColumn(1).setMaxWidth(0);
            tabProdutos.getColumnModel().getColumn(2).setResizable(false);
            tabProdutos.getColumnModel().getColumn(2).setPreferredWidth(250);
            tabProdutos.getColumnModel().getColumn(3).setResizable(false);
            tabProdutos.getColumnModel().getColumn(3).setPreferredWidth(75);
            tabProdutos.getColumnModel().getColumn(4).setResizable(false);
            tabProdutos.getColumnModel().getColumn(4).setPreferredWidth(50);
            tabProdutos.getColumnModel().getColumn(4).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getNumberInstance()));
            tabProdutos.getColumnModel().getColumn(5).setResizable(false);
            tabProdutos.getColumnModel().getColumn(5).setPreferredWidth(75);
            tabProdutos.getColumnModel().getColumn(5).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
            tabProdutos.getColumnModel().getColumn(6).setResizable(false);
            tabProdutos.getColumnModel().getColumn(6).setPreferredWidth(100);
            tabProdutos.getColumnModel().getColumn(6).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
            tabProdutos.getColumnModel().getColumn(7).setMinWidth(0);
            tabProdutos.getColumnModel().getColumn(7).setPreferredWidth(0);
            tabProdutos.getColumnModel().getColumn(7).setMaxWidth(0);
        }

        btnSelecionar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnSelecionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/ok.png"))); // NOI18N
        btnSelecionar.setText("Selecionar");
        btnSelecionar.setMaximumSize(new java.awt.Dimension(100, 30));
        btnSelecionar.setMinimumSize(new java.awt.Dimension(100, 30));
        btnSelecionar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnSelecionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionarActionPerformed(evt);
            }
        });
        btnSelecionar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSelecionarKeyPressed(evt);
            }
        });

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

        btnEcxluir.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnEcxluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/excluir.png"))); // NOI18N
        btnEcxluir.setText("Excluir");
        btnEcxluir.setMaximumSize(new java.awt.Dimension(100, 30));
        btnEcxluir.setMinimumSize(new java.awt.Dimension(100, 30));
        btnEcxluir.setPreferredSize(new java.awt.Dimension(100, 30));
        btnEcxluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEcxluirActionPerformed(evt);
            }
        });
        btnEcxluir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEcxluirKeyPressed(evt);
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
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(separador2)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, spTrocas)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, separador1)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, spProdutos)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(btnSelecionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnEcxluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(btnAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(btnRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(lblCpfCnpj)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtCpfCnpj, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblValor)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtValor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblECF)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtECF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblCOO)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtCOO, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(spTrocas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblCpfCnpj)
                    .add(txtCpfCnpj, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblValor)
                    .add(txtValor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblCOO)
                    .add(txtCOO, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblECF)
                    .add(txtECF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(separador2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spProdutos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 123, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnEcxluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnSelecionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(68, 68, 68))
        );

        setSize(new java.awt.Dimension(591, 478));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEcxluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEcxluirActionPerformed
        excluir();
    }//GEN-LAST:event_btnEcxluirActionPerformed

    private void btnEcxluirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEcxluirKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            excluir();
        }
    }//GEN-LAST:event_btnEcxluirKeyPressed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        salvar();
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnSalvarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalvarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            salvar();
        }
    }//GEN-LAST:event_btnSalvarKeyPressed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        novo();
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnNovoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnNovoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            novo();
        }
    }//GEN-LAST:event_btnNovoKeyPressed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        Pesquisa.getInstancia(pesquisado).setVisible(true);
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        int rowProd = tabProdutos.getSelectedRow();
        if (rowProd >= 0) {
            dtmProdutos.removeRow(rowProd);
            totalizar();
        }
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void txtCpfCnpjFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCpfCnpjFocusLost
        if (!txtCpfCnpj.getText().equals("") && validar() == false) {
            JOptionPane.showMessageDialog(this, "CPF ou CNPJ inválidos.", "Trocas", JOptionPane.INFORMATION_MESSAGE);
            txtCpfCnpj.setText("");
            txtCpfCnpj.requestFocus();
        }
    }//GEN-LAST:event_txtCpfCnpjFocusLost

    private void btnSelecionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionarActionPerformed
        selecionar();
    }//GEN-LAST:event_btnSelecionarActionPerformed

    private void btnSelecionarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSelecionarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            selecionar();
        }
    }//GEN-LAST:event_btnSelecionarKeyPressed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cancelar();
        }
    }//GEN-LAST:event_btnCancelarKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEcxluir;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionar;
    private javax.swing.JLabel lblCOO;
    private javax.swing.JLabel lblCpfCnpj;
    private javax.swing.JLabel lblECF;
    private javax.swing.JLabel lblValor;
    private javax.swing.JSeparator separador1;
    private javax.swing.JSeparator separador2;
    private javax.swing.JScrollPane spProdutos;
    private javax.swing.JScrollPane spTrocas;
    private javax.swing.JTable tabProdutos;
    private javax.swing.JTable tabTrocas;
    private javax.swing.JTextField txtCOO;
    private javax.swing.JTextField txtCpfCnpj;
    private javax.swing.JTextField txtECF;
    private javax.swing.JFormattedTextField txtValor;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo para adicionar um novo registro.
     */
    private void novo() {
        tabTrocas.clearSelection();
        txtCpfCnpj.requestFocus();
        row = -1;
        setDados();
    }

    /**
     * Metodo que valida o CPF e/ou CNPJ
     *
     * @return retorna verdadeiro se valido, falso caso contrario.
     */
    private boolean validar() {
        String texto = txtCpfCnpj.getText();
        texto = texto.replaceAll("\\D", "");
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
     * Metodo que selecionar uma troca para ser usada na venda.
     */
    private void selecionar() {
        dispose();
        if (async != null) {
            async.sucesso(selecionado);
        }
    }

    /**
     * Metodo que salva um novo registro.
     */
    private void salvar() {
        if (txtCpfCnpj.getText().equals("") || txtECF.getText().equals("") || txtECF.getText().equals("0") || txtCOO.getText().equals("") || txtCOO.getText().equals("0") || txtValor.getText().equals("0,00")) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios e não podem ser zeros!\nAdicione também produtos com valor maior que zero.", "Trocas", JOptionPane.INFORMATION_MESSAGE);
        } else {
            EntityManagerFactory emf = null;
            EntityManager em = null;

            try {
                // recupera uma instancia do gerenciador de entidades
                emf = Conexao.getInstancia();
                em = emf.createEntityManager();
                em.getTransaction().begin();

                // salva
                EcfTroca troca = new EcfTroca();
                troca.setEcfTrocaCliente(txtCpfCnpj.getText().replaceAll("\\D", ""));
                troca.setEcfTrocaEcf(Integer.valueOf(txtECF.getText()));
                troca.setEcfTrocaCoo(Integer.valueOf(txtCOO.getText()));
                troca.setEcfTrocaData(new Date());
                troca.setEcfTrocaValor((Double) txtValor.getValue());
                troca.setEcfTrocaAtivo(false);
                troca = (EcfTroca) service.salvar(em, troca);

                // valida sub-lista
                List<EcfTrocaProduto> produtos = new ArrayList<>();
                if (validarProdutos(produtos, troca)) {
                    produtos = (List<EcfTrocaProduto>) service.salvar(em, produtos);
                    em.getTransaction().commit();
                    troca.setEcfTrocaProdutos(produtos);
                    JOptionPane.showMessageDialog(this, "Registro salvo com sucesso.", "Trocas", JOptionPane.INFORMATION_MESSAGE);
                    this.selecionado = troca;
                    selecionar();
                } else {
                    em.getTransaction().rollback();
                }
            } catch (Exception ex) {
                if (em != null && em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                log.error("Erro ao salvar a troca.", ex);
                JOptionPane.showMessageDialog(this, "Não foi possível salvar o registro!", "Trocas", JOptionPane.WARNING_MESSAGE);
            } finally {
                em.close();
                emf.close();
            }
        }
    }

    /**
     * Metodo que cancela o registro do sistema.
     */
    private void cancelar() {
        dispose();
        Caixa.getInstancia().setJanela(null);
        if (async != null) {
            async.falha(null);
        }
    }

    /**
     * Metodo que exclui a troca selecionada.
     */
    private void excluir() {
        if (selecionado != null) {
            int escolha = JOptionPane.showOptionDialog(this, "Deseja excluir a Troca selecionada?", "Trocas",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                try {
                    service.deletar(selecionado);
                    JOptionPane.showMessageDialog(this, "Registro excluido com sucesso.", "Trocas", JOptionPane.INFORMATION_MESSAGE);
                    setLista();
                } catch (OpenPdvException ex) {
                    log.error("Erro ao excluir a troca.", ex);
                    JOptionPane.showMessageDialog(this, "Não foi possível excluir o registro!", "Trocas", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um registro na listagem.", "Trocas", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo que valida os prdoutos da nota
     *
     * @param produtos a lista de produtos
     * @param troca a nota atual
     * @return verdadeiro se valido, falso caso contrario
     */
    private boolean validarProdutos(List<EcfTrocaProduto> produtos, EcfTroca troca) {
        try {
            for (int i = 0; i < dtmProdutos.getRowCount(); i++) {
                String[] emb = dtmProdutos.getValueAt(i, 3).toString().split(" - ");
                Double qtd = (Double) dtmProdutos.getValueAt(i, 4);
                Double valor = (Double) dtmProdutos.getValueAt(i, 5);
                if (qtd <= 0.00 || valor <= 0.00) {
                    throw new Exception();
                }

                EcfTrocaProduto tp = new EcfTrocaProduto();
                tp.setEcfTroca(troca);
                tp.setProdProduto((ProdProduto) dtmProdutos.getValueAt(i, 1));
                tp.setProdEmbalagem(new ProdEmbalagem(Integer.valueOf(emb[0])));
                tp.setEcfTrocaProdutoBarra(tp.getProdProduto().getProdProdutoBarra());
                tp.setEcfTrocaProdutoQuantidade(qtd);
                tp.setEcfTrocaProdutoValor(valor);
                tp.setEcfTrocaProdutoTotal((Double) dtmProdutos.getValueAt(i, 6));
                tp.setEcfTrocaProdutoOrdem((Integer) dtmProdutos.getValueAt(i, 7));
                produtos.add(tp);
            }
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Problemas com os dados do produtos!", "Trocas", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    /**
     * Metodo que seta os valores da tabela vindas do banco de dados.
     */
    private void setLista() {
        try {
            FiltroBinario fb = new FiltroBinario("ecfTrocaAtivo", ECompara.IGUAL, false);
            List<EcfTroca> lista = service.selecionar(new EcfTroca(), 0, 0, fb);
            while (dtmTroca.getRowCount() > 0) {
                dtmTroca.removeRow(0);
            }

            for (EcfTroca troca : lista) {
                String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(troca.getEcfTrocaData());
                Object[] obj = new Object[]{troca.getId(), data, troca.getEcfTrocaCliente(), troca.getEcfTrocaValor(), troca.getEcfTrocaEcf(), troca.getEcfTrocaCoo(), troca.isEcfTrocaAtivo()};
                dtmTroca.addRow(obj);
            }

            row = -1;
            setDados();
        } catch (OpenPdvException ex) {
            log.error("Erro ao selecionar as trocas do sistema", ex);
        }
    }

    /**
     * Metodo que seta os valores nos campos do formulario.
     */
    private void setDados() {
        // removendo os produtos
        while (dtmProdutos.getRowCount() > 0) {
            dtmProdutos.removeRow(0);
        }

        if (row == -1) {
            txtCpfCnpj.setText("");
            txtValor.setValue(null);
            txtECF.setText("");
            txtCOO.setText("");
            btnSelecionar.setEnabled(false);
            btnAdicionar.setEnabled(true);
            btnRemover.setEnabled(true);
            btnSalvar.setEnabled(true);
            selecionado = null;
        } else {
            int rowModel = tabTrocas.convertRowIndexToModel(row);
            int cod = Integer.valueOf(tabTrocas.getModel().getValueAt(rowModel, 0).toString());
            txtCpfCnpj.setText(tabTrocas.getModel().getValueAt(rowModel, 2).toString());
            txtValor.setValue(tabTrocas.getModel().getValueAt(rowModel, 3));
            txtECF.setText(tabTrocas.getModel().getValueAt(rowModel, 4).toString());
            txtCOO.setText(tabTrocas.getModel().getValueAt(rowModel, 5).toString());
            btnSelecionar.setEnabled(true);
            btnAdicionar.setEnabled(false);
            btnRemover.setEnabled(false);
            btnSalvar.setEnabled(false);

            try {
                // recupera o produto para colocar os dados
                FiltroNumero fn = new FiltroNumero("ecfTrocaId", ECompara.IGUAL, cod);
                selecionado = (EcfTroca) service.selecionar(new EcfTroca(), fn);
                for (EcfTrocaProduto tp : selecionado.getEcfTrocaProdutos()) {
                    Object[] obj = new Object[]{tp.getId(), tp.getProdProduto().getId(), tp.getProdProduto().getProdProdutoDescricao(),
                        tp.getProdEmbalagem().getId() + " - " + tp.getProdEmbalagem().getProdEmbalagemNome(), tp.getEcfTrocaProdutoQuantidade(),
                        tp.getEcfTrocaProdutoValor(), tp.getEcfTrocaProdutoTotal(), tp.getEcfTrocaProdutoOrdem()};
                    dtmProdutos.addRow(obj);
                }
            } catch (OpenPdvException ex) {
                log.error("Erro ao selecionar a troca do sistema", ex);
            }
        }
    }

    /**
     * Metodo que totaliza os valores dos produtos adicionados.
     */
    private void totalizar() {
        List<EcfTrocaProduto> tps = new ArrayList<>();
        validarProdutos(tps, null);

        Double valor = 0.00;
        for (EcfTrocaProduto tp : tps) {
            valor += tp.getEcfTrocaProdutoTotal();
        }

        txtValor.setValue(valor);
    }

    public JButton getBtnAdicionar() {
        return btnAdicionar;
    }

    public void setBtnAdicionar(JButton btnAdicionar) {
        this.btnAdicionar = btnAdicionar;
    }

    public JButton getBtnCancelar() {
        return btnEcxluir;
    }

    public void setBtnCancelar(JButton btnCancelar) {
        this.btnEcxluir = btnCancelar;
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

    public DefaultTableModel getDtmTroca() {
        return dtmTroca;
    }

    public void setDtmTroca(DefaultTableModel dtmTroca) {
        this.dtmTroca = dtmTroca;
    }

    public JButton getBtnEcxluir() {
        return btnEcxluir;
    }

    public void setBtnEcxluir(JButton btnEcxluir) {
        this.btnEcxluir = btnEcxluir;
    }

    public JButton getBtnSelecionar() {
        return btnSelecionar;
    }

    public void setBtnSelecionar(JButton btnSelecionar) {
        this.btnSelecionar = btnSelecionar;
    }

    public JLabel getLblCPF_CNPJ() {
        return lblCpfCnpj;
    }

    public void setLblCPF_CNPJ(JLabel lblCPF_CNPJ) {
        this.lblCpfCnpj = lblCPF_CNPJ;
    }

    public JTable getTabTrocas() {
        return tabTrocas;
    }

    public void setTabTrocas(JTable tabTrocas) {
        this.tabTrocas = tabTrocas;
    }

    public DefaultTableModel getDtmProdutos() {
        return dtmProdutos;
    }

    public void setDtmProdutos(DefaultTableModel dtmProdutos) {
        this.dtmProdutos = dtmProdutos;
    }

    public JLabel getLblBruto() {
        return lblValor;
    }

    public void setLblBruto(JLabel lblBruto) {
        this.lblValor = lblBruto;
    }

    public EcfTroca getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(EcfTroca selecionado) {
        this.selecionado = selecionado;
    }

    public JLabel getLblCpfCnpj() {
        return lblCpfCnpj;
    }

    public void setLblCpfCnpj(JLabel lblCpfCnpj) {
        this.lblCpfCnpj = lblCpfCnpj;
    }

    public JLabel getLblValor() {
        return lblValor;
    }

    public void setLblValor(JLabel lblValor) {
        this.lblValor = lblValor;
    }

    public JLabel getLblCOO() {
        return lblCOO;
    }

    public void setLblCOO(JLabel lblCOO) {
        this.lblCOO = lblCOO;
    }

    public JLabel getLblECF() {
        return lblECF;
    }

    public void setLblECF(JLabel lblECF) {
        this.lblECF = lblECF;
    }

    public JTextField getTxtCOO() {
        return txtCOO;
    }

    public void setTxtCOO(JTextField txtCOO) {
        this.txtCOO = txtCOO;
    }

    public JTextField getTxtECF() {
        return txtECF;
    }

    public void setTxtECF(JTextField txtECF) {
        this.txtECF = txtECF;
    }

    public JTextField getTxtCpfCnpj() {
        return txtCpfCnpj;
    }

    public void setTxtCpfCnpj(JTextField txtCpfCnpj) {
        this.txtCpfCnpj = txtCpfCnpj;
    }

    public JFormattedTextField getTxtValor() {
        return txtValor;
    }

    public void setTxtValor(JFormattedTextField txtValor) {
        this.txtValor = txtValor;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public JSeparator getSeparador1() {
        return separador1;
    }

    public void setSeparador1(JSeparator separador1) {
        this.separador1 = separador1;
    }

    public JSeparator getSeparador2() {
        return separador2;
    }

    public void setSeparador2(JSeparator separador2) {
        this.separador2 = separador2;
    }

    public CoreService getService() {
        return service;
    }

    public void setService(CoreService service) {
        this.service = service;
    }

    public JScrollPane getSpTrocas() {
        return spTrocas;
    }

    public void setSpTrocas(JScrollPane spTrocas) {
        this.spTrocas = spTrocas;
    }

    public JScrollPane getSpProdutos() {
        return spProdutos;
    }

    public void setSpProdutos(JScrollPane spProdutos) {
        this.spProdutos = spProdutos;
    }

    public JTable getTabProdutos() {
        return tabProdutos;
    }

    public void setTabProdutos(JTable tabProdutos) {
        this.tabProdutos = tabProdutos;
    }

    public JFormattedTextField getTxtBruto() {
        return txtValor;
    }

    public void setTxtBruto(JFormattedTextField txtBruto) {
        this.txtValor = txtBruto;
    }
}
