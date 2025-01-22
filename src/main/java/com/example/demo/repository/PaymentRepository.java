package com.example.demo.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.demo.model.Payment;

public interface PaymentRepository extends ReactiveCrudRepository<Payment, Integer> {

}
