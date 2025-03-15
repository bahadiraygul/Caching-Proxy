package com.project.caching_proxy.controller;

import com.project.caching_proxy.service.ProxyService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/proxy")
public class ProxyController {
    private final ProxyService proxyService;

    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @GetMapping("/{endpoint}")
    public Mono<String> proxyRequest(@PathVariable String endpoint) {
        return proxyService.fetchData(endpoint);
    }
}
