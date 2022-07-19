package org.fazioMonchieri.utilities;

import java.util.List;

public class SchedaElettorale {
    private final String quesito;

    private final List<String> opzioni;

    public SchedaElettorale(String q, List<String> opt){
        if(q==null || q.length()==0) throw new IllegalArgumentException("il quesito della scheda non puó esser null");
        if(opt==null || opt.size()==0) throw new IllegalArgumentException("Opzioni non valide");

        quesito=q;
        opzioni=List.copyOf(opt);
        
    }

    public String getQuesito(){
        return quesito;
    }

    public List<String> getOpzioni(){
        return List.copyOf(opzioni);
    }

    @Override
    public String toString(){
        String s="quesito:\n";
        s+=quesito+"\n\n";
        s+="opzioni:\n";
        for(int i=0;i<opzioni.size();i++){
            s+=(i+1)+"-"+opzioni.get(i)+"\n";
        }
        return s;
    }
}
