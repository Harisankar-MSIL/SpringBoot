package com.example.demoproducts.controller;

import com.example.demoproducts.constants.ApiUrls;
import com.example.demoproducts.request.ProductRequest;
import com.example.demoproducts.request.QueryRequest;
import com.example.demoproducts.response.BaseResponse;
import com.example.demoproducts.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping(ApiUrls.SAVE_PRODUCTS)
    public ResponseEntity<BaseResponse> saveProductsToDatabase() {
        return productService.saveProductsToDatabase();
    }

    @GetMapping(ApiUrls.GET_PRODUCTS)
    public ResponseEntity<BaseResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping(ApiUrls.SEARCH_PRODUCTS)
    public ResponseEntity<BaseResponse> searchProducts(@RequestBody QueryRequest query) {
        return productService.searchProducts(query);
    }

    @PostMapping(ApiUrls.SEARCH_PRODUCTS_BY_CATEGORY)
    public ResponseEntity<BaseResponse> searchProductsByCategory(@RequestBody QueryRequest query) {
        return productService.searchProductsByCategory(query);
    }

    @PostMapping(ApiUrls.DELETE_BY_CATEGORY)
    public ResponseEntity<BaseResponse> deleteProductsByCategory(@RequestBody QueryRequest query) {
        return productService.deleteProductsByCategory(query);
    }

    @PostMapping(ApiUrls.UPDATE_PRODUCT)
    public ResponseEntity<BaseResponse> updateProduct(@RequestBody Map<String, Object> requestBody) {
        return productService.updateProduct(requestBody);
    }

}
