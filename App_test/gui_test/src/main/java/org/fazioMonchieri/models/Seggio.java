package org.fazioMonchieri.models;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Seggio {
    private String id;
    private String via;
    private String CAP;
    private String citta;
    private String provincia;
    
    public Seggio(){
        
    }
    
    public String getId(){
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
