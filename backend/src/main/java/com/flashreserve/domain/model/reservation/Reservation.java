package com.flashreserve.domain.model.reservation;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document(collation = "reservations")
public class Reservation {
    @Id
    private String id;
    @Indexed(unique = true)
    private String idempotencyKey;
    private String customerId;
    private String sku;
    private int quantity;
    private ReservationStatus status;
    private long lastSequence;
    private Instant createdAt;
    private Instant updatedAt;
}
