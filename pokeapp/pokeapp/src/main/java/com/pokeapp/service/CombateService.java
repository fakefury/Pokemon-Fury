package com.pokeapp.service;

import com.pokeapp.dao.CombateDAO;
import com.pokeapp.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/** Reglas de combate por turnos: el equipo del jugador y el rival tienen seis integrantes. */
public class CombateService {
    private final CombateDAO combateDAO = new CombateDAO();

    public Combate iniciarCombate(Partida partida) {
        if (partida == null || partida.getEquipoJugador().size() != 6 || partida.getEquipoRival().size() != 6) {
            throw new IllegalArgumentException("La batalla necesita dos equipos de seis Pokémon.");
        }
        Combate combate = new Combate();
        combate.setPartida(partida);
        return combateDAO.guardar(combate);
    }

    public String ejecutarMovimiento(Combate combate, int indiceJugador, Movimiento movimiento) {
        if (combate == null || !"EN_PROCESO".equals(combate.getResultado())) return "El combate ya terminó.";
        if (movimiento == null) return "No se pudo ejecutar ese movimiento.";

        Partida partida = combate.getPartida();
        if (indiceJugador < 0 || indiceJugador >= partida.getEquipoJugador().size()) return "Pokémon no válido.";
        PokemonJugador atacante = partida.getEquipoJugador().get(indiceJugador);
        if (atacante.estaDerrotado()) return "Ese Pokémon está debilitado. Elegí otro.";
        PokemonRival defensor = obtenerRivalActivo(combate);
        if (defensor == null) {
            combate.setResultado("VICTORIA");
            return "¡Ganaste el combate!";
        }
        if (!movimiento.tienePP()) return "Ese movimiento no tiene PP disponibles.";

        combate.setPokemonJugadorActual(indiceJugador);
        movimiento.reducirPp();
        int danio = calcularDanio(atacante, defensor, movimiento);
        defensor.recibirDanio(danio);
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("¡").append(atacante.getNombre()).append(" usó ")
                .append(movimiento.getNombre()).append("!\n");
        mensaje.append("Causó ").append(danio).append(" puntos de daño.");
        agregarEfectividad(mensaje, efectividad(movimiento.getTipo(), defensor));

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

        // El Pokémon rival contraataca solo si sobrevivió al movimiento recibido.
        PokemonJugador objetivo = partida.getEquipoJugador().get(indiceJugador);
        Movimiento movimientoRival = rivalSobrevivio ? elegirMovimientoRival(defensor) : null;
        if (objetivo != null && !objetivo.estaDerrotado() && movimientoRival != null) {
            movimientoRival.reducirPp();
            int danioRival = calcularDanio(defensor, objetivo, movimientoRival);
            objetivo.recibirDanio(danioRival);
            mensaje.append("\n¡").append(defensor.getNombre()).append(" respondió con ")
                    .append(movimientoRival.getNombre()).append("!");
            mensaje.append("\nTu Pokémon recibió ").append(danioRival).append(" puntos de daño.");
            agregarEfectividad(mensaje, efectividad(movimientoRival.getTipo(), objetivo));

            if (objetivo.estaDerrotado()) {
                mensaje.append("\n¡").append(objetivo.getNombre()).append(" quedó debilitado!");
                int siguiente = siguienteJugadorDisponible(combate, indiceJugador);
                combate.setPokemonJugadorActual(siguiente);
                if (siguiente < partida.getEquipoJugador().size()) {
                    mensaje.append("\n¡Adelante, ").append(partida.getEquipoJugador().get(siguiente).getNombre()).append("!");
                }
            }
        }

        combate.setTurnoActual(combate.getTurnoActual() + 1);
        if (obtenerJugadorActivo(combate) == null) {
            combate.setResultado("DERROTA");
            mensaje.append("\nDERROTA. Tu equipo quedó debilitado.");
        }
        if (obtenerRivalActivo(combate) == null) {
            combate.setResultado("VICTORIA");
            mensaje.append("\n¡VICTORIA! Derrotaste a los seis Pokémon rivales.");
        }
        return mensaje.toString();
    }

