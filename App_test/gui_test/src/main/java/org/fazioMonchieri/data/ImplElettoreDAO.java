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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public Persona getPersona(String codiceFiscale){
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        Persona persona = null;
        try{
            String query = "SELECT * FROM Persona WHERE codiceFiscale = " + codiceFiscale;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            persona = new Persona(resultSet.getString(1), resultSet.getBoolean(2), resultSet.getString(3), resultSet.getString(4), resultSet.getDate(5), resultSet.getString(6));
            
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
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        Elettore elettore = null;
        try{
            String query = "SELECT id, username, persona FROM Elettore WHERE persona = " + codiceFiscale;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            String query2 = "SELECT dataNascita FROM Persona WHERE codiceFiscale = " + codiceFiscale;
            statement = connection.prepareStatement(query2);
            ResultSet resultSet2 = statement.executeQuery();
            elettore = new Elettore(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet2.getDate(1));
            
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
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        String query = "SELECT * FROM Elettore WHERE username = " + username + "AND password = " + password;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            String query2 = "SELECT dataNascita FROM Persona WHERE codiceFiscale = " + resultSet.getString("persona");
            statement = connection.prepareStatement(query2);
            ResultSet resultSet2 = statement.executeQuery();
            if(resultSet.next())
                return Elettore.getInstance(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet2.getDate(1));
        } catch (SQLException ex) {
            Logger.getLogger(ImplElettoreDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        getInstance().notifyObservers(" [Login dell'elettore] ");
        return null;
    }

    @Override
    public List<Sessione> getSessioniElettore(Integer elettoreId) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        List<Sessione> sessioniAperte = new ArrayList<>();
        List<Sessione> sessioniVotate = new ArrayList<>();
        
        String query = "SELECT * FROM Sessione WHERE dataChiusura = null";
        try{
            Statement state1 = connection.createStatement();
            ResultSet r = state1.executeQuery(query);
            while(r.next()){
                TipoSessione tipoSessione = TipoSessione.valueOf(r.getString("tipoSessione"));
                TipoScrutinio tipoScrutinio = TipoScrutinio.valueOf(r.getString("tipoScrutinio"));
                Sessione sessione = new Sessione(r.getInt("id"), r.getString("nome"), r.getDate("dataApertura"), r.getDate("dataChiusura"), tipoSessione, tipoScrutinio, r.getInt("gestore"));
                sessioniAperte.add(sessione);
            }
            query = "SELECT sessione FROM Vota WHERE elettore = ?";
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, elettoreId);
            r = state.executeQuery();
            while(r.next()){
                TipoSessione tipoSessione = TipoSessione.valueOf(r.getString("tipoSessione"));
                TipoScrutinio tipoScrutinio = TipoScrutinio.valueOf(r.getString("tipoScrutinio"));
                Sessione sessione = new Sessione(r.getInt("id"), r.getString("nome"), r.getDate("dataApertura"), r.getDate("dataChiusura"), tipoSessione, tipoScrutinio, r.getInt("gestore"));
                sessioniVotate.add(sessione);
            }
            
            for(int i=0; i<sessioniAperte.size(); i++){
                for(int j=0; j<sessioniVotate.size(); j++){
                    if(sessioniAperte.get(i).getId().equals(sessioniVotate.get(j).getId())){
                        sessioniAperte.remove(i);
                    }
                }
            }
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
