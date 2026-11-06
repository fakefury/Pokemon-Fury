package com.pokeapp.exceptions;

/** Se lanza cuando se intenta usar o elegir un Pokémon que ya no tiene HP. */
public class PokemonDebilitadoException extends Exception {
    public PokemonDebilitadoException(String mensaje) {
        super(mensaje);
    }
}
