/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

public class VotiPartito {
    private Integer idSessione;
    private Integer idPartito;
    private int numeroVoti;
    
    public VotiPartito(Integer idSessione, Integer idPartito, int numeroVoti){
        this.idSessione = idSessione;
        this.idPartito = idPartito;
        this.numeroVoti = numeroVoti;
    }

    public Integer getIdSessione() {
        return idSessione;
    }

    public Integer getIdPartito() {
        return idPartito;
    }
    
    public int getNumeroVoti() {
        return numeroVoti;
    }
}
