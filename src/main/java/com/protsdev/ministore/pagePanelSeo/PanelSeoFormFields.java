package com.protsdev.ministore.pagePanelSeo;

import com.protsdev.ministore.forms.PanelFormFields;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PanelSeoFormFields(
        @NotBlank @Size(max = 50) String path,
        @NotBlank @Size(min = 5, max = 50) String title,
        @Size(max = 75) String keywords,
        @Size(max = 255) String description) implements PanelFormFields<SeoEntity> {

    @Override
    public SeoEntity getAsTagretEntity() {
        SeoEntity entity = new SeoEntity();
        entity.setPath(this.path() != null ? this.path() : "");
        entity.setTitle(this.title() != null ? this.title() : "");
        entity.setKeywords(this.keywords() != null ? this.keywords() : "");
        entity.setDescription(this.description() != null ? this.description() : "");
        return entity;
    }
}
