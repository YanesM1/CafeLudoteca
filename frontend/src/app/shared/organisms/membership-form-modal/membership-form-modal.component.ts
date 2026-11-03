import {
    Component,
    EventEmitter,
    Input,
    Output,
    OnChanges,
    SimpleChanges
} from '@angular/core';

import { InputComponent } from '../../atoms/input/input.component';
import { LabelComponent } from '../../atoms/label/label.component';

import {
    SelectComponent,
    SelectOption
} from '../../atoms/select/select.component';

import { ButtonComponent } from '../../atoms/button/button.component';

import {
    MembershipTableRow
} from '../membership-table/membership-table.component';

@Component({
    selector: 'app-membership-form-modal',
    standalone: true,
    imports: [
        InputComponent,
        LabelComponent,
        SelectComponent,
        ButtonComponent
    ],
    templateUrl: './membership-form-modal.component.html',
    styleUrl: './membership-form-modal.component.css'
})
export class MembershipFormModalComponent implements OnChanges {

    @Input() visible = false;
    @Input() mode: 'create' | 'edit' = 'create';
    @Input() member: MembershipTableRow | null = null;
    @Input() saving = false;

    @Input() membershipOptions: SelectOption[] = [];

    @Output() save = new EventEmitter<MembershipTableRow>();
    @Output() close = new EventEmitter<void>();

    statusOptions: SelectOption[] = [
        { value: 'Activa', label: 'Activa' },
        { value: 'Pendiente', label: 'Pendiente' },
        { value: 'Inactiva', label: 'Inactiva' },
        { value: 'Vencida', label: 'Vencida' }
    ];

    form: MembershipTableRow = this.emptyForm();

    ngOnChanges(changes: SimpleChanges): void {

        if (changes['visible'] || changes['member']) {
            this.loadForm();
        }
    }

    private emptyForm(): MembershipTableRow {

        return {
            identificador: '',
            documento: '',
            nombre: '',
            plan: '',
            tarifa: '',
            fechaInicio: '',
            fechaFin: '',
            estado: 'Activa'
        };
    }

    private loadForm(): void {

        this.form = this.member
            ? { ...this.member }
            : this.emptyForm();
    }

    get canSave(): boolean {

        const tarifa = Number(this.form.tarifa);

        return !!this.form.identificador.trim()
            && Number(this.form.identificador) > 0
            && !!this.form.documento.trim()
            && !!this.form.nombre.trim()
            && !!this.form.plan
            && !!this.form.fechaInicio
            && !!this.form.fechaFin
            && this.form.fechaFin >= this.form.fechaInicio
            && Number.isFinite(tarifa)
            && tarifa > 0
            && !!this.form.estado;
    }

    onSave(): void {

        if (!this.canSave || this.saving) {
            return;
        }

        this.save.emit({
            ...this.form,
            documento: this.form.documento.trim(),
            nombre: this.form.nombre.trim(),
            tarifa: String(Number(this.form.tarifa))
        });
    }

    onClose(): void {
        this.close.emit();
    }
}
