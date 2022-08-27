package org.fazioMonchieri.controllers;

import org.fazioMonchieri.models.Referendum;
import org.fazioMonchieri.models.Sessione;
import org.fazioMonchieri.models.TipoSessione;
import org.fazioMonchieri.utilities.Controller;
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
    }

    @Override
    public void init() {
        sessionTitle.setText(sessione.getNome());
        sessionId.setText("Session id: " + sessione.getId());
        sessionPw.setText("Nome: " + sessione.getPassword());
        sessionAdmin.setText("Gestore: " + sessione.getGestore().getUsername());


        TipoSessione sessionType = this.sessione.getTipoSessione();
        if (sessionType == TipoSessione.referendum) {
            sessionVotingType.setText("Tipo di votazione: referendum");
            quesitoCandidati.setText("Quesito:");
            question.setText("\"" + getRefQuesito(this.sessione.getId()) + "\"");
        } else {
            quesitoCandidati.setText("Candidati:");
            if (sessionType == TipoSessione.votoCategorico || sessionType == TipoSessione.votoOrdinale) {
                if (getPartito(0) == null)
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

        if (this.sessione.isClosed() == 0)
            OpenClose.setText("Apri");
        else {
            sessionOpenDate.setText("Data apertura: " + simpleDateFormat.format(this.sessione.getDataApertura()));
            buildPieChart();

            if (this.sessione.isClosed() == 1) {
                OpenClose.setOpacity(0);
                OpenClose.setDisable(true);
                winner.setDisable(false);
                winner.setText("Vincitore:");
                sessionCloseLabel.setText("Data chiusura: " + simpleDateFormat.format(this.sessione.getDataChiusura()));
            } else if (this.sessione.isClosed() == 2)
                OpenClose.setText("Chiudi");

        }
    }

    @FXML
    public void back() {
        navigate("GestoreView", this.sessione.getGestore());
    }

    @FXML
    public void updateSessionState(){
        if (this.sessione.isClosed() == 0) this.sessione.setOpen(); //Apri la sessione
        else if(this.sessione.isClosed() == 2) this.sessione.setClose(); //Chiudi sessione
        else{
            navigate("EsitiView", this.sessione.getGestore());
        }
        navigate("SessionView", this.sessione);
    }

    //Table maker
    private void candidateTabeleBuilder() {
        List<Candidato> c = getCandidati();
        Iterator<Candidato> ic = c.iterator();

        TableView<Candidato> candidateTable = new TableView<Candidato>();
        TableColumn<Candidato, String> partito = new TableColumn<>("Partito/Gruppo");

        partito.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPartito().getNome()));

        TableColumn<Candidato, String> candidato = new TableColumn<>("Candidato");

        candidato.setCellValueFactory(
                param -> new SimpleObjectProperty<>(param.getValue().getPersona().getCompleteName()));

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
        List<Partito> p = getPartito(0);
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
        int nonVotanti = getTotElettori() - this.sessione.getVotiTotali();
        ObservableList<PieChart.Data> turnOutData = FXCollections.observableArrayList(
                new PieChart.Data("Non votanti", nonVotanti),
                new PieChart.Data("Votanti", this.sessione.getVotiTotali()));

        pieChartTurnOut.setData(turnOutData);
        pieChartTurnOut.setDisable(false);
        pieChartTurnOut.setTitle("Affluenza");
    }

    // DB managaer function
    public String getRefQuesito(String sessionId) {
        Referendum refTest = new Referendum(this.sessione, "Vuoi abolire la schiavitú?");
        return refTest.getQuesito();
    }

    public int getTotElettori() {
        return 100;
    }

    public List<Candidato> getCandidati() {
        List<Candidato> c = new ArrayList<>();
        Partito p1 = new Partito("1234", "Lega Nord", new Date());
        Partito p2 = new Partito("4321", "Partito Democratico", new Date());
        Partito p3 = new Partito("5678", "Forza Italia", new Date());

        Persona pr1 = new Persona("SLVMTT73C09F205R", true, "Matteo", "Salvini", new Date(3, 9, 1973), "MI");
        Persona pr2 = new Persona("MLNGRG77A55H501C", false, "Giorgia", "Meloni", new Date(15, 1, 1977), "RM");
        Persona pr3 = new Persona("LTTNRC66P20H501D", true, "Enrico", "Letta", new Date(20, 8, 1966), "RM");

        Candidato c1 = new Candidato("1234", "Segretario", pr1, p1);
        Candidato c2 = new Candidato("4321", "Segretario", pr2, p2);
        Candidato c3 = new Candidato("5678", "Segretario", pr3, p3);

        c.add(c1);
        c.add(c2);
        c.add(c3);

        return c;
    }

    public List<Partito> getPartito(int i) {
        List<Partito> p = new ArrayList<>();

        Partito p1 = new Partito("1234", "Lega Nord", new Date());
        Partito p2 = new Partito("4321", "Partito Democratico", new Date());
        Partito p3 = new Partito("5678", "Forza Italia", new Date());

        p.add(p1);
        p.add(p2);
        p.add(p3);
        if (i == 0)
            return p;

        return null;

    }

   

}
