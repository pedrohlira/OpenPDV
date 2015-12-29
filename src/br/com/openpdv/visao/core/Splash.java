package br.com.openpdv.visao.core;

import br.com.openpdv.controlador.comandos.ComandoEmitirReducaoZ;
import br.com.openpdv.controlador.comandos.ComandoReceberDados;
import br.com.openpdv.controlador.comandos.ComandoValidarSistema;
import br.com.openpdv.controlador.core.Conexao;
import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.modelo.core.EDirecao;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.EModo;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.filtro.*;
import br.com.openpdv.modelo.ecf.EcfImpressora;
import br.com.openpdv.modelo.ecf.EcfZ;
import br.com.openpdv.modelo.sistema.SisEmpresa;
import br.com.phdss.ECF;
import br.com.phdss.EComando;
import br.com.phdss.IECF;
import br.com.phdss.TEF;
import br.com.phdss.controlador.PAF;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.*;
import javax.ws.rs.core.UriBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Classe que representa as informacoes do sistema.
 *
 * @author Pedro H. Lira
 */
public class Splash extends JFrame {

    private static Splash splash;
    private Logger log;
    private IECF ecf;

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
        } catch (Exception ex) {
            // Nao mudou o visual.
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
        setName("OpenPDV"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
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

        setSize(new java.awt.Dimension(652, 419));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (ecf != null) {
            ecf.desativar();
        }
    }//GEN-LAST:event_formWindowClosing

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
                    splash.pgBarra.setValue(10);
                } catch (Exception ex) {
                    log.error("Nao conseguiu conectar ao banco de dados.", ex);
                    JOptionPane.showMessageDialog(splash, "Problemas com acesso ao banco de dados.\nInforme ao administrador do sistema!", "OpenPDV", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }

                // realiza a limpeza do banco de dados deixando sempre o ultimo e atual mes
                try {
                    if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 1) {
                        File origem = new File("C:\\OpenPDV\\db\\limpeza.txt");
                        File destino = new File("C:\\OpenPDV\\db\\limpeza.sql");
                        FileUtils.copyFile(origem, destino);
                    }
                } catch (Exception ex) {
                    log.error("Nao conseguiu fazer a limpeza mensal do banco de dados.", ex);
                }

                // ativando o RESTful server, caso esteja configurado como localhost e rest
                if (Util.getConfig().getProperty("sinc.tipo").equals("rest") && Util.getConfig().getProperty("sinc.servidor").endsWith("localhost")) {
                    try {
                        splash.pgBarra.setString("Iniciando o serviço RESTful...");
                        URI uri = UriBuilder.fromUri(Util.getConfig().getProperty("sinc.servidor")).port(Integer.valueOf(Util.getConfig().getProperty("sinc.porta"))).build();
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
                        dados = Util.descriptar(dados);

                        // executa as instrucoes
                        for (String sql : dados.split("\n")) {
                            if (sql != null && !sql.equals("") && !sql.startsWith("/") && !sql.startsWith("#")) {
                                try {
                                    Integer resp = service.executar(sql);
                                    log.info("Sql executado: " + sql);
                                    log.info("Registros atingidos: " + resp);
                                } catch (Exception ex) {
                                    log.error("Sql executado: " + sql);
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
                    Util.descriptografar(null, PAF.AUXILIAR);
                    splash.pgBarra.setValue(30);
                } catch (Exception ex) {
                    // caso tenha algum problema tenta recuperar usando o backup
                    try {
                        Util.descriptografar("conf" + System.getProperty("file.separator") + "auxiliar.bak", PAF.AUXILIAR);
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
                    ECF.setInstancia(Util.getConfig().getProperty("ecf.marca"));
                    ecf = ECF.getInstancia();

                    splash.pgBarra.setString("Conectando no ECF...");
                    ecf.conectar(Util.getConfig().getProperty("ecf.porta"), Integer.valueOf(Util.getConfig().getProperty("ecf.velocidade")), Integer.valueOf(Util.getConfig().getProperty("ecf.modelo")));

                    splash.pgBarra.setString("Ativando o ECF...");
                    ecf.ativar();
                    splash.pgBarra.setValue(50);
                    ecfAtivo = true;
                } catch (Exception ex) {
                    log.error("Problemas ao conectar no ECF", ex);
                    JOptionPane.showMessageDialog(splash, "Problemas ao conectar ou ativar o ECF.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                }

                // validacao do TEF
                if (Boolean.valueOf(Util.getConfig().getProperty("pag.tef"))) {
                    Properties config = Util.getConfig();
                    config.putAll(PAF.AUXILIAR);
                    TEF.setTEF(config);
                    splash.pgBarra.setString("Validando o TEF...");
                    splash.pgBarra.setValue(60);
                    while (!TEF.gpAtivo()) {
                        JOptionPane.showMessageDialog(splash, "Gerenciador Padrão não está ativo!\nPor favor ative-o para continuar.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                    }

                    if (Boolean.valueOf(Util.getConfig().getProperty("pag.tef"))) {
                        try {
                            String arquivo = TEF.lerArquivo(TEF.getRespIntPos001(), 0);
                            boolean pendente = false;
                            if (arquivo != null) {
                                Map<String, String> mapa = TEF.iniToMap(arquivo);
                                pendente = !(mapa.get("000-000").equals("ATV") || mapa.get("000-000").equals("ADM"));
                            }
                            if (pendente || TEF.getPathTmp().listFiles(TEF.getFiltro()).length > 0) {
                                int opt = JOptionPane.showConfirmDialog(splash, "O sistema identificou problemas no último TEF!\nO sistema irá cancelar a última operação!\nDeseja realizar este procedimento?", "OpenPDV", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                if (opt == JOptionPane.YES_OPTION) {
                                    // cancela somente o TEF
                                    splash.pgBarra.setString("Cancelando os TEF pendentes...");
                                    TEF.cancelarPendentes(true);
                                }
                            }
                        } catch (Exception ex) {
                            log.error("Problemas ao cancelar pendentes do TEF", ex);
                            JOptionPane.showMessageDialog(splash, "Problemas ao cancelar pendentes do TEF.\nCaso precise, estorne os cartões pelo ADM.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                        } finally {
                            TEF.deletarArquivo(TEF.getRespIntPos001());
                            for (File arquivo : TEF.getPathTmp().listFiles(TEF.getFiltro())) {
                                arquivo.delete();
                            }
                        }
                    }
                }

                // recupera a impressora
                EcfImpressora impressora = null;
                try {
                    splash.pgBarra.setString("Recuperando impressora ativa...");
                    splash.pgBarra.setValue(70);
                    FiltroGrupo gf = new FiltroGrupo();
                    if (ecfAtivo) {
                        String[] resp = ecf.enviar(EComando.ECF_NumSerie);
                        if (IECF.OK.equals(resp[0])) {
                            FiltroTexto ft = new FiltroTexto("ecfImpressoraSerie", ECompara.IGUAL, resp[1]);
                            gf.add(ft, Filtro.E);
                        }
                    } else if (!PAF.AUXILIAR.isEmpty()) {
                        FiltroTexto ft = new FiltroTexto("ecfImpressoraSerie", ECompara.IGUAL, PAF.AUXILIAR.getProperty("ecf.serie").split(";")[0]);
                        gf.add(ft, Filtro.E);
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
                        ecf.validarSerial(PAF.AUXILIAR.getProperty("ecf.serie").split(";")[0]);
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
                        double novoGT = ecf.validarGT(gt);
                        if (novoGT > 0.00) {
                            EcfZ ultZ = new EcfZ();
                            ultZ.setOrdemDirecao(EDirecao.DESC);
                            FiltroObjeto fo = new FiltroObjeto("ecfImpressora", ECompara.IGUAL, impressora);
                            List<EcfZ> zs = service.selecionar(ultZ, 0, 1, fo);
                            if (zs != null && !zs.isEmpty()) {
                                ultZ = zs.get(0);
                                if (ecf.validarGT(ultZ.getEcfZCrz(), ultZ.getEcfZCro(), ultZ.getEcfZBruto())) {
                                    PAF.AUXILIAR.setProperty("ecf.gt", Util.formataNumero(novoGT, 1, 2, false));
                                    Util.criptografar(null, PAF.AUXILIAR);
                                    JOptionPane.showMessageDialog(splash, "Valor do GT recomposto no arquivo auxiliar.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                                } else {
                                    throw new Exception("Os dados de CRZ, CRO e Valor Bruto não estão iguais!");
                                }
                            } else {
                                throw new Exception("Não existe Z no banco.");
                            }
                        }
                    } catch (Exception ex) {
                        login = false;
                        log.error("Problemas ao validar o GT.", ex);
                        JOptionPane.showMessageDialog(splash, "Não foi possível recupara o GT! -> " + ex.getMessage(), "OpenPDV", JOptionPane.WARNING_MESSAGE);
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
                    String[] resp = ecf.enviar(EComando.ECF_DataHora);
                    if (IECF.OK.equals(resp[0])) {
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
                    caixa.modoIndisponivel();
                    caixa.getMnuPrincipal().setEnabled(false);
                    caixa.getMnuNota().setEnabled(false);
                    caixa.getMnuPesquisa().setEnabled(false);
                    JOptionPane.showMessageDialog(splash, "ATENCÃO: O OpenPDV está com a data de validade vencida!\n\n"
                            + "O sistema irá fazer uma tentativa de validar on-line.\n\n"
                            + "Caso não seja possível, tente manualmente pelo menu Ajuda - F1.", "OpenPDV", JOptionPane.WARNING_MESSAGE);
                    try {
                        new ComandoValidarSistema().executar();
                    } catch (OpenPdvException ex) {
                        log.error("Erro ao tentar validar.", ex);
                    }
                    // verifica se ecf + login + autorizado ok
                } else if (ecfAtivo && login && autorizado) {
                    // valida o estado do ECF
                    try {
                        caixa.statusMenus(EModo.OFF);
                        caixa.setJanela(Autenticacao.getInstancia());

                        switch (ecf.validarEstado()) {
                            case estLivre:
                                // Verifica se a ultima Z salva no banco e a mesma emitida pelo ECF
                                EcfZ ultZ = new EcfZ();
                                ultZ.setOrdemDirecao(EDirecao.DESC);
                                FiltroObjeto fo = new FiltroObjeto("ecfImpressora", ECompara.IGUAL, Caixa.getInstancia().getImpressora());
                                List<EcfZ> zs = service.selecionar(ultZ, 0, 1, fo);
                                if (!zs.isEmpty()) {
                                    // caso seja diferente pega os dados da ultima Z e salva
                                    if (Integer.valueOf(ecf.enviar(EComando.ECF_NumCRZ)[1]) != zs.get(0).getEcfZCrz()) {
                                        new ComandoEmitirReducaoZ().emitirReducaoZBanco();
                                    }
                                }

                                break;
                            case estNaoInicializada:
                            case estDesconhecido:
                                throw new OpenPdvException("Estado do ECF não inicializado ou desconhecido.");
                            case estRelatorio:
                                ecf.enviar(EComando.ECF_CorrigeEstadoErro);
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
                                break;
                        }
                    } catch (Exception ex) {
                        login = false;
                        log.error("Problemas ao validar o estado.", ex);
                        caixa.modoConsulta();
                        JOptionPane.showMessageDialog(splash, "Entrando automaticamente no Modo Consulta.", "OpenPDV", JOptionPane.INFORMATION_MESSAGE);
                    }

                    // verifica se precisa sincronizar
                    splash.pgBarra.setValue(100);
                    if (!Util.getConfig().getProperty("sinc.servidor").endsWith("localhost")) {
                        try {
                            Date recebimento = Util.getData(PAF.AUXILIAR.getProperty("out.recebimento", null)); // ultimo recebimento
                            if (recebimento == null || (atual.getTime() - recebimento.getTime()) / 86400000 > 0) { // maior que 1 dia em milisegundos
                                splash.pgBarra.setString("Sincronizando com o servidor...");
                                ComandoReceberDados.getInstancia().executar();
                            }
                        } catch (OpenPdvException ex) {
                            log.error("Nao conseguiu sincronizar com o servidor.", ex);
                            JOptionPane.showMessageDialog(splash, ex.getMessage(), "Sincronismo", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    splash.pgBarra.setString("Finalizado");
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
