/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.io.IOException;
import java.util.List;
import org.fazioMonchieri.models.Elettore;
import org.fazioMonchieri.models.Persona;
import org.fazioMonchieri.models.Sessione;

public interface ElettoreDAO {

    //Permette di creare un elettore all'interno del database.
    /**
     * @param usernam
     * @param password
     * @param codiceFiscale
     */
    public void createElettore(String usernam, String password, String codiceFiscale);
    
    //Permette di restituire la persona dato il codice fiscale.
    /**
     * @param codiceFiscale
     * @return
     */
    public Persona getPersona(String codiceFiscale);
    
    //Permette di restituire l'elettore dato il codice fiscale.
    /**
     * @param codiceFiscale
     * @return
     */
    public Elettore getElettore(String codiceFiscale);
    
    //Permette ad un Elettore di effettuare il login al sistema.
    /**
     * @param username
     * @param password
     * @return
     * @throws IOException
     */
    public Elettore loginElettore(String username, String password) throws IOException;
    
    //Permette di restituire, dato l'id di un Elettore, tutte le sessioni a cui deve ancora votare.
    /**
     * @param elettoreId
     * @return
     */
    public List<Sessione> getSessioniElettore(Integer elettoreId);
    
}
