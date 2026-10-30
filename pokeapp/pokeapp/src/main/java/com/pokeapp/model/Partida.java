package com.pokeapp.model;
import java.util.ArrayList;
import java.util.List;
public class Partida {
    private int idPartida;
    private Jugador jugador;
    private List<PokemonJugador> equipoJugador = new ArrayList<>();
    private List<PokemonRival> equipoRival = new ArrayList<>();
    public int getIdPartida() { return idPartida; }
    public void setIdPartida(int idPartida) { this.idPartida = idPartida; }
    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }
    public List<PokemonJugador> getEquipoJugador() { return equipoJugador; }
    public void setEquipoJugador(List<PokemonJugador> equipoJugador) { this.equipoJugador = equipoJugador; }
    public List<PokemonRival> getEquipoRival() { return equipoRival; }
    public void setEquipoRival(List<PokemonRival> equipoRival) { this.equipoRival = equipoRival; }
}
