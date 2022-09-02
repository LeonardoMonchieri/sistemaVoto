/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.io.IOException;
import java.util.List;
import org.fazioMonchieri.models.Candidato;

public interface VotaCandidatoDAO {
    
    //Permette di aggiungere una riga al database VotiCandidato.
    public void createVotaCandidato(Integer idSessione, Integer idCandidato);
    
    //Permette di votare ad una votazione ordinale di candidati in una sessione.
    public void votaCandidatoOrdinale(Integer id, Integer elettoreId, List<Candidato> candidati) throws IOException;
    
    //Permette di votare ad una votazione categorica di candidati in una sessione.
    public void votaCandidatoCategorico(Integer id, Integer elettoreId, Candidato candidato) throws IOException;
    
}
