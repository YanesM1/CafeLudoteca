import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
    selector: 'app-textarea',
    standalone: true,
    templateUrl: './textarea.component.html',
    styleUrl: './textarea.component.css'
})
export class TextareaComponent {

    @Input() name: string = '';
    @Input() inputId: string = '';
    @Input() placeholder: string = '';
    @Input() value: string = '';
    @Input() rows: number = 4;
    @Input() disabled: boolean = false;
    @Input() required: boolean = false;

    @Output() valueChange = new EventEmitter<string>();

    onInput(event: Event): void {
        const textarea = event.target as HTMLTextAreaElement;
        this.value = textarea.value;
        this.valueChange.emit(this.value);
    }
}
