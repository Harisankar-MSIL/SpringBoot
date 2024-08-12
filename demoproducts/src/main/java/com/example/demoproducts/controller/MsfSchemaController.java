package com.example.demoproducts.controller;

import com.example.demoproducts.exception.SchemesException;
import com.example.demoproducts.request.SchemesRequest;
import com.example.demoproducts.response.BaseResponse;
import com.example.demoproducts.response.SchemesBaseResponse;
import com.example.demoproducts.service.SchemeService;
import jakarta.validation.Valid;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MsfSchemaController {
    @Autowired
    SchemeService schemeService;

    @PostMapping("/mf/schema-Id")
    public SchemesBaseResponse getSchemaById(@Valid @RequestBody SchemesRequest schemaRequest, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            throw new SchemesException(errorMessage.toString().trim());
        }
        return schemeService.getSchemesById(schemaRequest);
    }

    @GetMapping("/mf/save-all")
    public ResponseEntity<BaseResponse> saveSchemes() {
        return schemeService.saveAllSchemes();
    }
}
