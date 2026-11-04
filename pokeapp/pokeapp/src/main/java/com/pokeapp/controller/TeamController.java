package com.pokeapp.controller;

import com.pokeapp.App;
import com.pokeapp.GameSession;
import com.pokeapp.model.Pokemon;
import com.pokeapp.service.CombateService;
import com.pokeapp.service.PartidaService;
import com.pokeapp.service.PokemonService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamController {
    @FXML private TilePane listaPokemones;
    @FXML private Label contador;
    @FXML private TextField nombreEntrenador;
    @FXML private Label mensaje;
    @FXML private Button botonConfirmar;

    private final PokemonService pokemonService = new PokemonService();
    private final List<Pokemon> catalogo = new ArrayList<>();
    private final List<ToggleButton> tarjetas = new ArrayList<>();

    @FXML
    public void initialize() {
        catalogo.addAll(pokemonService.obtenerCatalogo());
        for (Pokemon pokemon : catalogo) crearTarjetaPokemon(pokemon);
        actualizarContador();
    }

    private void crearTarjetaPokemon(Pokemon pokemon) {
        ToggleButton tarjeta = new ToggleButton();
        tarjeta.getStyleClass().add("pokemon-card");
        tarjeta.setPrefSize(178, 166);
        tarjeta.setMinSize(178, 166);
        tarjeta.setMaxSize(178, 166);
        tarjeta.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        tarjeta.setTooltip(new Tooltip(pokemon.getNombre() + " · " + pokemon.getTiposTexto()
                + " · HP " + pokemon.getHpMaximo()));

        ImageView imagen = crearImagen(pokemon.getSpriteKey() + "-chibi.png", 105, 100);
        Label nombre = new Label(pokemon.getNombre());
        nombre.getStyleClass().add("pokemon-card-name");
        Label tipo = new Label(pokemon.getTiposTexto());
        tipo.getStyleClass().add("pokemon-card-type");
        VBox contenido = new VBox(2, imagen, nombre, tipo);
        contenido.setAlignment(Pos.CENTER);
        tarjeta.setGraphic(contenido);

        tarjeta.setOnAction(event -> {
            if (tarjeta.isSelected() && contarSeleccionados() > 6) {
                tarjeta.setSelected(false);
                mensaje.setText("El equipo ya tiene seis Pokémon. Deseleccioná uno para cambiarlo.");
            } else {
                mensaje.setText("");
            }
            actualizarContador();
        });

        tarjetas.add(tarjeta);
        listaPokemones.getChildren().add(tarjeta);
    }

    private ImageView crearImagen(String archivo, double ancho, double alto) {
        ImageView vista = new ImageView();
        vista.setFitWidth(ancho);
        vista.setFitHeight(alto);
        vista.setPreserveRatio(true);
        vista.setSmooth(false);
        vista.setImage(cargarImagen(archivo));
        return vista;
    }

    private Image cargarImagen(String archivo) {
        String ruta = "/com/pokeapp/sprites/" + archivo;
        try {
            return new Image(getClass().getResourceAsStream(ruta));
        } catch (Exception ex) {
            return null;
        }
    }

    private int contarSeleccionados() {
        int total = 0;
        for (ToggleButton tarjeta : tarjetas) if (tarjeta.isSelected()) total++;
        return total;
    }

    private void actualizarContador() {
        int seleccionados = contarSeleccionados();
        contador.setText("EQUIPO: " + seleccionados + " / 6");
        botonConfirmar.setDisable(seleccionados != 6);
    }

    @FXML
    private void confirmarEquipo() throws IOException {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < tarjetas.size(); i++) {
            if (tarjetas.get(i).isSelected()) ids.add(catalogo.get(i).getIdPokemon());
        }
        try {
            GameSession.setPartida(new PartidaService().crearPartida(nombreEntrenador.getText(), ids));
            GameSession.setCombate(new CombateService().iniciarCombate(GameSession.getPartida()));
            App.setRoot("battle");
        } catch (IllegalArgumentException ex) {
            mensaje.setText(ex.getMessage());
        }
    }

    @FXML
    private void volverMenu() throws IOException {
        App.setRoot("primary");
    }
}
