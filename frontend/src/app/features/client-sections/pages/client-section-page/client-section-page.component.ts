import { Component } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
    selector: 'app-client-section-page',
    standalone: true,
    imports: [RouterLink],
    templateUrl: './client-section-page.component.html',
    styleUrl: './client-section-page.component.css'
})
export class ClientSectionPageComponent {

    section = '';
    title = '';
    eyebrow = '';
    description = '';

    constructor(private route: ActivatedRoute) {

        this.route.data.subscribe(data => {
            this.section = data['section'] ?? '';
            this.title = data['title'] ?? '';
            this.eyebrow = data['eyebrow'] ?? 'CAFÉ & LUDECA';
            this.description = data['description'] ?? '';
        });
    }
}
