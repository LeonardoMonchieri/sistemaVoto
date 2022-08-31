/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

import java.sql.Timestamp;

public class Vota {
    private Integer idElettore;
    private Integer idSessione;
    private Timestamp orarioVotazione;
    
    public Vota(Integer idElettore, Integer idSessione, Timestamp orarioVotazione){
        this.idElettore = idElettore;
        this.idSessione = idSessione;
        this.orarioVotazione = orarioVotazione;
    }
    
    public Integer getIdElettore(){
        return idElettore;
    }
    
    public Integer getIdSessione(){
        return idSessione;
    }
    
    public Timestamp getOrarioVotazione(){
        return orarioVotazione;
    }
}
