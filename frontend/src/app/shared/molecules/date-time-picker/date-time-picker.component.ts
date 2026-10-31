import { Component, EventEmitter, Input, Output } from '@angular/core';

import { LabelComponent } from '../../atoms/label/label.component';
import { InputComponent } from '../../atoms/input/input.component';

@Component({
    selector: 'app-date-time-picker',
    standalone: true,
    imports: [
        LabelComponent,
        InputComponent
    ],
    templateUrl: './date-time-picker.component.html',
    styleUrl: './date-time-picker.component.css'
})
export class DateTimePickerComponent {

    @Input() date: string = '';
    @Input() time: string = '';

    @Input() dateLabel: string = 'Fecha';
    @Input() timeLabel: string = 'Hora';

    @Input() dateInputId: string = 'reservation-date';
    @Input() timeInputId: string = 'reservation-time';

    @Input() minDate: string = '';
    @Input() maxDate: string = '';

    @Input() minTime: string = '';
    @Input() maxTime: string = '';

    @Input() required: boolean = false;
    @Input() disabled: boolean = false;

    @Output() dateChange = new EventEmitter<string>();
    @Output() timeChange = new EventEmitter<string>();

    onDateChange(value: string): void {
        this.date = value;
        this.dateChange.emit(value);
    }

    onTimeChange(value: string): void {
        this.time = value;
        this.timeChange.emit(value);
    }
}
