package co.edu.unbosque.config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import co.edu.unbosque.exceptions.PersistenceException;

public class ConnectionBD {
private static final String url="jdbc:mysql://100.64.242.64:3306/proyecto_db1";
    private static final String user="alejandra";
    private static final String password="Alejandra2026*";
    
    public static Connection obtenerConexion(){
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new PersistenceException("Error al conectar la base de datos", e);
        }
    }
}
