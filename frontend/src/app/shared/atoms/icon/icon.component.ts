import { Component, Input } from '@angular/core';

export type IconName =
    | 'search'
    | 'user'
    | 'cart'
    | 'calendar'
    | 'clock'
    | 'menu'
    | 'close'
    | 'plus'
    | 'minus'
    | 'edit'
    | 'trash'
    | 'check'
    | 'chevron-down'
    | 'chevron-right'
    | 'filter'
    | 'dice'
    | 'table'
    | 'logout';

@Component({
    selector: 'app-icon',
    standalone: true,
    templateUrl: './icon.component.html',
    styleUrl: './icon.component.css'
})
export class IconComponent {

    @Input() name: IconName = 'search';

    @Input() size: number = 20;

    @Input() strokeWidth: number = 1.8;

    @Input() ariaLabel: string = '';

}
