package org.fazioMonchieri.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "candidato")
public class Candidato{

    public Candidato() {

    }

    @DatabaseField
    private String id;

    public String getId() {
        return id;
    }

    @DatabaseField
    private String ruolo;

    public String getRuolo() {
        return ruolo;
    }

    @DatabaseField
    private Partito partito;

    public Partito getPartito() {
        return partito;
    }

    @DatabaseField
    private Persona persona;

    public Persona getPersona() {
        return persona;
    }    
}