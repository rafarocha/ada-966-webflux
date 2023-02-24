package com.example.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FilmeController {

    @GetMapping("/flux")
    public Flux<Integer> flux() {
        return Flux.just(1,2,3);
    }

    @GetMapping(value = "/mono")
    public Mono<String> mono() {
        return Mono.just("hello");
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> stream() {
        return Flux.interval(Duration.ofSeconds(2)).log();
    }

}

class Cliente {
    public Cliente log() {
        System.out.println("faz log");
        return this;
    }
}