package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.phdss.Util;
import br.com.openpdv.modelo.core.EComandoSQL;
import br.com.openpdv.modelo.core.EDirecao;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.core.Sql;
import br.com.openpdv.modelo.core.filtro.ECompara;
import br.com.openpdv.modelo.core.filtro.FiltroBinario;
import br.com.openpdv.modelo.core.filtro.FiltroData;
import br.com.openpdv.modelo.core.filtro.FiltroObjeto;
import br.com.openpdv.modelo.core.filtro.FiltroGrupo;
import br.com.openpdv.modelo.core.filtro.Filtro;
import br.com.openpdv.modelo.core.parametro.ParametroObjeto;
import br.com.openpdv.modelo.ecf.EcfDocumento;
import br.com.openpdv.modelo.ecf.EcfVenda;
import br.com.openpdv.modelo.ecf.EcfZ;
import br.com.openpdv.modelo.ecf.EcfZTotais;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.ECF;
import br.com.phdss.EComando;
import br.com.phdss.EEstado;
import br.com.phdss.IECF;
import br.com.phdss.controlador.PAF;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import org.apache.log4j.Logger;

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
        // verifica se e a primeira Z do mes, caso o ecf nao faca por conta
        if (Util.getConfig().getProperty("ecf.lmfc").equalsIgnoreCase("SIM")) {
            emitirLMFC();
            log.debug("Emitiu a LMFC");
        }
        // emite a reducao no ECF
        emitirReducaoZEcf();
        log.debug("Emitiu a Z na ECF");
        // salva os dados no banco com a reducao depois de impressao
        emitirReducaoZBanco();
        log.debug("Salvou a Z no banco de dados");
        // atualizando o servidor
        if (Util.getConfig().getProperty("sinc.tipo").equals("arquivo") || !Util.getConfig().getProperty("sinc.servidor").endsWith("localhost")) {
            try {
                ComandoEnviarDados.getInstancia().executar();
                log.debug("Enviou os dados para o sistema on-line");
            } catch (OpenPdvException ex) {
                log.error("Nao enviou os dados para o servidor!", ex);
            }
        }
        // gera o arquivo Movimento do ECF do dia
        if (Util.getConfig().getProperty("ecf.movimento").equalsIgnoreCase("SIM")) {
            // recupera os produtos
            ProdProduto dados = new ProdProduto();
            dados.setCampoOrdem(dados.getCampoId());
            List<ProdProduto> listaProd = service.selecionar(dados, 0, 0, null);
            new ComandoEmitirMovimentoECF(Caixa.getInstancia().getImpressora(), dataMovimento, dataMovimento, listaProd).executar();
            log.debug("Gerou o arquivo de movimento do dia");
        }
        // gera o arquivo do cat52
        if (Util.getConfig().getProperty("ecf.cat52") != null) {
            new ComandoGerarCat52(Caixa.getInstancia().getEmpresa(), Caixa.getInstancia().getImpressora(), dataMovimento).executar();
            log.debug("Gerou o arquivo CAT52");
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
        int tempo = Integer.valueOf(Util.getConfig().getProperty("ecf.tempo"));
        int tentativas = Integer.valueOf(Util.getConfig().getProperty("ecf.tentativas"));

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
     * @exception OpenPdvException dispara caso nao consiga executar.
     */
    public void emitirReducaoZBanco() throws OpenPdvException {
        Map<String, Object> dados = ecf.getDadosZ();
        if (dados != null) {
            try {
                // recuperando a ultima Z emitida desta impressora e pega o ultimo coo
                EcfZ ultZ = new EcfZ();
                ultZ.setOrdemDirecao(EDirecao.DESC);
                FiltroObjeto fo = new FiltroObjeto("ecfImpressora", ECompara.IGUAL, Caixa.getInstancia().getImpressora());
                List<EcfZ> zs = service.selecionar(ultZ, 0, 1, fo);

                // gera o registro EcfZ
                EcfZ z = new EcfZ();
                z.setEcfImpressora(Caixa.getInstancia().getImpressora());
                z.setEcfZUsuario(1);
                z.setEcfZCrz((int) dados.get("NumCRZ"));
                z.setEcfZCooIni(zs == null || zs.isEmpty() ? 1 : zs.get(0).getEcfZCooFin() + 1);
                z.setEcfZCooFin((int) dados.get("NumCOO"));
                z.setEcfZCro((int) dados.get("NumCRO"));
                z.setEcfZMovimento((Date) dados.get("DataMovimento"));
                z.setEcfZEmissao(new Date());
                z.setEcfZBruto((double) dados.get("VendaBruta"));
                z.setEcfZGt((double) dados.get("GrandeTotal"));
                String im = Caixa.getInstancia().getEmpresa().getSisEmpresaIm() == null ? "" : Caixa.getInstancia().getEmpresa().getSisEmpresaIm().replaceAll("\\D", "");
                z.setEcfZIssqn(!im.equals(""));
                z = (EcfZ) service.salvar(z);
                dataMovimento = z.getEcfZMovimento();

                Calendar cal = Calendar.getInstance();
                cal.setTime(dataMovimento);
                cal.add(Calendar.DAY_OF_MONTH, 1);

                // atualiza as vendas, marcando que pertence a esta Z
                ParametroObjeto po = new ParametroObjeto("ecfZ", z);
                FiltroData fd1 = new FiltroData("ecfVendaData", ECompara.MAIOR_IGUAL, dataMovimento);
                FiltroData fd2 = new FiltroData("ecfVendaData", ECompara.MENOR, cal.getTime());
                FiltroGrupo gf = new FiltroGrupo(Filtro.E, fo, fd1, fd2);
                Sql sql = new Sql(new EcfVenda(), EComandoSQL.ATUALIZAR, gf, po);
                service.executar(sql);

                // gera os registros EcfZTotais
                Map<String, Double> totalizadores = (Map<String, Double>) dados.get("Totalizadores");
                List<EcfZTotais> totais = new ArrayList<>();
                for (Entry<String, Double> tupla : totalizadores.entrySet()) {
                    EcfZTotais total = new EcfZTotais();
                    total.setEcfZ(z);
                    total.setEcfZTotaisCodigo(tupla.getKey());
                    total.setEcfZTotaisValor(tupla.getValue());
                    totais.add(total);
                }

                // caso nao exista totalizador Can-T verifica se existe vendas canceladas
                if (!totalizadores.containsKey("Can-T")) {
                    FiltroBinario fb = new FiltroBinario("ecfVendaCancelada", ECompara.IGUAL, true);
                    gf = new FiltroGrupo(Filtro.E, fo, fd1, fd2, fb);
                    List<EcfVenda> canceladas = service.selecionar(new EcfVenda(), 0, 0, gf);
                    if (canceladas != null && canceladas.size() > 0) {
                        EcfZTotais total = new EcfZTotais();
                        total.setEcfZ(z);
                        total.setEcfZTotaisCodigo("Can-T");
                        total.setEcfZTotaisValor(0.00);
                        totais.add(total);
                    }
                }
                // salva os totais do z
                service.salvar(totais);

                // atualiza os documentos, marcando que pertence a esta Z
                fd1 = new FiltroData("ecfDocumentoData", ECompara.MAIOR_IGUAL, dataMovimento);
                fd2 = new FiltroData("ecfDocumentoData", ECompara.MENOR, cal.getTime());
                gf = new FiltroGrupo(Filtro.E, fo, fd1, fd2);
                sql = new Sql(new EcfDocumento(), EComandoSQL.ATUALIZAR, gf, po);
                service.executar(sql);
            } catch (OpenPdvException | IllegalArgumentException ex) {
                log.error("Erro ao gerar e salvar os dados da reducao Z.", ex);
                throw new OpenPdvException("Nao foi possivel salvar os dados da Z no banco!\nAvise o administrador pra realizar manualmente!");
            }
        } else {
            log.error("Erro ao pegar os dados da ultima reducao Z.");
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
