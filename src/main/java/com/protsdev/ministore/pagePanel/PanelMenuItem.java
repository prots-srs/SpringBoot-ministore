package com.protsdev.ministore.pagePanel;

import java.util.TreeSet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PanelMenuItem(
        @NotNull Integer sort,
        @NotBlank String label,
        @NotBlank String link,
        @NotNull String icon,
        @NotNull Boolean active,
        TreeSet<PanelMenuItem> children,
        @NotNull Boolean activeChild) implements Comparable<PanelMenuItem> {

    @Override
    public int compareTo(PanelMenuItem o) {
        return this.sort.compareTo(o.sort);
    }

}
