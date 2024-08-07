package com.example.demoproducts.service.impl;

import com.example.demoproducts.constants.ApiConstants;
import com.example.demoproducts.dto.Dimensions;
import com.example.demoproducts.dto.Meta;
import com.example.demoproducts.dto.ProductEntity;
import com.example.demoproducts.dto.Review;
import com.example.demoproducts.repo.ProductRepository;
import com.example.demoproducts.request.ProductRequest;
import com.example.demoproducts.request.QueryRequest;
import com.example.demoproducts.response.BaseResponse;
import com.example.demoproducts.service.ProductService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository repository;

    @Override
    public ResponseEntity<BaseResponse> saveToDb() {
        String baseUrl ="https://dummyjson.com/products";
      WebClient.Builder builder =WebClient.builder();
      String catFact = builder.build()
              .get()
              .uri(baseUrl)
              .retrieve()
              .bodyToMono(String.class)
              .block();
        ProductEntity productEntity = null;
        try {
            // Create JsonParser instance
            JsonParser jsonParser = new JsonParser();
            // Parse the JSON string
            JsonElement jsonElement = jsonParser.parse(catFact);

            // Convert to JsonObject if necessary
            JsonArray jsonArray = jsonElement.getAsJsonObject().get("products").getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();

                productEntity = new ProductEntity();
                productEntity.setId(jsonObject.get("id").getAsInt());
                productEntity.setTitle(jsonObject.get("title").getAsString());
                productEntity.setDescription(jsonObject.get("description").getAsString());
                productEntity.setCategory(jsonObject.get("category").getAsString());
                productEntity.setPrice(jsonObject.get("price").getAsDouble());
                productEntity.setDiscountPercentage(jsonObject.get("discountPercentage").getAsDouble());
                productEntity.setRating(jsonObject.get("rating").getAsDouble());
                productEntity.setStock(jsonObject.get("stock").getAsInt());

                // Handle tags
                List<String> tags = new ArrayList<>();
                JsonArray tagsArray = jsonObject.getAsJsonArray("tags");
                for (JsonElement tagElement : tagsArray) {
                    tags.add(tagElement.getAsString());
                }
                productEntity.setTags(tags);

                productEntity.setBrand(jsonObject.get("brand").getAsString());
                productEntity.setSku(jsonObject.get("sku").getAsString());
                productEntity.setWeight(jsonObject.get("weight").getAsInt());

                // Handle dimensions
                JsonObject dimensionsObject = jsonObject.getAsJsonObject("dimensions");
                Dimensions dimensions = new Dimensions();
                dimensions.setWidth(dimensionsObject.get("width").getAsDouble());
                dimensions.setHeight(dimensionsObject.get("height").getAsDouble());
                dimensions.setDepth(dimensionsObject.get("depth").getAsDouble());
                productEntity.setDimensions(dimensions);

                productEntity.setWarrantyInformation(jsonObject.get("warrantyInformation").getAsString());
                productEntity.setShippingInformation(jsonObject.get("shippingInformation").getAsString());
                productEntity.setAvailabilityStatus(jsonObject.get("availabilityStatus").getAsString());

                // Handle reviews
                List<Review> reviews = new ArrayList<>();
                JsonArray reviewsArray = jsonObject.getAsJsonArray("reviews");
                for (JsonElement reviewElement : reviewsArray) {
                    JsonObject reviewObject = reviewElement.getAsJsonObject();
                    Review review = new Review();
                    review.setRating(reviewObject.get("rating").getAsInt());
                    review.setComment(reviewObject.get("comment").getAsString());
                    review.setDate(reviewObject.get("date").getAsString());
                    review.setReviewerName(reviewObject.get("reviewerName").getAsString());
                    review.setReviewerEmail(reviewObject.get("reviewerEmail").getAsString());
                    reviews.add(review);
                }
                productEntity.setReviews(reviews);

                productEntity.setReturnPolicy(jsonObject.get("returnPolicy").getAsString());
                productEntity.setMinimumOrderQuantity(jsonObject.get("minimumOrderQuantity").getAsInt());

                // Handle meta
                JsonObject metaObject = jsonObject.getAsJsonObject("meta");
                Meta meta = new Meta();
                meta.setCreatedAt(metaObject.get("createdAt").getAsString());
                meta.setUpdatedAt(metaObject.get("updatedAt").getAsString());
                meta.setBarcode(metaObject.get("barcode").getAsString());
                meta.setQrCode(metaObject.get("qrCode").getAsString());
                productEntity.setMeta(meta);

                // Handle images
                List<String> images = new ArrayList<>();
                JsonArray imagesArray = jsonObject.getAsJsonArray("images");
                for (JsonElement imageElement : imagesArray) {
                    images.add(imageElement.getAsString());
                }
                productEntity.setImages(images);

                productEntity.setThumbnail(jsonObject.get("thumbnail").getAsString());
                repository.save(productEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
      System.out.println(catFact);
        return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK,  "Success"), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BaseResponse> getAllProducts() {
        return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK,  repository.findAll()), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BaseResponse> searchProduct(QueryRequest queryRequest) {
        List<ProductEntity> list = repository.searchByProduct(queryRequest.getQuery());
        return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK,  list), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BaseResponse> searchByCategory(QueryRequest queryRequest) {
        List<ProductEntity> list = repository.searchByCategory(queryRequest.getQuery());
        return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK,  list), HttpStatus.CREATED);
    }
    @Transactional
    @Override
    public ResponseEntity<BaseResponse> deleteByCategory(QueryRequest queryRequest) {
        List<ProductEntity> productEntityValue = repository.searchByProduct(queryRequest.getQuery());

        List<Integer> idList = new ArrayList<>();
        for (ProductEntity productEntity : productEntityValue) {
            Integer productId = productEntity.getId();
            idList.add(productId);
        }
        if(!idList.isEmpty()) {
            for (int i = 0; i < idList.size(); i++) {
                repository.deleteById(idList.get(i));
            }
            return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK,  "Success"), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.OK,  "No Matches"), HttpStatus.CREATED);
    }
    @Override
    public ResponseEntity<BaseResponse> updateProductById(ProductRequest productRequest) {
        Optional<ProductEntity> productEntityValue = repository.findById(Integer.valueOf(productRequest.getId()));
        if (productEntityValue.isPresent()) {
            ProductEntity newProductEnity = productEntityValue.get();
            newProductEnity.setTitle(productRequest.getTitle());
            repository.save(newProductEnity);
            return new ResponseEntity<>(new BaseResponse(ApiConstants.SUCCESS_CODE, ApiConstants.UPDATE_MESSAGE, newProductEnity), HttpStatus.OK);
        }
        return new ResponseEntity<>(new BaseResponse(ApiConstants.NOT_FOUND, ApiConstants.NOT_FOUND_MESSAGE, null), HttpStatus.NOT_FOUND);
    }
}
