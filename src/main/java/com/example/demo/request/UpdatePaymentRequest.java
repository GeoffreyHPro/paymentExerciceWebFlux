package com.example.demo.request;

import com.example.demo.utils.Currency;
import com.example.demo.utils.PaymentMeans;
import com.example.demo.utils.PaymentStatus;

import lombok.Data;

@Data
public class UpdatePaymentRequest {
    private Currency currency;
    private PaymentMeans paymentMeans;
    private PaymentStatus paymentStatus;

    public UpdatePaymentRequest(Currency currency, PaymentMeans paymentMeans, PaymentStatus paymentStatus) {
        this.currency = currency;
        this.paymentMeans = paymentMeans;
        this.paymentStatus = paymentStatus;
    }
}
