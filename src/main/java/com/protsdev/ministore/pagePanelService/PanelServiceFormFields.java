package com.protsdev.ministore.pagePanelService;

import com.protsdev.ministore.enums.IconConfigurator;
import com.protsdev.ministore.forms.PanelFormFields;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PanelServiceFormFields(
        @NotNull @Positive @Max(999) Integer sort,
        Boolean active,
        @NotBlank @Size(min = 5, max = 150) String title,
        @NotBlank @Size(max = 255) String description,
        @NotNull IconConfigurator iconClass) implements PanelFormFields<ServiceEntity> {

    public ServiceEntity getAsTagretEntity() {
        ServiceEntity entity = new ServiceEntity();
        entity.setSort(this.sort() != null ? this.sort() : 10);
        entity.setActive(this.active() != null ? this.active() : false);
        entity.setTitle(this.title() != null ? this.title() : "");
        entity.setDescription(this.description() != null ? this.description() : "");
        entity.setIconClass(this.iconClass());
        return entity;
    }
}
