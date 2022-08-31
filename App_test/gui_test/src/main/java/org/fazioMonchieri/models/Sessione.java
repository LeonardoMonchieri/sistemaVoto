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
    private Boolean chiusa;
    private TipoSessione tipoSessione;
    private TipoScrutinio tipoScrutinio;
    private int elettori;
    private int votiTotali;
    private Integer idGestore;
    
    public Sessione(Integer id, String nome, Date dataApertura, Boolean chiusa, TipoSessione tipoSessione, TipoScrutinio tipoScrutinio, int elettori, int votiTotali, Integer idGestore){
        this.id = id;
        this.nome = nome;
        this.dataApertura = dataApertura;
        this.chiusa = chiusa;
        this.tipoSessione = tipoSessione;
        this.tipoScrutinio = tipoScrutinio;
        this.elettori = elettori;
        this.votiTotali = votiTotali;
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
    
    public Boolean getChiusa(){
        return chiusa;
    }
    
    public TipoSessione getTipoSessione(){
        return tipoSessione;
    }
    
    public TipoScrutinio getTipoScrutinio(){
        return tipoScrutinio;
    }
    
    public int getElettori(){
        return elettori;
    }
    
    public int getVotiTotali(){
        return votiTotali;
    }
    
    public Integer getIdGestore(){
        return idGestore;
    }
    
}
