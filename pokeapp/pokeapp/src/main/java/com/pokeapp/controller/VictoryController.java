package com.pokeapp.controller;

import com.pokeapp.App;
import com.pokeapp.GameSession;
import com.pokeapp.model.Partida;
import com.pokeapp.model.PokemonJugador;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;

/** Muestra el equipo que consiguió ganar la batalla. */
public class VictoryController {
    @FXML private Label nombreEntrenador;
    @FXML private Label resumenVictoria;
    @FXML private TilePane equipoCampeon;

    @FXML
    public void initialize() {
        Partida partida = GameSession.getPartida();
        if (partida == null || partida.getEquipoJugador() == null) {
            resumenVictoria.setText("No hay datos de una partida para mostrar.");
            return;
        }

        nombreEntrenador.setText("ENTRENADOR/A: " + partida.getJugador().getNombre().toUpperCase());
        resumenVictoria.setText("Tu equipo derrotó a los seis Pokémon rivales. ¡Sos el campeón de Pokémon Fury!");
        for (PokemonJugador pokemon : partida.getEquipoJugador()) {
            equipoCampeon.getChildren().add(crearTarjeta(pokemon));
        }
    }

    private VBox crearTarjeta(PokemonJugador pokemon) {
        VBox tarjeta = new VBox(5);
        tarjeta.getStyleClass().add("victory-pokemon-card");
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPrefSize(190, 178);

        ImageView arte = new ImageView(cargarImagen(pokemon.getSpriteKey() + ".png"));
        arte.setFitWidth(142);
        arte.setFitHeight(120);
        arte.setPreserveRatio(true);
        arte.setSmooth(false);

        Label nombre = new Label(pokemon.getNombre().toUpperCase());
        nombre.getStyleClass().add("victory-pokemon-name");
        Label tipo = new Label(pokemon.getTiposTexto());
        tipo.getStyleClass().add("victory-pokemon-type");
        Label vida = new Label("HP " + pokemon.getHpActual() + " / " + pokemon.getHpMaximo());
        vida.getStyleClass().add("victory-pokemon-hp");
        tarjeta.getChildren().addAll(arte, nombre, tipo, vida);
        return tarjeta;
    }

    private Image cargarImagen(String archivo) {
        String ruta = "/com/pokeapp/sprites/" + archivo;
        try (InputStream entrada = getClass().getResourceAsStream(ruta)) {
            return entrada == null ? null : new Image(entrada);
        } catch (Exception ex) {
            return null;
        }
    }

    @FXML
    private void volverMenu() throws IOException {
        GameSession.limpiar();
        App.setRoot("primary");
    }
}
