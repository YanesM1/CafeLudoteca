package co.edu.unbosque.services.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final BeneficioMapper beneficioMapper = new BeneficioMapper();

    @Override
    public BeneficioResponseDTO addBeneficio(BeneficioRequestDTO request) {
        if (request == null) {
            return null;
        }

        Beneficio beneficio = beneficioMapper.beneficioRequestDTOToBeneficio(request);

        if (!beneficioRepository.saveBeneficio(beneficio)) {
            return null;
        }

        return beneficioMapper.beneficioToResponseDTO(beneficio);
    }

    @Override
    public List<BeneficioResponseDTO> getBeneficiosPorMembresia(String idMembresia) {
        List<Beneficio> beneficios =
                beneficioRepository.findByIdMembresia(idMembresia);

        return beneficios.stream()
                .map(beneficioMapper::beneficioToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BeneficioResponseDTO getBeneficioPorId(
            String idMembresia,
            String idBeneficio) {

        Optional<Beneficio> beneficio =
                beneficioRepository.findByIdMembresiaAndIdBeneficio(
                        idMembresia, idBeneficio);

        return beneficio
                .map(beneficioMapper::beneficioToResponseDTO)
                .orElse(null);
    }

    @Override
    public BeneficioResponseDTO updateBeneficio(
            String idMembresia,
            String idBeneficio,
            BeneficioRequestDTO request) {

        if (idMembresia == null || idBeneficio == null || request == null) {
            return null;
        }

        Beneficio beneficio = beneficioMapper.beneficioRequestDTOToBeneficio(request);

        if (!beneficioRepository.updateBeneficio(
                idMembresia, idBeneficio, beneficio)) {
            return null;
        }

        return beneficioRepository
                .findByIdMembresiaAndIdBeneficio(idMembresia, idBeneficio)
                .map(beneficioMapper::beneficioToResponseDTO)
                .orElse(null);
    }

    @Override
    public BeneficioResponseDTO deleteBeneficio(
            String idMembresia,
            String idBeneficio) {

        Optional<Beneficio> beneficio =
                beneficioRepository.findByIdMembresiaAndIdBeneficio(
                        idMembresia, idBeneficio);

        if (beneficio.isEmpty()) {
            return null;
        }

        if (!beneficioRepository.deleteBeneficio(idMembresia, idBeneficio)) {
            return null;
        }

        return beneficioMapper.beneficioToResponseDTO(beneficio.get());
    }
}