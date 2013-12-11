package br.com.openpdv.visao.nota;

import br.com.openpdv.controlador.comandos.*;
import br.com.openpdv.controlador.core.*;
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
import br.com.openpdv.modelo.produto.ProdGrade;
import br.com.openpdv.modelo.produto.ProdPreco;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.visao.core.Aguarde;
import br.com.openpdv.visao.core.Caixa;
import br.com.openpdv.visao.principal.Pesquisa;
import br.com.openpdv.visao.venda.Grades;
import br.com.openpdv.visao.venda.Precos;
import br.com.opensig.eventocancnfe.TEvento;
import br.com.opensig.inutnfe.TInutNFe;
import br.com.opensig.nfe.TNFe;
import br.com.opensig.nfe.TNFe.InfNFe.Det;
import br.com.opensig.nfe.TNfeProc;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
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
    private SisCliente cliente;
    /**
     * Variavel que responde de modo assincrono a pesquisa de produto.
     */
    private AsyncCallback<ProdProduto> pesquisado = new AsyncCallback<ProdProduto>() {
        @Override
        public void sucesso(final ProdProduto prod) {
            if (prod == null) {
                JOptionPane.showMessageDialog(notaEletronica, "Produto não encontrado.", "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
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
                qtd, prod.getProdProdutoPreco(), 0.00, prod.getProdProdutoPreco(), prod.getProdProdutoPreco(), dtmProdutos.getRowCount() + 1};
            dtmProdutos.addRow(obj);
            totalizar();
        }

        private void erro(Exception ex) {
            log.error(ex);
            JOptionPane.showMessageDialog(notaEletronica, "Não foi possível adicionar o produto!", "Pesquisa", JOptionPane.WARNING_MESSAGE);
            btnAdicionar.requestFocus();
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
                    Double liquido = bruto - desc;
                    Double total = liquido * qtd;
                    model.setValueAt(liquido, row, 7);
                    model.setValueAt(total, row, 8);
                    totalizar();
                }
            }
        });

        // colocando limites nos campos
        txtCPF_CNPJ.setDocument(new TextFieldLimit(18));
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

        lblCPF_CNPJ = new javax.swing.JLabel();
        txtCPF_CNPJ = new javax.swing.JTextField();
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

        lblCPF_CNPJ.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCPF_CNPJ.setText("CPF/CNPJ:");

        txtCPF_CNPJ.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCPF_CNPJ.setToolTipText("CPF ou CNPJ.");
        txtCPF_CNPJ.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCPF_CNPJFocusLost(evt);
            }
        });

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
        tabProdutos.getColumnModel().getColumn(2).setPreferredWidth(300);
        tabProdutos.getColumnModel().getColumn(3).setResizable(false);
        tabProdutos.getColumnModel().getColumn(3).setPreferredWidth(75);
        tabProdutos.getColumnModel().getColumn(4).setResizable(false);
        tabProdutos.getColumnModel().getColumn(4).setPreferredWidth(50);
        tabProdutos.getColumnModel().getColumn(4).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getNumberInstance()));
        tabProdutos.getColumnModel().getColumn(5).setResizable(false);
        tabProdutos.getColumnModel().getColumn(5).setPreferredWidth(75);
        tabProdutos.getColumnModel().getColumn(5).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
        tabProdutos.getColumnModel().getColumn(6).setResizable(false);
        tabProdutos.getColumnModel().getColumn(6).setPreferredWidth(75);
        tabProdutos.getColumnModel().getColumn(6).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
        tabProdutos.getColumnModel().getColumn(7).setResizable(false);
        tabProdutos.getColumnModel().getColumn(7).setPreferredWidth(75);
        tabProdutos.getColumnModel().getColumn(7).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
        tabProdutos.getColumnModel().getColumn(8).setResizable(false);
        tabProdutos.getColumnModel().getColumn(8).setPreferredWidth(100);
        tabProdutos.getColumnModel().getColumn(8).setCellRenderer(new TableCellRendererNumber(DecimalFormat.getCurrencyInstance()));
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
                            .add(btnAdicionar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(btnRemover, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(lblCPF_CNPJ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(txtCPF_CNPJ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 215, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(lblTotalNota, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(lblTotal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE, false)
                    .add(btnRemover, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblTotal, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblTotalNota)
                    .add(btnAdicionar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblCPF_CNPJ)
                    .add(txtCPF_CNPJ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spProdutos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 309, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnGerar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnExcluir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnInutilizar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-790)/2, (screenSize.height-430)/2, 790, 430);
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
        if (cliente == null || lblTotal.getText().equals("R$ 0,00")) {
            JOptionPane.showMessageDialog(this, "O cliente é obrigatório!\nAdicione também produtos com valor maior que zero.", "Nota Eletrônica", JOptionPane.INFORMATION_MESSAGE);
            txtCPF_CNPJ.requestFocus();
        } else {
            // pega os produtos
            List<EcfNotaProduto> produtos = new ArrayList<>();
            if (validarProdutos(produtos)) {
                gerar(cliente, produtos);
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
            chave = chave.replaceAll("\\D", "");

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
            numero = numero.replaceAll("\\D", "");

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

    private void txtCPF_CNPJFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCPF_CNPJFocusLost
        validarCliente();
    }//GEN-LAST:event_txtCPF_CNPJFocusLost
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnGerar;
    private javax.swing.JButton btnInutilizar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JLabel lblCPF_CNPJ;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalNota;
    private javax.swing.JScrollPane spProdutos;
    private javax.swing.JTable tabProdutos;
    private javax.swing.JTextField txtCPF_CNPJ;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que limpa os campos para nova nota.
     */
    private void limpar() {
        txtCPF_CNPJ.setText("");
        lblTotal.setText("R$ 0,00");
        while (dtmProdutos.getRowCount() > 0) {
            dtmProdutos.removeRow(0);
        }
    }

    /**
     * Metodo que pesquisa o cliente pelo documento informado.
     */
    private void validarCliente() {
        try {
            String texto = txtCPF_CNPJ.getText().replaceAll("\\D", "");
            if (!texto.equals("")) {
                FiltroTexto ft = new FiltroTexto("sisClienteDoc", ECompara.IGUAL, texto);
                cliente = (SisCliente) service.selecionar(new SisCliente(), ft);
                if (cliente == null) {
                    throw new Exception();
                }
            }
        } catch (Exception ex) {
            cliente = null;
            txtCPF_CNPJ.setText("");
            JOptionPane.showMessageDialog(notaEletronica, "Cliente nao encontrado com o documento informado:\nCaso precise cadastre o cliente antes.", "Nota Eletrônica", JOptionPane.INFORMATION_MESSAGE);
            txtCPF_CNPJ.requestFocus();
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
                    JAXBElement<TEvento> element = gerarNFe.getElement();
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
                    service.executar(sqls.toArray(new Sql[]{}));

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
                    service.executar(sqls.toArray(new Sql[]{}));

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
                    JOptionPane.showMessageDialog(notaEletronica, "NFe autorizada com sucesso.\nArquivos gerados abaixo:\n\n"
                            + xml.getAbsolutePath() + "\n" + danfe.getAbsolutePath(), "Nota Eletrônica", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    log.error("Erro na recuperacao do protocolo da NFe.", ex);
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
                } finally {
                    Aguarde.getInstancia().setVisible(false);
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
    private boolean validarProdutos(List<EcfNotaProduto> produtos) {
        try {
            for (int i = 0; i < dtmProdutos.getRowCount(); i++) {
                String[] emb = dtmProdutos.getValueAt(i, 3).toString().split(" - ");
                Double bruto = (Double) dtmProdutos.getValueAt(i, 5);
                Double qtd = (Double) dtmProdutos.getValueAt(i, 4);
                Double desconto = (Double) dtmProdutos.getValueAt(i, 6);
                if (qtd <= 0.00 || bruto <= 0.00 || desconto < 0.00 || desconto >= bruto) {
                    throw new Exception();
                }
                
                EcfNotaProduto np = new EcfNotaProduto();
                np.setProdProduto((ProdProduto) dtmProdutos.getValueAt(i, 1));
                np.setEcfNotaProdutoBarra(np.getProdProduto().getProdProdutoBarra());
                np.setProdEmbalagem(new ProdEmbalagem(Integer.valueOf(emb[0])));
                np.setEcfNotaProdutoQuantidade(qtd);
                np.setEcfNotaProdutoBruto(bruto);
                np.setEcfNotaProdutoDesconto(desconto);
                np.setEcfNotaProdutoLiquido((Double) dtmProdutos.getValueAt(i, 7));
                np.setEcfNotaProdutoIcms(np.getProdProduto().getProdProdutoIcms());
                np.setEcfNotaProdutoIpi(0.00);
                np.setEcfNotaProdutoOrdem((Integer) dtmProdutos.getValueAt(i, 9));
                produtos.add(np);
            }
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Problemas com os dados do produtos!", "Nota Eletrônica", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    /**
     * Metodo que totaliza os valores dos produtos adicionados.
     */
    private void totalizar() {
        List<EcfNotaProduto> nps = new ArrayList<>();
        validarProdutos(nps);

        Double liquido = 0.00;
        for (EcfNotaProduto np : nps) {
            liquido += np.getEcfNotaProdutoLiquido() * np.getEcfNotaProdutoQuantidade();
        }
        lblTotal.setText("R$ " + Util.formataNumero(liquido, 1, 2, true));
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

    public DefaultTableModel getDtmProdutos() {
        return dtmProdutos;
    }

    public void setDtmProdutos(DefaultTableModel dtmProdutos) {
        this.dtmProdutos = dtmProdutos;
    }

    public JLabel getLblCPF() {
        return lblCPF_CNPJ;
    }

    public void setLblCPF(JLabel lblCPF) {
        this.lblCPF_CNPJ = lblCPF;
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

    public AsyncCallback<ProdProduto> getPesquisado() {
        return pesquisado;
    }

    public void setPesquisado(AsyncCallback<ProdProduto> pesquisado) {
        this.pesquisado = pesquisado;
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

    public JTextField getTxtCPF() {
        return txtCPF_CNPJ;
    }

    public void setTxtCPF(JTextField txtCPF) {
        this.txtCPF_CNPJ = txtCPF;
    }
}
