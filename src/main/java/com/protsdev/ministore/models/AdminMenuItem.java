package com.protsdev.ministore.models;

import java.util.TreeSet;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.NotBlank;

public record AdminMenuItem(
        @NonNull Integer sort,
        @NotBlank String label,
        @NotBlank String link,
        @NotBlank String icon,
        @NonNull Boolean active,
        TreeSet<AdminMenuItem> children,
        @NonNull Boolean activeChild) implements Comparable<AdminMenuItem> {

    @Override
    public int compareTo(AdminMenuItem o) {
        return this.sort.compareTo(o.sort);
    }

}
