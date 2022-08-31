/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

import java.util.Date;

public class Partito {
    private Integer id;
    private String nome;
    private Date dataFondazione;
    
    public Partito(Integer id, String nome, Date dataFondazione){
        this.id = id;
        this.nome = nome;
        this.dataFondazione = dataFondazione;
    }
    
    public Integer getId(){
        return id;
    }
    
    public String getNome(){
        return nome;
    }
    
    public Date getDataFondazione(){
        return dataFondazione;
    }
}
