package com.pokeapp.model;
public class PokemonRival extends Pokemon {
    private int nivelEntrenador = 1;
    private String estrategia = "Equilibrada";
    public PokemonRival() { }
    public PokemonRival(Pokemon pokemon) {
        super(pokemon.getIdPokemon(), pokemon.getNombre(), pokemon.getTipoPrimario(), pokemon.getTipoSecundario(),
              pokemon.getNivel(), pokemon.getHpMaximo(), pokemon.getAtaque(), pokemon.getDefensa(), pokemon.getVelocidad());
    }
    public int getNivelEntrenador() { return nivelEntrenador; }
    public void setNivelEntrenador(int nivelEntrenador) { this.nivelEntrenador = nivelEntrenador; }
    public String getEstrategia() { return estrategia; }
    public void setEstrategia(String estrategia) { this.estrategia = estrategia; }
}
