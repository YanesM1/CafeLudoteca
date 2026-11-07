import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-role-selection-page',
  standalone: true,
  imports: [RouterLink],
  template: `
  <main class="roles">
    <header>
      <span>CAFÉ & LUDOTECA</span>
      <h1>Bienvenido</h1>
      <p>Selecciona tu espacio para continuar</p>
    </header>

    <section class="cards">
      <a routerLink="/admin" class="card admin">
        <span class="symbol">♛</span>
        <h2>Administrador</h2>
        <p>Gestiona clientes, membresías, juegos, mesas y empleados.</p>
        <strong>INGRESAR →</strong>
      </a>

      <a routerLink="/empleado" class="card employee">
        <span class="symbol">♙</span>
        <h2>Empleado</h2>
        <p>Atiende clientes, reservas, préstamos y devoluciones.</p>
        <strong>INGRESAR →</strong>
      </a>

      <a routerLink="/inicio" class="card client">
        <span class="symbol">🎲</span>
        <h2>Cliente</h2>
        <p>Explora juegos, consulta disponibilidad y solicita reservas.</p>
        <strong>INGRESAR →</strong>
      </a>
    </section>

    <footer>CAFÉ & LUDOTECA · SISTEMA DE GESTIÓN</footer>
  </main>
  `,
  styles: [`
    :host{display:block;min-height:100vh}
    .roles{min-height:100vh;box-sizing:border-box;padding:65px 24px;background:radial-gradient(ellipse at top,#29213d,#0b1020 65%);color:#fff;display:flex;flex-direction:column;align-items:center;justify-content:center}
    header{text-align:center;margin-bottom:48px}
    header span{color:#d6b65b;font-size:13px;font-weight:800;letter-spacing:5px}
    h1{font-size:clamp(38px,6vw,58px);margin:15px 0 8px}
    header p{color:#aeb5c5;font-size:17px}
    .cards{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:24px;width:100%;max-width:1100px}
    .card{box-sizing:border-box;text-decoration:none;color:white;background:#1b2030;border:1px solid #465267;border-radius:20px;padding:35px 28px;min-height:290px;display:flex;flex-direction:column;transition:.2s}
    .card:hover{transform:translateY(-7px);border-color:#d6b65b;box-shadow:0 15px 40px #0005}
    .symbol{font-size:42px;color:#d6b65b}
    h2{font-size:23px;margin:22px 0 8px}
    .card p{color:#aeb5c5;line-height:1.7;flex:1}
    .card strong{color:#d6b65b;font-size:13px;letter-spacing:1px}
    .employee .symbol{color:#22d3ee}
    .employee strong{color:#22d3ee}
    .client .symbol{color:#bd91ec}
    .client strong{color:#bd91ec}
    footer{margin-top:55px;color:#7e8799;font-size:11px;letter-spacing:3px;text-align:center}
    @media(max-width:750px){.cards{grid-template-columns:1fr;max-width:430px}.card{min-height:220px}.roles{padding:45px 18px}}
  `]
})
export class RoleSelectionPageComponent {}
