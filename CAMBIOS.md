# Cambios de la revisión de código

No se modificó la estructura de la base de datos. Las consultas usan los nombres reales
de tablas y columnas de `proyecto_db1` (verificados con `information_schema.COLUMNS`).

## Backend (Java / JAX-RS)

**Respuestas y errores**
- `config/JacksonConfig`: las fechas viajan como `"2026-11-03"` (antes `[2026,11,3]`) y se ignoran campos desconocidos.
- `config/GlobalExceptionMapper`: todo error responde JSON `{"mensaje": "..."}`.
  Registro duplicado o con datos relacionados → 409; error de base de datos → 500; validación → 400.
- `exceptions/BusinessException` y `services/Validaciones`: validaciones con mensajes claros.
- Los 15 controladores devuelven el mismo formato de error (antes 12 devolvían texto plano).
- Los repositorios ya no ocultan errores SQL con `printStackTrace()`.

**Correcciones**
- Nombres de columnas que no existían en la base (causaban "Ocurrió un error al acceder a la base de datos"):
  `SOCIO`, `CLIENTE_FRECUENTE` y `RESERVA` usaban `CLIENTE_documento` (real: `documento_cliente`);
  `RESERVA` usaba `MESA_id_mesa` (real: `id_mesa`); `RESERVA_JUEGO` usaba `JUEGO_MESA_num_serie`,
  `RESERVA_id_reserva` y `RESERVA_documento` (reales: `num_serie`, `id_reserva`, `documento_cliente`).
  `MEMBRESIA` usaba `SOCIO_documento` (real: `documento_cliente`).
- Se compararon todas las consultas SQL del backend contra las 17 tablas reales: ya no queda ningún nombre de columna inexistente.
- `Socio.equals()` comparaba el objeto consigo mismo.
- `ElementoJuego` y `Expansion` comparaban `Integer` con `==`.
- `JuegoMesaService`: ya no lanza NullPointerException si falta un número.
- `ClienteService`: valida documento, nombre, apellido y fecha de nacimiento; avisa si el documento ya existe.
- `MesaService`: el número de mesa debe ser numérico (las reservas lo guardan como número).

**Nuevos endpoints de consulta**

| Endpoint | Uso |
|---|---|
| `GET /api/reservas?fecha=yyyy-MM-dd` | Todas las reservas (o las de un día), con sus juegos |
| `GET /api/socio` | Todos los socios |
| `GET /api/clienteFrecuente` | Todos los clientes frecuentes |
| `GET /api/expansion?nombre=` | Todas las expansiones o búsqueda parcial |
| `GET /api/categoria?categoria=` | Todas las categorías o búsqueda parcial |
| `GET /api/elementoJuego?numSerie=` | Todos los elementos o los de un juego |
| `GET /api/unidadJuego?numSerie=` | Todas las unidades o las de un juego |
| `GET /api/telefono/buscar?documento=` | Sin documento devuelve todos los teléfonos |

Las respuestas de reserva incluyen ahora la lista `juegos`.

**Reglas de negocio**
- Reservas: no se permite reservar una mesa ya ocupada en la misma fecha y hora (supuesto 5),
  ni fechas pasadas; se verifica que existan el cliente, la mesa y los juegos.
- Préstamos: solo los socios pueden pedir préstamos externos y deben tener membresía activa
  y vigente (RN-02, RN-07). Mensaje claro si la unidad no está disponible (RN-01).
- Devolución: la unidad queda `DISPONIBLE` (completo), `FALTANTES` (incompleto) o
  `MANTENIMIENTO` (dañado). No se puede devolver dos veces el mismo préstamo.
- Unidades: estados válidos `DISPONIBLE`, `EN_MESA`, `PRESTADO`, `MANTENIMIENTO`, `FALTANTES`.
  El estado `PRESTADO` solo lo cambian los módulos de préstamo y devolución.

**Configuración**
- Se quitó HikariCP del `pom.xml` (no se usaba; la conexión va por el datasource JNDI de WildFly).

## Frontend (Angular)

- `core/utils/http-error.ts` y `core/utils/fecha.ts`: manejo único de errores y fechas.
- **Clientes**: el modelo usaba `documento` y el backend `documentoCliente`; crear, editar y eliminar no funcionaban.
- **Reservas**: cada juego se marca o desmarca (antes se reservaba el catálogo completo);
  llegan los juegos elegidos en el catálogo; se muestra el número real de reserva;
  los errores de validación ya no bloquean el formulario.
