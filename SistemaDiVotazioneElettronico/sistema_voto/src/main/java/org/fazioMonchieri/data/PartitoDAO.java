/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import org.fazioMonchieri.models.Partito;

public interface PartitoDAO {

    //Permette di restituire true se l'id passato é un partito esitente.
    /**
     * @param id
     * @return
     */
    public Boolean isPartito(Integer id);

    //Permette di creare un nuovo partito nel database.
    /**
     * @param nome
     * @param dataFondazione
     */
    public void createPartito(String nome, Date dataFondazione) throws SQLException;
    
    //Permette di restituire tutti i partiti.
    /**
     * @return
     */
    public List<Partito> getPartiti();
    
}
