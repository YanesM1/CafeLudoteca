package co.edu.unbosque.services.implementations;

import java.util.*;
import co.edu.unbosque.mapper.PrestamoMapper;
import co.edu.unbosque.model.dto.*;
import co.edu.unbosque.model.entity.Prestamo;
import co.edu.unbosque.repository.implementations.PrestamoRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IPrestamoRepository;
import co.edu.unbosque.services.interfaces.IPrestamoServicio;

public class PrestamoServicio implements IPrestamoServicio {
    private final IPrestamoRepository repository=new PrestamoRepositoryJDBC();
    private final PrestamoMapper mapper=new PrestamoMapper();

    private boolean validar(PrestamoRequestDTO r) {
        return r!=null && r.getFechaSalida()!=null
            && r.getFechaDevolucion()!=null
            && !r.getFechaDevolucion().isBefore(r.getFechaSalida())
            && r.getDocumentoCliente()!=null && !r.getDocumentoCliente().isBlank()
            && r.getDocumentoEmp()!=null && !r.getDocumentoEmp().isBlank()
            && r.getTipoPrestamo()!=null && !r.getTipoPrestamo().isBlank()
            && r.getCodigoUnidad()!=null && !r.getCodigoUnidad().isBlank()
            && r.getNumSerieUnidad()!=null && !r.getNumSerieUnidad().isBlank();
    }

    private List<PrestamoResponseDTO> convertir(List<Prestamo> lista) {
        return lista.stream().map(mapper::prestamoToPrestamoResponseDTO).toList();
    }

    @Override
    public PrestamoResponseDTO addPrestamo(PrestamoRequestDTO r) {
        if(!validar(r)) return null;
        Prestamo p=mapper.prestamoRequestDTOToPrestamo(r);
        p.setEstadoComponentes("PENDIENTE_DEVOLUCION");
        return repository.savePrestamo(p)?mapper.prestamoToPrestamoResponseDTO(p):null;
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
            ||r.getEstadoComponentes()==null||r.getEstadoComponentes().isBlank())return null;
        if(getPrestamoByIdPrestamo(id)==null)return null;
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
