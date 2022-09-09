/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.util.List;
import java.util.Map;

import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Gestore;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;

public interface SessioneDAO {
    
    //Permette di aprire una sessione.
    /**
     * @param id
     * @param gestoreId
     */
    void openSessione(Integer id, Integer gestoreId);
    
    //Permette di chiudere una sessione.
    /**
     * @param id
     * @param gestoreId
     */
    void closeSessione(Integer id, Integer gestoreId);
    
    //Permette di creare una sessione.
    /**
     * @param nome
     * @param tipoSessione
     * @param tipoScrutinio
     * @param gestoreId
     */
    void createSessione(String nome, TipoSessione tipoSessione, TipoScrutinio tipoScrutinio, Integer gestoreId);
    
    //Permette di restituire una sessione.
    /**
     * @param id
     * @return
     */
    Sessione getSessione(Integer id);

    //Permette di restituire l'id della sessione dato il nome.
    /**
     * @param nome
     * @return
     */
    public Integer getId(String nome);

    //Restituisce tutti i partiti dato l'id di una sessione.
    /**
     * @param id
     * @return
     */
    public List<Partito> getPartiti(Integer id);
    
    //Restituisce tutti i candidati dato l'id di una sessione.
    /**
     * @param id
     * @return
     */
    public List<Candidato> getCandidati(Integer id);
    
    //Restituisce il gestore dato l'id di una sessione.
    /**
     * @param id
     * @return
     */
    public Gestore getGestore(Integer id);
    
    //Restituisce il numero dei votanti dato l'id di una sessione.
    /**
     * @param id
     * @return
     */
    public int getVotanti(Integer id);
    
    //Restituisce il numero degli elettori dato l'id di una sessione.
    /**
     * @return
     */
    public int getElettori();
    
    //Restituisce tutte le sessioni aperte.
    /**
     * @return
     */
    public List<Sessione> getOpenSession();
    
    //Restituisce il quesito del refendum.
    /**
     * @param id
     * @return
     */
    public String getQuesitoReferendum(Integer id);
    
    //Permette di eliminare una sessione dato il suo id.
    /**
     * @param id
     */
    public void delete(Integer id);
    
    //Permette di effettuare lo scrutinio di maggioranza sulla sessione.
    /**
     * @param id
     * @return
     */
    public Map<Integer, Integer> scrutinioMaggioranza(Integer id);
    
    //Permette di effettuare lo scrutinio di maggioranza assoluta sulla sessione.
    /**
     * @param id
     * @return
     */
    public Map<Integer, Integer> scrutinioMaggioranzaAssoluta(Integer id);
    
    //Permette di effettuare lo scrutinio di rferendum senza quorum sulla sessione.
    /**
     * @param id
     * @return
     */
    public Map<Integer, Integer> scrutinioReferendumSenzaQuorum(Integer id);
    
    //Permette di effettuare lo scrutinio di rferendum con quorum sulla sessione.
    /**
     * @param id
     * @return
     */
    public Map<Integer, Integer> scrutinioReferendumConQuorum(Integer id);
    
    //Permette di salvare nel database la votazione avvenuta.
    /**
     * @param elettoreId
     * @param sessioneId
     */
    public void votazione(Integer elettoreId, Integer sessioneId);
    
}
