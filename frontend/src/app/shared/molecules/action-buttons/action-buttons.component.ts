import { Component, EventEmitter, Input, Output } from '@angular/core';

import { ButtonComponent } from '../../atoms/button/button.component';
import {
    IconComponent,
    IconName
} from '../../atoms/icon/icon.component';

export interface ActionButton {
    id: string;
    label: string;
    variant?: 'primary' | 'secondary' | 'gold' | 'danger' | 'ghost';
    icon?: IconName;
    type?: 'button' | 'submit' | 'reset';
    disabled?: boolean;
}

type ButtonAlignment = 'start' | 'center' | 'end' | 'between';

@Component({
    selector: 'app-action-buttons',
    standalone: true,
    imports: [
        ButtonComponent,
        IconComponent
    ],
    templateUrl: './action-buttons.component.html',
    styleUrl: './action-buttons.component.css'
})
export class ActionButtonsComponent {

    @Input() actions: ActionButton[] = [];

    @Input() alignment: ButtonAlignment = 'end';

    @Input() fullWidth: boolean = false;

    @Output() actionSelected = new EventEmitter<string>();

    onAction(action: ActionButton): void {
        if (!action.disabled) {
            this.actionSelected.emit(action.id);
        }
    }
}
