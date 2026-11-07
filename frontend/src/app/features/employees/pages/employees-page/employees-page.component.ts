import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Empleado } from '../../../../core/models/empleado.model';
import { EmpleadoService } from '../../../../core/services/empleado.service';
import { mensajeError } from '../../../../core/utils/http-error';

@Component({
  selector: 'app-employees-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './employees-page.component.html',
  styles: [':host { display: block; }']
})
export class EmployeesPageComponent implements OnInit {
  empleados: Empleado[] = [];
  nombre = '';
  documento = '';
  modal = false;
  guardando = false;
  mensaje = '';
  error = false;
  documentoOriginal: string | null = null;

  form: Empleado = this.vacio();

  constructor(private service: EmpleadoService) {}

  ngOnInit(): void {
    this.buscar();
  }

  vacio(): Empleado {
    return {
      documentoEmp: '',
      cargo: '',
      nombre: '',
      apellido: '',
      correo: ''
    };
  }

  /** Busca por nombre; sin texto lista todos los empleados. */
  /** Cargos ya usados, para elegirlos sin escribir de nuevo. */
  get cargos(): string[] {
    return [...new Set(this.empleados.map(e => e.cargo).filter(Boolean))].sort();
  }

  busqueda = '';
  criterio: 'nombre' | 'documento' = 'nombre';

  buscarSegunCriterio(): void {
    const valor = this.busqueda.trim();
    if (this.criterio === 'documento' && valor) {
      this.documento = valor;
      this.buscarDocumento();
    } else {
      this.nombre = valor;
      this.buscar();
    }
  }

  buscar(): void {
    const peticion = this.nombre.trim()
      ? this.service.buscar(this.nombre.trim())
      : this.service.listar();

    peticion.subscribe({
      next: data => this.empleados = data,
      error: e => this.aviso(mensajeError(e, 'Error al consultar.'), true)
    });
  }

  buscarDocumento(): void {
    if (!this.documento.trim()) return;

    this.service.buscarDocumento(this.documento.trim()).subscribe({
      next: data => this.empleados = [data],
      error: e => {
        this.empleados = [];
        this.aviso(mensajeError(e, 'Empleado no encontrado.'), true);
      }
    });
  }

  nuevo(): void {
    this.form = this.vacio();
    this.documentoOriginal = null;
    this.modal = true;
  }

  editar(e: Empleado): void {
    this.form = { ...e };
    this.documentoOriginal = e.documentoEmp;
    this.modal = true;
  }

  guardar(): void {
    if (Object.values(this.form).some(v => !String(v ?? '').trim())) {
      this.aviso('Todos los campos son obligatorios.', true);
      return;
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.form.correo.trim())) {
      this.aviso('Escribe un correo válido.', true);
      return;
    }

    this.guardando = true;
    const editando = !!this.documentoOriginal;
    const data = { ...this.form };
    const peticion = editando
      ? this.service.actualizar(this.documentoOriginal!, data)
      : this.service.crear(data);

    peticion.subscribe({
      next: () => {
        this.guardando = false;
        this.modal = false;
        this.nombre = '';
        this.documento = '';
        this.aviso(editando ? 'Empleado actualizado.' : 'Empleado creado.');
        this.documentoOriginal = null;
        this.buscar();
      },
      error: e => {
        this.guardando = false;
        this.aviso(mensajeError(e, 'No se pudo guardar.'), true);
      }
    });
  }

  eliminar(e: Empleado): void {
    if (!confirm('¿Eliminar a ' + e.nombre + ' ' + e.apellido + '?')) return;

    this.service.eliminar(e.documentoEmp).subscribe({
      next: () => {
        this.aviso('Empleado eliminado.');
        this.buscar();
      },
      error: err => this.aviso(mensajeError(err, 'No se pudo eliminar.'), true)
    });
  }

  limpiar(): void {
    this.nombre = '';
    this.documento = '';
    this.mensaje = '';
    this.buscar();
  }

  aviso(texto: string, esError = false): void {
    this.mensaje = texto;
    this.error = esError;
  }
}
