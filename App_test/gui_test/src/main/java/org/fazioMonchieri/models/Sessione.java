package org.fazioMonchieri.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "sessione")
public class Sessione{

    @DatabaseField
    private String id;

    @DatabaseField
    private Date data;

    @DatabaseField
    private TipoSessione tipologiaSessione;

    @DatabaseField
    private TipoScrutinio tipologiaScrutinio;

    @DatabaseField
    private Boolean chiusa;

    @DatabaseField
    private String password;

    @DatabaseField
    private Gestore gestore;

    @DatabaseField
    private int elettori;

    @DatabaseField
    private int votiTotali;

    public Sessione(String sessionId, TipoSessione tipoSessione, TipoScrutinio TipoScrutinio, String password, Gestore gestore){
        id = sessionId;
        data = new Date();
        tipologiaSessione = tipoSessione;
        tipologiaScrutinio = TipoScrutinio;
        this.password = password;
        chiusa = null;
        this.gestore = gestore;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return data;
    }

    public TipoSessione getTipologiaSessione() {
        return tipologiaSessione;
    }

    public TipoScrutinio getTipologiaScrutinio() {
        return tipologiaScrutinio;
    }

    public boolean getChiusa() {
        return chiusa;
    }

    public void open(){
        chiusa = true;
    }

    public void close(){
        chiusa = false;
    }

    public String getPassword() {
        return password;
    }

    public Gestore getGestore() {
        return gestore;
    }

    public int getElettori() {
        return elettori;
    }

    public int getVotiTotali() {
        return votiTotali;
    }
}