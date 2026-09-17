package com.flashreserve.infrastructure.adapters.persistence;

import com.flashreserve.domain.model.processed.ProcessedEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SpringDataEventRepository extends ReactiveMongoRepository<ProcessedEvent, String> {
}
