import { Component, EventEmitter, Input, Output } from '@angular/core';

import {
    SelectComponent,
    SelectOption
} from '../../atoms/select/select.component';

import { LabelComponent } from '../../atoms/label/label.component';
import { ButtonComponent } from '../../atoms/button/button.component';
import { IconComponent } from '../../atoms/icon/icon.component';

export interface FilterDefinition {
    name: string;
    label: string;
    placeholder?: string;
    options: SelectOption[];
    disabled?: boolean;
}

@Component({
    selector: 'app-filter-group',
    standalone: true,
    imports: [
        SelectComponent,
        LabelComponent,
        ButtonComponent,
        IconComponent
    ],
    templateUrl: './filter-group.component.html',
    styleUrl: './filter-group.component.css'
})
export class FilterGroupComponent {

    @Input() filters: FilterDefinition[] = [];

    @Input() values: Record<string, string> = {};

    @Input() showClearButton: boolean = true;

    @Input() clearButtonText: string = 'Limpiar filtros';

    @Output() valuesChange =
        new EventEmitter<Record<string, string>>();

    onFilterChange(name: string, value: string): void {
        this.values = {
            ...this.values,
            [name]: value
        };

        this.valuesChange.emit(this.values);
    }

    clearFilters(): void {
        this.values = {};
        this.valuesChange.emit(this.values);
    }
}
