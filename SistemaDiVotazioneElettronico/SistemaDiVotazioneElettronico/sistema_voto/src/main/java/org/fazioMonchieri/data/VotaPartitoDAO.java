/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.io.IOException;
import java.util.List;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.models.VotiPartito;

public interface VotaPartitoDAO {

    //Permette di restituire il tipo VotiPartito dato l'id del partito.
    /**
     * @param id
     * @return
     */
    public VotiPartito getVotiPartito(Integer id);
    
    //Permette di aggiungere una riga al database VotiPartito.
    /**
     * @param idSessione
     * @param idPartito
     */
    public void createVotaPartito(Integer idSessione, Integer idPartito);
    
    //Permette di votare ad una votazione ordinale di partiti in una sessione.
    /**
     * @param SesisonId
     * @param partiti
     * @throws IOException
     */
    public void votaPartitoOrdinale(Integer SesisonId, List<Partito> partiti) throws IOException;
    
    //Permette di votare ad una votazione categorica di partiti in una sessione.
    /**
     * @param SesisonId
     * @param partito
     * @throws IOException
     */
    public void votaPartitoCategorico(Integer SesisonId, Partito partito) throws IOException;
    
}
