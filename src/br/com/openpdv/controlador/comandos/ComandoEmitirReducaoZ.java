package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.ECF;
import br.com.openpdv.controlador.EComandoECF;
import br.com.openpdv.controlador.PAF;
import br.com.openpdv.controlador.core.CoreService;
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
        gerarMovimentoECF();
        // atualizando o servidor
        new ComandoEnviarDados().executar();
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
            throw new OpenPdvException(resp[1]);
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
                z.setEcfZIssqn(false);
                // salva EcfZ
                z = (EcfZ) service.salvar(z);
                dataMovimento = z.getEcfZMovimento();
                zId = z.getId();

                // atualiza as vendas, marcando que pertence a esta Z
                ParametroObjeto po = new ParametroObjeto("ecfZ", z);
                FiltroObjeto fo = new FiltroObjeto("ecfZ", ECompara.NULO, null);
                List<Sql> slqs = new ArrayList<>();
                slqs.add(new Sql(new EcfVenda(), EComandoSQL.ATUALIZAR, fo, po));
                service.executar(slqs);

                // gera os registros EcfZTotais
                List<EcfZTotais> totais = new ArrayList<>();
                Map<String, String> aliq = ini.get("Aliquotas");
                for (String chave : aliq.keySet()) {
                    if (!aliq.get(chave).equals("0")) {
                        EcfZTotais total = new EcfZTotais();
                        total.setEcfZ(z);
                        total.setEcfZTotaisCodigo(chave);
                        total.setEcfZTotaisValor(Double.valueOf(aliq.get(chave)));
                        totais.add(total);
                    }
                }

                // outros icms
                Map<String, String> outras = ini.get("OutrasICMS");
                for (String chave : outras.keySet()) {
                    if (!outras.get(chave).equals("0")) {
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
                            total.setEcfZTotaisValor(Double.valueOf(outras.get(chave)));
                            totais.add(total);
                        }
                    }
                }

                // operacao nao fiscal
                double opnf = ini.get("Totalizadores", "TotalNaoFiscal", double.class);
                if (opnf > 0) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("OPNF");
                    total.setEcfZTotaisValor(opnf);
                    totais.add(total);
                }

                // descontos
                double descT = ini.get("Totalizadores", "TotalDescontos", double.class);
                if (descT > 0) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("DT");
                    total.setEcfZTotaisValor(descT);
                    totais.add(total);
                }
                double descS = ini.get("Totalizadores", "TotalDescontosISSQN", double.class);
                if (descT > 0) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("DS");
                    total.setEcfZTotaisValor(descS);
                    totais.add(total);
                }

                // acrescimos
                double acresT = ini.get("Totalizadores", "TotalAcrescimos", double.class);
                if (acresT > 0) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("AT");
                    total.setEcfZTotaisValor(acresT);
                    totais.add(total);
                }
                double acresS = ini.get("Totalizadores", "TotalAcrescimosISSQN", double.class);
                if (acresS > 0) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("AS");
                    total.setEcfZTotaisValor(acresS);
                    totais.add(total);
                }

                // cancelamentos
                double canT = ini.get("Totalizadores", "TotalCancelamentos", double.class);
                if (canT > 0) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("Can-T");
                    total.setEcfZTotaisValor(canT);
                    totais.add(total);
                }
                double cantS = ini.get("Totalizadores", "TotalCancelamentosISSQN", double.class);
                if (cantS > 0) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo("Can-S");
                    total.setEcfZTotaisValor(cantS);
                    totais.add(total);
                }

                // salva os totais do z
                service.salvar(totais);
            } catch (Exception ex) {
                log.error("Erro ao gerar ao salvar os dados da reducao Z.", ex);
                throw new OpenPdvException(ex);
            }
        } else {
            log.error("Erro ao pegar os dados da ultima reducao Z -> " + resp[1]);
            throw new OpenPdvException(resp[1]);
        }
    }

    /**
     * Metodo que emite o movimento do ECF logo apos a reducao Z.
     *
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void gerarMovimentoECF() throws OpenPdvException {
        ComandoEmitirMovimentoECF movimento = new ComandoEmitirMovimentoECF(Caixa.getInstancia().getImpressora(), dataMovimento, dataMovimento);
        movimento.executar();
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
                throw new OpenPdvException(ex);
            }
        } else {
            log.error("Erro ao recuperar a data do ECF -> " + resp[1]);
            throw new OpenPdvException(resp[1]);
        }

        // transforma a data para o dia 1º do mes
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(cal.get(Calendar.YEAR), Calendar.MONTH, 1, 0, 0, 0);

        // procura por uma Reducao Z no mes que esta o ECF
        FiltroData fd = new FiltroData("ecfZEmissao", ECompara.MAIOR_IGUAL, cal.getTime());
        List<EcfZ> zs = service.selecionar(new EcfZ(), 0, 0, fd);
        if (zs.isEmpty()) {
            cal.add(Calendar.MONTH, - 1);
            String inicio = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            String fim = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
            PAF.leituraMF(EComandoECF.ECF_PafMf_Lmfc_Impressao, new String[]{inicio, fim});
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
