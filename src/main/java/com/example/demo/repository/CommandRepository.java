package com.example.demo.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.demo.model.Command;

import reactor.core.publisher.Flux;

public interface CommandRepository extends ReactiveCrudRepository<Command, Integer> {
    Flux<Command> findByPaymentId(int id);
}
