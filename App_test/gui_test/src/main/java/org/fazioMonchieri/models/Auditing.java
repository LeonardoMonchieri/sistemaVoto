/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

import java.sql.Time;

public class Auditing {
    private Time time;
    private String azione;
    private Persona persona;
    private Seggio seggio;
    
    public Auditing(){
        
    }
    
    public Time getTime(){
        return time;
    }
    
    public String getAzione(){
        return azione;
    }
    
    public Persona getPersona(){
        return persona;
    }
    
    public Seggio getSeggio(){
        return seggio;
    }
    
}
