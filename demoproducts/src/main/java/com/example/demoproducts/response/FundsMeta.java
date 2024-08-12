package com.example.demoproducts.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class FundsMeta {
    @JsonProperty("fund_house")
    private String fundHouse;
    @JsonProperty("scheme_type")
    private String schemeType;
    @JsonProperty("scheme_category")
    private String schemeCategory;
    @JsonProperty("scheme_code")
    private int schemeCode;
    @JsonProperty("scheme_name")
    private String schemeName;
}
