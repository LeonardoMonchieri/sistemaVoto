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
import java.time.LocalDateTime;
import org.fazioMonchieri.utilities.Observer;


public class Gestore extends Utente implements Observer{
    
    private static Gestore uniqueInstance;
    
    public Gestore(Integer id, String username, String codiceFiscale){
        super(id, username, codiceFiscale);
    }
    
    public static Gestore getInstance(Integer id, String username, String codiceFiscale) {
        if(uniqueInstance == null) {
            uniqueInstance = new Gestore(id, username, codiceFiscale);
        }
        return uniqueInstance;
    }
    
    public static Gestore getInstance(){
        return uniqueInstance;
    }
    
    @Override
    public void update(String s) throws IOException {
        File f = new File("src/main/resources/logFiles/GestoreLogs.log");
        FileWriter fw = new FileWriter(f, true);
        BufferedWriter bw =  new BufferedWriter(fw);

        bw.write(LocalDateTime.now() + " - ");
        bw.write(Gestore.getInstance().toString() + " - ");
        bw.write(s);
        bw.write("\n");
        bw.close();
    }
  
}
