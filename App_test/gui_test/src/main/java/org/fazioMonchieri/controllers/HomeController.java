package org.fazioMonchieri.controllers;

import org.fazioMonchieri.utilities.Controller;

import javafx.fxml.FXML;



public class HomeController extends Controller{

    @FXML
    public void userLogin() {
        navigate("UserLoginView");
    }

    @FXML
    public void seggioLogin() {
        navigate("SeggiolocaleLoginView");
    }
}