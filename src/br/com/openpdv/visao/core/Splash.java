package br.com.openpdv.visao.core;

import br.com.openpdv.controlador.comandos.ComandoCancelarVenda;
import br.com.openpdv.controlador.comandos.ComandoEmitirReducaoZ;
import br.com.openpdv.controlador.comandos.ComandoReceberDados;
import br.com.openpdv.controlador.core.Conexao;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.modelo.core.EModo;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.EcfImpressora;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import br.com.phdss.TEF;
import br.com.phdss.controlador.PAF;
import br.com.phdss.modelo.anexo.x.*;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.simple.container.SimpleServerFactory;
import java.awt.Toolkit;
import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

            CoreService service;

            @Override
            public void run() {
                // valida a conexao com o bando de dados
                try {
                    splash.pgBarra.setString("Conectando ao banco de dados...");
                    Conexao.getInstancia();
                    // cria um objeto de manipulacao de dados e faz um backup do BD.
                    service = new CoreService();
                    String back = Util.getConfig().get("openpdv.backup");
                    if (back == null || back.equals("")) {
                        back = "db/backup.zip";
                    }
                    service.executar("BACKUP TO '" + back + "'");
                    splash.pgBarra.setValue(10);
                } catch (Exception ex) {
                    log.error("Nao conseguiu conectar ao banco de dados.", ex);
                    JOptionPane.showMessageDialog(splash, "Problemas com acesso ao banco de dados.\nInforme ao administrador do sistema!", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }

                // ativando o RESTful server, caso esteja configurado como localhost
                if (Util.getConfig().get("sinc.servidor").endsWith("localhost")) {
                    try {
                        splash.pgBarra.setString("Iniciando o serviço RESTful...");
                        URI uri = UriBuilder.fromUri(Util.getConfig().get("sinc.servidor")).port(Integer.valueOf(Util.getConfig().get("sinc.porta"))).build();
                        ResourceConfig rc = new PackagesResourceConfig("br.com.openpdv.rest");
                        rc.getContainerRequestFilters().add(new GZIPContentEncodingFilter());
                        rc.getContainerResponseFilters().add(new GZIPContentEncodingFilter());
                        SimpleServerFactory.create(uri, rc);
                    } catch (Exception ex) {
                        log.error("Nao conseguiu iniciar o servico RESTful.", ex);
                        JOptionPane.showMessageDialog(splash, "Problemas com acesso ao serviço RESTful.\n"
                                + "Informe ao administrador do sistema!", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                    }
                }

                // validando arquivo auxiliar
                boolean login = true;
                try {
                    splash.pgBarra.setString("Lendo arquivo auxiliar...");
                    PAF.descriptografar();
                    splash.pgBarra.setValue(20);
                } catch (Exception ex) {
                    login = false;
                    log.error("Problemas ao ler o auxiliar.txt", ex);
                    JOptionPane.showMessageDialog(splash, "Problemas com a leitura do arquivo auxiliar.\n"
                            + "Informe ao administrador do sistema!", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                }

                // recupera a empresa
                SisEmpresa empresa = null;
                try {
                    FiltroBinario fb = new FiltroBinario("sisEmpresaContador", ECompara.IGUAL, false);
                    empresa = (SisEmpresa) service.selecionar(new SisEmpresa(), fb);

                    // caso nao encontre a empresa, buscar no server
                    if (empresa == null && !Util.getConfig().get("sinc.servidor").endsWith("localhost")) {
                        splash.pgBarra.setString("Recuperando empresa do servidor...");
                        WebResource wr = Util.getRest(Util.getConfig().get("sinc.host") + "/empresa");
                        empresa = wr.accept(MediaType.APPLICATION_JSON_TYPE).get(SisEmpresa.class);

                        // salva no banco local
                        if (empresa != null) {
                            service.salvar(empresa);
                        }

                        // aproveita e busca o contador no server tambem
                        WebResource wr2 = Util.getRest(Util.getConfig().get("sinc.host") + "/contador");
                        SisEmpresa contador = wr2.accept(MediaType.APPLICATION_JSON_TYPE).get(SisEmpresa.class);
                        if (contador != null) {
                            service.salvar(contador);
                        }
                    }

                    if (empresa == null) {
                        JOptionPane.showMessageDialog(splash, "Nenhuma empresa cadastrada no banco local e nem no servidor.", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                    }
                } catch (Exception ex) {
                    log.error("Nao conseguiu carregar os dados da empresa.", ex);
                    JOptionPane.showMessageDialog(splash, "OpenPDV já está ativo.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                }

                // gerando arquivoMD5.txt
                try {
                    splash.pgBarra.setString("Gerando arquivoMD5.txt");
                    gerarArquivos(empresa);
                    splash.pgBarra.setValue(30);
                } catch (Exception ex) {
                    log.error("Problemas ao gerar o arquivoMD5.txt", ex);
                    JOptionPane.showMessageDialog(splash, "Problemas ao gerar o arquivoMD5.txt", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
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

                // validacao do TEF
                TEF.setTEF(Util.getConfig());
                splash.pgBarra.setString("Validando o TEF...");
                splash.pgBarra.setValue(60);
                if (Util.getConfig().get("tef.titulo") != null) {
                    while (!TEF.gpAtivo()) {
                        JOptionPane.showMessageDialog(splash, "Gerenciador Padrão não está ativo!\nPor favor ative-o para continuar.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                    }
                    // cancelando pendentes do TEF.
                    try {
                        splash.pgBarra.setString("Cancelando os TEF pendentes...");
                        File resp = new File(TEF.getRespIntPos001());
                        if (resp.exists() || TEF.getPathTmp().listFiles(TEF.getFiltro()).length > 0) {
                            new ComandoCancelarVenda(true).executar();
                        }
                    } catch (Exception ex) {
                        log.error("Problemas ao cancelar pendentes do TEF", ex);
                        JOptionPane.showMessageDialog(splash, "Problemas ao cancelar pendentes do TEF.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                        System.exit(0);
                    }
                }

                // recupera a impressora
                EcfImpressora impressora = null;
                try {
                    splash.pgBarra.setString("Recuperando impressora ativa...");
                    splash.pgBarra.setValue(70);
                    GrupoFiltro gf = new GrupoFiltro();
                    if (ecfAtivo) {
                        String[] resp = ECF.enviar(EComandoECF.ECF_NumSerie);
                        if (ECF.OK.equals(resp[0])) {
                            FiltroTexto ft = new FiltroTexto("ecfImpressoraSerie", ECompara.IGUAL, resp[1]);
                            gf.add(ft, EJuncao.E);
                        }
                    } else if (!PAF.AUXILIAR.isEmpty()) {
                        FiltroTexto ft = new FiltroTexto("ecfImpressoraSerie", ECompara.IGUAL, PAF.AUXILIAR.getProperty("ecf.serie").split(";")[0]);
                        gf.add(ft, EJuncao.E);
                    }
                    FiltroBinario fb = new FiltroBinario("ecfImpressoraAtivo", ECompara.IGUAL, true);
                    gf.add(fb);
                    impressora = (EcfImpressora) service.selecionar(new EcfImpressora(), gf);

                    // caso nao encontre a impressora, buscar no server
                    if (impressora == null && !Util.getConfig().get("sinc.servidor").endsWith("localhost")) {
                        splash.pgBarra.setString("Recuperando impressora do servidor...");
                        WebResource wr = Util.getRest(Util.getConfig().get("sinc.host") + "/impressora");
                        impressora = wr.accept(MediaType.APPLICATION_JSON_TYPE).get(EcfImpressora.class);

                        // salva a impressora no banco local
                        if (impressora != null) {
                            service.salvar(impressora);
                        }
                    }

                    if (impressora == null) {
                        JOptionPane.showMessageDialog(splash, "Nenhuma impressora cadastrada no banco local e nem no servidor.", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                    }
                } catch (Exception ex) {
                    log.error("Problemas na impressora no banco.", ex);
                    JOptionPane.showMessageDialog(splash, "A impressora informada não corresponde a nenhuma cadastrada.", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }

                // se login verdadeiro tem acesso ao ECF
                if (ecfAtivo) {
                    // valida o serial do ECF
                    try {
                        splash.pgBarra.setString("Validando Nº Série do ECF...");
                        splash.pgBarra.setValue(80);
                        ECF.validarSerial(PAF.AUXILIAR.getProperty("ecf.serie").split(";")[0]);
                    } catch (Exception ex) {
                        String msg = PAF.AUXILIAR.size() == 0 ? "Número de Série do arquivo auxiliar não reconhecido!" : ex.getMessage();
                        login = false;
                        log.error("Problemas ao validar o serial.", ex);
                        JOptionPane.showMessageDialog(splash, msg, "OpenPDV", JOptionPane.WARNING_MESSAGE);
                    }

                    // valida o GT
                    try {
                        splash.pgBarra.setString("Validando GT do ECF...");
                        double gt = Double.valueOf(PAF.AUXILIAR.getProperty("ecf.gt").replace(",", "."));
                        int cro = Integer.valueOf(PAF.AUXILIAR.getProperty("ecf.cro"));
                        double novoGT = ECF.validarGT(gt, cro);
                        if (novoGT > 0.00) {
                            PAF.AUXILIAR.setProperty("ecf.gt", Util.formataNumero(novoGT, 1, 2, false));
                            PAF.criptografar();
                        }
                    } catch (Exception ex) {
                        String msg = PAF.AUXILIAR.size() == 0 ? "Valor do GT do arquivo auxiliar não reconhecido!" : ex.getMessage();
                        login = false;
                        log.error("Problemas ao validar o GT.", ex);
                        JOptionPane.showMessageDialog(splash, msg, "OpenPDV", JOptionPane.WARNING_MESSAGE);
                    }
                }

                // carregando a tela do caixa e login
                splash.pgBarra.setString("Carregando o caixa...");
                splash.pgBarra.setValue(90);
                Caixa caixa = Caixa.getInstancia();
                caixa.setEmpresa(empresa);
                caixa.setImpressora(impressora);

                // recupera a data do ECF ou do sistema
                Date atual = new Date();
                if (ecfAtivo) {
                    String[] resp = ECF.enviar(EComandoECF.ECF_DataHora);
                    if (ECF.OK.equals(resp[0])) {
                        try {
                            atual = new SimpleDateFormat("dd/MM/yy HH:mm:ss").parse(resp[1]);
                        } catch (ParseException ex) {
                        }
                    }
                }
                // recupera a data de validade do auxiliar
                Date validade = null;
                if (!PAF.AUXILIAR.isEmpty()) {
                    validade = Util.formataData(PAF.AUXILIAR.getProperty("out.validade", null), "dd/MM/yyyy");
                }

                // compara as datas, para ver se pode usar o sistema
                boolean autorizado = true;
                if (validade == null || validade.compareTo(atual) < 0) {
                    login = false;
                    autorizado = false;
                    caixa.modoIndisponivel();
                    caixa.getMnuPrincipal().setEnabled(false);
                    caixa.getMnuNota().setEnabled(false);
                    caixa.getMnuPesquisa().setEnabled(false);
                    JOptionPane.showMessageDialog(splash, "ATENCÃO: O OpenPDV está com a data de validade vencida!\n\n"
                            + "Favor entre no menu Sobre - F1 e valide o sistema novamente.\n"
                            + "Caso não consiga re-validar pela internet, entre em contato.\n\n"
                            + "Entrando automaticamente no Modo Indisponível / PED.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                }

                // verifica se ecf + login + autorizado ok
                if (ecfAtivo && login && autorizado) {
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

                        caixa.statusMenus(EModo.OFF);
                        caixa.setJanela(Autenticacao.getInstancia());
                    } catch (Exception ex) {
                        login = false;
                        log.error("Problemas ao validar o estado.", ex);
                        caixa.modoConsulta();
                        JOptionPane.showMessageDialog(splash, "Entrando automaticamente no Modo Consulta / PED.", "OpenPDV", JOptionPane.INFORMATION_MESSAGE);
                    }

                    // verifica se precisa sincronizar
                    splash.pgBarra.setValue(100);
                    if (!Util.getConfig().get("sinc.servidor").endsWith("localhost")) {
                        splash.pgBarra.setString("Sincronizando com o servidor...");
                        try {
                            Date recebimento = Util.getDataHora(PAF.AUXILIAR.getProperty("out.recebimento", null)); // ultimo recebimento
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
                } else if (autorizado) {
                    login = false;
                    caixa.modoIndisponivel();
                    JOptionPane.showMessageDialog(splash, "Entrando automaticamente no Modo Indisponível / PED.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                }

                // abre a tela do caixa
                splash.setVisible(false);
                caixa.setVisible(true);
                if (login) {
                    caixa.getJanela().setVisible(true);
                }
                splash.dispose();
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
        n1.setCnpj(PAF.AUXILIAR.getProperty("sh.cnpj"));
        n1.setIe(PAF.AUXILIAR.getProperty("sh.ie"));
        n1.setIm(PAF.AUXILIAR.getProperty("sh.im"));
        n1.setRazao(PAF.AUXILIAR.getProperty("sh.razao"));
        // cria o objeto modelo n2
        N2 n2 = new N2();
        n2.setLaudo(PAF.AUXILIAR.getProperty("out.laudo"));
        n2.setNome(PAF.AUXILIAR.getProperty("paf.nome"));
        n2.setVersao(PAF.AUXILIAR.getProperty("paf.versao"));
        // binario principal
        N3 n3 = new N3();
        n3.setNome("OpenPDV.jar");
        StringBuilder principal = new StringBuilder(System.getProperty("user.dir"));
        principal.append(System.getProperty("file.separator")).append("OpenPDV.jar");
        n3.setMd5(PAF.gerarMD5(principal.toString()));
        // cria a lista de n3
        List<N3> listaN3 = new ArrayList<>();
        listaN3.add(n3);
        // cria o objeto modelo n9
        N9 n9 = new N9();
        n9.setCnpj(PAF.AUXILIAR.getProperty("sh.cnpj"));
        n9.setIe(PAF.AUXILIAR.getProperty("sh.ie"));
        n9.setTotal(listaN3.size());
        // cria o modelo do anexo X
        AnexoX anexoX = new AnexoX(n1, n2, listaN3, n9);
        String md5Arquivo = PAF.gerarArquivos(anexoX);
        if (!PAF.AUXILIAR.isEmpty()) {
            PAF.AUXILIAR.setProperty("out.autenticado", md5Arquivo);
            PAF.criptografar();
        }
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
