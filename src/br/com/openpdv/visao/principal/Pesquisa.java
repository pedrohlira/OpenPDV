package br.com.openpdv.visao.principal;

import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.TableCellRendererNumber;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 * Classe que representa a pesquisa de produtos do sistema.
 *
 * @author Pedro H. Lira
 */
public class Pesquisa extends javax.swing.JDialog {

    private static Pesquisa pesquisa;
    private Logger log;
    private AsyncCallback<ProdProduto> async;
    private CoreService<ProdProduto> service;

    /**
     * Construtor padrao.
     */
    private Pesquisa() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Pesquisa.class);
        initComponents();
        service = new CoreService<>();
    }

    /**
     * Metodo que retorna a instancia do componente.
     *
     * @param async o metodo assincrono de resposta.
     * @return o objeto do compoente.
     */
    public static Pesquisa getInstancia(AsyncCallback<ProdProduto> async) {
        if (pesquisa == null) {
            pesquisa = new Pesquisa();
        }

        pesquisa.async = async;
        pesquisa.txtFiltro.setText("");
        pesquisa.setLista(null);
        return pesquisa;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblFiltro = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        spProdutos = new javax.swing.JScrollPane();
        tabProdutos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Produtos");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
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
        tabProdutos.setShowVerticalLines(false);
        tabProdutos.getTableHeader().setReorderingAllowed(false);
        tabProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabProdutosMouseClicked(evt);
            }
        });
        tabProdutos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabProdutosKeyPressed(evt);
            }
        });
        spProdutos.setViewportView(tabProdutos);
        tabProdutos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tabProdutos.getColumnModel().getColumnCount() > 0) {
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
        }

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(spProdutos)
                    .add(layout.createSequentialGroup()
                        .add(lblFiltro)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtFiltro, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnPesquisar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
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
                            .add(btnPesquisar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(7, 7, 7)
                .add(spProdutos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(892, 535));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        Filtro filtro = pesquisar(txtFiltro.getText().toUpperCase());
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

    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            dispose();
            Caixa.getInstancia().setJanela(null);
        }
    }//GEN-LAST:event_btnCancelarKeyPressed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
        Caixa.getInstancia().setJanela(null);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void tabProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabProdutosMouseClicked
        if (evt.getClickCount() == 2) {
            selecionar();
        }
    }//GEN-LAST:event_tabProdutosMouseClicked

    private void tabProdutosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabProdutosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            selecionar();
        }
    }//GEN-LAST:event_tabProdutosKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Caixa.getInstancia().setJanela(null);
    }//GEN-LAST:event_formWindowClosing

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        txtFiltro.requestFocus();
    }//GEN-LAST:event_formWindowActivated
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JScrollPane spProdutos;
    private javax.swing.JTable tabProdutos;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que realiza gera o filtro da listagem.
     *
     * @param texto o valor a ser pesquisado.
     * @return um objeto de filtro.
     */
    public static Filtro pesquisar(String texto) {
        FiltroGrupo filtro = new FiltroGrupo();
        FiltroBinario fb = new FiltroBinario("prodProdutoAtivo", ECompara.IGUAL, true);
        filtro.add(fb, Filtro.E);
        ECompara compara;

        if (texto.contains("/")) {
            // identifica se foi informado alguma comparacao, ou usa o padrao
            if (texto.startsWith(">=")) {
                compara = ECompara.MAIOR_IGUAL;
                texto = texto.substring(2);
            } else if (texto.startsWith("<=")) {
                compara = ECompara.MENOR_IGUAL;
                texto = texto.substring(2);
            } else if (texto.startsWith(">")) {
                compara = ECompara.MAIOR;
                texto = texto.substring(1);
            } else if (texto.startsWith("<")) {
                compara = ECompara.MENOR;
                texto = texto.substring(1);
            } else {
                compara = ECompara.IGUAL;
            }

            // verifica se e data completa
            try {
                Date data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(texto);
                FiltroData fd1 = new FiltroData("prodProdutoCadastrado", compara, data);
                FiltroData fd2 = new FiltroData("prodProdutoAlterado", compara, data);
                filtro.add(new FiltroGrupo(Filtro.OU, fd1, fd2));
            } catch (ParseException ex) {
                // verifica se e data simples
                try {
                    // encontra o rande de datas
                    Date inicio = new SimpleDateFormat("dd/MM/yyyy").parse(texto);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(inicio);
                    cal.set(Calendar.HOUR, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    Date fim = cal.getTime();

                    if (compara == ECompara.IGUAL) {
                        // monta o grupo para cadastrado
                        FiltroData fd1 = new FiltroData("prodProdutoCadastrado", ECompara.MAIOR_IGUAL, inicio);
                        FiltroData fd2 = new FiltroData("prodProdutoCadastrado", ECompara.MENOR_IGUAL, fim);
                        FiltroGrupo cad = new FiltroGrupo(Filtro.E, fd1, fd2);
                        // monta o grupo para alterado
                        FiltroData fd3 = new FiltroData("prodProdutoAlterado", ECompara.MAIOR_IGUAL, inicio);
                        FiltroData fd4 = new FiltroData("prodProdutoAlterado", ECompara.MENOR_IGUAL, fim);
                        FiltroGrupo alt = new FiltroGrupo(Filtro.E, fd3, fd4);
                        // combinando os filtros
                        filtro.add(new FiltroGrupo(Filtro.OU, cad, alt));
                    } else {
                        FiltroData fd1 = new FiltroData("prodProdutoCadastrado", compara, inicio);
                        FiltroData fd2 = new FiltroData("prodProdutoAlterado", compara, inicio);
                        filtro.add(new FiltroGrupo(Filtro.OU, fd1, fd2));
                    }
                } catch (ParseException ex1) {
                    pesquisa.log.debug("Nao filtrou por data.", ex1);
                    JOptionPane.showMessageDialog(pesquisa, "O sistema não conseguiu filtrar pelos campos de data!", "Produtos", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            // verifica se e longo
            try {
                long valor = Long.valueOf(texto);
                // codigo
                FiltroNumero fn = new FiltroNumero("prodProdutoId", ECompara.IGUAL, valor);
                // barra
                FiltroTexto ft = new FiltroTexto("prodProdutoBarra", ECompara.IGUAL, texto);
                // barra do preco
                FiltroTexto ft1 = new FiltroTexto("t1.prodPrecoBarra", ECompara.IGUAL, texto);
                // barra da grade
                FiltroTexto ft2 = new FiltroTexto("t2.prodGradeBarra", ECompara.IGUAL, texto);
                // referencia
                FiltroTexto ft3 = new FiltroTexto("prodProdutoReferencia", ECompara.CONTEM, texto);
                filtro.add(new FiltroGrupo(Filtro.OU, fn, ft, ft1, ft2, ft3));
            } catch (NumberFormatException ex) {
                // verifica se e decimal
                try {
                    double valor = Double.valueOf(texto.replace(".", "").replace(",", "."));
                    // preco
                    FiltroNumero fn = new FiltroNumero("prodProdutoPreco", ECompara.IGUAL, valor);
                    // estoque
                    FiltroNumero fn1 = new FiltroNumero("prodProdutoEstoque", ECompara.IGUAL, valor);
                    // barra do preco
                    FiltroNumero fn2 = new FiltroNumero("t1.prodPrecoValor", ECompara.IGUAL, valor);
                    filtro.add(new FiltroGrupo(Filtro.OU, fn, fn1, fn2));
                } catch (NumberFormatException ex1) {
                    if (texto.startsWith("%")) {
                        compara = ECompara.CONTEM_FIM;
                    } else if (texto.endsWith("%")) {
                        compara = ECompara.CONTEM_INICIO;
                    } else {
                        compara = ECompara.CONTEM;
                    }
                    texto = texto.replace("%", "");

                    // descricao
                    FiltroTexto ft1 = new FiltroTexto("prodProdutoDescricao", compara, texto);
                    // referencia
                    FiltroTexto ft2 = new FiltroTexto("prodProdutoReferencia", compara, texto);
                    // barra
                    FiltroTexto ft3 = new FiltroTexto("prodProdutoBarra", compara, texto);
                    // barra do preco
                    FiltroTexto ft4 = new FiltroTexto("t1.prodPrecoBarra", compara, texto);
                    // barra da grade
                    FiltroTexto ft5 = new FiltroTexto("t2.prodGradeBarra", compara, texto);
                    filtro.add(new FiltroGrupo(Filtro.OU, ft1, ft2, ft3, ft4, ft5));
                }
            }
        }
        return filtro;
    }

    /**
     * Metodo que seta os valores da tabela vindas do banco de dados.
     */
    private void setLista(Filtro filtro) {
        try {
            DefaultTableModel dtmProduto = (DefaultTableModel) tabProdutos.getModel();
            while (dtmProduto.getRowCount() > 0) {
                dtmProduto.removeRow(0);
            }

            if (filtro != null) {
                List<ProdProduto> lista = service.selecionar(new ProdProduto(), 0, 0, filtro);
                for (ProdProduto prod : lista) {
                    String cadastrado = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(prod.getProdProdutoCadastrado());
                    String alterado = prod.getProdProdutoAlterado() == null ? "" : new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(prod.getProdProdutoAlterado());
                    String barra = prod.getProdProdutoBarra() == null ? "" : prod.getProdProdutoBarra();

                    Object[] obj = new Object[]{prod.getId(), prod.getProdProdutoNcm(), barra, prod.getProdProdutoDescricao(), prod.getProdProdutoReferencia(),
                        prod.getProdProdutoPreco(), prod.getProdEmbalagem().getId(), prod.getProdEmbalagem().getProdEmbalagemNome(), prod.getProdProdutoEstoque(), prod.getProdProdutoTipo(), "" + prod.getProdProdutoOrigem(),
                        prod.getProdProdutoCstCson(), prod.getProdProdutoTributacao(), prod.getProdProdutoIcms(), prod.getProdProdutoIssqn(), "" + prod.getProdProdutoIat(), "" + prod.getProdProdutoIppt(),
                        cadastrado, alterado, prod.getProdProdutoAtivo()};
                    dtmProduto.addRow(obj);
                }

                if (lista.size() > 0) {
                    tabProdutos.addRowSelectionInterval(0, 0);
                    tabProdutos.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhum produto encontrado!", "Produtos", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (OpenPdvException ex) {
            log.error("Erro ao selecionar os produtos do sistema", ex);
        }
    }

    /**
     * Metodo que realiza a selecao do produto.
     */
    private void selecionar() {
        int row = tabProdutos.convertRowIndexToModel(tabProdutos.getSelectedRow());
        if (row >= 0) {
            dispose();
            Caixa.getInstancia().setJanela(null);

            int cod = Integer.valueOf(tabProdutos.getModel().getValueAt(row, 0).toString());
            FiltroNumero fn = new FiltroNumero("prodProdutoId", ECompara.IGUAL, cod);
            selecionar(fn);
        }
    }

    /**
     * Metodo que realiza a selecao do produto.
     *
     * @param filtro informa o filtro a ser usado.
     */
    public void selecionar(Filtro filtro) {
        try {
            List<ProdProduto> lista = service.selecionar(new ProdProduto(), 0, 2, filtro);
            if (lista.isEmpty()) {
                async.sucesso(null);
            } else if (lista.size() == 1) {
                async.sucesso(lista.get(0));
            } else {
                throw new OpenPdvException("Existe mais de um registro encontrado.\nUse a tecla [F5] para identificá-lo.");
            }

        } catch (OpenPdvException ex) {
            async.falha(ex);
        }
    }

    // GETs e SETs
    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
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

    public JTextField getTxtFiltro() {
        return txtFiltro;
    }

    public void setTxtFiltro(JTextField txtFiltro) {
        this.txtFiltro = txtFiltro;
    }
}
