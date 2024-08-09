package com.example.demobank.service;

import com.example.demobank.request.CreateAccountRequest;
import com.example.demobank.request.DepositAmountRequest;
import com.example.demobank.request.TransferFundsRequest;
import com.example.demobank.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    ResponseEntity<BaseResponse> createNewAccount(CreateAccountRequest createAccountRequest);
    ResponseEntity<BaseResponse> retrieveAccountBalance(Long accountId);
    ResponseEntity<BaseResponse> depositFunds(DepositAmountRequest depositAmountRequest);
    ResponseEntity<BaseResponse> withdrawFunds(DepositAmountRequest depositAmountRequest);
    ResponseEntity<BaseResponse> transferFunds(TransferFundsRequest transferFundsRequest);
    ResponseEntity<BaseResponse> getTransactionHistory(Long accountId);
}
