/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

import java.util.Date;

public class Sessione {
    private String id;
    private String nome;
    private Date dataApertura;
    private Boolean chiusa;
    private String password;
    private TipoSessione tipoSessione;
    private TipoScrutinio tipoScrutinio;
    private int elettori;
    private int votiTotali;
    private Gestore gestore;
    
    public Sessione(String id, String nome, Date dataApertura, String password, TipoSessione tipoSessione, TipoScrutinio tipoScrutinio, int elettori, Gestore gestore){
        this.id = id;
        this.nome = nome;
        this.dataApertura = dataApertura;
        chiusa = null;
        this.password = password;
        this.tipoSessione = tipoSessione;
        this.tipoScrutinio = tipoScrutinio;
        this.elettori = elettori;
        votiTotali = 0;
        this.gestore = gestore;
    }
    
    public String getId(){
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
    
    public String getPassword(){
        return password;
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
    
    public Gestore getGestore(){
        return gestore;
    }
    
    public void setOpen(){
        chiusa = false;
    }
    
    public void setClose(){
        chiusa = true;
    }
    
}
