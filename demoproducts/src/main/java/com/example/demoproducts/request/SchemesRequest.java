package com.example.demoproducts.request;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchemesRequest {
    @Valid
    private SchemsByIdRequest request;

}
