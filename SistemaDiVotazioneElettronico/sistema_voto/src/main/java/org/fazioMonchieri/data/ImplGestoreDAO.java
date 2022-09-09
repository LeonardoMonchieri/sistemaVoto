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
import java.sql.Date;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Persona;
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
    
    @Override
    public Persona getPersona(String codiceFiscale){
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        try{
            String query = "SELECT * FROM Persona WHERE codiceFiscale = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, codiceFiscale);
            ResultSet resultSet = statement.executeQuery();
            Persona persona = new Persona(resultSet.getString(1), resultSet.getBoolean(2), resultSet.getString(3), resultSet.getString(4), Date.valueOf(resultSet.getString(5)), resultSet.getString(6));
            connection.close();
            return persona;
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return null;
    }
    
    public Gestore getGestore(String codiceFiscale) throws IllegalArgumentException, NullPointerException{
        Objects.requireNonNull(codiceFiscale);
        if(codiceFiscale.length() != 16)
            throw new IllegalArgumentException("Codice fiscale non riconosciuto");
        Gestore gestore = null;
        try{
            //Apertura della connessione.
            Connection connection = null;
            try{
                Class.forName("org.sqlite.JDBC"); 
                connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
            }catch(SQLException | ClassNotFoundException e){
                System.out.println(e.getMessage());
            }
            
            String query = "SELECT * FROM Gestore WHERE persona = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, codiceFiscale);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet != null)
                gestore = Gestore.getInstance(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
            connection.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return gestore;
    }
    
    public boolean isGestore(String codiceFiscale) throws IOException {
        return this.getGestore(codiceFiscale) != null;
    }

    @Override
    public Gestore loginGestore(String username, String password) throws IOException {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
            
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        String query = "SELECT * FROM Gestore WHERE username = ? AND password = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, username);
            state.setString(2, password);
            ResultSet r = state.executeQuery();
            if(r!=null){
                Gestore gestore = new Gestore(r.getInt(1), r.getString(2), r.getString(4));
                connection.close();
                return gestore;
            }
            connection.close();
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
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        List<Sessione> sessioni = new ArrayList<>();
        
        String query = "SELECT id, nome, dataApertura, dataChiusura, tipoSessione, tipoScrutinio, " + 
                       "gestore FROM Sessione WHERE gestore = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, gestoreId);
            ResultSet r = state.executeQuery();
            while(r.next()){
                TipoSessione tipoSessione = TipoSessione.valueOf(r.getString(5));
                TipoScrutinio tipoScrutinio = TipoScrutinio.valueOf(r.getString(6));
                Sessione sessione;
                if(r.getString(3) == null && r.getString(4) == null){
                    sessione = new Sessione(r.getInt(1), r.getString(2), null, null, tipoSessione, tipoScrutinio, r.getInt(7));
                }
                else if(r. getString(3) != null && r.getString(4) == null)
                    sessione = new Sessione(r.getInt(1), r.getString(2), r.getDate(3), null, tipoSessione, tipoScrutinio, r.getInt(7));
                else 
                    sessione = new Sessione(r.getInt(1), r.getString(2), r.getDate(3), r.getDate(4), tipoSessione, tipoScrutinio, r.getInt(7));
                
                sessioni.add(sessione);
            }
            connection.close();
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
