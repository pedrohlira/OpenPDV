package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.Conexao;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.visao.core.Aguarde;
import br.com.phdss.Util;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import javax.swing.JOptionPane;
import javax.ws.rs.core.MediaType;

/**
 * Classe que realiza a operacao de validar o sistema.
 *
 * @author Pedro H. Lira
 */
public class ComandoValidarSistema implements IComando {

    @Override
    public void executar() throws OpenPdvException {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // recuperando os dados do auxiliar local
                    File aux = new File("conf" + System.getProperty("file.separator") + "auxiliar.txt");
                    String local = null;
                    byte[] bytes;

                    if (aux.exists()) {
                        try (FileInputStream inArquivo = new FileInputStream(aux)) {
                            bytes = new byte[inArquivo.available()];
                            inArquivo.read(bytes);
                            local = new String(bytes);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Atenção: Arquivo nao existe -> " + aux.getAbsolutePath(), "Sobre", JOptionPane.WARNING_MESSAGE);
                    }

                    if (local != null && !local.equals("")) {
                        // enviando o auxiliar local e recebendo o novo do servidor
                        Client c = Conexao.getClientRest();
                        WebResource wr = c.resource(Util.getConfig().getProperty("openpdv.url"));
                        String remoto = wr.type(MediaType.TEXT_PLAIN).accept(MediaType.TEXT_PLAIN).put(String.class, local);

                        try (FileWriter fw = new FileWriter(aux, false)) {
                            fw.write(remoto);
                            fw.flush();

                            Aguarde.getInstancia().setVisible(false);
                            JOptionPane.showMessageDialog(null, "Arquivo salvo com sucesso, abra novamente o OpenPDV para ativá-lo,", "Sobre", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(0);
                        }
                    }
                } catch (Exception ex) {
                    Aguarde.getInstancia().setVisible(false);
                    JOptionPane.showMessageDialog(null, "Atenção: Problemas ao tentar atualizar a validade!", "Sobre", JOptionPane.WARNING_MESSAGE);
                }
            }
        }).start();

        Aguarde.getInstancia().setVisible(true);
    }

    @Override
    public void desfazer() throws OpenPdvException {
    }
    
}
