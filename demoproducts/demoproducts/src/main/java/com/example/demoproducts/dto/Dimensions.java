package com.example.demoproducts.dto;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Dimensions {
    public double width;
    public double height;
    public double depth;
}