package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.PaymentStatusException;
import com.example.demo.model.Payment;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.request.UpdatePaymentRequest;

import reactor.core.publisher.Mono;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Mono<Payment> getPayment(int id) throws NotFoundException {
        Mono<Payment> payment = paymentRepository.findById(id);
        return payment.switchIfEmpty(Mono.error(new NotFoundException()));
    }

    public Mono<Payment> addPayment() {
        return paymentRepository.save(new Payment());
    }

    public Mono<Payment> updatePayment(int id, UpdatePaymentRequest updatePaymentRequest) {
        return paymentRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .flatMap((Payment payment) -> {
                    try {
                        payment.setPaymentStatus(updatePaymentRequest.getPaymentStatus());
                        payment.setCurrency(updatePaymentRequest.getCurrency());
                        payment.setPaymentMeans(updatePaymentRequest.getPaymentMeans());
                        return paymentRepository.save(payment);
                    } catch (PaymentStatusException e) {
                        return Mono.error(new PaymentStatusException());
                    }
                });
    }

}
