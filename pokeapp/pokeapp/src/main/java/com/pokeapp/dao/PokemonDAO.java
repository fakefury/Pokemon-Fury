package com.pokeapp.dao;

import com.pokeapp.model.Movimiento;
import com.pokeapp.model.Pokemon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Catálogo local del juego. No requiere instalar ni configurar MySQL. */
public class PokemonDAO {
    private final List<Pokemon> catalogo = crearCatalogo();

    public List<Pokemon> obtenerTodos() {
        List<Pokemon> copia = new ArrayList<>();
        for (Pokemon pokemon : catalogo) copia.add(pokemon.copiar());
        return copia;
    }

    private List<Pokemon> crearCatalogo() {
        List<Pokemon> lista = new ArrayList<>();
        lista.add(p(1, "Dugtrio", "TIERRA", null, 82, 80, 62, 105, "dugtrio",
                m("Excavar", "TIERRA", 70, 15), m("Terremoto", "TIERRA", 90, 10), m("Corte", "NORMAL", 50, 25), m("Avalancha", "ROCA", 65, 15)));
        lista.add(p(2, "Electrode", "ELÉCTRICO", null, 78, 75, 58, 125, "electrode",
                m("Rayo", "ELÉCTRICO", 80, 15), m("Chispa", "ELÉCTRICO", 55, 25), m("Bola Voltio", "ELÉCTRICO", 70, 15), m("Placaje", "NORMAL", 40, 35)));
        lista.add(p(3, "Haunter", "FANTASMA", "VENENO", 76, 92, 58, 95, "haunter",
                m("Bola Sombra", "FANTASMA", 80, 15), m("Bomba Lodo", "VENENO", 75, 15), m("Lengüetazo", "FANTASMA", 45, 25), m("Tinieblas", "FANTASMA", 60, 20)));
        lista.add(p(4, "Hitmonchan", "LUCHA", null, 84, 88, 82, 72, "hitmonchan",
                m("Puño Fuego", "FUEGO", 70, 15), m("Puño Hielo", "HIELO", 70, 15), m("Puño Trueno", "ELÉCTRICO", 70, 15), m("Golpe Karate", "LUCHA", 65, 20)));
        lista.add(p(5, "Hypno", "PSÍQUICO", null, 88, 80, 92, 67, "hypno-f",
                m("Psíquico", "PSÍQUICO", 85, 10), m("Confusión", "PSÍQUICO", 55, 25), m("Puño Fuego", "FUEGO", 70, 15), m("Golpe Cabeza", "NORMAL", 65, 20)));
        lista.add(p(6, "Ninetales", "FUEGO", null, 82, 91, 72, 100, "ninetales",
                m("Lanzallamas", "FUEGO", 85, 10), m("Ascuas", "FUEGO", 50, 25), m("Rapidez", "NORMAL", 60, 20), m("Mordisco", "NORMAL", 60, 20)));
        lista.add(p(7, "Onix", "ROCA", "TIERRA", 105, 65, 110, 40, "onix",
                m("Roca Afilada", "ROCA", 85, 10), m("Excavar", "TIERRA", 70, 15), m("Avalancha", "ROCA", 65, 15), m("Placaje", "NORMAL", 40, 35)));
        lista.add(p(8, "Parasect", "BICHO", "PLANTA", 90, 88, 82, 35, "parasect",
                m("Tijera X", "BICHO", 80, 15), m("Bomba Germen", "PLANTA", 80, 15), m("Corte", "NORMAL", 50, 25), m("Gigadrenado", "PLANTA", 75, 10)));
        lista.add(p(9, "Pidgeot", "NORMAL", "VOLADOR", 86, 90, 78, 100, "pidgeot",
                m("Ataque Ala", "VOLADOR", 60, 25), m("Tornado", "VOLADOR", 55, 20), m("Ataque Rápido", "NORMAL", 45, 30), m("Pico Taladro", "VOLADOR", 80, 15)));
        lista.add(p(10, "Starmie", "AGUA", "PSÍQUICO", 82, 88, 82, 115, "starmie",
                m("Surf", "AGUA", 85, 10), m("Hidrobomba", "AGUA", 95, 5), m("Psicorrayo", "PSÍQUICO", 65, 20), m("Rayo Hielo", "HIELO", 75, 15)));
        lista.add(p(11, "Tangela", "PLANTA", null, 94, 78, 105, 60, "tangela",
                m("Latigazo", "PLANTA", 80, 15), m("Hoja Aguda", "PLANTA", 75, 15), m("Bomba Germen", "PLANTA", 80, 15), m("Poder Pasado", "ROCA", 60, 15)));
        lista.add(p(12, "Weezing", "VENENO", null, 92, 95, 112, 60, "weezing",
                m("Bomba Lodo", "VENENO", 80, 15), m("Gas Venenoso", "VENENO", 50, 25), m("Autodestrucción", "NORMAL", 100, 5), m("Lanzallamas", "FUEGO", 75, 15)));
        return lista;
    }

    private Pokemon p(int id, String nombre, String tipo1, String tipo2, int hp,
                      int ataque, int defensa, int velocidad, String sprite, Movimiento... movimientos) {
        return new Pokemon(id, nombre, tipo1, tipo2, 30, hp, ataque, defensa, velocidad, sprite, movimientos);
    }

    private Movimiento m(String nombre, String tipo, int potencia, int pp) {
        return new Movimiento(nombre, tipo, potencia, pp, true);
    }
}
