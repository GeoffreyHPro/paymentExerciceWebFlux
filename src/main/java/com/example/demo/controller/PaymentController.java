package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PaymentDTO;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.PaymentStatusException;
import com.example.demo.request.UpdatePaymentRequest;
import com.example.demo.service.CommandService;
import com.example.demo.service.PaymentMapper;
import com.example.demo.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/payment")
public class PaymentController {

        @Autowired
        private PaymentService paymentService;

        @Autowired
        private CommandService commandService;

        @Autowired
        private PaymentMapper paymentMapper;

        @Operation(summary = "Get the payment with the id given", description = "")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "The payment is successfully get", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentDTO.class))),
                        @ApiResponse(responseCode = "404", description = "The payment is not found", content = @Content(mediaType = ""))
        })
        @GetMapping("/{id}")
        public Mono<ResponseEntity<PaymentDTO>> getPayment(@PathVariable int id) {
                return paymentService.getPayment(id)
                                .flatMap(payment -> {
                                        return commandService.getCommands(id)
                                                        .collectList()
                                                        .map(commands -> {
                                                                payment.setListeCommands(commands);
                                                                return ResponseEntity.status(200).body(
                                                                                paymentMapper.toPaymentDTO(payment));
                                                        });
                                }).onErrorResume(NotFoundException.class,
                                                e -> Mono.just(ResponseEntity.status(404).body(null)));
        }

        @Operation(summary = "Get all the payments", description = "")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "The payment is successfully get", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentDTO.class))),
                        @ApiResponse(responseCode = "404", description = "The payment is not found", content = @Content(mediaType = ""))
        })
        @GetMapping
        public Flux<PaymentDTO> getPayments() {
                return paymentService.getPayments()
                                .flatMap(payment -> {
                                        return commandService.getCommands(payment.getId())
                                                        .collectList()
                                                        .map(commands -> {
                                                                payment.setListeCommands(commands);
                                                                return paymentMapper.toPaymentDTO(payment);
                                                        });
                                });
        }

        @PostMapping
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "The payment is successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentDTO.class))),
        })
        public Mono<ResponseEntity<PaymentDTO>> addPayment() {
                return paymentService.addPayment()
                                .map(payment -> ResponseEntity.status(201).body(paymentMapper.toPaymentDTO(payment)));
        }

        @PutMapping("/{id}")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "The payment is successfully modified", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentDTO.class))),
                        @ApiResponse(responseCode = "404", description = "The payment is not Found"),
                        @ApiResponse(responseCode = "400", description = "Payment status is not modifiable")
        })
        public Mono<ResponseEntity<PaymentDTO>> modifyPayment(@PathVariable int id,
                        @RequestBody UpdatePaymentRequest updatePaymentRequest)
                        throws NotFoundException, PaymentStatusException {

                return paymentService.updatePayment(id, updatePaymentRequest)
                                .map(payment -> ResponseEntity.status(200).body(paymentMapper.toPaymentDTO(payment)))
                                .onErrorResume(
                                                NotFoundException.class,
                                                e -> Mono.just(ResponseEntity.status(404).body(null)))
                                .onErrorResume(PaymentStatusException.class,
                                                e -> Mono.just(ResponseEntity.status(400).body(null)));

        }
}
