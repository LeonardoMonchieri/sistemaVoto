package org.fazioMonchieri.controllers;

import org.fazioMonchieri.App;
import org.fazioMonchieri.utilities.Controller;

/* 
import org.manuelelucchi.common.Controller;
import org.manuelelucchi.models.Persona;*/

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class NavigationController  extends Controller{
    
    @FXML
    public Label codeLabel;

    private Persona persona;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        persona = (Persona) parameter;
    }

    @Override
    public void init() {
        codeLabel.setText("" + persona.getCodiceFiscale());
    }

    @FXML
    public void next() {
        navigate("HomeView", persona);
    }

    @FXML
    public void home() {
        navigate("HomeView");
    }
}
