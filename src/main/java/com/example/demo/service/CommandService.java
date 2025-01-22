package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.NegativeValueException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.NulValueException;
import com.example.demo.exception.PaymentStatusException;
import com.example.demo.model.Command;
import com.example.demo.repository.CommandRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.utils.PaymentStatus;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CommandService {

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public Mono<Command> getCommand(int id) throws NotFoundException {
        Mono<Command> command = commandRepository.findById(id);
        return command.switchIfEmpty(Mono.error(new NotFoundException()));
    }

    public Flux<Command> getCommands(int idPayment) {
        Flux<Command> command = commandRepository.findByPaymentId(idPayment);
        return command.switchIfEmpty(Mono.error(new NotFoundException()));
    }

    public Mono<Command> addCommand(int id) throws NegativeValueException, NulValueException {

        return paymentRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .flatMap(payment -> {
                    try {
                        String status = payment.getPaymentStatus();
                        if (status.equals(PaymentStatus.IN_PROGRESS.name())) {
                            Command command = new Command("ball", "16546", 2, 20.0);
                            command.setPaymentId(id);
                            return commandRepository.save(command);
                        } else {
                            throw new PaymentStatusException();
                        }
                    } catch (Exception e) {
                        return Mono.empty();
                    }
                });
    }
}
