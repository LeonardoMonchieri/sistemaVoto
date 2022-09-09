/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.io.IOException;
import java.util.List;
import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.VotiCandidato;

public interface VotaCandidatoDAO {

    //Permette di restituire il tipo VotiCandidato dato l'id del candidato.
    /**
     * @param id
     * @return
     */
    public VotiCandidato getVotiCandidato(Integer id);
    
    //Permette di aggiungere una riga al database VotiCandidato.
    /**
     * @param idSessione
     * @param idCandidato
     */
    public void createVotaCandidato(Integer idSessione, Integer idCandidato);
    
    //Permette di votare ad una votazione ordinale di candidati in una sessione.
    /**
     * @param idSessione
     * @param candidati
     * @throws IOException
     */
    public void votaCandidatoOrdinale(Integer idSessione, List<Candidato> candidati) throws IOException;
    
    //Permette di votare ad una votazione categorica di candidati in una sessione.
    /**
     * @param idSessione
     * @param candidato
     * @throws IOException
     */
    public void votaCandidatoCategorico(Integer idSessione, Candidato candidato) throws IOException;
    
}
