package com.pokeapp.dao;

import com.pokeapp.config.ConexionBD;
import com.pokeapp.model.Movimiento;
import com.pokeapp.model.Pokemon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Lee el catálogo, las estadísticas y los cuatro movimientos directamente desde MySQL. */
public class PokemonDAO {
    private static final Map<String, String> TIPOS_MOVIMIENTO = crearTiposMovimiento();
    private static final Map<String, Integer> POTENCIAS = crearPotencias();

    private static final String CONSULTA_CATALOGO =
            "SELECT p.id, p.nombre, p.tipo, p.atk, p.hp, p.spd, "
            + "m1.nombre AS mov1_nombre, m1.pp AS mov1_pp, m1.hace_dano AS mov1_dano, "
            + "m2.nombre AS mov2_nombre, m2.pp AS mov2_pp, m2.hace_dano AS mov2_dano, "
            + "m3.nombre AS mov3_nombre, m3.pp AS mov3_pp, m3.hace_dano AS mov3_dano, "
            + "m4.nombre AS mov4_nombre, m4.pp AS mov4_pp, m4.hace_dano AS mov4_dano "
            + "FROM pokemon p "
            + "LEFT JOIN movimiento m1 ON m1.nombre = p.movimiento_1 "
            + "LEFT JOIN movimiento m2 ON m2.nombre = p.movimiento_2 "
            + "LEFT JOIN movimiento m3 ON m3.nombre = p.movimiento_3 "
            + "LEFT JOIN movimiento m4 ON m4.nombre = p.movimiento_4 "
            + "ORDER BY p.id";

    public List<Pokemon> obtenerTodos() throws SQLException {
        List<Pokemon> catalogo = new ArrayList<>();

        try (Connection conexion = ConexionBD.Conectar();
             PreparedStatement consulta = conexion.prepareStatement(CONSULTA_CATALOGO);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                String nombre = resultado.getString("nombre");
                String tipo = resultado.getString("tipo");
                Movimiento[] movimientos = new Movimiento[4];
                for (int i = 1; i <= 4; i++) {
                    movimientos[i - 1] = crearMovimiento(resultado, "mov" + i, tipo);
                }

                int hp = resultado.getInt("hp");
                // La tabla pokemon no guarda defensa ni nivel: se usa HP como defensa base y nivel 30.
                int defensaBase = Math.max(35, hp);
                String spriteKey = obtenerClaveSprite(nombre);

                Pokemon pokemon = new Pokemon(
                        resultado.getInt("id"), nombre, tipo, null, 30,
                        hp, resultado.getInt("atk"), defensaBase,
                        resultado.getInt("spd"), spriteKey, movimientos
                );
                catalogo.add(pokemon);
            }
        }

        if (catalogo.isEmpty()) {
            throw new SQLException("La tabla pokemon no contiene registros. Importá el archivo database/pokeapp.sql.");
        }
        return catalogo;
    }

    private Movimiento crearMovimiento(ResultSet resultado, String prefijo, String tipoPokemon) throws SQLException {
        String nombre = resultado.getString(prefijo + "_nombre");
        if (nombre == null || nombre.trim().isEmpty()) return null;

        int pp = resultado.getInt(prefijo + "_pp");
        boolean haceDano = resultado.getBoolean(prefijo + "_dano");
        String clave = normalizar(nombre);
        String tipoMovimiento = TIPOS_MOVIMIENTO.getOrDefault(clave, tipoPokemon);
        int potencia = haceDano ? POTENCIAS.getOrDefault(clave, 60) : 0;

        return new Movimiento(nombre, tipoMovimiento, potencia, pp, haceDano);
    }

    private String obtenerClaveSprite(String nombre) {
        String clave = normalizar(nombre).replace(' ', '-');
        if ("hypno".equals(clave)) return "hypno-f";
        return clave;
    }

    private static String normalizar(String texto) {
        String sinAcentos = Normalizer.normalize(texto == null ? "" : texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinAcentos.toLowerCase(Locale.ROOT).trim();
    }

    private static Map<String, String> crearTiposMovimiento() {
        Map<String, String> mapa = new HashMap<>();
        poner(mapa, "Fantasma", "Lengüetazo", "Tinieblas", "Rayo Confuso");
        poner(mapa, "Psíquico", "Hipnosis", "Psíquico", "Confusión", "Anulación", "Recuperación");
        poner(mapa, "Roca", "Lanza Rocas", "Tornillo Roca");
        poner(mapa, "Bicho", "Chupavidas");
        poner(mapa, "Planta", "Espora", "Rayo Solar", "Látigo Cepa", "Megagotar", "Atadura");
        poner(mapa, "Eléctrico", "Paralizador", "Rayo", "Trueno", "Onda Voltio");
        poner(mapa, "Volador", "Ataque Ala", "Vuelo", "Torbellino");
        poner(mapa, "Tierra", "Terremoto", "Excavar", "Ataque Arena", "Fisura");
        poner(mapa, "Veneno", "Residuos", "Tóxico", "Polución");
        poner(mapa, "Lucha", "Golpe Kárate", "Sumisión", "Movimiento Sísmico", "Contador");
        poner(mapa, "Fuego", "Lanzallamas", "Ascuas", "Llamarada");
        poner(mapa, "Agua", "Surf", "Hidrobomba");
        poner(mapa, "Hielo", "Rayo Hielo");
        poner(mapa, "Normal", "Chirrido", "Fortaleza", "Agilidad", "Autodestrucción");
        return mapa;
    }

    private static Map<String, Integer> crearPotencias() {
        Map<String, Integer> mapa = new HashMap<>();
        potencia(mapa, 95, "Hidrobomba", "Llamarada", "Fisura");
        potencia(mapa, 90, "Terremoto", "Rayo Solar", "Trueno", "Autodestrucción");
        potencia(mapa, 85, "Lanzallamas", "Surf", "Psíquico");
        potencia(mapa, 80, "Chupavidas", "Rayo", "Látigo Cepa", "Megagotar", "Hidrobomba");
        potencia(mapa, 70, "Excavar", "Vuelo", "Lanza Rocas", "Sumisión");
        potencia(mapa, 45, "Lengüetazo", "Ataque Arena", "Ascuas");
        return mapa;
    }

    private static void poner(Map<String, String> mapa, String tipo, String... movimientos) {
        for (String movimiento : movimientos) mapa.put(normalizar(movimiento), tipo);
    }

    private static void potencia(Map<String, Integer> mapa, int potencia, String... movimientos) {
        for (String movimiento : movimientos) mapa.put(normalizar(movimiento), potencia);
    }
}
