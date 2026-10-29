
package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.Reserva;

public interface IReservaRepository {

    boolean saveReserva(Reserva reserva);

    List<Reserva> findByDocumento(String documento);

    Optional<Reserva> findByIdReserva(String idReserva);

    boolean updateReserva(String idReserva, Reserva reserva);

    boolean deleteReserva(String idReserva);

}
