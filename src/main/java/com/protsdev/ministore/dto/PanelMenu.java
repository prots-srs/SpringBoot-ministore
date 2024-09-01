package com.protsdev.ministore.dto;

import java.util.TreeSet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PanelMenu(
        @NotNull Integer sort,
        @NotBlank String label,
        @NotBlank String link,
        @NotNull String icon,
        @NotNull Boolean active,
        TreeSet<PanelMenu> children,
        @NotNull Boolean activeChild) implements Comparable<PanelMenu> {

    @Override
    public int compareTo(PanelMenu o) {
        return this.sort.compareTo(o.sort);
    }

}
