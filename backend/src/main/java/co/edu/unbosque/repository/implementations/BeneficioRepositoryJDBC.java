package co.edu.unbosque.repository.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.model.entity.Beneficio;
import co.edu.unbosque.repository.interfaces.IBeneficioRepository;

public class BeneficioRepositoryJDBC implements IBeneficioRepository {

    @Override
    public boolean saveBeneficio(Beneficio beneficio) {
        String sql = "INSERT INTO BENEFICIO "
                + "(id_membresia, id_beneficio, beneficio) "
                + "VALUES (?, ?, ?)";

        if (beneficio == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, beneficio.getIdMembresia());
            statement.setString(2, beneficio.getIdBeneficio());
            statement.setString(3, beneficio.getBeneficio());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Beneficio> findByIdMembresia(String idMembresia) {
        List<Beneficio> beneficios = new ArrayList<>();

        String sql = "SELECT id_membresia, id_beneficio, beneficio "
                + "FROM BENEFICIO WHERE id_membresia = ?";

        if (idMembresia == null || idMembresia.isBlank()) {
            return beneficios;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, idMembresia);

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    beneficios.add(mapearBeneficio(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return beneficios;
    }

    @Override
    public Optional<Beneficio> findByIdMembresiaAndIdBeneficio(String idMembresia,
            String idBeneficio) {
        String sql = "SELECT id_membresia, id_beneficio, beneficio "
                + "FROM BENEFICIO WHERE id_membresia = ? AND id_beneficio = ?";

        if (idMembresia == null || idMembresia.isBlank()
                || idBeneficio == null || idBeneficio.isBlank()) {
            return Optional.empty();
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, idMembresia);
            statement.setString(2, idBeneficio);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearBeneficio(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean updateBeneficio(String idMembresia, String idBeneficio,
            Beneficio beneficio) {
        String sql = "UPDATE BENEFICIO SET beneficio = ? "
                + "WHERE id_membresia = ? AND id_beneficio = ?";

        if (idMembresia == null || idBeneficio == null || beneficio == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, beneficio.getBeneficio());
            statement.setString(2, idMembresia);
            statement.setString(3, idBeneficio);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBeneficio(String idMembresia, String idBeneficio) {
        String sql = "DELETE FROM BENEFICIO WHERE id_membresia = ? AND id_beneficio = ?";

        if (idMembresia == null || idMembresia.isBlank()
                || idBeneficio == null || idBeneficio.isBlank()) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, idMembresia);
            statement.setString(2, idBeneficio);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Beneficio mapearBeneficio(ResultSet resultado)
            throws SQLException {

        Beneficio beneficio = new Beneficio();

        beneficio.setIdMembresia(resultado.getString("id_membresia"));
        beneficio.setIdBeneficio(resultado.getString("id_beneficio"));
        beneficio.setBeneficio(resultado.getString("beneficio"));

        return beneficio;
    }
}