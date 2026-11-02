import { Component, EventEmitter, Input, Output } from '@angular/core';

type InputType =
    | 'text'
    | 'email'
    | 'password'
    | 'number'
    | 'search'
    | 'date'
    | 'time'
    | 'tel';

@Component({
    selector: 'app-input',
    standalone: true,
    templateUrl: './input.component.html',
    styleUrl: './input.component.css'
})
export class InputComponent {

    @Input() type: InputType = 'text';
    @Input() name: string = '';
    @Input() inputId: string = '';
    @Input() placeholder: string = '';
    @Input() value: string = '';
    @Input() disabled: boolean = false;
    @Input() required: boolean = false;
    @Input() ariaLabel: string = '';
    @Input() min: string = '';
    @Input() max: string = '';

    @Output() valueChange = new EventEmitter<string>();

    onInput(event: Event): void {
        const input = event.target as HTMLInputElement;
        this.value = input.value;
        this.valueChange.emit(this.value);
    }
}
