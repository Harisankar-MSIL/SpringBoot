package com.example.demoproducts.service;

import com.example.demoproducts.request.ProductRequest;
import com.example.demoproducts.request.QueryRequest;
import com.example.demoproducts.response.BaseResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ProductService {
    ResponseEntity<BaseResponse> saveProductsToDatabase();
    ResponseEntity<BaseResponse> getAllProducts();
    ResponseEntity<BaseResponse> searchProducts(QueryRequest queryRequest);
    ResponseEntity<BaseResponse> searchProductsByCategory(QueryRequest queryRequest);
    ResponseEntity<BaseResponse> deleteProductsByCategory(QueryRequest queryRequest);
    ResponseEntity<BaseResponse> updateProduct(Map<String, Object> requestBody);
}
