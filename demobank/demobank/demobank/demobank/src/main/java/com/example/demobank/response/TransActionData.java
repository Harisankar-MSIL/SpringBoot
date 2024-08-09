package com.example.demobank.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TransActionData {

    private String transactionType;

    private Double amount;

    private Date transactionDate;

}