    /** Cambiar de Pokémon consume el turno: el rival responde al cambio. */
    public String cambiarPokemon(Combate combate, int nuevoIndice) {
        if (combate == null || !"EN_PROCESO".equals(combate.getResultado())) return "El combate ya terminó.";
        List<PokemonJugador> equipo = combate.getPartida().getEquipoJugador();
        if (nuevoIndice < 0 || nuevoIndice >= equipo.size()) return "Pokémon no válido.";
        PokemonJugador nuevoPokemon = equipo.get(nuevoIndice);
        if (nuevoPokemon.estaDerrotado()) return "Ese Pokémon está debilitado.";
        if (nuevoIndice == combate.getPokemonJugadorActual()) return "Ese Pokémon ya está combatiendo.";

        StringBuilder mensaje = new StringBuilder("¡Adelante, ")
                .append(nuevoPokemon.getNombre()).append("!\n");
        combate.setPokemonJugadorActual(nuevoIndice);

        PokemonRival rival = obtenerRivalActivo(combate);
        if (rival != null) {
            Movimiento movimientoRival = elegirMovimientoRival(rival);
            if (movimientoRival != null) {
                movimientoRival.reducirPp();
                int danio = calcularDanio(rival, nuevoPokemon, movimientoRival);
                nuevoPokemon.recibirDanio(danio);
                mensaje.append(rival.getNombre()).append(" aprovechó el cambio y usó ")
                        .append(movimientoRival.getNombre()).append(".\n")
                        .append(nuevoPokemon.getNombre()).append(" recibió ").append(danio)
                        .append(" puntos de daño.");
                if (nuevoPokemon.estaDerrotado()) {
                    mensaje.append("\n¡").append(nuevoPokemon.getNombre()).append(" quedó debilitado!");
                    int siguiente = siguienteJugadorDisponible(combate, nuevoIndice);
                    combate.setPokemonJugadorActual(siguiente);
                    if (siguiente < equipo.size()) {
                        mensaje.append("\n¡Adelante, ").append(equipo.get(siguiente).getNombre()).append("!");
                    }
                }
            }
        }

        combate.setTurnoActual(combate.getTurnoActual() + 1);
        if (obtenerJugadorActivo(combate) == null) {
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
        if (indice >= 0 && indice < equipo.size() && !equipo.get(indice).estaDerrotado()) return equipo.get(indice);
        int siguiente = siguienteJugadorDisponible(combate, indice - 1);
        combate.setPokemonJugadorActual(siguiente);
        return siguiente < equipo.size() ? equipo.get(siguiente) : null;
    }

    public boolean esPokemonDisponible(PokemonJugador pokemon) {
        return pokemon != null && !pokemon.estaDerrotado();
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
            if (movimiento != null && movimiento.tienePP() && movimiento.isHaceDano()) disponibles.add(movimiento);
        }
        if (disponibles.isEmpty()) return new Movimiento("Ataque básico", "NORMAL", 35, 1, true);
        return disponibles.get(ThreadLocalRandom.current().nextInt(disponibles.size()));
    }

    private int calcularDanio(Pokemon atacante, Pokemon defensor, Movimiento movimiento) {
        double nivelFactor = (2.0 * atacante.getNivel() / 5.0) + 2.0;
        double base = ((nivelFactor * movimiento.getPotencia() * Math.max(1, atacante.getAtaque()))
                / Math.max(35, defensor.getDefensa()) / 50.0) + 2.0;
        double stab = movimiento.getTipo() != null
                && (movimiento.getTipo().equalsIgnoreCase(atacante.getTipoPrimario())
                || movimiento.getTipo().equalsIgnoreCase(atacante.getTipoSecundario())) ? 1.35 : 1.0;
        double efectividad = efectividad(movimiento.getTipo(), defensor);
        if (efectividad == 0.0) return 0;
        double variacion = ThreadLocalRandom.current().nextDouble(0.90, 1.01);
        return Math.max(5, (int) Math.round(base * stab * efectividad * variacion));
    }

