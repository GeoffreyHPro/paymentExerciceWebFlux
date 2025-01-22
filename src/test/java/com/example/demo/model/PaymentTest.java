package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.exception.NegativeValueException;
import com.example.demo.exception.NulValueException;
import com.example.demo.exception.PaymentStatusException;
import com.example.demo.utils.PaymentStatus;


public class PaymentTest {
    private Payment payment;
    private Command command;
    private Command command2;

    @BeforeEach
    public void setUp() throws NegativeValueException, NulValueException {
        this.payment = new Payment();
        this.command = new Command("ball", "051854", 2, 10.0);
        this.command2 = new Command("hat", "051823", 1, 20.0);
    }

    @Test
    public void testPaymentConstructor() {
        assertEquals(PaymentStatus.IN_PROGRESS.name(), this.payment.getPaymentStatus());
    }

    @Test
    public void testPaymentStatusSuccess() throws PaymentStatusException {
        this.payment.setPaymentStatus(PaymentStatus.AUTHORIZED);
        assertEquals(PaymentStatus.AUTHORIZED.name(), this.payment.getPaymentStatus());
        this.payment.setPaymentStatus(PaymentStatus.CAPTURED);
        assertEquals(PaymentStatus.CAPTURED.name(), this.payment.getPaymentStatus());
    }

    @Test
    public void testPaymentStatusThrowException() throws PaymentStatusException {
        assertThrows(PaymentStatusException.class, () -> {
            this.payment.setPaymentStatus(PaymentStatus.CAPTURED);
            this.payment.setPaymentStatus(PaymentStatus.IN_PROGRESS);
        });

        this.payment.setPaymentStatus(PaymentStatus.AUTHORIZED);

        assertThrows(PaymentStatusException.class, () -> {
            this.payment.setPaymentStatus(PaymentStatus.AUTHORIZED);
            this.payment.setPaymentStatus(PaymentStatus.IN_PROGRESS);
        });

        this.payment.setPaymentStatus(PaymentStatus.CAPTURED);

        assertThrows(PaymentStatusException.class, () -> {
            this.payment.setPaymentStatus(PaymentStatus.CAPTURED);
            this.payment.setPaymentStatus(PaymentStatus.IN_PROGRESS);
            this.payment.setPaymentStatus(PaymentStatus.AUTHORIZED);
        });
    }

    @Test
    public void testPaymentAmountSuccess() throws NegativeValueException, NulValueException {
        this.payment.setAmount(20.0);
        assertEquals(20.0, this.payment.getAmount());
    }

    @Test
    public void testPaymentAmountThrowException() throws NegativeValueException, NulValueException {
        assertThrows(NegativeValueException.class, () -> {
            this.payment.setAmount(-1.0);
        });

        assertThrows(NulValueException.class, () -> {
            this.payment.setAmount(0.0);
        });
    }

    @Test
    public void testAddCommandSuccess() throws NegativeValueException, NulValueException {
        assertEquals(0, this.payment.getListCommands().size());
        this.payment.addCommand(this.command);
        assertEquals(1, this.payment.getListCommands().size());
        this.payment.addCommand(new Command("hat", "051823", 1, 20.0));
        assertEquals(2, this.payment.getListCommands().size());
        assertEquals(20.0, this.payment.getListCommands().get(1).getPrice());
    }

    @Test
    public void testAddCommandThrowException() throws NegativeValueException, NulValueException {
        assertThrows(NegativeValueException.class, () -> {
            this.payment.addCommand(new Command("ball", "051854", -1, 10.0));
        });
    }

    @Test
    public void testGetAmountSucces() throws NegativeValueException, NulValueException {
        assertEquals(0, this.payment.getAmount());
        this.payment.addCommand(this.command);
        assertEquals(10.0, this.payment.getAmount());
        this.payment.addCommand(this.command2);
        assertEquals(30.0, this.payment.getAmount());
    }

}
