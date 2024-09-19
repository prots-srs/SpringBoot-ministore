package com.protsdev.ministore.pagePanelProduct;

import java.util.Map;

import com.protsdev.ministore.enums.ProductTypes;

public record PanelProductListHeaders(
        Integer sort,
        Boolean active,
        String name,
        Float price,
        String picture,
        ProductTypes type,
        Map<String, String> actions) {

}
