import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-label',
    standalone: true,
    templateUrl: './label.component.html',
    styleUrl: './label.component.css'
})
export class LabelComponent {

    @Input() text: string = '';

    @Input() forId: string = '';

    @Input() required: boolean = false;

}
