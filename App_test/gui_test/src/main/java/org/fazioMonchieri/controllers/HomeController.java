package org.fazioMonchieri.controllers;

import org.fazioMonchieri.utilities.Controller;

import javafx.fxml.FXML;



public class HomeController extends Controller{

    @FXML
    public void userLogin() {
        navigate("LoginUserView");
    }

    @FXML
    public void sessionLogin() {
        navigate("LoginSessionView");
    }
}