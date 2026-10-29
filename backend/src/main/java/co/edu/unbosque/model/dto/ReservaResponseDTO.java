
package co.edu.unbosque.model.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponseDTO {

    private String idReserva;
    private LocalDate fecha;
    private String horario;
    private String documento;
    private String idMesa;

}
