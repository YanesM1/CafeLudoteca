import { Component, Input } from '@angular/core';

type ButtonVariant =
    | 'primary'
    | 'secondary'
    | 'gold'
    | 'danger'
    | 'ghost';

type ButtonType = 'button' | 'submit' | 'reset';

@Component({
    selector: 'app-button',
    standalone: true,
    templateUrl: './button.component.html',
    styleUrl: './button.component.css'
})
export class ButtonComponent {

    @Input() variant: ButtonVariant = 'primary';

    @Input() type: ButtonType = 'button';

    @Input() disabled: boolean = false;

}
