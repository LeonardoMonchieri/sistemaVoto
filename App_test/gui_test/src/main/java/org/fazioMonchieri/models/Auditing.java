package org.fazioMonchieri.models;

import java.sql.Timestamp;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "auditing")
public class Auditing{

    public Auditing() {

    }

    @DatabaseField
    private String id;

    public String getId() {
        return id;
    }

    @DatabaseField
    private Timestamp time;

    public Timestamp getTime() {
        return time;
    }

    @DatabaseField
    private String azione;

    public String getAzione() {
        return azione;
    }

    @DatabaseField
    private String indirizzoIp;

    public String getIndirizzoIp() {
        return indirizzoIp;
    }

    @DatabaseField
    private Persona persona;

    public Persona getPersona() {
        return persona;
    }

}