package com.flashreserve.infrastructure.adapters.persistence;

import com.flashreserve.domain.model.reservation.Reservation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface SpringDataReservationRepository extends ReactiveMongoRepository<Reservation, String> {

    Mono<Reservation> findByIdempotencyKey(String idempotencyKey);
}
