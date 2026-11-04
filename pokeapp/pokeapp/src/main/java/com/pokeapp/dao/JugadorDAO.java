package com.pokeapp.dao;

import com.pokeapp.model.Jugador;
import java.util.concurrent.atomic.AtomicInteger;

public class JugadorDAO {
    private final AtomicInteger siguienteId = new AtomicInteger(1);

    public Jugador crearJugador(String nombre) {
        String nombreFinal = nombre == null || nombre.trim().isEmpty() ? "Entrenador" : nombre.trim();
        return new Jugador(siguienteId.getAndIncrement(), nombreFinal);
    }
}
