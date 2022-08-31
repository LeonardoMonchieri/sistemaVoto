/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

public class VotiCandidato {
    private Integer idSessione;
    private Integer idCandidato;
    private int numeroVoti;
    
    public VotiCandidato(Integer idSessione, Integer idCandidato, int numeroVoti){
        this.idSessione = idSessione;
        this.idCandidato = idCandidato;
        this.numeroVoti = numeroVoti;
    }

    public Integer getIdSessione() {
        return idSessione;
    }

    public Integer getIdCandidato() {
        return idCandidato;
    }
    
    public int getNumeroVoti() {
        return numeroVoti;
    }
}
