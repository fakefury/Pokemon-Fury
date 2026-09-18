package dao;

import java.sql.*;
import model.Combate;

public class CombateDAO {

    public void registrar(Combate combate) throws SQLException {
        String sql = "INSERT INTO combate (id_partida, id_pokemon_jugador, id_pokemon_enemigo, resultado, turno_actual) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, combate.getIdPartida());
            stmt.setInt(2, combate.getPokemonJugador().getIdPokemon());
            stmt.setInt(3, combate.getPokemonEnemigo().getIdPokemon());
            stmt.setString(4, combate.getResultado());
            stmt.setInt(5, combate.getTurnoActual());

            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    combate.setIdCombate(rs.getInt(1));
                }
            }
        }
    }
    public void actualizarEstado(Combate combate) throws SQLException {
        String sql = "UPDATE combate SET resultado = ?, turno_actual = ? WHERE id_combate = ?";
        try (Connection conn = ConexionBD.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, combate.getResultado());
            stmt.setInt(2, combate.getTurnoActual());
            stmt.setInt(3, combate.getIdCombate());
            stmt.executeUpdate();
        }
    }
}
