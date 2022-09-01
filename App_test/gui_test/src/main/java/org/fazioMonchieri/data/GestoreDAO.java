/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.io.IOException;
import java.util.List;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Persona;
import org.fazioMonchieri.models.Sessione;

public interface GestoreDAO {
    
    //Permette di restituire la persona dato il codice fiscale.
    public Persona getPersona(String codiceFiscale);
    
    //Permette ad un Gestore di effettuare il login  al sistema.
    public Gestore loginGestore(String username, String password) throws IOException;
    
    //Permette di restituire, dato l'id di un Gestore, tutte le sessioni che ha creato.
    public List<Sessione> getSessioniGestore(Integer gestoreId);
    
}



