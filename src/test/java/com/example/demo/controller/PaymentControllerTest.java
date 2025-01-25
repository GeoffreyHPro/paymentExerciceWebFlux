package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.demo.dto.PaymentDTO;
import com.example.demo.exception.NegativeValueException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.NulValueException;
import com.example.demo.model.Command;
import com.example.demo.model.Payment;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.CommandService;
import com.example.demo.service.PaymentMapper;
import com.example.demo.service.PaymentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private CommandService commandService;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private PaymentMapper paymentMapper;

    @Test
    public void testGetPaymentSuccess() throws NotFoundException, NegativeValueException, NulValueException {
        List<Command> listCommand = new ArrayList<>();
        listCommand.add(new Command("ball", "4168486", 2, 10.0));
        Payment payment = new Payment();
        payment.setListeCommands(listCommand);
        PaymentDTO paymentDTO = paymentMapper.toPaymentDTO(payment);

        when(commandService.getCommands(2)).thenReturn(Flux.fromIterable(listCommand));
        when(paymentService.getPayment(2)).thenReturn(Mono.just(payment));

        webTestClient.get()
                .uri("/payment/2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Flux.class)
                .value(response -> {
                    assertEquals(paymentDTO, response);
                });
    }
}
