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
    private int id;

    private Double amount;

    private String currency;

    private String paymentMeans;

    private String paymentStatus;

    private List<Command> listCommands;

    public Payment() {
        this.listCommands = new ArrayList<>();
        this.paymentStatus = PaymentStatus.IN_PROGRESS.name();
        this.amount = 0.0;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getAmount() {
        return amount;
    }

    public int getIdPayment() {
        return id;
    }

    public String getPaymentMeans() {
        return paymentMeans;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setAmount(Double amount) throws NegativeValueException, NulValueException {
        validateAmount(amount);
        this.amount = amount;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency.name();
    }

    public void setPaymentMeans(PaymentMeans paymentMeans) {
        this.paymentMeans = paymentMeans.name();
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
                this.paymentStatus.equals(PaymentStatus.AUTHORIZED.name())) {
            this.paymentStatus = paymentStatus.name();
        } else if (paymentStatus.equals(PaymentStatus.AUTHORIZED)
                && this.paymentStatus.equals(PaymentStatus.IN_PROGRESS.name())) {
            this.paymentStatus = paymentStatus.name();
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
