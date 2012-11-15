package br.com.openpdv.visao.core;

import br.com.openpdv.modelo.core.EModo;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;

/**
 * Classe que representa a tela de espera.
 *
 * @author Pedro H. Lira
 */
public class Aguarde extends javax.swing.JDialog {

    private static Aguarde aguarde;

    /**
     * Construtor padrao.
     */
    private Aguarde() {
        super();
        initComponents();
        pgbBarra.setIndeterminate(true);
    }

    /**
     * Metodo que retorna a instancia unica de Aguarde.
     *
     * @return o objeto de Aguarde.
     */
    public static Aguarde getInstancia() {
        if (aguarde == null) {
            aguarde = new Aguarde();
        }

        return aguarde;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblMensagem = new javax.swing.JLabel();
        separador = new javax.swing.JSeparator();
        pgbBarra = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Processando...");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setModal(true);
        setResizable(false);

        lblMensagem.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblMensagem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMensagem.setText("Aguarde o processamento...");

        pgbBarra.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        pgbBarra.setToolTipText("Aguarde o processamento.");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lblMensagem, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                    .add(separador)
                    .add(pgbBarra, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(lblMensagem)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pgbBarra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblMensagem;
    private javax.swing.JProgressBar pgbBarra;
    private javax.swing.JSeparator separador;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setVisible(boolean b) {
        Caixa caixa = Caixa.getInstancia();
        if (b) {
            caixa.statusMenus(EModo.OFF);
            caixa.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } else {
            caixa.statusMenus(caixa.getModo());
            caixa.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            lblMensagem.setText("Aguarde o processamento...");
        }

        Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        aguarde.pack();
        aguarde.setBounds((d.width - aguarde.getWidth()) / 2, (d.height - aguarde.getHeight()) / 2, aguarde.getWidth(), aguarde.getHeight());
        super.setVisible(b);
    }

    // GETs e SETs
    public JLabel getLblMensagem() {
        return lblMensagem;
    }

    public void setLblMensagem(JLabel lblMensagem) {
        this.lblMensagem = lblMensagem;
    }

    public JProgressBar getPgbBarra() {
        return pgbBarra;
    }

    public void setPgbBarra(JProgressBar pgbBarra) {
        this.pgbBarra = pgbBarra;
    }

    public JSeparator getSeparador() {
        return separador;
    }

    public void setSeparador(JSeparator separador) {
        this.separador = separador;
    }
}
