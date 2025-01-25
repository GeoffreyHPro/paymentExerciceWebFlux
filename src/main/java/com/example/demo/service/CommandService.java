package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.NegativeValueException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.NulValueException;
import com.example.demo.exception.PaymentStatusException;
import com.example.demo.model.Command;
import com.example.demo.model.Payment;
import com.example.demo.repository.CommandRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.request.CommandRequest;
import com.example.demo.request.UpdateCommandRequest;
import com.example.demo.utils.PaymentStatus;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CommandService {

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public Mono<Command> getCommand(int id) {
        return commandRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    public Flux<Command> getAllCommands() {
        return commandRepository.findAll();
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

    public Mono<Command> updateCommand(int id, UpdateCommandRequest updateCommandRequest) {
        return paymentRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .flatMap((Payment payment) -> {
                    if (payment.getPaymentStatus().equals(PaymentStatus.IN_PROGRESS.name())) {
                        return commandRepository.findById(id).flatMap((Command command) -> {
                            try {
                                Double oldPrice = command.getPrice();
                                command.setPrice(updateCommandRequest.getPrice());
                                command.setProductName(updateCommandRequest.getProductName());
                                command.setProductRef(updateCommandRequest.getProductRef());
                                command.setQuantity(updateCommandRequest.getQuantity());
                                payment.setAmount(payment.getAmount() + (updateCommandRequest.getPrice() - oldPrice));
                                return paymentRepository.save(payment)
                                        .then(commandRepository.save(command));
                            } catch (NulValueException e) {
                                return Mono.error(new NulValueException());
                            } catch (NegativeValueException e) {
                                return Mono.error(new NegativeValueException());
                            }
                        });
                    } else {
                        return Mono.error(new PaymentStatusException());
                    }
                });
    }
}