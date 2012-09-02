package br.com.openpdv.visao.core;

import javax.swing.*;

/**
 * Classe que representa as informacoes do sistema.
 *
 * @author Pedro H. Lira
 */
public class Sobre extends JDialog {

    private static Sobre sobre;

    /**
     * Construtor padrao.
     */
    private Sobre() {
        super(Caixa.getInstancia());
        initComponents();
    }

    /**
     * Metodo que retorna a instancia unica de Sobre.
     *
     * @return o objeto de Sobre.
     */
    public static Sobre getInstancia() {
        if (sobre == null) {
            sobre = new Sobre();
        }
        return sobre;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabSobre = new javax.swing.JTabbedPane();
        panSistema = new javax.swing.JPanel();
        lblSistema = new javax.swing.JLabel();
        lblEmpresa = new javax.swing.JLabel();
        lblAutor = new javax.swing.JLabel();
        lblSistemaNome = new javax.swing.JLabel();
        lblEmpresaNome = new javax.swing.JLabel();
        lblAutorNome = new javax.swing.JLabel();
        separador = new javax.swing.JSeparator();
        lblSite = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblTelefone = new javax.swing.JLabel();
        lblSkype = new javax.swing.JLabel();
        lblSiteNome = new javax.swing.JLabel();
        lblEmailNome = new javax.swing.JLabel();
        lblTelefoneNome = new javax.swing.JLabel();
        lblSkypeNome = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        panLicenca = new javax.swing.JPanel();
        spLicenca = new javax.swing.JScrollPane();
        txtLicenca = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sobre");
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tabSobre.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        panSistema.setFont(new java.awt.Font("Serif", 0, 13)); // NOI18N

        lblSistema.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        lblSistema.setText("Sistema:");

        lblEmpresa.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        lblEmpresa.setText("Empresa:");

        lblAutor.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        lblAutor.setText("Autor:");

        lblSistemaNome.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblSistemaNome.setText("OpenPDV - Ponto De Venda Open Source");

        lblEmpresaNome.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblEmpresaNome.setText("PhD - Systems Solutions");

        lblAutorNome.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblAutorNome.setText("Pedro Henrique de Lira");

        lblSite.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        lblSite.setText("Site:");

        lblEmail.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        lblEmail.setText("Email:");

        lblTelefone.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        lblTelefone.setText("Telefone:");

        lblSkype.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        lblSkype.setText("Skype:");

        lblSiteNome.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblSiteNome.setText("http://phdss.com.br");

        lblEmailNome.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblEmailNome.setText("openpdv@phdss.com.br");

        lblTelefoneNome.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblTelefoneNome.setText("+55 (82) 3313-6532");

        lblSkypeNome.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblSkypeNome.setText("pedroh.lira");

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/openpdv_logo.png"))); // NOI18N
        lblLogo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblLogo.setIconTextGap(0);

        org.jdesktop.layout.GroupLayout panSistemaLayout = new org.jdesktop.layout.GroupLayout(panSistema);
        panSistema.setLayout(panSistemaLayout);
        panSistemaLayout.setHorizontalGroup(
            panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panSistemaLayout.createSequentialGroup()
                .addContainerGap()
                .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panSistemaLayout.createSequentialGroup()
                        .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblEmpresa)
                            .add(lblSistema))
                        .add(18, 18, 18)
                        .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(panSistemaLayout.createSequentialGroup()
                                .add(lblSistemaNome)
                                .add(0, 0, Short.MAX_VALUE))
                            .add(panSistemaLayout.createSequentialGroup()
                                .add(lblEmpresaNome)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(lblAutor)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblAutorNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 168, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                    .add(panSistemaLayout.createSequentialGroup()
                        .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(panSistemaLayout.createSequentialGroup()
                                .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblSite)
                                    .add(lblEmail))
                                .add(18, 18, 18)
                                .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(lblSiteNome, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(lblEmailNome, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblTelefone)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, panSistemaLayout.createSequentialGroup()
                                        .add(lblSkype)
                                        .add(18, 18, 18)
                                        .add(lblSkypeNome, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .add(panSistemaLayout.createSequentialGroup()
                                .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 443, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(lblTelefoneNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 165, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(0, 5, Short.MAX_VALUE))
                            .add(lblLogo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(11, 11, 11)))
                .addContainerGap())
        );
        panSistemaLayout.setVerticalGroup(
            panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panSistemaLayout.createSequentialGroup()
                .addContainerGap()
                .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblSistema)
                    .add(lblSistemaNome))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblEmpresa)
                    .add(lblEmpresaNome)
                    .add(lblAutor)
                    .add(lblAutorNome))
                .add(8, 8, 8)
                .add(separador, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panSistemaLayout.createSequentialGroup()
                        .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(lblSite)
                            .add(lblSiteNome))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(lblEmail)
                            .add(lblEmailNome)))
                    .add(panSistemaLayout.createSequentialGroup()
                        .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(lblTelefone)
                            .add(lblTelefoneNome))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panSistemaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(lblSkype)
                            .add(lblSkypeNome))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(lblLogo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabSobre.addTab("Sistema", new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/principal.png")), panSistema); // NOI18N

        panLicenca.setFont(new java.awt.Font("Serif", 0, 13)); // NOI18N

        spLicenca.setBorder(null);
        spLicenca.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spLicenca.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        txtLicenca.setBorder(null);
        txtLicenca.setEditable(false);
        txtLicenca.setFont(new java.awt.Font("Serif", 0, 13)); // NOI18N
        txtLicenca.setText("Copyright 2012 PhD - Systems Solutions\n\n     Licenciado sob a Licença Apache, Versão 2.0 (a \"Licença\");\n     você não pode usar esse arquivo exceto em conformidade com a Licença.\n     Você pode obter uma cópia da Licença em \n\n     http://www.apache.org/licenses/LICENSE-2.0 \n\n     Menos que exigido por lei aplicável ou acordado por escrito, o software \n     distribuído sob a Licença é distribuído \"COMO ESTÁ\", SEM GARANTIAS OU \n     CONDIÇÕES DE QUALQUER TIPO, sejam expressas ou implícitas. \n     Consulte a Licença para o idioma específico que governam as permissões e \n     limitações sob a Licença.\n");
        spLicenca.setViewportView(txtLicenca);

        org.jdesktop.layout.GroupLayout panLicencaLayout = new org.jdesktop.layout.GroupLayout(panLicenca);
        panLicenca.setLayout(panLicencaLayout);
        panLicencaLayout.setHorizontalGroup(
            panLicencaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panLicencaLayout.createSequentialGroup()
                .addContainerGap()
                .add(spLicenca, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panLicencaLayout.setVerticalGroup(
            panLicencaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panLicencaLayout.createSequentialGroup()
                .add(spLicenca, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabSobre.addTab("Licença", new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/padrao.png")), panLicenca); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(tabSobre, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(12, 12, 12)
                .add(tabSobre, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-504)/2, (screenSize.height-353)/2, 504, 353);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Caixa.getInstancia().setJanela(null);
    }//GEN-LAST:event_formWindowClosing
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblAutor;
    private javax.swing.JLabel lblAutorNome;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEmailNome;
    private javax.swing.JLabel lblEmpresa;
    private javax.swing.JLabel lblEmpresaNome;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblSistema;
    private javax.swing.JLabel lblSistemaNome;
    private javax.swing.JLabel lblSite;
    private javax.swing.JLabel lblSiteNome;
    private javax.swing.JLabel lblSkype;
    private javax.swing.JLabel lblSkypeNome;
    private javax.swing.JLabel lblTelefone;
    private javax.swing.JLabel lblTelefoneNome;
    private javax.swing.JPanel panLicenca;
    private javax.swing.JPanel panSistema;
    private javax.swing.JSeparator separador;
    private javax.swing.JScrollPane spLicenca;
    private javax.swing.JTabbedPane tabSobre;
    private javax.swing.JTextPane txtLicenca;
    // End of variables declaration//GEN-END:variables

    //GETs e SETs
    public JLabel getLblAutor() {
        return lblAutor;
    }

    public void setLblAutor(JLabel lblAutor) {
        this.lblAutor = lblAutor;
    }

    public JLabel getLblAutorNome() {
        return lblAutorNome;
    }

    public void setLblAutorNome(JLabel lblAutorNome) {
        this.lblAutorNome = lblAutorNome;
    }

    public JLabel getLblEmail() {
        return lblEmail;
    }

    public void setLblEmail(JLabel lblEmail) {
        this.lblEmail = lblEmail;
    }

    public JLabel getLblEmailNome() {
        return lblEmailNome;
    }

    public void setLblEmailNome(JLabel lblEmailNome) {
        this.lblEmailNome = lblEmailNome;
    }

    public JLabel getLblEmpresa() {
        return lblEmpresa;
    }

    public void setLblEmpresa(JLabel lblEmpresa) {
        this.lblEmpresa = lblEmpresa;
    }

    public JLabel getLblEmpresaNome() {
        return lblEmpresaNome;
    }

    public void setLblEmpresaNome(JLabel lblEmpresaNome) {
        this.lblEmpresaNome = lblEmpresaNome;
    }

    public JLabel getLblSistema() {
        return lblSistema;
    }

    public void setLblSistema(JLabel lblSistema) {
        this.lblSistema = lblSistema;
    }

    public JLabel getLblSistemaNome() {
        return lblSistemaNome;
    }

    public void setLblSistemaNome(JLabel lblSistemaNome) {
        this.lblSistemaNome = lblSistemaNome;
    }

    public JLabel getLblSite() {
        return lblSite;
    }

    public void setLblSite(JLabel lblSite) {
        this.lblSite = lblSite;
    }

    public JLabel getLblSiteNome() {
        return lblSiteNome;
    }

    public void setLblSiteNome(JLabel lblSiteNome) {
        this.lblSiteNome = lblSiteNome;
    }

    public JLabel getLblSkype() {
        return lblSkype;
    }

    public void setLblSkype(JLabel lblSkype) {
        this.lblSkype = lblSkype;
    }

    public JLabel getLblSkypeNome() {
        return lblSkypeNome;
    }

    public void setLblSkypeNome(JLabel lblSkypeNome) {
        this.lblSkypeNome = lblSkypeNome;
    }

    public JLabel getLblTelefone() {
        return lblTelefone;
    }

    public void setLblTelefone(JLabel lblTelefone) {
        this.lblTelefone = lblTelefone;
    }

    public JLabel getLblTelefoneNome() {
        return lblTelefoneNome;
    }

    public void setLblTelefoneNome(JLabel lblTelefoneNome) {
        this.lblTelefoneNome = lblTelefoneNome;
    }

    public JPanel getPanLicenca() {
        return panLicenca;
    }

    public void setPanLicenca(JPanel panLicenca) {
        this.panLicenca = panLicenca;
    }

    public JPanel getPanSistema() {
        return panSistema;
    }

    public void setPanSistema(JPanel panSistema) {
        this.panSistema = panSistema;
    }

    public JScrollPane getSpLicenca() {
        return spLicenca;
    }

    public void setSpLicenca(JScrollPane spLicenca) {
        this.spLicenca = spLicenca;
    }

    public JTabbedPane getTabSobre() {
        return tabSobre;
    }

    public void setTabSobre(JTabbedPane tabSobre) {
        this.tabSobre = tabSobre;
    }

    public JTextPane getTxtLicenca() {
        return txtLicenca;
    }

    public void setTxtLicenca(JTextPane txtLicenca) {
        this.txtLicenca = txtLicenca;
    }

    public JLabel getLblLogo() {
        return lblLogo;
    }

    public void setLblLogo(JLabel lblLogo) {
        this.lblLogo = lblLogo;
    }

    public JSeparator getSeparador() {
        return separador;
    }

    public void setSeparador(JSeparator separador) {
        this.separador = separador;
    }
}
