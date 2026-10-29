
package co.edu.unbosque.repository.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.model.entity.Empleado;
import co.edu.unbosque.repository.interfaces.IEmpleadoRepository;

public class EmpleadoRepositoryJDBC implements IEmpleadoRepository {

    @Override
    public boolean saveEmpleado(Empleado empleado) {
        String sql = "INSERT INTO EMPLEADO "
                + "(documento, nombre, apellido, correo, cargo) "
                + "VALUES (?, ?, ?, ?, ?)";

        if (empleado == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, empleado.getDocumento());
            statement.setString(2, empleado.getNombre());
            statement.setString(3, empleado.getApellido());
            statement.setString(4, empleado.getCorreo());
            statement.setString(5, empleado.getCargo());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Empleado> findByNombre(String nombre) {
        List<Empleado> empleados = new ArrayList<>();

        String sql = "SELECT documento, nombre, apellido, correo, cargo "
                + "FROM EMPLEADO WHERE nombre LIKE ?";

        if (nombre == null || nombre.isBlank()) {
            return empleados;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, "%" + nombre.trim() + "%");

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    empleados.add(mapearEmpleado(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empleados;
    }

    @Override
    public Optional<Empleado> findByDocumento(String documento) {
        String sql = "SELECT documento, nombre, apellido, correo, cargo "
                + "FROM EMPLEADO WHERE documento = ?";

        if (documento == null || documento.isBlank()) {
            return Optional.empty();
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documento);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearEmpleado(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean updateEmpleado(String documento, Empleado empleado) {
        String sql = "UPDATE EMPLEADO SET nombre = ?, apellido = ?, "
                + "correo = ?, cargo = ? WHERE documento = ?";

        if (documento == null || empleado == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, empleado.getNombre());
            statement.setString(2, empleado.getApellido());
            statement.setString(3, empleado.getCorreo());
            statement.setString(4, empleado.getCargo());
            statement.setString(5, documento);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEmpleado(String documento) {
        String sql = "DELETE FROM EMPLEADO WHERE documento = ?";

        if (documento == null || documento.isBlank()) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documento);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
