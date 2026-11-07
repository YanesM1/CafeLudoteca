package co.edu.unbosque.services.implementations;

import java.util.*;
import co.edu.unbosque.mapper.PrestamoMapper;
import co.edu.unbosque.model.dto.*;
import co.edu.unbosque.model.entity.Prestamo;
import co.edu.unbosque.exceptions.BusinessException;
import co.edu.unbosque.model.entity.Membresia;
import co.edu.unbosque.repository.implementations.ClienteRepositoryJDBC;
import co.edu.unbosque.repository.implementations.EmpleadoRepositoryJDBC;
import co.edu.unbosque.repository.implementations.MembresiaRepositoryJDBC;
import co.edu.unbosque.repository.implementations.PrestamoRepositoryJDBC;
import co.edu.unbosque.repository.implementations.SocioRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IPrestamoRepository;
import co.edu.unbosque.services.interfaces.IPrestamoServicio;

public class PrestamoServicio implements IPrestamoServicio {
    private final IPrestamoRepository repository=new PrestamoRepositoryJDBC();
    private final PrestamoMapper mapper=new PrestamoMapper();
    private final ClienteRepositoryJDBC clientes=new ClienteRepositoryJDBC();
    private final EmpleadoRepositoryJDBC empleados=new EmpleadoRepositoryJDBC();
    private final SocioRepositoryJDBC socios=new SocioRepositoryJDBC();
    private final MembresiaRepositoryJDBC membresias=new MembresiaRepositoryJDBC();

    private static final Set<String> ESTADOS_DEVOLUCION=Set.of("COMPLETO","INCOMPLETO","DANADO");

    private static boolean esExterno(String tipo) {
        String t=tipo==null?"":tipo.trim().toLowerCase();
        return t.equals("externo")||t.equals("domicilio")||t.equals("alquiler");
    }

    private boolean tieneMembresiaActiva(String documento) {
        java.time.LocalDate hoy=java.time.LocalDate.now();
        for(Membresia m:membresias.findByDocumento(documento)) {
            boolean activa="ACTIVA".equalsIgnoreCase(m.getEstado()==null?"":m.getEstado().trim());
            boolean vigente=m.getFechaFin()==null||!m.getFechaFin().isBefore(hoy);
            if(activa&&vigente) return true;
        }
        return false;
    }

    /** Reglas de negocio del préstamo (RN-02 y RN-07). Lanza 400 con un mensaje claro. */
    private void validarReglas(PrestamoRequestDTO r) {
        String doc=r.getDocumentoCliente().trim();
        r.setDocumentoCliente(doc);
        r.setDocumentoEmp(r.getDocumentoEmp().trim());
        if(clientes.findByDocumento(doc).isEmpty())
            throw BusinessException.badRequest("No existe un cliente con el documento "+doc+".");
        if(empleados.findByDocumento(r.getDocumentoEmp()).isEmpty())
            throw BusinessException.badRequest("El empleado responsable no existe.");

        String tipo=r.getTipoPrestamo().trim().toLowerCase();
        if(!tipo.equals("interno")&&!esExterno(tipo))
            throw BusinessException.badRequest("El tipo de préstamo debe ser interno o externo.");
        r.setTipoPrestamo(esExterno(tipo)?"Externo":"Interno");

        boolean esSocio=socios.findByDocumento(doc).isPresent();
        if(esExterno(tipo)&&!esSocio)
            throw BusinessException.badRequest(
                "Solo los socios pueden solicitar préstamos externos (a domicilio).");
        if(esSocio&&!tieneMembresiaActiva(doc))
            throw BusinessException.badRequest(
                "El socio no tiene una membresía activa y vigente, por eso no puede solicitar préstamos.");
    }

    private boolean validar(PrestamoRequestDTO r) {
        return r!=null && r.getFechaSalida()!=null
            && r.getFechaDevolucion()!=null
            && !r.getFechaDevolucion().isBefore(r.getFechaSalida())
            && r.getDocumentoCliente()!=null && !r.getDocumentoCliente().isBlank()
            && r.getDocumentoEmp()!=null && !r.getDocumentoEmp().isBlank()
            && r.getTipoPrestamo()!=null && !r.getTipoPrestamo().isBlank();
    }

