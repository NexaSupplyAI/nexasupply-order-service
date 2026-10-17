# Order Service

API propietaria del esquema `ordering`, puerto `8082`. Solo guarda UUID de producto/almacén y consume Inventory Service por REST tipado.

Flujo: pedido `PENDING` → líneas validadas contra Inventory → confirmar/reservar → completar/consumir, o cancelar/liberar. Si falla la persistencia al confirmar, intenta compensar la reserva. El historial es de solo lectura y se genera desde las transiciones del dominio.

Variables adicionales: `INVENTORY_SERVICE_URL`, `HTTP_CONNECT_TIMEOUT`, `HTTP_READ_TIMEOUT`, `INTER_SERVICE_AUTH_MODE` y, para Managed Identity, `AZURE_INTER_SERVICE_SCOPE`.

```bash
export SPRING_PROFILES_ACTIVE=local DB_URL=jdbc:postgresql://localhost:5432/nexastock DB_USER=postgres DB_PASSWORD='<local>'
./mvnw clean test
```

El perfil `azure` usa las mismas variables PostgreSQL/Entra descritas en el README raíz. OpenAPI: `/v3/api-docs`; health: `/actuator/health`.
