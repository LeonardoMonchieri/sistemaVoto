/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.utilities;

import java.util.List;

public class SchedaElettoraleCategoricoOrdinale extends SchedaElettorale{
    private List<String> opzioni;
    
    public SchedaElettoraleCategoricoOrdinale(String quesito, List<String> opzioni){
        super(quesito);
        if(opzioni == null || opzioni.size() == 0) throw new IllegalArgumentException("Opzioni non valido.");
        else this.opzioni = opzioni;
        
    }
    
    public List<String> getOpzioni(){
        return opzioni;
    }
    
    @Override
    public String toString(){
        String s = super.toString() + "Opzioni:\n";
        for(int i=0;i<opzioni.size();i++){
            s+=(i+1)+"- "+opzioni.get(i)+"\n";
        }
        return s;
    }
}
