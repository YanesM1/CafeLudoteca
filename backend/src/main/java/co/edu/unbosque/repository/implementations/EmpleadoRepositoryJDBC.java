
package co.edu.unbosque.repository.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.exceptions.PersistenceException;
import co.edu.unbosque.model.entity.Empleado;
import co.edu.unbosque.repository.interfaces.IEmpleadoRepository;

public class EmpleadoRepositoryJDBC implements IEmpleadoRepository {

    // REGISTRAR EMPLEADO
    @Override
    public boolean saveEmpleado(Empleado empleado) {

        if (empleado == null) {
            return false;
        }

        String sql = "INSERT INTO EMPLEADO "
                + "(documento, nombre, apellido, correo, cargo) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, empleado.getDocumento());
            statement.setString(2, empleado.getNombre());
            statement.setString(3, empleado.getApellido());
            statement.setString(4, empleado.getCorreo());
            statement.setString(5, empleado.getCargo());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al registrar el empleado", e);
        }
    }

    // BUSCAR EMPLEADOS POR NOMBRE
    @Override
    public List<Empleado> findByNombre(String nombre) {

        List<Empleado> empleados = new ArrayList<>();

        if (nombre == null || nombre.isBlank()) {
            return empleados;
        }

        String sql = "SELECT documento, nombre, apellido, correo, cargo "
                + "FROM EMPLEADO WHERE nombre LIKE ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, "%" + nombre.trim() + "%");

            try (ResultSet resultado = statement.executeQuery()) {

                while (resultado.next()) {
                    empleados.add(mapearEmpleado(resultado));
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al buscar empleados por nombre", e);
        }

        return empleados;
    }

    // BUSCAR EMPLEADO POR DOCUMENTO
    @Override
    public Optional<Empleado> findByDocumento(String documento) {

        if (documento == null || documento.isBlank()) {
            return Optional.empty();
        }

        String sql = "SELECT documento, nombre, apellido, correo, cargo "
                + "FROM EMPLEADO WHERE documento = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documento);

            try (ResultSet resultado = statement.executeQuery()) {

                if (resultado.next()) {
                    return Optional.of(mapearEmpleado(resultado));
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al consultar el empleado por documento", e);
        }

        return Optional.empty();
    }

    // ACTUALIZAR EMPLEADO
    @Override
    public boolean updateEmpleado(String documento, Empleado empleado) {

        if (documento == null || documento.isBlank() || empleado == null) {
            return false;
        }

        String sql = "UPDATE EMPLEADO SET nombre = ?, apellido = ?, "
                + "correo = ?, cargo = ? WHERE documento = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion()) {

            int filasAfectadas;

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {

                statement.setString(1, empleado.getNombre());
                statement.setString(2, empleado.getApellido());
                statement.setString(3, empleado.getCorreo());
                statement.setString(4, empleado.getCargo());
                statement.setString(5, documento);

                filasAfectadas = statement.executeUpdate();
            }

            // Si hubo modificaciones, la actualización fue exitosa.
            if (filasAfectadas > 0) {
                return true;
            }

            // Si no hubo modificaciones, comprobar si el empleado existe.
            return existeEmpleado(conexion, documento);

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al actualizar el empleado", e);
        }
    }

    // VERIFICAR SI EXISTE UN EMPLEADO
    private boolean existeEmpleado(Connection conexion, String documento)
            throws SQLException {

        String sql = "SELECT 1 FROM EMPLEADO WHERE documento = ?";

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documento);

            try (ResultSet resultado = statement.executeQuery()) {
                return resultado.next();
            }
        }
    }

    // ELIMINAR EMPLEADO
    @Override
    public boolean deleteEmpleado(String documento) {

        if (documento == null || documento.isBlank()) {
            return false;
        }

        String sql = "DELETE FROM EMPLEADO WHERE documento = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documento);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al eliminar el empleado", e);
        }
    }

    // CONVERTIR RESULTSET A ENTIDAD EMPLEADO
    private Empleado mapearEmpleado(ResultSet resultado)
            throws SQLException {

        Empleado empleado = new Empleado();

        empleado.setDocumento(resultado.getString("documento"));
        empleado.setNombre(resultado.getString("nombre"));
        empleado.setApellido(resultado.getString("apellido"));
        empleado.setCorreo(resultado.getString("correo"));
        empleado.setCargo(resultado.getString("cargo"));

        return empleado;
    }
}
