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
    /** Unidades elegidas como "codigo::numSerie" (un préstamo puede tener varias). */
    unidades: string[];
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
    /** Texto con la situación del cliente elegido (socio, membresía). */
    @Input() clienteInfo = '';
    /** true si el cliente elegido no puede pedir el tipo de préstamo marcado. */
    @Input() clienteAviso = false;
    /** Unidades del préstamo seleccionado para devolver. */
    @Input() activeLoanDetail = '';
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
    @Output() clienteChange = new EventEmitter<{ documento: string; tipo: string }>();
    @Output() cancel = new EventEmitter<void>();
    @Output() retry = new EventEmitter<void>();

    loanData: LoanFormData = this.nuevoPrestamo();

    private hoy(): string {
        const d = new Date();
        return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
    }

    /** Préstamo vacío: interno, saliendo hoy. */
    private nuevoPrestamo(): LoanFormData {
        const hoy = this.hoy();
        return {
            documentoCliente: '',
            unidades: [],
            documentoEmpleado: '',
            tipoPrestamo: 'interno',
            fechaSalida: hoy,
            fechaDevolucion: hoy
        };
    }

    /** Unidades disponibles que aún no se agregaron. */
    get unidadesLibres(): SelectOption[] {
        return this.unitOptions.filter(o => !this.loanData.unidades.includes(String(o.value)));
    }

    etiquetaUnidad(valor: string): string {
        return String(this.unitOptions.find(o => o.value === valor)?.label ?? valor.replace('::', ' · '));
    }

    agregarUnidad(valor: string): void {
        if (valor && !this.loanData.unidades.includes(valor)) {
            this.loanData = { ...this.loanData, unidades: [...this.loanData.unidades, valor] };
        }
    }

    quitarUnidad(valor: string): void {
        this.loanData = { ...this.loanData, unidades: this.loanData.unidades.filter(u => u !== valor) };
    }

    onClienteChange(documento: string): void {
        this.loanData.documentoCliente = documento;
        this.clienteChange.emit({ documento, tipo: this.loanData.tipoPrestamo });
    }

    onTipoChange(tipo: 'interno' | 'externo'): void {
        this.loanData.tipoPrestamo = tipo;
        // Interno: se usa en el local y se devuelve el mismo día
        this.loanData.fechaDevolucion = tipo === 'interno' ? this.loanData.fechaSalida : '';
        this.clienteChange.emit({ documento: this.loanData.documentoCliente, tipo });
    }

    onFechaSalida(fecha: string): void {
        this.loanData.fechaSalida = fecha;
        if (this.loanData.tipoPrestamo === 'interno') this.loanData.fechaDevolucion = fecha;
    }

    auditData: LoanAuditData = {
        fechaDevolucion: '',
        estadoComponentes: '',
        observaciones: ''
    };

    get canRegister(): boolean {
        return !!this.loanData.documentoCliente
            && this.loanData.unidades.length > 0
            && !this.clienteAviso
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
                variant: 'ghost',
                disabled: this.submitting
            },
            {
                id: 'register',
                label: this.submitting
                    ? 'Procesando...'
                    : 'Registrar préstamo',
                variant: 'gold',
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
                disabled: !this.canUpdateAudit
            }
        ];
    }

    onActiveLoanChange(value: string): void {
        this.activeLoanId = value;

        this.auditData = {
            fechaDevolucion: this.hoy(),
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
            this.registerLoan.emit({ ...this.loanData, unidades: [...this.loanData.unidades] });
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

    /** Limpia los datos del préstamo sin emitir eventos (tras registrar). */
    resetLoan(): void {
        this.loanData = this.nuevoPrestamo();
    }

    /** Limpia la auditoría sin emitir eventos (tras registrar la devolución). */
    resetAudit(): void {
        this.activeLoanId = '';
        this.auditData = {
            fechaDevolucion: '',
            estadoComponentes: '',
            observaciones: ''
        };
    }

    clearForm(): void {

        this.loanData = this.nuevoPrestamo();

        this.cancel.emit();
    }
}
