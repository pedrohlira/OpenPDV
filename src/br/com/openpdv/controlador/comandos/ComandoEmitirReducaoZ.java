package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.controlador.core.Util;
import br.com.openpdv.controlador.permissao.Login;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.EDirecao;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroData;
import br.com.openpdv.modelo.core.filtro.FiltroObjeto;
import br.com.openpdv.modelo.core.parametro.ParametroObjeto;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.ecf.EcfZ;
import br.com.openpdv.modelo.ecf.EcfZTotais;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.ECF;
import br.com.phdss.EComandoECF;
import br.com.phdss.EEstadoECF;
import br.com.phdss.controlador.PAF;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.ini4j.Wini;

/**
 * Classe responsavel por executar a reducao Z
 *
 * @author Pedro H. Lira
 */
public class ComandoEmitirReducaoZ implements IComando {

    private Logger log;
    private CoreService service;
    private Date dataMovimento;
    private int zId;

    /**
     * Construtor padrao.
     */
    public ComandoEmitirReducaoZ() {
        this.log = Logger.getLogger(ComandoEmitirReducaoZ.class);
        this.service = new CoreService();
    }

    @Override
    public void executar() throws OpenPdvException {
        // verifica se e a primeira Z do mes
        emitirLMFC();
        // emite a reducao no ECF
        emitirReducaoZEcf();
        // salva os dados no banco
        emitirReducaoZBanco();
        // gera o arquivo Movimento do ECF do dia
        ComandoEmitirMovimentoECF movimento = new ComandoEmitirMovimentoECF(Caixa.getInstancia().getImpressora(), dataMovimento, dataMovimento);
        movimento.executar();
        // gera os totais dos pagamentos
        ComandoTotalizarPagamentos totalizar = new ComandoTotalizarPagamentos(dataMovimento);
        totalizar.executar();
        // atualizando o servidor
        if (!Util.getConfig().get("sinc.servidor").endsWith("localhost")) {
            new ComandoEnviarDados().executar();
        }
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }

