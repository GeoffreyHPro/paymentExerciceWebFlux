package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.Command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {
    private int id;
    private Double amount;
    private String currency;
    private String paymentMeans;
    private String paymentStatus;
    private List<Command> listeCommands;
}
