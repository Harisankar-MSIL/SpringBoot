package com.example.demoproducts.service;

import com.example.demoproducts.constants.ApiConstants;
import com.example.demoproducts.exception.SchemesException;
import com.example.demoproducts.request.SchemesRequest;
import com.example.demoproducts.response.BaseResponse;
import com.example.demoproducts.response.FundResponse;
import com.example.demoproducts.response.SchemeResponse;
import com.example.demoproducts.response.SchemesBaseResponse;
import com.example.demoproducts.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SchemeServiceImpl implements SchemeService {
    private static final Logger logger = LoggerFactory.getLogger(SchemeServiceImpl.class);

    String filePath = "D:/SchemeId/schemeId.json";

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SchemesBaseResponse getSchemesById(SchemesRequest schemesRequest) {
        if (schemesRequest.getRequest().getFilter().contains("M") || schemesRequest.getRequest().getFilter().contains("Y") ||schemesRequest.getRequest().getFilter().contains("W")) {
            FundResponse fundsResponse;
            try {
                fundsResponse = objectMapper.readValue(new File(filePath), FundResponse.class);
                SchemesBaseResponse schemesBaseResponse = new SchemesBaseResponse();
                SchemeResponse schemeResponse = new SchemeResponse();
                schemeResponse.setSchemeCode(fundsResponse.getMeta().getSchemeCode());
                schemeResponse.setSchemeName(fundsResponse.getMeta().getSchemeName());
                schemeResponse.setFundHouse(fundsResponse.getMeta().getFundHouse());

                List<List<String>> dateList = new ArrayList<>();

                List<String> dateValues = new ArrayList<>();
                List<String> navValues = new ArrayList<>();

                List<FundResponse.Data> dataList = filterData(fundsResponse.getData(), schemesRequest.getRequest().getFilter());

                for(FundResponse.Data data : dataList){
                    dateValues.add(data.getDate());
                    navValues.add(data.getNav());
                }

                dateList.add(dateValues);
                dateList.add(navValues);
                schemeResponse.setData(dateList);

                schemesBaseResponse.setResponse(schemeResponse);

                return schemesBaseResponse;
            } catch (IOException e) {
                logger.error("Error writing to file", e);
                throw new SchemesException("file parsing error");
            }
        }
        throw new SchemesException("check filter values");

    }

    public static List<FundResponse.Data> filterData(List<FundResponse.Data> data, String value) {
        int val= StringUtils.extractNumber(value);
        boolean isYear = value.toLowerCase().contains("y");
        boolean isMonth = value.toLowerCase().contains("m");
        boolean isWeek = value.toLowerCase().contains("w");
        LocalDate now = LocalDate.now();
        LocalDate startDate;
        if (isYear) {
            startDate = now.minus(Period.ofYears(val));
        } else if (isMonth) {
            startDate = now.minus(Period.ofMonths(val));
        } else if (isWeek) {
            startDate = now.minus(val, ChronoUnit.WEEKS);
        } else {
            throw new IllegalStateException("Unexpected time unit.");
        }
        return data.stream()
                .filter(entry -> entry.getDateAsLocalDate().isAfter(startDate))
                .collect(Collectors.toList());
    }

    public static List<FundResponse.Data> filterDataByMonths(List<FundResponse.Data> data, int months) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minus(Period.ofMonths(months));
        return data.stream()
                .filter(entry -> entry.getDateAsLocalDate().isAfter(startDate))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<BaseResponse> saveAllSchemes() {
        String baseUrl = "https://api.mfapi.in/mf/100077";
        WebClient webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();

        try {
            FundResponse schemeDatas = webClient.get()
                    .uri(baseUrl)
                    .retrieve()
                    .bodyToMono(FundResponse.class)
                    .block();
            objectMapper.writeValue(new File(filePath), schemeDatas);

        } catch (IOException e) {
            logger.error("Error writing to file", e);
            return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK, "Fail"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK, "Success"), HttpStatus.OK);
    }
}
