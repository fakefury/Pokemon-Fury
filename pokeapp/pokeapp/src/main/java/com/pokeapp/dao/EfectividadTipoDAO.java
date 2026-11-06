package com.pokeapp.dao;

import com.pokeapp.config.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Consulta los multiplicadores guardados en efectividad_tipo. */
public class EfectividadTipoDAO {
    public double obtenerMultiplicador(String tipoAtacante, String tipoDefensor) throws SQLException {
        if (tipoAtacante == null || tipoDefensor == null) return 1.0;

        String sql = "SELECT multiplicador FROM efectividad_tipo "
                + "WHERE tipo_atacante = ? AND tipo_defensor = ?";
        try (Connection conexion = ConexionBD.Conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, tipoAtacante);
            consulta.setString(2, tipoDefensor);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? resultado.getDouble("multiplicador") : 1.0;
            }
        }
    }
}
