package com.pokeapp.dao;
import com.pokeapp.model.Partida;
import java.util.concurrent.atomic.AtomicInteger;
public class PartidaDAO {
    private final AtomicInteger siguienteId = new AtomicInteger(1);
    public Partida guardar(Partida partida) {
        if (partida.getIdPartida() == 0) partida.setIdPartida(siguienteId.getAndIncrement());
        return partida;
    }
}
