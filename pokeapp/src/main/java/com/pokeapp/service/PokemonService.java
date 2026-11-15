package com.pokeapp.service;

import com.pokeapp.dao.PokemonDAO;
import com.pokeapp.exceptions.PokemonNoCargadosException;
import com.pokeapp.model.Pokemon;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PokemonService {
    private final PokemonDAO pokemonDAO;

    public PokemonService() {
        this(new PokemonDAO());
    }

    public PokemonService(PokemonDAO pokemonDAO) {
        this.pokemonDAO = pokemonDAO;
    }

    public List<Pokemon> obtenerCatalogo() throws SQLException, PokemonNoCargadosException {
        List<Pokemon> catalogo = pokemonDAO.obtenerTodos();
        if (catalogo == null || catalogo.size() != 12) {
            throw new PokemonNoCargadosException(
                    "No están cargados los 12 Pokémon necesarios. Revisá la tabla pokemon de la base pokeapp.");
        }
        return catalogo;
    }

    public void validarEquipo(List<Integer> idsSeleccionados) {
        if (idsSeleccionados == null || idsSeleccionados.size() != 6) {
            throw new IllegalArgumentException("Tenés que seleccionar exactamente 6 Pokémon.");
        }
        if (idsSeleccionados.stream().distinct().count() != 6) {
            throw new IllegalArgumentException("No podés seleccionar un Pokémon más de una vez.");
        }
    }

    public List<Pokemon> obtenerSeleccionados(List<Integer> ids, List<Pokemon> catalogo) {
        validarEquipo(ids);
        List<Pokemon> seleccionados = new ArrayList<>();
        for (Pokemon pokemon : catalogo) {
            if (ids.contains(pokemon.getIdPokemon())) seleccionados.add(pokemon);
        }
        if (seleccionados.size() != 6) {
            throw new IllegalArgumentException("La selección contiene Pokémon que no están en el catálogo de la base de datos.");
        }
        return seleccionados;
    }
}
