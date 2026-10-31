package co.edu.unbosque.repository.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.Socio;

public interface ISocioRepository {

    boolean saveSocio(Socio socio);

    List<Socio> findByFechaAfiliacion(LocalDate fechaAfiliacion);

    Optional<Socio> findByDocumento(String documento);

    boolean updateSocio(String documento, Socio socio);

    boolean deleteSocio(String documento);

}