/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author franc
 */
public class Candidato {
    private String id;
    private String ruolo;
    private Persona persona;
    private Partito partito;
    
    public Candidato(){
        
    }
    
    public String getId(){
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
