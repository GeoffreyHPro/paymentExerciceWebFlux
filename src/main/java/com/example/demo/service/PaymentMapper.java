package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CommandDTO;
import com.example.demo.dto.PaymentDTO;
import com.example.demo.model.Command;
import com.example.demo.model.Payment;

@Service
public class PaymentMapper {
    public CommandDTO toCommandDTO(Command command) {
        CommandDTO commandDTO = new CommandDTO();
        commandDTO.setId(command.getId());
        commandDTO.setPaymentId(command.getPaymentId());
        commandDTO.setPrice(command.getPrice());
        commandDTO.setProductName(command.getProductName());
        commandDTO.setProductRef(command.getProductRef());
        commandDTO.setQuantity(command.getQuantity());
        return commandDTO;
    }

    public PaymentDTO toPaymentDTO(Payment payment) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(payment.getId());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setCurrency(payment.getCurrency());
        paymentDTO.setPaymentMeans(payment.getPaymentMeans());
        paymentDTO.setPaymentStatus(payment.getPaymentStatus());
        if (payment.getListeCommands() != null && payment.getListeCommands().size() > 0) {
            paymentDTO.setListeCommands(payment.getListeCommands());
        } else {
            paymentDTO.setListeCommands(new ArrayList<>());
        }
        return paymentDTO;
    }
}
