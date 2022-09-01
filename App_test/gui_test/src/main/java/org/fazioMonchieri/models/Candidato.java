/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

public class Candidato {
    private final Integer id;
    private final String ruolo;
    private final String codiceFiscale;
    private final Integer idPartito;
    
    public Candidato(Integer id, String ruolo, String codiceFiscale, Integer idPartito){
        this.id = id;
        this.ruolo = ruolo;
        this.codiceFiscale = codiceFiscale;
        this.idPartito = idPartito;
    }
    
    public Integer getId(){
        return id;
    }
    
    public String getRuolo(){
        return ruolo;
    }
    
    public String getCodiceFiscale(){
        return codiceFiscale;
    }
    
    public Integer getIdPartito(){
        return idPartito;
    }
}
