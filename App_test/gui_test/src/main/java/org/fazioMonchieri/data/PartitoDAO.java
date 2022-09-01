/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.io.IOException;
import java.util.List;

public interface PartitoDAO {
    
    //Permette di votare ad una votazione ordinale di partiti in una sessione.
    public void votaPartitoOrdinale(Integer id, Integer elettoreId, List<Integer> partitiId) throws IOException;
    
    //Permette di votare ad una votazione categorica di partiti in una sessione.
    public void votaPartitoCategorico(Integer id, Integer elettoreId, Integer partitoId) throws IOException;
    
    //Permette di votare ad una votazione categorica con preferenza in una sessione.
    public void votaCategoricoConPreferenza(Integer id, Integer elettoreId, Integer partitoId, Integer candidatoId);
    
}
