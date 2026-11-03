package co.edu.unbosque.repository.implementations;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.exceptions.PersistenceException;
import co.edu.unbosque.model.entity.Membresia;
import co.edu.unbosque.repository.interfaces.IMembresiaRepository;

public class MembresiaRepositoryJDBC implements IMembresiaRepository {

    private static final String COLUMNS =
        "id_membresia, plan, estado, fecha_inicio, fecha_fin, SOCIO_documento, tarifa";

    @Override
    public boolean saveMembresia(Membresia m) {
        String sql = "INSERT INTO MEMBRESIA (" + COLUMNS + ") VALUES (?,?,?,?,?,?,?)";
        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, m.getIdMembresia());
            ps.setString(2, m.getPlan());
            ps.setString(3, m.getEstado());
            ps.setDate(4, java.sql.Date.valueOf(m.getFechaInicio()));
            if (m.getFechaFin() == null) ps.setNull(5, Types.DATE);
            else ps.setDate(5, java.sql.Date.valueOf(m.getFechaFin()));
            ps.setString(6, m.getDocumento());
            ps.setBigDecimal(7, java.math.BigDecimal.valueOf(m.getTarifa()));
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new PersistenceException("Error al registrar membresía", e);
        }
    }

    @Override
    public List<Membresia> findAll() {
        return consultar("SELECT " + COLUMNS + " FROM MEMBRESIA", null);
    }

    @Override
    public List<Membresia> findByEstado(String estado) {
        return consultar("SELECT " + COLUMNS + " FROM MEMBRESIA WHERE estado = ?", estado);
    }

    @Override
    public List<Membresia> findByDocumento(String documento) {
        return consultar("SELECT " + COLUMNS + " FROM MEMBRESIA WHERE SOCIO_documento = ?", documento);
    }

    private List<Membresia> consultar(String sql, String parametro) {
        List<Membresia> lista = new ArrayList<>();
        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (parametro != null) ps.setString(1, parametro.trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenceException("Error al consultar membresías", e);
        }
    }

    @Override
    public Optional<Membresia> findByIdMembresia(int id, String documento) {
        String sql = "SELECT " + COLUMNS +
            " FROM MEMBRESIA WHERE id_membresia = ? AND SOCIO_documento = ?";
        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, documento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar membresía", e);
        }
    }

    @Override
    public boolean updateMembresia(int id, String documento, Membresia m) {
        String sql = "UPDATE MEMBRESIA SET plan=?, estado=?, fecha_inicio=?, " +
            "fecha_fin=?, tarifa=? WHERE id_membresia=? AND SOCIO_documento=?";
        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getPlan());
            ps.setString(2, m.getEstado());
            ps.setDate(3, java.sql.Date.valueOf(m.getFechaInicio()));
            if (m.getFechaFin() == null) ps.setNull(4, Types.DATE);
            else ps.setDate(4, java.sql.Date.valueOf(m.getFechaFin()));
            ps.setBigDecimal(5, java.math.BigDecimal.valueOf(m.getTarifa()));
            ps.setInt(6, id);
            ps.setString(7, documento);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar membresía", e);
        }
    }

    @Override
    public boolean deleteMembresia(int id, String documento) {
        String sql = "DELETE FROM MEMBRESIA WHERE id_membresia=? AND SOCIO_documento=?";
        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, documento);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new PersistenceException("Error al eliminar membresía", e);
        }
    }

    private Membresia mapear(ResultSet rs) throws SQLException {
        Membresia m = new Membresia();
        m.setIdMembresia(rs.getInt("id_membresia"));
        m.setPlan(rs.getString("plan"));
        m.setEstado(rs.getString("estado"));
        java.sql.Date inicio = rs.getDate("fecha_inicio");
        java.sql.Date fin = rs.getDate("fecha_fin");
        m.setFechaInicio(inicio == null ? null : inicio.toLocalDate());
        m.setFechaFin(fin == null ? null : fin.toLocalDate());
        m.setDocumento(rs.getString("SOCIO_documento"));
        m.setTarifa(rs.getDouble("tarifa"));
        return m;
    }
}
