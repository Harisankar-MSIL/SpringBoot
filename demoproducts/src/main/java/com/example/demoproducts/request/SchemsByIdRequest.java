package com.example.demoproducts.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchemsByIdRequest {
    @NotNull(message = "filter key is required")
    private String filter;

    @NotNull(message = "schemeId is required")
    private Integer schemeId;
}
