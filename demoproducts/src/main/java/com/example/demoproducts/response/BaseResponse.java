package com.example.demoproducts.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseResponse <T> implements Serializable {
    int statusCode;
    String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data;
    public BaseResponse( int statusCode,String status,T data ){
        this.status= status;
        this.statusCode=statusCode;
        this.data=data;
    }
}