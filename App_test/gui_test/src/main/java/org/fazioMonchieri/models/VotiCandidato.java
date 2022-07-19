/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

/**
 *
 * @author franc
 */
public class VotiCandidato {
    private Sessione sessione;
    private Candidato candidato;
    private int numeroVoti;

    public Sessione getSessione() {
        return sessione;
    }

    public Candidato getCandidato() {
        return candidato;
    }
    
    public int getNumeroVoti() {
        return numeroVoti;
    }
}
