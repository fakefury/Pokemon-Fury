package com.pokeapp.model;

public class PokemonJugador extends Pokemon {
    private int idPartida;
    private String apodo;

    public PokemonJugador() { }

    public PokemonJugador(Pokemon pokemon) {
        super(pokemon.getIdPokemon(), pokemon.getNombre(), pokemon.getTipoPrimario(),
                pokemon.getTipoSecundario(), pokemon.getNivel(), pokemon.getHpMaximo(),
                pokemon.getAtaque(), pokemon.getDefensa(), pokemon.getVelocidad(),
                pokemon.getSpriteKey(), pokemon.getMovimientos());
    }

    public int getIdPartida() { return idPartida; }
    public void setIdPartida(int idPartida) { this.idPartida = idPartida; }
    public String getApodo() { return apodo; }
    public void setApodo(String apodo) { this.apodo = apodo; }
}
