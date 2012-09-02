package br.com.openpdv.visao.nota;

import br.com.openpdv.controlador.core.*;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.core.filtro.FiltroTexto;
import br.com.openpdv.modelo.core.parametro.ParametroFormula;
import br.com.openpdv.modelo.ecf.EcfNota;
import br.com.openpdv.modelo.ecf.EcfNotaProduto;
import br.com.openpdv.modelo.produto.ProdComposicao;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdPreco;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.visao.core.Caixa;
import br.com.openpdv.visao.principal.Pesquisa;
import br.com.openpdv.visao.venda.Precos;
import java.awt.event.KeyEvent;
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
public class NotaConsumidor extends javax.swing.JDialog {

    private static NotaConsumidor notaConsumidor;
    private Logger log;
    private int row;
    private DefaultTableModel dtmNota;
    private DefaultTableModel dtmProdutos;
    private CoreService service;
    /**
     * Variavel que responde de modo assincrono a pesquisa de produto.
     */
    private AsyncCallback<ProdProduto> pesquisado = new AsyncCallback<ProdProduto>() {

        @Override
        public void sucesso(final ProdProduto prod) {
            if (prod == null) {
                JOptionPane.showMessageDialog(notaConsumidor, "Produto não encontrado.", "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
                btnAdicionar.requestFocus();
            } else {
                if (!prod.getProdPrecos().isEmpty()) {
                    Precos.getInstancia(new AsyncCallback<ProdPreco>() {

                        @Override
                        public void sucesso(ProdPreco preco) {
                            // se selecionou
                            if (preco != null) {
                                prod.setProdEmbalagem(preco.getProdEmbalagem());
                                prod.setProdProdutoPreco(preco.getProdPrecoValor());
                                prod.setProdProdutoBarra(preco.getProdPrecoBarra());
                            }
                            falha(null);
                        }

                        @Override
                        public void falha(Exception excecao) {
                            try {
                                adicionar(prod, 1.00);
                            } catch (OpenPdvException ex) {
                                log.error(ex);
                                JOptionPane.showMessageDialog(notaConsumidor, "Não foi possível adicionar o produto!", "Pesquisa", JOptionPane.WARNING_MESSAGE);
                            } finally {
                                btnAdicionar.requestFocus();
                            }
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
                            log.error(ex);
                            JOptionPane.showMessageDialog(notaConsumidor, "Não foi possível adicionar um item!\nCancele os itens adicionados.", "Pesquisa", JOptionPane.WARNING_MESSAGE);
                            break;
                        }
                    }
                    btnAdicionar.requestFocus();
                } else {
                    try {
                        adicionar(prod, 1.00);
                    } catch (OpenPdvException ex) {
                        log.error(ex);
                        JOptionPane.showMessageDialog(notaConsumidor, "Não foi possível adicionar o produto!", "Pesquisa", JOptionPane.WARNING_MESSAGE);
                    } finally {
                        btnAdicionar.requestFocus();
                    }
                }
            }
        }

        @Override
        public void falha(Exception excecao) {
            log.error("Problemas na pesquisa de produtos.", excecao);
            JOptionPane.showMessageDialog(notaConsumidor, "Erro ao pesquisar o produto.", "Pesquisa", JOptionPane.ERROR_MESSAGE);
            btnAdicionar.requestFocus();
        }

        private void adicionar(ProdProduto prod, double qtd) throws OpenPdvException {
            Object[] obj = new Object[]{0, prod, prod.getProdProdutoDescricao(), prod.getProdEmbalagem().getId() + " - " + prod.getProdEmbalagem().getProdEmbalagemNome(),
                qtd, prod.getProdProdutoPreco(), 0.00, prod.getProdProdutoPreco(), prod.getProdProdutoPreco(), dtmProdutos.getRowCount() + 1};
            dtmProdutos.addRow(obj);
            totalizar();
        }
    };

    /**
     * Construtor padrao.
     */
    private NotaConsumidor() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(NotaConsumidor.class);
        initComponents();

        service = new CoreService<>();
        dtmNota = (DefaultTableModel) tabNotas.getModel();
        dtmProdutos = (DefaultTableModel) tabProdutos.getModel();
        // escuta a selecao de linhas
        tabNotas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                row = tabNotas.getSelectedRow();
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

                if (col == 4 || col == 6) {
                    Double qtd = (Double) model.getValueAt(row, 4);
                    Double bruto = (Double) model.getValueAt(row, 5);
                    Double desc = (Double) model.getValueAt(row, 6);

                    // valida alguns valores
                    if (qtd <= 0) {
                        qtd = 1.00;
                    }
                    if (desc < 0) {
                        desc = 0.00;
                    } else if (desc > bruto) {
                        desc = bruto;
                    }

                    Double liquido = bruto - desc;
                    Double total = liquido * qtd;
                    model.setValueAt(liquido, row, 7);
                    model.setValueAt(total, row, 8);
                    totalizar();
                }
            }
        });

        // colocando limites nos campos
        txtSerie.setDocument(new TextFieldLimit(3));
        txtSubSerie.setDocument(new TextFieldLimit(3));
        txtNumero.setDocument(new TextFieldLimit(6, true));
        txtCliente.setDocument(new TextFieldLimit(18));
    }

    /**
     * Metodo que retorna a instancia do componente.
     *
     * @return o objeto do componente.
     */
    public static NotaConsumidor getInstancia() {
        if (notaConsumidor == null) {
            notaConsumidor = new NotaConsumidor();
        }

        notaConsumidor.setLista();
        return notaConsumidor;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spNotas = new javax.swing.JScrollPane();
        tabNotas = new javax.swing.JTable();
        separador1 = new javax.swing.JSeparator();
        lblSerie = new javax.swing.JLabel();
        txtSerie = new javax.swing.JTextField();
        lblSubSerie = new javax.swing.JLabel();
        txtSubSerie = new javax.swing.JTextField();
        lblNumero = new javax.swing.JLabel();
        txtNumero = new javax.swing.JFormattedTextField();
        lblData = new javax.swing.JLabel();
        txtData = new javax.swing.JFormattedTextField();
        lblCliente = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        lblBruto = new javax.swing.JLabel();
        txtBruto = new javax.swing.JFormattedTextField();
        lblDesconto = new javax.swing.JLabel();
        txtDesconto = new javax.swing.JFormattedTextField();
        lblLiquido = new javax.swing.JLabel();
        txtLiquido = new javax.swing.JFormattedTextField();
        lblPIS = new javax.swing.JLabel();
        txtPIS = new javax.swing.JFormattedTextField();
        lblCOFINS = new javax.swing.JLabel();
        txtCOFINS = new javax.swing.JFormattedTextField();
        separador2 = new javax.swing.JSeparator();
        btnAdicionar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        spProdutos = new javax.swing.JScrollPane();
        tabProdutos = new javax.swing.JTable();
        btnNovo = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nota Fiscal de Venda ao Consumidor");
        setModal(true);
        setResizable(false);

        spNotas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        spNotas.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        tabNotas.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabNotas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Série", "Sub-Série", "Número", "Data", "Cliente", "Bruto", "Desconto", "Líquido", "PIS", "COFINS", "Cancelada"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabNotas.setAutoCreateRowSorter(true);
        tabNotas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabNotas.setRowHeight(20);
        tabNotas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabNotas.setShowGrid(true);
        tabNotas.getTableHeader().setReorderingAllowed(false);
        spNotas.setViewportView(tabNotas);
        tabNotas.getColumnModel().getColumn(0).setMinWidth(0);
        tabNotas.getColumnModel().getColumn(0).setPreferredWidth(0);
        tabNotas.getColumnModel().getColumn(0).setMaxWidth(0);
        tabNotas.getColumnModel().getColumn(1).setResizable(false);
        tabNotas.getColumnModel().getColumn(1).setPreferredWidth(50);
        tabNotas.getColumnModel().getColumn(2).setResizable(false);
        tabNotas.getColumnModel().getColumn(2).setPreferredWidth(75);
        tabNotas.getColumnModel().getColumn(3).setResizable(false);
        tabNotas.getColumnModel().getColumn(3).setPreferredWidth(50);
        tabNotas.getColumnModel().getColumn(4).setResizable(false);
        tabNotas.getColumnModel().getColumn(4).setPreferredWidth(75);
        tabNotas.getColumnModel().getColumn(5).setResizable(false);
        tabNotas.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabNotas.getColumnModel().getColumn(6).setResizable(false);
        tabNotas.getColumnModel().getColumn(6).setPreferredWidth(75);
        tabNotas.getColumnModel().getColumn(7).setResizable(false);
        tabNotas.getColumnModel().getColumn(7).setPreferredWidth(75);
        tabNotas.getColumnModel().getColumn(8).setResizable(false);
        tabNotas.getColumnModel().getColumn(8).setPreferredWidth(75);
        tabNotas.getColumnModel().getColumn(9).setResizable(false);
        tabNotas.getColumnModel().getColumn(9).setPreferredWidth(50);
        tabNotas.getColumnModel().getColumn(10).setResizable(false);
        tabNotas.getColumnModel().getColumn(10).setPreferredWidth(50);
        tabNotas.getColumnModel().getColumn(11).setResizable(false);
        tabNotas.getColumnModel().getColumn(11).setPreferredWidth(75);

        lblSerie.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblSerie.setText("Série:");

        txtSerie.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtSerie.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblSubSerie.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblSubSerie.setText("Sub-Série:");

        txtSubSerie.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtSubSerie.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblNumero.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblNumero.setText("Número:");

        txtNumero.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtNumero.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtNumero.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblData.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblData.setText("Data:");

        txtData.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtData.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblCliente.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCliente.setText("Cliente:");

        txtCliente.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCliente.setToolTipText("Digite o CPF ou CNPJ do cliente.");

        lblBruto.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblBruto.setText("Bruto:");

        txtBruto.setEditable(false);
        txtBruto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txtBruto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBruto.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblDesconto.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblDesconto.setText("Desconto:");

        txtDesconto.setEditable(false);
        txtDesconto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txtDesconto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDesconto.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblLiquido.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblLiquido.setText("Líquido:");

        txtLiquido.setEditable(false);
        txtLiquido.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txtLiquido.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtLiquido.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblPIS.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblPIS.setText("PIS:");

        txtPIS.setEditable(false);
        txtPIS.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txtPIS.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPIS.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblCOFINS.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCOFINS.setText("COFINS:");

        txtCOFINS.setEditable(false);
        txtCOFINS.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txtCOFINS.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCOFINS.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

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
        spProdutos.setRowHeaderView(null);

        tabProdutos.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "ProdProduto", "Produto", "Embalagem", "Qtd", "Bruto", "Desc", "Líquido", "Total", "Ordem"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false, true, false, false, false
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
        tabProdutos.getTableHeader().setReorderingAllowed(false);
        spProdutos.setViewportView(tabProdutos);
        tabProdutos.getColumnModel().getColumn(0).setMinWidth(0);
        tabProdutos.getColumnModel().getColumn(0).setPreferredWidth(0);
        tabProdutos.getColumnModel().getColumn(0).setMaxWidth(0);
        tabProdutos.getColumnModel().getColumn(1).setMinWidth(0);
        tabProdutos.getColumnModel().getColumn(1).setPreferredWidth(0);
        tabProdutos.getColumnModel().getColumn(1).setMaxWidth(0);
        tabProdutos.getColumnModel().getColumn(2).setResizable(false);
        tabProdutos.getColumnModel().getColumn(2).setPreferredWidth(400);
        tabProdutos.getColumnModel().getColumn(3).setResizable(false);
        tabProdutos.getColumnModel().getColumn(3).setPreferredWidth(75);
        tabProdutos.getColumnModel().getColumn(4).setResizable(false);
        tabProdutos.getColumnModel().getColumn(4).setPreferredWidth(50);
        tabProdutos.getColumnModel().getColumn(5).setResizable(false);
        tabProdutos.getColumnModel().getColumn(5).setPreferredWidth(50);
        tabProdutos.getColumnModel().getColumn(6).setResizable(false);
        tabProdutos.getColumnModel().getColumn(6).setPreferredWidth(50);
        tabProdutos.getColumnModel().getColumn(7).setResizable(false);
        tabProdutos.getColumnModel().getColumn(7).setPreferredWidth(50);
        tabProdutos.getColumnModel().getColumn(8).setResizable(false);
        tabProdutos.getColumnModel().getColumn(8).setPreferredWidth(75);
        tabProdutos.getColumnModel().getColumn(9).setMinWidth(0);
        tabProdutos.getColumnModel().getColumn(9).setPreferredWidth(0);
        tabProdutos.getColumnModel().getColumn(9).setMaxWidth(0);

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
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(spNotas)
                    .add(separador1)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblSerie)
                            .add(lblBruto))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(layout.createSequentialGroup()
                                .add(txtSerie, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblSubSerie))
                            .add(txtBruto))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtSubSerie, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblDesconto))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(layout.createSequentialGroup()
                                .add(lblNumero)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtNumero, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(txtDesconto))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(lblData)
                                .add(17, 17, 17))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(lblLiquido)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(txtData, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblCliente))
                            .add(txtLiquido))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(lblPIS)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtPIS, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblCOFINS)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtCOFINS, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                            .add(txtCliente)))
                    .add(layout.createSequentialGroup()
                        .add(btnAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(btnRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, spProdutos)
                    .add(separador2)
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
                .add(spNotas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 179, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblSerie)
                    .add(txtSerie, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblSubSerie)
                    .add(txtSubSerie, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblNumero)
                    .add(txtNumero, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblData)
                    .add(txtData, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblCliente)
                    .add(txtCliente, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblBruto)
                    .add(lblDesconto)
                    .add(lblLiquido)
                    .add(lblPIS)
                    .add(lblCOFINS)
                    .add(txtBruto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtDesconto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtLiquido, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtPIS, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtCOFINS, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spProdutos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 123, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnSalvar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnNovo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-800)/2, (screenSize.height-552)/2, 800, 552);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cancelar();
        }
    }//GEN-LAST:event_btnCancelarKeyPressed

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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JLabel lblBruto;
    private javax.swing.JLabel lblCOFINS;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblDesconto;
    private javax.swing.JLabel lblLiquido;
    private javax.swing.JLabel lblNumero;
    private javax.swing.JLabel lblPIS;
    private javax.swing.JLabel lblSerie;
    private javax.swing.JLabel lblSubSerie;
    private javax.swing.JSeparator separador1;
    private javax.swing.JSeparator separador2;
    private javax.swing.JScrollPane spNotas;
    private javax.swing.JScrollPane spProdutos;
    private javax.swing.JTable tabNotas;
    private javax.swing.JTable tabProdutos;
    private javax.swing.JFormattedTextField txtBruto;
    private javax.swing.JFormattedTextField txtCOFINS;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JFormattedTextField txtData;
    private javax.swing.JFormattedTextField txtDesconto;
    private javax.swing.JFormattedTextField txtLiquido;
    private javax.swing.JFormattedTextField txtNumero;
    private javax.swing.JFormattedTextField txtPIS;
    private javax.swing.JTextField txtSerie;
    private javax.swing.JTextField txtSubSerie;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo para adicionar um novo registro.
     */
    private void novo() {
        tabNotas.clearSelection();
        txtSerie.requestFocus();
        row = -1;
        setDados();
    }

    /**
     * Metodo que salva um novo registro.
     */
    private void salvar() {
        if (txtNumero.getText().equals("") || txtData.getText().equals("") || txtLiquido.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Os campos Número e Data são obrigatórios!\nAlém de ser preciso ter itens.", "Nota Consumidor", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                SisCliente cliente = validarCliente();
                EntityManagerFactory emf = null;
                EntityManager em = null;

                try {
                    EcfNota nota = new EcfNota();
                    nota.setSisCliente(cliente);
                    nota.setSisEmpresa(Caixa.getInstancia().getEmpresa());
                    nota.setEcfNotaSerie(txtSerie.getText());
                    nota.setEcfNotaSubserie(txtSubSerie.getText());
                    nota.setEcfNotaNumero(Integer.valueOf(txtNumero.getText()));
                    nota.setEcfNotaData(new SimpleDateFormat("dd/MM/yyyy").parse(txtData.getText()));
                    nota.setEcfNotaBruto((Double) txtBruto.getValue());
                    nota.setEcfNotaDesconto((Double) txtDesconto.getValue());
                    nota.setEcfNotaLiquido((Double) txtLiquido.getValue());
                    nota.setEcfNotaPis((Double) txtPIS.getValue());
                    nota.setEcfNotaCofins((Double) txtCOFINS.getValue());
                    nota.setEcfNotaCancelada(false);

                    // recupera uma instancia do gerenciador de entidades
                    emf = Conexao.getInstancia();
                    em = emf.createEntityManager();
                    em.getTransaction().begin();
                    // salva
                    nota = (EcfNota) service.salvar(em, nota);

                    // valida sub-lista
                    List<EcfNotaProduto> produtos = new ArrayList<>();
                    if (validaProdutos(produtos, nota)) {
                        service.salvar(em, produtos);

                        // atualiza o estoque
                        for (EcfNotaProduto np : produtos) {
                            // fatorando a quantida no estoque
                            double qtd = np.getEcfNotaProdutoQuantidade();
                            if (np.getProdEmbalagem().getProdEmbalagemId() != np.getProdProduto().getProdEmbalagem().getProdEmbalagemId()) {
                                qtd *= np.getProdEmbalagem().getProdEmbalagemUnidade();
                                qtd /= np.getProdProduto().getProdEmbalagem().getProdEmbalagemUnidade();
                            }

                            // atualiza o estoque
                            ParametroFormula pf = new ParametroFormula("prodProdutoEstoque", -1 * qtd);
                            FiltroNumero fn1 = new FiltroNumero("prodProdutoId", ECompara.IGUAL, np.getProdProduto().getId());
                            Sql sql = new Sql(np.getProdProduto(), EComandoSQL.ATUALIZAR, fn1, pf);
                            service.executar(em, sql);
                        }

                        em.getTransaction().commit();
                        JOptionPane.showMessageDialog(this, "Registro salvo com sucesso.", "Nota Consumidor", JOptionPane.INFORMATION_MESSAGE);
                        setLista();
                    } else {
                        em.getTransaction().rollback();
                    }
                } catch (Exception ex) {
                    if (em != null && em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    log.error("Erro ao salvar a nota.", ex);
                    JOptionPane.showMessageDialog(this, "Não foi possível salvar o registro!", "Nota Consumidor", JOptionPane.WARNING_MESSAGE);
                } finally {
                    em.close();
                    emf.close();
                }
            } catch (OpenPdvException ex) {
                log.error("Erro ao salvar ou selecionar o cliente.", ex);
                JOptionPane.showMessageDialog(this, "O CPF/CNPJ informado não é válido!", "Nota Consumidor", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Metodo que sai da tela ou cancela a nota selecionada.
     */
    private void cancelar() {
        if (row == -1) {
            dispose();
            Caixa.getInstancia().setJanela(null);
        } else {
            int escolha = JOptionPane.showOptionDialog(this, "Deseja cancelar a NF selecionada?", "Nota Consumidor",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                try {
                    String cod = dtmNota.getValueAt(row, 0).toString();
                    FiltroNumero fn = new FiltroNumero("ecfNotaId", ECompara.IGUAL, cod);
                    EcfNota nota = (EcfNota) service.selecionar(new EcfNota(), fn);
                    nota.setEcfNotaCancelada(true);
                    service.salvar(nota);

                    // atualiza o estoque
                    List<Sql> sqls = new ArrayList<>();
                    for (EcfNotaProduto np : nota.getEcfNotaProdutos()) {
                        // fatorando a quantida no estoque
                        double qtd = np.getEcfNotaProdutoQuantidade();
                        if (np.getProdEmbalagem().getProdEmbalagemId() != np.getProdProduto().getProdEmbalagem().getProdEmbalagemId()) {
                            qtd *= np.getProdEmbalagem().getProdEmbalagemUnidade();
                            qtd /= np.getProdProduto().getProdEmbalagem().getProdEmbalagemUnidade();
                        }

                        // atualiza o estoque
                        ParametroFormula pf = new ParametroFormula("prodProdutoEstoque", qtd);
                        FiltroNumero fn1 = new FiltroNumero("prodProdutoId", ECompara.IGUAL, np.getProdProduto().getId());
                        Sql sql = new Sql(np.getProdProduto(), EComandoSQL.ATUALIZAR, fn1, pf);
                        sqls.add(sql);
                    }
                    service.executar(sqls);
                } catch (OpenPdvException ex) {
                    log.error("Erro ao cancelar a nota.", ex);
                    JOptionPane.showMessageDialog(this, "Não foi possível cancelar o registro!", "Nota Consumidor", JOptionPane.WARNING_MESSAGE);
                } finally {
                    setLista();
                }
            }
        }
    }

    /**
     * Metodo que valida os prdoutos da nota
     *
     * @param produtos a lista de produtos
     * @param nota a nota atual
     * @return verdadeiro se valido, falso caso contrario
     */
    private boolean validaProdutos(List<EcfNotaProduto> produtos, EcfNota nota) {
        try {
            for (int i = 0; i < dtmProdutos.getRowCount(); i++) {
                EcfNotaProduto np = new EcfNotaProduto();
                np.setEcfNota(nota);
                np.setProdProduto((ProdProduto) dtmProdutos.getValueAt(i, 1));
                String[] emb = dtmProdutos.getValueAt(i, 3).toString().split(" - ");
                np.setProdEmbalagem(new ProdEmbalagem(Integer.valueOf(emb[0])));
                Double qtd = (Double) dtmProdutos.getValueAt(i, 4);
                if (qtd <= 0.00) {
                    throw new Exception();
                }
                np.setEcfNotaProdutoQuantidade(qtd);
                np.setEcfNotaProdutoBruto((Double) dtmProdutos.getValueAt(i, 5));
                Double desconto = (Double) dtmProdutos.getValueAt(i, 6);
                if (desconto < 0.00 || desconto > np.getEcfNotaProdutoBruto()) {
                    throw new Exception();
                }
                np.setEcfNotaProdutoDesconto(desconto);
                np.setEcfNotaProdutoLiquido((Double) dtmProdutos.getValueAt(i, 7));
                np.setEcfNotaProdutoOrdem((Integer) dtmProdutos.getValueAt(i, 9));
                produtos.add(np);
            }
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Problemas com os dados do produtos!", "Nota Consumidor", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    /**
     * Metodo que seta os valores da tabela vindas do banco de dados.
     */
    private void setLista() {
        try {
            List<EcfNota> lista = service.selecionar(new EcfNota(), 0, 0, null);
            while (dtmNota.getRowCount() > 0) {
                dtmNota.removeRow(0);
            }

            for (EcfNota nota : lista) {
                String data = new SimpleDateFormat("dd/MM/yyyy").format(nota.getEcfNotaData());
                String cliente = nota.getSisCliente() == null ? "" : nota.getSisCliente().getSisClienteDoc();
                Object[] obj = new Object[]{nota.getId(), nota.getEcfNotaSerie(), nota.getEcfNotaSubserie(), nota.getEcfNotaNumero(), data,
                    cliente, nota.getEcfNotaBruto(), nota.getEcfNotaDesconto(), nota.getEcfNotaLiquido(), nota.getEcfNotaPis(), nota.getEcfNotaCofins(), nota.isEcfNotaCancelada()};
                dtmNota.addRow(obj);
            }

            row = -1;
            setDados();
        } catch (OpenPdvException ex) {
            log.error("Erro ao selecionar as notas do sistema", ex);
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
            txtSerie.setText("");
            txtSubSerie.setText("");
            txtNumero.setValue(null);
            txtData.setValue(null);
            txtCliente.setText("");
            txtBruto.setValue(null);
            txtDesconto.setValue(null);
            txtLiquido.setValue(null);
            txtPIS.setValue(null);
            txtCOFINS.setValue(null);
            btnAdicionar.setEnabled(true);
            btnRemover.setEnabled(true);
            btnSalvar.setEnabled(true);
        } else {
            int rowModel = tabNotas.convertRowIndexToModel(row);
            int cod = Integer.valueOf(tabNotas.getModel().getValueAt(rowModel, 0).toString());
            txtSerie.setText(tabNotas.getModel().getValueAt(rowModel, 1).toString());
            txtSubSerie.setText(tabNotas.getModel().getValueAt(rowModel, 2).toString());
            txtNumero.setValue(tabNotas.getModel().getValueAt(rowModel, 3));
            txtData.setText(tabNotas.getModel().getValueAt(rowModel, 4).toString());
            txtCliente.setText(tabNotas.getModel().getValueAt(rowModel, 5).toString());
            txtBruto.setValue(tabNotas.getModel().getValueAt(rowModel, 6));
            txtDesconto.setValue(tabNotas.getModel().getValueAt(rowModel, 7));
            txtLiquido.setValue(tabNotas.getModel().getValueAt(rowModel, 8));
            txtPIS.setValue(tabNotas.getModel().getValueAt(rowModel, 9));
            txtCOFINS.setValue(tabNotas.getModel().getValueAt(rowModel, 10));
            btnAdicionar.setEnabled(false);
            btnRemover.setEnabled(false);
            btnSalvar.setEnabled(false);

            try {
                // recupera o produto para colocar os precos e itens
                EcfNota nota = new EcfNota();
                FiltroNumero fn = new FiltroNumero("ecfNotaId", ECompara.IGUAL, cod);
                nota = (EcfNota) service.selecionar(nota, fn);
                for (EcfNotaProduto np : nota.getEcfNotaProdutos()) {
                    double total = np.getEcfNotaProdutoLiquido() * np.getEcfNotaProdutoQuantidade();
                    Object[] obj = new Object[]{np.getId(), np.getProdProduto().getId(), np.getProdProduto().getProdProdutoDescricao(),
                        np.getProdEmbalagem().getId() + " - " + np.getProdEmbalagem().getProdEmbalagemNome(), np.getEcfNotaProdutoQuantidade(),
                        np.getEcfNotaProdutoBruto(), np.getEcfNotaProdutoDesconto(), np.getEcfNotaProdutoLiquido(), total, np.getEcfNotaProdutoOrdem()};
                    dtmProdutos.addRow(obj);
                }
            } catch (OpenPdvException ex) {
                log.error("Erro ao selecionar a nota do sistema", ex);
            }
        }
    }

    /**
     * Metodo que valida o cliente informado.
     *
     * @return retorna null ou um objeto de cliente.
     * @throws OpenPdvException caso os dados informados nao sejam validos.
     */
    private SisCliente validarCliente() throws OpenPdvException {
        String texto = txtCliente.getText();
        texto = texto.replaceAll("[^0-9]", "");

        if (!texto.equals("")) {
            boolean valido = texto.length() == 11 ? Util.isCPF(texto) : Util.isCNPJ(texto);
            if (valido) {
                FiltroTexto ft = new FiltroTexto("sisClienteDoc", ECompara.IGUAL, texto);
                SisCliente cliente = (SisCliente) service.selecionar(new SisCliente(), ft);

                if (cliente == null) {
                    cliente = new SisCliente();
                    cliente.setSisClienteDoc(texto);
                    cliente.setSisClienteEndereco("");
                    cliente.setSisClienteNome("");
                    cliente.setSisClienteCadastrado(new Date());
                    cliente = (SisCliente) service.salvar(cliente);
                }
                return cliente;
            } else {
                throw new OpenPdvException();
            }
        } else {
            return null;
        }
    }

    /**
     * Metodo que totaliza os valores dos produtos adicionados.
     */
    private void totalizar() {
        List<EcfNotaProduto> nps = new ArrayList<>();
        validaProdutos(nps, null);

        Double bruto = 0.00;
        Double desconto = 0.00;
        Double liquido = 0.00;
        for (EcfNotaProduto np : nps) {
            bruto += np.getEcfNotaProdutoBruto() * np.getEcfNotaProdutoQuantidade();
            desconto += np.getEcfNotaProdutoDesconto() * np.getEcfNotaProdutoQuantidade();
            liquido += np.getEcfNotaProdutoLiquido() * np.getEcfNotaProdutoQuantidade();
        }
        Double pis = liquido * Double.valueOf(Util.getConfig().get("nfe.pis")) / 100;
        Double cofins = liquido * Double.valueOf(Util.getConfig().get("nfe.cofins")) / 100;

        txtBruto.setValue(bruto);
        txtDesconto.setValue(desconto);
        txtLiquido.setValue(liquido);
        txtPIS.setValue(pis);
        txtCOFINS.setValue(cofins);
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

    public DefaultTableModel getDtmNota() {
        return dtmNota;
    }

    public void setDtmNota(DefaultTableModel dtmNota) {
        this.dtmNota = dtmNota;
    }

    public DefaultTableModel getDtmProdutos() {
        return dtmProdutos;
    }

    public void setDtmProdutos(DefaultTableModel dtmProdutos) {
        this.dtmProdutos = dtmProdutos;
    }

    public JLabel getLblBruto() {
        return lblBruto;
    }

    public void setLblBruto(JLabel lblBruto) {
        this.lblBruto = lblBruto;
    }

    public JLabel getLblCOFINS() {
        return lblCOFINS;
    }

    public void setLblCOFINS(JLabel lblCOFINS) {
        this.lblCOFINS = lblCOFINS;
    }

    public JLabel getLblCliente() {
        return lblCliente;
    }

    public void setLblCliente(JLabel lblCliente) {
        this.lblCliente = lblCliente;
    }

    public JLabel getLblData() {
        return lblData;
    }

    public void setLblData(JLabel lblData) {
        this.lblData = lblData;
    }

    public JLabel getLblDesconto() {
        return lblDesconto;
    }

    public void setLblDesconto(JLabel lblDesconto) {
        this.lblDesconto = lblDesconto;
    }

    public JLabel getLblLiquido() {
        return lblLiquido;
    }

    public void setLblLiquido(JLabel lblLiquido) {
        this.lblLiquido = lblLiquido;
    }

    public JLabel getLblNumero() {
        return lblNumero;
    }

    public void setLblNumero(JLabel lblNumero) {
        this.lblNumero = lblNumero;
    }

    public JLabel getLblPIS() {
        return lblPIS;
    }

    public void setLblPIS(JLabel lblPIS) {
        this.lblPIS = lblPIS;
    }

    public JLabel getLblSerie() {
        return lblSerie;
    }

    public void setLblSerie(JLabel lblSerie) {
        this.lblSerie = lblSerie;
    }

    public JLabel getLblSubSerie() {
        return lblSubSerie;
    }

    public void setLblSubSerie(JLabel lblSubSerie) {
        this.lblSubSerie = lblSubSerie;
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

    public JScrollPane getSpNotas() {
        return spNotas;
    }

    public void setSpNotas(JScrollPane spNotas) {
        this.spNotas = spNotas;
    }

    public JScrollPane getSpProdutos() {
        return spProdutos;
    }

    public void setSpProdutos(JScrollPane spProdutos) {
        this.spProdutos = spProdutos;
    }

    public JTable getTabNotas() {
        return tabNotas;
    }

    public void setTabNotas(JTable tabNotas) {
        this.tabNotas = tabNotas;
    }

    public JTable getTabProdutos() {
        return tabProdutos;
    }

    public void setTabProdutos(JTable tabProdutos) {
        this.tabProdutos = tabProdutos;
    }

    public JFormattedTextField getTxtBruto() {
        return txtBruto;
    }

    public void setTxtBruto(JFormattedTextField txtBruto) {
        this.txtBruto = txtBruto;
    }

    public JFormattedTextField getTxtCOFINS() {
        return txtCOFINS;
    }

    public void setTxtCOFINS(JFormattedTextField txtCOFINS) {
        this.txtCOFINS = txtCOFINS;
    }

    public JTextField getTxtCliente() {
        return txtCliente;
    }

    public void setTxtCliente(JTextField txtCliente) {
        this.txtCliente = txtCliente;
    }

    public JFormattedTextField getTxtData() {
        return txtData;
    }

    public void setTxtData(JFormattedTextField txtData) {
        this.txtData = txtData;
    }

    public JFormattedTextField getTxtDesconto() {
        return txtDesconto;
    }

    public void setTxtDesconto(JFormattedTextField txtDesconto) {
        this.txtDesconto = txtDesconto;
    }

    public JFormattedTextField getTxtLiquido() {
        return txtLiquido;
    }

    public void setTxtLiquido(JFormattedTextField txtLiquido) {
        this.txtLiquido = txtLiquido;
    }

    public JFormattedTextField getTxtNumero() {
        return txtNumero;
    }

    public void setTxtNumero(JFormattedTextField txtNumero) {
        this.txtNumero = txtNumero;
    }

    public JFormattedTextField getTxtPIS() {
        return txtPIS;
    }

    public void setTxtPIS(JFormattedTextField txtPIS) {
        this.txtPIS = txtPIS;
    }

    public JTextField getTxtSerie() {
        return txtSerie;
    }

    public void setTxtSerie(JTextField txtSerie) {
        this.txtSerie = txtSerie;
    }

    public JTextField getTxtSubSerie() {
        return txtSubSerie;
    }

    public void setTxtSubSerie(JTextField txtSubSerie) {
        this.txtSubSerie = txtSubSerie;
    }
}
