import { Component, EventEmitter, Input, Output } from '@angular/core';

export interface SelectOption {
    value: string;
    label: string;
    disabled?: boolean;
}

@Component({
    selector: 'app-select',
    standalone: true,
    templateUrl: './select.component.html',
    styleUrl: './select.component.css'
})
export class SelectComponent {

    @Input() name: string = '';

    @Input() inputId: string = '';

    @Input() placeholder: string = 'Selecciona una opción';

    @Input() value: string = '';

    @Input() options: SelectOption[] = [];

    @Input() disabled: boolean = false;

    @Input() required: boolean = false;

    @Input() ariaLabel: string = '';

    @Output() valueChange = new EventEmitter<string>();

    onChange(event: Event): void {
        const select = event.target as HTMLSelectElement;

        this.value = select.value;
        this.valueChange.emit(this.value);
    }
}
