package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.example.demo.exception.NegativeValueException;
import com.example.demo.exception.NulValueException;
import com.example.demo.exception.PaymentStatusException;
import com.example.demo.utils.Currency;
import com.example.demo.utils.PaymentMeans;
import com.example.demo.utils.PaymentStatus;

public class Payment {
    @Id
    private int idPayment;

    private Double amount;

    private Currency currency;

    private PaymentMeans paymentMeans;

    private PaymentStatus paymentStatus;

    private List<Command> listCommands;

    public Payment() {
        this.listCommands = new ArrayList<>();
        this.paymentStatus = PaymentStatus.IN_PROGRESS;
        this.amount = 0.0;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Double getAmount() {
        return amount;
    }

    public int getIdPayment() {
        return idPayment;
    }

    public PaymentMeans getPaymentMeans() {
        return paymentMeans;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setAmount(Double amount) throws NegativeValueException, NulValueException {
        validateAmount(amount);
        this.amount = amount;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setPaymentMeans(PaymentMeans paymentMeans) {
        this.paymentMeans = paymentMeans;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) throws PaymentStatusException {
        paymentStatusIsValid(paymentStatus);
    }

    private void validateAmount(Double amount) throws NegativeValueException, NulValueException {
        if (amount < 0) {
            throw new NegativeValueException();
        }
        if (amount == 0) {
            throw new NulValueException();
        }
    }

    private void paymentStatusIsValid(PaymentStatus paymentStatus) throws PaymentStatusException {
        if (paymentStatus.equals(PaymentStatus.CAPTURED) &&
                this.paymentStatus.equals(PaymentStatus.AUTHORIZED)) {
            this.paymentStatus = paymentStatus;
        } else if (paymentStatus.equals(PaymentStatus.AUTHORIZED)
                && this.paymentStatus.equals(PaymentStatus.IN_PROGRESS)) {
            this.paymentStatus = paymentStatus;
        } else {
            throw new PaymentStatusException();
        }
    }

    public List<Command> getListCommands() {
        return listCommands;
    }

    public void setListCommands(List<Command> listCommands) {
        this.listCommands = listCommands;
    }

    public void addCommand(Command command) {
        this.listCommands.add(command);
        this.amount += command.getPrice();
    }
}
