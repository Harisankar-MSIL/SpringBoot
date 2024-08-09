package com.example.demobank.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawFundsResponse {
    private Double newBalance;
    private Double withDrawAmount;
}
