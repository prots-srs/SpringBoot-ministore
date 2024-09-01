package com.protsdev.ministore.dto;

import org.springframework.lang.NonNull;

public record PublicMenu(
        @NonNull Integer sort,
        @NonNull String label,
        @NonNull String link,
        @NonNull Boolean active) implements Comparable<PublicMenu> {

    @Override
    public int compareTo(PublicMenu o) {
        return this.sort.compareTo(o.sort);
    }

}
