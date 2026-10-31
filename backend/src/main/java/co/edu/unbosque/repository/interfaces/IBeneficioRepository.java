package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.Beneficio;

public interface IBeneficioRepository {

    boolean saveBeneficio(Beneficio beneficio);

    List<Beneficio> findByIdMembresia(String idMembresia);

    Optional<Beneficio> findByIdMembresiaAndIdBeneficio(String idMembresia, String idBeneficio);

    boolean updateBeneficio(String idMembresia, String idBeneficio, Beneficio beneficio);

    boolean deleteBeneficio(String idMembresia, String idBeneficio);

}