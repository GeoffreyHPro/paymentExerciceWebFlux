package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.example.demo.exception.NegativeValueException;
import com.example.demo.exception.NulValueException;

@Table("command")
public class Command {

    @Id
    private int id;

    @Column("productName")
    private String productName;

    @Column("productRef")
    private String productRef;

    private int quantity;

    private Double price;

    @Column("paymentId")
    private int paymentId;

    public Command() {

    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Command(String productName, String productRef, int quantity, Double price)
            throws NegativeValueException, NulValueException {
        validatePrice(price);
        validateQuantity(quantity);
        this.productName = productName;
        this.productRef = productRef;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductRef() {
        return productRef;
    }

    public void setPrice(Double price) throws NegativeValueException, NulValueException {
        validatePrice(price);
        this.price = price;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductRef(String productRef) {
        this.productRef = productRef;
    }

    public void setQuantity(int quantity) throws NegativeValueException, NulValueException {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validatePrice(Double price) throws NegativeValueException, NulValueException {
        if (price < 0) {
            throw new NegativeValueException();
        }
        if (price == 0) {
            throw new NulValueException();
        }
    }

    private void validateQuantity(int quantity) throws NegativeValueException, NulValueException {
        if (quantity < 0) {
            throw new NegativeValueException();
        }
        if (quantity == 0) {
            throw new NulValueException();
        }
    }

}