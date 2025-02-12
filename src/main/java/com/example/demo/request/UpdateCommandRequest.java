package com.example.demo.request;

import lombok.Data;

@Data
public class UpdateCommandRequest {
    private String productName;
    private String productRef;
    private int quantity;
    private Double price;

    public UpdateCommandRequest(String productName, String productRef, int quantity, Double price) {
        this.productName = productName;
        this.productRef = productRef;
        this.quantity = quantity;
        this.price = price;
    }
}
