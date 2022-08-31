/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

public class Referendum {
    private final Integer id;
    private final Integer idSessione;
    private final String quesito;
    private int votiSi;
    private int votiNo;
    private final boolean quorum;
    
    public Referendum(Integer id, Integer idSessione, String quesito, boolean quorum, int votiSi, int votiNo){
        this.id = id;
        this.idSessione = idSessione;
        this.quesito = quesito;
        this.quorum = quorum;
        this.votiSi = votiSi;
        this.votiNo = votiNo;
    }
    
    public Integer getId(){
        return id;
    }
    
    public Integer getIdSessione() {
        return idSessione;
    }

    public String getQuesito() {
        return quesito;
    }
    
    public Boolean hasQuorum(){
        return quorum;
    }

    public int getVotiSi() {
        return votiSi;
    }

    public int getVotiNo() {
        return votiNo;
    }
}
