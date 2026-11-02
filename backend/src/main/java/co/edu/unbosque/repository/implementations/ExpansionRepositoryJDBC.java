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
import co.edu.unbosque.model.entity.Expansion;
import co.edu.unbosque.repository.interfaces.IExpansionRepository;

public class ExpansionRepositoryJDBC implements IExpansionRepository {
    @Override
    public boolean saveExpansion(Expansion expansion) {
        String sql = """
                INSERT INTO EXPANSION (id_expansion, nombre, descripcion, contenido_adicional, num_serie)
                VALUES(?,?,?,?,?)""";

        try (Connection conection = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conection.prepareStatement(sql)) {

            ps.setInt(1, expansion.getIdExpansion());
            ps.setString(2, expansion.getNombre());
            ps.setString(3, expansion.getDescripcion());
            ps.setString(4, expansion.getContenidoAdicional());
            ps.setString(5, expansion.getNumSerie());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error al guardar en la base de datos", e);
        }
    }

    @Override
    public List<Expansion> findByNombre(String nombre) {
        List<Expansion> expansiones = new ArrayList<>();

        String sql = """
                SELECT *
                FROM EXPANSION
                WHERE nombre=?
                """;

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombre);

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

            ps.setInt(1, expansion.getIdExpansion());
            ps.setString(2, expansion.getNombre());
            ps.setString(3, expansion.getDescripcion());
            ps.setString(4, expansion.getContenidoAdicional());
            ps.setString(5, expansion.getNumSerie());

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
