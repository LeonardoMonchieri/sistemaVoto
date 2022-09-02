package org.fazioMonchieri.controllers;

import org.fazioMonchieri.models.Referendum;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoSessione;
import org.fazioMonchieri.utilities.Controller;
import org.fazioMonchieri.data.ImplCandidatoDAO;
import org.fazioMonchieri.data.ImplSessioneDAO;
import org.fazioMonchieri.models.Candidato;
import org.fazioMonchieri.models.Partito;
import org.fazioMonchieri.models.Persona;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.AnchorPane;


import javafx.scene.control.Button;


import javafx.scene.chart.PieChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Iterator;

public class SessionController extends Controller {

    private Sessione sessione;

    private ImplSessioneDAO sessioneDAO;

    @FXML
    private Label sessionTitle;

    @FXML
    private Label sessionId;

    @FXML
    private Label sessionPw;

    @FXML
    private Label sessionAdmin;

    @FXML
    private Label sessionVotingType;

    @FXML
    private Label sessionBallotType;

    @FXML
    private Label quesitoCandidati;

    @FXML
    private Label question;

    @FXML
    private Label sessionOpenDate;

    @FXML
    private Label sessionCloseLabel;

    @FXML
    private Label winner;

    @FXML
    private Button OpenClose;

    @FXML
    private PieChart pieChartTurnOut;

    @FXML
    private AnchorPane pane;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.sessione = (Sessione) parameter;
        this.sessioneDAO = ImplSessioneDAO.getInstance();
    }

    @Override
    public void init() {
        sessionTitle.setText(sessione.getNome());
        sessionId.setText("Session id: " + sessione.getId());
        sessionAdmin.setText("Gestore: " + sessione.getIdGestore());


        TipoSessione sessionType = this.sessione.getTipoSessione();
        if (sessionType == TipoSessione.referendum) {
            sessionVotingType.setText("Tipo di votazione: referendum");
            quesitoCandidati.setText("Quesito:");
            question.setText("\"" + sessioneDAO.getQuesitoReferendum(this.sessione.getId()) + "\"");
        } else {
            quesitoCandidati.setText("Candidati:");
            if (sessionType == TipoSessione.votoCategorico || sessionType == TipoSessione.votoOrdinale) {
                if (sessioneDAO.getPartiti(this.sessione.getId()) == null)
                    candidateTabeleBuilder();
                else
                    partyTableBuilder();
                if (sessionType == TipoSessione.votoCategorico)
                    sessionVotingType.setText("Tipo di votazione: voto ordinale");
                else
                sessionVotingType.setText("Tipo di votazione: voto categorico");
            } else if (sessionType == TipoSessione.votoCategoricoPreferenza) {
                sessionVotingType.setText("Tipo di votazione: voto categorico preferenza");
                candidateTabeleBuilder();
            }
        }

        switch (sessione.getTipoScrutinio()) {
            case maggioranza:
                sessionBallotType.setText("Tipo di balotaggio: maggioranza");
                break;
            case maggioranzaAssoluta:
                sessionBallotType.setText("Tipo di balotaggio: maggioranza assoluta");
                break;
            case referendumSenzaQuorum:
                sessionBallotType.setText("Tipo di balotaggio: referendum senza quorum");
                break;
            case referendumConQuorum:
                sessionBallotType.setText("Tipo di balotaggio: referendum con quorum");
                break;
        }

      
        String pattern = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        if (this.sessione.status() == 0)
            OpenClose.setText("Apri");
        else {
            sessionOpenDate.setText("Data apertura: " + simpleDateFormat.format(this.sessione.getDataApertura()));
            buildPieChart();

            if (this.sessione.status() == 1) {
                OpenClose.setOpacity(0);
                OpenClose.setDisable(true);
                winner.setDisable(false);
                winner.setText("Vincitore:");
                sessionCloseLabel.setText("Data chiusura: " + simpleDateFormat.format(this.sessione.getDataChiusura()));
            } else if (this.sessione.status() == 2)
                OpenClose.setText("Chiudi");

        }
    }

    @FXML
    public void back() {
        navigate("GestoreView", sessioneDAO.getGestore(this.sessione.getId()));
    }

    @FXML
    public void updateSessionState(){
        if (this.sessione.status() == 0) sessioneDAO.openSessione(this.sessione.getId(), this.sessione.getIdGestore()); //Apri la sessione
        else if(this.sessione.status() == 2) sessioneDAO.closeSessione(this.sessione.getId(), this.sessione.getIdGestore()); //Chiudi sessione
        navigate("SessionView", this.sessione);
    }

    //Table maker
    private void candidateTabeleBuilder() {
        ImplCandidatoDAO candidatoDAO=ImplCandidatoDAO.getInstance();

        List<Candidato> c = sessioneDAO.getCandidati(this.sessione.getId());
        Iterator<Candidato> ic = c.iterator();

        TableView<Candidato> candidateTable = new TableView<Candidato>();
        TableColumn<Candidato, String> partito = new TableColumn<>("Partito/Gruppo");

        partito.setCellValueFactory(param -> new SimpleObjectProperty<>(candidatoDAO.getPartito(param.getValue().getId())));

        TableColumn<Candidato, String> candidato = new TableColumn<>("Candidato");

        candidato.setCellValueFactory(
                param -> new SimpleObjectProperty<>(candidatoDAO.getNomeCompleto(param.getValue().getId())));

        candidateTable.getColumns().add(partito);
        candidateTable.getColumns().add(candidato);

        while (ic.hasNext()) {
            candidateTable.getItems().add(ic.next());
        }

        candidateTable.setColumnResizePolicy(candidateTable.CONSTRAINED_RESIZE_POLICY);
        candidateTable.setLayoutX(260);
        candidateTable.setLayoutY(270);
        candidateTable.setPrefHeight(200);
        pane.getChildren().add(candidateTable);
    }

    private void partyTableBuilder() {
        System.out.println("partyBuilder");
        List<Partito> p = sessioneDAO.getPartiti(this.sessione.getId());
        Iterator<Partito> ip = p.iterator();

        TableView<Partito> partyTable = new TableView<Partito>();
        TableColumn<Partito, String> partito = new TableColumn<>("Partito/Gruppo");

        partito.setCellValueFactory(new PropertyValueFactory<Partito, String>("nome"));

        partyTable.getColumns().add(partito);

        while (ip.hasNext()) {
            partyTable.getItems().add(ip.next());
        }

        partyTable.setColumnResizePolicy(partyTable.CONSTRAINED_RESIZE_POLICY);
        partyTable.setLayoutX(260);
        partyTable.setLayoutY(270);
        partyTable.setPrefHeight(200);
        pane.getChildren().add(partyTable);
    }

    private void buildPieChart() {
        int votanti = this.sessioneDAO.getVotanti(this.sessione.getId());
        int nonVotanti = this.sessioneDAO.getElettori() - votanti ;
        ObservableList<PieChart.Data> turnOutData = FXCollections.observableArrayList(
                new PieChart.Data("Non votanti", nonVotanti),
                new PieChart.Data("Votanti", votanti));

        pieChartTurnOut.setData(turnOutData);
        pieChartTurnOut.setDisable(false);
        pieChartTurnOut.setTitle("Affluenza");
    }

    
}
