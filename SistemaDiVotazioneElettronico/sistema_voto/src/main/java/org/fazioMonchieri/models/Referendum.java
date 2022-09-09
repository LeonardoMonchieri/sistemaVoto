/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

public class Referendum {
    private final Integer idSessione;
    private final String quesito;
    private int votiSi;
    private int votiNo;
    
    public Referendum(Integer idSessione, String quesito, int votiSi, int votiNo){
        this.idSessione = idSessione;
        this.quesito = quesito;
        this.votiSi = votiSi;
        this.votiNo = votiNo;
    }
    
    public Integer getIdSessione() {
        return idSessione;
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
