# Order Service

Microservicio propietario de pedidos, sus líneas y el historial de estados. Utiliza el esquema PostgreSQL `ordering`.

## Responsabilidades

- Crear y consultar pedidos.
- Gestionar detalles del pedido.
- Registrar historial de estados.
- Validar productos activos contra Inventory Service.
- Validar importes y totales antes de persistir.

## Requisitos y configuración

Java 21, PostgreSQL 16+, Inventory Service y un emisor OpenID Connect.

| Variable | Obligatoria | Valor local predeterminado |
|---|---:|---|
| `DB_URL` | No | `jdbc:postgresql://localhost:5432/nexasupplyai` |
| `DB_USER` | No | `postgres` |
| `DB_PASSWORD` | No | `postgres` |
| `JWT_ISSUER_URI` | Producción | `http://localhost:9999` |
| `JWT_JWK_SET_URI` | Producción | `http://localhost:9999/.well-known/jwks.json` |
| `APP_SECURITY_ENABLED` | No | `true` |
| `CORS_ALLOWED_ORIGINS` | No | `http://localhost:4200` |
| `INVENTORY_SERVICE_URL` | No | `http://localhost:8081` |

Inicializa la base con [`../nexasupplyai.sql`](../nexasupplyai.sql). Hibernate valida el esquema al arrancar.

## Ejecución

```bash
export JWT_ISSUER_URI="https://login.microsoftonline.com/<tenant-id>/v2.0"
./mvnw spring-boot:run
```

Puerto: `8082`. Salud pública: `GET /actuator/health`. La API requiere un bearer token. El token y `X-Correlation-ID` se propagan a Inventory Service.

### Docker

```bash
docker build -t nexasupply/order-service:local .
docker run --rm --add-host=host.docker.internal:host-gateway \
  -p 8082:8082 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/nexasupplyai \
  -e DB_USER=postgres \
  -e DB_PASSWORD=postgres \
  -e JWT_ISSUER_URI=https://login.microsoftonline.com/<tenant-id>/v2.0 \
  -e INVENTORY_SERVICE_URL=http://host.docker.internal:8081 \
  nexasupply/order-service:local
```

## API

| Método | Ruta | Uso |
|---|---|---|
| `GET` | `/api/v1/ordenes?estado=PENDING` | Listar; el filtro es opcional |
| `POST` | `/api/v1/ordenes` | Crear pedido |
| `GET/PUT/DELETE` | `/api/v1/ordenes/{id}` | Operaciones por pedido |
| `POST` | `/api/v1/ordenes/{id}/confirmar` | Reservar inventario y confirmar |
| `POST` | `/api/v1/ordenes/{id}/completar` | Consumir reserva y completar |
| `POST` | `/api/v1/ordenes/{id}/cancelar?motivo=...` | Liberar reserva y cancelar |
| `GET` | `/api/v1/detalles-pedido?idPedido={uuid}` | Listar líneas |
| `POST` | `/api/v1/detalles-pedido` | Crear línea y validar el producto |
| `GET/PUT/DELETE` | `/api/v1/detalles-pedido/{id}` | Operaciones por línea |
| `GET` | `/api/v1/historial-estados-pedido?idPedido={uuid}` | Consultar historial |
| `POST` | `/api/v1/historial-estados-pedido` | Registrar cambio |
| `GET/PUT/DELETE` | `/api/v1/historial-estados-pedido/{id}` | Operaciones por evento |

Estados admitidos: `PENDING`, `CONFIRMED`, `PROCESSING`, `COMPLETED` y `CANCELLED`.

El `montoTotal` debe ser exactamente `subtotal + montoImpuesto + montoEnvio - montoDescuento`.

## Pruebas

```bash
./mvnw clean test
./mvnw clean package
```

## Flujo empresarial

Un pedido nace `PENDING` y sus líneas solo son editables en ese estado. Confirmar reserva todas las líneas en Inventory Service; si falla la persistencia local se ejecuta la liberación compensatoria. Completar consume la reserva y cancelar la libera. Todas las transiciones generan historial e Inventory Service hace las llamadas reintentables e idempotentes.
