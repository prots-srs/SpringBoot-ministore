package com.protsdev.ministore.enums;

import lombok.Getter;

@Getter
public enum IconConfigurator {
    CART("cart", "cart-outline"),
    QUALITY("quality", "quality"),
    PRICE_TAG("priceTag", "price-tag"),
    SHIELD("shield", "shield-plus");

    private final String type;
    private final String ico;

    IconConfigurator(String type, String ico) {
        this.type = type;
        this.ico = ico;
    }
}