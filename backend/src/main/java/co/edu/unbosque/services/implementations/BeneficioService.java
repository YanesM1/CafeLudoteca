package co.edu.unbosque.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.exceptions.BusinessException;
import co.edu.unbosque.mapper.BeneficioMapper;
import co.edu.unbosque.repository.implementations.MembresiaRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IMembresiaRepository;
import co.edu.unbosque.model.dto.BeneficioRequestDTO;
import co.edu.unbosque.model.dto.BeneficioResponseDTO;
import co.edu.unbosque.model.entity.Beneficio;
import co.edu.unbosque.repository.implementations.BeneficioRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IBeneficioRepository;
import co.edu.unbosque.services.interfaces.IBeneficioService;

public class BeneficioService implements IBeneficioService {
    private final IBeneficioRepository repository = new BeneficioRepositoryJDBC();
    private final BeneficioMapper mapper = new BeneficioMapper();

    private final IMembresiaRepository membresias = new MembresiaRepositoryJDBC();

    private void validar(BeneficioRequestDTO r) {
        if (r == null || r.getIdMembresia() == null || r.getIdMembresia() <= 0
                || r.getDocumentoSocio() == null || r.getDocumentoSocio().isBlank())
            throw BusinessException.badRequest("Selecciona la membresía a la que pertenece el beneficio.");
        if (r.getBeneficio() == null || r.getBeneficio().isBlank())
            throw BusinessException.badRequest("Escribe la descripción del beneficio.");
        r.setBeneficio(r.getBeneficio().trim());
        r.setDocumentoSocio(r.getDocumentoSocio().trim());
    }

    /** El número de beneficio se asigna automáticamente. */
    @Override
    public BeneficioResponseDTO addBeneficio(BeneficioRequestDTO r) {
        validar(r);
        if (membresias.findByIdMembresia(r.getIdMembresia(), r.getDocumentoSocio()).isEmpty())
            throw BusinessException.notFound("La membresía seleccionada no existe.");
        r.setIdBeneficio(repository.siguienteId());
        Beneficio b = mapper.beneficioRequestDTOToBeneficio(r);
        if (!repository.saveBeneficio(b))
            throw BusinessException.badRequest("No fue posible registrar el beneficio.");
        return mapper.beneficioToResponseDTO(b);
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
        validar(r);
        r.setIdBeneficio(id);
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
