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

    @Override
    public boolean saveBeneficio(Beneficio beneficio) {

        if (beneficio == null) {
            return false;
        }

        String sql = """
                INSERT INTO BENEFICIO
                (
                    beneficio,
                    id_beneficio,
                    id_membresia
                )
                VALUES
                (
                    ?, ?, ?
                )
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, beneficio.getBeneficio());
            ps.setInt(2, beneficio.getIdBeneficio());
            ps.setInt(3, beneficio.getIdMembresia());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al guardar el beneficio con id "
                            + beneficio.getIdBeneficio(),
                    e);
        }
    }

    @Override
    public List<Beneficio> findAll() {

        List<Beneficio> beneficios = new ArrayList<>();

        String sql = """
                SELECT id_beneficio,
                       beneficio,
                       id_membresia
                FROM BENEFICIO
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                beneficios.add(mapearBeneficio(rs));
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al listar los beneficios",
                    e);
        }

        return beneficios;
    }

    @Override
    public Optional<Beneficio> findByIdBeneficio(
            Integer idBeneficio,
            Integer idMembresia) {

        if (idBeneficio == null || idBeneficio <= 0
                || idMembresia == null || idMembresia <= 0) {
            return Optional.empty();
        }

        String sql = """
                SELECT id_beneficio,
                       beneficio,
                       id_membresia
                FROM BENEFICIO
                WHERE id_beneficio = ?
                AND id_membresia = ?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idBeneficio);
            ps.setInt(2, idMembresia);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapearBeneficio(rs));
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al buscar el beneficio con id "
                            + idBeneficio,
                    e);
        }

        return Optional.empty();
    }

    @Override
    public boolean updateBeneficio(
            Integer idBeneficio,
            Integer idMembresia,
            Beneficio beneficio) {

        if (idBeneficio == null || idBeneficio <= 0
                || idMembresia == null || idMembresia <= 0
                || beneficio == null) {
            return false;
        }

        String sql = """
                UPDATE BENEFICIO
                SET beneficio = ?
                WHERE id_beneficio = ?
                AND id_membresia = ?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, beneficio.getBeneficio());
            ps.setInt(2, idBeneficio);
            ps.setInt(3, idMembresia);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al actualizar el beneficio con id "
                            + idBeneficio,
                    e);
        }
    }

    @Override
    public boolean deleteBeneficio(
            Integer idBeneficio,
            Integer idMembresia) {

        if (idBeneficio == null || idBeneficio <= 0
                || idMembresia == null || idMembresia <= 0) {
            return false;
        }

        String sql = """
                DELETE FROM BENEFICIO
                WHERE id_beneficio = ?
                AND id_membresia = ?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idBeneficio);
            ps.setInt(2, idMembresia);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al eliminar el beneficio con id "
                            + idBeneficio,
                    e);
        }
    }

    private Beneficio mapearBeneficio(ResultSet rs)
            throws SQLException {

        Beneficio beneficio = new Beneficio();

        beneficio.setIdBeneficio(
                rs.getInt("id_beneficio"));

        beneficio.setBeneficio(
                rs.getString("beneficio"));

        beneficio.setIdMembresia(
                rs.getInt("id_membresia"));

        return beneficio;
    }
}