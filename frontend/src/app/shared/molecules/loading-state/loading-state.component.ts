import { Component, EventEmitter, Input, Output } from '@angular/core';

export type LoadingStateType = 'loading' | 'empty' | 'error';

@Component({
    selector: 'app-loading-state',
    standalone: true,
    templateUrl: './loading-state.component.html',
    styleUrl: './loading-state.component.css'
})
export class LoadingStateComponent {

    @Input() state: LoadingStateType = 'loading';

    @Input() title = '';

    @Input() message = '';

    @Input() retryLabel = 'Intentar nuevamente';

    @Input() showRetry = false;

    @Output() retry = new EventEmitter<void>();

    onRetry(): void {
        this.retry.emit();
    }
}
