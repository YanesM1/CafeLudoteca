import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

export interface MembershipTableRow {
    identificador: string;
    documento: string;
    nombre: string;
    plan: string;
    tarifa: string;
    fechaInicio: string;
    fechaFin: string;
    estado: string;
}

@Component({
    selector: 'app-membership-table',
    standalone: true,
    imports: [FormsModule],
    templateUrl: './membership-table.component.html',
    styleUrl: './membership-table.component.css'
})
export class MembershipTableComponent {

    @Input() members: MembershipTableRow[] = [];
    @Input() loading = false;
    @Input() errorMessage = '';

    @Output() createMember = new EventEmitter<void>();
    @Output() editMember = new EventEmitter<MembershipTableRow>();
    @Output() deleteMember = new EventEmitter<MembershipTableRow>();
    @Output() retry = new EventEmitter<void>();

    searchTerm = '';

    filterValues: Record<string, string> = { plan: '', estado: '' };

    get planes(): string[] {
        return [...new Set(this.members.map(member => member.plan).filter(Boolean))].sort();
    }

    /** Estados de la propuesta más cualquier otro valor ya guardado. */
    get estados(): string[] {
        const base = ['Activa', 'Suspendida', 'Vencida'];
        const extra = this.members.map(member => member.estado)
            .filter(estado => estado && !base.includes(estado));
        return [...base, ...new Set(extra)];
    }

    limpiarFiltros(): void {
        this.searchTerm = '';
        this.filterValues = { plan: '', estado: '' };
    }

    get filteredMembers(): MembershipTableRow[] {

        const term = this.searchTerm.trim().toLowerCase();

        return this.members.filter(member => {

            const matchesSearch =
                member.nombre.toLowerCase().includes(term) ||
                member.documento.toLowerCase().includes(term) ||
                member.identificador.toLowerCase().includes(term) ||
                member.plan.toLowerCase().includes(term);

            const matchesPlan =
                !this.filterValues['plan'] ||
                member.plan === this.filterValues['plan'];

            const matchesStatus =
                !this.filterValues['estado'] ||
                member.estado.toLowerCase() ===
                    this.filterValues['estado'].toLowerCase();

            return matchesSearch && matchesPlan && matchesStatus;
        });
    }

    formatCurrency(value: string): string {

        const amount = Number(value);

        if (!Number.isFinite(amount)) {
            return value;
        }

        return new Intl.NumberFormat('es-CO', {
            style: 'currency',
            currency: 'COP',
            maximumFractionDigits: 0
        }).format(amount);
    }

    formatDate(value: string): string {

        if (!value) {
            return '';
        }

        const date = new Date(`${value}T00:00:00`);

        if (Number.isNaN(date.getTime())) {
            return value;
        }

        return new Intl.DateTimeFormat('es-CO', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        }).format(date);
    }

    statusVariant(
        estado: string
    ): 'success' | 'warning' | 'danger' | 'info' {

        const status = estado.toLowerCase();

        if (['activo', 'activa', 'vigente'].includes(status)) {
            return 'success';
        }

        if (['pendiente', 'por vencer', 'suspendida', 'suspendido'].includes(status)) {
            return 'warning';
        }

        if (['inactivo', 'inactiva', 'vencido', 'vencida'].includes(status)) {
            return 'danger';
        }

        return 'info';
    }

    onSearch(value: string): void {
        this.searchTerm = value;
    }

    onCreate(): void {
        this.createMember.emit();
    }

    onEdit(member: MembershipTableRow): void {
        this.editMember.emit(member);
    }

    onDelete(member: MembershipTableRow): void {
        this.deleteMember.emit(member);
    }

    onRetry(): void {
        this.retry.emit();
    }
}
