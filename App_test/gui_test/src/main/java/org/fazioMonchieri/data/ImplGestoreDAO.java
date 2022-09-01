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
import java.util.Objects;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;
import org.fazioMonchieri.utilities.Observable;
import org.fazioMonchieri.utilities.Observer;

public class ImplGestoreDAO implements GestoreDAO, Observable{
    
    private final List<Observer> obs;
    private static ImplGestoreDAO uniqueInstance;
    
    private ImplGestoreDAO() {
        obs = new ArrayList<>();
        obs.add(Gestore.getInstance());
    }
    
    public static ImplGestoreDAO getInstance() {
        if(uniqueInstance == null)
            uniqueInstance = new ImplGestoreDAO();
        return uniqueInstance;
    }
    
    public Gestore getGestore(Integer id, String username, String codiceFiscale) throws IllegalArgumentException, NullPointerException{
        Objects.requireNonNull(id);
        Objects.requireNonNull(username);
        Objects.requireNonNull(codiceFiscale);
        if(codiceFiscale.length() != 16)
            throw new IllegalArgumentException("Codice fiscale non riconosciuto");
        Gestore gestore = null;
        try{
            //Apertura della connessione.
            Connection connection = null;
            try{
                Class.forName("org.sqlite.JDBC"); 
                connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
                System.out.println(connection);
            }catch(SQLException | ClassNotFoundException e){
                System.out.println(e.getMessage());
            }
            
            String query = "SELECT * FROM Utente WHERE id = " + id + " AND username = " + username + " AND codiceFiscale = " + codiceFiscale;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            //guarda se ci sono risultati
            if(resultSet.next())
                gestore = Gestore.getInstance(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                
            resultSet.close();
            connection.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return gestore;
    }
    
    public boolean isGestore(Integer id, String username, String codiceFiscale) throws IOException {
        return this.getGestore(id, username, codiceFiscale) != null;
    }

    @Override
    public Gestore loginGestore(String username, String password) throws IOException {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        String query = "SELECT * FROM Gestore WHERE username = ? "
                + "AND password = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, username);
            state.setString(2, password);
            ResultSet r = state.executeQuery();
            if(r!=null){
                Gestore g = new Gestore(r.getInt("id"), r.getString("username"), r.getString("persona"));
                return g;
            }
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        getInstance().notifyObservers(" [Login del gestore] ");
        return null;
    }

    @Override
    public List<Sessione> getSessioniGestore(Integer gestoreId){
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        List<Sessione> sessioni = new ArrayList<>();
        
        String query = "SELECT * FROM Sessione WHERE gestore = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, gestoreId);
            ResultSet r = state.executeQuery();
            while(r.next()){
                TipoSessione tipoSessione = TipoSessione.valueOf(r.getString("tipoSessione"));
                TipoScrutinio tipoScrutinio = TipoScrutinio.valueOf(r.getString("tipoScrutinio"));
                
                Sessione sessione = new Sessione(r.getInt("id"), r.getString("nome"), r.getDate("data"), r.getDate("dataChiusura"), tipoSessione, tipoScrutinio, r.getInt("gestore"));
                sessioni.add(sessione);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return sessioni;
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
