package org.fazioMonchieri.models;

import java.util.Date;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "partito")
public class Partito{

    public Partito() {

    }

    @DatabaseField
    private String nome;

    public String getNome() {
        return nome;
    }

    @DatabaseField
    private Date dataFondazione;

    public Date getDataFondazione() {
        return dataFondazione;
    }
}