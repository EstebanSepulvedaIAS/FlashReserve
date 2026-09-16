# FlashReserve
Application for Company Flash Reserve

# Base de datos
El proyecto utiliza MongoDB como base de datos. La configuración necesaria para ejeuctarse mediante Docker se encuentra en la carpeta db/

## Comandos para ejecutarse

1. Deberá ejecutar si así lo desea desde la raíz del proyecto: docker compose -f db/docker-compose.yml up -d para iniciar el contenedor 
2. Ejecutar docker compose logs -f mongo-init para validar que haya subido correctamente la base de datos y que al final muestre un mensaje que diga "Base de datos de FlashReserve lista para usar"
3. Ejecutar docker exec -it flashreserve-mongo mongosh flashreserve_db para ingresar a la base de datos
4. Ejecutar db.skus.find(); para validar que haya guardado correctamente la información de prueba
5. Ejecutar db.reservations.getIndexes(); para validar que se hayan creado los índices