package com.example.demobank.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DepositAmountRequest {

    @NotNull(message = "account-id is required")
    private Long accountId;

    @NotNull(message = "amount is required")
    @NotNull(message = "Amount is mandatory")
    @Min(value = 1, message = "Deposit amount must be greater than zero")
    private BigDecimal amount;
}
