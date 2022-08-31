/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.time.LocalDateTime;
import org.fazioMonchieri.utilities.Observer;

public class Elettore extends Utente implements Observer{
    
    private static Elettore uniqueInstance;
    private final Date dataNascita;
    
    public Elettore(Integer id, String username, String codiceFiscale, Date dataNascita){
        super(id, username, codiceFiscale);
        this.dataNascita = dataNascita;
    }
    
    public static Elettore getInstance(Integer id, String username, String codiceFiscale, Date dataNascita) {
        if(uniqueInstance == null)
            uniqueInstance = new Elettore(id, username, codiceFiscale, dataNascita);
        return uniqueInstance;
    }
    
    public static Elettore getInstance(){
        return uniqueInstance;
    }
    
    public boolean isMaggiorenne(){
        Date now = new Date();
        if(now.getYear() - dataNascita.getYear() > 18)
            return true;
        else if(now.getYear() - dataNascita.getYear() < 18)
            return false;

        if(now.getMonth() - dataNascita.getMonth() > 0)
            return true;
        if(now.getMonth() - dataNascita.getMonth() < 0)
            return false;

        return now.getDay() - dataNascita.getDay() >= 0;
    }
    
    @Override
    public void update(String s) throws IOException {
        File f = new File("src/main/resources/logFiles/elettoreLogs.log");
        FileWriter fw = new FileWriter(f, true);
        BufferedWriter bw =  new BufferedWriter(fw);

        bw.write(LocalDateTime.now() + " - ");
        bw.write(Elettore.getInstance().toString() + " - ");
        bw.write(s);
        bw.write("\n");
        bw.close();
    }
    
}
