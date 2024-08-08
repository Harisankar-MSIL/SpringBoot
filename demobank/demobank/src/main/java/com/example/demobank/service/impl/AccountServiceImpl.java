package com.example.demobank.service.impl;

import com.example.demobank.constants.ApiConstants;
import com.example.demobank.entity.AccountsTable;
import com.example.demobank.entity.TransactionsTable;
import com.example.demobank.entity.UsersTable;
import com.example.demobank.repo.AccountsTableRepository;
import com.example.demobank.repo.TransactionsTableRepository;
import com.example.demobank.repo.UsersTableRepository;
import com.example.demobank.request.CreateAccountRequest;
import com.example.demobank.request.DepositAmountRequest;
import com.example.demobank.request.TransferFundsRequest;
import com.example.demobank.response.BaseResponse;
import com.example.demobank.response.TransActionData;
import com.example.demobank.response.TransActionHistoryResponse;
import com.example.demobank.response.WithdrawFundsResponse;
import com.example.demobank.service.AccountService;
import com.example.demobank.utility.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountsTableRepository accountsTableRepository;
    @Autowired
    UsersTableRepository usersTableRepository;
    @Autowired
    TransactionsTableRepository transactionsTableRepository;

    @Override
    public ResponseEntity<BaseResponse> createNewAccount(CreateAccountRequest createAccountRequest) {
        try {

            UsersTable usersTable = new UsersTable();
            usersTable.setEmail(createAccountRequest.getEmail());
            usersTable.setPassword(createAccountRequest.getPassword());
            usersTable.setUsername(createAccountRequest.getUserName());
            usersTable.setPhoneNumber(createAccountRequest.getPhoneNumber());

            UsersTable savedUser = usersTableRepository.save(usersTable);

            AccountsTable accountsTable = new AccountsTable();
            accountsTable.setAccountNumber(Common.generateAccountNumber());
            accountsTable.setUser(savedUser);
            accountsTable.setBalance(0.0);

            accountsTableRepository.save(accountsTable);

        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse(409, ApiConstants.OK, ApiConstants.DUPLICATE), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new BaseResponse(200, ApiConstants.OK, ApiConstants.CREATED), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BaseResponse> retrieveAccountBalance(Long accountId) {
        try {
            Optional<AccountsTable> accountsTable = accountsTableRepository.findById(accountId);
            if (accountsTable.isPresent()) {
                AccountsTable accountsTable1 = accountsTable.get();
                return new ResponseEntity<>(new BaseResponse(200, ApiConstants.OK, "Balance: " + accountsTable1.getBalance()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new BaseResponse(200, ApiConstants.OK, ApiConstants.NO_ACC_AVIL), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse(500, ApiConstants.ERROR, ApiConstants.FAILED_RETRIVE), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<BaseResponse> depositFunds(DepositAmountRequest depositAmountRequest) {
        Optional<AccountsTable> accountsTable = accountsTableRepository.findById(depositAmountRequest.getAccountId());
        if (accountsTable.isPresent()) {
            AccountsTable accountsTable1 = accountsTable.get();
            Double tot = accountsTable1.getBalance() + depositAmountRequest.getAmount();
            TransactionsTable transactionsTable = new TransactionsTable();
            transactionsTable.setAccount(accountsTable1);
            transactionsTable.setTransactionDate(new Date());
            transactionsTable.setAmount(depositAmountRequest.getAmount());
            transactionsTable.setTransactionType("Deposit");
            transactionsTableRepository.save(transactionsTable);
            accountsTable1.setBalance(tot);
            accountsTableRepository.save(accountsTable1);
            return new ResponseEntity<>(new BaseResponse(200, ApiConstants.OK, "Deposit amount is: " + depositAmountRequest.getAmount()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new BaseResponse(500, ApiConstants.ERROR, ApiConstants.FAILED_RETRIVE), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<BaseResponse> withdrawFunds(DepositAmountRequest depositAmountRequest) {
        Optional<AccountsTable> accountsTable = accountsTableRepository.findById(depositAmountRequest.getAccountId());
        if (accountsTable.isPresent()) {
            AccountsTable accountsTable1 = accountsTable.get();
            boolean checkIsWithDrawable = accountsTable1.getBalance() > depositAmountRequest.getAmount() && accountsTable1.getBalance() > 0.0;
            if (checkIsWithDrawable) {
                Double newBalance = accountsTable1.getBalance() - depositAmountRequest.getAmount();
                accountsTable1.setBalance(newBalance);
                accountsTableRepository.save(accountsTable1);

                TransactionsTable transactionsTable = new TransactionsTable();
                transactionsTable.setAccount(accountsTable1);
                transactionsTable.setTransactionDate(new Date());
                transactionsTable.setAmount(depositAmountRequest.getAmount());
                transactionsTable.setTransactionType("Withdrawal");
                transactionsTableRepository.save(transactionsTable);

                WithdrawFundsResponse withdrawFundsResponse = new WithdrawFundsResponse();
                withdrawFundsResponse.setNewBalance(newBalance);
                withdrawFundsResponse.setWithDrawAmount(depositAmountRequest.getAmount());
                return new ResponseEntity<>(new BaseResponse(200, ApiConstants.OK, withdrawFundsResponse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new BaseResponse(200, ApiConstants.OK, ApiConstants.INSUFFICENT), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new BaseResponse(500, ApiConstants.ERROR, ApiConstants.FAILED_TO_WITHDRAW_AMOUNT), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<BaseResponse> transferFunds(TransferFundsRequest transferFundsRequest) {
        Optional<AccountsTable> sourceAccountTable = accountsTableRepository.findById(transferFundsRequest.getSourceAccountId());
        Optional<AccountsTable> destinationAccountTable = accountsTableRepository.findById(transferFundsRequest.getDestinationAccountId());
        if (sourceAccountTable.isPresent() && destinationAccountTable.isPresent()) {
            AccountsTable accountsTable1 = sourceAccountTable.get();
            boolean checkIsWithDrawable = accountsTable1.getBalance() > transferFundsRequest.getAmount() && accountsTable1.getBalance() > 0.0;
            if (checkIsWithDrawable) {
                double newBalance = accountsTable1.getBalance() - transferFundsRequest.getAmount();
                accountsTable1.setBalance(newBalance);
                accountsTableRepository.save(accountsTable1);
                AccountsTable accountsTable2 = destinationAccountTable.get();
                newBalance = accountsTable2.getBalance() + transferFundsRequest.getAmount();
                accountsTable2.setBalance(newBalance);
                accountsTableRepository.save(accountsTable2);

                TransactionsTable transactionsTable = new TransactionsTable();
                transactionsTable.setAccount(accountsTable1);
                transactionsTable.setTransactionDate(new Date());
                transactionsTable.setAmount(transferFundsRequest.getAmount());
                transactionsTable.setTransactionType("Fund transfer to " + transferFundsRequest.getDestinationAccountId());
                transactionsTableRepository.save(transactionsTable);

                return new ResponseEntity<>(new BaseResponse(200, ApiConstants.OK, ApiConstants.TRANSFER_SUCCESSFULL), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new BaseResponse(200, ApiConstants.OK, ApiConstants.INSUFFICENT), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new BaseResponse(500, ApiConstants.ERROR, ApiConstants.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<BaseResponse> transactionHistory(Long accountId) {
        List<TransactionsTable> transactionsTableList = transactionsTableRepository.getByAccountId(accountId);
        TransActionHistoryResponse transActionHistoryResponse = new TransActionHistoryResponse();
        if (!transactionsTableList.isEmpty()) {
            List<TransActionData> transActionData = new ArrayList<>();
            for (TransactionsTable table : transactionsTableList) {
                TransActionData transActionData1 = new TransActionData();
                transActionData1.setAmount(table.getAmount());
                transActionData1.setTransactionDate(table.getTransactionDate());
                transActionData1.setTransactionType(table.getTransactionType());
                transActionData.add(transActionData1);
            }
            transActionHistoryResponse.setTransactionHistory(transActionData);
        }
        return new ResponseEntity<>(new BaseResponse(200, ApiConstants.OK, transActionHistoryResponse), HttpStatus.OK);

    }
}
