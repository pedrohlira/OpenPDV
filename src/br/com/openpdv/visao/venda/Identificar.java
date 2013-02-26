package br.com.openpdv.visao.venda;

import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.TextFieldLimit;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.modelo.sistema.SisEstado;
import br.com.openpdv.modelo.sistema.SisMunicipio;
import br.com.openpdv.modelo.sistema.SisUsuario;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 * Classe que representa o formulario de identificacao do cliente no sistema.
 *
 * @author Pedro H. Lira
 */
public class Identificar extends javax.swing.JDialog {

    private static Identificar identificar;
    private Logger log;
    private AsyncCallback<SisCliente> async;
    private SisCliente cliente;
    private SisUsuario vendedor;
    private CoreService service;
    private EcfVenda venda;

    /**
     * Construtor padrao.
     */
    private Identificar() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Identificar.class);
        service = new CoreService<>();
        initComponents();
        setUF();

        // colocando limites nos campos
        txtCPF_CNPJ.setDocument(new TextFieldLimit(20));
        txtNome.setDocument(new TextFieldLimit(100));
        txtIE.setDocument(new TextFieldLimit(20));
        txtEndereco.setDocument(new TextFieldLimit(255));
        txtNumero.setDocument(new TextFieldLimit(11, true));
        txtComplemento.setDocument(new TextFieldLimit(100));
        txtBairro.setDocument(new TextFieldLimit(100));
        txtCEP.setDocument(new TextFieldLimit(9));
        txtTelefone.setDocument(new TextFieldLimit(20));
        txtEmail.setDocument(new TextFieldLimit(100));
    }

    /**
     * Metodo que retorna a instancia unica de Usuarios.
     *
     * @param async o metodo assincrono de resposta.
     * @return o objeto de Usuarios.
     */
    public static Identificar getInstancia(AsyncCallback<SisCliente> async) {
        if (identificar == null) {
            identificar = new Identificar();
        }

        identificar.limpar();
        identificar.async = async;
        return identificar;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCodigo = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        lblVendedor = new javax.swing.JLabel();
        txtVendedor = new javax.swing.JTextField();
        separador1 = new javax.swing.JSeparator();
        chkRecuperar = new javax.swing.JCheckBox();
        txtCCF = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        painel = new javax.swing.JPanel();
        lblCPF_CNPJ = new javax.swing.JLabel();
        txtCPF_CNPJ = new javax.swing.JTextField();
        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        lblIE = new javax.swing.JLabel();
        txtIE = new javax.swing.JTextField();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Identificar Cliente e/ou Vendedor");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lblCodigo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCodigo.setText("Código:");

        txtCodigo.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCodigo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodigoFocusLost(evt);
            }
        });
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
        });

        lblVendedor.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblVendedor.setText("Vendedor:");

        txtVendedor.setEditable(false);
        txtVendedor.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtVendedor.setFocusable(false);

        chkRecuperar.setText("Recuperar Venda pelo CCF ->");
        chkRecuperar.setToolTipText("");
        chkRecuperar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRecuperarActionPerformed(evt);
            }
        });

        txtCCF.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCCF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCCF.setEnabled(false);
        txtCCF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCCFFocusLost(evt);
            }
        });
        txtCCF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCCFKeyPressed(evt);
            }
        });

        btnOK.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/salvar.png"))); // NOI18N
        btnOK.setText("OK");
        btnOK.setMaximumSize(new java.awt.Dimension(100, 30));
        btnOK.setMinimumSize(new java.awt.Dimension(100, 30));
        btnOK.setPreferredSize(new java.awt.Dimension(100, 30));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        btnOK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnOKKeyPressed(evt);
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

        painel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblCPF_CNPJ.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblCPF_CNPJ.setText("CPF/CNPJ:");

        txtCPF_CNPJ.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtCPF_CNPJ.setToolTipText("CPF ou CNPJ.");
        txtCPF_CNPJ.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCPF_CNPJFocusLost(evt);
            }
        });
        txtCPF_CNPJ.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCPF_CNPJKeyPressed(evt);
            }
        });

        lblNome.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblNome.setText("Nome:");

        txtNome.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtNome.setToolTipText("Nome ou Razão Social.");
        txtNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomeKeyPressed(evt);
            }
        });

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

        org.jdesktop.layout.GroupLayout painelLayout = new org.jdesktop.layout.GroupLayout(painel);
        painel.setLayout(painelLayout);
        painelLayout.setHorizontalGroup(
            painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(painelLayout.createSequentialGroup()
                .addContainerGap()
                .add(painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(painelLayout.createSequentialGroup()
                        .add(lblUF)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmbUF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblMunicipio)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmbMunicipio, 0, 249, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblTelefone)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtTelefone, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblEmail)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtEmail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(1, 1, 1))
                    .add(painelLayout.createSequentialGroup()
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
                                .add(lblIE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtIE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
                            .add(txtCEP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))))
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
                    .add(lblIE)
                    .add(txtIE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(btnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, separador1)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                            .add(lblCodigo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(txtCodigo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(lblVendedor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(txtVendedor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 260, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(chkRecuperar)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(txtCCF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, painel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(painel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblCodigo)
                    .add(txtCodigo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblVendedor)
                    .add(txtVendedor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkRecuperar)
                    .add(txtCCF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-825)/2, (screenSize.height-243)/2, 825, 243);
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        salvar();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnOKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOKKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            salvar();
        }
    }//GEN-LAST:event_btnOKKeyPressed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cancelar();
        }
    }//GEN-LAST:event_btnCancelarKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cancelar();
    }//GEN-LAST:event_formWindowClosing

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnOK.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cancelar();
        }
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void txtCodigoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoFocusLost
        acharVendedor();
    }//GEN-LAST:event_txtCodigoFocusLost

    private void txtCCFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCCFFocusLost
        acharVenda();
    }//GEN-LAST:event_txtCCFFocusLost

    private void txtCCFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCCFKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnOK.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cancelar();
        }
    }//GEN-LAST:event_txtCCFKeyPressed

    private void chkRecuperarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRecuperarActionPerformed
        if (chkRecuperar.isSelected()) {
            txtCCF.setEnabled(true);
            txtCCF.requestFocus();
        } else {
            txtCCF.setText("");
            txtCCF.setEnabled(false);
        }
    }//GEN-LAST:event_chkRecuperarActionPerformed

    private void cmbUFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUFActionPerformed
        setMunicipio((SisEstado) cmbUF.getSelectedItem());
    }//GEN-LAST:event_cmbUFActionPerformed

    private void txtCPF_CNPJFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCPF_CNPJFocusLost
        validarCliente();
    }//GEN-LAST:event_txtCPF_CNPJFocusLost

    private void txtCPF_CNPJKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCPF_CNPJKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cancelar();
        }
    }//GEN-LAST:event_txtCPF_CNPJKeyPressed

    private void txtNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cancelar();
        }
    }//GEN-LAST:event_txtNomeKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnOK;
    private javax.swing.JCheckBox chkRecuperar;
    private javax.swing.JComboBox cmbMunicipio;
    private javax.swing.JComboBox cmbUF;
    private javax.swing.JLabel lblBairro;
    private javax.swing.JLabel lblCEP;
    private javax.swing.JLabel lblCPF_CNPJ;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblComplemento;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEndereco;
    private javax.swing.JLabel lblIE;
    private javax.swing.JLabel lblMunicipio;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblNumero;
    private javax.swing.JLabel lblTelefone;
    private javax.swing.JLabel lblUF;
    private javax.swing.JLabel lblVendedor;
    private javax.swing.JPanel painel;
    private javax.swing.JSeparator separador1;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JTextField txtCCF;
    private javax.swing.JFormattedTextField txtCEP;
    private javax.swing.JTextField txtCPF_CNPJ;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtComplemento;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEndereco;
    private javax.swing.JTextField txtIE;
    private javax.swing.JTextField txtNome;
    private javax.swing.JFormattedTextField txtNumero;
    private javax.swing.JTextField txtTelefone;
    private javax.swing.JTextField txtVendedor;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que salva um novo registro.
     */
    private void salvar() {
        if (validar() && !txtNome.getText().equals("")) {
            try {
                if (cliente == null) {
                    cliente = new SisCliente();
                }
                cliente.setSisClienteDoc(txtCPF_CNPJ.getText());
                cliente.setSisClienteNome(txtNome.getText());
                cliente.setSisClienteDoc1(txtIE.getText());
                cliente.setSisClienteEndereco(txtEndereco.getText());
                cliente.setSisClienteNumero(Integer.valueOf(txtNumero.getText()));
                cliente.setSisClienteComplemento(txtComplemento.getText());
                cliente.setSisClienteBairro(txtBairro.getText());
                cliente.setSisClienteCep(txtCEP.getText());
                cliente.setSisMunicipio((SisMunicipio) cmbMunicipio.getSelectedItem());
                cliente.setSisClienteTelefone(txtTelefone.getText());
                cliente.setSisClienteEmail(txtEmail.getText());
                cliente.setSisClienteData(new Date());
                cliente = (SisCliente) service.salvar(cliente);
                cliente.setVendedor(vendedor);

                setVisible(false);
                async.sucesso(cliente);
            } catch (OpenPdvException ex) {
                async.falha(ex);
                log.error("Erro ao salvar o cliente.", ex);
            } finally {
                Caixa.getInstancia().setJanela(null);
                dispose();
            }
        } else if (txtCPF_CNPJ.getText().equals("")) {
            cliente = new SisCliente();
            cliente.setVendedor(vendedor);
            setVisible(false);
            async.sucesso(cliente);
        } else {
            JOptionPane.showMessageDialog(this, "O CPF/CNPJ não é válido ou Não informou o Nome!", "Identificar", JOptionPane.INFORMATION_MESSAGE);
            txtCPF_CNPJ.requestFocus();
        }
    }

    /**
     * Metodo que cancela o registro.
     */
    private void cancelar() {
        async.sucesso(null);
        Caixa.getInstancia().setJanela(null);
        dispose();
    }

    /**
     * Metodo que pesquisa o cliente pelo documento informado.
     */
    private void validarCliente() {
        try {
            String texto = txtCPF_CNPJ.getText().replaceAll("\\D", "");
            FiltroTexto ft = new FiltroTexto("sisClienteDoc", ECompara.IGUAL, texto);
            cliente = (SisCliente) service.selecionar(new SisCliente(), ft);

            if (cliente != null) {
                txtNome.setText(cliente.getSisClienteNome());
                txtIE.setText(cliente.getSisClienteDoc1());
                txtEndereco.setText(cliente.getSisClienteEndereco());
                txtNumero.setText(cliente.getSisClienteNumero() + "");
                txtComplemento.setText(cliente.getSisClienteComplemento());
                txtBairro.setText(cliente.getSisClienteBairro());
                txtCEP.setText(cliente.getSisClienteCep());
                if (cliente.getSisMunicipio() != null) {
                    Util.selecionarCombo(cmbUF, cliente.getSisMunicipio().getSisEstado().getSisEstadoSigla());
                    Util.selecionarCombo(cmbMunicipio, cliente.getSisMunicipio().getSisMunicipioDescricao());
                } else {
                    Util.selecionarCombo(cmbUF, Caixa.getInstancia().getEmpresa().getSisMunicipio().getSisEstado().getSisEstadoSigla());
                    cmbMunicipio.setSelectedIndex(-1);
                }
                txtTelefone.setText(cliente.getSisClienteTelefone());
                txtEmail.setText(cliente.getSisClienteEmail());
            }
        } catch (Exception ex) {
            cliente = null;
        }
    }

    /**
     * Metodo que limpa os campos.
     */
    private void limpar() {
        cliente = null;
        vendedor = null;
        venda = null;
        txtCPF_CNPJ.setText("");
        txtNome.setText("");
        txtIE.setText("");
        txtEndereco.setText("");
        txtNumero.setText("");
        txtComplemento.setText("");
        txtBairro.setText("");
        txtCEP.setText("");
        cmbMunicipio.setSelectedIndex(-1);
        txtTelefone.setText("");
        txtEmail.setText("");
        txtCodigo.setText("");
        txtVendedor.setText("");
        txtCCF.setText("");
        chkRecuperar.setSelected(false);
        txtCPF_CNPJ.requestFocus();
    }

    /**
     * Metodo que valida o CPF e/ou CNPJ
     *
     * @return retorna verdadeiro se valido, falso caso contrario.
     */
    private boolean validar() {
        String texto = txtCPF_CNPJ.getText();
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
     * Metodo que carrega os valores das UFs.
     */
    private void setUF() {
        try {
            List<SisEstado> lista = service.selecionar(new SisEstado(), 0, 0, null);
            cmbUF.removeAllItems();
            for (SisEstado est : lista) {
                cmbUF.addItem(est);
            }
            cmbUF.setSelectedItem(Caixa.getInstancia().getEmpresa().getSisMunicipio().getSisEstado());
            setMunicipio((SisEstado) cmbUF.getSelectedItem());
        } catch (OpenPdvException ex) {
            log.error("Nao carregou as UFs.", ex);
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

    /**
     * Metodo que acha o vendedor pelo codigo.
     */
    private void acharVendedor() {
        if (!txtCodigo.getText().equals("")) {
            FiltroNumero fn = new FiltroNumero("sisUsuarioId", ECompara.IGUAL, txtCodigo.getText());
            FiltroBinario fb1 = new FiltroBinario("sisUsuarioAtivo", ECompara.IGUAL, true);
            FiltroBinario fb2 = new FiltroBinario("sisUsuarioCaixa", ECompara.IGUAL, false);
            GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fn, fb1, fb2});

            try {
                vendedor = (SisUsuario) service.selecionar(new SisUsuario(), gf);
                if (vendedor != null) {
                    txtVendedor.setText(vendedor.getSisUsuarioLogin());
                } else {
                    throw new OpenPdvException();
                }
            } catch (OpenPdvException ex) {
                txtCodigo.setText("");
                txtVendedor.setText("");
                JOptionPane.showMessageDialog(this, "Vendedor não encontrado!", "Identificar", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Metodo que acha a venda pelo CCF informado.
     */
    private void acharVenda() {
        if (!txtCCF.getText().equals("")) {
            FiltroNumero fn = new FiltroNumero("ecfVendaCcf", ECompara.IGUAL, txtCCF.getText());

            try {
                venda = (EcfVenda) service.selecionar(new EcfVenda(), fn);
                if (venda == null) {
                    throw new OpenPdvException();
                }
            } catch (OpenPdvException ex) {
                txtCCF.setText("");
                JOptionPane.showMessageDialog(this, "Venda não encontrada!", "Identificar", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // GETs e SETs
    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

    public JButton getBtnOK() {
        return btnOK;
    }

    public void setBtnOK(JButton btnOK) {
        this.btnOK = btnOK;
    }

    public SisCliente getCliente() {
        return cliente;
    }

    public void setCliente(SisCliente cliente) {
        this.cliente = cliente;
    }

    public JLabel getLblCodigo() {
        return lblCodigo;
    }

    public void setLblCodigo(JLabel lblCodigo) {
        this.lblCodigo = lblCodigo;
    }

    public JLabel getLblVendedor() {
        return lblVendedor;
    }

    public void setLblVendedor(JLabel lblVendedor) {
        this.lblVendedor = lblVendedor;
    }

    public JSeparator getSeparador1() {
        return separador1;
    }

    public void setSeparador1(JSeparator separador1) {
        this.separador1 = separador1;
    }

    public JTextField getTxtCodigo() {
        return txtCodigo;
    }

    public void setTxtCodigo(JTextField txtCodigo) {
        this.txtCodigo = txtCodigo;
    }

    public JTextField getTxtVendedor() {
        return txtVendedor;
    }

    public void setTxtVendedor(JTextField txtVendedor) {
        this.txtVendedor = txtVendedor;
    }

    public SisUsuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(SisUsuario vendedor) {
        this.vendedor = vendedor;
    }

    public AsyncCallback<SisCliente> getAsync() {
        return async;
    }

    public void setAsync(AsyncCallback<SisCliente> async) {
        this.async = async;
    }

    public CoreService<SisCliente> getService() {
        return service;
    }

    public void setService(CoreService<SisCliente> service) {
        this.service = service;
    }

    public EcfVenda getVenda() {
        return venda;
    }

    public void setVenda(EcfVenda venda) {
        this.venda = venda;
    }

    public JCheckBox getChkRecuperar() {
        return chkRecuperar;
    }

    public void setChkRecuperar(JCheckBox chkRecuperar) {
        this.chkRecuperar = chkRecuperar;
    }

    public JTextField getTxtCCF() {
        return txtCCF;
    }

    public void setTxtCCF(JTextField txtCCF) {
        this.txtCCF = txtCCF;
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

    public JTextField getTxtTelefone() {
        return txtTelefone;
    }

    public void setTxtTelefone(JTextField txtTelefone) {
        this.txtTelefone = txtTelefone;
    }
}
