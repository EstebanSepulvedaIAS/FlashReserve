package com.flashreserve.infrastructure.adapters.persistence;

import com.flashreserve.domain.model.sku.Sku;
import com.flashreserve.domain.ports.InventoryPort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoInventoryAdapter implements InventoryPort {

    private final ReactiveMongoTemplate template;

    public MongoInventoryAdapter(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Boolean> reserveInventory(String skuId, int quantity) {

        Query query = new Query(Criteria.where("_id").is(skuId).and("available").gte(quantity));
        Update update = new Update().inc("available", -quantity).inc("reserved", quantity);

        return template.updateFirst(query, update, Sku.class)
                .map(updateResult -> updateResult.getModifiedCount() > 0);
    }

    @Override
    public Mono<Void> confirmInventory(String skuId, int quantity) {
        Query query = new Query(Criteria.where("_id").is(skuId));
        Update update = new Update().inc("reserved", -quantity);
        return template.updateFirst(query, update, Sku.class).then();
    }

    @Override
    public Mono<Void> rollbackInventory(String skuId, int quantity) {
        Query query = new Query(Criteria.where("_id").is(skuId));
        Update update = new Update().inc("reserved", -quantity).inc("available", quantity);
        return template.updateFirst(query, update, Sku.class).then();
    }
}
