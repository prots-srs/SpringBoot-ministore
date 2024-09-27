package com.protsdev.ministore.pagePanelBanner;

import org.springframework.web.multipart.MultipartFile;

import com.protsdev.ministore.forms.PanelFormFields;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PanelBannerFormFields(
        @NotNull @Positive @Max(999) Integer sort,
        Boolean active,
        @NotBlank @Size(min = 5, max = 150) String title,
        @Size(min = 0, max = 150) String link,
        Long fileSaved,
        MultipartFile file) implements PanelFormFields<BannerEntity> {

    public BannerEntity getAsTagretEntity() {

        BannerEntity entity = new BannerEntity();
        entity.setSort(this.sort() != null ? this.sort() : 10);
        entity.setActive(this.active() != null ? this.active() : false);
        entity.setTitle(this.title() != null ? this.title() : "");
        entity.setLink(this.link() != null ? this.link() : "");

        return entity;
    }
}
