/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

import java.util.Date;

/**
 *
 * @author franc
 */
public class Partito {
    private String id;
    private String nome;
    private Date dataFondazione;
    
    public Partito(){
        
    }
    
    public String getId(){
        return id;
    }
    
    public String getNome(){
        return nome;
    }
    
    public Date getDataFondazione(){
        return dataFondazione;
    }
}
