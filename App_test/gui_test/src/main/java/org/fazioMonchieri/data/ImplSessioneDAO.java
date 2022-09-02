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
import java.util.List;
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
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
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
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
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
    public void createSessione(Integer id, String nome, TipoSessione tipoSessione, TipoScrutinio tipoScrutinio, String password, int elettori, Integer gestoreId) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
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
                query = "INSERT INTO Sessione (id, nome, dataApertura, dataChiusura, tipoSessione, tipoScrutinio, "
                        + "password, gestore) VALUES (?, ?, ?, ?, ?,"
                        + "?, ?, ?, ?, ?, ?);";
                 try{
                    state = connection.prepareStatement(query);
                    state.setInt(1, id);
                    state.setString(2, nome);
                    state.setDate(3, null);
                    state.setDate(4, null);
                    state.setString(5, tipoSessione.toString());
                    state.setString(6, tipoScrutinio.toString());
                    state.setString(8, password);
                    state.setInt(11, gestoreId);

                    state.executeUpdate();

                }catch(SQLException e){
                        System.out.println(e.getMessage());
                }
            }
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        try {
            getInstance().notifyObservers("[Creazione della sessione: " + id + "]");
        } catch (IOException ex) {
            Logger.getLogger(ImplSessioneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Partito> getPartiti(Integer id) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        List<Partito> partiti = new ArrayList<>();
        String query = "SELECT partito FROM VotiPartito WHERE sessione = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            ResultSet r = state.executeQuery();
            if(r != null){
                query = "SELECT * FROM Partito WHERE id = ?";
                state = connection.prepareStatement(query);
                state.setInt(1, r.getInt("partito"));
                r = state.executeQuery();
                Partito p = new Partito(r.getInt("id"), r.getString("nome"), r.getDate("dataFondazione"));
                partiti.add(p);
            }
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
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        Integer gestoreId = null;
        Gestore gestore = null;
        try{
            String query = "SELECT gestore FROM Sessione WHERE id = " + id;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            gestoreId = resultSet.getInt(1);
            
            query = "SELECT * FROM Gestore WHERE id = " + gestoreId;
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            gestore = new Gestore(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("persona"));
            
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
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        List<Candidato> candidati = new ArrayList<>();
        String query = "SELECT candidato FROM VotiCandidato WHERE sessione = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, id);
            ResultSet r = state.executeQuery();
            if(r != null){
                query = "SELECT * FROM Candidato WHERE id = ?";
                state = connection.prepareStatement(query);
                state.setInt(1, r.getInt("candidato"));
                r = state.executeQuery();
                
                Candidato c = new Candidato(r.getInt("id"), r.getString("ruolo"), r.getString("persona"), r.getInt("partito"));
                candidati.add(c);
            }
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
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        int votanti = 0;
        try{
            String query = "SELECT COUNT(*) FROM Vota WHERE sessione = " + id;
            PreparedStatement statement = connection.prepareStatement(query);
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
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        int elettori = 0;
        try{
            String query = "SELECT COUNT(*) FROM Elettore";
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
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        List<Sessione> sessioni = new ArrayList<>();
        try{
            String query = "SELECT * FROM Sessione WHERE dataChiusura = null";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
                sessioni.add(new Sessione(resultSet.getInt(1), resultSet.getString(2), resultSet.getDate(3), resultSet.getDate(4), TipoSessione.valueOf(resultSet.getString(5)), TipoScrutinio.valueOf(resultSet.getString(5)),resultSet.getInt(7)));
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
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        String quesito = null;
        try{
            String query = "SELECT quesito FROM Referendum WHERE sessione = " + id;
            PreparedStatement statement = connection.prepareStatement(query);
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
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        try{
            String query = "DELETE FROM Sessione WHERE id = " + id;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            connection.close();
        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    @Override
    public String scrutinioMaggioranza(Integer id, Integer gestoreId) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        int max = 0;
        String partito = "";
        String candidato = "";
        
        //Controllo che l'id del Gestore sia corretto.
        String query = "SELECT id FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                query = "SELECT partito, numeroVoti FROM VotiPartito WHERE sessione = ?;";
                state = connection.prepareStatement(query);
                state.setInt(1, id);
                r = state.executeQuery();
                if(r != null){
                    do{
                        if(max < r.getInt("numeroVoti")){
                            max = r.getInt("numeroVoti");
                            partito = r.getString("partito");
                        }
                    }while(r.next());
                    return partito;
                }
                else{
                    query = "SELECT candidato, numeroVoti FROM VotiCandidato WHERE sessione = ?;";
                    state = connection.prepareStatement(query);
                    state.setInt(1, id);
                    state.setString(2, partito);
                    r = state.executeQuery();

                    do{
                        if(max < r.getInt("numeroVoti")){
                            max = r.getInt("numeroVoti");
                            candidato = r.getString("candidato");
                        }
                    }while(r.next());

                    return candidato;
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
    public String scrutinioMaggioranzaAssoluta(Integer id, Integer gestoreId) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        int max = 0;
        String partito = "";
        String candidato = "";
        
        
        //Controllo che l'id del Gestore sia corretto.
        String query = "SELECT id FROM Gestore WHERE id = ?;";
        String query1 = "SELECT partito, numeroVoti FROM VotiPartito WHERE sessione = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                state = connection.prepareStatement(query1);
                state.setInt(1, id);
                ResultSet r1 = state.executeQuery();

                if(r1 != null){
                    do{
                        if(max < r1.getInt("numeroVoti") && 
                                r1.getInt("numeroVoti") >= (getInstance().getVotanti(id) * 0.5) + 1){
                            max = r1.getInt("numeroVoti");
                            partito = r1.getString("partito");
                        }
                    }while(r1.next());
                    return partito;
                }
                else{
                    query1 = "SELECT candidato, numeroVoti FROM VotiCandidato WHERE sessione = ?;";
                    state = connection.prepareStatement(query1);
                    state.setInt(1, id);
                    r1 = state.executeQuery();

                    do{
                        if(max < r1.getInt("numeroVoti") && 
                                r1.getInt("numeroVoti") >= (0.5 * getInstance().getVotanti(id)) + 1){
                            max = r1.getInt("numeroVoti");
                            candidato = r1.getString("candidato");
                        }
                    }while(r1.next());

                    return candidato;
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        try {
            getInstance().notifyObservers("[Scrutinio maggioranza assoluta sulla sessione: " + id + "]");
        } catch (IOException ex) {
            Logger.getLogger(ImplSessioneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String scrutinioReferendumSenzaQuorum(Integer id, Integer gestoreId) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
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
                query = "SELECT votiSi, votiNo FROM Referendum WHERE sessione = ?;";
                state = connection.prepareStatement(query);
                state.setInt(1, id);
                r = state.executeQuery();

                if(r.getInt("votiSi") > r.getInt("votiNo"))
                    return "Si";
                else if(r.getInt("votiSi") < r.getInt("votiNo"))
                    return "No";
                return "Pareggio";
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
    public String scrutinioReferendumConQuorum(Integer id, Integer gestoreId) {
        
        //Apertura della connessione.
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
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
                query = "SELECT votiSi, votiNo FROM Referendum WHERE sessione = ?;";
                state = connection.prepareStatement(query);
                state.setInt(1, id);
                r = state.executeQuery();
                if(r.getInt("votiSi") + r.getInt("votiNo") > (0.5 * getInstance().getVotanti(id)) + 1){
                    if(r.getInt("votiSi") > r.getInt("votiNo"))
                        return "Si";
                    else if(r.getInt("votiSi") < r.getInt("votiNo"))
                        return "No";
                    else 
                        return "Pareggio";
                }
            }
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
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        
        String query = "INSERT INTO Vota (elettore, sessione, orarioVotazione) "
                + "VALUES (?, ?, ?);";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, elettoreId);
            state.setInt(2, sessioneId);
            state.setTime(3, Time.valueOf(java.time.LocalTime.now()));
            
            state.executeUpdate();
            
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        try {
            getInstance().notifyObservers("[Salvataggio della votazione avvenuta sulla sessione: " + sessioneId + "]");
        } catch (IOException ex) {
            Logger.getLogger(ImplSessioneDAO.class.getName()).log(Level.SEVERE, null, ex);
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
