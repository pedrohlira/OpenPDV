package br.com.openpdv.visao.core;

import br.com.openpdv.visao.principal.Cat52;
import br.com.openpdv.controlador.comandos.*;
import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.controlador.core.AsyncDoubleBack;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.permissao.Login;
import br.com.openpdv.modelo.core.EDirecao;
import br.com.openpdv.modelo.core.EModo;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroData;
import br.com.openpdv.modelo.core.filtro.FiltroGrupo;
import br.com.openpdv.modelo.core.filtro.Filtro;
import br.com.openpdv.modelo.core.filtro.FiltroBinario;
import br.com.openpdv.modelo.core.filtro.FiltroNumero;
import br.com.openpdv.modelo.core.filtro.FiltroObjeto;
import br.com.openpdv.modelo.ecf.EcfDocumento;
import br.com.openpdv.modelo.ecf.EcfImpressora;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.ecf.EcfVendaProduto;
import br.com.openpdv.modelo.ecf.EcfZ;
import br.com.openpdv.modelo.produto.ProdComposicao;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdGrade;
import br.com.openpdv.modelo.produto.ProdPreco;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.openpdv.modelo.sistema.SisUsuario;
import br.com.openpdv.visao.fiscal.PAF_LMF;
import br.com.openpdv.visao.fiscal.PAF_VendasPeriodo;
import br.com.openpdv.visao.fiscal.PAF_Registros;
import br.com.openpdv.visao.nota.NotaConsumidor;
import br.com.openpdv.visao.nota.NotaEletronica;
import br.com.openpdv.visao.principal.*;
import br.com.openpdv.visao.principal.LeiturasZ;
import br.com.openpdv.visao.venda.Fechamento;
import br.com.openpdv.visao.venda.Grades;
import br.com.openpdv.visao.venda.Identificar;
import br.com.openpdv.visao.venda.Precos;
import br.com.phdss.ECF;
import br.com.phdss.EComando;
import br.com.phdss.IECF;
import br.com.phdss.TEF;
import br.com.phdss.Util;
import br.com.phdss.controlador.PAF;
import java.awt.Cursor;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.JPopupMenu.Separator;
import org.apache.log4j.Logger;

/**
 * Classe que representa o caixa, para executar as operacoes de venda.
 *
 * @author Pedro H. Lira
 */
public class Caixa extends JFrame {

