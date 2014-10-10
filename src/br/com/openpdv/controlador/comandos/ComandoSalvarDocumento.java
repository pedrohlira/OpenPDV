package br.com.openpdv.controlador.comandos;

import br.com.openpdv.controlador.core.CoreService;
import br.com.openpdv.modelo.core.OpenPdvException;
import br.com.openpdv.modelo.ecf.EcfDocumento;
import br.com.openpdv.visao.core.Caixa;
import br.com.phdss.ECF;
import br.com.phdss.EComando;
import br.com.phdss.IECF;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe responsavel por salvar os documentos
 *
 * @author Pedro H. Lira
 */
public class ComandoSalvarDocumento implements IComando {

    private String tipo;
    private IECF ecf;

    /**
     * Costrutor padrao passando o tipo de documento a ser salvo.
     *
     * @param tipo o tipo de documento [CM, RV, CC, CN, NC, RG]
     */
    public ComandoSalvarDocumento(String tipo) {
        this.tipo = tipo;
        this.ecf = ECF.getInstancia();
    }

    @Override
    public void executar() throws OpenPdvException {
        // seta os dados do documento.
        EcfDocumento doc = new EcfDocumento();
        doc.setEcfImpressora(Caixa.getInstancia().getImpressora());
        doc.setEcfDocumentoUsuario(1);
        String[] resp = ecf.enviar(EComando.ECF_NumCupom);
        doc.setEcfDocumentoCoo(Integer.valueOf(resp[1]));
        if (!tipo.equals("RV")) {
            resp = ecf.enviar(EComando.ECF_NumGNF);
            doc.setEcfDocumentoGnf(Integer.valueOf(resp[1]));
        }
        if (tipo.equals("RG")) {
            resp = ecf.enviar(EComando.ECF_NumGRG);
            doc.setEcfDocumentoGrg(Integer.valueOf(resp[1]));
        } else if (tipo.equals("CC")) {
            resp = ecf.enviar(EComando.ECF_NumCDC);
            doc.setEcfDocumentoCdc(Integer.valueOf(resp[1]));
        }
        doc.setEcfDocumentoTipo(tipo);
        resp = ecf.enviar(EComando.ECF_DataHora);
        Date data;
        try {
            data = new SimpleDateFormat("dd/MM/yy HH:mm:ss").parse(resp[1]);
        } catch (ParseException ex) {
            data = new Date();
        }
        doc.setEcfDocumentoData(data);
        // salva
        CoreService<EcfDocumento> service = new CoreService<>();
        service.salvar(doc);
    }

    @Override
    public void desfazer() throws OpenPdvException {
        // comando nao aplicavel.
    }
}
