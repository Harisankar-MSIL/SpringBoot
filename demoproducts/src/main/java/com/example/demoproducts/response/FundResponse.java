package com.example.demoproducts.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FundResponse {

    @JsonProperty("meta")
    private Meta meta;

    @JsonProperty("data")
    private List<Data> data;

    @JsonProperty("status")
    private String status;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        @JsonProperty("date")
        private String date;

        @JsonProperty("nav")
        private String nav;
        public LocalDate getDateAsLocalDate() {
            return LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
    }
}
