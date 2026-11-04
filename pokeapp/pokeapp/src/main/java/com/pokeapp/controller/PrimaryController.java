package com.pokeapp.controller;

import com.pokeapp.App;
import com.pokeapp.GameSession;
import javafx.fxml.FXML;
import java.io.IOException;

public class PrimaryController {
    @FXML
    private void comenzar() throws IOException {
        GameSession.limpiar();
        App.setRoot("team");
    }

    @FXML
    private void salir() {
        javafx.application.Platform.exit();
    }
}
