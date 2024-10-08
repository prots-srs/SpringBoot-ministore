package com.protsdev.ministore.enums;

import lombok.Getter;

@Getter
public enum MenuConfigurator {
    PANEL_DASHBOARD("/panel", "bx-home-circle"),
    PANEL_CONTENT("", "bx-layout"),
    PANEL_FILES("/panel/files", ""),
    PANEL_SEO("/panel/seo", ""),
    PANEL_SERVICE("/panel/service", ""),
    PANEL_PRODUCT("/panel/product", ""),
    PANEL_BANNER("/panel/banner", ""),
    MP_HOME("#billboard", ""),
    MP_SALE("#yearly-sale", ""),
    MP_BLOG("#latest-blog", ""),
    MP_SERVICES("#company-services", ""),
    MP_MOBILES("#mobile-products", ""),
    MP_WATCHES("#smart-watches", "");

    private final String link;
    private final String ico;

    MenuConfigurator(String link, String ico) {
        this.link = link;
        this.ico = ico;
    }
}