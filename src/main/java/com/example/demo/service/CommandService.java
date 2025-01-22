package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.Command;
import com.example.demo.repository.CommandRepository;

import reactor.core.publisher.Mono;

@Service
public class CommandService {

    @Autowired
    private CommandRepository commandRepository;

    public Mono<Command> getCommand(int id) throws NotFoundException {
        Mono<Command> command = commandRepository.findById(id);
        return command.switchIfEmpty(Mono.error(new NotFoundException()));
    }

}
