package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.EDirecao;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.EJuncao;
import br.com.openpdv.modelo.core.filtro.FiltroData;
import br.com.openpdv.modelo.core.filtro.FiltroObjeto;
import br.com.openpdv.modelo.core.filtro.GrupoFiltro;
import br.com.openpdv.modelo.core.filtro.IFiltro;
import br.com.openpdv.modelo.core.parametro.ParametroObjeto;
import br.com.openpdv.modelo.ecf.EcfDocumento;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.ecf.EcfZ;
import br.com.openpdv.modelo.ecf.EcfZTotais;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.ECF;
import br.com.phdss.EComando;
import br.com.phdss.EEstado;
import br.com.phdss.IECF;
import br.com.phdss.controlador.PAF;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private IECF ecf;

    /**
     * Construtor padrao.
     */
    public ComandoEmitirReducaoZ() {
        this.log = Logger.getLogger(ComandoEmitirReducaoZ.class);
        this.service = new CoreService();
        this.ecf = ECF.getInstancia();
    }

    @Override
    public void executar() throws OpenPdvException {
        // verifica se e a primeira Z do mes
        emitirLMFC();
        // pega os dados da z antes de imprimir
        String[] antesZ = ecf.enviar(EComando.ECF_DadosReducaoz);
        // emite a reducao no ECF
        emitirReducaoZEcf();
        // pega os dados da z depois de imprimir
        String[] depoisZ = ecf.enviar(EComando.ECF_DadosUltimaReducaoZ);
        try {
            // salva os dados no banco com a reducao depois de impressao
            emitirReducaoZBanco(depoisZ);
        } catch (OpenPdvException ex) {
            // caso aconteca um erro, tenta com os dados antes da impressao
            emitirReducaoZBanco(antesZ);
        }

        // gera o arquivo Movimento do ECF do dia
        new ComandoEmitirMovimentoECF(Caixa.getInstancia().getImpressora(), dataMovimento, dataMovimento).executar();
        // gera os totais dos pagamentos
        new ComandoTotalizarPagamentos(dataMovimento).executar();
        // gera o arquivo do cat52
        if (Util.getConfig().get("ecf.cat52") != null) {
            new ComandoGerarCat52(Caixa.getInstancia().getEmpresa(), Caixa.getInstancia().getImpressora(), dataMovimento).executar();
        }
        // atualizando o servidor
        if (Util.getConfig().get("sinc.tipo").equals("arquivo") || !Util.getConfig().get("sinc.servidor").endsWith("localhost")) {
            ComandoEnviarDados.getInstancia().executar();
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
        ecf.enviar(EComando.ECF_ReducaoZ);

        // enquanto o estado nao for bloqueado(Z emitida) ou o limite superar 3 tentativas
        EEstado estado;
        int tempo = Integer.valueOf(Util.getConfig().get("ecf.tempo"));
        int tentativas = Integer.valueOf(Util.getConfig().get("ecf.tentativas"));

        do {
            // recupera o estado
            try {
                estado = ecf.validarEstado();
            } catch (Exception ex) {
                estado = EEstado.estDesconhecido;
            } finally {
                tentativas--;
            }

            // espera alguns segundos
            if (estado != EEstado.estBloqueada) {
                try {
                    Thread.sleep(tempo * 1000);
                } catch (InterruptedException ex) {
                    // nao faz nada
                }
            } else {
                break;
            }
        } while (tentativas > 0);
    }

    /**
     * Metodo que emite salva os dados da ultima Reducao Z no Banco.
     *
     * @param dados informacoes da z.
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void emitirReducaoZBanco(String[] dados) throws OpenPdvException {
        if (IECF.OK.equals(dados[0])) {
            try {
                // pega os dados
                InputStream stream = new ByteArrayInputStream(dados[1].replace(",", ".").getBytes("UTF-8"));
                Wini ini = new Wini(stream);

                // recuperando a ultima Z emitida desta impressora e pega o ultimo coo
                int cooIni;
                EcfZ ultZ = new EcfZ();
                ultZ.setOrdemDirecao(EDirecao.DESC);
                FiltroObjeto fo = new FiltroObjeto("ecfImpressora", ECompara.IGUAL, Caixa.getInstancia().getImpressora());
                List<EcfZ> zs = service.selecionar(ultZ, 0, 1, fo);
                if (zs == null || zs.isEmpty()) {
                    cooIni = 1;
                } else {
                    cooIni = zs.get(0).getEcfZCooFin() + 1;
                }

                // gera o registro EcfZ
                EcfZ z = new EcfZ();
                z.setEcfImpressora(Caixa.getInstancia().getImpressora());
                z.setEcfZUsuario(1);
                z.setEcfZCrz(ini.get("ECF", "NumCRZ", int.class));
                z.setEcfZCooIni(cooIni);
                z.setEcfZCooFin(ini.get("ECF", "NumCOO", int.class));
                z.setEcfZCro(ini.get("ECF", "NumCRO", int.class));
                try {
                    String movimento = ini.get("ECF", "DataMovimento");
                    z.setEcfZMovimento(new SimpleDateFormat("dd/MM/yy").parse(movimento));
                } catch (ParseException ex) {
                    z.setEcfZMovimento(new Date());
                }
                z.setEcfZEmissao(new Date());
                z.setEcfZBruto(ini.get("Totalizadores", "VendaBruta", double.class));
                z.setEcfZGt(ini.get("Totalizadores", "GrandeTotal", double.class));
                String im = Caixa.getInstancia().getEmpresa().getSisEmpresaIm() == null ? "" : Caixa.getInstancia().getEmpresa().getSisEmpresaIm().replaceAll("\\D", "");
                z.setEcfZIssqn(!im.equals(""));
                // salva EcfZ
                z = (EcfZ) service.salvar(z);
                dataMovimento = z.getEcfZMovimento();

                Calendar cal = Calendar.getInstance();
                cal.setTime(dataMovimento);
                cal.add(Calendar.DAY_OF_MONTH, 1);

                // atualiza as vendas, marcando que pertence a esta Z
                ParametroObjeto po = new ParametroObjeto("ecfZ", z);
                FiltroData fd1 = new FiltroData("ecfVendaData", ECompara.MAIOR_IGUAL, dataMovimento);
                FiltroData fd2 = new FiltroData("ecfVendaData", ECompara.MENOR, cal.getTime());
                GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fo, fd1, fd2});
                Sql sql = new Sql(new EcfVenda(), EComandoSQL.ATUALIZAR, gf, po);
                service.executar(sql);

                // atualiza os documentos, marcando que pertence a esta Z
                fd1 = new FiltroData("ecfDocumentoData", ECompara.MAIOR_IGUAL, dataMovimento);
                fd2 = new FiltroData("ecfDocumentoData", ECompara.MENOR, cal.getTime());
                gf = new GrupoFiltro(EJuncao.E, new IFiltro[]{fo, fd1, fd2});
                sql = new Sql(new EcfDocumento(), EComandoSQL.ATUALIZAR, gf, po);
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
                log.error("Erro ao gerar e salvar os dados da reducao Z -> " + dados[1], ex);
                throw new OpenPdvException("Nao foi possivel salvar os dados da Z no banco!\nAvise o administrador pra realizar manualmente!");
            }
        } else {
            log.error("Erro ao pegar os dados da ultima reducao Z -> " + dados[1]);
            throw new OpenPdvException("Nao foi possivel salvar os dados da Z no banco!\nAvise o administrador pra realizar manualmente!");
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
        String[] resp = ecf.enviar(EComando.ECF_DataHora);
        if (IECF.OK.equals(resp[0])) {
            try {
                data = new SimpleDateFormat("dd/MM/yy HH:mm:ss").parse(resp[1]);
            } catch (ParseException ex) {
                log.error("Erro na formatacao da data do ECF.", ex);
                data = new Date();
            }
        } else {
            log.error("Erro ao recuperar a data do ECF -> " + resp[1]);
            data = new Date();
        }

        // transforma a data para o dia 1º do mes
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // procura por uma Reducao Z no mes que esta o ECF
        FiltroData fd = new FiltroData("ecfZMovimento", ECompara.MAIOR_IGUAL, cal.getTime());
        List<EcfZ> zs = service.selecionar(new EcfZ(), 0, 1, fd);
        if (zs.isEmpty()) {
            cal.add(Calendar.MONTH, - 1);
            String inicio = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            String fim = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
            PAF.leituraMF(EComando.ECF_PafMf_Lmfc_Impressao, new String[]{inicio, fim});
        }
    }
}
