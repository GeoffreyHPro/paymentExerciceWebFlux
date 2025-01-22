package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.exception.NegativeValueException;

public class CommandTest {

    private Command command;

    @BeforeEach
    public void setUp() {
        this.command = new Command();
    }

    @Test
    public void testPriceSuccess() {
        try {
            this.command.setPrice(1.0);
            assertEquals(1.0, this.command.getPrice());
            Command command = new Command("ball", "051564", 10, 25.59);
            assertEquals(25.59, command.getPrice());
        } catch (Exception e) {

        }
    }

    @Test
    public void testPriceThrowsException() {
        assertThrows(NegativeValueException.class, () -> {
            this.command.setPrice(-10.0);
        });

        assertThrows(NegativeValueException.class, () -> {
            new Command("ball", "051564", 10, -15.0);
        });
    }

}
