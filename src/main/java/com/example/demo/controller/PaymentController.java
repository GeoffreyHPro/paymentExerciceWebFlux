package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Payment;
import com.example.demo.service.PaymentService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/payment")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Payment>> getPayment(@PathVariable int id) throws NotFoundException {

        return paymentService.getPayment(id).map(command -> ResponseEntity.status(200).body(command))
                .onErrorResume(NotFoundException.class, e -> Mono.just(ResponseEntity.status(404).body(null)));

    }
}
