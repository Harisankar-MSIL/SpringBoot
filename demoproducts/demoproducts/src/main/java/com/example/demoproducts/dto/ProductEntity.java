package com.example.demoproducts.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "msiproductdetails")
public class ProductEntity {
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    private String title;
    private String description;
    private String category;
    private double price;
    private double discountPercentage;
    private double rating;
    private int stock;
    @ElementCollection
    private List<String> tags;
    private String brand;
    private String sku;
    private int weight;
    @Embedded
    private Dimensions dimensions;
    private String warrantyInformation;
    private String shippingInformation;
    private String availabilityStatus;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
    private List<Review> reviews;
    private String returnPolicy;
    private int minimumOrderQuantity;
    @Embedded
    private Meta meta;
    @ElementCollection
    private List<String> images;
    private String thumbnail;
}
