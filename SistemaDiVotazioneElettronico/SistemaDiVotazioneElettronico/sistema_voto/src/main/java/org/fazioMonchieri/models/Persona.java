/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

import java.util.Date;

public class Persona {
    private String codiceFiscale;
    private Boolean sesso;
    private String nome;
    private String cognome;
    private Date dataNascita;
    private String luogoNascita;
    
    public Persona(String codiceFiscale, Boolean sesso, String nome, String cognome, Date dataNascita, String luogoNascita){
        this.codiceFiscale = codiceFiscale;
        this.sesso = sesso;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = new java.util.Date(dataNascita.getTime());
        this.luogoNascita = luogoNascita;
    }
    
    public String getCodiceFiscale(){
        return codiceFiscale;
    }
    
    public Boolean getSesso(){
        return sesso;
    }
    
    public String getNome(){
        return nome;
    }
    
    public String getCognome(){
        return cognome;
    }
    
    public Date getdataNascita(){
        return dataNascita;
    }
    
    public String getLuogoNascita(){
        return luogoNascita;
    }
    
    public String getFullName(){
        return nome + " " + cognome;
    }
}
