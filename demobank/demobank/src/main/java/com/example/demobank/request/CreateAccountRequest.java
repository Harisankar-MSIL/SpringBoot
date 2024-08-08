package com.example.demobank.request;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
public class CreateAccountRequest {

    @NotEmpty(message = "Username is required")
    private String userName;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Phone number is required")
    private String phoneNumber;
}
