package co.edu.unbosque.repository.implementations;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.exceptions.PersistenceException;
import co.edu.unbosque.model.entity.Prestamo;
import co.edu.unbosque.model.dto.DevolucionPrestamoRequestDTO;
import co.edu.unbosque.repository.interfaces.IPrestamoRepository;

public class PrestamoRepositoryJDBC implements IPrestamoRepository {

    private static final String SELECT =
        "SELECT p.id_prestamo,p.fecha_salida,p.estado_componentes," +
        "p.tipo_prestamo,p.fecha_devolucion,p.documento_cliente," +
        "p.documento_emp,pu.codigo,pu.num_serie " +
        "FROM PRESTAMO p LEFT JOIN PRESTAMO_UNIDAD_JUEGO pu " +
        "ON p.id_prestamo=pu.id_prestamo ";

    @Override
    public boolean savePrestamo(Prestamo p) {
        String insert = """
            INSERT INTO PRESTAMO
            (fecha_salida,estado_componentes,tipo_prestamo,
             fecha_devolucion,documento_cliente,documento_emp)
            VALUES(?,?,?,?,?,?)
            """;

        try (Connection c = ConnectionBD.obtenerConexion()) {
            c.setAutoCommit(false);
            try {
                String estado;
                try (PreparedStatement lock = c.prepareStatement(
                    "SELECT estado FROM UNIDAD_JUEGO WHERE codigo=? AND num_serie=? FOR UPDATE")) {
                    lock.setString(1,p.getCodigoUnidad());
                    lock.setString(2,p.getNumSerieUnidad());
                    try (ResultSet rs=lock.executeQuery()) {
                        if (!rs.next()) {
                            c.rollback();
                            return false;
                        }
                        estado=rs.getString("estado");
                    }
                }

                if (!"DISPONIBLE".equalsIgnoreCase(estado.trim())) {
                    c.rollback();
                    return false;
                }

                try (PreparedStatement ps=c.prepareStatement(
                        insert,Statement.RETURN_GENERATED_KEYS)) {
                    ps.setObject(1,p.getFechaSalida());
                    ps.setString(2,"PENDIENTE_DEVOLUCION");
                    ps.setString(3,p.getTipoPrestamo());
                    ps.setObject(4,p.getFechaDevolucion());
                    ps.setString(5,p.getDocumentoCliente());
                    ps.setString(6,p.getDocumentoEmp());

                    if (ps.executeUpdate()!=1) {
                        c.rollback();
                        return false;
                    }

                    try (ResultSet keys=ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("No se generó el ID.");
                        p.setIdPrestamo(keys.getInt(1));
                    }
                }

                try (PreparedStatement ps=c.prepareStatement(
                    "INSERT INTO PRESTAMO_UNIDAD_JUEGO(id_prestamo,codigo,num_serie) VALUES(?,?,?)")) {
                    ps.setInt(1,p.getIdPrestamo());
                    ps.setString(2,p.getCodigoUnidad());
                    ps.setString(3,p.getNumSerieUnidad());
                    ps.executeUpdate();
                }

                try (PreparedStatement ps=c.prepareStatement(
                    "UPDATE UNIDAD_JUEGO SET estado='PRESTADO' WHERE codigo=? AND num_serie=? AND UPPER(TRIM(estado))='DISPONIBLE'")) {
                    ps.setString(1,p.getCodigoUnidad());
                    ps.setString(2,p.getNumSerieUnidad());
                    if (ps.executeUpdate()!=1) throw new SQLException("La unidad dejó de estar disponible.");
                }

                c.commit();
                return true;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error transaccional al registrar préstamo",e);
        }
    }

    private List<Prestamo> consultar(String condicion, String parametro) {
        List<Prestamo> lista=new ArrayList<>();
        String sql=SELECT+condicion+" ORDER BY p.id_prestamo DESC";
        try (Connection c=ConnectionBD.obtenerConexion();
             PreparedStatement ps=c.prepareStatement(sql)) {
            if (parametro!=null) ps.setString(1,parametro);
            try (ResultSet rs=ps.executeQuery()) {
                while(rs.next()) lista.add(mapear(rs));
            }
            return lista;
        } catch(SQLException e) {
            throw new PersistenceException("Error al consultar préstamos",e);
        }
    }

    @Override
    public List<Prestamo> findAll() {
        return consultar("",null);
    }

    @Override
    public List<Prestamo> findActivos() {
        return consultar(
            "JOIN UNIDAD_JUEGO u ON u.codigo=pu.codigo AND u.num_serie=pu.num_serie " +
            "WHERE UPPER(TRIM(u.estado))='PRESTADO'",null);
    }

    @Override
    public List<Prestamo> findByTipoPrestamo(String tipo) {
        return consultar("WHERE p.tipo_prestamo=?",tipo);
    }

    @Override
    public Optional<Prestamo> findByIdPrestamo(Integer id) {
        List<Prestamo> r=consultar("WHERE p.id_prestamo=?",String.valueOf(id));
        return r.stream().findFirst();
    }

    @Override
    public boolean updatePrestamo(Integer id,Prestamo p) {
        String sql="""
            UPDATE PRESTAMO SET fecha_salida=?,estado_componentes=?,
            tipo_prestamo=?,fecha_devolucion=?,documento_cliente=?,documento_emp=?
            WHERE id_prestamo=?
            """;
        try(Connection c=ConnectionBD.obtenerConexion();
            PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setObject(1,p.getFechaSalida());
            ps.setString(2,p.getEstadoComponentes());
            ps.setString(3,p.getTipoPrestamo());
            ps.setObject(4,p.getFechaDevolucion());
            ps.setString(5,p.getDocumentoCliente());
            ps.setString(6,p.getDocumentoEmp());
            ps.setInt(7,id);
            return ps.executeUpdate()==1;
        } catch(SQLException e) {
            throw new PersistenceException("Error al actualizar préstamo",e);
        }
    }

    @Override
    public boolean registrarDevolucion(
            Integer id,DevolucionPrestamoRequestDTO request) {
        try(Connection c=ConnectionBD.obtenerConexion()) {
            c.setAutoCommit(false);
            try {
                List<String[]> unidades=new ArrayList<>();
                try(PreparedStatement ps=c.prepareStatement(
                    "SELECT codigo,num_serie FROM PRESTAMO_UNIDAD_JUEGO WHERE id_prestamo=?")) {
                    ps.setInt(1,id);
                    try(ResultSet rs=ps.executeQuery()) {
                        while(rs.next()) unidades.add(new String[]{
                            rs.getString("codigo"),rs.getString("num_serie")});
                    }
                }

                if(unidades.isEmpty()) {
                    c.rollback();
                    return false;
                }

                String detalle="DEVUELTO "+request.getFechaDevolucionReal()+
                    " | Estado: "+request.getEstadoComponentes()+
                    " | "+(request.getObservaciones()==null?"":request.getObservaciones());
                if(detalle.length()>200) detalle=detalle.substring(0,200);

                try(PreparedStatement ps=c.prepareStatement(
                    "UPDATE PRESTAMO SET estado_componentes=? WHERE id_prestamo=?")) {
                    ps.setString(1,detalle);
                    ps.setInt(2,id);
                    if(ps.executeUpdate()!=1) throw new SQLException("Préstamo no encontrado.");
                }

                try(PreparedStatement ps=c.prepareStatement(
                    "UPDATE UNIDAD_JUEGO SET estado='DISPONIBLE' WHERE codigo=? AND num_serie=? AND UPPER(TRIM(estado))='PRESTADO'")) {
                    for(String[] unidad:unidades) {
                        ps.setString(1,unidad[0]);
                        ps.setString(2,unidad[1]);
                        if(ps.executeUpdate()!=1) throw new SQLException("No se pudo liberar una unidad.");
                    }
                }

                c.commit();
                return true;
            } catch(SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        } catch(SQLException e) {
            throw new PersistenceException("Error al registrar devolución",e);
        }
    }

    @Override
    public boolean deletePrestamo(Integer id) {
        try(Connection c=ConnectionBD.obtenerConexion()) {
            c.setAutoCommit(false);
            try {
                try(PreparedStatement ps=c.prepareStatement(
                    "SELECT COUNT(*) FROM UNIDAD_JUEGO u JOIN PRESTAMO_UNIDAD_JUEGO pu " +
                    "ON u.codigo=pu.codigo AND u.num_serie=pu.num_serie " +
                    "WHERE pu.id_prestamo=? AND UPPER(TRIM(u.estado))='PRESTADO'")) {
                    ps.setInt(1,id);
                    try(ResultSet rs=ps.executeQuery()) {
                        if(rs.next()&&rs.getInt(1)>0) {
                            c.rollback();
                            return false;
                        }
                    }
                }
                try(PreparedStatement ps=c.prepareStatement(
                    "DELETE FROM PRESTAMO_UNIDAD_JUEGO WHERE id_prestamo=?")) {
                    ps.setInt(1,id);
                    ps.executeUpdate();
                }
                try(PreparedStatement ps=c.prepareStatement(
                    "DELETE FROM PRESTAMO WHERE id_prestamo=?")) {
                    ps.setInt(1,id);
                    boolean ok=ps.executeUpdate()==1;
                    c.commit();
                    return ok;
                }
            } catch(SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        } catch(SQLException e) {
            throw new PersistenceException("Error al eliminar préstamo",e);
        }
    }

    private Prestamo mapear(ResultSet rs)throws SQLException {
        Prestamo p=new Prestamo();
        p.setIdPrestamo(rs.getInt("id_prestamo"));
        p.setFechaSalida(rs.getObject("fecha_salida",LocalDate.class));
        p.setEstadoComponentes(rs.getString("estado_componentes"));
        p.setTipoPrestamo(rs.getString("tipo_prestamo"));
        p.setFechaDevolucion(rs.getObject("fecha_devolucion",LocalDate.class));
        p.setDocumentoCliente(rs.getString("documento_cliente"));
        p.setDocumentoEmp(rs.getString("documento_emp"));
        p.setCodigoUnidad(rs.getString("codigo"));
        p.setNumSerieUnidad(rs.getString("num_serie"));
        return p;
    }
}
