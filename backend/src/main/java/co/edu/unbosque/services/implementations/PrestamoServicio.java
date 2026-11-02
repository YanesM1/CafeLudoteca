
package co.edu.unbosque.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.mapper.PrestamoMapper;
import co.edu.unbosque.model.dto.PrestamoRequestDTO;
import co.edu.unbosque.model.dto.PrestamoResponseDTO;
import co.edu.unbosque.model.entity.Prestamo;
import co.edu.unbosque.repository.implementations.PrestamoRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IPrestamoRepository;
import co.edu.unbosque.services.interfaces.IPrestamoServicio;

public class PrestamoServicio implements IPrestamoServicio {

    private final IPrestamoRepository prestamoRepository = new PrestamoRepositoryJDBC();
    private final PrestamoMapper prestamoMapper = new PrestamoMapper();

    private boolean validarPrestamo(PrestamoRequestDTO request) {

        if (request == null) {
            return false;
        }
        
        if (request.getFechaSalida() == null) {
            return false;
        }

        if (request.getEstadoComponentes() == null
                || request.getEstadoComponentes().isBlank()) {
            return false;
        }

        if (request.getTipoPrestamo() == null
                || request.getTipoPrestamo().isBlank()) {
            return false;
        }

        if (request.getFechaDevolucion() == null) {
            return false;
        }

        if (request.getFechaDevolucion().isBefore(request.getFechaSalida())) {
            return false;
        }

        if (request.getDocumentoEmp() == null
                || request.getDocumentoEmp().isBlank()) {
            return false;
        }

        if (request.getDocumentoCliente() == null
                || request.getDocumentoCliente().isBlank()) {
            return false;
        }

        return true;
    }

    @Override
    public PrestamoResponseDTO addPrestamo(PrestamoRequestDTO request) {

        if (!validarPrestamo(request)) {
            return null;
        }

        Prestamo prestamo = prestamoMapper.prestamoRequestDTOToPrestamo(request);

        if (!prestamoRepository.savePrestamo(prestamo)) {
            return null;
        }

        return prestamoMapper.prestamoToPrestamoResponseDTO(prestamo);
    }

    @Override
    public List<PrestamoResponseDTO> getPrestamo(String tipoPrestamo) {

        List<PrestamoResponseDTO> response = new ArrayList<>();

        if (tipoPrestamo == null || tipoPrestamo.isBlank()) {
            return response;
        }

        List<Prestamo> prestamos =
                prestamoRepository.findByTipoPrestamo(tipoPrestamo);

        for (Prestamo prestamo : prestamos) {
            response.add(
                    prestamoMapper.prestamoToPrestamoResponseDTO(prestamo)
            );
        }

        return response;
    }

    @Override
    public PrestamoResponseDTO getPrestamoByIdPrestamo(Integer idPrestamo) {

        if (idPrestamo == null || idPrestamo <= 0) {
            return null;
        }

        Optional<Prestamo> prestamo =
                prestamoRepository.findByIdPrestamo(idPrestamo);

        if (prestamo.isEmpty()) {
            return null;
        }

        return prestamoMapper.prestamoToPrestamoResponseDTO(prestamo.get());
    }

    @Override
    public PrestamoResponseDTO updatePrestamo(
            Integer idPrestamo, PrestamoRequestDTO request) {

        if (idPrestamo == null || idPrestamo <= 0) {
            return null;
        }

        if (!validarPrestamo(request)) {
            return null;
        }


        
        Prestamo prestamo =
                prestamoMapper.prestamoRequestDTOToPrestamo(request);

        if (!prestamoRepository.updatePrestamo(idPrestamo, prestamo)) {
            return null;
        }

        Optional<Prestamo> actualizado =
                prestamoRepository.findByIdPrestamo(idPrestamo);

        if (actualizado.isEmpty()) {
            return null;
        }

        return prestamoMapper.prestamoToPrestamoResponseDTO(actualizado.get());
    }

    @Override
    public PrestamoResponseDTO deletePrestamo(Integer idPrestamo) {

        if (idPrestamo == null || idPrestamo <= 0) {
            return null;
        }

        Optional<Prestamo> prestamo =
                prestamoRepository.findByIdPrestamo(idPrestamo);

        if (prestamo.isEmpty()) {
            return null;
        }

        if (!prestamoRepository.deletePrestamo(idPrestamo)) {
            return null;
        }

        return prestamoMapper.prestamoToPrestamoResponseDTO(prestamo.get());
    }
}
