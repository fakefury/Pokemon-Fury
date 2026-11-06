package com.pokeapp.service;

import com.pokeapp.dao.CombateDAO;
import com.pokeapp.dao.EfectividadTipoDAO;
import com.pokeapp.exceptions.PokemonDebilitadoException;
import com.pokeapp.exceptions.PpInsuficienteException;
import com.pokeapp.model.Combate;
import com.pokeapp.model.Movimiento;
import com.pokeapp.model.Partida;
import com.pokeapp.model.Pokemon;
import com.pokeapp.model.PokemonJugador;
import com.pokeapp.model.PokemonRival;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/** Reglas del combate 6 contra 6. Los multiplicadores se leen de efectividad_tipo. */
public class CombateService {
    private final CombateDAO combateDAO;
    private final EfectividadTipoDAO efectividadDAO;

    public CombateService() {
        this(new CombateDAO(), new EfectividadTipoDAO());
    }

    public CombateService(CombateDAO combateDAO, EfectividadTipoDAO efectividadDAO) {
        this.combateDAO = combateDAO;
        this.efectividadDAO = efectividadDAO;
    }

    public Combate iniciarCombate(Partida partida) {
        if (partida == null || partida.getEquipoJugador().size() != 6 || partida.getEquipoRival().size() != 6) {
            throw new IllegalArgumentException("La batalla necesita dos equipos de seis Pokémon.");
        }
        Combate combate = new Combate();
        combate.setPartida(partida);
        return combateDAO.guardar(combate);
    }

    public String ejecutarMovimiento(Combate combate, int indiceJugador, Movimiento movimiento)
            throws PokemonDebilitadoException, PpInsuficienteException, SQLException {
        validarCombate(combate);
        if (movimiento == null) return "No se pudo ejecutar ese movimiento.";

        Partida partida = combate.getPartida();
        if (indiceJugador < 0 || indiceJugador >= partida.getEquipoJugador().size()) return "Pokémon no válido.";
        PokemonJugador atacante = partida.getEquipoJugador().get(indiceJugador);
        if (atacante.estaDerrotado()) {
            throw new PokemonDebilitadoException(atacante.getNombre() + " está debilitado y no puede combatir. Elegí otro Pokémon.");
        }
        if (!movimiento.tienePP()) {
            throw new PpInsuficienteException("¡No quedan PP para " + movimiento.getNombre() + "! Elegí otro movimiento.");
        }

        PokemonRival defensor = obtenerRivalActivo(combate);
        if (defensor == null) {
            combate.setResultado("VICTORIA");
            return "¡Ganaste el combate!";
        }

        combate.setPokemonJugadorActual(indiceJugador);
        movimiento.reducirPp();
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("¡").append(atacante.getNombre()).append(" usó ")
                .append(movimiento.getNombre()).append("!\n");

        int danio = 0;
        if (movimiento.isHaceDano() && movimiento.getPotencia() > 0) {
            double multiplicador = efectividad(movimiento, defensor);
            danio = calcularDanio(atacante, defensor, movimiento, multiplicador);
            defensor.recibirDanio(danio);
            mensaje.append("Causó ").append(danio).append(" puntos de daño.");
            agregarEfectividad(mensaje, multiplicador);
        } else {
            mensaje.append("No causa daño directo, pero el movimiento se ejecutó.");
        }

        boolean rivalSobrevivio = !defensor.estaDerrotado();
        if (!rivalSobrevivio) {
            mensaje.append("\n¡").append(defensor.getNombre()).append(" quedó debilitado!");
            combate.setPokemonRivalActual(combate.getPokemonRivalActual() + 1);
            defensor = obtenerRivalActivo(combate);
            if (defensor == null) {
                combate.setResultado("VICTORIA");
                combate.setTurnoActual(combate.getTurnoActual() + 1);
                mensaje.append("\n¡VICTORIA! Derrotaste a los seis Pokémon rivales.");
                return mensaje.toString();
            }
        }

        // Si el rival cayó, el siguiente Pokémon entra sin atacar en ese mismo turno.
        if (rivalSobrevivio) {
            Movimiento movimientoRival = elegirMovimientoRival(defensor);
            if (movimientoRival != null) {
                movimientoRival.reducirPp();
                int danioRival = calcularDanio(defensor, atacante, movimientoRival,
                        efectividad(movimientoRival, atacante));
                atacante.recibirDanio(danioRival);
                mensaje.append("\n¡").append(defensor.getNombre()).append(" respondió con ")
                        .append(movimientoRival.getNombre()).append("!")
                        .append("\nTu Pokémon recibió ").append(danioRival).append(" puntos de daño.");
                agregarEfectividad(mensaje, efectividad(movimientoRival, atacante));

                if (atacante.estaDerrotado()) {
                    mensaje.append("\n¡").append(atacante.getNombre()).append(" quedó debilitado!");
                    // No cambiamos automáticamente de Pokémon. La interfaz debe
                    // pedir al jugador que elija quién entra después del debilitamiento.
                }
            } else {
                mensaje.append("\n").append(defensor.getNombre()).append(" no tiene movimientos con PP disponibles.");
            }
        }

        combate.setTurnoActual(combate.getTurnoActual() + 1);
        if (!hayJugadorDisponible(combate)) {
            combate.setResultado("DERROTA");
            mensaje.append("\nDERROTA. Tu equipo quedó debilitado.");
        }
        if (obtenerRivalActivo(combate) == null) {
            combate.setResultado("VICTORIA");
            mensaje.append("\n¡VICTORIA! Derrotaste a los seis Pokémon rivales.");
        }
        return mensaje.toString();
    }

