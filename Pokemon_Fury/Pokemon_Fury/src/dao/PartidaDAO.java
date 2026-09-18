package dao;

import java.sql.*;
import model.Partida;

public class PartidaDAO {

    public void crear(Partida partida) throws SQLException {
        String sql = "INSERT INTO partida (id_jugador, progreso_historia, ubicacion_actual, cantidad_pokeballs, final_obtenido) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, partida.getIdJugador());
            stmt.setInt(2, partida.getProgresoHistoria());
            stmt.setString(3, partida.getUbicacionActual());
            stmt.setInt(4, partida.getCantidadPokeballs());
            stmt.setString(5, partida.getFinalObtenido());
            
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    partida.setIdPartida(rs.getInt(1));
                }
            }
        }
    }

    public Partida obtenerPorId(int idPartida) throws SQLException {
        String sql = "SELECT * FROM partida WHERE id_partida = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPartida);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Partida p = new Partida();
                    p.setIdPartida(rs.getInt("id_partida"));
                    p.setIdJugador(rs.getInt("id_jugador"));
                    p.setProgresoHistoria(rs.getInt("progreso_historia"));
                    p.setUbicacionActual(rs.getString("ubicacion_actual"));
                    p.setCantidadPokeballs(rs.getInt("cantidad_pokeballs"));
                    p.setFinalObtenido(rs.getString("final_obtenido"));
                    return p;
                }
            }
        }
        return null;
    }

    public void actualizarProgreso(Partida partida) throws SQLException {
        String sql = "UPDATE partida SET progreso_historia = ?, ubicacion_actual = ?, cantidad_pokeballs = ?, final_obtenido = ? " +
                     "WHERE id_partida = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, partida.getProgresoHistoria());
            stmt.setString(2, partida.getUbicacionActual());
            stmt.setInt(3, partida.getCantidadPokeballs());
            stmt.setString(4, partida.getFinalObtenido());
            stmt.setInt(5, partida.getIdPartida());
            stmt.executeUpdate();
        }
    }
}
