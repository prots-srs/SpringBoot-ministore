package com.protsdev.ministore.pagePanelSeo;

import java.util.Map;

public record PanelSeoListHeaders(
        String path,
        String title,
        String keywords,
        String description,
        Map<String, String> actions) {

}
