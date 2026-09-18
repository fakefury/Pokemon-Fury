package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JugadorDAO {
	
    public void crear(PokemonJugador jugador) throws SQLException {
        String sql = "INSERT INTO jugador (nombre) VALUES (?)";
        try (Connection conn = ConexionBD.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, jugador.getNombre());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    jugador.setIdJugador(rs.getInt(1)); 
                }
            }
        }
    }

    public PokemonJugador obtenerPorId(int idJugador) throws SQLException {
        String sql = "SELECT * FROM jugador WHERE id_jugador = ?";
        try (Connection conn = ConexionBD.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idJugador);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Jugador j = new Jugador();
                    j.setIdJugador(rs.getInt("id_jugador"));
                    j.setNombre(rs.getString("nombre"));
                    return j;
                }
            }
        }
        return null;
    }

    public void actualizar(PokemonJugador jugador) throws SQLException {
        String sql = "UPDATE jugador SET nombre = ? WHERE id_jugador = ?";
        try (Connection conn = ConexionBD.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, jugador.getNombre());
            stmt.setInt(2, jugador.getIdJugador());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int idJugador) throws SQLException {
        String sql = "DELETE FROM jugador WHERE id_jugador = ?";
        try (Connection conn = ConexionBD.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idJugador);
            stmt.executeUpdate();
        }
    }
}
