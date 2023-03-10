package com.example.controller;

import com.example.domain.Cliente;
import com.example.service.ClienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequestMapping("/clientes")
@Slf4j
public class ClienteController {

    private ClienteService service;

    public ClienteController(ClienteService clienteService) {
        this.service = clienteService;
    }

    Sinks.Many<Cliente> sinks = Sinks.many().replay().latest();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cliente> salvar(@RequestBody Cliente cliente) {
        return service.salvar(cliente)
                .doOnNext(salvo -> {
                   sinks.tryEmitNext(salvo);
                });
    }

    @GetMapping
    public Flux<Cliente> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Cliente>> atualizar(@RequestBody Cliente cliente, @PathVariable String id) {
        Mono<Cliente> atualizar = service.atualizar(cliente, id);
        return atualizar
            .map(atual -> ResponseEntity.ok().body(atual))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> remover(@PathVariable String id) {
        return service.remover(id);
    }

    @GetMapping("/stream")
    public Flux<Cliente> stream() {
        return sinks.asFlux();
    }

}