    /** Cambiar de Pokémon consume el turno y permite un contraataque rival. */
    public String cambiarPokemon(Combate combate, int nuevoIndice)
            throws PokemonDebilitadoException, SQLException {
        validarCombate(combate);
        List<PokemonJugador> equipo = combate.getPartida().getEquipoJugador();
        if (nuevoIndice < 0 || nuevoIndice >= equipo.size()) return "Pokémon no válido.";
        PokemonJugador nuevoPokemon = equipo.get(nuevoIndice);
        if (nuevoPokemon.estaDerrotado()) {
            throw new PokemonDebilitadoException(nuevoPokemon.getNombre() + " está debilitado. No podés seleccionarlo para combatir.");
        }
        if (nuevoIndice == combate.getPokemonJugadorActual()) return nuevoPokemon.getNombre() + " ya está combatiendo.";

        StringBuilder mensaje = new StringBuilder("¡Adelante, ").append(nuevoPokemon.getNombre()).append("!");
        combate.setPokemonJugadorActual(nuevoIndice);
        PokemonRival rival = obtenerRivalActivo(combate);
        if (rival != null) {
            Movimiento movimientoRival = elegirMovimientoRival(rival);
            if (movimientoRival != null) {
                movimientoRival.reducirPp();
                double multiplicador = efectividad(movimientoRival, nuevoPokemon);
                int danio = calcularDanio(rival, nuevoPokemon, movimientoRival, multiplicador);
                nuevoPokemon.recibirDanio(danio);
                mensaje.append("\n").append(rival.getNombre()).append(" aprovechó el cambio y usó ")
                        .append(movimientoRival.getNombre()).append(".")
                        .append("\n").append(nuevoPokemon.getNombre()).append(" recibió ").append(danio)
                        .append(" puntos de daño.");
                agregarEfectividad(mensaje, multiplicador);
                if (nuevoPokemon.estaDerrotado()) {
                    mensaje.append("\n¡").append(nuevoPokemon.getNombre()).append(" quedó debilitado!");
                    // El siguiente Pokémon se elige desde la interfaz después del popup.
                }
            }
        }

        combate.setTurnoActual(combate.getTurnoActual() + 1);
        if (!hayJugadorDisponible(combate)) {
            combate.setResultado("DERROTA");
            mensaje.append("\nDERROTA. Tu equipo quedó debilitado.");
        }
        return mensaje.toString();
    }

