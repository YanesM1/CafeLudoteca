package co.edu.unbosque.repository.implementations;

import co.edu.unbosque.exceptions.PersistenceException;

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
                + "(documento_cliente, primer_apellido, segundo_apellido, "
                + "primer_nombre, segundo_nombre, direccion, fecha_nacimiento) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, cliente.getDocumentoCliente());
            statement.setString(2, cliente.getPrimerApellido());
            statement.setString(3, cliente.getSegundoApellido());
            statement.setString(4, cliente.getPrimerNombre());
            statement.setString(5, cliente.getSegundoNombre());
            statement.setString(6, cliente.getDireccion());
            statement.setDate(7, convertirFecha(cliente.getFechaNacimiento()));

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException("Error al acceder a la base de datos", e);
        }
    }

    @Override
    public List<Cliente> findByNombre(String nombre) {
        List<Cliente> clientes = new ArrayList<>();
        boolean filtrar = nombre != null && !nombre.isBlank();

        String sql = "SELECT documento_cliente, primer_apellido, segundo_apellido, "
                + "primer_nombre, segundo_nombre, direccion, fecha_nacimiento "
                + "FROM CLIENTE"
                + (filtrar ? " WHERE primer_nombre LIKE ?" : "");

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            if (filtrar) {
                statement.setString(1, "%" + nombre.trim() + "%");
            }

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    clientes.add(mapearCliente(resultado));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al consultar clientes", e);
        }

        return clientes;
    }

        @Override
    public Optional<Cliente> findByDocumento(String documento) {

        String sql = "SELECT documento_cliente, primer_apellido, segundo_apellido, "
                + "primer_nombre, segundo_nombre, direccion, fecha_nacimiento "
                + "FROM CLIENTE WHERE documento_cliente = ?";

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
            throw new PersistenceException("Error al acceder a la base de datos", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean updateCliente(String documentoCliente, Cliente cliente) {

        if (documentoCliente == null || cliente == null) {
            return false;
        }

        String sql = "UPDATE CLIENTE SET "
                + "primer_apellido = ?, "
                + "segundo_apellido = ?, "
                + "primer_nombre = ?, "
                + "segundo_nombre = ?, "
                + "direccion = ?, "
                + "fecha_nacimiento = ? "
                + "WHERE documento_cliente = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, cliente.getPrimerApellido());
            statement.setString(2, cliente.getSegundoApellido());
            statement.setString(3, cliente.getPrimerNombre());
            statement.setString(4, cliente.getSegundoNombre());
            statement.setString(5, cliente.getDireccion());
            statement.setDate(6, convertirFecha(cliente.getFechaNacimiento()));
            statement.setString(7, documentoCliente);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException("Error al acceder a la base de datos", e);
        }
    }

    @Override
    public boolean deleteCliente(String documentoCliente) {

        String sql = "DELETE FROM CLIENTE WHERE documento_cliente = ?";

        if (documentoCliente == null || documentoCliente.isBlank()) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documentoCliente);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException("Error al acceder a la base de datos", e);
        }
    }

    private Cliente mapearCliente(ResultSet resultado)
            throws SQLException {

        Cliente cliente = new Cliente();

        cliente.setDocumentoCliente(resultado.getString("documento_cliente"));

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