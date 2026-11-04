package com.pokeapp.model;

public class Combate {
    private Partida partida;
    private int turnoActual = 1;
    private String resultado = "EN_PROCESO";
    private int pokemonJugadorActual;
    private int pokemonRivalActual;

    public Partida getPartida() { return partida; }
    public void setPartida(Partida partida) { this.partida = partida; }
    public int getTurnoActual() { return turnoActual; }
    public void setTurnoActual(int turnoActual) { this.turnoActual = turnoActual; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public int getPokemonJugadorActual() { return pokemonJugadorActual; }
    public void setPokemonJugadorActual(int valor) { pokemonJugadorActual = valor; }
    public int getPokemonRivalActual() { return pokemonRivalActual; }
    public void setPokemonRivalActual(int valor) { pokemonRivalActual = valor; }
}
