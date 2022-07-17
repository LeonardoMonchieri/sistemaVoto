package org.fazioMonchieri.controllers;

import org.fazioMonchieri.utilities.Controller;



import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeController extends Controller{
    
    
    @FXML
    public Button session;

    @FXML
    public Button user;

    @FXML
    public void userLogin() {
        navigate("LoginUserView");
    }

    @FXML
    public void sessionLogin() {
        navigate("LoginSessionView");
    }
}