    private static Caixa caixa;
    private Logger log;
    private CoreService service;
    private SisEmpresa empresa;
    private EcfVenda venda;
    private EcfImpressora impressora;
    private EModo modo;
    private JDialog janela;
    private JOptionPane option;
    private DefaultListModel bobina;
    private KeyEventPostProcessor teclas;
    private IECF ecf;
    /**
     * Variavel de sincronismo dos precos adicionais.
     */
    private AsyncDoubleBack<ProdProduto, ProdPreco> asyncPreco = new AsyncDoubleBack<ProdProduto, ProdPreco>() {
        @Override
        public void sucesso(ProdProduto prod, ProdPreco preco) {
            try {
                adicionar(prod, preco.getProdEmbalagem(), preco.getProdPrecoValor(), Double.valueOf(txtQuantidade.getText()), preco.getProdPrecoBarra());
            } catch (OpenPdvException ex) {
                falha(ex);
            }
        }

        @Override
        public void falha(Exception excecao) {
            log.error(excecao);
            JOptionPane.showMessageDialog(caixa, "Não foi possível adicionar o produto!", "Venda", JOptionPane.WARNING_MESSAGE);
        }
    };
    /**
     * Variavel de sincronismo das grades.
     */
    AsyncDoubleBack<ProdProduto, ProdGrade> asyncGrade = new AsyncDoubleBack<ProdProduto, ProdGrade>() {
        @Override
        public void sucesso(ProdProduto prod, ProdGrade grade) {
            try {
                adicionar(prod, prod.getProdEmbalagem(), prod.getProdProdutoPreco(), Double.valueOf(txtQuantidade.getText()), grade.getProdGradeBarra());
            } catch (OpenPdvException ex) {
                falha(ex);
            }
        }

        @Override
        public void falha(Exception excecao) {
            log.error(excecao);
            JOptionPane.showMessageDialog(caixa, "Não foi possível adicionar o produto!", "Venda", JOptionPane.WARNING_MESSAGE);
        }
    };
    /**
     * Variavel que responde de modo assincrono a pesquisa de produto.
     */
    private AsyncCallback<ProdProduto> pesquisado = new AsyncCallback<ProdProduto>() {
        @Override
        public void sucesso(final ProdProduto prod) {
            if (prod == null) {
                JOptionPane.showMessageDialog(caixa, "Produto não encontrado.", "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
            } else if (modo == EModo.ABERTO) {
                if (!prod.getProdPrecos().isEmpty()) {
                    // procura a barra nos precos
                    boolean achou = false;
                    for (ProdPreco preco : prod.getProdPrecos()) {
                        if (txtCodigo.getText().equals(preco.getProdPrecoBarra())) {
                            asyncPreco.sucesso(prod, preco);
                            achou = true;
                            break;
                        }
                    }
                    // se nao achou abre janela
                    if (!achou) {
                        Precos.getInstancia(asyncPreco, prod).setVisible(true);
                    }
                } else if (!prod.getProdComposicoes().isEmpty()) {
                    // abre tela pra informa que o produto e um kit de varios produtos
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // percorre os itens do produto
                            for (ProdComposicao comp : prod.getProdComposicoes()) {
                                ProdProduto prod = comp.getProdProduto();
                                double preco = comp.getProdComposicaoValor() / comp.getProdComposicaoQuantidade();
                                double qtd = comp.getProdComposicaoQuantidade() * Double.valueOf(txtQuantidade.getText());

                                try {
                                    adicionar(prod, comp.getProdEmbalagem(), preco, qtd, prod.getProdProdutoBarra());
                                } catch (OpenPdvException ex) {
                                    log.error(ex);
                                    JOptionPane.showMessageDialog(caixa, "Não foi possível adicionar um item!\nCancele os itens adicionados.", "Venda", JOptionPane.WARNING_MESSAGE);
                                    break;
                                }
                            }
                            Aguarde.getInstancia().setVisible(false);
                        }
                    }).start();

                    Aguarde.getInstancia().setVisible(true);
                } else if (!prod.getProdGrades().isEmpty()) {
                    // procura a barra nas grades
                    boolean achou = false;
                    for (ProdGrade grade : prod.getProdGrades()) {
                        if (txtCodigo.getText().equals(grade.getProdGradeBarra())) {
                            asyncGrade.sucesso(prod, grade);
                            achou = true;
                            break;
                        }
                    }
                    // se nao achou abre janela
                    if (!achou) {
                        Grades.getInstancia(asyncGrade, prod).setVisible(true);
                    }
                } else {
                    try {
                        adicionar(prod, prod.getProdEmbalagem(), prod.getProdProdutoPreco(), Double.valueOf(txtQuantidade.getText()), prod.getProdProdutoBarra());
                    } catch (OpenPdvException ex) {
                        log.error(ex);
                        JOptionPane.showMessageDialog(caixa, "Não foi possível adicionar o produto!", "Venda", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else {
                lblProduto.setText(Util.normaliza(prod.getProdProdutoDescricao()));
                lblProduto.setToolTipText(lblProduto.getText());
                lblTotal.setText(DecimalFormat.getCurrencyInstance().format(prod.getProdProdutoPreco()));
            }

            txtCodigo.setText("");
            txtCodigo.requestFocus();
        }

        @Override
        public void falha(Exception excecao) {
            JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
            txtCodigo.setText("");
            txtCodigo.requestFocus();
        }
    };

    /**
     * Construtor padrao.
     */
    private Caixa() {
        log = Logger.getLogger(Caixa.class);
        service = new CoreService();
        initComponents();
        bobina = new DefaultListModel();
        lstBobina.setModel(bobina);
        ImageIcon mini = new ImageIcon("conf" + System.getProperty("file.separator") + "logo_mini.png");
        lblLogo.setIcon(mini);
        ImageIcon logo = new ImageIcon("conf" + System.getProperty("file.separator") + "logo.png");
        lblLivre.setIcon(logo);
        modo = EModo.OFF;
        ecf = ECF.getInstancia();

        // mapeando as teclas apertadas
        teclas = new KeyEventPostProcessor() {
            @Override
            public boolean postProcessKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1 && mnuSobre.isEnabled()) { // Sobre
                    mnuSobreMouseClicked(null);
                } else if (e.getKeyCode() == KeyEvent.VK_F2 && mnuPrincipal.isEnabled() && janela == null) { // Menu Principal
                    mnuPrincipal.doClick();
                } else if (e.getKeyCode() == KeyEvent.VK_F3 && mnuFiscal.isEnabled() && modo != EModo.ABERTO) { // Menu Fiscal
                    if (janela != null) {
                        if (janela instanceof Autenticacao) {
                            modoConsulta();
                        }
                        janela.setVisible(false);
                        janela = null;
                    }
                    mnuFiscal.doClick();
                } else if (e.getKeyCode() == KeyEvent.VK_F4 && mnuNota.isEnabled()) { // Nota Fiscal
                    mnuNota.doClick();
                } else if (e.getKeyCode() == KeyEvent.VK_F5 && mnuPesquisa.isEnabled()) { // Pesquisa
                    mnuPesquisaMouseClicked(null);
                } else if (e.getKeyCode() == KeyEvent.VK_F6 && mnuGaveta.isEnabled()) { // Menu Gaveta
                    mnuGavetaMouseClicked(null);
                } else if (e.getKeyCode() == KeyEvent.VK_F7 && mnuVenda.isEnabled() && mnuAbrirVenda.isEnabled()) { // Abrir Venda
                    mnuAbrirVendaActionPerformed(null);
                } else if (e.getKeyCode() == KeyEvent.VK_F8 && mnuVenda.isEnabled() && mnuFecharVenda.isEnabled()) { // Fechar Venda
                    mnuFecharVendaActionPerformed(null);
                } else if (e.getKeyCode() == KeyEvent.VK_F9 && mnuVenda.isEnabled() && mnuCancelarItem.isEnabled()) { // Cancelar Item
                    mnuCancelarItemActionPerformed(null);
                } else if (e.getKeyCode() == KeyEvent.VK_F10 && mnuVenda.isEnabled() && mnuCancelarVenda.isEnabled()) { // Cancelar Venda
                    mnuCancelarVendaActionPerformed(null);
                } else if (e.getKeyCode() == KeyEvent.VK_F11 && mnuIdentificar.isEnabled()) { // Identificar
                    mnuIdentificarMouseClicked(null);
                } else if (e.getKeyCode() == KeyEvent.VK_F12 && mnuSair.isEnabled()) { // Sair
                    mnuSairMouseClicked(null);
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // ESC
                    if (janela != null && !(janela instanceof Aguarde) && !(janela instanceof Autenticacao)) {
                        janela.setVisible(false);
                        janela = null;
                        statusMenus(modo);
                    }
                } else if (e.getModifiers() == KeyEvent.CTRL_MASK && e.getKeyCode() == KeyEvent.VK_Q && modo == EModo.ABERTO && option == null) { // quantidade
                    option = new JOptionPane();
                    String texto = option.showInputDialog(caixa, "Digite o valor da quantidade.", "Venda", JOptionPane.OK_CANCEL_OPTION);
                    if (texto != null && !texto.equals("")) {
                        texto = texto.replaceAll("\\D,", "");
                        try {
                            double valor = Double.valueOf(texto.replace(",", "."));
                            if (valor > 0) {
                                txtQuantidade.setValue(valor);
                            } else {
                                txtQuantidade.setValue(1.00);
                            }
                        } catch (NumberFormatException nfe) {
                            txtQuantidade.setValue(1.00);
                        }
                    }
                    option = null;
                }
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(teclas);

        // seta o icone
        if (System.getProperty(
                "os.name").contains("Windows")) {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/openpdv/imagens/logo.png")));
        }
    }

    /**
     * Metodo que retorna a instancia unica de caixa.
     *
     * @return o objeto Caixa.
     */
    public static Caixa getInstancia() {
        if (caixa == null) {
            caixa = new Caixa();
        }
        return caixa;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panCamadas = new javax.swing.JLayeredPane();
        lblLivre = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        lblOperador = new javax.swing.JLabel();
        lblCaixa = new javax.swing.JLabel();
        lblProduto = new javax.swing.JLabel();
        panBobina = new javax.swing.JScrollPane();
        lstBobina = new javax.swing.JList();
        lblLogo = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtQuantidade = new javax.swing.JFormattedTextField();
        txtUnitario = new javax.swing.JFormattedTextField();
        txtTotalItem = new javax.swing.JFormattedTextField();
        txtSubTotal = new javax.swing.JFormattedTextField();
        lblTotal = new javax.swing.JLabel();
        lblMensagem = new javax.swing.JLabel();
        lblFundo = new javax.swing.JLabel();
        barMenu = new javax.swing.JMenuBar();
        mnuSobre = new javax.swing.JMenu();
        mnuPrincipal = new javax.swing.JMenu();
        mnuSuprimento = new javax.swing.JMenuItem();
        mnuSangria = new javax.swing.JMenuItem();
        mnuReducaoZ = new javax.swing.JMenuItem();
        mnuCat52 = new javax.swing.JMenuItem();
        mnuTEF = new javax.swing.JMenuItem();
        separador1 = new javax.swing.JPopupMenu.Separator();
        mnuProdutos = new javax.swing.JMenuItem();
        mnuEmbalagens = new javax.swing.JMenuItem();
        mnuTipoPagamentos = new javax.swing.JMenuItem();
        mnuClientes = new javax.swing.JMenuItem();
        mnuUsuarios = new javax.swing.JMenuItem();
        separador2 = new javax.swing.JPopupMenu.Separator();
        mnuTrocas = new javax.swing.JMenuItem();
        mnuLeiturasZ = new javax.swing.JMenuItem();
        mnuSincronizacao = new javax.swing.JMenu();
        mnuReceber = new javax.swing.JMenuItem();
        mnuEnviar = new javax.swing.JMenuItem();
        mnuFiscal = new javax.swing.JMenu();
        mnuLX = new javax.swing.JMenuItem();
        mnuLMF = new javax.swing.JMenuItem();
        mnuMF = new javax.swing.JMenuItem();
        mnuMFD = new javax.swing.JMenuItem();
        mnuPAF = new javax.swing.JMenuItem();
        mnuVendas = new javax.swing.JMenuItem();
        mnuIndice = new javax.swing.JMenuItem();
        mnuParamConfiguracao = new javax.swing.JMenuItem();
        mnuEstoque = new javax.swing.JMenuItem();
        mnuNota = new javax.swing.JMenu();
        mnuNotaConsumidor = new javax.swing.JMenuItem();
        mnuNotaEletronica = new javax.swing.JMenuItem();
        mnuPesquisa = new javax.swing.JMenu();
        mnuGaveta = new javax.swing.JMenu();
        mnuVenda = new javax.swing.JMenu();
        mnuAbrirVenda = new javax.swing.JMenuItem();
        mnuFecharVenda = new javax.swing.JMenuItem();
        mnuCancelarItem = new javax.swing.JMenuItem();
        mnuCancelarVenda = new javax.swing.JMenuItem();
        separador3 = new javax.swing.JPopupMenu.Separator();
        mnuCupomPresente = new javax.swing.JMenuItem();
        mnuCartaoPresente = new javax.swing.JMenuItem();
        mnuIdentificar = new javax.swing.JMenu();
        mnuSair = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("OpenPDV");
        setMaximumSize(new java.awt.Dimension(1024, 746));
        setMinimumSize(new java.awt.Dimension(1024, 746));
        setName("OpenPDV"); // NOI18N
        setUndecorated(Boolean.valueOf(Util.getConfig().getProperty("openpdv.semborda")));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lblLivre.setBackground(new java.awt.Color(255, 255, 255));
        lblLivre.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblLivre.setForeground(new java.awt.Color(24, 24, 88));
        lblLivre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLivre.setFocusable(false);
        lblLivre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblLivre.setIconTextGap(0);
        lblLivre.setMaximumSize(new java.awt.Dimension(957, 450));
        lblLivre.setMinimumSize(new java.awt.Dimension(957, 450));
        lblLivre.setOpaque(true);
        lblLivre.setPreferredSize(new java.awt.Dimension(957, 450));
        panCamadas.add(lblLivre);
        lblLivre.setBounds(33, 190, 957, 450);
        panCamadas.setLayer(lblLivre, javax.swing.JLayeredPane.MODAL_LAYER);

        lblTitulo.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("OpenPDV | PhD - Systems Solutions | http://phdss.com.br");
        lblTitulo.setFocusable(false);
        lblTitulo.setName("lblTitulo"); // NOI18N
        lblTitulo.setRequestFocusEnabled(false);
        panCamadas.add(lblTitulo);
        lblTitulo.setBounds(300, 5, 710, 30);

        lblOperador.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblOperador.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOperador.setText("Operador :");
        lblOperador.setFocusable(false);
        panCamadas.add(lblOperador);
        lblOperador.setBounds(674, 60, 310, 14);

        lblCaixa.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblCaixa.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCaixa.setText("Caixa :");
        lblCaixa.setFocusable(false);
        panCamadas.add(lblCaixa);
        lblCaixa.setBounds(674, 80, 310, 14);

        lblProduto.setFont(new java.awt.Font("Serif", 1, 56)); // NOI18N
        lblProduto.setForeground(new java.awt.Color(255, 255, 255));
        lblProduto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProduto.setFocusable(false);
        lblProduto.setName("lblProduto"); // NOI18N
        panCamadas.add(lblProduto);
        lblProduto.setBounds(40, 110, 945, 83);

        panBobina.setBorder(null);
        panBobina.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panBobina.setFocusable(false);
        panBobina.setName("panBobina"); // NOI18N
        panBobina.setOpaque(false);

        lstBobina.setBackground(new java.awt.Color(255, 253, 228));
        lstBobina.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        lstBobina.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstBobina.setFocusable(false);
        lstBobina.setName("Bobina"); // NOI18N
        panBobina.setViewportView(lstBobina);

        panCamadas.add(panBobina);
        panBobina.setBounds(45, 240, 400, 350);

        lblLogo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setFocusable(false);
        lblLogo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblLogo.setName("imageProduto"); // NOI18N
        panCamadas.add(lblLogo);
        lblLogo.setBounds(730, 250, 250, 250);

        txtCodigo.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        txtCodigo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCodigo.setBorder(null);
        txtCodigo.setEnabled(false);
        txtCodigo.setName("txtCodigo"); // NOI18N
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
        });
        panCamadas.add(txtCodigo);
        txtCodigo.setBounds(490, 262, 200, 30);

        txtQuantidade.setEditable(false);
        txtQuantidade.setBorder(null);
        txtQuantidade.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.###"))));
        txtQuantidade.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQuantidade.setText("1");
        txtQuantidade.setFocusable(false);
        txtQuantidade.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        txtQuantidade.setName("txtQuantidade"); // NOI18N
        panCamadas.add(txtQuantidade);
        txtQuantidade.setBounds(490, 362, 200, 30);

        txtUnitario.setBorder(null);
        txtUnitario.setEditable(false);
        txtUnitario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        txtUnitario.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtUnitario.setText("R$ 0,00");
        txtUnitario.setFocusable(false);
        txtUnitario.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        txtUnitario.setName("txtUnitario"); // NOI18N
        panCamadas.add(txtUnitario);
        txtUnitario.setBounds(490, 462, 200, 30);

        txtTotalItem.setBorder(null);
        txtTotalItem.setEditable(false);
        txtTotalItem.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        txtTotalItem.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalItem.setText("R$ 0,00");
        txtTotalItem.setFocusable(false);
        txtTotalItem.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        txtTotalItem.setName("txtTotalItem"); // NOI18N
        panCamadas.add(txtTotalItem);
        txtTotalItem.setBounds(490, 560, 200, 30);

        txtSubTotal.setBorder(null);
        txtSubTotal.setEditable(false);
        txtSubTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        txtSubTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSubTotal.setText("R$ 0,00");
        txtSubTotal.setFocusable(false);
        txtSubTotal.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        txtSubTotal.setName("txtSubTotal"); // NOI18N
        panCamadas.add(txtSubTotal);
        txtSubTotal.setBounds(730, 560, 250, 30);

        lblTotal.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(255, 255, 255));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("R$ 0,00");
        lblTotal.setFocusable(false);
        lblTotal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblTotal.setName("labelMensagens"); // NOI18N
        lblTotal.setPreferredSize(new java.awt.Dimension(772, 20));
        panCamadas.add(lblTotal);
        lblTotal.setBounds(40, 650, 400, 45);

        lblMensagem.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        lblMensagem.setForeground(new java.awt.Color(255, 255, 204));
        lblMensagem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMensagem.setFocusable(false);
        lblMensagem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMensagem.setName("lblMensagem"); // NOI18N
        lblMensagem.setPreferredSize(new java.awt.Dimension(772, 20));
        panCamadas.add(lblMensagem);
        lblMensagem.setBounds(485, 650, 500, 45);

        lblFundo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblFundo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/tela.png"))); // NOI18N
        lblFundo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblFundo.setFocusable(false);
        panCamadas.add(lblFundo);
        lblFundo.setBounds(0, 0, 1030, 730);

