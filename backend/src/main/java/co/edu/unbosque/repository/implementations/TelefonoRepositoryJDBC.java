package co.edu.unbosque.repository.implementations;

import co.edu.unbosque.exceptions.PersistenceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.model.entity.Telefono;
import co.edu.unbosque.repository.interfaces.ITelefonoRepository;

public class TelefonoRepositoryJDBC implements ITelefonoRepository {

    @Override
    public boolean saveTelefono(Telefono telefono) {
        String sql = "INSERT INTO TELEFONO "
                + "(documento_cliente, telefono) "
                + "VALUES (?, ?)";

        if (telefono == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, telefono.getDocumento());
            statement.setString(2, telefono.getTelefono());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException("Error al acceder a la base de datos", e);
        }
    }

    @Override
    public List<Telefono> findByDocumento(String documento) {
        List<Telefono> telefonos = new ArrayList<>();

        // Sin documento se listan todos los teléfonos
        boolean todos = documento == null || documento.isBlank();
        String sql = "SELECT documento_cliente, telefono FROM TELEFONO"
                + (todos ? " ORDER BY documento_cliente, telefono"
                         : " WHERE documento_cliente = ?");

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            if (!todos) {
                statement.setString(1, documento.trim());
            }

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    telefonos.add(mapearTelefono(resultado));
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al acceder a la base de datos", e);
        }

        return telefonos;
    }

    @Override
    public Optional<Telefono> findByDocumentoAndNumero(String documento, String numero) {
        String sql = "SELECT documento_cliente, telefono "
                + "FROM TELEFONO WHERE documento_cliente = ? AND telefono = ?";

        if (documento == null || documento.isBlank()
                || numero == null || numero.isBlank()) {
            return Optional.empty();
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documento);
            statement.setString(2, numero);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearTelefono(resultado));
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al acceder a la base de datos", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean updateTelefono(String documento, String numeroActual, Telefono telefono) {
        String sql = "UPDATE TELEFONO SET telefono = ? "
                + "WHERE documento_cliente = ? AND telefono = ?";

        if (documento == null || numeroActual == null || telefono == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, telefono.getTelefono());
            statement.setString(2, documento);
            statement.setString(3, numeroActual);
          

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException("Error al acceder a la base de datos", e);
        }
    }

    @Override
    public boolean deleteTelefono(String documento, String numero) {
        String sql = "DELETE FROM TELEFONO WHERE documento_cliente = ? AND telefono = ?";

        if (documento == null || documento.isBlank()
                || numero == null || numero.isBlank()) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documento);
            statement.setString(2, numero);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException("Error al acceder a la base de datos", e);
        }
    }

    private Telefono mapearTelefono(ResultSet resultado)
            throws SQLException {

        Telefono telefono = new Telefono();

        telefono.setDocumento(resultado.getString("documento_cliente"));
        telefono.setTelefono(resultado.getString("telefono"));

        return telefono;
    }
}