package co.edu.unbosque.repository.implementations;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.model.entity.Socio;
import co.edu.unbosque.repository.interfaces.ISocioRepository;

public class SocioRepositoryJDBC implements ISocioRepository {

    @Override
    public boolean saveSocio(Socio socio) {
        String sql = "INSERT INTO SOCIO "
                + "(documento, fecha_afiliacion) "
                + "VALUES (?, ?)";

        if (socio == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, socio.getDocumento());
            statement.setDate(2, convertirFecha(socio.getFechaAfiliacion()));

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Socio> findByFechaAfiliacion(LocalDate fechaAfiliacion) {
        List<Socio> socios = new ArrayList<>();

        String sql = "SELECT documento, fecha_afiliacion "
                + "FROM SOCIO WHERE fecha_afiliacion = ?";

        if (fechaAfiliacion == null) {
            return socios;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setDate(1, convertirFecha(fechaAfiliacion));

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    socios.add(mapearSocio(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return socios;
    }

    @Override
    public Optional<Socio> findByDocumento(String documento) {
        String sql = "SELECT documento, fecha_afiliacion "
                + "FROM SOCIO WHERE documento = ?";

        if (documento == null || documento.isBlank()) {
            return Optional.empty();
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documento);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearSocio(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean updateSocio(String documento, Socio socio) {
        String sql = "UPDATE SOCIO SET fecha_afiliacion = ? WHERE documento = ?";

        if (documento == null || socio == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setDate(1, convertirFecha(socio.getFechaAfiliacion()));
            statement.setString(2, documento);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteSocio(String documento) {
        String sql = "DELETE FROM SOCIO WHERE documento = ?";

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

    private Socio mapearSocio(ResultSet resultado)
            throws SQLException {

        Socio socio = new Socio();

        socio.setDocumento(resultado.getString("documento"));

        Date fecha = resultado.getDate("fecha_afiliacion");
        if (fecha != null) {
            socio.setFechaAfiliacion(fecha.toLocalDate());
        }

        return socio;
    }

    private Date convertirFecha(LocalDate fecha) {
        return fecha == null ? null : Date.valueOf(fecha);
    }
}