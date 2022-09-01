/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

public interface VotaDAO {
    
    //Permette di dire se un elettore ha votato o meno per una sessione.
    public Boolean hasVoted(Integer idElettore, Integer idSessione);
            
}
