/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.util.List;
import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.models.Persona;

public interface CandidatoDAO {
    
    //Permette di restituire il nome completo del candidato, dato il suo id.
    public String getNomeCompleto(Integer id);
    
    //Permette di restituire il partito del candidato, dato il suo codice fiscale.
    public Partito getPartito(String codiceFiscale);
    
    //Permette di restituire la persona dato il codice fiscale.
    public Persona getPersona(String codiceFiscale);
    
    //Permette di restituire tutti i candidati.
    public List<Candidato> getCandidati();
    
}