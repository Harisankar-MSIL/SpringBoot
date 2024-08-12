package com.example.demoproducts.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class FundsData {
    @JsonProperty("date")
    private String date;
    @JsonProperty("nav")
    private String nav;
}