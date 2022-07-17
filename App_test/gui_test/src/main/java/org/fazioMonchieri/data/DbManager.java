package org.fazioMonchieri.data;

import org.fazioMonchieri.models.*;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
;

public class DbManager {
    private static DbManager _instance;

    private static String databaseUrl = "jdbc:mysql://localhost/sistemavotoelettronico";
    private ConnectionSource source;

    private Dao<Persona, String> persone;
    private Dao<Elettore, String> elettori;
    private Dao<Gestore, String> gestori;
    private Dao<Sessione, String> sessioni;
    private Dao<Referendum, String> referendum;
    private Dao<VotiCandidati, String> votiCandidati;
    private Dao<VotiPartiti, String> votiPartiti;
    private Dao<Candidato, String> candidati;
    private Dao<Partito, String> partiti;

    private DbManager() {
    }

    public static DbManager getInstance() {
        if (_instance == null) {
            _instance = new DbManager();
        }
        return _instance;
    }

    public boolean ensureCreated() {
        if (!open())
            return false;

        try {
            TableUtils.createTableIfNotExists(source, Persona.class);
            TableUtils.createTableIfNotExists(source, Elettore.class);
            TableUtils.createTableIfNotExists(source, Gestore.class);
            TableUtils.createTableIfNotExists(source, Sessione.class);
            TableUtils.createTableIfNotExists(source, Referendum.class);
            TableUtils.createTableIfNotExists(source, VotiCandidati.class);
            TableUtils.createTableIfNotExists(source, VotiPartiti.class);
            TableUtils.createTableIfNotExists(source, Candidato.class);
            TableUtils.createTableIfNotExists(source, Partito.class);

            persone = DaoManager.createDao(source, Persona.class);
            elettori = DaoManager.createDao(source, Elettore.class);
            gestori = DaoManager.createDao(source, Gestore.class);
            sessioni = DaoManager.createDao(source, Sessione.class);
            referendum = DaoManager.createDao(source, Referendum.class);
            votiCandidati = DaoManager.createDao(source, VotiCandidati.class);
            votiPartiti = DaoManager.createDao(source, VotiPartiti.class);
            candidati = DaoManager.createDao(source, Candidato.class);
            partiti = DaoManager.createDao(source, Partito.class);

            return true;
        }catch (SQLException e) {
            return false;
        }
    }

    public boolean open() {
        try {
            source = new JdbcConnectionSource(databaseUrl);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean close() {
        try {
            source.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    //@REQUIRES: password != null
    public Persona login(String cF, String password) {
        try {
            var login = Persona.queryForId(cF);
            if (login == null) {
                return null;
            }
            return Persona.getPassword().equals(password) ? login : null;
        } catch (SQLException e) {
            return null;
        }
    }

    //REQUIRES: sessionId != null 
    //REQUIRES: password != null
    public boolean createSession(String sessionId, TipoSessione tipoSessione, TipoScrutinio TipoScrutinio, String password, Gestore gestore){
        try{
            var sessione = new Sessione(sessionId, tipoSessione, TipoScrutinio, password, gestore);
            sessioni.create(sessione);
        } catch(SQLException e) {
            return false;
        }
    }

    public void openSession(String id){
        var sessione = sessioni.queryForId(id);
        if(sessione == null){
            //Error
        }
        sessione.open();
        sessioni.refresh(sessione);
        sessioni.update(sessione);
    }

    public void closeSession(String id){
        var sessione = sessioni.queryForId(id);
        if(sessione == null){
            //Error
        }
        sessione.close();
        sessioni.refresh(sessione);
        sessioni.update(sessione);
    }

    public void votaReferendum(String sessioneId, boolean voto){
        var sessione = sessioni.queryForId(sessioneId);
        if(sessione == null){
            //Error
        }
        var r = referendum.queryForId(sessioneId);
        r.setVoto(voto);
    }

    public void votaPartitoOrdinale(String sessioneId, List<String> partitiId){
        
    }

    public void votaCandidatoOrdinale(String sessioneId, List<String> candidatiId){
        
    }

    public void votaPartitoCategorico(String sessioneId, String partitoId){
        
    }

    public void votaCandidatoCategorico(String sessioneId, String candidatoId){
        
    }

    public Object scrutinio(String sessioneId){
        var sessione = sessioni.queryForId(sessioneId);
        if(sessione == null){
            //Error
        }

        if(sessione.getTipologiaScrutinio() == TipoScrutinio.maggioranza){
            var p = votiPartiti.queryForId(sessioneId);
            var maxP = p.stream().max(new Comparator<VotiPartiti>(){
                @Override
                public int compare(VotiPartiti p1, VotiPartiti p2) {
                    return Integer.compare(Integer.valueOf(p1.getNumeroVoti()), Integer.valueOf(p2.getNumeroVoti()));
                }
            }).get().getNumeroVoti();

            var c = votiCandidati.queryForId(sessioneId);
            if(c.getCandidato().getPartito().getNome().equals(maxP.getPartito().getNome())){
                var maxC = c.stream().max(new Comparator<VotiCandidati>(){
                    @Override
                    public int compare(VotiCandidati c1, VotiCandidati c2) {
                        return Integer.compare(Integer.valueOf(c1.getNumeroVoti()), Integer.valueOf(c2.getNumeroVoti()));
                    }
                }).get().getNumeroVoti();
            }

            return maxC.getCandidato();

        }
        else if(sessione.getTipologiaScrutinio() == TipoScrutinio.maggioranzaAssoluta){
            var p = votiPartiti.queryForId(sessioneId);
            var maxP = p.stream().max(new Comparator<VotiPartiti>(){
                @Override
                public int compare(VotiPartiti p1, VotiPartiti p2) {
                    return Integer.compare(Integer.valueOf(p1.getNumeroVoti()), Integer.valueOf(p2.getNumeroVoti()));
                }
            }).get().getNumeroVoti();

            var c = votiCandidati.queryForId(sessioneId);
            if(c.getCandidato().getPartito().getNome().equals(maxP.getPartito().getNome())){
                var maxC = c.stream().max(new Comparator<VotiCandidati>(){
                    @Override
                    public int compare(VotiCandidati c1, VotiCandidati c2) {
                        return Integer.compare(Integer.valueOf(c1.getNumeroVoti()), Integer.valueOf(c2.getNumeroVoti()));
                    }
                }).get().getNumeroVoti();
            }
            if(maxC.getNumeroVoti() >= (0.5 * sessione.getVotiTotali()) + 1)
                return maxC.getCandidato();
            else 
                return null;
        }
        else if(sessione.getTipologiaScrutinio() == TipoScrutinio.referendumSenzaQuorum){
            var r = referendum.queryForId(sessioneId);
            if(r.getVotiSi() >= r.getVotiNo())
                return "si";
            else
                return "no";
        }   
        else if(sessione.getTipologiaScrutinio() == TipoScrutinio.referendumConQuorum){
            var r = referendum.queryForId(sessioneId);
            if((r.getVotiNo() + r.getVotiSi()) >= (0.5 * sessione.getNumeroVoti()) + 1){
                if(r.getVotiSi() >= r.getVotiNo())
                    return "si";
                else
                    return "no";
            }
        }     
    }

}