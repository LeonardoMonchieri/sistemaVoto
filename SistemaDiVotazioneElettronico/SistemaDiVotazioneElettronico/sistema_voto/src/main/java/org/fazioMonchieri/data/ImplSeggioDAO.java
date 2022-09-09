/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Seggio;
import org.fazioMonchieri.utilities.Observable;
import org.fazioMonchieri.utilities.Observer;

public class ImplSeggioDAO implements SeggioDAO, Observable{
    
    private final List<Observer> obs;
    private static ImplSeggioDAO uniqueInstance;
    
    private ImplSeggioDAO() {
        obs = new ArrayList<>();
        obs.add(Gestore.getInstance());
    }
    
    public static ImplSeggioDAO getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new ImplSeggioDAO();
        return uniqueInstance;
    }
    
    @Override
    public Seggio loginSeggio(Integer id, String password) throws IOException{
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\Download\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        String query = "SELECT * FROM Seggio WHERE id = ? AND password = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            state.setString(2, password);
            ResultSet r = state.executeQuery();
            if(r!=null){
                Seggio seggio =  new Seggio(r.getInt("id"), r.getString("via"), r.getString("CAP"), r.getString("citta"), r.getString("provincia"));
                connection.close();
                return seggio;
            }
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        getInstance().notifyObservers(" [Login del seggio.] ");
        return null;
    }
    
    @Override
    public void subscribe(Observer o){ 
        uniqueInstance.obs.add(o); 
    }

    @Override
    public void unsubcribe(Observer o){ 
        uniqueInstance.obs.remove(o); 
    }

    @Override
    public void notifyObservers(String s) throws IOException {
        for(Observer o : uniqueInstance.obs)
            if(o != null)
                o.update(s);
    }
    
}
