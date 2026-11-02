package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.Beneficio;

public interface IBeneficioRepository {

    boolean saveBeneficio(Beneficio beneficio);

    List<Beneficio> findAll();

    Optional<Beneficio> findByIdBeneficio(
            Integer idBeneficio,
            Integer idMembresia);

    boolean updateBeneficio(
            Integer idBeneficio,
            Integer idMembresia,
            Beneficio beneficio);

    boolean deleteBeneficio(
            Integer idBeneficio,
            Integer idMembresia);
}