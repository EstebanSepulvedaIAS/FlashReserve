package com.flashreserve.domain.ports;

import com.flashreserve.domain.model.reservation.Reservation;
import reactor.core.publisher.Mono;

public interface ReservationPort {
    Mono<Reservation> save(Reservation reservation);
    Mono<Reservation> findById(String id);
    Mono<Reservation> findByIdempotencyKey(String idempotencyKey);
}
