
package co.edu.unbosque.repository.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.model.entity.Mesa;
import co.edu.unbosque.repository.interfaces.IMesaRepository;

public class MesaRepositoryJDBC implements IMesaRepository {

    @Override
    public boolean saveMesa(Mesa mesa) {
        String sql = "INSERT INTO MESA "
                + "(id_mesa, capacidad, tematica, ubicacion) "
                + "VALUES (?, ?, ?, ?)";

        if (mesa == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, mesa.getIdMesa());
            statement.setInt(2, mesa.getCapacidad());
            statement.setString(3, mesa.getTematica());
            statement.setString(4, mesa.getUbicacion());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Mesa> findByTematica(String tematica) {
        List<Mesa> mesas = new ArrayList<>();

        String sql = "SELECT id_mesa, capacidad, tematica, ubicacion "
                + "FROM MESA WHERE tematica LIKE ?";

        if (tematica == null || tematica.isBlank()) {
            return mesas;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, "%" + tematica.trim() + "%");

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    mesas.add(mapearMesa(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mesas;
    }

    @Override
    public Optional<Mesa> findByIdMesa(String idMesa) {
        String sql = "SELECT id_mesa, capacidad, tematica, ubicacion "
                + "FROM MESA WHERE id_mesa = ?";

        if (idMesa == null || idMesa.isBlank()) {
            return Optional.empty();
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, idMesa);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearMesa(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean updateMesa(String idMesa, Mesa mesa) {
        String sql = "UPDATE MESA SET capacidad = ?, "
                + "tematica = ?, ubicacion = ? WHERE id_mesa = ?";

        if (idMesa == null || idMesa.isBlank() || mesa == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, mesa.getCapacidad());
            statement.setString(2, mesa.getTematica());
            statement.setString(3, mesa.getUbicacion());
            statement.setString(4, idMesa);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteMesa(String idMesa) {
        String sql = "DELETE FROM MESA WHERE id_mesa = ?";

        if (idMesa == null || idMesa.isBlank()) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, idMesa);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Mesa mapearMesa(ResultSet resultado) throws SQLException {
        Mesa mesa = new Mesa();

        mesa.setIdMesa(resultado.getString("id_mesa"));
        mesa.setCapacidad(resultado.getInt("capacidad"));
        mesa.setTematica(resultado.getString("tematica"));
        mesa.setUbicacion(resultado.getString("ubicacion"));

        return mesa;
    }
}
