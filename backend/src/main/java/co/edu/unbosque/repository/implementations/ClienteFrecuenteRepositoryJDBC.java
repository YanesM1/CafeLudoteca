package co.edu.unbosque.repository.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.model.entity.ClienteFrecuente;
import co.edu.unbosque.repository.interfaces.IClienteFrecuenteRepository;

public class ClienteFrecuenteRepositoryJDBC implements IClienteFrecuenteRepository {

    @Override
    public boolean saveClienteFrecuente(ClienteFrecuente clienteFrecuente) {
        String sql = "INSERT INTO CLIENTE_FRECUENTE "
                + "(documento, puntos_acumulados, nivel_fidelidad) "
                + "VALUES (?, ?, ?)";

        if (clienteFrecuente == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, clienteFrecuente.getDocumento());
            statement.setInt(2, clienteFrecuente.getPuntosAcumulados());
            statement.setString(3, clienteFrecuente.getNivelFidelidad());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ClienteFrecuente> findByNivelFidelidad(String nivelFidelidad) {
        List<ClienteFrecuente> clientesFrecuentes = new ArrayList<>();

        String sql = "SELECT documento, puntos_acumulados, nivel_fidelidad "
                + "FROM CLIENTE_FRECUENTE WHERE nivel_fidelidad = ?";

        if (nivelFidelidad == null || nivelFidelidad.isBlank()) {
            return clientesFrecuentes;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, nivelFidelidad.trim());

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    clientesFrecuentes.add(mapearClienteFrecuente(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientesFrecuentes;
    }

    @Override
    public Optional<ClienteFrecuente> findByDocumento(String documento) {
        String sql = "SELECT documento, puntos_acumulados, nivel_fidelidad "
                + "FROM CLIENTE_FRECUENTE WHERE documento = ?";

        if (documento == null || documento.isBlank()) {
            return Optional.empty();
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documento);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearClienteFrecuente(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean updateClienteFrecuente(String documento, ClienteFrecuente clienteFrecuente) {
        String sql = "UPDATE CLIENTE_FRECUENTE SET puntos_acumulados = ?, "
                + "nivel_fidelidad = ? WHERE documento = ?";

        if (documento == null || clienteFrecuente == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, clienteFrecuente.getPuntosAcumulados());
            statement.setString(2, clienteFrecuente.getNivelFidelidad());
            statement.setString(3, documento);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteClienteFrecuente(String documento) {
        String sql = "DELETE FROM CLIENTE_FRECUENTE WHERE documento = ?";

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

    private ClienteFrecuente mapearClienteFrecuente(ResultSet resultado)
            throws SQLException {

        ClienteFrecuente clienteFrecuente = new ClienteFrecuente();

        clienteFrecuente.setDocumento(resultado.getString("documento"));
        clienteFrecuente.setPuntosAcumulados(resultado.getInt("puntos_acumulados"));
        clienteFrecuente.setNivelFidelidad(resultado.getString("nivel_fidelidad"));

        return clienteFrecuente;
    }
}