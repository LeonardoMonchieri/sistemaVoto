/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fazioMonchieri.data;

import java.util.List;
import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;

public interface SessioneDAO {
    
    //Permette di aprire una sessione.
    void openSessione(Integer id, Integer gestoreId);
    
    //Permette di chiudere una sessione.
    void closeSessione(Integer id, Integer gestoreId);
    
    //Permette di creare una sessione.
    void createSessione(Integer id, String nome, TipoSessione tipoSessione, TipoScrutinio tipoScrutinio, String password, int elettori, Integer gestoreId);
    
    //Restituisce tutti i partiti dato l'id di una sessione.
    public List<Partito> getPartiti(String id);
    
    //Restituisce tutti i candidati dato l'id di una sessione.
    public List<Candidato> getCandidati(String id);
    
    //Restituisce il numero dei votanti dato l'id di una sessione.
    public int getVotanti(String id);
    
    //Permette di effettuare lo scrutinio di maggioranza sulla sessione.
    public String scrutinioMaggioranza(String id, String gestoreId);
    
    //Permette di effettuare lo scrutinio di maggioranza assoluta sulla sessione.
    public String scrutinioMaggioranzaAssoluta(String id, String gestoreId);
    
    //Permette di effettuare lo scrutinio di rferendum senza quorum sulla sessione.
    public String scrutinioReferendumSenzaQuorum(String id, String gestoreId);
    
    //Permette di effettuare lo scrutinio di rferendum con quorum sulla sessione.
    public String scrutinioReferendumConQuorum(String id, String gestoreId);
    
    //Permette di salvare nel database la votazione avvenuta.
    public void votazione(String elettoreId, String sessioneId);
    
}
