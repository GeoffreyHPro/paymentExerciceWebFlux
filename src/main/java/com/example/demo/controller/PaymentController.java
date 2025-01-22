package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Payment;
import com.example.demo.request.UpdatePaymentRequest;
import com.example.demo.service.CommandService;
import com.example.demo.service.PaymentMapper;
import com.example.demo.service.PaymentService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CommandService commandService;

    @Autowired
    private PaymentMapper paymentMapper;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<?>> getPayment(@PathVariable int id) {
        return paymentService.getPayment(id)
                .flatMap(payment -> {

                    return commandService.getCommands(id)
                            .collectList()
                            .map(commands -> {
                                payment.setListeCommands(commands);
                                return ResponseEntity.status(200).body(paymentMapper.toPaymentDTO(payment));
                            });
                });
    }

    @PostMapping
    public Mono<ResponseEntity<?>> addPayment() {
        try {
            Mono<Payment> payment = paymentService.addPayment();
            return Mono.just(ResponseEntity.status(201).body(payment));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(201).body("The payment is created"));
        }
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<?>> modifyPayment(@PathVariable int id,
            @RequestBody UpdatePaymentRequest updatePaymentRequest) {
        try {
            Mono<Payment> payment = paymentService.updatePayment(id, updatePaymentRequest);
            return Mono.just(ResponseEntity.status(200).body(payment));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(400).body("Error in Payment"));
        }
    }
}
