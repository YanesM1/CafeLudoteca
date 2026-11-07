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
import co.edu.unbosque.model.entity.Categoria;
import co.edu.unbosque.repository.interfaces.ICategoriaRepository;

public class CategoriaRepositoryJDBC implements  ICategoriaRepository{
    
    @Override
    public boolean saveCategoria(Categoria categoria) {
          String sql = """
                INSERT INTO CATEGORIA (num_categoria, categoria, num_serie)
                VALUES(?,?,?)""";

        try (Connection conection = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conection.prepareStatement(sql)) {

         
            ps.setInt(1, categoria.getNumCategoria());
            ps.setString(2, categoria.getCategoria());
            ps.setString(3, categoria.getNumSerie());
       

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error no puede guardar la categoria en la base de datos", e);
        }
    }

    @Override
    public List<Categoria> findByCategoria(String categoria) {
         List<Categoria> categorias = new ArrayList<>();

        // Búsqueda parcial sin distinguir mayúsculas; sin texto lista todas.
        boolean filtrar = categoria != null && !categoria.isBlank();
        String sql = "SELECT * FROM CATEGORIA"
                + (filtrar ? " WHERE LOWER(categoria) LIKE LOWER(?)" : "")
                + " ORDER BY num_serie, num_categoria";

        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql);) {

            if (filtrar) {
                ps.setString(1, "%" + categoria.trim() + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Categoria categoriaJ = new Categoria();

                categoriaJ.setNumCategoria(rs.getInt("num_categoria"));
                categoriaJ.setCategoria(rs.getString("categoria"));
                categoriaJ.setNumSerie(rs.getString("num_serie"));
                

                categorias.add(categoriaJ);
            }
            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar la categoría en la base de datos", e);
        }
        return categorias;
    }

@Override
public Optional<Categoria> findByNumCategoria(Integer numCategoria, String numSerie) {
      String sql = """
                SELECT*
                FROM CATEGORIA
                WHERE num_categoria=?
                AND num_serie=?
                """;
        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql);) {
            ps.setInt(1, numCategoria);
            ps.setString(2, numSerie);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Categoria categoria = new Categoria();
                 categoria.setCategoria(rs.getString("categoria"));
                categoria.setNumCategoria(numCategoria);
                categoria.setNumSerie(numSerie);
               

                return Optional.of(categoria);

            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar la categoria en la base de datos", e);
        }
         return Optional.empty();
}

@Override
public boolean updateCategoria(Integer numCategoria, String numSerie, Categoria categoria) {
     String sql="""
            UPDATE CATEGORIA
              SET categoria=?
            WHERE num_categoria=?
            AND num_serie=?
              """;

              try(Connection conexion=ConnectionBD.obtenerConexion();
            PreparedStatement ps=conexion.prepareStatement(sql)) {
                ps.setString(1, categoria.getCategoria());
                ps.setInt(2, numCategoria);
                ps.setString(3, numSerie);
     

                return ps.executeUpdate()>0;
                
              } catch (SQLException e) {
                throw new PersistenceException("Error no se encuentra la categoria en la base de datos", e);
              
            }
}


@Override
public boolean deleteCategoria(Integer numCategoria, String numSerie) {
     String sql="""
                DELETE FROM CATEGORIA
                WHERE num_categoria=?
                AND num_serie=?
                """;

                try (Connection conexion=ConnectionBD.obtenerConexion();
            PreparedStatement ps=conexion.prepareStatement(sql)){

                ps.setInt(1, numCategoria);
                ps.setString(2, numSerie);
                return ps.executeUpdate()>0;
                    
                } catch (SQLException e) {
                    throw new PersistenceException("Error no se pudo borrar la categoria en la base de datos", e);
                }
    }


    @Override
    public int siguienteNumero(String numSerie) {
        String sql = "SELECT COALESCE(MAX(num_categoria), 0) + 1 FROM CATEGORIA WHERE num_serie = ?";
        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, numSerie);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al calcular el número de categoría", e);
        }
    }
}
