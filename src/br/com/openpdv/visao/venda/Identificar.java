package br.com.openpdv.visao.venda;

import br.com.openpdv.controlador.core.AsyncCallback;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.TextFieldLimit;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.openpdv.modelo.sistema.SisUsuario;
import br.com.openpdv.visao.core.Caixa;
import br.com.openpdv.visao.principal.Clientes;
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

        // colocando limites nos campos
        txtCPF_CNPJ.setDocument(new TextFieldLimit(14, true));
        txtNome.setDocument(new TextFieldLimit(100));
        txtEndereco.setDocument(new TextFieldLimit(255));
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

        painel = new javax.swing.JPanel();
        lblCPF_CNPJ = new javax.swing.JLabel();
        txtCPF_CNPJ = new javax.swing.JTextField();
        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        lblEndereco = new javax.swing.JLabel();
        txtEndereco = new javax.swing.JTextField();
        lblCodigo = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        lblVendedor = new javax.swing.JLabel();
        txtVendedor = new javax.swing.JTextField();
        chkRecuperar = new javax.swing.JCheckBox();
        separador1 = new javax.swing.JSeparator();
        txtCCF = new javax.swing.JTextField();
        btnCadastrar = new javax.swing.JButton();
        chkCPF = new javax.swing.JCheckBox();
        btnOK = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

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

        lblEndereco.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblEndereco.setText("Endereço:");

        txtEndereco.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        txtEndereco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEnderecoKeyPressed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout painelLayout = new org.jdesktop.layout.GroupLayout(painel);
        painel.setLayout(painelLayout);
        painelLayout.setHorizontalGroup(
            painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(painelLayout.createSequentialGroup()
                .addContainerGap()
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
                        .add(txtNome))
                    .add(txtEndereco))
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
                    .add(txtCPF_CNPJ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(painelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblEndereco)
                    .add(txtEndereco, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        chkRecuperar.setText("Recuperar CCF:");
        chkRecuperar.setToolTipText("Use esta opção para recuperar uma venda anterior.");
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

        btnCadastrar.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/cliente.png"))); // NOI18N
        btnCadastrar.setText("Cadastrar Cliente");
        btnCadastrar.setMaximumSize(new java.awt.Dimension(100, 30));
        btnCadastrar.setMinimumSize(new java.awt.Dimension(100, 30));
        btnCadastrar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });
        btnCadastrar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCadastrarKeyPressed(evt);
            }
        });

        chkCPF.setSelected(true);
        chkCPF.setText("CPF no Cupom");

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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(layout.createSequentialGroup()
                        .add(btnCadastrar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 181, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chkCPF)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(btnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, painel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(lblCodigo)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtCodigo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblVendedor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtVendedor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chkRecuperar)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtCCF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, separador1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .add(btnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnCadastrar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkCPF))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(522, 211));
        setLocationRelativeTo(null);
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

    private void txtCPF_CNPJFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCPF_CNPJFocusLost
        validarCliente();
    }//GEN-LAST:event_txtCPF_CNPJFocusLost

    private void txtCPF_CNPJKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCPF_CNPJKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNome.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cancelar();
        }
    }//GEN-LAST:event_txtCPF_CNPJKeyPressed

    private void txtNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtEndereco.requestFocus();
        }
    }//GEN-LAST:event_txtNomeKeyPressed

    private void txtEnderecoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEnderecoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnOK.requestFocus();
        }
    }//GEN-LAST:event_txtEnderecoKeyPressed

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        AsyncCallback<SisCliente> async = new AsyncCallback<SisCliente>() {
            @Override
            public void sucesso(SisCliente resultado) {
                cliente = resultado;
                txtCPF_CNPJ.setText(cliente.getSisClienteDoc());
                txtNome.setText(cliente.getSisClienteNome());
                txtEndereco.setText(cliente.getSisClienteEndereco());
                btnOK.requestFocus();
            }

            @Override
            public void falha(Exception excecao) {
                cliente = null;
                txtCPF_CNPJ.setText("");
                txtNome.setText("NAO INFORMADO");
                txtEndereco.setText("NAO INFORMADO");
                btnOK.requestFocus();
            }
        };
        Clientes clientes = Clientes.getInstancia(async);
        clientes.setVisible(true);
    }//GEN-LAST:event_btnCadastrarActionPerformed

    private void btnCadastrarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCadastrarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnCadastrarActionPerformed(null);
        }
    }//GEN-LAST:event_btnCadastrarKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnOK;
    private javax.swing.JCheckBox chkCPF;
    private javax.swing.JCheckBox chkRecuperar;
    private javax.swing.JLabel lblCPF_CNPJ;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblEndereco;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblVendedor;
    private javax.swing.JPanel painel;
    private javax.swing.JSeparator separador1;
    private javax.swing.JTextField txtCCF;
    private javax.swing.JTextField txtCPF_CNPJ;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtEndereco;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtVendedor;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que salva um novo registro.
     */
    private void salvar() {
        if (validar()) {
            try {
                if (cliente == null) {
                    FiltroBinario fb = new FiltroBinario("sisEmpresaContador", ECompara.IGUAL, false);
                    SisEmpresa empresa = (SisEmpresa) service.selecionar(new SisEmpresa(), fb);

                    cliente = new SisCliente();
                    cliente.setSisClienteDoc(txtCPF_CNPJ.getText().replaceAll("\\D", ""));
                    cliente.setSisClienteNome(txtNome.getText());
                    cliente.setSisClienteEndereco(txtEndereco.getText());
                    cliente.setSisClienteDoc1("ISENTO");
                    cliente.setSisClienteNumero(0);
                    cliente.setSisClienteComplemento("");
                    cliente.setSisClienteBairro("NAO INFORMADO");
                    cliente.setSisClienteCep("00000-000");
                    cliente.setSisMunicipio(empresa.getSisMunicipio());
                    cliente.setSisClienteTelefone("(00) 0000-0000");
                    cliente.setSisClienteEmail("n@o.informado");
                    cliente.setSisClienteData(new Date());
                    cliente = (SisCliente) service.salvar(cliente);
                }
                cliente.setVendedor(vendedor);
                cliente.setCpfCupom(chkCPF.isSelected());
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
            cliente.setCpfCupom(false);
            async.sucesso(cliente);
            Caixa.getInstancia().setJanela(null);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "O CPF/CNPJ não é válido!", "Identificar", JOptionPane.INFORMATION_MESSAGE);
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
            if (validar()) {
                String texto = txtCPF_CNPJ.getText().replaceAll("\\D", "");
                FiltroTexto ft = new FiltroTexto("sisClienteDoc", ECompara.IGUAL, texto);
                List<SisCliente> lista = (List<SisCliente>) service.selecionar(new SisCliente(), 0, 1, ft);

                if (lista.size() > 0) {
                    cliente = lista.get(0);
                    txtNome.setText(cliente.getSisClienteNome());
                    txtEndereco.setText(cliente.getSisClienteEndereco());
                } else {
                    txtNome.setText("NAO INFORMADO");
                    txtEndereco.setText("NAO INFORMADO");
                }
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
        txtEndereco.setText("");
        txtCodigo.setText("");
        txtVendedor.setText("");
        txtCCF.setText("");
        chkRecuperar.setSelected(false);
        chkCPF.setSelected(true);
        txtCPF_CNPJ.requestFocus();
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
     * Metodo que acha o vendedor pelo codigo.
     */
    private void acharVendedor() {
        if (!txtCodigo.getText().equals("")) {
            FiltroNumero fn = new FiltroNumero("sisUsuarioId", ECompara.IGUAL, txtCodigo.getText());
            FiltroBinario fb1 = new FiltroBinario("sisUsuarioAtivo", ECompara.IGUAL, true);
            FiltroGrupo gf = new FiltroGrupo(Filtro.E, fn, fb1);

            try {
                vendedor = (SisUsuario) service.selecionar(new SisUsuario(), gf);
                if (vendedor != null) {
                    txtVendedor.setText(vendedor.getSisUsuarioLogin());
                    btnOK.requestFocus();
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

    public JLabel getLblCPF_CNPJ() {
        return lblCPF_CNPJ;
    }

    public void setLblCPF_CNPJ(JLabel lblCPF_CNPJ) {
        this.lblCPF_CNPJ = lblCPF_CNPJ;
    }

    public JLabel getLblEndereco() {
        return lblEndereco;
    }

    public void setLblEndereco(JLabel lblEndereco) {
        this.lblEndereco = lblEndereco;
    }

    public JLabel getLblNome() {
        return lblNome;
    }

    public void setLblNome(JLabel lblNome) {
        this.lblNome = lblNome;
    }

    public JPanel getPainel() {
        return painel;
    }

    public void setPainel(JPanel painel) {
        this.painel = painel;
    }

    public JTextField getTxtCPF_CNPJ() {
        return txtCPF_CNPJ;
    }

    public void setTxtCPF_CNPJ(JTextField txtCPF_CNPJ) {
        this.txtCPF_CNPJ = txtCPF_CNPJ;
    }

    public JTextField getTxtEndereco() {
        return txtEndereco;
    }

    public void setTxtEndereco(JTextField txtEndereco) {
        this.txtEndereco = txtEndereco;
    }

    public JTextField getTxtNome() {
        return txtNome;
    }

    public void setTxtNome(JTextField txtNome) {
        this.txtNome = txtNome;
    }
}
