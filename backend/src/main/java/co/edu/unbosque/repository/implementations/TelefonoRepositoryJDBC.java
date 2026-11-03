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
import co.edu.unbosque.model.entity.Telefono;
import co.edu.unbosque.repository.interfaces.ITelefonoRepository;

public class TelefonoRepositoryJDBC implements ITelefonoRepository {

    @Override
    public boolean saveTelefono(Telefono telefono) {
        String sql = """
                INSERT INTO TELEFONO (documento_cliente, telefono)
                VALUES(?, ?)""";

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, telefono.getDocumentoCliente());
            ps.setString(2, telefono.getTelefono());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar en la base de datos", e);
        }
    }

    @Override
    public List<Telefono> findByDocumento(String documentoCliente) {
        List<Telefono> telefonos = new ArrayList<>();

        String sql = """
                SELECT *
                FROM TELEFONO
                WHERE documento_cliente=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, documentoCliente);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Telefono telefono = new Telefono();

                    telefono.setDocumentoCliente(rs.getString("documento_cliente"));
                    telefono.setTelefono(rs.getString("telefono"));

                    telefonos.add(telefono);
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar en la base de datos", e);
        }

        return telefonos;
    }

    @Override
    public Optional<Telefono> findByDocumentoAndNumero(String documentoCliente, String telefono) {
        String sql = """
                SELECT *
                FROM TELEFONO
                WHERE documento_cliente=? AND telefono=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, documentoCliente);
            ps.setString(2, telefono);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Telefono telefonoC = new Telefono();

                    telefonoC.setDocumentoCliente(rs.getString("documento_cliente"));
                    telefonoC.setTelefono(rs.getString("telefono"));

                    return Optional.of(telefonoC);
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar en la base de datos", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean updateTelefono(String documentoCliente, String telefonoActual, Telefono telefono) {
        String sql = """
                UPDATE TELEFONO
                SET telefono=?
                WHERE documento_cliente=? AND telefono=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, telefono.getTelefono());
            ps.setString(2, documentoCliente);
            ps.setString(3, telefonoActual);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error no se encuentra en la base de datos", e);
        }
    }

    @Override
    public boolean deleteTelefono(String documentoCliente, String telefono) {
        String sql = """
                DELETE FROM TELEFONO
                WHERE documento_cliente=? AND telefono=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, documentoCliente);
            ps.setString(2, telefono);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error no se encontro en la base de datos", e);
        }
    }
}
