package com.example.demoproducts.controller;

import com.example.demoproducts.dto.ProductEntity;
import com.example.demoproducts.request.ProductRequest;
import com.example.demoproducts.request.QueryRequest;
import com.example.demoproducts.response.BaseResponse;
import com.example.demoproducts.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    ProductService productService;
    @GetMapping("/save-products") //need to call using webclient
    public ResponseEntity<BaseResponse> addEmployee() {
        return productService.saveToDb();
    }
    @GetMapping("/get-products")
    public ResponseEntity<BaseResponse> getAllProducts() {
        return productService.getAllProducts();
    }
    @PostMapping("/search-products")
    public ResponseEntity<BaseResponse> searchProducts(@RequestBody QueryRequest query) {
        return productService.searchProduct(query);
    }
    @PostMapping("/search-by-category")
    public ResponseEntity<BaseResponse> searchByCategory(@RequestBody QueryRequest query) {
        return productService.searchByCategory(query);
    }
    @PostMapping("/delete-by-category")
    public ResponseEntity<BaseResponse> deleteByCategory(@RequestBody QueryRequest query) {
        return productService.deleteByCategory(query);
    }
 @PostMapping("/update-product-by-id")
    public ResponseEntity<BaseResponse> updateProductById(@RequestBody ProductRequest productRequest) {
        return productService.updateProductById(productRequest);
    }

}
