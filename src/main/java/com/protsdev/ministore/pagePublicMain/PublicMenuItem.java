package com.protsdev.ministore.pagePublicMain;

public record PublicMenuItem(
        Integer sort,
        String label,
        String link,
        Boolean active) implements Comparable<PublicMenuItem> {

    @Override
    public int compareTo(PublicMenuItem o) {
        return this.sort.compareTo(o.sort);
    }

}
