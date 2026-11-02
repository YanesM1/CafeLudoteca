import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-role-selection-page',
  standalone: true,
  imports: [RouterLink],
  template: `
    <main class="selection">
      <section class="panel">
        <div class="brand">
          <span class="logo">CL</span>
          <span>Café & Ludoteca</span>
        </div>

        <span class="eyebrow">CAFÉ · JUEGOS · EXPERIENCIAS</span>
        <h1>Bienvenido a Café & Ludoteca</h1>
        <p class="subtitle">Selecciona cómo deseas ingresar a la plataforma.</p>

        <div class="roles">
          <a routerLink="/inicio" class="role-card">
            <span class="role-icon">♙</span>
            <h2>Cliente</h2>
            <p>Explora juegos, consulta el catálogo y realiza reservas.</p>
            <span class="action">Ingresar →</span>
          </a>

          <a routerLink="/empleado/prestamos" class="role-card">
            <span class="role-icon">▤</span>
            <h2>Empleado</h2>
            <p>Gestiona préstamos y consulta las funciones de auditoría.</p>
            <span class="action">Ingresar →</span>
          </a>

          <a routerLink="/admin/membresias" class="role-card">
            <span class="role-icon">⚙</span>
            <h2>Administrador</h2>
            <p>Administra clientes, empleados, socios y membresías.</p>
            <span class="action">Ingresar →</span>
          </a>
        </div>

        <footer>© Café & Ludoteca</footer>
      </section>
    </main>
  `,
  styles: [`
    :host { display:block; min-height:100vh; }
    .selection {
      min-height:100vh;
      display:grid;
      place-items:center;
      padding:35px 20px;
    }
    .panel {
      width:min(1100px,100%);
      padding:clamp(25px,5vw,65px);
      background:var(--cl-surface);
      border:1px solid var(--cl-border-cyan);
      border-radius:22px;
      box-shadow:var(--cl-glow-cyan);
      text-align:center;
    }
    .brand {
      display:flex;
      align-items:center;
      justify-content:center;
      gap:14px;
      margin-bottom:65px;
      font-size:20px;
      font-weight:700;
    }
    .logo {
      display:grid;
      place-items:center;
      width:48px;height:48px;
      color:var(--cl-cyan);
      background:var(--cl-purple);
      border:1px solid var(--cl-cyan);
      border-radius:12px;
    }
    .eyebrow {
      color:var(--cl-cyan);
      font-size:12px;
      font-weight:800;
      letter-spacing:3px;
    }
    h1 { font-size:clamp(28px,4vw,42px); margin:15px 0; }
    .subtitle { color:var(--cl-text-secondary); margin-bottom:45px; }
    .roles {
      display:grid;
      grid-template-columns:repeat(3,minmax(0,1fr));
      gap:22px;
    }
    .role-card {
      display:flex;
      flex-direction:column;
      align-items:center;
      padding:30px 22px;
      text-decoration:none;
      color:var(--cl-text);
      background:var(--cl-bg-secondary);
      border:1px solid var(--cl-border);
      border-radius:16px;
      transition:.2s ease;
    }
    .role-card:hover {
      transform:translateY(-5px);
      border-color:var(--cl-cyan);
      box-shadow:var(--cl-glow-cyan);
    }
    .role-icon { font-size:38px; color:var(--cl-cyan); }
    h2 { margin:17px 0 10px; }
    .role-card p {
      min-height:65px;
      color:var(--cl-text-secondary);
      font-size:14px;
    }
    .action { color:var(--cl-gold); font-weight:700; margin-top:20px; }
    footer { color:var(--cl-text-muted); font-size:12px; margin-top:50px; }
    @media(max-width:750px) {
      .roles { grid-template-columns:1fr; }
      .brand { margin-bottom:40px; }
      .role-card p { min-height:0; }
    }
  `]
})
export class RoleSelectionPageComponent {}
