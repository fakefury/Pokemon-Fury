package service;

import exceptions.CombateFinalizadoException;
import exceptions.PokemonDebilitadoException;
import exceptions.PokemonNoDisponibleException;
import model.*;

public class CombateService {

    public Combate iniciarCombate(Partida partida, Pokemon pokemonEnemigo) throws PokemonNoDisponibleException {
        if (partida.getPokemonActivo() == null) {
            throw new PokemonNoDisponibleException("No es posible iniciar el combate: El jugador no tiene un Pokémon activo seleccionado.");
        }

        Combate combate = new Combate();
        combate.setIdPartida(partida.getIdPartida());
        combate.setPokemonJugador(partida.getPokemonActivo());
        combate.setPokemonEnemigo(pokemonEnemigo);
        combate.setResultado("EN_PROCESO");
        combate.setTurnoActual(1);

        return combate;
    }

    public void resolverAtaque(Combate combate, Combatible atacante, Combatible defensor) 
            throws CombateFinalizadoException, PokemonDebilitadoException {

        if ("VICTORIA".equals(combate.getResultado()) || "DERROTA".equals(combate.getResultado())) {
            throw new CombateFinalizadoException("El combate ya ha finalizado con el resultado: " + combate.getResultado());
        }

        if (atacante.estaDerrotado()) {
            throw new PokemonDebilitadoException("El Pokémon atacante está debilitado y no puede realizar ninguna acción.");
        }

        int danio = calcularDanio(atacante, defensor);
        defensor.recibirDanio(danio);

        if (defensor.estaDerrotado()) {
            if (defensor == combate.getPokemonEnemigo()) {
                combate.setResultado("VICTORIA");
            } else {
                combate.setResultado("DERROTA");
            }
        }

        combate.setTurnoActual(combate.getTurnoActual() + 1);
    }

    private int calcularDanio(Combatible atacante, Combatible defensor) {
        int danioBase = 10;
        return Math.max(1, danioBase);
    }
}
