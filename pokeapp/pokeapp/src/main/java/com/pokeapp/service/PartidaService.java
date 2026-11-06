package com.pokeapp.service;

import com.pokeapp.dao.JugadorDAO;
import com.pokeapp.dao.PartidaDAO;
import com.pokeapp.model.Jugador;
import com.pokeapp.model.Partida;
import com.pokeapp.model.Pokemon;
import com.pokeapp.model.PokemonJugador;
import com.pokeapp.model.PokemonRival;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Arma los dos equipos y registra al entrenador en MySQL antes de iniciar el combate. */
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
            throws SQLException {
        pokemonService.validarEquipo(idsElegidos);
        if (catalogo == null || catalogo.size() != 12) {
            throw new IllegalStateException("El catálogo de la base de datos debe tener los 12 Pokémon del juego.");
        }
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

        // La tabla entrenador guarda el nombre y los IDs elegidos (en el mismo orden de selección).
        Jugador jugador = jugadorDAO.crearJugador(nombreJugador, idsElegidos);
        Partida partida = new Partida();
        partida.setJugador(jugador);
        partida.setEquipoJugador(equipoJugador);
        partida.setEquipoRival(equipoRival);
        return partidaDAO.guardar(partida);
    }
}
