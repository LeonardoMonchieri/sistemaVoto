/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

public class Candidato {
    private Integer id;
    private String ruolo;
    private Persona persona;
    private Partito partito;
    
    public Candidato(Integer id, String ruolo, Persona persona, Partito partito){
        this.id = id;
        this.ruolo = ruolo;
        this.persona = persona;
        this.partito = partito;
    }
    
    public Integer getId(){
        return id;
    }
    
    public String getRuolo(){
        return ruolo;
    }
    
    public Persona getPersona(){
        return persona;
    }
    
    public Partito getPartito(){
        return partito;
    }
}
