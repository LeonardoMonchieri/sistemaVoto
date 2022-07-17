package org.fazioMonchieri.data;

import org.fazioMonchieri.models.*;

public class SessionManager {
    private static SessionManager _instance;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (_instance == null) {
            _instance = new SessionManager();
        }
        return _instance;
    }

    public boolean openSession(Sessione sessione) {
        return true; 
    }

    public boolean closeSession(Sessione sessione) {
        return true; 
    }
    
}