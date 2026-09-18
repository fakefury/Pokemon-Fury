package main;

import model.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE INSTANCIACIÓN DE MODELOS: POKEMON FURY ===");

        PokemonJugador jugador = new PokemonJugador();
        jugador.setNombre("Gible");
        jugador.setNivel(5);
        jugador.setApodo("SlowYuji");
        System.out.println("-> Creado Pokemon Jugador: " + jugador.getNombre() + " (Apodo: " + jugador.getApodo() + ")");

        PokemonSalvaje salvaje = new PokemonSalvaje();
        salvaje.setNombre("Pidgey");
        salvaje.setNivel(3);
        salvaje.setZona("Ruta 1");
        System.out.println("-> Creado Pokemon Salvaje: " + salvaje.getNombre() + " en la zona " + salvaje.getZona());

        PokemonRival rival = new PokemonRival();
        rival.setNombre("Charmander");
        rival.setNivel(6);
        rival.setEstrategia("Ofensiva");
        System.out.println("-> Creado Pokemon Rival: " + rival.getNombre() + " con estrategia " + rival.getEstrategia());

        PokemonJefe jefe = new PokemonJefe();
        jefe.setNombre("Nidoking");
        jefe.setNivel(50);
        jefe.setFaseActual(1);
        System.out.println("-> Creado Pokemon Jefe: " + jefe.getNombre() + " en fase " + jefe.getFaseActual());

        System.out.println("=== PRUEBA FINALIZADA CON ÉXITO ===");
    }
}