package com.pokeapp.controller;
import com.pokeapp.App;
import com.pokeapp.GameSession;
import com.pokeapp.model.*;
import com.pokeapp.service.CombateService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class BattleController {
    @FXML private Label rivalNombre;
    @FXML private Label rivalVida;
    @FXML private Label jugadorNombre;
    @FXML private Label jugadorVida;
    @FXML private TextArea estado;
    @FXML private Label tituloResultado;
    @FXML private ComboBox<PokemonJugador> selectorPokemon;
    @FXML private VBox listaEquipoJugador;
    @FXML private VBox listaEquipoRival;
    @FXML private Button botonAtacar;
    private final CombateService combateService = new CombateService();

    @FXML public void initialize() {
        cargarEquipo();
        actualizarPantalla();
    }
    private void cargarEquipo() {
        for (PokemonJugador pokemon : GameSession.partida.getEquipoJugador()) {
            selectorPokemon.getItems().add(pokemon);
        }
        selectorPokemon.getSelectionModel().selectFirst();
        selectorPokemon.setOnAction(event -> actualizarPantalla());
    }
    @FXML private void atacar() {
        PokemonJugador seleccionado = selectorPokemon.getValue();
        if (seleccionado == null) return;
        int indice = GameSession.partida.getEquipoJugador().indexOf(seleccionado);
        GameSession.combate.setPokemonJugadorActual(indice);
        if (seleccionado.estaDerrotado()) {
            estado.setText("Ese Pokémon está debilitado. Elegí otro.");
            return;
        }
        estado.setText(combateService.atacar(GameSession.combate, indice));
        actualizarPantalla();
    }
    private void actualizarPantalla() {
        if (GameSession.combate == null) return;
        Combate combate = GameSession.combate;
        PokemonRival rival = combateService.obtenerRivalActivo(combate);
        PokemonJugador jugador = selectorPokemon.getValue();
        if (rival == null) {
            rivalNombre.setText("¡Equipo rival derrotado!");
            rivalVida.setText("0 / 0 HP");
        } else {
            rivalNombre.setText(rival.getNombre() + "  (" + rival.getTipoPrimario() + ")");
            rivalVida.setText(rival.getHpActual() + " / " + rival.getHpMaximo() + " HP");
        }
        if (jugador != null) {
            jugadorNombre.setText(jugador.getNombre() + (jugador.estaDerrotado() ? " - DEBILITADO" : ""));
            jugadorVida.setText(jugador.getHpActual() + " / " + jugador.getHpMaximo() + " HP");
        }
        listaEquipoJugador.getChildren().clear();
        for (PokemonJugador pokemon : GameSession.partida.getEquipoJugador()) {
            listaEquipoJugador.getChildren().add(new Label((pokemon.estaDerrotado() ? "✖ " : "● ")
                    + pokemon.getNombre() + "  " + pokemon.getHpActual() + "/" + pokemon.getHpMaximo() + " HP"));
        }
        listaEquipoRival.getChildren().clear();
        for (PokemonRival pokemon : GameSession.partida.getEquipoRival()) {
            listaEquipoRival.getChildren().add(new Label((pokemon.estaDerrotado() ? "✖ " : "● ")
                    + pokemon.getNombre() + "  " + pokemon.getHpActual() + "/" + pokemon.getHpMaximo() + " HP"));
        }
        boolean terminado = !"EN_PROCESO".equals(combate.getResultado());
        botonAtacar.setDisable(terminado || jugador == null || jugador.estaDerrotado() || rival == null);
        selectorPokemon.setDisable(terminado);
        if (terminado) {
            tituloResultado.setText("VICTORIA".equals(combate.getResultado()) ? "¡GANASTE!" : "DERROTA");
            botonAtacar.setText("Combate finalizado");
        }
    }
    @FXML private void volverMenu() throws IOException {
        GameSession.limpiar();
        App.setRoot("primary");
    }
}
