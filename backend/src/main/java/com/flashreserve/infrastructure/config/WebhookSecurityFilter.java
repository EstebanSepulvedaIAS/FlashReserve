package com.flashreserve.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class WebhookSecurityFilter implements WebFilter {

    @Value("${webhook.api.key}")
    private String validApiKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if(path.startsWith("/api/v1/webhooks/")){
            String apiKey = exchange.getRequest().getHeaders().getFirst("X-API-KEY");
            if(!validApiKey.equals(apiKey)){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);
    }
}
