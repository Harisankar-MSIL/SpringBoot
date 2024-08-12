package com.example.demoproducts.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SchemeResponse {
    private String fundHouse;
    private int schemeCode;
    private String schemeName;
    private List<List<String>> data;
}
