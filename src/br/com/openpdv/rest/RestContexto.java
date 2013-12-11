package br.com.openpdv.rest;

import br.com.openpdv.modelo.ecf.*;
import br.com.openpdv.modelo.produto.ProdComposicao;
import br.com.openpdv.modelo.produto.ProdEmbalagem;
import br.com.openpdv.modelo.produto.ProdGrade;
import br.com.openpdv.modelo.produto.ProdGradeTipo;
import br.com.openpdv.modelo.produto.ProdPreco;
import br.com.openpdv.modelo.produto.ProdProduto;
import br.com.openpdv.modelo.sistema.*;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;

/**
 * Classe que habilita o contexto do JAXB usando a implementacao do EclipseLink
 * moxy.
 *
 * @author Pedro H. Lira
 */
@Provider
public class RestContexto extends MOXyJsonProvider implements ContextResolver<JAXBContext> {

    private JAXBContext context;
    private Class[] types = {SisEmpresa.class, SisEstado.class, SisMunicipio.class, SisUsuario.class, SisCliente.class,
        EcfPagamentoTipo.class, ProdEmbalagem.class, ProdProduto.class, ProdPreco.class, ProdComposicao.class, ProdGrade.class, ProdGradeTipo.class,
        EcfImpressora.class, EcfDocumento.class, EcfNota.class, EcfNotaProduto.class, EcfNotaEletronica.class, ENotaStatus.class,
        EcfZ.class, EcfZTotais.class, EcfVenda.class, EcfVendaProduto.class, EcfPagamentoTipo.class, EcfPagamento.class,
        EcfPagamentoParcela.class, EcfPagamentoTotais.class, EcfTroca.class, EcfTrocaProduto.class};

    /**
     * Construtor padrao.
     *
     * @throws Exception dispara caso nao consiga criar.
     */
    public RestContexto() throws Exception {
        this.context = JAXBContextFactory.createContext(types, null);
    }

    /**
     * Recupera o contexto atual da classe informada.
     *
     * @param classe o tipo de classe informada.
     * @return o contexto atual.
     */
    @Override
    public JAXBContext getContext(Class<?> classe) {
        return context;
    }
}
