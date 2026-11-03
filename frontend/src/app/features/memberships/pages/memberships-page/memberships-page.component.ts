import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { forkJoin } from 'rxjs';

import {
  MembershipTableComponent,
  MembershipTableRow
} from '../../../../shared/organisms/membership-table/membership-table.component';

import {
  MembershipFormModalComponent
} from '../../../../shared/organisms/membership-form-modal/membership-form-modal.component';

import {
  NotificationToastComponent,
  NotificationType
} from '../../../../shared/molecules/notification-toast/notification-toast.component';

import { SelectOption } from '../../../../shared/atoms/select/select.component';
import { Membresia, MembresiaService } from '../../../../core/services/membresia.service';

@Component({
  selector: 'app-memberships-page',
  standalone: true,
  imports: [
    MembershipTableComponent,
    MembershipFormModalComponent,
    NotificationToastComponent
  ],
  templateUrl: './memberships-page.component.html',
  styleUrl: './memberships-page.component.css'
})
export class MembershipsPageComponent implements OnInit {

  members: MembershipTableRow[] = [];
  membershipOptions: SelectOption[] = [];

  loading = false;
  saving = false;
  errorMessage = '';

  showModal = false;
  modalMode: 'create' | 'edit' = 'create';

  selectedMember: MembershipTableRow | null = null;
  memberToDelete: MembershipTableRow | null = null;

  notificationVisible = false;
  notificationType: NotificationType = 'info';
  notificationTitle = '';
  notificationMessage = '';

  constructor(private readonly service: MembresiaService) {}

  ngOnInit(): void {
    this.loadMembers();
  }

  private normalizeDate(value: unknown): string {
    if (Array.isArray(value)) {
      const [year, month, day] = value;
      return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
    }

    return typeof value === 'string' ? value.substring(0, 10) : '';
  }

  private toRow(m: Membresia): MembershipTableRow {
    return {
      identificador: String(m.idMembresia),
      documento: m.documento,
      nombre: `Socio ${m.documento}`,
      plan: m.plan,
      tarifa: String(m.tarifa),
      fechaInicio: this.normalizeDate(m.fechaInicio),
      fechaFin: this.normalizeDate(m.fechaFin),
      estado: m.estado
    };
  }

  loadMembers(): void {
    this.loading = true;
    this.errorMessage = '';

    this.service.listar().subscribe({
      next: data => {
        this.members = data.map(m => this.toRow(m));

        const plans = [...new Set(data.map(m => m.plan).filter(Boolean))];
        this.membershipOptions = plans.map(plan => ({
          value: plan,
          label: plan
        }));

        this.loading = false;
      },
      error: error => {
        this.loading = false;
        this.errorMessage = this.errorText(
          error,
          'No fue posible consultar las membresías.'
        );
      }
    });
  }

  openCreate(): void {
    this.selectedMember = null;
    this.modalMode = 'create';
    this.showModal = true;
  }

  openEdit(member: MembershipTableRow): void {
    this.selectedMember = { ...member };
    this.modalMode = 'edit';
    this.showModal = true;
  }

  saveMember(member: MembershipTableRow): void {
    const request: Membresia = {
      idMembresia: Number(member.identificador),
      documento: member.documento.trim(),
      plan: member.plan,
      estado: member.estado,
      fechaInicio: member.fechaInicio,
      fechaFin: member.fechaFin,
      tarifa: Number(member.tarifa)
    };

    if (!Number.isInteger(request.idMembresia) || request.idMembresia <= 0) {
      this.showNotification('error', 'Identificador inválido',
        'El ID de membresía debe ser un número entero positivo.');
      return;
    }

    if (!request.documento || !request.plan ||
        !request.fechaInicio || !request.fechaFin ||
        !Number.isFinite(request.tarifa) || request.tarifa <= 0) {
      this.showNotification('error', 'Datos incompletos',
        'Verifica todos los campos de la membresía.');
      return;
    }

    if (request.fechaFin < request.fechaInicio) {
      this.showNotification('error', 'Fechas inválidas',
        'La fecha final no puede ser anterior a la inicial.');
      return;
    }

    this.saving = true;

    const editing = this.modalMode === 'edit';
    const operation = editing
      ? this.service.actualizar(request.idMembresia, request.documento, request)
      : this.service.crear(request);

    operation.subscribe({
      next: () => {
        this.saving = false;
        this.closeModal();
        this.showNotification(
          'success',
          editing ? 'Membresía actualizada' : 'Membresía registrada',
          'La operación se guardó correctamente.'
        );
        this.loadMembers();
      },
      error: error => {
        this.saving = false;
        this.showNotification('error', 'No se pudo guardar',
          this.errorText(error, 'Error al guardar la membresía.'));
      }
    });
  }

  closeModal(): void {
    this.showModal = false;
    this.selectedMember = null;
  }

  requestDelete(member: MembershipTableRow): void {
    this.memberToDelete = member;
  }

  confirmDelete(): void {
    if (!this.memberToDelete) return;

    const id = Number(this.memberToDelete.identificador);

    this.service.eliminar(id, this.memberToDelete.documento).subscribe({
      next: () => {
        this.memberToDelete = null;
        this.showNotification('success', 'Membresía eliminada',
          'El registro fue eliminado correctamente.');
        this.loadMembers();
      },
      error: error => {
        this.showNotification('error', 'No se pudo eliminar',
          this.errorText(error, 'Error al eliminar la membresía.'));
      }
    });
  }

  cancelDelete(): void {
    this.memberToDelete = null;
  }

  showNotification(
    type: NotificationType,
    title: string,
    message: string
  ): void {
    this.notificationType = type;
    this.notificationTitle = title;
    this.notificationMessage = message;
    this.notificationVisible = true;
  }

  closeNotification(): void {
    this.notificationVisible = false;
  }

  private errorText(error: HttpErrorResponse, fallback: string): string {
    if (typeof error.error === 'string') return error.error;
    return error.error?.mensaje || error.message || fallback;
  }
}
