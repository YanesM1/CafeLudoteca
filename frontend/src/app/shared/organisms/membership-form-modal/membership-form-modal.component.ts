import {
    Component,
    EventEmitter,
    Input,
    Output,
    OnChanges,
    SimpleChanges
} from '@angular/core';
import { FormsModule } from '@angular/forms';

import { MembershipTableRow } from '../membership-table/membership-table.component';
import { PlanMembresia } from '../../../core/services/membresia.service';
import { Opcion } from '../../../core/services/opciones.service';
import { fechaCorta, hoyISO, pesos, sumarMeses } from '../../../core/utils/fecha';

/**
 * Formulario de membresía: el número se asigna solo, la tarifa sale del plan
 * y la fecha de fin se calcula con la duración elegida.
 */
@Component({
    selector: 'app-membership-form-modal',
    standalone: true,
    imports: [FormsModule],
    templateUrl: './membership-form-modal.component.html',
    styleUrl: './membership-form-modal.component.css'
})
export class MembershipFormModalComponent implements OnChanges {

    @Input() visible = false;
    @Input() mode: 'create' | 'edit' = 'create';
    @Input() member: MembershipTableRow | null = null;
    @Input() saving = false;
    @Input() socioOptions: Opcion[] = [];
    @Input() planes: PlanMembresia[] = [];

    @Output() save = new EventEmitter<MembershipTableRow>();
    @Output() close = new EventEmitter<void>();

    readonly duraciones = [1, 3, 6, 12];

    // Estados definidos en la propuesta: Activa, Suspendida o Vencida
    private readonly baseStatusOptions = [
        { value: 'Activa', label: 'Activa' },
        { value: 'Suspendida', label: 'Suspendida' },
        { value: 'Vencida', label: 'Vencida' }
    ];

    form: MembershipTableRow = this.emptyForm();
    /** Meses de duración; 0 = mantener la fecha de fin actual (solo al editar). */
    duracion = 1;

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['visible'] || changes['member']) {
            this.form = this.member ? { ...this.member } : this.emptyForm();
            this.duracion = this.member ? 0 : 1;
        }
    }

    private emptyForm(): MembershipTableRow {
        return {
            identificador: '',
            documento: '',
            nombre: '',
            plan: '',
            tarifa: '',
            fechaInicio: hoyISO(),
            fechaFin: '',
            estado: 'Activa'
        };
    }

    /** Incluye el socio actual al editar aunque no esté en la lista. */
    get opcionesSocio(): Opcion[] {
        const doc = this.form.documento;
        return doc && !this.socioOptions.some(o => o.value === doc)
            ? [{ value: doc, label: `${this.form.nombre || 'Socio'} · ${doc}` }, ...this.socioOptions]
            : this.socioOptions;
    }

    /** Planes con su tarifa; un plan antiguo que no está en la lista se muestra para poder cambiarlo. */
    get opcionesPlan(): PlanMembresia[] {
        const actual = this.form.plan;
        return actual && !this.planes.some(p => p.plan === actual)
            ? [...this.planes, { plan: actual, tarifa: Number(this.form.tarifa) }]
            : this.planes;
    }

    get statusOptions() {
        const actual = this.form?.estado;
        return actual && !this.baseStatusOptions.some(o => o.value === actual)
            ? [...this.baseStatusOptions, { value: actual, label: actual }]
            : this.baseStatusOptions;
    }

    get tarifa(): number | null {
        // Al editar sin cambiar el plan se conserva la tarifa pactada al contratar
        if (this.mode === 'edit' && this.member && this.form.plan === this.member.plan) {
            return Number(this.member.tarifa);
        }
        const plan = this.planes.find(p => p.plan === this.form.plan);
        return plan ? plan.tarifa : null;
    }

    get tarifaTexto(): string {
        return this.tarifa === null ? 'Elige un plan' : pesos(this.tarifa) + ' al mes';
    }

    get fechaFin(): string {
        return this.duracion > 0
            ? sumarMeses(this.form.fechaInicio, this.duracion)
            : this.form.fechaFin;
    }

    get fechaFinTexto(): string {
        return this.fechaFin ? fechaCorta(this.fechaFin) : '—';
    }

    get canSave(): boolean {
        return !!String(this.form.documento ?? '').trim()
            && this.tarifa !== null
            && !!this.form.fechaInicio
            && !!this.fechaFin
            && this.fechaFin > this.form.fechaInicio
            && !!this.form.estado;
    }

    onSave(): void {
        if (!this.canSave || this.saving) return;
        this.save.emit({
            ...this.form,
            documento: String(this.form.documento).trim(),
            tarifa: String(this.tarifa),
            fechaFin: this.fechaFin
        });
    }

    onClose(): void {
        this.close.emit();
    }
}