    private double efectividad(String tipoAtaque, Pokemon defensor) {
        if (tipoAtaque == null) return 1.0;
        double resultado = contraTipo(tipoAtaque.toUpperCase(), defensor.getTipoPrimario());
        if (defensor.getTipoSecundario() != null) resultado *= contraTipo(tipoAtaque.toUpperCase(), defensor.getTipoSecundario());
        return resultado;
    }

    private double contraTipo(String ataque, String defensa) {
        if (defensa == null) return 1.0;
        String tipo = defensa.toUpperCase();
        switch (ataque) {
            case "FUEGO": return contiene(tipo, "PLANTA", "BICHO", "HIELO") ? 2.0 : contiene(tipo, "AGUA", "ROCA", "FUEGO") ? 0.5 : 1.0;
            case "AGUA": return contiene(tipo, "FUEGO", "ROCA", "TIERRA") ? 2.0 : contiene(tipo, "AGUA", "PLANTA") ? 0.5 : 1.0;
            case "PLANTA": return contiene(tipo, "AGUA", "ROCA", "TIERRA") ? 2.0 : contiene(tipo, "FUEGO", "PLANTA", "VENENO", "VOLADOR", "BICHO") ? 0.5 : 1.0;
            case "ELÉCTRICO": case "ELECTRICO": return contiene(tipo, "AGUA", "VOLADOR") ? 2.0 : contiene(tipo, "PLANTA", "ELÉCTRICO", "ELECTRICO") ? 0.5 : tipo.equals("TIERRA") ? 0.0 : 1.0;
            case "TIERRA": return contiene(tipo, "FUEGO", "ELÉCTRICO", "ELECTRICO", "VENENO", "ROCA") ? 2.0 : contiene(tipo, "PLANTA", "BICHO") ? 0.5 : tipo.equals("VOLADOR") ? 0.0 : 1.0;
            case "LUCHA": return contiene(tipo, "NORMAL", "ROCA", "HIELO") ? 2.0 : contiene(tipo, "VENENO", "VOLADOR", "PSÍQUICO", "PSIQUICO", "BICHO") ? 0.5 : 1.0;
            case "PSÍQUICO": case "PSIQUICO": return contiene(tipo, "LUCHA", "VENENO") ? 2.0 : contiene(tipo, "PSÍQUICO", "PSIQUICO") ? 0.5 : 1.0;
            case "FANTASMA": return tipo.equals("FANTASMA") ? 2.0 : tipo.equals("NORMAL") ? 0.0 : 1.0;
            case "ROCA": return contiene(tipo, "FUEGO", "HIELO", "VOLADOR", "BICHO") ? 2.0 : contiene(tipo, "LUCHA", "TIERRA") ? 0.5 : 1.0;
            case "HIELO": return contiene(tipo, "PLANTA", "TIERRA", "VOLADOR") ? 2.0 : contiene(tipo, "FUEGO", "AGUA", "HIELO") ? 0.5 : 1.0;
            case "BICHO": return contiene(tipo, "PLANTA", "PSÍQUICO", "PSIQUICO") ? 2.0 : contiene(tipo, "FUEGO", "LUCHA", "VENENO", "VOLADOR") ? 0.5 : 1.0;
            case "VENENO": return tipo.equals("PLANTA") ? 2.0 : contiene(tipo, "VENENO", "TIERRA", "ROCA", "FANTASMA") ? 0.5 : 1.0;
            case "VOLADOR": return contiene(tipo, "PLANTA", "LUCHA", "BICHO") ? 2.0 : contiene(tipo, "ELÉCTRICO", "ELECTRICO", "ROCA") ? 0.5 : 1.0;
            default: return 1.0;
        }
    }

    private boolean contiene(String valor, String... opciones) {
        for (String opcion : opciones) if (valor.equals(opcion)) return true;
        return false;
    }

    private void agregarEfectividad(StringBuilder mensaje, double multiplicador) {
        if (multiplicador >= 2.0) mensaje.append("\n¡Es súper eficaz!");
        else if (multiplicador == 0.0) mensaje.append("\nNo afecta al rival.");
        else if (multiplicador > 0.0 && multiplicador <= 0.5) mensaje.append("\nNo es muy eficaz...");
    }
}
