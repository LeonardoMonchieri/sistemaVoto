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
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.models.VotiPartito;
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
    public VotiPartito getVotiPartito(Integer id){
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\Download\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        String query = "SELECT * FROM VotiPartito WHERE partito = ?;";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet r = statement.executeQuery();
            if(r != null){
                VotiPartito vP = new VotiPartito(r.getInt(1), r.getInt(2), r.getInt(3));
                connection.close();
                return vP;
            }
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return null;
    }

    @Override
    public void createVotaPartito(Integer idSessione, Integer idPartito) {
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\Download\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        try{
            String query = "INSERT INTO VotiPartito ('sessione', 'partito', 'numeroVoti') VALUES (?, ?, 0);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idSessione);
            statement.setInt(2, idPartito);
            statement.executeUpdate();
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
    public void votaPartitoOrdinale(Integer SesisonId, List<Partito> partiti) throws IOException{
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\Download\\SistemaDiVotoElettronico.db");
            
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        try{
            for(int i = partiti.size(); i > 0; i--){
                String query = "UPDATE VotiPartito SET numeroVoti = numeroVoti + ? WHERE sessione = ? AND partito = ?;";
                PreparedStatement state = connection.prepareStatement(query);
                state.setInt(1, i);
                state.setInt(2, SesisonId);
                state.setInt(3, partiti.get(partiti.size() - i).getId());
                state.executeUpdate();
            }
            connection.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        getInstance().notifyObservers(" [Votazione di tipo ordinale su una lista di partiti.] ");
        
    }

    @Override
    public void votaPartitoCategorico(Integer SesisonId, Partito partito) throws IOException{
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\Download\\SistemaDiVotoElettronico.db");
            
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        try{
            String query = "UPDATE VotiPartito SET numeroVoti = numeroVoti + 1 WHERE sessione = ? AND partito = ?;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, SesisonId);
            state.setInt(2, partito.getId());
            state.executeUpdate();
            connection.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        getInstance().notifyObservers(" [Votazione di tipo categorica su una lista di partiti.] ");
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
