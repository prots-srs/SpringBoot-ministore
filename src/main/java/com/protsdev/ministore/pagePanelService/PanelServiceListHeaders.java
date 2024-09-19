package com.protsdev.ministore.pagePanelService;

import java.util.Map;

import com.protsdev.ministore.enums.IconConfigurator;

public record PanelServiceListHeaders(
        Integer sort,
        Boolean active,
        String title,
        String description,
        IconConfigurator iconClass,
        Map<String, String> actions) {

}
