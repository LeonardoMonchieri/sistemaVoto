package com.example;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import org.fazioMonchieri.data.ImplCandidatoDAO;
import org.fazioMonchieri.data.ImplElettoreDAO;
import org.fazioMonchieri.data.ImplGestoreDAO;
import org.fazioMonchieri.data.ImplPartitoDAO;
import org.fazioMonchieri.data.ImplSeggioDAO;
import org.fazioMonchieri.data.ImplSessioneDAO;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DbTest{

  private static ImplGestoreDAO gestoreDAO = ImplGestoreDAO.getInstance();
  private static ImplElettoreDAO elettoreDAO = ImplElettoreDAO.getInstance();
  private static ImplSeggioDAO seggioDAO = ImplSeggioDAO.getInstance();
  private static ImplPartitoDAO partitoDAO = ImplPartitoDAO.getInstance();
  private static ImplCandidatoDAO candidatoDAO = ImplCandidatoDAO.getInstance();
  private static ImplSessioneDAO sessioneDAO = ImplSessioneDAO.getInstance();

  @DisplayName("Test Login Gestore")
  @Test
  void testLoginGestore() throws IOException {
    gestoreDAO.loginGestore("Gestore1", "Gestore1");
  }

  @DisplayName("Test Login Elettore")
  @Test
  void testLoginElettore() throws IOException {
    elettoreDAO.loginElettore("Elettore1", "Elettore1");
  }

  @DisplayName("Test Login Seggio")
  @Test
  void testLoginSeggio() throws IOException {
    seggioDAO.loginSeggio(1, "Seggio1");
  }

  @DisplayName("Test Creazione Utente")
  @Test
  void testCreazioneUtente() {
    elettoreDAO.createElettore("Elettore2", "Elettore2", "RSSMRA80A01C933H");
  }

  @DisplayName("Test Creazione Partito e Candidato")
  @Test
  void testCreazionePartitoCandidato() {
    partitoDAO.createPartito("Partito1", Date.valueOf(LocalDate.now()));
    candidatoDAO.createCandidato("Ruolo1", "RSSMRA80A01C933H", "Partito1");
  }

  @DisplayName("Test Creazione Sessione")
  @Test
  void testCreazioneSessione() {
    sessioneDAO.createSessione("Sessione1", TipoSessione.votoCategorico, TipoScrutinio.maggioranza, 1);
  }

  @DisplayName("Test Apertura Sessione")
  @Test
  void testAperturaSessione() {
    sessioneDAO.openSessione(1, 1);
  }

  @DisplayName("Test Chiusura Sessione")
  @Test
  void testChiusuraSessione() {
    sessioneDAO.closeSessione(1, 1);
  }

}
