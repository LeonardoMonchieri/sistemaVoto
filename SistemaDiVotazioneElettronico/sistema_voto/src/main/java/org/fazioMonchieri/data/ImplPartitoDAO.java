/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.fazioMonchieri.models.Elettore;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.utilities.Observable;
import org.fazioMonchieri.utilities.Observer;

public class ImplPartitoDAO implements PartitoDAO, Observable{
    
    private final List<Observer> obs;
    private static ImplPartitoDAO uniqueInstance;
    
    private ImplPartitoDAO(){
        obs = new ArrayList<>();
        obs.add(Elettore.getInstance());
    }
    
    public static ImplPartitoDAO getInstance() {
        if(uniqueInstance == null)
            uniqueInstance = new ImplPartitoDAO();
        return uniqueInstance;
    }

    @Override
    public Boolean isPartito(Integer id){
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        String query = "SELECT * FROM Partito WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state = connection.prepareStatement(query);
            state.setInt(1, id);
            ResultSet r = state.executeQuery();
            if(r != null){
                connection.close();
                return true;
            }
            connection.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public void createPartito(String nome, Date date){
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        String query = "INSERT INTO Partito ('nome', 'dataFondazione') VALUES (?, ?);";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state = connection.prepareStatement(query);
            state.setString(1, nome);
            state.setDate(2, date);
            state.executeUpdate();
            connection.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public List<Partito> getPartiti(){
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        List<Partito> partiti = new ArrayList<>();
        try{
            String query = "SELECT * FROM Partito;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Partito p = new Partito(resultSet.getInt(1), resultSet.getString(2), resultSet.getDate(3));
                partiti.add(p);
            }
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return partiti;
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
