package com.example.demoproducts.service;

import com.example.demoproducts.request.ProductRequest;
import com.example.demoproducts.request.QueryRequest;
import com.example.demoproducts.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<BaseResponse> saveToDb();
    ResponseEntity<BaseResponse> getAllProducts();
    ResponseEntity<BaseResponse> searchProduct(QueryRequest queryRequest);
    ResponseEntity<BaseResponse> searchByCategory(QueryRequest queryRequest);
    ResponseEntity<BaseResponse> deleteByCategory(QueryRequest queryRequest);
    ResponseEntity<BaseResponse> updateProductById(ProductRequest productRequest);
}
