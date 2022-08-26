/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

import java.sql.Timestamp;

public class Vota {
    private Elettore elettore;
    private Sessione sessione;
    private Timestamp orarioVotazione;
    
    public Vota(){
        
    }
    
    public Elettore getElettore(){
        return elettore;
    }
    
    public Sessione getSessione(){
        return sessione;
    }
    
    public Timestamp getOrarioVotazione(){
        return orarioVotazione;
    }
}
