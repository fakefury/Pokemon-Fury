package pokemonfury;

import pokemonfury.model.*;
import pokemonfury.service.*;
import pokemonfury.exceptions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== INICIO DE POKÉMON FURY ===");

        JugadorService jugadorService = new JugadorService();
        PartidaService partidaService = new PartidaService();
        PokemonService pokemonService = new PokemonService();
        CombateService combateService = new CombateService();
        DecisionService decisionService = new DecisionService();
 
        try {
            Jugador jugador = jugadorService.crearJugador("Alex");
            System.out.println("Jugador creado: " + jugador.getNombre());

            Partida partida = partidaService.crearPartida(jugador.getIdJugador(), "Pueblo Inicial", 5);
            System.out.println("Partida creada con éxito. Poké Balls disponibles: " + partida.getCantidadPokeballs());

            System.out.println("\n--- Selección de Pokémon Inicial ---");
            try {
                System.out.println("Intentando elegir un inicial no válido ('Pikachu')...");
                pokemonService.validarPokemonInicial("Pikachu");
            } catch (InicialInvalidoException e) {
                System.out.println("[EXCEPCIÓN CAPTURADA CORRECTAMENTE]: " + e.getMessage());
            }

            String pokemonElegido = "Gible";
            pokemonService.validarPokemonInicial(pokemonElegido);
            PokemonJugador miPokemon = (PokemonJugador) pokemonService.crearPokemonJugador(
                partida.getIdPartida(), "GarchompJunior", pokemonElegido, 5, 50, 50, 15, 12, 10, 0
            );
            partida.setPokemonActivo(miPokemon.getIdPokemon());
            System.out.println("¡Has elegido a " + pokemonElegido + " (" + miPokemon.getApodo() + ") como tu compañero!");

            System.out.println("\n--- Exploración y Decisiones ---");
            Decision decisionActual = decisionService.obtenerDecisionActual(1);
            System.out.println("Situación: " + decisionActual.getDescripcion());
            System.out.println("1. " + decisionActual.getOpcionA());
            System.out.println("2. " + decisionActual.getOpcionB());
            
            decisionService.ejecutarDecision(decisionActual, "A");
            System.out.println("Consecuencia aplicada: " + decisionActual.getConsecuenciaA());

            System.out.println("\n--- Encuentro y Combate Salvaje ---");
            PokemonSalvaje pokemonSalvaje = (PokemonSalvaje) pokemonService.crearPokemonSalvaje(
                "Pidgey", "Normal", "Volador", 4, 30, 30, 8, 7, 12, 0, "Ruta 1", true, 0.5
            );

            try {
                combateService.iniciarCombate(partida.getIdPartida(), null, pokemonSalvaje.getIdPokemon());
            } catch (PokemonNoDisponibleException e) {
                System.out.println("[EXCEPCIÓN CAPTURADA CORRECTAMENTE]: " + e.getMessage());
            }

            Combate combate = combateService.iniciarCombate(
                partida.getIdPartida(), miPokemon.getIdPokemon(), pokemonSalvaje.getIdPokemon()
            );
            System.out.println("¡Un " + pokemonSalvaje.getNombre() + " salvaje apareció en " + pokemonSalvaje.getZona() + "!");

            combateService.resolverAtaque(miPokemon, pokemonSalvaje);
            System.out.println("¡" + miPokemon.getApodo() + " atacó al " + pokemonSalvaje.getNombre() + " salvaje!");
            
            pokemonSalvaje.setHpActual(0);
            if (pokemonSalvaje.estaDerrotado()) {
                combate.setResultado("VICTORIA");
                System.out.println("¡El Pokémon salvaje ha sido derrotado! Victoria para el jugador.");
            }

            combateService.finalizarCombate(combate);

            System.out.println("\n--- Intento de Captura ---");
            partida.setCantidadPokeballs(0);
            
            try {
                pokemonService.capturarPokemon(partida, pokemonSalvaje);
            } catch (SinPokeBallsException e) {
                System.out.println("[EXCEPCIÓN CAPTURADA CORRECTAMENTE]: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Error inesperado en el flujo: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== FIN DEL FLUJO END-TO-END DE POKÉMON FURY ===");
    }
}