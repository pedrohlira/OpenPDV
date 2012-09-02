package br.com.openpdv.modelo.ecf;

import java.io.Serializable;

public enum ENotaStatus implements Serializable {

    /**
     * Enquanto autoriza a nfe.
     */
    AUTORIZANDO(1),
    /**
     * Após autorizado a nfe.
     */
    AUTORIZADO(2),
    /**
     * Enquando cancela a nfe.
     */
    CANCELANDO(3),
    /**
     * Após cancelado a nfe.
     */
    CANCELADO(4),
    /**
     * Enquando inutiliza a nfe.
     */
    INUTILIZANDO(5),
    /**
     * Após inutilizado a nfe.
     */
    INUTILIZADO(6),
    /**
     * Enquando tem erro na nfe.
     */
    ERRO(7),
    /**
     * NFe em contigencia de FS-DA.
     */
    FS_DA(8);
    
    private int id;

    private ENotaStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
