package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PokemonDAO {

    public void crear(Pokemon pokemon) throws SQLException {
        String sql = "INSERT INTO pokemon (nombre, tipo, tipo_primario, tipo_secundario, nivel, hp_maximo, hp_actual, " +
                     "ataque, defensa, velocidad, experiencia, id_partida, apodo, zona, capturable, probabilidad_huida, " +
                     "nivel_entrenador, estrategia, bono_rival, fase_actual, multiplicador_defensa, recompensa_historia) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pokemon.getNombre());
            stmt.setString(3, pokemon.getTipoPrimario());
            stmt.setString(4, pokemon.getTipoSecundario());
            stmt.setInt(5, pokemon.getNivel());
            stmt.setInt(6, pokemon.getHpMaximo());
            stmt.setInt(7, pokemon.getHpActual());
            stmt.setInt(8, pokemon.getAtaque());
            stmt.setInt(9, pokemon.getDefensa());
            stmt.setInt(10, pokemon.getVelocidad());
            stmt.setInt(11, pokemon.getExperiencia());

            if (pokemon instanceof PokemonJugador pj) {
                stmt.setString(2, "JUGADOR");
                stmt.setInt(12, pj.getIdPartida());
                stmt.setString(13, pj.getApodo());
                setNulls(stmt, 14, 22);
            } else if (pokemon instanceof PokemonSalvaje ps) {
                stmt.setString(2, "SALVAJE");
                setNulls(stmt, 12, 13);
                stmt.setString(14, ps.getZona());
                stmt.setBoolean(15, ps.isCapturable());
                stmt.setDouble(16, ps.getProbabilidadHuida());
                setNulls(stmt, 17, 22);
            } else if (pokemon instanceof PokemonRival pr) {
                stmt.setString(2, "RIVAL");
                setNulls(stmt, 12, 16);
                stmt.setInt(17, pr.getNivelEntrenador());
                stmt.setString(18, pr.getEstrategia());
                stmt.setInt(19, pr.getBonoRival());
                setNulls(stmt, 20, 22);
            } else if (pokemon instanceof PokemonJefe pjefe) {
                stmt.setString(2, "JEFE");
                setNulls(stmt, 12, 19);
                stmt.setInt(20, pjefe.getFaseActual());
                stmt.setDouble(21, pjefe.getMultiplicadorDefensa());
                stmt.setString(22, pjefe.getRecompensaHistoria());
            }

            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pokemon.setIdPokemon(rs.getInt(1));
                }
            }
        }
    }

    public Pokemon obtenerPorId(int idPokemon) throws SQLException {
        String sql = "SELECT * FROM pokemon WHERE id_pokemon = ?";
        try (Connection conn = ConexionBD.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPokemon);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPokemon(rs);
                }
            }
        }
        return null;
    }

    public void actualizarEstado(Pokemon pokemon) throws SQLException {
        String sql = "UPDATE pokemon SET hp_actual = ?, nivel = ?, experiencia = ? WHERE id_pokemon = ?";
        try (Connection conn = ConexionBD.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pokemon.getHpActual());
            stmt.setInt(2, pokemon.getNivel());
            stmt.setInt(3, pokemon.getExperiencia());
            stmt.setInt(4, pokemon.getIdPokemon());
            stmt.executeUpdate();
        }
    }

    private Pokemon mapearPokemon(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        Pokemon p;

        switch (tipo) {
            case "JUGADOR" -> {
                PokemonJugador pj = new PokemonJugador();
                pj.setIdPartida(rs.getInt("id_partida"));
                pj.setApodo(rs.getString("apodo"));
                p = pj;
            }
            case "SALVAJE" -> {
                PokemonSalvaje ps = new PokemonSalvaje();
                ps.setZona(rs.getString("zona"));
                ps.setCapturable(rs.getBoolean("capturable"));
                ps.setProbabilidadHuida(rs.getDouble("probabilidad_huida"));
                p = ps;
            }
            case "RIVAL" -> {
                PokemonRival pr = new PokemonRival();
                pr.setNivelEntrenador(rs.getInt("nivel_entrenador"));
                pr.setEstrategia(rs.getString("estrategia"));
                pr.setBonoRival(rs.getInt("bono_rival"));
                p = pr;
            }
            case "JEFE" -> {
                PokemonJefe pjefe = new PokemonJefe();
                pjefe.setFaseActual(rs.getInt("fase_actual"));
                pjefe.setMultiplicadorDefensa(rs.getDouble("multiplicador_defensa"));
                pjefe.setRecompensaHistoria(rs.getString("recompensa_historia"));
                p = pjefe;
            }
            default -> throw new IllegalArgumentException("Tipo de Pokémon desconocido: " + tipo);
        }
        
        p.setIdPokemon(rs.getInt("id_pokemon"));
        p.setNombre(rs.getString("nombre"));
        p.setTipoPrimario(rs.getString("tipo_primario"));
        p.setTipoSecundario(rs.getString("tipo_secundario"));
        p.setNivel(rs.getInt("nivel"));
        p.setHpMaximo(rs.getInt("hp_maximo"));
        p.setHpActual(rs.getInt("hp_actual"));
        p.setAtaque(rs.getInt("ataque"));
        p.setDefensa(rs.getInt("defensa"));
        p.setVelocidad(rs.getInt("velocidad"));
        p.setExperiencia(rs.getInt("experiencia"));

        return p;
    }

    private void setNulls(PreparedStatement stmt, int from, int to) throws SQLException {
        for (int i = from; i <= to; i++) {
            stmt.setNull(i, Types.NULL);
        }
    }
}
