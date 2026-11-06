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
import java.io.InputStream;
import java.sql.SQLException;
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
    private boolean baseDisponible;

    @FXML
    public void initialize() {
        try {
            catalogo.addAll(pokemonService.obtenerCatalogo());
            if (catalogo.size() != 12) {
                throw new SQLException("Se esperaban 12 Pokémon en la tabla pokemon y se encontraron " + catalogo.size() + ".");
            }
            baseDisponible = true;
            for (Pokemon pokemon : catalogo) crearTarjetaPokemon(pokemon);
            mensaje.setText("Catálogo cargado desde MySQL. Elegí tus seis Pokémon.");
        } catch (SQLException ex) {
            baseDisponible = false;
            botonConfirmar.setDisable(true);
            mensaje.setText("No se pudo cargar el catálogo de MySQL.");
            mostrarAlerta(Alert.AlertType.ERROR, "NO SE PUDO CONECTAR A POKEAPP",
                    "Revisá que MySQL esté iniciado, que hayas importado database/pokeapp.sql y que database.properties tenga tus datos.\n\nDetalle: "
                            + ex.getMessage());
        }
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
                + " · HP " + pokemon.getHpMaximo() + " · ATK " + pokemon.getAtaque()
                + " · SPD " + pokemon.getVelocidad()));

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
                mensaje.setText("Datos obtenidos de MySQL. Cuando estés listo, confirmá el equipo.");
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
        try (InputStream entrada = getClass().getResourceAsStream(ruta)) {
            if (entrada == null) return null;
            return new Image(entrada);
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
        botonConfirmar.setDisable(!baseDisponible || seleccionados != 6);
    }

    @FXML
    private void confirmarEquipo() throws IOException {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < tarjetas.size(); i++) {
            if (tarjetas.get(i).isSelected()) ids.add(catalogo.get(i).getIdPokemon());
        }

        try {
            PartidaService partidaService = new PartidaService();
            GameSession.setPartida(partidaService.crearPartida(nombreEntrenador.getText(), ids, catalogo));
            GameSession.setCombate(new CombateService().iniciarCombate(GameSession.getPartida()));
            GameSession.setResultadoGuardado(false);
            App.setRoot("battle");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            mensaje.setText(ex.getMessage());
            mostrarAlerta(Alert.AlertType.WARNING, "REVISÁ LOS DATOS", ex.getMessage());
        } catch (SQLException ex) {
            mensaje.setText("No se pudo guardar el entrenador en MySQL.");
            mostrarAlerta(Alert.AlertType.ERROR, "ERROR AL GUARDAR EL ENTRENADOR",
                    "No se inició el combate porque no se pudo crear el registro en la tabla entrenador.\n\nDetalle: "
                            + ex.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    @FXML
    private void volverMenu() throws IOException {
        App.setRoot("primary");
    }
}
