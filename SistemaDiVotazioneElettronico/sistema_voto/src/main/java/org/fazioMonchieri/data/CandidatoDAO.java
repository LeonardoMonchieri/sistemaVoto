/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.sql.SQLException;
import java.util.List;
import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.models.Persona;

public interface CandidatoDAO {

    //Permette di creare un candidato nel database.
    /**
     * @param ruolo
     * @param codiceFiscale
     * @param partito
     */
    public void createCandidato(String ruolo, String codiceFiscale, String partito);
    
    //Permette di restituire il nome completo del candidato, dato il suo id.
    /**
     * @param id
     * @return
     */
    public String getNomeCompleto(Integer id);
    
    //Permette di restituire il partito del candidato, dato il suo id.
    /**
     * @param id
     * @return
     */
    public Partito getPartito(Integer id);
    
    //Permette di restituire la persona dato il codice fiscale.
    /**
     * @param codiceFiscale
     * @return
     */
    public Persona getPersona(String codiceFiscale);
    
    //Permette di restituire tutti i candidati.
    /**
     * @return
     */
    public List<Candidato> getCandidati();
    
}
