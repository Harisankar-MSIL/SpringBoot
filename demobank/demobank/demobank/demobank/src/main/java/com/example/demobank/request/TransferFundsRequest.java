package com.example.demobank.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferFundsRequest {
    @NotNull(message = "Source-account-id is required")
    private Long sourceAccountId;

    @NotNull(message = "Destination-account-id is required")
    private Long destinationAccountId;

    @NotNull(message = "amount is required")
    @NotNull(message = "Amount is mandatory")
    @Min(value = 1, message = "Transfer amount must be greater than zero")
    private Double amount;
}
