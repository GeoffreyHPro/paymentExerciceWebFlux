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

import com.example.demo.dto.CommandDTO;
import com.example.demo.dto.PaymentDTO;
import com.example.demo.exception.NegativeValueException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.NulValueException;
import com.example.demo.exception.PaymentStatusException;
import com.example.demo.request.CommandRequest;
import com.example.demo.request.UpdateCommandRequest;
import com.example.demo.service.CommandService;
import com.example.demo.service.PaymentMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/command")
public class CommandController {

        @Autowired
        private CommandService commandService;

        @Autowired
        private PaymentMapper paymentMapper;

        @Operation(summary = "Get the command by Id", description = "The id given in controller give you the command that is associated")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "The command is successfully get", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommandDTO.class))),
                        @ApiResponse(responseCode = "404", description = "The command is not found", content = @Content(mediaType = ""))
        })
        @GetMapping("/{id}")
        public Mono<ResponseEntity<CommandDTO>> getCommand(@PathVariable int id) {
                return commandService.getCommand(id)
                                .map(command -> ResponseEntity.status(200).body(paymentMapper.toCommandDTO(command)))
                                .onErrorResume(NotFoundException.class,
                                                e -> Mono.just(ResponseEntity.status(404).body(null)));
        }

        @Operation(summary = "Get All the commands", description = "Return an array of all commands, if is empty, it's also return")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "The command is successfully get", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommandDTO.class))),
        })
        @GetMapping
        public Flux<CommandDTO> getAllCommands() {
                return commandService.getAllCommands()
                                .map(command -> paymentMapper.toCommandDTO(command));
        }

        @Operation(summary = "Add a new command linked with a payment", description = "The new command is linked with a payment if he's exist")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "The command is successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommandDTO.class))),
                        @ApiResponse(responseCode = "404", description = "The payment is not found to link with the command", content = @Content(mediaType = "")),
                        @ApiResponse(responseCode = "400", description = "The request body is not correctly give. Price or quantity is wrong", content = @Content(mediaType = "")),
                        @ApiResponse(responseCode = "401", description = "You cannot add command to the payment id gived. The payment must be in IN_PROGRESS status", content = @Content(mediaType = ""))
        })
        @PostMapping("/{idPayment}")
        public Mono<ResponseEntity<CommandDTO>> addCommand(@PathVariable int idPayment,
                        @RequestBody CommandRequest commandRequest) {

                if (!commandRequest.isValid()) {
                        return Mono.just(ResponseEntity.status(400).body(null));
                }

                return commandService.addCommand(idPayment, commandRequest)
                                .map(command -> ResponseEntity.status(201).body(paymentMapper.toCommandDTO(command)))
                                .onErrorResume(NotFoundException.class,
                                                e -> Mono.just(ResponseEntity.status(404).body(null)))
                                .onErrorResume(NulValueException.class,
                                                e -> Mono.just(ResponseEntity.status(400).body(null)))
                                .onErrorResume(PaymentStatusException.class,
                                                e -> Mono.just(ResponseEntity.status(401).body(null)))
                                .onErrorResume(NegativeValueException.class,
                                                e -> Mono.just(ResponseEntity.status(400).body(null)));
        }

        @Operation(summary = "Modify the command", description = "You can modify the command if he is in IN_PROGRESS status")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "The payment is successfully modified", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentDTO.class))),
                        @ApiResponse(responseCode = "404", description = "The payment is not Found", content = @Content(mediaType = "")),
                        @ApiResponse(responseCode = "401", description = "Payment status is not modifiable", content = @Content(mediaType = "")),
                        @ApiResponse(responseCode = "400", description = "The quantity/price is not valid", content = @Content(mediaType = ""))
        })
        @PutMapping("/{id}")
        public Mono<ResponseEntity<CommandDTO>> modifyPayment(@PathVariable int id,
                        @RequestBody UpdateCommandRequest updateCommandRequest) {

                return commandService.updateCommand(id, updateCommandRequest)
                                .map(command -> ResponseEntity.status(200).body(paymentMapper.toCommandDTO(command)))
                                .onErrorResume(NotFoundException.class,
                                                e -> Mono.just(ResponseEntity.status(404).body(null)))
                                .onErrorResume(PaymentStatusException.class,
                                                e -> Mono.just(ResponseEntity.status(401).body(null)))
                                .onErrorResume(NulValueException.class,
                                                e -> Mono.just(ResponseEntity.status(400).body(null)))
                                .onErrorResume(NegativeValueException.class,
                                                e -> Mono.just(ResponseEntity.status(400).body(null)));

        }
}
