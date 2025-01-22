package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;

import com.example.demo.model.Payment;
import com.example.demo.utils.PaymentStatus;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataR2dbcTest
public class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void testSaveAndFindById() {
        Payment payment = new Payment();

        Mono<Payment> savedPayment = paymentRepository.save(payment);

        StepVerifier.create(savedPayment)
                .assertNext(saved -> {
                    assertEquals(0.0, saved.getAmount());
                    assertNotEquals(0, saved.getId());
                    assertEquals(PaymentStatus.IN_PROGRESS.name(), saved.getPaymentStatus());
                }).verifyComplete();

        Mono<Payment> paymentFound = paymentRepository.findById(payment.getId());

        StepVerifier.create(paymentFound)
                .assertNext(pFound -> {
                    assertEquals(0.0, pFound.getAmount());
                    assertEquals(PaymentStatus.IN_PROGRESS.name(), pFound.getPaymentStatus());
                    assertNotEquals(0, pFound.getId());
                }).verifyComplete();
    }
}
