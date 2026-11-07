package co.edu.unbosque.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.exceptions.BusinessException;
import co.edu.unbosque.mapper.CategoriaMapper;
import co.edu.unbosque.repository.implementations.JuegoMesaRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.IJuegoMesaRepository;
import co.edu.unbosque.model.dto.CategoriaRequestDTO;
import co.edu.unbosque.model.dto.CategoriaResponseDTO;
import co.edu.unbosque.model.entity.Categoria;
import co.edu.unbosque.repository.implementations.CategoriaRepositoryJDBC;
import co.edu.unbosque.repository.interfaces.ICategoriaRepository;
import co.edu.unbosque.services.interfaces.ICategoriaService;

public class CategoriaService implements ICategoriaService {
    private final ICategoriaRepository categoriaRepository = new CategoriaRepositoryJDBC();
    private final CategoriaMapper categoriaMapper = new CategoriaMapper();
    private final IJuegoMesaRepository juegos = new JuegoMesaRepositoryJDBC();

    private boolean validarCategoria(CategoriaRequestDTO request) {

        if (request == null) {
            return false;
        }

        if (request.getNumCategoria() <= 0) {
            return false;
        }

        if (request.getCategoria() == null || request.getCategoria().isBlank()) {
            return false;
        }

        if (request.getNumSerie() == null || request.getNumSerie().isBlank()) {
            return false;
        }

        return true;
    }

    /** El número de categoría se asigna automáticamente dentro de cada juego. */
    @Override
    public CategoriaResponseDTO addCategoria(CategoriaRequestDTO request) {
        if (request == null || request.getNumSerie() == null || request.getNumSerie().isBlank())
            throw BusinessException.badRequest("Selecciona el juego.");
        if (request.getCategoria() == null || request.getCategoria().isBlank())
            throw BusinessException.badRequest("Escribe o elige la categoría.");
        String numSerie = request.getNumSerie().trim();
        String nombre = request.getCategoria().trim();
        if (juegos.findByNumSerie(numSerie).isEmpty())
            throw BusinessException.notFound("El juego " + numSerie + " no existe.");
        boolean repetida = categoriaRepository.findByCategoria("").stream()
            .anyMatch(c -> numSerie.equals(c.getNumSerie()) && nombre.equalsIgnoreCase(c.getCategoria()));
        if (repetida)
            throw BusinessException.conflict("El juego ya tiene la categoría " + nombre + ".");
        request.setNumSerie(numSerie);
        request.setCategoria(nombre);
        request.setNumCategoria(categoriaRepository.siguienteNumero(numSerie));

        Categoria categoria = categoriaMapper.categoriaRequestDTOToCategoria(request);

        if (!categoriaRepository.saveCategoria(categoria)) {
            return null;
        }

        return categoriaMapper.categoriaToCategoriaResponseDTO(categoria);
    }

    @Override
    public List<CategoriaResponseDTO> getCategoria(String categoria) {

        List<Categoria> categorias = categoriaRepository.findByCategoria(categoria);
        List<CategoriaResponseDTO> response = new ArrayList<>();

        if (categorias == null) {
            return response;
        }

        for (Categoria categoriaJ : categorias) {
            response.add(
                    categoriaMapper.categoriaToCategoriaResponseDTO(categoriaJ));
        }

        return response;
    }

    @Override
    public CategoriaResponseDTO getCategoriaNumSerie(Integer numCategoria, String numSerie) {
        Optional<Categoria> categoriaJ = categoriaRepository.findByNumCategoria(numCategoria, numSerie);

        if (categoriaJ.isEmpty()) {
            return null;
        }

        return categoriaMapper.categoriaToCategoriaResponseDTO(
                categoriaJ.get());
    }

    @Override
    public CategoriaResponseDTO updateCategoria(Integer numCategoria, String numSerie, CategoriaRequestDTO request) {
        if (request != null) {
            request.setNumCategoria(numCategoria);
            request.setNumSerie(numSerie);
        }
        if (!validarCategoria(request)) {
            return null;
        }

        Categoria categoria = categoriaMapper.categoriaRequestDTOToCategoria(request);

        if (!categoriaRepository.updateCategoria(numCategoria, numSerie, categoria)) {
            return null;
        }

        Optional<Categoria> actualizado = categoriaRepository.findByNumCategoria(numCategoria, numSerie);

        if (actualizado.isEmpty()) {
            return null;
        }

        return categoriaMapper.categoriaToCategoriaResponseDTO(actualizado.get());
    }

    @Override
    public CategoriaResponseDTO deleteCategoria(Integer numCategoria, String numSerie) {
        Optional<Categoria> categoriaj = categoriaRepository.findByNumCategoria(numCategoria, numSerie);

        if (categoriaj.isEmpty()) {
            return null;
        }

        if (!categoriaRepository.deleteCategoria(numCategoria, numSerie)) {
            return null;
        }

        return categoriaMapper.categoriaToCategoriaResponseDTO(
                categoriaj.get());
    }

}
