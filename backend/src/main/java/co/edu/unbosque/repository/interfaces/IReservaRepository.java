
package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.Reserva;

public interface IReservaRepository {

    boolean saveReserva(Reserva reserva, List<String> juegos);

    List<Reserva> findByDocumento(String documento);

    List<Reserva> findAll(java.time.LocalDate fecha);

    Optional<Reserva> findByIdReserva(String idReserva);

    boolean updateReserva(String idReserva, Reserva reserva, List<String> juegos);

    boolean deleteReserva(String idReserva);

}
