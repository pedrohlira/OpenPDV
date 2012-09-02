package br.com.openpdv.modelo.sistema;

import br.com.openpdv.modelo.core.Dados;
import br.com.openpdv.modelo.core.ELetra;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modelo que representa um usuario no sistema.
 *
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "sis_usuario")
@XmlRootElement
public class SisUsuario extends Dados implements Serializable {

    @Id
    @Column(name = "sis_usuario_id")
    private Integer sisUsuarioId;
    @Column(name = "sis_usuario_login")
    private String sisUsuarioLogin;
    @Column(name = "sis_usuario_senha")
    private String sisUsuarioSenha;
    @Column(name = "sis_usuario_desconto")
    private int sisUsuarioDesconto;
    @Column(name = "sis_usuario_ativo")
    private boolean sisUsuarioAtivo;
    @Column(name = "sis_usuario_gerente")
    private boolean sisUsuarioGerente;

    /**
     * Construtor padrao
     */
    public SisUsuario() {
        this(0);
    }

    /**
     * Construtor padrao passando o id
     *
     * @param sisUsuarioId o id do usuario
     */
    public SisUsuario(Integer sisUsuarioId) {
        super("SisUsuario", "sisUsuarioId", "sisUsuarioLogin");
        this.sisUsuarioId = sisUsuarioId;
        this.setTipoLetra(ELetra.GRANDE);
    }

    @Override
    public Integer getId() {
        return this.sisUsuarioId;
    }

    @Override
    public void setId(Integer id) {
        this.sisUsuarioId = id;
    }

    // GETs e SETs

    public Integer getSisUsuarioId() {
        return sisUsuarioId;
    }

    public void setSisUsuarioId(Integer sisUsuarioId) {
        this.sisUsuarioId = sisUsuarioId;
    }

    public String getSisUsuarioLogin() {
        return sisUsuarioLogin;
    }

    public void setSisUsuarioLogin(String sisUsuarioLogin) {
        this.sisUsuarioLogin = sisUsuarioLogin;
    }

    public String getSisUsuarioSenha() {
        return sisUsuarioSenha;
    }

    public void setSisUsuarioSenha(String sisUsuarioSenha) {
        this.sisUsuarioSenha = sisUsuarioSenha;
    }

    public int getSisUsuarioDesconto() {
        return sisUsuarioDesconto;
    }

    public void setSisUsuarioDesconto(int sisUsuarioDesconto) {
        this.sisUsuarioDesconto = sisUsuarioDesconto;
    }

    public boolean isSisUsuarioAtivo() {
        return sisUsuarioAtivo;
    }

    public void setSisUsuarioAtivo(boolean sisUsuarioAtivo) {
        this.sisUsuarioAtivo = sisUsuarioAtivo;
    }

    public boolean isSisUsuarioGerente() {
        return sisUsuarioGerente;
    }

    public void setSisUsuarioGerente(boolean sisUsuarioGerente) {
        this.sisUsuarioGerente = sisUsuarioGerente;
    }
}
