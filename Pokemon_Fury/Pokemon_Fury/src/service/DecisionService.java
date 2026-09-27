package service;

import exceptions.DecisionInvalidaException;
import model.Decision;
import model.Partida;

public class DecisionService {

    public void validarDecision(Partida partida, Decision decision) throws DecisionInvalidaException {
        if (partida == null || decision == null) {
            throw new DecisionInvalidaException("La partida o la decisión no pueden ser nulas.");
        }

        if (decision.getIdDecision() != partida.getProgresoHistoria()) {
            throw new DecisionInvalidaException("No se puede tomar la decisión ID: " + decision.getIdDecision() +
                    ". El jugador se encuentra en el evento N° " + partida.getProgresoHistoria() + ".");
        }
    }

    public void ejecutarDecision(Partida partida, Decision decision, boolean eligioOpcionA) throws DecisionInvalidaException {

        validarDecision(partida, decision);


        aplicarConsecuencia(partida, decision, eligioOpcionA);

        actualizarHistoria(partida);
    }

    public void aplicarConsecuencia(Partida partida, Decision decision, boolean eligioOpcionA) {
        String consecuenciaAplicada;

        if (eligioOpcionA) {
            consecuenciaAplicada = decision.getConsecuenciaA();
            System.out.println("Has elegido: " + decision.getOpcionA());
        } else {
            consecuenciaAplicada = decision.getConsecuenciaB();
            System.out.println("Has elegido: " + decision.getOpcionB());
        }

        System.out.println("Resultado: " + consecuenciaAplicada);


        if (consecuenciaAplicada.toLowerCase().contains("pokeball") || consecuenciaAplicada.toLowerCase().contains("ayudar")) {
            partida.setCantidadPokeballs(partida.getCantidadPokeballs() + 2);
            System.out.println("¡Has recibido 2 Poké Balls adicionales por tu elección!");
        }
    }

    public void actualizarHistoria(Partida partida) {
        int nuevoProgreso = partida.getProgresoHistoria() + 1;
        partida.setProgresoHistoria(nuevoProgreso);
        System.out.println("Progreso de historia actualizado al evento N° " + nuevoProgreso);
    }
}
