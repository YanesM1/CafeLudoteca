package co.edu.unbosque.config;


import java.sql.Conection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectionBD {
private static final String url="jdbc:mysql://localhost:3306/cafeludoteca";
    private static final String user="cafeludoc";
    private static final String password="gladiador";
    
    public static Connection obtenerConexion() throws SQLException{
        return DriverManager.getConnection(url, user, password);
    }
}
