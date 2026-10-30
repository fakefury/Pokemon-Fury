package com.pokeapp.controller;
import com.pokeapp.App;
import com.pokeapp.GameSession;
import com.pokeapp.model.Pokemon;
import com.pokeapp.service.PartidaService;
import com.pokeapp.service.PokemonService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamController {
    @FXML private VBox listaPokemones;
    @FXML private Label contador;
    @FXML private TextField nombreEntrenador;
    @FXML private Label mensaje;
    private final PokemonService pokemonService = new PokemonService();
    private final List<CheckBox> casillas = new ArrayList<>();

    @FXML public void initialize() {
        for (Pokemon pokemon : pokemonService.obtenerCatalogo()) {
            CheckBox check = new CheckBox(pokemon.getNombre() + "  |  " + pokemon.getTipoPrimario()
                    + (pokemon.getTipoSecundario() == null ? "" : " / " + pokemon.getTipoSecundario())
                    + "  |  HP " + pokemon.getHpMaximo());
            check.setUserData(pokemon.getIdPokemon());
            check.setStyle("-fx-font-size: 15px; -fx-padding: 7 10 7 10;");
            check.selectedProperty().addListener((obs, antes, seleccionado) -> {
                if (seleccionado && contarSeleccionados() > 6) check.setSelected(false);
                actualizarContador();
            });
            casillas.add(check);
            listaPokemones.getChildren().add(check);
        }
        actualizarContador();
    }
    private int contarSeleccionados() {
        int total = 0;
        for (CheckBox check : casillas) if (check.isSelected()) total++;
        return total;
    }
    private void actualizarContador() { contador.setText("Seleccionados: " + contarSeleccionados() + " / 6"); }

    @FXML private void confirmarEquipo() throws IOException {
        List<Integer> ids = new ArrayList<>();
        for (CheckBox check : casillas) if (check.isSelected()) ids.add((Integer) check.getUserData());
        try {
            GameSession.partida = new PartidaService().crearPartida(nombreEntrenador.getText(), ids);
            GameSession.combate = new com.pokeapp.service.CombateService().iniciarCombate(GameSession.partida);
            App.setRoot("battle");
        } catch (IllegalArgumentException ex) { mensaje.setText(ex.getMessage()); }
    }
    @FXML private void volverMenu() throws IOException { App.setRoot("primary"); }
}
