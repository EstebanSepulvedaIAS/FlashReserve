package com.flashreserve.domain.model.sku;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "skus")
public class Sku {
    @Id
    private String id;
    private int available;
    private int reserved;
}