    /**
     * Metodo que emite a Reducao Z no ECF.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void emitirReducaoZEcf() throws OpenPdvException {
        String[] resp = ECF.enviar(EComandoECF.ECF_ReducaoZ);

        if (ECF.ERRO.equals(resp[0])) {
            // enquanto o estado nao for bloquado(Z emitida), espera
            EEstadoECF estado;
            do {
                int escolha = JOptionPane.showOptionDialog(null, "O documento ainda está sendo impresso?", "Redução Z",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, Util.OPCOES, JOptionPane.YES_OPTION);
                if (escolha == JOptionPane.YES_OPTION) {
                    try {
                        // aguarda meio minuto
                        Thread.sleep(30000);
                    } catch (InterruptedException ex) {
                    }
                } else {
                    break;
                }

                // recupera o estado
                try {
                    estado = ECF.validarEstado();
                } catch (Exception ex) {
                    estado = EEstadoECF.estDesconhecido;
                }
            } while (estado != EEstadoECF.estBloqueada);
        }
    }

    /**
     * Metodo que emite salva os dados da ultima Reducao Z no Banco.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void emitirReducaoZBanco() throws OpenPdvException {
        String[] resp = ECF.enviar(EComandoECF.ECF_DadosUltimaReducaoZ);
        if (ECF.OK.equals(resp[0])) {
            try {
                // pega os dados
                InputStream stream = new ByteArrayInputStream(resp[1].replace(",", ".").getBytes("UTF-8"));
                Wini ini = new Wini(stream);

                // recuperando a ultima Z emitida desta impressora e pega o ultimo coo
                int cooIni;
                EcfZ ultZ = new EcfZ();
                ultZ.setOrdemDirecao(EDirecao.DESC);
                FiltroObjeto fobj = new FiltroObjeto("ecfImpressora", ECompara.IGUAL, Caixa.getInstancia().getImpressora());
                List<EcfZ> zs = service.selecionar(ultZ, 0, 1, fobj);
                if (zs == null || zs.isEmpty()) {
                    cooIni = 1;
                } else {
                    cooIni = zs.get(0).getEcfZCooFin() + 1;
                }

                // gera o registro EcfZ
                EcfZ z = new EcfZ();
                z.setEcfImpressora(Caixa.getInstancia().getImpressora());
                z.setEcfZUsuario(Login.getOperador() == null ? 0 : Login.getOperador().getId());
                z.setEcfZCrz(ini.get("ECF", "NumCRZ", int.class));
                z.setEcfZCooIni(cooIni);
                z.setEcfZCooFin(ini.get("ECF", "NumCOO", int.class));
                z.setEcfZCro(ini.get("ECF", "NumCRO", int.class));
                String movimento = ini.get("ECF", "DataMovimento");
                z.setEcfZMovimento(new SimpleDateFormat("dd/MM/yy").parse(movimento));
                z.setEcfZEmissao(new Date());
                z.setEcfZBruto(ini.get("Totalizadores", "VendaBruta", double.class));
                z.setEcfZGt(ini.get("Totalizadores", "GrandeTotal", double.class));
                String im = Caixa.getInstancia().getEmpresa().getSisEmpresaIm() == null ? "" : Caixa.getInstancia().getEmpresa().getSisEmpresaIm().replaceAll("\\D", "");
                z.setEcfZIssqn(!im.equals(""));
                // salva EcfZ
                z = (EcfZ) service.salvar(z);
                dataMovimento = z.getEcfZMovimento();
                zId = z.getId();

                // atualiza as vendas, marcando que pertence a esta Z
                ParametroObjeto po = new ParametroObjeto("ecfZ", z);
                FiltroObjeto fo = new FiltroObjeto("ecfZ", ECompara.NULO, null);
                Sql sql = new Sql(new EcfVenda(), EComandoSQL.ATUALIZAR, fo, po);
                service.executar(sql);

                // gera os registros EcfZTotais
                Map<String, EcfZTotais> totais = new HashMap<>();
                Map<String, String> aliq = ini.get("Aliquotas");
                for (String chave : aliq.keySet()) {
                    double valor = Double.valueOf(aliq.get(chave));
                    if (valor > 0.00) {
                        EcfZTotais total = new EcfZTotais();
                        total.setEcfZ(z);
                        total.setEcfZTotaisCodigo(chave);
                        total.setEcfZTotaisValor(valor);
                        if (!totais.containsKey(total.getEcfZTotaisCodigo())) {
                            totais.put(total.getEcfZTotaisCodigo(), total);
                        }
                    }
                }

                // outros icms
                Map<String, String> outras = ini.get("OutrasICMS");
                for (String chave : outras.keySet()) {
                    double valor = Double.valueOf(outras.get(chave));
                    if (valor > 0.00) {
                        // valida qual o tipo
                        String codigo = "";
                        if (chave.contains("Substituicao")) {
                            codigo = "F";
                        } else if (chave.contains("NaoTributado")) {
                            codigo = "N";
                        } else if (chave.contains("Isencao")) {
                            codigo = "I";
                        }
                        // se achou um tipo valido adiciona
                        if (!codigo.equals("")) {
                            codigo += chave.contains("ISSQN") ? "S1" : "1";
                            EcfZTotais total = new EcfZTotais();
                            total.setEcfZ(z);
                            total.setEcfZTotaisCodigo(codigo);
                            total.setEcfZTotaisValor(valor);
                            if (!totais.containsKey(total.getEcfZTotaisCodigo())) {
                                totais.put(total.getEcfZTotaisCodigo(), total);
                            }
                        }
                    }
                }

                // operacao nao fiscal
                double opnf = ini.get("Totalizadores", "TotalNaoFiscal", double.class);
                if (opnf > 0.00) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("OPNF");
                    total.setEcfZTotaisValor(opnf);
                    if (!totais.containsKey(total.getEcfZTotaisCodigo())) {
                        totais.put(total.getEcfZTotaisCodigo(), total);
                    }
                }

                // descontos
                double descT = ini.get("Totalizadores", "TotalDescontos", double.class);
                if (descT > 0.00) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("DT");
                    total.setEcfZTotaisValor(descT);
                    if (!totais.containsKey(total.getEcfZTotaisCodigo())) {
                        totais.put(total.getEcfZTotaisCodigo(), total);
                    }
                }
                double descS = ini.get("Totalizadores", "TotalDescontosISSQN", double.class);
                if (descS > 0.00) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("DS");
                    total.setEcfZTotaisValor(descS);
                    if (!totais.containsKey(total.getEcfZTotaisCodigo())) {
                        totais.put(total.getEcfZTotaisCodigo(), total);
                    }
                }

                // acrescimos
                double acresT = ini.get("Totalizadores", "TotalAcrescimos", double.class);
                if (acresT > 0.00) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("AT");
                    total.setEcfZTotaisValor(acresT);
                    if (!totais.containsKey(total.getEcfZTotaisCodigo())) {
                        totais.put(total.getEcfZTotaisCodigo(), total);
                    }
                }
                double acresS = ini.get("Totalizadores", "TotalAcrescimosISSQN", double.class);
                if (acresS > 0.00) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("AS");
                    total.setEcfZTotaisValor(acresS);
                    if (!totais.containsKey(total.getEcfZTotaisCodigo())) {
                        totais.put(total.getEcfZTotaisCodigo(), total);
                    }
                }

                // cancelamentos
                double canT = ini.get("Totalizadores", "TotalCancelamentos", double.class);
                if (canT > 0.00) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("Can-T");
                    total.setEcfZTotaisValor(canT);
                    if (!totais.containsKey(total.getEcfZTotaisCodigo())) {
                        totais.put(total.getEcfZTotaisCodigo(), total);
                    }
                }
                double canS = ini.get("Totalizadores", "TotalCancelamentosISSQN", double.class);
                if (canS > 0.00) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("Can-S");
                    total.setEcfZTotaisValor(canS);
                    if (!totais.containsKey(total.getEcfZTotaisCodigo())) {
                        totais.put(total.getEcfZTotaisCodigo(), total);
                    }
                }

                // salva os totais do z
                service.salvar(totais.values());
            } catch (Exception ex) {
                log.error("Erro ao gerar ao salvar os dados da reducao Z.", ex);
                log.error(resp);
                throw new OpenPdvException(ex);
            }
        } else {
            log.error("Erro ao pegar os dados da ultima reducao Z -> " + resp[1]);
            throw new OpenPdvException(resp[1]);
        }
    }

    /**
     * Metodo que emite a LMFC caso seja a primeira Reducao Z do mes.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void emitirLMFC() throws OpenPdvException {
        Date data;

        // data atual do ECF
        String[] resp = ECF.enviar(EComandoECF.ECF_DataHora);
        if (ECF.OK.equals(resp[0])) {
            try {
                data = new SimpleDateFormat("dd/MM/yy HH:mm:ss").parse(resp[1]);
            } catch (ParseException ex) {
                log.error("Erro na formatacao da data do ECF.", ex);
                data = new Date();
            }
        } else {
            log.error("Erro ao recuperar a data do ECF -> " + resp[1]);
            throw new OpenPdvException(resp[1]);
        }

        // transforma a data para o dia 1º do mes
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // procura por uma Reducao Z no mes que esta o ECF
        FiltroData fd = new FiltroData("ecfZMovimento", ECompara.MAIOR_IGUAL, cal.getTime());
        List<EcfZ> zs = service.selecionar(new EcfZ(), 0, 0, fd);
        if (zs.isEmpty()) {
            // somente gera caso tenha algum registro de Z.
            zs = service.selecionar(new EcfZ(), 0, 1, null);
            if (!zs.isEmpty()) {
                cal.add(Calendar.MONTH, - 1);
                String inicio = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                String fim = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
                PAF.leituraMF(EComandoECF.ECF_PafMf_Lmfc_Impressao, new String[]{inicio, fim});
            }
        }
    }

    public Date getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(Date dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public int getzId() {
        return zId;
    }

    public void setzId(int zId) {
        this.zId = zId;
    }
}
