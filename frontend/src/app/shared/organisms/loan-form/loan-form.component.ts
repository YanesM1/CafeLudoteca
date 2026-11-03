import { Component, EventEmitter, Input, Output } from '@angular/core';

import {
    SelectComponent,
    SelectOption
} from '../../atoms/select/select.component';

import { InputComponent } from '../../atoms/input/input.component';
import { LabelComponent } from '../../atoms/label/label.component';
import { BadgeComponent } from '../../atoms/badge/badge.component';
import { TextareaComponent } from '../../atoms/textarea/textarea.component';

import {
    ActionButtonsComponent,
    ActionButton
} from '../../molecules/action-buttons/action-buttons.component';

import { LoadingStateComponent } from '../../molecules/loading-state/loading-state.component';

export interface LoanFormData {
    documentoCliente: string;
    idMembresia: string;
    idUnidad: string;
    documentoEmpleado: string;
    tipoPrestamo: string;
    fechaSalida: string;
    fechaDevolucion: string;
}

export interface LoanAuditData {
    fechaDevolucion: string;
    estadoComponentes: string;
    observaciones: string;
}

@Component({
    selector: 'app-loan-form',
    standalone: true,
    imports: [
        SelectComponent,
        InputComponent,
        LabelComponent,
        BadgeComponent,
        TextareaComponent,
        ActionButtonsComponent,
        LoadingStateComponent
    ],
    templateUrl: './loan-form.component.html',
    styleUrl: './loan-form.component.css'
})
export class LoanFormComponent {

    @Input() clienteOptions: SelectOption[] = [];
    @Input() membershipOptions: SelectOption[] = [];
    @Input() unitOptions: SelectOption[] = [];
    @Input() employeeOptions: SelectOption[] = [];
    @Input() componentStatusOptions: SelectOption[] = [];
    @Input() activeLoanOptions: SelectOption[] = [];

    @Input() activeLoanId = '';
    @Input() submitting = false;
    @Input() loading = false;
    @Input() errorMessage = '';

    @Output() registerLoan = new EventEmitter<LoanFormData>();

    @Output() updateAudit = new EventEmitter<{
        prestamoId: string;
        auditoria: LoanAuditData;
    }>();

    @Output() activeLoanChange = new EventEmitter<string>();
    @Output() cancel = new EventEmitter<void>();
    @Output() retry = new EventEmitter<void>();

    loanData: LoanFormData = {
        documentoCliente: '',
        idMembresia: '',
        idUnidad: '',
        documentoEmpleado: '',
        tipoPrestamo: '',
        fechaSalida: '',
        fechaDevolucion: ''
    };

    auditData: LoanAuditData = {
        fechaDevolucion: '',
        estadoComponentes: '',
        observaciones: ''
    };

    get canRegister(): boolean {
        return !!this.loanData.documentoCliente
            && !!this.loanData.idUnidad
            && !!this.loanData.documentoEmpleado
            && !!this.loanData.tipoPrestamo
            && !!this.loanData.fechaSalida
            && !!this.loanData.fechaDevolucion
            && !this.submitting;
    }

    get canUpdateAudit(): boolean {
        return !!this.activeLoanId
            && !!this.auditData.fechaDevolucion
            && !!this.auditData.estadoComponentes
            && !this.submitting;
    }

    get loanActions(): ActionButton[] {
        return [
            {
                id: 'clear',
                label: 'Cancelar',
                variant: 'danger',
                icon: 'close',
                disabled: this.submitting
            },
            {
                id: 'register',
                label: this.submitting
                    ? 'Procesando...'
                    : 'Registrar préstamo',
                variant: 'gold',
                icon: 'check',
                disabled: !this.canRegister
            }
        ];
    }

    get auditActions(): ActionButton[] {
        return [
            {
                id: 'update-audit',
                label: this.submitting
                    ? 'Procesando...'
                    : 'Finalizar y actualizar',
                variant: 'gold',
                icon: 'check',
                disabled: !this.canUpdateAudit
            }
        ];
    }

    onActiveLoanChange(value: string): void {
        this.activeLoanId = value;

        this.auditData = {
            fechaDevolucion: '',
            estadoComponentes: '',
            observaciones: ''
        };

        this.activeLoanChange.emit(value);
    }

    onRetry(): void {
        this.retry.emit();
    }

    onLoanAction(action: string): void {

        if (action === 'clear') {
            this.clearForm();
        }

        if (action === 'register' && this.canRegister) {
            this.registerLoan.emit({ ...this.loanData });
        }
    }

    onAuditAction(action: string): void {

        if (action === 'update-audit' && this.canUpdateAudit) {

            this.updateAudit.emit({
                prestamoId: this.activeLoanId,
                auditoria: { ...this.auditData }
            });
        }
    }

    clearForm(): void {

        this.loanData = {
            documentoCliente: '',
            idMembresia: '',
            idUnidad: '',
            documentoEmpleado: '',
            tipoPrestamo: '',
            fechaSalida: '',
            fechaDevolucion: ''
        };

        this.cancel.emit();
    }
}
