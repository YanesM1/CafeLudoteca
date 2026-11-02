import { Component, EventEmitter, Input, Output } from '@angular/core';

import { BadgeComponent } from '../../atoms/badge/badge.component';
import { ButtonComponent } from '../../atoms/button/button.component';
import { IconComponent } from '../../atoms/icon/icon.component';

import { SearchBarComponent } from '../../molecules/search-bar/search-bar.component';

import {
    FilterGroupComponent,
    FilterDefinition
} from '../../molecules/filter-group/filter-group.component';

import { LoadingStateComponent } from '../../molecules/loading-state/loading-state.component';

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
    imports: [
        BadgeComponent,
        ButtonComponent,
        IconComponent,
        SearchBarComponent,
        FilterGroupComponent,
        LoadingStateComponent
    ],
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

    filterValues: Record<string, string> = {};

    get filters(): FilterDefinition[] {

        const plans = [
            ...new Set(this.members.map(member => member.plan))
        ].sort();

        return [
            {
                name: 'plan',
                label: 'Plan',
                placeholder: 'Todos los planes',
                options: plans.map(plan => ({
                    value: plan,
                    label: plan
                }))
            },
            {
                name: 'estado',
                label: 'Estado',
                placeholder: 'Todos los estados',
                options: [
                    { value: 'Activa', label: 'Activa' },
                    { value: 'Pendiente', label: 'Pendiente' },
                    { value: 'Inactiva', label: 'Inactiva' },
                    { value: 'Vencida', label: 'Vencida' }
                ]
            }
        ];
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

        if (['pendiente', 'por vencer'].includes(status)) {
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
