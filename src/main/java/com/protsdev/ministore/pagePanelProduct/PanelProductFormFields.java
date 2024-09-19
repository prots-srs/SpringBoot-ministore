package com.protsdev.ministore.pagePanelProduct;

import org.springframework.web.multipart.MultipartFile;

import com.protsdev.ministore.enums.ProductTypes;
import com.protsdev.ministore.forms.PanelFormFields;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PanelProductFormFields(
        @NotNull @Positive @Max(999) Integer sort,
        Boolean active,
        @NotBlank @Size(min = 5, max = 150) String name,
        @NotNull @Digits(fraction = 2, integer = 6) @Positive @Max(1000000) Float price,
        Long fileSaved,
        MultipartFile file,
        @NotNull ProductTypes type) implements PanelFormFields<ProductEntity> {

    public ProductEntity getAsTagretEntity() {

        ProductEntity entity = new ProductEntity();
        entity.setSort(this.sort() != null ? this.sort() : 10);
        entity.setActive(this.active() != null ? this.active() : false);
        entity.setName(this.name() != null ? this.name() : "");
        entity.setPrice(this.price() != null ? this.price() : 0.0f);
        entity.setType(this.type());

        return entity;
    }
}