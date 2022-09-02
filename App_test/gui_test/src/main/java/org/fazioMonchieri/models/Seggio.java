package org.fazioMonchieri.models;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Seggio {
    private Integer id;
    private String via;
    private String CAP;
    private String citta;
    private String provincia;
    
    public Seggio(Integer id, String via, String cap, String citta, String provincia){
        this.id = id;
        this.via = via;
        this.CAP = cap;
        this.citta = citta;
        this.provincia = provincia;
    }
    
    public Integer getId(){
        return id;
    }
    
    public String getVia(){
        return via;
    }
    
    public String getCAP(){
        return CAP;
    }
    
    public String getCitta(){
        return citta;
    }
    
    public String getProvincia(){
        return provincia;
    }
    
}
