package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String url = "jdbc:mysql://localhost:3306/pokemonfury"; 
    private static final String user = "root"; 
    private static final String password = "S9tbd2dnka"; 

    public static Connection Conectar() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}