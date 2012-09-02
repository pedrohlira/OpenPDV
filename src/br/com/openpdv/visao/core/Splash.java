package br.com.openpdv.visao.core;

import br.com.openpdv.controlador.ECF;
import br.com.openpdv.controlador.EComandoECF;
import br.com.openpdv.controlador.PAF;
import br.com.openpdv.controlador.TEF;
import br.com.openpdv.controlador.comandos.ComandoEmitirReducaoZ;
import br.com.openpdv.controlador.comandos.ComandoReceberDados;
import br.com.openpdv.controlador.core.Conexao;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.controlador.permissao.Login;
import br.com.openpdv.modelo.anexo.x.*;
import br.com.openpdv.modelo.core.EModo;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.EcfImpressora;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.simple.container.SimpleServerFactory;
import java.awt.Toolkit;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Classe que representa as informacoes do sistema.
 *
 * @author Pedro H. Lira
 */
public class Splash extends JFrame {

    private static Logger log;
    private static Splash splash;

    /**
     * Construtor padrao.
     */
    private Splash() {
        log = Logger.getLogger(Splash.class);
        initComponents();
    }

    /**
     * Metodo de inicializacao do sistema.
     *
     * @param args nenhum argumento.
     */
    public static void main(String[] args) {
        PropertyConfigurator.configure("conf" + System.getProperty("file.separator") + "log4j.properties");

        // identifica se tem o LaF do windows para usar
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equalsIgnoreCase("Windows")) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            log.error("Problemas ao mudar LaF.", ex);
        }

        // abre uma thread para iniciar a aplicacao
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                splash = new Splash();
                splash.setVisible(true);
                splash.iniciar();
                splash.toFront();
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblPhd = new javax.swing.JLabel();
        separador = new javax.swing.JSeparator();
        lblOpenPDV = new javax.swing.JLabel();
        pgBarra = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("OpenPDV");
        setName("OpenPDV");
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblPhd.setBackground(java.awt.Color.white);
        lblPhd.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblPhd.setForeground(new java.awt.Color(255, 255, 255));
        lblPhd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/phd.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 6);
        getContentPane().add(lblPhd, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 639;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 6);
        getContentPane().add(separador, gridBagConstraints);

        lblOpenPDV.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        lblOpenPDV.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOpenPDV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/openpdv/imagens/openpdv_logo.png"))); // NOI18N
        lblOpenPDV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 0);
        getContentPane().add(lblOpenPDV, gridBagConstraints);

        pgBarra.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        pgBarra.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 364;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 6, 0, 6);
        getContentPane().add(pgBarra, gridBagConstraints);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-652)/2, (screenSize.height-419)/2, 652, 419);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblOpenPDV;
    private javax.swing.JLabel lblPhd;
    private javax.swing.JProgressBar pgBarra;
    private javax.swing.JSeparator separador;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo que inicializa o sistema.
     */
    private void iniciar() {
        // seta o icone
        if (System.getProperty("os.name").contains("Windows")) {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/openpdv/imagens/logo.png")));
        }

        // abre uma thread para carregar as validacoes da aplicacao
        new Thread(new Runnable() {

            @Override
            public void run() {
                // valida a conexao com o bando de dados
                try {
                    splash.pgBarra.setString("Conectando ao banco de dados...");
                    Conexao.getInstancia();
                    splash.pgBarra.setValue(10);
                } catch (Exception ex) {
                    log.error("Nao conseguiu conectar ao banco de dados.", ex);
                    JOptionPane.showMessageDialog(splash, "Problemas com acesso ao banco de dados.\n"
                                                          + "Verifique os dados de acesso ou informe ao administrador!", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                    Login.sair();
                }

                // ativando o RESTful server, caso esteja configurado como localhost
                if (Util.getConfig().get("openpdv.servidor").endsWith("localhost")) {
                    try {
                        splash.pgBarra.setString("Iniciando o serviço RESTful...");
                        URI uri = UriBuilder.fromUri(Util.getConfig().get("openpdv.servidor")).port(Integer.valueOf(Util.getConfig().get("openpdv.porta"))).build();
                        ResourceConfig rc = new PackagesResourceConfig("br.com.openpdv.rest");
                        rc.getContainerRequestFilters().add(new GZIPContentEncodingFilter());
                        rc.getContainerResponseFilters().add(new GZIPContentEncodingFilter());
                        SimpleServerFactory.create(uri, rc);
                    } catch (Exception ex) {
                        log.error("Nao conseguiu iniciar o servico RESTful.", ex);
                        JOptionPane.showMessageDialog(splash, "Problemas com acesso ao serviço RESTful.\n"
                                                              + "Verifique os dados do config ou informe ao administrador!", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                        Login.sair();
                    }
                }

                // validando arquivo auxiliar
                try {
                    splash.pgBarra.setString("Lendo arquivo auxiliar...");
                    PAF.descriptografarAuxiliar();
                    splash.pgBarra.setValue(20);
                } catch (Exception ex) {
                    log.error("Problemas ao ler o auxiliar.txt", ex);
                    JOptionPane.showMessageDialog(splash, "Problemas com a leitura do arquivo auxiliar.\n"
                                                          + "Informe ao administrador do sistema!", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                    Login.sair();
                }

                // recupera a empresa
                SisEmpresa empresa = null;
                try {
                    FiltroTexto ft = new FiltroTexto("sisEmpresaCnpj", ECompara.IGUAL, PAF.AUXILIAR.getProperty("cli.cnpj"));
                    CoreService<SisEmpresa> service = new CoreService<>();
                    empresa = service.selecionar(new SisEmpresa(), ft);

                    // caso nao encontre a empresa, buscar no server
                    if (empresa == null && !Util.getConfig().get("openpdv.servidor").endsWith("localhost")) {
                        splash.pgBarra.setString("Recuperando empresa do servidor...");
                        WebResource wr = Util.getRest(Util.getConfig().get("openpdv.host") + "/empresa/" + PAF.AUXILIAR.getProperty("cli.cnpj"));
                        empresa = wr.accept(MediaType.APPLICATION_JSON_TYPE).get(SisEmpresa.class);

                        // salva no banco local
                        if (empresa != null) {
                            service.salvar(empresa);
                        }
                    }

                    if (empresa == null) {
                        JOptionPane.showMessageDialog(splash, "Nenhuma empresa cadastrada no banco local e nem no servidor.", "OpenPDV", JOptionPane.INFORMATION_MESSAGE);
                        Login.sair();
                    }
                } catch (Exception ex) {
                    log.error("Nao conseguiu carregar os dados da empresa.", ex);
                    JOptionPane.showMessageDialog(splash, "OpenPDV já está ativo.", "OpenPDV", JOptionPane.INFORMATION_MESSAGE);
                    Login.sair();
                }

                // gerando arquivos.txt
                try {
                    splash.pgBarra.setString("Gerando arquivo com executaveis...");
                    gerarArquivos(empresa);
                    splash.pgBarra.setValue(30);
                } catch (Exception ex) {
                    log.error("Problemas ao gerar o arquivos.txt", ex);
                    JOptionPane.showMessageDialog(splash, "Problemas ao gerar o arquivos.txt", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                    Login.sair();
                }

                // valida a comunicao e ativacao com o ECF
                boolean ecfAtivo = false;
                try {
                    splash.pgBarra.setString("Conectando no ECF...");
                    ECF.conectar(Util.getConfig().get("ecf.servidor"), Integer.valueOf(Util.getConfig().get("ecf.porta")));
                    splash.pgBarra.setValue(40);

                    splash.pgBarra.setString("Ativando o ECF...");
                    ECF.ativar();
                    splash.pgBarra.setValue(50);
                    ecfAtivo = true;
                } catch (Exception ex) {
                    log.error("Problemas ao conectar no ECF", ex);
                    JOptionPane.showMessageDialog(splash, "Problemas ao conectar ou ativar o ECF.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                }

                // ativacao do TEF
                boolean tefAtivo = false;
                TEF.setTEF(Util.getConfig());
                try {
                    splash.pgBarra.setString("Ativando o TEF...");
                    tefAtivo = TEF.ativar();
                    splash.pgBarra.setValue(60);
                } catch (Exception ex) {
                    log.error("Problemas ao conectar no TEF", ex);
                    JOptionPane.showMessageDialog(splash, "Problemas ao ativar o TEF.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                }

                // cancelando os pendentes do TEF
                try {
                    if (tefAtivo) {
                        splash.pgBarra.setString("Cancelando os TEF pendentes...");
                        TEF.cancelarPendentes();
                    }
                } catch (Exception ex) {
                    log.error("Problemas ao cancelar pendentes do TEF", ex);
                    JOptionPane.showMessageDialog(splash, "Existe TEF pendentes, mas o sistema encontrou problemas ao executar.", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                    Login.sair();
                }

                // recupera a impressora
                EcfImpressora impressora = null;
                try {
                    splash.pgBarra.setString("Recuperando impressora ativa...");
                    splash.pgBarra.setValue(70);
                    GrupoFiltro gf = new GrupoFiltro();
                    if (ecfAtivo) {
                        String[] resp = ECF.enviar(EComandoECF.ECF_NumSerie);
                        FiltroTexto ft = new FiltroTexto("ecfImpressoraSerie", ECompara.IGUAL, resp[1]);
                        gf.add(ft, EJuncao.E);
                    } else {
                        FiltroNumero fn = new FiltroNumero("ecfImpressoraId", ECompara.IGUAL, 1);
                        gf.add(fn, EJuncao.E);
                    }
                    FiltroBinario fb = new FiltroBinario("ecfImpressoraAtivo", ECompara.IGUAL, true);
                    gf.add(fb);
                    CoreService<EcfImpressora> service = new CoreService<>();
                    impressora = service.selecionar(new EcfImpressora(), gf);

                    // caso nao encontre a impressora, buscar no server
                    if (impressora == null && !Util.getConfig().get("openpdv.servidor").endsWith("localhost")) {
                        splash.pgBarra.setString("Recuperando impressora do servidor...");
                        WebResource wr = Util.getRest(Util.getConfig().get("openpdv.host") + "/impressora/" + PAF.AUXILIAR.getProperty("ecf.serie"));
                        impressora = wr.accept(MediaType.APPLICATION_JSON_TYPE).get(EcfImpressora.class);

                        // salva a impressora no banco local
                        if (impressora != null) {
                            service.salvar(impressora);
                        }
                    }

                    if (impressora == null) {
                        JOptionPane.showMessageDialog(splash, "Nenhuma impressora cadastrada no banco local e nem no servidor.", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                        Login.sair();
                    }
                } catch (Exception ex) {
                    log.error("Problemas na impressora no banco.", ex);
                    JOptionPane.showMessageDialog(splash, "A impressora informada não corresponde a nenhuma cadastrada.", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                    Login.sair();
                }

                // se login verdadeiro tem acesso ao ECF
                boolean login = true;
                if (ecfAtivo) {
                    // valida o serial do ECF
                    try {
                        splash.pgBarra.setString("Validando Nº Série do ECF...");
                        splash.pgBarra.setValue(80);
                        ECF.validarSerial();
                    } catch (Exception ex) {
                        login = false;
                        log.error("Problemas ao validar o serial.", ex);
                        JOptionPane.showMessageDialog(splash, ex.getMessage(), "OpenPDV", JOptionPane.WARNING_MESSAGE);
                    }

                    // valida o GT
                    try {
                        splash.pgBarra.setString("Validando GT do ECF...");
                        ECF.validarGT();
                    } catch (Exception ex) {
                        login = false;
                        log.error("Problemas ao validar o GT.", ex);
                        JOptionPane.showMessageDialog(splash, ex.getMessage(), "OpenPDV", JOptionPane.WARNING_MESSAGE);
                    }
                }

                // carregando a tela do caixa e login
                splash.pgBarra.setString("Carregando o caixa...");
                splash.pgBarra.setValue(90);
                Caixa caixa = Caixa.getInstancia();
                caixa.setEmpresa(empresa);
                caixa.setImpressora(impressora);

                if (ecfAtivo) {
                    // valida o estado do ECF
                    try {
                        switch (ECF.validarEstado()) {
                            case estNaoInicializada:
                            case estDesconhecido:
                                throw new OpenPdvException("Estado do ECF não inicializado ou desconhecido.");
                            case estRelatorio:
                                ECF.enviar(EComandoECF.ECF_CorrigeEstadoErro);
                                break;
                            case estBloqueada:
                                login = false;
                                JOptionPane.showMessageDialog(splash, "ECF bloqueado até as 00:00!", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                                break;
                            case estRequerZ:
                                splash.pgBarra.setString("Emitindo a redução Z...");
                                new ComandoEmitirReducaoZ().executar();
                                break;
                            case estRequerX:
                                splash.pgBarra.setString("Emitindo a leitura X...");
                                PAF.leituraX();
                                break;
                            case estVenda:
                            case estPagamento:
                                Autenticacao.getInstancia().getBtnConsulta().setEnabled(false);
                                Autenticacao.getInstancia().getBtnFiscal().setEnabled(false);
                                JOptionPane.showMessageDialog(splash, "Atenção: Existe uma venda em aberto!\n\nPor favor efetue login para recuperar a venda\nou para cancelar a mesma.", "Venda Aberta", JOptionPane.WARNING_MESSAGE);

                        }
                    } catch (Exception ex) {
                        login = false;
                        log.error("Problemas ao validar o estado.", ex);
                        JOptionPane.showMessageDialog(splash, ex.getMessage(), "OpenPDV", JOptionPane.WARNING_MESSAGE);
                    }

                    if (login) {
                        caixa.statusMenus(EModo.OFF);
                        caixa.setJanela(Autenticacao.getInstancia());
                    } else {
                        caixa.modoConsulta();
                        JOptionPane.showMessageDialog(splash, "Entrando automaticamente no Modo Consulta.", "OpenPDV", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    login = false;
                    caixa.modoIndisponivel();
                    JOptionPane.showMessageDialog(splash, "Entrando automaticamente no Modo Indisponível.", "OpenPDV", JOptionPane.INFORMATION_MESSAGE);
                }

                // verifica se precisa sincronizar
                splash.pgBarra.setValue(100);
                if (!Util.getConfig().get("openpdv.servidor").endsWith("localhost")) {
                    splash.pgBarra.setString("Sincronizando com o servidor...");
                    try {
                        Date recebimento = Util.getDataHora(PAF.AUXILIAR.getProperty("out.recebimento", null)); // ultimo recebimento
                        Date atual = new Date(); // data atual
                        if (recebimento == null || (atual.getTime() - recebimento.getTime()) / 86400000 > 0) { // maior que 1 dia em milisegundos
                            new ComandoReceberDados().executar();
                        }
                    } catch (OpenPdvException ex) {
                        log.error("Nao conseguiu sincronizar com o servidor.", ex);
                        JOptionPane.showMessageDialog(splash, ex.getMessage(), "Sincronismo", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    splash.pgBarra.setString("Finalizado");
                }

                // abre a tela do caixa
                caixa.setVisible(true);
                if (login) {
                    caixa.getJanela().setVisible(true);
                }
                dispose();
            }
        }).start();
    }

    /**
     * Gere o arquivo com os arquivos autenticados.
     *
     * @throws Exception dispara caso nao consiga.
     */
    private void gerarArquivos(SisEmpresa empresa) throws Exception {
        // cria o objeto modelo n1
        N1 n1 = new N1();
        n1.setCnpj(empresa.getSisEmpresaCnpj());
        n1.setIe(empresa.getSisEmpresaIe());
        n1.setIm(empresa.getSisEmpresaIm());
        n1.setRazao(Util.normaliza(empresa.getSisEmpresaRazao()));
        // cria o objeto modelo n2
        N2 n2 = new N2();
        n2.setLaudo(PAF.AUXILIAR.getProperty("out.laudo"));
        n2.setNome(PAF.AUXILIAR.getProperty("paf.nome"));
        n2.setVersao(PAF.AUXILIAR.getProperty("paf.versao"));
        // binario principal
        N3 principal = new N3();
        principal.setNome("OpenPDV.jar");
        principal.setMd5(PAF.AUXILIAR.getProperty("paf.md5"));
        // cria a lista de n3
        List<N3> listaN3 = new ArrayList<>();
        listaN3.add(principal);
        // cria o objeto modelo n9
        N9 n9 = new N9();
        n9.setCnpj(empresa.getSisEmpresaCnpj());
        n9.setIe(empresa.getSisEmpresaIe());
        n9.setTotal(listaN3.size());
        // cria o modelo do anexo X
        AnexoX anexoX = new AnexoX(n1, n2, listaN3, n9);
        String md5Arquivo = PAF.gerarArquivos(anexoX);
        PAF.AUXILIAR.setProperty("out.autenticado", md5Arquivo);
        PAF.criptografarAuxiliar(null);
    }

    public JLabel getLblOpenPDV() {
        return lblOpenPDV;
    }

    public void setLblOpenPDV(JLabel lblOpenPDV) {
        this.lblOpenPDV = lblOpenPDV;
    }

    public JLabel getLblPhd() {
        return lblPhd;
    }

    public void setLblPhd(JLabel lblPhd) {
        this.lblPhd = lblPhd;
    }

    public JProgressBar getPgBarra() {
        return pgBarra;
    }

    public void setPgBarra(JProgressBar pgBarra) {
        this.pgBarra = pgBarra;
    }

    public JSeparator getSeparador() {
        return separador;
    }

    public void setSeparador(JSeparator separador) {
        this.separador = separador;
    }
}
