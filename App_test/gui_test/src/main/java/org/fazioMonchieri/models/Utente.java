/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

public abstract class Utente {
    private final Integer id;
    private final String username;
    private final String codiceFiscale;
    
     public Utente(Integer id, String username, String codiceFiscale){
        this.id = id;
        this.username = username;
        this.codiceFiscale = codiceFiscale;
    }
    
    public Integer getId(){
        return id;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getCodiceFiscale(){
        return codiceFiscale;
    }
}
