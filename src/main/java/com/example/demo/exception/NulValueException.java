package com.example.demo.exception;

public class NulValueException extends Exception {
    public NulValueException() {
        super("The value is nul");
    }
}
