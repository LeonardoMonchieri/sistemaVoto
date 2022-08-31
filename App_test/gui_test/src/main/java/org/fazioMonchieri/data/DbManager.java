/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.utilities.Observable;
import org.fazioMonchieri.utilities.Observer;

public class DbManager implements Observable{
    
    private static DbManager uniqueInstance;
    private final List<Observer> obs;
    
    public DbManager(){
        obs = new ArrayList<>();
        obs.add(Gestore.getInstance());
    }

    public static DbManager getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new DbManager();
        return uniqueInstance;
    }
    
    public void resetVotazioni() throws IOException {
        try{
            //apro connessione
            try { 
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ImplElettoreDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            
            String query = "SET SQL_SAFE_UPDATES=0; delete from Vota; delete from Referendum; delete from Sessione; delete from VotiPartiti; delete from VotiCandidati;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
            
            connection.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        getInstance().notifyObservers("[Cancellazione di tutte le votazioni.]");
    }
    
    @Override
    public void subscribe(Observer o) {
        this.obs.add(o);
    }

    @Override
    public void unsubcribe(Observer o) {
        this.obs.remove(o);
    }

    @Override
    public void notifyObservers(String s) throws IOException {
        for(Observer o : this.obs)
                o.update(s);
    }

}