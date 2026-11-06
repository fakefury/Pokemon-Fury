package com.pokeapp.controller;

import com.pokeapp.App;
import com.pokeapp.GameSession;
import com.pokeapp.dao.JugadorDAO;
import com.pokeapp.exceptions.PokemonDebilitadoException;
import com.pokeapp.exceptions.PpInsuficienteException;
import com.pokeapp.model.Combate;
import com.pokeapp.model.Movimiento;
import com.pokeapp.model.PokemonJugador;
import com.pokeapp.model.PokemonRival;
import com.pokeapp.service.CombateService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
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
    @FXML private Label indicadorDanioRival;
    @FXML private Label indicadorDanioJugador;

    private final CombateService combateService = new CombateService();
    private final ToggleGroup grupoEquipo = new ToggleGroup();
    private ToggleButton[] botonesEquipo;
    private int indiceSeleccionado;

    @FXML
    public void initialize() {
        if (GameSession.getPartida() == null || GameSession.getCombate() == null) {
            tituloResultado.setText("No hay una partida activa.");
            desactivarMovimientos();
            botonVolver.setVisible(true);
            botonVolver.setManaged(true);
            return;
        }

        elegirFondoAleatorio();
        crearBotonesEquipo();
        PokemonJugador activo = combateService.obtenerJugadorActivo(GameSession.getCombate());
        if (activo != null) indiceSeleccionado = GameSession.getPartida().getEquipoJugador().indexOf(activo);
        seleccionarBotonActivo();
        agregarEstado("¡Un entrenador rival te desafía!\nElegí un movimiento para comenzar el combate.");
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
                    + " · HP " + pokemon.getHpMaximo()));
            boton.setOnAction(event -> seleccionarPokemon(indice));
            botonesEquipo[i] = boton;
            contenedorEquipo.add(boton, i % 3, i / 3);
        }
    }

    private void seleccionarPokemon(int indice) {
        if (indice < 0 || indice >= GameSession.getPartida().getEquipoJugador().size()) return;
        PokemonJugador pokemonElegido = GameSession.getPartida().getEquipoJugador().get(indice);
        if (indice != indiceSeleccionado) {
            PokemonJugador nuevo = pokemonElegido;
            int hpAntes = nuevo.getHpActual();
            try {
                agregarEstado(combateService.cambiarPokemon(GameSession.getCombate(), indice));

                int danioCambio = Math.max(0, hpAntes - nuevo.getHpActual());
                DamageIndicator.show(indicadorDanioJugador, danioCambio);
            } catch (PokemonDebilitadoException ex) {
                mostrarExcepcion("POKÉMON DEBILITADO", ex.getMessage());
                seleccionarBotonActivo();
                return;
            } catch (SQLException ex) {
                mostrarErrorBD(ex);
                return;
            } catch (IllegalStateException ex) {
                mostrarExcepcion("COMBATE NO DISPONIBLE", ex.getMessage());
                return;
            }
        }

        PokemonJugador activo = combateService.obtenerJugadorActivo(GameSession.getCombate());
        if (activo != null) indiceSeleccionado = GameSession.getPartida().getEquipoJugador().indexOf(activo);
        seleccionarBotonActivo();
        actualizarPantalla();

        if ((activo == null || pokemonElegido.estaDerrotado())
                && "EN_PROCESO".equals(GameSession.getCombate().getResultado())) {
            solicitarSiguientePokemon();
        }

        comprobarFinalCombate();
    }

    private void seleccionarBotonActivo() {
        if (botonesEquipo == null) return;
        for (int i = 0; i < botonesEquipo.length; i++) botonesEquipo[i].setSelected(i == indiceSeleccionado);
    }

    private void configurarMovimiento(Button boton, Movimiento movimiento) {
        boolean combateActivo = "EN_PROCESO".equals(GameSession.getCombate().getResultado());
        if (movimiento == null) {
            boton.setText("-");
            boton.setDisable(true);
            boton.setOnAction(null);
            return;
        }
        // Se deja clickeable aunque tenga 0 PP para que la excepción se muestre dentro del juego.
        String textoPp = movimiento.tienePP()
                ? "PP " + movimiento.getPpActual() + "/" + movimiento.getPpMax()
                : "SIN PP";
        boton.setText(movimiento.getNombre() + "  ·  " + movimiento.getTipo() + "\n" + textoPp);
        boton.setDisable(!combateActivo);
        boton.setOnAction(event -> ejecutarMovimiento(movimiento));
    }

    private void ejecutarMovimiento(Movimiento movimiento) {
        if (!"EN_PROCESO".equals(GameSession.getCombate().getResultado())) return;

        PokemonJugador jugadorAntes = combateService.obtenerJugadorActivo(GameSession.getCombate());
        PokemonRival rivalAntes = combateService.obtenerRivalActivo(GameSession.getCombate());
        int hpJugadorAntes = jugadorAntes == null ? 0 : jugadorAntes.getHpActual();
        int hpRivalAntes = rivalAntes == null ? 0 : rivalAntes.getHpActual();

        try {
            String mensaje = combateService.ejecutarMovimiento(
                    GameSession.getCombate(), indiceSeleccionado, movimiento);
            agregarEstado(mensaje);

            int danioRival = rivalAntes == null ? 0 : Math.max(0, hpRivalAntes - rivalAntes.getHpActual());
            int danioJugador = jugadorAntes == null ? 0 : Math.max(0, hpJugadorAntes - jugadorAntes.getHpActual());

            DamageIndicator.show(indicadorDanioRival, danioRival);
            DamageIndicator.show(indicadorDanioJugador, danioJugador);

            actualizarPantalla();

            // El servicio ya no cambia automáticamente de Pokémon cuando el activo cae.
            // Primero mostramos el popup y dejamos que el jugador decida.
            if (jugadorAntes != null && jugadorAntes.estaDerrotado()
                    && "EN_PROCESO".equals(GameSession.getCombate().getResultado())) {
                solicitarSiguientePokemon();
            }

            comprobarFinalCombate();
        } catch (PokemonDebilitadoException ex) {
            mostrarExcepcion("POKÉMON DEBILITADO", ex.getMessage());
        } catch (PpInsuficienteException ex) {
            mostrarExcepcion("PP INSUFICIENTES", ex.getMessage());
        } catch (SQLException ex) {
            mostrarErrorBD(ex);
        } catch (IllegalStateException ex) {
            mostrarExcepcion("COMBATE NO DISPONIBLE", ex.getMessage());
        }
    }

    /**
     * Abre un popup con los Pokémon disponibles y obliga a elegir el siguiente.
     * Si el rival debilita inmediatamente al elegido, se vuelve a pedir otro.
     */
    private void solicitarSiguientePokemon() {
        while ("EN_PROCESO".equals(GameSession.getCombate().getResultado())
                && !combateService.hayJugadorDisponible(GameSession.getCombate())) {
            return;
        }

        while ("EN_PROCESO".equals(GameSession.getCombate().getResultado())) {
            Dialog<PokemonJugador> dialogo = new Dialog<>();
            dialogo.setTitle("POKÉMON DEBILITADO");
            dialogo.setHeaderText("¡" + obtenerNombrePokemonDerrotado()
                    + " quedó debilitado!\nElegí quién sale a combatir.");

            Map<ButtonType, PokemonJugador> opciones = new HashMap<>();
            for (PokemonJugador pokemon : GameSession.getPartida().getEquipoJugador()) {
                if (pokemon.estaDerrotado()) continue;
                ButtonType tipo = new ButtonType(
                        pokemon.getNombre(), ButtonBar.ButtonData.OK_DONE);
                opciones.put(tipo, pokemon);
                dialogo.getDialogPane().getButtonTypes().add(tipo);
            }

            dialogo.getDialogPane().getButtonTypes().add(
                    new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE));

            dialogo.setResultConverter(boton -> opciones.get(boton));

            Optional<PokemonJugador> elegido = dialogo.showAndWait();
            if (!elegido.isPresent()) return;

            PokemonJugador nuevo = elegido.get();
            int indice = GameSession.getPartida().getEquipoJugador().indexOf(nuevo);
            int hpAntes = nuevo.getHpActual();

            try {
                agregarEstado(combateService.cambiarPokemon(GameSession.getCombate(), indice));

                int danioCambio = Math.max(0, hpAntes - nuevo.getHpActual());
                DamageIndicator.show(indicadorDanioJugador, danioCambio);

                PokemonJugador activo = combateService.obtenerJugadorActivo(GameSession.getCombate());
                if (activo != null) {
                    indiceSeleccionado = GameSession.getPartida().getEquipoJugador().indexOf(activo);
                }
                seleccionarBotonActivo();
                actualizarPantalla();

                if (nuevo.estaDerrotado()) {
                    continue;
                }
                return;
            } catch (PokemonDebilitadoException ex) {
                mostrarExcepcion("POKÉMON DEBILITADO", ex.getMessage());
            } catch (SQLException ex) {
                mostrarErrorBD(ex);
                return;
            }
        }
    }

    private String obtenerNombrePokemonDerrotado() {
        for (PokemonJugador pokemon : GameSession.getPartida().getEquipoJugador()) {
            if (pokemon.estaDerrotado()) {
                return pokemon.getNombre();
            }
        }
        return "Tu Pokémon";
    }

    private void actualizarPantalla() {
        if (GameSession.getCombate() == null || botonesEquipo == null) return;
        Combate combate = GameSession.getCombate();
        PokemonRival rival = combateService.obtenerRivalActivo(combate);
        PokemonJugador jugador = combateService.obtenerJugadorActivo(combate);
        etiquetaTurno.setText("TURNO " + combate.getTurnoActual());

        if (rival != null) {
            rivalNombre.setText(rival.getNombre() + "  ·  " + rival.getTiposTexto());
            rivalVida.setText("HP  " + rival.getHpActual() + " / " + rival.getHpMaximo());
            rivalBarraVida.setProgress(ratioVida(rival.getHpActual(), rival.getHpMaximo()));
            rivalSprite.setImage(cargarImagen(rival.getSpriteKey() + ".png"));
        } else {
            rivalNombre.setText("Equipo rival derrotado");
            rivalVida.setText("HP  0 / 0");
            rivalBarraVida.setProgress(0);
            rivalSprite.setImage(null);
        }

        if (jugador != null) {
            indiceSeleccionado = GameSession.getPartida().getEquipoJugador().indexOf(jugador);
            jugadorNombre.setText(jugador.getNombre() + "  ·  " + jugador.getTiposTexto());
            jugadorVida.setText("HP  " + jugador.getHpActual() + " / " + jugador.getHpMaximo());
            jugadorBarraVida.setProgress(ratioVida(jugador.getHpActual(), jugador.getHpMaximo()));
            jugadorSprite.setImage(cargarImagen(jugador.getSpriteKey() + "-back.png"));
            Movimiento[] movimientos = jugador.getMovimientos();
            configurarMovimiento(btnMov1, movimientos[0]);
            configurarMovimiento(btnMov2, movimientos[1]);
            configurarMovimiento(btnMov3, movimientos[2]);
            configurarMovimiento(btnMov4, movimientos[3]);
        } else {
            jugadorNombre.setText("EQUIPO DEBILITADO");
            jugadorVida.setText("HP  0 / 0");
            jugadorBarraVida.setProgress(0);
            jugadorSprite.setImage(null);
            desactivarMovimientos();
        }

        for (int i = 0; i < botonesEquipo.length; i++) {
            PokemonJugador integrante = GameSession.getPartida().getEquipoJugador().get(i);
            // No se deshabilitan los debilitados: al pulsarlos se muestra PokemonDebilitadoException.
            botonesEquipo[i].setDisable(!"EN_PROCESO".equals(combate.getResultado()));
            botonesEquipo[i].setOpacity(integrante.estaDerrotado() ? 0.42 : 1.0);
            botonesEquipo[i].setSelected(i == indiceSeleccionado);
        }

        if (!"EN_PROCESO".equals(combate.getResultado())) {
            tituloResultado.setText("VICTORIA".equals(combate.getResultado())
                    ? "¡VICTORIA! ¡GANASTE!" : "DERROTA · SE TERMINÓ EL COMBATE");
            desactivarMovimientos();
            botonVolver.setVisible(true);
            botonVolver.setManaged(true);
        }
    }

    /**
     * Mantiene únicamente las últimas cinco líneas del registro.
     * Así no aparece un scrollbar y el mensaje más reciente siempre queda visible.
     */
    private void agregarEstado(String mensaje) {
        if (estado == null || mensaje == null || mensaje.isEmpty()) return;
        String actual = estado.getText() == null ? "" : estado.getText().trim();
        String combinado = actual.isEmpty() ? mensaje : actual + "\n" + mensaje;
        String[] lineas = combinado.split("\\R");
        int inicio = Math.max(0, lineas.length - 5);
        StringBuilder ultimas = new StringBuilder();
        for (int i = inicio; i < lineas.length; i++) {
            if (ultimas.length() > 0) ultimas.append("\n");
            ultimas.append(lineas[i]);
        }
        estado.setText(ultimas.toString());
        estado.positionCaret(estado.getLength());
    }

    private double ratioVida(int actual, int maximo) {
        return maximo <= 0 ? 0.0 : Math.max(0.0, Math.min(1.0, (double) actual / maximo));
    }

    private void comprobarFinalCombate() {
        String resultado = GameSession.getCombate().getResultado();
        if ("EN_PROCESO".equals(resultado)) return;

        if (!GameSession.isResultadoGuardado()) {
            try {
                int idEntrenador = GameSession.getPartida().getJugador().getIdJugador();
                new JugadorDAO().actualizarCampeon(idEntrenador, "VICTORIA".equals(resultado));
                GameSession.setResultadoGuardado(true);
            } catch (SQLException ex) {
                GameSession.setResultadoGuardado(true);
                mostrarErrorBD(ex);
            }
        }

        if ("VICTORIA".equals(resultado)) {
            try {
                App.setRoot("victory");
            } catch (IOException ex) {
                mostrarExcepcion("NO SE PUDO ABRIR LA PANTALLA DE VICTORIA", ex.getMessage());
            }
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
        String ruta = "/com/pokeapp/sprites/" + archivo;
        try (InputStream entrada = getClass().getResourceAsStream(ruta)) {
            return entrada == null ? null : new Image(entrada);
        } catch (Exception ex) {
            return null;
        }
    }

    private void mostrarExcepcion(String titulo, String mensaje) {
        agregarEstado(mensaje);
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarErrorBD(SQLException ex) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("ERROR DE BASE DE DATOS");
        alerta.setHeaderText("No se pudo completar la operación en MySQL.");
        alerta.setContentText(ex.getMessage());
        alerta.showAndWait();
    }

    @FXML
    private void volverMenu() throws IOException {
        GameSession.limpiar();
        App.setRoot("primary");
    }
}
