package com.flashreserve.infrastructure.adapters.web;

import com.flashreserve.application.services.ReservationService;
import com.flashreserve.domain.model.reservation.Reservation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @PostMapping
    public Mono<ResponseEntity<Reservation>> create(@RequestBody CreateRequest request){
        return service.createReservation(request.idempotencyKey, request.customerId, request.sku, request.quantity)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class, e -> Mono.just(ResponseEntity.badRequest().build()))
                .onErrorResume(IllegalArgumentException.class, e -> Mono.just(ResponseEntity.unprocessableEntity().build()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Reservation>> get(@PathVariable String id){
        return service.getReservation(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

     @Data static class CreateRequest {String idempotencyKey, customerId, sku; int quantity; }
}
