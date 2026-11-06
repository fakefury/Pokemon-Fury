package com.pokeapp.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Conexión JDBC. Lee database.properties y permite sobrescribir sus valores por variables de entorno. */
public final class ConexionBD {
    private static final String URL_DEFECTO =
            "jdbc:mysql://localhost:3306/pokeapp?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";

    private ConexionBD() {
    }

    public static Connection Conectar() throws SQLException {
        Properties propiedades = cargarPropiedades();
        String url = valor("db.url", "POKEAPP_DB_URL", URL_DEFECTO, propiedades);
        String usuario = valor("db.user", "POKEAPP_DB_USER", "root", propiedades);
        String clave = valor("db.password", "POKEAPP_DB_PASSWORD", "", propiedades);

        try {
            // Nombre del driver incluido en Connector/J 5.1.49.
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("No se encontró MySQL Connector/J. Revisá las dependencias de Maven.", ex);
        }

        return DriverManager.getConnection(url, usuario, clave);
    }

    private static Properties cargarPropiedades() throws SQLException {
        Properties propiedades = new Properties();
        Path archivo = Path.of("database.properties");
        if (Files.exists(archivo)) {
            try (InputStream entrada = Files.newInputStream(archivo)) {
                propiedades.load(entrada);
            } catch (IOException ex) {
                throw new SQLException("No se pudo leer el archivo database.properties.", ex);
            }
        }
        return propiedades;
    }

    private static String valor(String propiedad, String variable, String defecto, Properties propiedades) {
        String sistema = System.getProperty(propiedad);
        if (sistema != null && !sistema.trim().isEmpty()) return sistema.trim();

        String entorno = System.getenv(variable);
        if (entorno != null && !entorno.trim().isEmpty()) return entorno.trim();

        String archivo = propiedades.getProperty(propiedad);
        return archivo == null ? defecto : archivo.trim();
    }
}
