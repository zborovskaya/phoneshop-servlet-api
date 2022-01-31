package com.es.phoneshop.model.product.exception;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(){}

    public ProductNotFoundException(String message){super(message);}
}
