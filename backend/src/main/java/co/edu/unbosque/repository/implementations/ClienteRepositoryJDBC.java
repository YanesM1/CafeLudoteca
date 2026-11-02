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
        String sql = "INSERT INTO CLIENTE "
                + "(documento, fecha_nacimiento, primer_nombre, segundo_nombre, "
                + "primer_apellido, segundo_apellido, direccion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        if (cliente == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, cliente.getDocumento());
            statement.setDate(2, convertirFecha(cliente.getFechaNacimiento()));
            statement.setString(3, cliente.getPrimerNombre());
            statement.setString(4, cliente.getSegundoNombre());
            statement.setString(5, cliente.getPrimerApellido());
            statement.setString(6, cliente.getSegundoApellido());
            statement.setString(7, cliente.getDireccion());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Cliente> findByNombre(String nombre) {
        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT documento, fecha_nacimiento, primer_nombre, segundo_nombre, "
                + "primer_apellido, segundo_apellido, direccion "
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
        String sql = "SELECT documento, fecha_nacimiento, primer_nombre, segundo_nombre, "
                + "primer_apellido, segundo_apellido, direccion "
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
        String sql = "UPDATE CLIENTE SET fecha_nacimiento = ?, primer_nombre = ?, "
                + "segundo_nombre = ?, primer_apellido = ?, segundo_apellido = ?, "
                + "direccion = ? WHERE documento = ?";

        if (documento == null || cliente == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setDate(1, convertirFecha(cliente.getFechaNacimiento()));
            statement.setString(2, cliente.getPrimerNombre());
            statement.setString(3, cliente.getSegundoNombre());
            statement.setString(4, cliente.getPrimerApellido());
            statement.setString(5, cliente.getSegundoApellido());
            statement.setString(6, cliente.getDireccion());
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

        cliente.setPrimerNombre(resultado.getString("primer_nombre"));
        cliente.setSegundoNombre(resultado.getString("segundo_nombre"));
        cliente.setPrimerApellido(resultado.getString("primer_apellido"));
        cliente.setSegundoApellido(resultado.getString("segundo_apellido"));
        cliente.setDireccion(resultado.getString("direccion"));

        return cliente;
    }

    private Date convertirFecha(LocalDate fecha) {
        return fecha == null ? null : Date.valueOf(fecha);
    }
}