/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.io.IOException;
import java.util.List;

public interface CandidatoDAO {
    
    //Permette di votare ad una votazione ordinale di candidati in una sessione.
    public void votaCandidatoOrdinale(String id, String elettoreId, List<String> candidatiId) throws IOException;
    
    //Permette di votare ad una votazione categorica di candidati in una sessione.
    public void votaCandidatoCategorico(String id, String elettoreId, String candidatoId) throws IOException;
    
}
