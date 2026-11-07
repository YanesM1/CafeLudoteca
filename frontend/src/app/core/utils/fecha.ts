/**
 * Convierte una fecha recibida del backend a "yyyy-MM-dd".
 * Acepta tanto texto ISO como el formato antiguo en arreglo [2026, 11, 3].
 */
export function fechaISO(valor: unknown): string {
  if (Array.isArray(valor) && valor.length >= 3) {
    const [anio, mes, dia] = valor as number[];
    return `${anio}-${String(mes).padStart(2, '0')}-${String(dia).padStart(2, '0')}`;
  }
  return typeof valor === 'string' ? valor.substring(0, 10) : '';
}

/** Muestra una fecha como dd/MM/yyyy. */
export function fechaCorta(valor: unknown): string {
  const iso = fechaISO(valor);
  if (!iso) return '';
  const [anio, mes, dia] = iso.split('-');
  return `${dia}/${mes}/${anio}`;
}

/** Fecha local de hoy en formato yyyy-MM-dd. */
export function hoyISO(): string {
  const d = new Date();
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
}

/** Suma meses a una fecha "yyyy-MM-dd" (si el día no existe, usa el último del mes). */
export function sumarMeses(fecha: string, meses: number): string {
  if (!fecha) return '';
  const [a, m, d] = fecha.split('-').map(Number);
  const destino = new Date(a, m - 1 + meses, 1);
  const ultimo = new Date(destino.getFullYear(), destino.getMonth() + 1, 0).getDate();
  destino.setDate(Math.min(d, ultimo));
  const mm = String(destino.getMonth() + 1).padStart(2, '0');
  const dd = String(destino.getDate()).padStart(2, '0');
  return `${destino.getFullYear()}-${mm}-${dd}`;
}

/** Formato de pesos colombianos: 35000 -> "$ 35.000". */
export function pesos(valor: number | string): string {
  const n = Number(valor);
  return Number.isFinite(n) ? '$ ' + n.toLocaleString('es-CO', { maximumFractionDigits: 0 }) : '—';
}
