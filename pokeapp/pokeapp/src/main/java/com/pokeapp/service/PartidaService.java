package com.pokeapp.service;
import com.pokeapp.dao.PartidaDAO;
import com.pokeapp.model.*;
import java.util.ArrayList;
import java.util.List;

public class PartidaService {
    private final PartidaDAO partidaDAO = new PartidaDAO();
    private final PokemonService pokemonService = new PokemonService();

    public Partida crearPartida(String nombreJugador, List<Integer> idsElegidos) {
        List<Pokemon> elegidos = pokemonService.obtenerSeleccionados(idsElegidos);
        Partida partida = new Partida();
        partida.setJugador(new Jugador(1, nombreJugador == null || nombreJugador.trim().isEmpty() ? "Entrenador" : nombreJugador.trim()));
        List<PokemonJugador> equipoJugador = new ArrayList<>();
        List<PokemonRival> equipoRival = new ArrayList<>();
        for (Pokemon pokemon : pokemonService.obtenerCatalogo()) {
            if (idsElegidos.contains(pokemon.getIdPokemon())) {
                equipoJugador.add(new PokemonJugador(pokemon));
            } else {
                equipoRival.add(new PokemonRival(pokemon));
            }
        }
        partida.setEquipoJugador(equipoJugador);
        partida.setEquipoRival(equipoRival);
        return partidaDAO.guardar(partida);
    }
}
