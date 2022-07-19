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
public class Referendum {
    private Sessione sessione;
    private String quesito;
    private int votiSi;
    private int votiNo;
    
    public Referendum(){
        
    }
    
    public Sessione getSessione() {
        return sessione;
    }

    public String getQuesito() {
        return quesito;
    }

    public int getVotiSi() {
        return votiSi;
    }

    public int getVotiNo() {
        return votiNo;
    }
}
