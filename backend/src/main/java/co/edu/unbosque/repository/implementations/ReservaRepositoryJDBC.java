
package co.edu.unbosque.repository.implementations;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.model.entity.Reserva;
import co.edu.unbosque.repository.interfaces.IReservaRepository;

public class ReservaRepositoryJDBC implements IReservaRepository {

    @Override
    public boolean saveReserva(Reserva reserva) {
        String sql = "INSERT INTO RESERVA "
                + "(id_reserva, fecha, horario, documento, id_mesa) "
                + "VALUES (?, ?, ?, ?, ?)";

        if (reserva == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, reserva.getIdReserva());

            statement.setDate(
                    2,
                    reserva.getFecha() == null
                            ? null
                            : Date.valueOf(reserva.getFecha())
            );

            statement.setString(3, reserva.getHorario());
            statement.setString(4, reserva.getDocumento());
            statement.setString(5, reserva.getIdMesa());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Reserva> findByDocumento(String documento) {
        List<Reserva> reservas = new ArrayList<>();

        String sql = "SELECT id_reserva, fecha, horario, documento, id_mesa "
                + "FROM RESERVA WHERE documento = ?";

        if (documento == null || documento.isBlank()) {
            return reservas;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documento);

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    reservas.add(mapearReserva(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservas;
    }

    @Override
    public Optional<Reserva> findByIdReserva(String idReserva) {
        String sql = "SELECT id_reserva, fecha, horario, documento, id_mesa "
                + "FROM RESERVA WHERE id_reserva = ?";

        if (idReserva == null || idReserva.isBlank()) {
            return Optional.empty();
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, idReserva);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearReserva(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean updateReserva(String idReserva, Reserva reserva) {
        String sql = "UPDATE RESERVA SET fecha = ?, horario = ?, "
                + "documento = ?, id_mesa = ? WHERE id_reserva = ?";

        if (idReserva == null || idReserva.isBlank() || reserva == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setDate(
                    1,
                    reserva.getFecha() == null
                            ? null
                            : Date.valueOf(reserva.getFecha())
            );

            statement.setString(2, reserva.getHorario());
            statement.setString(3, reserva.getDocumento());
            statement.setString(4, reserva.getIdMesa());
            statement.setString(5, idReserva);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteReserva(String idReserva) {
        String sql = "DELETE FROM RESERVA WHERE id_reserva = ?";

        if (idReserva == null || idReserva.isBlank()) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, idReserva);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Reserva mapearReserva(ResultSet resultado)
            throws SQLException {

        Reserva reserva = new Reserva();

        reserva.setIdReserva(resultado.getString("id_reserva"));

        Date fecha = resultado.getDate("fecha");
        reserva.setFecha(fecha == null ? null : fecha.toLocalDate());

        reserva.setHorario(resultado.getString("horario"));
        reserva.setDocumento(resultado.getString("documento"));
        reserva.setIdMesa(resultado.getString("id_mesa"));

        return reserva;
    }
}
