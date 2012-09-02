package br.com.openpdv.visao.principal;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.sistema.SisCliente;
import br.com.openpdv.visao.core.Caixa;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
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
    private DefaultTableModel dtm;
    private CoreService<SisCliente> service;

    /**
     * Construtor padrao.
     */
    private Clientes() {
        super(Caixa.getInstancia());
        log = Logger.getLogger(Clientes.class);
        initComponents();
        service = new CoreService<>();
        dtm = (DefaultTableModel) tabClientes.getModel();
    }

    /**
     * Metodo que retorna a instancia do componente.
     *
     * @return o objeto do componente.
     */
    public static Clientes getInstancia() {
        if (clientes == null) {
            clientes = new Clientes();
        }

        clientes.setLista();
        return clientes;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spClientes = new javax.swing.JScrollPane();
        tabClientes = new javax.swing.JTable();
        separador = new javax.swing.JSeparator();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Clientes");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tabClientes.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        tabClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CPF/CNPJ", "Nome", "Endereço"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabClientes.setAutoCreateRowSorter(true);
        tabClientes.setRowHeight(20);
        tabClientes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabClientes.setShowGrid(true);
        tabClientes.setShowVerticalLines(false);
        tabClientes.getTableHeader().setReorderingAllowed(false);
        spClientes.setViewportView(tabClientes);
        tabClientes.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabClientes.getColumnModel().getColumn(0).setResizable(false);
        tabClientes.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabClientes.getColumnModel().getColumn(1).setResizable(false);
        tabClientes.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabClientes.getColumnModel().getColumn(2).setResizable(false);
        tabClientes.getColumnModel().getColumn(2).setPreferredWidth(200);

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
                    .add(spClientes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 648, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 648, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(6, 6, 6))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(spClientes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 216, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnCancelar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-671)/2, (screenSize.height-304)/2, 671, 304);
    }// </editor-fold>//GEN-END:initComponents

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

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Caixa.getInstancia().setJanela(null);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JSeparator separador;
    private javax.swing.JScrollPane spClientes;
    private javax.swing.JTable tabClientes;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que seta os valores da tabela vindas do banco de dados.
     */
    private void setLista() {
        try {
            List<SisCliente> lista = service.selecionar(new SisCliente(), 0, 0, null);

            while (dtm.getRowCount() > 0) {
                dtm.removeRow(0);
            }

            for (SisCliente cliente : lista) {
                Object[] obj = new Object[]{cliente.getSisClienteDoc(), cliente.getSisClienteNome(), cliente.getSisClienteEndereco()};
                dtm.addRow(obj);
            }
        } catch (OpenPdvException ex) {
            log.error("Erro ao selecionar os clientes do sistema", ex);
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

    public JSeparator getjSeparator() {
        return separador;
    }

    public void setjSeparator1(JSeparator jSeparator) {
        this.separador = jSeparator;
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
}
