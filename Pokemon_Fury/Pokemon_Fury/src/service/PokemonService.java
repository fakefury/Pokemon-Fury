package service;

import exceptions.InicialInvalidoException;
import exceptions.PokemonNoCapturableException;
import exceptions.SinPokeBallsException;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class PokemonService {


    public void validarPokemonInicial(String nombrePokemon) throws InicialInvalidoException {
        if (!nombrePokemon.equalsIgnoreCase("Gible") &&
            !nombrePokemon.equalsIgnoreCase("Bagon") &&
            !nombrePokemon.equalsIgnoreCase("Dreepy")) {
            throw new InicialInvalidoException("El Pokémon inicial '" + nombrePokemon + "' no es válido. Opciones permitidas: Gible, Bagon, Dreepy.");
        }
    }


    public boolean capturarPokemon(Partida partida, PokemonSalvaje pokemonSalvaje) 
            throws SinPokeBallsException, PokemonNoCapturableException {
        

        if (partida.getCantidadPokeballs() <= 0) {
            throw new SinPokeBallsException("No tienes Poké Balls suficientes para realizar el intento de captura.");
        }


        if (!pokemonSalvaje.isCapturable()) {
            throw new PokemonNoCapturableException("El Pokémon " + pokemonSalvaje.getNombre() + " no se puede capturar.");
        }


        partida.setCantidadPokeballs(partida.getCantidadPokeballs() - 1);


        double suerte = Math.random();
        if (suerte <= pokemonSalvaje.getProbabilidadHuida()) {
            return true; 
        }
        return false; 
    }

    public void recuperarPokemon(Pokemon pokemon) {
        pokemon.setHpActual(pokemon.getHpMaximo());
    }
}
