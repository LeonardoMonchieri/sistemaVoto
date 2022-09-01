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
import static org.fazioMonchieri.data.ImplPartitoDAO.getInstance;
import static org.fazioMonchieri.data.ImplVotaCandidatoDAO.getInstance;
import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.utilities.Observable;
import org.fazioMonchieri.utilities.Observer;

public class ImplVotaPartitoDAO implements VotaPartitoDAO, Observable{
    
    private final List<Observer> obs;
    private static ImplVotaPartitoDAO uniqueInstance;
    
    private ImplVotaPartitoDAO() {
        obs = new ArrayList<>();
        obs.add(Gestore.getInstance());
    }
    
    public static ImplVotaPartitoDAO getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new ImplVotaPartitoDAO();
        return uniqueInstance;
    }

    @Override
    public void createVotaPartito(Integer idSessione, Integer idPartito) {
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
            String query = "INSERT INTO VotiPartito (idSessione, idPartito, numeroVoti) VALUES (?, ?, 0)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idSessione);
            statement.setInt(2, idPartito);
            ResultSet resultSet = statement.executeQuery();
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        
        try {
            getInstance().notifyObservers("[Creazione riga database VotiPartito]");
        } catch (IOException ex) {
            Logger.getLogger(ImplSessioneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void votaPartitoOrdinale(Integer id, Integer elettoreId, List<Partito> partiti) throws IOException{
        
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
            for(int i = partiti.size(); i > 0; i--){
                String query = "UPDATE VotiPartito SET numeroVoti = numeroVoti + ? WHERE sessione = ? AND partito = ?;";
                PreparedStatement state = connection.prepareStatement(query);
                state.setInt(1, i);
                state.setInt(2, id);
                state.setInt(3, partiti.get(partiti.size() - i).getId());
                state.executeUpdate();
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        getInstance().notifyObservers(" [Votazione di tipo ordinale su una lista di partiti.] ");
        
    }

    @Override
    public void votaPartitoCategorico(Integer id, Integer elettoreId, Partito partito) throws IOException{
        
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
            String query = "UPDATE VotiPartito SET numeroVoti = numeroVoti + 1 WHERE sessione = ? AND partito = ?;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            state.setInt(2, partito.getId());
            state.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        getInstance().notifyObservers(" [Votazione di tipo categorica su una lista di partiti.] ");
    }
    
    @Override
    public void votaCategoricoConPreferenza(Integer id, Integer elettoreId, Partito partito, Candidato candidato){
        
        try{
            
            try { 
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ImplElettoreDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");

            
            String query = "UPDATE VotiPartito SET numeroVoti = numeroVoti + 1 WHERE sessione = ? AND partito = ?;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            state.setInt(2, partito.getId());
            state.executeUpdate();
            
            if(candidato.getId() != null){
                query = "UPDATE VotiCandidatoSET numeroVoti = numeroVoti + 1 WHERE sessione = ? AND candidato = ?;";
                state = connection.prepareStatement(query);
                state.setInt(1, id);
                state.setInt(2, candidato.getId());
                state.executeUpdate();
            }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
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
