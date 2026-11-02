package co.edu.unbosque.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import co.edu.unbosque.exceptions.PersistenceException;

public final class ConnectionBD {

    private static final String JNDI_NAME =
            "java:/jdbc/CafeLudotecaDS";

    private ConnectionBD() {
    }

    public static Connection obtenerConexion() {
        try {
            InitialContext context = new InitialContext();

            DataSource dataSource =
                    (DataSource) context.lookup(JNDI_NAME);

            context.close();

            return dataSource.getConnection();

        } catch (NamingException | SQLException e) {
            throw new PersistenceException(
                    "Error al obtener la conexion con MySQL mediante WildFly",
                    e
            );
        }
    }
}