    public PokemonRival obtenerRivalActivo(Combate combate) {
        if (combate == null || combate.getPartida() == null) return null;
        List<PokemonRival> equipo = combate.getPartida().getEquipoRival();
        while (combate.getPokemonRivalActual() < equipo.size()
                && equipo.get(combate.getPokemonRivalActual()).estaDerrotado()) {
            combate.setPokemonRivalActual(combate.getPokemonRivalActual() + 1);
        }
        return combate.getPokemonRivalActual() < equipo.size()
                ? equipo.get(combate.getPokemonRivalActual()) : null;
    }

    public PokemonJugador obtenerJugadorActivo(Combate combate) {
        if (combate == null || combate.getPartida() == null) return null;
        List<PokemonJugador> equipo = combate.getPartida().getEquipoJugador();
        int indice = combate.getPokemonJugadorActual();
        if (indice >= 0 && indice < equipo.size() && !equipo.get(indice).estaDerrotado()) {
            return equipo.get(indice);
        }
        return null;
    }

    /** Indica si todavía queda al menos un Pokémon del jugador disponible. */
    public boolean hayJugadorDisponible(Combate combate) {
        if (combate == null || combate.getPartida() == null) return false;
        for (PokemonJugador pokemon : combate.getPartida().getEquipoJugador()) {
            if (!pokemon.estaDerrotado()) return true;
        }
        return false;
    }

    private void validarCombate(Combate combate) {
        if (combate == null || combate.getPartida() == null || !"EN_PROCESO".equals(combate.getResultado())) {
            throw new IllegalStateException("El combate ya terminó o no está disponible.");
        }
    }

    private int siguienteJugadorDisponible(Combate combate, int desde) {
        List<PokemonJugador> equipo = combate.getPartida().getEquipoJugador();
        for (int i = 1; i <= equipo.size(); i++) {
            int indice = (desde + i) % equipo.size();
            if (indice < 0) indice += equipo.size();
            if (!equipo.get(indice).estaDerrotado()) return indice;
        }
        return equipo.size();
    }

    private Movimiento elegirMovimientoRival(PokemonRival rival) {
        List<Movimiento> disponibles = new ArrayList<>();
        for (Movimiento movimiento : rival.getMovimientos()) {
            if (movimiento != null && movimiento.tienePP() && movimiento.isHaceDano()
                    && movimiento.getPotencia() > 0) disponibles.add(movimiento);
        }
        if (disponibles.isEmpty()) return null;
        return disponibles.get(ThreadLocalRandom.current().nextInt(disponibles.size()));
    }

    private double efectividad(Movimiento movimiento, Pokemon defensor) throws SQLException {
        if (movimiento.getTipo() == null || defensor.getTipoPrimario() == null) return 1.0;
        return efectividadDAO.obtenerMultiplicador(movimiento.getTipo(), defensor.getTipoPrimario());
    }

    private int calcularDanio(Pokemon atacante, Pokemon defensor, Movimiento movimiento, double efectividad) {
        if (!movimiento.isHaceDano() || movimiento.getPotencia() <= 0 || efectividad <= 0.0) return 0;
        double nivelFactor = (2.0 * atacante.getNivel() / 5.0) + 2.0;
        double base = ((nivelFactor * movimiento.getPotencia() * Math.max(1, atacante.getAtaque()))
                / Math.max(35, defensor.getDefensa()) / 50.0) + 2.0;
        boolean mismoTipo = movimiento.getTipo() != null
                && (movimiento.getTipo().equalsIgnoreCase(atacante.getTipoPrimario())
                || movimiento.getTipo().equalsIgnoreCase(atacante.getTipoSecundario()));
        double stab = mismoTipo ? 1.35 : 1.0;
        double variacion = ThreadLocalRandom.current().nextDouble(0.90, 1.01);
        return Math.max(1, (int) Math.round(base * stab * efectividad * variacion));
    }

    private void agregarEfectividad(StringBuilder mensaje, double multiplicador) {
        if (multiplicador >= 2.0) mensaje.append("\n¡Es súper eficaz!");
        else if (multiplicador == 0.0) mensaje.append("\nNo afecta al rival.");
        else if (multiplicador > 0.0 && multiplicador <= 0.5) mensaje.append("\nNo es muy eficaz...");
    }
}
