/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;

public class SessionManager {
    
    Connection connection;
    
    public SessionManager(Connection connection){
        this.connection = connection;
    }
    
    
    //Permette di creare una sessione.
    public void createSessione(String id, String nome, TipoSessione tipoSessione, TipoScrutinio tipoScrutinio, String password, int elettori, Integer gestoreId){
        String query;
        
        //Controllo che l'id del Gestore sia corretto.
        query = "SELECT id FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                query = "INSERT INTO Sessione (Id, nome, dataApertura, dataChiusura, TipoSessione, TipoScrutinio, "
                        + "Chiusa, Password, Elettori, VotiTotali, Gestore) VALUES (?, ?, ?, ?, ?,"
                        + "?, ?, ?, ?, ?, ?);";
                 try{
                    state = connection.prepareStatement(query);
                    state.setString(1, id);
                    state.setString(2, nome);
                    state.setDate(3, null);
                    state.setDate(4, null);
                    state.setString(5, tipoSessione.toString());
                    state.setString(6, tipoScrutinio.toString());
                    state.setBoolean(7, true);
                    state.setString(8, password);
                    state.setInt(9, elettori);
                    state.setInt(10, 0);
                    state.setInt(11, gestoreId);

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
    public void openSessione(String id, Integer gestoreId){
        String query;
            
        //Controllo che l'id del Gestore sia corretto.
        query = "SELECT id FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                Date data = new Date(System.currentTimeMillis());
                query = "UPDATE Sessione SET chiusa = ? WHERE id = ?;";
                state = connection.prepareStatement(query);
                state.setBoolean(1, false);
                state.setString(2, id);
                state.executeUpdate();
                
                query = "UPDATE Sessione SET dataApertura = ? WHERE id = ?;";
                state = connection.prepareStatement(query);
                state.setDate(1, data);
                state.setString(2, id);
                state.executeUpdate();
            }
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
    }   
    
    //Permette di chiudere una sessione.
    public void closeSessione(String id, Integer gestoreId){
        String query;
        
        //Controllo che l'id del Gestore sia corretto.
        query = "SELECT id FROM Gestore WHERE id = ?;";
        try{
            PreparedStatement state = connection.prepareStatement(query);
            state.setInt(1, gestoreId);
            ResultSet r = state.executeQuery();
            if(r != null){
                Date data = new Date(System.currentTimeMillis());
                query = "UPDATE Sessione SET chiusa = ? WHERE id = ?;";
                state = connection.prepareStatement(query);
                
                state.setBoolean(1, true);
                state.setString(2, id);

                state.executeUpdate();
                
                query = "UPDATE Sessione SET dataChiusura = ? WHERE id = ?;";
                state = connection.prepareStatement(query);
                
                state.setDate(1, data);
                state.setString(2, id);

                state.executeUpdate();
            }
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
    }
}
