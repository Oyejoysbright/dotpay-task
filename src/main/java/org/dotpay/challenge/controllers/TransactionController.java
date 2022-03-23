package org.dotpay.challenge.controllers;

import org.dotpay.challenge.dtos.TransactionRequest;
import org.dotpay.challenge.enums.TransactionStatus;
import org.dotpay.challenge.enums.TransferType;
import org.dotpay.challenge.services.TransactionService;
import org.dotpay.challenge.utils.ServerResponse.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping("/request")
    public ResponseEntity<ResponseMessage<Object>> requestTransfer(@RequestBody TransactionRequest payload) {
        return service.performTransfer(payload);
    }

    @GetMapping
    public ResponseEntity<ResponseMessage<Object>> getTransactions (
        @RequestParam(required = false, defaultValue = "") TransactionStatus status,
        @RequestParam(required = false, defaultValue = "") String senderAccountNumber,
        @RequestParam(required = false, defaultValue = "") String receiverAccountNumber,
        @RequestParam(required = false, defaultValue = "") String startDate,
        @RequestParam(required = false, defaultValue = "") String endDate,
        @RequestParam(required = false, defaultValue = "") TransferType type) {
        return service.getTransactionHistory(status, senderAccountNumber, receiverAccountNumber, startDate, endDate, type);
    }
}