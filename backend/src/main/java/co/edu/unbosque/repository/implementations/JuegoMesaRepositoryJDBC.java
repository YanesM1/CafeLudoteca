
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
import co.edu.unbosque.model.entity.JuegoMesa;
import co.edu.unbosque.repository.interfaces.IJuegoMesaRepository;

public class JuegoMesaRepositoryJDBC implements IJuegoMesaRepository {

    @Override
    public boolean saveJuegoMesa(JuegoMesa juegoMesa) {
        String sql = """
                INSERT INTO JUEGO_MESA (num_serie, reglamento, cantidad_minima_jugadores,
                cantidad_maxima_jugadores, edad_minima, nombre, duracion_partida)
                VALUES(?, ?, ?, ?, ?, ?, ?)""";

        try (Connection conection = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conection.prepareStatement(sql)) {
            ps.setString(1, juegoMesa.getNumSerie());
            ps.setString(2, juegoMesa.getReglamento());
            ps.setInt(3, juegoMesa.getCantidadMinimaJugadores());
            ps.setInt(4, juegoMesa.getCantidadMaximaJugadores());
            ps.setInt(5, juegoMesa.getEdadMinima());
            ps.setString(6, juegoMesa.getNombre());
            ps.setInt(7, juegoMesa.getDuracionPartida());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar en la base de datos", e);
        }
    }

    @Override
    public List<JuegoMesa> findByNombre(String nombre) {
        List<JuegoMesa> juegos = new ArrayList<>();

        String sql = """
                SELECT *
                FROM JUEGO_MESA
                """ + (nombre == null || nombre.isBlank()
                        ? ""
                        : " WHERE LOWER(nombre) LIKE LOWER(?)");

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            if (nombre != null && !nombre.isBlank()) {
                ps.setString(1, "%" + nombre.trim() + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    JuegoMesa juegoMesa = new JuegoMesa();

                    juegoMesa.setNumSerie(rs.getString("num_serie"));
                    juegoMesa.setReglamento(rs.getString("reglamento"));
                    juegoMesa.setCantidadMinimaJugadores(rs.getInt("cantidad_minima_jugadores"));
                    juegoMesa.setCantidadMaximaJugadores(rs.getInt("cantidad_maxima_jugadores"));
                    juegoMesa.setEdadMinima(rs.getInt("edad_minima"));
                    juegoMesa.setNombre(rs.getString("nombre"));
                    juegoMesa.setDuracionPartida(rs.getInt("duracion_partida"));

                    juegos.add(juegoMesa);
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar en la base de datos", e);
        }

        return juegos;
    }

    @Override
    public Optional<JuegoMesa> findByNumSerie(String numSerie) {
        String sql = """
                SELECT *
                FROM JUEGO_MESA
                WHERE num_serie=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, numSerie);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    JuegoMesa juegoMesa = new JuegoMesa();

                    juegoMesa.setNumSerie(rs.getString("num_serie"));
                    juegoMesa.setReglamento(rs.getString("reglamento"));
                    juegoMesa.setCantidadMinimaJugadores(rs.getInt("cantidad_minima_jugadores"));
                    juegoMesa.setCantidadMaximaJugadores(rs.getInt("cantidad_maxima_jugadores"));
                    juegoMesa.setEdadMinima(rs.getInt("edad_minima"));
                    juegoMesa.setNombre(rs.getString("nombre"));
                    juegoMesa.setDuracionPartida(rs.getInt("duracion_partida"));

                    return Optional.of(juegoMesa);
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar en la base de datos", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean updateJuegoMesa(String numSerie, JuegoMesa juegoMesa) {
        String sql = """
                UPDATE JUEGO_MESA
                SET reglamento=?,
                cantidad_minima_jugadores=?,
                cantidad_maxima_jugadores=?,
                edad_minima=?,
                nombre=?,
                duracion_partida=?
                WHERE num_serie=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, juegoMesa.getReglamento());
            ps.setInt(2, juegoMesa.getCantidadMinimaJugadores());
            ps.setInt(3, juegoMesa.getCantidadMaximaJugadores());
            ps.setInt(4, juegoMesa.getEdadMinima());
            ps.setString(5, juegoMesa.getNombre());
            ps.setInt(6, juegoMesa.getDuracionPartida());
            ps.setString(7, numSerie);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error no se encuentra en la base de datos", e);
        }
    }

    @Override
    public boolean deleteJuegoMesa(String numSerie) {
        String sql = """
                DELETE FROM JUEGO_MESA
                WHERE num_serie=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, numSerie);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error no se encontro en la base de datos", e);
        }
    }
}
