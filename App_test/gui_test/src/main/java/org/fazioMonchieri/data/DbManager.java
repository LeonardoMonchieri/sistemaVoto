/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import org.fazioMonchieri.models.*;
import org.fazioMonchieri.utilities.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.sql.Date;
import java.sql.Time;

public class DbManager {
    
    private Connection connection;

    public DbManager(){
        
    }
    
    public static void main(String[] args) {
        DbManager db = new DbManager();
        db.openConnection();
        List<Sessione> sessioni = db.getSessioniElettore("Elettore0");
        for(int i=0; i<sessioni.size(); i++)
            System.out.println(sessioni.get(i).getId());
        db.closeConnection();
    }
    
    //Permette di aprire la connessione con il database.
    public void openConnection(){
        try{
            Class.forName("org.sqlite.JDBC"); 
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\franc\\OneDrive\\Documenti\\Universitá\\Ingegneria del Software\\Progettino\\sistemaVotazioneElettronico\\SistemaVotazioneElettronico\\SistemaDiVotoElettronico.db");
            System.out.println(connection);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    //Permette di chiudere la connessione con il database.
    public void closeConnection(){
        if(connection != null){
            try{
                connection.close();
        
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    public List<Sessione> getSessioniGestore(String gestoreId){
        List<Sessione> sessioni = new ArrayList<>();
        
        String query = "SELECT * FROM Sessione WHERE gestore = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r1 = state.executeQuery();
            while(r1.next()){
                TipoSessione tipoSessione = TipoSessione.valueOf(r1.getString("tipoSessione"));
                TipoScrutinio tipoScrutinio = TipoScrutinio.valueOf(r1.getString("tipoScrutinio"));
                query = "SELECT * FROM Gestore WHERE id = ?;";
                state = connection.prepareStatement(query);
                state.setString(1, r1.getString("gestore"));
                ResultSet r2 = state.executeQuery();
                query = "SELECT * FROM Persona WHERE codiceFiscale = ?;";
                state = connection.prepareStatement(query);
                state.setString(1, r2.getString("persona"));
                ResultSet r3 = state.executeQuery();
                Persona persona = new Persona(r3.getString("codiceFiscale"), r3.getBoolean("sesso"), r3.getString("nome"), r3.getString("Cognome"), r3.getDate("dataNascita"), r3.getString("luogoNascita"));
                Gestore gestore = new Gestore(r2.getString("id"), r2.getString("username"), r2.getString("password"), persona);
                Sessione sessione = new Sessione(r1.getString("id"), r1.getString("nome"), r1.getDate("data"), r1.getString("password"), tipoSessione, tipoScrutinio, r1.getInt("elettori"), gestore);
                sessioni.add(sessione);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return sessioni;
    }
    
    public List<Sessione> getSessioniElettore(String elettoreId){
        List<Sessione> sessioniAperte = new ArrayList<>();
        List<Sessione> sessioniVotate = new ArrayList<>();
        
        String query = "SELECT id, data, tipoSessione FROM Sessione WHERE chiusa = 0";
        try{
            Statement state1 = connection.createStatement();
            ResultSet r = state1.executeQuery(query);
            while(r.next()){
                TipoSessione tipoSessione = TipoSessione.valueOf(r.getString("tipoSessione"));
                Sessione sessione = new Sessione(r.getString("id"), null, r.getDate("data"), null, tipoSessione, null, 0, null);
                sessioniAperte.add(sessione);
            }
            query = "SELECT sessione FROM Vota WHERE elettore = ?";
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, elettoreId);
            r = state.executeQuery();
            while(r.next()){
                Sessione sessione = new Sessione(r.getString("sessione"), null, null, null, null, null, 0, null);
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
    
    
    public SchedaElettorale getScheda(String id){
        String query = "SELECT nome, tipoSessione FROM Sessione WHERE id = ?";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, id);
            ResultSet r = state.executeQuery();
            String nomeSessione = r.getString("nome");
            switch(r.getString("tipoSessione")){
                case "votoOrdinale":
                    query = "SELECT partito FROM VotiPartiti WHERE sessione = ?";
                    state = connection.prepareStatement(query);
                    state.setString(1, id);
                    r = state.executeQuery();
                    if(r!=null){
                        query = "SELECT nome FROM Partito WHERE id = ?";
                        state = connection.prepareStatement(query);
                        state.setString(1, r.getString("partito"));
                        r = state.executeQuery();
                        List<String> partiti = new ArrayList<>();
                        while(r.next()){
                            partiti.add(r.getString("nome"));
                        }
                        return new SchedaElettoraleCategoricoOrdinale(nomeSessione, partiti);
                    }
                    else{
                        query = "SELECT candidato FROM VotiCandidato WHERE sessione = ?";
                        state = connection.prepareStatement(query);
                        state.setString(1, id);
                        r = state.executeQuery();
                        query = "SELECT nome,cognome FROM Persona WHERE id = ?";
                        state = connection.prepareStatement(query);
                        state.setString(1, r.getString("candidato"));
                        r = state.executeQuery();
                        List<String> candidati = new ArrayList<>();
                        while(r.next()){
                            candidati.add(r.getString("nome") + " " + r.getString("cognome"));
                        }
                        return new SchedaElettoraleCategoricoOrdinale(nomeSessione, candidati);
                    }
                    
                case "votoCategorico":
                    query = "SELECT partito FROM VotiPartiti WHERE sessione = ?";
                    state = connection.prepareStatement(query);
                    state.setString(1, id);
                    r = state.executeQuery();
                    if(r!=null){
                        query = "SELECT nome FROM Partito WHERE id = ?";
                        state = connection.prepareStatement(query);
                        state.setString(1, r.getString("partito"));
                        r = state.executeQuery();
                        List<String> partiti = new ArrayList<>();
                        while(r.next()){
                            partiti.add(r.getString("nome"));
                        }
                        return new SchedaElettoraleCategoricoOrdinale(nomeSessione, partiti);
                    }
                    else{
                        query = "SELECT candidato FROM VotiCandidato WHERE sessione = ?";
                        state = connection.prepareStatement(query);
                        state.setString(1, id);
                        r = state.executeQuery();
                        query = "SELECT nome,cognome FROM Persona WHERE id = ?";
                        state = connection.prepareStatement(query);
                        state.setString(1, r.getString("candidato"));
                        r = state.executeQuery();
                        List<String> candidati = new ArrayList<>();
                        while(r.next()){
                            candidati.add(r.getString("nome") + " " + r.getString("cognome"));
                        }
                        return new SchedaElettoraleCategoricoOrdinale(nomeSessione, candidati);
                    }
                            
                case "votoCategoricoPreferenza":
                    query = "SELECT partito FROM VotiPartiti WHERE sessione = ?";
                    state = connection.prepareStatement(query);
                    state.setString(1, id);
                    r = state.executeQuery();
                    query = "SELECT nome FROM Partito WHERE id = ?";
                    state = connection.prepareStatement(query);
                    state.setString(1, r.getString("partito"));
                    r = state.executeQuery();
                    List<String> partiti = new ArrayList<>();
                    while(r.next()){
                        partiti.add(r.getString("nome"));
                    }
                    Map<String, List<String>> candidati = new HashMap<>();
                    for(int i=0; i<partiti.size(); i++){
                        List<String> candidatiPartito = new ArrayList<>();
                        query = "SELECT persona FROM Candidato WHERE partito = ?";
                        state = connection.prepareStatement(query);
                        state.setString(1, partiti.get(i));
                        r = state.executeQuery();
                        query = "SELECT nome, cognome FROM Persona WHERE codiceFiscale = ?";
                        state = connection.prepareStatement(query);
                        state.setString(1, r.getString("persona"));
                        r = state.executeQuery();
                        while(r.next()){
                            candidatiPartito.add(r.getString("nome") + " " + r.getString("cognome"));
                        }   
                        candidati.put(partiti.get(i), candidatiPartito);
                    }
                    
                    return new SchedaElettoraleCategoricoPreferenza(nomeSessione, partiti, candidati);
                    
                case "referendum":
                    query = "SELECT quesito FROM Referendum WHERE sessione = ?";
                    state = connection.prepareStatement(query);
                    state.setString(1, id);
                    r = state.executeQuery();
                    return new SchedaElettoraleReferendum(r.getString("quesito"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    
    //Permette di effettuare il login al sistema.
    public Gestore loginGestore(String username, String password){
        String query;
        //Auditing
        query = "SELECT persona FROM Gestore WHERE username = ? "
                    + "AND password = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, username);
            state.setString(2, password);
            ResultSet r = state.executeQuery();

            this.auditing("loginGestore", r.getString("persona"));

        }catch(SQLException e){
                System.out.println(e.getMessage());
        }

        query = "SELECT * FROM Gestore WHERE username = ? "
                + "AND password = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, username);
            state.setString(2, password);
            ResultSet r1 = state.executeQuery();
            if(r1!=null){
                query = "SELECT * FROM Persona WHERE codiceFiscale = ?";
                state = connection.prepareStatement(query);
                state.setString(1, r1.getString("persona"));
                ResultSet r2 = state.executeQuery();
                Persona p = new Persona(r2.getString("codiceFiscale"), r2.getBoolean("sesso"), r2.getString("nome"), r2.getString("cognome"), r2.getDate("dataNascita"), r2.getString("luogoNascita"));
                Gestore g = new Gestore(r1.getString("id"), r1.getString("username"), r1.getString("password"), p);
                return g;
            }
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        return null;
    }
        
    public Elettore loginElettore(String username, String password){ 
        //Auditing
        String query = "SELECT persona FROM Elettore WHERE username = ? "
                    + "AND password = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, username);
            state.setString(2, password);
            ResultSet r = state.executeQuery();

            this.auditing("loginElettore", r.getString("persona"));

        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
            
        query = "SELECT * FROM Elettore WHERE username = ? "
                + "AND password = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, username);
            state.setString(2, password);
            ResultSet r1 = state.executeQuery();
            if(r1!=null){
                query = "SELECT * FROM Persona WHERE codiceFiscale = ?";
                state = connection.prepareStatement(query);
                state.setString(1, r1.getString("persona"));
                ResultSet r2 = state.executeQuery();
                Persona p = new Persona(r2.getString("codiceFiscale"), r2.getBoolean("sesso"), r2.getString("nome"), r2.getString("cognome"), r2.getDate("dataNascita"), r2.getString("luogoNascita"));
                Elettore e = new Elettore(r1.getString("id"), r1.getString("username"), r1.getString("password"), p);
                return e;
            }
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        return null;
    }
    
    //Permette di creare una sessione.
    public void createSessione(String id, String nome, TipoSessione tipoSessione, TipoScrutinio tipoScrutinio, String password, int elettori, String gestoreId){
        String query;
        
        //Auditing
        query = "SELECT persona FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();

            this.auditing("creazioneSessione", r.getString("persona"));

        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        
        //Controllo che l'id del Gestore sia corretto.
        query = "SELECT id FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                Date data = new Date(System.currentTimeMillis());
                query = "INSERT INTO Sessione (Id, nome, Data, TipoSessione, TipoScrutinio, "
                        + "Chiusa, Password, Elettori, VotiTotali, Gestore) VALUES (?, ?, ?, ?,"
                        + "?, ?, ?, ?, ?, ?);";
                 try{
                    state = connection.prepareStatement(query);
                    state.setString(1, id);
                    state.setString(2, nome);
                    state.setDate(3, data);
                    state.setString(4, tipoSessione.toString());
                    state.setString(5, tipoScrutinio.toString());
                    state.setBoolean(6, true);
                    state.setString(7, password);
                    state.setInt(8, elettori);
                    state.setInt(9, 0);
                    state.setString(10, gestoreId);

                    state.executeUpdate();

                }catch(SQLException e){
                        System.out.println(e.getMessage());
                }
            }
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
    }
    
    //Permette di aprire una sessione.
    public void openSessione(String id, String gestoreId){
        String query;
        
        //Auditing
        query = "SELECT persona FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();

            this.auditing("aperturaSessione", r.getString("persona"));

        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
            
        //Controllo che l'id del Gestore sia corretto.
        query = "SELECT id FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                query = "UPDATE Sessione SET chiusa = ? WHERE id = ?;";
                state = connection.prepareStatement(query);
                state.setBoolean(1, false);
                state.setString(2, id);
                
                state.executeUpdate();
            }
            
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
    }   
    
    //Permette di chiudere una sessione.
    public void closeSessione(String id, String gestoreId){
        String query;
        
        //Auditing
        query = "SELECT persona FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();

            this.auditing("chiusuraSessione", r.getString("persona"));

        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        
        //Controllo che l'id del Gestore sia corretto.
        query = "SELECT id FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                query = "UPDATE Sessione SET chiusa = ? WHERE id = ?;";
                state = connection.prepareStatement(query);
                state.setBoolean(1, true);
                state.setString(2, id);

                state.executeUpdate();
            }
            
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
    }
    
    //Permette di effettuare lo scrutinio di maggioranza sulla sessione.
    public String scrutinioMaggioranza(String id, String gestoreId){
        
        //Auditing
        String query = "SELECT persona FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();

            this.auditing("scrutinioMaggioranza", r.getString("persona"));

        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        
        int max = 0;
        String partito = "";
        String candidato = "";
        
        //Controllo che l'id del Gestore sia corretto.
        query = "SELECT id FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                query = "SELECT partito, numeroVoti FROM VotiPartito WHERE sessione = ?;";
                state = connection.prepareStatement(query);
                state.setString(1, id);
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
                    state.setString(1, id);
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
        return null;
    }
    
    //Permette di effettuare lo scrutinio di maggioranza assoluta sulla sessione.
    public String scrutinioMaggioranzaAssoluta(String id, String gestoreId){
        
        //Auditing
        String query = "SELECT persona FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();

            this.auditing("scrutinioMaggioranzaAssoluta", r.getString("persona"));

        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        
        int max = 0;
        String partito = "";
        String candidato = "";
        
        
        //Controllo che l'id del Gestore sia corretto.
        query = "SELECT id FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                String query1 = "SELECT partito, numeroVoti FROM VotiPartito WHERE sessione = ?;";
                state = connection.prepareStatement(query1);
                state.setString(1, id);
                ResultSet r1 = state.executeQuery();

                String query2 = "SELECT votiTotali FROM Sessione WHERE id = ?;";
                state = connection.prepareStatement(query2);
                state.setString(1, id);
                ResultSet r2 = state.executeQuery();

                if(r1 != null){
                    do{
                        if(max < r1.getInt("numeroVoti") && 
                                r1.getInt("numeroVoti") >= (0.5 * r2.getInt("votiTotali")) + 1){
                            max = r1.getInt("numeroVoti");
                            partito = r1.getString("partito");
                        }
                    }while(r1.next());
                    return partito;
                }
                else{
                    query1 = "SELECT candidato, numeroVoti FROM VotiCandidato WHERE sessione = ?;";
                    state = connection.prepareStatement(query1);
                    state.setString(1, id);
                    r1 = state.executeQuery();

                    do{
                        if(max < r1.getInt("numeroVoti") && 
                                r1.getInt("numeroVoti") >= (0.5 * r2.getInt("votiTotali")) + 1){
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
        return null;
    }
                  
    //Permette di effettuare lo scrutinio di rferendum senza quorum sulla sessione.
    public String scrutinioReferendumSenzaQuorum(String id, String gestoreId){
        
        //Auditing
        String query = "SELECT persona FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();

            this.auditing("scrutinioReferendumSenzaQuorum", r.getString("persona"));

        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        
        //Controllo che l'id del Gestore sia corretto.
        query = "SELECT id FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                query = "SELECT votiSi, votiNo FROM Referendum WHERE sessione = ?;";
                state = connection.prepareStatement(query);
                state.setString(1, id);
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
        return null;
    }
    
    //Permette di effettuare lo scrutinio di rferendum con quorum sulla sessione.
    public String scrutinioReferendumConQuorum(String id, String gestoreId){
        
        //Auditing
        String query = "SELECT persona FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();

            this.auditing("scrutinioReferendumConQuorum", r.getString("persona"));

        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        
        //Controllo che l'id del Gestore sia corretto.
        query = "SELECT id FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                String query1 = "SELECT votiTotali FROM Sessione WHERE id = ?;";
                state = connection.prepareStatement(query1);
                state.setString(1, id);
                ResultSet r1 = state.executeQuery();

                String query2 = "SELECT votiSi, votiNo FROM Referendum WHERE sessione = ?;";
                state = connection.prepareStatement(query2);
                state.setString(1, id);
                ResultSet r2 = state.executeQuery();
                if(r2.getInt("votiSi") + r2.getInt("votiNo") > (0.5 * r1.getInt("votiTotali")) + 1){
                    if(r2.getInt("votiSi") > r2.getInt("votiNo"))
                        return "Si";
                    else if(r2.getInt("votiSi") < r2.getInt("votiNo"))
                        return "No";
                    else 
                        return "Pareggio";
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    //Permette di votare ad un referedum in una sessione.
    public void votaReferendum(String id, String elettoreId, Boolean voto){
        String query;
        
        //Auditing
        this.auditing("votazioneReferendum", elettoreId);
        
        this.votazione(id, elettoreId);
        
        if(voto){
            try{
                query = "UPDATE Referendum SET votiSi = votiSi + 1 WHERE sessione = ?;";
                PreparedStatement state = connection.prepareStatement(query);
                state.setString(1, id);
                state.executeUpdate();
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        else{
            try{
                query = "UPDATE Referendum SET votiNo = votiNo + 1 WHERE sessione = ?;";
                PreparedStatement state = connection.prepareStatement(query);
                state.setString(1, id);
                state.executeUpdate();
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    //Permette di votare ad una votazione ordinale di partiti in una sessione.
    public void votaPartitoOrdinale(String id, String elettoreId, List<String> partitiId){
        String query;
        
        //Auditing
        this.auditing("votazionePartitoOrdinale",  elettoreId);
        
        this.votazione(id, elettoreId);
        
        try{
            for(int i = partitiId.size(); i > 0; i--){
                query = "UPDATE VotiPartito SET numeroVoti = numeroVoti + ? WHERE sessione = ? AND partito = ?;";
                PreparedStatement state = connection.prepareStatement(query);
                state.setInt(1, i);
                state.setString(2, id);
                state.setString(3, partitiId.get(partitiId.size() - i));
                state.executeUpdate();
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Permette di votare ad una votazione ordinale di candidati in una sessione.
    public void votaCandidatoOrdinale(String id, String elettoreId, List<String> candidatiId){
        String query;
        
        //Auditing
        this.auditing("votazioneCandidatoOrdinale", elettoreId);
        
        this.votazione(id, elettoreId);
        
        try{
            for(int i = candidatiId.size(); i > 0; i--){
                query = "UPDATE VotiCandidati SET numeroVoti = numeroVoti + ? WHERE sessione = ? AND candidato = ?;";
                PreparedStatement state = connection.prepareStatement(query);
                state.setInt(1, i);
                state.setString(2, id);
                state.setString(3, candidatiId.get(candidatiId.size() - i));
                state.executeUpdate();
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Permette di votare ad una votazione categorica di partiti in una sessione.
    public void votaPartitoCategorico(String id, String elettoreId, String partitoId){
        String query;
        
        //Auditing
        this.auditing("votazionePartitoCategorico", elettoreId);
        
        this.votazione(id, elettoreId);
        
        try{
            query = "UPDATE VotiPartito SET numeroVoti = numeroVoti + 1 WHERE sessione = ? AND partito = ?;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, id);
            state.setString(2, partitoId);
            state.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Permette di votare ad una votazione categorica di candidati in una sessione.
    public void votaCandidatoCategorico(String id, String elettoreId, String candidatoId){
        String query;
        
        //Auditing
        this.auditing("votazioneCandidatoCategorico", elettoreId);
        
        this.votazione(id, elettoreId);

        try{
            query = "UPDATE VotiCandidatoSET numeroVoti = numeroVoti + 1 WHERE sessione = ? AND candidato = ?;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, id);
            state.setString(2, candidatoId);
            state.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Permette di votare ad una votazione categorica con preferenza in una sessione.
    public void votaCategoricoConPreferenza(String id, String elettoreId, String partitoId, String candidatoId){
        
        //Auditing
        this.auditing("votazioneCatorgicoConPreferenza", elettoreId);
        
        this.votazione(id, elettoreId);

        try{
            String query = "UPDATE VotiPartito SET numeroVoti = numeroVoti + 1 WHERE sessione = ? AND partito = ?;";
            PreparedStatement state = connection.prepareStatement(query);
            state.setString(1, id);
            state.setString(2, partitoId);
            state.executeUpdate();
            
            if(candidatoId != null){
                query = "UPDATE VotiCandidatoSET numeroVoti = numeroVoti + 1 WHERE sessione = ? AND candidato = ?;";
                state = connection.prepareStatement(query);
                state.setString(1, id);
                state.setString(2, candidatoId);
                state.executeUpdate();
            }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    public void auditing(String azione, String id){
        String query = "INSERT INTO Auditing (time, azione, persona) "
                + "VALUES (?, ?, ?);";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            Time time = Time.valueOf(java.time.LocalTime.now());
            state.setTime(1, time);
            state.setString(2, azione);
            state.setString(3, id);
            
            state.executeUpdate();
            
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
    }
    
    public void votazione(String elettoreId, String sessioneId){
        String query = "INSERT INTO Vota (elettore, sessione, orario) "
                + "VALUES (?, ?, ?);";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            Time time = Time.valueOf(java.time.LocalTime.now());
            state.setString(1, elettoreId);
            state.setString(2, sessioneId);
            state.setTime(3, time);
            
            state.executeUpdate();
            
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
    }
}