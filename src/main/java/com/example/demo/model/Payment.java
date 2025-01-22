package com.example.demo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.example.demo.exception.NegativeValueException;
import com.example.demo.exception.NulValueException;
import com.example.demo.exception.PaymentStatusException;
import com.example.demo.utils.Currency;
import com.example.demo.utils.PaymentMeans;
import com.example.demo.utils.PaymentStatus;

@Table("payment")
public class Payment {
    @Id
    private int id;

    private Double amount;

    private String currency;

    @Column("paymentMeans")
    private String paymentMeans;

    @Column("paymentStatus")
    private String paymentStatus;

    @org.springframework.data.annotation.Transient
    private List<Command> listeCommands;

    public Payment() {
        this.paymentStatus = PaymentStatus.IN_PROGRESS.name();
        this.amount = 0.0;
        this.currency = Currency.DOLLAR.name();
        this.paymentMeans = PaymentMeans.BANK_CARD.name();
    }

    public String getCurrency() {
        return currency;
    }

    public List<Command> getListeCommands() {
        return listeCommands;
    }

    public void setListeCommands(List<Command> listeCommands) {
        this.listeCommands = listeCommands;
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

    public int getId() {
        return id;
    }

    /*
     * public List<Command> getListCommands() {
     * return listCommands;
     * }
     * 
     * public void setListCommands(List<Command> listCommands) {
     * this.listCommands = listCommands;
     * }
     */

    /*
     * public void addCommand(Command command) {
     * this.listCommands.add(command);
     * this.amount += command.getPrice();
     * }
     */
}
