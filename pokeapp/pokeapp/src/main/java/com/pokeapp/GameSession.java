package com.pokeapp;
import com.pokeapp.model.Partida;
import com.pokeapp.model.Combate;
public class GameSession {
    public static Partida partida;
    public static Combate combate;
    public static void limpiar() { partida = null; combate = null; }
}
