/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import org.fazioMonchieri.models.Referendum;

public interface ReferendumDAO {
    
    //Permette di creare una nuova riga all'interno del database Referendum.
    /**
     * @param id
     * @param quesito
     */
    public void createReferendum(Integer id, String quesito);
    
    //Permette di votare ad un referedum in una sessione.
    /**
     * @param id
     * @param voto
     */
    public void votaReferendum(Integer id, Boolean voto);

    //Permette di restituire il referendum dato l'id della sessione.
    /**
     * @param id
     * @return
     */
    public Referendum getReferendum(Integer id);
    
}
