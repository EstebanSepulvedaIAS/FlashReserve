package com.flashreserve.infrastructure.adapters.persistence;

import com.flashreserve.domain.model.processed.ProcessedEvent;
import com.flashreserve.domain.ports.EventStorePort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoEventStoreAdapter implements EventStorePort {

    private final SpringDataEventRepository repository;

    public MongoEventStoreAdapter(SpringDataEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Boolean> eventExists(String eventId) {
        return repository.existsById(eventId);
    }

    @Override
    public Mono<ProcessedEvent> saveEvent(ProcessedEvent event) {
        return repository.save(event);
    }
}
