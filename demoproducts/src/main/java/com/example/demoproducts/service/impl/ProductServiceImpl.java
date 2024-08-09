package com.example.demoproducts.service.impl;

import com.example.demoproducts.constants.ApiConstants;
import com.example.demoproducts.dto.Dimensions;
import com.example.demoproducts.dto.Meta;
import com.example.demoproducts.dto.ProductEntity;
import com.example.demoproducts.dto.Review;
import com.example.demoproducts.repo.ProductRepository;
import com.example.demoproducts.request.QueryRequest;
import com.example.demoproducts.response.BaseResponse;
import com.example.demoproducts.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ResponseEntity<BaseResponse> saveProductsToDatabase() {
        String baseUrl = "https://dummyjson.com/products?limit=3000";
        WebClient webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String productData = webClient.get()
                    .uri(baseUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Check if the response data is null or empty
            if (productData == null || productData.isEmpty()) {
                return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK, "No data received"), HttpStatus.NO_CONTENT);
            }

            JsonNode rootNode = objectMapper.readTree(productData);
            JsonNode productsNode = rootNode.path("products");

            List<Map<String, Object>> products = objectMapper.convertValue(productsNode, new TypeReference<List<Map<String, Object>>>() {});


            for (Map<String, Object> productMap : products) {
                ProductEntity productEntity = objectMapper.convertValue(productMap, ProductEntity.class);


                handleNestedEntities(productEntity, productMap);

                repository.saveAndFlush(productEntity);
            }

            return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK, "Success"), HttpStatus.CREATED);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new BaseResponse(ApiConstants.ERROR_CODE, ApiConstants.ERROR_MESSAGE, "Failed to parse data"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new BaseResponse(ApiConstants.ERROR_CODE, ApiConstants.ERROR_MESSAGE, "An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void handleNestedEntities(ProductEntity productEntity, Map<String, Object> productMap) {
        ObjectMapper objectMapper = new ObjectMapper();

        if (productMap.containsKey("dimensions")) {
            Map<String, Object> dimensionsMap = (Map<String, Object>) productMap.get("dimensions");
            Dimensions dimensions = objectMapper.convertValue(dimensionsMap, Dimensions.class);
            productEntity.setDimensions(dimensions);
        }

        if (productMap.containsKey("reviews")) {
            List<Map<String, Object>> reviewsList = (List<Map<String, Object>>) productMap.get("reviews");
            List<Review> reviews = reviewsList.stream()
                    .map(reviewMap -> objectMapper.convertValue(reviewMap, Review.class))
                    .collect(Collectors.toList());
            productEntity.setReviews(reviews);
        }

        if (productMap.containsKey("meta")) {
            Map<String, Object> metaMap = (Map<String, Object>) productMap.get("meta");
            Meta meta = objectMapper.convertValue(metaMap, Meta.class);
            productEntity.setMeta(meta);
        }
    }
    @Transactional
    public void saveProductsInBatch(List<ProductEntity> products) {
        int batchSize = 50; // Configure according to your needs
        for (int i = 0; i < products.size(); i++) {
            repository.save(products.get(i));
            if (i > 0 && i % batchSize == 0) {
                // Flush a batch of inserts and release memory
                entityManager.flush();
                entityManager.clear(); // Clear the persistence context
            }
        }
        // Save remaining records
        entityManager.flush();
        entityManager.clear();
    }
    @Override
    public ResponseEntity<BaseResponse> getAllProducts() {
        List<ProductEntity> products = repository.findAll();
        return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK, products.isEmpty() ? ApiConstants.TABLE_EMPTY : products), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<BaseResponse> searchProducts(QueryRequest queryRequest) {
        List<ProductEntity> list = repository.searchByProduct(queryRequest.getQuery());
        return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK, list), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResponse> searchProductsByCategory(QueryRequest queryRequest) {
        List<ProductEntity> list = repository.searchByCategory(queryRequest.getQuery());
        return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK, list), HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<BaseResponse> deleteProductsByCategory(QueryRequest queryRequest) {
        List<ProductEntity> existingProductEntity = repository.searchByCategory(queryRequest.getQuery());

        List<Integer> idList = new ArrayList<>();
        for (ProductEntity productEntity : existingProductEntity) {
            Integer productId = productEntity.getId();
            idList.add(productId);
        }
        if (!idList.isEmpty()) {
            for (Integer integer : idList) {
                repository.deleteById(integer);
            }
            return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK, "Success"), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK, "No Matches"), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BaseResponse> updateProduct(Map<String, Object> requestBody) {
        Optional<ProductEntity> productEntityOpt = repository.findById((Integer) requestBody.get("id"));

        if (productEntityOpt.isPresent()) {
            ProductEntity productEntity = productEntityOpt.get();

            for (Map.Entry<String, Object> entry : requestBody.entrySet()) {
                String fieldName = entry.getKey();
                Object value = entry.getValue();

                try {
                    Field field = ProductEntity.class.getDeclaredField(fieldName);

                    if (field.getType() == Dimensions.class && value instanceof Map) {
                        Dimensions dimensions = (Dimensions) field.get(productEntity);
                        Map<String, Object> dimensionMap = (Map<String, Object>) value;
                        if (dimensions == null) {
                            dimensions = new Dimensions();
                            field.set(productEntity, dimensions);
                        }
                        updateDimensions(dimensions, dimensionMap);
                    } else if (field.getType() == List.class && value instanceof List) {
                        field.set(productEntity, value);
                    } else {
                        field.set(productEntity, value);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            repository.save(productEntity);
            return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.UPDATE_MESSAGE, productEntity), HttpStatus.OK);
        }
        return new ResponseEntity<>(new BaseResponse(ApiConstants.NOT_FOUND, ApiConstants.NOT_FOUND_MESSAGE, null), HttpStatus.NOT_FOUND);
    }

    private void updateDimensions(Dimensions dimensions, Map<String, Object> dimensionMap) {
        if (dimensionMap.containsKey("width")) {
            dimensions.setWidth((Double) dimensionMap.get("width"));
        }
        if (dimensionMap.containsKey("height")) {
            dimensions.setHeight((Double) dimensionMap.get("height"));
        }
        if (dimensionMap.containsKey("depth")) {
            dimensions.setDepth((Double) dimensionMap.get("depth"));
        }
    }
}
