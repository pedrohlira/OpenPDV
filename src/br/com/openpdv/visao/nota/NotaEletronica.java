package br.com.openpdv.visao.nota;

import br.com.openpdv.cancnfe.TCancNFe;
import br.com.openpdv.controlador.comandos.*;
import br.com.openpdv.controlador.core.*;
import br.com.openpdv.inutnfe.TInutNFe;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.core.parametro.ParametroFormula;
import br.com.openpdv.modelo.ecf.ENotaStatus;
import br.com.openpdv.modelo.ecf.EcfNotaEletronica;
import br.com.openpdv.modelo.ecf.EcfNotaProduto;
import br.com.openpdv.modelo.produto.ProdComposicao;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdPreco;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.modelo.sistema.SisEstado;
import br.com.openpdv.modelo.sistema.SisMunicipio;
import br.com.openpdv.nfe.TNFe;
import br.com.openpdv.nfe.TNFe.InfNFe.Det;
import br.com.openpdv.nfe.TNfeProc;
import br.com.openpdv.visao.core.Aguarde;
import br.com.openpdv.visao.core.Caixa;
import br.com.openpdv.visao.principal.Pesquisa;
import br.com.openpdv.visao.venda.Precos;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.bind.JAXBElement;
import org.apache.log4j.Logger;

/**
 * Classe que representa as notas eletronicas do sistema.
 *
 * @author Pedro H. Lira
 */
public class NotaEletronica extends javax.swing.JDialog {

