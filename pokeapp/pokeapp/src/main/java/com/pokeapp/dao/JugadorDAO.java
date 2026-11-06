package com.pokeapp.dao;

import com.pokeapp.config.ConexionBD;
import com.pokeapp.model.Jugador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/** Persiste al entrenador y los seis integrantes de su equipo en la tabla entrenador. */
public class JugadorDAO {
    private static final String INSERTAR_ENTRENADOR =
            "INSERT INTO entrenador (nombre, pokemon1, pokemon2, pokemon3, pokemon4, pokemon5, pokemon6, campeon) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, 0)";

    public Jugador crearJugador(String nombre, List<Integer> idsPokemon) throws SQLException {
        if (idsPokemon == null || idsPokemon.size() != 6) {
            throw new IllegalArgumentException("El entrenador debe tener seis Pokémon registrados.");
        }

        String nombreFinal = nombre == null ? "" : nombre.trim();
        if (nombreFinal.isEmpty()) {
            throw new IllegalArgumentException("Ingresá el nombre del entrenador antes de comenzar.");
        }

        try (Connection conexion = ConexionBD.Conectar();
             PreparedStatement consulta = conexion.prepareStatement(INSERTAR_ENTRENADOR, Statement.RETURN_GENERATED_KEYS)) {
            consulta.setString(1, nombreFinal);
            for (int i = 0; i < 6; i++) consulta.setInt(i + 2, idsPokemon.get(i));

            if (consulta.executeUpdate() != 1) {
                throw new SQLException("No se pudo guardar el entrenador en la base de datos.");
            }

            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("MySQL no devolvió el ID del entrenador creado.");
                return new Jugador(claves.getInt(1), nombreFinal);
            }
        }
    }

    /** campeon=1 cuando gana; campeon=0 cuando pierde. */
    public void actualizarCampeon(int idEntrenador, boolean campeon) throws SQLException {
        String sql = "UPDATE entrenador SET campeon = ? WHERE id = ?";
        try (Connection conexion = ConexionBD.Conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, campeon ? 1 : 0);
            consulta.setInt(2, idEntrenador);
            if (consulta.executeUpdate() != 1) {
                throw new SQLException("No se encontró el registro del entrenador para guardar el resultado.");
            }
        }
    }
}
