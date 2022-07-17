package org.fazioMonchieri.models;

import java.security.Timestamp;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "vota")
public class Vota{

    public Vota() {

    }

    @DatabaseField
    private Elettore elettore;

    public Elettore getElettore() {
        return elettore;
    }

    @DatabaseField
    private Sessione sessione;

    public Sessione getSessione() {
        return sessione;
    }

    @DatabaseField
    private Timestamp orario;

    public Timestamp getOrario() {
        return orario;
    }
   
}