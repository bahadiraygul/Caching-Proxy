package com.project.caching_proxy.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ProxyService {
    private final WebClient webClient;
    private final CacheService cacheService;

    public ProxyService(WebClient.Builder webClientBuilder, CacheService cacheService) {
        this.webClient = webClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
        this.cacheService = cacheService;
    }

    public Mono<String> fetchData(String endpoint) {
        String cachedData = String.valueOf(cacheService.getCachedResponse(endpoint));
        if (cachedData != null) {
            return Mono.just(cachedData);
        }

        return webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> cacheService.cacheResponse(endpoint, response));
    }
}
