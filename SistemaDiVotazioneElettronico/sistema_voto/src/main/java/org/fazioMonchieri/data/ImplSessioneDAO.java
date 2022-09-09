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
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;
import org.fazioMonchieri.utilities.Observable;
import org.fazioMonchieri.utilities.Observer;

public class ImplSessioneDAO implements SessioneDAO, Observable{

    private final List<Observer> obs;
    private static ImplSessioneDAO uniqueInstance;
    
    private ImplSessioneDAO() {
        obs = new ArrayList<>();
        obs.add(Gestore.getInstance());
    }
    
    public static ImplSessioneDAO getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new ImplSessioneDAO();
        return uniqueInstance;
    }
    
    @Override
    public void openSessione(Integer id, Integer gestoreId) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        //Controllo che l'id del Gestore sia corretto.
        String query = "SELECT id FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                query = "UPDATE Sessione SET dataApertura = ? WHERE id = ?;";
                state = connection.prepareStatement(query);
                state.setDate(1, Date.valueOf(LocalDate.now()));
                state.setInt(2, id);
                state.executeUpdate();
                connection.close();
            }
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        try {
            getInstance().notifyObservers("[Apertura della sessione: " + id + "]");
        } catch (IOException ex) {
            Logger.getLogger(ImplSessioneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void closeSessione(Integer id, Integer gestoreId) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        //Controllo che l'id del Gestore sia corretto.
        String query = "SELECT id FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                query = "UPDATE Sessione SET dataChiusura = ? WHERE id = ?;";
                state = connection.prepareStatement(query);
                state.setDate(1, Date.valueOf(LocalDate.now()));
                state.setInt(2, id);
                state.executeUpdate();
                connection.close();
            }
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        try {
            getInstance().notifyObservers("[Chiusura della sessione: " + id + "]");
        } catch (IOException ex) {
            Logger.getLogger(ImplSessioneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createSessione(String nome, TipoSessione tipoSessione, TipoScrutinio tipoScrutinio, Integer gestoreId) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        String query = "INSERT INTO Sessione ('nome', 'dataApertura', 'dataChiusura', 'tipoSessione', 'tipoScrutinio', "
        + "'gestore') VALUES (?, ?, ?, ?, ?, ?);";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state = connection.prepareStatement(query);
            state.setString(1, nome);
            state.setDate(2, null);
            state.setDate(3, null);
            state.setString(4, tipoSessione.toString());
            state.setString(5, tipoScrutinio.toString());
            state.setInt(6, gestoreId);
            state.executeUpdate();
            connection.close();

        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        try {
            getInstance().notifyObservers("[Creazione della sessione: ]");
        } catch (IOException ex) {
            Logger.getLogger(ImplSessioneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Sessione getSessione(Integer id){

        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        String query = "SELECT * FROM Sessione WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            ResultSet r = state.executeQuery();
            if(r != null){
                Sessione sessione = new Sessione(r.getInt(1), r.getString(2), r.getDate(3), r.getDate(4), TipoSessione.valueOf(r.getString(5)), TipoScrutinio.valueOf(r.getString(6)), r.getInt(7));
                connection.close();
                return sessione;
            }
            connection.close();
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        return null;

    }

    @Override
    public Integer getId(String nome){

        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        String query = "SELECT id FROM Sessione WHERE nome = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, nome);
            ResultSet r = state.executeQuery();
            if(r != null){
                Integer id = r.getInt(1);
                connection.close();
                return id;
            }
            connection.close();
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Partito> getPartiti(Integer id) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        List<Partito> partiti = new ArrayList<>();
       
        try{
            String query = "SELECT * FROM Partito JOIN VotiPartito ON Partito.id=VotiPartito.partito WHERE VotiPartito.sessione= ? ;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            ResultSet r = state.executeQuery();
            if(r.next() != false){
                do{
                    Partito p = new Partito(r.getInt("id"), r.getString("nome"), r.getDate("dataFondazione"));
                    partiti.add(p);
                    System.out.println(p.getNome());
                }while(r.next());
            }else{
                connection.close();
                return null; 
            }
            
            connection.close();
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        return partiti;
    }
    
    @Override
    public Gestore getGestore(Integer id){
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        Gestore gestore = null;
        try{
            String query = "SELECT G.id, G.username, G.persona FROM Sessione AS S JOIN " +
                           "Gestore AS G WHERE S.id = ? AND S.gestore = G.id;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            gestore = new Gestore(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return gestore;
    }

    @Override
    public List<Candidato> getCandidati(Integer id) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
            
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        List<Candidato> candidati = new ArrayList<>();
        
        try{
            String query = "SELECT * FROM Candidato JOIN VotiCandidato ON Candidato.id=VotiCandidato.candidato WHERE VotiCandidato.sessione = ?;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            ResultSet r = state.executeQuery();
            if(r.next() != false){
                do{
                    Candidato c = new Candidato(r.getInt("id"), r.getString("ruolo"), r.getString("persona"), r.getInt("partito"));
                    candidati.add(c);
                }while(r.next());
            }else{
                connection.close();
                return null; 
            }
            connection.close();
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        return candidati; 
    }


    @Override
    public int getVotanti(Integer id) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
            
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        int votanti = 0;
        try{
            String query = "SELECT COUNT(*) FROM Vota WHERE sessione = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            votanti = resultSet.getInt(1);
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return votanti;
    }
    
    @Override
    public int getElettori() {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
            
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        int elettori = 0;
        try{
            String query = "SELECT COUNT(*) FROM Elettore;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            elettori = resultSet.getInt(1);
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return elettori;
    }
    
    @Override
    public List<Sessione> getOpenSession(){
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
            
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        List<Sessione> sessioni = new ArrayList<>();
        try{
            String query = "SELECT * FROM Sessione WHERE dataApertura IS NOT NULL AND dataChiusura IS NULL;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet r = statement.executeQuery();
            while(r.next()){
                TipoSessione tipoSessione = TipoSessione.valueOf(r.getString(5));
                TipoScrutinio tipoScrutinio = TipoScrutinio.valueOf(r.getString(6));
                Sessione sessione = new Sessione(r.getInt(1), r.getString(2), r.getDate(3), null, tipoSessione, tipoScrutinio, r.getInt(7));
                sessioni.add(sessione);
            }
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return sessioni;
    }
    
    @Override
    public String getQuesitoReferendum(Integer id){
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
            
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        String quesito = null;
        try{
            String query = "SELECT quesito FROM Referendum WHERE sessione = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            quesito = resultSet.getString(1);
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return quesito;
        
    }
    
    @Override
    public void delete(Integer id){
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
            
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        try{
            String query = "DELETE FROM Sessione WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    @Override
    public Map<Integer, Integer> scrutinioMaggioranza(Integer id) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");  
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        Map<Integer, Integer> esiti = new HashMap<>();
        
        try{
            String query = "SELECT tipoSessione FROM Sessione WHERE id = ?;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            ResultSet r = state.executeQuery();
            if(r.getString(1).equals("votoCategoricoPreferenza")){

                query = "SELECT P.id, MAX(VP.numeroVoti) FROM votiPartito AS VP " + 
                        "JOIN Partito AS P ON VP.partito=P.id WHERE VP.sessione = ? GROUP BY VP.sessione";
                state = connection.prepareStatement(query);  
                state.setInt(1, id);
                r = state.executeQuery();

                esiti.put(r.getInt(1), r.getInt(2));
                query = "SELECT C.id, VC.numeroVoti FROM Candidato AS C JOIN " + 
                        "votiCandidato AS VC ON C.id=VC.candidato   " + 
                        " WHERE VC.sessione= ? AND C.partito = ? ORDER BY VC.numeroVoti DESC;";
                state = connection.prepareStatement(query);
                state.setInt(1, id);
                state.setInt(2, r.getInt(2));
                r = state.executeQuery();

                while(r.next()){
                    Integer v= r.getInt(2);
                    if(v==null) v=0;
                    esiti.put(r.getInt(1), v);
                }

                connection.close();
                return esiti;
            }

            else{

                query = "SELECT C.id,VC.numeroVoti FROM Candidato AS C " +
                            "JOIN VotiCandidato AS VC ON C.id=VC.candidato  " + 
                            "WHERE VC.sessione = ?  ORDER BY VC.numeroVoti DESC;";
                state = connection.prepareStatement(query);
                state.setInt(1, id);
                r = state.executeQuery();
                if(r.next()){
                    do{
                        esiti.put(r.getInt(1), r.getInt(2));
                    }while(r.next());
                    
                    connection.close();
                    return esiti;
                }
                else{
                    query = "SELECT P.id, VP.numeroVoti FROM Partito AS P JOIN " + 
                            "votiPartito AS VP ON P.id=VP.partito WHERE VP.sessione = ? ORDER BY VP.numeroVoti DESC";
                    state = connection.prepareStatement(query);
                    state.setInt(1, id);
                    r = state.executeQuery();

                    while(r.next()){
                        esiti.put(r.getInt(1), r.getInt(2));
                    }
                    connection.close();
                    return esiti;
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        try {
            getInstance().notifyObservers("[Scrutinio maggioranza sulla sessione: " + id + "]");
        } catch (IOException ex) {
            Logger.getLogger(ImplSessioneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Map<Integer, Integer> scrutinioMaggioranzaAssoluta(Integer id) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        Map<Integer, Integer> esiti = new HashMap<>();
        try{
            String query = "SELECT tipoSessione FROM Sessione WHERE sessione = ?;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            ResultSet r = state.executeQuery();
            if(r.getString(3).equals("votoCategoricoPreferenza")){
                query = "SELECT P.id, MAX(VP.numeroVoti) FROM votiPartito AS VP " + 
                        "JOIN Partito AS P ON VP.partito=P.id WHERE VP.sessione = ? GROUP BY VP.sessione";
                state = connection.prepareStatement(query);
                state.setInt(1, id);
                r = state.executeQuery();
                if(r.getInt(3) >= (getInstance().getVotanti(id) * 0.5) + 1){
                    esiti.put(r.getInt(1), r.getInt(2));
                    query = "SELECT C.id, P.Cognome, VC.numeroVoti FROM Candidato AS C JOIN " + 
                            "votiCandidato AS VC ON C.id=VC.candidato  " + 
                            "WHERE VC.sessione= ? AND C.partito = ? ORDER BY VC.numeroVoti DESC;";
                    state = connection.prepareStatement(query);
                    state.setInt(1, id);
                    state.setInt(2, r.getInt(2));
                    r = state.executeQuery();
                    while(r.next()){
                        Integer v= r.getInt(2);
                        if(v==null) v=0;
                        esiti.put(r.getInt(1), v);
                    }
                }
                connection.close();
                return esiti;
            }
            else{
                query = "SELECT * FROM VotiCandidati WHERE sessione = ?";
                state = connection.prepareStatement(query);
                state.setInt(1, id);
                r = state.executeQuery();
                if(r.next()){
                    query = "SELECT C.id , VC.numeroVoti FROM Candidato AS C " +
                            "JOIN VotiCandidato AS VC ON C.id=VC.candidato " + 
                            "WHERE VC.sessione = ? ORDER BY VC.numeroVoti DESC;";
                    state = connection.prepareStatement(query);
                    state.setInt(1, id);
                    r = state.executeQuery();
                    if(r.getInt(3) >= (getInstance().getVotanti(id) * 0.5) + 1){
                        while(r.next()){
                            Integer v= r.getInt(2);
                            if(v==null) v=0;
                            esiti.put(r.getInt(1), v);
                        }
                    }
                    connection.close();
                    return esiti;
                }
                else{
                    query = "SELECT P.id, VP.numeroVoti FROM Partito AS P JOIN " + 
                            "votiPartito AS VP ON P.id=VP.partito WHERE VP.sessione = ? ORDER BY VP.numeroVoti DESC";
                    state = connection.prepareStatement(query);
                    state.setInt(1, id);
                    r = state.executeQuery();
                    if(r.getInt(2) >= (getInstance().getVotanti(id) * 0.5) + 1){
                        while(r.next()){
                            Integer v= r.getInt(2);
                            if(v==null) v=0;
                            esiti.put(r.getInt(1), v);
                        }
                    }
                    connection.close();
                    return esiti;
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        try {
            getInstance().notifyObservers("[Scrutinio maggioranza sulla sessione: " + id + "]");
        } catch (IOException ex) {
            Logger.getLogger(ImplSessioneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Map<Integer, Integer> scrutinioReferendumSenzaQuorum(Integer id) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        Map<Integer, Integer> esiti = new HashMap<>();
        
        try{
            String query = "SELECT votiSi, votiNo FROM Referendum WHERE sessione = ?;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            ResultSet r = state.executeQuery();
            if(r.getInt(1) >= r.getInt(2)){
                esiti.put(1, r.getInt(1));
                esiti.put(0, r.getInt(2));
                connection.close();
                return esiti;
            }
            else{
                esiti.put(0, r.getInt(2));
                esiti.put(1, r.getInt(1));
                connection.close();
                return esiti;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        try {
            getInstance().notifyObservers("[Scrutinio referendum senza quorum sulla sessione: " + id + "]");
        } catch (IOException ex) {
            Logger.getLogger(ImplSessioneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Map<Integer, Integer> scrutinioReferendumConQuorum(Integer id) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
            
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        Map<Integer, Integer> esiti = new HashMap<>();

        try{
            String query = "SELECT votiSi, votiNo FROM Referendum WHERE sessione = ?;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            ResultSet r = state.executeQuery();
            if(r.getInt(1) + r.getInt(2) > (0.5 * getInstance().getVotanti(id)) + 1){
                if(r.getInt(1) >= r.getInt(2)){
                    esiti.put(0, r.getInt(1));
                    esiti.put(0, r.getInt(2));
                    connection.close();
                    return esiti;
                }
                else{
                    esiti.put(0, r.getInt(2));
                    esiti.put(1, r.getInt(1));
                    connection.close();
                    return esiti;
                }
            }
            connection.close();
            return null;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        try {
            getInstance().notifyObservers("[Scrutinio referendum con quorum sulla sessione: " + id + "]");
        } catch (IOException ex) {
            Logger.getLogger(ImplSessioneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    //Permette di salvare nel database la votazione avvenuta.
    public void votazione(Integer elettoreId, Integer sessioneId){
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\Downloads\\SistemaDiVotoElettronico.db");
            
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        String query = "INSERT INTO Vota ('elettore', 'sessione', 'orarioVotazione') "
                + "VALUES (?, ?, ?);";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, elettoreId);
            state.setInt(2, sessioneId);
            state.setTime(3, Time.valueOf(java.time.LocalTime.now()));
            state.executeUpdate();
            connection.close();
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        try {
            getInstance().notifyObservers("[Salvataggio della votazione avvenuta sulla sessione: " + sessioneId + "]");
        } catch (IOException ex) {
            Logger.getLogger(ImplSessioneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return;
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
