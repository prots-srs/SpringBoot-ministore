package com.protsdev.ministore.models;

import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer sort;

    private Boolean active;

    private String picture;

    private String productName;

    private Float productPrice;

    private String productTypeCode;

    public boolean isNew() {
        return this.id == null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String name) {
        this.productName = name;
    }

    public Float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Float price) {
        this.productPrice = price;
    }

    public String getProductTypeCode() {
        return productTypeCode;
    }

    public void setProductTypeCode(String productTypeCode) {
        this.productTypeCode = productTypeCode;
    }

    public static ArrayList<ProductType> getProductTypes() {
        ArrayList<ProductType> types = new ArrayList<>();

        types.add(new ProductType("Mobile", "mobile"));
        types.add(new ProductType("Watch", "watch"));

        return types;
    }
}
