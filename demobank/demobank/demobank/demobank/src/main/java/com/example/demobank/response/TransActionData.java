package com.example.demobank.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class TransActionData {

    private String transactionType;

    private BigDecimal amount;

    private Date transactionDate;

}
