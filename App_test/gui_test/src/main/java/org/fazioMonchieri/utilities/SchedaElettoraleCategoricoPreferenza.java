/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.utilities;

import java.util.List;
import java.util.Map;

public class SchedaElettoraleCategoricoPreferenza extends SchedaElettorale{
    private List<String> partiti;
    private Map<String, List<String>> candidati;
    
    public SchedaElettoraleCategoricoPreferenza(String quesito, List<String> partiti, Map<String, List<String>> candidati){
        super(quesito);
        if(partiti == null || partiti.size() == 0 || candidati == null || candidati.size() == 0) throw new IllegalArgumentException("Opzioni non valido.");
        else{
            this.partiti = partiti;
            this.candidati = candidati;
        }
        
    }
    
    public List<String> getPartiti(){
        return partiti;
    }
    
    public List<String> getCandidati(String nomePartito){
        return candidati.get(nomePartito);
    }
    
    public String stampaCandidati(String nomePartito){
        List<String> c = candidati.get(nomePartito);
        String s = "Opzioni partiti:\n";
        for(int i=0;i<c.size();i++){
            s+=(i+1)+"- "+c.get(i)+"\n";
        }
        return s;
    }
    
    @Override
    public String toString(){
        String s = super.toString() + "Opzioni partiti:\n";
        for(int i=0;i<partiti.size();i++){
            s+=(i+1)+"- "+partiti.get(i)+"\n";
        }
        return s;
    }
}
