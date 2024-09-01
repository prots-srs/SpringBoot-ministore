package com.protsdev.ministore.models;

import java.util.ArrayList;

import com.protsdev.ministore.domen.ProductTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "goods")
@Getter
@Setter
public class ProductEntity extends BaseEntity {

    private Integer sort;

    private Boolean active;

    private String picture;

    private String name;

    private Float priceValue;

    private ProductTypes type;
}
