package co.edu.unbosque.repository.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.exceptions.PersistenceException;
import co.edu.unbosque.model.entity.ElementoJuego;
import co.edu.unbosque.repository.interfaces.IElementoJuegoRepository;

public class ElementoJuegoRepositoryJDBC implements IElementoJuegoRepository {

    @Override
    public boolean saveElementoJuego(ElementoJuego elementoJuego) {

        String sql = """
                INSERT INTO ELEMENTO_JUEGO (elemento_juego, num_serie)
                VALUES (?, ?)
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, elementoJuego.getElementoJuego());
            ps.setString(2, elementoJuego.getNumSerie());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        elementoJuego.setIdElementoJuego(rs.getInt(1));
                    }
                }
                return true;
            }

            return false;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar en la base de datos", e);
        }
    }

    @Override
    public Optional<ElementoJuego> findByIdElementoJuego(Integer idElementoJuego) {

        String sql = """
                SELECT *
                FROM ELEMENTO_JUEGO
                WHERE id_elemento_juego = ?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idElementoJuego);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    ElementoJuego elementoJuego = new ElementoJuego();

                    elementoJuego.setIdElementoJuego(rs.getInt("id_elemento_juego"));
                    elementoJuego.setElementoJuego(rs.getString("elemento_juego"));
                    elementoJuego.setNumSerie(rs.getString("num_serie"));

                    return Optional.of(elementoJuego);
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar en la base de datos", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean updateElementoJuego(Integer idElementoJuego, ElementoJuego elementoJuego) {

        String sql = """
                UPDATE ELEMENTO_JUEGO
                SET elemento_juego = ?,
                    num_serie = ?
                WHERE id_elemento_juego = ?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, elementoJuego.getElementoJuego());
            ps.setString(2, elementoJuego.getNumSerie());
            ps.setInt(3, idElementoJuego);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar en la base de datos", e);
        }
    }

    @Override
    public boolean deleteElementoJuego(Integer idElementoJuego) {

        String sql = """
                DELETE FROM ELEMENTO_JUEGO
                WHERE id_elemento_juego = ?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idElementoJuego);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error al eliminar en la base de datos", e);
        }
    }

    // LISTAR ELEMENTOS (TODOS O LOS DE UN JUEGO)
    @Override
    public List<ElementoJuego> findAll(String numSerie) {
        boolean filtrar = numSerie != null && !numSerie.isBlank();
        String sql = "SELECT id_elemento_juego, elemento_juego, num_serie FROM ELEMENTO_JUEGO"
                + (filtrar ? " WHERE num_serie = ?" : "")
                + " ORDER BY num_serie, id_elemento_juego";
        List<ElementoJuego> lista = new ArrayList<>();
        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            if (filtrar) {
                ps.setString(1, numSerie.trim());
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ElementoJuego elemento = new ElementoJuego();
                    elemento.setIdElementoJuego(rs.getInt("id_elemento_juego"));
                    elemento.setElementoJuego(rs.getString("elemento_juego"));
                    elemento.setNumSerie(rs.getString("num_serie"));
                    lista.add(elemento);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenceException("Error al listar elementos de juego", e);
        }
    }
}
