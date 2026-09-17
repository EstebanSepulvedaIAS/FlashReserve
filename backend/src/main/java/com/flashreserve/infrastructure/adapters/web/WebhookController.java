package com.flashreserve.infrastructure.adapters.web;

import com.flashreserve.application.services.ReservationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
class WebhookController {

    private final ReservationService service;

    @PostMapping("/provider")
    public Mono<ResponseEntity<Void>> handleWebhook(@RequestBody WebhookPayload payload){
        return service.processProviderEvent(payload.eventId, payload.reservationId, payload.sequence, payload.status, payload.occurredAt)
                .thenReturn(ResponseEntity.ok().<Void>build());
    }

    @Data static class WebhookPayload {String eventId, reservationId, status; long sequence; Instant occurredAt; }
}
