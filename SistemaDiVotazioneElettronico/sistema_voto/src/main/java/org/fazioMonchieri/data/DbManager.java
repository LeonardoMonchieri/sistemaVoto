/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;
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

    /**
     * @param id
     * @param pw
     * @return
     */
    public Sessione loginSessione(Integer id, String pw){
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        Sessione sessione = null;
        
        String query = "SELECT * FROM Sessione WHERE id = ? AND password = ?";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            state.setString(2, pw);
            ResultSet r = state.executeQuery();
            TipoSessione tipoSessione = TipoSessione.valueOf(r.getString("tipoSessione"));
            TipoScrutinio tipoScrutinio = TipoScrutinio.valueOf(r.getString("tipoScrutinio"));
                
            sessione = new Sessione(r.getInt("id"), r.getString("nome"), Date.valueOf(r.getString("dataApertura")), Date.valueOf(r.getString("dataChiusura")), tipoSessione, tipoScrutinio, r.getInt("gestore"));
            connection.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return sessione;
    }
    
    /**
     * @throws IOException
     */
    public void resetVotazioni() throws IOException {
        try{
            //apro connessione
            try { 
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ImplElettoreDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
            
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