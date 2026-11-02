import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Empleado } from '../../../../core/models/empleado.model';
import { EmpleadoService } from '../../../../core/services/empleado.service';

@Component({
  selector: 'app-employees-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './employees-page.component.html',
  styles: [`
    :host { display:block; color:#f4f4fa; }
    main { padding:24px; }
    header { display:flex; justify-content:space-between; align-items:center; gap:15px; }
    h1 { font-size:28px; }
    small { color:#22d3ee; font-weight:bold; }
    section { background:#1b2030; padding:20px; margin-top:20px; border-radius:12px; }
    input { padding:12px; background:#101827; color:white; border:1px solid #394256; border-radius:7px; }
    button { padding:11px 15px; margin:4px; border-radius:7px; cursor:pointer; }
    .gold { background:#d5b75d; border:0; color:#171322; }
    .search { display:flex; flex-wrap:wrap; gap:15px; }
    form { display:flex; flex-wrap:wrap; gap:8px; align-items:center; }
    table { width:100%; border-collapse:collapse; margin-top:15px; }
    th,td { padding:13px; text-align:left; border-bottom:1px solid #394256; }
    th { color:#22d3ee; }
    .overlay { position:fixed; inset:0; z-index:3000; background:#000b; display:grid; place-items:center; }
    .modal { background:#1b2030; padding:25px; border:1px solid #22d3ee; border-radius:12px; width:min(480px,90%); }
    .modal form { display:grid; }
    .modal input { width:100%; box-sizing:border-box; }
    .message { padding:12px; background:#17352d; }
    .error { background:#49252a; }
  `]
})
export class EmployeesPageComponent {
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

  vacio(): Empleado {
    return {
      documentoEmp: '',
      cargo: '',
      nombre: '',
      apellido: '',
      correo: ''
    };
  }

  buscar(): void {
    if (!this.nombre.trim()) {
      this.aviso('Escribe un nombre para buscar.', true);
      return;
    }

    this.service.buscar(this.nombre.trim()).subscribe({
      next: data => this.empleados = data,
      error: e => this.aviso(e.error?.mensaje || 'Error al consultar.', true)
    });
  }

  buscarDocumento(): void {
    if (!this.documento.trim()) return;

    this.service.buscarDocumento(this.documento.trim()).subscribe({
      next: data => this.empleados = [data],
      error: e => {
        this.empleados = [];
        this.aviso(e.error?.mensaje || 'Empleado no encontrado.', true);
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
    if (Object.values(this.form).some(v => !v.trim())) {
      this.aviso('Todos los campos son obligatorios.', true);
      return;
    }

    this.guardando = true;
    const data = { ...this.form };
    const peticion = this.documentoOriginal
      ? this.service.actualizar(this.documentoOriginal, data)
      : this.service.crear(data);

    peticion.subscribe({
      next: () => {
        this.guardando = false;
        this.modal = false;
        this.nombre = data.nombre;
        this.documento = '';
        this.aviso(this.documentoOriginal ? 'Empleado actualizado.' : 'Empleado creado.');
        this.documentoOriginal = null;
        this.buscar();
      },
      error: e => {
        this.guardando = false;
        this.aviso(e.error?.mensaje || 'No se pudo guardar.', true);
      }
    });
  }

  eliminar(e: Empleado): void {
    if (!confirm('¿Eliminar a ' + e.nombre + ' ' + e.apellido + '?')) return;

    this.service.eliminar(e.documentoEmp).subscribe({
      next: () => {
        this.aviso('Empleado eliminado.');
        this.nombre.trim() ? this.buscar() : this.empleados = [];
      },
      error: err => this.aviso(err.error?.mensaje || 'No se pudo eliminar.', true)
    });
  }

  limpiar(): void {
    this.nombre = '';
    this.documento = '';
    this.empleados = [];
    this.mensaje = '';
  }

  aviso(texto: string, esError = false): void {
    this.mensaje = texto;
    this.error = esError;
  }
}