    private static NotaEletronica notaEletronica;
    private Logger log;
    private DefaultTableModel dtmProdutos;
    private CoreService service;
    /**
     * Variavel que responde de modo assincrono a pesquisa de produto.
     */
    private AsyncCallback<ProdProduto> pesquisado = new AsyncCallback<ProdProduto>() {

        @Override
        public void sucesso(final ProdProduto prod) {
            if (prod == null) {
                JOptionPane.showMessageDialog(notaEletronica, "Produto não encontrado.", "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
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
                                JOptionPane.showMessageDialog(notaEletronica, "Não foi possível adicionar o produto!", "Pesquisa", JOptionPane.WARNING_MESSAGE);
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
                            JOptionPane.showMessageDialog(notaEletronica, "Não foi possível adicionar um item!\nCancele os itens adicionados.", "Pesquisa", JOptionPane.WARNING_MESSAGE);
                            break;
                        }
                    }
                    btnAdicionar.requestFocus();
                } else {
                    try {
                        adicionar(prod, 1.00);
                    } catch (OpenPdvException ex) {
                        log.error(ex);
                        JOptionPane.showMessageDialog(notaEletronica, "Não foi possível adicionar o produto!", "Pesquisa", JOptionPane.WARNING_MESSAGE);
                    } finally {
                        btnAdicionar.requestFocus();
                    }
                }
            }
        }

        @Override
        public void falha(Exception excecao) {
            log.error("Problemas na pesquisa de produtos.", excecao);
            JOptionPane.showMessageDialog(notaEletronica, "Erro ao pesquisar o produto.", "Pesquisa", JOptionPane.ERROR_MESSAGE);
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
    private NotaEletronica() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(NotaEletronica.class);
        initComponents();
        service = new CoreService<>();
        setUF();

        dtmProdutos = (DefaultTableModel) tabProdutos.getModel();
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
        txtNome.setDocument(new TextFieldLimit(60));
        txtCPF.setDocument(new TextFieldLimit(18));
        txtIE.setDocument(new TextFieldLimit(15));
        txtEndereco.setDocument(new TextFieldLimit(60));
        txtNumero.setDocument(new TextFieldLimit(5, true));
        txtBairro.setDocument(new TextFieldLimit(20));
        txtEmail.setDocument(new TextFieldLimit(100));
    }

    /**
     * Metodo que retorna a instancia do componente.
     *
     * @return o objeto do componente.
     */
    public static NotaEletronica getInstancia() {
        if (notaEletronica == null) {
            notaEletronica = new NotaEletronica();
        }

        notaEletronica.limpar();
        return notaEletronica;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        lblCPF = new javax.swing.JLabel();
        txtCPF = new javax.swing.JTextField();
        lblIE = new javax.swing.JLabel();
        txtIE = new javax.swing.JTextField();
        lblEndereco = new javax.swing.JLabel();
        txtEndereco = new javax.swing.JTextField();
        lblNumero = new javax.swing.JLabel();
        txtNumero = new javax.swing.JFormattedTextField();
        lblBairro = new javax.swing.JLabel();
        txtBairro = new javax.swing.JTextField();
        lblCEP = new javax.swing.JLabel();
        txtCEP = new javax.swing.JFormattedTextField();
        lblUF = new javax.swing.JLabel();
        cmbUF = new javax.swing.JComboBox();
        lblMunicipio = new javax.swing.JLabel();
        cmbMunicipio = new javax.swing.JComboBox();
        lblFone = new javax.swing.JLabel();
        txtFone = new javax.swing.JFormattedTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        separador = new javax.swing.JSeparator();
        btnAdicionar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        lblTotalNota = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        spProdutos = new javax.swing.JScrollPane();
        tabProdutos = new javax.swing.JTable();
        btnGerar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnInutilizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nota Fiscal Eletrônica");
        setModal(true);
        setResizable(false);

        lblNome.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblNome.setText("Nome:");

        txtNome.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtNome.setToolTipText("Nome ou Razão Social.");

        lblCPF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCPF.setText("CPF:");

        txtCPF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCPF.setToolTipText("CPF ou CNPJ.");

        lblIE.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblIE.setText("IE:");

        txtIE.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtIE.setToolTipText("CPF ou CNPJ.");

        lblEndereco.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblEndereco.setText("Endereço:");

        txtEndereco.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblNumero.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblNumero.setText("Nº:");

        txtNumero.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtNumero.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

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
        cmbUF.setEnabled(false);

        lblMunicipio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblMunicipio.setText("Município:");

        cmbMunicipio.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        cmbMunicipio.setMaximumRowCount(20);
        cmbMunicipio.setToolTipText("Selecione uma Cidade");

        lblFone.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblFone.setText("Fone:");

        try {
            txtFone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtFone.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        lblEmail.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblEmail.setText("Email:");

        txtEmail.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtEmail.setToolTipText("Nome ou Razão Social.");

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

        lblTotalNota.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblTotalNota.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalNota.setText("Total da Nota:");

        lblTotal.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("R$ 0,00");

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

        btnGerar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnGerar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/salvar.png"))); // NOI18N
        btnGerar.setText("Gerar");
        btnGerar.setToolTipText("Gera a NFe e envia pra sefaz.");
        btnGerar.setMaximumSize(new java.awt.Dimension(100, 30));
        btnGerar.setMinimumSize(new java.awt.Dimension(100, 30));
        btnGerar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnGerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarActionPerformed(evt);
            }
        });
        btnGerar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnGerarKeyPressed(evt);
            }
        });

        btnExcluir.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/excluir.png"))); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.setToolTipText("Verifica se o NFe informada pode ser cancelada e envia pra sefaz.");
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

        btnInutilizar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnInutilizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/informacao.png"))); // NOI18N
        btnInutilizar.setText("Inutilizar");
        btnInutilizar.setToolTipText("Inutiliza uma NFe junto a sefaz.");
        btnInutilizar.setMaximumSize(new java.awt.Dimension(100, 30));
        btnInutilizar.setMinimumSize(new java.awt.Dimension(100, 30));
        btnInutilizar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnInutilizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInutilizarActionPerformed(evt);
            }
        });
        btnInutilizar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnInutilizarKeyPressed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cancelar.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setToolTipText("Sai da tela.");
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
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(lblUF)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cmbUF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblMunicipio)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cmbMunicipio, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblFone)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(txtFone, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblEmail)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtEmail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblEndereco)
                                    .add(lblNome))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(layout.createSequentialGroup()
                                        .add(txtEndereco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 301, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(lblNumero)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 26, Short.MAX_VALUE)
                                        .add(txtNumero, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(txtNome))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                        .add(lblCPF)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED))
                                    .add(layout.createSequentialGroup()
                                        .add(lblBairro)
                                        .add(4, 4, 4)))
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(txtBairro, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                    .add(txtCPF))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblIE)
                                    .add(lblCEP))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(txtIE)
                                    .add(txtCEP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, separador))
                        .add(33, 33, 33))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(btnAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(layout.createSequentialGroup()
                                    .add(btnGerar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(btnInutilizar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, spProdutos, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 761, Short.MAX_VALUE)
                                    .add(layout.createSequentialGroup()
                                        .add(lblTotalNota, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(lblTotal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblNome)
                    .add(txtNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblCPF)
                    .add(txtCPF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblIE)
                    .add(txtIE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblEndereco)
                    .add(txtEndereco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblNumero)
                    .add(txtNumero, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblBairro)
                    .add(txtBairro, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblCEP)
                    .add(txtCEP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblUF)
                    .add(cmbUF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblMunicipio)
                    .add(cmbMunicipio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblFone)
                    .add(txtFone, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblEmail)
                    .add(txtEmail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE, false)
                    .add(btnRemover, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblTotal, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblTotalNota)
                    .add(btnAdicionar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spProdutos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 309, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnGerar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnInutilizar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
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

    private void btnGerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarActionPerformed
        if (txtNome.getText().equals("") || txtCPF.getText().equals("") || txtEndereco.getText().equals("")
                || txtNumero.getValue() == null || txtBairro.getText().equals("") || txtCEP.getValue() == null
                || cmbUF.getSelectedIndex() == -1 || cmbMunicipio.getSelectedIndex() == -1 || lblTotal.getText().equals("R$ 0,00")) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!\nAdicione também produtos com valor maior que zero.", "Nota Eletrônica", JOptionPane.INFORMATION_MESSAGE);
            txtNome.requestFocus();
        } else {
            try {
                // pega o cliente
                SisCliente cliente = validarCliente();
                // pega os produtos
                List<EcfNotaProduto> produtos = validarProdutos();
                gerar(cliente, produtos);
            } catch (OpenPdvException ex) {
                log.debug(ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnGerarActionPerformed

    private void btnGerarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnGerarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnGerarActionPerformed(null);
        }
    }//GEN-LAST:event_btnGerarKeyPressed

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

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        String chave = JOptionPane.showInputDialog(notaEletronica, "Digite os 44 numeros da chave de acesso na NFe.", "CHAVE", JOptionPane.QUESTION_MESSAGE);
        if (chave != null) {
            chave = chave.replaceAll("[^0-9]", "");

            if (chave.length() == 44) {
                String obs = JOptionPane.showInputDialog(notaEletronica, "Digite o motivo do cancelamento, entre 15 e 255 letras.", "MOTIVO", JOptionPane.QUESTION_MESSAGE);
                obs = obs.replaceAll(Util.getConfig().get("nfe.regexp"), "");

                if (obs.length() >= 15 && obs.length() <= 255) {
                    FiltroTexto ft = new FiltroTexto("ecfNotaEletronicaChave", ECompara.IGUAL, chave);
                    FiltroTexto ft1 = new FiltroTexto("ecfNotaEletronicaStatus", ECompara.IGUAL, ENotaStatus.AUTORIZADO.toString());
                    GrupoFiltro gp = new GrupoFiltro(EJuncao.E, new IFiltro[]{ft, ft1});
                    EcfNotaEletronica nota;
                    try {
                        nota = (EcfNotaEletronica) service.selecionar(new EcfNotaEletronica(), gp);
                    } catch (OpenPdvException ex) {
                        nota = null;
                    }

                    if (nota != null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(nota.getEcfNotaEletronicaData());
                        cal.add(Calendar.DAY_OF_MONTH, 1);

                        if (cal.getTime().compareTo(new Date()) > 0) {
                            excluir(nota, obs.toUpperCase());
                        } else {
                            JOptionPane.showMessageDialog(this, "O limite para cancelar um NFe é de 24 horas.\nData e Hora da NFe solicitada: "
                                    + Util.getDataHora(nota.getEcfNotaEletronicaData()), "Nota Eletrônica", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "A chave informada não corresponde a nenhuma NFe salva: " + chave, "Nota Eletrônica", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Nao informou um motivo válido entre 15 e 255 letras: " + obs, "Nota Eletrônica", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Nao informou uma chave válida com 44 digitos: " + chave, "Nota Eletrônica", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnExcluirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnExcluirKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnExcluirActionPerformed(null);
        }
    }//GEN-LAST:event_btnExcluirKeyPressed

    private void btnInutilizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInutilizarActionPerformed
        String numero = JOptionPane.showInputDialog(notaEletronica, "Digite o número da NFe que deseja inutilizar.", "NÚMERO", JOptionPane.QUESTION_MESSAGE);
        if (numero != null) {
            numero = numero.replaceAll("[^0-9]", "");

            if (numero.length() > 0) {
                String obs = JOptionPane.showInputDialog(notaEletronica, "Digite o motivo da inutilização, entre 15 e 255 letras.", "MOTIVO", JOptionPane.QUESTION_MESSAGE);
                obs = obs.replaceAll(Util.getConfig().get("nfe.regexp"), "");

                if (obs.length() >= 15 && obs.length() <= 255) {
                    inutilizar(numero, obs);
                } else {
                    JOptionPane.showMessageDialog(this, "Nao informou um motivo válido entre 15 e 255 letras: " + obs, "Nota Eletrônica", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Nao informou uma chave válida com 44 digitos: " + numero, "Nota Eletrônica", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnInutilizarActionPerformed

    private void btnInutilizarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnInutilizarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnInutilizarActionPerformed(null);
        }
    }//GEN-LAST:event_btnInutilizarKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnGerar;
    private javax.swing.JButton btnInutilizar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JComboBox cmbMunicipio;
    private javax.swing.JComboBox cmbUF;
    private javax.swing.JLabel lblBairro;
    private javax.swing.JLabel lblCEP;
    private javax.swing.JLabel lblCPF;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEndereco;
    private javax.swing.JLabel lblFone;
    private javax.swing.JLabel lblIE;
    private javax.swing.JLabel lblMunicipio;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblNumero;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalNota;
    private javax.swing.JLabel lblUF;
    private javax.swing.JSeparator separador;
    private javax.swing.JScrollPane spProdutos;
    private javax.swing.JTable tabProdutos;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JFormattedTextField txtCEP;
    private javax.swing.JTextField txtCPF;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEndereco;
    private javax.swing.JFormattedTextField txtFone;
    private javax.swing.JTextField txtIE;
    private javax.swing.JTextField txtNome;
    private javax.swing.JFormattedTextField txtNumero;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que limpa os campos para nova nota.
     */
    private void limpar() {
        txtNome.setText("");
        txtCPF.setText("");
        txtIE.setText("");
        txtEndereco.setText("");
        txtNumero.setValue(0);
        txtBairro.setText("");
        txtCEP.setValue(null);
        cmbMunicipio.setSelectedIndex(-1);
        txtFone.setValue(null);
        txtEmail.setText("");
        lblTotal.setText("R$ 0,00");
        while (dtmProdutos.getRowCount() > 0) {
            dtmProdutos.removeRow(0);
        }
    }

    /**
     * Metodo que gera a NFe na sefaz.
     *
     * @param cliente dados do cliente.
     * @param produtos dados dos produtos.
     */
    private void gerar(final SisCliente cliente, final List<EcfNotaProduto> produtos) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // gera o xml
                    ComandoGerarNFe gerar = new ComandoGerarNFe(cliente, produtos);
                    gerar.executar();
                    JAXBElement<TNFe> elemet = gerar.getElement();
                    // envia o xml para sefaz
                    ComandoEnviarNFe enviarNFe = new ComandoEnviarNFe(elemet);
                    enviarNFe.executar();
                    // salva o registro no banco
                    EcfNotaEletronica nota = enviarNFe.getNota();
                    nota.setSisCliente(cliente);
                    nota.setSisEmpresa(Caixa.getInstancia().getEmpresa());
                    service.salvar(nota);
                    Aguarde.getInstancia().setVisible(false);
                    // chama a rotina de verificacao de pendente
                    analisarPendente(nota, produtos);
                } catch (Exception ex) {
                    Aguarde.getInstancia().setVisible(false);
                    log.error("Erro na geracao da NFe.", ex);
                    JOptionPane.showMessageDialog(notaEletronica, "Problemas na geração do xml da NFe!\nAnalisar o log do sistema para ver o erro.", "Nota Eletrônica", JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
        Aguarde.getInstancia().setVisible(true);
    }

    /**
     * Metodo que cancela a NFe na sefaz.
     *
     * @param nota a nota referenciada pela chave.
     * @param obs o texto de motivo de cancelamento.
     */
    private void excluir(final EcfNotaEletronica nota, final String obs) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // gera o xml
                    ComandoGerarNFeCancelada gerarNFe = new ComandoGerarNFeCancelada(nota, obs);
                    gerarNFe.executar();
                    JAXBElement<TCancNFe> element = gerarNFe.getElement();
                    // envia o xml para sefaz
                    ComandoEnviarNFeCancelada enviarNFe = new ComandoEnviarNFeCancelada(element, nota);
                    enviarNFe.executar();
                    // salva o registro no banco
                    EcfNotaEletronica nota2 = enviarNFe.getNota();
                    service.salvar(nota2);

                    // atualiza o estoque
                    TNfeProc produtos = NFe.xmlToObj(nota2.getEcfNotaEletronicaXml(), TNfeProc.class);
                    List<Sql> sqls = new ArrayList<>();
                    for (Det det : produtos.getNFe().getInfNFe().getDet()) {
                        // achando o produto
                        IFiltro filtro;
                        if (det.getProd().getCEAN() == null) {
                            filtro = new FiltroNumero("prodProdutoId", ECompara.IGUAL, det.getProd().getCProd());
                        } else {
                            filtro = new FiltroTexto("prodProdutoBarra", ECompara.IGUAL, det.getProd().getCEAN());
                        }
                        ProdProduto prod = (ProdProduto) service.selecionar(new ProdProduto(), filtro);
                        // achando a embalagem usada na venda
                        FiltroTexto ft = new FiltroTexto("prodEmbalagemNome", ECompara.IGUAL, det.getProd().getUCom());
                        ProdEmbalagem emb = (ProdEmbalagem) service.selecionar(new ProdEmbalagem(), ft);

                        // fatorando a quantida no estoque
                        double qtd = Double.valueOf(det.getProd().getQCom());
                        if (emb.getProdEmbalagemId() != prod.getProdEmbalagem().getProdEmbalagemId()) {
                            qtd *= emb.getProdEmbalagemUnidade();
                            qtd /= prod.getProdEmbalagem().getProdEmbalagemUnidade();
                        }

                        // atualiza o estoque
                        ParametroFormula pf = new ParametroFormula("prodProdutoEstoque", qtd);
                        FiltroNumero fn1 = new FiltroNumero("prodProdutoId", ECompara.IGUAL, prod.getId());
                        Sql sql = new Sql(prod, EComandoSQL.ATUALIZAR, fn1, pf);
                        sqls.add(sql);
                    }
                    service.executar(sqls);

                    // salva o xml no arquivo
                    File xml = new File("nfe/" + nota2.getEcfNotaEletronicaChave() + "-procCanNFe.xml");
                    try (FileWriter fw = new FileWriter(xml)) {
                        fw.write(nota2.getEcfNotaEletronicaXmlCancelado());
                    } catch (IOException io) {
                        log.error("Erro ao salvar o arquivo xml de cancelamento.", io);
                    }

                    // notifica
                    limpar();
                    Aguarde.getInstancia().setVisible(false);
                    JOptionPane.showMessageDialog(notaEletronica, "NFe cancelada com sucesso.\nArquivo gerado abaixo:\n\n"
                            + xml.getAbsolutePath(), "Nota Eletrônica", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    Aguarde.getInstancia().setVisible(false);
                    log.error("Erro na geracao da NFe de cancelamento.", ex);
                    JOptionPane.showMessageDialog(notaEletronica, "Problemas na geração do xml de cancelamento!\n\n" + ex.getMessage(), "Nota Eletrônica", JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
        Aguarde.getInstancia().setVisible(true);
    }

    /**
     * Metodo que inutiliza a NFe na sefaz.
     *
     * @param numero o numero na NFe a ser inutilizada.
     * @param obs o texto de motivo de inutilizacao.
     */
    private void inutilizar(final String numero, final String obs) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // gera o xml
                    ComandoGerarNFeInutilizada gerarNFe = new ComandoGerarNFeInutilizada(numero, obs);
                    gerarNFe.executar();
                    JAXBElement<TInutNFe> element = gerarNFe.getElement();
                    // envia o xml para sefaz
                    ComandoEnviarNFeInutilizada enviarNFe = new ComandoEnviarNFeInutilizada(element);
                    enviarNFe.executar();
                    // salva o registro no banco
                    EcfNotaEletronica nota = enviarNFe.getNota();
                    nota.setSisCliente(null);
                    nota.setSisEmpresa(Caixa.getInstancia().getEmpresa());
                    service.salvar(nota);

                    // salva o xml no arquivo
                    File xml = new File("nfe/" + nota.getEcfNotaEletronicaChave() + "-procInutNFe.xml");
                    try (FileWriter fw = new FileWriter(xml)) {
                        fw.write(nota.getEcfNotaEletronicaXml());
                    } catch (IOException io) {
                        log.error("Erro ao salvar o arquivo xml de inutilizacao.", io);
                    }

                    // notifica
                    limpar();
                    Aguarde.getInstancia().setVisible(false);
                    JOptionPane.showMessageDialog(notaEletronica, "NFe inutilizada com sucesso.\nArquivo gerado abaixo:\n\n"
                            + xml.getAbsolutePath(), "Nota Eletrônica", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    Aguarde.getInstancia().setVisible(false);
                    log.error("Erro na geracao da NFe de inutilizacao.", ex);
                    JOptionPane.showMessageDialog(notaEletronica, "Problemas na geração do xml de inutilização!\n\n" + ex.getMessage(), "Nota Eletrônica", JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
        Aguarde.getInstancia().setVisible(true);
    }

    /**
     * Metodo que sai da tela.
     */
    private void cancelar() {
        dispose();
        Caixa.getInstancia().setJanela(null);
    }

    /**
     * Metodo que analisa a NFe pendente de confirmacao.
     *
     * @param nota o registro da nota salva.
     */
    private void analisarPendente(final EcfNotaEletronica nota, final List<EcfNotaProduto> produtos) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // solicita a confirmacao na sefaz
                    ComandoRecuperarNFe recNFe = new ComandoRecuperarNFe(nota);
                    recNFe.executar();
                    // se tudo ok salva o registro
                    service.salvar(nota);
                    Aguarde.getInstancia().setVisible(false);

                    // atualiza o estoque
                    List<Sql> sqls = new ArrayList<>();
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
                        sqls.add(sql);
                    }
                    service.executar(sqls);

                    // salva o xml no arquivo
                    File xml = new File("nfe/" + nota.getEcfNotaEletronicaChave() + "-procNFe.xml");
                    try (FileWriter fw = new FileWriter(xml)) {
                        fw.write(nota.getEcfNotaEletronicaXml());
                    } catch (IOException io) {
                        log.error("Erro ao salvar o arquivo xml.", io);
                    }

                    // salva o danfe no arquivo
                    File danfe = new File("nfe/" + nota.getEcfNotaEletronicaChave() + "-procNFe.pdf");
                    try (FileOutputStream fos = new FileOutputStream(danfe)) {
                        byte[] pdf = NFe.getDanfe(nota.getEcfNotaEletronicaXml());
                        fos.write(pdf);
                        fos.flush();
                    } catch (IOException io) {
                        log.error("Erro ao salvar o arquivo danfe.", io);
                    }

                    limpar();
                    Aguarde.getInstancia().setVisible(false);
                    Aguarde.getInstancia().getLblMensagem().setText("Aguarde o processamento...");
                    JOptionPane.showMessageDialog(notaEletronica, "NFe autorizada com sucesso.\nArquivos gerados abaixo:\n\n"
                            + xml.getAbsolutePath() + "\n" + danfe.getAbsolutePath(), "Nota Eletrônica", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    log.error("Erro na recuperacao do protocolo da NFe.", ex);
                    Aguarde.getInstancia().getLblMensagem().setText("Aguarde o processamento...");
                    Aguarde.getInstancia().setVisible(false);

                    // salva o erro no arquivo para analise.
                    File arquivo = new File("nfe/" + nota.getEcfNotaEletronicaChave() + ".err");
                    try (FileWriter fw = new FileWriter(arquivo)) {
                        fw.write(ex.getMessage());
                    } catch (IOException io) {
                        log.error("Erro ao salvar o arquivo com detalhes na sefaz.", io);
                    }

                    // informa ao operador e solicita tentar ou deletar.
                    int escolha = JOptionPane.showOptionDialog(notaEletronica, "A Sefaz não autorizou a NFe.\nVeja os detalhes no arquivo:\n"
                            + arquivo.getAbsolutePath() + "\n\nO que deseja fazer?", "Nota Eletrônica", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, new String[]{"Tentar Novamente", "Deletar"}, JOptionPane.YES_OPTION);

                    if (escolha == JOptionPane.YES_OPTION) {
                        analisarPendente(nota, produtos);
                    } else {
                        try {
                            service.deletar(nota);
                        } catch (OpenPdvException ex1) {
                            log.error("Erro ao deletar a nota salva e pendente.", ex);
                        }
                    }
                }
            }
        }).start();

        Aguarde.getInstancia().getLblMensagem().setText("Autorizando na sefaz...");
        Aguarde.getInstancia().setVisible(true);
    }

    /**
     * Metodo que valida os prdoutos da nota
     *
     * @param produtos a lista de produtos
     * @param nota a nota atual
     */
    private List<EcfNotaProduto> validarProdutos() throws OpenPdvException {
        try {
            List<EcfNotaProduto> produtos = new ArrayList<>();
            for (int i = 0; i < dtmProdutos.getRowCount(); i++) {
                EcfNotaProduto np = new EcfNotaProduto();
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
            return produtos;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Problemas com os dados do produtos!", "Nota Eletrônica", JOptionPane.WARNING_MESSAGE);
            throw new OpenPdvException(ex);
        }
    }

    /**
     * Metodo que valida o cliente informado.
     *
     * @return um objeto de cliente.
     * @throws OpenPdvException caso os dados informados nao sejam validos.
     */
    private SisCliente validarCliente() throws OpenPdvException {
        String texto = txtCPF.getText();
        texto = texto.replaceAll("[^0-9]", "");
        boolean valido = texto.length() == 11 ? Util.isCPF(texto) : Util.isCNPJ(texto);

        if (valido) {
            FiltroTexto ft = new FiltroTexto("sisClienteDoc", ECompara.IGUAL, texto);
            SisCliente cliente = (SisCliente) service.selecionar(new SisCliente(), ft);
            // caso nao tenha cria
            if (cliente == null) {
                cliente = new SisCliente();
            }
            cliente.setSisClienteDoc(texto);
            cliente.setSisClienteCadastrado(new Date());
            cliente.setSisClienteNome(txtNome.getText().toUpperCase());
            cliente.setSisClienteEndereco(txtEndereco.getText().toUpperCase() + ", " + txtNumero.getText());
            cliente = (SisCliente) service.salvar(cliente);
            // seleciona o objeto de municipio
            String[] mun = cmbMunicipio.getSelectedItem().toString().split(" - ");
            FiltroNumero fn = new FiltroNumero("sisMunicipioIbge", ECompara.IGUAL, mun[1]);
            SisMunicipio municipio = (SisMunicipio) service.selecionar(new SisMunicipio(), fn);
            // outros dados do cliente
            cliente.setIe(txtIE.getText());
            cliente.setNumero(Integer.valueOf(txtNumero.getText()));
            cliente.setBairro(txtBairro.getText().toUpperCase());
            cliente.setCep(txtCEP.getValue().toString());
            cliente.setSisMunicipio(municipio);
            cliente.setFone(txtFone.getValue() == null ? "" : txtFone.getValue().toString());
            cliente.setEmail(txtEmail.getText().toUpperCase());
            return cliente;
        } else {
            JOptionPane.showMessageDialog(this, "Problemas com os dados do cliente!", "Nota Eletrônica", JOptionPane.WARNING_MESSAGE);
            throw new OpenPdvException();
        }
    }

    /**
     * Metodo que totaliza os valores dos produtos adicionados.
     */
    private void totalizar() {
        try {
            List<EcfNotaProduto> nps = validarProdutos();
            Double liquido = 0.00;
            for (EcfNotaProduto np : nps) {
                liquido += np.getEcfNotaProdutoLiquido() * np.getEcfNotaProdutoQuantidade();
            }
            lblTotal.setText("R$ " + Util.formataNumero(liquido, 1, 2, true));
        } catch (OpenPdvException ex) {
            // nao altera nada
        }
    }

    /**
     * Metodo que carrega os valores das UFs.
     */
    private void setUF() {
        try {
            List<SisEstado> lista = service.selecionar(new SisEstado(), 0, 0, null);
            for (SisEstado est : lista) {
                cmbUF.addItem(est.getSisEstadoSigla());
            }
            cmbUF.setSelectedIndex(Caixa.getInstancia().getEmpresa().getSisMunicipio().getSisEstado().getId() - 1);
            setMunicipio();
        } catch (OpenPdvException ex) {
            log.error("Nao carregou as UFs.", ex);
        }
    }

    /**
     * Metodo que carrega os valores dos municipios.
     */
    private void setMunicipio() {
        try {
            FiltroObjeto fo = new FiltroObjeto("sisEstado", ECompara.IGUAL, new SisEstado(Caixa.getInstancia().getEmpresa().getSisMunicipio().getSisEstado().getId()));
            List<SisMunicipio> lista = service.selecionar(new SisMunicipio(), 0, 0, fo);
            for (SisMunicipio mun : lista) {
                cmbMunicipio.addItem(mun.getSisMunicipioDescricao() + " - " + mun.getSisMunicipioIbge());
            }
        } catch (OpenPdvException ex) {
            log.error("Nao carregou as cidades.", ex);
        } finally {
            cmbMunicipio.setSelectedIndex(-1);
        }
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

    public JButton getBtnExcluir() {
        return btnExcluir;
    }

    public void setBtnExcluir(JButton btnExcluir) {
        this.btnExcluir = btnExcluir;
    }

    public JButton getBtnGerar() {
        return btnGerar;
    }

    public void setBtnGerar(JButton btnGerar) {
        this.btnGerar = btnGerar;
    }

    public JButton getBtnInutilizar() {
        return btnInutilizar;
    }

    public void setBtnInutilizar(JButton btnInutilizar) {
        this.btnInutilizar = btnInutilizar;
    }

    public JButton getBtnRemover() {
        return btnRemover;
    }

    public void setBtnRemover(JButton btnRemover) {
        this.btnRemover = btnRemover;
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

    public DefaultTableModel getDtmProdutos() {
        return dtmProdutos;
    }

    public void setDtmProdutos(DefaultTableModel dtmProdutos) {
        this.dtmProdutos = dtmProdutos;
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

    public JLabel getLblCPF() {
        return lblCPF;
    }

    public void setLblCPF(JLabel lblCPF) {
        this.lblCPF = lblCPF;
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

    public JLabel getLblFone() {
        return lblFone;
    }

    public void setLblFone(JLabel lblFone) {
        this.lblFone = lblFone;
    }

    public JLabel getLblIE() {
        return lblIE;
    }

    public void setLblIE(JLabel lblIE) {
        this.lblIE = lblIE;
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

    public JLabel getLblTotal() {
        return lblTotal;
    }

    public void setLblTotal(JLabel lblTotal) {
        this.lblTotal = lblTotal;
    }

    public JLabel getLblTotalNota() {
        return lblTotalNota;
    }

    public void setLblTotalNota(JLabel lblTotalNota) {
        this.lblTotalNota = lblTotalNota;
    }

    public JLabel getLblUF() {
        return lblUF;
    }

    public void setLblUF(JLabel lblUF) {
        this.lblUF = lblUF;
    }

    public AsyncCallback<ProdProduto> getPesquisado() {
        return pesquisado;
    }

    public void setPesquisado(AsyncCallback<ProdProduto> pesquisado) {
        this.pesquisado = pesquisado;
    }

    public JSeparator getSeparador() {
        return separador;
    }

    public void setSeparador(JSeparator separador) {
        this.separador = separador;
    }

    public CoreService getService() {
        return service;
    }

    public void setService(CoreService service) {
        this.service = service;
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

    public JTextField getTxtCPF() {
        return txtCPF;
    }

    public void setTxtCPF(JTextField txtCPF) {
        this.txtCPF = txtCPF;
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

    public JFormattedTextField getTxtFone() {
        return txtFone;
    }

    public void setTxtFone(JFormattedTextField txtFone) {
        this.txtFone = txtFone;
    }

    public JTextField getTxtIE() {
        return txtIE;
    }

    public void setTxtIE(JTextField txtIE) {
        this.txtIE = txtIE;
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
}
