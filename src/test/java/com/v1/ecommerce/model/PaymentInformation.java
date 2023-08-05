package com.v1.ecommerce.model;

import jakarta.persistence.Column;

import java.time.LocalDate;

public class PaymentInformation {
    @Column(name = "cardHolder_Name")
    private String cardholderName;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "cvv")
    private String cvv;
}
