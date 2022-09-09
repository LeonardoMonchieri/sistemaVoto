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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.fazioMonchieri.models.Elettore;
import org.fazioMonchieri.models.Persona;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;
import org.fazioMonchieri.utilities.Observable;
import org.fazioMonchieri.utilities.Observer;

public class ImplElettoreDAO implements ElettoreDAO, Observable{
    
    private final List<Observer> obs;
    private static ImplElettoreDAO uniqueInstance;
    
    private ImplElettoreDAO(){
        obs = new ArrayList<>();
        obs.add(Elettore.getInstance());
    }
    
    public static ImplElettoreDAO getInstance() {
        if(uniqueInstance == null)
            uniqueInstance = new ImplElettoreDAO();
        return uniqueInstance;
    }

    @Override
    public void createElettore(String username, String password, String codiceFiscale){
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        String query = "INSERT INTO Elettore ('username', 'password', 'persona') VALUES (?, ?, ?);";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state = connection.prepareStatement(query);
            state.setString(1, username);
            state.setString(2, password);
            state.setString(3, codiceFiscale);
            state.executeUpdate();
            connection.close();
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
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
        
        Persona persona = null;
        try{
            String query = "SELECT * FROM Persona WHERE codiceFiscale = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, codiceFiscale);
            ResultSet resultSet = statement.executeQuery();
            persona = new Persona(resultSet.getString(1), resultSet.getBoolean(2), resultSet.getString(3), resultSet.getString(4), Date.valueOf(resultSet.getString(5)), resultSet.getString(6));
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return persona;
    }
    
    @Override
    public Elettore getElettore(String codiceFiscale){
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        Elettore elettore = null;
        try{
            String query = "SELECT E.id, E.username, E.password, P.dataNascita FROM Elettore AS E JOIN " + 
                           "Persona AS P WHERE E.persona = ? AND E.persona = P.codiceFiscale;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, codiceFiscale);
            ResultSet resultSet = statement.executeQuery();
            elettore = new Elettore(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), Date.valueOf(resultSet.getString(4)));
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return elettore;
    }
    
    public boolean isElettore(String codiceFiscale){
        return getElettore(codiceFiscale) != null;
    }

    @Override
    public Elettore loginElettore(String username, String password) throws IOException {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        String query = "SELECT E.id, E.username, E.persona, P.dataNascita FROM Elettore AS E JOIN " + 
        "Persona AS P WHERE E.username = ? AND E.password = ? AND E.persona = P.codiceFiscale;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet != null){
                Elettore elettore = new Elettore(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), Date.valueOf(resultSet.getString(4)));  
                connection.close();
                getInstance().notifyObservers(" [Login dell'elettore] ");
                return elettore;
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return null;
    }

    @Override
    public List<Sessione> getSessioniElettore(Integer elettoreId) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        List<Sessione> sessioniAperte = new ArrayList<>();
        List<Sessione> sessioniVotate = new ArrayList<>();
        String query = "SELECT * FROM Sessione WHERE dataApertura IS NOT NULL AND dataChiusura IS NULL;";
        try{
            Statement state1 = connection.createStatement();
            ResultSet r = state1.executeQuery(query);
            while(r.next()){
                TipoSessione tipoSessione = TipoSessione.valueOf(r.getString("tipoSessione"));
                TipoScrutinio tipoScrutinio = TipoScrutinio.valueOf(r.getString("tipoScrutinio"));
                Sessione sessione = new Sessione(r.getInt("id"), r.getString("nome"), r.getDate("dataApertura"), null, tipoSessione, tipoScrutinio, r.getInt("gestore"));
                sessioniAperte.add(sessione);
            }
            query = "SELECT S.id, S.nome, S.dataApertura, S.dataChiusura, S.tipoSessione, S.tipoScrutinio, S.gestore FROM Vota AS V " + 
                    "JOIN Sessione AS S WHERE V.elettore = ? AND V.sessione = S.id;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, elettoreId);
            r = state.executeQuery();
            while(r.next()){
                TipoSessione tipoSessione = TipoSessione.valueOf(r.getString(5));
                TipoScrutinio tipoScrutinio = TipoScrutinio.valueOf(r.getString(6));
                Sessione sessione = new Sessione(r.getInt(1), r.getString(2), r.getDate(3), r.getDate(4), tipoSessione, tipoScrutinio, r.getInt(7));
                sessioniVotate.add(sessione);
            }
            for(int i=0; i<sessioniAperte.size(); i++){
                for(int j=0; j<sessioniVotate.size(); j++){
                    if(sessioniAperte.get(i).getId().equals(sessioniVotate.get(j).getId())){
                        sessioniAperte.remove(i);
                        break;
                    }
                }
            }
            connection.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return sessioniAperte;
    }
    
    @Override
    public void subscribe(Observer o) { uniqueInstance.obs.add(o); }

    @Override
    public void unsubcribe(Observer o) { uniqueInstance.obs.remove(o); }

    @Override
    public void notifyObservers(String s) throws IOException {
        for(Observer o : uniqueInstance.obs)
            if(o != null)
                o.update(s);
    }
    
}
