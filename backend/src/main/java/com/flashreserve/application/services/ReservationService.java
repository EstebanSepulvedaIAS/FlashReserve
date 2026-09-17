package com.flashreserve.application.services;

import com.flashreserve.domain.model.processed.ProcessedEvent;
import com.flashreserve.domain.model.reservation.Reservation;
import com.flashreserve.domain.model.reservation.ReservationStatus;
import com.flashreserve.domain.ports.EventStorePort;
import com.flashreserve.domain.ports.InventoryPort;
import com.flashreserve.domain.ports.ReservationPort;
import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationPort reservationPort;
    private final InventoryPort inventoryPort;
    private final EventStorePort eventStorePort;
    private final ApplicationEventPublisher eventPublisher;

    public Mono<Reservation> createReservation(String idempotencyKey, String customerId, String sku, int quantity){
        if(quantity < 1 || quantity > 5) return Mono.error(new IllegalArgumentException("Quantity must be between 1 and 5"));
        Reservation newReservation = Reservation.builder()
                .id(UUID.randomUUID().toString())
                .idempotencyKey(idempotencyKey)
                .customerId(customerId)
                .sku(sku)
                .quantity(quantity)
                .status(ReservationStatus.PENDING)
                .lastSequence(0)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return inventoryPort.reserveInventory(sku, quantity)
                .flatMap(reserved -> {
                    if(!reserved) return Mono.error(new IllegalArgumentException("Insufficient inventory for SKU: " + sku));
                    return reservationPort.save(newReservation)
                            .onErrorResume(DuplicateKeyException.class, e ->
                                    rollbackAndReturnExisting(sku, quantity, idempotencyKey));
                })
                .doOnSuccess(reservation -> emitAsyncAuditTask(reservation.getId(), "CREATED"));
    }

    private Mono<Reservation> rollbackAndReturnExisting(String sku, int quantity, String idempotencyKey){
        return inventoryPort.rollbackInventory(sku, quantity)
                .then(reservationPort.findByIdempotencyKey(idempotencyKey));
    }

    @Transactional
    public Mono<Void> processProviderEvent(String eventId, String reservationId, long sequence, String statusStr, Instant occurredAt){
        ReservationStatus newStatus = ReservationStatus.valueOf(statusStr);
        return eventStorePort.eventExists(eventId)
                .flatMap(exists -> {
                    if(exists) return Mono.empty();
                    return reservationPort.findById(reservationId)
                            .flatMap(reservation -> {
                                if(sequence <= reservation.getLastSequence()){
                                    return Mono.empty();
                                }

                                reservation.setStatus(newStatus);
                                reservation.setLastSequence(sequence);
                                reservation.setUpdatedAt(Instant.now());

                                Mono<Void> inventoryUpdate = newStatus == ReservationStatus.CONFIRMED
                                        ? inventoryPort.confirmInventory(reservation.getSku(), reservation.getQuantity())
                                        : inventoryPort.rollbackInventory(reservation.getSku(), reservation.getQuantity());

                                ProcessedEvent event = ProcessedEvent.builder()
                                        .id(eventId).reservationId(reservationId)
                                        .sequence(sequence).processedAt(Instant.now()).build();

                                return inventoryUpdate
                                        .then(reservationPort.save(reservation))
                                        .then(eventStorePort.saveEvent(event))
                                        .then();
                            });
                });
    }

    public Mono<Reservation> getReservation(String id){
        return reservationPort.findById(id);
    }

    private void emitAsyncAuditTask(String id, String action){
        eventPublisher.publishEvent(new AuditEvent(id, action));
    }


}
