package com.example.demo.request;

import java.beans.Transient;

import lombok.Data;

@Data
public class CommandRequest {
    private String productName;
    private String productRef;
    private int quantity;
    private Double price;

    public CommandRequest(String productName, String productRef, int quantity, Double price) {
        this.productName = productName;
        this.productRef = productRef;
        this.quantity = quantity;
        this.price = price;
    }

    @Transient
    public Boolean isValid() {
        return (this.price > 0) && (this.quantity > 0);
    }
}