        barMenu.setAutoscrolls(true);
        barMenu.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        mnuSobre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/sobre.png"))); // NOI18N
        mnuSobre.setText("Sobre - F1");
        mnuSobre.setToolTipText("Sobre o sistema");
        mnuSobre.setEnabled(false);
        mnuSobre.setFocusable(false);
        mnuSobre.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuSobre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuSobreMouseClicked(evt);
            }
        });
        mnuSobre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mnuSobreKeyPressed(evt);
            }
        });
        barMenu.add(mnuSobre);

        mnuPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/caixa.png"))); // NOI18N
        mnuPrincipal.setText("Principal - F2");
        mnuPrincipal.setEnabled(false);
        mnuPrincipal.setFocusable(false);
        mnuPrincipal.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        mnuSuprimento.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuSuprimento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/valor.png"))); // NOI18N
        mnuSuprimento.setText("Suprimento");
        mnuSuprimento.setToolTipText("Suprimento");
        mnuSuprimento.setEnabled(false);
        mnuSuprimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSuprimentoActionPerformed(evt);
            }
        });
        mnuPrincipal.add(mnuSuprimento);

        mnuSangria.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuSangria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/receber.png"))); // NOI18N
        mnuSangria.setText("Sangria");
        mnuSangria.setToolTipText("Sangria");
        mnuSangria.setEnabled(false);
        mnuSangria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSangriaActionPerformed(evt);
            }
        });
        mnuPrincipal.add(mnuSangria);

        mnuReducaoZ.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuReducaoZ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/ecfz.png"))); // NOI18N
        mnuReducaoZ.setText("Redução Z");
        mnuReducaoZ.setToolTipText("Redução Z");
        mnuReducaoZ.setEnabled(false);
        mnuReducaoZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuReducaoZActionPerformed(evt);
            }
        });
        mnuPrincipal.add(mnuReducaoZ);

        mnuCat52.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuCat52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/padrao.png"))); // NOI18N
        mnuCat52.setText("CAT52");
        mnuCat52.setToolTipText("Gerar Arquivo Cat52");
        mnuCat52.setEnabled(false);
        mnuCat52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCat52ActionPerformed(evt);
            }
        });
        mnuPrincipal.add(mnuCat52);

        mnuTEF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuTEF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cartao.png"))); // NOI18N
        mnuTEF.setText("TEF");
        mnuTEF.setEnabled(false);
        mnuTEF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTEFActionPerformed(evt);
            }
        });
        mnuPrincipal.add(mnuTEF);
        mnuPrincipal.add(separador1);

        mnuProdutos.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuProdutos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/produto.png"))); // NOI18N
        mnuProdutos.setText("Produtos");
        mnuProdutos.setToolTipText("Produtos");
        mnuProdutos.setEnabled(false);
        mnuProdutos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProdutosActionPerformed(evt);
            }
        });
        mnuPrincipal.add(mnuProdutos);

        mnuEmbalagens.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuEmbalagens.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/estoque.png"))); // NOI18N
        mnuEmbalagens.setText("Embalagens");
        mnuEmbalagens.setToolTipText("Embalagens");
        mnuEmbalagens.setEnabled(false);
        mnuEmbalagens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuEmbalagensActionPerformed(evt);
            }
        });
        mnuPrincipal.add(mnuEmbalagens);

        mnuTipoPagamentos.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuTipoPagamentos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/preco.png"))); // NOI18N
        mnuTipoPagamentos.setText("Tipo Pagamentos");
        mnuTipoPagamentos.setToolTipText("Tipo Pagamentos");
        mnuTipoPagamentos.setEnabled(false);
        mnuTipoPagamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTipoPagamentosActionPerformed(evt);
            }
        });
        mnuPrincipal.add(mnuTipoPagamentos);

        mnuClientes.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cliente.png"))); // NOI18N
        mnuClientes.setText("Clientes");
        mnuClientes.setToolTipText("Clientes");
        mnuClientes.setEnabled(false);
        mnuClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuClientesActionPerformed(evt);
            }
        });
        mnuPrincipal.add(mnuClientes);

        mnuUsuarios.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/grupo.png"))); // NOI18N
        mnuUsuarios.setText("Usuários");
        mnuUsuarios.setToolTipText("Usuários");
        mnuUsuarios.setEnabled(false);
        mnuUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuUsuariosActionPerformed(evt);
            }
        });
        mnuPrincipal.add(mnuUsuarios);
        mnuPrincipal.add(separador2);

        mnuTrocas.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuTrocas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/padrao.png"))); // NOI18N
        mnuTrocas.setText("Trocas");
        mnuTrocas.setToolTipText("Trocas");
        mnuTrocas.setEnabled(false);
        mnuTrocas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTrocasActionPerformed(evt);
            }
        });
        mnuPrincipal.add(mnuTrocas);

        mnuLeiturasZ.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuLeiturasZ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/padrao.png"))); // NOI18N
        mnuLeiturasZ.setText("Leituras Z");
        mnuLeiturasZ.setToolTipText("Leituras Z");
        mnuLeiturasZ.setEnabled(false);
        mnuLeiturasZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLeiturasZActionPerformed(evt);
            }
        });
        mnuPrincipal.add(mnuLeiturasZ);

        mnuSincronizacao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/sincroniza.png"))); // NOI18N
        mnuSincronizacao.setText("Sincronização");
        mnuSincronizacao.setToolTipText("Sincroniza os PDVs");
        mnuSincronizacao.setEnabled(false);

        mnuReceber.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuReceber.setText("Receber");
        mnuReceber.setToolTipText("Receber dados do servidor.");
        mnuReceber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuReceberActionPerformed(evt);
            }
        });
        mnuSincronizacao.add(mnuReceber);

        mnuEnviar.setText("Enviar");
        mnuEnviar.setToolTipText("Enviar dados ao servidor.");
        mnuEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuEnviarActionPerformed(evt);
            }
        });
        mnuSincronizacao.add(mnuEnviar);

        mnuPrincipal.add(mnuSincronizacao);

        barMenu.add(mnuPrincipal);

        mnuFiscal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/fiscal.png"))); // NOI18N
        mnuFiscal.setText("Menu Fiscal - F3");
        mnuFiscal.setToolTipText("Menu Fiscal");
        mnuFiscal.setEnabled(false);
        mnuFiscal.setFocusable(false);
        mnuFiscal.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        mnuLX.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuLX.setText("LX");
        mnuLX.setToolTipText("Leitura X");
        mnuLX.setEnabled(false);
        mnuLX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLXActionPerformed(evt);
            }
        });
        mnuFiscal.add(mnuLX);

        mnuLMF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuLMF.setText("LMF");
        mnuLMF.setToolTipText("Leitura Memória Fiscal");
        mnuLMF.setEnabled(false);
        mnuLMF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLMFActionPerformed(evt);
            }
        });
        mnuFiscal.add(mnuLMF);

        mnuMF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuMF.setText("Arq. MF");
        mnuMF.setToolTipText("Aruivo MF");
        mnuMF.setEnabled(false);
        mnuMF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMFActionPerformed(evt);
            }
        });
        mnuFiscal.add(mnuMF);

        mnuMFD.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuMFD.setText("Arq. MFD");
        mnuMFD.setToolTipText("Arquivo MFD");
        mnuMFD.setEnabled(false);
        mnuMFD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMFDActionPerformed(evt);
            }
        });
        mnuFiscal.add(mnuMFD);

        mnuPAF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuPAF.setText("Identificação do PAF-ECF");
        mnuPAF.setToolTipText("Identificação do PAF-ECF");
        mnuPAF.setEnabled(false);
        mnuPAF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPAFActionPerformed(evt);
            }
        });
        mnuFiscal.add(mnuPAF);

        mnuVendas.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuVendas.setText("Vendas do Período");
        mnuVendas.setToolTipText("Vendas do Período");
        mnuVendas.setEnabled(false);
        mnuVendas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVendasActionPerformed(evt);
            }
        });
        mnuFiscal.add(mnuVendas);

        mnuIndice.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuIndice.setText("Tabela Índice Técnico Produção");
        mnuIndice.setToolTipText("Tabela Índice Técnico Produção");
        mnuIndice.setEnabled(false);
        mnuIndice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIndiceActionPerformed(evt);
            }
        });
        mnuFiscal.add(mnuIndice);

        mnuParamConfiguracao.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuParamConfiguracao.setText("Parâmetros de Configuração");
        mnuParamConfiguracao.setToolTipText("Parâmetros de Configuração");
        mnuParamConfiguracao.setEnabled(false);
        mnuParamConfiguracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuParamConfiguracaoActionPerformed(evt);
            }
        });
        mnuFiscal.add(mnuParamConfiguracao);

        mnuEstoque.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuEstoque.setText("Registros do PAF-ECF");
        mnuEstoque.setToolTipText("Registros do PAF-ECF");
        mnuEstoque.setEnabled(false);
        mnuEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuEstoqueActionPerformed(evt);
            }
        });
        mnuFiscal.add(mnuEstoque);

        barMenu.add(mnuFiscal);

        mnuNota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/padrao.png"))); // NOI18N
        mnuNota.setText("Nota Fiscal - F4");
        mnuNota.setToolTipText("Nota Fiscal");
        mnuNota.setEnabled(false);
        mnuNota.setFocusable(false);
        mnuNota.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        mnuNotaConsumidor.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuNotaConsumidor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/principal.png"))); // NOI18N
        mnuNotaConsumidor.setText("Consumidor");
        mnuNotaConsumidor.setToolTipText("Emissão de Nota Fiscal de Venda ao Consumidor");
        mnuNotaConsumidor.setEnabled(false);
        mnuNotaConsumidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNotaConsumidorActionPerformed(evt);
            }
        });
        mnuNota.add(mnuNotaConsumidor);

        mnuNotaEletronica.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuNotaEletronica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/nfe.png"))); // NOI18N
        mnuNotaEletronica.setText("Eletrônica");
        mnuNotaEletronica.setToolTipText("Emissão de Nota Fiscal Eletrônica");
        mnuNotaEletronica.setEnabled(false);
        mnuNotaEletronica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNotaEletronicaActionPerformed(evt);
            }
        });
        mnuNota.add(mnuNotaEletronica);

        barMenu.add(mnuNota);

        mnuPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/pesquisa.png"))); // NOI18N
        mnuPesquisa.setText("Pesquisar - F5");
        mnuPesquisa.setToolTipText("Pesquisar Produto");
        mnuPesquisa.setEnabled(false);
        mnuPesquisa.setFocusable(false);
        mnuPesquisa.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuPesquisa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuPesquisaMouseClicked(evt);
            }
        });
        mnuPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mnuPesquisaKeyPressed(evt);
            }
        });
        barMenu.add(mnuPesquisa);

        mnuGaveta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/acesso.png"))); // NOI18N
        mnuGaveta.setText("Abrir Gaveta - F6");
        mnuGaveta.setToolTipText("Abre a gaveta do caixa.");
        mnuGaveta.setEnabled(false);
        mnuGaveta.setFocusable(false);
        mnuGaveta.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuGaveta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuGavetaMouseClicked(evt);
            }
        });
        mnuGaveta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mnuGavetaKeyPressed(evt);
            }
        });
        barMenu.add(mnuGaveta);

        mnuVenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/venda.png"))); // NOI18N
        mnuVenda.setText("Venda");
        mnuVenda.setToolTipText("Menu de operações de venda");
        mnuVenda.setEnabled(false);
        mnuVenda.setFocusable(false);
        mnuVenda.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        mnuAbrirVenda.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuAbrirVenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/formulario.png"))); // NOI18N
        mnuAbrirVenda.setText("Abrir Venda - F7");
        mnuAbrirVenda.setEnabled(false);
        mnuAbrirVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAbrirVendaActionPerformed(evt);
            }
        });
        mnuVenda.add(mnuAbrirVenda);

        mnuFecharVenda.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuFecharVenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/fechar.png"))); // NOI18N
        mnuFecharVenda.setText("Fechar Venda - F8");
        mnuFecharVenda.setToolTipText("Fecha a venda atual");
        mnuFecharVenda.setEnabled(false);
        mnuFecharVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuFecharVendaActionPerformed(evt);
            }
        });
        mnuVenda.add(mnuFecharVenda);

        mnuCancelarItem.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuCancelarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/excluir.png"))); // NOI18N
        mnuCancelarItem.setText("Cancelar Item - F9");
        mnuCancelarItem.setToolTipText("Cancela um item da venda");
        mnuCancelarItem.setEnabled(false);
        mnuCancelarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCancelarItemActionPerformed(evt);
            }
        });
        mnuVenda.add(mnuCancelarItem);

        mnuCancelarVenda.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuCancelarVenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cancelar.png"))); // NOI18N
        mnuCancelarVenda.setText("Cancelar Venda - F10");
        mnuCancelarVenda.setToolTipText("Cancela a venda atual ou ultima realizada");
        mnuCancelarVenda.setEnabled(false);
        mnuCancelarVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCancelarVendaActionPerformed(evt);
            }
        });
        mnuVenda.add(mnuCancelarVenda);
        mnuVenda.add(separador3);

        mnuCupomPresente.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuCupomPresente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/estoque.png"))); // NOI18N
        mnuCupomPresente.setText("Cupom Presente");
        mnuCupomPresente.setToolTipText("Cupom que é ser usado nas compras para presente, facilitando possíveis trocas.");
        mnuCupomPresente.setEnabled(false);
        mnuCupomPresente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCupomPresenteActionPerformed(evt);
            }
        });
        mnuVenda.add(mnuCupomPresente);

        mnuCartaoPresente.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuCartaoPresente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cartao.png"))); // NOI18N
        mnuCartaoPresente.setText("Cartão Presente");
        mnuCartaoPresente.setToolTipText("Liberação e emissão do comprovante do Cartão Presente.");
        mnuCartaoPresente.setEnabled(false);
        mnuCartaoPresente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCartaoPresenteActionPerformed(evt);
            }
        });
        mnuVenda.add(mnuCartaoPresente);

        barMenu.add(mnuVenda);

        mnuIdentificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cliente.png"))); // NOI18N
        mnuIdentificar.setText("Identificar - F11");
        mnuIdentificar.setToolTipText("Identificar o Cliente e/ou Vendedor no Cupom Fiscal");
        mnuIdentificar.setEnabled(false);
        mnuIdentificar.setFocusable(false);
        mnuIdentificar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuIdentificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuIdentificarMouseClicked(evt);
            }
        });
        mnuIdentificar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mnuIdentificarKeyPressed(evt);
            }
        });
        barMenu.add(mnuIdentificar);

        mnuSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/sair.png"))); // NOI18N
        mnuSair.setText("Sair - F12");
        mnuSair.setToolTipText("Sai do sistema");
        mnuSair.setEnabled(false);
        mnuSair.setFocusable(false);
        mnuSair.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        mnuSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuSairMouseClicked(evt);
            }
        });
        mnuSair.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mnuSairKeyPressed(evt);
            }
        });
        barMenu.add(mnuSair);

        setJMenuBar(barMenu);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panCamadas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1024, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panCamadas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 724, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(1024, 768));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void mnuSobreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuSobreMouseClicked
        if (janela == null && mnuSobre.isEnabled()) {
            janela = Sobre.getInstancia();
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuSobreMouseClicked

    private void mnuLXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLXActionPerformed
        int escolha = JOptionPane.showOptionDialog(this, "Deseja emitir a LeituraX?", "Menu Fiscal",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
        if (escolha == JOptionPane.YES_OPTION) {
            try {
                PAF.leituraX();
            } catch (Exception ex) {
                log.error("Não foi possivel emitir a LeituraX! -> ", ex);
                JOptionPane.showMessageDialog(caixa, "Não foi possível emitir a LeituraX!", "Menu Fiscal", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_mnuLXActionPerformed

    private void mnuSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuSairMouseClicked
        if (mnuSair.isEnabled() && option == null) {
            option = new JOptionPane();
            int escolha;
            if (modo == EModo.DISPONIVEL) {
                escolha = JOptionPane.showOptionDialog(this, "Deseja sair do sistema ou trocar de operador?", "OpenPDV",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sair", "Cancelar", "Trocar",}, JOptionPane.YES_OPTION);
            } else {
                escolha = JOptionPane.showOptionDialog(this, "Deseja sair do sistema?", "OpenPDV",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
            }

            if (escolha == JOptionPane.YES_OPTION) {
                if (ecf != null) {
                    ecf.desativar();
                }
                System.exit(0);
            } else if (escolha == JOptionPane.CANCEL_OPTION) {
                modoOff();
                Login.setOperador(null);
                janela = Autenticacao.getInstancia();
                janela.setVisible(true);
            }
            option = null;
        }
    }//GEN-LAST:event_mnuSairMouseClicked

    private void mnuSairKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuSairKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            mnuSairMouseClicked(null);
        }
    }//GEN-LAST:event_mnuSairKeyPressed

    private void mnuSobreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuSobreKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            mnuSobreMouseClicked(null);
        }
    }//GEN-LAST:event_mnuSobreKeyPressed

    private void mnuLMFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLMFActionPerformed
        janela = PAF_LMF.getInstancia();
        janela.setVisible(true);
    }//GEN-LAST:event_mnuLMFActionPerformed

    private void mnuMFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMFActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] path = PAF.gerarArqMF();
                    Aguarde.getInstancia().setVisible(false);
                    JOptionPane.showMessageDialog(caixa, "Arquivos gerados com sucesso em:\n" + path[0] + "\n" + path[1], "Menu Fiscal", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    Aguarde.getInstancia().setVisible(false);
                    log.error("Nao foi possivel gerar o arquivo -> ", ex);
                    JOptionPane.showMessageDialog(caixa, "Não foi possível gerar o arquivo!", "Menu Fiscal", JOptionPane.WARNING_MESSAGE);
                }
            }
        }).start();
        Aguarde.getInstancia().setVisible(true);
    }//GEN-LAST:event_mnuMFActionPerformed

    private void mnuSuprimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSuprimentoActionPerformed
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                ecf.enviar(EComando.ECF_AbreGaveta);
                String texto = JOptionPane.showInputDialog(caixa, "Digite o valor do suprimento.", "Suprimento", JOptionPane.OK_CANCEL_OPTION);

                if (texto != null) {
                    texto = texto.replace(".", "").replace(",", ".");
                    try {
                        double valor = Double.valueOf(texto);
                        String[] param = new String[]{valor + "", "", Util.getConfig().getProperty("ecf.suprimento"), "DINHEIRO"};
                        String[] resp = ecf.enviar(EComando.ECF_Suprimento, param);

                        if (IECF.OK.equals(resp[0])) {
                            new ComandoSalvarDocumento("CN").executar();
                        } else {
                            log.error("Não foi possivel realizar o Suprimento! -> " + resp[1]);
                            JOptionPane.showMessageDialog(caixa, "Não foi possível realizar o Suprimento!", "Suprimento", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (OpenPdvException ex) {
                        JOptionPane.showMessageDialog(caixa, "Não existe esta função na ECF.\nDeve-se solicitar uma intervenção!", "Suprimento", JOptionPane.WARNING_MESSAGE);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(caixa, "Valor informado inválido!", "Suprimento", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            janela = Gerente.getInstancia(async);
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuSuprimentoActionPerformed

    private void mnuSangriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSangriaActionPerformed
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                ecf.enviar(EComando.ECF_AbreGaveta);
                String texto = JOptionPane.showInputDialog(caixa, "Digite o valor da sangria.", "Sangria", JOptionPane.OK_CANCEL_OPTION);

                if (texto != null) {
                    texto = texto.replace(".", "").replace(",", ".");
                    try {
                        double valor = Double.valueOf(texto);
                        String[] param = new String[]{valor + "", "", Util.getConfig().getProperty("ecf.sangria"), "DINHEIRO"};
                        String[] resp = ecf.enviar(EComando.ECF_Sangria, param);

                        if (IECF.OK.equals(resp[0])) {
                            new ComandoSalvarDocumento("CN").executar();
                        } else {
                            log.error("Não foi possivel realizar a Sangria! -> " + resp[1]);
                            JOptionPane.showMessageDialog(caixa, "Não foi possível realizar a Sangria!", "Sangria", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (OpenPdvException ex) {
                        JOptionPane.showMessageDialog(caixa, "Não existe esta função na ECF.\nDeve-se solicitar uma intervenção!", "Sangria", JOptionPane.WARNING_MESSAGE);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(caixa, "Valor informado inválido!", "Sangria", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            janela = Gerente.getInstancia(async);
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuSangriaActionPerformed

    private void mnuReducaoZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuReducaoZActionPerformed
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                int escolha = JOptionPane.showOptionDialog(caixa, "Deseja emitir a ReduçãoZ?", "Redução Z",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
                if (escolha == JOptionPane.YES_OPTION) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // dia atua formatado
                                Date dataMovimento = Util.getData(Util.getData(new Date()));
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(dataMovimento);
                                cal.add(Calendar.DAY_OF_MONTH, 1);

                                // verifica se tem vendas efetuadas no dia atual
                                FiltroData fd1 = new FiltroData("ecfVendaData", ECompara.MAIOR_IGUAL, dataMovimento);
                                FiltroData fd2 = new FiltroData("ecfVendaData", ECompara.MENOR, cal.getTime());
                                FiltroGrupo gf = new FiltroGrupo(Filtro.E, fd1, fd2);
                                List<EcfVenda> vendas = service.selecionar(new EcfVenda(), 0, 0, gf);

                                // verifica se tem documentos efetuadas no dia atual
                                fd1 = new FiltroData("ecfDocumentoData", ECompara.MAIOR_IGUAL, dataMovimento);
                                fd2 = new FiltroData("ecfDocumentoData", ECompara.MENOR, cal.getTime());
                                gf = new FiltroGrupo(Filtro.E, fd1, fd2);
                                List<EcfDocumento> documentos = service.selecionar(new EcfDocumento(), 0, 0, gf);

                                if (vendas.size() > 0 || documentos.size() > 0) {
                                    new ComandoEmitirReducaoZ().executar();
                                    Aguarde.getInstancia().setVisible(false);
                                    modoConsulta();
                                } else {
                                    JOptionPane.showMessageDialog(caixa, "Não houve movimento fiscal hoje, então não precisa emititr a Redução Z!", "Redução Z", JOptionPane.WARNING_MESSAGE);
                                }
                            } catch (OpenPdvException ex) {
                                Aguarde.getInstancia().setVisible(false);
                                log.error("Não foi possivel realizar a ReducaoZ.", ex);
                                JOptionPane.showMessageDialog(caixa, "Não foi possível realizar a Redução Z!", "Redução Z", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }).start();
                    Aguarde.getInstancia().setVisible(true);
                }
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            janela = Gerente.getInstancia(async);
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuReducaoZActionPerformed

    private void mnuUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuUsuariosActionPerformed
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                janela = Usuarios.getInstancia();
                janela.setVisible(true);
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            janela = Gerente.getInstancia(async);
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuUsuariosActionPerformed

    private void mnuClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuClientesActionPerformed
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                janela = Clientes.getInstancia(null);
                janela.setVisible(true);
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            janela = Gerente.getInstancia(async);
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuClientesActionPerformed

    private void mnuEmbalagensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuEmbalagensActionPerformed
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                janela = Embalagens.getInstancia();
                janela.setVisible(true);
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            janela = Gerente.getInstancia(async);
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuEmbalagensActionPerformed

    private void mnuProdutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProdutosActionPerformed
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                janela = Produtos.getInstancia();
                janela.setVisible(true);
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            janela = Gerente.getInstancia(async);
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuProdutosActionPerformed

    private void mnuPesquisaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuPesquisaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            mnuPesquisaMouseClicked(null);
        }
    }//GEN-LAST:event_mnuPesquisaKeyPressed

    private void mnuPesquisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuPesquisaMouseClicked
        if (janela == null && mnuPesquisa.isEnabled()) {
            janela = Pesquisa.getInstancia(pesquisado);
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuPesquisaMouseClicked

    private void mnuIndiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuIndiceActionPerformed
        JOptionPane.showMessageDialog(this, "Este PAF-ECF não executa funções de baixa de estoque\n"
                + "com base em índices técnicos de produção, não podendo\n"
                + "ser utilizado por estabelecimento que necessite deste.", "Menu Fiscal", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_mnuIndiceActionPerformed

    private void mnuParamConfiguracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuParamConfiguracaoActionPerformed
        int escolha = JOptionPane.showOptionDialog(this, "Deseja emitir os Parâmetros de Configuração?", "Menu Fiscal",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
        if (escolha == JOptionPane.YES_OPTION) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        PAF.emitirConfiguracao(Util.getConfig().getProperty("ecf.relconfig"));
                        new ComandoSalvarDocumento("RG").executar();
                        Aguarde.getInstancia().setVisible(false);
                    } catch (Exception ex) {
                        Aguarde.getInstancia().setVisible(false);
                        log.error("Não foi possivel emitir o relatorio de configuracao! -> ", ex);
                        JOptionPane.showMessageDialog(caixa, "Não foi possível emitir o Relatório!\nVerifique se o ECF está ativa e livre.", "Menu Fiscal", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }).start();

            Aguarde.getInstancia().setVisible(true);
        }
    }//GEN-LAST:event_mnuParamConfiguracaoActionPerformed

    private void mnuPAFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPAFActionPerformed
        int escolha = JOptionPane.showOptionDialog(this, "Deseja emitir o relatório de identificação do PAF-ECF?", "Menu Fiscal",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
        if (escolha == JOptionPane.YES_OPTION) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        PAF.emitirIdentificaoPAF(Util.getConfig().getProperty("ecf.relpaf"));
                        new ComandoSalvarDocumento("RG").executar();
                        Aguarde.getInstancia().setVisible(false);
                    } catch (Exception ex) {
                        Aguarde.getInstancia().setVisible(false);
                        log.error("Não foi possivel emitir o relatorio de identificacao do paf! -> ", ex);
                        JOptionPane.showMessageDialog(caixa, "Não foi possível emitir o Relatório!\nVerifique se o ECF está ativa e livre.", "Menu Fiscal", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }).start();

            Aguarde.getInstancia().setVisible(true);
        }
    }//GEN-LAST:event_mnuPAFActionPerformed

    private void mnuEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuEstoqueActionPerformed
        janela = PAF_Registros.getInstancia();
        janela.setVisible(true);
    }//GEN-LAST:event_mnuEstoqueActionPerformed

    private void mnuAbrirVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAbrirVendaActionPerformed
        boolean permite = true;
        StringBuilder erro = new StringBuilder();

        // valida o serial do ECF
        try {
            ecf.validarSerial(PAF.AUXILIAR.getProperty("ecf.serie").split(";")[0]);
        } catch (Exception ex) {
            permite = false;
            log.error("Problemas ao validar o serial.", ex);
            erro.append(ex.getMessage()).append("\n");
        }

        // valida o GT do ECF
        try {
            double gt = Double.valueOf(PAF.AUXILIAR.getProperty("ecf.gt").replace(",", "."));
            double novoGT = ecf.validarGT(gt);
            if (novoGT > 0.00) {
                EcfZ ultZ = new EcfZ();
                ultZ.setOrdemDirecao(EDirecao.DESC);
                FiltroObjeto fo = new FiltroObjeto("ecfImpressora", ECompara.IGUAL, Caixa.getInstancia().getImpressora());
                List<EcfZ> zs = service.selecionar(ultZ, 0, 1, fo);
                if (zs != null && !zs.isEmpty()) {
                    ultZ = zs.get(0);
                    if (ecf.validarGT(ultZ.getEcfZCrz(), ultZ.getEcfZCro(), ultZ.getEcfZBruto())) {
                        PAF.AUXILIAR.setProperty("ecf.gt", Util.formataNumero(novoGT, 1, 2, false));
                        Util.criptografar(null, PAF.AUXILIAR);
                        JOptionPane.showMessageDialog(caixa, "Valor do GT recomposto no arquivo auxiliar.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                    } else {
                        throw new Exception("Os dados de CRZ, CRO e Valor Bruto não estão iguais!");
                    }
                } else {
                    throw new Exception("Não existe Z no banco.");
                }
            }
        } catch (Exception ex) {
            permite = false;
            log.error("Problemas ao validar o GT.", ex);
            erro.append("Não foi possível recupara o GT! -> ").append(ex.getMessage()).append("\n");
        }

        // se permitir abre a venda, solicita o cliente
        if (permite) {
            statusMenus(EModo.OFF);
            final Identificar ident = Identificar.getInstancia(null);
            AsyncCallback<SisCliente> async = new AsyncCallback<SisCliente>() {
                @Override
                public void sucesso(SisCliente resultado) {
                    if (resultado != null && resultado.isCpfCupom()) {
                        String[] resp = ecf.enviar(EComando.ECF_IdentificaConsumidor,
                                resultado.getSisClienteDoc(), resultado.getSisClienteNome(), resultado.getSisClienteEndereco());
                        if (IECF.ERRO.equals(resp[0])) {
                            falha(new Exception(resp[1]));
                        }
                    }

                    try {
                        new ComandoAbrirVenda(resultado).executar();
                        modoAberto();
                    } catch (OpenPdvException ex) {
                        statusMenus(modo);
                        log.error(ex);
                        JOptionPane.showMessageDialog(caixa, "Erro ao abrir a venda.", "Venda", JOptionPane.ERROR_MESSAGE);
                    }

                    // adiciona os itens da venda recuperada
                    if (ident.getVenda() != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // percorre os produtos da venda recuperada
                                for (EcfVendaProduto vp : ident.getVenda().getEcfVendaProdutos()) {
                                    try {
                                        adicionar(vp.getProdProduto(), vp.getProdEmbalagem(), vp.getEcfVendaProdutoLiquido(), vp.getEcfVendaProdutoQuantidade(), vp.getEcfVendaProdutoBarra());
                                    } catch (OpenPdvException ex) {
                                        JOptionPane.showMessageDialog(caixa, "Erro ao adicionar o item com codigo -> " + vp.getProdProduto().getId(), "Venda", JOptionPane.WARNING_MESSAGE);
                                    }
                                }
                                Aguarde.getInstancia().setVisible(false);
                            }
                        }).start();
                        Aguarde.getInstancia().setVisible(true);
                    }
                }

                @Override
                public void falha(Exception excecao) {
                    log.error("Erro na identificacao do cliente.", excecao);
                    JOptionPane.showMessageDialog(caixa, "Não foi possível identificar o cliente.", "Identificar Cliente", JOptionPane.WARNING_MESSAGE);
                }
            };

            ident.setAsync(async);
            ident.getChkRecuperar().setEnabled(true);
            ident.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, erro.toString(), "Venda", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuAbrirVendaActionPerformed

    private void mnuCancelarVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCancelarVendaActionPerformed
        final Gerente gerente = Gerente.getInstancia(null);
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            statusMenus(EModo.OFF);
                            new ComandoCancelarVenda(gerente.getSisGerente()).executar();
                        } catch (OpenPdvException ex) {
                            log.error(ex);
                            Aguarde.getInstancia().setVisible(false);
                            JOptionPane.showMessageDialog(caixa, "Erro ao cancelar a venda.", "Cancelar Venda", JOptionPane.ERROR_MESSAGE);
                        } finally {
                            janela = null;
                        }
                    }
                }).start();
                Aguarde.getInstancia().setVisible(true);
            }

            @Override
            public void falha(Exception excecao) {
                janela = null;
                JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            gerente.setSisGerente(Login.getOperador());
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            janela = gerente;
            gerente.setAsync(async);
            gerente.setVisible(true);
        }
    }//GEN-LAST:event_mnuCancelarVendaActionPerformed

    private void mnuCancelarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCancelarItemActionPerformed
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                String texto = JOptionPane.showInputDialog(caixa, "Digite o número do item.", "Cancelar Item", JOptionPane.OK_CANCEL_OPTION);
                if (texto != null) {
                    texto = texto.replaceAll("\\D", "");
                    try {
                        int item = Integer.valueOf(texto);
                        if (item < 1 || item > venda.getEcfVendaProdutos().size()) {
                            JOptionPane.showMessageDialog(caixa, "O número informado não corresponde a nenhum item da venda.", "Cancelar Item", JOptionPane.WARNING_MESSAGE);
                        } else {
                            EcfVendaProduto vendaProduto = venda.getEcfVendaProdutos().get(item - 1);
                            if (vendaProduto.getEcfVendaProdutoCancelado()) {
                                JOptionPane.showMessageDialog(caixa, "O item informado já está cancelado.", "Cancelar Item", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                try {
                                    new ComandoCancelarItem(vendaProduto).executar();
                                    vendaProduto.setEcfVendaProdutoCancelado(true);
                                    totalizar();
                                } catch (OpenPdvException ex) {
                                    log.error(ex);
                                    JOptionPane.showMessageDialog(caixa, "Erro ao cancelar o item da venda.", "Cancelar Item", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(caixa, "Não foi informado um número inteiro.", "Cancelar Item", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            janela = Gerente.getInstancia(async);
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuCancelarItemActionPerformed

    private void mnuFecharVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuFecharVendaActionPerformed
        if (lblTotal.getText().equals("R$ 0,00")) {
            JOptionPane.showMessageDialog(caixa, "Venda com valor R$ 0,00", "Fechar Venda", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Fechamento.getInstancia(totalizar()).setVisible(true);
        }
    }//GEN-LAST:event_mnuFecharVendaActionPerformed

    private void mnuGavetaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuGavetaMouseClicked
        if (mnuGaveta.isEnabled() && janela == null) {
            AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
                @Override
                public void sucesso(Integer resultado) {
                    String[] resp = ecf.enviar(EComando.ECF_AbreGaveta);
                    if (IECF.ERRO.equals(resp[0])) {
                        log.error("Erro ao abrir a gaveta. -> " + resp[1]);
                        JOptionPane.showMessageDialog(caixa, "Não foi possível abrir a gaveta!", "OpenPDV", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                @Override
                public void falha(Exception excecao) {
                    JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
                }
            };

            if (Login.getOperador().isSisUsuarioGerente()) {
                async.sucesso(Login.getOperador().getSisUsuarioDesconto());
            } else {
                janela = Gerente.getInstancia(async);
                janela.setVisible(true);
            }
        }
    }//GEN-LAST:event_mnuGavetaMouseClicked

    private void mnuGavetaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuGavetaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            mnuGavetaMouseClicked(null);
        }
    }//GEN-LAST:event_mnuGavetaKeyPressed

    private void mnuIdentificarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnuIdentificarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            mnuIdentificarMouseClicked(null);
        }
    }//GEN-LAST:event_mnuIdentificarKeyPressed

    private void mnuIdentificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuIdentificarMouseClicked
        if (mnuIdentificar.isEnabled()) {
            statusMenus(EModo.OFF);
            Identificar ident = Identificar.getInstancia(new AsyncCallback<SisCliente>() {
                @Override
                public void sucesso(SisCliente resultado) {
                    if (resultado != null) {
                        if (resultado.getSisClienteId() > 0) {
                            venda.setSisCliente(resultado);
                            mnuIdentificar.setEnabled(false);
                        }
                        venda.setSisVendedor(resultado.getVendedor());
                    }
                    statusMenus(modo);
                }

                @Override
                public void falha(Exception excecao) {
                    statusMenus(modo);
                    log.error("Erro na identificacao do cliente.", excecao);
                    JOptionPane.showMessageDialog(caixa, "Não foi possível identificar o cliente.", "Identificar", JOptionPane.WARNING_MESSAGE);
                }
            });
            ident.getChkRecuperar().setEnabled(false);
            ident.setVisible(true);
        }
    }//GEN-LAST:event_mnuIdentificarMouseClicked

    private void mnuVendasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVendasActionPerformed
        janela = PAF_VendasPeriodo.getInstancia();
        janela.setVisible(true);
    }//GEN-LAST:event_mnuVendasActionPerformed

    private void mnuNotaConsumidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNotaConsumidorActionPerformed
        janela = NotaConsumidor.getInstancia();
        janela.setVisible(true);
    }//GEN-LAST:event_mnuNotaConsumidorActionPerformed

    private void mnuNotaEletronicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNotaEletronicaActionPerformed
        janela = NotaEletronica.getInstancia();
        janela.setVisible(true);
    }//GEN-LAST:event_mnuNotaEletronicaActionPerformed

    private void mnuTEFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTEFActionPerformed
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String id = TEF.gerarId();
                            TEF.abrirADM(id);
                            String msg = TEF.getDados().get("030-000");

                            // imprime as vias
                            if (TEF.getDados().get("028-000") != null && Integer.valueOf(TEF.getDados().get("028-000")) > 0) {
                                // verifica se tem mensagem
                                if (msg != null && !msg.equals("")) {
                                    Aguarde.getInstancia().getLblMensagem().setText(msg);
                                    Aguarde.getInstancia().setVisible(true);
                                }

                                try {
                                    TEF.bloquear(true);
                                    ecf.enviar(EComando.ECF_FechaRelatorio);
                                    ecf.enviar(EComando.ECF_AbreRelatorioGerencial, Util.getConfig().getProperty("ecf.reltef"));
                                    TEF.imprimirVias(TEF.getDados(), EComando.ECF_LinhaRelatorioGerencial);
                                    ecf.enviar(EComando.ECF_FechaRelatorio);
                                    TEF.bloquear(false);
                                    TEF.confirmarTransacao(id, true);
                                } catch (Exception ex) {
                                    TEF.bloquear(false);
                                    ecf.enviar(EComando.ECF_FechaRelatorio);
                                    TEF.confirmarTransacao(id, false);
                                    throw new Exception("Impressora não responde!");
                                } finally {
                                    TEF.bloquear(false);
                                }
                            } else {
                                TEF.confirmarTransacao(id, true);
                                JOptionPane.showMessageDialog(caixa, msg, "TEF", JOptionPane.INFORMATION_MESSAGE);
                            }
                            Aguarde.getInstancia().setVisible(false);
                        } catch (Exception ex) {
                            Aguarde.getInstancia().setVisible(false);
                            if (ex.getMessage() != null) {
                                JOptionPane.showMessageDialog(caixa, ex.getMessage(), "TEF", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }).start();
                Aguarde.getInstancia().setVisible(true);
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            janela = Gerente.getInstancia(async);
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuTEFActionPerformed

    private void mnuReceberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuReceberActionPerformed
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ComandoReceberDados.getInstancia().executar();
                    Aguarde.getInstancia().setVisible(false);
                    JOptionPane.showMessageDialog(caixa, "Realizado com sucesso.", "Sincronismo - Receber", JOptionPane.INFORMATION_MESSAGE);
                } catch (OpenPdvException ex) {
                    log.error("Não conseguiu receber dados do servidor.", ex);
                    Aguarde.getInstancia().setVisible(false);
                    JOptionPane.showMessageDialog(caixa, ex.getMessage(), "Sincronismo - Receber", JOptionPane.WARNING_MESSAGE);
                } finally {
                    caixa.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        }).start();

        caixa.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Aguarde.getInstancia().setVisible(true);
    }//GEN-LAST:event_mnuReceberActionPerformed

    private void mnuTipoPagamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTipoPagamentosActionPerformed
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                janela = TiposPagamento.getInstancia();
                janela.setVisible(true);
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            janela = Gerente.getInstancia(async);
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuTipoPagamentosActionPerformed

    private void mnuCupomPresenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCupomPresenteActionPerformed
        try {
            List<EcfVenda> vendas = service.selecionar(new EcfVenda(), 0, 1, null);
            Integer ccf = vendas.isEmpty() ? 0 : vendas.get(0).getEcfVendaCcf();
            String valor = JOptionPane.showInputDialog(caixa, "Insira o número do CCF.", ccf);

            ComandoCupomPresente ccp = new ComandoCupomPresente(Integer.valueOf(valor));
            ccp.executar();
        } catch (OpenPdvException | NumberFormatException ex) {
            if (ex instanceof OpenPdvException) {
                JOptionPane.showMessageDialog(caixa, "CCF não existe ou problemas na impressão!", "Cupom Presente", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_mnuCupomPresenteActionPerformed

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = txtCodigo.getText().trim();
            if (!texto.equals("")) {
                Filtro filtro = Pesquisa.pesquisar(texto);
                Pesquisa.getInstancia(pesquisado).selecionar(filtro);
            }
        }
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void mnuTrocasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTrocasActionPerformed
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                janela = Trocas.getInstancia(null);
                janela.setVisible(true);
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            janela = Gerente.getInstancia(async);
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuTrocasActionPerformed

    private void mnuLeiturasZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLeiturasZActionPerformed
        AsyncCallback<Integer> async = new AsyncCallback<Integer>() {
            @Override
            public void sucesso(Integer resultado) {
                janela = LeiturasZ.getInstancia();
                janela.setVisible(true);
            }

            @Override
            public void falha(Exception excecao) {
                JOptionPane.showMessageDialog(caixa, excecao.getMessage(), "Gerente", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        if (Login.getOperador().isSisUsuarioGerente()) {
            async.sucesso(Login.getOperador().getSisUsuarioDesconto());
        } else {
            janela = Gerente.getInstancia(async);
            janela.setVisible(true);
        }
    }//GEN-LAST:event_mnuLeiturasZActionPerformed

    private void mnuCat52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCat52ActionPerformed
        janela = Cat52.getInstancia();
        janela.setVisible(true);
    }//GEN-LAST:event_mnuCat52ActionPerformed

    private void mnuCartaoPresenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCartaoPresenteActionPerformed
        Object codigo = JOptionPane.showInputDialog(this, "Digite o código do vendedor.", "CARTÃO PRESENTE",
                JOptionPane.OK_CANCEL_OPTION, mnuCartaoPresente.getIcon(), null, null);
        if (codigo != null) {
            FiltroNumero fn = new FiltroNumero("sisUsuarioId", ECompara.IGUAL, codigo.toString());
            FiltroBinario fb1 = new FiltroBinario("sisUsuarioAtivo", ECompara.IGUAL, true);
            FiltroGrupo gf = new FiltroGrupo(Filtro.E, fn, fb1);

            try {
                SisUsuario vendedor = (SisUsuario) service.selecionar(new SisUsuario(), gf);
                if (vendedor == null) {
                    vendedor = Login.getOperador();
                }
                Object cartao = JOptionPane.showInputDialog(this, "Digite o número do cartão presente.", "CARTÃO PRESENTE",
                        JOptionPane.OK_CANCEL_OPTION, mnuCartaoPresente.getIcon(), null, null);
                if (cartao != null) {
                    try {
                        ComandoCartaoPresente ccp = new ComandoCartaoPresente("ativarCartao", cartao.toString(), vendedor);
                        ccp.executar();
                        ccp.recibo();
                    } catch (OpenPdvException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "CARTÃO PRESENTE", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (OpenPdvException ex) {
                JOptionPane.showMessageDialog(this, "Vendedor não encontrado!", "CARTÃO PRESENTE", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_mnuCartaoPresenteActionPerformed

    private void mnuMFDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMFDActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] path = PAF.gerarArqMFD();
                    Aguarde.getInstancia().setVisible(false);
                    JOptionPane.showMessageDialog(caixa, "Arquivos gerados com sucesso em:\n" + path[0] + "\n" + path[1], "Menu Fiscal", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    Aguarde.getInstancia().setVisible(false);
                    log.error("Nao foi possivel gerar o arquivo -> ", ex);
                    JOptionPane.showMessageDialog(caixa, "Não foi possível gerar o arquivo!", "Menu Fiscal", JOptionPane.WARNING_MESSAGE);
                }
            }
        }).start();
        Aguarde.getInstancia().setVisible(true);
    }//GEN-LAST:event_mnuMFDActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (ecf != null) {
            ecf.desativar();
        }
    }//GEN-LAST:event_formWindowClosing

    private void mnuEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuEnviarActionPerformed
        janela = Sincronismo.getInstancia();
        janela.setVisible(true);
    }//GEN-LAST:event_mnuEnviarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar barMenu;
    private javax.swing.JLabel lblCaixa;
    private javax.swing.JLabel lblFundo;
    private javax.swing.JLabel lblLivre;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblMensagem;
    private javax.swing.JLabel lblOperador;
    private javax.swing.JLabel lblProduto;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JList lstBobina;
    private javax.swing.JMenuItem mnuAbrirVenda;
    private javax.swing.JMenuItem mnuCancelarItem;
    private javax.swing.JMenuItem mnuCancelarVenda;
    private javax.swing.JMenuItem mnuCartaoPresente;
    private javax.swing.JMenuItem mnuCat52;
    private javax.swing.JMenuItem mnuClientes;
    private javax.swing.JMenuItem mnuCupomPresente;
    private javax.swing.JMenuItem mnuEmbalagens;
    private javax.swing.JMenuItem mnuEnviar;
    private javax.swing.JMenuItem mnuEstoque;
    private javax.swing.JMenuItem mnuFecharVenda;
    private javax.swing.JMenu mnuFiscal;
    private javax.swing.JMenu mnuGaveta;
    private javax.swing.JMenu mnuIdentificar;
    private javax.swing.JMenuItem mnuIndice;
    private javax.swing.JMenuItem mnuLMF;
    private javax.swing.JMenuItem mnuLX;
    private javax.swing.JMenuItem mnuLeiturasZ;
    private javax.swing.JMenuItem mnuMF;
    private javax.swing.JMenuItem mnuMFD;
    private javax.swing.JMenu mnuNota;
    private javax.swing.JMenuItem mnuNotaConsumidor;
    private javax.swing.JMenuItem mnuNotaEletronica;
    private javax.swing.JMenuItem mnuPAF;
    private javax.swing.JMenuItem mnuParamConfiguracao;
    private javax.swing.JMenu mnuPesquisa;
    private javax.swing.JMenu mnuPrincipal;
    private javax.swing.JMenuItem mnuProdutos;
    private javax.swing.JMenuItem mnuReceber;
    private javax.swing.JMenuItem mnuReducaoZ;
    private javax.swing.JMenu mnuSair;
    private javax.swing.JMenuItem mnuSangria;
    private javax.swing.JMenu mnuSincronizacao;
    private javax.swing.JMenu mnuSobre;
    private javax.swing.JMenuItem mnuSuprimento;
    private javax.swing.JMenuItem mnuTEF;
    private javax.swing.JMenuItem mnuTipoPagamentos;
    private javax.swing.JMenuItem mnuTrocas;
    private javax.swing.JMenuItem mnuUsuarios;
    private javax.swing.JMenu mnuVenda;
    private javax.swing.JMenuItem mnuVendas;
    private javax.swing.JScrollPane panBobina;
    private javax.swing.JLayeredPane panCamadas;
    private javax.swing.JPopupMenu.Separator separador1;
    private javax.swing.JPopupMenu.Separator separador2;
    private javax.swing.JPopupMenu.Separator separador3;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JFormattedTextField txtQuantidade;
    private javax.swing.JFormattedTextField txtSubTotal;
    private javax.swing.JFormattedTextField txtTotalItem;
    private javax.swing.JFormattedTextField txtUnitario;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que desabilita os menus de venda aberta e seta o modo para
     * disponivel.
     */
    public void modoDisponivel() {
        modo = EModo.DISPONIVEL;
        limpar();
        statusMenus(modo);

        lblLivre.setVisible(true);
        lblMensagem.setText("F7 - ABRIR VENDA");
        lblProduto.setText("CAIXA LIVRE");
        lblProduto.setToolTipText(lblProduto.getText());
        txtCodigo.setEnabled(false);
    }

    /**
     * Metodo que desabilita os menus que usam a ECF e cadastros, seta o modo
     * para indisponivel.
     */
    public void modoIndisponivel() {
        modo = EModo.INDISPONIVEL;
        limpar();
        statusMenus(modo);

        lblLivre.setVisible(true);
        lblMensagem.setText("ECF INDISPONÍVEL");
        txtCodigo.setEnabled(false);
    }

    /**
     * Metodo que desabilita os menus que usam para venda e cadastro, seta o
     * modo para consulta.
     */
    public void modoConsulta() {
        modo = EModo.CONSULTA;
        limpar();
        statusMenus(modo);

        lblLivre.setVisible(true);
        lblMensagem.setText("MODO CONSULTA");
        txtCodigo.setEnabled(true);
        txtCodigo.requestFocus();
    }

    /**
     * Metodo que desabilita os demais menus exceto de operacoes de venda e seta
     * o modo para aberto.
     */
    public void modoAberto() {
        modo = EModo.ABERTO;
        limpar();
        statusMenus(modo);

        lblLivre.setVisible(false);
        lblMensagem.setText("F8 - FECHAR VENDA");
        txtCodigo.setEnabled(true);
        txtCodigo.requestFocus();
        totalizar();
    }

    /**
     * Metodo que desabilita tudo, deixando no estado antes de logar.
     */
    public void modoOff() {
        modo = EModo.OFF;
        limpar();
        statusMenus(modo);

        lblLivre.setVisible(true);
        txtCodigo.setEnabled(false);
        lblOperador.setText("Operador : ");
        lblCaixa.setText("Caixa : ");
    }

    /**
     * Metodo que limpa os campos do caixa.
     */
    public void limpar() {
        lblProduto.setText("");
        lblProduto.setToolTipText(lblProduto.getText());
        txtCodigo.setText("");
        txtQuantidade.setText("1");
        txtUnitario.setText("R$ 0,00");
        txtTotalItem.setText("R$ 0,00");
        txtSubTotal.setText("R$ 0,00");
        lblTotal.setText("R$ 0,00");
        lblMensagem.setText("");
    }

    /**
     * Metodo que desabilita os menus quando em modo consulta ou fiscal.
     *
     * @param atual informa o status atual.
     */
    public void statusMenus(EModo atual) {
        boolean tipo = atual != EModo.OFF;

        // habilita todos os menus
        for (MenuElement me : barMenu.getSubElements()) {
            me.getComponent().setEnabled(tipo);
            for (MenuElement sub : me.getSubElements()) {
                sub.getComponent().setEnabled(tipo);
                for (MenuElement sub1 : sub.getSubElements()) {
                    sub1.getComponent().setEnabled(tipo);
                }
            }
        }

        // verifica o status do caixa e desabilita os menus que nao podem usar
        switch (atual) {
            case ABERTO:
                // menu principal
                mnuSuprimento.setEnabled(false);
                mnuSangria.setEnabled(false);
                mnuReducaoZ.setEnabled(false);
                mnuTEF.setEnabled(false);
                // outros
                mnuFiscal.setEnabled(false);
                mnuNota.setEnabled(false);
                mnuAbrirVenda.setEnabled(false);
                mnuCupomPresente.setEnabled(false);
                mnuCartaoPresente.setEnabled(false);
                if (venda.getSisCliente() != null) {
                    mnuIdentificar.setEnabled(false);
                }
                mnuSair.setEnabled(false);
                break;
            case CONSULTA:
                mnuPrincipal.setEnabled(false);
                mnuNota.setEnabled(false);
                mnuVenda.setEnabled(false);
                mnuIdentificar.setEnabled(false);
                break;
            case DISPONIVEL:
                mnuFecharVenda.setEnabled(false);
                mnuCancelarItem.setEnabled(false);
                mnuIdentificar.setEnabled(false);
                break;
            case INDISPONIVEL:
                // menu principal
                mnuSuprimento.setEnabled(false);
                mnuSangria.setEnabled(false);
                mnuReducaoZ.setEnabled(false);
                mnuTEF.setEnabled(false);
                // menu fical
                mnuLX.setEnabled(false);
                mnuLMF.setEnabled(false);
                mnuMF.setEnabled(false);
                mnuMFD.setEnabled(false);
                mnuPAF.setEnabled(false);
                mnuParamConfiguracao.setEnabled(false);
                // outros
                mnuVenda.setEnabled(false);
                mnuIdentificar.setEnabled(false);
                break;
        }

        // somente habilita o sincronismo nas maquinas host
        if (Util.getConfig().getProperty("sinc.servidor").endsWith("localhost")) {
            mnuSincronizacao.setEnabled(false);
        } else {
            mnuProdutos.setEnabled(false);
            mnuEmbalagens.setEnabled(false);
            mnuUsuarios.setEnabled(false);
        }

        // somente habilita o ADM do TEF se tiver no conf
        if (!Boolean.valueOf(Util.getConfig().getProperty("pag.tef"))) {
            mnuTEF.setEnabled(false);
        }

        // somente mostra o Cat52 caso esteja setado no config
        if (Util.getConfig().getProperty("ecf.cat52") == null) {
            mnuCat52.setVisible(false);
        }

        // somente mostra o Cartao Presente caso esteja setado no config como true
        if (!Boolean.valueOf(Util.getConfig().getProperty("pag.presente"))) {
            mnuCartaoPresente.setVisible(false);
        }
    }

    /**
     * Metodo que adiciona um produto a venda nas operacoes necessarias.
     *
     * @param prod o produto vendido.
     * @param emb referencia para a embalagem do produto usado.
     * @param preco o preco selecionado.
     * @param qtd a quantidade vendida.
     * @throws OpenPdvException dispara caso aconteca algum erro.
     */
    private void adicionar(ProdProduto prod, ProdEmbalagem emb, double preco, double qtd, String barra) throws OpenPdvException {
        // fluxo de adicao ecf, bd e tela
        EcfVendaProduto vp = new EcfVendaProduto(prod, emb, preco, qtd, barra);
        ComandoAdicionarItem adicionarItem = new ComandoAdicionarItem();
        adicionarItem.setVendaProduto(vp);
        adicionarItem.executar();

        // colocando os dados do produto visivel
        lblProduto.setText(Util.normaliza(prod.getProdProdutoDescricao()));
        lblProduto.setToolTipText(lblProduto.getText());
        txtUnitario.setValue(prod.getProdProdutoPreco());
        txtTotalItem.setValue(prod.getProdProdutoPreco() * qtd);
        txtQuantidade.setText("1");
        venda.getEcfVendaProdutos().add(vp);
        totalizar();
    }

    /**
     * Metodo que totaliza os valores no sistema.
     *
     * @return o valor totalizado.
     */
    public double totalizar() {
        double total = 0.00;
        for (EcfVendaProduto vp : venda.getEcfVendaProdutos()) {
            if (!vp.getEcfVendaProdutoCancelado()) {
                total += vp.getEcfVendaProdutoTotal();
            }
        }
        txtSubTotal.setValue(total);
        lblTotal.setText("R$ " + Util.formataNumero(total, 1, 2, true));
        return total;
    }

    //GETs e SETs
    public JMenuBar getBarMenu() {
        return barMenu;
    }

    public void setBarMenu(JMenuBar barMenu) {
        this.barMenu = barMenu;
    }

    public EcfImpressora getImpressora() {
        return impressora;
    }

    public void setImpressora(EcfImpressora impressora) {
        this.impressora = impressora;
    }

    public JDialog getJanela() {
        return janela;
    }

    public void setJanela(JDialog janela) {
        this.janela = janela;
    }

    public JLabel getLblCaixa() {
        return lblCaixa;
    }

    public void setLblCaixa(JLabel lblCaixa) {
        this.lblCaixa = lblCaixa;
    }

    public JLabel getLblFundo() {
        return lblFundo;
    }

    public void setLblFundo(JLabel lblFundo) {
        this.lblFundo = lblFundo;
    }

    public JLabel getLblLivre() {
        return lblLivre;
    }

    public void setLblLivre(JLabel lblLivre) {
        this.lblLivre = lblLivre;
    }

    public JLabel getLblLogo() {
        return lblLogo;
    }

    public void setLblLogo(JLabel lblLogo) {
        this.lblLogo = lblLogo;
    }

    public JLabel getLblMensagem() {
        return lblMensagem;
    }

    public void setLblMensagem(JLabel lblMensagem) {
        this.lblMensagem = lblMensagem;
    }

    public JLabel getLblOperador() {
        return lblOperador;
    }

    public void setLblOperador(JLabel lblOperador) {
        this.lblOperador = lblOperador;
    }

    public JLabel getLblProduto() {
        return lblProduto;
    }

    public void setLblProduto(JLabel lblProduto) {
        this.lblProduto = lblProduto;
    }

    public JLabel getLblTitulo() {
        return lblTitulo;
    }

    public void setLblTitulo(JLabel lblTitulo) {
        this.lblTitulo = lblTitulo;
    }

    public JLabel getLblTotal() {
        return lblTotal;
    }

    public void setLblTotal(JLabel lblTotal) {
        this.lblTotal = lblTotal;
    }

    public JList getLstBobina() {
        return lstBobina;
    }

    public void setLstBobina(JList lstBobina) {
        this.lstBobina = lstBobina;
    }

    public JMenuItem getMnuAbrirVenda() {
        return mnuAbrirVenda;
    }

    public void setMnuAbrirVenda(JMenuItem mnuAbrirVenda) {
        this.mnuAbrirVenda = mnuAbrirVenda;
    }

    public JMenuItem getMnuArquivo() {
        return mnuMFD;
    }

    public void setMnuArquivo(JMenuItem mnuArquivo) {
        this.mnuMFD = mnuArquivo;
    }

    public JMenuItem getMnuCancelarItem() {
        return mnuCancelarItem;
    }

    public void setMnuCancelarItem(JMenuItem mnuCancelarItem) {
        this.mnuCancelarItem = mnuCancelarItem;
    }

    public JMenuItem getMnuCancelarVenda() {
        return mnuCancelarVenda;
    }

    public void setMnuCancelarVenda(JMenuItem mnuCancelarVenda) {
        this.mnuCancelarVenda = mnuCancelarVenda;
    }

    public JMenuItem getMnuClientes() {
        return mnuClientes;
    }

    public void setMnuClientes(JMenuItem mnuClientes) {
        this.mnuClientes = mnuClientes;
    }

    public JMenuItem getMnuEmbalagens() {
        return mnuEmbalagens;
    }

    public void setMnuEmbalagens(JMenuItem mnuEmbalagens) {
        this.mnuEmbalagens = mnuEmbalagens;
    }

    public JMenuItem getMnuEspelho() {
        return mnuMF;
    }

    public void setMnuEspelho(JMenuItem mnuEspelho) {
        this.mnuMF = mnuEspelho;
    }

    public JMenuItem getMnuEstoque() {
        return mnuEstoque;
    }

    public void setMnuEstoque(JMenuItem mnuEstoque) {
        this.mnuEstoque = mnuEstoque;
    }

    public JMenuItem getMnuFecharVenda() {
        return mnuFecharVenda;
    }

    public void setMnuFecharVenda(JMenuItem mnuFecharVenda) {
        this.mnuFecharVenda = mnuFecharVenda;
    }

    public JMenu getMnuFiscal() {
        return mnuFiscal;
    }

    public void setMnuFiscal(JMenu mnuFiscal) {
        this.mnuFiscal = mnuFiscal;
    }

    public JMenu getMnuIdentificar() {
        return mnuIdentificar;
    }

    public void setMnuIdentificar(JMenu mnuIdentificar) {
        this.mnuIdentificar = mnuIdentificar;
    }

    public JMenuItem getMnuIndice() {
        return mnuIndice;
    }

    public void setMnuIndice(JMenuItem mnuIndice) {
        this.mnuIndice = mnuIndice;
    }

    public JMenuItem getMnuLMF() {
        return mnuLMF;
    }

    public void setMnuLMF(JMenuItem mnuLMF) {
        this.mnuLMF = mnuLMF;
    }

    public JMenuItem getMnuLX() {
        return mnuLX;
    }

    public void setMnuLX(JMenuItem mnuLX) {
        this.mnuLX = mnuLX;
    }

    public JMenu getMnuNota() {
        return mnuNota;
    }

    public void setMnuNota(JMenu mnuNota) {
        this.mnuNota = mnuNota;
    }

    public JMenuItem getMnuPAF() {
        return mnuPAF;
    }

    public void setMnuPAF(JMenuItem mnuPAF) {
        this.mnuPAF = mnuPAF;
    }

    public JMenuItem getMnuParamConfiguracao() {
        return mnuParamConfiguracao;
    }

    public void setMnuParamConfiguracao(JMenuItem mnuParamConfiguracao) {
        this.mnuParamConfiguracao = mnuParamConfiguracao;
    }

    public JMenu getMnuPesquisa() {
        return mnuPesquisa;
    }

    public void setMnuPesquisa(JMenu mnuPesquisa) {
        this.mnuPesquisa = mnuPesquisa;
    }

    public JMenu getMnuPrincipal() {
        return mnuPrincipal;
    }

    public void setMnuPrincipal(JMenu mnuPrincipal) {
        this.mnuPrincipal = mnuPrincipal;
    }

    public JMenuItem getMnuProdutos() {
        return mnuProdutos;
    }

    public void setMnuProdutos(JMenuItem mnuProdutos) {
        this.mnuProdutos = mnuProdutos;
    }

    public JMenuItem getMnuReducaoZ() {
        return mnuReducaoZ;
    }

    public void setMnuReducaoZ(JMenuItem mnuReducaoZ) {
        this.mnuReducaoZ = mnuReducaoZ;
    }

    public JMenu getMnuSair() {
        return mnuSair;
    }

    public void setMnuSair(JMenu mnuSair) {
        this.mnuSair = mnuSair;
    }

    public JMenuItem getMnuSangria() {
        return mnuSangria;
    }

    public void setMnuSangria(JMenuItem mnuSangria) {
        this.mnuSangria = mnuSangria;
    }

    public JMenuItem getMnuSincronizacao() {
        return mnuReceber;
    }

    public void setMnuSincronizacao(JMenuItem mnuSincronizacao) {
        this.mnuReceber = mnuSincronizacao;
    }

    public JMenu getMnuSobre() {
        return mnuSobre;
    }

    public void setMnuSobre(JMenu mnuSobre) {
        this.mnuSobre = mnuSobre;
    }

    public JMenuItem getMnuSuprimento() {
        return mnuSuprimento;
    }

    public void setMnuSuprimento(JMenuItem mnuSuprimento) {
        this.mnuSuprimento = mnuSuprimento;
    }

    public JMenuItem getMnuUsuarios() {
        return mnuUsuarios;
    }

    public void setMnuUsuarios(JMenuItem mnuUsuarios) {
        this.mnuUsuarios = mnuUsuarios;
    }

    public JMenu getMnuVenda() {
        return mnuVenda;
    }

    public void setMnuVenda(JMenu mnuVenda) {
        this.mnuVenda = mnuVenda;
    }

    public JMenuItem getMnuVendas() {
        return mnuVendas;
    }

    public void setMnuVendas(JMenuItem mnuVendas) {
        this.mnuVendas = mnuVendas;
    }

    public EModo getModo() {
        return modo;
    }

    public void setModo(EModo modo) {
        this.modo = modo;
    }

    public JScrollPane getPanBobina() {
        return panBobina;
    }

    public void setPanBobina(JScrollPane panBobina) {
        this.panBobina = panBobina;
    }

    public JLayeredPane getPanCamadas() {
        return panCamadas;
    }

    public void setPanCamadas(JLayeredPane panCamadas) {
        this.panCamadas = panCamadas;
    }

    public AsyncCallback<ProdProduto> getPesquisado() {
        return pesquisado;
    }

    public void setPesquisado(AsyncCallback<ProdProduto> pesquisado) {
        this.pesquisado = pesquisado;
    }

    public Separator getSeparador1() {
        return separador1;
    }

    public void setSeparador1(Separator separador1) {
        this.separador1 = separador1;
    }

    public Separator getSeparador2() {
        return separador2;
    }

    public void setSeparador2(Separator separador2) {
        this.separador2 = separador2;
    }

    public CoreService getService() {
        return service;
    }

    public void setService(CoreService service) {
        this.service = service;
    }

    public KeyEventPostProcessor getTeclas() {
        return teclas;
    }

    public void setTeclas(KeyEventPostProcessor teclas) {
        this.teclas = teclas;
    }

    public JTextField getTxtCodigo() {
        return txtCodigo;
    }

    public void setTxtCodigo(JTextField txtCodigo) {
        this.txtCodigo = txtCodigo;
    }

    public JFormattedTextField getTxtQuantidade() {
        return txtQuantidade;
    }

    public void setTxtQuantidade(JFormattedTextField txtQuantidade) {
        this.txtQuantidade = txtQuantidade;
    }

    public JFormattedTextField getTxtSubTotal() {
        return txtSubTotal;
    }

    public void setTxtSubTotal(JFormattedTextField txtSubTotal) {
        this.txtSubTotal = txtSubTotal;
    }

    public JFormattedTextField getTxtTotalItem() {
        return txtTotalItem;
    }

    public void setTxtTotalItem(JFormattedTextField txtTotalItem) {
        this.txtTotalItem = txtTotalItem;
    }

    public JFormattedTextField getTxtUnitario() {
        return txtUnitario;
    }

    public void setTxtUnitario(JFormattedTextField txtUnitario) {
        this.txtUnitario = txtUnitario;
    }

    public SisEmpresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(SisEmpresa empresa) {
        this.empresa = empresa;
    }

    public JMenu getMnuGaveta() {
        return mnuGaveta;
    }

    public void setMnuGaveta(JMenu mnuGaveta) {
        this.mnuGaveta = mnuGaveta;
    }

    public JMenuItem getMnuNotaConsumidor() {
        return mnuNotaConsumidor;
    }

    public void setMnuNotaConsumidor(JMenuItem mnuNotaConsumidor) {
        this.mnuNotaConsumidor = mnuNotaConsumidor;
    }

    public JMenuItem getMnuNotaEletronica() {
        return mnuNotaEletronica;
    }

    public void setMnuNotaEletronica(JMenuItem mnuNotaEletronica) {
        this.mnuNotaEletronica = mnuNotaEletronica;
    }

    public JOptionPane getOption() {
        return option;
    }

    public void setOption(JOptionPane option) {
        this.option = option;
    }

    public EcfVenda getVenda() {
        return venda;
    }

    public void setVenda(EcfVenda venda) {
        this.venda = venda;
    }

    public DefaultListModel getBobina() {
        return bobina;
    }

    public void setBobina(DefaultListModel bobina) {
        this.bobina = bobina;
    }

    public JMenuItem getMnuCupomPresente() {
        return mnuCupomPresente;
    }

    public void setMnuCupomPresente(JMenuItem mnuCupomPresente) {
        this.mnuCupomPresente = mnuCupomPresente;
    }

    public JMenuItem getMnuTEF() {
        return mnuTEF;
    }

    public void setMnuTEF(JMenuItem mnuTEF) {
        this.mnuTEF = mnuTEF;
    }

    public JMenuItem getMnuTipoPagamentos() {
        return mnuTipoPagamentos;
    }

    public void setMnuTipoPagamentos(JMenuItem mnuTipoPagamentos) {
        this.mnuTipoPagamentos = mnuTipoPagamentos;
    }
}
