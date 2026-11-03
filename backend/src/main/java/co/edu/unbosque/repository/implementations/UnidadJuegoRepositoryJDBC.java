package co.edu.unbosque.repository.implementations;

import co.edu.unbosque.model.dto.DisponibilidadUnidadJuegoDTO;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.exceptions.PersistenceException;
import co.edu.unbosque.model.entity.UnidadJuego;
import co.edu.unbosque.repository.interfaces.IUnidadJuegoRepository;

public class UnidadJuegoRepositoryJDBC implements IUnidadJuegoRepository {

@Override
    public boolean saveUnidadJuego(UnidadJuego unidadJuego) {
        String sql = """
                INSERT INTO UNIDAD_JUEGO (codigo, estado, num_serie, observaciones)
                VALUES(?,?,?,?)""";

        try (Connection conection = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conection.prepareStatement(sql)) {

            ps.setString(1, unidadJuego.getCodigo());
            ps.setString(2, unidadJuego.getEstado());
            ps.setString(3, unidadJuego.getNumSerie());
            ps.setString(4, unidadJuego.getObservaciones());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new PersistenceException("Error al conectar la base de datos", e);
        }
    }


    @Override
    public Optional<UnidadJuego> findByCodigo(String codigo, String numSerie) {
        String sql = """
                SELECT*
                FROM UNIDAD_JUEGO
                WHERE codigo=?
                AND num_serie=?
                """;
        try (Connection conexion = ConnectionBD.obtenerConexion();
                PreparedStatement ps = conexion.prepareStatement(sql);) {
            ps.setString(1, codigo);
             ps.setString(2, numSerie);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
            UnidadJuego unidadJuego= new UnidadJuego();
                unidadJuego.setCodigo(rs.getString("codigo"));
                unidadJuego.setEstado(rs.getString("estado"));
                unidadJuego.setObservaciones(rs.getString("observaciones"));
                unidadJuego.setNumSerie(rs.getString("num_serie"));

                return Optional.of(unidadJuego);

            }

        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar la unidad en la base de datos", e);
        }
         return Optional.empty();
     
    }

    

    @Override
    public boolean updateUnidadJuego(String codigo, String numSerie, UnidadJuego unidadJuego) {
        String sql="""
            UPDATE UNIDAD_JUEGO
              SET estado=?,
              observaciones=?
            WHERE codigo=?
            AND num_serie=?
              """;

              try(Connection conexion=ConnectionBD.obtenerConexion();
            PreparedStatement ps=conexion.prepareStatement(sql)) {
                ps.setString(1, unidadJuego.getEstado());
                ps.setString(2, unidadJuego.getObservaciones());
                ps.setString(3, codigo);
                ps.setString(4, numSerie);
               

                return ps.executeUpdate()>0;
                
              } catch (SQLException e) {
                throw new PersistenceException("Error al actualizar la unidad en la base de datos", e);
              
            }
     
    }

    @Override
    public boolean deleteUnidadJuego(String codigo, String numSerie) {
         String sql="""
                DELETE FROM UNIDAD_JUEGO
                WHERE codigo=?
                AND num_serie=?
                """;

                try (Connection conexion=ConnectionBD.obtenerConexion();
            PreparedStatement ps=conexion.prepareStatement(sql)){

                ps.setString(1, codigo);
                ps.setString(2, numSerie);
                return ps.executeUpdate()>0;
                    
                } catch (SQLException e) {
                    throw new PersistenceException("Error al borrar la unidad en la base de datos", e);
                }
    
    }


    @Override
    public List<DisponibilidadUnidadJuegoDTO> consultarDisponibilidad() {

        String sql = """
            SELECT j.num_serie,
                   COUNT(
                       CASE
                           WHEN UPPER(TRIM(u.estado)) = 'DISPONIBLE'
                           THEN 1
                       END
                   ) AS unidades_disponibles
            FROM JUEGO_MESA j
            LEFT JOIN UNIDAD_JUEGO u
                ON u.num_serie = j.num_serie
            GROUP BY j.num_serie
            ORDER BY j.num_serie
            """;

        List<DisponibilidadUnidadJuegoDTO> resultado = new ArrayList<>();

        try (Connection conexion = ConnectionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultado.add(new DisponibilidadUnidadJuegoDTO(
                    rs.getString("num_serie"),
                    rs.getInt("unidades_disponibles")
                ));
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                "Error al consultar disponibilidad de juegos", e
            );
        }

        return resultado;
    }


    @Override
    public List<UnidadJuego> findDisponibles() {
        List<UnidadJuego> lista = new ArrayList<>();
        String sql = "SELECT codigo,estado,observaciones,num_serie FROM UNIDAD_JUEGO WHERE UPPER(TRIM(estado))='DISPONIBLE' ORDER BY num_serie,codigo";
        try (Connection c=ConnectionBD.obtenerConexion();
             PreparedStatement ps=c.prepareStatement(sql);
             ResultSet rs=ps.executeQuery()) {
            while(rs.next()) {
                UnidadJuego u=new UnidadJuego();
                u.setCodigo(rs.getString("codigo"));
                u.setEstado(rs.getString("estado"));
                u.setObservaciones(rs.getString("observaciones"));
                u.setNumSerie(rs.getString("num_serie"));
                lista.add(u);
            }
            return lista;
        } catch(SQLException e) {
            throw new PersistenceException("Error al listar unidades disponibles",e);
        }
    }

}
