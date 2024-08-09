package com.example.demobank.utility;

import java.security.SecureRandom;

public class Common {
    private Common(){

    }
    private static final SecureRandom random = new SecureRandom();

    public static String generateAccountNumber() {
        StringBuilder accountNumber = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            int digit = random.nextInt(10);
            accountNumber.append(digit);
        }
        return accountNumber.toString();
    }

}
