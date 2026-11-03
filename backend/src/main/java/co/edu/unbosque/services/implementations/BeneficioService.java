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
    private final IBeneficioRepository repository = new BeneficioRepositoryJDBC();
    private final BeneficioMapper mapper = new BeneficioMapper();

    private boolean valido(BeneficioRequestDTO r) {
        return r != null && r.getIdBeneficio() != null && r.getIdBeneficio() > 0
            && r.getIdMembresia() != null && r.getIdMembresia() > 0
            && r.getDocumentoSocio() != null && !r.getDocumentoSocio().isBlank()
            && r.getBeneficio() != null && !r.getBeneficio().isBlank();
    }

    @Override
    public BeneficioResponseDTO addBeneficio(BeneficioRequestDTO r) {
        if (!valido(r)) return null;
        Beneficio b = mapper.beneficioRequestDTOToBeneficio(r);
        return repository.saveBeneficio(b) ? mapper.beneficioToResponseDTO(b) : null;
    }

    @Override
    public List<BeneficioResponseDTO> getBeneficios() {
        List<BeneficioResponseDTO> salida = new ArrayList<>();
        for (Beneficio b : repository.findAll())
            salida.add(mapper.beneficioToResponseDTO(b));
        return salida;
    }

    @Override
    public BeneficioResponseDTO getBeneficioPorId(Integer id, Integer membresia, String documento) {
        Optional<Beneficio> b = repository.findByIdBeneficio(id, membresia, documento);
        return b.map(mapper::beneficioToResponseDTO).orElse(null);
    }

    @Override
    public BeneficioResponseDTO updateBeneficio(Integer id, Integer membresia, String documento, BeneficioRequestDTO r) {
        if (!valido(r)) return null;
        Beneficio b = mapper.beneficioRequestDTOToBeneficio(r);
        if (!repository.updateBeneficio(id, membresia, documento, b)) return null;
        return getBeneficioPorId(id, membresia, documento);
    }

    @Override
    public BeneficioResponseDTO deleteBeneficio(Integer id, Integer membresia, String documento) {
        Beneficio b = repository.findByIdBeneficio(id, membresia, documento).orElse(null);
        if (b == null || !repository.deleteBeneficio(id, membresia, documento)) return null;
        return mapper.beneficioToResponseDTO(b);
    }
}
