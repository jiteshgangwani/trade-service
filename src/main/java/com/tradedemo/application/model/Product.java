package com.tradedemo.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

    private String productId;

    public Product() {
    }

    private String productName;

    @JsonProperty("product_id")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("product_name")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }



}
