package com.protsdev.ministore.pagePanelBanner;

import java.util.Map;

public record PanelBannerListHeaders(
        Integer sort,
        Boolean active,
        String title,
        String link,
        String picture,
        Map<String, String> actions) {

}
