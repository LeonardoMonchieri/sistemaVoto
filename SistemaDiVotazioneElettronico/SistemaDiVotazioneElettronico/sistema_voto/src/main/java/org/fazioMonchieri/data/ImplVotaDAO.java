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
import org.fazioMonchieri.models.Elettore;
import org.fazioMonchieri.utilities.Observable;
import org.fazioMonchieri.utilities.Observer;

public class ImplVotaDAO implements VotaDAO, Observable{
    
    private final List<Observer> obs;
    private static ImplVotaDAO uniqueInstance;
    
    private ImplVotaDAO() {
        obs = new ArrayList<>();
        obs.add(Elettore.getInstance());
    }
    
    public static ImplVotaDAO getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new ImplVotaDAO();
        return uniqueInstance;
    }

    @Override
    public Boolean hasVoted(Integer elettoreId, Integer sessioneId) {
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\Download\\SistemaDiVotoElettronico.db");
            
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        try{
            String query = "SELECT * FROM Vota WHERE elettore = ? AND sessione = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, elettoreId);
            statement.setInt(2, sessioneId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                return true;
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return false;
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
