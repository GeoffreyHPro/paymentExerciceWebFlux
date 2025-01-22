package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Command;
import com.example.demo.service.CommandService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/command")
public class CommandController {

    @Autowired
    private CommandService commandService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Command>> getCommand(@PathVariable int id) throws NotFoundException {

        return commandService.getCommand(id).map(command -> ResponseEntity.status(200).body(command))
                .onErrorResume(NotFoundException.class, e -> Mono.just(ResponseEntity.status(404).body(null)));

    }

    @PostMapping
    public Mono<ResponseEntity<?>> addCommand() {
        try {
            Mono<Command> command = commandService.addCommand();
            return Mono.just(ResponseEntity.status(201).body(command));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(400).body("Error of created command"));
        }
    }
}
