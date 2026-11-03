
package co.edu.unbosque.model.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequestDTO {

    private String idReserva;
    private LocalDate fecha;
    private String horario;
    private String documento;
    private String idMesa;
    private List<String> juegos;

}
