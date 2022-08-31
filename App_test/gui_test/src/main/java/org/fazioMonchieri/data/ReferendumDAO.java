/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

/**
 *
 * @author franc
 */
public interface ReferendumDAO {
    
    //Permette di votare ad un referedum in una sessione.
    public void votaReferendum(String id, String elettoreId, Boolean voto);
    
}
