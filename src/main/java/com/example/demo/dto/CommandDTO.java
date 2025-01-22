package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommandDTO {
    private int id;
    private String productName;
    private String productRef;
    private int quantity;
    private Double price;
    private int paymentId;
}
