package com.example.demo.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.demo.model.Command;

public interface CommandRepository extends ReactiveCrudRepository<Command, Integer> {

}
