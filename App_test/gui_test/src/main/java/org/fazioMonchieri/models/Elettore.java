package org.fazioMonchieri.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "elettore")
public class Elettore{

    public Elettore() {

    }

    @DatabaseField
    private String id;

    public String getId() {
        return id;
    }

    @DatabaseField
    private Persona persona;

    public Persona getPersona() {
        return persona;
    }

}