package co.edu.unbosque.model.dto;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DevolucionPrestamoRequestDTO {
    private LocalDate fechaDevolucionReal;
    private String estadoComponentes;
    private String observaciones;
}
