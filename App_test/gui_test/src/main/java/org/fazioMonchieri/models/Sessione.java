/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

import java.util.Date;

public class Sessione {
    private Integer id;
    private String nome;
    private Date dataApertura;
    private Date dataChiusura;
    private TipoSessione tipoSessione;
    private TipoScrutinio tipoScrutinio;
    private Integer idGestore;
    
    public Sessione(Integer id, String nome, Date dataApertura, Date dataChiusura, TipoSessione tipoSessione, TipoScrutinio tipoScrutinio, Integer idGestore){
        this.id = id;
        this.nome = nome;
        this.dataApertura = dataApertura;
        this.dataChiusura = dataChiusura;
        this.tipoSessione = tipoSessione;
        this.tipoScrutinio = tipoScrutinio;
        this.idGestore = idGestore;
    }
    
    public Integer getId(){
        return id;
    }
    
    public String getNome(){
        return nome;
    }
    
    public Date getDataApertura(){
        return dataApertura;
    }
    
    public Date getDataChiusura(){
        return dataChiusura;
    }
    
    public TipoSessione getTipoSessione(){
        return tipoSessione;
    }
    
    public TipoScrutinio getTipoScrutinio(){
        return tipoScrutinio;
    }
    
    public Integer getIdGestore(){
        return idGestore;
    }
    
}
