package com.flashreserve.domain.ports;

import com.flashreserve.domain.model.processed.ProcessedEvent;
import reactor.core.publisher.Mono;

public interface EventStorePort {
    Mono<Boolean> eventExists(String eventId);
    Mono<ProcessedEvent> saveEvent(ProcessedEvent event);
}
