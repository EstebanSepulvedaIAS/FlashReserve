package com.flashreserve.domain.model.processed;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document(collection = "processed_events")
public class ProcessedEvent {
    @Id
    private String id;
    private String reservationId;
    private long sequence;
    private Instant processedAt;
}
