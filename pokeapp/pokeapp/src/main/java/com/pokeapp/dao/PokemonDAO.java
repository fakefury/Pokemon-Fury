package com.pokeapp.dao;

import com.pokeapp.model.Pokemon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PokemonDAO {
    private final List<Pokemon> pokemones = new ArrayList<>();

    public PokemonDAO() {
        cargarCatalogo();
    }

    private void cargarCatalogo() {
        pokemones.add(new Pokemon(1, "Pikachu", "Electrico", null, 30, 110, 55, 40, 90));
        pokemones.add(new Pokemon(2, "Charizard", "Fuego", "Volador", 30, 125, 84, 78, 100));
        pokemones.add(new Pokemon(3, "Blastoise", "Agua", null, 30, 130, 83, 100, 78));
        pokemones.add(new Pokemon(4, "Venusaur", "Planta", "Veneno", 30, 130, 82, 83, 80));
        pokemones.add(new Pokemon(5, "Gengar", "Fantasma", "Veneno", 30, 105, 90, 60, 110));
        pokemones.add(new Pokemon(6, "Dragonite", "Dragon", "Volador", 30, 140, 100, 95, 80));
        pokemones.add(new Pokemon(7, "Lucario", "Lucha", "Acero", 30, 120, 100, 75, 90));
        pokemones.add(new Pokemon(8, "Garchomp", "Dragon", "Tierra", 30, 140, 105, 90, 95));
        pokemones.add(new Pokemon(9, "Snorlax", "Normal", null, 30, 170, 85, 80, 30));
        pokemones.add(new Pokemon(10, "Greninja", "Agua", "Siniestro", 30, 115, 95, 67, 115));
        pokemones.add(new Pokemon(11, "Arcanine", "Fuego", null, 30, 125, 95, 80, 95));
        pokemones.add(new Pokemon(12, "Sylveon", "Hada", null, 30, 125, 85, 75, 60));
    }

    public List<Pokemon> obtenerTodos() {
        List<Pokemon> copia = new ArrayList<>();
        for (Pokemon pokemon : pokemones) {
            copia.add(new Pokemon(pokemon.getIdPokemon(), pokemon.getNombre(), pokemon.getTipoPrimario(),
                    pokemon.getTipoSecundario(), pokemon.getNivel(), pokemon.getHpMaximo(),
                    pokemon.getAtaque(), pokemon.getDefensa(), pokemon.getVelocidad()));
        }
        return copia;
    }
}
