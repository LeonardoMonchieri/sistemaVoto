/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

public class VotiPartito {
    private Sessione sessione;
    private Partito partito;
    private int numeroVoti;

    public Sessione getSessione() {
        return sessione;
    }

    public Partito getPartito() {
        return partito;
    }
    
    public int getNumeroVoti() {
        return numeroVoti;
    }
}
