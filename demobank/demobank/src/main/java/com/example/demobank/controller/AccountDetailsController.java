package com.example.demobank.controller;

import com.example.demobank.request.CreateAccountRequest;
import com.example.demobank.request.DepositAmountRequest;
import com.example.demobank.request.TransferFundsRequest;
import com.example.demobank.response.BaseResponse;
import com.example.demobank.service.impl.AccountServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AccountDetailsController {
    @Autowired
    AccountServiceImpl accountServiceImpl;

    @PostMapping("/accounts/create")
    public ResponseEntity<BaseResponse> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest, BindingResult result) {

        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new BaseResponse(400, "Missing Fields", errorMessage.toString().trim()), HttpStatus.CREATED);
        }
        return accountServiceImpl.createNewAccount(createAccountRequest);
    }

    @GetMapping("/accounts/{account_id}/balance")
    public ResponseEntity<BaseResponse> getAccountBalance(@PathVariable("account_id") Long accountId) {
        return accountServiceImpl.retrieveAccountBalance(accountId);
    }
    @PostMapping("/accounts/deposit")
    public ResponseEntity<BaseResponse> depositFunds(@Valid @RequestBody DepositAmountRequest depositAmountRequest, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new BaseResponse(400, "Missing Fields", errorMessage.toString().trim()), HttpStatus.CREATED);
        }
        return accountServiceImpl.depositFunds(depositAmountRequest);
    }

    @PostMapping("/accounts/withdraw")
    public ResponseEntity<BaseResponse> withdrawFunds(@Valid @RequestBody DepositAmountRequest depositAmountRequest, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new BaseResponse(400, "Missing Fields", errorMessage.toString().trim()), HttpStatus.CREATED);
        }
        return accountServiceImpl.withdrawFunds(depositAmountRequest);
    }
    @PostMapping("/accounts/transfer")
    public ResponseEntity<BaseResponse> transferFunds(@Valid @RequestBody TransferFundsRequest transferFundsRequest, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new BaseResponse(400, "Missing Fields", errorMessage.toString().trim()), HttpStatus.CREATED);
        }
        return accountServiceImpl.transferFunds(transferFundsRequest);
    }
    @GetMapping("/accounts/{account_id}/transactions")
    public ResponseEntity<BaseResponse> transactionHistory(@PathVariable("account_id") Long accountId){
        return accountServiceImpl.transactionHistory(accountId);
    }
}
