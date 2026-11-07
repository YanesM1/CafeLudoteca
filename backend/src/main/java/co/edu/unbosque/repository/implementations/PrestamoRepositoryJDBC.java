package co.edu.unbosque.repository.implementations;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import co.edu.unbosque.config.ConnectionBD;
import co.edu.unbosque.exceptions.PersistenceException;
import co.edu.unbosque.model.entity.Prestamo;
import co.edu.unbosque.model.dto.DevolucionPrestamoRequestDTO;
import co.edu.unbosque.model.dto.UnidadPrestadaDTO;
import co.edu.unbosque.repository.interfaces.IPrestamoRepository;

public class PrestamoRepositoryJDBC implements IPrestamoRepository {

    /** Estados de una unidad mientras está en un préstamo (supuesto 4 de la propuesta). */
    private static final String EN_PRESTAMO = "('PRESTADO','EN_MESA')";

    /**
     * Un préstamo está activo si no se ha registrado su devolución y al menos una de sus
     * unidades sigue en préstamo con este préstamo como el más reciente de esa unidad.
     * (Así un préstamo viejo no "revive" cuando la misma unidad se presta otra vez.)
     */
    private static final String ACTIVO =
        "COALESCE(p.estado_componentes,'') NOT LIKE 'DEVUELTO%' AND EXISTS (" +
        " SELECT 1 FROM PRESTAMO_UNIDAD_JUEGO x JOIN UNIDAD_JUEGO u" +
        " ON u.codigo=x.codigo AND u.num_serie=x.num_serie" +
        " WHERE x.id_prestamo=p.id_prestamo AND UPPER(TRIM(u.estado)) IN " + EN_PRESTAMO +
        " AND x.id_prestamo=(SELECT MAX(y.id_prestamo) FROM PRESTAMO_UNIDAD_JUEGO y" +
        " WHERE y.codigo=x.codigo AND y.num_serie=x.num_serie))";

    /** Longitud de PRESTAMO.estado_componentes leída de la base (se calcula una vez). */
    private static volatile int largoEstadoComponentes = 0;

