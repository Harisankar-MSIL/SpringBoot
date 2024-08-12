package com.example.demoproducts.service;

import com.example.demoproducts.request.SchemesRequest;
import com.example.demoproducts.response.BaseResponse;
import com.example.demoproducts.response.SchemesBaseResponse;
import org.springframework.http.ResponseEntity;


public interface SchemeService {
    SchemesBaseResponse getSchemesById(SchemesRequest schemesRequest);
    ResponseEntity<BaseResponse> saveAllSchemes();
}
