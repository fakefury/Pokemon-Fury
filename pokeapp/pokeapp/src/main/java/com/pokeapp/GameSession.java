package com.pokeapp;

import com.pokeapp.model.Combate;
import com.pokeapp.model.Partida;

/** Mantiene el estado de la partida mientras se navega entre pantallas. */
public final class GameSession {
    private static Partida partida;
    private static Combate combate;

    private GameSession() { }
    public static Partida getPartida() { return partida; }
    public static void setPartida(Partida valor) { partida = valor; }
    public static Combate getCombate() { return combate; }
    public static void setCombate(Combate valor) { combate = valor; }
    public static void limpiar() { partida = null; combate = null; }
}
