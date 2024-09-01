package com.protsdev.ministore.pageSeo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FormSeo(
        @NotBlank @Size(max = 50) String path,
        @NotBlank @Size(min = 5, max = 50) String title,
        @NotNull @Size(max = 75) String keywords,
        @NotNull @Size(max = 255) String description) {

}
