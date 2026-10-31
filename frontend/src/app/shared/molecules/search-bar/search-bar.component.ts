import { Component, EventEmitter, Input, Output } from '@angular/core';

import { InputComponent } from '../../atoms/input/input.component';
import { ButtonComponent } from '../../atoms/button/button.component';
import { IconComponent } from '../../atoms/icon/icon.component';

@Component({
    selector: 'app-search-bar',
    standalone: true,
    imports: [
        InputComponent,
        ButtonComponent,
        IconComponent
    ],
    templateUrl: './search-bar.component.html',
    styleUrl: './search-bar.component.css'
})
export class SearchBarComponent {

    @Input() placeholder: string = 'Buscar...';

    @Input() buttonText: string = 'Buscar';

    @Input() value: string = '';

    @Input() disabled: boolean = false;

    @Input() name: string = 'search';

    @Output() valueChange = new EventEmitter<string>();

    @Output() search = new EventEmitter<string>();

    onValueChange(newValue: string): void {
        this.value = newValue;
        this.valueChange.emit(newValue);
    }

    onSearch(): void {
        const searchValue = this.value.trim();

        if (searchValue.length > 0 && !this.disabled) {
            this.search.emit(searchValue);
        }
    }
}
