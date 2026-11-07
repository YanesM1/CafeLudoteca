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
import co.edu.unbosque.model.entity.Expansion;
import co.edu.unbosque.repository.interfaces.IExpansionRepository;

public class ExpansionRepositoryJDBC implements IExpansionRepository {
   @Override
public boolean saveExpansion(Expansion expansion) {
    String sql = """
            INSERT INTO EXPANSION (nombre, descripcion, contenido_adicional, num_serie)
            VALUES(?,?,?,?)""";

    try (Connection conection = ConnectionBD.obtenerConexion();
            PreparedStatement ps = conection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, expansion.getNombre());
        ps.setString(2, expansion.getDescripcion());
        ps.setString(3, expansion.getContenidoAdicional());
        ps.setString(4, expansion.getNumSerie());

        int filas = ps.executeUpdate();
        if (filas != 1) return false;

        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next()) {
                expansion.setIdExpansion(keys.getInt(1));
                return true;
            }
        }
        return false;

    } catch (SQLException e) {
        throw new PersistenceException("Error al guardar en la base de datos", e);
    }
}


    @Override
    public List<Expansion> findByNombre(String nombre) {
        List<Expansion> expansiones = new ArrayList<>();

        // Búsqueda parcial sin distinguir mayúsculas; sin texto lista todas.
        boolean filtrar = nombre != null && !nombre.isBlank();
        String sql = "SELECT * FROM EXPANSION"
                + (filtrar ? " WHERE LOWER(nombre) LIKE LOWER(?)" : "")
                + " ORDER BY nombre";

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            if (filtrar) {
                ps.setString(1, "%" + nombre.trim() + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Expansion expansion = new Expansion();

                    expansion.setIdExpansion(rs.getInt("id_expansion"));
                    expansion.setNombre(rs.getString("nombre"));
                    expansion.setDescripcion(rs.getString("descripcion"));
                    expansion.setContenidoAdicional(rs.getString("contenido_adicional"));
                    expansion.setNumSerie(rs.getString("num_serie"));

                    expansiones.add(expansion);
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar la expansión en la base de datos", e);
        }

        return expansiones;
    }

    @Override
    public Optional<Expansion> findByIdExpansion(Integer idExpansion) {
        String sql = """
                SELECT *
                FROM EXPANSION
                WHERE id_expansion=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idExpansion);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Expansion expansion = new Expansion();

                    expansion.setIdExpansion(rs.getInt("id_expansion"));
                    expansion.setNombre(rs.getString("nombre"));
                    expansion.setDescripcion(rs.getString("descripcion"));
                    expansion.setContenidoAdicional(rs.getString("contenido_adicional"));
                    expansion.setNumSerie(rs.getString("num_serie"));

                    return Optional.of(expansion);
                }
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar la expansion en la base de datos", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean updateExpansion(Integer idExpansion, Expansion expansion) {
        String sql = """
                UPDATE EXPANSION
                SET nombre=?,
                descripcion=?,
                contenido_adicional=?,
                num_serie=?
                WHERE id_expansion=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, expansion.getNombre());
            ps.setString(2, expansion.getDescripcion());
            ps.setString(3, expansion.getContenidoAdicional());
            ps.setString(4, expansion.getNumSerie());
            ps.setInt(5, idExpansion);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error no se encuentra en la base de datos", e);
        }
    }

    @Override
    public boolean deleteExpansion(Integer idExpansion) {
        String sql = """
                DELETE FROM EXPANSION
                WHERE id_expansion=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idExpansion);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error no se encontro en la base de datos", e);
        }

    }
}
