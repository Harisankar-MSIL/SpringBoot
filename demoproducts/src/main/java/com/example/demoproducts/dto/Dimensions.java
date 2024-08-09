package com.example.demoproducts.dto;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Dimensions {
    public Double width;
    public Double height;
    public Double depth;
}