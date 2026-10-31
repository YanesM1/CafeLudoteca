import { Component } from '@angular/core';

import {
    LoanFormComponent,
    LoanFormData,
    LoanAuditData
} from '../../../../shared/organisms/loan-form/loan-form.component';

import { SelectOption } from '../../../../shared/atoms/select/select.component';

import {
    NotificationToastComponent,
    NotificationType
} from '../../../../shared/molecules/notification-toast/notification-toast.component';

@Component({
    selector: 'app-loans-page',
    standalone: true,
    imports: [
        LoanFormComponent,
        NotificationToastComponent
    ],
    templateUrl: './loans-page.component.html',
    styleUrl: './loans-page.component.css'
})
export class LoansPageComponent {

    clienteOptions: SelectOption[] = [];
    membershipOptions: SelectOption[] = [];
    unitOptions: SelectOption[] = [];
    employeeOptions: SelectOption[] = [];
    componentStatusOptions: SelectOption[] = [];
    activeLoanOptions: SelectOption[] = [];

    activeLoanId = '';

    submitting = false;
    loading = false;
    errorMessage = '';

    notificationVisible = false;
    notificationType: NotificationType = 'info';
    notificationTitle = '';
    notificationMessage = '';

    onRegisterLoan(_data: LoanFormData): void {

        this.showNotification(
            'info',
            'Integración pendiente',
            'El formulario está preparado. El registro estará disponible cuando se conecte el backend. No se guardó información.'
        );
    }

    onActiveLoanChange(value: string): void {
        this.activeLoanId = value;
    }

    onUpdateAudit(_data: {
        prestamoId: string;
        auditoria: LoanAuditData;
    }): void {

        this.showNotification(
            'info',
            'Integración pendiente',
            'La auditoría está preparada. Su actualización estará disponible cuando se conecte el backend.'
        );
    }

    onCancel(): void {

        this.showNotification(
            'info',
            'Formulario restablecido',
            'Puedes comenzar un nuevo registro.'
        );
    }

    onRetry(): void {
        this.errorMessage = '';
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
