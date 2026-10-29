package co.edu.unbosque.model.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteFrecuenteRequestDTO {
    private String documento;
    private int puntosAcumulados;
    private String nivelFidelidad;

}
