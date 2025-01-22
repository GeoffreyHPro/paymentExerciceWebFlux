package com.example.demo.exception;

public class NegativeValueException extends Exception {
    public NegativeValueException() {
        super("The number is negative");
    }
}
