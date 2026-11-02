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
import co.edu.unbosque.exceptions.PersistenceException;
import co.edu.unbosque.model.entity.Membresia;
import co.edu.unbosque.repository.interfaces.IMembresiaRepository;

public class MembresiaRepositoryJDBC implements IMembresiaRepository {

    @Override
    public boolean saveMembresia(Membresia membresia) {
        if (membresia == null) {
            return false;
        }

        String sql = "INSERT INTO MEMBRESIA "
                + "(id_membresia, plan, estado, fecha_inicio, fecha_fin, documento, tarifa) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, membresia.getIdMembresia());
            statement.setString(2, membresia.getPlan());
            statement.setString(3, membresia.getEstado());
            statement.setDate(4, convertirFecha(membresia.getFechaInicio()));
            statement.setDate(5, convertirFecha(membresia.getFechaFin()));
            statement.setString(6, membresia.getDocumento());
            statement.setDouble(7, membresia.getTarifa());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al guardar la membresía con id " + membresia.getIdMembresia(), e);
        }
    }

    @Override
    public List<Membresia> findByEstado(String estado) {
        List<Membresia> membresias = new ArrayList<>();

        if (estado == null || estado.isBlank()) {
            return membresias;
        }

        String sql = "SELECT id_membresia, plan, estado, fecha_inicio, fecha_fin, "
                + "documento, tarifa FROM MEMBRESIA WHERE estado = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, estado.trim());

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    membresias.add(mapearMembresia(resultado));
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al buscar membresías con estado " + estado, e);
        }

        return membresias;
    }

    @Override
    public Optional<Membresia> findByIdMembresia(int idMembresia) {
        if (idMembresia <= 0) {
            return Optional.empty();
        }

        String sql = "SELECT id_membresia, plan, estado, fecha_inicio, fecha_fin, "
                + "documento, tarifa FROM MEMBRESIA WHERE id_membresia = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, idMembresia);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearMembresia(resultado));
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al buscar la membresía con id " + idMembresia, e);
        }

        return Optional.empty();
    }

    @Override
    public boolean updateMembresia(int idMembresia, Membresia membresia) {
        if (idMembresia <= 0 || membresia == null) {
            return false;
        }

        String sql = "UPDATE MEMBRESIA SET plan = ?, estado = ?, fecha_inicio = ?, "
                + "fecha_fin = ?, documento = ?, tarifa = ? WHERE id_membresia = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, membresia.getPlan());
            statement.setString(2, membresia.getEstado());
            statement.setDate(3, convertirFecha(membresia.getFechaInicio()));
            statement.setDate(4, convertirFecha(membresia.getFechaFin()));
            statement.setString(5, membresia.getDocumento());
            statement.setDouble(6, membresia.getTarifa());
            statement.setInt(7, idMembresia);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al actualizar la membresía con id " + idMembresia, e);
        }
    }

    @Override
    public boolean deleteMembresia(int idMembresia) {
        if (idMembresia <= 0) {
            return false;
        }

        String sql = "DELETE FROM MEMBRESIA WHERE id_membresia = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, idMembresia);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al eliminar la membresía con id " + idMembresia, e);
        }
    }

    private Membresia mapearMembresia(ResultSet resultado) throws SQLException {
        Membresia membresia = new Membresia();

        membresia.setIdMembresia(resultado.getInt("id_membresia"));
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

    private static Date convertirFecha(LocalDate fecha) {
        return fecha == null ? null : Date.valueOf(fecha);
    }
@Override
    public List<Membresia> findByDocumento(String documento) {

        List<Membresia> membresias = new ArrayList<>();

        if (documento == null || documento.isBlank()) {
            return membresias;
        }

        String sql = "SELECT id_membresia, plan, estado, fecha_inicio, fecha_fin, "
                + "documento, tarifa FROM MEMBRESIA WHERE documento = ?";

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, documento.trim());

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    membresias.add(mapearMembresia(resultado));
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error al buscar membresías del documento " + documento, e);
        }

        return membresias;
    }


}


    

