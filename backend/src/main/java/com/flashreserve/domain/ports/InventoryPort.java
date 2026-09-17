package com.flashreserve.domain.ports;

import reactor.core.publisher.Mono;

public interface InventoryPort {
    Mono<Boolean> reserveInventory(String skuId, int quantity);
    Mono<Void> confirmInventory(String skuId, int quantity);
    Mono<Void> rollbackInventory(String skuId, int quantity);
}
