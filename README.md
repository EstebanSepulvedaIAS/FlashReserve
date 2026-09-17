# FlashReserve
Application for Company Flash Reserve

# Base de datos
El proyecto utiliza MongoDB como base de datos. La configuración necesaria para ejeuctarse mediante Docker se encuentra en la carpeta db/

## Comandos para ejecutarse

## Crear una red compartida en Docker

1. Ejecutar el comando docker network create flashreserve-network desde la raíz o donde lo desees

## Base de datos
1. Deberá ejecutar si así lo desea desde la raíz del proyecto: docker compose -f db/docker-compose.yml up -d para iniciar el contenedor 
2. Ejecutar docker compose logs -f mongo-init para validar que haya subido correctamente la base de datos y que al final muestre un mensaje que diga "Base de datos de FlashReserve lista para usar"
3. Ejecutar docker exec -it flashreserve-mongo mongosh flashreserve_db para ingresar a la base de datos
4. Ejecutar db.skus.find(); para validar que haya guardado correctamente la información de prueba
5. Ejecutar db.reservations.getIndexes(); para validar que se hayan creado los índices

## Backend
1. Deberá ejecutar si así lo desea desde la raíz del proyecto: docker compose -f backend/docker-compose.yaml up --build -d api para iniciar el contenedor

# Prueba
## Crear reserva
curl -X POST http://localhost:8080/api/v1/reservations \
-H "Content-Type: application/json" \
-d '{"idempotencyKey": "req-12345", "customerId": "user-99", "sku": "IPHONE-15", "quantity": 2}'

## Consultar reserva
curl http://localhost:8080/api/v1/reservations/1111-2222-3333

## Simular webhook del proveedor
curl -X POST http://localhost:8080/api/v1/webhooks/provider \
-H "Content-Type: application/json" \
-H "X-API-KEY: secreto_seguro_123" \
-d '{
"eventId": "evt-001",
"reservationId": "1111-2222-3333",
"sequence": 1,
"status": "CONFIRMED",
"occurredAt": "2026-09-16T20:24:38Z"
}'