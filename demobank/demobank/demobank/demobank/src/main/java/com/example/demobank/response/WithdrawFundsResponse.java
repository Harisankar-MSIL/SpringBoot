package com.example.demobank.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawFundsResponse {
    private BigDecimal newBalance;
    private BigDecimal withDrawAmount;
}
