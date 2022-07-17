package org.fazioMonchieri.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "referendum")
public class Referendum{

    public Referendum() {

    }

    @DatabaseField
    private Sessione sessione;

    public Sessione getSessione() {
        return sessione;
    }

    @DatabaseField
    private String quesito;

    public String getQuesito() {
        return quesito;
    }

    @DatabaseField
    private int votiSi;

    public int getVotiSi() {
        return votiSi;
    }

    @DatabaseField
    private int votiNo;

    public int getVotiNo() {
        return votiNo;
    }

    public void setVoto(boolean voto){
        if(voto)
            votiSi++;
        else votiNo++;
    }
   
}