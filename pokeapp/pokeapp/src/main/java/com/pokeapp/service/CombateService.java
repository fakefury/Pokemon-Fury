package com.pokeapp.service;
import com.pokeapp.dao.CombateDAO;
import com.pokeapp.model.*;
import java.util.List;

public class CombateService {
    private final CombateDAO combateDAO = new CombateDAO();

    public Combate iniciarCombate(Partida partida) {
        Combate combate = new Combate();
        combate.setPartida(partida);
        return combateDAO.guardar(combate);
    }

    public String atacar(Combate combate, int indicePokemonJugador) {
        if (!"EN_PROCESO".equals(combate.getResultado())) return "El combate ya terminó.";
        Partida partida = combate.getPartida();
        PokemonJugador atacante = partida.getEquipoJugador().get(indicePokemonJugador);
        if (atacante.estaDerrotado()) return "Ese Pokémon está debilitado. Elegí otro.";
        PokemonRival defensor = obtenerRivalActivo(combate);
        if (defensor == null) {
            combate.setResultado("VICTORIA");
            return "¡Ganaste el combate!";
        }

        int danio = Math.max(8, atacante.getAtaque() - defensor.getDefensa() / 3);
        int danioReal = defensor.recibirDanio(danio);
        StringBuilder mensaje = new StringBuilder(atacante.getNombre() + " atacó a " + defensor.getNombre()
                + " e hizo " + danioReal + " puntos de daño.");

        if (defensor.estaDerrotado()) {
            mensaje.append("\n¡").append(defensor.getNombre()).append(" quedó debilitado!");
            combate.setPokemonRivalActual(combate.getPokemonRivalActual() + 1);
            defensor = obtenerRivalActivo(combate);
        }
        if (defensor != null) {
            PokemonJugador objetivo = obtenerJugadorActivo(combate);
            if (objetivo != null) {
                int danioRival = Math.max(6, defensor.getAtaque() - objetivo.getDefensa() / 3);
                objetivo.recibirDanio(danioRival);
                mensaje.append("\n").append(defensor.getNombre()).append(" contraatacó e hizo ")
                       .append(danioRival).append(" puntos de daño.");
                if (objetivo.estaDerrotado()) {
                    mensaje.append("\n").append(objetivo.getNombre()).append(" quedó debilitado.");
                    combate.setPokemonJugadorActual(siguienteJugadorDisponible(combate, indicePokemonJugador));
                }
            }
        }
        combate.setTurnoActual(combate.getTurnoActual() + 1);
        if (obtenerRivalActivo(combate) == null) combate.setResultado("VICTORIA");
        else if (obtenerJugadorActivo(combate) == null) combate.setResultado("DERROTA");
        if ("VICTORIA".equals(combate.getResultado())) mensaje.append("\n¡Victoria! Derrotaste al equipo rival.");
        if ("DERROTA".equals(combate.getResultado())) mensaje.append("\nDerrota. Tu equipo fue debilitado.");
        return mensaje.toString();
    }

    public PokemonRival obtenerRivalActivo(Combate combate) {
        List<PokemonRival> equipo = combate.getPartida().getEquipoRival();
        while (combate.getPokemonRivalActual() < equipo.size()
                && equipo.get(combate.getPokemonRivalActual()).estaDerrotado())
            combate.setPokemonRivalActual(combate.getPokemonRivalActual() + 1);
        return combate.getPokemonRivalActual() < equipo.size() ? equipo.get(combate.getPokemonRivalActual()) : null;
    }

    public PokemonJugador obtenerJugadorActivo(Combate combate) {
        List<PokemonJugador> equipo = combate.getPartida().getEquipoJugador();
        int indice = combate.getPokemonJugadorActual();
        if (indice < 0 || indice >= equipo.size() || equipo.get(indice).estaDerrotado()) return null;
        return equipo.get(indice);
    }

    private int siguienteJugadorDisponible(Combate combate, int desde) {
        List<PokemonJugador> equipo = combate.getPartida().getEquipoJugador();
        for (int i = 0; i < equipo.size(); i++) {
            int indice = (desde + i + 1) % equipo.size();
            if (!equipo.get(indice).estaDerrotado()) return indice;
        }
        return equipo.size();
    }

    public boolean puedeContinuar(Combate combate) {
        return "EN_PROCESO".equals(combate.getResultado())
                && obtenerJugadorActivo(combate) != null && obtenerRivalActivo(combate) != null;
    }
}