    private static int largoEstadoComponentes(Connection c) {
        if (largoEstadoComponentes > 0) return largoEstadoComponentes;
        int largo = 200;
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.COLUMNS " +
                "WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='PRESTAMO' AND COLUMN_NAME='estado_componentes'");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) largo = rs.getInt(1);
        } catch (SQLException e) {
            // Si no se puede leer, se usa el valor por defecto
        }
        largoEstadoComponentes = largo;
        return largo;
    }

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

        List<UnidadPrestadaDTO> unidades = p.getUnidades();

        try (Connection c = ConnectionBD.obtenerConexion()) {
            c.setAutoCommit(false);
            try {
                // Se bloquean y verifican todas las unidades antes de registrar (RN-01, RN-06)
                try (PreparedStatement lock = c.prepareStatement(
                    "SELECT estado FROM UNIDAD_JUEGO WHERE codigo=? AND num_serie=? FOR UPDATE")) {
                    for (UnidadPrestadaDTO u : unidades) {
                        lock.setString(1,u.getCodigo());
                        lock.setString(2,u.getNumSerie());
                        try (ResultSet rs=lock.executeQuery()) {
                            if (!rs.next()) {
                                c.rollback();
                                throw co.edu.unbosque.exceptions.BusinessException.badRequest(
                                    "La unidad "+u.getCodigo()+" no existe.");
                            }
                            String estado=rs.getString("estado");
                            if (estado == null || !"DISPONIBLE".equalsIgnoreCase(estado.trim())) {
                                c.rollback();
                                throw co.edu.unbosque.exceptions.BusinessException.conflict(
                                    "La unidad "+u.getCodigo()+" ya no está disponible para préstamo (RN-01).");
                            }
                        }
                    }
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

                try (PreparedStatement link=c.prepareStatement(
                         "INSERT INTO PRESTAMO_UNIDAD_JUEGO(id_prestamo,codigo,num_serie) VALUES(?,?,?)");
                     PreparedStatement marcar=c.prepareStatement(
                         "UPDATE UNIDAD_JUEGO SET estado=? WHERE codigo=? AND num_serie=? AND UPPER(TRIM(estado))='DISPONIBLE'")) {
                    // Interno: la unidad queda "En uso en mesa"; externo: "Prestado" (a domicilio)
                    String estadoNuevo = "externo".equalsIgnoreCase(p.getTipoPrestamo()) ? "PRESTADO" : "EN_MESA";
                    for (UnidadPrestadaDTO u : unidades) {
                        link.setInt(1,p.getIdPrestamo());
                        link.setString(2,u.getCodigo());
                        link.setString(3,u.getNumSerie());
                        link.executeUpdate();

                        marcar.setString(1,estadoNuevo);
                        marcar.setString(2,u.getCodigo());
                        marcar.setString(3,u.getNumSerie());
                        if (marcar.executeUpdate()!=1) throw new SQLException("Una unidad dejó de estar disponible.");
                    }
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
        String sql=SELECT+condicion+" ORDER BY p.id_prestamo DESC, pu.codigo";
        try (Connection c=ConnectionBD.obtenerConexion();
             PreparedStatement ps=c.prepareStatement(sql)) {
            if (parametro!=null) ps.setString(1,parametro);
            // Un préstamo con varias unidades llega en varias filas: se agrupan
            Map<Integer,Prestamo> porId=new LinkedHashMap<>();
            try (ResultSet rs=ps.executeQuery()) {
                while(rs.next()) {
                    Prestamo fila=mapear(rs);
                    Prestamo p=porId.computeIfAbsent(fila.getIdPrestamo(),k->fila);
                    if (fila.getCodigoUnidad()!=null)
                        p.getUnidades().add(new UnidadPrestadaDTO(fila.getCodigoUnidad(),fila.getNumSerieUnidad()));
                }
            }
            lista.addAll(porId.values());
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
        return consultar("WHERE "+ACTIVO,null);
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

    /** Indica si el préstamo sigue activo (ver ACTIVO). */
    private boolean esActivo(Connection c, int id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT 1 FROM PRESTAMO p WHERE p.id_prestamo=? AND " + ACTIVO)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean registrarDevolucion(
            Integer id,DevolucionPrestamoRequestDTO request) {
        try(Connection c=ConnectionBD.obtenerConexion()) {
            c.setAutoCommit(false);
            try {
                try(PreparedStatement ps=c.prepareStatement(
                    "SELECT 1 FROM PRESTAMO WHERE id_prestamo=? FOR UPDATE")) {
                    ps.setInt(1,id);
                    try(ResultSet rs=ps.executeQuery()) {
                        if(!rs.next()) { c.rollback(); return false; }
                    }
                }
                if(!esActivo(c,id)) {
                    c.rollback();
                    throw co.edu.unbosque.exceptions.BusinessException.conflict(
                        "Este préstamo ya fue devuelto.");
                }

                // Estado en que queda la unidad según cómo se devolvió (RF-06, supuesto 4)
                String estadoUnidad=switch(request.getEstadoComponentes()) {
                    case "INCOMPLETO" -> "FALTANTES";
                    case "DANADO" -> "MANTENIMIENTO";
                    default -> "DISPONIBLE";
                };

                // Observaciones de la devolución (RF-06), ajustadas al tamaño real de la columna
                String obs=request.getObservaciones()==null?"":request.getObservaciones().trim();
                String detalle="DEVUELTO "+request.getFechaDevolucionReal()+
                    " | Estado: "+request.getEstadoComponentes()+(obs.isEmpty()?"":" | "+obs);
                int largo=largoEstadoComponentes(c);
                if(detalle.length()>largo) detalle=detalle.substring(0,largo);

                try(PreparedStatement ps=c.prepareStatement(
                    "UPDATE PRESTAMO SET estado_componentes=? WHERE id_prestamo=?")) {
                    ps.setString(1,detalle);
                    ps.setInt(2,id);
                    if(ps.executeUpdate()!=1) throw new SQLException("Préstamo no encontrado.");
                }

                // Solo se liberan las unidades cuyo préstamo más reciente es este
                List<String[]> unidades=new ArrayList<>();
                try(PreparedStatement ps=c.prepareStatement(
                    "SELECT x.codigo,x.num_serie FROM PRESTAMO_UNIDAD_JUEGO x JOIN UNIDAD_JUEGO u " +
                    "ON u.codigo=x.codigo AND u.num_serie=x.num_serie " +
                    "WHERE x.id_prestamo=? AND UPPER(TRIM(u.estado)) IN " + EN_PRESTAMO + " " +
                    "AND x.id_prestamo=(SELECT MAX(y.id_prestamo) FROM PRESTAMO_UNIDAD_JUEGO y " +
                    "WHERE y.codigo=x.codigo AND y.num_serie=x.num_serie) FOR UPDATE")) {
                    ps.setInt(1,id);
                    try(ResultSet rs=ps.executeQuery()) {
                        while(rs.next()) unidades.add(new String[]{rs.getString(1),rs.getString(2)});
                    }
                }
                if(unidades.isEmpty()) throw new SQLException("No se pudo liberar ninguna unidad.");
                try(PreparedStatement ps=c.prepareStatement(
                    "UPDATE UNIDAD_JUEGO SET estado=? WHERE codigo=? AND num_serie=?")) {
                    for(String[] unidad:unidades) {
                        ps.setString(1,estadoUnidad);
                        ps.setString(2,unidad[0]);
                        ps.setString(3,unidad[1]);
                        ps.executeUpdate();
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
    public boolean unidadEnPrestamoActivo(String codigo, String numSerie) {
        String sql = "SELECT 1 FROM PRESTAMO p JOIN PRESTAMO_UNIDAD_JUEGO pu ON p.id_prestamo=pu.id_prestamo " +
            "WHERE pu.codigo=? AND pu.num_serie=? AND " + ACTIVO + " LIMIT 1";
        try (Connection c = ConnectionBD.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.setString(2, numSerie);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al consultar préstamos de la unidad", e);
        }
    }

    @Override
    public boolean deletePrestamo(Integer id) {
        try(Connection c=ConnectionBD.obtenerConexion()) {
            c.setAutoCommit(false);
            try {
                // Un préstamo activo no se elimina: primero se registra la devolución
                if(esActivo(c,id)) {
                    c.rollback();
                    return false;
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
