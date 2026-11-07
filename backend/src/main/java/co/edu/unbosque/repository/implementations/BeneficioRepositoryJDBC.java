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
import co.edu.unbosque.model.entity.Beneficio;
import co.edu.unbosque.repository.interfaces.IBeneficioRepository;

public class BeneficioRepositoryJDBC implements IBeneficioRepository {

    private static final String COLUMNAS =
        "id_beneficio, beneficio, MEMBRESIA_id_membresia AS id_membresia, " +
        "MEMBRESIA_documento AS documento_socio";

    @Override
    public boolean saveBeneficio(Beneficio b) {
        String sql = "INSERT INTO BENEFICIO (id_beneficio, beneficio, " +
            "MEMBRESIA_id_membresia, MEMBRESIA_documento) VALUES (?, ?, ?, ?)";

        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, b.getIdBeneficio());
            ps.setString(2, b.getBeneficio());
            ps.setInt(3, b.getIdMembresia());
            ps.setString(4, b.getDocumentoSocio());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar beneficio", e);
        }
    }

    @Override
    public int siguienteId() {
        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT COALESCE(MAX(id_beneficio), 0) + 1 FROM BENEFICIO");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new PersistenceException("Error al calcular el número de beneficio", e);
        }
    }

    @Override
    public List<Beneficio> findAll() {
        List<Beneficio> lista = new ArrayList<>();
        String sql = "SELECT " + COLUMNAS + " FROM BENEFICIO ORDER BY MEMBRESIA_id_membresia DESC, id_beneficio";

        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        } catch (SQLException e) {
            throw new PersistenceException("Error al listar beneficios", e);
        }
    }

    @Override
    public Optional<Beneficio> findByIdBeneficio(
            Integer id, Integer membresia, String documento) {
        String sql = "SELECT " + COLUMNAS + " FROM BENEFICIO " +
            "WHERE id_beneficio = ? AND MEMBRESIA_id_membresia = ? " +
            "AND MEMBRESIA_documento = ?";

        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, membresia);
            ps.setString(3, documento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar beneficio", e);
        }
    }

    @Override
    public boolean updateBeneficio(
            Integer id, Integer membresia, String documento, Beneficio b) {
        String sql = "UPDATE BENEFICIO SET beneficio = ? WHERE " +
            "id_beneficio = ? AND MEMBRESIA_id_membresia = ? " +
            "AND MEMBRESIA_documento = ?";

        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getBeneficio());
            ps.setInt(2, id);
            ps.setInt(3, membresia);
            ps.setString(4, documento);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar beneficio", e);
        }
    }

    @Override
    public boolean deleteBeneficio(Integer id, Integer membresia, String documento) {
        String sql = "DELETE FROM BENEFICIO WHERE id_beneficio = ? " +
            "AND MEMBRESIA_id_membresia = ? AND MEMBRESIA_documento = ?";

        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, membresia);
            ps.setString(3, documento);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new PersistenceException("Error al eliminar beneficio", e);
        }
    }

    private Beneficio mapear(ResultSet rs) throws SQLException {
        Beneficio b = new Beneficio();
        b.setIdBeneficio(rs.getInt("id_beneficio"));
        b.setBeneficio(rs.getString("beneficio"));
        b.setIdMembresia(rs.getInt("id_membresia"));
        b.setDocumentoSocio(rs.getString("documento_socio"));
        return b;
    }
}
