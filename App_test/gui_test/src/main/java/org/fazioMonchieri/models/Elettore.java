/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

/**
 *
 * @author franc
 */
public class Elettore {
    private String id;
    private Persona persona;
    private String username;
    private String password;
    
    public Elettore(){
        
    }
    
    public String getId(){
        return id;
    }
    
    public Persona getPersona(){
        return persona;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
}
