package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.Payment;
import com.example.demo.repository.PaymentRepository;

import reactor.core.publisher.Mono;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Mono<Payment> getPayment(int id) throws NotFoundException {
        Mono<Payment> payment = paymentRepository.findById(id);
        return payment.switchIfEmpty(Mono.error(new NotFoundException()));
    }

}
