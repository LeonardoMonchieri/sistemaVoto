/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.io.IOException;
import java.util.List;
import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Partito;

public interface PartitoDAO {
    
    //Permette di restituire tutti i partiti.
    public List<Partito> getPartiti();
    
}
