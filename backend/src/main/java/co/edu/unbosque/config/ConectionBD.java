package co.edu.unbosque.config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectionBD {
private static final String url="jdbc:mysql://100.64.242.64:3306/proyecto_db1";
    private static final String user="alejandra";
    private static final String password="Alejandra2026*";
    
    public static Connection obtenerConexion() throws SQLException{
        return DriverManager.getConnection(url, user, password);
    }
}
