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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.fazioMonchieri.models.Elettore;
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
    public void votaPartitoOrdinale(String id, String elettoreId, List<String> partitiId) throws IOException{
        
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
            for(int i = partitiId.size(); i > 0; i--){
                String query = "UPDATE VotiPartito SET numeroVoti = numeroVoti + ? WHERE sessione = ? AND partito = ?;";
                PreparedStatement state = connection.prepareStatement(query);
                state.setInt(1, i);
                state.setString(2, id);
                state.setString(3, partitiId.get(partitiId.size() - i));
                state.executeUpdate();
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        getInstance().notifyObservers(" [Votazione di tipo ordinale su una lista di partiti.] ");
        
    }

    @Override
    public void votaPartitoCategorico(String id, String elettoreId, String partitoId) throws IOException{
        
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
            state.setString(1, id);
            state.setString(2, partitoId);
            state.executeUpdate();
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
