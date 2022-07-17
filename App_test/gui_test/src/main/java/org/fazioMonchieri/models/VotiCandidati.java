package org.fazioMonchieri.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "voticandidati")
public class VotiCandidati{

    public VotiCandidati(Sessione sessione, Candidato candidato) {
        this.sessione = sessione;
        this.candidato = candidato;
    }

    @DatabaseField
    private Sessione sessione;

    public Sessione getSessione() {
        return sessione;
    }

    @DatabaseField
    private Candidato candidato;

    public Candidato getCandidato() {
        return candidato;
    }

    @DatabaseField
    private int numeroVoti;

    public int getNumeroVoti() {
        return numeroVoti;
    }

    public void setNumeroVoti(){
        numeroVoti++;
    }
   
}