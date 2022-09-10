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
import java.util.ArrayList;
import java.util.List;
import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Elettore;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.models.Persona;
import org.fazioMonchieri.utilities.Observable;
import org.fazioMonchieri.utilities.Observer;

public class ImplCandidatoDAO implements CandidatoDAO, Observable{

    private final List<Observer> obs;
    private static ImplCandidatoDAO uniqueInstance;
    
    private ImplCandidatoDAO(){
        obs = new ArrayList<>();
        obs.add(Elettore.getInstance());
    }
    
    public static ImplCandidatoDAO getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new ImplCandidatoDAO();
        return uniqueInstance;
    }
    
    @Override
    public String getNomeCompleto(Integer id){
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\Download\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        String nomeCompleto = null;
        try{
            String query = "SELECT P.nome, P.cognome FROM Candidato AS C JOIN " + 
                           "Persona AS P WHERE C.id = ? AND C.persona = P.codiceFiscale;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            nomeCompleto = resultSet.getString(1) + " " + resultSet.getString(2);
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return nomeCompleto;
    }

    @Override
    public void createCandidato(String ruolo, String codiceFiscale, String partito){
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\Download\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        String query = "SELECT id FROM Partito WHERE nome = ?";
        Integer idPartito = null;
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, partito);
            ResultSet resultSet = state.executeQuery();
            idPartito = resultSet.getInt(1);
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }

        query="SELECT * FROM Candidato WHERE persona = ?";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state = connection.prepareStatement(query);
            state.setString(1, codiceFiscale);
            ResultSet resultSet = state.executeQuery();
            if(resultSet.next()){
                connection.close();
                throw new IllegalArgumentException("Candidato giá presente");
            }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        query = "INSERT INTO Candidato ('ruolo', 'persona', 'partito') VALUES (?, ?, ?);";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state = connection.prepareStatement(query);
            state.setString(1, ruolo);
            state.setString(2, codiceFiscale);
            state.setInt(3, idPartito);
            state.executeUpdate();
            connection.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public Partito getPartito(Integer id){
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\Download\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        Partito partito = null;
        try{
            String query = "SELECT P.id, P.nome, P.dataFondazione FROM " +
                           "Candidato AS C JOIN Partito AS P WHERE C.id = ? AND C.partito = P.id;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            partito = new Partito(resultSet.getInt(1), resultSet.getString(2), resultSet.getDate(3));
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return partito;
    }
    
    @Override
    public Persona getPersona(String codiceFiscale){
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\Download\\SistemaDiVotoElettronico.db");
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
    public List<Candidato> getCandidati(){
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\Download\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        List<Candidato> candidati = new ArrayList<>();
        try{
            String query = "SELECT * FROM Candidato;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Candidato c = new Candidato(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4));
                candidati.add(c);
            }
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return candidati;
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
