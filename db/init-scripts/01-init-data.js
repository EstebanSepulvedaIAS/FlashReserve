print("Inicio de configuración de Mongo");

try{
    rs.initiate({
        _id: "rs0",
        members: [{ _id: 0, host: "mongodb:27017"}]
    });
    print("Replica set configurado");
}catch (e){
    print("Replica set ya estaba iniciado o hubo un error: " + e);
}
print("Esperando elección de nodo PRIMARY");
while(!db.hello().isWritablePrimary){
    sleep(1000);
    print("Aún no es primary, esperando 1 segundo");
}
print("El nodo ahora es primary");

db = db.getSiblingDB('flashreserve');

print("Creando colecciones e indices");

db.createCollection('skus');
db.createCollection('reservations');
db.createCollection('processed_events');

db.reservations.createIndex({ "idempotencyKey": 1 }, { unique: true} );
db.reservations.createIndex({ "customerId": 1 });
db.reservations.createIndex({ "status": 1 });
db.reservations.createIndex({ "sku": 1 });

db.processed_events.createIndex({ "reservationId": 1 });

print("Insertando inventario incial en skus");

try{
    db.skus.insertMany([
        {
            _id: "IPHONE-15",
            available: 100,
            reserved: 0
        },
        {
            _id: "MACKBOOK-AIR",
            available: 5,
            reserved: 0
        }
    ]);
    print("SKUs de prueba insertados");
}catch(e){
    print("Los datos de prueba ya existian o hubo error: " + e);
}

print("Base de datos de FlashReserve lista para usar");