- **Reservas (empleado)**: nuevo listado para consultar por fecha o cliente y cancelar reservas.
- **Préstamos (empleado)**: una sola página con registrar préstamo, registrar devolución e historial
  (antes "Auditoría" repetía la misma página). El historial muestra el estado con etiqueta de color.
- **Préstamos**: el formulario se limpia al registrar; se quitó el selector de membresía que no hacía nada.
- **Inventario**: expansiones, categorías, elementos y unidades se listan al entrar y permiten búsqueda parcial.
- **Socios, clientes frecuentes y membresías**: listan todo al entrar y muestran el nombre del cliente.
- **Juegos**: las validaciones coinciden con el backend (reglamento obligatorio, edad mínima ≥ 1).
- **Mesas**: el número de mesa debe ser numérico.
- **Catálogo**: filtro por categoría y regreso a la zona correcta (cliente o empleado).
- Corrección del texto "CAFÉ & LUDECA" → "CAFÉ & LUDOTECA".

## Diseño unificado de las vistas

- **Estilo común** (`src/styles.css`, sección 12): todas las páginas usan las mismas clases `cl-*`:
  encabezado (etiqueta, título, descripción, contador y botón "+ Nuevo ..."), tarjeta con barra de búsqueda
  ("Buscar" dorado y "Ver todos" con borde), tabla, botones (`btn--gold`, `btn--ghost`, `btn--danger`, `btn--sm`),
  mensajes y ventanas emergentes para crear o editar.
- **Etiquetas iguales en todas las páginas**: "Administración · Sección", "Empleado · Sección", "Cliente · Sección".
- **Barra superior**: el rol actual y "Cambiar de rol" aparecen juntos, una sola vez, a la derecha.
- **Cliente**: menú con Inicio, Catálogo y Reservas. Se quitaron Negociaciones, Contacto y Carta (páginas vacías;
  sus direcciones llevan a Inicio). Inicio ya no repite las tarjetas del encabezado: abajo tiene una sección
  "Sobre nosotros" con información tomada de la propuesta del proyecto (local de juegos de mesa en el centro de
  Bogotá, mesas temáticas con reserva, membresía de socios y préstamos a domicilio para socios).
- **Catálogo**: búsqueda y filtros en una sola tarjeta.
- **Reservas**: el documento del cliente es el paso 1 del formulario (antes era una tarjeta sin número).
- **Empleado**: menú con Catálogo, Reservas y Préstamos (Auditoría se unió a Préstamos).
- **Administrador**: menú de 5 opciones por tema; las páginas relacionadas aparecen como pestañas debajo.
- En celular las tablas se desplazan de lado sin partir códigos ni fechas.

**Direcciones del administrador** (las antiguas redirigen a las nuevas):

| Menú | Pestañas | Rutas |
|---|---|---|
| Clientes | Clientes, Clientes frecuentes, Socios, Teléfonos | `/admin/clientes/listado`, `/frecuentes`, `/socios`, `/telefonos` |
| Membresías | Membresías, Beneficios | `/admin/membresias/listado`, `/beneficios` |
| Juegos | Juegos, Unidades, Elementos, Expansiones, Categorías | `/admin/juegos/catalogo`, `/unidades`, `/elementos`, `/expansiones`, `/categorias` |
| Mesas | — | `/admin/mesas` |
| Empleados | — | `/admin/empleados` |

## Procesos revisados (sin cambios en MySQL)

**Membresías**
- Planes de la propuesta con tarifa mensual fija, asignada automáticamente según el plan:

  | Plan | Tarifa mensual |
  |---|---|
  | Premium | $ 35.000 |
  | Alta | $ 25.000 |
  | Media | $ 18.000 |
  | Básica | $ 12.000 |
  | Baja | $ 8.000 |

  Las tarifas están en `services/PlanesMembresia.java` y se consultan con `GET /api/membresia/planes`.
- La tarifa es el precio pactado al contratar: al editar una membresía se conserva, y solo cambia si cambia
  el plan. Así, si un plan sube de precio, las membresías existentes mantienen el suyo (no es una dependencia
  transitiva plan → tarifa, ver 3FN de MEMBRESIA).
