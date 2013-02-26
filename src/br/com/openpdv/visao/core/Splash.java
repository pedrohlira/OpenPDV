package br.com.openpdv.visao.core;

import br.com.openpdv.controlador.comandos.ComandoCancelarCartao;
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
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import br.com.phdss.TEF;
import br.com.phdss.controlador.PAF;
import br.com.phdss.modelo.anexo.x.*;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.simple.container.SimpleServerFactory;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
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
                    service = new CoreService();

                    // realiza o backup do banco se preciso for
                    String back = Util.getConfig().get("openpdv.backup");
                    if (back == null || back.equals("")) {
                        back = "db/";
                    }

                    String periodo = Util.getConfig().get("openpdv.backup.periodo");
                    if (back == null || back.equals("")) {
                        periodo = "mes";
                    }

                    SimpleDateFormat sdf;
                    switch (periodo) {
                        case "dia":
                            sdf = new SimpleDateFormat("DDyyyy");
                            break;
                        case "semana":
                            sdf = new SimpleDateFormat("wwyyyy");
                            break;
                        default:
                            sdf = new SimpleDateFormat("MMyyyy");
                            break;
                    }
                    back += String.format("backup_%s.zip", sdf.format(new Date()));
                    File arquivo = new File(back);
                    if (!arquivo.exists()) {
                        service.executar("BACKUP TO '" + back + "'");
                    }
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
                    }
                }

                // verifica se existes atualizacoes da base de dados
                FilenameFilter filtro = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".sql");
                    }
                };
                File pathDB = new File("db");
                for (File arquivo : pathDB.listFiles(filtro)) {
                    splash.pgBarra.setString("Atualizando base de dados...");
                    splash.pgBarra.setValue(20);

                    try {
                        // retorna os dados do arquivo
                        String dados = "";
                        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                            while (br.ready()) {
                                dados = br.readLine();
                                break;
                            }
                        }
                        dados = PAF.descriptar(dados);

                        // executa as instrucoes
                        for (String sql : dados.split("\n")) {
                            if (sql != null && !sql.equals("") && !sql.startsWith("/") && !sql.startsWith("#")) {
                                try {
                                    Integer resp = service.executar(sql);
                                    log.info("Sql executdo: " + sql);
                                    log.info("Registros atingidos: " + resp);
                                } catch (Exception ex) {
                                    log.error("Sql executdo: " + sql);
                                    log.error("Erro retornado: ", ex);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        log.error("Erro ao tentar utilizar o arquivo: " + arquivo.getName(), ex);
                    } finally {
                        arquivo.delete();
                    }
                }

                // validando arquivo auxiliar
                boolean login = true;
                try {
                    splash.pgBarra.setString("Lendo arquivo auxiliar...");
                    PAF.descriptografar();
                    splash.pgBarra.setValue(30);
                } catch (Exception ex) {
                    // caso tenha algum problema tenta recuperar usando o backup
                    try {
                        PAF.descriptografar("conf" + System.getProperty("file.separator") + "auxiliar.bak", PAF.AUXILIAR);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        PAF.AUXILIAR.setProperty("out.recebimento", Util.getData(cal.getTime()));
                        cal.add(Calendar.DAY_OF_MONTH, -1);
                        PAF.AUXILIAR.setProperty("out.envio", Util.getData(cal.getTime()));
                        PAF.AUXILIAR.setProperty("out.validade", Util.getData(cal.getTime()));
                        JOptionPane.showMessageDialog(splash, "Backup do arquivo auxiliar recuperado, necessita validar o sistema!", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                    } catch (Exception ex1) {
                        login = false;
                        log.error("Problemas ao ler o auxiliar.txt", ex);
                        JOptionPane.showMessageDialog(splash, "Problemas com a leitura do arquivo auxiliar.\n"
                                + "Informe ao administrador do sistema!", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                    }
                }

                // recupera a empresa
                SisEmpresa empresa = null;
                try {
                    FiltroBinario fb = new FiltroBinario("sisEmpresaContador", ECompara.IGUAL, false);
                    empresa = (SisEmpresa) service.selecionar(new SisEmpresa(), fb);

                    if (empresa == null) {
                        JOptionPane.showMessageDialog(splash, "Nenhuma empresa cadastrada no banco local.", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                    }
                } catch (Exception ex) {
                    log.error("Nao conseguiu carregar os dados da empresa.", ex);
                    JOptionPane.showMessageDialog(splash, "Nao conseguiu carregar os dados da empresa.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                }

                // gerando arquivoMD5.txt
                try {
                    splash.pgBarra.setString("Gerando arquivoMD5.txt");
                    PAF.gerarArquivos();
                    splash.pgBarra.setValue(40);
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
                            // tenta cancelar a venda, caso ela já esteja cancelada, cancela somente o TEF
                            try {
                                new ComandoCancelarVenda(true).executar();
                            } catch (Exception ex) {
                                log.error("Venda ja estava cancelada.", ex);
                                EcfVenda venda = null;
                                List<EcfVenda> vendas = service.selecionar(new EcfVenda(), 0, 1, null);
                                if (!vendas.isEmpty()) {
                                    venda = vendas.get(0);
                                }
                                new ComandoCancelarCartao(venda.getEcfPagamentos(), true).executar();
                                Caixa.getInstancia().modoDisponivel();
                            }
                        }
                    } catch (Exception ex) {
                        log.error("Problemas ao cancelar pendentes do TEF", ex);
                        JOptionPane.showMessageDialog(splash, "Problemas ao cancelar pendentes do TEF.\nRemova os arquivos pendentes e estorne pelo ADM.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
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

                    if (impressora == null) {
                        JOptionPane.showMessageDialog(splash, "Nenhuma impressora cadastrada e ativa no banco local.", "OpenPDV", JOptionPane.ERROR_MESSAGE);
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
                        splash.pgBarra.setValue(80);
                        double gt = Double.valueOf(PAF.AUXILIAR.getProperty("ecf.gt").replace(",", "."));
                        double novoGT = ECF.validarGT(gt);
                        if (novoGT > 0.00) {
                            PAF.AUXILIAR.setProperty("ecf.gt", Util.formataNumero(novoGT, 1, 2, false));
                            PAF.criptografar();
                            JOptionPane.showMessageDialog(splash, "Valor do GT recomposto no arquivo auxiliar.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
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
                    validade = Util.formataData(PAF.AUXILIAR.getProperty("out.validade", null) + " 23:59:59", "dd/MM/yyyy HH:mm:ss");
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
                        caixa.statusMenus(EModo.OFF);
                        caixa.setJanela(Autenticacao.getInstancia());

                        switch (ECF.validarEstado()) {
                            case estNaoInicializada:
                            case estDesconhecido:
                                throw new OpenPdvException("Estado do ECF não inicializado ou desconhecido.");
                            case estRelatorio:
                                ECF.enviar(EComandoECF.ECF_CorrigeEstadoErro);
                                break;
                            case estBloqueada:
                                login = false;
                                caixa.modoConsulta();
                                JOptionPane.showMessageDialog(splash, "Entrando automaticamente no Modo Consulta.", "Redução Z emitida", JOptionPane.INFORMATION_MESSAGE);
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
                        caixa.modoConsulta();
                        JOptionPane.showMessageDialog(splash, "Entrando automaticamente no Modo Consulta.", "OpenPDV", JOptionPane.INFORMATION_MESSAGE);
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
                            JOptionPane.showMessageDialog(splash, "Nao conseguiu sincronizar com o servidor.", "Sincronismo", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        splash.pgBarra.setString("Finalizado");
                    }
                } else if (autorizado) {
                    login = false;
                    caixa.modoIndisponivel();
                    JOptionPane.showMessageDialog(splash, "Entrando automaticamente no Modo Indisponível / PED.", "OpenPDV", JOptionPane.INFORMATION_MESSAGE);
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
