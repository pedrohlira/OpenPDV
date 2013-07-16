package br.com.openpdv.controlador.core;

import br.com.openpdv.rest.RestContexto;
import br.com.phdss.controlador.PAF;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComboBox;
import javax.swing.text.MaskFormatter;

/**
 * Classe responsavel para funcoes utilitarias.
 *
 * @author Pedro H. Lira
 */
public class Util {

    // tabela com vinculos das letras
    private static Map<String, String> config;
    private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    public static final String[] OPCOES = {"Sim", "Não"};

    /**
     * Constutor padrao.
     */
    private Util() {
    }

    /**
     * Metodo que normaliza os caracteres removendo os acentos.
     *
     * @param texto o texto acentuado.
     * @return o texto sem acentos.
     */
    public static String normaliza(String texto) {
        CharSequence cs = new StringBuilder(texto == null ? "" : texto);
        return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * Metodo que normaliza os caracteres removendo os acentos de todos os
     * campos de um objeto.
     *
     * @param bloco o objeto que sera modificado.
     */
    public static void normaliza(Object bloco) {
        for (Method metodo : bloco.getClass().getMethods()) {
            try {
                if (isGetter(metodo)) {
                    Object valorMetodo = metodo.invoke(bloco, new Object[]{});

                    if (metodo.getReturnType() == String.class) {
                        String nomeMetodo = metodo.getName().replaceFirst("get", "set");
                        Method set = bloco.getClass().getMethod(nomeMetodo, new Class[]{String.class});
                        String valor = valorMetodo == null ? "" : valorMetodo.toString();
                        valor = normaliza(valor);
                        set.invoke(bloco, new Object[]{valor.trim()});
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                // pula o item
            }
        }
    }

    /**
     * Metodo que informa se o metodo da classe é do tipo GET.
     *
     * @param method usando reflection para descrobrir os metodos.
     * @return verdadeiro se o metodo for GET, falso caso contrario.
     */
    public static boolean isGetter(Method method) {
        if (!method.getName().startsWith("get")) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        if (void.class.equals(method.getReturnType())) {
            return false;
        }
        return true;
    }

    /**
     * Metodo que informa se o metodo da classe é do tipo SET.
     *
     * @param method usando reflection para descrobrir os metodos.
     * @return verdadeiro se o metodo for SET, falso caso contrario.
     */
    public static boolean isSetter(Method method) {
        if (!method.getName().startsWith("set")) {
            return false;
        }
        if (method.getParameterTypes().length == 0) {
            return false;
        }
        if (!void.class.equals(method.getReturnType())) {
            return false;
        }
        return true;
    }

    /**
     * Metodo que formata um texto em data no padrao dd/MM/aaaa
     *
     * @param data o texto da data.
     * @return um objeto Date ou null caso nao consiga fazer o parser.
     */
    public static Date getData(String data) {
        return formataData(data, "dd/MM/yyyy");
    }

    /**
     * Metodo que formata uma data em texto no padrao dd/MM/aaaa
     *
     * @param data o objeto Date.
     * @return uma String formatada ou null caso a data nao seja valida.
     */
    public static String getData(Date data) {
        return formataData(data, "dd/MM/yyyy");
    }

    /**
     * Metodo que formata um texto em data no padrao dd/MM/aaaa HH:mm:ss
     *
     * @param data o texto da data.
     * @return um objeto Date ou null caso nao consiga fazer o parser.
     */
    public static Date getDataHora(String data) {
        return formataData(data, "dd/MM/yyyy HH:mm:ss");
    }

    /**
     * Metodo que formata uma data em texto no padrao dd/MM/aaaa HH:mm:ss
     *
     * @param data o objeto Date.
     * @return uma String formatada ou null caso a data nao seja valida.
     */
    public static String getDataHora(Date data) {
        return formataData(data, "dd/MM/yyyy HH:mm:ss");
    }

    /**
     * Metodo que formata a data.
     *
     * @param data a data do tipo Date.
     * @param formato o formado desejado.
     * @return a data formatada como solicidato.
     */
    public static String formataData(Date data, String formato) {
        try {
            return new SimpleDateFormat(formato).format(data);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Metodo que formata a data.
     *
     * @param data a data em formato string.
     * @param formato o formado desejado.
     * @return a data como objeto ou null se tiver erro.
     */
    public static Date formataData(String data, String formato) {
        try {
            return new SimpleDateFormat(formato).parse(data);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * @see #formataNumero(double, int, int, boolean)
     */
    public static String formataNumero(String valor, int inteiros, int decimal, boolean grupo) {
        return formataNumero(Double.valueOf(valor), inteiros, decimal, grupo);
    }

    /**
     * Metodo que faz a formatacao de numeros com inteiros e fracoes
     *
     * @param valor o valor a ser formatado
     * @param inteiros o minimo de inteiros, que serao completados com ZEROS se
     * preciso
     * @param decimal o minimo de decimais, que serao completados com ZEROS se
     * preciso
     * @param grupo se sera colocado separador de grupo de milhar
     * @return uma String com o numero formatado
     */
    public static String formataNumero(double valor, int inteiros, int decimal, boolean grupo) {
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(inteiros);
        nf.setMinimumFractionDigits(decimal);
        nf.setMaximumFractionDigits(decimal);
        nf.setGroupingUsed(grupo);
        return nf.format(valor);
    }

    /**
     * Metodo que formata o texto usando a mascara passada.
     *
     * @param texto o texto a ser formatado.
     * @param mascara a mascara a ser usada.
     * @return o texto formatado.
     * @throws ParseException caso ocorra erro.
     */
    public static String formataTexto(String texto, String mascara) throws ParseException {
        MaskFormatter mf = new MaskFormatter(mascara);
        mf.setValueContainsLiteralCharacters(false);
        return mf.valueToString(texto);
    }

    /**
     * Metodo que formata o texto.
     *
     * @param texto o texto a ser formatado.
     * @param caracter o caracter que sera repetido.
     * @param tamanho o tamanho total do texto de resposta.
     * @param direita a direcao onde colocar os caracteres.
     * @return o texto formatado.
     */
    public static String formataTexto(String texto, String caracter, int tamanho, boolean direita) {
        StringBuilder sb = new StringBuilder();
        int fim = tamanho - texto.length();
        for (int i = 0; i < fim; i++) {
            sb.append(caracter);
        }
        return direita ? texto + sb.toString() : sb.toString() + texto;
    }

    /**
     * Metodo que calcula o digito.
     *
     * @param str valor do texto.
     * @param peso array de pesos.
     * @return um numero calculado.
     */
    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * peso[peso.length - str.length() + indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    /**
     * Metodo que valida se e CPF
     *
     * @param cpf o valor do texto.
     * @return verdadeiro se valido, falso caso contrario.
     */
    public static boolean isCPF(String cpf) {
        if ((cpf == null) || (cpf.length() != 11)) {
            return false;
        } else {
            Pattern p = Pattern.compile(cpf.charAt(0) + "{11}");
            Matcher m = p.matcher(cpf);
            if (m.find()) {
                return false;
            }
        }

        Integer digito1 = calcularDigito(cpf.substring(0, 9), pesoCPF);
        Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCPF);
        return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
    }

    /**
     * Metodo que valida se e CNPJ
     *
     * @param cnpj o valor do texto.
     * @return verdadeiro se valido, falso caso contrario.
     */
    public static boolean isCNPJ(String cnpj) {
        if ((cnpj == null) || (cnpj.length() != 14)) {
            return false;
        } else {
            Pattern p = Pattern.compile(cnpj.charAt(0) + "{14}");
            Matcher m = p.matcher(cnpj);
            if (m.find()) {
                return false;
            }
        }

        Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
        Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ);
        return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
    }

    /**
     * Metodo que recupera as configuracoes do sistema.
     *
     * @return Um mapa de String contendo chave/valor.
     */
    public static Map<String, String> getConfig() {
        if (config == null) {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("conf" + System.getProperty("file.separator") + "config.properties")) {
                props.load(fis);
                config = new HashMap<>();
                for (String chave : props.stringPropertyNames()) {
                    config.put(chave, props.getProperty(chave));
                }
            } catch (Exception ex) {
                config = null;
            }
        }
        return config;
    }

    /**
     * Metodo que gera um objeto de comunicaxao com o RESTful do sistema.
     *
     * @param path o caminho especifico do comando no servidor.
     * @return um objeto de referencia web.
     */
    public static WebResource getRest(String path) {
        Client c = getClientRest();

        // set a url completa
        StringBuilder sb = new StringBuilder();
        sb.append(Util.getConfig().get("sinc.servidor")).append(":");
        sb.append(Util.getConfig().get("sinc.porta")).append(path);
        WebResource wr = c.resource(sb.toString());

        return wr;
    }

    /**
     * Metodo que gera um cliente rest usando os parametros padroes de
     * comunicacao.
     *
     * @return um objeto de acesso ao RestFull.
     */
    public static Client getClientRest() {
        // seta o cliente
        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(RestContexto.class);
        Client c = Client.create(cc);
        c.setFollowRedirects(true);
        c.setConnectTimeout(Integer.valueOf(config.get("sinc.timeout")) * 1000);
        c.setReadTimeout(Integer.valueOf(config.get("sinc.timeout")) * 1000);

        // cria a autenticacao
        String usuario = PAF.AUXILIAR.getProperty("cli.cnpj");
        String senha = PAF.AUXILIAR.getProperty("ecf.serie").split(";")[0];
        
        // criptografa a senha se estiver setado no config
        if(Boolean.valueOf(config.get("sinc.criptografar"))){
            senha = PAF.encriptar(senha);
        }

        // seta os filtros
        c.addFilter(new HTTPBasicAuthFilter(usuario, senha));
        c.addFilter(new GZIPContentEncodingFilter(true));
        return c;
    }
    
    
    /**
     * Metodo que seleciona um item da combo pelo valor.
     *
     * @param combo a ser verificada.
     * @param valor a ser comparado.
     */
    public static void selecionarCombo(JComboBox combo, String valor) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i).toString();
            if (item.startsWith(valor)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }
}