- El número de membresía se asigna solo (siguiente número disponible).
- Se elige el socio de una lista, el plan de una lista y la duración (1, 3, 6 o 12 meses); la fecha de fin se calcula.
- Un socio solo puede tener una membresía Activa vigente a la vez.
- Al consultar membresías, las Activas cuya fecha de fin ya pasó se marcan como Vencidas.
- Solo se aceptan los planes Premium, Alta, Media, Básica y Baja, y los estados Activa, Suspendida y Vencida.

**Beneficios**: el número se asigna solo; la membresía se elige de una lista y hay sugerencias
tomadas de la propuesta (descuento, más préstamos simultáneos, prioridad en reservas).

**Socios**: se elige el cliente de una lista (solo los que aún no son socios) y, en el mismo paso,
se puede crear su primera membresía. La tabla muestra la membresía actual de cada socio.

**Clientes frecuentes y teléfonos**: cliente desde una lista; nivel de fidelidad Bronce, Plata u Oro;
el teléfono debe tener entre 7 y 15 dígitos. Al crear un cliente se puede registrar su teléfono.

**Juegos e inventario**
- Al crear un juego se indican cuántas unidades físicas llegan y se registran automáticamente.
- La tabla de juegos muestra cuántas unidades hay disponibles.
- Unidades, elementos, expansiones y categorías eligen el juego de una lista.
- El código de unidad se genera solo si se deja vacío, con el formato `UJ-CATAN-001`.
- Al crear una unidad solo se permite Disponible, En mantenimiento o Con faltantes.
- El número de categoría se asigna solo dentro de cada juego, y no se repite una categoría en el mismo juego.

**Mesas**: se sugiere el siguiente número de mesa; temáticas y ubicaciones ya usadas aparecen como sugerencia.

**Préstamos**
- Un préstamo puede incluir varias unidades (supuesto 3); se verifican y marcan todas en la misma transacción.
- Al elegir el cliente se muestra si es socio y si su membresía está activa, y se bloquea el registro si no
  puede pedir ese tipo de préstamo.
- Préstamo interno: la devolución es el mismo día. La fecha de salida y la de devolución real vienen con el día de hoy.
- El historial y la devolución muestran los nombres de los juegos.

**Reservas**: el paso 4 muestra solo los juegos elegidos y permite agregar otros desde una lista con los
juegos que tienen unidades disponibles. La lista del empleado muestra nombres de clientes y juegos.

## Ajustes finales

**Préstamos y estados de las unidades (supuesto 4 de la propuesta)**
- Préstamo interno → la unidad queda "En uso en mesa" (`EN_MESA`); externo → "Prestado a domicilio" (`PRESTADO`).
  Antes ambos quedaban como Prestado y el estado En mesa no se usaba.
- Corrección: un préstamo se consideraba activo mirando solo el estado de la unidad. Si un juego se devolvía
  y se volvía a prestar, el préstamo viejo reaparecía como activo y su devolución liberaba la unidad del préstamo
  nuevo. Ahora un préstamo está activo solo si no se ha registrado su devolución y es el más reciente de su unidad.
- Las observaciones de la devolución se recortan al tamaño real de la columna `estado_componentes`.
- El tipo de préstamo se guarda como "Interno" / "Externo" (igual que los datos de prueba del equipo).
- Una unidad en un préstamo sin devolver no se puede editar a otro estado ni eliminar.

**Validaciones y mensajes**
- Errores de MySQL con mensaje claro: registro duplicado, no se puede eliminar porque tiene información
  relacionada, referencia a un registro que no existe, dato obligatorio faltante, texto demasiado largo.
- Reservas: no se puede reservar hoy a una hora que ya pasó.
- Expansiones: descripción y contenido adicional son obligatorios (el backend ya los exigía; el formulario no).
- Expansiones y elementos: mensajes específicos en vez de "Datos inválidos".
- Empleados: el backend valida el formato del correo.
- Los campos de texto tienen el largo máximo de su columna (código 30, observaciones 500, categoría 50, etc.).

**Vistas**
- Reservas del empleado: el cliente se elige de una lista.
- Expansiones: muestra el nombre del juego.
- Textos de la selección de rol y del catálogo del empleado alineados con los menús.

## Recomendado en la base de datos (opcional, requiere autorización del equipo)

No es necesario: el backend ya valida esta regla. Para reforzar desde MySQL la regla de "una mesa no puede tener dos reservas a la misma hora":

```sql
ALTER TABLE RESERVA
  ADD CONSTRAINT uq_reserva_mesa_horario UNIQUE (id_mesa, fecha, horario);
```
