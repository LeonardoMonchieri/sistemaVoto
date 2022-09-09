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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fazioMonchieri.models.Elettore;
import org.fazioMonchieri.models.Referendum;
import org.fazioMonchieri.utilities.Observable;
import org.fazioMonchieri.utilities.Observer;

public class ImplReferendumDAO implements ReferendumDAO, Observable{

    private final List<Observer> obs;
    private static ImplReferendumDAO uniqueInstance;
    
    private ImplReferendumDAO() {
        obs = new ArrayList<>();
        obs.add(Elettore.getInstance());
    }
    
    public static ImplReferendumDAO getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new ImplReferendumDAO();
        return uniqueInstance;
    }
    
    @Override
    public void createReferendum(Integer id, String quesito){
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        try{
            String query = "INSERT INTO Referendum ('sessione', 'quesito', 'votiSi', 'votiNo') VALUES (?, ?, 0, 0);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.setString(2, quesito);
            statement.executeUpdate();
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }
    
    @Override
    public void votaReferendum(Integer id, Boolean voto) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        if(voto){
            try{
                String query = "UPDATE Referendum SET votiSi = votiSi + 1 WHERE sessione = ?;";
                PreparedStatement state = connection.prepareStatement(query);
                state.setInt(1, id);
                state.executeUpdate();
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        else{
            try{
                String query = "UPDATE Referendum SET votiNo = votiNo + 1 WHERE sessione = ?;";
                PreparedStatement state = connection.prepareStatement(query);
                state.setInt(1, id);
                state.executeUpdate();
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        try {
            getInstance().notifyObservers(" [Votazione per il referendum della sessione: " + id + "]");
        } catch (IOException ex) {
            Logger.getLogger(ImplReferendumDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Referendum getReferendum(Integer id){

        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        String query = "SELECT * FROM Referendum WHERE sessione = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            ResultSet resultSet = state.executeQuery();
            if(resultSet != null){
                Referendum r = new Referendum(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4));
                connection.close();
                return r;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
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
