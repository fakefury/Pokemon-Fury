package com.pokeapp.service;

import com.pokeapp.dao.JugadorDAO;
import com.pokeapp.dao.PartidaDAO;
import com.pokeapp.model.*;
import java.util.ArrayList;
import java.util.List;

public class PartidaService {
    private final PartidaDAO partidaDAO = new PartidaDAO();
    private final JugadorDAO jugadorDAO = new JugadorDAO();
    private final PokemonService pokemonService = new PokemonService();

    public Partida crearPartida(String nombreJugador, List<Integer> idsElegidos) {
        pokemonService.validarEquipo(idsElegidos);
        List<Pokemon> catalogo = pokemonService.obtenerCatalogo();
        pokemonService.obtenerSeleccionados(idsElegidos);

        Partida partida = new Partida();
        partida.setJugador(jugadorDAO.crearJugador(nombreJugador));

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
            throw new IllegalStateException("No se pudo formar correctamente los dos equipos.");
        }

        partida.setEquipoJugador(equipoJugador);
        partida.setEquipoRival(equipoRival);
        return partidaDAO.guardar(partida);
    }
}
