package service;

import exceptions.DecisionInvalidaException;
import exceptions.InicialInvalidoException;
import model.*;

public class PartidaService {

    private PokemonService pokemonService = new PokemonService();

    public Partida crearPartida(Jugador jugador) {
        Partida nuevaPartida = new Partida();
        nuevaPartida.setIdJugador(jugador.getIdJugador());
        nuevaPartida.setProgresoHistoria(1);
        nuevaPartida.setUbicacionActual("Pueblo de Inicio");
        nuevaPartida.setCantidadPokeballs(5);
        return nuevaPartida;
    }

    public PokemonJugador seleccionarPokemonInicial(Partida partida, String nombreElegido) throws InicialInvalidoException {
        
    	pokemonService.validarPokemonInicial(nombreElegido);

        PokemonJugador inicial = new PokemonJugador();
        inicial.setNombre(nombreElegido);
        inicial.setNivel(5);
        inicial.setHpMaximo(20);
        inicial.setHpActual(20);
        inicial.setAtaque(10);
        inicial.setDefensa(10);
        inicial.setVelocidad(10);
        inicial.setIdPartida(partida.getIdPartida());

        partida.setPokemonActivo(inicial);
        return inicial;
    }

    public void ejecutarDecision(Partida partida, Decision decision, boolean eligioOpcionA) throws DecisionInvalidaException {
        if (decision.getIdDecision() != partida.getProgresoHistoria()) {
            throw new DecisionInvalidaException("La decisión tomada (ID: " + decision.getIdDecision() + 
                    ") no corresponde al progreso actual del evento (Progreso: " + partida.getProgresoHistoria() + ").");
        }

        if (eligioOpcionA) {
            System.out.println("Consecuencia: " + decision.getConsecuenciaA());
        } else {
            System.out.println("Consecuencia: " + decision.getConsecuenciaB());
        }

        partida.setProgresoHistoria(partida.getProgresoHistoria() + 1);
    }
}
