package com.protsdev.ministore.dto;

import org.springframework.lang.NonNull;

public record PublicMenuItem(
        @NonNull Integer sort,
        @NonNull String label,
        @NonNull String link,
        @NonNull Boolean active) implements Comparable<PublicMenuItem> {

    @Override
    public int compareTo(PublicMenuItem o) {
        return this.sort.compareTo(o.sort);
    }

}
