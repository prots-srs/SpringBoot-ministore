package com.protsdev.ministore.enums;

import lombok.Getter;

@Getter
public enum StorageModules {
    PRODUCT("product"),
    BANNER("banner"),
    BLOG("blog");

    private final String dir;

    StorageModules(String type) {
        this.dir = type;
    }
}
