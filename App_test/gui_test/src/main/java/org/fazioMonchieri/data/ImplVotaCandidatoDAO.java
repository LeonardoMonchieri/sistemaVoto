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
import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.utilities.Observable;
import org.fazioMonchieri.utilities.Observer;

public class ImplVotaCandidatoDAO implements VotaCandidatoDAO, Observable{
    
    private final List<Observer> obs;
    private static ImplVotaCandidatoDAO uniqueInstance;
    
    private ImplVotaCandidatoDAO() {
        obs = new ArrayList<>();
        obs.add(Gestore.getInstance());
    }
    
    public static ImplVotaCandidatoDAO getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new ImplVotaCandidatoDAO();
        return uniqueInstance;
    }
    
    @Override
    public void createVotaCandidato(Integer idSessione, Integer idCandidato) {
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        try{
            String query = "INSERT INTO VotiCandidato (sessione, candidato, numeroVoti) VALUES (?, ?, 0)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idSessione);
            statement.setInt(2, idCandidato);
            ResultSet resultSet = statement.executeQuery();
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        
        try {
            getInstance().notifyObservers("[Creazione riga database VotiCandidato]");
        } catch (IOException ex) {
            Logger.getLogger(ImplSessioneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void votaCandidatoOrdinale(Integer id, Integer elettoreId, List<Candidato> candidati) throws IOException{
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        try{
            for(int i = candidati.size(); i > 0; i--){
                String query = "UPDATE VotiCandidati SET numeroVoti = numeroVoti + ? WHERE sessione = ? AND candidato = ?;";
                PreparedStatement state = connection.prepareStatement(query);
                state.setInt(1, i);
                state.setInt(2, id);
                state.setInt(3, candidati.get(candidati.size() - i).getId());
                state.executeUpdate();
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }   
        getInstance().notifyObservers(" [Votazione di tipo ordinale su una lista di candidati.] ");
    }

    @Override
    public void votaCandidatoCategorico(Integer id, Integer elettoreId, Candidato candidato) throws IOException{
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        try{
            String query = "UPDATE VotiCandidato SET numeroVoti = numeroVoti + 1 WHERE sessione = ? AND candidato = ?;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            state.setInt(2, candidato.getId());
            state.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        getInstance().notifyObservers(" [Votazione di tipo categorica su una lista di candidati.] ");
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
