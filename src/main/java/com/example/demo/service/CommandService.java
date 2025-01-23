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
import com.example.demo.request.CommandRequest;
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

    public Flux<Command> getAllCommands() throws NotFoundException {
        Flux<Command> command = commandRepository.findAll();
        return command.switchIfEmpty(Mono.error(new NotFoundException()));
    }

    public Flux<Command> getCommands(int idPayment) {
        Flux<Command> command = commandRepository.findByPaymentId(idPayment);
        return command;
    }

    public Mono<Command> addCommand(int id, CommandRequest commandRequest)
            throws NegativeValueException, NulValueException {
        Command command = new Command();
        command.setPrice(commandRequest.getPrice());
        command.setQuantity(commandRequest.getQuantity());
        command.setProductName(commandRequest.getProductName());
        command.setProductRef(commandRequest.getProductRef());

        return paymentRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .flatMap(payment -> {
                    try {
                        String status = payment.getPaymentStatus();
                        if (status.equals(PaymentStatus.IN_PROGRESS.name())) {
                            command.setPaymentId(id);
                            payment.setAmount(payment.getAmount() + command.getPrice());
                            return paymentRepository.save(payment)
                                    .then(commandRepository.save(command));
                        } else {
                            return Mono.error(new PaymentStatusException());
                        }
                    } catch (Exception e) {
                        return Mono.error(new PaymentStatusException());
                    }
                });
    }
}
