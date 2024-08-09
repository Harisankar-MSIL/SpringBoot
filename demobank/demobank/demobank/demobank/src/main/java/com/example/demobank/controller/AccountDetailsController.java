package com.example.demobank.controller;

import com.example.demobank.constants.ApiConstants;
import com.example.demobank.constants.ApiUrls;
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

    @PostMapping(ApiUrls.ACC_CREATE)
    public ResponseEntity<BaseResponse> createNewAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest, BindingResult result) {

        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new BaseResponse(400, ApiConstants.MISSING_FIELDS, errorMessage.toString().trim()), HttpStatus.CREATED);
        }
        return accountServiceImpl.createNewAccount(createAccountRequest);
    }

    @GetMapping(ApiUrls.CHECK_BALANCE)
    public ResponseEntity<BaseResponse> retrieveAccountBalance(@PathVariable("account_id") Long accountId) {
        return accountServiceImpl.retrieveAccountBalance(accountId);
    }
    @PostMapping(ApiUrls.DEPOSIT_FUNDS)
    public ResponseEntity<BaseResponse> depositFunds(@Valid @RequestBody DepositAmountRequest depositAmountRequest, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new BaseResponse(400, ApiConstants.MISSING_FIELDS, errorMessage.toString().trim()), HttpStatus.CREATED);
        }
        return accountServiceImpl.depositFunds(depositAmountRequest);
    }

    @PostMapping(ApiUrls.WITHDRAW_FUNDS)
    public ResponseEntity<BaseResponse> withdrawFunds(@Valid @RequestBody DepositAmountRequest depositAmountRequest, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new BaseResponse(400, ApiConstants.MISSING_FIELDS, errorMessage.toString().trim()), HttpStatus.CREATED);
        }
        return accountServiceImpl.withdrawFunds(depositAmountRequest);
    }
    @PostMapping(ApiUrls.TRANSFER_FUNDS)
    public ResponseEntity<BaseResponse> transferFunds(@Valid @RequestBody TransferFundsRequest transferFundsRequest, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new BaseResponse(400, ApiConstants.MISSING_FIELDS, errorMessage.toString().trim()), HttpStatus.CREATED);
        }
        return accountServiceImpl.transferFunds(transferFundsRequest);
    }
    @GetMapping(ApiUrls.ACC_TRANSACTIONS)
    public ResponseEntity<BaseResponse> getTransactionHistory(@PathVariable("account_id") Long accountId){
        return accountServiceImpl.getTransactionHistory(accountId);
    }
}
