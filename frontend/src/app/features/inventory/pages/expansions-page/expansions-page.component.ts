import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Expansion, ExpansionService } from '../../../../core/services/expansion.service';

@Component({
  selector: 'app-expansions-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <main class="page">
    <h1>Expansiones</h1>
    <p>Administra el contenido adicional de los juegos.</p>
    <section class="panel">
      <h2>{{ editing ? 'Editar expansión' : 'Registrar expansión' }}</h2>
      <form (ngSubmit)="save()">
        <label>Nombre<input name="nombre" [(ngModel)]="form.nombre" required></label>
        <label>Número de serie del juego<input name="serie" [(ngModel)]="form.numSerie" required></label>
        <label>Descripción<input name="descripcion" [(ngModel)]="form.descripcion"></label>
        <label>Contenido adicional<input name="contenido" [(ngModel)]="form.contenidoAdicional"></label>
        <div class="actions">
          <button class="primary" [disabled]="saving">{{ saving ? 'Guardando...' : editing ? 'Actualizar' : 'Registrar' }}</button>
          <button type="button" *ngIf="editing" (click)="cancel()">Cancelar</button>
        </div>
      </form>
    </section>
    <section class="panel">
      <h2>Consultar expansiones</h2>
      <div class="search">
        <input [(ngModel)]="search" placeholder="Nombre de expansión">
        <button class="primary" (click)="load()">Buscar</button>
      </div>
      <p class="error">{{ error }}</p>
      <p class="message">{{ message }}</p>
      <table *ngIf="items.length">
        <thead><tr><th>ID</th><th>Nombre</th><th>Juego</th><th>Descripción</th><th>Acciones</th></tr></thead>
        <tbody>
          @for (item of items; track item.idExpansion) {
            <tr>
              <td>{{ item.idExpansion }}</td><td>{{ item.nombre }}</td>
              <td>{{ item.numSerie }}</td><td>{{ item.descripcion }}</td>
              <td class="actions">
                <button (click)="edit(item)">Editar</button>
                <button class="danger" (click)="remove(item)">Eliminar</button>
              </td>
            </tr>
          }
        </tbody>
      </table>
      <p *ngIf="searched && !items.length">No se encontraron expansiones.</p>
    </section>
  </main>`,
  styles: [`
    .page{padding:28px;background:#0b1020;color:#fff;min-height:80vh}
    .panel{background:#1b2030;border:1px solid #465267;border-radius:14px;padding:22px;margin:20px 0}
    p{color:#aeb5c5}form{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:15px}
    label{display:grid;gap:8px;color:#c6ccd7}
    input{box-sizing:border-box;width:100%;padding:12px;background:#101827;color:white;border:1px solid #526078;border-radius:8px}
    button{padding:10px 14px;border-radius:8px;cursor:pointer;background:transparent;color:#22d3ee;border:1px solid #22d3ee}
    .primary{background:#d3b354;color:#171827;border-color:#d3b354}.danger{background:#a32154;color:white;border-color:#a32154}
    .actions,.search{display:flex;gap:9px;align-items:center;flex-wrap:wrap}.actions{grid-column:1/-1}
    table{width:100%;border-collapse:collapse}td,th{text-align:left;padding:12px;border-bottom:1px solid #465267}th{color:#22d3ee}
    .error{color:#ff7892}.message{color:#22d3ee}
    @media(max-width:650px){form{grid-template-columns:1fr}}
  `]
})
export class ExpansionsPageComponent {
  items: Expansion[] = [];
  form: Expansion = this.empty();
  search = '';
  editing = false;
  saving = false;
  searched = false;
  error = '';
  message = '';

  constructor(private service: ExpansionService) {}

  private empty(): Expansion {
    return { nombre:'', descripcion:'', contenidoAdicional:'', numSerie:'' };
  }

  load(): void {
    if (!this.search.trim()) { this.error='Escribe el nombre que deseas consultar.'; return; }
    this.error=''; this.searched=true;
    this.service.buscar(this.search.trim()).subscribe({
      next: data => this.items=data,
      error: e => this.error=e.error?.mensaje || 'Error al consultar expansiones.'
    });
  }

  save(): void {
    if (!this.form.nombre.trim() || !this.form.numSerie.trim()) {
      this.error='Nombre y número de serie son obligatorios.'; return;
    }
    this.saving=true; this.error='';
    const data={...this.form};
    const request=this.editing
      ? this.service.actualizar(data.idExpansion!,data)
      : this.service.crear(data);
    request.subscribe({
      next: () => {
        this.saving=false;
        this.message=this.editing?'Expansión actualizada.':'Expansión registrada.';
        this.cancel();
        if(this.search.trim()) this.load();
      },
      error: e => {this.saving=false;this.error=e.error?.mensaje || 'No se pudo guardar.';}
    });
  }

  edit(item: Expansion): void { this.form={...item}; this.editing=true; this.error=''; }

  cancel(): void { this.form=this.empty(); this.editing=false; }

  remove(item: Expansion): void {
    if(!item.idExpansion || !confirm(`¿Eliminar ${item.nombre}?`)) return;
    this.service.eliminar(item.idExpansion).subscribe({
      next:()=>{this.message='Expansión eliminada.';this.load();},
      error:e=>this.error=e.error?.mensaje || 'No se pudo eliminar.'
    });
  }
}
