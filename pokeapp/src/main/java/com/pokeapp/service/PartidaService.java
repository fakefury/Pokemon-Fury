package com.pokeapp.service;

import com.pokeapp.dao.JugadorDAO;
import com.pokeapp.dao.PartidaDAO;
import com.pokeapp.exceptions.EquipoIncompletoException;
import com.pokeapp.exceptions.NombreJugadorVacioException;
import com.pokeapp.exceptions.PokemonNoCargadosException;
import com.pokeapp.model.Jugador;
import com.pokeapp.model.Partida;
import com.pokeapp.model.Pokemon;
import com.pokeapp.model.PokemonJugador;
import com.pokeapp.model.PokemonRival;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PartidaService {
    private final PartidaDAO partidaDAO;
    private final JugadorDAO jugadorDAO;
    private final PokemonService pokemonService;

    public PartidaService() {
        this(new PartidaDAO(), new JugadorDAO(), new PokemonService());
    }

    public PartidaService(PartidaDAO partidaDAO, JugadorDAO jugadorDAO, PokemonService pokemonService) {
        this.partidaDAO = partidaDAO;
        this.jugadorDAO = jugadorDAO;
        this.pokemonService = pokemonService;
    }

    public Partida crearPartida(String nombreJugador, List<Integer> idsElegidos, List<Pokemon> catalogo)
            throws SQLException, NombreJugadorVacioException, EquipoIncompletoException,
            PokemonNoCargadosException {
        if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
            throw new NombreJugadorVacioException("Ingresá el nombre del entrenador antes de comenzar.");
        }
        if (idsElegidos == null || idsElegidos.size() != 6) {
            throw new EquipoIncompletoException("Tenés que seleccionar exactamente 6 Pokémon.");
        }
        if (catalogo == null || catalogo.size() != 12) {
            throw new PokemonNoCargadosException(
                    "No están cargados los 12 Pokémon necesarios para iniciar la partida.");
        }
        pokemonService.validarEquipo(idsElegidos);
        pokemonService.obtenerSeleccionados(idsElegidos, catalogo);

        List<PokemonJugador> equipoJugador = new ArrayList<>();
        List<PokemonRival> equipoRival = new ArrayList<>();
        for (Pokemon pokemon : catalogo) {
            if (idsElegidos.contains(pokemon.getIdPokemon())) {
                PokemonJugador integrante = new PokemonJugador(pokemon);
                integrante.setApodo(pokemon.getNombre());
                equipoJugador.add(integrante);
            } else {
                equipoRival.add(new PokemonRival(pokemon));
            }
        }

        if (equipoJugador.size() != 6 || equipoRival.size() != 6) {
            throw new IllegalStateException("No se pudieron formar los dos equipos de seis Pokémon.");
        }
        Jugador jugador = jugadorDAO.crearJugador(nombreJugador, idsElegidos);
        Partida partida = new Partida();
        partida.setJugador(jugador);
        partida.setEquipoJugador(equipoJugador);
        partida.setEquipoRival(equipoRival);
        return partidaDAO.guardar(partida);
    }

    public void actualizarResultadoEntrenador(int idEntrenador, boolean campeon) throws SQLException {
        jugadorDAO.actualizarCampeon(idEntrenador, campeon);
    }
}
