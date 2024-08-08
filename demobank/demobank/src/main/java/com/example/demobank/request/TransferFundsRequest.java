package com.example.demobank.request;

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
    private Double amount;
}
