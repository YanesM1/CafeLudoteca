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
public class ClienteRequestDTO {
     private String documentoCliente; // PK
     private String primerApellido;
     private String segundoApellido;
     private String primerNombre;
     private String segundoNombre;
     private String direccion;
     private LocalDate fechaNacimiento;

}
