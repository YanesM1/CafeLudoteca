import { Component, EventEmitter, Input, Output } from '@angular/core';

import { LabelComponent } from '../../atoms/label/label.component';
import { InputComponent } from '../../atoms/input/input.component';

type FormInputType =
    | 'text'
    | 'email'
    | 'password'
    | 'number'
    | 'search'
    | 'date'
    | 'time'
    | 'tel';

@Component({
    selector: 'app-form-field',
    standalone: true,
    imports: [
        LabelComponent,
        InputComponent
    ],
    templateUrl: './form-field.component.html',
    styleUrl: './form-field.component.css'
})
export class FormFieldComponent {

    @Input() label: string = '';

    @Input() inputId: string = '';

    @Input() name: string = '';

    @Input() type: FormInputType = 'text';

    @Input() placeholder: string = '';

    @Input() value: string = '';

    @Input() required: boolean = false;

    @Input() disabled: boolean = false;

    @Input() errorMessage: string = '';

    @Output() valueChange = new EventEmitter<string>();

    onValueChange(newValue: string): void {
        this.value = newValue;
        this.valueChange.emit(newValue);
    }
}
