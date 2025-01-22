package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.NegativeValueException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.NulValueException;
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

    public Flux<Command> getCommands(int idPayment) {
        Flux<Command> command = commandRepository.findByPaymentId(idPayment);
        return command.switchIfEmpty(Mono.error(new NotFoundException()));
    }

    public Mono<Command> addCommand(int id, CommandRequest commandRequest) throws NegativeValueException, NulValueException {
        Command command = new Command();
        command.setPrice(commandRequest.getPrice());
        command.setQuantity(commandRequest.getQuantity());
        command.setProductName(commandRequest.getProductName());
        command.setProductRef(commandRequest.getProductRef());

        return paymentRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .flatMap(payment -> {
                    String status = payment.getPaymentStatus();
                    if (status.equals(PaymentStatus.IN_PROGRESS.name())) {
                        command.setPaymentId(id);
                        return commandRepository.save(command);
                    } else {
                        return null;
                    }
                });
    }
}
