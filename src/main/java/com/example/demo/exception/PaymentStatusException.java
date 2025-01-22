package com.example.demo.exception;

public class PaymentStatusException extends Exception {
    public PaymentStatusException() {
        super("The new status of payment is not possible");
    }
}
