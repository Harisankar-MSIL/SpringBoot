package com.example.demobank.constants;

public class ApiUrls {

    private ApiUrls() {

    }
    public static final String ACC_CREATE="/accounts/create";
    public static final String CHECK_BALANCE="/accounts/{account_id}/balance";
    public static final String DEPOSIT_FUNDS="/accounts/deposit";
    public static final String WITHDRAW_FUNDS="/accounts/withdraw";
    public static final String TRANSFER_FUNDS="/accounts/transfer";
    public static final String ACC_TRANSACTIONS="/accounts/{account_id}/transactions";
}
