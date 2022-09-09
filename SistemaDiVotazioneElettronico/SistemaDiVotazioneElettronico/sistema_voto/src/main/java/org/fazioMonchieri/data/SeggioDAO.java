/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.io.IOException;
import org.fazioMonchieri.models.Seggio;

public interface SeggioDAO {
    
    //Permette di eseguire il login sul seggio.
    /**
     * @param id
     * @param password
     * @return
     * @throws IOException
     */
    public Seggio loginSeggio(Integer id, String password) throws IOException;
    
}
