package com.example.demobank.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositAmountRequest {
    @NotNull(message = "account-id is required")
    private Long accountId;
    @NotNull(message = "amount is required")
    private Double amount;
}
