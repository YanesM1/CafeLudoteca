
package co.edu.unbosque.services.implementations;

import java.util.Optional;

import co.edu.unbosque.mapper.ElementoJuegoMapper;
import co.edu.unbosque.model.dto.ElementoJuegoRequestDTO;
import co.edu.unbosque.model.dto.ElementoJuegoResponseDTO;
import co.edu.unbosque.model.entity.ElementoJuego;
import co.edu.unbosque.repository.implementations.ElementoJuegoRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IElementoJuegoRepository;
import co.edu.unbosque.services.interfaces.IElementoJuegoService;

public class ElementoJuegoService implements IElementoJuegoService {

    private final IElementoJuegoRepository elementoJuegoRepository = new ElementoJuegoRepositoryJDBC();
    private final ElementoJuegoMapper elementoJuegoMapper = new ElementoJuegoMapper();

    private boolean validarElementoJuego(ElementoJuegoRequestDTO request) {

        if (request == null) {
            return false;
        }

        if (request.getIdElementoJuego() <= 0) {
            return false;
        }

        if (request.getElementoJuego() == null || request.getElementoJuego().isBlank()) {
            return false;
        }

        if (request.getNumSerie() == null || request.getNumSerie().isBlank()) {
            return false;
        }

        return true;
    }

    @Override
    public ElementoJuegoResponseDTO addElementoJuego(ElementoJuegoRequestDTO request) {

        if (!validarElementoJuego(request)) {
            return null;
        }

        ElementoJuego elementoJuego = elementoJuegoMapper.elementoJuegoRequestDTOToElementoJuego(request);

        if (!elementoJuegoRepository.saveElementoJuego(elementoJuego)) {
            return null;
        }

        return elementoJuegoMapper.elementoJuegoToElementoJuegoResponseDTO(elementoJuego);
    }

    @Override
    public ElementoJuegoResponseDTO getPorIdElementoJuego(Integer idElementoJuego) {
 Optional<ElementoJuego> elementoJuego = elementoJuegoRepository.findByIdElementoJuego(idElementoJuego);

        if (elementoJuego.isEmpty()) {
            return null;
        }

        return elementoJuegoMapper.elementoJuegoToElementoJuegoResponseDTO(
                elementoJuego.get());
    }

    @Override
    public ElementoJuegoResponseDTO updateElementoJuego(
            Integer idElementoJuego, ElementoJuegoRequestDTO request) {

        if (idElementoJuego == null || idElementoJuego <= 0) {
            return null;
        }

        if (!validarElementoJuego(request)) {
            return null;
        }

        Optional<ElementoJuego> existente =
                elementoJuegoRepository.findByIdElementoJuego(idElementoJuego);

        if (existente.isEmpty()) {
            return null;
        }

        ElementoJuego elementoJuego =
                elementoJuegoMapper.elementoJuegoRequestDTOToElementoJuego(request);

        if (!elementoJuegoRepository.updateElementoJuego(idElementoJuego, elementoJuego)) {
            return null;
        }

        Optional<ElementoJuego> actualizado =
                elementoJuegoRepository.findByIdElementoJuego(idElementoJuego);

        if (actualizado.isEmpty()) {
            return null;
        }

        return elementoJuegoMapper.elementoJuegoToElementoJuegoResponseDTO(actualizado.get());
    }

    @Override
    public ElementoJuegoResponseDTO deleteElementoJuego(Integer idElementoJuego) {

        if (idElementoJuego == null || idElementoJuego <= 0) {
            return null;
        }

        Optional<ElementoJuego> elementoJuego =
                elementoJuegoRepository.findByIdElementoJuego(idElementoJuego);

        if (elementoJuego.isEmpty()) {
            return null;
        }

        if (!elementoJuegoRepository.deleteElementoJuego(idElementoJuego)) {
            return null;
        }

        return elementoJuegoMapper.elementoJuegoToElementoJuegoResponseDTO(elementoJuego.get());
    }
}
