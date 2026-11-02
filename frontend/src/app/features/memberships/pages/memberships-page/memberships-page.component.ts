import { Component } from '@angular/core';

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

import {
    SelectOption
} from '../../../../shared/atoms/select/select.component';

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
export class MembershipsPageComponent {

    members: MembershipTableRow[] = [];

    membershipOptions: SelectOption[] = [];

    loading = false;
    saving = false;

    showModal = false;
    modalMode: 'create' | 'edit' = 'create';

    selectedMember: MembershipTableRow | null = null;
    memberToDelete: MembershipTableRow | null = null;

    notificationVisible = false;
    notificationType: NotificationType = 'info';
    notificationTitle = '';
    notificationMessage = '';

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

    saveMember(_member: MembershipTableRow): void {

        this.closeModal();

        this.showNotification(
            'info',
            'Integración pendiente',
            'El formulario está preparado. Los cambios se guardarán cuando se conecte el backend. No se modificaron registros.'
        );
    }

    closeModal(): void {

        this.showModal = false;
        this.selectedMember = null;
    }

    requestDelete(member: MembershipTableRow): void {

        this.memberToDelete = member;
    }

    confirmDelete(): void {

        if (!this.memberToDelete) {
            return;
        }

        this.memberToDelete = null;

        this.showNotification(
            'info',
            'Integración pendiente',
            'La eliminación estará disponible cuando se conecte el backend. No se eliminó información.'
        );
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
}
