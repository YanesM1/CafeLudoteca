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
import co.edu.unbosque.model.entity.Membresia;
import co.edu.unbosque.repository.interfaces.IMembresiaRepository;

public class MembresiaRepositoryJDBC implements IMembresiaRepository {

    @Override
    public boolean saveMembresia(Membresia membresia) {
        String sql = "INSERT INTO MEMBRESIA "
                + "(id_membresia, plan, estado, fecha_inicio, fecha_fin, documento, tarifa) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        if (membresia == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, membresia.getIdMembresia());
            statement.setString(2, membresia.getPlan());
            statement.setString(3, membresia.getEstado());
            statement.setDate(4, convertirFecha(membresia.getFechaInicio()));
            statement.setDate(5, convertirFecha(membresia.getFechaFin()));
            statement.setString(6, membresia.getDocumento());
            statement.setDouble(7, membresia.getTarifa());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Membresia> findByDocumento(String documento) {
        List<Membresia> membresias = new ArrayList<>();

        String sql = "SELECT id_membresia, plan, estado, fecha_inicio, fecha_fin, "
                + "documento, tarifa FROM MEMBRESIA WHERE documento = ?";

        if (documento == null || documento.isBlank()) {
            return membresias;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documento);

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    membresias.add(mapearMembresia(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return membresias;
    }

    @Override
    public List<Membresia> findByEstado(String estado) {
        List<Membresia> membresias = new ArrayList<>();

        String sql = "SELECT id_membresia, plan, estado, fecha_inicio, fecha_fin, "
                + "documento, tarifa FROM MEMBRESIA WHERE estado = ?";

        if (estado == null || estado.isBlank()) {
            return membresias;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, estado.trim());

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    membresias.add(mapearMembresia(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return membresias;
    }

    @Override
    public Optional<Membresia> findByIdMembresia(String idMembresia) {
        String sql = "SELECT id_membresia, plan, estado, fecha_inicio, fecha_fin, "
                + "documento, tarifa FROM MEMBRESIA WHERE id_membresia = ?";

        if (idMembresia == null || idMembresia.isBlank()) {
            return Optional.empty();
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, idMembresia);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearMembresia(resultado));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean updateMembresia(String idMembresia, Membresia membresia) {
        String sql = "UPDATE MEMBRESIA SET plan = ?, estado = ?, fecha_inicio = ?, "
                + "fecha_fin = ?, documento = ?, tarifa = ? WHERE id_membresia = ?";

        if (idMembresia == null || membresia == null) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, membresia.getPlan());
            statement.setString(2, membresia.getEstado());
            statement.setDate(3, convertirFecha(membresia.getFechaInicio()));
            statement.setDate(4, convertirFecha(membresia.getFechaFin()));
            statement.setString(5, membresia.getDocumento());
            statement.setDouble(6, membresia.getTarifa());
            statement.setString(7, idMembresia);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteMembresia(String idMembresia) {
        String sql = "DELETE FROM MEMBRESIA WHERE id_membresia = ?";

        if (idMembresia == null || idMembresia.isBlank()) {
            return false;
        }

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, idMembresia);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Membresia mapearMembresia(ResultSet resultado)
            throws SQLException {

        Membresia membresia = new Membresia();

        membresia.setIdMembresia(resultado.getString("id_membresia"));
        membresia.setPlan(resultado.getString("plan"));
        membresia.setEstado(resultado.getString("estado"));

        Date inicio = resultado.getDate("fecha_inicio");
        if (inicio != null) {
            membresia.setFechaInicio(inicio.toLocalDate());
        }

        Date fin = resultado.getDate("fecha_fin");
        if (fin != null) {
            membresia.setFechaFin(fin.toLocalDate());
        }

        membresia.setDocumento(resultado.getString("documento"));
        membresia.setTarifa(resultado.getDouble("tarifa"));

        return membresia;
    }

    private Date convertirFecha(LocalDate fecha) {
        return fecha == null ? null : Date.valueOf(fecha);
    }
}