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
    public void votaPartitoOrdinale(String id, String elettoreId, List<String> partitiId) throws IOException;
    
    //Permette di votare ad una votazione categorica di partiti in una sessione.
    public void votaPartitoCategorico(String id, String elettoreId, String partitoId) throws IOException;
    
    
    
    
    
}
