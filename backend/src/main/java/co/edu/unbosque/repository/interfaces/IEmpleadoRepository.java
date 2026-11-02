
package co.edu.unbosque.repository.interfaces;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.model.entity.Empleado;

public interface IEmpleadoRepository {
    boolean saveEmpleado(Empleado empleado);

    List<Empleado> findByNombre(String nombre);

    Optional<Empleado> findByDocumento(String documentoEmp);

    boolean updateEmpleado(String documentoEmp, Empleado empleado);

    boolean deleteEmpleado(String documentoEmp);
}
