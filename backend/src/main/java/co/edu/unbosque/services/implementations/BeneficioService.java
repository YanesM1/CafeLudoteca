package co.edu.unbosque.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.mapper.BeneficioMapper;
import co.edu.unbosque.model.dto.BeneficioRequestDTO;
import co.edu.unbosque.model.dto.BeneficioResponseDTO;
import co.edu.unbosque.model.entity.Beneficio;
import co.edu.unbosque.repository.implementations.BeneficioRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IBeneficioRepository;
import co.edu.unbosque.services.interfaces.IBeneficioService;

public class BeneficioService implements IBeneficioService {

    private final IBeneficioRepository beneficioRepository =
            new BeneficioRepositoryJDBC();

    private final BeneficioMapper beneficioMapper =
            new BeneficioMapper();

    private boolean validarBeneficio(BeneficioRequestDTO request) {

        if (request == null) {
            return false;
        }

        if (request.getIdBeneficio() == null
                || request.getIdBeneficio() <= 0) {
            return false;
        }

        if (request.getIdMembresia() == null
                || request.getIdMembresia() <= 0) {
            return false;
        }

        if (request.getBeneficio() == null
                || request.getBeneficio().isBlank()) {
            return false;
        }

        return true;
    }

    @Override
    public BeneficioResponseDTO addBeneficio(
            BeneficioRequestDTO request) {

        if (!validarBeneficio(request)) {
            return null;
        }

        Beneficio beneficio =
                beneficioMapper.beneficioRequestDTOToBeneficio(request);

        if (!beneficioRepository.saveBeneficio(beneficio)) {
            return null;
        }

        return beneficioMapper.beneficioToResponseDTO(beneficio);
    }

    @Override
    public List<BeneficioResponseDTO> getBeneficios() {

        List<Beneficio> beneficios =
                beneficioRepository.findAll();

        List<BeneficioResponseDTO> response =
                new ArrayList<>();

        for (Beneficio beneficio : beneficios) {
            response.add(
                    beneficioMapper.beneficioToResponseDTO(
                            beneficio));
        }

        return response;
    }

    @Override
    public BeneficioResponseDTO getBeneficioPorId(
            Integer idBeneficio,
            Integer idMembresia) {

        if (idBeneficio == null || idBeneficio <= 0
                || idMembresia == null || idMembresia <= 0) {
            return null;
        }

        Optional<Beneficio> beneficio =
                beneficioRepository.findByIdBeneficio(
                        idBeneficio,
                        idMembresia);

        if (beneficio.isEmpty()) {
            return null;
        }

        return beneficioMapper
                .beneficioToResponseDTO(beneficio.get());
    }

    @Override
    public BeneficioResponseDTO updateBeneficio(Integer idBeneficio, Integer idMembresia, 
        BeneficioRequestDTO request) {

        if (idBeneficio == null || idBeneficio <= 0
                || idMembresia == null || idMembresia <= 0) {
            return null;
        }

        if (!validarBeneficio(request)) {
            return null;
        }

        Beneficio beneficio =
                beneficioMapper.beneficioRequestDTOToBeneficio(request);

        if (!beneficioRepository.updateBeneficio(
                idBeneficio,
                idMembresia,
                beneficio)) {
            return null;
        }

        Optional<Beneficio> actualizado =
                beneficioRepository.findByIdBeneficio(
                        idBeneficio,
                        idMembresia);

        if (actualizado.isEmpty()) {
            return null;
        }

        return beneficioMapper
                .beneficioToResponseDTO(actualizado.get());
    }

    @Override
    public BeneficioResponseDTO deleteBeneficio(
            Integer idBeneficio,
            Integer idMembresia) {

        if (idBeneficio == null || idBeneficio <= 0
                || idMembresia == null || idMembresia <= 0) {
            return null;
        }

        Optional<Beneficio> beneficio =
                beneficioRepository.findByIdBeneficio(
                        idBeneficio,
                        idMembresia);

        if (beneficio.isEmpty()) {
            return null;
        }

        if (!beneficioRepository.deleteBeneficio(
                idBeneficio,
                idMembresia)) {
            return null;
        }

        return beneficioMapper
                .beneficioToResponseDTO(beneficio.get());
    }
}