    /**
     * Unidades del préstamo: la lista "unidades" o, por compatibilidad, la unidad única
     * (codigoUnidad / numSerieUnidad). Se quitan repetidas.
     */
    private List<UnidadPrestadaDTO> unidadesDe(PrestamoRequestDTO r) {
        Map<String,UnidadPrestadaDTO> unicas=new LinkedHashMap<>();
        List<UnidadPrestadaDTO> recibidas=new ArrayList<>();
        if(r.getUnidades()!=null) recibidas.addAll(r.getUnidades());
        if(recibidas.isEmpty()&&r.getCodigoUnidad()!=null)
            recibidas.add(new UnidadPrestadaDTO(r.getCodigoUnidad(),r.getNumSerieUnidad()));
        for(UnidadPrestadaDTO u:recibidas) {
            if(u==null||u.getCodigo()==null||u.getCodigo().isBlank()
                ||u.getNumSerie()==null||u.getNumSerie().isBlank()) continue;
            UnidadPrestadaDTO limpia=new UnidadPrestadaDTO(u.getCodigo().trim(),u.getNumSerie().trim());
            unicas.put(limpia.getCodigo()+"::"+limpia.getNumSerie(),limpia);
        }
        return new ArrayList<>(unicas.values());
    }

    private List<PrestamoResponseDTO> convertir(List<Prestamo> lista) {
        return lista.stream().map(mapper::prestamoToPrestamoResponseDTO).toList();
    }

    @Override
    public PrestamoResponseDTO addPrestamo(PrestamoRequestDTO r) {
        if(!validar(r)) throw BusinessException.badRequest(
            "Completa cliente, empleado, tipo y fechas. La devolución no puede ser anterior a la salida.");
        List<UnidadPrestadaDTO> unidades=unidadesDe(r);
        if(unidades.isEmpty())
            throw BusinessException.badRequest("Agrega al menos una unidad física al préstamo.");
        validarReglas(r);
        Prestamo p=mapper.prestamoRequestDTOToPrestamo(r);
        p.setUnidades(unidades);
        p.setCodigoUnidad(unidades.get(0).getCodigo());
        p.setNumSerieUnidad(unidades.get(0).getNumSerie());
        p.setEstadoComponentes("PENDIENTE_DEVOLUCION");
        if(!repository.savePrestamo(p))
            throw BusinessException.badRequest("No fue posible registrar el préstamo.");
        return mapper.prestamoToPrestamoResponseDTO(p);
    }

    @Override
    public List<PrestamoResponseDTO> getPrestamo(String tipo) {
        return convertir(tipo==null||tipo.isBlank()
            ?repository.findAll():repository.findByTipoPrestamo(tipo));
    }

    @Override
    public List<PrestamoResponseDTO> getActivos() {
        return convertir(repository.findActivos());
    }

    @Override
    public List<PrestamoResponseDTO> getHistorial() {
        return convertir(repository.findAll());
    }

    @Override
    public PrestamoResponseDTO getPrestamoByIdPrestamo(Integer id) {
        if(id==null||id<=0)return null;
        return repository.findByIdPrestamo(id)
            .map(mapper::prestamoToPrestamoResponseDTO).orElse(null);
    }

    @Override
    public PrestamoResponseDTO updatePrestamo(Integer id,PrestamoRequestDTO r) {
        if(id==null||id<=0||!validar(r))return null;
        Prestamo p=mapper.prestamoRequestDTOToPrestamo(r);
        if(!repository.updatePrestamo(id,p))return null;
        return getPrestamoByIdPrestamo(id);
    }

    @Override
    public PrestamoResponseDTO registrarDevolucion(
            Integer id,DevolucionPrestamoRequestDTO r) {
        if(id==null||id<=0||r==null||r.getFechaDevolucionReal()==null
            ||r.getEstadoComponentes()==null||r.getEstadoComponentes().isBlank())
            throw BusinessException.badRequest("Indica la fecha de devolución y el estado de los componentes.");
        r.setEstadoComponentes(r.getEstadoComponentes().trim().toUpperCase());
        if(!ESTADOS_DEVOLUCION.contains(r.getEstadoComponentes()))
            throw BusinessException.badRequest("El estado de los componentes debe ser COMPLETO, INCOMPLETO o DANADO.");
        PrestamoResponseDTO actual=getPrestamoByIdPrestamo(id);
        if(actual==null)return null;
        if(actual.getFechaSalida()!=null&&r.getFechaDevolucionReal().isBefore(actual.getFechaSalida()))
            throw BusinessException.badRequest("La fecha de devolución no puede ser anterior a la fecha de salida.");
        return repository.registrarDevolucion(id,r)
            ?getPrestamoByIdPrestamo(id):null;
    }

    @Override
    public PrestamoResponseDTO deletePrestamo(Integer id) {
        if(id==null||id<=0)return null;
        PrestamoResponseDTO p=getPrestamoByIdPrestamo(id);
        if(p==null||!repository.deletePrestamo(id))return null;
        return p;
    }
}
