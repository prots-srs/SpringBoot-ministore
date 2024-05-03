package com.protsprog.ministore.models;

import java.util.TreeSet;

import org.springframework.lang.NonNull;

public record AdminMenuItem(
        @NonNull Integer sort,
        @NonNull String label,
        @NonNull String link,
        @NonNull String icon,
        @NonNull Boolean active,
        TreeSet<AdminMenuItem> children,
        @NonNull Boolean activeChild) implements Comparable<AdminMenuItem> {

    @Override
    public int compareTo(AdminMenuItem o) {
        return this.sort.compareTo(o.sort);
    }

}
