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
public class Gestore {
    private String id;
    private Persona persona;
    private String username;
    private String password;
    
    public Gestore(String id, String username, String password, Persona persona){
        this.id = id;
        this.username = username;
        this.password = password;
        this.persona = persona;
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
