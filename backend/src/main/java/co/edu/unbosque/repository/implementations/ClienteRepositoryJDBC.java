package co.edu.unbosque.repository.implementations;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.model.entity.Cliente;
import co.edu.unbosque.repository.interfaces.IClienteRepository;

public class ClienteRepositoryJDBC implements IClienteRepository {

    @Override
    public boolean saveCliente(Cliente cliente) {

        if (cliente == null) {
            return false;
        }

        String sql = "INSERT INTO CLIENTE "
                + "(documento, primer_apellido, segundo_apellido, "
                + "primer_nombre, segundo_nombre, direccion, fecha_nacimiento) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, cliente.getDocumento());
            statement.setString(2, cliente.getPrimerApellido());
            statement.setString(3, cliente.getSegundoApellido());
            statement.setString(4, cliente.getPrimerNombre());
            statement.setString(5, cliente.getSegundoNombre());
            statement.setString(6, cliente.getDireccion());
            statement.setDate(7, convertirFecha(cliente.getFechaNacimiento()));

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Cliente> findByNombre(String nombre) {

        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT documento, primer_apellido, segundo_apellido, "
                + "primer_nombre, segundo_nombre, direccion, fecha_nacimiento "
                + "FROM CLIENTE WHERE primer_nombre LIKE ?";

        if (nombre == null || nombre.isBlank()) {
            return clientes;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, "%" + nombre.trim() + "%");

            try (ResultSet resultado = statement.executeQuery()) {

                while (resultado.next()) {
                    clientes.add(mapearCliente(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    @Override
    public Optional<Cliente> findByDocumento(String documento) {

        String sql = "SELECT documento, primer_apellido, segundo_apellido, "
                + "primer_nombre, segundo_nombre, direccion, fecha_nacimiento "
                + "FROM CLIENTE WHERE documento = ?";

        if (documento == null || documento.isBlank()) {
            return Optional.empty();
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documento);

            try (ResultSet resultado = statement.executeQuery()) {

                if (resultado.next()) {
                    return Optional.of(mapearCliente(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean updateCliente(String documento, Cliente cliente) {

        if (documento == null || cliente == null) {
            return false;
        }

        String sql = "UPDATE CLIENTE SET "
                + "primer_apellido = ?, "
                + "segundo_apellido = ?, "
                + "primer_nombre = ?, "
                + "segundo_nombre = ?, "
                + "direccion = ?, "
                + "fecha_nacimiento = ? "
                + "WHERE documento = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, cliente.getPrimerApellido());
            statement.setString(2, cliente.getSegundoApellido());
            statement.setString(3, cliente.getPrimerNombre());
            statement.setString(4, cliente.getSegundoNombre());
            statement.setString(5, cliente.getDireccion());
            statement.setDate(6, convertirFecha(cliente.getFechaNacimiento()));
            statement.setString(7, documento);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCliente(String documento) {

        String sql = "DELETE FROM CLIENTE WHERE documento = ?";

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

    private Cliente mapearCliente(ResultSet resultado)
            throws SQLException {

        Cliente cliente = new Cliente();

        cliente.setDocumento(resultado.getString("documento"));

        Date fecha = resultado.getDate("fecha_nacimiento");

        if (fecha != null) {
            cliente.setFechaNacimiento(fecha.toLocalDate());
        }

        cliente.setPrimerApellido(
                resultado.getString("primer_apellido"));

        cliente.setSegundoApellido(
                resultado.getString("segundo_apellido"));

        cliente.setPrimerNombre(
                resultado.getString("primer_nombre"));

        cliente.setSegundoNombre(
                resultado.getString("segundo_nombre"));

        cliente.setDireccion(
                resultado.getString("direccion"));

        return cliente;
    }

    private Date convertirFecha(LocalDate fecha) {
        return fecha == null ? null : Date.valueOf(fecha);
    }
}