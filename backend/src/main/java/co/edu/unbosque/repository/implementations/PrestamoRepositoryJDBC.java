package co.edu.unbosque.repository.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.exceptions.PersistenceException;
import co.edu.unbosque.model.entity.Prestamo;
import co.edu.unbosque.repository.interfaces.IPrestamoRepository;

public class PrestamoRepositoryJDBC implements IPrestamoRepository {

    @Override
    public boolean savePrestamo(Prestamo prestamo) {
        String sql = """
                INSERT INTO PRESTAMO (id_prestamo, fecha_salida, estado_componentes,
                tipo_prestamo, fecha_devolusion, documento_empleado, documento_cliente)
                VALUES(?, ?, ?, ?, ?, ?, ?)""";

        try (Connection conection = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conection.prepareStatement(sql)) {
            ps.setInt(1, prestamo.getIdPrestamo());
            ps.setObject(2, prestamo.getFechaSalida());
            ps.setString(3, prestamo.getEstadoComponentes());
            ps.setString(4, prestamo.getTipoPrestamo());
            ps.setObject(5, prestamo.getFechaDevolucion());
            ps.setString(6, prestamo.getDocumentoEmpleado());
            ps.setString(7, prestamo.getDocumentoCliente());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar en la base de datos", e);
        }
    }

    @Override
    public List<Prestamo> findByTipoPrestamo(String tipoPrestamo) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = """
                SELECT *
                FROM PRESTAMO
                WHERE tipo_prestamo=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, tipoPrestamo);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Prestamo prestamo = new Prestamo();

                    prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                    prestamo.setFechaSalida(rs.getObject("fecha_salida", LocalDate.class));
                    prestamo.setEstadoComponentes(rs.getString("estado_componentes"));
                    prestamo.setTipoPrestamo(rs.getString("tipo_prestamo"));
                    prestamo.setFechaDevolucion(rs.getObject("fecha_devolucion", LocalDate.class));

                    prestamos.add(prestamo);
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error guardar el prestamo en la base de datos", e);
        }

        return prestamos;
    }

    @Override
    public Optional<Prestamo> findByIdPrestamo(Integer idPrestamo) {
        String sql = """
                SELECT *
                FROM PRESTAMO
                WHERE id_prestamo=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPrestamo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Prestamo prestamo = new Prestamo();

                    prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                    prestamo.setFechaSalida(rs.getObject("fecha_salida", LocalDate.class));
                    prestamo.setEstadoComponentes(rs.getString("estado_componentes"));
                    prestamo.setTipoPrestamo(rs.getString("tipo_prestamo"));
                    prestamo.setFechaDevolucion(rs.getObject("fecha_devolucion", LocalDate.class));
                    prestamo.setDocumentoEmpleado(rs.getString("documento_emp"));
                    prestamo.setDocumentoCliente(rs.getString("documento"));

                    return Optional.of(prestamo);
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar en la base de datos", e);
        }
          return Optional.empty();
    }

    @Override
    public boolean updatePrestamo(Integer idPrestamo, Prestamo prestamo) {
         String sql = """
                UPDATE PRESTAMO
                SET fecha_salida=?,
                estado_componentes=?,
                tipo_prestamo=?,
                fecha_devolucion=?,
                documento_emp=?,
                documento=?
                WHERE id_prestamo=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

         
            ps.setObject(1, prestamo.getFechaSalida());
            ps.setString(2, prestamo.getEstadoComponentes());
            ps.setString(3, prestamo.getTipoPrestamo());
            ps.setObject(4, prestamo.getFechaDevolucion());
            ps.setString(5, prestamo.getDocumentoEmpleado());
            ps.setString(6, prestamo.getDocumentoCliente());
            ps.setInt(7, idPrestamo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error no se puede actualizar en la base de datos", e);
        }
    }

    @Override
    public boolean deletePrestamo(Integer idPrestamo) {
        String sql = """
                DELETE FROM PRESTAMO
                WHERE id_prestamo=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPrestamo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error no se puede borrar en la base de datos", e);
        }
    }

}
