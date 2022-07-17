package org.fazioMonchieri.models;

import java.util.Date;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "persona")
public class Persona{

    public Persona() {

    }

    @DatabaseField
    private String codiceFiscale;

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    @DatabaseField
    private boolean sesso;

    public boolean getSesso() {
        return sesso;
    }

    @DatabaseField
    private String cognome;

    public String getCognome() {
        return cognome;
    }

    @DatabaseField
    private Date dataNascita;

    public Date getDataNascita() {
        return dataNascita;
    }

    @DatabaseField
    private String luogoNascita;

    public String getLuogoNascita() {
        return luogoNascita;
    }

    @DatabaseField
    private String username;

    public String getUsername() {
        return username;
    }

    @DatabaseField
    private static String password;

    public static String getPassword() {
        return password;
    }
}