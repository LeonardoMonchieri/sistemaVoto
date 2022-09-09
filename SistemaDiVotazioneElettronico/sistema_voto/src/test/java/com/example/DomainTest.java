package com.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.fazioMonchieri.data.ImplElettoreDAO;
import org.fazioMonchieri.data.ImplSessioneDAO;
import org.fazioMonchieri.models.Elettore;
import org.fazioMonchieri.models.TipoScrutinio;
import org.fazioMonchieri.models.TipoSessione;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DomainTest{

  private static ImplElettoreDAO elettoreDAO = ImplElettoreDAO.getInstance();
  private static ImplSessioneDAO sessioneDAO = ImplSessioneDAO.getInstance();

  @DisplayName("Test Utente Maggiorenne")
  @Test
  void testUtenteMaggiorenne() throws IOException {
    Elettore e = elettoreDAO.getElettore("VRDMRA80A41C933H");
    assertTrue(e.isMaggiorenne() == true);
  }

  @DisplayName("Test Integrita' Stato Sessione")
  @Test
  void testStatoSessione() throws IOException {
    sessioneDAO.createSessione("Sessione2", TipoSessione.referendum, TipoScrutinio.referendumConQuorum, 1);
    sessioneDAO.openSessione(sessioneDAO.getId("Sessione2"), 1);
    assertTrue(sessioneDAO.getSessione(sessioneDAO.getId("Sessione2")).getDataApertura() != null);
    sessioneDAO.closeSessione(sessioneDAO.getId("Sessione2"), 1);
    assertTrue(sessioneDAO.getSessione(sessioneDAO.getId("Sessione2")).getDataChiusura() != null);
  }

}