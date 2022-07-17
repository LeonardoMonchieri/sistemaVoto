package org.fazioMonchieri.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "votipartiti")
public class VotiPartiti{

    public VotiPartiti(Sessione sessione, Partito partito) {
        this.sessione = sessione;
        this.partito = partito;
    }

    @DatabaseField
    private Sessione sessione;

    public Sessione getSessione() {
        return sessione;
    }

    @DatabaseField
    private Partito partito;

    public Partito getPartito() {
        return partito;
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