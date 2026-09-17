package com.flashreserve.infrastructure.adapters.persistence;

import com.flashreserve.domain.model.reservation.Reservation;
import com.flashreserve.domain.ports.ReservationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoReservationAdapter implements ReservationPort {

    private final SpringDataReservationRepository reservationRepository;

    public MongoReservationAdapter(SpringDataReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Mono<Reservation> save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Mono<Reservation> findById(String id) {
        return reservationRepository.findById(id);
    }

    @Override
    public Mono<Reservation> findByIdempotencyKey(String idempotencyKey) {
        return reservationRepository.findByIdempotencyKey(idempotencyKey);
    }
}
