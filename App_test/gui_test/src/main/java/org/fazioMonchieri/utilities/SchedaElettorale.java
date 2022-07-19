/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.utilities;

public abstract class SchedaElettorale {
    protected final String quesito;
    
    public SchedaElettorale(String quesito){
        if(quesito == null || quesito.length() == 0) throw new IllegalArgumentException("Il quesito non valido.");
        else this.quesito = quesito;
    }

    public String getQuesito(){
        return quesito;
    }

    @Override
    public String toString(){
        return "Quesito:\n" + quesito + "\n\n";
    }
}