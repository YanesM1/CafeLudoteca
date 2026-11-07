import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ClienteService } from '../../../../core/services/cliente.service';
import { ClienteRequest, ClienteResponse } from '../../../../core/models/cliente.model';

import { fechaCorta, fechaISO } from '../../../../core/utils/fecha';
import { mensajeError } from '../../../../core/utils/http-error';

@Component({
    selector: 'app-clients-page',
    standalone: true,
    imports: [FormsModule],
    templateUrl: './clients-page.component.html',
    styleUrl: './clients-page.component.css'
})
export class ClientsPageComponent implements OnInit {

    clientes: ClienteResponse[] = [];
    loading = false;
    saving = false;
    deleting = false;

    searchTerm = '';
    modalOpen = false;
    editingDocument: string | null = null;
    selectedClient: ClienteResponse | null = null;

    notification = '';
    notificationType: 'success' | 'error' = 'success';

    form: ClienteRequest = this.emptyForm();

    /** Teléfono opcional al crear el cliente (se guarda en TELEFONO). */
    telefono = '';

    constructor(private clienteService: ClienteService, private http: HttpClient) {}

    ngOnInit(): void {
        this.cargarClientes();
    }

    private emptyForm(): ClienteRequest {
        return {
            documentoCliente: '',
            primerNombre: '',
            segundoNombre: null,
            primerApellido: '',
            segundoApellido: null,
            direccion: null,
            fechaNacimiento: null
        };
    }

    cargarClientes(): void {
        this.loading = true;

        this.clienteService.listar(this.searchTerm).subscribe({
            next: data => {
                this.clientes = data;
                this.loading = false;
            },
            error: error => {
                this.loading = false;
                this.notify('error', this.errorMessage(error));
            }
        });
    }

    buscar(): void {
        this.cargarClientes();
    }

    limpiar(): void {
        this.searchTerm = '';
        this.cargarClientes();
    }

    abrirCrear(): void {
        this.form = this.emptyForm();
        this.telefono = '';
        this.editingDocument = null;
        this.modalOpen = true;
    }

    abrirEditar(cliente: ClienteResponse): void {
        this.form = { ...cliente, fechaNacimiento: fechaISO(cliente.fechaNacimiento) || null };
        this.editingDocument = cliente.documentoCliente;
        this.modalOpen = true;
    }

    cerrarModal(): void {
        if (this.saving) return;
        this.modalOpen = false;
    }

    guardar(): void {
        if (this.saving) return;

        const data: ClienteRequest = {
            ...this.form,
            documentoCliente: this.form.documentoCliente.trim(),
            primerNombre: this.form.primerNombre.trim(),
            primerApellido: this.form.primerApellido.trim(),
            segundoNombre: this.form.segundoNombre?.trim() || null,
            segundoApellido: this.form.segundoApellido?.trim() || null,
            direccion: this.form.direccion?.trim() || null,
            fechaNacimiento: this.form.fechaNacimiento || null
        };

        if (!data.documentoCliente || !data.primerNombre || !data.primerApellido) {
            this.notify('error',
                'Documento, primer nombre y primer apellido son obligatorios.');
            return;
        }
        const telefono = this.editingDocument ? '' : this.telefono.replace(/[\s-]/g, '');
        if (telefono && !/^\d{7,15}$/.test(telefono)) {
            this.notify('error', 'El teléfono debe tener entre 7 y 15 dígitos.');
            return;
        }

        const editing = this.editingDocument;
        this.saving = true;

        const request = editing
            ? this.clienteService.actualizar(editing, data)
            : this.clienteService.crear(data);

        request.subscribe({
            next: () => {
                this.saving = false;
                this.modalOpen = false;
                this.editingDocument = null;
                this.notify('success',
                    editing ? 'Cliente actualizado.' : 'Cliente registrado.');
                this.cargarClientes();
                if (telefono) {
                    this.http.post('/api/telefono', { documento: data.documentoCliente, telefono }).subscribe({
                        next: () => this.notify('success', 'Cliente registrado con su teléfono.'),
                        error: e => this.notify('error',
                            'Cliente registrado, pero el teléfono no se guardó: ' + this.errorMessage(e))
                    });
                }
                this.telefono = '';
            },
            error: error => {
                this.saving = false;
                this.notify('error', this.errorMessage(error));
            }
        });
    }

    confirmarEliminacion(cliente: ClienteResponse): void {
        this.selectedClient = cliente;
    }

    cancelarEliminacion(): void {
        this.selectedClient = null;
    }

    eliminar(): void {
        if (!this.selectedClient || this.deleting) return;

        this.deleting = true;
        const documento = this.selectedClient.documentoCliente;

        this.clienteService.eliminar(documento).subscribe({
            next: () => {
                this.deleting = false;
                this.selectedClient = null;
                this.notify('success', 'Cliente eliminado correctamente.');
                this.cargarClientes();
            },
            error: error => {
                this.deleting = false;
                this.notify('error', this.errorMessage(error));
            }
        });
    }

    nombreCompleto(c: ClienteResponse): string {
        return [
            c.primerNombre, c.segundoNombre,
            c.primerApellido, c.segundoApellido
        ].filter(Boolean).join(' ');
    }

    notify(type: 'success' | 'error', message: string): void {
        this.notificationType = type;
        this.notification = message;
    }

    fechaCorta(valor: unknown): string {
        return fechaCorta(valor);
    }

    private errorMessage(error: unknown): string {
        return mensajeError(error, 'No fue posible completar la operación.');
    }
}
