package com.project.caching_proxy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class CacheService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private static final long CACHE_TTL = 60;

    public CacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void cacheResponse(String url, Object response) {
        try {
            String json = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(url, json, Duration.ofSeconds(CACHE_TTL));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Mono<String> getCachedResponse(String url) {
        return Mono.justOrEmpty(redisTemplate.opsForValue().get(url));
    }
}
