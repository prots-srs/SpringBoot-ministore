package com.protsprog.ministore.models;

import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Services
 */
@Entity
public class ServiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer sort;

    private Boolean active;

    private String title;

    private String description;

    private String iconClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isNew() {
        return this.id == null;
    }

    public Integer getSort() {
        return this.sort;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public static ArrayList<IconClass> getListIconClasses() {
        ArrayList<IconClass> iconClasses = new ArrayList<>();

        iconClasses.add(new IconClass("Select Icon class", ""));

        iconClasses.add(new IconClass("Cart", "cart-outline"));
        iconClasses.add(new IconClass("Quality", "quality"));
        iconClasses.add(new IconClass("Price tag", "price-tag"));
        iconClasses.add(new IconClass("Shield", "shield-plus"));

        return iconClasses;
    }
}