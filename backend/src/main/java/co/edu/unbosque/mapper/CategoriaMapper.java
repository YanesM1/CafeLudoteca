package co.edu.unbosque.mapper;

import co.edu.unbosque.model.dto.CategoriaRequestDTO;
import co.edu.unbosque.model.dto.CategoriaResponseDTO;
import co.edu.unbosque.model.entity.Categoria;

public class CategoriaMapper {
    public Categoria categoriaRequestDTOToCategoria(CategoriaRequestDTO dto){
        Categoria categoria=new Categoria();
        categoria.setNumCategoria(dto.getNumCategoria());
        categoria.setCategoria(dto.getCategoria());
        return categoria;
    }

    public CategoriaResponseDTO categoriaToCategoriaResponseDTO(Categoria categoria){
        CategoriaResponseDTO dto=new CategoriaResponseDTO();
        dto.setNumCategoria(categoria.getNumCategoria());
        dto.setCategoria(categoria.getCategoria());
        return dto;


    }

}
