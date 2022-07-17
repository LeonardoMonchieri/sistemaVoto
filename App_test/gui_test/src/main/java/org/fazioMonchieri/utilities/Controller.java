package org.fazioMonchieri.utilities;

import java.io.IOException;

import org.fazioMonchieri.App;
//import org.fazioMonchieri.data.DbManager;
//import org.fazioMonchieri.models.Totem;

public abstract class Controller {
    public void init() {

    }

    public void onNavigateFrom(Controller sender, Object parameter) {

    }

    public boolean isFirstHomeLoad() {
        return !App.homeLoaded;
    }

    public void navigate(String view, Object parameter) {
        try {
            App.navigate(this, view, parameter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigate(String view) {
        navigate(view, null);
    }
}