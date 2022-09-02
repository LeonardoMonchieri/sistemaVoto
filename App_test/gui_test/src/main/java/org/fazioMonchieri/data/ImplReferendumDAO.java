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
import org.fazioMonchieri.models.Sessione;
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
    public void createReferndum(Integer id, String quesito, Boolean quorum){
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        try{
            String query = "INSERT INTO Referendum (idSessione, quesito, quorum, votiSi, votiNo) VALUES (?, ?, ?, 0, 0)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.setString(2, quesito);
            statement.setBoolean(3, quorum);
            ResultSet resultSet = statement.executeQuery();
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }
    
    @Override
    public void votaReferendum(Integer id, Integer elettoreId, Boolean voto) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
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
