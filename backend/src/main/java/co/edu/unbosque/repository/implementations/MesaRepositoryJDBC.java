
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
import co.edu.unbosque.model.entity.Mesa;
import co.edu.unbosque.repository.interfaces.IMesaRepository;

public class MesaRepositoryJDBC implements IMesaRepository {

    // REGISTRAR MESA
    @Override
    public boolean saveMesa(Mesa mesa) {

        if (mesa == null) {
            return false;
        }

        String sql = "INSERT INTO MESA "
                + "(id_mesa, capacidad, tematica, ubicacion) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, mesa.getIdMesa());
            statement.setInt(2, mesa.getCapacidad());
            statement.setString(3, mesa.getTematica());
            statement.setString(4, mesa.getUbicacion());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al registrar la mesa", e);
        }
    }

    // BUSCAR MESAS POR TEMATICA
    @Override
    public List<Mesa> findByTematica(String tematica) {

        List<Mesa> mesas = new ArrayList<>();

        if (tematica == null || tematica.isBlank()) {
            return mesas;
        }

        String sql = "SELECT id_mesa, capacidad, tematica, ubicacion "
                + "FROM MESA WHERE tematica LIKE ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, "%" + tematica.trim() + "%");

            try (ResultSet resultado = statement.executeQuery()) {

                while (resultado.next()) {
                    mesas.add(mapearMesa(resultado));
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al buscar mesas por temática", e);
        }

        return mesas;
    }

    // BUSCAR MESA POR IDENTIFICADOR
    @Override
    public Optional<Mesa> findByIdMesa(String idMesa) {

        if (idMesa == null || idMesa.isBlank()) {
            return Optional.empty();
        }

        String sql = "SELECT id_mesa, capacidad, tematica, ubicacion "
                + "FROM MESA WHERE id_mesa = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, idMesa);

            try (ResultSet resultado = statement.executeQuery()) {

                if (resultado.next()) {
                    return Optional.of(mapearMesa(resultado));
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al consultar la mesa por identificador", e);
        }

        return Optional.empty();
    }

    // ACTUALIZAR MESA
    @Override
    public boolean updateMesa(String idMesa, Mesa mesa) {

        if (idMesa == null || idMesa.isBlank() || mesa == null) {
            return false;
        }

        String sql = "UPDATE MESA SET capacidad = ?, "
                + "tematica = ?, ubicacion = ? WHERE id_mesa = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion()) {

            int filasAfectadas;

            try (PreparedStatement statement = conexion.prepareStatement(sql)) {

                statement.setInt(1, mesa.getCapacidad());
                statement.setString(2, mesa.getTematica());
                statement.setString(3, mesa.getUbicacion());
                statement.setString(4, idMesa);

                filasAfectadas = statement.executeUpdate();
            }

            // Si hubo modificaciones, la actualización fue exitosa.
            if (filasAfectadas > 0) {
                return true;
            }

            // Si no hubo cambios, comprobar si la mesa existe.
            return existeMesa(conexion, idMesa);

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al actualizar la mesa", e);
        }
    }

    // VERIFICAR SI EXISTE UNA MESA
    private boolean existeMesa(Connection conexion, String idMesa)
            throws SQLException {

        String sql = "SELECT 1 FROM MESA WHERE id_mesa = ?";

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, idMesa);

            try (ResultSet resultado = statement.executeQuery()) {
                return resultado.next();
            }
        }
    }

    // ELIMINAR MESA
    @Override
    public boolean deleteMesa(String idMesa) {

        if (idMesa == null || idMesa.isBlank()) {
            return false;
        }

        String sql = "DELETE FROM MESA WHERE id_mesa = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, idMesa);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al eliminar la mesa", e);
        }
    }

    // CONVERTIR RESULTSET A ENTIDAD MESA
    private Mesa mapearMesa(ResultSet resultado) throws SQLException {

        Mesa mesa = new Mesa();

        mesa.setIdMesa(resultado.getString("id_mesa"));
        mesa.setCapacidad(resultado.getInt("capacidad"));
        mesa.setTematica(resultado.getString("tematica"));
        mesa.setUbicacion(resultado.getString("ubicacion"));

        return mesa;
    }
}
