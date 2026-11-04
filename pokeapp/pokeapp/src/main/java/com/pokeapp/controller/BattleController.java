package com.pokeapp.controller;

import com.pokeapp.App;
import com.pokeapp.GameSession;
import com.pokeapp.model.*;
import com.pokeapp.service.CombateService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ToggleGroup;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BattleController {
    @FXML private ImageView backgroundImage;
    @FXML private ImageView rivalSprite;
    @FXML private ImageView jugadorSprite;
    @FXML private Label rivalNombre;
    @FXML private Label rivalVida;
    @FXML private Label jugadorNombre;
    @FXML private Label jugadorVida;
    @FXML private ProgressBar rivalBarraVida;
    @FXML private ProgressBar jugadorBarraVida;
    @FXML private Label etiquetaTurno;
    @FXML private Label tituloResultado;
    @FXML private TextArea estado;
    @FXML private GridPane contenedorEquipo;
    @FXML private Button btnMov1;
    @FXML private Button btnMov2;
    @FXML private Button btnMov3;
    @FXML private Button btnMov4;
    @FXML private Button botonVolver;

    private final CombateService combateService = new CombateService();
    private final ToggleGroup grupoEquipo = new ToggleGroup();
    private ToggleButton[] botonesEquipo;
    private int indiceSeleccionado;

    @FXML
    public void initialize() {
        if (GameSession.getPartida() == null || GameSession.getCombate() == null) {
            tituloResultado.setText("No hay una partida activa.");
            desactivarMovimientos();
            return;
        }
        elegirFondoAleatorio();
        crearBotonesEquipo();
        PokemonJugador activo = combateService.obtenerJugadorActivo(GameSession.getCombate());
        if (activo != null) indiceSeleccionado = GameSession.getPartida().getEquipoJugador().indexOf(activo);
        seleccionarBotonActivo();
        estado.setText("¡Un entrenador rival te desafía!\nElegí un movimiento para comenzar el combate.");
        actualizarPantalla();
    }

    private void elegirFondoAleatorio() {
        List<String> fondos = Arrays.asList(
                "battle_background_oceano.png",
                "battle_background_pasto.png",
                "battle_background_simple-gris.png",
                "battle_background_tierra-cueva.png"
        );
        String fondo = fondos.get(ThreadLocalRandom.current().nextInt(fondos.size()));
        backgroundImage.setImage(cargarImagen(fondo));
        backgroundImage.setSmooth(false);
    }

    private void crearBotonesEquipo() {
        List<PokemonJugador> equipo = GameSession.getPartida().getEquipoJugador();
        botonesEquipo = new ToggleButton[equipo.size()];
        for (int i = 0; i < equipo.size(); i++) {
            final int indice = i;
            PokemonJugador pokemon = equipo.get(i);
            ToggleButton boton = new ToggleButton();
            boton.setToggleGroup(grupoEquipo);
            boton.getStyleClass().add("battle-team-card");
            boton.setPrefSize(98, 84);
            boton.setMinSize(98, 84);
            boton.setMaxSize(98, 84);
            boton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            ImageView imagen = crearVista(pokemon.getSpriteKey() + "-chibi.png", 54, 48);
            Label nombre = new Label(pokemon.getNombre());
            nombre.getStyleClass().add("battle-team-name");
            VBox contenido = new VBox(0, imagen, nombre);
            contenido.setAlignment(Pos.CENTER);
            boton.setGraphic(contenido);
            boton.setTooltip(new Tooltip(pokemon.getNombre() + " · " + pokemon.getTiposTexto()
                    + " · " + pokemon.getHpMaximo() + " HP"));
            boton.setOnAction(event -> seleccionarPokemon(indice));
            botonesEquipo[i] = boton;
            contenedorEquipo.add(boton, i % 3, i / 3);
        }
    }

    private void seleccionarPokemon(int indice) {
        if (indice < 0 || indice >= GameSession.getPartida().getEquipoJugador().size()) return;
        PokemonJugador pokemon = GameSession.getPartida().getEquipoJugador().get(indice);
        if (pokemon.estaDerrotado()) {
            estado.setText("" + pokemon.getNombre() + " está debilitado. Elegí otro Pokémon.");
            seleccionarBotonActivo();
            return;
        }
        if (indice != indiceSeleccionado) {
            estado.setText(combateService.cambiarPokemon(GameSession.getCombate(), indice));
            PokemonJugador activo = combateService.obtenerJugadorActivo(GameSession.getCombate());
            if (activo != null) {
                indiceSeleccionado = GameSession.getPartida().getEquipoJugador().indexOf(activo);
            }
        }
        seleccionarBotonActivo();
        actualizarPantalla();
    }

    private void seleccionarBotonActivo() {
        if (botonesEquipo == null) return;
        for (int i = 0; i < botonesEquipo.length; i++) {
            botonesEquipo[i].setSelected(i == indiceSeleccionado);
        }
    }

    private void configurarMovimiento(Button boton, Movimiento movimiento) {
        boolean combateActivo = "EN_PROCESO".equals(GameSession.getCombate().getResultado());
        if (movimiento == null) {
            boton.setText("-");
            boton.setDisable(true);
            boton.setOnAction(null);
            return;
        }
        boton.setText(movimiento.getNombre() + "  ·  " + movimiento.getTipo()
                + "\nPP " + movimiento.getPpActual() + "/" + movimiento.getPpMax());
        boton.setDisable(!combateActivo || !movimiento.tienePP());
        boton.setOnAction(event -> ejecutarMovimiento(movimiento));
    }

    private void ejecutarMovimiento(Movimiento movimiento) {
        if (!"EN_PROCESO".equals(GameSession.getCombate().getResultado())) return;
        PokemonJugador pokemon = GameSession.getPartida().getEquipoJugador().get(indiceSeleccionado);
        if (pokemon.estaDerrotado()) {
            estado.setText("Ese Pokémon está debilitado. Elegí otro.");
            return;
        }
        String mensaje = combateService.ejecutarMovimiento(GameSession.getCombate(), indiceSeleccionado, movimiento);
        estado.setText(mensaje);

        PokemonJugador activo = combateService.obtenerJugadorActivo(GameSession.getCombate());
        if (activo != null) {
            indiceSeleccionado = GameSession.getPartida().getEquipoJugador().indexOf(activo);
        }
        seleccionarBotonActivo();
        actualizarPantalla();
    }

    private void actualizarPantalla() {
        if (GameSession.getCombate() == null || botonesEquipo == null) return;
        Combate combate = GameSession.getCombate();
        PokemonRival rival = combateService.obtenerRivalActivo(combate);
        PokemonJugador jugador = GameSession.getPartida().getEquipoJugador().get(indiceSeleccionado);

        etiquetaTurno.setText("TURNO " + combate.getTurnoActual());
        if (rival != null) {
            rivalNombre.setText(rival.getNombre() + "  ·  " + rival.getTiposTexto());
            rivalVida.setText("HP  " + rival.getHpActual() + " / " + rival.getHpMaximo());
            rivalBarraVida.setProgress((double) rival.getHpActual() / rival.getHpMaximo());
            rivalSprite.setImage(cargarImagen(rival.getSpriteKey() + ".png"));
        } else {
            rivalNombre.setText("Equipo rival derrotado");
            rivalVida.setText("HP  0 / 0");
            rivalBarraVida.setProgress(0);
            rivalSprite.setImage(null);
        }

        if (jugador != null) {
            jugadorNombre.setText(jugador.getNombre() + "  ·  " + jugador.getTiposTexto());
            jugadorVida.setText("HP  " + jugador.getHpActual() + " / " + jugador.getHpMaximo());
            jugadorBarraVida.setProgress((double) jugador.getHpActual() / jugador.getHpMaximo());
            jugadorSprite.setImage(cargarImagen(jugador.getSpriteKey() + "-back.png"));
        }

        for (int i = 0; i < botonesEquipo.length; i++) {
            PokemonJugador integrante = GameSession.getPartida().getEquipoJugador().get(i);
            botonesEquipo[i].setDisable(integrante.estaDerrotado() || !"EN_PROCESO".equals(combate.getResultado()));
            botonesEquipo[i].setOpacity(integrante.estaDerrotado() ? 0.42 : 1.0);
            botonesEquipo[i].setSelected(i == indiceSeleccionado);
        }

        boolean finalizado = !"EN_PROCESO".equals(combate.getResultado());
        if (finalizado) {
            tituloResultado.setText("VICTORIA".equals(combate.getResultado())
                    ? "¡VICTORIA! ¡GANASTE!" : "DERROTA · SE TERMINÓ EL COMBATE");
            tituloResultado.getStyleClass().remove("battle-result-hidden");
            desactivarMovimientos();
            botonVolver.setVisible(true);
            botonVolver.setManaged(true);
        } else {
            tituloResultado.setText("ELEGÍ UN MOVIMIENTO");
            botonVolver.setVisible(false);
            botonVolver.setManaged(false);
            Movimiento[] movimientos = jugador.getMovimientos();
            configurarMovimiento(btnMov1, movimientos[0]);
            configurarMovimiento(btnMov2, movimientos[1]);
            configurarMovimiento(btnMov3, movimientos[2]);
            configurarMovimiento(btnMov4, movimientos[3]);
        }
    }

    private void desactivarMovimientos() {
        if (btnMov1 != null) btnMov1.setDisable(true);
        if (btnMov2 != null) btnMov2.setDisable(true);
        if (btnMov3 != null) btnMov3.setDisable(true);
        if (btnMov4 != null) btnMov4.setDisable(true);
    }

    private ImageView crearVista(String archivo, double ancho, double alto) {
        ImageView vista = new ImageView(cargarImagen(archivo));
        vista.setFitWidth(ancho);
        vista.setFitHeight(alto);
        vista.setPreserveRatio(true);
        vista.setSmooth(false);
        return vista;
    }

    private Image cargarImagen(String archivo) {
        try {
            return new Image(getClass().getResourceAsStream("/com/pokeapp/sprites/" + archivo));
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
