import { Component, EventEmitter, Input, Output } from '@angular/core';

export type NotificationType = 'success' | 'error' | 'warning' | 'info';

@Component({
    selector: 'app-notification-toast',
    standalone: true,
    templateUrl: './notification-toast.component.html',
    styleUrl: './notification-toast.component.css'
})
export class NotificationToastComponent {

    @Input() visible = false;
    @Input() type: NotificationType = 'info';
    @Input() title = '';
    @Input() message = '';

    @Output() dismiss = new EventEmitter<void>();

    close(): void {
        this.dismiss.emit();
    }
}
