package co.edu.unbosque.repository.implementations;

import java.sql.*;
import java.util.*;

import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.exceptions.PersistenceException;
import co.edu.unbosque.model.entity.Reserva;
import co.edu.unbosque.repository.interfaces.IReservaRepository;

public class ReservaRepositoryJDBC implements IReservaRepository {

    private static final String LOCK = "cafeludoteca_reserva_id";

    private boolean bloquear(Connection c) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT GET_LOCK(?,10)")) {
            ps.setString(1, LOCK);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) == 1;
            }
        }
    }

    private void liberar(Connection c) {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT RELEASE_LOCK(?)")) {
            ps.setString(1, LOCK);
            ps.executeQuery().close();
        } catch (SQLException ignored) {}
    }

    private int siguienteId(Connection c) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT COALESCE(MAX(id_reserva),0)+1 FROM RESERVA");
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) throw new SQLException("No se pudo generar ID.");
            int id = rs.getInt(1);
            if (id <= 0) throw new SQLException("ID de reserva fuera de rango.");
            return id;
        }
    }

    // ¿La mesa ya tiene una reserva en esa fecha y hora? (supuesto 5)
    private boolean mesaOcupada(Connection c, int idMesa, java.time.LocalDate fecha,
                                String horario, Integer excluirId) throws SQLException {
        String sql = "SELECT 1 FROM RESERVA WHERE id_mesa=? AND fecha=? "
                + "AND TIME(horario)=TIME(?)"
                + (excluirId != null ? " AND id_reserva<>?" : "")
                + " LIMIT 1";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idMesa);
            ps.setDate(2, java.sql.Date.valueOf(fecha));
            ps.setString(3, horario);
            if (excluirId != null) ps.setInt(4, excluirId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void validarMesaLibre(Connection c, Reserva r, Integer excluirId) throws SQLException {
        if (mesaOcupada(c, Integer.parseInt(r.getIdMesa()), r.getFecha(), r.getHorario(), excluirId)) {
            throw co.edu.unbosque.exceptions.BusinessException.conflict(
                    "La mesa ya está reservada en esa fecha y hora.");
        }
    }

    // Juegos preseleccionados de una reserva (RESERVA_JUEGO)
    private List<String> juegosDe(Connection c, int idReserva) throws SQLException {
        List<String> juegos = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT num_serie FROM RESERVA_JUEGO WHERE id_reserva=?")) {
            ps.setInt(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) juegos.add(rs.getString(1));
            }
        }
        return juegos;
    }

    private List<Reserva> conJuegos(Connection c, List<Reserva> reservas) throws SQLException {
        for (Reserva r : reservas) {
            r.setJuegos(juegosDe(c, Integer.parseInt(r.getIdReserva())));
        }
        return reservas;
    }

    private void guardarJuegos(
            Connection c, int id, String documento,
            List<String> juegos) throws SQLException {

        if (juegos == null || juegos.isEmpty())
            throw new SQLException("La reserva debe incluir juegos.");

        String sql = """
            INSERT INTO RESERVA_JUEGO
            (num_serie, id_reserva, documento_cliente)
            VALUES (?, ?, ?)
            """;

        Set<String> unicos = new LinkedHashSet<>(juegos);

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            for (String serie : unicos) {
                if (serie == null || serie.isBlank())
                    throw new SQLException("Número de serie inválido.");

                ps.setString(1, serie.trim());
                ps.setInt(2, id);
                ps.setString(3, documento);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void eliminarJuegos(
            Connection c, int id, String documento) throws SQLException {

        String sql = """
            DELETE FROM RESERVA_JUEGO
            WHERE id_reserva=?
            AND documento_cliente=?
            """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, documento);
            ps.executeUpdate();
        }
    }

    private String documentoActual(Connection c, int id)
            throws SQLException {

        try (PreparedStatement ps = c.prepareStatement(
                "SELECT documento_cliente FROM RESERVA " +
                "WHERE id_reserva=? LIMIT 1 FOR UPDATE")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    @Override
    public boolean saveReserva(Reserva r, List<String> juegos) {
        if (r == null || r.getDocumento() == null ||
            r.getFecha() == null || r.getHorario() == null ||
            r.getIdMesa() == null) return false;

        boolean lock = false;

        try (Connection c = ConnectionBD.obtenerConexion()) {
            try {
                lock = bloquear(c);
                if (!lock)
                    throw new SQLException("No se pudo bloquear la generación de IDs.");

                c.setAutoCommit(false);

                try {
                    validarMesaLibre(c, r, null);
                    int id = siguienteId(c);

                    String sql = """
                        INSERT INTO RESERVA
                        (id_reserva, fecha, horario, id_mesa,
                         documento_cliente)
                        VALUES (?, ?, ?, ?, ?)
                        """;

                    try (PreparedStatement ps = c.prepareStatement(sql)) {
                        ps.setInt(1, id);
                        ps.setDate(2, java.sql.Date.valueOf(r.getFecha()));
                        ps.setString(3, r.getHorario());
                        ps.setInt(4, Integer.parseInt(r.getIdMesa()));
                        ps.setString(5, r.getDocumento());
                        ps.executeUpdate();
                    }

                    guardarJuegos(c, id, r.getDocumento(), juegos);

                    c.commit();
                    r.setIdReserva(String.valueOf(id));
                    return true;

                } catch (SQLException | RuntimeException e) {
                    c.rollback();
                    throw e;
                } finally {
                    c.setAutoCommit(true);
                }

            } finally {
                if (lock) liberar(c);
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                "Error transaccional al registrar reserva.", e);
        }
    }

    @Override
    public List<Reserva> findByDocumento(String documento) {
        List<Reserva> lista = new ArrayList<>();

        String sql = """
            SELECT id_reserva, fecha, horario,
                   documento_cliente, id_mesa
            FROM RESERVA
            WHERE documento_cliente=?
            ORDER BY fecha DESC, id_reserva DESC
            """;

        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, documento);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
            return conJuegos(c, lista);

        } catch (SQLException e) {
            throw new PersistenceException("Error al consultar reservas.", e);
        }
    }

    // LISTAR TODAS LAS RESERVAS (opcionalmente de una fecha)
    @Override
    public List<Reserva> findAll(java.time.LocalDate fecha) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT id_reserva, fecha, horario, documento_cliente, id_mesa "
                + "FROM RESERVA"
                + (fecha != null ? " WHERE fecha=?" : "")
                + " ORDER BY fecha DESC, horario, id_reserva DESC";
        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (fecha != null) ps.setDate(1, java.sql.Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
            return conJuegos(c, lista);
        } catch (SQLException e) {
            throw new PersistenceException("Error al listar reservas.", e);
        }
    }

    @Override
    public Optional<Reserva> findByIdReserva(String id) {
        try {
            int numero = Integer.parseInt(id);

            String sql = """
                SELECT id_reserva, fecha, horario,
                       documento_cliente, id_mesa
                FROM RESERVA
                WHERE id_reserva=?
                ORDER BY documento_cliente
                LIMIT 1
                """;

            try (Connection c = ConnectionBD.obtenerConexion();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, numero);

                Reserva reserva = null;
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) reserva = mapear(rs);
                }
                if (reserva == null) return Optional.empty();
                reserva.setJuegos(juegosDe(c, numero));
                return Optional.of(reserva);
            }
        } catch (NumberFormatException e) {
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Error al consultar reserva.", e);
        }
    }

    @Override
    public boolean updateReserva(
            String id, Reserva r, List<String> juegos) {

        try (Connection c = ConnectionBD.obtenerConexion()) {
            c.setAutoCommit(false);

            try {
                int numero = Integer.parseInt(id);
                String documento = documentoActual(c, numero);

                if (documento == null) {
                    c.rollback();
                    return false;
                }

                validarMesaLibre(c, r, numero);
                String sql = """
                    UPDATE RESERVA
                    SET fecha=?, horario=?, id_mesa=?
                    WHERE id_reserva=? AND documento_cliente=?
                    """;

                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setDate(1, java.sql.Date.valueOf(r.getFecha()));
                    ps.setString(2, r.getHorario());
                    ps.setInt(3, Integer.parseInt(r.getIdMesa()));
                    ps.setInt(4, numero);
                    ps.setString(5, documento);
                    ps.executeUpdate();
                }

                eliminarJuegos(c, numero, documento);
                guardarJuegos(c, numero, documento, juegos);

                c.commit();
                return true;

            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar reserva.", e);
        }
    }

    @Override
    public boolean deleteReserva(String id) {
        try (Connection c = ConnectionBD.obtenerConexion()) {
            c.setAutoCommit(false);

            try {
                int numero = Integer.parseInt(id);
                String documento = documentoActual(c, numero);

                if (documento == null) {
                    c.rollback();
                    return false;
                }

                eliminarJuegos(c, numero, documento);

                try (PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM RESERVA WHERE id_reserva=? " +
                    "AND documento_cliente=?")) {
                    ps.setInt(1, numero);
                    ps.setString(2, documento);

                    if (ps.executeUpdate() != 1)
                        throw new SQLException("No se pudo eliminar la reserva.");
                }

                c.commit();
                return true;

            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al eliminar reserva.", e);
        }
    }

    private Reserva mapear(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setIdReserva(String.valueOf(rs.getInt("id_reserva")));

        java.sql.Date fecha = rs.getDate("fecha");
        r.setFecha(fecha == null ? null : fecha.toLocalDate());

        r.setHorario(rs.getString("horario"));
        r.setDocumento(rs.getString("documento_cliente"));
        r.setIdMesa(String.valueOf(rs.getInt("id_mesa")));
        return r;
    }
}
