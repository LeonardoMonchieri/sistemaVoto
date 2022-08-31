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
import java.util.Date;
import java.util.List;
import org.fazioMonchieri.models.Elettore;
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
    
    public Elettore getElettore(Integer id, String username, String codiceFiscale, Date dataNascita) {
        Elettore elettore = null;
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
            
            String query = "SELECT * FROM utenti WHERE id = " + id + " AND username = " + username + " AND codiceFiscale = " + codiceFiscale + " AND dataNascita = " + dataNascita;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                elettore = Elettore.getInstance(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getDate(4));
            
            resultSet.close();
            connection.close();
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return elettore;
    }
    
    public boolean isElettore(Integer id, String username, String codiceFiscale, Date dataNascita){
        return getElettore(id, username, codiceFiscale, dataNascita) != null;
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
        
        String query = "SELECT * FROM Elettore WHERE username = ? "
                + "AND password = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, username);
            state.setString(2, password);
            ResultSet r = state.executeQuery();
            if(r!=null){
                return new Elettore(r.getInt("id"), r.getString("username"), r.getString("persona"), r.getDate("dataNascita"));
            }
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        getInstance().notifyObservers(" [Login dell'elettore] ");
        return null;
    }

    @Override
    public List<Sessione> getSessioniElettore(String elettoreId) {
        
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
        
        String query = "SELECT id, data, tipoSessione FROM Sessione WHERE chiusa = 0";
        try{
            Statement state1 = connection.createStatement();
            ResultSet r = state1.executeQuery(query);
            while(r.next()){
                TipoSessione tipoSessione = TipoSessione.valueOf(r.getString("tipoSessione"));
                TipoScrutinio tipoScrutinio = TipoScrutinio.valueOf(r.getString("tipoScrutinio"));
                Sessione sessione = new Sessione(r.getInt("id"), r.getString("nome"), r.getDate("data"), r.getBoolean("chiusa"), tipoSessione, tipoScrutinio, r.getInt("elettori"), r.getInt("votiTotali"), r.getInt("gestore"));
                sessioniAperte.add(sessione);
            }
            query = "SELECT sessione FROM Vota WHERE elettore = ?";
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, elettoreId);
            r = state.executeQuery();
            while(r.next()){
                TipoSessione tipoSessione = TipoSessione.valueOf(r.getString("tipoSessione"));
                TipoScrutinio tipoScrutinio = TipoScrutinio.valueOf(r.getString("tipoScrutinio"));
                Sessione sessione = new Sessione(r.getInt("id"), r.getString("nome"), r.getDate("data"), r.getBoolean("chiusa"), tipoSessione, tipoScrutinio, r.getInt("elettori"), r.getInt("votiTotali"), r.getInt("gestore"));
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
