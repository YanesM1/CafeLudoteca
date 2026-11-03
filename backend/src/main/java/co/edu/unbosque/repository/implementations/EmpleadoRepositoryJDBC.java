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
                + "(documento_emp, cargo, nombre, apellido, correo) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, empleado.getDocumentoEmp());
            statement.setString(2, empleado.getCargo());
            statement.setString(3, empleado.getNombre());
            statement.setString(4, empleado.getApellido());
            statement.setString(5, empleado.getCorreo());
          

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al registrar el empleado", e);
        }
    }


    @Override
    public List<Empleado> findAll() {
        List<Empleado> lista=new ArrayList<>();
        String sql="SELECT documento_emp,cargo,nombre,apellido,correo FROM EMPLEADO ORDER BY apellido,nombre";
        try(Connection c=ConnectionBD.obtenerConexion();
            PreparedStatement ps=c.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()) {
            while(rs.next()) lista.add(mapearEmpleado(rs));
            return lista;
        } catch(SQLException e) {
            throw new PersistenceException("Error al listar empleados",e);
        }
    }

    // BUSCAR EMPLEADOS POR NOMBRE
    @Override
    public List<Empleado> findByNombre(String nombre) {

        List<Empleado> empleados = new ArrayList<>();

        if (nombre == null || nombre.isBlank()) {
            return empleados;
        }

        String sql = "SELECT documento_emp, cargo, nombre, apellido, correo "
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
    public Optional<Empleado> findByDocumento(String documentoEmp) {

        if (documentoEmp == null || documentoEmp.isBlank()) {
            return Optional.empty();
        }

        String sql = "SELECT documento_emp, cargo, nombre, apellido, correo "
                + "FROM EMPLEADO WHERE documento_emp = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documentoEmp);

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
    public boolean updateEmpleado(String documentoEmp, Empleado empleado) {

        if (documentoEmp == null || documentoEmp.isBlank() || empleado == null) {
            return false;
        }

        String sql = "UPDATE EMPLEADO SET  cargo = ?, nombre = ?, apellido = ?, "
                + "correo = ? WHERE documento_emp = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion()) {

            int filasAfectadas;

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                statement.setString(1, empleado.getCargo());
                statement.setString(2, empleado.getNombre());
                statement.setString(3, empleado.getApellido());
                statement.setString(4, empleado.getCorreo());
        
                statement.setString(5, documentoEmp);

                filasAfectadas = statement.executeUpdate();
            }

            // Si hubo modificaciones, la actualización fue exitosa.
            if (filasAfectadas > 0) {
                return true;
            }

            // Si no hubo modificaciones, comprobar si el empleado existe.
            return existeEmpleado(conexion, documentoEmp);

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al actualizar el empleado", e);
        }
    }

    // VERIFICAR SI EXISTE UN EMPLEADO
    private boolean existeEmpleado(Connection conexion, String documentoEmp)
            throws SQLException {

        String sql = "SELECT 1 FROM EMPLEADO WHERE documento_emp = ?";

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documentoEmp);

            try (ResultSet resultado = statement.executeQuery()) {
                return resultado.next();
            }
        }
    }

    // ELIMINAR EMPLEADO
    @Override
    public boolean deleteEmpleado(String documentoEmp) {

        if (documentoEmp == null || documentoEmp.isBlank()) {
            return false;
        }

        String sql = "DELETE FROM EMPLEADO WHERE documento_emp = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documentoEmp);

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

        empleado.setDocumentoEmp(resultado.getString("documento_emp"));
        empleado.setCargo(resultado.getString("cargo"));
        empleado.setNombre(resultado.getString("nombre"));
        empleado.setApellido(resultado.getString("apellido"));
        empleado.setCorreo(resultado.getString("correo"));


        return empleado;
    }
}
